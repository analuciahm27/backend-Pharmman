package com.pharmman.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Ingreso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime fecha = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;

    @OneToMany(mappedBy = "ingreso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DetalleIngreso> detalles = new ArrayList<>();
}
