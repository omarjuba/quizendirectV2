package fr.univangers.UnitTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.univangers.controller.ConnexionSalonStudentController;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ConnexionSalonStudentController.class)
public class ConnexionSalonControllerUnitTest {

	@Autowired
	private MockMvc mvc;
	
	
	@Test
	public void salonconnexion() throws Exception {
		
		RequestBuilder request = MockMvcRequestBuilders.get("/ConnexionSalonStudent");
		mvc.perform(request).andExpect(status().isOk())
							.andExpect(view().name("ConnexionSallonStudent"));
		
		
	} 	
}
