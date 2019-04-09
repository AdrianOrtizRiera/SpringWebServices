package com.adrian.ortiz.springwebservice.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.adrian.ortiz.gs_ws.ActorType;
import com.adrian.ortiz.gs_ws.AddActorRequest;
import com.adrian.ortiz.gs_ws.UpdateActorRequest;
import com.adrian.ortiz.springwebservice.dao.entity.ActorEntity;
import com.adrian.ortiz.springwebservice.dao.interfaces.ActorEntityService;

@Controller
public class ActorController {
	private ActorEntityService service;
	
	public ActorController() { }
	
	@Autowired
	public ActorController(ActorEntityService service) {
		this.service = service;
	}
	
	public ActorType getActorById(int id) {
		ActorEntity actorEntity = service.getEntityById(id);
		ActorType actorType = new ActorType();
		actorType.setActorId(actorEntity.getActor_id());
		actorType.setFirstName(actorEntity.getFirst_name());
		actorType.setLastName(actorEntity.getLast_name());
		actorType.setLastUpdate(actorType.getLastUpdate());
		
		return actorType;
	}
	
	public List<ActorType> getAllActors() {
		List<ActorType> actorTypeList = new ArrayList<ActorType>();
		List<ActorEntity> actorEntityList = service.getAllEntities();
		for (ActorEntity entity : actorEntityList) {
			ActorType actorType = new ActorType();
			actorType.setActorId(entity.getActor_id());
			actorType.setFirstName(entity.getFirst_name());
			actorType.setLastName(entity.getLast_name());
			actorType.setLastUpdate((Date)entity.getLast_update());
			actorTypeList.add(actorType);
		}
		
		return actorTypeList;
	}
	
	public ActorType addActor(AddActorRequest request) {
		ActorType actorType = new ActorType();
		ActorEntity actorEntity = new ActorEntity(request.getFirstName(), request.getLastName());
		actorEntity.setLast_update(new Date());
		ActorEntity saveActorEntity = this.service.addEntity(actorEntity); 
		
		if (saveActorEntity == null) return null;
	
		actorType.setActorId(saveActorEntity.getActor_id());
		actorType.setFirstName(saveActorEntity.getFirst_name());
		actorType.setLastName(saveActorEntity.getLast_name());
		actorType.setLastUpdate(saveActorEntity.getLast_update());
		return actorType;
	}
	
	public Boolean updateActor(UpdateActorRequest request) {
		ActorEntity actorResult = this.service.getEntityById(request.getActorId());
		if (actorResult == null) {
			return null;
		} 
		
		actorResult.setFirst_name(request.getFirstName());
		actorResult.setLast_name(request.getLastName());
		
		boolean flag = this.service.updateEntity(actorResult);
		if(flag == false) {
			return false;
		}
		
		return true;
	}
	
	public Boolean deleteActor(int id) {
		try {
			return this.service.deleteEntity(id);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
