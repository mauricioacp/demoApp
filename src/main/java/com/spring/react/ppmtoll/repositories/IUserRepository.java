package com.spring.react.ppmtoll.repositories;

import com.spring.react.ppmtoll.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User,Long> {
	User findByUsername(String username);
	User getById(Long id);

}
