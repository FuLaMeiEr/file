package com.wahaha.demo.contents;


import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * @author Mr.Fan
 */
public class HttpStatic {

    public final static class Header {
        /**
         * COMMON
         */
        @CorsAllowed
        public static final String ACCEPT = "Accept";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String CONNECTION = "Connection";
        public static final String COOKIE = "Cookie";
        @CorsAllowed
        public static final String CONTENT_LENGTH = "Content-Length";
        @CorsAllowed
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String DATE = "Date";
        public static final String EXPECT = "Expect";
        public static final String FROM = "From";
        public static final String HOST = "Host";
        public static final String IF_MATCH = "If-Match";
        public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
        public static final String IF_NONE_MATCH = "If-None-Match";
        public static final String IF_RANGE = "If-Range";
        public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        public static final String MAX_FORWARDS = "Max-Forwards";
        public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
        @CorsAllowed
        public static final String AUTHORIZATION = "Authorization";
        @CorsAllowed
        public static final String RANGE = "Range";
        public static final String REFERER = "Referer";
        public static final String TE = "TE";
        public static final String UPGRADE = "Upgrade";
        public static final String USER_AGENT = "User-Agent";
        public static final String VIA = "Via";
        public static final String WARNING = "Warning";
        @CorsAllowed
        public static final String ORIGIN = "Origin";
        @CorsAllowed
        public static final String X_REQUESTED_WITH = "X-Requested-With";
        @CorsAllowed
        public static final String LAST_MODIFIED = "Last-Modified";

        /**
         * CUSTOM
         */
        @CorsAllowed
        public static final String CONTENT_CRC32 = "Content-CRC32";
        @CorsAllowed
        public static final String CONTENT_MD5 = "Content-MD5";
        @CorsAllowed
        public static final String CONTENT_SHA1 = "Content-SHA1";
        @CorsAllowed
        public static final String CONTENT_SHA256 = "Content-SHA256";


        @CorsAllowed
        public static final String TOKEN = "Token";
        @CorsAllowed
        public static final String X_AUTH_TOKEN = "X-Auth-Token";
        @CorsAllowed
        public static final String CAPTCHA = "Captcha";
        @CorsAllowed
        public static final String DATA_TYPE = "Data-Type";
        @CorsAllowed
        public static final String DEVICE = "Device";
        @CorsAllowed
        public static final String USER_ID = "User-Id";


        public static final String CORS_ALLOW_HEADERS = getCorsHeaders();


        private static String getCorsHeaders() {
            final StringBuilder sb = new StringBuilder("*");
            try {
                Field[] fields = Header.class.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    CorsAllowed allowed = field.getDeclaredAnnotation(CorsAllowed.class);
                    if (allowed == null) {
                        continue;
                    }
                    sb.append(",").append(field.get(null));
                }
            } catch (Exception e) {
            }
            return sb.toString();
        }

    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    private @interface CorsAllowed {
        boolean value() default true;
    }
}
