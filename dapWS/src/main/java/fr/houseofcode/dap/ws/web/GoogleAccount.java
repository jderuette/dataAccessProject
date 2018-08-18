/**
 * 
 */
package fr.houseofcode.dap.ws.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;

import fr.houseofcode.dap.ws.Config;
import fr.houseofcode.dap.ws.google.AppPeopleService;
import fr.houseofcode.dap.ws.google.CalendarService;
import fr.houseofcode.dap.ws.google.GmailService;
import fr.houseofcode.dap.ws.google.GoogleService;

/**
 * @author djer @SessionAttributes("user")
 */
@Controller
public class Auth extends GoogleService {

    /**
     * Logger.
     */
    private static final Logger LOG = LogManager.getLogger();

    public Auth(Config config) {
        super(config);
    }

    @Autowired
    GoogleService googleService;
    @Autowired
    Config config;

    @RequestMapping("/account/add/{userId}")
    public String addAccount(@PathVariable final String userId, HttpServletRequest request, HttpSession session) {
        String response = "errorOccurs";
        GoogleAuthorizationCodeFlow flow;
        Credential credential = null;
        try {
            flow = googleService.getFlow();
            credential = flow.loadCredential(userId);

            if (credential != null && credential.getAccessToken() != null) {
                response = "AccountAlreadyAdded";
            } else {
                // redirect to the authorization flow
                AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
                authorizationUrl.setRedirectUri(buildRedirectUri(request, config.getoAuth2CallbackUrl()));
                // store userId in session for CallBack Access
                session.setAttribute("userId", userId);
                return "redirect:" + authorizationUrl.build();
            }
        } catch (IOException e) {
            LOG.error("Error while loading credential (or Google Flow)", e);
        }
        // only when error occurs, else redirected BEFORE
        return response;
    }

    @RequestMapping("/oAuth2Callback")
    public String oAuthCallback(HttpServletRequest request, @RequestParam final String code, HttpSession session)
            throws ServletException {
        String userId = null;

        StringBuffer buf = request.getRequestURL();
        if (request.getQueryString() != null) {
            buf.append('?').append(request.getQueryString());
        }
        AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
        String decodeCode = responseUrl.getCode();
        if (responseUrl.getError() != null) {
            LOG.error("Error when trying to add Google acocunt : " + responseUrl.getError());
            throw new ServletException("Error when trying to add Google acocunt");
            //onError(request, resp, responseUrl);
        } else if (decodeCode == null) {
            throw new MissingServletRequestParameterException("code", "String");
        } else {
            String redirectUri = buildRedirectUri(request, config.getoAuth2CallbackUrl());
            GoogleAuthorizationCodeFlow flow;
            try {
                flow = googleService.getFlow();
                TokenResponse response = flow.newTokenRequest(decodeCode).setRedirectUri(redirectUri).execute();
                if (null != session.getAttribute("userId")) {
                    userId = (String) session.getAttribute("userId");
                }

                if (null == userId) {
                    LOG.error("userId in Session i NULL in Callback");
                    throw new ServletException(
                            "Error when trying to add Google acocunt : userId is NULL is User Session");
                }

                Credential credential = flow.createAndStoreCredential(response, userId);
                if (LOG.isDebugEnabled()) {
                    if (null != credential && null != credential.getAccessToken()) {
                        LOG.debug("New user credential stored with userId : " + userId + "partial AccessToken : "
                                + credential.getAccessToken().substring(0, 8));
                    } else {
                        LOG.warn("Trying to store a NULL AccessToken for user : " + userId);
                    }
                }
                // onSuccess(request, resp, credential);
            } catch (IOException e) {
                LOG.error("Exception while trying to store user Credential", e);
            }

            return "redirect:/";
        }
    }

    protected String buildRedirectUri(HttpServletRequest req, String destination) {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath(destination);
        return url.build();
    }

    @Override
    protected Logger getLog() {
        return LOG;
    }

    @Override
    protected List<String> getScopes() {
        List<String> allScopes = new ArrayList<>(GmailService.SCOPES);
        allScopes.addAll(AppPeopleService.SCOPES);
        allScopes.addAll(CalendarService.SCOPES);
        return allScopes;
    }
}
