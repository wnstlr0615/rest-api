package com.example.restapi.config;

import com.example.restapi.account.Account;
import com.example.restapi.account.AccountRole;
import com.example.restapi.account.AccountService;
import com.example.restapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigControllerTest extends BaseControllerTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    @Description("인증 토큰을 발급 받는 테스트")
    public void getAuthToken () throws Exception{
        String username="jun@naver.com";
        String password="joon";
        Account account = createAccount(username, password);
        accountService.createUser(account);
        //given
        String clientId="myApp";
        String clientSecret="pass";
        mvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                ;

        //when

        //then
    }

    private Account createAccount(String username, String password) {
        return Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.USER))
                .build();
    }
}