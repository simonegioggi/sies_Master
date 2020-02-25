<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<table>
  <tr>
<%
    // presenza del Link per il bottone di ritorno
    boolean retFlag = false;
    retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
    String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

    String lModificabile = "SI";

    if (request.getParameter("Modificabile") != null)
    {
      lModificabile = request.getParameter("Modificabile");
    }
    
    String lEventoCancellareAnnullare = null;
    if (request.getParameter("EventoCancellareAnnullare") != null)
    {
      lEventoCancellareAnnullare = request.getParameter("EventoCancellareAnnullare");
    }
    
    String lOrdinamento = null;
    if (request.getParameter("lOrdinamento") != null)
    {
      lOrdinamento = request.getParameter("lOrdinamento");
    }

    Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

     //Visualizzazione dei bottoni
    if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
    {
	      Iterator lIterBottoni = lFunFiglie.iterator();
	      FunctionModel lFun = null;
	      while(lIterBottoni.hasNext())
	      {
		        lFun = (FunctionModel)lIterBottoni.next();
		        
		        // STUB 25/02/2005 Vincenzo. Occorre gestire solo le proprie funzioni! !
		        // Patch temporanea di esclusione della funzione SIUS 31130592 che se trattata, causa una JASPER.
		        if (lFun.getFunctionId().toString().trim().compareTo("31130592")!=0 )
		        {
		        	if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
			        {
		            	if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
		            	{
			                	if (( "57".equals(request.getParameter("TipoProvvedimento")))||
			                		("02".equals(request.getParameter("TipoProvvedimento")) || "03".equals(request.getParameter("TipoProvvedimento")))
			                  		  && "*".equals(request.getParameter("TemIdTemplate")) )
			                	{
			                		
				                  		if(("2140".equals(request.getParameter("MotivoEvento"))))
				                  		{
				                  			//  AMBROSINO 05/2013  Provvedimento Decreto Espulsione	A
				                  		    String lActDettaglio = "siap.siep.sospensione.action.ActDettaglioConcessioneEspulsione";
				                  	   		%>
						                  	<td>
						                    	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"> 
						                      	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
						                    	</a>
						                  	</td>
				<%
				                  		}
				                  		else
				                  		{	
		
				                  			// Provvedimento Generico
				                  			String lActDettaglio = "siap.siep.provvedimentogenerico.action.ActLoadDettaglioProvvedimentoGenerico";
		   		%>
						                  	<td>
						                    	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
						                      	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
						                    	</a>
						                  	</td>
				<%
				                  		}
			                  		
			            		}	// CHIUDE if (( "57".equals(reques
		                	
		            	}   // CHIUDE if(lFun.getFunctionType().
		            			
			            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A") && (lModificabile.equals("SI"))
			              		&& request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA).equals(lEventoCancellareAnnullare) )
			            {
			%>
				              	<td>
				                	<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>','<%=lOrdinamento%>');">
				                	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
				                	</a>
				              	</td>
			<%
			      	   	}
		            		
		       		}	// CHIUDE if(lFun.getVisualizzazionType(
		    		   
	     		}	// CHIUDE if (lFun.getFunctionId()
	    		 
   		  }		// CHIUDE ciclo while(lIterBottoni.
   				  
    }		// CHIUDE if( (lFunFiglie					  
%>
  </tr>
</table>