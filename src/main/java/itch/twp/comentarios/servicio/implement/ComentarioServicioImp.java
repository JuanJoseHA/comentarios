package itch.twp.comentarios.servicio.implement;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import itch.twp.comentarios.dto.ComentarioDto;
import itch.twp.comentarios.entity.Comentario;
import itch.twp.comentarios.repositorio.ComentarioRepositorio;
import itch.twp.comentarios.servicio.ComentarioServicio;
// Asumiendo que crearás tu propia ResourceNotFoundException como en la escuela
import itch.twp.comentarios.exception.ResourceNotFoundException; 

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComentarioServicioImp implements ComentarioServicio {

    private ComentarioRepositorio comentarioRepositorio;

    @Override
    public ComentarioDto createComentario(ComentarioDto comentarioDto) {
        // Mapeo manual de DTO a Entidad (puedes abstraerlo a un ComentarioMappear después)
        Comentario comentario = new Comentario();
        comentario.setIncidenciaId(comentarioDto.getIncidenciaId());
        comentario.setUsuarioId(comentarioDto.getUsuarioId());
        comentario.setMensaje(comentarioDto.getMensaje());
        comentario.setEsOficial(comentarioDto.getEsOficial());

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
        // Utilizamos el método personalizado del repositorio
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
    
    // Método privado de apoyo para no repetir el mapeo (equivale a tu clase Mappear)
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
        // 1. Buscamos si existe
        Comentario comentario = comentarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el comentario con ID: " + id));
        
        // 2. Actualizamos solo los campos permitidos
        comentario.setMensaje(comentarioDto.getMensaje());
        
        // Validamos por si envían nulo en este campo booleano
        if(comentarioDto.getEsOficial() != null) {
            comentario.setEsOficial(comentarioDto.getEsOficial());
        }

        // 3. Guardamos los cambios
        Comentario updatedComentario = comentarioRepositorio.save(comentario);
        
        // 4. Devolvemos el DTO actualizado
        return mapToDto(updatedComentario);
    }
}