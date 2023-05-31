package com.songkiat.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
	
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  private String empname;
	  
	  private Double amount;

	  //@NotBlank
	  //@Size(max = 20)
	  private String username;

	  //@NotBlank
	  //@Size(max = 50)
	  //@Email
	  private String email;

	  //@NotBlank
	  //@Size(max = 120)
	  private String password;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  private User manager;
	  
	  @OneToMany(fetch = FetchType.LAZY,mappedBy = "manager")
	  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	  private Set<User> reportee;

	  @ManyToMany(fetch = FetchType.LAZY)
	  @JoinTable(  name = "user_roles", 
	        joinColumns = @JoinColumn(name = "user_id"), 
	        inverseJoinColumns = @JoinColumn(name = "role_id"))
	  private Set<Role> roles = new HashSet<>();
	  
	  @JsonIgnore
	  public Set<User> getReportee() {
	      return reportee;
	  }
	  
	  public User(String username, String empname, Double amount, String email, User manager, String password) {
		  this.username = username;
		    this.empname = empname;
		    this.amount = amount;
		    this.email = email;
		    this.manager = manager;
		    this.password = password;
	  }
	 

}
