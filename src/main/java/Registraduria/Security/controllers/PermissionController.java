package Registraduria.Security.controllers;

import Registraduria.Security.models.Permission;
import Registraduria.Security.repositories.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionRepository permissionRepository;
    @PostMapping("")
    Permission createPermission(@RequestBody Permission infoPermission) {
        return permissionRepository.save(infoPermission);
    }

    @GetMapping("")
    List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("{permissionId}")
    Permission getPermission(@PathVariable("permissionId") String permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permiso no existe"));
    }

    @PutMapping("{permissionId}")
    Permission updatePermission(@PathVariable String permissionId, @RequestBody Permission permissionInfo) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Permiso no existe"));
        if (permissionInfo.getUrl() != null) permission.setUrl(permissionInfo.getUrl());
        if (permissionInfo.getMethod() != null) permission.setMethod(permissionInfo.getMethod());
        return permissionRepository.save(permission);
    }

    @DeleteMapping("{permissionId}")
    ResponseEntity<Object> deletePermission(@PathVariable("permissionId") String permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El permiso no existe"));
        permissionRepository.delete(permission);
        Map<String, String> data = new HashMap<>();
        data.put("message", "Permiso '" + permission.getUrl() + "' fue eliminado satisfactoriamente");
        return new ResponseEntity<Object>(data, HttpStatus.ACCEPTED);
    }
}
