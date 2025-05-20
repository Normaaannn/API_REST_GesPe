package com.example.GesPeSpring.Tablas.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Page<Cliente> obtenerClientesPaginados(int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id").descending());
        return clienteRepository.findAll(pageable);
    }

    public Page<Cliente> obtenerClientesActivosPaginados(Pageable pageable) {
        return clienteRepository.findByActivoTrue(pageable);
    }
    
    public Page<Cliente> obtenerClientesInactivosPaginados(int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id").descending());
        return clienteRepository.findByActivoFalse(pageable);
    }

    public Page<Cliente> buscarClientes(String texto, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id").descending());
        return clienteRepository.buscarPorParametro(texto, pageable);
    }
    
    public Page<Cliente> buscarClientesActivos(String texto, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id").descending());
        return clienteRepository.buscarPorParametroAndActivo(texto, pageable);
    }
    
    public Page<Cliente> buscarClientesInactivos(String texto, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id").descending());
        return clienteRepository.buscarPorParametroAndInactivo(texto, pageable);
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
