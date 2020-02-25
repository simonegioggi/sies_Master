<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

  <table>
    <tr>
<%
			// presenza del Link per il bottone di ritorno
			String retParam = "&nextAction=siap.siep.istruttoria.action.ActRicercaRichiesteIstruttoria";
			
		  String lModificabile = "SI";

		  if (request.getParameter("Modificabile") != null)
		  {
		    lModificabile = request.getParameter("Modificabile");
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

	          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
	          {
		            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
		            {
			               if ( (request.getParameter("MotivoEvento").compareTo("0047")==0 || request.getParameter("MotivoEvento").compareTo("1048")==0)
			            		   && (lFun.getVisualizationOrder().intValue()==2)  )
			               {
			%>
					              <td>
					                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
					                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
					                </a>
					              </td>
			<%             } 
			               else if ((request.getParameter("MotivoEvento").compareTo("0049")==0) && (lFun.getVisualizationOrder().intValue()==1))
			               {
			%>
					              <td>
					                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
					                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
					                </a>
					              </td>
			<%             
							}
							// Fabio - MAC a7-rr-307
							// richiesta codice cui
			               else if (
			            		   ((request.getParameter("MotivoEvento").compareTo("0557")==0) 
			            		   || request.getParameter("MotivoEvento").compareTo("0578")==0)
			            		   && (lFun.getVisualizationOrder().intValue()==3))
			               {
			%>
					              <td>
					                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
					                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
					                </a>
					              </td>
			<%             
							}
			   				// AMBROSINO 05/2011
							// richiesta pagamento pena pecuniaria
			               else if ((request.getParameter("MotivoEvento").compareTo("1050")==0) 
			            		     && (lFun.getVisualizationOrder().intValue()==4))
			               {
			%>
					              <td>
					                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
					                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
					                </a>
					              </td>
			<%             
							}              
		            }
	            
		            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
		            {
		%>
		              <td>
		                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
		                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
		                </a>
		              </td>
		<%
		            }
	            
		        	if(	 lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) 
		        	      && !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A") 
		        	      && (lModificabile.equals("SI")) )
		            {
		%>
		              <td>
			      				<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>','EM');">
		                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
		                </a>
		              </td>
		<%
		            }
	            
		        	if(     lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) 
		        	      && ( request.getParameter("Evento").compareTo("SI") == 0 ) 
		        	      && !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A")
		        	 	    && !request.getParameter("docRegistrato").equals("M") // migrato
		        	 	   )
		        	{
		        	 	    String lActStampa ="siap.sico.evento.action.ActLoadDocumento";
		
		%>
										<!-- BOTTONE DI STAMPA -->
										<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_RICERCHE_STAMPA_SIEP%>">
											<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lActStampa+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
										</jsp:include>
		<%
		        	}
	          }
        }
      } 
%>
    </tr>
  </table>