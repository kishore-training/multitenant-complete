package com.udemy.multitenancy.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException {

        // ...

        String hospital = request.getParameter("hospital");

        if (hospital != null) {
            Cookie cookie = new Cookie("HOSPITAL",hospital);
            cookie.setValue(hospital);
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);
        }else{
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("HOSPITAL")){
                    hospital = cookie.getValue();
                }
            }
        }
        UsernamePasswordAuthenticationToken authRequest
                = getAuthRequest(request, hospital);
        setDetails(request, authRequest);


        return this.getAuthenticationManager()
                .authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(
            HttpServletRequest request, String hospital) {

        String username = obtainUsername(request);
        String password = obtainPassword(request);


        // ...

        String usernameDomain = String.format("%s%s%s", username.trim(),
                String.valueOf(Character.LINE_SEPARATOR), hospital);
        return new UsernamePasswordAuthenticationToken(
                usernameDomain, password);
    }
}
