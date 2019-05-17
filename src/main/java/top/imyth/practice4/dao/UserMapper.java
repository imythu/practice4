package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;import org.springframework.stereotype.Repository;
import top.imyth.practice4.entity.User;
import top.imyth.practice4.entity.combination.FocusUser;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Map<String, Object> selectPasswordByPhoneNumber(@Param("phoneNumber")String phoneNumber);

    String selectHeadImageUrlByUserId(@Param("userId")Long userId);

    List<FocusUser> selectFocusUserByUserId(@Param("userId")Long userId);

    User selectByPrimaryPhoneNumber(@Param("phoneNumber")String phoneNumber);
}