package io.bondopangaji.springsecurityjwt.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bondopangaji.springsecurityjwt.user.model.Role;
import io.bondopangaji.springsecurityjwt.user.model.User;
import io.bondopangaji.springsecurityjwt.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@ContextConfiguration(classes = {UserController.class})
@ActiveProfiles({"IntegrationTesting"})
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSaveUser() throws Exception {
        User user = new User();
        user.setId(123L);
        user.setName("Bondo Pangaji");
        user.setUsername("bondo");
        user.setEmail("bondo@gmail.com");
        user.setPassword("password");
        user.setRoles(new ArrayList<>());

        when(userService.saveUser(user)).thenReturn(user);

        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.userController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(content))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/api/user/save"));
    }

    @Test
    public void testSaveRole() throws Exception {
        Role role = new Role();
        role.setId(123L);
        role.setName("ROLE_ADMIN");

        when(userService.saveRole(role)).thenReturn(role);

        String content = (new ObjectMapper()).writeValueAsString(role);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/role/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvc buildResult = MockMvcBuilders.standaloneSetup(this.userController).build();

        ResultActions actualPerformResult = buildResult.perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(content))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/api/role/save"));
    }

    @Test
    public void testSaveRoleToUser() {
        String username = "bondo";
        String roleName = "ROLE_ADMIN";

        UserController.RoleToUser roleToUser = new UserController.RoleToUser();
        roleToUser.setUsername(username);
        roleToUser.setRoleName(roleName);

        userController.saveRoleToUser(roleToUser);

        verify(userService, times(1)).saveRoleToUser(username, roleName);
    }

    @Test
    public void testGetUser() {
        List<User> users = new ArrayList<>();
        users.add(new User().setId(1L).setName("Bondo").setUsername("bondo").setEmail("bondo@gmail.com"));
        users.add(new User().setId(2L).setName("Bondo").setUsername("bondo").setEmail("bondo@gmail.com"));
        users.add(new User().setId(3L).setName("Bondo").setUsername("bondo").setEmail("bondo@gmail.com"));

        when(userService.getUsers()).thenReturn(users);

        ResponseEntity<List<User>> responseEntity = userController.getUser();

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(3, Objects.requireNonNull(responseEntity.getBody()).size());
    }
}