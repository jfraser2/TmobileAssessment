package springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springboot.entities.TemplateEntity;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateEntity, Long>{
	
}
