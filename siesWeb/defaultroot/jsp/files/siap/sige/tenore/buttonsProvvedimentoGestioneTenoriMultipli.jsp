<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"     scope="request" class="java.lang.String"/>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
    <tr>

<%
  // Flag di abilitazione del bottone di modifica
  boolean abilitaModifica = true;

  String modifica = request.getParameter("Modifica");
  if( modifica!= null && modifica.compareTo("NO")==0 )
  	abilitaModifica = false;
    	
  // presenza del Link per il bottone di ritorno
  boolean retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  String flagTitoliEsecutivi=request.getParameter("flagTitoliEsecutivi");
  boolean isTitoliEsecutivi =(flagTitoliEsecutivi==null ? false : flagTitoliEsecutivi.equalsIgnoreCase("true"));
  String codOggettoSige=request.getParameter("codOggettoSige");
  String idProvvedimento=request.getParameter("idProvvedimento");
    
  Collection <FunctionModel>lFunFiglie = (Collection <FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
  //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) ) {
        for (FunctionModel lFun : lFunFiglie) {
          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
        
				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && isTitoliEsecutivi) {
					%>
					<td rowspan=2>
					  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&codOggettoSige=<%=codOggettoSige%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=true">
		                  <img src="/images/new24.gif" alt="Esito Unico per Oggetto" width="12" height="12" border="0">
		              </a>
					</td>
				   <% 	
			  }
		
          } // IF FUNZIONE_BOTTONE
      }  // end WHILE
      
      
    }
%>
    </tr>
  </table>
