package fr.houseofcode.dap.ws.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;

import fr.houseofcode.dap.ws.google.GmailService;

/**
 * @author djer
 */
@Controller
@RequestMapping("/admin")
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class Admin {

    /** GmailService instance to access Google flow (and it's DataStore). */
    @Autowired
    private GmailService gmaiLServivce;

    /**
     * Entry Point for admin actions.
     * @param model the MVC Model
     * @return admin View
     * @throws IOException whene Google Error occurs
     */
    @RequestMapping("/")
    public String index(final ModelMap model) throws IOException {
        final DataStore<StoredCredential> usersTokens = gmaiLServivce.getAllCredentials();

        // Populate in a statndard Map
        Map<String, StoredCredential> userTokensMap = new HashMap<>();

        if (null != usersTokens && usersTokens.size() > 0) {
            for (String userKey : usersTokens.keySet()) {
                userTokensMap.put(userKey, usersTokens.get(userKey));
            }
        }

        model.addAttribute("usersTokens", userTokensMap);

        return "adminIndex";
    }

    /**
     * Retrieve Stored Credentials, and update the admin "content" fragment.
     * @param model the MVC Model
     * @return fragment to update
     */
//    @RequestMapping("/showGoogleTokens")
//    public String getGoogleAccount(final Model model) {
//
//        final DataStore<StoredCredential> usersTokens = gmaiLServivce.getAllCredentials();
//        model.addAttribute(usersTokens);
//        return "adminIndex";
//    }

}
