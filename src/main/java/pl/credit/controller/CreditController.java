package pl.credit.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import pl.credit.dao.CreditRepository;
import pl.credit.model.Credit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class CreditController {
	protected final Logger log = Logger.getLogger(getClass().getName());

	@Autowired // Na potrzeby obsługi bazy
	CreditRepository creditRepository;

	// Namiary na enpointy pozostalych komponentow
	final String productURI = "http://productcontainer:8081/";
	final String customerURI = "http://customercontainer:8082/";

	RestTemplate restTemplate = new RestTemplate(); // Do wysylki poprzez REST
	HttpHeaders headers = new HttpHeaders(); // Do wysylki poprzez REST

	
	// Metoda tworzaca credyt
	@RequestMapping(value = "/createCredit", method = RequestMethod.POST)
	public String createCredit(@RequestBody String payload) throws Exception {

		// Tworzenie JSON
		GsonBuilder builder = new GsonBuilder();
		builder.setVersion(2.0);

		// do mapowania JSON na Credit
		Credit credit = new Credit();
		log.log(Level.INFO, "Wygenerowany numer to: " + credit);

		// Rozbicie komunikatu przychodzacego na obiekty JSON
		JsonParser parser = new JsonParser();
		JsonElement jsonTree = parser.parse(payload);
		

		if (jsonTree.isJsonObject()) {
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			JsonElement clientElement = jsonObject.get("client"); //
			JsonElement productElement = jsonObject.get("product");
			JsonElement creditElement = jsonObject.get("credit");

			// dodanie numeru kredytu do JSON elemtnow:
			productElement.getAsJsonObject().addProperty("creditNumber", credit.getCredit_id());
			clientElement.getAsJsonObject().addProperty("creditNumber", credit.getCredit_id());

			// POST TO URI PRODUCT
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entityProduct = new HttpEntity<String>(productElement.toString(), headers); // wysyłka pod URL
			String answerProductPost = restTemplate.postForObject(productURI + "createProduct", entityProduct, String.class);
			log.log(Level.INFO, "Odpowiedz serwisu product: " + answerProductPost);

			// POST TO URI CUSTOMER
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entityCustomer = new HttpEntity<String>(clientElement.toString(), headers); // wysyłka pod URL
			String answerCustomerPost = restTemplate.postForObject(customerURI + "createCustomer", entityCustomer, String.class);
			log.log(Level.INFO, "Odpowiedz serwisu customer: " + answerCustomerPost);
		}
		log.log(Level.INFO, "Zapisuje info o kredycie do bazy: " + credit);
		// Zapis informacji o kredycie do bazy
		creditRepository.save(credit);

		return "Przydzielony numer kredytu to: " + credit.getCredit_id();
	}
	
	
	// metoda zwracajaca wszystkie utworzone kredyty
	@RequestMapping("/getCredit")
	public String getCredit() {
		List<Credit> listOfAllCredits = new ArrayList<Credit>(); // Lista do ktorej zostana zapisane wsztskie kredyty
		listOfAllCredits = creditRepository.findAll(); // Pobranie wszystkich danych z bazy i przypisanie do listy
		Set<Integer> setCreditNumbers = new HashSet<>(); // zbiór tylko numerów kredytów

		Gson gson = new Gson();
		JsonArray creditJsonArr = new JsonArray();

		// Parsowanie obiektu credit do JSON
		for (Credit credit : listOfAllCredits) {
			setCreditNumbers.add(credit.getCredit_id());

			JsonObject obj = new JsonObject();
			obj.addProperty("creditNumber", credit.getCredit_id());
			creditJsonArr.add(obj);

		}
		log.log(Level.INFO, "Zawartosc setCreditNumbers: " + setCreditNumbers);

		// GET TO URI CUSTOMER
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entityListOfCreditNumbers = new HttpEntity<String>(setCreditNumbers.toString(), headers);
		String clients = restTemplate.postForObject(customerURI + "getCustomers", entityListOfCreditNumbers, String.class);
		log.log(Level.INFO, "Odpowiedz serwisu client: " + clients);
		

		// GET TO URI PRODUCT
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entityListOfProducts = new HttpEntity<String>(setCreditNumbers.toString(), headers); // wysyłka pod URL
		String products = restTemplate.postForObject(productURI + "getProduct", entityListOfProducts, String.class);
		log.log(Level.INFO, "Odpowiedz serwisu product: " + products);


		
		// parsowane całości
		JsonParser parser = new JsonParser(); // konwersja string do JSON ARRAY

		JsonElement clientElement = parser.parse(clients);
		JsonArray clientJsonArray = clientElement.getAsJsonArray();

		JsonElement producElement = parser.parse(products);
		JsonArray productJsonArray = producElement.getAsJsonArray();

		JsonArray outputArray = new JsonArray();
		JsonObject addObjectAll = new JsonObject();
		// petla do powiazania wartosci
		for (Credit c : listOfAllCredits) {
			JsonObject addObject = new JsonObject();
			for (JsonElement client : clientJsonArray) {
				//JsonObject setClientName = new JsonObject();
				JsonObject clientObject = client.getAsJsonObject(); // objekt produktu jako JSON

				if (clientObject.get("creditNumber").toString().equals(Integer.toString(c.getCredit_id()))) {
					// Ustawia nazwę client dla obiektu poziomu wyzej
					addObject.add("client", clientObject);
				}
			}

			for (JsonElement product : productJsonArray) {
				//JsonObject setProductName = new JsonObject();
				JsonObject procutObject = product.getAsJsonObject(); // objekt produktu jako JSON

				if (procutObject.get("creditNumber").toString().equals(Integer.toString(c.getCredit_id()))) {
					// Ustawia nazwę product dla obiektu poziomu wyzej
					addObject.add("product", procutObject);
				}
			}
			for (JsonElement credit : creditJsonArr) {
				//JsonObject setCreditName = new JsonObject();
				JsonObject creditObject = credit.getAsJsonObject(); // objekt produktu jako JSON

				if (creditObject.get("creditNumber").toString().equals(Integer.toString(c.getCredit_id()))) {
					// Ustawia nazwę credit dla obiektu poziomu wyzej
					addObject.add("credit", creditObject);
				}
			}
			
			outputArray.add(addObject);
		}
		String menuJson = gson.toJson(outputArray); 	//parsowanie tablicy na JSON
		return "Info o numerach wszystkich kredytów: \n" + menuJson;
	}
}
