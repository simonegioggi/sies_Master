<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.jms.ICostantiJMS" %>
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

  String lModificabile= "SI";
  boolean lDiProprieta = true; //Booleano che indica se il fasicolo è prorpio o di un altro ufficio


  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
    // Nel caso di "Posizione Materiale Fascicolo SIUS" se il fascicolo
    // non è modificabile bisogna disattivare anche il bottone di Inserimento.
    if (lModificabile.equalsIgnoreCase("NO") && request.getParameter("tipo_posizione_materiale") != null)
    {
       if (request.getParameter("tipo_posizione_materiale").equalsIgnoreCase("SIUS"))
          lDiProprieta = false;
    }
  }
  else
  {
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();

  if (!(lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UDS")||lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("TDS")))
   {
      FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");
      if (lFas != null)
      {
        if (!lUffUtente.equals(lFas.getChiaveUfficio()))
           lDiProprieta = false;
      }
    }
  }

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    String flagDocReg=request.getParameter("FlagDocumentoRegistrato");

    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && lDiProprieta)
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)  && (lModificabile.compareTo("SI")==0 && lDiProprieta))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && (lModificabile.compareTo("SI")==0 && lDiProprieta))
        {
%>
          <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
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
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO)&& lDiProprieta)
        {
          if (flagDocReg.compareTo("S") == 0) {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasmissione provvedimento" width="24" height="24" border="0">
          </a>
<%				}
        }

        lIterBottoni.remove();
      }
    }
    //Visualizzazione della combo eliminata!
  }

  if(   request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE) != null
     && !request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE).equals(""))
  {
    String lAzioneChiamante = request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
%>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAzioneChiamante%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
<%
  }
%>