package com.pharmman.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.CrearProductoRequest;
import com.pharmman.backend.dto.request.EditarProductoRequest;
import com.pharmman.backend.dto.response.ProductoResponse;
import com.pharmman.backend.entity.Categoria;
import com.pharmman.backend.entity.Producto;
import com.pharmman.backend.repository.ICategoriaRepository;
import com.pharmman.backend.repository.IProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final IProductoRepository productoRepository;
    private final ICategoriaRepository categoriaRepository;

    public String siguienteCodigo(Integer categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        String prefijo = categoria.getPrefijo();
        if (prefijo == null || prefijo.isBlank())
            throw new RuntimeException("La categoría no tiene prefijo configurado");

        // Buscar el último código de esta categoría
        List<Producto> productos = productoRepository.findAll().stream()
            .filter(p -> p.getCodigo() != null && p.getCodigo().startsWith(prefijo + "-"))
            .toList();

        int siguiente = 1;
        if (!productos.isEmpty()) {
            siguiente = productos.stream()
                .mapToInt(p -> {
                    try {
                        return Integer.parseInt(p.getCodigo().substring(prefijo.length() + 1));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max().orElse(0) + 1;
        }

        return String.format("%s-%03d", prefijo, siguiente);
    }

    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<ProductoResponse> buscar(String termino) {
        return productoRepository
            .findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(termino, termino)
            .stream().map(this::toResponse).toList();
    }

    public ProductoResponse crear(CrearProductoRequest request) {
        // Validaciones
        if (request.getCodigo() == null || request.getCodigo().trim().isEmpty()) {
            throw new RuntimeException("El código es obligatorio");
        }
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (request.getPrecio() == null || request.getPrecio() <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        if (productoRepository.existsByCodigo(request.getCodigo()))
            throw new RuntimeException("El código ya está registrado");

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto p = new Producto();
        p.setCodigo(request.getCodigo().trim().toUpperCase());
        p.setNombre(request.getNombre().trim());
        p.setDescripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null);
        p.setPrecio(request.getPrecio());
        p.setCategoria(categoria);
        return toResponse(productoRepository.save(p));
    }

    public ProductoResponse editar(Integer id, EditarProductoRequest request) {
        // Validaciones
        if (request.getCodigo() == null || request.getCodigo().trim().isEmpty()) {
            throw new RuntimeException("El código es obligatorio");
        }
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (request.getPrecio() == null || request.getPrecio() <= 0) {
            throw new RuntimeException("El precio debe ser mayor a 0");
        }

        Producto p = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        p.setCodigo(request.getCodigo().trim().toUpperCase());
        p.setNombre(request.getNombre().trim());
        p.setDescripcion(request.getDescripcion() != null ? request.getDescripcion().trim() : null);
        p.setPrecio(request.getPrecio());
        p.setCategoria(categoria);
        return toResponse(productoRepository.save(p));
    }

    public ProductoResponse cambiarEstado(Integer id) {
        Producto p = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setActivo(!p.isActivo());
        return toResponse(productoRepository.save(p));
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(
            p.getId(), p.getCodigo(), p.getNombre(), p.getDescripcion(),
            p.getPrecio(), p.getStock(), p.isActivo(),
            p.getCategoria() != null ? p.getCategoria().getId() : null,
            p.getCategoria() != null ? p.getCategoria().getNombre() : null
        );
    }
}
