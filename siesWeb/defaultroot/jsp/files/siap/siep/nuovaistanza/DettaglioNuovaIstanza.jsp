<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovaistanza" scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="competenza" scope="request" class="java.lang.String" />

<%
    String lAnnullato = "N";
if (nuovaistanza != null
		&& nuovaistanza.getCodStatoIstanza() != null
		&& nuovaistanza.getCodStatoIstanza().equals("08")) {
       lAnnullato = "S";
     }
%>
<html>
<head>

  <title> Dettaglio Istanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">
// Richiamo della finestra di pop-up per inserire motivazione Annullamento
function conferma(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2) {
	if (window.confirm("Confermi l'annullamento?")) {
       var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 +"&<%=IWebConstants.GOTO_PAGE%>=<%=(String)request.getAttribute(IWebConstants.GOTO_PAGE)%>", "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
       window.parent.close();
    }
}

</script>
</head>
<body class="corpo">
<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Istanza</font>
     </td>
<%
if (competenza=="SI") {
%>     
     <td class="LBG">
	     	  <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_NUOVA_ISTANZA%>">
              <jsp:param name="CampoIdEntita" value="<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>" />
              <jsp:param name="ValoreIdEntita" value="<%=nuovaistanza.getIdNuovaIstanza()%>" />
              <jsp:param name="CampoIdEntitaEvento" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
              <jsp:param name="ValoreIdEntitaEvento" value="<%=nuovaistanza.getEveIdEvento()%>" />
              <jsp:param name="annullato" value="<%=lAnnullato%>" />
            </jsp:include>
        <td class="LBG">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_REGISTRO_ISTANZE%>">
           <jsp:param name="CampoIdEntita" value="ChiaveFascicolo" />
           <jsp:param name="ValoreIdEntita" value="<%=nuovaistanza.getFasSieIdFascicoloSiep()%>" />
           <jsp:param name="FlagValidato" value="" />
        </jsp:include>
      </td>   
<%
}
%>            
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
</FORM>

   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<table cellspacing=4 cellpadding=4>
<%

//ISTANZA Pervenuta
if (nuovaistanza != null && "P".equals(nuovaistanza.getFlagPresdep())) {
%>
  <tr>
      <td class="titolo">Dati Dell'Istanza</td>
      <td class="titolo" align="center">Pervenuta</td>                          
   </tr>  
  <tr>
    <td class="l">Data Atto</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
  </tr>
  <tr>
	  	<td class="l">Autorita' Mittente</td>
    <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
		<td class="l"><font class="campo">
			<%=StringUtils.toStringJSP(nuovaistanza.getDescrAutoritaMittente())%>&nbsp;<%=StringUtils.toStringJSP(nuovaistanza.getDescrMittente())%></font>
			&nbsp;di&nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrSedeMittente()) %></font>
		</td>
  </tr>
<%
} else if (nuovaistanza != null && "D".equals(nuovaistanza.getFlagPresdep())) {
	// ISTANZA Depositata
%>
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
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getSoggPresentante()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Identificato con</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getSoggPresentanteIdentificato()) %></font>&nbsp;</td>
  </tr>
  <tr>
    <td class="l">Presentata da Avvocato</td>
<%
	if(nuovaistanza.getAvvocatoPresentante() != null) {
%>
  <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(nuovaistanza.getAvvocatoPresentante().getNome()) %></font>
		</td>
<%
	} else {
%>
   <td class="l">&nbsp;</td>
<%
	}
%>
	</tr>  
<%
}
		String NomeAvvocato="-", Foro="-",TipoDifensore="-",  Nominato="";
if (nuovaistanza.getAvvocato() != null) {
			NomeAvvocato = nuovaistanza.getAvvocato().getCognome()+" " +nuovaistanza.getAvvocato().getNome(); 
			Foro=nuovaistanza.getAvvocato().getForo();
			TipoDifensore=nuovaistanza.getAvvocato().getDescrTipo();
	// Ticket#20230328019 - SIEP : Data visualizzata Istanza non corretta
	// Nominato = StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataIstanza(),"dd-MM-yyyy"));
	Nominato = StringUtils.toStringJSP(DateUtils.getDateToString(nuovaistanza.getDataNotificaAvvocato(),"dd-MM-yyyy"));
	// Ticket#20230328019 - FINE
		}
%>
  <tr>
		<td class="l">Avvocato</td>
		<td class="l">
			<font class="campo"><%=NomeAvvocato%></font>
			<input type="hidden" value="<%=nuovaistanza.getAvvIdAvvocato()%>" name="<%= ICostantiNuovaIstanza.CAMPO_AVV_ID_AVVOCATO%>">
		</td>
	</tr>
	<tr>
		<td class="l">Foro di Competenza</td>
		<td class="l"><font class="campo"><%=Foro%></font></td>
	</tr>
	<tr>
		<td class="l">Tipo Difensore</td>
		<td class="l"><font class="campo"><%=TipoDifensore%></font></td>
	</tr>
	<tr>
		<td class="l">Nominato in Data</td>
		<td class="l"><font class="campo"><%=Nominato%>&nbsp;</font></td>
	</tr>
	<tr>
    <td class="l">Contenuto</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrContenuto())%></font></td>
  </tr>  
  <tr>
    <td class="l">Note</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getNote()) %></font></td>
  </tr>

  <tr>
    <td class="l">Stato Istanza</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrStatoIstanza()) %></font></td>
  </tr>
  <tr>
    <td class="l">Ufficio Destinatario</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrTipoUfficioDestinatario()) %></font></td>
  </tr>
  <tr>
    <td class="l">Luogo Destinatario</td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(nuovaistanza.getDescrLuogoDestinatario()) %></font></td>
  </tr>
</table>

</body>
</html>