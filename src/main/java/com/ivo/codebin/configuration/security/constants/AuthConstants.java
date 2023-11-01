package com.ivo.codebin.configuration.security.constants;

public class AuthConstants {

    public static final String SECRET = "%`T<Zod2^./UF$s1,j)QW?6rlDGcJ:K'";
    public static final String CSRF_HEADER = "X-XSRF-TOKEN";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 10000; // 1 day
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 60000; // 7 days
    public static final long HTTP_ACCESS_COOKIE_EXPIRATION_TIME = 86_400_000; // 1 day
    public static final long HTTP_REFRESH_COOKIE_EXPIRATION_TIME = 604_800_800; // 7 days

}
