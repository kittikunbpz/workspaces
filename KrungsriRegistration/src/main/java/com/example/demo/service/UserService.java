package com.example.demo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Transactional
@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	public List<User> getAllUserInfo() {
		return repo.findAll();
	}

	public User getUserInfo(String username) {
		return repo.findByUsername(username);
	}

	public boolean save(User entity) {
		boolean isSaved;
		try {
			repo.save(entity);
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}
		return isSaved;
	}

	public boolean saveAll(List<User> entities) {
		boolean isSaved;
		try {
			repo.saveAll(entities);
			isSaved = true;
		} catch (Exception e) {
			isSaved = false;
		}
		return isSaved;
	}
}
