package Registraduria.Security.repositories;

import Registraduria.Security.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findUserByDocumento(String documento);

    @Query("{'username': {'$regex': ?0, '$options': 'i' }}")
    List<User> findByRegexpUsername(String username);

    User findByUsername(String username);
}
