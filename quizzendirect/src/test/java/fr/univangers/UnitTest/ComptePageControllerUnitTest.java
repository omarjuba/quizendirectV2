package fr.univangers.UnitTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.univangers.controller.*;
import fr.univangers.services.EnseignantService;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CompteController.class)
public class ComptePageControllerUnitTest {

	@Autowired
	private MockMvc mvc;

	@MockBean 
	private EnseignantService enseignantService;
	
	@Test
	public void comptepage() throws Exception {
		
		RequestBuilder request = MockMvcRequestBuilders.get("/connection");
		mvc.perform(request).andExpect(status().isOk())
							.andExpect(view().name("comptePage"));
		
		
	} 	
}
