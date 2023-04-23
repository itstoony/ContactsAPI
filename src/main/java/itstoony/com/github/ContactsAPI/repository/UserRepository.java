package itstoony.com.github.ContactsAPI.repository;

import itstoony.com.github.ContactsAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login);

}
