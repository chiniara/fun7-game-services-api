package org.fun7.service;

import org.fun7.model.User;
import org.fun7.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.LockModeType;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository repository;

    public List<User> listAllUsers() {
        return repository.listAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id);
    }

    public User getUserByIdWithLock(Long id) {
        return repository.findById(id, LockModeType.PESSIMISTIC_WRITE);
    }

    public User createUser(User user) {
        repository.persist(user);
        return user;
    }

    public boolean removeUser(Long userId) {
        return repository.deleteById(userId);
    }

}
