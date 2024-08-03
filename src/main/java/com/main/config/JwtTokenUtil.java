package com.main.config;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.main.model.*;
import com.main.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.main.common.GlobalConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    @Autowired
    UserMappingWithDepartmentRepo userMappingWithDepartmentRepo;
    @Autowired
    UserMappingWithAuthorizationGroupRepo userMappingWithAuthorizationGroupRepo;
    @Autowired
    UserMappingWIthCompanyCodeRepo userMappingWIthCompanyCodeRepo;
    @Autowired
    UserMappingWithBusinessAreaRepo userMappingWithBusinessAreaRepo;
    @Autowired
    UserMappingWithUserAccessRepo userMappingWithUserAccessRepo;
    @Autowired
    UserMappingWithPurchasingRepo userMappingWithPurchasingRepo;
    private int jwtTokenValidity;
    @Value("${jwt.jwtTokenExpiryInMs}")
    public void setjwtTokenExpiryInMs(int jwtTokenValidity) {
        this.jwtTokenValidity = jwtTokenValidity;
    }
    private int refreshExpirationDateInMs;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refreshExpirationDateInMs}")
    public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
        this.refreshExpirationDateInMs = refreshExpirationDateInMs;
    }
    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    // Generate token for user
    public String generateToken(User authenticatedUserObj) {
        Map<String, Object> claims = new HashMap<>();
        JWTUserDetails jwtUserDetails = new JWTUserDetails();

        // Fetch and process department information
        List<UserMappingWithDepartment> departmentList = userMappingWithDepartmentRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
        List<Long> depIdList = departmentList.stream().map(UserMappingWithDepartment::getDepartmentId).distinct().collect(Collectors.toList());
        List<String> depNameList = departmentList.stream().map(UserMappingWithDepartment::getDepartmentName).distinct().collect(Collectors.toList());

        // Fetch and process company code information
        List<UserMappingWithCompanyCode> companyCodeList = userMappingWIthCompanyCodeRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
        List<Long> companyCodeIdList = companyCodeList.stream().map(UserMappingWithCompanyCode::getCompanyCodeId).distinct().collect(Collectors.toList());
        List<String> companyCodeNameList = companyCodeList.stream().map(UserMappingWithCompanyCode::getCompanyCode).distinct().collect(Collectors.toList());

        // Fetch and process authorization group information
        List<UserMappingWithAuthorizationGroup> authorizationList = userMappingWithAuthorizationGroupRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
        List<Long> authIdList = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getAuthorizationGroupId).distinct().collect(Collectors.toList());
        List<String> authGroupCodeList = authorizationList.stream().map(UserMappingWithAuthorizationGroup::getAuthorizationGroup).distinct().collect(Collectors.toList());

        // Fetch and process business area information
        List<UserMappingWithBusinessArea> businessAreasList = userMappingWithBusinessAreaRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
        List<Long> busAreaIdList = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessAreaId).distinct().collect(Collectors.toList());
        List<String> busAreaNameList = businessAreasList.stream().map(UserMappingWithBusinessArea::getBusinessArea).distinct().collect(Collectors.toList());

        // Fetch and process user access information
        List<UserMappingWithUserAccess> userAccessList = userMappingWithUserAccessRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
        List<Long> userAccessIdList = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccessId).distinct().collect(Collectors.toList());
        List<String> userAccessNameList = userAccessList.stream().map(UserMappingWithUserAccess::getUserAccess).distinct().collect(Collectors.toList());

        List<Long> purchasingIdList = new ArrayList<>();
        List<String> purchasingCodeList = new ArrayList<>();
        try {
            // Fetch and process user access information
            List<UserMappingWithPurchasing> purchasingList = userMappingWithPurchasingRepo.findByUserNameAndStatus(authenticatedUserObj.getUsername(), GlobalConstant.ACTIVE_STATUS);
           purchasingIdList = purchasingList.stream().map(UserMappingWithPurchasing::getPurchasingId).distinct().collect(Collectors.toList());
            purchasingCodeList = purchasingList.stream().map(UserMappingWithPurchasing::getPurchasing).distinct().collect(Collectors.toList());
        }
        catch(Exception e){
            System.out.println(e);
        }

        // Set JWT user details
        jwtUserDetails.setRole(authenticatedUserObj.getRolesObj().getRoleName());
        jwtUserDetails.setRoleId(authenticatedUserObj.getRolesObj().getId());
        jwtUserDetails.setCompanyCodeId(companyCodeIdList);
        jwtUserDetails.setCompanyCode(companyCodeNameList);
        jwtUserDetails.setAuthorizationGroupId(authIdList);
        jwtUserDetails.setAuthorizationGroupCode(authGroupCodeList);
        jwtUserDetails.setDepListId(depIdList);
        jwtUserDetails.setDepName(depNameList);
        jwtUserDetails.setBusinessAreaCode(busAreaNameList);
        jwtUserDetails.setBusinessAreaId(busAreaIdList);
        jwtUserDetails.setUserAccessIdList(userAccessIdList);
        jwtUserDetails.setUserAccessNameList(userAccessNameList);
        jwtUserDetails.setInitiator(authenticatedUserObj.getInitiator());
        jwtUserDetails.setUsername(authenticatedUserObj.getUsername());
        jwtUserDetails.setUserId(authenticatedUserObj.getId());
        jwtUserDetails.setPurchasingId(purchasingIdList);
        jwtUserDetails.setPurchasingCode(purchasingCodeList);

        // Add user details to claims
        claims.put(GlobalConstant.JWTTOKEN_CLAIM, jwtUserDetails);

        // Generate and return the token
        return doGenerateToken(claims, authenticatedUserObj.getUsername());
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String getPureJWTToken(String token) {
        return token.substring(7);
    }
}