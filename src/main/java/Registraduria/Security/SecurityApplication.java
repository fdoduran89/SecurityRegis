package Registraduria.Security;

import Registraduria.Security.models.User;
import Registraduria.Security.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


    private void withMongoAndQuery(UserRepository repository, MongoTemplate mongoTemplate, User user, String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<User> users = mongoTemplate.find(query, User.class);

        if (users.size() > 1) {
            throw new IllegalStateException("Se encontraron varios usuarios con el email " + email);
        }
        if (users.isEmpty()) {
            repository.insert(user);
        } else {
            System.out.println(user + " ya existe");
        }
    }

}
