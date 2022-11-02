package Registraduria.Security.repositories;

import Registraduria.Security.models.PermissionRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionRoleRepository extends MongoRepository<PermissionRole,String> {
}
