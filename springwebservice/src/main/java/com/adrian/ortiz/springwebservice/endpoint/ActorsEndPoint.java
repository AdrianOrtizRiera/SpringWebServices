package com.adrian.ortiz.springwebservice.endpoint;

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
import com.adrian.ortiz.springwebservice.controllers.ActorController;
import com.adrian.ortiz.springwebservice.dao.interfaces.ActorEntityService;


@Endpoint
public class ActorsEndPoint {
	
	public static final String NAMESPACE_URI = "http://www.adrian.ortiz.com/actors-ws";
	
//	private ActorEntityService service;
	
	private ActorController actorController;
	
	public ActorsEndPoint() {
		
	}
	
	@Autowired
	public ActorsEndPoint(ActorEntityService service, ActorController actorController) {
	//	this.service = service;
		this.actorController = actorController;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getActorByIdRequest")
	@ResponsePayload
	public GetActorByIdResponse getActorById(@RequestPayload GetActorByIdRequest request) {
		GetActorByIdResponse response = new GetActorByIdResponse();
		response.setActorType(this.actorController.getActorById(request.getActorId()));
		return response;
	}
	
	
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="getAllActorsRequest")
	@ResponsePayload
	public GetAllActorsResponse getAllActors(@RequestPayload GetAllActorsRequest request) {
		GetAllActorsResponse response = new GetAllActorsResponse();
		response.getActorType().addAll(this.actorController.getAllActors());
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="addActorRequest")
	@ResponsePayload
	public AddActorResponse addActor(@RequestPayload AddActorRequest request) {
		AddActorResponse response = new AddActorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		ActorType actorResult = new ActorType();
		actorResult = this.actorController.addActor(request);
		
		if(actorResult == null) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Error quan s'ha intentat afegir l'entitat.");
		} else {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("S'ha afegit l'entitat correctament.");
		}
		response.setActorType(actorResult);
		response.setServiceStatus(serviceStatus);
		
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart="updateActorRequest")
	@ResponsePayload
	public UpdateActorResponse updateActor(@RequestPayload UpdateActorRequest request) {
		
		UpdateActorResponse response = new UpdateActorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		
		Boolean result = this.actorController.updateActor(request);
		if(result == null) {
			serviceStatus.setStatusCode("NOT FOUND");
			serviceStatus.setMessage("L'actor " + request.getFirstName() + " no existeix.");
		}
		
		if(result == false) {
			serviceStatus.setStatusCode("CONFLICT");
			serviceStatus.setMessage("Error quan s'ha intentat guardarl'entitat.");
		} else {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("S'ha actualitzat l'entitat");
		}
		
		response.setServiceStatus(serviceStatus);
		return response;
	}
	
	@PayloadRoot(namespace=NAMESPACE_URI, localPart = "deleteActorRequest")
	@ResponsePayload
	public DeleteActorResponse deleteActor(@RequestPayload DeleteActorRequest request) {
		DeleteActorResponse response = new DeleteActorResponse();
		ServiceStatus serviceStatus = new ServiceStatus();
		Boolean result = this.actorController.deleteActor(request.getActorId());
		if(result) {
			serviceStatus.setStatusCode("SUCCESS");
			serviceStatus.setMessage("Esborrat correctament.");
		} else {
			serviceStatus.setStatusCode("FAIL");
			serviceStatus.setMessage("Error quan s'ha intentat esborrar.");
		}
		
		response.setServiceStatus(serviceStatus);
		return response; 
	}
}
