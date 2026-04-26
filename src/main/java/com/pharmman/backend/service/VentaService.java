package com.pharmman.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmman.backend.dto.request.RegistrarVentaRequest;
import com.pharmman.backend.dto.response.VentaResponse;
import com.pharmman.backend.entity.DetalleVenta;
import com.pharmman.backend.entity.Producto;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.entity.Venta;
import com.pharmman.backend.repository.IProductoRepository;
import com.pharmman.backend.repository.IUsuarioRepository;
import com.pharmman.backend.repository.IVentaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final IVentaRepository ventaRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;

    @Transactional
    public VentaResponse registrar(RegistrarVentaRequest request, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setMetodoPago(request.getMetodoPago());
        venta.setFecha(LocalDateTime.now());
        venta.setUsuario(usuario);

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0;

        for (RegistrarVentaRequest.DetalleVentaRequest d : request.getDetalles()) {
            Producto producto = productoRepository.findById(d.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProductoId()));

            if (producto.getStock() < d.getCantidad())
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());

            producto.setStock(producto.getStock() - d.getCantidad());
            productoRepository.save(producto);

            double subtotal = producto.getPrecio() * d.getCantidad();
            total += subtotal;

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            detalle.setVenta(venta);
            detalles.add(detalle);
        }

        venta.setTotal(total);
        venta.setDetalles(detalles);
        return toResponse(ventaRepository.save(venta));
    }

    public List<VentaResponse> listar() {
        return ventaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<VentaResponse> listarPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return ventaRepository.findByFechaBetweenOrderByFechaDesc(desde, hasta)
            .stream().map(this::toResponse).toList();
    }

    private VentaResponse toResponse(Venta v) {
        List<VentaResponse.DetalleVentaResponse> detalles = v.getDetalles().stream()
            .map(d -> new VentaResponse.DetalleVentaResponse(
                d.getProducto().getNombre(), d.getCantidad(),
                d.getPrecioUnitario(), d.getSubtotal()))
            .toList();
        return new VentaResponse(
            v.getId(), v.getMetodoPago(), v.getTotal(), v.getFecha(),
            v.getUsuario().getNombre() + " " + v.getUsuario().getApellidoPaterno(),
            detalles
        );
    }
}
