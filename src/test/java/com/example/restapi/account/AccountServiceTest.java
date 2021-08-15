package com.example.restapi.account;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Rule
    public ExpectedException expectedException=ExpectedException.none();
    @Test
    public void findByUserName() throws Exception{
        //given
        String email = "jun@naver.com";
        String password = "1234";
        Account account = createUser(email, password);
        accountRepository.save(account);
        //when
        UserDetails userDetails = accountService.loadUserByUsername(email);
        //then
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isNotEqualTo(password);
    }
    @Test
    @DisplayName("UserNameNotFoundException 예외 발생 확인")
    public void userNameNotFoundExceptionCheck(){
        String username = "hell@naver.com";
//        expectedException.expect(UsernameNotFoundException.class);
//        expectedException.expectMessage(Matchers.containsString(username));
        //when
        assertThrows(UsernameNotFoundException.class, ()->{
            accountService.loadUserByUsername(username);
        });
    }
    private Account createUser(String email, String password) {
        return Account.builder()
        .email(email)
        .password(passwordEncoder.encode(password))
        .roles(Set.of(AccountRole.ROLE_USER))
        .build();
    }
}