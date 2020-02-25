<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
 
<jsp:useBean id="modificaOggettoAtto"     scope="request" class="java.lang.String"/>
 
 <%
	// Funzioni abilitate
  	Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
  	boolean modificabile = false;

  	if (modificaOggettoAtto.equalsIgnoreCase("SI")) {
		modificabile = true;
 	}
  
  	// Visualizzazione dei bottoni
  	if ( (lFunFiglie != null) && (lFunFiglie.size() != 0) ) {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA ) && modificabile)
        {
%>
			<%-- MERGE v10 COLLAUDO: modificato layout della pagina --%>
			<td class="LBG">
	            <a href="Main.jsp?Action=<%=lFun.getNameAction()%>&<%=ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE%>=<%=request.getParameter(ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE)%>">
	            	<img src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
	            </a>&nbsp;
	        </td>
<%
        }
      }
    } // endWhile
  } // endif
%>