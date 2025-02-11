package com.henriquernandes.PicPay.controllerTests;

import com.henriquernandes.PicPay.configs.SecurityConfiguration;
import com.henriquernandes.PicPay.controllers.AuthController;
import com.henriquernandes.PicPay.controllers.UserController;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.services.JwtService;
import com.henriquernandes.PicPay.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureTestDatabase
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockitoBean
    UserRepository userRepository;

    @Autowired
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @InjectMocks
    UserController userController;

    private User consumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.consumer = new User("consumer", "consumerName", "53544699028", "consumer@mail.com", "secret", BigDecimal.valueOf(100), UserType.CONSUMER);
        userRepository.save(consumer);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(consumer);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser
    public void testAuthenticatedUserSuccess() throws Exception {
        when(userService.authenticatedUser()).thenReturn(consumer);

        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"firstName\":\"consumer\",\"lastName\":\"consumerName\",\"cpfCnpj\":\"53544699028\",\"email\":\"consumer@mail.com\",\"balance\":100,\"type\":\"CONSUMER\"}"));
    }

    @Test
    public void testAuthenticatedUserNotFound() throws Exception {
        mockMvc.perform(get("/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}