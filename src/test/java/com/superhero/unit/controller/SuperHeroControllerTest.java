package com.superhero.unit.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.superhero.rest.controller.SuperHeroController;

@RunWith(SpringRunner.class)
@WebMvcTest(SuperHeroController.class)
public class SuperHeroControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SuperHeroController superHeroController;
    

//    private Users createUser() {
//        Users users = new Users();
//        users.setName("BigG");
//        users.setAddress("Glandale road 4");
//        users.setCity("Glandale");
//        users.setState("California");
//        users.setZipCode("AD2123");
//        users.setCardType("master");
//        users.setCardNumber("2864528645765429346");
//        users.setCardExpirationMonth(6);
//        users.setCardExpirationYear(2020);
//        users.setCardNameOnCard("BigGInDaHouse");
//        users.setFlightId(12);
//
//        return users;
//    }

    @Test
    public void getAllUsers() throws Exception {
//        Users user = createUser();
//
//        List<Users> users = singletonList(user);
//
//        given(usersController.getAllUsers()).willReturn(users);
//
//        mvc.perform(get(VERSION + USERS + "all")
//                .with(user("blaze").password("Q1w2e3r4"))
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].address", is(user.getAddress())));
    }

    @Test
    public void getUsersById() throws Exception {
//        Users user = createUser();
//
//        given(usersController.getUsersById(user.getId())).willReturn(user);
//
//        mvc.perform(get(VERSION + USERS + user.getId())
//                .with(user("blaze").password("Q1w2e3r4"))
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("address", is(user.getAddress())));
    }

    @Test
    public void removeUsersById() throws Exception {
//        Users user = createUser();
//
//        mvc.perform(delete(VERSION + USERS + user.getId())
//                .with(user("blaze").password("Q1w2e3r4"))
//                .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk());
    }
}
