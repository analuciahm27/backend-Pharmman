package com.pharmman.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pharmman.backend.dto.request.RegistrarIngresoRequest;
import com.pharmman.backend.dto.response.IngresoResponse;
import com.pharmman.backend.entity.DetalleIngreso;
import com.pharmman.backend.entity.Ingreso;
import com.pharmman.backend.entity.Producto;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IIngresoRepository;
import com.pharmman.backend.repository.IProductoRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngresoService {

    private final IIngresoRepository ingresoRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;

    @Transactional
    public IngresoResponse registrar(RegistrarIngresoRequest request, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(LocalDateTime.now());
        ingreso.setUsuario(usuario);

        List<DetalleIngreso> detalles = new ArrayList<>();

        for (RegistrarIngresoRequest.DetalleIngresoRequest d : request.getDetalles()) {
            Producto producto = productoRepository.findById(d.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProductoId()));

            producto.setStock(producto.getStock() + d.getCantidad());
            productoRepository.save(producto);

            DetalleIngreso detalle = new DetalleIngreso();
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalle.setIngreso(ingreso);
            detalles.add(detalle);
        }

        ingreso.setDetalles(detalles);
        return toResponse(ingresoRepository.save(ingreso));
    }

    public List<IngresoResponse> listar() {
        return ingresoRepository.findAllByOrderByFechaDesc()
            .stream().map(this::toResponse).toList();
    }

    private IngresoResponse toResponse(Ingreso i) {
        List<IngresoResponse.DetalleIngresoResponse> detalles = i.getDetalles().stream()
            .map(d -> new IngresoResponse.DetalleIngresoResponse(
                d.getProducto().getNombre(), d.getCantidad()))
            .toList();
        return new IngresoResponse(
            i.getId(), i.getFecha(),
            i.getUsuario().getNombre() + " " + i.getUsuario().getApellidoPaterno(),
            detalles
        );
    }
}
