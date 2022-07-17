package com.bochkarev.restbackend.controller;

import com.bochkarev.restbackend.domain.LoginInfo;
import com.bochkarev.restbackend.domain.UserInfo;
import com.bochkarev.restbackend.exception.InvalidUsernameException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class BankController {

    private Map<String, UserInfo> users = new HashMap<>();
    {
       users.put("Dima", UserInfo.builder().userName("Dima").build());
       users.put("Olga", UserInfo.builder().userName("Olga").build());
       users.put("Ivan", UserInfo.builder().userName("Ivan").build());
    }

    @PostMapping("user/login")
    @ApiOperation("авторизация")
    public UserInfo doLogin(@RequestBody LoginInfo loginInfo) {
        if (loginInfo.getUserName().equals("Dima")) {
            return UserInfo.builder()
                    .loginDate(new Date())
                    .userName(loginInfo.getUserName())
                    .build();
        } else {
            throw new InvalidUsernameException();
        }
    }

    @GetMapping("user/getall")
    @ApiOperation("Получение всех юзеров")
    public List<UserInfo> getAllUserInfo () {
        return users.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


}
