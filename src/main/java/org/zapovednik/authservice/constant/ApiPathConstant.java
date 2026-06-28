package org.zapovednik.authservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPathConstant {
    public static final String LOGIN_API_PATH = "/api/auth/login";
    public static final String REGISTER_API_PATH = "/api/auth/register";
    public static final String REFRESH_API_PATH = "/api/auth/refresh";
    public static final String ADMIN_API_PATH = "/api/admin/**";
}