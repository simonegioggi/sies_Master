<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@ page import="f3b.web.IWebConstants"%>
 <%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
 <%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
 <%@ page import="f3b.security.model.FunctionModel"%>
 <%@ page import="f3b.log.LogF3B"%>
 
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
 <jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
 <jsp:useBean id="Modificabile"     scope="request" class="java.lang.String"/>
 <jsp:useBean id="Cancellabile"     scope="request" class="java.lang.String"/>
 
 
 <%
  // Funzioni abilitate
  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
 // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 // siesLogger.debug("Bottone Modifica Cancella");

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {

    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();
 
      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK))
       { 
 
          if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA ) && Modificabile.compareTo("SI")==0)
       	  { 
  %>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="<%=lFun.getLabelFunction()%>" width="24" height="24" border="0">
          </a>
<%
       	  }
          else  if( lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)  && Cancellabile.equals("SI"))
          {
             %>
          <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="<%=lFun.getLabelFunction()%>" width="24" height="24" border="0">
          </a>
            <%
          }
       }
    } // endWhile
  } // endif
%>