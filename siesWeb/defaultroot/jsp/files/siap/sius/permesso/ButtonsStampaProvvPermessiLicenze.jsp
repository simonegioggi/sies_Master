<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Enumeration"%>

<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.security.model.FunctionModel"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
	<tr>
<%
	RedirectTo lRedir = new RedirectTo(); //lRedir.setPage(IWebConstants.PG_MAIN);
		
	//Vengono inseriti tanti campi di input quanti sono i parametri nella request
	//per poterli passare alla Action successiva.
	for (Enumeration r = request.getParameterNames(); r.hasMoreElements();) {
  	String lNomeParam = (String) r.nextElement();
   	String lValoreParam = request.getParameter(lNomeParam);
   	
   	lRedir.setParameter(lNomeParam, lValoreParam );
	}
  
	Collection lFunFiglie = 
	  (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

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
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
          lRedir.setAction( lFun.getNameAction() );
          
%>
          <td>
             <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=lRedir.toString().substring( lRedir.toString().indexOf("?")+1 )%>')">
              <img src="/images/print24.gif" alt="Stampa" border="0">
            </a>
          </td>
<%
        }
      }
    }
  }      
%>
    </tr>
  </table>