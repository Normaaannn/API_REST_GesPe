package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Page<Pedido> obtenerTodosPageable(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    public Optional<Pedido> obtenerPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> obtenerPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
    
    public Page<Pedido> obtenerPorUsuarioCreador(String username, Pageable pageable) {
        return pedidoRepository.findByUsuarioCreador_Username(username, pageable);
    }

    public List<Pedido> obtenerPorMesYAnio(int year, int month) {
        return pedidoRepository.findByMonthAndYear(year, month);
    }

    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}
