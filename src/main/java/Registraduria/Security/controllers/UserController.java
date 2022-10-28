package Registraduria.Security.controllers;

import Registraduria.Security.models.Role;
import Registraduria.Security.models.User;
import Registraduria.Security.repositories.RoleRepository;
import Registraduria.Security.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements UserDetailsService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().toString()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    // PostMapping -> Método POST
    // "" -> "/users"
    @PostMapping("")
    User createUser(@RequestBody User user, @RequestParam("roleId") Optional<String> roleId) {
        Role role = null;
        if (roleId.isPresent()) {
            role = roleRepository.findById(roleId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol no existe"));
        }
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // GetMapping -> Método GET
    // "" -> "/users"
    @GetMapping("")
    ResponseEntity<List<User>> getUsers(@RequestParam("username") Optional<String> username) {
        if (username.isPresent()) {
            return ResponseEntity.status(200).body(userRepository.findByRegexpUsername(username.get()));
        }
        return ResponseEntity.status(200).body(userRepository.findAll());
    }

    // @PathVariable("userId") -> /users/{userId}
    @GetMapping("{userId}")
    User getUserById(@PathVariable("userId") String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe"));
    }

    @DeleteMapping("{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable("userId") String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe"));
        userRepository.delete(user);
        Map<String, String> data = new HashMap<>();
        data.put("message", "Usuario " + user.getUsername() + " fue eliminado satisfactoriamente");
        return new ResponseEntity<Object>(data, HttpStatus.ACCEPTED);
    }

    @PutMapping("{userId}")
    User updateUser(@PathVariable String userId, @RequestBody User userinfo, @RequestParam("roleId") Optional<String> roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe"));
        if (userinfo.getUsername() != null) user.setUsername(userinfo.getUsername());
        if (userinfo.getEmail() != null) user.setEmail(userinfo.getEmail());
        if (roleId.isPresent()) {
            Role role = roleRepository.findById(roleId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no encontrado"));
            user.setRole(role);
        }
        return userRepository.save(user);
    }
}
