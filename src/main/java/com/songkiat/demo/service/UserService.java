package com.songkiat.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.songkiat.demo.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Double findAmountById(Long id) {
		return userRepository.findAmountById(id);
	}
	
	public Double findTotalAmountByPersonId(Long id){
		return userRepository.findTotalAmountByPersonId(id);
	}
	
	public Double findTotalAmountDirectedToPersonAndChain(Long id) {
	    return userRepository.findTotalAmountDirectedToPersonAndChain(id);
	}


}
