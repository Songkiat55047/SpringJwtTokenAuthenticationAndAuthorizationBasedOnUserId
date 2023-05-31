package com.songkiat.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.songkiat.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	  Optional<User> findByUsername(String username);

	  Boolean existsByUsername(String username);
	  
	  Boolean existsByEmpname(String empmame);
	  
	  Boolean existsByAmount(Double amount);

	  Boolean existsByEmail(String email);
	  
	  Boolean existsByManager(User manager);
	  
	  
	  @Query("SELECT p.amount FROM User p WHERE p.id = :id")
		Double findAmountById(@Param("id") Long id);
		
		@Query(value = "WITH RECURSIVE manager_chain(id, empname, amount, manager_id) AS "
			      + "(SELECT id, empname, amount, manager_id FROM users WHERE id = ?1 "
			      + "UNION ALL "
			      + "SELECT p.id, p.empname, p.amount, p.manager_id FROM users p JOIN manager_chain mc ON p.manager_id = mc.id) "
			      + "SELECT SUM(p.amount) AS total_amount FROM users p "
			      + "WHERE p.id = ?1 OR p.manager_id = ?1",
			      nativeQuery = true)
		Double findTotalAmountByPersonId(Long id);
		
		@Query(value = "WITH RECURSIVE manager_chain(id, manager_id, amount) AS (\n" +
	          "  SELECT id, manager_id, amount\n" +
	          "  FROM users\n" +
	          "  WHERE id = :id\n" +
	          "  UNION ALL\n" +
	          "  SELECT p.id, p.manager_id, p.amount\n" +
	          "  FROM users p\n" +
	          "  INNER JOIN manager_chain mc ON p.manager_id = mc.id\n" +
	          ")\n" +
	          "SELECT SUM(amount) FROM manager_chain", nativeQuery = true)
		Double findTotalAmountDirectedToPersonAndChain(@Param("id") Long id);

}
