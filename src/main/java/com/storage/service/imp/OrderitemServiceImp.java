package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Orderitem;
import com.storage.entity.OrderitemExample;
import com.storage.entity.OrderitemExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.OrderitemMapper;
import com.storage.service.OrderitemService;
@Service
public class OrderitemServiceImp implements  OrderitemService{
	@Autowired
	OrderitemMapper orderitemMapper;
	
	@Override
	public StorageResult addOrderitem(Orderitem orderitem){
		if(orderitem==null) {
			return StorageResult.failed("invalid parameter");
		}
	
		this.orderitemMapper.insert(orderitem);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateOrderitem(Orderitem orderitem ){
		if(orderitem==null ||orderitem.getId()==null ||orderitem.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}
	
		this.orderitemMapper.updateByPrimaryKey(orderitem);
		
		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteOrderitemById(Integer id){
		if(id==null || id<0){
		  	return StorageResult.failed("invalid id");
		}
		this.orderitemMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getOrderitemByExample(Orderitem  orderitem ){
		
		
		OrderitemExample example=new OrderitemExample();
		Criteria criteria = example.createCriteria();
		
			if(orderitem.getId()!=null){
				criteria.andIdEqualTo(orderitem.getId());
			}
		
			if(orderitem.getProductid()!=null){
				criteria.andProductidEqualTo(orderitem.getProductid());
			}
		
			if(orderitem.getOrderid()!=null){
				criteria.andOrderidEqualTo(orderitem.getOrderid());
			}
		
			if(orderitem.getQuantity()!=null){
				criteria.andQuantityEqualTo(orderitem.getQuantity());
			}
		
			if(orderitem.getUnitprice()!=null){
				criteria.andUnitpriceEqualTo(orderitem.getUnitprice());
			}
		
			if(orderitem.getTotalprice()!=null){
				criteria.andTotalpriceEqualTo(orderitem.getTotalprice());
			}
		
			if(orderitem.getPic()!=null){
				criteria.andPicEqualTo(orderitem.getPic());
			}
		
		

		List<Orderitem> results = this.orderitemMapper.selectByExample(example);
		
		
		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getOrderitemById(Integer id){
		if(id==null || id<0){
		  	return StorageResult.failed("invalid id");
		}
		
		Orderitem orderitem = this.orderitemMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(orderitem);
	}
	@Override
	public StorageResult updateOrderitemSelective(Orderitem orderitem){
		if(orderitem==null ||orderitem.getId()==null ||orderitem.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}
		
		
		this.orderitemMapper.updateByPrimaryKeySelective(orderitem);
		return  StorageResult.succeed();
	
	}
	
	@Override
	public StorageResult count(){
		OrderitemExample example=new OrderitemExample();
		long count = this.orderitemMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}
	


	


}




