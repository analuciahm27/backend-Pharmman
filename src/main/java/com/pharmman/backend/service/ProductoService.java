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
        if (productoRepository.existsByCodigo(request.getCodigo()))
            throw new RuntimeException("El código ya está registrado");

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto p = new Producto();
        p.setCodigo(request.getCodigo());
        p.setNombre(request.getNombre());
        p.setDescripcion(request.getDescripcion());
        p.setPrecio(request.getPrecio());
        p.setCategoria(categoria);
        return toResponse(productoRepository.save(p));
    }

    public ProductoResponse editar(Integer id, EditarProductoRequest request) {
        Producto p = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        p.setCodigo(request.getCodigo());
        p.setNombre(request.getNombre());
        p.setDescripcion(request.getDescripcion());
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
