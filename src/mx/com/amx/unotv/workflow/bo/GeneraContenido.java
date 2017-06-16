package mx.com.amx.unotv.workflow.bo;

import mx.com.amx.unotv.workflow.dto.ContentDTO;
import mx.com.amx.unotv.workflow.dto.ParametrosDTO;
import mx.com.amx.unotv.workflow.exception.LlamadasWSBOException;
import mx.com.amx.unotv.workflow.util.UtileriaWorkFlow;

import org.apache.log4j.Logger;

import com.ibm.workplace.wcm.api.Content;
import com.ibm.workplace.wcm.api.ContentComponent;
import com.ibm.workplace.wcm.api.ShortTextComponent;
import com.ibm.workplace.wcm.api.Workspace;
import com.ibm.workplace.wcm.api.exceptions.WorkflowException;
public class GeneraContenido{
	
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	/**
	 * Método utilizado para validar el estado del contenido y llevar a cabo diferentes acciones
	 * dependiendo del estado en el que se encuetré (revisión, publicado, caducado)
	 * @param Content myContent
	 * @return void
	 * @author jesus
	 * */
	public void validateInsertContent(Content myContent) {
		try {
			Workspace ws = null;					
			String authoringTemplate = myContent.getAuthoringTemplateID().getName();
			ParametrosDTO parametrosDTO=UtileriaWorkFlow.obtenerPropiedades("ambiente.resources.properties");
			LlamadasWSBO llamadasWSBO=new LlamadasWSBO(parametrosDTO.getURL_WS_BASE());
			
			if(authoringTemplate != null && authoringTemplate.trim().equals(parametrosDTO.getNameAuthoringTemplate())) {
				
				ContentDTO contentDTO=UtileriaWorkFlow.getContenido(myContent, ws, parametrosDTO);
				
				if (myContent.getWorkflowStageId().getName().trim().equals(parametrosDTO.getNameStageExpireWF())) {
					ws = myContent.getSourceWorkspace();																
					ws.login();	
					logger.info("Etapa de caducacion...");
					logger.info("Se elimino la nota: "+contentDTO.getFcTitulo()+llamadasWSBO.caducarNota(contentDTO));
					ws.logout();
				}else if(myContent.getWorkflowStageId().getName().trim().equals(parametrosDTO.getNameStageReviewWF())){
					ws = myContent.getSourceWorkspace();																
					ws.login();	
					logger.info("Etapa de revision...");
					String urlContenido=llamadasWSBO.revisarNota(contentDTO);
					ContentComponent x=myContent.getComponent("txtURLContenido");
					ShortTextComponent shortText = (ShortTextComponent) x;
		 			shortText.setText(urlContenido);
		 			logger.info("Seteamos la url del contenido: "+urlContenido);
		 			myContent.setComponent("txtURLContenido", shortText);
					ws.logout();
				}else if (myContent.getWorkflowStageId().getName().trim().equals(parametrosDTO.getNameStagePublishWF())) {
					ws = myContent.getSourceWorkspace();																
					ws.login();	
					logger.info("Etapa de publicacion...");

					String fb_id=llamadasWSBO.publicarNota(contentDTO);
					ContentComponent x=myContent.getComponent("txtArticleId");
					ShortTextComponent shortText = (ShortTextComponent) x;
		 			shortText.setText(fb_id);
		 			logger.info("Seteamos el fb_id: "+fb_id);
		 			myContent.setComponent("txtArticleId", shortText);
					ws.logout();
				}
			}
		} catch (LlamadasWSBOException e) {
			logger.error("Error en llamada a los WS: "+e.getMessage());
		} catch (WorkflowException e) {
			logger.error("Error en el workflowException: "+e.getMessage());
		} catch (Exception e) {
		logger.error("Exceptio validateInsertContentn: "+e.getMessage());
		}
		
	}
	
}
