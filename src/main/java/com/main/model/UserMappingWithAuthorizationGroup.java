package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_mapping_with_authorization_group")
public class UserMappingWithAuthorizationGroup extends BaseEntity {

//    private Long userId;
    private String  userName;
    private Long authorizationGroupId;
    private String authorizationGroup;
    private String authorizationGroupDescription;

    @Column(name = "user_id")
    private Long userId;



}