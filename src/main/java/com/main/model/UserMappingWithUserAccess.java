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
@Table(name = "user_mapping_with_user_access")
public class UserMappingWithUserAccess extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;
    private String  userName;
    private Long userAccessId;
    private String userAccess;
    private String userAccessDescription;


}