package com.pharmman.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.CrearCategoriaRequest;
import com.pharmman.backend.entity.Categoria;
import com.pharmman.backend.repository.ICategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final ICategoriaRepository categoriaRepository;

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria crear(CrearCategoriaRequest request) {
        if (categoriaRepository.existsByNombreIgnoreCase(request.getNombre().trim()))
            throw new RuntimeException("Ya existe una categoría con ese nombre");

        if (categoriaRepository.existsByPrefijoIgnoreCase(request.getPrefijo().trim()))
            throw new RuntimeException("Ya existe una categoría con ese prefijo");

        Categoria c = new Categoria();
        c.setNombre(request.getNombre().trim());
        c.setPrefijo(request.getPrefijo().trim().toUpperCase());
        return categoriaRepository.save(c);
    }

    public Categoria editar(Integer id, CrearCategoriaRequest request) {
        Categoria c = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre().trim(), id))
            throw new RuntimeException("Ya existe una categoría con ese nombre");

        if (categoriaRepository.existsByPrefijoIgnoreCaseAndIdNot(request.getPrefijo().trim(), id))
            throw new RuntimeException("Ya existe una categoría con ese prefijo");

        c.setNombre(request.getNombre().trim());
        c.setPrefijo(request.getPrefijo().trim().toUpperCase());
        return categoriaRepository.save(c);
    }
}
