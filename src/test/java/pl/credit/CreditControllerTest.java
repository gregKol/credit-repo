//package pl.credit;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import pl.credit.controller.CreditController;
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(CreditController.class)
//public class CreditControllerTest {

//	@Autowired
//	private MockMvc mockMvc;
//
//	String postJson = "{\"client\":{\"firstname\":\"Franek2\",\"surname\":\"Dolas2\",\"pesel\":\"43258123984\"},\"product\": {\"productName\":\"Produkt 12\",\"productValue\":111},\"credit\":{\"creditName\":\"NazwaKredytu1 2\"}}";
//
//	@Test
//	public void retrieveDetailsForCourse() throws Exception {
//
//		Mockito.when(studentService.retrieveCourse(Mockito.anyString(), Mockito.anyString())).thenReturn(mockCourse);
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/students/Student1/courses/Course1")
//				.accept(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//		System.out.println(result.getResponse());
//		String expected = "{id:Course1,name:Spring,description:10 Steps}";
//
//		// {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K
//		// Students","steps":["Learn Maven","Import Project","First Example","Second
//		// Example"]}
//
//		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
//	}

//}
