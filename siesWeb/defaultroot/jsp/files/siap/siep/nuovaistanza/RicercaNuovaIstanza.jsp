<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>
<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.SIEPException"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="nuovaistanza" scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicolosiep" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Istanza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
// Richiamo della finestra di pop-up per inserire motivazione Annullamento
function conferma(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2)
{
   if (window.confirm("Confermi l'annullamento?"))
   {
       var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 +"&<%=IWebConstants.GOTO_PAGE%>=<%=(String)request.getAttribute(IWebConstants.GOTO_PAGE)%>", "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
       window.parent.close();
    }
}

function chiama(idIstanza)
{
    window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.nuovaistanza.action.ActLoadCancellaNuovaIstanza&<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>="+idIstanza,"Annulla_Istanza", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}
</script>
</head>
<body class="corpo">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font>&nbsp;&nbsp;
      <font class="campo">Ricerca Istanza</font>
     </td>
    </tr>
  </table>
  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Luogo di Nascita</td>
      <td class="int">Data di Nascita</td>
      <td class="int">Oggetto</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Stato Istanza</td>
 	  <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = nuovaistanza.iterator();
  Iterator itx1 = fascicolosiep.iterator();
  while ( itx.hasNext())
  {
	  NuovaIstanzaModel lIstanza = (NuovaIstanzaModel)itx.next();
	  FascicoloSiepModel fascSiepMod = (FascicoloSiepModel)itx1.next();
%>
    <tr>
         <% if (fascSiepMod == null) {
        	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        	  siesLogger.debug("WARNING: Errore per lIstanza.getFasSieIdFascicoloSiep() " + lIstanza.getFasSieIdFascicoloSiep());
        	  %>
              <td class="l">Dati Fascicolo Incompleti</td>
              <td class="l">-</td>
              <td class="l">-</td>
              <td class="l">-</td>
              <td class="l"><%=StringUtils.toStringJSP(lIstanza.getDescrContenuto(),"-")%></td>
              <td class="l">ID: <%=lIstanza.getFasSieIdFascicoloSiep()%></td>
              <%
              String lAnnullato = "N";
         	  if((lIstanza.getCodStatoIstanza() != null) && (lIstanza.getCodStatoIstanza().compareTo("08") == 0)) { 
         	  lAnnullato = "S";	 %>
      		  <td class="cRosso">ANNULLATA</td>
      		  <% }
      			else  { %>
			 <td class="l"><%=StringUtils.toStringJSP(lIstanza.getDescrStatoIstanza(),"-")%></td>
	      	  <% } %>

	      	<td class="c">
        	<jsp:include page="<%=ICostantiIstanza.PG_BUTTONS_ISTANZA%>">
          	<jsp:param name="CampoIdEntitaEvento" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          	<jsp:param name="ValoreIdEntitaEvento" value="<%=lIstanza.getEveIdEvento()%>" />
          	<jsp:param name="CampoIdEntitaIstanza" value="<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>" />
          	<jsp:param name="ValoreIdEntitaIstanza" value="<%=lIstanza.getIdNuovaIstanza()%>" />
          	<jsp:param name="annullato" value="<%=lAnnullato%>" />
        	</jsp:include>
      		</td>
              <%
        	  continue;
          }
         
         if (fascSiepMod != null && fascSiepMod.getSoggetto() != null && fascSiepMod.getSoggetto().getCognome() != null) {
        	 %>
             <td class="l"><%=fascSiepMod.getSoggetto().getCognome()%></td>
         <% } else { %>
        <td class="l">-</td>
        <%}
         if (fascSiepMod != null && fascSiepMod.getSoggetto() != null && fascSiepMod.getSoggetto().getNome() != null) {
        	 %>
             <td class="l"><%=fascSiepMod.getSoggetto().getNome()%></td>
         <% } else { %>
        <td class="l">-</td>
        <%}
         if (fascSiepMod != null && fascSiepMod.getSoggetto() != null && fascSiepMod.getSoggetto().getDescrComuneNascita() != null) {
        	 %>
             <td class="l"><%=fascSiepMod.getSoggetto().getDescrComuneNascita()%></td>
         <% } else { %>
        <td class="l">-</td>
        <%}
         if (fascSiepMod != null && fascSiepMod.getSoggetto() != null && fascSiepMod.getSoggetto().getDataNascita() != null) {
        	 %>
             <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascSiepMod.getSoggetto().getDataNascita(),"dd-MM-yyyy"))%></td>
         <% } else { %>
        <td class="l">-</td>
        <%}
         %>
      <td class="l"><%=StringUtils.toStringJSP(lIstanza.getDescrContenuto(),"-")%></td>
         <%
         if (fascSiepMod != null && fascSiepMod.getChiaveAnno() != null && fascSiepMod.getChiaveProgr() != null) {
         %>
      <td class="l"><%=fascSiepMod.getChiaveAnno()%>/<%=fascSiepMod.getChiaveProgr()%></td>
      <% } else { %>
      <td class="l">0000/0000</td>
      <%}
         String lAnnullato = "N";
         if((lIstanza.getCodStatoIstanza() != null) && (lIstanza.getCodStatoIstanza().compareTo("08") == 0)) { 
         	lAnnullato = "S";	%>
      		<td class="cRosso">ANNULLATA</td>
      <% }
      	else  { %>
		<td class="l"><%=StringUtils.toStringJSP(lIstanza.getDescrStatoIstanza(),"-")%></td>
	      <% } %>
      <td class="c">
        <jsp:include page="<%=ICostantiIstanza.PG_BUTTONS_ISTANZA%>">
          <jsp:param name="CampoIdEntitaEvento" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntitaEvento" value="<%=lIstanza.getEveIdEvento()%>" />
          <jsp:param name="CampoIdEntitaIstanza" value="<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>" />
          <jsp:param name="ValoreIdEntitaIstanza" value="<%=lIstanza.getIdNuovaIstanza()%>" />
          <jsp:param name="annullato" value="<%=lAnnullato%>" />
 
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
    </table>
  </div>
	</body>
</html>