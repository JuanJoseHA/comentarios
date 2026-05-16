package itch.twp.comentarios.servicio.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import itch.twp.comentarios.client.AuthClient;
import itch.twp.comentarios.client.IncidenciaClient;
import itch.twp.comentarios.dto.ComentarioDto;
import itch.twp.comentarios.dto.IncidenciaDTO;
import itch.twp.comentarios.dto.UsuarioDetalleDTO;
import itch.twp.comentarios.entity.Comentario;
import itch.twp.comentarios.exception.ResourceNotFoundException;
import itch.twp.comentarios.repositorio.ComentarioRepositorio;
import itch.twp.comentarios.servicio.ComentarioServicio;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComentarioServicioImp implements ComentarioServicio {

    private ComentarioRepositorio comentarioRepositorio;
    private IncidenciaClient incidenciaClient;
    private AuthClient authClient;

    @Override
    public ComentarioDto createComentario(ComentarioDto comentarioDto) {
        // PRIMERA VALIDACIÓN: ¿Existe la incidencia?
        try {
            incidenciaClient.obtenerPorId(comentarioDto.getIncidenciaId().intValue());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error: La incidencia " + comentarioDto.getIncidenciaId() + " no existe.");
        }

        // SEGUNDA VALIDACIÓN: ¿Existe el usuario en el servicio de Uri/Kevin?
        try {
            // Llamada al microservicio de Uri para verificar existencia
            authClient.obtenerUsuarioPorId(comentarioDto.getUsuarioId());
        } catch (Exception e) {
            // Si el usuario no existe en el Auth, lanzamos la excepción AQUÍ
            // Esto evita que el código llegue al 'save' de abajo
            throw new ResourceNotFoundException("Error: El usuario " + comentarioDto.getUsuarioId() + " no es válido.");
        }

        // Si ambas validaciones pasan, procedemos con la lógica de roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String rol = (authentication != null && !authentication.getAuthorities().isEmpty()) 
                     ? authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "") 
                     : "";

        boolean esOficial = "EMPLEADO".equals(rol) || "ADMIN".equals(rol) || "FUNCIONARIO".equals(rol);

        Comentario comentario = new Comentario();
        comentario.setIncidenciaId(comentarioDto.getIncidenciaId());
        comentario.setUsuarioId(comentarioDto.getUsuarioId());
        comentario.setMensaje(comentarioDto.getMensaje());
        comentario.setEsOficial(esOficial);
        
        if (esOficial) {
            comentario.setLeidoFuncionario(true);
            comentario.setLeidoCiudadano(false);
        } else {
            comentario.setLeidoCiudadano(true);
            comentario.setLeidoFuncionario(false);
        }

        return mapToDto(comentarioRepositorio.save(comentario));
    }

    @Override
    public ComentarioDto getComentarioById(Long id) {
        Comentario comentario = comentarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));
        
        return mapToDto(comentario);
    }

    @Override
    public List<ComentarioDto> getComentariosByIncidencia(Long incidenciaId) {
        List<Comentario> comentarios = comentarioRepositorio.findByIncidenciaIdOrderByFechaCreacionAsc(incidenciaId);
        
        return comentarios.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioDto> getAllComentarios() {
        List<Comentario> comentarios = comentarioRepositorio.findAll();
        
        return comentarios.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComentario(Long id) {
        Comentario comentario = comentarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un comentario con el ID - " + id));
        
        comentarioRepositorio.delete(comentario);
    }
    
    private ComentarioDto mapToDto(Comentario comentario) {
        return new ComentarioDto(
                comentario.getId(),
                comentario.getIncidenciaId(),
                comentario.getUsuarioId(),
                comentario.getMensaje(),
                comentario.getFechaCreacion(),
                comentario.getEsOficial(),
                comentario.getLeidoCiudadano(),
                comentario.getLeidoFuncionario()
        );
    }
    
    @Override
    public void marcarComoLeidosPorCiudadano(Long incidenciaId) {
        List<Comentario> comentarios = comentarioRepositorio.findByIncidenciaIdOrderByFechaCreacionAsc(incidenciaId);

        comentarios.stream()
                .filter(comentario -> Boolean.TRUE.equals(comentario.getEsOficial()))
                .forEach(comentario -> comentario.setLeidoCiudadano(true));

        comentarioRepositorio.saveAll(comentarios);
    }

    @Override
    public void marcarComoLeidosPorFuncionario(Long incidenciaId) {
        List<Comentario> comentarios = comentarioRepositorio.findByIncidenciaIdOrderByFechaCreacionAsc(incidenciaId);

        comentarios.stream()
                .filter(comentario -> !Boolean.TRUE.equals(comentario.getEsOficial()))
                .forEach(comentario -> comentario.setLeidoFuncionario(true));

        comentarioRepositorio.saveAll(comentarios);
    }
    
    @Override
    public ComentarioDto updateComentario(Long id, ComentarioDto comentarioDto) {
        Comentario comentario = comentarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));

        comentario.setMensaje(comentarioDto.getMensaje());

        Comentario updatedComentario = comentarioRepositorio.save(comentario);

        return mapToDto(updatedComentario);
    }

    @Override
    public itch.twp.comentarios.dto.IncidenciaDTO validarIncidencia(Integer id) {
        return incidenciaClient.obtenerPorId(id);
    }
    
    @Override
    public String probarConexiones(Integer incidenciaId, Long usuarioId) {
        StringBuilder reporte = new StringBuilder();

        // Prueba 1: Incidencias (Kevin)
        try {
            incidenciaClient.obtenerPorId(incidenciaId);
            reporte.append("✅ Conexión con Incidencias (Kevin) OK\n");
        } catch (Exception e) {
            reporte.append("❌ Error en Incidencias: ").append(e.getMessage()).append("\n");
        }

        // Prueba 2: Auth (Uri)
        try {
            authClient.obtenerUsuarioPorId(usuarioId);
            reporte.append("✅ Conexión con Auth (Uri) OK");
        } catch (Exception e) {
            reporte.append("❌ Error en Auth (Uri): ").append(e.getMessage());
        }

        return reporte.toString();
    }
}