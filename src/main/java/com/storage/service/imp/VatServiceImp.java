package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Vat;
import com.storage.entity.VatExample;
import com.storage.entity.VatExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.VatMapper;
import com.storage.service.VatService;
@Service
public class VatServiceImp implements  VatService{
	@Autowired
	VatMapper vatMapper;

	@Override
	public StorageResult addVat(Vat vat){
		if(vat==null)
			return StorageResult.failed("invalid parameter");

		this.vatMapper.insert(vat);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateVat(Vat vat ){
		if(vat==null ||vat.getId()==null ||vat.getId()<0)
			return StorageResult.failed("invalid parameter ");

		this.vatMapper.updateByPrimaryKey(vat);

		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteVatById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");
		this.vatMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getVatByExample(Vat  vat ){


		VatExample example=new VatExample();
		Criteria criteria = example.createCriteria();

		if(vat.getId()!=null){
			criteria.andIdEqualTo(vat.getId());
		}

		if(vat.getPrice()!=null){
			criteria.andPriceEqualTo(vat.getPrice());
		}

		if(vat.getRate()!=null){
			criteria.andRateEqualTo(vat.getRate());
		}



		List<Vat> results = this.vatMapper.selectByExample(example);


		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getVatById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");

		Vat vat = this.vatMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(vat);
	}
	@Override
	public StorageResult updateVatSelective(Vat vat){
		if(vat==null ||vat.getId()==null ||vat.getId()<0)
			return StorageResult.failed("invalid parameter ");


		this.vatMapper.updateByPrimaryKeySelective(vat);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		VatExample example=new VatExample();
		long count = this.vatMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}






}




