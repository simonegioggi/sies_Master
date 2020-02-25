<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di caricamento per inserimento archiviazione per provvedimento di cumulo --%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="posizioneluogoaltra"   	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="flagergastolo" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua" 				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="magistratocompetente" 		scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="datiCumulo" 		scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>
<jsp:useBean id="codTipoUfficioCumulo" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="descrTipoUfficioCumulo"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="codComuneCumulo"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComuneCumulo"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="uffrecrediti"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="codUfficioCumulo"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="codUfficioUtente"    		scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo"     			scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<%
//===================================================================================================
// Form per inserimento di Archiviazione per Provvedimento di cumulo
//====================================================================================================
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
if (lPosizione == null)
  	lPosizione = new PosizioneGiuridicaModel();
if (lLuogoDetenzione == null)
  	lLuogoDetenzione = new LuogoDetenzioneModel();
if (lAltraCausa == null)
  lAltraCausa = new AltraCausaModel();
%>

<!-- LoadInserisciArchiviazionePerProvvCumulo -->
<html>
  	<head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Inserimento Archiviazione per Provvedimento di Cumulo</title>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
    var desktop;
	function Verify() {
		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
     	// DATA EMISSIONE
		if (document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		if (document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
		var data_to_verify = document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
				+ '/'+document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
				+ '/'+document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
		if (!ControllaDataPassaVuota(data_to_verify)) {
	          alert('Data Emissione non valida');
	          document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	          return false;
		}
	 	// 1) Controllo: data di sistema deve essere >= Data Emissione
		if (!CompareDate(data_to_verify, data_sistema)) {
			alert('Data Emissione non può essere superiore alla data odierna!');
		    document.ArchiviazionePC.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		    return false;
	    }
		// DATA TRASMISSIONE
		if (document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		if (document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
		var data_to_verify = document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
				+ '/'+document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
				+ '/'+document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
		if (!ControllaDataPassaVuota(data_to_verify)) {
			alert('Data Trasmissione non valida');
	        document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
	        return false;
        }
	 	// 2) Controllo: data di sistema deve essere >= Data Trasmissione
		if (!CompareDate(data_to_verify, data_sistema)) {
			alert('Data Trasmissione non può essere superiore alla data odierna!');
			document.ArchiviazionePC.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>.focus();
	      	return false;
	    }
		// DATA PROVVEDIMENTO CUMULO
        if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value.length == 1)
          	document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value = '0'
          		+ document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value;
        if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value.length == 1)
          	document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value = '0'
          		+ document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value;
        var data_to_verify = document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value
        		+ '/' + document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value
        		+ '/' + document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>.value;
        if (!ControllaData(data_to_verify)) {
          	alert('Data Provvedimento di Cumulo non valida');
          	document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.focus();
          	return false;
		}
		// DATA DEFINIZIONE
		if (document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
		if (document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length == 1)
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value = '0'
				+ document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;
		var data_to_verify = document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value
				+ '/' + document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value
				+ '/' + document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;
		if (!ControllaData(data_to_verify)) {
			alert('Data Definizione non valida');
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
			return false;
		}
		// Ufficio e Sede Emissario Cumulo
		if (!document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled) {
	        if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.value == '-') {
	        	alert("Il Campo Ufficio che ha emesso il cumulo è obbligatorio");
	          	return false;
	        }
		}
		if (!document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled) {
	        if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE %>.value == "") {
	        	alert("Il Campo Sede Ufficio che ha emesso il cumulo è obbligatorio");
	          	return false;
	        }
		}
		// Anno e Numero Procedimento SIEP
		if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>.value == "") {
          	alert("Anno Procedimento SIEP è obbligatorio");
          	return false;
        }
        if (document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>.value == "") {
          	alert("Numero Procedimento SIEP è obbligatorio");
          	return false;
        }
		// Magistrato
		if (document.ArchiviazionePC.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value == "") {
			alert("Il Cognome del Magistrato è obbligatorio");
			document.ArchiviazionePC.<%=ICostantiMagistrato.CAMPO_COGNOME%>.focus();
			return false;
		}
		if (document.ArchiviazionePC.<%=ICostantiMagistrato.CAMPO_NOME%>.value == "") {
			alert("Il Nome del Magistrato è obbligatorio");
			document.ArchiviazionePC.<%=ICostantiMagistrato.CAMPO_NOME%>.focus();
			return false;
		}
		return true;
	}
	// End Verify

	function ListaComuniUff(a_formname,a_fieldname,codTipoUfficio) {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

	function ListaComuni(a_formname,a_fieldname) {
		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}


	function ListaMagistrati(a_formname) {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
	}

	function ListaUDS(a_formname,a_fieldname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

   	function ListaComuniTds(formname,fieldname) {
      	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

   	function radio() {
		var nodedestinatari = document.getElementById('divdestinatari');
        var nodeufficio = document.getElementById('divufficio');
        if(document.ArchiviazionePC.codUfficioCumulo.value == document.ArchiviazionePC.codUfficioUtente.value){
        	nodeufficio.style.display = 'none';
			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled = true;
			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled = true;
			nodedestinatari.style.display = 'none';
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].disabled=true;
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked=true;
			
        }
        else{
        	nodeufficio.style.display = 'block';
			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled = false;
			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled = false;
			nodedestinatari.style.display = 'block';
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].disabled=true;
			document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked=true;
        }
<%-- 		if (document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked) { --%>
// 			nodeufficio.style.display = 'none';
<%-- 			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled = true; --%>
<%-- 			document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled = true; --%>
// 			nodedestinatari.style.display = 'none';
//          } else {
<%--          	if (document.ArchiviazionePC.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked) { --%>
// 				nodeufficio.style.display = 'block';
<%-- 				document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled = false; --%>
<%-- 				document.ArchiviazionePC.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled = false; --%>
// 				nodedestinatari.style.display = 'block';
//          	}
// 		}
    }
   	

 	</script>
  	</head>
  	<!--  body class="corpo" onLoad="valorizzaAutorita();" -->
  	<body class="corpo" onload="radio();">
    <table>
      	<tr>
      		<td class="LBG">
      			<a href="Javascript:window.print();">
      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      			</a>
      		</td>
        	<td class=lbg>
           		<font  class="label">Funzione:&nbsp;</font>
         		<font class="campo">Archiviazione per Assorbimento in Cumulo - Misure di Sicurezza</font>
        	</td>
      	</tr>
	</table>
    <br>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="ArchiviazionePC" action="<%=IWebConstants.PG_MAIN%>">
    <input type="hidden" name="codUfficioUtente" value="<%=codUfficioUtente%>">     	    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciArchiviazionePerProvvCumulo">
    <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
    <table cellspacing=0 cellpadding=0 width="95%">
      	<tr>
        	<td class="l" width="20%">Posizione Giuridica </td>
        	<td class="L" colspan="8">
          		<font class="campo">
            		<%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          		</font>
        	</td>
      	</tr>
<%
if ("N".equals(flagergastolo)) {
	if (penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0) {
		
	} else {
%>
		<tr>
			<td class="l">Reclusione</td>
            <td class="l" colspan=2>
              	<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
              	<font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
              	<font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
		</tr>
<%
	}
	if (penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
			&& penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0) {
		
	} else {
%>
		<tr>
          	<td class="l" >Arresto</td>
          	<td class="l" colspan=2>
             	<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
             	<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
             	<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
          	</td>
          	<td class="l">Ammenda</td>
          	<td class="l" colspan="2">
          		<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
          		<font class="l">Euro</font>
          	</td>
		</tr>
<%
	}
}
if (penaresidua != null) {
%>
      	<tr>
<%
	if (penaresidua.getDataInizio() != null) {
%>
			<td class="l">Pena Espiata dal</td>
         	<td class="L">
         		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "dd-MM-yyyy"))%></font>
         	</td>
<%
	}
	if (penaresidua.getFlagErgastolo() != null) {
		if ("S".equals(penaresidua.getFlagErgastolo())) {
%>
			<td class="l">Pena Detentiva</td>
			<td class="L"><font class="campo">ERGASTOLO</font></td>
<%
		} else if ("D".equals(penaresidua.getFlagErgastolo())) {
%>
			<td class="l">Pena Detentiva</td>
			<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
<%
		}
	}
	if (!lPosizione.isLibero()
			|| (fascicolo.getFlagAltraCausa() != null
				&& "S".equals(fascicolo.getFlagAltraCausa()))) {
		if (penaresidua.getFlagErgastolo() == null
				|| (penaresidua.getFlagErgastolo() != null
					&& !"S".equals(penaresidua.getFlagErgastolo())
					&& !"D".equals(penaresidua.getFlagErgastolo()))) {
			if (penaresidua.getDataFine() != null) {
				if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
			<td class="l">al</td>
			<td class="L" colspan="2">
				<font class="campo">
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>
				</font>
			</td>
<%
				} else {
%>
			<td class="l">al</td>
			<td class="lRosso" colspan=2>
				<font class="lRosso">
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%>
				</font>
			</td>
<%
				}
			}
      	}
	}
%>
   		</tr>
<%
} // chiude if (penaresidua != null)
%>
		<tr>
        	<td class="l">Data Emissione</td>
        	<td class="L">
          		<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          		<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          		<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        	</td>
        	<td class="l">Data Trasmissione</td>
        	<td class="L">
          		<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          		<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          		<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        	</td>
    	</tr>
  	</table>
 	<br>
	<!--  Definizione Procedimento -->
  	<table cellspacing=0 cellpadding=0 width="95%">
    	<tr>
      		<td colspan="2" class="titolo">Dati Definizione Procedimento</td>
    	</tr>
    	<tr>
     		<td class="l" colspan="2">
         		Assorbimento Cumulo Stesso Ufficio&nbsp;<input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="1153" onclick="radio();" >
         		&nbsp;Assorbimento Cumulo Altro Ufficio &nbsp;<input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="1154" onclick="radio();"  >
      		</td>
    	</tr>
    	<tr>
      		<td class="l" width="25%">Data Provvedimento di Cumulo <font class=ob>(*)</font></td>
      		<td class="l">
        		<input type="text" Title="Giorno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiCumulo.getDataProvvedimento(), "dd"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" readonly="readonly">
        		/
        		<input type="text" Title="Mese cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiCumulo.getDataProvvedimento(), "MM"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" readonly="readonly">
        		/
        		<input type="text" Title="Anno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiCumulo.getDataProvvedimento(), "yyyy"))%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" readonly="readonly">
      		</td> 
    	</tr>
	</table>
	<div id="divufficio" style="width: 100%; display: none; position: relative;">
	<table cellspacing=0 cellpadding=0 width="95%">
		<tr>
     		<td class="l" width="25%">Ufficio che ha Emesso il Cumulo <font class=ob>(*)</font></td>
      		<td class="L">
      			<input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>" value="<%=codTipoUfficioCumulo%>">
      			<input title="Tipo Ufficio" name="descrTipoUfficioCumulo" value="<%=descrTipoUfficioCumulo%>" type="text" maxlength="35" size="70" readonly="readonly">
      		</td>
    	</tr>
    	<tr>
      		<td class="l">Sede <font class=ob>(*)</font></td>
     		<td class="L">
     			<input type="hidden" name="codComuneCumulo" value="<%=codComuneCumulo%>">
     			<input type="hidden" name="codUfficioCumulo" value="<%=codUfficioCumulo%>">     			
          		<input title="Sede Ufficio" name="<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>" value="<%=descrComuneCumulo%>" type="text" maxlength="35" size="35" readonly="readonly">
      		</td>
    	</tr>
	</table>
	</div>
	<table cellspacing=0 cellpadding=0 width="95%">
		 <tr>
      		<td class="l" width="25%">Numero Procedimento SIEP <font class=ob>(*)</font></td>
      		<td class="l">
         		<input Title="Anno"   value="<%=StringUtils.toStringJSP(datiCumulo.getChiaveAnno())%>"  name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>" type="text" size="4" maxlength="4" > /
         		<input Title="Numero" value="<%=StringUtils.toStringJSP(datiCumulo.getChiaveProgr())%>" name="<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>"  type="text" size="6" maxlength="6" >
      		</td>
    	</tr>
    	<tr>
      		<td class="l">Data Definizione <font class=ob>(*)</font></td>
      		<td class="l">
        		<input type="text" Title="Giorno definizione" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        		/
        		<input type="text" Title="Mese definizione" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        		/
        		<input type="text" Title="Anno definizione" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      		</td>
    	</tr>
    	<tr>
     		<td class="L">Motivazione</td>
      		<td class="L">
        		<TEXTAREA title="Motivazione" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>" cols="70" rows="2"></textarea>
      		</td>
     	</tr>
  	</table>
  	<br>
	<!-- Magistrato Firmatario -->
  	<table cellspacing=0 cellpadding=0 width="95%">
	    <tr>
	      	<td colspan="2" class="titolo">Magistrato Firmatario</td>
	    </tr>
    	<tr>
      		<td class="l" width="25%">Magistrato Firmatario</td>
      		<td class="L">
        		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25">
        		<input readonly title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome())%>" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25">
         		<a href="Javascript:ListaMagistrati('ArchiviazionePC');">
           			<img src="/images/filefolder.gif" border="0">
         		</a>
         		<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato())%>" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" maxlength="35" size="35">
       		</td>
    	</tr>
	</table>
	<div id="divdestinatari" style="width: 100%; display: none; position: relative;">
	<table cellspacing=0 cellpadding=0 width="95%">
  		<tr>
    		<td colspan="4" class="titolo">Destinatari</td>
  		</tr>
  		<tr>
      		<td class="l" width="25%">Ufficio recupero crediti presso</td>
      		<td class="L">
        		<select  Title="Ufficio recupero crediti" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          			<%=uffrecrediti%>
         		</select>
      		</td>
     		<td class="l">di</td>
     		<td class="L">
          		<input title="Sede Ufficio recupero crediti"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="25">
       			<a href="Javascript:ListaComuniUff('ArchiviazionePC','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>',document.ArchiviazionePC.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>[document.ArchiviazionePC.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.selectedIndex].value);">
          			<img src="/images/filefolder.gif" border=0>
       			</a>
      		</td>
  		</tr>
  		<tr>
      		<td class="l">Autorità di polizia</td>
      		<td class="L" colspan="3">
        		<select  Title="Autorita" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
         			<%=codiceAutorita%>
         		</select>
      		</td>
    	</tr>
    	<tr>
     		<td class="l">Sede</td>
     		<td class="L">
          		<input title="Sede Autorita"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
          		<a href="Javascript:ListaComuni('ArchiviazionePC','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          			<img src="/images/filefolder.gif" border=0>
        		</a>
      		</td>
      		<td class="l">Indirizzo</td>
      		<td class="L">
         		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="30"></textarea>
      		</td>
    	</tr>
    	<tr>
      		<td class="l">Magistrato di Sorveglianza</td>
      		<td class="L" colspan="3">
       			<input title="ufficio" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="25">
       			<a href="Javascript:ListaUDS('ArchiviazionePC','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
       				<img src="/images/filefolder.gif" border="0">
       			</a>
      		</td>
   		</tr>
    	<tr>
       		<td class="L">Tribunale di Sorveglianza</td>
       		<td class="L" colspan="3">
         		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
         		<a href="Javascript:ListaComuniTds('ArchiviazionePC','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
         			<img src="/images/filefolder.gif" border="0">
         		</a>
			</td>
		</tr>
    	<tr>
      		<td class="l">Altra Autorità</td>
      		<td class="L" colspan="3">
        		<select  Title="Autorita" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
         			<%=codiceAutorita%>
         		</select>
      		</td>
    	</tr>
    	<tr>
     		<td class="l">Sede</td>
     		<td class="L">
          		<input title="Sede Autorita"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
          		<a href="Javascript:ListaComuni('ArchiviazionePC','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
          			<img src="/images/filefolder.gif" border=0>
        		</a>
      		</td>
      		<td class="l">Indirizzo</td>
      		<td class="L">
          		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>" cols="30"></textarea>
       		</td>
    	</tr>
    	<tr>
      		<td class="l">Altra Autorità </td>
       		<td class="l" colspan="3">
       			<font class="campo">
        			<input Title="Altra Autorità" name="<%=ICostantiNotifica.CAMPO_NOTE%>" size="70" type="text">
        		</font>
       		</td>
    	</tr>
	</table>
	</div>
	<table width="100%">
    <tr><td>&nbsp;</td></tr>
    	<tr>
      		<td class="lNoBord" colspan="2">
        		<INPUT class="bottone" type="submit" name="I" value="Conferma">
      		</td>
    	</tr>
  	</table>
	</form>

  	<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("ArchiviazionePC");
	// data definizione
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno Data Definizione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese Data Definizione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno Data Definizione è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","maxlen=4","La lunghezza massima per l''Anno Data Definizione è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","minlen=4","La lunghezza minima per l''Anno Data Definizione è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");
 	// data provvedimento cumulo
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>","req","Il campo Giorno Data Provvedimento di Cumulo è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>","req","Il campo Mese Data Provvedimento di Cumulo è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","req","Il campo Anno Data Provvedimento di Cumulo è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","maxlen=4","La lunghezza massima per l''Anno Data Provvedimento di Cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","minlen=4","La lunghezza minima per l''Anno Data Provvedimento di Cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","numeric");
    frmvalidator.setAddnlValidationFunction("Verify");
  	</script>

	</body>
</html>