<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  
  boolean attivaBottoni = true;


  if (request.getParameter("Modificabile") != null)
  {
    String lModificabile = request.getParameter("Modificabile");
 
    if (lModificabile.equalsIgnoreCase("NO") )
    {
    	attivaBottoni = false;
    }
  }

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) && attivaBottoni)
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) )
        {
%>          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)  )
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA))
        {
%>
          <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
        // Nuova Stampa Luigi 26-11-2004
%>
          <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO_COPIA))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrow24.gif" alt="Inserimento con Copia" width="24" height="24" border="0">
          </a>
<%
        }

      }
    } // end while
  }
%>