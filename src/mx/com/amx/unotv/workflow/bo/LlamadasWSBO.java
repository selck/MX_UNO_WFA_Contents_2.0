package mx.com.amx.unotv.workflow.bo;

import mx.com.amx.unotv.workflow.dto.ContentDTO;
import mx.com.amx.unotv.workflow.exception.LlamadasWSBOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ibm.workplace.wcm.api.exceptions.WorkflowException;


@Component
@Qualifier("llamadasWSBO")
public class LlamadasWSBO {

	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private RestTemplate restTemplate;
	private String URL_WS_BASE="";
	private HttpHeaders headers = new HttpHeaders();
	
	public LlamadasWSBO(String urlWS){
		super();
		restTemplate = new RestTemplate();
		ClientHttpRequestFactory factory = restTemplate.getRequestFactory();

	        if ( factory instanceof SimpleClientHttpRequestFactory)
	        {
	            ((SimpleClientHttpRequestFactory) factory).setConnectTimeout( 50 * 1000 );
	            ((SimpleClientHttpRequestFactory) factory).setReadTimeout( 50 * 1000 );
	        }
	        else if ( factory instanceof HttpComponentsClientHttpRequestFactory)
	        {
	            ((HttpComponentsClientHttpRequestFactory) factory).setReadTimeout( 50 * 1000);
	            ((HttpComponentsClientHttpRequestFactory) factory).setConnectTimeout( 50 * 1000);
	            
	        }
	        restTemplate.setRequestFactory( factory );
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
			URL_WS_BASE = urlWS;
	}
	/**
	 * Método que sirve para mandar a llamar la acción de revisar Nota
	 * @param ContentDTO contentDTO
	 * @return String url_preview_nota
	 * @throws WorkflowException
	 * @author jesus
	 * */
	String revisarNota(ContentDTO contentDTO) throws LlamadasWSBOException{
		String respuesta="";
		String metodo="revisarNota";
		String URL_WS=URL_WS_BASE+metodo;
		try {
			HttpEntity<ContentDTO> entity = new HttpEntity<ContentDTO>( contentDTO );
			respuesta=restTemplate.postForObject(URL_WS, entity, String.class);
		} catch(Exception e) {
			logger.error("Error revisarNota [BO]: "+e.getMessage());
			throw new LlamadasWSBOException(e.getMessage());
		}
		return respuesta;
	}
	/**
	 * Método que sirve para mandar a llamar la acción de publicar Nota
	 * @param ContentDTO contentDTO
	 * @return String idFacebook
	 * @throws WorkflowException
	 * @author jesus
	 * */
	String publicarNota(ContentDTO contentDTO) throws LlamadasWSBOException{
		String respuesta="";
		String metodo="publicarNota";
		String URL_WS=URL_WS_BASE+metodo;
		try {
			HttpEntity<ContentDTO> entity = new HttpEntity<ContentDTO>( contentDTO );
			respuesta=restTemplate.postForObject(URL_WS, entity, String.class);
		} catch(Exception e) {
			logger.error("Error publicarNota [BO]: "+e.getMessage());
			throw new LlamadasWSBOException(e.getMessage());
		}
		return respuesta;
	}
	/**
	 * Método que sirve para mandar a llamar la acción de publicar Nota
	 * @param ContentDTO contentDTO
	 * @return Boolean
	 * @throws WorkflowException
	 * @author jesus
	 * */
	Boolean caducarNota(ContentDTO contentDTO) throws LlamadasWSBOException{
		boolean respuesta=false;
		String metodo="caducarNota";
		String URL_WS=URL_WS_BASE+metodo;
		try {
			HttpEntity<ContentDTO> entity = new HttpEntity<ContentDTO>( contentDTO );
			respuesta=restTemplate.postForObject(URL_WS, entity, Boolean.class);
		} catch(Exception e) {
			logger.error("Error caducarNota [BO]: "+e.getMessage());
			throw new LlamadasWSBOException(e.getMessage());
		}
		return respuesta;
	}

}
