package com.wsl.meta.SessionProperty;

import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wsl
 * @date 2019/6/13
 */
public class CustomInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        onSessionInvalid(request, response);
    }

    public CustomInvalidSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }
}
