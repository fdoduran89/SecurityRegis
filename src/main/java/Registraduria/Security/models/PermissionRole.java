package Registraduria.Security.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
    @Data
    @Document
    public class PermissionRole {
        @Id
        private String id;
        @DBRef
        private Role role;
        @DBRef
        private Permission permission;

        public PermissionRole(Role role, Permission permission) {
            this.role = role;
            this.permission = permission;
        }

    }
