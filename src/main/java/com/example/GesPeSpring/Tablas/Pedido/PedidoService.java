package com.example.GesPeSpring.Tablas.Pedido;

import com.example.GesPeSpring.Tablas.InfoEmpresa.InfoEmpresa;
import com.example.GesPeSpring.Tablas.Pedido.Pedido;
import com.example.GesPeSpring.Tablas.PedidoDetalle.PedidoDetalleDTO;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public float obtenerSumaTotalPedidosPorMes(int year, int month) {
        float total = pedidoRepository.sumarTotalFecha(year, month);
        return total;
    }
    
    public float obtenerSumaTotalPedidosPorMesUsername(int year, int month, String username) {
        float total = pedidoRepository.sumarTotalFechaUsuario(year, month, username);
        return total;
    }

    public Pedido crearPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public byte[] generarPdfFactura(Pedido pedido, List<PedidoDetalleDTO> pedidoDetalles, InfoEmpresa empresa) throws Exception {
        Context context = new Context();
        //Ruta absoluta al archivo logo.jpg dentro de templates

        //Saco el total neto y de impuestos del pedido entero
        BigDecimal totalNeto = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;

        for (PedidoDetalleDTO detalle : pedidoDetalles) {
            BigDecimal subtotal = detalle.getSubtotal();
            BigDecimal iva = detalle.getIva();

            //ivaDecimal = iva / 100
            BigDecimal ivaDecimal = iva.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            //divisor = 1 + (iva / 100)
            BigDecimal divisor = BigDecimal.ONE.add(ivaDecimal);

            //neto = subtotal / (1 + iva/100)
            BigDecimal neto = subtotal.divide(divisor, 2, RoundingMode.HALF_UP);

            //ivaCalculado = subtotal - neto
            BigDecimal ivaCalculado = subtotal.subtract(neto);

            //Sumar a totales
            totalNeto = totalNeto.add(neto);
            totalIVA = totalIVA.add(ivaCalculado);
        }

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
