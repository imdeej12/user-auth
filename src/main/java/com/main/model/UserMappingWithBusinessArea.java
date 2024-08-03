package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMappingWithBusinessArea extends BaseEntity {
	private static final long serialVersionUID = 1L;

//	private Long userId;

	private String  userName;

	private Long  businessAreaId;

	private String  businessArea;

	private String businessAreaDescription;

	@Column(name = "user_id")
	private Long userId;


}


