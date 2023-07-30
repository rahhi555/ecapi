package com.example.ecapi.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @see https://youtu.be/KxqlJblhzfI?t=2939
 */
@Service
public class JwtService {

    /** HS256のキー */
    private static final String SECRET_KEY = "TFUkQIk8gXB+35eyw+rEa44jgo/tvnDkNYwAUMWBHnE=";

    /**
     * jwtからユーザーid取得
     * @param token JWTの文字列
     * @return tokenから取得したユーザーid
     */
    public String extractUserId(String token) {
        // jwtのsubjectは一意なユーザー名（本アプリにおいてはid）
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * JWT（ヘッダー.ペイロード(複数のクレーム).署名）のペイロードから単一のクレームを取得
     * @param token JWTの文字列
     * @param claimsResolver クレームを取り出すためのFunction(例: Claims::getSubject)
     * @return 単一のクレーム
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * ユーザー情報からJWTを作成して返す
     * @param userDetails ユーザー詳細情報
     * @return 作成したJWTの文字列
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * 既存のクレーム集合とユーザー情報からJWTを作成して返す
     * @param extraClaims 追加のクレーム
     * @param userDetails ユーザー詳細情報
     * @return 作成したJWTの文字列
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // 一意のユーザー名（id）
                .setIssuedAt(new Date(System.currentTimeMillis())) // 発行時刻
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // 有効期限
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * トークンが有効かどうか判定する
     * @param token JWTの文字列
     * @param userDetails ユーザー詳細情報
     * @return トークンが有効な場合はtrue、そうでない場合はfalse
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserId(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * トークンが期限切れかどうか判定する
     * @param token JWTの文字列
     * @return トークンが期限切れの場合はtrue、そうでない場合はfalse
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * トークンから有効期限を取得する
     * @param token JWTの文字列
     * @return トークンの有効期限
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * トークンからクレーム集合を取得する
     * @param token JWTの文字列
     * @return の全クレーム
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 署名に使用するキーを取得する
     * @return 署名キー
     */
    private Key getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
