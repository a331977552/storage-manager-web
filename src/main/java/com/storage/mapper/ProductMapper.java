package com.storage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.storage.entity.Product;
import com.storage.entity.ProductExample;
import com.storage.entity.custom.CustomeProductName;
@Mapper
public interface ProductMapper {
	long countByExample(ProductExample example);

	int deleteByExample(ProductExample example);

	int deleteByPrimaryKey(Integer id);

	int insert(Product record);

	int insertSelective(Product record);

	List<Product> selectByExampleWithBLOBs(ProductExample example);

	List<Product> selectByExample(ProductExample example);

	Product selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductExample example);

	int updateByExampleWithBLOBs(@Param("record") Product record, @Param("example") ProductExample example);

	int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

	int updateByPrimaryKeySelective(Product record);

	int updateByPrimaryKeyWithBLOBs(Product record);

	int updateByPrimaryKey(Product record);

	List<CustomeProductName> selectNameByExample(ProductExample example);
}
