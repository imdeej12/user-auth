package com.main.model;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "audit_report")
@Data
public class AuditReport extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "role", columnDefinition = "TEXT")
    private String role;
    @Column(name = "dep_id_list", columnDefinition = "TEXT")
    private String depIdList;
    @Column(name = "dep_name_List", columnDefinition = "TEXT")
    private String depNameList;
    @Column(name = "company_code_id_list")
    private String companyCodeIdList;
    @Column(name = "company_code_name_list", columnDefinition = "TEXT")
    private String companyCodeNameList;
    @Column(name = "auth_group_id_list")
    private String authGroupIdList;
    @Column(name = "auth_group_name_list", columnDefinition = "TEXT")
    private String authGroupNameList;
    @Column(name = "action_type", columnDefinition = "TEXT")
    private String actionType;
    @Column(name = "date_time")
    private Date dateTime;
    @Column(name = "initiator")
    private String initiator;
    @Column(name = "buss_area_id_list", columnDefinition = "TEXT")
    private String busAreaIdList;
    @Column(name = "buss_area_name_list", columnDefinition = "TEXT")
    private String busAreaNameList;
    @Column(name = "user_access_id_list", columnDefinition = "TEXT")
    private String userAccessIdList;
    @Column(name = "user_access_name_list", columnDefinition = "TEXT")
    private String userAccessNameList;
    @Column(name = "purchasing_id_list", columnDefinition = "TEXT")
    private String purchasingId;
    @Column(name = "purchasing_name_list", columnDefinition = "TEXT")
    private String purchasingNameList;
}