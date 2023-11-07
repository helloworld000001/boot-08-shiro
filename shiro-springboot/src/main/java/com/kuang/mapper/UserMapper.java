package com.kuang.mapper;

import com.kuang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @auther 陈彤琳
 * @Description $
 * 2023/11/6 19:01
 */
@Repository
@Mapper
public interface UserMapper {
     User queryUserByName(String name);
}
