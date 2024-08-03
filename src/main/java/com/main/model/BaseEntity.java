package com.main.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	protected static final String PK = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private Date createdOn;

	private String modifiedBy;

	@UpdateTimestamp
	private Date modifiedOn;

	private String status;


}
