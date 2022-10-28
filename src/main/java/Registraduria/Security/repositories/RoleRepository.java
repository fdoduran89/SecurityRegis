package Registraduria.Security.repositories;

import Registraduria.Security.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository <Role, String>{
}
