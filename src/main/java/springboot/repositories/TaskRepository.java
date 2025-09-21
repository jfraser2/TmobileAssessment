package springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.entities.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>{
    @Query(value = "SELECT entity FROM TaskEntity entity WHERE entity.taskStatus = :taskStatus")
    List<TaskEntity> findByTaskStatus(@Param("taskStatus") String taskStatus);
	
}
