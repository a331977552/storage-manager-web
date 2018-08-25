package com.storage.mapper;

import com.storage.entity.Vat;
import com.storage.entity.VatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface VatMapper {
    long countByExample(VatExample example);

    int deleteByExample(VatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Vat record);

    int insertSelective(Vat record);

    List<Vat> selectByExample(VatExample example);

    Vat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Vat record, @Param("example") VatExample example);

    int updateByExample(@Param("record") Vat record, @Param("example") VatExample example);

    int updateByPrimaryKeySelective(Vat record);

    int updateByPrimaryKey(Vat record);
}
