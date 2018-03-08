package com.cll.wenda.utils;

import com.cll.wenda.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static Key key= MacProvider.generateKey();
   //private static String key= "cllcll";

    //@Value("${jwt.expiration}")
    private static int expiration=3600;

    public static String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>(3);
        System.out.println(user);
        claims.put("uid", user.getId());
        claims.put("name", user.getUsername());
        //claims.put("img",user.getHeadimg());

        String res=generateToken(claims);
        System.out.println(res);
        return res;
    }

    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    private static Date generateExpirationDate() {

        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public static boolean valid(String token){
        if (getClaimsFromToken(token)==null){
            return false;
        }
        return true;
    }




    public static String getUidFromToken(String token){
        Claims claims=getClaimsFromToken(token);
        String role= (String) claims.get("uid");
        return role;
    }
}
