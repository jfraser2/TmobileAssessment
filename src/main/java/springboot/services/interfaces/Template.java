package springboot.services.interfaces;

import java.util.List;

import springboot.dto.request.CreateTemplate;
import springboot.entities.TemplateEntity;

public interface Template {
	
	public TemplateEntity findById(Long id);
	public List<TemplateEntity> findAll();
	
	public TemplateEntity buildTemplateEntity(CreateTemplate createTemplateRequest);
	public TemplateEntity persistData(TemplateEntity templateEntity);

}
