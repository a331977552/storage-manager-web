package com.storage.service.imp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Customer;
import com.storage.entity.CustomerExample;
import com.storage.entity.CustomerExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.CustomerMapper;
import com.storage.service.CustomerService;
import com.storage.utils.StringUtils;

@Service
public class CustomerServiceImp implements  CustomerService{
	@Autowired
	CustomerMapper customerMapper;

	@Override
	public StorageResult addCustomer(Customer customer){
		if(customer==null)
			return StorageResult.failed("invalid parameter");

		//if has ID, then update
		if(customer.getId()!=null && customer.getId()>0) {
			customer.setUpdatetime(new Date());
			customerMapper.updateByPrimaryKeySelective(customer);
			return StorageResult.succeed(customer);
		}

		//null name, return failure
		if(StringUtils.isEmpty(customer.getName()))
			return StorageResult.failed("customer name required");


		//create customer
		customer.setUpdatetime(new Date());
		customer.setCreatetime(new Date());
		this.customerMapper.insert(customer);
		return StorageResult.succeed(customer);
	}
	@Override
	public StorageResult updateCustomer(Customer customer ){
		if(customer==null ||customer.getId()==null ||customer.getId()<0)
			return StorageResult.failed("invalid parameter ");

		this.customerMapper.updateByPrimaryKey(customer);

		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteCustomerById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");
		this.customerMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getCustomerByExample(Customer  customer ){


		CustomerExample example=new CustomerExample();
		Criteria criteria = example.createCriteria();

		if(customer.getId()!=null){
			criteria.andIdEqualTo(customer.getId());
		}

		if(customer.getName()!=null){
			criteria.andNameEqualTo(customer.getName());
		}

		if(customer.getPhone()!=null){
			criteria.andPhoneEqualTo(customer.getPhone());
		}

		if(customer.getAddress()!=null){
			criteria.andAddressEqualTo(customer.getAddress());
		}

		if(customer.getPostcode()!=null){
			criteria.andPostcodeEqualTo(customer.getPostcode());
		}

		if(customer.getCreatetime()!=null){
			criteria.andCreatetimeEqualTo(customer.getCreatetime());
		}

		if(customer.getUpdatetime()!=null){
			criteria.andUpdatetimeEqualTo(customer.getUpdatetime());
		}



		List<Customer> results = this.customerMapper.selectByExample(example);


		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getCustomerById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");

		Customer customer = this.customerMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(customer);
	}
	@Override
	public StorageResult updateCustomerSelective(Customer customer){
		if(customer==null ||customer.getId()==null ||customer.getId()<0)
			return StorageResult.failed("invalid parameter ");


		this.customerMapper.updateByPrimaryKeySelective(customer);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		CustomerExample example=new CustomerExample();
		long count = this.customerMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}
	@Override
	public Object getCustomerList(String order) {
		CustomerExample customerExample=new CustomerExample();
		List<Customer> selectByExample = customerMapper.selectByExample(customerExample);
		return selectByExample;
	}

	@Override
	public StorageResult getCustomerByName(String name) {
		CustomerExample example=new CustomerExample();
		if(StringUtils.isEmpty(name))
			return StorageResult.failed("name required");
		example.createCriteria().andNameEqualTo(name);
		List<Customer> selectByExample = customerMapper.selectByExample(example);


		return null;
	}
	@Override
	public Object List() {
		CustomerExample example=new CustomerExample();

		List<Customer> selectByExample = customerMapper.selectByExample(example);
		return selectByExample;
	}






}




