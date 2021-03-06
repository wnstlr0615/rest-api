package com.example.restapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import({RestDocsConfiguration.class})
@ActiveProfiles("test")
@Disabled
public class BaseControllerTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected ModelMapper modelMapper;
}
