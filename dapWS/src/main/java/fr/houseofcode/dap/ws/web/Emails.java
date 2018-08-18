package fr.houseofcode.dap.ws.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.gmail.model.Label;

import fr.houseofcode.dap.ws.GoogleFacade;

/**
 * @author djer
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
    @RequestMapping(path = "/labels", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getLabels(@RequestParam final String userId) {
        return googleFacade.buildInboxLabels(userId);
    }

    /**
     * Retrieve the Google Labels.
     * @param userId The user ID used to store the credentials
     * @return a string representation of labels
     */
    @RequestMapping(path = "/labels", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsJson(@RequestParam final String userId) {
        return googleFacade.getInboxLabels(userId);
    }

    /**
     * count unread messages.
     * @param userId The user ID used to store the credentials
     * @return the number of unread email
     */
    @RequestMapping(path = "/unread/count", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getNbunreadEmail(@RequestParam final String userId) {
        return googleFacade.getNbUnreadEmail(userId);
    }
}
