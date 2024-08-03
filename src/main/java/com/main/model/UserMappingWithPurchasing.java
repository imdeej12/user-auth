package com.main.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMappingWithPurchasing extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String  userName;
    private Long purchasingId;
    private String purchasing;
    private String purchasingDescription;
    @Column(name = "user_id")
    private Long userId;


}


