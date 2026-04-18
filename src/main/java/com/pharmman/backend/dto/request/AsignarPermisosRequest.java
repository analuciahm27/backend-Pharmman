package com.pharmman.backend.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class AsignarPermisosRequest {
    private Long rolId;
    private List<Long> permisoIds;
}
