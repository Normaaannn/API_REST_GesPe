package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.InfoEmpresa.InfoEmpresa;
import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalle;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalleDTO;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

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
    
    public Page<Pedido> buscarPedidosFechaBetween(Pageable pageable, int year1, int month1, int year2, int month2) {
        LocalDate startDate = LocalDate.of(year1, month1, 1);
        LocalDate endDate = LocalDate.of(year2, month2, 1)
                .plusMonths(1)
                .minusDays(1);

        return pedidoRepository.findByFechaBetween(startDate, endDate, pageable);
    }
    
    public Page<Pedido> buscarPedidosUsuarioFechaBetween(
            Pageable pageable,
            String username,
            int year1, int month1,
            int year2, int month2
    ) {
        LocalDate startDate = LocalDate.of(year1, month1, 1);
        LocalDate endDate = LocalDate.of(year2, month2, 1)
                .plusMonths(1)
                .minusDays(1); // último día del mes final

        return pedidoRepository.findByUsuarioCreadorAndFechaBetween(username, startDate, endDate, pageable);
    }

    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public byte[] generarPdfFactura(Pedido pedido, List<PedidoDetalleDTO> pedidoDetalles, InfoEmpresa empresa) throws Exception {
        Context context = new Context();
        //Ruta absoluta al archivo logo.jpg dentro de templates
        byte[] logoBytes = Files.readAllBytes(Paths.get("src/main/resources/templates/logo.jpg"));
        String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);

        //Saco el total neto y de impuestos del pedido entero
        float totalNeto = 0;
        float totalIVA = 0;
        for (PedidoDetalleDTO detalle : pedidoDetalles) {
            float neto;
            neto = detalle.getSubtotal() / (1 + (detalle.getIva() / 100));
            totalNeto += neto;
            totalIVA += detalle.getSubtotal() - neto;
        }

        context.setVariable("logoBase64", logoBase64);
        context.setVariable("pedido", pedido);
        context.setVariable("pedidoDetalles", pedidoDetalles);
        context.setVariable("empresa", empresa);
        context.setVariable("totalNeto", totalNeto);
        context.setVariable("totalIVA", totalIVA);

        String html = templateEngine.process("factura", context);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos);
            return baos.toByteArray();
        }
    }
}
