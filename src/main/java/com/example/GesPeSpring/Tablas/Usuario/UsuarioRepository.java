package com.example.GesPeSpring.Tablas.Usuario;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    Usuario findByEmail(String email);
    
    Usuario findByResetToken(String resetToken);
    
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :parametro, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :parametro, '%'))")
    Page<Usuario> buscarPorParametro(@Param("parametro") String parametro, Pageable pageable);
    
    @Query("SELECT u FROM Usuario u WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :parametro, '%')) "
            + "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :parametro, '%'))) AND u.role = :parametro2")
    Page<Usuario> buscarPorParametroAndRole(@Param("parametro") String parametro, @Param("parametro2") String parametro2, Pageable pageable);   
        
    Page<Usuario> findByRole(String role, Pageable pageable);
    
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.role = 'ROLE_BANNED' WHERE u.id = :id")
    void banearUsuario(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.role = 'ROLE_USER' WHERE u.id = :id")
    int darRolUsuario(@Param("id") Long id);
}
