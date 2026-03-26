package itch.twp.comentarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import itch.twp.comentarios.dto.UsuarioDetalleDTO;


@FeignClient(name = "servicio-auth", url = "http://192.168.212.117:8081")
public interface AuthClient {

    // La ruta exacta que acabas de programar en tu AuthController
    @GetMapping("/api/auth/usuario/{id}")
    UsuarioDetalleDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
    
}

