package itch.twp.comentarios.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComentarioDto {
    private Long id;
    private Long incidenciaId;
    private Long usuarioId;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private Boolean esOficial;
}