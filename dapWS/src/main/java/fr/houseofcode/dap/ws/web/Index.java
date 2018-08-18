package fr.houseofcode.dap.ws.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author djer
 */
@RestController
public class Index {

    /**
     * A basic Controller to test.
     * @return an hello String
     */
    @RequestMapping("/")
    public String index() {
        return "Bonjour et bienvenu dans votre application de gestion et centralisation de vos données";
    }
}
