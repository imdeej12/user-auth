package com.main.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_map_details")
public class UserMapDetails extends BaseEntity{

	
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id",  insertable = false, updatable = false)
	private User users;
	
	
	@ManyToOne(targetEntity = Department.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_id", referencedColumnName = "id",  insertable = false, updatable = false)
	private Department departmentMdm;
	
	@ManyToOne(targetEntity = Designation.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Designation designationMdm;
	
	@ManyToOne(targetEntity = ProjectEntity.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
	private ProjectEntity projectMdmEntity;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "designation_id")
	private Long designationId;
	
	@Column(name = "project_id")
	private Long projectId;



}
