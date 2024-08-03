package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_mapping_with_department")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserMappingWithDepartment extends BaseEntity {

    private Long departmentId;
    private String departmentName;
    private String  userName;
    @Column(name = "user_id")
    private Long userId;


}
