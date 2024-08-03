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
@Table(name = "user_mapping_with_company_code")
public class UserMappingWithCompanyCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

//    @Column(name = "user_id")
//    private Long userId;
    private String  userName;

    private Long companyCodeId;

    private String companyCode;

    private String companyCodeDescription;
    @Column(name = "user_id")
    private Long userId;






}
