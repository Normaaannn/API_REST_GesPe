package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    //Buscar pedidos por usuario creador, solo quiero que me devuelva 20
    Page<Pedido> findByUsuarioCreador_Username(String username, Pageable pageable);

    // Buscar pedidos por mes y año
    @Query("SELECT p FROM Pedido p WHERE YEAR(p.fechaEmision) = :year AND MONTH(p.fechaEmision) = :month")
    List<Pedido> findByMonthAndYear(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE YEAR(p.fechaEmision) = :year AND MONTH(p.fechaEmision) = :month")
    float sumarTotalMonthAndYear(@Param("year") int year, @Param("month") int month);

    @Query("SELECT p FROM Pedido p WHERE p.fechaEmision BETWEEN :startDate AND :endDate")
    Page<Pedido> findByFechaBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT p FROM Pedido p WHERE p.usuarioCreador.username = :username AND p.fechaEmision BETWEEN :startDate AND :endDate")
    Page<Pedido> findByUsuarioCreadorAndFechaBetween(
            @Param("username") String username,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    // Método para obtener el último pedido creado (con el id más alto)
    Optional<Pedido> findTopByOrderByIdDesc();
}
