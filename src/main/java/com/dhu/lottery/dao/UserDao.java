package com.dhu.lottery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dhu.lottery.model.User;

@Repository
public interface UserDao {
	public User findUser(Map<String, Object> pMap);

	public void insertAndGetId(User user);

	public List<User> listUserByParameter(Map<String, Object> pMap);

	public Integer countUserByParameter(Map<String, Object> pMap);

	public void updateUser(User user);

	public void deleteUserById(Long ucid);
	

	
	

}