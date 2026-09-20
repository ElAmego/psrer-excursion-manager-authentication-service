package org.zapovednik.authservice.configuration.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPathConstant {
    public static final String LOGIN_API_PATH = "/api/user/authentication/login";
    public static final String REFRESH_API_PATH = "/api/user/authentication/refresh";
    public static final String VALIDATE_API_PATH = "/api/user/authentication/validate";
    public static final String LOGOUT_API_PATH = "/api/user/authentication/logout";
    public static final String REGISTER_API_PATH = "/api/admin/authentication/register";
    public static final String ADMIN_API_PATH = "/api/admin/**";
}