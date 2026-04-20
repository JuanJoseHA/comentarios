package itch.twp.comentarios.controlador;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.twp.comentarios.dto.ComentarioDto;
import itch.twp.comentarios.dto.IncidenciaDTO;
import itch.twp.comentarios.servicio.ComentarioServicio;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comentarios")
public class ComentarioControlador {

    private ComentarioServicio comentarioServicio;

    @PostMapping
    public ResponseEntity<ComentarioDto> createComentario(@RequestBody ComentarioDto comentarioDto) {
        ComentarioDto saved = comentarioServicio.createComentario(comentarioDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/incidencia/{incidenciaId}")
    public ResponseEntity<List<ComentarioDto>> getComentarios(@PathVariable("incidenciaId") Long incidenciaId) {
        List<ComentarioDto> comentarios = comentarioServicio.getComentariosByIncidencia(incidenciaId);
        return ResponseEntity.ok(comentarios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDto> getComentarioById(@PathVariable("id") Long id) {
        ComentarioDto comentarioDto = comentarioServicio.getComentarioById(id);
        return ResponseEntity.ok(comentarioDto);
    }

    @GetMapping
    public ResponseEntity<List<ComentarioDto>> getAllComentarios() {
        List<ComentarioDto> comentarios = comentarioServicio.getAllComentarios();
        return ResponseEntity.ok(comentarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComentario(@PathVariable("id") Long id) {
        comentarioServicio.deleteComentario(id);
        return ResponseEntity.ok("Comentario eliminado correctamente");
    }
    
   @PutMapping("/{id}")
   public ResponseEntity<ComentarioDto> updateComentario(@PathVariable("id") Long id,
                                                          @RequestBody ComentarioDto comentarioDto) {
       ComentarioDto updatedComentario = comentarioServicio.updateComentario(id, comentarioDto);
       return ResponseEntity.ok(updatedComentario);
   }

   @GetMapping("/test/incidencia/{id}")
   public ResponseEntity<String> testIncidencia(@PathVariable Integer id) {
       try {
           IncidenciaDTO incidencia = comentarioServicio.validarIncidencia(id);
           return ResponseEntity.ok("✅ Incidencia encontrada: ID=" + incidencia.getId() +
                                  ", Título='" + incidencia.getTitulo() + "'" +
                                  ", Estado='" + incidencia.getNombreEstadoActual() + "'");
       } catch (Exception e) {
           return ResponseEntity.badRequest().body("❌ Error al consumir incidencia: " + e.getMessage());
       }
   }
}