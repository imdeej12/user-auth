package com.main.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.main.model.*;
import com.main.repo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.common.AESEncryption;
import com.main.common.DataContainer;
import com.main.common.GenerateCaptcha;
import com.main.common.GlobalConstant;
import com.main.config.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	public UserMappingWithDepartmentRepo userMappingWithDepartmentRepo;


	@Autowired
	GenerateCaptcha generateCaptcha;

	@Autowired
	AESEncryption aesEncryption;

	@Autowired
	AuditReportRepo auditReportRepo;

	@Autowired
	UserMapDetailsRepo userMapDetailsRepo;

	@Autowired
	UserSessionRepo userSessionRepo;

	@Autowired
	UserMappingWithAuthorizationGroupRepo userMappingWithAuthorizationGroupRepo;

	@Autowired
	UserMappingWIthCompanyCodeRepo userMappingWIthCompanyCodeRepo;

	@Autowired
	UserMappingWithBusinessAreaRepo userMappingWithBusinessAreaRepo;

	@Autowired
	UserMappingWithUserAccessRepo userMappingWithUserAccessRepo;



	@Value("${crossOrigin}")
	public String crossOrigin;

	@Value("${requiredKeyForDetailAtLoginTime}")
	public String requiredKeyForDetailAtLoginTime;

	private static final Logger logger = LogManager.getLogger(JwtAuthenticationController.class);
	@Autowired
	private UserMappingWithPurchasingRepo userMappingWithPurchasingRepo;

	@CrossOrigin(origins = "${crossOrigin}")
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
		try {
			// Decrypt CSRF Token
			logger.info("encryCRSFToken: {}", authenticationRequest.getCsrft());
			String decryptedCsrf = aesEncryption.decrypt2(authenticationRequest.getCsrft());
			logger.info("decryptCRSFToken: {}", decryptedCsrf);

			// Validate CSRF Token
			if (authenticationRequest.getCsrf() == null || authenticationRequest.getCsrft() == null
					|| authenticationRequest.getCsrf().isEmpty() || authenticationRequest.getCsrft().isEmpty()
					|| !authenticationRequest.getCsrf().equals(decryptedCsrf)) {
				return new ResponseEntity<>(GlobalConstant.INVALID_CSRF, HttpStatus.BAD_REQUEST);
			}

			// Validate Captcha
			String decryptedCaptcha = aesEncryption.decrypt2(authenticationRequest.getEncCaptcha());
			if (!authenticationRequest.getCaptchaInput().equals(decryptedCaptcha)) {
				return ResponseEntity.ok(new JwtResponse(GlobalConstant.INVALID_CAPTCHA, GlobalConstant.MSG_FAILED, authenticationRequest.getLoginFlag()));
			}

			// Authenticate User
			User authenticatedUser = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

			// Generate JWT Token
			final String token = jwtTokenUtil.generateToken(authenticatedUser);
			logger.info("jwtToken: {}", token);

			// Collect Department Information
			List<UserMappingWithDepartment> departmentList = userMappingWithDepartmentRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String depIds = departmentList.stream().map(UserMappingWithDepartment::getDepartmentId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String depNameLists = departmentList.stream().map(UserMappingWithDepartment::getDepartmentName).distinct().collect(Collectors.joining(","));

			// Collect Company Code Information
			List<UserMappingWithCompanyCode> companyCodeList = userMappingWIthCompanyCodeRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String companyCodeIds = companyCodeList.stream().map(UserMappingWithCompanyCode::getCompanyCodeId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String companyCodeLists = companyCodeList.stream().map(UserMappingWithCompanyCode::getCompanyCode).distinct().collect(Collectors.joining(","));

			// Collect Authorization Group Information
			List<UserMappingWithAuthorizationGroup> authorizationList = userMappingWithAuthorizationGroupRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedAuthIds = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getAuthorizationGroupId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedAuthName = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getAuthorizationGroup).distinct().collect(Collectors.joining(","));
			String commaSeparatedPurchaseIds="";
			String commaSeparatedPurchase="";
			try {
				// Collect Purchasing Group Information
				List<UserMappingWithPurchasing> purchasingListList = userMappingWithPurchasingRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
				commaSeparatedPurchaseIds = purchasingListList.stream().map(UserMappingWithPurchasing::getPurchasingId).distinct().map(String::valueOf).collect(Collectors.joining(","));
				commaSeparatedPurchase = purchasingListList.stream().map(UserMappingWithPurchasing::getPurchasing).distinct().collect(Collectors.joining(","));
			}
			catch(Exception e){
				System.out.println(e);
			}
			// Collect Business Area Information
			List<UserMappingWithBusinessArea> businessAreasList = userMappingWithBusinessAreaRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedBusAreaIds = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessAreaId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedBusAreaNames = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessArea).distinct().collect(Collectors.joining(","));

			// Collect User Access Information
			List<UserMappingWithUserAccess> userAccessList = userMappingWithUserAccessRepo.findByUserNameAndStatus(authenticatedUser.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedAccessIds = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccessId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedAccessNames = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccess).distinct().collect(Collectors.joining(","));

			// Audit Report
			AuditReport auditReport = new AuditReport();
			auditReport.setUserName(authenticatedUser.getUsername());
			auditReport.setRole(authenticatedUser.getRolesObj().getRoleName());
			auditReport.setActionType(GlobalConstant.Login_success);
			auditReport.setAuthGroupIdList(commaSeparatedAuthIds);
			auditReport.setAuthGroupNameList(commaSeparatedAuthName);
			auditReport.setCompanyCodeIdList(companyCodeIds);
			auditReport.setCompanyCodeNameList(companyCodeLists);
			auditReport.setDepIdList(depIds);
			auditReport.setDepNameList(depNameLists);
			auditReport.setDateTime(new Date());
			auditReport.setInitiator(authenticatedUser.getInitiator());
			auditReport.setBusAreaIdList(commaSeparatedBusAreaIds);
			auditReport.setBusAreaNameList(commaSeparatedBusAreaNames);
			auditReport.setUserAccessIdList(commaSeparatedAccessIds);
			auditReport.setUserAccessNameList(commaSeparatedAccessNames);
			auditReport.setPurchasingId(commaSeparatedPurchaseIds);
			auditReport.setPurchasingNameList(commaSeparatedPurchase);
			auditReportRepo.save(auditReport);

			return ResponseEntity.ok(new JwtResponse(token, GlobalConstant.MSG_SUCCESS, ""));
		} catch (DisabledException e) {
			logger.info(GlobalConstant.USER_DISABLED);
			return ResponseEntity.ok(new JwtResponse(GlobalConstant.INVALID_CREDENTIALS, GlobalConstant.MSG_FAILED, ""));
		} catch (BadCredentialsException e) {
			logger.info("INVALID_CREDENTIALS: {}", HttpStatus.FORBIDDEN);
			return ResponseEntity.ok(new JwtResponse(GlobalConstant.INVALID_CREDENTIALS, GlobalConstant.MSG_FAILED, ""));
		} catch (Exception e) {
			logger.info("Exception: {}", e.toString());
			if ("USER_NOT_ACTIVE".equalsIgnoreCase(e.getMessage())) {
				CredentialResponse credRes = new CredentialResponse();
				credRes.setError("User not active");
				credRes.setStatus(400);
				credRes.setMessage("User not active");
				return new ResponseEntity<>(credRes, HttpStatus.BAD_REQUEST);
			}
			return ResponseEntity.ok(new JwtResponse(GlobalConstant.INVALID_CREDENTIALS, GlobalConstant.MSG_FAILED, ""));
		}
	}

	@CrossOrigin(origins = "${crossOrigin}")
	@RequestMapping(value = "/invalidateToken", method = RequestMethod.POST)
	public ResponseEntity<?> invalidateToken(Principal principal) throws Exception {
		CredentialResponse credRes = new CredentialResponse();
		return new ResponseEntity<>("{\"status\":\"success\"}", HttpStatus.ACCEPTED);
	}

	private User authenticate(String username, String password) throws Exception {
		User user = null;
		List<String> statusList = new ArrayList<>();
		statusList.add(GlobalConstant.ACTIVE_STATUS);
		statusList.add(GlobalConstant.PASSWORD_CHANGE_STATUS);
		user = userRepo.findByUsernameAndStatusIn(username, statusList);
		if (user == null) {
			logger.info("User not found with username: {}", username);
			throw new UsernameNotFoundException(
					"Either User is Disabled or not present in the system with username: " + username);
		}

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		return user;

	}

	@CrossOrigin(origins = "${crossOrigin}")
	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		logger.info("claims  {}", request.getAttribute("claims"));
		if (null == request.getAttribute("claims")) {
			return new ResponseEntity<>(GlobalConstant.TOKEN_EXPIRED, HttpStatus.BAD_REQUEST);
		}
		DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

		ObjectMapper mapper = new ObjectMapper();
		Claims allClaimsFromToken = jwtTokenUtil.getAllClaimsFromToken(token);
		JWTUserDetails userDetails = mapper.convertValue(allClaimsFromToken.get(GlobalConstant.JWTTOKEN_CLAIM),
				JWTUserDetails.class);

		logger.info("jwtToken customClaimValue:- {}", userDetails);
		Date expirationTime = allClaimsFromToken.getExpiration();
		logger.info("jwtToken expirationTime----------------{}", expirationTime);


		return ResponseEntity.ok(new JwtResponse(token, GlobalConstant.MSG_SUCCESS, ""));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	@CrossOrigin(origins = "${crossOrigin}")
	@PostMapping(value = "/detailAtLoginTime")
	public ResponseEntity<?> detailAtLoginTime(
			@RequestHeader(value = "Key-for-Authenticate-Api", required = true) String requiredKey,
			HttpServletRequest request, Model model) {
		DataContainer data = new DataContainer();
		try {

			if (requiredKey.equalsIgnoreCase(requiredKeyForDetailAtLoginTime)) {

				DataContainer captcha = generateCaptcha.doGetCaptcha();

				UUID uuid = UUID.randomUUID();
				model.addAttribute("csrf", uuid.toString());
				logger.info("uuid   {}", uuid.toString());
				String encCscfToken = aesEncryption.encrypt(uuid.toString());
				logger.info("enuuid   {}", encCscfToken);
				JwtRequest jwtDetails = new JwtRequest();
				jwtDetails.setCsrf(uuid.toString());
				jwtDetails.setEncCaptcha(captcha.getCaptcha());
				jwtDetails.setCsrft(encCscfToken);
				jwtDetails.setCaptchaImg(captcha.getData());
				data.setData(jwtDetails);

				data.setMsg(GlobalConstant.MSG_SUCCESS);

			} else {
				data.setMsg(GlobalConstant.MSG_ERROR);
			}

		} catch (Exception e) {
			data.setMsg(GlobalConstant.MSG_ERROR);
			logger.info("Exceptions {}", e.toString());
			return ResponseEntity.ok(data);

		}
		return ResponseEntity.ok(data);

	}

	@CrossOrigin(origins = "${crossOrigin}")
	@PostMapping(value = "/logoutDetail")
	public ResponseEntity<?> logoutDetail(
			@RequestHeader(value = "Authorization", required = true) String authorizationToken,
			HttpServletRequest request, Model model) {

		DataContainer data = new DataContainer();

		try {
			// Extract and log the pure JWT token
			authorizationToken = jwtTokenUtil.getPureJWTToken(authorizationToken);
			logger.info("Pure JWT Token in logoutDetail: {}", authorizationToken);

			// Get user details from the token
			Claims allClaimsFromToken = jwtTokenUtil.getAllClaimsFromToken(authorizationToken);
			ObjectMapper mapper = new ObjectMapper();
			JWTUserDetails userDetails = mapper.convertValue(allClaimsFromToken.get(GlobalConstant.JWTTOKEN_CLAIM), JWTUserDetails.class);

			// Collect department information
			List<UserMappingWithDepartment> departmentList = userMappingWithDepartmentRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String depIds = departmentList.stream().map(UserMappingWithDepartment::getDepartmentId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String depNameLists = departmentList.stream().map(UserMappingWithDepartment::getDepartmentName).distinct().collect(Collectors.joining(","));

			// Collect company code information
			List<UserMappingWithCompanyCode> companyCodeList = userMappingWIthCompanyCodeRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String companyCodeIds = companyCodeList.stream().map(UserMappingWithCompanyCode::getId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String companyCodeLists = companyCodeList.stream().map(UserMappingWithCompanyCode::getCompanyCode).distinct().collect(Collectors.joining(","));

			// Collect authorization group information
			List<UserMappingWithAuthorizationGroup> authorizationList = userMappingWithAuthorizationGroupRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedAuthIds = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedAuthName = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getAuthorizationGroup).distinct().collect(Collectors.joining(","));

			// Collect Business Area Information
			List<UserMappingWithBusinessArea> businessAreasList = userMappingWithBusinessAreaRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedBusAreaIds = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessAreaId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedBusAreaNames = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessArea).distinct().collect(Collectors.joining(","));

			// Collect User Access Information
			List<UserMappingWithUserAccess> userAccessList = userMappingWithUserAccessRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedAccessIds = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccessId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedAccessNames = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccess).distinct().collect(Collectors.joining(","));

			// Collect Purchasing Group Information
			List<UserMappingWithPurchasing> purchasingListList = userMappingWithPurchasingRepo.findByUserNameAndStatus(userDetails.getUsername(), GlobalConstant.ACTIVE_STATUS);
			String commaSeparatedPurchaseIds = purchasingListList.stream().map(UserMappingWithPurchasing::getPurchasingId).distinct().map(String::valueOf).collect(Collectors.joining(","));
			String commaSeparatedPurchase = purchasingListList.stream().map(UserMappingWithPurchasing::getPurchasing).distinct().collect(Collectors.joining(","));

			// Audit Report
			AuditReport auditReport = new AuditReport();
			auditReport.setUserName(userDetails.getUsername());
			auditReport.setRole(userDetails.getRole());
			auditReport.setActionType(GlobalConstant.Logout_success);
			auditReport.setAuthGroupIdList(commaSeparatedAuthIds);
			auditReport.setAuthGroupNameList(commaSeparatedAuthName);
			auditReport.setCompanyCodeIdList(companyCodeIds);
			auditReport.setCompanyCodeNameList(companyCodeLists);
			auditReport.setDepIdList(depIds);
			auditReport.setDepNameList(depNameLists);
			auditReport.setDateTime(new Date());
			auditReport.setInitiator(userDetails.getInitiator());
			auditReport.setBusAreaIdList(commaSeparatedBusAreaIds);
			auditReport.setBusAreaNameList(commaSeparatedBusAreaNames);
			auditReport.setUserAccessIdList(commaSeparatedAccessIds);
			auditReport.setUserAccessNameList(commaSeparatedAccessNames);
			auditReport.setPurchasingId(commaSeparatedPurchaseIds);
			auditReport.setPurchasingNameList(commaSeparatedPurchase);
			auditReportRepo.save(auditReport);

			// Set success message
			data.setMsg(GlobalConstant.MSG_SUCCESS);

			// Delete user session if it exists and matches the token
			Optional<UserSession> userSessionOptnl = userSessionRepo.findByUserName(userDetails.getUsername());
			if (userSessionOptnl.isPresent() && userSessionOptnl.get().getJwtToken().equals(authorizationToken)) {
				userSessionRepo.delete(userSessionOptnl.get());
			}

		} catch (Exception e) {
			// Log exception and set error message
			data.setMsg(GlobalConstant.MSG_ERROR);
			logger.info("Exception: {}", e.toString());
			return ResponseEntity.ok(data);
		}

		return ResponseEntity.ok(data);
	}


	public Date getExpirationTime(String token) {
		try {
			Claims claims = Jwts.parser()
					.parseClaimsJws(token)
					.getBody();

			// Get the expiration time as a Date object

			return claims.getExpiration();
		} catch (Exception e) {
			// Handle any exceptions here, such as token validation failures.
			return null;
		}
	}

	@CrossOrigin(origins = "${crossOrigin}")
	@PostMapping(value = "/validateUser")
	public ResponseEntity<?> validateUser(@RequestBody UserSession userSession) {

		Long count = userSessionRepo.countByUserNameAndJwtToken(userSession.getUserName(), userSession.getJwtToken());
		if (count > 0) {
			return ResponseEntity.ok(new JwtResponse(GlobalConstant.FALSE, GlobalConstant.FALSE, ""));

		} else {
			return ResponseEntity.ok(new JwtResponse(GlobalConstant.TRUE, GlobalConstant.TRUE, ""));

		}

	}


}
