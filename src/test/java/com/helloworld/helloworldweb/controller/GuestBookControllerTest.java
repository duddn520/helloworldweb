package com.helloworld.helloworldweb.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.dto.guestbook.GuestBookRequestDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = GuestBookController.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GuestBookControllerTest {

    @Autowired ObjectMapper mapper;
    @Autowired private MockMvc mvc;
    @Autowired JwtTokenProvider jwtTokenProvider;



    private String generateToken(){
        return jwtTokenProvider.createToken("wlsdn1372@hanmail.net", Role.USER);
    }

//    @Test
//    void getGuestBooks() throws Exception {
//        mvc.perform(get("/api/guestbook")
//            .header("Auth",generateToken()))
//                .andExpect(status().isOk());
//    }

    @Test
    void registerGuestBook() throws Exception {
        String body = mapper.writeValueAsString(
                GuestBookRequestDto.builder().targetUserEmail("wlsdn1372@hanmail.net").content("test").build()
        );
        mvc.perform(post("/api/guestbook")
                .header("Auth",generateToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                    .andExpect(status().isOk());
    }

//    @Test
//    void updateGuestBook() throws Exception{
//
//        String body = mapper.writeValueAsString(
//                GuestBookRequestDto.builder().id(11L).targetUserEmail("wlsdn1372@hanmail.net").content("test").build()
//        );
//        mvc.perform(put("/api/guestbook")
//                .header("Auth", generateToken())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body))
//                    .andExpect(status().isOk());
//    }

//    @Test
//    void deleteGuestBook() throws Exception {
//        mvc.perform(delete("/api/guestbook")
//                .header("Auth", generateToken())
//                .param("id","11")
//                .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk());
//    }
}
