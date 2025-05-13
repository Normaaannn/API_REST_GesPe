package com.example.GesPeSpring.Tablas.Producto;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.activo = false WHERE p.id = :id")
    void desactivarProducto(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Producto p SET p.activo = true WHERE p.id = :id AND p.activo = false")
    int activarProducto(@Param("id") Long id);

    Page<Producto> findAll(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Producto> buscarPorNombre(@Param("nombre") String nombre, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = true")
    Page<Producto> buscarPorNombreAndActivo(@Param("nombre") String nombre, Pageable pageable);
    
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = false")
    Page<Producto> buscarPorNombreAndInactivo(@Param("nombre") String nombre, Pageable pageable);
    
    Page<Producto> findByActivoTrue(Pageable pageable);
    
    Page<Producto> findByActivoFalse(Pageable pageable);
}
