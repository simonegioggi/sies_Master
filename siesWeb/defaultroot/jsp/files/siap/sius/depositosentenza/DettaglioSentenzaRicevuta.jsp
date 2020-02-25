<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.xml.TreeModel" %>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.sico.evento.model.XModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="eventoNotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Sentenza Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Sentenza Ricevuta</font>
          </td>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
          <jsp:include page="<%=ICostantiDepositoSentenza.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=Messaggio.getIdMessaggio()%>" />
          <jsp:param name="FlagVisto" value="<%=Messaggio.getFlagVisto()%>" />
          </jsp:include>
     </td>
<%
  		String lStato="Stato Messaggio : ";
  		if (Messaggio.getFlagVisto().compareTo("N") == 0) lStato+="Ricevuto";
  		else if (Messaggio.getFlagVisto().compareTo("V") == 0) lStato+="Preso in Visione";
  		else if (Messaggio.getFlagVisto().compareTo("S") == 0) lStato+="Preso in Carico";
  		else if (Messaggio.getFlagVisto().compareTo("R") == 0)
      {
        lStato+="Restituito al Mittente";
        if (Messaggio.getMessaggioCorrelato() != null)
        {
        	XModel lXMod = (XModel)Messaggio.getMessaggioCorrelato().getTreeModel().getModel();
					if (lXMod != null && lXMod.getMessage()!=null)
        		lStato += "  -    Motivo : "+lXMod.getMessage();
        }
      }
%>
     <td class="LBG">
       <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>news.gif" alt="<%=lStato%>" width="24" height="24" border="0">
     </td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>
      </table>
    </FORM>
<%
  SoggettoModel soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();
%>
  <table cellspacing=2 cellpadding=2 width=70%>
    <tr><td class="Titolo" colspan=4>Fascicolo SIUS</td></tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno()%>
          /
          <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr()%>
        </font>
       <font class="label"> relativo a: <%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
       </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if(soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
<%  if ((fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP())!=null)
    {
%>
    <tr>
      <td class="L">
        <font class="label">Titolo Esecutivo: N.ro SIEP </font>

        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>">
            <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgrSIEP()%>
          </a>
        </font>&nbsp;
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="label"><%=fascicoloSiusGP.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrComuneUfficio()%> del </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
      </td>
    </tr>
<%
    }
%>
    <tr>
      <td class="L">
        <font class="label">Data Udienza : </font>
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-" )%></font>
        </font>
      </td>
    </tr>
  </table>
  <br>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoOrdinanza" onsubmit="document.ConfermaPresaInCaricoOrdinanza.I.disabled=true;">
  <table cellspacing=2 cellpadding=2 width="70%">
<!--------------------- EVENTO ---------------------->
    <tr><td class="Titolo" colspan=4>Sentenza</td></tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Natura Provvedimento</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(eventoNotifica.getEvento().getDescrEsito())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Oggetto Procedimento</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(eventoNotifica.getEvento().getDescrMotivo())%>&nbsp;</font></td>
    </tr>
  </table>
  <br>

<%
  if (!(UtenteConnesso.getUfficioUtente().getCodTipoUfficio().trim().startsWith("UEPE"))) {
%>
  <table cellspacing=2 cellpadding=2 width=95%>
      <tr>
        <td>
          <input class=bottone  name="I" type="submit" value="Conferma Presa in Carico">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.depositosentenza.action.ActConfermaPresaInCarico">
      <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
  </table>
	<%}%>
  </form >
</body>
</html>