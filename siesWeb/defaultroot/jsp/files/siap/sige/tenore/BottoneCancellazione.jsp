<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>
 
<jsp:useBean id="modificaOggettoAtto"     scope="request" class="java.lang.String"/>
 
 <%
  // Funzioni abilitate
  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
  boolean modificabile = false;
 
  // Indica se si tratta della cancellazione per singolo titolo esecutivo/oggetti (S)
  // oppure della cancellazione di tutti i titoli esecutivi/oggetti (G)
  String tipoCanc = "";
  tipoCanc = request.getParameter("TipoCanc");
  
  if (modificaOggettoAtto.equalsIgnoreCase("SI"))
  {
	  modificabile = true;
  }

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA ) && modificabile)
        {
        	if(tipoCanc != null && tipoCanc.equals("S")){
%>
	            <a href="Javascript:myConfirm('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter("CampoIdSentenza")%>','<%=request.getParameter("ValoreIdSentenza")%>');">
	                <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
	          	</a>
<%
        	} else {
%>
        	<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                <img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
          	</a>
<%        		
        	}
        }
      }
    } // endWhile
  } // endif
%>