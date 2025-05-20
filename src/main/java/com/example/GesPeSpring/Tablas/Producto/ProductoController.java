package com.example.GesPeSpring.Tablas.Producto;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    // Crear un producto
    @PostMapping
    public String crearProducto(@RequestBody Producto producto) {
        productoService.crearProducto(producto);
        return "Producto añadido";
    }

    @GetMapping("/todos/page/{pagina}")
    public Page<Producto> obtenerProductos(@PathVariable int pagina) {
        return productoService.obtenerProductosPaginados(pagina - 1);
    }

    @GetMapping("/activos/page/{pagina}")
    public Page<Producto> obtenerProductosActivos(@PathVariable int pagina, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(pagina, size);
        return productoService.obtenerProductosActivosPaginados(pageable);
    }

    @GetMapping("/inactivos/page/{pagina}")
    public Page<Producto> obtenerProductosInactivos(@PathVariable int pagina) {
        return productoService.obtenerProductosInactivosPaginados(pagina - 1);
    }

    @GetMapping("/buscar/todos/{texto}/page/{page}")
    public Page<ProductoDTO> buscarProductos(@PathVariable String texto, @PathVariable int page) {
        return productoService.buscarProductos(texto, page - 1);
    }

    @GetMapping("/buscar/activos/{texto}/page/{page}")
    public Page<ProductoDTO> buscarProductosActivos(@PathVariable String texto, @PathVariable int page) {
        return productoService.buscarProductos(texto, page - 1);
    }

    @GetMapping("/buscar/inactivos/{texto}/page/{page}")
    public Page<ProductoDTO> buscarProductosInactivos(@PathVariable String texto, @PathVariable int page) {
        return productoService.buscarProductos(texto, page - 1);
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @PatchMapping("/{id}")
    public String actualizarParcialProducto(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.updateValue(producto, updates);  // Solo se actualizan los campos enviados
            productoRepository.save(producto);
            return "Producto actualizado";  // Solo se devuelve un mensaje de texto
        } catch (Exception e) {
            return "Error actualizando producto: " + e.getMessage();  // Si hay error, se devuelve un mensaje de error
        }
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<String> desactivarProducto(@PathVariable Long id) {
        productoRepository.desactivarProducto(id);
        return ResponseEntity.ok("Producto desactivado correctamente");
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<String> activarProducto(@PathVariable Long id) {
        productoRepository.activarProducto(id);
        return ResponseEntity.ok("Producto activado correctamente");
    }
}
