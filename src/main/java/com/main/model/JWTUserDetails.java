package com.main.model;

import java.util.List;

import lombok.Data;

@Data
public class JWTUserDetails {
	private Long roleId;
	private String role;
	private Long userId;
	private String username;
	private String initiator;
	private List<Long> companyCodeId;
	private List<String> companyCode;
	private List<Long> depListId;
	private List<String> depName;
	private List<Long> authorizationGroupId;
	private List<String> authorizationGroupCode;
	private List<Long> businessAreaId;
	private List<String> businessAreaCode;
	private List<Long> userAccessIdList;
	private List<String> userAccessNameList;

}
