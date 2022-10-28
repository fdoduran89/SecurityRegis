package Registraduria.Security.controllers;

import Registraduria.Security.models.Role;
import Registraduria.Security.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    @PostMapping("")
    Role createrole(@RequestBody Role role){

        return roleRepository.save(role);
    }
}
