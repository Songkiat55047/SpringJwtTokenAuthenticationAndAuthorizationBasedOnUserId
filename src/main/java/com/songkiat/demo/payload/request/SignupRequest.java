package com.songkiat.demo.payload.request;

import java.util.Set;

import com.songkiat.demo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SignupRequest {
	
	  private String username;
	  
	  private String empname;
	  
	  private Double amount;

	  private String email;
	  
	  private Set<String> role;

	  private String password;
	  
	  private User manager;

}
