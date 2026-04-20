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
        try {
            incidenciaClient.obtenerPorId(comentarioDto.getIncidenciaId().intValue());
        } catch (Exception e) {
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        rol = rol.replace("ROLE_", "");
        boolean esOficial = "EMPLEADO".equals(rol) || "ADMIN".equals(rol) || "FUNCIONARIO".equals(rol);

        Comentario comentario = new Comentario();
        comentario.setIncidenciaId(comentarioDto.getIncidenciaId());
        comentario.setUsuarioId(comentarioDto.getUsuarioId());
        comentario.setMensaje(comentarioDto.getMensaje());
        comentario.setEsOficial(esOficial);

        Comentario savedComentario = comentarioRepositorio.save(comentario);

        return mapToDto(savedComentario);
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
                comentario.getEsOficial()
        );
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
}