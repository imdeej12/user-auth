package com.main.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private Integer roleId;
    private String emailId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String contactNo;
    private String roleName;
    private boolean isActive;
    private String createdBy;
    private Date createdOn;
    private String modifiedBy;
    private Date modifiedOn;
    private String status;

}
