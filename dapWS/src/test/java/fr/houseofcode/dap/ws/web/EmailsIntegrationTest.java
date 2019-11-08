package fr.houseofcode.dap.ws.web;

import org.mockito.BDDMockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fr.houseofcode.dap.ws.GoogleFacade;

/**
 * @author djer1
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(Emails.class)
@ComponentScan({ "fr.houseofcode.dap.ws" })
public class EmailsIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GoogleFacade service;

    @Test
    // givenUserId_whengetNbUnreadEmail_returnNumber
    public void testGetNbunreadEmail() throws Exception {

        BDDMockito.given(service.getNbUnreadEmail("djer")).willReturn("8");

        mvc.perform(MockMvcRequestBuilders.get("/emails/unread/count").contentType(MediaType.TEXT_PLAIN_VALUE)
                .param("userId", "djer")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("8"));
    }

}
