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
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="eventoNotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="nuovaistanza" scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Istanza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Istanza Ricevuta</font>
          </td>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
          <jsp:include page="<%=ICostantiDepositoOrdinanzaPc.PG_TOOLBAR_HEADER%>">
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
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();
%>

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
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

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">

 <%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
       <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
<%
      }
%>

      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
          <font class="label">del</font>&nbsp;

            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>

        </font>
         <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr--%>
  </table>

  <br>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoOrdinanza" onsubmit="document.ConfermaPresaInCaricoOrdinanza.I.disabled=true;">
  <table cellspacing=4 cellpadding=4 >

<%
//ISTANZA Pervenuta
if(nuovaistanza != null && "P".equals(nuovaistanza.getFlagPresdep())) 
{
%>
  <tr>
      <td class="titolo">Dati Dell'Istanza</td>
      <td class="titolo" align="center">Pervenuta</td>                          
   </tr>  
  <tr>
    <td class="l">Data Istanza</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Mittente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrAutoritaMittente()) %> - <%=StringUtils.toStringJSP(nuovaistanza.getDescrMittente()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Sede Mittente</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrSedeMittente()) %></font>&nbsp;</td>
  </tr>  

<%}else if(nuovaistanza != null && "D".equals(nuovaistanza.getFlagPresdep()))  
{%>
  <tr>
      <td class="titolo">Dati Dell'Istanza</td>
      <td class="titolo" align="center">Depositata</td>                          
   </tr>  
  <tr>
    <td class="l">Depositata in data</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Soggetto Presentante</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getSoggPresentanteIdentificato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Presentata da Avvocato</td>
<%
  if(nuovaistanza.getAvvocatoPresentante() != null)
  {
%>
	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getNome())%>&nbsp;<%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getCognome()) %></font>&nbsp;</td>
<%
  }else 
  {
%>
   <td class="l">&nbsp;</td>
<% 
  }
%>
 
  </tr>  

<% }%>
  <tr>
    <td class="l">Avvocato</td>
<%
  if(nuovaistanza.getAvvocato() != null)
  {
%>
    <input type="hidden" value="<%=nuovaistanza.getAvvIdAvvocato()%>" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>">
    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getAvvocato().getNome())%>&nbsp;<%=StringUtils.toStringJSP(nuovaistanza.getAvvocato().getCognome()) %></font>&nbsp;</td>
<%
  }else 
  {
%>
   <td class="l">&nbsp;</td>
<% 
  }
%>    
  </tr>
  <tr>
    <td class="l">Foro di compentenza</td>
<%
  if(nuovaistanza.getAvvocato() != null)
  {
%>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getAvvocato().getForo()) %> </font>&nbsp;</td>
<%
  }else 
  {
%>
   <td class="l">&nbsp;</td>
<% 
  }
%>     
  </tr>
  <tr>
    <td class="l">Tipo difensore</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrTipoAvvocato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Nominato in data</td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataNotificaAvvocato(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Contenuto</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrContenuto()) %></font>&nbsp;</td>
  </tr>  
  <tr>
    <td class="l">Note</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getNote()) %></font>&nbsp;</td>
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
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActConfermaPresaInCarico">
      <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
  </table>
	<%}%>
  </form >
</body>
</html>