package com.example.GesPeSpring.Tablas.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder; // Instancia de BCryptPasswordEncoder
    }

    public Page<Usuario> obtenerUsuariosPaginados(int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> obtenerUsuariosRolePaginados(String rol, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return usuarioRepository.findByRole(rol, pageable);
    }

    public Page<Usuario> buscarUsuarios(String texto, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return usuarioRepository.buscarPorParametro(texto, pageable);
    }

    public Page<Usuario> buscarUsuariosRole(String texto, String rol, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return usuarioRepository.buscarPorParametroAndRole(texto, rol, pageable);
    }

    // Método para registrar un nuevo usuario (guardar hash de la contraseña)
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword); // Hashea la contraseña
    }

    // Método para verificar la contraseña durante el login
    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword); // Verifica si el hash coincide
    }
}
