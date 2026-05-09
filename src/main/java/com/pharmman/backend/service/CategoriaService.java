package com.pharmman.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.CrearCategoriaRequest;
import com.pharmman.backend.entity.Categoria;
import com.pharmman.backend.entity.Producto;
import com.pharmman.backend.repository.ICategoriaRepository;
import com.pharmman.backend.repository.IProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final ICategoriaRepository categoriaRepository;
    private final IProductoRepository productoRepository;

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

    @Transactional
    public Categoria editar(Integer id, CrearCategoriaRequest request, boolean actualizarCodigos) {
        Categoria c = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(request.getNombre().trim(), id))
            throw new RuntimeException("Ya existe una categoría con ese nombre");

        if (categoriaRepository.existsByPrefijoIgnoreCaseAndIdNot(request.getPrefijo().trim(), id))
            throw new RuntimeException("Ya existe una categoría con ese prefijo");

        String prefijoAnterior = c.getPrefijo();
        String prefijoNuevo = request.getPrefijo().trim().toUpperCase();

        c.setNombre(request.getNombre().trim());
        c.setPrefijo(prefijoNuevo);
        categoriaRepository.save(c);

        // Si el prefijo cambió y se solicitó actualizar los códigos
        if (actualizarCodigos && prefijoAnterior != null && !prefijoAnterior.equals(prefijoNuevo)) {
            List<Producto> productos = productoRepository.findByCategoriaId(id);
            for (Producto p : productos) {
                if (p.getCodigo() != null && p.getCodigo().startsWith(prefijoAnterior + "-")) {
                    String sufijo = p.getCodigo().substring(prefijoAnterior.length() + 1);
                    p.setCodigo(prefijoNuevo + "-" + sufijo);
                }
            }
            productoRepository.saveAll(productos);
        }

        return c;
    }
}
