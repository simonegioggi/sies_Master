<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String" />
<jsp:useBean id="flagSN" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />

<head>
	<title>[S.I.E.S.] - Inserimento Sentenza, Decreto Penale, Sentenza Straniera Delibata </title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript">
		var desktop;
		function ListaComuni(a_formname,a_fieldname) {
			desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
		}
		// segnalazioni 4: aggiunte funzioni
		function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
	   		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	 	}
	  	function ListaOrdinanzeNelDistretto(a_formname, a_fieldname, a_fieldname1, a_fieldname2, a_fieldname3, a_fieldname4, a_fieldname5, codTipoUfficio) {
	 		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.sentenza.action.ActLoadListaOrdinanzeNelDistretto&formname="+a_formname+ "&fieldname="+a_fieldname+" &fieldname1="+a_fieldname1+" &fieldname2="+a_fieldname2+" &fieldname3="+a_fieldname3+" &fieldname4="+a_fieldname4+" &fieldname5="+a_fieldname5+"  &<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=300");
		}
	</script>
	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=ICostantiFasSigeSentenza.JS_SENTENZA%>"></script>
	
	<%
	String lTitolo = "";
	String lIncludeFile= "";
	 
	if (sentenza.getCodTipoProvvedimento().equals("02")) { // DECRETO
		lTitolo = "Modifica Estremi Decreto Penale";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_DECRETO;
	} else if (sentenza.getCodTipoProvvedimento().equals("05")) { // SENTENZA STRANIERA
		lTitolo = "Modifica Sentenza Straniera";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_SENTENZA_STRANIERA;
	}
	// segnalazioni 4: aggiunte tre casistiche
	else if ("13".equals(sentenza.getCodTipoProvvedimento())) { // CUMULO
		lTitolo = "Modifica Cumulo";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_CUMULO;
	} else if ("03".equals(sentenza.getCodTipoProvvedimento())) { // ORDINANZA
		lTitolo = "Modifica Ordinanza";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_ORDINANZA;
	} else if ("63".equals(sentenza.getCodTipoProvvedimento())) { // DECRETO ARCHIVIAZIONE
		lTitolo = "Modifica Decreto di Archiviazione";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_DECRETO_ARCHIVIAZIONE;
	} else { // SENTENZA
		lTitolo = "Modifica Sentenza";
		lIncludeFile = ICostantiFasSigeSentenza.DIV_SENTENZA;
	}
	%>
</head>

<body class="corpo" >
	<form name="f">
  		<table>
    		<tr>
    			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      			<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
      			</td>
    		</tr>
  		</table>
	</form>
	<jsp:include page="<%=lIncludeFile%>"/>
</body>
</html>