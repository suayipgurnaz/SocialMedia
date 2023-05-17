package com.bilgeadam.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.repository.enums.ERole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {
    /** Bu degerleri ortam degiskenlerine ekledik,
     *  application.yml'de tanımladık ve burada cagırdık:   */
    @Value("${jwt.secretkey}") // @Value, springframework ten gelmeli, lomboktan degil!
    String secretKey;
    @Value("${jwt.issuer}")
    String issuer;  // kimin yarattıgı
    @Value("${jwt.audience}")
    String audience; // kurum, daha ust yapi

    public Optional<String> createToken(Long id){
        String token=null;
        Date date=new Date(System.currentTimeMillis()+(1000*60*5));
        try {
            token= JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id",id) // withClaim ile bu bilgiyi token içine geçiyoruz
                    .sign(Algorithm.HMAC512(secretKey));
                return Optional.of(token);
        }catch (Exception e){
            System.out.println(e.getMessage());
                return Optional.empty();
        }
    }
    public Optional<String> createToken(Long id, ERole role){
        String token=null;
        Date date=new Date(System.currentTimeMillis()+(1000*60*5));
        try {
            token= JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(date)
                    .withClaim("id",id) // withClaim ile bu bilgiyi token içine geçiyoruz
                    .withClaim("role",role.toString()) // withClaim ile bu bilgiyi token içine geçiyoruz
                    .sign(Algorithm.HMAC512(secretKey));
            return Optional.of(token);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**  token'ı decode edecek method:   */
    public Boolean validateToken(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC512(secretKey);
            JWTVerifier verifier=JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT=verifier.verify(token);

            if (decodedJWT==null){
                return false;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
        return true;
    }

    /**  hem token'ı validate edecek, hem de id'yi Long olarak donecek method:   */
    public Optional<Long> getIdFromToken(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC512(secretKey);
            JWTVerifier verifier=JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT=verifier.verify(token);
            if (decodedJWT==null){
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            Long id=decodedJWT.getClaim("id").asLong(); // id ismiyle verdigim claim'i Long olarak geri dön
            return Optional.of(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }

    /**  hem token'ı validate edecek, hem de role'ü String olarak donecek method:   */
    public Optional<String> getRoleFromToken(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC512(secretKey);
            JWTVerifier verifier=JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build();
            DecodedJWT decodedJWT=verifier.verify(token);
            if (decodedJWT==null){
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
            String role=decodedJWT.getClaim("role").asString();
            return Optional.of(role);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }
}
