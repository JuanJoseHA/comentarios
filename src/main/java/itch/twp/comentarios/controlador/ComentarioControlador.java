package itch.twp.comentarios.controlador;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import itch.twp.comentarios.dto.ComentarioDto;
import itch.twp.comentarios.servicio.ComentarioServicio;
import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comentarios") // Ruta base definida en el API Gateway [cite: 106]
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
    
 // Obtener un Comentario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDto> getComentarioById(@PathVariable("id") Long id) {
        ComentarioDto comentarioDto = comentarioServicio.getComentarioById(id);
        return ResponseEntity.ok(comentarioDto);
    }

    // Obtener absolutamente todos los comentarios (opcional, para reportes generales)
    @GetMapping
    public ResponseEntity<List<ComentarioDto>> getAllComentarios() {
        List<ComentarioDto> comentarios = comentarioServicio.getAllComentarios();
        return ResponseEntity.ok(comentarios);
    }

    // ELIMINAR un Comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComentario(@PathVariable("id") Long id) {
        comentarioServicio.deleteComentario(id);
        return ResponseEntity.ok("Comentario eliminado correctamente");
    }
    
 // ACTUALIZAR un Comentario
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDto> updateComentario(@PathVariable("id") Long id, 
                                                          @RequestBody ComentarioDto comentarioDto) {
        ComentarioDto updatedComentario = comentarioServicio.updateComentario(id, comentarioDto);
        return ResponseEntity.ok(updatedComentario);
    }
}