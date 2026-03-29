package com.application.discussion.project.application.services.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HexFormat;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.application.discussion.project.application.dtos.exceptions.ApplicationLayerException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * JWTトークンの生成、検証、抽出を行うユーティリティクラス
 * Spring Securityの認証・認可処理で使用される
 */
@Component
public class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    private static final Integer TOKEN_SECRET_LENGTH = 256;
    private static final Integer TOKEN_SECRET_BYTE_LENGTH = TOKEN_SECRET_LENGTH / 8;

    @Value("${springboot.app.authentication.jwt.token.expiration}")
    private long jwtTokenExpirationMs;
    @Value("${springboot.app.authentication.jwt.token.secret}")
    private String jwtTokenSecret;
    @Value("${springboot.app.cookies.name}")
    private String jwtCookieName;
    @Value("${springboot.app.cookies.path}")
    private String jwtCookiesPath;
    @Value("${springboot.app.cookies.httpOnly}")
    private Boolean isCookieHttpOnly;
    @Value("${springboot.app.cookies.secure}")
    private Boolean isCookieSecure;

    @Value("${springboot.app.authentication.jwt.refreshtoken.secret}")
    private String jwtRefreshTokenSecret;
    @Value("${springboot.app.authentication.jwt.refreshtoken.expiration}")
    private long jwtRefreshTokenExpirationMs;
    @Value("${springboot.app.cookies.refresh.name:refreshToken}")
    private String refreshCookieName;
    @Value("${springboot.app.cookies.refresh.path:/v1/auth}")
    private String refreshCookiePath;

    /**
     * HTTPリクエストのAuthorizationヘッダーからJWTトークンを抽出する
     * 
     * @param httpServletRequest HTTPサーブレットリクエスト
     * @return 抽出されたJWTトークン。ヘッダーが存在しないか不正な形式の場合はnull
     */
    public String getJwtFromHeader(HttpServletRequest httpServletRequest) {
        logger.info("Extracting JWT from Authorization header");
        
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }

    /**
     * HTTPリクエストのCookieからJWTトークンを抽出する
     * 
     * @param httpServletRequest HTTPサーブレットリクエスト
     * @return 抽出されたJWTトークン。Cookieが存在しない場合はnull
     */
    public String getJwtFromCookies(final HttpServletRequest httpServletRequest) {
        logger.info("Extracting JWT from cookies");
        
        Cookie[] cookies = httpServletRequest.getCookies();

        logger.info("Cookies: {}",Arrays.toString(cookies));
        
        return Optional.ofNullable(cookies)
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> jwtCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * ユーザー詳細情報からJWTトークンを生成する
     * トークンにはユーザーID、ログインID、ユーザー名、発行日時、有効期限が含まれる
     * 
     * @param userDetails JWT認証用のユーザー詳細情報
     * @return 生成されたJWTトークン文字列
     */
    public String generateToken(final JWTAuthUserDetails userDetails) {
        logger.info("Generating JWT token for user: {} {}", userDetails.getUsername(), userDetails.getEmail());
        logger.info("create jwt token: user details={}", userDetails.toString());
        
        return Jwts.builder()
                .subject(userDetails.getLoginId() != null ? userDetails.getLoginId() : userDetails.getEmail())
                .id(userDetails.getUserId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTokenExpirationMs))
                .signWith(getKey())
                .claim("username",userDetails.getUsername())
                .claim("loginId", userDetails.getLoginId())
                .compact();
    }
    
    /**
     * JWTトークンからメールアドレスまたはログインIDを取得する
     * 
     * @param token JWTトークン文字列
     * @return トークンのsubjectに設定されているメールアドレスまたはログインID
     */
    public String getEmailOrLoginId(final String token){
        logger.info("Extracting email or login ID from JWT token");
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * JWTトークンからユーザーIDを取得する
     * 
     * @param token JWTトークン文字列
     * @return トークンに含まれるユーザーID
     */
    public String getUserIdFromToken(String token) {
        logger.info("Extracting user ID from JWT token");
        
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    /**
     * JWTトークンの妥当性を検証する
     * トークンの署名、有効期限、形式をチェックし、問題があれば例外をスローする
     * 
     * @param token 検証対象のJWTトークン文字列
     * @return トークンが有効な場合はtrue
     * @throws ApplicationLayerException トークンが不正な形式、期限切れ、サポート外の形式、空の場合
     */
    public Boolean validateJwtToken(String token) {
        logger.info("Try validating JWT token");
        
        try {
            Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException malformedJwtException){
            logger.error("Malformed JWT Exception Invalid JWT token: {}", malformedJwtException.getMessage());
            throw new ApplicationLayerException("Invalid JWT token",HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400));
        
        } catch (ExpiredJwtException expiredJwtException){
            logger.error("JWT token is expired: ", expiredJwtException.getMessage());
            throw new ApplicationLayerException("JWT token is expired", HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400));
        } catch (UnsupportedJwtException unsupportedJwtException){
            logger.error("UnsupportedJwtException Unsupported JWT token: {}", unsupportedJwtException.getMessage());
            throw new ApplicationLayerException("Unsupported JWT token: ", HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400));
        
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("JWT claims string is empty: {}", illegalArgumentException.getMessage());
            throw new ApplicationLayerException("JWT claims string is empty", HttpStatus.BAD_REQUEST, HttpStatusCode.valueOf(400));
        
        }
    }

    /**
     * ユーザー詳細情報からJWT Cookieを生成する
     * Cookieには HttpOnly、Secure、SameSite などのセキュリティ属性が設定される
     * 
     * @param userDetails JWT認証用のユーザー詳細情報
     * @return 生成されたResponseCookieオブジェクト
     */
    public ResponseCookie generateJwtCookie(final JWTAuthUserDetails userDetails) {
        logger.info("Generating JWT cookie for user: {} {}", userDetails.getUsername(), userDetails.getEmail());
        
        final String jwt = generateToken(userDetails);
        return ResponseCookie.from(jwtCookieName, jwt)
                .httpOnly(true)
                .secure(false)
                .sameSite(SameSiteCookies.NONE.name())
                .path(jwtCookiesPath)
                .maxAge(jwtTokenExpirationMs / 1000)
                .build();
    }
    
    /**
     * JWT署名用の秘密鍵を取得する
     * application.propertiesに設定されたBase64エンコードされた秘密鍵をデコードして返す
     * 
     * @return HMAC-SHA アルゴリズム用の署名鍵
     */
    public Key getKey(){
        logger.info("Retrieving signing key for JWT token");
        byte[] keyBytes = Decoders.BASE64.decode(jwtTokenSecret);
        logger.info("Decoded JWT token secret, length: {} bytes", keyBytes.length);

        if (keyBytes.length < TOKEN_SECRET_BYTE_LENGTH){
            logger.error("The JWT token secret must be at least {} bits long", TOKEN_SECRET_LENGTH);
            return Jwts.SIG.HS512.key().build();
        }
        Key key = Keys.hmacShaKeyFor(keyBytes);
        logger.info("Generated signing key using algorithm: {}", key.getAlgorithm());
        return key;
    }

    /**
     * JWTトークンが格納されているCookieを削除する
     * 
     * @param response HTTPレスポンス
     */
    public ResponseCookie getClearJwtCookie() {
        logger.info("Clearing JWT token cookie");
        ResponseCookie cookie = ResponseCookie.from(jwtCookieName, "")
                .path(jwtCookiesPath)
                .maxAge(0)
                .httpOnly(isCookieHttpOnly)
                .secure(isCookieSecure)
                .sameSite(SameSiteCookies.STRICT.name())
                .build();
        logger.info("remove jwt token cookie: {}", jwtCookieName);
        return cookie;
    }

    /**
     * リフレッシュトークン用のJWTを生成する
     * アクセストークンとは異なる秘密鍵と有効期限を使用する
     *
     * @param userDetails JWT認証用のユーザー詳細情報
     * @return 生成されたリフレッシュトークン文字列
     */
    public String generateRefreshToken(final JWTAuthUserDetails userDetails) {
        logger.info("Generating refresh token for user: {}", userDetails.getUsername());

        return Jwts.builder()
                .subject(userDetails.getLoginId() != null ? userDetails.getLoginId() : userDetails.getEmail())
                .id(userDetails.getUserId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshTokenExpirationMs))
                .signWith(getRefreshTokenKey())
                .claim("type", "refresh")
                .compact();
    }

    /**
     * リフレッシュトークンをHttpOnly Cookieに設定するためのResponseCookieを生成する
     *
     * @param token リフレッシュトークン文字列
     * @return 生成されたResponseCookieオブジェクト
     */
    public ResponseCookie generateRefreshTokenCookie(final String token) {
        logger.info("Generating refresh token cookie");

        return ResponseCookie.from(refreshCookieName, token)
                .httpOnly(true)
                .secure(isCookieSecure)
                .sameSite(SameSiteCookies.STRICT.name())
                .path(refreshCookiePath)
                .maxAge(jwtRefreshTokenExpirationMs / 1000)
                .build();
    }

    /**
     * HTTPリクエストのCookieからリフレッシュトークンを抽出する
     *
     * @param httpServletRequest HTTPサーブレットリクエスト
     * @return 抽出されたリフレッシュトークン。Cookieが存在しない場合はnull
     */
    public String getRefreshTokenFromCookies(final HttpServletRequest httpServletRequest) {
        logger.info("Extracting refresh token from cookies");

        Cookie[] cookies = httpServletRequest.getCookies();

        return Optional.ofNullable(cookies)
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> refreshCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * リフレッシュトークンの妥当性を検証する
     * アクセストークンとは異なる秘密鍵で検証を行う
     *
     * @param token 検証対象のリフレッシュトークン文字列
     * @return トークンが有効な場合はtrue
     * @throws ApplicationLayerException トークンが不正な形式、期限切れ、サポート外の形式、空の場合
     */
    public Boolean validateRefreshToken(final String token) {
        logger.info("Try validating refresh token");

        try {
            Jwts.parser()
                .verifyWith((SecretKey) getRefreshTokenKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            logger.error("Malformed refresh token: {}", malformedJwtException.getMessage());
            throw new ApplicationLayerException("Invalid refresh token", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("Refresh token is expired: {}", expiredJwtException.getMessage());
            throw new ApplicationLayerException("Refresh token is expired", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
        } catch (UnsupportedJwtException unsupportedJwtException) {
            logger.error("Unsupported refresh token: {}", unsupportedJwtException.getMessage());
            throw new ApplicationLayerException("Unsupported refresh token", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("Refresh token claims string is empty: {}", illegalArgumentException.getMessage());
            throw new ApplicationLayerException("Refresh token claims string is empty", HttpStatus.UNAUTHORIZED, HttpStatusCode.valueOf(401));
        }
    }

    /**
     * リフレッシュトークンからユーザーIDを取得する
     *
     * @param token リフレッシュトークン文字列
     * @return トークンに含まれるユーザーID
     */
    public String getUserIdFromRefreshToken(final String token) {
        logger.info("Extracting user ID from refresh token");

        return Jwts.parser()
                .verifyWith((SecretKey) getRefreshTokenKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    /**
     * リフレッシュトークンからメールアドレスまたはログインIDを取得する
     *
     * @param token リフレッシュトークン文字列
     * @return トークンのsubjectに設定されているメールアドレスまたはログインID
     */
    public String getEmailOrLoginIdFromRefreshToken(final String token) {
        logger.info("Extracting email or login ID from refresh token");

        return Jwts.parser()
                .verifyWith((SecretKey) getRefreshTokenKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * リフレッシュトークン用の署名鍵を取得する
     *
     * @return HMAC-SHA アルゴリズム用の署名鍵
     */
    public Key getRefreshTokenKey() {
        logger.info("Retrieving signing key for refresh token");
        byte[] keyBytes = Decoders.BASE64.decode(jwtRefreshTokenSecret);

        if (keyBytes.length < TOKEN_SECRET_BYTE_LENGTH) {
            logger.error("The refresh token secret must be at least {} bits long", TOKEN_SECRET_LENGTH);
            return Jwts.SIG.HS512.key().build();
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * リフレッシュトークン用のCookieを削除する
     *
     * @return maxAge=0のリフレッシュトークン削除用Cookie
     */
    public ResponseCookie getClearRefreshTokenCookie() {
        logger.info("Clearing refresh token cookie");

        return ResponseCookie.from(refreshCookieName, "")
                .path(refreshCookiePath)
                .maxAge(0)
                .httpOnly(true)
                .secure(isCookieSecure)
                .sameSite(SameSiteCookies.STRICT.name())
                .build();
    }

    /**
     * トークン文字列をSHA-256でハッシュ化する
     * DB保存時にトークンの生値を保存しないためのセキュリティ対策
     *
     * @param token ハッシュ化対象のトークン文字列
     * @return SHA-256ハッシュの16進数文字列
     */
    public String hashToken(final String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA-256 algorithm not available: {}", e.getMessage());
            throw new ApplicationLayerException(
                "Token hashing failed",
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatusCode.valueOf(500)
            );
        }
    }

    /**
     * リフレッシュトークンの有効期限をミリ秒で取得する
     *
     * @return リフレッシュトークンの有効期限（ミリ秒）
     */
    public long getRefreshTokenExpirationMs() {
        return jwtRefreshTokenExpirationMs;
    }
}
