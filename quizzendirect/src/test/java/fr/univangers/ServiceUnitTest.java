package fr.univangers;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import fr.univangers.models.*;
import fr.univangers.repositories.*;
import fr.univangers.services.*;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceUnitTest {

	@Autowired
	private EnseignantService enseignantservice;
	@MockBean
	private EnseignantRepository enseignantrepository;
	@Autowired
	private QuestionService questionservice;
	@MockBean
	private QuestionRepository questionrepository;

	// stream de toute les données de test avant les tests
	@Before
	public void streamdata() {
		when(enseignantrepository.findAll()).thenReturn(Stream
				.of(new Enseignant("Test1","test@univ-angers.fr","1111"),new Enseignant("Test2","test@univ-angers.fr","1212122")).collect(Collectors.toList()));
		
		ArrayList<String> repb = new ArrayList<String>();
		
		when(questionrepository.findAll()).thenReturn(Stream
				.of(new Question("TEST 1", 1, repb, repb, 10),new Question("TEST 2", 1, repb, repb, 10)).collect(Collectors.toList()));
	}
	
	@Test
	public void getAllService() {
		
		assertEquals(2, enseignantservice.getAllEnseignants().size());
		assertEquals(2, questionservice.getAllQuestions().size());
		
	}

}
