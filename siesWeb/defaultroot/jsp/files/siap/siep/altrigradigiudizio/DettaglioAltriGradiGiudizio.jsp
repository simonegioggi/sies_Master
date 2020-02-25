<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel"%>
<%@ page import="siap.siep.altrigradigiudizio.action.ICostantiAltriGradiGiudizio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="altrogradogiudizio" scope="request" class="siap.siep.altrigradigiudizio.model.AltriGradiGiudizioModel" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="sentenza" scope="session" class="siap.siep.sentenza.model.SentenzaModel" />
<%
	AltriGradiGiudizioModel lAltroGradoGiudizio = new AltriGradiGiudizioModel(altrogradogiudizio);
	String lTipoRito = "-";
	if(StringUtils.toStringJSP(lAltroGradoGiudizio.getCodTipoRito())!=null &&
		 StringUtils.toStringJSP(lAltroGradoGiudizio.getCodTipoRito()).compareTo("M")==0	)
		lTipoRito = "Monocratico";
	if(StringUtils.toStringJSP(lAltroGradoGiudizio.getCodTipoRito())!=null &&
		 StringUtils.toStringJSP(lAltroGradoGiudizio.getCodTipoRito()).compareTo("C")==0	)
			lTipoRito = "Collegiale";
	
%>
<html>
<head>		
<title>[S.I.A.P.] - Dettaglio Altri Gradi Giudizio </title>

<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
<%
			if (!lTipoFunzione.equals("")
			&& !lTipoFunzione.equals("ritornodettaglio")) // lTipoFunzione per capire che si proviene da iscrizione guidata
	{
%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("FascSiep");

    document.Abbandona.A.disabled = true;
    document.FascSiep.NS.disabled = true;

   aForm.submit();
  }
</script>
<%
}
%>
</head>


<BODY class="corpo">
<FORM name="comandi" >
    <table>
      <tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
			class="campo">Dettaglio Altro Grado Giudizio</font></td>
		<%
			if (lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
			{
		%>
		<td class="LBG">
		<%String lCancellabile ="SI";
		if (lAltroGradoGiudizio.getEsistonoFascicoliAssociati())
			{//Se la sentenza ha un fascicolo associato non si cancella
				lCancellabile = "NO";
			}
		%>
		
<% if (Modificabile.length() > 0) {%>		
		<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiAltriGradiGiudizio.CAMPO_ID_ALTRIGRADIGIUDIZIO%>" />
			<jsp:param name="ValoreIdEntita" value="<%=altrogradogiudizio.getIdAltrigradigiudizio()%>" />
			<jsp:param name="Modificabile" value="<%=Modificabile%>" />
			<jsp:param name="Cancellabile" value="<%=lCancellabile%>" />
		</jsp:include></td>
	<%} else { %>
		<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiAltriGradiGiudizio.CAMPO_ID_ALTRIGRADIGIUDIZIO%>" />
			<jsp:param name="ValoreIdEntita" value="<%=altrogradogiudizio.getIdAltrigradigiudizio()%>" />
			<jsp:param name="Cancellabile" value="<%=lCancellabile%>" />
		</jsp:include></td>
<%} %>		
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
		<%
		}
		%>
		<%
			if (request.getParameter("NomeAzione") != null
			&& request.getParameter("NomeAzione").equals(
			"siap.siep.altrigradigiudizio.action.ActRicercaAltriGradiGiudizio")) {
		%>
			<td class="LBG"><a href="javascript:history.go(-1);"> <img
				align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif"
				alt="ritorna su" width="24" height="24" border="0"> </a></td>
		<%
		}
		%>
	</tr>
 </table>

</FORM>

<table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
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

  </table>
  <br>
		 <table cellspacing=2 cellpadding=2>
		 <tr>
			<td class="Titolo" colspan=6>Sentenza di Primo Grado</td>
		</tr>	
		
		<tr>
			<td class="l">Data Sentenza</td>
				<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
					lAltroGradoGiudizio.getDataSentenzaIGrado(), "dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
		
		<tr>
			<td class="l">Anno/Numero Sentenza</td>
				<% if (!altrogradogiudizio.getAnnoSentenzaIGrado().equals(null) && !altrogradogiudizio.getAnnoSentenzaIGrado().equals("")) {%>
					<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio
											.getAnnoSentenzaIGrado())%></font>&nbsp; / <font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaIGrado())%></font>&nbsp;
					</td>
				<%} %>
		</tr>
		
		<tr>
			<td class="l">Autorità Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrAutEmittSentIGrado())%></font>&nbsp;</td>
		</tr>		
		
		<tr>
			<td class="l">Luogo Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrLuoEmittSentIGrado())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l">Sezione Autorità Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumSezEmittSentIGrado())%></font>&nbsp;</td>
		</tr>
		<%
			if (lAltroGradoGiudizio.getCodAutEmittSentIGrado().equals("DIB") || 
					lAltroGradoGiudizio.getCodAutEmittSentIGrado().equals("TRIBSD")) {
		%>
				<tr>
					<td class="l">Tipo Rito</td>
					<td class="L" colspan=5><font class="campo"><%=lTipoRito%></font>&nbsp;</td>
				</tr>
		<%
		}
		%>
		
				
		<tr>
			<td class="Titolo" colspan=6>Sentenza di Secondo Grado</td>
		</tr>
		
		<tr>
			<td class="l">Tipo Sentenza</td>
			<td class="L" colspan=5>
				<font class="campo">
				<%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrTipoSentenzaIiGrado())%>
				</font>&nbsp;
			</td>
		</tr>
		
		<tr>
			<td class="l">Data Sentenza</td>
				<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
					lAltroGradoGiudizio.getDataSentenzaIiGrado(), "dd-MM-yyyy"))%></font>&nbsp;</td>
		</tr>
		
		<tr>
			<td class="l">Anno/Numero Sentenza</td>	
				<% if (lAltroGradoGiudizio.getNumeroSentenzaIiGrado() != null) {
				%>
					<td class="L" colspan=5>
						<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoSentenzaIiGrado())%></font>&nbsp; / 
						<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaIiGrado())%></font>&nbsp;
					</td>
				<%
				}else{ 
				%>
					<td class="L" colspan=5><font class="campo"></font>&nbsp;</td>
				<%} %>
		</tr>
		
		<tr>
			<td class="l">Autorità Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrAutEmittSentIiGrado())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l">Luogo Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getDescrLuoEmittSentIiGrado())%></font>&nbsp;</td>
		</tr>
		<tr>
			<td class="l">Sezione Autorità Emittente</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumSezEmittSentIiGrado())%></font>&nbsp;</td>
		</tr>
		<%
			if (lAltroGradoGiudizio.getCodAutEmittSentIiGrado().equals("DIB")	|| 
					lAltroGradoGiudizio.getCodAutEmittSentIiGrado().equals("TRIBSD")) {
		%>
				<tr>
					<td class="l">Tipo Rito</td>
					<td class="L" colspan=5><font class="campo"><%=lTipoRito%></font>&nbsp;</td>
				</tr>
		<%
		}
		%>
		
		
		<tr>
			<td class="Titolo" colspan=6>Sentenza di Cassazione di rinvio</td>
		</tr>
		
		<tr>
			<td class="l">Anno/Numero Reg.Gen.</td>
			
			<% if (lAltroGradoGiudizio.getNumeroRegGenCassaz() != null) {
			%>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoRegGenCassaz())%></font>&nbsp; / 
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroRegGenCassaz())%></font>&nbsp;
				</td>
			<%
			}else{ 
			%>
				<td class="L" colspan=5><font class="campo"></font>&nbsp;</td>
			<%} %>
		</tr>
		
		<tr>
			<td class="l">Anno/Numero Sentenza</td>
			
			<% if (lAltroGradoGiudizio.getNumeroSentenzaCassaz() != null) {
			%>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoSentenzaCassaz())%></font>&nbsp; / 
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroSentenzaCassaz())%></font>&nbsp;
				</td>
			<%
			}else{ 
			%>
				<td class="L" colspan=5><font class="campo"></font>&nbsp;</td>
			<%} %>							
		</tr>
				
				
		<tr>
			<td class="l">Anno/Numero Raccolta Generale</td>
			
			<% if (lAltroGradoGiudizio.getNumeroRaccGenealeIiGrado() != null) {
			%>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getAnnoRaccGenealeIiGrado())%></font>&nbsp; / 
					<font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio.getNumeroRaccGenealeIiGrado())%></font>&nbsp;
				</td>
			<%
			}else{ 
			%>
				<td class="L" colspan=5><font class="campo"></font>&nbsp;</td>
			<%} %>								
		</tr>
		
		<tr>
			<td class="l">Dispositivo</td>
			<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltroGradoGiudizio
								.getDescrTipoDecisioneCassazione())%></font>&nbsp;</td>
		</tr>							
		</table>	</body>
</html>