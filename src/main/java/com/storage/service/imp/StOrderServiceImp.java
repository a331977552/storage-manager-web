package com.storage.service.imp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Customer;
import com.storage.entity.Orderitem;
import com.storage.entity.StOrder;
import com.storage.entity.StOrderExample;
import com.storage.entity.StOrderExample.Criteria;
import com.storage.entity.custom.CustomProduct;
import com.storage.entity.custom.OrderWrap;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.OrderitemMapper;
import com.storage.mapper.StOrderMapper;
import com.storage.service.StOrderService;
import com.storage.utils.IDUtils;
@Service
public class StOrderServiceImp implements  StOrderService{
	@Autowired
	StOrderMapper stOrderMapper;
	@Autowired
	OrderitemMapper orderItemMapper;

	@Override
	public StorageResult addStOrder(StOrder stOrder){
		if(stOrder==null)
			return StorageResult.failed("invalid parameter");

		this.stOrderMapper.insert(stOrder);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateStOrder(StOrder stOrder ){
		if(stOrder==null ||stOrder.getId()==null ||stOrder.getId()<0)
			return StorageResult.failed("invalid parameter ");

		this.stOrderMapper.updateByPrimaryKey(stOrder);

		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteStOrderById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");
		this.stOrderMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getStOrderByExample(StOrder  stOrder ){


		StOrderExample example=new StOrderExample();
		Criteria criteria = example.createCriteria();

		if(stOrder.getId()!=null){
			criteria.andIdEqualTo(stOrder.getId());
		}

		if(stOrder.getPayment()!=null){
			criteria.andPaymentEqualTo(stOrder.getPayment());
		}

		if(stOrder.getStatus()!=null){
			criteria.andStatusEqualTo(stOrder.getStatus());
		}

		if(stOrder.getTotalprice()!=null){
			criteria.andTotalpriceEqualTo(stOrder.getTotalprice());
		}

		if(stOrder.getShippingfee()!=null){
			criteria.andShippingfeeEqualTo(stOrder.getShippingfee());
		}

		if(stOrder.getCreatedtime()!=null){
			criteria.andCreatedtimeEqualTo(stOrder.getCreatedtime());
		}

		if(stOrder.getUpdatetime()!=null){
			criteria.andUpdatetimeEqualTo(stOrder.getUpdatetime());
		}

		if(stOrder.getClosedtime()!=null){
			criteria.andClosedtimeEqualTo(stOrder.getClosedtime());
		}

		if(stOrder.getBuyername()!=null){
			criteria.andBuyernameEqualTo(stOrder.getBuyername());
		}

		if(stOrder.getComment()!=null){
			criteria.andCommentEqualTo(stOrder.getComment());
		}

		if(stOrder.getCustomerid()!=null){
			criteria.andCustomeridEqualTo(stOrder.getCustomerid());
		}

		if(stOrder.getShippingtime()!=null){
			criteria.andShippingtimeEqualTo(stOrder.getShippingtime());
		}



		List<StOrder> results = this.stOrderMapper.selectByExample(example);


		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getStOrderById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");

		StOrder stOrder = this.stOrderMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(stOrder);
	}
	@Override
	public StorageResult updateStOrderSelective(StOrder stOrder){
		if(stOrder==null ||stOrder.getId()==null ||stOrder.getId()<0)
			return StorageResult.failed("invalid parameter ");


		this.stOrderMapper.updateByPrimaryKeySelective(stOrder);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		StOrderExample example=new StOrderExample();
		long count = this.stOrderMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}
	@Override
	public OrderWrap creaOrder(Customer customer, OrderWrap result) {
		Integer customerId = customer.getId();

		StOrder record=new StOrder();
		record.setCustomerid(customerId);
		record.setBuyername(customer.getName());
		record.setCreatedtime(new Date());
		long closedTime=System.currentTimeMillis()+1000*60*60;
		record.setClosedtime(new Date(closedTime));
		Double d=result.getTotalPrice()*100;
		int intValue = d.intValue();
		//1 need to be confirmed 3 conceled
		record.setStatus(1);
		record.setTotalprice(intValue);
		record.setUpdatetime(new Date());
		String orderId = IDUtils.getOrderId();
		record.setOrdernumber(orderId);
		this.stOrderMapper.insert(record);
		Integer id = record.getId();
		List<CustomProduct> list = result.getList();

		for (CustomProduct customProduct : list) {
			Orderitem item=new Orderitem();
			item.setOrderid(id);
			item.setProductid(customProduct.getProduct().getId());
			item.setQuantity(customProduct.getQty());
			item.setTotalprice(((Double)(customProduct.getSubtotal()*100)).intValue());
			item.setUnitprice(customProduct.getProduct().getSellingprice());
			orderItemMapper.insert(item);
		}
		result.setOrder(record);
		result.setDate(new Date());



		return result;
	}






}




