package com.adrian.ortiz.springwebservice.endpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


import com.adrian.ortiz.gs_ws.ActorType;
import com.adrian.ortiz.gs_ws.AddActorRequest;
import com.adrian.ortiz.gs_ws.AddActorResponse;
import com.adrian.ortiz.gs_ws.DeleteActorRequest;
import com.adrian.ortiz.gs_ws.DeleteActorResponse;
import com.adrian.ortiz.gs_ws.GetActorByIdRequest;
import com.adrian.ortiz.gs_ws.GetActorByIdResponse;
import com.adrian.ortiz.gs_ws.GetAllActorsRequest;
import com.adrian.ortiz.gs_ws.GetAllActorsResponse;
import com.adrian.ortiz.gs_ws.ServiceStatus;
import com.adrian.ortiz.gs_ws.UpdateActorRequest;
import com.adrian.ortiz.gs_ws.UpdateActorResponse;
import com.adrian.ortiz.springwebservice.dao.entity.ActorEntity;
import com.adrian.ortiz.springwebservice.dao.interfaces.ActorEntityService;


@Endpoint
public class ActorsEndPoint {
	
	public static final String NAMESPACE_URI = "http://adrian.ortiz.com/actors-ws";
	
	private ActorEntityService service;
	
	public ActorsEndPoint() {
		
	}
	
	@Autowired
	public ActorsEndPoint(ActorEntityService service) {
		this.service = service;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getActorByIdRequest")
	@ResponsePayload
	public GetActorByIdResponse getActorById(@RequestPayload GetActorByIdRequest request) {
		GetActorByIdResponse response = new GetActorByIdResponse();
		ActorEntity actorEntity = service.getEntityById(request.getActorId());
		ActorType actorType = new ActorType();
		actorType.setActorId(actorEntity.getActor_id());
		actorType.setFirstName(actorEntity.getFirst_name());
		actorType.setLastName(actorEntity.getLast_name());
		actorType.setLastUpdate(actorType.getLastUpdate());
		response.setActorType(actorType);
		return response;
	}
	
	
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getAllActorsRequest")
	@ResponsePayload
	public GetAllActorsResponse getAllActors(@RequestPayload GetAllActorsRequest request) {
		GetAllActorsResponse response = new GetAllActorsResponse();
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
		
		response.getActorType().addAll(actorTypeList);
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="addActorRequest")
	@ResponsePayload
	public AddActorResponse addActor(@RequestPayload AddActorRequest request) {
		AddActorResponse response = new AddActorResponse();
		ActorType actorType = new ActorType();
		ServiceStatus serviceStatus = new ServiceStatus();
		
		ActorEntity actorEntity = new ActorEntity(request.getFirstName(), request.getLastName());
		ActorEntity saveActorEntity = this.service.addEntity(actorEntity); 
		
		if (saveActorEntity == null) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Error quan s'ha intentat afegir l'entitat.");
		} else {
			actorType.setActorId(saveActorEntity.getActor_id());
			actorType.setFirstName(saveActorEntity.getFirst_name());
			actorType.setLastName(saveActorEntity.getLast_name());
			actorType.setLastUpdate(saveActorEntity.getLast_update());
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("S'ha afegit l'entitat correctament.");
		}
		
		response.setActorType(actorType);
		response.setServiceStatus(serviceStatus);
		
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="updateActorRequest")
	@ResponsePayload
	public UpdateActorResponse updateActor(@RequestPayload UpdateActorRequest request) {
		
		UpdateActorResponse response = new UpdateActorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		
		ActorEntity actorResult = this.service.getEntityById(request.getActorId());
		if (actorResult == null) {
			serviceStatus.setStatusCode("NOT FOUND");
			serviceStatus.setMessage("L'actor " + request.getFirstName() + " no existeix.");
			response.setServiceStatus(serviceStatus);
			return response;
		} 
		
		actorResult.setFirst_name(request.getFirstName());
		actorResult.setLast_name(request.getLastName());
		
		boolean flag = this.service.updateEntity(actorResult);
		if(flag == false) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Error quan s'ha intentat guardarl'entitat.");
			response.setServiceStatus(serviceStatus);
			return response;
		}

		serviceStatus.setStatusCode("SUCCESS");
		serviceStatus.setMessage("S'ha actualitzat l'entitat");
		response.setServiceStatus(serviceStatus);
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart = "deleteActorRequest")
	public DeleteActorResponse deleteActor(@RequestPayload DeleteActorRequest request) {
		DeleteActorResponse response = new DeleteActorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		
		boolean flag = this.service.deleteEntity(request.getActorId());
		if (!flag ) {
			serviceStatus.setStatusCode("FAIL");
			serviceStatus.setMessage("Error quan s'ha intentat esborrar.");
		} else {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Esborrat correctament.");
		}
		
		response.setServiceStatus(serviceStatus);
		return response; 
		
	}
}
