package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "username")
	private String username;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "password")
	private String password;

	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "initiator")
	private String initiator;

	@Column(name = "role_id")
	private Long roleId;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id",insertable = false,updatable = false)
	private RolesEntity rolesObj;
}
