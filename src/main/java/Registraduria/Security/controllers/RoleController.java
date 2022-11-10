package Registraduria.Security.controllers;

import Registraduria.Security.models.Permission;
import Registraduria.Security.models.Role;
import Registraduria.Security.models.User;
import Registraduria.Security.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping("")
    Role createrole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    @GetMapping("")
    List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @GetMapping("{roleId}")
    Role getRoleById(@PathVariable("roleId") String roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol no existe"));
    }

    @PutMapping("{roleId}")
    Role updateRole(@PathVariable String roleId, @RequestBody Role roleInfo) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol no existe"));
        if (roleInfo.getName() != null) role.setName(roleInfo.getName());
        if (roleInfo.getDescription() != null) role.setDescription(roleInfo.getDescription());
        return roleRepository.save(role);
    }

    @DeleteMapping("{roleId}")
    ResponseEntity<Object> deleteRole(@PathVariable("roleId") String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol no existe"));
        roleRepository.delete(role);
        Map<String, String> data = new HashMap<>();
        data.put("message", "Rol " + role.getName() + " fue eliminado satisfactoriamente");
        return new ResponseEntity<Object>(data, HttpStatus.ACCEPTED);
    }
}
