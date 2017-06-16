package mx.com.amx.unotv.workflow.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;

import mx.com.amx.unotv.workflow.dto.ContentDTO;
import mx.com.amx.unotv.workflow.dto.ParametrosDTO;

import org.apache.log4j.Logger;

import com.ibm.workplace.wcm.api.Content;
import com.ibm.workplace.wcm.api.ContentComponent;
import com.ibm.workplace.wcm.api.DocumentId;
import com.ibm.workplace.wcm.api.HTMLComponent;
import com.ibm.workplace.wcm.api.OptionSelectionComponent;
import com.ibm.workplace.wcm.api.RichTextComponent;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.TextComponent;
import com.ibm.workplace.wcm.api.Workspace;
import com.ibm.workplace.wcm.api.exceptions.ComponentNotFoundException;
import com.ibm.workplace.wcm.api.exceptions.WorkflowException;

public class UtileriaWorkFlow {

	
	private final static Logger logger = Logger.getLogger(UtileriaWorkFlow.class.getName());
	
	/**
	 * Método que sirve para generar el path de una Nota
	 * @param ContentDTO contentDTO
	 * @param ParametrosDTO parametrosDTO
	 * @return String path
	 * @author jesus
	 * */
	public static String getPathContenido(ContentDTO contentDTO, ParametrosDTO parametrosDTO){
		String rutaContenido="";
		try {
			String tipoSeccion="";
			if(contentDTO.getFcTipoSeccion().equalsIgnoreCase("noticia") || contentDTO.getFcTipoSeccion().equalsIgnoreCase("noticias"))
				tipoSeccion="noticias";
			else if(contentDTO.getFcTipoSeccion().equalsIgnoreCase("videoblog") || contentDTO.getFcTipoSeccion().equalsIgnoreCase("videoblogs"))
				tipoSeccion="videoblogs";
			else
				tipoSeccion=contentDTO.getFcTipoSeccion();						
			
			String id_categoria=contentDTO.getFcFriendlyURLCategoria() !=null && !contentDTO.getFcFriendlyURLCategoria().equals("")?contentDTO.getFcFriendlyURLCategoria():contentDTO.getFcIdCategoria();
			
			String id_seccion=contentDTO.getFcFriendlyURLSeccion() !=null && !contentDTO.getFcFriendlyURLSeccion().equals("")?contentDTO.getFcFriendlyURLSeccion():contentDTO.getFcSeccion();
			
			rutaContenido = tipoSeccion + "/" + id_seccion +"/"+ id_categoria+"/"+ parametrosDTO.getPathDetalle() + "/" +contentDTO.getFcNombre();
		
		} catch (Exception e) {
			logger.error("Error getPathContenido: ",e);
		}
		return rutaContenido;
	}
	/**
	 * Método que sirve para setear la información de los properties en una instancia de la clase ParametrosDTO
	 * @param String properties
	 * @return ParametrosDTO
	 * @throws WorkflowException
	 * @author jesus
	 * */
	public static ParametrosDTO obtenerPropiedades(String properties) throws WorkflowException {
		ParametrosDTO parametrosDTO = new ParametrosDTO();		 
		try {	    		
			Properties propsTmp = new Properties();
			propsTmp.load(UtileriaWorkFlow.class.getResourceAsStream( "/general.properties" ));
			String ambiente = propsTmp.getProperty("ambiente");
			//logger.info("ambiente: "+ambiente);
			String rutaProperties = propsTmp.getProperty(properties.replace("ambiente", ambiente));
			//logger.info("rutaProperties: "+rutaProperties);
			Properties props = new Properties();
			
			props.load(new FileInputStream(new File(rutaProperties)));
			parametrosDTO.setURL_WS(propsTmp.getProperty(ambiente+".URL_WS"));
			parametrosDTO.setURL_WS_VIDEO(propsTmp.getProperty(ambiente+".URL_WS_VIDEO"));
			parametrosDTO.setURL_WS_PARAMETROS(propsTmp.getProperty(ambiente+".URL_WS_PARAM"));
			parametrosDTO.setCarpetaResources(props.getProperty("carpetaResources"));
			parametrosDTO.setPathFiles(props.getProperty("pathFiles"));
			parametrosDTO.setBasePaginaPlantilla(props.getProperty("basePaginaPlantilla"));
			parametrosDTO.setPathShell(props.getProperty("pathShell"));
			parametrosDTO.setPathRemote(props.getProperty("pathRemote"));
			parametrosDTO.setNameAuthoringTemplate(props.getProperty("nameAuthoringTemplate"));
			parametrosDTO.setNameHTML(props.getProperty("nameHTML"));
			parametrosDTO.setBaseTheme(props.getProperty("baseTheme"));		
			parametrosDTO.setBaseURL(props.getProperty("baseURL"));
			parametrosDTO.setURL_WS_BASE(props.getProperty("URL_WS_BASE"));
			parametrosDTO.setBasePagesPortal(props.getProperty("basePagesPortal"));	
			parametrosDTO.setBaseNoticiasComscore(props.getProperty("baseNoticiasComscore"));		
			parametrosDTO.setPathShellElimina(props.getProperty("pathShellElimina"));
			parametrosDTO.setPathDetalle(props.getProperty("pathDetalle"));
			parametrosDTO.setDominio(props.getProperty("dominio"));
			parametrosDTO.setNameStageExpireWF(props.getProperty("nameStageExpireWF"));
			parametrosDTO.setAmbiente(props.getProperty("ambiente"));
			parametrosDTO.setUrlBuscador(props.getProperty("urlBuscador"));
			parametrosDTO.setURL_WS_BASE_BUSCADOR(props.getProperty("URL_WS_BASE_BUSCADOR"));
			parametrosDTO.setMetaVideo(props.getProperty("metaVideo"));
			parametrosDTO.setMetaVideoSecureUrl(props.getProperty("metaVideoSecureUrl"));
			parametrosDTO.setURL_WEBSERVER(props.getProperty("URL_WEBSERVER"));
			
			parametrosDTO.setNameStagePublishWF(props.getProperty("nameStagePublishWF"));
			parametrosDTO.setNameStageReviewWF(props.getProperty("nameStageReviewWF"));
			parametrosDTO.setPathFilesTest(props.getProperty("pathFilesTest"));
			parametrosDTO.setBaseURLTest(props.getProperty("baseURLTest"));
			
			parametrosDTO.setURL_WS_BASE_AMP(props.getProperty("URL_WS_BASE_AMP"));
			parametrosDTO.setURL_WEBSERVER_AMP(props.getProperty("URL_WEBSERVER_AMP"));
			parametrosDTO.setURL_WEBSERVER_CSS_AMP(props.getProperty("URL_WEBSERVER_CSS_AMP"));
			parametrosDTO.setURL_WS_BASE_FB(props.getProperty("URL_WS_BASE_FB"));
			
			parametrosDTO.setCatalogoParametros(propsTmp.getProperty("catalogoParametros"));
			
			
		} catch (Exception ex) {
			logger.error("No se encontro el Archivo de propiedades: ", ex);
			throw new WorkflowException(ex.getMessage());
		}
		return parametrosDTO;
    }
	
	
	/**
	 * Método que sirve para setear la información del contenido de portal a la clase ContentDTO
	 * @param Content
	 * @param Workspace
	 * @param ParametrosDTO
	 * @return ContentDTO
	 * @throws WorkflowException
	 * @author jesus
	 * */
	public static ContentDTO getContenido(Content myContent, Workspace ws, ParametrosDTO parametrosDTO) throws WorkflowException {
		ContentDTO contentDTO = new ContentDTO();
		
		try {			
			
			contentDTO.setFcSeccion(getValueSiteAreaPortal(myContent, ws, "txtSeccion"));
			contentDTO.setFcTipoSeccion(getValueSiteAreaPortal(myContent, ws, "txtTipoSeccion"));
			contentDTO.setFcIdCategoria(getValueSiteAreaPortal(myContent, ws, "txtCategoria"));
			contentDTO.setFcNombreCategoria(getValueSiteAreaPortal(myContent, ws, "txtNombreCategoria"));
			contentDTO.setFcFriendlyURLSeccion(getValueSiteAreaPortal(myContent, ws, "txtFriendlyURLSeccion"));
			contentDTO.setFcFriendlyURLCategoria(getValueSiteAreaPortal(myContent, ws, "txtFriendlyURLCategoria"));
			
			String keyWords [] = myContent.getKeywords();
			String k = "";
			for (String kw : keyWords) {
				k+=kw+",";
			}
			contentDTO.setFcKeywords(k.substring(0, k.length()-1));
			
			contentDTO.setFcTitulo(myContent.getTitle().trim().replaceAll("\n", "").replaceAll("\r", ""));
			contentDTO.setFcNombre(myContent.getName().trim().replaceAll("\n", "").replaceAll("\r", ""));
			contentDTO.setFcIdContenido(myContent.getId().getId());
			contentDTO.setFcPaisRegistro("MEX");
			contentDTO.setFcDescripcion(myContent.getDescription().trim().replaceAll("/\r?\n/g", "").replaceAll("\n", "").replaceAll("\r", ""));
			contentDTO.setFdFechaPublicacion(new Timestamp(myContent.getEffectiveDate().getTime()));
			
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			contentDTO.setFcFecha(format.format(myContent.getEffectiveDate()));
			format = new SimpleDateFormat("HH:mm");
			contentDTO.setFcHora(format.format(myContent.getEffectiveDate()));
			
			contentDTO.setFcTags(getValueElementPortal(myContent, "txtTags"));
			contentDTO.setFcEscribio(getValueElementPortal(myContent, "txtEscribio"));
			contentDTO.setFcImgPrincipal(getValueElementPortal(myContent, "txtImagenPrincipal"));
			contentDTO.setFcPieFoto(getValueElementPortal(myContent, "txtPieImagenPrincipal"));
			contentDTO.setFcImgInfografia(getValueElementPortal(myContent, "txtImagenInfografia"));
			contentDTO.setClGaleriaImagenes(getValueElementPortal(myContent, "htmlData"));
			contentDTO.setPlaceGallery(getValueElementPortal(myContent, "selectGalery"));
			contentDTO.setFcPCode(getValueElementPortal(myContent, "selectPcodes"));
			contentDTO.setClRtfContenido(getValueElementPortal(myContent, "rtfContenido"));
			contentDTO.setFcIdVideoYouTube(getValueElementPortal(myContent, "txtIDVideoYouTube"));
			contentDTO.setFcIdVideoOoyala(getValueElementPortal(myContent, "txtIDVideoOoyala"));
			contentDTO.setFcIdPlayerOoyala(getValueElementPortal(myContent, "txtIDPlayerOoyala"));
			contentDTO.setFcTituloComentario(getValueElementPortal(myContent, "txtTituloComentario"));
			contentDTO.setFcEscribio(getValueElementPortal(myContent, "txtEscribio"));
			contentDTO.setFcLugar(getValueElementPortal(myContent, "txtLugar"));
			contentDTO.setFcFuente(getValueElementPortal(myContent, "txtFuente"));
			
			String [] categorias=getValueElementPortal(myContent, "selectCategorias").split("\\|");
			for (String c : categorias) {
				if(c.toLowerCase().trim().equals("infinito-home")){
					contentDTO.setFiBanInfinito(1);
				}else if(c.toLowerCase().trim().equals("videos-virales")){
					contentDTO.setFiBanVideoViral(1);
				}else if(c.toLowerCase().trim().equals("no-te-lo-pierdas")){
					contentDTO.setFiBanNoTeLoPierdas(1);
				}else if(c.toLowerCase().trim().equals("patrocinio")){
					contentDTO.setFiBanPatrocinio(1);
				}
			}
			contentDTO.setFcTagsApp(getValueElementPortal(myContent, "selectTagApp").split("\\|"));
			contentDTO.setFcIdTipoNota(validateNota(contentDTO));
			
			
		} catch(Exception e) {
			logger.error("Error en getContenido: ",e);
			throw new WorkflowException(e.getMessage());
			
		}
		return contentDTO;
	}		
	/**
	 * Método que sirve para validar el tipo de Nota
	 * @param ContentDTO contentDTO
	 * @return String
	 * @author jesus
	 * */
	private static String validateNota(ContentDTO contentDTO){
		String respuesta = "";
		try {

			if(//contentDTO.getFcIdVideoYouTube() !=null && !contentDTO.getFcIdVideoYouTube().equals("") && contentDTO.getClGaleriaImagenes() != null && !contentDTO.getClGaleriaImagenes().equals("") || 
			   contentDTO.getFcIdVideoOoyala() !=null && !contentDTO.getFcIdVideoOoyala().equals("") && contentDTO.getClGaleriaImagenes() != null && !contentDTO.getClGaleriaImagenes().equals("") 
			   ){
			   respuesta="multimedia";
			}else if(contentDTO.getFcIdVideoYouTube() !=null && !contentDTO.getFcIdVideoYouTube().equals("") || contentDTO.getFcIdVideoOoyala() !=null && !contentDTO.getFcIdVideoOoyala().equals("")){
				respuesta="video";
			}else if( contentDTO.getClGaleriaImagenes() != null && !contentDTO.getClGaleriaImagenes().equals("")){
				respuesta="galeria";
			}else if(contentDTO.getFcImgInfografia() != null && !contentDTO.getFcImgInfografia().equals("")){
				respuesta="infografia";
			}else
				respuesta="imagen";
		} catch (Exception e) {
			logger.error("Error validateNota: ",e);
		}
		return respuesta;
	}
	/**
	 * Método que sirve para obtener el valor de cierto campo en la plantilla de creación 
	 * de un contenido por medio de su id
	 * @param Content myContent
	 * @param String id
	 * @return String
	 * @author jesus
	 * */
	private static String getValueElementPortal(Content myContent,String id){
		String value="";
		try {
			ContentComponent x=myContent.getComponent(id);
			if(x instanceof TextComponent){
				TextComponent instancia = (TextComponent) x;
				value=instancia.getText();
			}else if(x instanceof RichTextComponent){
				RichTextComponent instancia = (RichTextComponent) x;
				value=instancia.getRichText();
			}else if(x instanceof HTMLComponent){
				HTMLComponent instancia = (HTMLComponent) x;
				value=instancia.getHTML();
			}else if(x instanceof OptionSelectionComponent){
				OptionSelectionComponent instancia = (OptionSelectionComponent) x;
				if(id.equalsIgnoreCase("selectPcodes")){
		 			try {
		 				Properties propsTmp = new Properties();
		 				propsTmp.load(UtileriaWorkFlow.class.getResourceAsStream( "/general.properties" ));
		 				String seleccion = instancia.getSelections()[0];
		 				value=propsTmp.getProperty("pcode."+seleccion);
		 			} catch (Exception e){
		 				logger.error("Error en selectPcode");
		 			}
				}else if(id.equalsIgnoreCase("selectGalery")){
					value=instancia.getSelections()[0];
				}else if(id.equalsIgnoreCase("selectCategorias")){
					for (String seleccion : instancia.getSelections()) {
						if(seleccion.toLowerCase().trim().equals("infinito home")){
							value+="infinito-home|";
						}else if(seleccion.toLowerCase().trim().equals("videos-virales")){
							value+="videos-virales|";
						}else if(seleccion.toLowerCase().trim().equals("no-te-lo-pierdas")){
							value+="no-te-lo-pierdas|";
						}else if(seleccion.toLowerCase().trim().equals("patrocinio")){
							value+="patrocinio";
						}
					}
				}else if(id.equalsIgnoreCase("selectTagApp")){
					for (String opcion: instancia.getSelections()) {
						value+=opcion+"|";
					}
					value=value.substring(0, value.length()-1);
				}
			}
		} catch (ComponentNotFoundException e) {
			logger.error("No se encontro el campo "+id+": "+e.getMessage());
		} catch (Exception e) {
			logger.error("Error en getValuePortal: ",e);
		}
		return value;
	}
	/**
	 * Método que sirve para obtener el valor de cierto campo en la plantilla de creación 
	 * de un area de sitio por medio de su id
	 * @param Content myContent
	 * @param Workspace ws
	 * @param String id
	 * @return String
	 * @author jesus
	 * */
	private static String getValueSiteAreaPortal(Content myContent, Workspace ws,String id){
		String value="";
		try {
			DocumentId sitA = myContent.getDirectParent();
			SiteArea siteAreaParent = (SiteArea) ws.getById(sitA);
			ContentComponent x=siteAreaParent.getComponent(id);
			if(x instanceof TextComponent){
				TextComponent instancia = (TextComponent) x;
				value=instancia.getText();
			}
		} catch (ComponentNotFoundException e) {
			logger.error("No se encontro el campo ["+id+"]: "+e.getMessage());
		} catch (Exception e) {
			logger.error("Error en getValueSiteAreaPortal: ",e);
		}
		return value;
	}

}
