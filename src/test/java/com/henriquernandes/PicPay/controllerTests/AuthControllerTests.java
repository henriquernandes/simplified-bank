package com.henriquernandes.PicPay.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henriquernandes.PicPay.controllers.AuthController;
import com.henriquernandes.PicPay.dtos.LoginUserDTO;
import com.henriquernandes.PicPay.dtos.UserDTO;
import com.henriquernandes.PicPay.entities.User;
import com.henriquernandes.PicPay.enums.UserType;
import com.henriquernandes.PicPay.repositories.UserRepository;
import com.henriquernandes.PicPay.services.JwtService;
import com.henriquernandes.PicPay.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@WithMockUser
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() throws Exception {
        UserDTO userDTO = new UserDTO(
                "Henrique",
                "Ernandes",
                "53544699028",
                "consumer@mail.com",
                "secret",
                BigDecimal.ZERO,
                UserType.CONSUMER
        );

        User user = new User(userDTO);

        when(userService.findByCpfCnpj(user.getCpfCnpj())).thenReturn(user);


        mockMvc.perform(post("/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(UserDTO.class));

        User result = userService.findByCpfCnpj(user.getCpfCnpj());

        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getCpfCnpj(), result.getCpfCnpj());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void testAuthenticate() throws Exception {
        LoginUserDTO loginUserDTO = new LoginUserDTO("consumer@mail.com", "secret");

        User user = new User();
        user.setEmail("consumer@mail.com");
        user.setPassword("secret");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userService.authenticate(any(LoginUserDTO.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("token");

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUserWithErrors() throws Exception {
        UserDTO userDTO = new UserDTO(
                "",
                "",
                null,
                "",
                null,
                BigDecimal.ZERO,
                UserType.CONSUMER
        );

        mockMvc.perform(post("/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }
}