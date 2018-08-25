package com.storage.service.imp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.storage.entity.Manager;
import com.storage.entity.ManagerExample;
import com.storage.entity.ManagerExample.Criteria;
import com.storage.entity.custom.StorageResult;
import com.storage.mapper.ManagerMapper;
import com.storage.service.ManagerService;
import com.storage.utils.IDUtils;
@Service
public class ManagerServiceImp implements  ManagerService{
	@Autowired
	ManagerMapper managerMapper;

	@Override
	public StorageResult addManager(Manager manager){
		if(manager==null) {
			return StorageResult.failed("invalid parameter");
		}

		String password = manager.getPassword();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		manager.setPassword(bCryptPasswordEncoder.encode(password.trim()));
		this.managerMapper.insert(manager);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult updateManager(Manager manager ){
		if(manager==null ||manager.getId()==null ||manager.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}

		this.managerMapper.updateByPrimaryKey(manager);

		return StorageResult.succeed();
	}
	@Override
	public StorageResult deleteManagerById(Integer id){
		if(id==null || id<0){
			return StorageResult.failed("invalid id");
		}
		this.managerMapper.deleteByPrimaryKey(id);
		return StorageResult.succeed();
	}
	@Override
	public StorageResult getManagerByExample(Manager  manager ){


		ManagerExample example=new ManagerExample();
		Criteria criteria = example.createCriteria();

		if(manager.getId()!=null){
			criteria.andIdEqualTo(manager.getId());
		}

		if(manager.getUsername()!=null){
			criteria.andUsernameEqualTo(manager.getUsername());
		}

		if(manager.getPassword()!=null){
			criteria.andPasswordEqualTo(manager.getPassword());
		}



		List<Manager> results = this.managerMapper.selectByExample(example);


		return StorageResult.succeed(results);
	}
	@Override
	public StorageResult getManagerById(Integer id){
		if(id==null || id<0){
			return StorageResult.failed("invalid id");
		}

		Manager manager = this.managerMapper.selectByPrimaryKey(id);
		return StorageResult.succeed(manager);
	}
	@Override
	public StorageResult updateManagerSelective(Manager manager){
		if(manager==null ||manager.getId()==null ||manager.getId()<0) {
			return StorageResult.failed("invalid parameter ");
		}


		this.managerMapper.updateByPrimaryKeySelective(manager);
		return  StorageResult.succeed();

	}

	@Override
	public StorageResult count(){
		ManagerExample example=new ManagerExample();
		long count = this.managerMapper.countByExample(example);
		return  StorageResult.succeed(count);
	}
	@Override
	public StorageResult login(Manager manager) {
		if(manager==null) {
			return StorageResult.failed("invalid parameter ");
		}
		ManagerExample example=new ManagerExample();
		Criteria criteria = example.createCriteria();


		if(!StringUtils.isEmpty(manager.getUsername())){
			criteria.andUsernameEqualTo(manager.getUsername());
		}else {
			return StorageResult.failed("empty userName");
		}

		if(!StringUtils.isEmpty(manager.getPassword())){
			criteria.andPasswordEqualTo(manager.getPassword());
		}else {
			return StorageResult.failed("empty password");
		}


		List<Manager> selectByExample = this.managerMapper.selectByExample(example);
		if(selectByExample!=null && selectByExample.size() >0) {
			if(manager.isRemember()) {
				//TODO
				String uuid = IDUtils.getUUID();
				manager.setToken(uuid);
			}
			manager.setPassword(null);
			return StorageResult.succeed(manager);
		}else {
			return StorageResult.failed("username or password is wrong");
		}
	}






}




