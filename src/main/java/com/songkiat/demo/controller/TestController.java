package com.songkiat.demo.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.songkiat.demo.entity.UserDTO;
import com.songkiat.demo.repository.UserRepository;
import com.songkiat.demo.service.UserService;
import com.songkiat.demo.entity.User;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }
  
  @GetMapping("/{id}")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<UserDTO> getAllDetails(@PathVariable("id") Long id) {
      return userRepository.findById(id).map(mapToPersonDTO).map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
  }
  
  
  @GetMapping("/findAmounts/{id}")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<UserDTO> getAllAmount(@PathVariable("id") Long id) {
      return userRepository.findById(id).map(findAmounts).map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
  }
  

  @GetMapping("/{id}/siblings")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<Set<UserDTO>> getAllSiblings(@PathVariable("id") Long id) {
      return userRepository.findById(id).map(findSiblings).map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
  }
  
  
  @GetMapping("/{id}/total-amount")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<Map<String, Double>> getTotalAmount(@PathVariable Long id) {
  	Double result = userService.findTotalAmountByPersonId(id);
  	Map<String, Double> responseBody = new HashMap<>();
  	responseBody.put("Sale Direct Him: ", result);
  	return ResponseEntity.ok(responseBody);
  	//return ResponseEntity.ok(responseBody);
  }
  
  @GetMapping("/{id}/total")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<Map<String, Double>> getTotalAmountAll(@PathVariable Long id) {
  	Double result = userService.findTotalAmountDirectedToPersonAndChain(id);
  	Map<String, Double> responseBody = new HashMap<>();
  	responseBody.put("Sale Under His Chain: ", result);
  	return ResponseEntity.ok(responseBody);
  }
  
  @GetMapping("/{id}/amount")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<Map<String, Double>> getAmount(@PathVariable Long id) {
  	Double result = userService.findAmountById(id);
  	Map<String, Double> responseBody = new HashMap<>();
  	responseBody.put("Sale By Him: ", result);
  	return ResponseEntity.ok(responseBody);
	}
  
  @GetMapping("/{id}/details")
  @PreAuthorize("authentication.principal.id == #id")
  public ResponseEntity<?> getPersonDetails(@PathVariable Long id) {
      User person = userRepository.findById(id).orElse(null);
      if (person == null) {
          return ResponseEntity.notFound().build();
      }

      Map<String, Object> responseBody = new HashMap<>();
      //responseBody.put("id", person.getId());
      responseBody.put("Employee Name", person.getEmpname());
      //responseBody.put("Amount", person.getAmount());
      
      Double directAmount = userService.findTotalAmountByPersonId(id);
      responseBody.put("SaleDirectHim", directAmount);
      
      Double chainAmount = userService.findTotalAmountDirectedToPersonAndChain(id);
      responseBody.put("SaleUnderHisChain", chainAmount);
      
      Double saleAmount = userService.findAmountById(id);
      responseBody.put("SaleByHim", saleAmount);

      return ResponseEntity.ok(responseBody);
      		   }
  
  
  
  private Function<User, Set<UserDTO>> findSiblings = user -> user.getManager().getReportee().stream()
          .map(p -> UserDTO.builder().id(p.getId()).empname(p.getEmpname()).amount(p.getAmount()).build()).collect(Collectors.toSet());

  private Function<User, UserDTO> mapToPersonDTO = p -> UserDTO.builder().id(p.getId()).empname(p.getEmpname()).amount(p.getAmount()).reportee(p.getReportee()).build();
  
  /*private Function<Person, Set<PersonDTO>> findAmounts_1 = person -> person.getDirectsale().getReportee().stream()
  		.map(p -> PersonDTO.builder().amount(p.getAmount()).build()).collect(Collectors.toSet());
  
  private Function<Person, Set<PersonDTO>> findAmounts_2 = person -> person.getDirectsale().getReportee().stream()
  		.map(p -> PersonDTO.builder().amount(p.getAmount()).directsale(p.getDirectsale()).build()).collect(Collectors.toSet());
  */
  private Function<User, UserDTO> findAmounts = p -> UserDTO.builder().id(p.getId()).empname(p.getEmpname()).amount(p.getAmount()).build();
  
	/*public Long getSumPrice(@PathVariable List<Long> id) {
		return personService.count(id);
	}*/

}
