<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String" />

<%
boolean boolMinorenni = ("true".equals(filtroMinorenni));
%>

<script language="JavaScript">
function ListaComuniEmitMinor(a_formname, a_fieldname) {
	var a_typename = document.getElementById('<%=MinorMask.ComboEmittenteId%>').value;
	//alert('EmittentePopUp: ' + a_typename);
	if ( a_typename == "UDS" || a_typename == "UDSM" || a_typename == "TDS" || a_typename == "TDSM")  {
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    } else {
         alert("Selezionare il tipo di ufficio emittente");
    }
}

// [EMITTENTE] funzione richiamata nel caso sia presente la combo sull'ufficio emittente
function ListaComuniEmitUTMinor(a_formname, a_fieldname) {
<%if (boolMinorenni) {%>
	ListaComuniEmitMinor(a_formname, a_fieldname);
<%} else {%>
	var valore = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value;
	if ( valore == "UDS")  {
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	} else if (valore == "TDS") {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    } else {
         alert("Selezionare il tipo di ufficio emittente");
    }
<%}%>
}

// [EMITTENTE] funzione richiamata nel caso sia presente solo ufficio emittente
function ListaComuniEmitUdsMinor(a_formname, a_fieldname) {
<%if (boolMinorenni) {%>
	ListaComuniEmitMinor(a_formname, a_fieldname);
<%} else {%>
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
<%}%>
}

// [EMITTENTE] funzione richiamata nel caso sia presente solo tribunale emittente
function ListaComuniEmitTdsMinor(a_formname, a_fieldname) {
<%if (boolMinorenni) {%>
	ListaComuniEmitMinor(a_formname, a_fieldname);
<%} else {%>
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
<%}%>
}


//[CSSA] funzione per il campo UEPE / USSM
function ListaCSSAMinor(a_formname, a_fieldname, a_field2) {
<%if (boolMinorenni) {%>
	var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
	//alert('UfficioPopUp: ' + a_typename);
	<%-- MEV10-s3: aggiunto controllo preventivo --%>
	if (a_typename == "-")  {
         alert("Selezionare il Destinatario dell'UEPE/USSM");
    } else {
   		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
<%} else {%>
   	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
<%}%>
}

//[CSSA] funzione per il campo UEPE / USSM
function ListaCSSAIdMinor(a_formname, a_fieldname, a_field2, a_idcombo) {
<%if (boolMinorenni) {%>
	var a_typename = document.getElementById(a_idcombo).value;
	//alert('UfficioPopUp: ' + a_typename);
	<%-- MEV10-s3: aggiunto controllo preventivo --%>
	if (a_typename == "-")  {
         alert("Selezionare il Destinatario dell'UEPE/USSM");
    } else {
   		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
<%} else {%>
   	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
<%}%>
}

// [MAGISTRATO] funzione per il campo Ufficio / Magistrato di Sorveglianza
function ListaComuniMagiSorvMinor(a_formname, a_fieldname) {
<%if (boolMinorenni) {%>
	var a_typename = document.getElementById('<%=MinorMask.ComboMagistratoId%>').value;
	//alert('MagistratoPopUp: ' + a_typename);
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
<%} else {%>
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
<%}%>
}

// [TRIBUNALE] funzione per il campo Tribunale di Sorveglianza
function ListaComuniTribSorvMinor(a_formname, a_fieldname) {
<%if (boolMinorenni) {%>
	var a_typename = document.getElementById('<%=MinorMask.ComboTribunaleId%>').value;
	//alert('TribunalePopUp: ' + a_typename);
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
<%} else {%>
	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname=" + a_formname + "&fieldname=" + a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_Comune_Tds", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
<%}%>
}
</script>