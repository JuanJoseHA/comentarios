package itch.twp.comentarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequest {
    private Long incidenciaId;
    private String titulo;
    private String mensaje;
    private Long usuarioId; // Usuario que recibe la notificación
}