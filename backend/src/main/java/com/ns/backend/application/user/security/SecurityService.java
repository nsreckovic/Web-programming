package com.ns.backend.application.user.security;

import com.ns.backend.application.user.models.User;
import com.ns.backend.application.user.models.UserType;
import com.ns.backend.application.user.models.response.UserResponseDto;
import com.ns.backend.application.user.repository.UserRepository;
import com.ns.backend.application.user.repository.UserRepositoryImplSQLite;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecurityService {
    private String key = "WebAirSecretKey";
    private UserRepository userRepository = UserRepositoryImplSQLite.getInstance();

    public String generateJWT(UserResponseDto user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("type", user.getType());

        return Jwts.builder()
                .setSubject(user.getID())
                .setExpiration(null)
                .claim("username", user.getUsername())
                .claim("type", user.getType())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public boolean isADMIN(String token) {
        if (token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return false;
            }

            String username = claims.getBody().get("username", String.class);

            User user = userRepository.getUserByUsername(username);
            if (user != null) return user.getType().equals(UserType.ADMIN);
            else System.out.println("Admin doesn't exist.");
        }
        return false;
    }

    public boolean isCUSTOMER(String token, String current_password) {
        if (token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return false;
            }

            String username = claims.getBody().get("username", String.class);

            User user = userRepository.getUserByUsername(username);
            if (user != null) return user.getType().equals(UserType.CUSTOMER) && user.getPassword().equals(current_password);
            else System.out.println("Customer doesn't exist.");
        }
        return false;
    }

    public boolean isUSER(String token) {
        if (token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return false;
            }

            String username = claims.getBody().get("username", String.class);

            User user = userRepository.getUserByUsername(username);
            if (user != null) return true;
            else System.out.println("User doesn't exist.");
        }
        return false;
    }

    public boolean checkUsername(String token, String username) {
        if (username != null && token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return false;
            }

            String jwt_username = claims.getBody().get("username", String.class);

            if (!jwt_username.equals(username)) {
                System.out.println("Not presented username.");
                return false;
            }

            User user = userRepository.getUserByUsername(username);
            if (user != null) return true;
            else System.out.println("User doesn't exist.");
        }
        return false;
    }

    public String getUsername(String token) {
        if (token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return null;
            }
            return claims.getBody().get("username", String.class);
        }
        return null;
    }

    public String getUserID(String token) {
        if (token != null && !token.isEmpty() && token.contains("Bearer ")) {
            String jwt = token.substring(token.indexOf("Bearer ") + 7);
            Jws<Claims> claims;
            try { claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt); }
            catch (SignatureException e) {
                System.out.println("SignatureException: JWT signature does not match locally computed signature.");
                return null;
            }
            String username = claims.getBody().get("username", String.class);
            User user = userRepository.getUserByUsername(username);
            if (user != null) return user.getID().toString();
        }
        return null;
    }

}
