<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="descTipoProvvedimento" scope="request" class="java.lang.String" />

<% 
SentenzaModel lSentenza = new SentenzaModel(sentenza);
String titolo = "Dettaglio Sentenza Straniera";
String titoloDaEseguire = "Sentenza di appello";
String annoNum = "Provvedimento";
if (descTipoProvvedimento != null && descTipoProvvedimento.equals("Cumulo")) {
	titolo = "Dettaglio Altri Titoli";
	titoloDaEseguire = "Cumulo";
	annoNum = "SIEP";
} else if (descTipoProvvedimento != null && descTipoProvvedimento.equals("Ordinanza")) {
	titolo = "Dettaglio Altri Titoli";
	titoloDaEseguire = "Ordinanza";
	annoNum = "SIGE";
} else if (descTipoProvvedimento != null && descTipoProvvedimento.equals("Decreto di Archiviazione")) {
	titolo = "Dettaglio Altri Titoli";
	titoloDaEseguire = "Decreto di Archiviazione";
}
%>

<html>
	<head>
    	<title> [S.I.E.S.] - <%=titolo%> - </title>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" />
    	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    	</script>
  	</head>
  	<BODY class="corpo">
  		<FORM name="comandi" >
  			<input type="hidden" name="isSentenza" value="true" />
    		<table>
      			<tr>
      				<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        			<td class="LBG">
          				<font class="label">Funzione :</font>&nbsp;
          				<font class="campo"><%=titolo%></font>
        			</td>
        			<td class="LBG">
						<% if (Modificabile.length() > 0) {%>		
							<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
								<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" />
								<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>" />
								<jsp:param name="Modificabile" value="<%=Modificabile%>" />
							</jsp:include>
						<% } else { %>
				          	<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
				             	<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" />
				             	<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>" />
				          	</jsp:include>
		          		<% } %>
        			</td>
    				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
         			<% if(request.getParameter("NomeAzione") != null && request.getParameter("NomeAzione").equals("siap.siep.sentenza.action.ActRicercaSentenza")) { %>
          				<td class="LBG">
            				<a href="javascript:history.go(-1);">
            	 				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0" />
            				</a>
          				</td>
        			<% } %>
      			</tr>
    		</table>
  		</FORM>
  		<table cellspacing=2 cellpadding=2>    
    		<tr><td class="Titolo" colspan=6><%=titoloDaEseguire%> da Eseguire</td></tr>
			<% if (lSentenza.getDataSentenza() != null) { %>
    			<tr>
      				<td class="l">Data Sentenza</td>
      				<td class="L" colspan=5>
        				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataSentenza(),"dd-MM-yyyy"))%></font>&nbsp;
      				</td>
    			</tr>
			<% }
			if (descTipoProvvedimento != null) { %>
				<tr>
					<td class="l"> Tipo di Provvedimento </td>
					<td class="L" colspan=5>
  						<font class="campo"><%=StringUtils.toStringJSP(descTipoProvvedimento)%></font>&nbsp;
					</td>
				</tr>
			<% }
			// segnalazioni 4: aggiunta gestione campi
			if ((lSentenza.getAnnoRegePm() != null && !" ".equals(lSentenza.getAnnoRegePm()))
					|| (lSentenza.getNumeroRegePm() != null && !" ".equals(lSentenza.getNumeroRegePm()))) { %>   
     			<tr>
		      		<td class="l"> Anno/Numero R.G.N.R. </td>
		      		<td class="L" colspan=5>
        				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>&nbsp;
        				/
        				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%></font>&nbsp;
      				</td>
    			</tr>
			<% }
			if ((lSentenza.getAnnoRegeGip() != null && !" ".equals(lSentenza.getAnnoRegeGip()))
					|| (lSentenza.getNumeroRegeGip() != null && !" ".equals(lSentenza.getNumeroRegeGip()))) { %>   
	 			<tr>
		      		<td class="l"> Anno/Numero Reg.Gen. GIP </td>
		      		<td class="L" colspan=5>
	    				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegeGip())%></font>&nbsp;
        				/
        				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegeGip())%></font>&nbsp;
	  				</td>
				</tr>
			<% }
			if (lSentenza.getDataProvvedimento() != null) { %>   
     			<tr>
		      		<td class="l"> Data Emissione Provvedimento </td>
		      		<td class="L" colspan=5>
        				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
      				</td>
    			</tr>
			<% }
			if ((lSentenza.getAnnoSentenza() != null && !lSentenza.getAnnoSentenza().equals(" "))
					|| (lSentenza.getNumeroSentenza() != null && !lSentenza.getNumeroSentenza().equals(" "))) { %>
    			<tr>
			      	<td class="l">Anno/Numero Sentenza</td>
			      	<td class="L" colspan=5>
        				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoSentenza())%></font>&nbsp;
        				/
        				<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      				</td>
    			</tr>
			<% }
			if ((lSentenza.getAnnoProvvedimento() != null && !lSentenza.getAnnoProvvedimento().equals(" "))
					|| (lSentenza.getNumeroProvvedimento() != null && !lSentenza.getNumeroProvvedimento().equals(" "))) { %>
     			<tr>
     				<td class="l">Anno/Numero <%=annoNum %></td>
      				<td class="L" colspan=5>
						<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoProvvedimento())%></font>&nbsp;
						/
						<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroProvvedimento())%></font>&nbsp;
      				</td>
    			</tr>
			<% } %>
   	 		<tr>
      			<td class="l">Autorità Emittente</td>
      			<td class="L" colspan=5>
        			<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
      			</td>
			</tr>
			<tr>
				<td class="l">Luogo Emittente</td>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
				</td>
			</tr>
			<% if (descTipoProvvedimento != null && !descTipoProvvedimento.equals("Cumulo")) { %>
			<tr>
				<td class="l">Sezione Autorità Emittente</td>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente())%></font>&nbsp;
				</td>
			</tr>
			<% }  %> 
		   	<% if (descTipoProvvedimento != null && !descTipoProvvedimento.equals("Cumulo")
		   			&& !descTipoProvvedimento.equals("Ordinanza") && !descTipoProvvedimento.equals("Decreto di Archiviazione")) { %>
				<tr><td class="Titolo" colspan=6>Sentenza di riferimento</td></tr>
			<% } %>
			<tr>
				<td class="l">Note</td>
				<td class="L" colspan=5>
					<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
				</td>
			</tr>
		</table>
		<br>
	</body>
</html>