package fr.houseofcode.dap.ws.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.calendar.model.Event;

import fr.houseofcode.dap.ws.GoogleFacade;

/**
 * @author djer
 */
@RestController
@RequestMapping("/events")
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class Events {
    /** Google facade. */
    @Autowired
    private GoogleFacade googleFacade;

    /**
     * Display the next Event.
     * @param userId The user ID used to store the credentials
     * @return informations about he next Event
     */
    @RequestMapping(path = "/next", consumes = "!"
            + MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String nextEvent(@RequestParam final String userId) {
        return googleFacade.display(userId, googleFacade.getNextEvent(userId));
    }

    /**
     * return a JSON representation of the next Event.
     * @param userId The user ID used to store the credentials
     * @return informations about he next Event
     */
    @RequestMapping(path = "/next", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Event nextEventJson(@RequestParam final String userId) {
        return googleFacade.getNextEvent(userId);
    }

}
