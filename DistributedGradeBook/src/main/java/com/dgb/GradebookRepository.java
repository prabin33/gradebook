package com.dgb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradebookRepository extends JpaRepository<Gradebook, Integer> {
	
}