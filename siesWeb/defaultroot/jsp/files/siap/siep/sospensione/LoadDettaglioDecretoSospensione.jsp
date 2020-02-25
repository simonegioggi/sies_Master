<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="eventonotificasimeone"    scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="autoritaN" scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>


<html>
<head>
<title>[S.I.E.S.] - Dettaglio Decreto</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSosp">

  <table>
    <tr>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <INPUT type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      <font class="campo">Dettaglio Decreto</font>
    </td>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaDecretoSospensione&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaDecretoSospensione&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
   <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
    </td>

 <%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaDecretoSospensione&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaDecretoSospensione&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
   <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
    </td>

<%}%>

    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
 <table>
    <tr>
      <td class="l" >Decreto di sospensione emesso in data</td>
       <td class="l">
        <font class="campo">
         <%if(eventonotificasimeone != null && eventonotificasimeone.getEvento()!= null)
          {%>
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotificasimeone.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
        <%}%>
        &nbsp;</font>
       </td>
    </tr>
    <tr>
      <td class="l">Decreto di irreperibilità emesso in data</td>
      <td class="l">
      <font class="campo">
           <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
      </font>
     </td>
   </tr>
   <tr>
    <td class="l">Magistrato Competente
    <td class="L" >
       <font class="campo">
          <%=StringUtils.toStringJSP(magistratocompetente.getCognome() )%> &nbsp; <%=StringUtils.toStringJSP(magistratocompetente.getNome() )%>
      </font>
    </td>
   </tr>


<%int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
   <tr>
	    <td class="l">Autorita delegata alla Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
     </tr>
<%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")){%>

     <tr>
      <td class="l">Indirizzo</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>

<%
}}
count++;
}
%>
<%
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("Aggiungo la sezione dei destinatari per la Restituzione Ordine Esecuzione con Sospensione");
  //============================================================================
  // Aggiungo la sezione dei destinatari per la Restituzione Ordine Esecuzione
  //============================================================================
  int count2=0;
  while(count2 < eventonotifica.getNotifiche().length)
  {
    NotificaModel lNotMod = eventonotifica.getNotifiche()[count2];

    if ( lNotMod.getCodTipoNotifica().equals("R") ) 
    {
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("notifica = "+lNotMod);
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("getAutoritaEsterna() = "+lNotMod.getAutoritaEsterna());

%>
    <tr>
      <td class="Titolo" colspan="4">Destinatari per la Restituzione Decreto Sospensione</td>
    </tr>
<%
      if (lNotMod.getIstDetIdIstitutoDetenzione()!= null ) {
        // Istituto di detenzione
        IstitutoDetenzioneModel istitutoModel = lNotMod.getIstitutoDetenzione();
%>
    <tr>
      <td class="l" >Istituto di Detenzione </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(istitutoModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutoModel.getDescrComune())%></font>
      </td>
    </tr>
<%
      }
      else if (lNotMod.getAutoritaEsterna() != null) 
      {
        // Autorità competente
        AutoritaEsternaModel lModAut = lNotMod.getAutoritaEsterna();
%>
    <tr>
      <td class="l" >Destinatario per esecuzione </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lModAut.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(lModAut.getDescrSede())%></font>
      </td>
    </tr>
<%
      }
%>
    <tr height="3"><td></td></tr>
<%
    }

    count2++;
  }  
  // end while
%>

</table>
</form>
 <br>
<%
	if ( 	 eventonotifica != null 
	    && eventonotifica.getEvento() != null 
	    && eventonotifica.getEvento().getFlagDocumentoRegistrato() != null 
	    && "S".equals( eventonotifica.getEvento().getFlagDocumentoRegistrato() ) 
	    )
	{
%>
			 <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scadenzario.action.ActLoadDettaglioScadenzario" title="Stato Notifiche">Visualizza Stato Notifiche</a>
		<br>
<%
	}
%>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActUploadDecretoSospensione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioDecretoSospensione">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>

  </body>
</html>