package com.adrian.ortiz.springwebservice.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="actor")
public class ActorEntity implements Serializable {
	
	private static final long serialVersionUID = 7694997433915109752L;
	
	@Id
	@GeneratedValue
	private int actor_id;
	private String first_name;
	private String last_name;
	@GeneratedValue
	private Date last_update;
	
	public ActorEntity() {
		
	}
	
	public ActorEntity(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	public int getActor_id() {
		return actor_id;
	}
	public void setActor_id(int actor_id) {
		this.actor_id = actor_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public Date getLast_update() {
		return last_update;
	}
	public void setLast_update(Date last_update) {
		this.last_update = last_update;
	}

}
