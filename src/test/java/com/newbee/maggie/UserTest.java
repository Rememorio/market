package com.newbee.maggie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbee.maggie.entity.User;
import com.newbee.maggie.mapper.UserMapper;
import com.newbee.maggie.web.ApiResult;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testJackSon() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(200);
        apiResult.setMsg("success");
        //Java对象转json
        String rs = objectMapper.writeValueAsString(apiResult);
        System.out.println(rs);
        //json转Java对象
        ApiResult src = objectMapper.readValue(rs, ApiResult.class);
        System.out.println(src);
    }

}
