package dl.news.portal.web.config.security.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.news.portal.domain.dto.UserDto;
import dl.news.portal.domain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
    private static final String GENERATE_TOKEN_PATH = "/token/generate";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean()
    private AuthenticationManager authenticationManager;

    @Test
    public void getAuthToken_whenCredentialsIsCorrect_returnOk() throws Exception {
        final String username = "testUser";
        final String password = "password";
        final String token = "IT.is.ToKeN";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        Mockito.when(authenticationManager.authenticate(
                any(Authentication.class)))
                .thenReturn(authentication);
        Mockito.when(tokenProvider.generateAccessToken(authentication)).thenReturn(token);
        mockMvc.perform(post(GENERATE_TOKEN_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token));
    }

    @Test
    public void getAuthToken_whenUserNotAuthenticated_returnIsUnauthorized() throws Exception {
        final String username = "testUser";
        final String password = "password";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String body = mapper.writeValueAsString(dto);

        Mockito.when(authenticationManager.authenticate(authentication)).thenThrow(BadCredentialsException.class);
        mockMvc.perform(post(GENERATE_TOKEN_PATH)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}