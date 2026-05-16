package itch.twp.comentarios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incidencia_id", nullable = false)
    private Long incidenciaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "es_oficial")
    private Boolean esOficial;
    
    @Column(name = "leido_ciudadano", nullable = false)
    private Boolean leidoCiudadano = false;

    @Column(name = "leido_funcionario", nullable = false)
    private Boolean leidoFuncionario = false;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}