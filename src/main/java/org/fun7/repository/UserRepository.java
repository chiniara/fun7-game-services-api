package org.fun7.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.fun7.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
