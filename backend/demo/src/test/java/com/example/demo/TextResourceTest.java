package com.example.demo;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DemoApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class TextResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${app.document-root}")
    String documentRoot;

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
    public void testPrefixSizeLargerThanTextSizeShouldReturnEmpty() throws Exception {
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
                        .param("prefixSize","4"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("",mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void maxOutputSizeIsOutputContentLengthAndReturns200() throws Exception {
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

    @Test
    public void testFileSizeTooLargeReturn500() throws Exception {
        String fileName = "test.txt";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                "she sells sea shells ".repeat(10000).getBytes()
        );

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/text");

         mockMvc.perform(multipartRequest.file(sampleFile)
                        .param("prefixSize","2"))
                .andExpect(status().isBadRequest());

    }

    @Test
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
}