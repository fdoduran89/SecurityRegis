package Registraduria.Security.controllers;


import Registraduria.Security.models.Permission;
import Registraduria.Security.models.PermissionRole;
import Registraduria.Security.models.Role;
import Registraduria.Security.repositories.PermissionRepository;
import Registraduria.Security.repositories.PermissionRoleRepository;

import Registraduria.Security.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/PermissionRole")
public class PermissionRoleController {

    @Autowired
    private PermissionRoleRepository permissionRoleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("roles/{roleId}/permissions/{permissionId}")
    public PermissionRole createPermissionRole(@PathVariable String roleId, @PathVariable String permissionId) {
        PermissionRole nuevo = new PermissionRole();
        Role elRole = this.roleRepository.findById(roleId).get();
        Permission elPermission = this.permissionRepository.findById(permissionId).get();
        if (elRole!=null && elPermission!=null){
            nuevo.setPermission(elPermission);
            nuevo.setRole(elRole);
            return this.permissionRoleRepository.save(nuevo);
        }
        return null;
    }

    @GetMapping("")
    List<PermissionRole> getPermissionsRoles() {
        return permissionRoleRepository.findAll();
    }

    @GetMapping("{permissionRoleId}")
    PermissionRole getPermissionRole(@PathVariable("permissionRoleId") String permissionRoleId) {
        return permissionRoleRepository.findById(permissionRoleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: PermisoRole no existe"));
    }

    @PutMapping("{permissionRoleId}")
    PermissionRole updatePermissionRole(@PathVariable String permissionRoleId, @RequestBody PermissionRole permissionRoleInfo) {
        PermissionRole permissionRole = permissionRoleRepository.findById(permissionRoleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: PermisoRole no existe"));
        if (permissionRoleInfo.getRole() != null) permissionRole.setRole(permissionRoleInfo.getRole());
        if (permissionRoleInfo.getPermission() != null) permissionRole.setPermission(permissionRoleInfo.getPermission());
        return permissionRoleRepository.save(permissionRole);
    }

    @DeleteMapping("{permissionRoleId}")
    ResponseEntity<Object> deletePermissionRole(@PathVariable("permissionRoleId") String permissionRoleId) {
        PermissionRole permissionRole = permissionRoleRepository.findById(permissionRoleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: El permiso no existe"));
        permissionRoleRepository.delete(permissionRole);
        Map<String, String> data = new HashMap<>();
        data.put("message", "PermisoRole '" + permissionRole.getRole() + " " + permissionRole.getPermission() + "' fue eliminado satisfactoriamente");
        return new ResponseEntity<Object>(data, HttpStatus.ACCEPTED);



    }}
