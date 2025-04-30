package com.example.GesPeSpring.Tablas.Cliente;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findAll(Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE LOWER(CONCAT(c.nombre, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :parametro, '%')) "
            + "OR LOWER(c.nif) LIKE LOWER(:parametro) OR LOWER(c.ciudad) LIKE LOWER(:parametro)")
    Page<Cliente> buscarPorParametro(@Param("parametro") String parametro, Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE (LOWER(CONCAT(c.nombre, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :parametro, '%')) "
            + "OR LOWER(c.nif) LIKE LOWER(:parametro) OR LOWER(c.ciudad) LIKE LOWER(:parametro)) AND c.activo = true")
    Page<Cliente> buscarPorParametroAndActivo(@Param("parametro") String parametro, Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE (LOWER(CONCAT(c.nombre, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :parametro, '%')) "
            + "OR LOWER(c.nif) LIKE LOWER(:parametro) OR LOWER(c.ciudad) LIKE LOWER(:parametro)) AND c.activo = false")
    Page<Cliente> buscarPorParametroAndInactivo(@Param("parametro") String parametro, Pageable pageable);
    
    Cliente findByNif(String nif);
    
    Page<Cliente> findByActivoTrue(Pageable pageable);
    
    Page<Cliente> findByActivoFalse(Pageable pageable);
    
    @Modifying
    @Transactional
    @Query("UPDATE Cliente p SET p.activo = false WHERE p.id = :id")
    void desactivarCliente(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Cliente p SET p.activo = true WHERE p.id = :id AND p.activo = false")
    int activarCliente(@Param("id") Long id);

}
