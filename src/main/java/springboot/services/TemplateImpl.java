package springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import springboot.dto.request.CreateTemplate;
import springboot.entities.TemplateEntity;
import springboot.repositories.TemplateRepository;
import springboot.services.interfaces.Template;

@Service
public class TemplateImpl
	implements Template
{
	@Autowired
	private TemplateRepository templateRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public TemplateEntity findById(Long templateId) {
		TemplateEntity retVar = null;
		
		Optional<TemplateEntity> usne = templateRepository.findById(templateId);
		if (usne.isPresent())
			retVar = usne.get();
		
		return retVar;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<TemplateEntity> findAll() {
		List<TemplateEntity> retVar = null;
		
		List<TemplateEntity> usne = templateRepository.findAll();
		if (null != usne)
			retVar = usne;
		
		return retVar;
	}
	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TemplateEntity persistData(TemplateEntity templateEntity) {
		
		TemplateEntity retVar = null;
		
		try {
			if (null != templateEntity) {
				retVar = templateRepository.save(templateEntity);
			}	
		} catch (Exception e) {
			retVar = null;
		}
		
		return retVar;
	}

	@Override
	public TemplateEntity buildTemplateEntity(CreateTemplate createTemplateRequest) {
		
		TemplateEntity retVar = null;
		
		try {
			String templateBody = createTemplateRequest.getTemplateText();
			if (null != templateBody && templateBody.length() > 0)
			{
				retVar = new TemplateEntity();
				retVar.setBody(templateBody);
				
			}
		} catch (Exception e) {
			retVar = null;
		}
		
		return retVar;
	}

}
