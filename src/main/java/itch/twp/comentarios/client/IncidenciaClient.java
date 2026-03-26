package itch.twp.comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import itch.twp.comentarios.dto.IncidenciaDTO;
import java.util.List;

@FeignClient(name = "servicio-incidencias", url = "http://192.168.212.117:8082")
public interface IncidenciaClient {

    // 1. Crear el reporte
    @PostMapping("/api/incidencias")
    IncidenciaDTO crear(@RequestBody IncidenciaDTO dto);

    // 2. Consultar una incidencia por ID
    @GetMapping("/api/incidencias/{id}")
    IncidenciaDTO obtenerPorId(@PathVariable("id") Integer id);

    // 3. Cambiar estado/Asignar personal
    @PutMapping("/api/incidencias/{id}/estado")
    Object actualizarEstado(@PathVariable("id") Integer id, @RequestParam("nuevoEstadoId") Integer nuevoEstadoId);

    // 4. Listar todas las incidencias
    @GetMapping("/api/incidencias")
    List<IncidenciaDTO> listar();

    // 5. Actualizar datos generales
    @PutMapping("/api/incidencias/{id}")
    IncidenciaDTO actualizar(@PathVariable("id") Integer id, @RequestBody IncidenciaDTO dto);

    // 6. Eliminar incidencia
    @DeleteMapping("/api/incidencias/{id}")
    void eliminar(@PathVariable("id") Integer id);
}