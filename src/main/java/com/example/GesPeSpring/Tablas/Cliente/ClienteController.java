package com.example.GesPeSpring.Tablas.Cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/page/{pagina}")
    public Page<Cliente> obtenerClientes(@PathVariable int pagina) {
        return clienteService.obtenerClientesPaginados(pagina - 1);
    }

    @GetMapping("activos/page/{pagina}")
    public Page<Cliente> obtenerClientesActivos(@PathVariable int pagina) {
        return clienteService.obtenerClientesActivosPaginados(pagina - 1);
    }

    @GetMapping("inactivos/page/{pagina}")
    public Page<Cliente> obtenerClientesInactivos(@PathVariable int pagina) {
        return clienteService.obtenerClientesInactivosPaginados(pagina - 1);
    }

    @GetMapping("/buscar/{texto}/page/{page}")
    public Page<Cliente> buscarClientes(@PathVariable String texto, @PathVariable int page) {
        return clienteService.buscarClientes(texto, page - 1);
    }

    @GetMapping("/buscar/activos/{texto}/page/{page}")
    public Page<Cliente> buscarClientesActivos(@PathVariable String texto, @PathVariable int page) {
        return clienteService.buscarClientesActivos(texto, page - 1);
    }

    @GetMapping("/buscar/inactivos/{texto}/page/{page}")
    public Page<Cliente> buscarClientesInactivos(@PathVariable String texto, @PathVariable int page) {
        return clienteService.buscarClientesInactivos(texto, page - 1);
    }

    @GetMapping("/{id}")
    public Optional<Cliente> obtenerCliente(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }

    @PostMapping
    public String crearCliente(@RequestBody Cliente cliente) {

        // Verificar si el correo ya está registrado
        if (clienteRepository.findByNif(cliente.getNif()) != null) {
            return "NIF existe";
        }
        clienteService.guardarCliente(cliente);
        return "Cliente añadido";
    }

    @PutMapping("/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        cliente.setId(id);
        return clienteService.guardarCliente(cliente);
    }

    @PatchMapping("/{id}")
    public String actualizarParcialCliente(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.updateValue(cliente, updates);  // Solo se actualizan los campos enviados
            clienteRepository.save(cliente);
            return "Cliente actualizado";  // Solo se devuelve un mensaje de texto
        } catch (Exception e) {
            return "Error actualizando cliente: " + e.getMessage();  // Si hay error, se devuelve un mensaje de error
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivarCliente(@PathVariable Long id) {
        clienteRepository.desactivarCliente(id);
        return ResponseEntity.ok("Cliente desactivado correctamente");
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<String> activarCliente(@PathVariable Long id) {
        clienteRepository.activarCliente(id);
        return ResponseEntity.ok("Cliente activado correctamente");
    }
}
