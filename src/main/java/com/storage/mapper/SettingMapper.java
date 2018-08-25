package com.storage.mapper;

import com.storage.entity.Setting;
import com.storage.entity.SettingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface SettingMapper {
    long countByExample(SettingExample example);

    int deleteByExample(SettingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Setting record);

    int insertSelective(Setting record);

    List<Setting> selectByExampleWithBLOBs(SettingExample example);

    List<Setting> selectByExample(SettingExample example);

    Setting selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Setting record, @Param("example") SettingExample example);

    int updateByExampleWithBLOBs(@Param("record") Setting record, @Param("example") SettingExample example);

    int updateByExample(@Param("record") Setting record, @Param("example") SettingExample example);

    int updateByPrimaryKeySelective(Setting record);

    int updateByPrimaryKeyWithBLOBs(Setting record);

    int updateByPrimaryKey(Setting record);
}
