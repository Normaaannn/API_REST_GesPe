package com.example.GesPeSpring.Tablas.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    //Método para crear un producto
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    //Método para obtener todos los productos
    public Iterable<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Page<Producto> obtenerProductosPaginados(int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return productoRepository.findAll(pageable);
    }
    
    public Page<Producto> obtenerProductosActivosPaginados(Pageable pageable) {
        return productoRepository.findByActivoTrue(pageable);
    }
    
    public Page<Producto> obtenerProductosInactivosPaginados(int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);
        return productoRepository.findByActivoFalse(pageable);
    }

    public Page<ProductoDTO> buscarProductos(String texto, int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Producto> productos = productoRepository.buscarPorNombre(texto, pageable);

        return productos.map(p -> new ProductoDTO(
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecioNeto(),
                p.getIva()
        ));
    }

    public Page<ProductoDTO> buscarProductosActivos(String texto, int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Producto> productos = productoRepository.buscarPorNombreAndActivo(texto, pageable);

        return productos.map(p -> new ProductoDTO(
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecioNeto(),
                p.getIva()
        ));
    }

    public Page<ProductoDTO> buscarProductosInactivos(String texto, int page) {
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Producto> productos = productoRepository.buscarPorNombreAndInactivo(texto, pageable);

        return productos.map(p -> new ProductoDTO(
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecioNeto(),
                p.getIva()
        ));
    }

    //Método para obtener un producto por su ID
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

}
