package dev.backend.authservice.repository;


import org.springframework.data.repository.CrudRepository;
import dev.backend.authservice.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String name);
}
