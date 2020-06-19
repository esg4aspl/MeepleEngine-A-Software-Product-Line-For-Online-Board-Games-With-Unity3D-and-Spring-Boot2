package com.meepleengine.meepleserver.security;

public class SecurityConstants {

    // TODO: extract secret as env var
    public static final String JWT_SECRET = "4t7w!z%C*F-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVmYp3s6v9y$B&E)H@McQfTjW";

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "meeple-engine";
    public static final String TOKEN_AUDIENCE = "meeple-engine";

    private SecurityConstants() {}
}