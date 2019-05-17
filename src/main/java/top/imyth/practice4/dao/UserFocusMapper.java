package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;
import top.imyth.practice4.entity.UserFocus;

public interface UserFocusMapper {
    int deleteByPrimaryKey(@Param("myUserId") Long myUserId, @Param("focusUserId") Long focusUserId);

    int insert(UserFocus record);

    int insertSelective(UserFocus record);

    UserFocus selectByPrimaryKey(@Param("myUserId") Long myUserId, @Param("focusUserId") Long focusUserId);

    int updateByPrimaryKeySelective(UserFocus record);

    int updateByPrimaryKey(UserFocus record);
}