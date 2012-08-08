package kr.swmaestro.hsb.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 쿠키 유틸리티 클래스
 * 
 * @author Mr. 하늘
 */
public class CookieBox {

        private final static String encodingType = "UTF-8";
        private Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();

        public CookieBox(HttpServletRequest request) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                        for (int i = 0; i < cookies.length; i++) {
                                cookieMap.put(cookies[i].getName(), cookies[i]);
                        }
                }
        }

        public static Cookie createCookie(String name, String value) throws IOException {
                return new Cookie(name, URLEncoder.encode(value, encodingType));
        }

        public static Cookie createCookie(String name, String value, String path) throws IOException {
                Cookie cookie = new Cookie(name, URLEncoder.encode(value, encodingType));
                cookie.setPath(path);
                return cookie;
        }

        public static Cookie createCookie(String name, String value, int maxAge) throws IOException {
                Cookie cookie = new Cookie(name, URLEncoder.encode(value, encodingType));
                cookie.setMaxAge(maxAge);
                return cookie;
        }

        public static Cookie createCookie(
                        String name,
                        String value,
                        String path,
                        int maxAge) throws IOException {
                Cookie cookie = new Cookie(name, URLEncoder.encode(value, encodingType));
                cookie.setPath(path);
                cookie.setMaxAge(maxAge);
                return cookie;
        }

        public static Cookie createCookie(
                        String name,
                        String value,
                        String domain,
                        String path,
                        int maxAge) throws IOException {
                Cookie cookie = new Cookie(name, URLEncoder.encode(value, encodingType));
                cookie.setDomain(domain);
                cookie.setPath(path);
                cookie.setMaxAge(maxAge);
                return cookie;
        }

        public Cookie getCookie(String name) {
                return (Cookie) cookieMap.get(name);
        }

        public String getValue(String name) throws IOException {
                Cookie cookie = (Cookie) cookieMap.get(name);
                if (cookie == null) {
                        return null;
                }
                return URLDecoder.decode(cookie.getValue(), encodingType);
        }

        public boolean exists(String name) {
                return cookieMap.get(name) != null;
        }

}