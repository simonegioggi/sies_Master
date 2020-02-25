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
<script language="JavaScript">
	function confermaRestituzione(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2)
	{
   	if (window.confirm('Confermi la restituzione ?'))
   	{
			var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_parameter + "=" +a_entityname + "&" + a_parameter2 + "=" +a_entityname2, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         window.parent.close();
    }
	}
</script>

<%

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    String flagVisto=request.getParameter("FlagVisto");

    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) )
        {
          if (flagVisto.compareTo("S") != 0  && flagVisto.compareTo("R")!= 0) {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&CodTipoOperazione=<%=ICostantiJMS.ESITO_TRASFERIMENTO_ORDINANZA%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Presa in Carico Ordinanza e Iscrizione Procedimento SIEPE" width="24" height="24" border="0">
          </a>
<%				}
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) )
        {
          if (flagVisto.compareTo("N") == 0) {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&CodTipoOperazione=<%=ICostantiJMS.ESITO_TRASFERIMENTO_ORDINANZA%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>PresaVisione.gif" alt="Presa Visione Ordinanza Ricevuta" width="24" height="24" border="0">
          </a>
<%				}
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) )
        {
					String nameAction = lFun.getNameAction().concat("&CodTipoOperazione="+ICostantiJMS.ESITO_TRASFERIMENTO_ORDINANZA);
          if (flagVisto.compareTo("N") == 0) {
%>
          <a href="Javascript:confermaRestituzione('<%=nameAction%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>', 'Confermi la restituzione ?');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>Restituzione.gif" alt="Restituzione Ordinanza al Mittente" width="24" height="24" border="0">
          </a>
<%				}
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
%>
          <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa Ordinanza" width="24" height="24" border="0">
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