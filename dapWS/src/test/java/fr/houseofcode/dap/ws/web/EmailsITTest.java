/**
 * 
 */
package fr.houseofcode.dap.ws.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.houseofcode.dap.ws.GoogleFacade;

/**
 * @author house
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(Emails.class)
@ComponentScan({ "fr.houseofcode.dap.ws" })
public class EmailsITTest {

    /**
     * le bouchon  = mock.
     */
    @Autowired
    private MockMvc mvc;

    /**
     * le service testé.
     */
    @MockBean
    private GoogleFacade service;

    /**
     * le test.
     * @throws Exception if error.
     */
    @Test
    public void testGetNbUnreadEmail() throws Exception {

        BDDMockito.given(service.getNbUnreadEmail("lydie")).willReturn("1");

        mvc.perform(MockMvcRequestBuilders.get("/emails/unread/count").contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("userId", "lydie")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

}
