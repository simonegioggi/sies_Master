<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenza" scope="request"		class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="TornaQui" scope="request" 		class="java.lang.String" />
<jsp:useBean id="NomeAzione" scope="request" 	class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" 	class="java.lang.String" />

<%
SentenzaModel lSentenza = new SentenzaModel(sentenza);

//a Seconda delL'AUTORITA' EMITTENTE preparo 2 conbo distinte:	<--	--	--	--	
String lTitolo="";
if(lSentenza.getCodTipoAutoritaEmittente()!=null)
{	
	if(lSentenza.getCodTipoAutoritaEmittente().compareTo("UDSM")==0 || 
		lSentenza.getCodTipoAutoritaEmittente().compareTo("UDS")==0 ||
		lSentenza.getCodTipoAutoritaEmittente().compareTo("TDS")==0 )
	{
 		lTitolo="Dettaglio Estremi Provvedimento - Fascicolo di Misura Sicurezza disposta fuori sentenza";
	}
	else
	{
		lTitolo="Dettaglio Estremi Provvedimento - Fascicolo di Misura Sicurezza Provvisoria";
	}
}
//
String lTipoprov="";
if(lSentenza.getDescrTipoProvvedimento()!=null)
	lTipoprov=lSentenza.getDescrTipoProvvedimento();
//
String ARG = "";
String NRG = "";
String Tipo = "";
if (lSentenza.getAnnoRegeCap() != null) {
	ARG = lSentenza.getAnnoRegeCap() + "";
	NRG = lSentenza.getNumeroRegeCap() + "";
	Tipo = "cap";
}
if (lSentenza.getAnnoRegeCas() != null) {
	ARG = lSentenza.getAnnoRegeCas() + "";
	NRG = lSentenza.getNumeroRegeCas() + "";
	Tipo = "cas";
}
if (lSentenza.getAnnoRegeDib() != null) {
	ARG = lSentenza.getAnnoRegeDib() + "";
	NRG = lSentenza.getNumeroRegeDib() + "";
	Tipo = "dib";
}
if (lSentenza.getAnnoRegeGip() != null) {
	ARG = lSentenza.getAnnoRegeGip() + "";
	NRG = lSentenza.getNumeroRegeGip() + "";
	Tipo = "gip";
}
if (lSentenza.getAnnoRegeCasap() != null) {
	ARG = lSentenza.getAnnoRegeCasap() + "";
	NRG = lSentenza.getNumeroRegeCasap() + "";
	Tipo = "casap";
}
// MEV_66: aggiunte quattro nuove proprietà
if (lSentenza.getAnnoRegeGup() != null) {
	ARG = lSentenza.getAnnoRegeGup() + "";
	NRG = lSentenza.getNumeroRegeGup() + "";
	Tipo = "gup";
}
if (lSentenza.getAnnoRegeCapsm() != null) {
	ARG = lSentenza.getAnnoRegeCapsm() + "";
	NRG = lSentenza.getNumeroRegeCapsm() + "";
	Tipo = "capsm";
}
%>

<!--  DettaglioDatiProvvedimentoMSFuoriSent	 -->
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Dati provvedimento di fascicolo di Misura sic. disposta fuori sentenza o provvisoria -</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
</head>

<BODY class="corpo">

<FORM name="comandi">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; 
			<font class="campo"><%=lTitolo %></font>
		</td>
		<td class="LBG">
	      	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
	    </td>
 <%
	  	if(Modificabile.equals("SI") )
	    {%>
				<!-- BOTTONE DI MODIFICA -->
	      <td class="LBG">
	        <a href="/jsp/Main.jsp?Action=siap.siep.sentenza.action.ActLoadModificaDatiProvvedimentoMSFuoriSent&IdSentenza=<%=lSentenza.getIdSentenza()%>">
	          <img align="middle" src="/images/modifica24.gif" alt="Modifica Provvedimento" width="24" height="24" border="0">
	        </a>
	      </td>
	<%   } %>
	</tr>
</table>
</FORM>
<table cellspacing=2 cellpadding=2>

	<tr>
		<td class="l">Tipo Provvedimento </td>
		<td class="L"><font class="campo">
			<%=StringUtils.toStringJSP(lSentenza.getDescrTipoProvvedimento() )%> </font>
		</td>
	</tr>
	<tr>
		<td class="l">Data <%=lTipoprov%></td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "dd-MM-yyyy"))%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero <%=lTipoprov%></td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getAnnoSentenza())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrTipoAutoritaEmittente())%></font>&nbsp;
		</td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrLuogoEmittente())%></font>&nbsp;
		</td>
	</tr>
<%	if(lSentenza.getNumSezioneAutoritaEmittente()!=null &&
	  !lSentenza.getNumSezioneAutoritaEmittente().equals("") )
	{%>	
	<tr>
		<td class="l">Sezione Autorità Emittente</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getNumSezioneAutoritaEmittente())%></font>&nbsp;
		</td>
	</tr>
<%	} %>

<%	if ( (lSentenza.getCodTipoAutoritaEmittente().equals("DIB")
		  || lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD") ) &&
		lSentenza.getCodTipoRito()!=null && !lSentenza.getCodTipoRito().equals("-") )
	{	%>
		<tr>
			<td class="l">Tipo Rito</td>
			<td class="L" colspan=5><font class="campo">
				<%=StringUtils.toStringJSP(lSentenza.getDescrTipoRito())%></font>
	  &nbsp;</td>
		</tr>
<%	}%>	
	<tr><td>&nbsp;</td></tr>
<%  if(lSentenza.getAnnoRegePm()!=null && !lSentenza.getAnnoRegePm().equals("") )
	{%>
	<tr>
		<td class="l">Anno/Numero R.G.N.R.</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>&nbsp;
		/ <font class="campo"><%=StringUtils.toStringJSP(lSentenza
									.getNumeroRegePm())%></font>&nbsp;
		</td>

		<td class="l"></td>
		<td class="L"></td>
		<td class="l"></td>
		<td class="L"></td>
	</tr>
<%	} %>	

<%	if(!Tipo.equals(""))
  	{	%>	
	<tr>
		<td class="l">Numero Reg.Gen.</td>
		<td class="L" colspan=5><font class="campo"> <%=ARG%>
		/ <%=NRG%>&nbsp;&nbsp;&nbsp; <%=Tipo%></font></td>
	</tr>
<%	} %>

<%	if(lSentenza.getDescrSedeNotiziaReato()!=null && 
	!lSentenza.getDescrSedeNotiziaReato().equals("") &&
	!lSentenza.getDescrSedeNotiziaReato().equals("-") )
	{%>
	<tr>
		<td class="l">Sede PM</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrSedeNotiziaReato())%></font>&nbsp;</td>
	</tr>	
<%	} %>	
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
		</td>
	</tr>
</table>
<br>
</body>

</html>