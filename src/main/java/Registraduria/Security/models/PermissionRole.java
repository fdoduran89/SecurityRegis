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


        public PermissionRole() {

        }


        public Role getRole(){
            return role;
        }

        public Permission getPermission() {
            return permission;
        }

        public void setRole(Role role){
            this.role = role;
        }
        public void setPermission (Permission permission){
            this.permission = permission;
        }

    }
