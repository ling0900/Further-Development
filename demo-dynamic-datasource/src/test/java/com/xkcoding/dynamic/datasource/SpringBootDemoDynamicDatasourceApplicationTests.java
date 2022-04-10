package com.xkcoding.dynamic.datasource;

import com.xkcoding.dynamic.datasource.controller.UserController;
import com.xkcoding.dynamic.datasource.model.User;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.NewThreadAction;

import java.util.List;
@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDemoDynamicDatasourceApplicationTests {

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
        // List<User> userList = userController.getUserList();
        List<User> userList2 = userController.getUserList2();
        // List<User> userList3 = userController.getUserList3();
        // log.info("默认数据库" + userList);
        log.info("如果成功应该是非默认数据库，结果是：{}",userList2);
        // log.info("原则上是默认数据库" + userList3);
    }

}
