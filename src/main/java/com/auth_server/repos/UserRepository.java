package com.auth_server.repos;

import com.auth_server.entity.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllByRolesId(Long id);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByPasswordIgnoreCase(String password);

}
