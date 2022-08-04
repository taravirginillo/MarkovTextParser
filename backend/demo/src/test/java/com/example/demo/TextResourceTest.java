package com.example.demo;

import jdk.jfr.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class TextResourceTest {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();


    @Test
    public void greetingShouldReturnDefaultMessage() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/hello"),
                HttpMethod.GET, entity, String.class);
        assertEquals("Hello World!", response.getBody());
    }

    @Test
    public void nullPrefixSizeThen400IsReceived() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("she sells sea shells", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?maxOutputSize=10"),
                HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void nullMaxOutputSizeThen200IsReceived() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("she sells sea shells", headers);
        Map<String, Integer> params = new HashMap<>();
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=2"),
                HttpMethod.PUT, entity, String.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void prefixSizeLargerThanTextSizeThen400IsReceived() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("she sells sea shells", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=10"),
                HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void maxOutputSizeLessThanPrefixSizeThen400IsReceived() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("she sells sea shells", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=3&maxOutputSize=2"),
                HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void maxOutputSizeEqualToPrefixSizeThen400IsReceived() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("she sells sea shells", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=2&maxOutputSize=2"),
                HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void prefixSizeEqualTextLengthThen200InputTextReturned() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("this is the test", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=4&maxOutputSize=5"),
                HttpMethod.PUT, entity, String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("this is the test", response.getBody());
    }

    @Test
    public void prefixLessThanTextLessThanMaxOutputThen200Returned() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>("this is the test", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=1&maxOutputSize=5"),
                HttpMethod.PUT, entity, String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void nullTextThen400BadRequestReturned() {
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=1&maxOutputSize=5"),
                HttpMethod.PUT, entity, String.class);
        System.out.println(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void
    givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsTextPlain() {
        HttpEntity<String> entity = new HttpEntity<>("this is a test", null);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/text?prefixSize=1"),
                HttpMethod.PUT, entity, String.class);

        MediaType mimeType = response.getHeaders().getContentType();
        assertEquals( MediaType.TEXT_PLAIN, mimeType );
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}