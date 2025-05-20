package com.example.GesPeSpring.Tablas.Usuario;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @GetMapping("/page/{pagina}")
    public Page<Usuario> obtenerUsuarios(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosPaginados(pagina - 1);
    }

    @GetMapping("/user/page/{pagina}")
    public Page<Usuario> obtenerUsuariosUser(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosRolePaginados("ROLE_USER", pagina - 1);
    }

    @GetMapping("/guest/page/{pagina}")
    public Page<Usuario> obtenerUsuariosGuest(@PathVariable int pagina) {
        return usuarioService.obtenerUsuariosRolePaginados("ROLE_GUEST", pagina - 1);
    }

    @GetMapping("/buscar/{texto}/page/{page}")
    public Page<Usuario> buscarUsuarios(@PathVariable String texto, @PathVariable int page) {
        return usuarioService.buscarUsuarios(texto, page - 1);
    }

    @GetMapping("/buscar/user/{texto}/page/{page}")
    public Page<Usuario> buscarUsuariosUser(@PathVariable String texto, @PathVariable int page) {
        return usuarioService.buscarUsuariosRole(texto, "ROLE_USER", page - 1);
    }

    @GetMapping("/buscar/guest/{texto}/page/{page}")
    public Page<Usuario> buscarUsuariosGuest(@PathVariable String texto, @PathVariable int page) {
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

    @PatchMapping("/{id}/banear")
    public ResponseEntity<String> quitarRolUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!usuario.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            usuarioRepository.banearUsuario(id);
            return ResponseEntity.ok("Usuario baneado correctamente");
        } else {
            return ResponseEntity.ok("No se puede banear a un administrador");
        }
    }

    @PatchMapping("/{id}/darRolUsuario")
    public ResponseEntity<String> darRolUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!usuario.getRole().equalsIgnoreCase("ROLE_ADMIN")) {
            usuarioRepository.darRolUsuario(id);
            return ResponseEntity.ok("Usuario activado correctamente");
        } else {
            return ResponseEntity.ok("No se puede degradar a un administrador");
        }
    }

    @PatchMapping("/actualizarPassword")
    public ResponseEntity<String> actualizarPassword(@RequestBody Map<String, String> update, Authentication authentication) {
        if (update == null || update.isEmpty()) {
            return ResponseEntity.badRequest().body("Los datos no pueden estar vacíos.");
        }

        String oldPassword = update.get("password");
        String newPassword = update.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Debes proporcionar la contraseña actual y la nueva.");
        }

        try {
            // Verifica que la contraseña actual sea correcta
            Authentication authCheck = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentication.getName(), oldPassword)
            );

            if (!authCheck.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta.");
            }

            // Actualiza la contraseña
            Usuario usuario = usuarioRepository.findByUsername(authentication.getName());
            String passwordHash = usuarioService.hashPassword(newPassword);
            usuario.setPasswordHash(passwordHash);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Contraseña actualizada");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña actual incorrecta.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error actualizando la contraseña: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordRecoveryDTO dto) {
        Usuario user = usuarioRepository.findByEmail(dto.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Email no encontrado");
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(user);

        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendResetLink(dto.getEmail(), resetLink);

        return ResponseEntity.ok("Link de recuperación enviado");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordRecoveryDTO dto) {
        Usuario user = usuarioRepository.findByResetToken(dto.getToken());
        if (user == null || user.getTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token no válido o expirado");
        }

        String passwordHash = usuarioService.hashPassword(dto.getNewPassword());
        user.setPasswordHash(passwordHash);
        user.setResetToken(null);
        user.setTokenExpiration(null);
        usuarioRepository.save(user);

        return ResponseEntity.ok("Contraseña actualizada");
    }

    @PatchMapping("/actualizarAvatar")
    public ResponseEntity<String> actualizarAvatar(@RequestBody Map<String, String> update, Authentication authentication) {

        if (update == null || update.isEmpty()) {
            return ResponseEntity.badRequest().body("Los datos no pueden estar vacíos.");
        }

        String avatarUrl = update.get("avatarUrl");

        if (avatarUrl == null) {
            return ResponseEntity.badRequest().body("Debes proporcionar la url del avatar.");
        }

        String usernameAuth = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(usernameAuth);

        usuario.setAvatarUrl(avatarUrl);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Avatar actualizado");
    }

    @GetMapping("/obtenerAvatar")
    public ResponseEntity<String> obtenerAvatar(Authentication authentication) {
        String usernameAuth = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(usernameAuth);

        return ResponseEntity.ok(usuario.getAvatarUrl());
    }

}
