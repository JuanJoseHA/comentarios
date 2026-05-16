package itch.twp.comentarios.controlador;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import itch.twp.comentarios.client.AuthClient;
import itch.twp.comentarios.dto.ComentarioDto;
import itch.twp.comentarios.dto.IncidenciaDTO;
import itch.twp.comentarios.servicio.ComentarioServicio;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/comentarios")
public class ComentarioControlador {

    private ComentarioServicio comentarioServicio;
    private AuthClient authClient;

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
    
    @PutMapping("/incidencia/{incidenciaId}/leidos/ciudadano")
    public ResponseEntity<Void> marcarLeidosCiudadano(@PathVariable Long incidenciaId) {
        comentarioServicio.marcarComoLeidosPorCiudadano(incidenciaId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/incidencia/{incidenciaId}/leidos/funcionario")
    public ResponseEntity<Void> marcarLeidosFuncionario(@PathVariable Long incidenciaId) {
        comentarioServicio.marcarComoLeidosPorFuncionario(incidenciaId);
        return ResponseEntity.noContent().build();
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

   @GetMapping("/test/conexiones/{incidenciaId}/{usuarioId}")
   public ResponseEntity<String> testConexiones(@PathVariable Integer incidenciaId, @PathVariable Long usuarioId) {
       StringBuilder resultado = new StringBuilder();
       
       // 1. Probar conexión con Microservicio de Incidencias (Kevin - 26.116.60.216)
       try {
           IncidenciaDTO incidencia = comentarioServicio.validarIncidencia(incidenciaId);
           resultado.append("✅ Incidencia encontrada: ID=").append(incidencia.getId())
                    .append(", Título='").append(incidencia.getTitulo()).append("'\n");
       } catch (Exception e) {
           resultado.append("❌ Error al conectar con Incidencias: ").append(e.getMessage()).append("\n");
       }

       // 2. Probar conexión con Microservicio Auth (Uri - 26.87.230.97)
       try {
           // Usamos el cliente directamente para la prueba rápida
           itch.twp.comentarios.dto.UsuarioDetalleDTO usuario = authClient.obtenerUsuarioPorId(usuarioId);
           resultado.append("✅ Usuario encontrado: ID=").append(usuario.getId())
                    .append(", Nombre='").append(usuario.getNombre()).append("'");
       } catch (Exception e) {
           resultado.append("❌ Error al conectar con Auth (Uri): ").append(e.getMessage());
       }

       return ResponseEntity.ok(resultado.toString());
   }
}