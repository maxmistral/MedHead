package eu.echodream.medhead;

import eu.echodream.medhead.Models.Hopital;
import eu.echodream.medhead.Models.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedHeadApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(MedHeadApplicationTests.class);


	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String getUrl(String uri) {
		return "http://localhost:" + port + uri;
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getPatientsTest() {
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/patient"), String.class);
		logger.info("Response Body: {}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void getPatientByIdTest() {
		int testPatientId = 1;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/patient/" + testPatientId), String.class);
		logger.info("Response Body: {}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void getHospitalsTest() {
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/hospital"), String.class);
		logger.info("Response Body: {}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void getHospitalByIdTest() {
		int testHospitalId = 1;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/hospital/" + testHospitalId), String.class);
		logger.info("Response Body: {}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void matchHospitalTest() {
		int testPatientId = 1;
		ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/api/emergency/matchHospital/" + testPatientId), String.class);
		logger.info("Response Body: {}", response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void testCrudHospital() {
		// Ajout d'un hôpital
		Hopital newHospital = new Hopital(0, "Hopital Test", "123 Rue Test", "Ville Test", "12345", 10);
		ResponseEntity<Integer> addResponse = restTemplate.postForEntity(getUrl("/api/hospital"), newHospital, Integer.class);
		assertEquals(HttpStatus.OK, addResponse.getStatusCode());
		assertNotNull(addResponse.getBody());
		logger.info("Response Body: {}", addResponse.getBody());

		// Récupération de l'ID de l'hôpital ajouté
		int hospitalId = addResponse.getBody();

		// Mise à jour de l'hôpital
		newHospital.setNom("Hopital Test MAJ");
		restTemplate.put(getUrl("/api/hospital/" + hospitalId), newHospital);
		ResponseEntity<Hopital> updateResponse = restTemplate.getForEntity(getUrl("/api/hospital/" + hospitalId), Hopital.class);
		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
		assertEquals("Hopital Test MAJ", updateResponse.getBody().getNom());
		logger.info("Response Body: {}", updateResponse.getBody());

		// Suppression de l'hôpital
		restTemplate.delete(getUrl("/api/hospital/" + hospitalId));
		ResponseEntity<Hopital> deleteResponse = restTemplate.getForEntity(getUrl("/api/hospital/" + hospitalId), Hopital.class);
		assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
		logger.info("Response Body: {}", deleteResponse.getBody());
	}

	@Test
	void testCrudPatient() {

		// Ajout d'un patient
		Patient newPatient = new Patient(0, "Test", "Patient", "TestJF", Date.valueOf(LocalDate.of(1990, 1, 1)),
				"123 Rue Test", "Ville Test", "12345", "123456789", -1, 1);
		ResponseEntity<Integer> addResponse = restTemplate.postForEntity(getUrl("/api/patient"), newPatient, Integer.class);
		assertEquals(HttpStatus.OK, addResponse.getStatusCode());
		assertNotNull(addResponse.getBody());
		logger.info("Response Body: {}", addResponse.getBody());

		// Récupération de l'ID du patient ajouté
		int patientId = addResponse.getBody();

		// Mise à jour du patient
		newPatient.setNom("Test MAJ");
		restTemplate.put(getUrl("/api/patient/" + patientId), newPatient);
		ResponseEntity<Patient> updateResponse = restTemplate.getForEntity(getUrl("/api/patient/" + patientId), Patient.class);
		assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
		assertEquals("Test MAJ", updateResponse.getBody().getNom());
		logger.info("Response Body: {}", updateResponse.getBody());

		// Suppression du patient
		restTemplate.delete(getUrl("/api/patient/" + patientId));
		ResponseEntity<Patient> deleteResponse = restTemplate.getForEntity(getUrl("/api/patient/" + patientId), Patient.class);
		assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
		logger.info("Response Body: {}", deleteResponse.getBody());
	}
}
