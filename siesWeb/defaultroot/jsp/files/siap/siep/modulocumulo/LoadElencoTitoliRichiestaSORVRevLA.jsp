<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli" 		scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"			scope="request" class="java.lang.String"/>

<!-- 		LoadElencoTitoliRichiestaSORVRevLA		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per l'inserimento delle
//	Richieste del PM alla Sorveglianza di Revoca L.A.
//==================================================================================
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli per Richieste del PM alla SORV - Revoca Liberazione Anticipata </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function eseguiFunzione(action) {
	document.elencoPerRicRevLA.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.elencoPerRicRevLA.submit();
}
      
function controlla() {
	if (document.elencoPerRicRevLA.numeroTitoli.value == 0) {
		alert("Non risultano provvedimenti di concessione di LA sui Titoli in Istruttoria");
	}
}
   
function Verify() {  
	// Controllo che sia selezionato almeno un Titolo
	var Spunta="NO";
	// Se è presente una sola richiesta
	if (typeof (document.elencoPerRicRevLA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>.length) == "undefined") {
		if (document.elencoPerRicRevLA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>.checked) {
  			Spunta="SI";
   		}						
	} else {
		// Se sono presenti più richieste.
		for (var j = 0; j < document.elencoPerRicRevLA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>.length; j++) {
			if (document.elencoPerRicRevLA.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>[j].checked) {
   				Spunta="SI";
    		}
    	}
	}
  	if (Spunta == "NO") {
		alert("Attenzione selezionare almeno un Titolo e dare conferma!");
		// document.elencoPerRicRevLA.< %=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>[0].focus();
		return false;
  	}	
	return true; 
}
</script>
</head>
  
<body class="corpo" onload="controlla();">
<table>
	<tr>
		<td class="LBG">
          	<a href="Javascript:window.print();">
              	<img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          	</a>
        </td>
        <td class="LBG">
          	<font class="label">Funzione :</font>
          	<%-- font class="campo">ELENCO TITOLI per Richiesta Revoca Liberazione anticipata&nbsp;</font--%>
			<font class="campo">Richieste alla Sorveglianza - Revoca L.A.&nbsp;</font>
        </td>
        <td class="LBG">
        	<a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        	</a>
      	</td>
	</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
<table cellspacing="2" cellpadding="2" width="100%">
  	<tr><td>&nbsp;</td></tr>
  	<tr>
  		<td class="l">
  			<center>
  				<font class="label" style="color:red; font-size: 10pt">Selezionare i Titoli oggetto della Richiesta dal successivo Elenco</font>
  			</center>
  		</td>
  	</tr>
</table>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="elencoPerRicRevLA" >
<%-- input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaGEBenefici" --%>
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaSORVRevocaLA">
<input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
<input type="hidden" name="modalita" value="<%=modalita%>">
<input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
<table cellspacing="2" cellpadding="2" width="100%">
  	<tr>
    	<td class="titolo" colspan=3 width=100%>Elenco Titoli con Ordinanze di concessione Liberazione Anticipata</td>
	</tr>
</table>
<table>
	<tr>
		<td class="int" width="8%"  >Titolo</td>
		<td class="int" width="10%" >Data Titolo </td>
		<td class="int" width="8%"  >Numero sentenza</td>
		<td class="int" width="18%" >Autorità Titolo Esecutivo</td>
		<td class="int" width="10%" >Data Irrevocabilita</td>
		<td class="int" width="8%"  >Numero SIEP</td>
		<td class="int" width="25%" >Ufficio Esecuzione</td>
		<td class="int" width="5%"  >Selezione</td>
    </tr>
<%
if (ListaTitoli == null || ListaTitoli.size() == 0 ) {
%>  
	<tr>
		<td colspan="10">Nessun dato presente</td>
	</tr>
<%
} else {
	String NumAutoritaSiep = "";
  	Iterator itx = ListaTitoli.iterator();
	while (itx.hasNext()) {
    	TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
    	NumAutoritaSiep = "";
%>
	<tr>
      	<td class="L">
        	<%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      	</td>
		<td class="C" nowrap>
		  	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
		</td>
		<td class="C" nowrap>
		  	<%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
		</td>
		<td class="C" >
		  	<%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
		</td>
		<td class="C" nowrap>
		  	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
		</td>
		<td class="C" nowrap>
<%
		if (lTitoCum.getProcedimentoCumulato() != null) {   
     		if ("S".equals(lTitoCum.getProcedimentoCumulato().getFlagAccorpato())) {
	       		UfficioModel lUfficioOrigine = lTitoCum.getProcedimentoCumulato().getUfficioOrigine();
	       		NumAutoritaSiep = lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato() +"/"+ lTitoCum.getProcedimentoCumulato().getChiaveProgrOrigine();
	       		NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>";
%>
			<%=NumAutoritaSiep%>
<%
			} else {
%>
   			<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato())%>
<%
			}
    	} else {
%>
        	&nbsp;
<%
		}
%>
		</td>
      	<td class="C" >
<%
		if (lTitoCum.getProcedimentoCumulato() != null) {
%>
        	<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato())%>
<%
		} else {
%>
        	&nbsp;
<%
		}
%>
		</td>
		<td class="C">
			<input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato(), "")%>">
		</td>
    </tr>
<%
	}
%>
</table>

<table>
	<tr>
    	<td class="lNoBord" colspan="2">
     		<INPUT class="bottone" type="submit" name="bottConferma" value="Conferma">&nbsp;&nbsp;
    	</td>
	</tr>
</table>
<%
} // Chiude il listaTitoli == null
%>
</form>

<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("elencoPerRicRevLA");

//================================================================
// Aggiungere le opportune chiamate al genvalidator 
//================================================================
frmvalidator.setAddnlValidationFunction("Verify"); 
</script>

</body>
</html>