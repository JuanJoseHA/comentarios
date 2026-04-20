package itch.twp.comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import itch.twp.comentarios.dto.NotificacionRequest;

@FeignClient(name = "NOTIFICACIONES", url = "http://192.168.212.117:8086")
public interface NotificacionClient {

    @PostMapping("/api/notificaciones")
    void enviarNotificacion(@RequestBody NotificacionRequest request);

    @PostMapping("/api/notificaciones/enviar")
    void enviarNotificacionAlt(@RequestBody NotificacionRequest request);

    @PostMapping("/notificaciones")
    void enviarNotificacionSimple(@RequestBody NotificacionRequest request);
}