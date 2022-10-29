package Registraduria.Security.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Permission {
    @Id
    private String id;
    private String url; // url -> "/users"
    private String method; // method -> "post"

    public Permission(String url, String method) {
        this.url = url;
        this.method = method;
    }
}
