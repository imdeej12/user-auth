package com.main.model;

import javax.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolesEntity extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "rolesObj", cascade = CascadeType.ALL)
    private List<User> users;
}