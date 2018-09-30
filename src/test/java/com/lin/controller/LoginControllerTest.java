package com.lin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin.BaseControllerTest;
import com.lin.BaseTest;
import com.lin.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lkmc2
 * @date 2018/9/30
 * @description 登陆控制器测试
 */
public class LoginControllerTest extends BaseControllerTest {

    @Test
    public void login() throws Exception {
        User user = new User();
        user.setUsername("Java");
        user.setPassword("123456");

        String requestJson = JSONObject.toJSONString(user);

        String responseString = mockMvc.perform
                (
                        MockMvcRequestBuilders
                        .post("http://127.0.0.1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("Java"))
                .andExpect(jsonPath("$.data.password").value(""))
                .andExpect(jsonPath("$.data.userToken", notNullValue()))
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
    }

}