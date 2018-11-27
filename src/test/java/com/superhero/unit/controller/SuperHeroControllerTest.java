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

    @Test
    public void getAllUsers() throws Exception {
    }

    @Test
    public void getUsersById() throws Exception {
    }

    @Test
    public void removeUsersById() throws Exception {
    }
}
