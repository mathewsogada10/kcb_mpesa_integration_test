package com.kcb.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kcb.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	User findByUsername(String username);
}
