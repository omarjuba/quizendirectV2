package fr.univangers.UnitTest;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
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
	
	
	@Autowired
	private JavaMailSender javamailsender;

	@Autowired 
	private SalonService salonservice;
	@MockBean
	private SalonRepository salonrepository;
	
	@Autowired
	private RepertoireService repertoireservice;
	@MockBean
	private RepertoireRepository repertoirerepository;
	
	@Autowired
	private HistoriqueService historiqueservice;
	@MockBean
	private HistoriqueRepository historiquerepository;
	
	// stream de toutes les données de test avant les tests
	@Before
	public void streamdata() {
		when(enseignantrepository.findAll()).thenReturn(Stream
				.of(new Enseignant("Test1","test@univ-angers.fr","1111"),new Enseignant("Test2","test@univ-angers.fr","1212122")).collect(Collectors.toList()));
		
		ArrayList<String> repb = new ArrayList<String>();
		
		when(questionrepository.findAll()).thenReturn(Stream
				.of(new Question("TEST 1", 1, repb, repb, 10),new Question("TEST 2", 1, repb, repb, 10)).collect(Collectors.toList()));
		
		when(salonrepository.findAll()).thenReturn(Stream
				.of(new Salon(7895,new Enseignant("Test1","test@univ-angers.fr","1111")),new Salon(4521,new Enseignant("Test1","test@univ-angers.fr","1111"))).collect(Collectors.toList()));
	
		when(repertoirerepository.findAll()).thenReturn(Stream
				.of(new Repertoire("POO",new Enseignant("Test1","test@univ-angers.fr","1111")),new Repertoire("NASM",new Enseignant("Test1","test@univ-angers.fr","1111"))).collect(Collectors.toList()));
		
		when(historiquerepository.findAll()).thenReturn(Stream
				.of(new Historique(),new Historique()).collect(Collectors.toList()));

	}
	
	@Test
	public void getAllService() {
		
		assertEquals(2, enseignantservice.getAllEnseignants().size());
		assertEquals(2, questionservice.getAllQuestions().size());
		assertEquals(2, salonservice.getAllSalons().size());
		assertEquals(2, repertoireservice.getAllRepertoires().size());
		assertEquals(2, historiqueservice.getAllHistoriques().size());
	}
	
	@Test
	public void sendmailtest() {
		SendMailService sendmailservice = new SendMailService(javamailsender);
		int result;
		try {
			result = sendmailservice.sendEmail("test@gamil.com", "this is a mail body", "this is a mail topic");
		} catch (MessagingException e) {
			result = 0;
			e.printStackTrace();
		}
		
		assertEquals(1,result);
	}

}
