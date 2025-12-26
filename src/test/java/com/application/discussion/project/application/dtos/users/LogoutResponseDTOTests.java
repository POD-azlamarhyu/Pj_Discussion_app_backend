package com.application.discussion.project.application.dtos.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LogoutResponseDTO テストクラス")
public class LogoutResponseDTOTests {

    private static final String TEST_COOKIE_NAME = "jwt";
    private static final String TEST_COOKIE_VALUE = "test-token";
    private static final String TEST_MESSAGE = "Logout successful";
    
    private LogoutResponse mockLogoutResponse;
    private ResponseCookie mockJwtCookie;

    @BeforeEach
    void setUp() {
        mockLogoutResponse = LogoutResponse.of(TEST_MESSAGE, true);
        mockJwtCookie = ResponseCookie.from(TEST_COOKIE_NAME, TEST_COOKIE_VALUE)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
    }

    @Test
    @DisplayName("デフォルトコンストラクタが正常に動作すること")
    void constructDefaultSuccessfully() {
        LogoutResponseDTO actualDto = new LogoutResponseDTO();
        
        assertNotNull(actualDto);
        assertNull(actualDto.getLogoutResponse());
        assertNull(actualDto.getJwtCookie());
    }

    @Test
    @DisplayName("ファクトリーメソッドでインスタンスが正常に生成されること")
    void createInstanceUsingFactoryMethod() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(mockLogoutResponse, mockJwtCookie);
        
        assertNotNull(actualDto);
        assertEquals(mockLogoutResponse, actualDto.getLogoutResponse());
        assertEquals(mockJwtCookie, actualDto.getJwtCookie());
    }

    @Test
    @DisplayName("getLogoutResponseが正常に値を返すこと")
    void getLogoutResponseReturnsCorrectValue() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(mockLogoutResponse, mockJwtCookie);
        
        LogoutResponse actualResponse = actualDto.getLogoutResponse();
        
        assertEquals(mockLogoutResponse, actualResponse);
    }

    @Test
    @DisplayName("getJwtCookieが正常に値を返すこと")
    void getJwtCookieReturnsCorrectValue() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(mockLogoutResponse, mockJwtCookie);
        
        ResponseCookie actualCookie = actualDto.getJwtCookie();
        
        assertEquals(mockJwtCookie, actualCookie);
        assertEquals(TEST_COOKIE_NAME, actualCookie.getName());
        assertEquals(TEST_COOKIE_VALUE, actualCookie.getValue());
    }

    @Test
    @DisplayName("ファクトリーメソッドがnullのLogoutResponseを受け入れること")
    void createInstanceWithNullLogoutResponse() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(null, mockJwtCookie);
        
        assertNotNull(actualDto);
        assertNull(actualDto.getLogoutResponse());
        assertEquals(mockJwtCookie, actualDto.getJwtCookie());
    }

    @Test
    @DisplayName("ファクトリーメソッドがnullのResponseCookieを受け入れること")
    void createInstanceWithNullJwtCookie() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(mockLogoutResponse, null);
        
        assertNotNull(actualDto);
        assertEquals(mockLogoutResponse, actualDto.getLogoutResponse());
        assertNull(actualDto.getJwtCookie());
    }

    @Test
    @DisplayName("ファクトリーメソッドが両方nullの引数を受け入れること")
    void createInstanceWithBothNull() {
        LogoutResponseDTO actualDto = LogoutResponseDTO.of(null, null);
        
        assertNotNull(actualDto);
        assertNull(actualDto.getLogoutResponse());
        assertNull(actualDto.getJwtCookie());
    }
}
