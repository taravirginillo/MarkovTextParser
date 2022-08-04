package com.example.demo;

import jdk.jfr.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class TextResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @Value("${app.document-root}")
    String documentRoot;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    List<Path> filesToBeDeleted = new ArrayList<>();

    @Test
    public void test_handleFileUpload() throws Exception {
        String fileName = "test.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "This is the file content".getBytes()
        );
        HashMap<String, Object> body = new HashMap<>();
        body.put("text", sampleFile);
        body.put("prefixSize", 2);

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

        mockMvc.perform(multipartRequest.file(sampleFile))
                .andExpect(status().isOk());

    }

    @Test
    public void testPrefixSizeLargerThanMaxOutputShouldReturnEmpty() throws Exception {
        String fileName = "test.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "she sells".getBytes()
        );

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

        MvcResult mvcResult = mockMvc.perform(multipartRequest.file(sampleFile)
                .param("prefixSize","2"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("",mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void maxOutputSizeIsMaxContentAndReturns200() throws Exception {
        String fileName = "test.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "she sells sea shells by the sea shore".getBytes()
        );

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

        MvcResult mvcResult = mockMvc.perform(multipartRequest.file(sampleFile)
                        .param("prefixSize","1")
                        .param("maxOutputSize","2"))
                .andExpect(status().isOk()).andReturn();

        String[] words = mvcResult.getResponse().getContentAsString().split("\\s+");
        assertEquals(2,words.length);
    }

    public void maxOutputSizeEqualPrefixSizeShouldReturn400() throws Exception {
        String fileName = "test.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "she sells sea shells by the sea shore".getBytes()
        );

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

        mockMvc.perform(multipartRequest.file(sampleFile)
                        .param("prefixSize","2")
                        .param("maxOutputSize","2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNonTextFileNotAllowed() throws Exception {
        String fileName = "test.csv";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/csv",
                "This is the file content".getBytes()
        );
        HashMap<String, Object> body = new HashMap<>();
        body.put("text", sampleFile);
        body.put("prefixSize", 2);

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

        mockMvc.perform(multipartRequest.file(sampleFile))
                .andExpect(status().isBadRequest());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}