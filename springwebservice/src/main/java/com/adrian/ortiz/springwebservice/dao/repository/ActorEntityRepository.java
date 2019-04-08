package com.adrian.ortiz.springwebservice.dao.repository;

import com.adrian.ortiz.springwebservice.dao.entity.ActorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorEntityRepository extends CrudRepository<ActorEntity, Integer> {
	
}
