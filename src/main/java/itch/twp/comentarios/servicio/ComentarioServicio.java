package itch.twp.comentarios.servicio;


import java.util.List;
import itch.twp.comentarios.dto.ComentarioDto;

public interface ComentarioServicio {
    
    ComentarioDto createComentario(ComentarioDto comentarioDto);
    
    ComentarioDto getComentarioById(Long id);
    

    List<ComentarioDto> getComentariosByIncidencia(Long incidenciaId);
    
    List<ComentarioDto> getAllComentarios();
    
    ComentarioDto updateComentario(Long id, ComentarioDto comentarioDto);

    void deleteComentario(Long id);

    itch.twp.comentarios.dto.IncidenciaDTO validarIncidencia(Integer id);
}

