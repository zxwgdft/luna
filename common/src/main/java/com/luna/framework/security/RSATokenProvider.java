package com.luna.framework.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luna.framework.exception.BusinessException;
import com.luna.framework.utils.convert.JsonUtil;
import com.luna.framework.utils.secure.RSAEncryptUtil;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSATokenProvider implements TokenProvider {

    private Algorithm algorithm;

    public RSATokenProvider(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.algorithm = Algorithm.RSA512(publicKey, privateKey);
    }

    public RSATokenProvider(String privateKeyString, String publicKeyString) throws Exception {
        RSAPublicKey publicKey = RSAEncryptUtil.getRSAPublicKey(publicKeyString);
        RSAPrivateKey privateKey = RSAEncryptUtil.getRSAPrivateKey(privateKeyString);
        this.algorithm = Algorithm.RSA512(publicKey, privateKey);
    }

    @Override
    public String createJWT(UserClaims userClaims) {
        try {
            String subject = JsonUtil.getJson(userClaims);
            return JWT.create()
                    .withSubject(subject)
                    .sign(algorithm);
        } catch (IOException e) {
            throw new BusinessException("创建token异常");
        }
    }

    @Override
    public UserClaims parseJWT(String jwtToken, Class<? extends UserClaims> clazz) {
        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        String subject = decodedJWT.getSubject();
        try {
            return JsonUtil.parseJson(subject, clazz);
        } catch (IOException e) {
            throw new BusinessException("解析token异常");
        }
    }

}
