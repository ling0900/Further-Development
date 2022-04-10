package com.xkcoding.dynamic.datasource.controller;

import com.xkcoding.dynamic.datasource.annotation.DatasourceDynamic;
import com.xkcoding.dynamic.datasource.mapper.UserMapper;
import com.xkcoding.dynamic.datasource.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * <p>
 * 用户 Controller
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-09-04 16:40
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserMapper userMapper;

    /**
     * 获取用户列表
     */
    @GetMapping("/user")
    public List<User> getUserList() {
        List<User> users = userMapper.selectAll();
        System.out.println(users);

        return users;
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/user2")
    @DatasourceDynamic(configId = 2L)
    public List<User> getUserList2() {
        List<User> users = userMapper.selectAll();
        System.out.println(users);
        return users;
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/user3")
    @DatasourceDynamic
    public List<User> getUserList3() {
        List<User> users = userMapper.selectAll();
        System.out.println(users);

        return users;
    }

}
