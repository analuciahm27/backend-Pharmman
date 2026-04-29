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
        Categoria c = new Categoria();
        c.setNombre(request.getNombre());
        if (request.getPrefijo() != null)
            c.setPrefijo(request.getPrefijo().trim().toUpperCase());
        return categoriaRepository.save(c);
    }

    public Categoria editar(Integer id, CrearCategoriaRequest request) {
        Categoria c = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        c.setNombre(request.getNombre());
        if (request.getPrefijo() != null)
            c.setPrefijo(request.getPrefijo().trim().toUpperCase());
        return categoriaRepository.save(c);
    }
}
