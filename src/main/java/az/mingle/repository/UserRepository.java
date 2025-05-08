package az.mingle.repository;

import az.mingle.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long>  {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
           "(:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:surname IS NULL OR LOWER(u.surname) LIKE LOWER(CONCAT('%', :surname, '%'))) AND " +
           "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))")
    Page<User> search(@Param("name") String name,
                      @Param("surname") String surname,
                      @Param("username") String username,
                      Pageable pageable);
}
