package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Productimg;
import com.storage.entity.ProductimgExample;
import com.storage.entity.ProductimgExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.ProductimgMapper;
import com.storage.service.ProductimgService;
@Service
public class ProductimgServiceImp implements  ProductimgService{
	@Autowired
	ProductimgMapper productimgMapper;
	
	@Override
	public StorageResult addProductimg(Productimg productimg){
		if(productimg==null) {
			return StorageResult.failed("invalid parameter");
		}
	
		this.productimgMapper.insert(productimg);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateProductimg(Productimg productimg ){
		if(productimg==null ||productimg.getId()==null ||productimg.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}
	
		this.productimgMapper.updateByPrimaryKey(productimg);
		
		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteProductimgById(Integer id){
		if(id==null || id<0){
		  	return StorageResult.failed("invalid id");
		}
		this.productimgMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getProductimgByExample(Productimg  productimg ){
		
		
		ProductimgExample example=new ProductimgExample();
		Criteria criteria = example.createCriteria();
		
			if(productimg.getId()!=null){
				criteria.andIdEqualTo(productimg.getId());
			}
		
			if(productimg.getProdcutid()!=null){
				criteria.andProdcutidEqualTo(productimg.getProdcutid());
			}
		
			if(productimg.getUrl()!=null){
				criteria.andUrlEqualTo(productimg.getUrl());
			}
		
		

		List<Productimg> results = this.productimgMapper.selectByExample(example);
		
		
		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getProductimgById(Integer id){
		if(id==null || id<0){
		  	return StorageResult.failed("invalid id");
		}
		
		Productimg productimg = this.productimgMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(productimg);
	}
	@Override
	public StorageResult updateProductimgSelective(Productimg productimg){
		if(productimg==null ||productimg.getId()==null ||productimg.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}
		
		
		this.productimgMapper.updateByPrimaryKeySelective(productimg);
		return  StorageResult.succeed();
	
	}
	
	@Override
	public StorageResult count(){
		ProductimgExample example=new ProductimgExample();
		long count = this.productimgMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}
	


	


}




