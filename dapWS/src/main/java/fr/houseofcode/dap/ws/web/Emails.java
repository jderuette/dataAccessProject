package fr.houseofcode.dap.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.houseofcode.dap.GoogleFacade;

/**
 * @author djer
 *
 */
@RestController
@RequestMapping("/emails")
public class Emails {

    /** Google facade. */
    @Autowired
    private GoogleFacade googleFacade;

    /**
     * Emails.
     */
    public Emails() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieve the Google Labels.
     * @param requestedUser user Id or "me"
     * @return a string representation of labels
     */
    @RequestMapping("/labels/{requestedUser}")
    public String getLabels(@PathVariable final String requestedUser) {
        return googleFacade.buildInboxLabels(requestedUser);
    }

    /**
     * count unread messages.
     * @param requestedUser user Id or "me"
     * @return the number of unread email
     */
    @RequestMapping("/unread/count/{requestedUser}")
    public String getNbunreadEmail(@PathVariable final String requestedUser) {
        return googleFacade.getNbUnreadEmail(requestedUser);
    }

}
