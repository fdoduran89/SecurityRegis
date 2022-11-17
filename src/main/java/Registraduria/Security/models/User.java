package Registraduria.Security.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data//Reemplaza los set y get
@Document
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String nombres;
    private String apellidos;
    @Indexed(unique = true)
    private String documento;
    private String password;
    @Indexed(unique = true)
    private String email;
    @DBRef
    private Role role;

    public User(
            String id,
            String username,
            String nombres,
            String apellidos,
            String documento,
            String password,
            String email,
            Role role
    ){
        this.id =id;
        this.username = username;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}
