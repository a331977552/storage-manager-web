package com.storage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.storage.entity.Productimg;
import com.storage.entity.ProductimgExample;
@Mapper
public interface ProductimgMapper {
	long countByExample(ProductimgExample example);

	int deleteByExample(ProductimgExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(Productimg record);

	int insertSelective(Productimg record);

	List<Productimg> selectByExample(ProductimgExample example);

	Productimg selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") Productimg record, @Param("example") ProductimgExample example);

	int updateByExample(@Param("record") Productimg record, @Param("example") ProductimgExample example);

	int updateByPrimaryKeySelective(Productimg record);

	int updateByPrimaryKey(Productimg record);
}
