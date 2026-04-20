package com.pharmman.backend.repository;

import com.pharmman.backend.entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModuloRepository extends JpaRepository<Modulo, Integer> {}