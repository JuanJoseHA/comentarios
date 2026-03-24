package itch.twp.comentarios.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import itch.twp.comentarios.entity.Comentario;
import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {
    // Busca todo el historial de una incidencia
    List<Comentario> findByIncidenciaIdOrderByFechaCreacionAsc(Long incidenciaId);
}
