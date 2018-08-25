package com.storage.mapper;

import com.storage.entity.StOrder;
import com.storage.entity.StOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface StOrderMapper {
    long countByExample(StOrderExample example);

    int deleteByExample(StOrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StOrder record);

    int insertSelective(StOrder record);

    List<StOrder> selectByExample(StOrderExample example);

    StOrder selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StOrder record, @Param("example") StOrderExample example);

    int updateByExample(@Param("record") StOrder record, @Param("example") StOrderExample example);

    int updateByPrimaryKeySelective(StOrder record);

    int updateByPrimaryKey(StOrder record);
}
