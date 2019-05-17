package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;
import top.imyth.practice4.entity.Image;

public interface ImageMapper {
    int deleteByPrimaryKey(@Param("imageId") Long imageId, @Param("imageUrl") String imageUrl);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(@Param("imageId") Long imageId, @Param("imageUrl") String imageUrl);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);
}