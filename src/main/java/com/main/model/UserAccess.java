package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccess extends BaseEntity {

    private String  userAccess;

    private String userAccessDescription;
}
