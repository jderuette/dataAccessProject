package fr.houseofcode.dap.ws.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.houseofcode.dap.ws.GoogleFacade;

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
     * Retrieve the Google Labels.
     * @param userId The user ID used to store the credentials
     * @return a string representation of labels
     */
    @RequestMapping("/labels/{userId}")
    public String getLabels(@PathVariable final String userId) {
        return googleFacade.buildInboxLabels(userId);
    }

    /**
     * count unread messages.
     * @param userId The user ID used to store the credentials
     * @return the number of unread email
     */
    @RequestMapping("/unread/count/{userId}")
    public String getNbunreadEmail(@PathVariable final String userId) {
        return googleFacade.getNbUnreadEmail(userId);
    }

}
