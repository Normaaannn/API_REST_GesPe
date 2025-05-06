package com.example.GesPeSpring.Tablas.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/page/{pagina}")
    public Page<Usuario> obtenerClientes(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosPaginados(pagina - 1);
    }

    @GetMapping("/user/page/{pagina}")
    public Page<Usuario> obtenerClientesUser(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosRolePaginados("ROLE_USER", pagina - 1);
    }

    @GetMapping("/guest/page/{pagina}")
    public Page<Usuario> obtenerClientesGuest(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosRolePaginados("ROLE_GUEST", pagina - 1);
    }

    @GetMapping("/buscar/{texto}/page/{page}")
    public Page<Usuario> buscarClientes(@PathVariable String texto, @PathVariable int page) {
        return usuarioService.buscarUsuarios(texto, page - 1);
    }

    @GetMapping("/buscar/user/{texto}/page/{page}")
    public Page<Usuario> buscarClientesUser(@PathVariable String texto, @PathVariable int page) {
        return usuarioService.buscarUsuariosRole(texto, "ROLE_USER", page - 1);
    }

    @GetMapping("/buscar/guest/{texto}/page/{page}")
    public Page<Usuario> buscarClientesGuest(@PathVariable String texto, @PathVariable int page) {
        return usuarioService.buscarUsuariosRole(texto, "ROLE_GUEST", page - 1);
    }

    @PostMapping("/register")
    public String registrarUsuario(
            @RequestBody Usuario usuario) {

        //Verificar si el correo ya esta registrado
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null || usuario.getEmail().isBlank() || usuario.getEmail() == null) {
            return "Correo existe";
        }

        //Verificar si el nombre de usuario ya esta registrado
        if (usuarioRepository.findByUsername(usuario.getUsername()) != null || usuario.getUsername().isBlank() || usuario.getUsername() == null) {
            return "Usuario existe";
        }

        //Verificar si la contraseña es nula o vacia
        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().isBlank()) {
            return "La contraseña no puede estar vacía.";
        }

        //Hashear la contraseña
        String passwordHash = usuarioService.hashPassword(usuario.getPasswordHash());

        //Actualizar objeto usuario
        usuario.setPasswordHash(passwordHash);
        usuario.setRole("ROLE_GUEST");

        //Guardar el usuario en la base de datos
        usuarioRepository.save(usuario);

        return "Registro completado";
    }

}
