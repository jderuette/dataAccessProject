package fr.houseofcode.dap.ws.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author djer
 */
@Controller
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class Index {

    /**
     * A basic Controller to test.
     * @return an hello String
     */
    @RequestMapping("/")
    public String index() {
        return "welcome";
    }
}
