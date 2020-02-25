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
<!-- segnalazioni 4: modificato il titolo -->
<title>[S.I.E.S.] - Inserimento Cumulo, Ordinanza o Decreto di Archiviazione </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname) {
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
  	// 21/02/2011 Lista Uffici per TIPO_UFFICIO
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

<script language="JavaScript">
	function Init() {
		// segnalazioni 4: modificato il nome del radio button
		if (document.f.<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>[0].checked) {
			VisualizzaCumulo();
		}
		else if (document.f.<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>[1].checked) {
			VisualizzaDecretoArchiviazione();
		}
		else {
			VisualizzaOrdinanza();
		}
	}

	function VisualizzaCumulo() {
	 	ChiudiDecretoArchiviazione();
	 	ChiudiOrdinanza();
	   	node=document.getElementById("DecretoArchiviazioneDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	   	node=document.getElementById("OrdinanzaDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	    node=document.getElementById("CumuloDiv");
	    node.style.visibility='visible';
	    node.disabled = false;   
	}

	function VisualizzaOrdinanza() {
	 	ChiudiCumulo();
	 	ChiudiDecretoArchiviazione();
	   	node=document.getElementById("CumuloDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	   	node=document.getElementById("DecretoArchiviazioneDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	    node=document.getElementById("OrdinanzaDiv");
	    node.style.visibility='visible';
	    node.disabled = false;   
	}

	function VisualizzaDecretoArchiviazione() {
	 	ChiudiCumulo();
	 	ChiudiOrdinanza();
	   	node=document.getElementById("CumuloDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	   	node=document.getElementById("OrdinanzaDiv");
	    node.style.visibility='hidden';
	    node.disabled = true;
	    node=document.getElementById("DecretoArchiviazioneDiv");
	    node.style.visibility='visible';
	    node.disabled = false;   
	}
	  
	function ChiudiCumulo() {
		DisabilitaDiv("CumuloDiv");
	}

	function ChiudiOrdinanza() {
		DisabilitaDiv("OrdinanzaDiv");
	}
	  
	function ChiudiDecretoArchiviazione() {
		DisabilitaDiv("DecretoArchiviazioneDiv");
	}
</script>
</head>

<body class="corpo" onLoad="Init();">

<%-- inizio aggiunta --%> 
<form name="f">
  	<table>
    	<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      		<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Altri Titoli</font></td>
    	</tr>
  	</table>
  	<br>
  	<table width="85%">
   	 	<tr>
      		<td class="Titolo" width="35%" > Selezionare il tipo di Provvedimento&nbsp; </td>
      		<td class="Titolo" >
        		Cumulo <input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>" value="13" onClick="VisualizzaCumulo();" checked>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				Ordinanza <input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>" value="03" onClick="VisualizzaOrdinanza();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				Decreto di Archiviazione<input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>" value="63" onClick="VisualizzaDecretoArchiviazione();">
      		</td>
    	</tr>
  	</table>
</form>
<br>
<div id="comune" style="position: relative; top: 0; left: 0; visibility:visible;">     
  	<div id="CumuloDiv" style="position:relative; top: 0; left: 0; visibility:visible;">  
		<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_CUMULO%>"/>
  	</div>
  	<div id="DecretoArchiviazioneDiv" style="position: absolute; top: 0; left: 0; visibility:hidden;">      
    	<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_DECRETO_ARCHIVIAZIONE%>"/>
  	</div>
  	<div id="OrdinanzaDiv" style="position: absolute; top: 0; left: 0; visibility:hidden;">      
    	<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_ORDINANZA%>"/>
  	</div>
</div>
<%-- fine aggiunta --%> 

</body>
</html>