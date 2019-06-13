package com.wsl.meta.SessionProperty;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author wsl
 * @date 2019/6/13
 */
public class CustomExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        onSessionInvalid(event.getRequest(), event.getResponse());
    }

    public CustomExpiredSessionStrategy(String invalidSessionUrl) {
        super(invalidSessionUrl);
    }

    @Override
    protected boolean isConcurrency() {
        return super.isConcurrency();
    }
}
