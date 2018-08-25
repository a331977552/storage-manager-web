package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Category;
import com.storage.entity.CategoryExample;
import com.storage.entity.CategoryExample.Criteria;
import com.storage.entity.Product;
import com.storage.entity.ProductExample;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.CategoryMapper;
import com.storage.mapper.ProductMapper;
import com.storage.service.CategoryService;
import com.storage.utils.JsonUtils;
import com.storage.utils.StringUtils;
import com.storage.utils.jedis.JedisClientPool;

@Service
public class CategoryServiceImp implements  CategoryService{
	@Autowired
	CategoryMapper categoryMapper;
	@Autowired
	ProductMapper productMapper;
	@Autowired
	JedisClientPool jedisPool;
	
	private  static final String REDIS_CACHE="category";
	@Override
	public StorageResult addCategory(Category category){
		if(category==null) {
			return StorageResult.failed("invalid parameter");
		}
		if(StringUtils.isEmpty(category.getName())) {
			return StorageResult.failed("name required!");
		}
		jedisPool.expire(REDIS_CACHE,0);

		this.categoryMapper.insert(category);
		return StorageResult.succeed(category);
	}
	@Override
	public StorageResult updateCategory(Category category ){
		if(category==null ||category.getId()==null ||category.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}
		if(StringUtils.isEmpty(category.getName())) {
			return StorageResult.failed("required!");
		}

		this.categoryMapper.updateByPrimaryKey(category);
		jedisPool.expire(REDIS_CACHE,0);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteCategoryById(Integer id){
		if(id==null || id<0){
			return StorageResult.failed("invalid id");
		}

		ProductExample example=new ProductExample();
		com.storage.entity.ProductExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andCategoryEqualTo(id);

		List<Product> selectByExample = this.productMapper.selectByExample(example);
		if(selectByExample.size()!=0) {
			return StorageResult.failed("there are products under this category, you have to remove those product firstly");
		}

		this.categoryMapper.deleteByPrimaryKey(id);
		jedisPool.expire(REDIS_CACHE,0);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getCategoryByExample(Category  category ){

		
		String string = jedisPool.get(REDIS_CACHE);
		
		if(!StringUtils.isEmpty(string)) {
			return StorageResult.succeed(JsonUtils.jsonToList(string,Category.class));
		}
		CategoryExample example=new CategoryExample();
		Criteria criteria = example.createCriteria();

		if(category.getId()!=null){
			criteria.andIdEqualTo(category.getId());
		}

		if(category.getName()!=null){
			criteria.andNameEqualTo(category.getName());
		}



		List<Category> results = this.categoryMapper.selectByExample(example);
		jedisPool.set(REDIS_CACHE,JsonUtils.objectToJson(results));
	
		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getCategoryById(Integer id){
		if(id==null || id<0){
			return StorageResult.failed("invalid id");
		}

		Category category = this.categoryMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(category);
	}
	@Override
	public StorageResult updateCategorySelective(Category category){
		if(category==null ||category.getId()==null ||category.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}


		this.categoryMapper.updateByPrimaryKeySelective(category);
		jedisPool.expire(REDIS_CACHE,0);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		CategoryExample example=new CategoryExample();
		long count = this.categoryMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}






}




