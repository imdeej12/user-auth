package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthorizationGroup extends BaseEntity {
	private static final long serialVersionUID = 1L;


	private String authorizationGroupCode;

	private String authorizationGroupDescription;


}


