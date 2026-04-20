package itch.twp.comentarios.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import itch.twp.comentarios.dto.IncidenciaDTO;
import java.util.Collections;
import java.util.List;

@Component
public class IncidenciaClientFallback implements FallbackFactory<IncidenciaClient> {

    private static final Logger log = LoggerFactory.getLogger(IncidenciaClientFallback.class);

    @Override
    public IncidenciaClient create(Throwable cause) {
        log.error("Error al conectar con el servicio de incidencias: {}", cause.getMessage());
        return new IncidenciaClient() {
            @Override
            public IncidenciaDTO crear(IncidenciaDTO dto) {
                log.warn("Fallback: No se puede crear incidencia - servicio no disponible");
                return null;
            }

            @Override
            public IncidenciaDTO obtenerPorId(Integer id) {
                log.warn("Fallback: Retornando null para incidencia ID {} - servicio no disponible", id);
                return null;
            }

            @Override
            public Object actualizarEstado(Integer id, Integer nuevoEstadoId) {
                log.warn("Fallback: No se puede actualizar estado - servicio no disponible");
                return null;
            }

            @Override
            public List<IncidenciaDTO> listar() {
                log.warn("Fallback: Retornando lista vacía - servicio no disponible");
                return Collections.emptyList();
            }

            @Override
            public IncidenciaDTO actualizar(Integer id, IncidenciaDTO dto) {
                log.warn("Fallback: No se puede actualizar incidencia - servicio no disponible");
                return null;
            }

            @Override
            public void eliminar(Integer id) {
                log.warn("Fallback: No se puede eliminar incidencia - servicio no disponible");
            }
        };
    }
}