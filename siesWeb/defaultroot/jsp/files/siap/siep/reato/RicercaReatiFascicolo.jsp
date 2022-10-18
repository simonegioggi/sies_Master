<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>

<jsp:useBean id="notFound" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="reati" 			scope="request" class="java.util.Vector"/>
<jsp:useBean id="stringacampi" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="stringareati" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="Formdipartenza" 	scope="request" class="java.lang.String"/>

<html>
<head>
<title> [S.I.E.S.] - Reati - </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
window.top.ricercareatifascicolo.f.CONFERMA.disabled = false;
function SelezionaTutti(total) {
	if (total == 1) {
		document.listareati.ceccati.checked=true;
	} else {
		for (var i = 0; i < total; i++) {
			document.listareati.ceccati[i].checked=true;
       	}
	}
}

function CopiaReato(IdFascdaCopia, total) {
	// INIZIO	Ticket#202210060112 - Siep - errore copia reati
	var test = false;
	for (var x = 0; x < total; x++) {
		if (document.listareati.ceccati[x].checked) {
			test = false;
			break;
		} else {
			test = true;
		}
   	}
	if (test) {
		alert("Selezionare almeno un Reato!"); 
        return false;
	}
	// FINE		Ticket#202210060112 - Siep - errore copia reati
	IddeiReati = new Array();
	c = 0;
	if (total == 1) {
		IdRea = document.listareati.<%=ICostantiReato.CAMPO_NUM_REATO_DA_COPIA%>.value;
		IddeiReati[c] = IdRea;
	} else {
		for (i = 0; i < total ; i++) {
			if (document.listareati.ceccati[i].checked) {
       			IdRea = document.listareati.<%=ICostantiReato.CAMPO_NUM_REATO_DA_COPIA%>[i].value;
   				IddeiReati[c] = IdRea;
   				c++;
    		}
    	}
	}
	window.parent.opener.document.<%=Formdipartenza%>.<%=ICostantiReato.CAMPO_NUM_REATI_N%>.value =IddeiReati;			
	window.parent.opener.document.<%=Formdipartenza%>.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.reato.action.ActCopiaReato";
	window.parent.opener.document.<%=Formdipartenza%>.<%=ICostantiReato.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_DA_COPIA%>.value =IdFascdaCopia;
	window.parent.opener.document.<%=Formdipartenza%>.submit();
	window.parent.close();
}
</script>
</head>
<body class="corpo">
<form method="POST" name="listareati" action="<%=IWebConstants.PG_MAIN%>">
<input type="hidden" name="<%=ICostantiReato.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_DA_COPIA%>" value="">
<table>
	<tr>
		<td class="LBG">&nbsp;&nbsp;&nbsp;Elenco Reati&nbsp;&nbsp;&nbsp;</td>
	</tr>
</table>
<br>
<%
if (notFound == "") {
	Iterator itx = reati.iterator();
	int cont = 0;
	int total = 0;
	String IdFascdaCopia = new String();
	String str = new String();
	String strReato = new String();
	String lProgressivo = new String();
  	while (itx.hasNext()) {
		ReatoModel lReato = (ReatoModel) itx.next();
		if (lReato.getProgrCircostanza().intValue() == 1) {
			lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale().toString() : lReato.getProgrReato().toString();
			total++;
      		IdFascdaCopia = StringUtils.toStringJSP(lReato.getFasSieIdFascicoloSiep());
%>
<Table width="100%">
	<tr>
		<td class="l" width=90%><font class="cRosso">Reato <%=lProgressivo%>: </font>
<%
			strReato = (String)stringareati.get(cont);
%>
			<%=StringUtils.cStrForJS(strReato)%>
		</td>
<%
			str = (String)stringacampi.get(cont);
			cont++;
%>
	       	        			
		<td class="r" width="5%">&nbsp;
			<input type="checkbox" name="ceccati" value="<%=lProgressivo%>">
			<input type="hidden" name="<%=ICostantiReato.CAMPO_NUM_REATO_DA_COPIA%>" value="<%=lReato.getProgrReato()%>">
		</td>	
	</tr>
</table>
<table width=90%>
<%
			if (lReato.getDescrTipoReato() != null && !lReato.getDescrTipoReato().equals("")
					&& !lReato.getDescrTipoReato().equals("-") && lReato.getDescLuogo() != null && !lReato.getDescLuogo().equals("")) {
%>
	<tr>
		<td class="l" width="25%">Tipo Reato</td>
		<td class="l" width="25%">	
			<font class="campo"><%=lReato.getDescrTipoReato()%></font>&nbsp;
		</td>	
		<td class="l" width="25%">Luogo Reato</td>
		<td class="l" width="25%">	
			<font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>&nbsp;
		</td>
	</tr>
<%
			} 
			if (lReato.getCodPeriodoConsumazione() != null && !lReato.getCodPeriodoConsumazione().equals("")
					&& !lReato.getCodPeriodoConsumazione().equals("-")) {
%>
	<tr>	
		<td class="l" width="25%">Reato  </td>
		<td class="l" width="75%">	
			<font class="campo"><%=lReato.getDescrPeriodoConsumazione()%></font>&nbsp;
		</td>
	</tr>
	<tr>
<%
				if (lReato.getGiornoInizio() != null || lReato.getMeseInizio() != null || lReato.getAnnoInizio() != null) { 
%>
		<td class="l" width="25%">&lt;Data1&gt;</td>
		<td class="l" width="25%">
			<font class="campo">
<%
					String lStrGGInizio = StringUtils.toStringJSP( lReato.getGiornoInizio(), "**");
					if (!lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
  						lStrGGInizio = "0" + lStrGGInizio;
					String lStrMMInizio = StringUtils.toStringJSP( lReato.getMeseInizio(), "**");
					if (!lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
  						lStrMMInizio = "0" + lStrMMInizio;
					String lStrAAInizio = StringUtils.toStringJSP( lReato.getAnnoInizio(), "**");
%>
				<%=lStrGGInizio%>
				-
				<%=lStrMMInizio%>
				-
				<%=lStrAAInizio%>
			</font>
		</td>
<%
				}
				if (lReato.getGiornoFine() != null || lReato.getMeseFine() != null || lReato.getAnnoFine() != null) {
%>
		<td class="l" width="25%">&lt;Data2&gt;</td>
		<td class="l" width="25%">
<%
					String lStrGGFine = StringUtils.toStringJSP( lReato.getGiornoFine(), "**");
					if (!lStrGGFine.equals("**") && lStrGGFine.length() == 1)
						lStrGGFine = "0" + lStrGGFine;
					String lStrMMFine = StringUtils.toStringJSP( lReato.getMeseFine(), "**");
					if (!lStrMMFine.equals("**") && lStrMMFine.length() == 1)
  						lStrMMFine = "0" + lStrMMFine;
					String lStrAAFine = StringUtils.toStringJSP( lReato.getAnnoFine(), "**");
%>
			<font class="campo">
  				<%=lStrGGFine%>
				-
				<%=lStrMMFine%>
				-
				<%=lStrAAFine%>
  			</font>
		</td>
<%
				}
%>
	</tr>
<%
			}
			if (lReato.getNote() != null && !lReato.getNote().equals("")) {
%>
	<tr>
	  	<td class="l" width="25"><font class="label">Note</font></td>
	  	<td class="l" width="75"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font></td>
	</tr> 
<%
			}
%>
</table>
<%
		} // chiude if circostanza =1
	} // Chiude while
%>
<br>
<table>
	<tr>
		<td class="c">
			<input  type="button" name="COPIA" onclick = "javascript:CopiaReato('<%=IdFascdaCopia%>','<%=total%>');" value="   COPIA   ">
		</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>		
		<td class="LBG">&nbsp;&nbsp;&nbsp; SELEZIONA TUTTI I REATI &nbsp;&nbsp;&nbsp;</td> 
		<td class="r" width="5%">&nbsp;
   			<input  type="checkbox" name="ceccatutti" value="S" onclick="javascript:SelezionaTutti(<%=total%>);">
	    </td>	      				
	</tr>
</table>
<% 			
} else if (notFound.equals("F")) {
%>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p><CENTER><STRONG><font color="red">FASCICOLO NON TROVATO</font></STRONG></CENTER></p>
<%
} else if (notFound.equals("R")) {
%>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p><CENTER><STRONG><font color="red">NESSUN REATO TROVATO</font></STRONG></CENTER></p>
<%
}
%>
</form>
</body>
</html>