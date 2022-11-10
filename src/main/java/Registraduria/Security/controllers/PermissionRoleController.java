package Registraduria.Security.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/permissions-roles")
@AllArgsConstructor
public class PermissionRoleController {
    private final PermissionRoleRepository permissionRoleRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final UserRepository userRepository;

    @GetMapping("")
    List<PermissionRole> getAll() {
        return this.permissionRoleRepository.findAll();
    }

    @GetMapping("{id}")
    PermissionRole getById(@PathVariable String id) {
        return this.permissionRoleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping("role/{idRole}")
    List<PermissionRole> getAllByRole(@PathVariable String idRole) {
        Role role = this.roleRepository.findById(idRole).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return this.permissionRoleRepository.findByRole(role);
    }

    @PostMapping("verify")
    ResponseEntity<Object> validatePermissionsByRole(HttpServletRequest request, @RequestBody Permission info) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        Map<String, String> data = new HashMap<>();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring("Bearer ".length()); // "Bearer <encrypted-token>"
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                User user = userRepository.findByUsername(username);
                List<PermissionRole> accesses = this.permissionRoleRepository.findByRole(user.getRole());
                for (int i = 0; i < accesses.size(); i++) {
                    boolean hasAccess = accesses.get(i).getPermission().getMethod().equals(info.getMethod())
                            && accesses.get(i).getPermission().getUrl().equals(info.getUrl());
                    if (hasAccess) {
                        data.put("message", "El usuario tiene acceso");
                        return new ResponseEntity<Object>(data, HttpStatus.OK);
                    }
                }
                data.put("message", "El usuario no tiene acceso al recurso solicitado");
                return new ResponseEntity<Object>(data, HttpStatus.FORBIDDEN);
            }
            catch(Exception exception) {
                log.error("Error {}", exception.getMessage());
                data.put("message", exception.getMessage());
                return new ResponseEntity<Object>(data, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            data.put("message", "No autorizado, inicia sesión!");
            return new ResponseEntity<Object>(data, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("permission/{idPermission}")
    List<PermissionRole> getAllByPermission(@PathVariable String idPermission) {
        Permission permission = this.permissionRepository.findById(idPermission).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return this.permissionRoleRepository.findByPermission(permission);
    }

    @PostMapping("role/{roleId}/permission/{permissionId}")
    PermissionRole create(@PathVariable String roleId, @PathVariable String permissionId) {
        Role role = this.roleRepository.findById(roleId).orElse(null);
        Permission permission = this.permissionRepository.findById(permissionId).orElse(null);
        if (role == null || permission == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean exist = this.checkIfAlreadyExists(role, permission);
        if (exist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            PermissionRole permissionRole = new PermissionRole(role, permission);
            return this.permissionRoleRepository.save(permissionRole);
        }
    }

    boolean checkIfAlreadyExists(Role role, Permission permission) {
        /*
         * Verificar si existe relación entre el rol y el permiso dado
         * */
        List<PermissionRole> items = this.permissionRoleRepository.findByRole(role);
        for (int i = 0; i < items.size(); i++) {
            boolean exist = items.get(i).getPermission().getId().equals(permission.getId());
            if (exist) {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("{permissionRoleId}")
    ResponseEntity<Object> deletePermission(@PathVariable("permissionRoleId") String permissionRoleId) {
        PermissionRole permissionRole = permissionRoleRepository.findById(permissionRoleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El acceso no existe"));
        permissionRoleRepository.delete(permissionRole);
        Map<String, String> data = new HashMap<>();
        data.put(
                "message",
                "Acceso " +
                        permissionRole.getPermission().getUrl() + " " +
                        permissionRole.getPermission().getMethod() + " " +
                        " fue eliminado satisfactoriamente");
        return new ResponseEntity<Object>(data, HttpStatus.ACCEPTED);
    }


}
