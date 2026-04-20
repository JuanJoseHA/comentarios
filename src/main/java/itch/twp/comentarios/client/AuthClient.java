package itch.twp.comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.twp.comentarios.dto.UsuarioDetalleDTO;


@FeignClient(name = "SERVICIO-AUTH", url = "http://192.168.210.229:8088")
public interface AuthClient {

    @GetMapping("/api/auth/usuario/{id}")
    UsuarioDetalleDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
    
}

