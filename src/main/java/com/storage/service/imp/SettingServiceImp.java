package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storage.entity.Setting;
import com.storage.entity.SettingExample;
import com.storage.entity.SettingExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.SettingMapper;
import com.storage.service.SettingService;
@Service
public class SettingServiceImp implements  SettingService{
	@Autowired
	SettingMapper settingMapper;

	@Override
	public StorageResult addSetting(Setting setting){
		if(setting==null)
			return StorageResult.failed("invalid parameter");

		this.settingMapper.insert(setting);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateSetting(Setting setting ){
		if(setting==null ||setting.getId()==null ||setting.getId()<0)
			return StorageResult.failed("invalid parameter ");

		this.settingMapper.updateByPrimaryKey(setting);

		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteSettingById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");
		this.settingMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getSettingByExample(Setting  setting ){


		SettingExample example=new SettingExample();
		Criteria criteria = example.createCriteria();

		if(setting.getId()!=null){
			criteria.andIdEqualTo(setting.getId());
		}

		if(setting.getCompanyname()!=null){
			criteria.andCompanynameEqualTo(setting.getCompanyname());
		}

		if(setting.getCompanyaddress()!=null){
			criteria.andCompanyaddressEqualTo(setting.getCompanyaddress());
		}

		if(setting.getPostcode()!=null){
			criteria.andPostcodeEqualTo(setting.getPostcode());
		}

		if(setting.getPhone()!=null){
			criteria.andPhoneEqualTo(setting.getPhone());
		}

		if(setting.getName()!=null){
			criteria.andNameEqualTo(setting.getName());
		}

		if(setting.getBusinesshours()!=null){
			criteria.andBusinesshoursEqualTo(setting.getBusinesshours());
		}




		List<Setting> results = this.settingMapper.selectByExample(example);


		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getSettingById(Integer id){
		if(id==null || id<0)
			return StorageResult.failed("invalid id");

		Setting setting = this.settingMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(setting);
	}
	@Override
	public StorageResult updateSettingSelective(Setting setting){
		if(setting==null ||setting.getId()==null ||setting.getId()<0)
			return StorageResult.failed("invalid parameter ");


		this.settingMapper.updateByPrimaryKeySelective(setting);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		SettingExample example=new SettingExample();
		long count = this.settingMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}






}




