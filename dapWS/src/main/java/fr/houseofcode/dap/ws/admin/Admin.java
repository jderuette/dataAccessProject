package fr.houseofcode.dap.ws.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.DataStore;

import fr.houseofcode.dap.ws.google.GmailService;
import fr.houseofcode.dap.ws.user.AppUserDTO;
import fr.houseofcode.dap.ws.user.UserService;

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

    /** User management service. */
    @Autowired
    private UserService userService;

    /**
     * Entry Point for administration actions.
     * @param model  the MVC Model
     * @param userId the public User Id performing request (must be an
     *               Administrator)
     * @return administration View
     * @throws IOException when Google Error occurs
     */
    @RequestMapping("/")
    public String index(final ModelMap model, @RequestParam final String userId) throws IOException {
        String view = null;
        if (userService.isAdmin(userId)) {
            final DataStore<StoredCredential> usersTokens = gmaiLServivce.getAllCredentials();

            // Populate in a standard Map
            final Map<String, StoredCredential> userTokensMap = new HashMap<>();

            if (null != usersTokens && usersTokens.size() > 0) {
                for (final String userKey : usersTokens.keySet()) {
                    userTokensMap.put(userKey, usersTokens.get(userKey));
                }
            }

            model.addAttribute("usersTokens", userTokensMap);

            final List<AppUserDTO> allUsers = userService.getAllUsers();
            model.addAttribute("users", allUsers);

            view = "adminIndex";

        } else {
            view = "userNotAllowed";
        }

        return view;
    }

}
