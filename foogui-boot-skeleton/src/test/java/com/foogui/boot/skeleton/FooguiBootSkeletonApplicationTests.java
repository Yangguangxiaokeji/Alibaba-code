package com.foogui.boot.skeleton;

import com.foogui.boot.skeleton.common.enums.SexEnum;
import com.foogui.boot.skeleton.modules.user.domain.UserPO;
import com.foogui.boot.skeleton.modules.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class FooguiBootSkeletonApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(FooguiBootSkeletonApplicationTests.class);

    @Resource
    private UserMapper userMapper;

    @Test
    public void find() {

        UserPO user = userMapper.selectById(8);

        logger.info("{}", user);
    }

    @Test
    public void insert() {
        UserPO userPO = new UserPO();
        userPO.setUsername("foogui");
        userPO.setPassword("foogui");
        userPO.setSex(SexEnum.MAN);
        userPO.setAge(15);
        userPO.setPhone("18840832314");

        userMapper.insert(userPO);

    }

    /**
     * 乐观锁使用的规则
     * 1. 如果更新数据中不带有version字段：不使用乐观锁，并且version不会累加
     * 2. 如果更新字段中带有version，但与数据库中不一致，更新失败
     * 3. 如果带有version，并且与数据库中一致，更新成功，并且version会累加
     */
    @Test
    public void update() {
        UserPO userPO = new UserPO();
        userPO.setId(12L);
        userPO.setUsername("wangxin");
        userPO.setPassword("wangxin");
        userPO.setSex(SexEnum.MAN);
        userPO.setAge(30);
        userPO.setPhone("18840832314");
        userPO.setVersion(2L);
        userMapper.updateById(userPO);

    }

    @Test
    public void delete() {
        userMapper.deleteById(8);
    }

    @Test
    public void test() {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.replaceAll((k, v) -> {
            if (v.equals("1")) {
                return "";
            }
            return v;
        });
        System.out.println(map);
    }


}
