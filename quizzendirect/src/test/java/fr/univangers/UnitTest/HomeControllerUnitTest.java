package fr.univangers.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.univangers.controller.Home;
import fr.univangers.services.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Home.class)
public class HomeControllerUnitTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RepertoireService repertoireservice;
	@MockBean
	private QuestionService questionservice;
	
	@MockBean 
	private EnseignantService enseignantService;
	
	
	
	@Test
	public void home() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/");
		mvc.perform(request).andExpect(status().isOk())
							.andExpect(view().name("home"));
		
	}
	
}

