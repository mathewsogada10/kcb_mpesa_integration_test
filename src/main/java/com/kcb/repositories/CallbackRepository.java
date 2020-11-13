package com.kcb.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kcb.model.CallbackUrl;

public interface CallbackRepository extends CrudRepository<CallbackUrl, Long>{

}
