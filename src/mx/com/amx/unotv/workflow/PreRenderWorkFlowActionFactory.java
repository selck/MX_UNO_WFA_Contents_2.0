package mx.com.amx.unotv.workflow;

import java.util.Locale;

import com.ibm.workplace.wcm.api.Document;
import com.ibm.workplace.wcm.api.custom.CustomWorkflowAction;
import com.ibm.workplace.wcm.api.custom.CustomWorkflowActionFactory;

public class PreRenderWorkFlowActionFactory  implements CustomWorkflowActionFactory 
{
	public CustomWorkflowAction getAction(String arg0, Document arg1) 
	{		
		if (arg0.equalsIgnoreCase( "GENERATE" )) 
		{
			 return new PreRenderWorkFlowAction();
		}	 
		return null;
	}

	public String getActionDescription(Locale arg0, String arg1) {
		return "MX_UNO_WFA_Contents_2.0: Genera el detalle de Contenidos del portal de UNOTV" ;
	}

	public String[] getActionNames() 
	{
 		 String names[] = { "generateDetailUNOTV" };
		 return names;
	}

	public String getActionTitle(Locale arg0, String arg1) 
	{
		if (arg1.equalsIgnoreCase( "generateDetailUNOTV" )) 		
			return "generateDetailUNOTV" ;		
		return null;
	}

	public String getName() {
		return "PreRenderUNOTVWorkFlowAction" ;
	}

	public String getTitle(Locale arg0) {
		return "UNOTV - WorkFlow";
	}
	
  
}
