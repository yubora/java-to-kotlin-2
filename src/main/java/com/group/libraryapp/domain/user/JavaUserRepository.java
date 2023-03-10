package com.group.libraryapp.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Deprecated
public interface JavaUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByName(String name);

}
