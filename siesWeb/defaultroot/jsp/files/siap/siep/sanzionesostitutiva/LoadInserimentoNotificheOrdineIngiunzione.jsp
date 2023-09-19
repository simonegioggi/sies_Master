<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.pagoPA.model.CivilmenteObbligatoModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="ordineIngiunzione"   scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="comboAutNotifica"    scope="request" class="java.lang.String"/>
<jsp:useBean id="notificaAlCondannato" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="listaNotAvvSiep"      scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lListaNotObbligati"   scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="notifichePending"   scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

String listaIdNotAvv = "";
for (int i = 0; i < listaNotAvvSiep.size(); i++) 
	listaIdNotAvv += "," + ((NotificaModel) listaNotAvvSiep.get(i)).getIdNotifica();
if (listaNotAvvSiep.size() > 0)
	listaIdNotAvv = "[" + listaIdNotAvv.substring(1) + "]";
else
	listaIdNotAvv = "[]";

String listaIdNotObbl = "";
for (int i = 0; i < lListaNotObbligati.size(); i++) 
	listaIdNotObbl += "," + ((NotificaModel) lListaNotObbligati.get(i)).getIdNotifica();
if (lListaNotObbligati.size() > 0)
	listaIdNotObbl = "[" + listaIdNotObbl.substring(1) + "]";
else
    listaIdNotObbl = "[]";

Date dataEmissioneOI = ordineIngiunzione.getEvento().getDataEmissione();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname) {
	var desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function abilitaCancella(idNot) {

	if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_CANCELLA%>_" + idNot) != null) {
  		if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_CANCELLA%>_" + idNot).checked) {
  			document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot).checked = false;
  			abilitaNotifica (idNot);
		} else {
	  		document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot).checked = true;
	      	abilitaNotifica (idNot);
    	}
  	}
}

function abilitaNotifica(idNot) {
	if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot) != null) {
  		if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot).checked) { 
  			if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_CANCELLA%>_" + idNot) != null)
  				document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_CANCELLA%>_" + idNot).checked = false;
			document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_" + idNot).disabled = false;
			document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot).disabled = false;
			document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_" + idNot).disabled = false;
			document.getElementById("<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot).disabled = false;
			document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_" + idNot).disabled = false;
			document.getElementById("folderIcon_" + idNot).style.display = "inline-block";
			document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_" + idNot).disabled = false;
		} else {
  			document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_" + idNot).disabled = true;
			<%-- document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot).disabled = true; --%>
			<%-- document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_" + idNot).disabled = true; --%>
			<%-- document.getElementById("<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot).disabled = true; --%>
			document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_" + idNot).disabled = true;
			document.getElementById("folderIcon_" + idNot).style.display = "none";
			document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_" + idNot).disabled = true;
    	}
  	}
}

function Verify() {
	// Aggiungere i controlli
  	var idCondannato        = <%=notificaAlCondannato.getIdNotifica()%>;
	var arrayIdNotificaAvv  = <%=listaIdNotAvv%>;
	var arrayIdNotificaObbl = <%=listaIdNotObbl%>;
  	if (!checkNotifica(idCondannato))
		return false;
  	for (var i = 0; i < arrayIdNotificaAvv.length; i++) {
    	id = arrayIdNotificaAvv[i];
    	if (!checkNotifica(id))
      		return false;
  	}
  	for (var i = 0; i < arrayIdNotificaObbl.length; i++) {
    	id = arrayIdNotificaObbl[i];
    	if (!checkNotifica(id))
      	return false;
  	}        
	return true;        
}

function checkNotifica (idNot) {
  	var giornoObj = document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot);
	var meseObj   = document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_" + idNot);
	var annoObj   = document.getElementById("<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_" + idNot);          
	if (giornoObj.value.length == 1)
		giornoObj.value = '0' + giornoObj.value;
	if (meseObj.value.length == 1)
		meseObj.value = '0' + meseObj.value;
	var data_to_verify = giornoObj.value + '/' + meseObj.value + '/' + annoObj.value;
	if (!ControllaDataPassaVuota(data_to_verify)) {
		alert('Data Notifica non valida');
		giornoObj.focus();
		return false;
	}
	var dataOdierna = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
	if (data_to_verify != '//') {
  		if (!CompareDate(data_to_verify, dataOdierna)) {
			alert("La Data di Notifica non puo' essere superiore alla data odierna");
			giornoObj.focus();
			return false;
  		}
	}
	// data notifica al condannato >= data emissione ordine di ingiunzione
	var dataEmissioneOI = '<%=DateUtils.getDateToString(dataEmissioneOI, "dd/MM/yyyy")%>';
	if (data_to_verify != '//') {
  		if (!CompareDate(dataEmissioneOI, data_to_verify)) {
			alert("La Data di Notifica non puo' essere inferiore alla Data di Emissione Ordine Esecuzione di Ingiunzione al Pagamento");
			giornoObj.focus();
			return false;
  		}
	}
	if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot) != null) {
  		if (document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_" + idNot).checked) {
  			// La notifica e' stata effettuata a una autorita' differente da quella di partenza
  			if (data_to_verify == '//') {
				alert("Non e' stata indicata la data di notifica");
				giornoObj.focus();
				return false;
  			}
			if (document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_" + idNot).value == "-") {
				alert("Indicare l'autorita' che ha effettuato la notifica");
				document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_" + idNot).focus();
				return false;
      		}
  		}
  	}
	return true;
}

function caricaCombo() {
<%
NotificaModel[] lNotifiche = ordineIngiunzione.getNotifiche();
for (int i = 0; i < lNotifiche.length; i++) {
	NotificaModel lNotifica = lNotifiche[i];
  	if ("03".equals(lNotifica.getCodEsito())) {
		String codTipoAutorita = "";
		String sedeAutorita = "";
		String indirizzoAutorita = "";
    	if (lNotifica.getAutoritaEsternaDelegata() != null) {
			codTipoAutorita = lNotifica.getAutoritaEsternaDelegata().getCodTipoAutorita();
			sedeAutorita    = StringUtils.toStringJSP(lNotifica.getAutoritaEsternaDelegata().getDescrSede(), "");
			indirizzoAutorita = StringUtils.toStringJSP(lNotifica.getAutoritaEsternaDelegata().getDescrizione(), "");
%>
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=lNotifica.getIdNotifica()%> option[value="<%=codTipoAutorita%>"]').attr("selected", "selected");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_<%=lNotifica.getIdNotifica()%>').val("<%=sedeAutorita%>");
	$('#<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=lNotifica.getIdNotifica()%>').val("<%=indirizzoAutorita%>");
	document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_"+<%=lNotifica.getIdNotifica()%>).checked = true;
<%
		} else {
%>
	document.getElementById("<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_"+<%=lNotifica.getIdNotifica()%>).checked = false;
<%
		}
	}
%>
	abilitaNotifica ('<%=lNotifica.getIdNotifica()%>');
<%
}
%>
}
</script>
</head>

<body class="corpo" onLoad="caricaCombo();">
<table>
    <tr>
      	<td class="LBG">
        	<a href="Javascript:window.print();">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          	</a>
      	</td>
      	<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;&nbsp;
			<font class="campo">Inserimento avvenuta notifica alle parti</font>
		</td>
    </tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" name="LoadInserisciNotifica" action="<%= IWebConstants.PG_MAIN%>">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciNotificaOrdineIngiunzione">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=ordineIngiunzione.getEvento().getIdEvento()%>">
<table>
	<tr>
		<td class="l">Posizione Giuridica </td>
		<td class="L" colspan="5">
        	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
          		DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
<%
} else {
%>
				<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
        	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
		</td>
	</tr>
</table>
<table>
	<tr>
		<td class="L" colspan="5">
			<font class="campo">
        		<%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
        		&nbsp;
        		<%=ordineIngiunzione.getEvento().getDescrMotivo()%>
        		&nbsp;emesso in data&nbsp;
        		<%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
			</font>
        	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
		</td>
    </tr>
</table>
<%
//=======================================================================
//                     Notifica al condannato
//=======================================================================
%>
<table>
	<tr>
		<td class="Titolo" colspan="6">Notifica al Condannato</td>
	</tr>
<%
if (notificaAlCondannato.getAutoritaEsterna() != null) {
%>
	<tr>
		<td class="L">Autorita' preposta alla notifica</td>
       	<td class="L" colspan="3">
        	<font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
       		di
        	<font class="campo"><%=StringUtils.toStringJSP( notificaAlCondannato.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
       	</td>
	</tr>
<%
	if (notificaAlCondannato.getNote() != null) {
%>
	<tr>
		<td class="L">Indirizzo</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(notificaAlCondannato.getNote())%></font>&nbsp;</td>
	</tr>
<%
	}
}
if (notificaAlCondannato.getIstitutoDetenzione() != null) {
	String lNotificaIstituto = notificaAlCondannato.getIstitutoDetenzione().getDescrTipoIstituto();
	if (notificaAlCondannato.getIstitutoDetenzione().getDescrComune() != null)
	  	lNotificaIstituto += " di " + notificaAlCondannato.getIstitutoDetenzione().getDescrComune();
	if (notificaAlCondannato.getIstitutoDetenzione().getIndirizzo() != null)
	  	lNotificaIstituto += " - " + notificaAlCondannato.getIstitutoDetenzione().getIndirizzo();
%>
	<tr>
		<td class="L">Istituto Notifica</td>
        <td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(lNotificaIstituto)%></font>&nbsp;
		</td>
	</tr>
<%
}
// ==================================================
// sezione per registrare
// ==================================================
%>
  	<tr>
	    <td class="l">Data Notifica</td>
	    <td class="l">
      		<font class="campo">
		        <input type="text" maxlength="2" size="2" 
	               id="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>" 
	               name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>" 
	               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notificaAlCondannato.getDataAvvenutaNotifica(), "dd"))%>"         
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        		-
        		<input type="text" maxlength="2" size="2" 
					id="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=notificaAlCondannato.getIdNotifica()%>"  
					name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=notificaAlCondannato.getIdNotifica()%>"  
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notificaAlCondannato.getDataAvvenutaNotifica(), "MM"))%>"         
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				-
        		<input type="text" maxlength="4" size="4" 
					id="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>"   
					name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>"   
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notificaAlCondannato.getDataAvvenutaNotifica(), "yyyy"))%>" 
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</font>
    	</td>    
  	</tr> 
	<tr>
    	<td class="l">
      		<input type="checkbox" onclick="Javascript:abilitaNotifica('<%=notificaAlCondannato.getIdNotifica()%>');" 
				id="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>" 
				name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=notificaAlCondannato.getIdNotifica()%>" 
				value="<%=notificaAlCondannato.getIdNotifica()%>" >
    	</td>    
	</tr> 
	<tr>               
    	<td class="l">Autorita' che ha effettuato la notifica</td>
    	<td class="l">
      		<select disabled Title="Autorita' che ha effettuato la notifica"  class="small" 
					id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=notificaAlCondannato.getIdNotifica()%>"
					name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=notificaAlCondannato.getIdNotifica()%>">
        		<%=comboAutNotifica%>
      		</select>
    	</td>
	</tr>
	<tr>               
    	<td class="l">Sede</td>
    	<td class="L">
	      	<input type="text" maxlength="35" size="35" title="Sede Autorita' che ha effettuato la notifica" value="" 
				name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=notificaAlCondannato.getIdNotifica()%>"
	           	id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=notificaAlCondannato.getIdNotifica()%>">
	        <a href="Javascript:ListaComuni('LoadInserisciNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_<%=notificaAlCondannato.getIdNotifica()%>');" 
	            	id="folderIcon_<%=notificaAlCondannato.getIdNotifica()%>" style="display:none;">
				<img src="/images/filefolder.gif" border="0" >
	        </a>
		</td>
    	<td class="l">Indirizzo</td>
    	<td class="L">
      		<TEXTAREA title="Indirizzo" id="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=notificaAlCondannato.getIdNotifica()%>" 
				name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=notificaAlCondannato.getIdNotifica()%>" cols="30">
			</TEXTAREA>
		</td>
  	</tr>
<%
//=======================================================================
//                      Notifica al Difensore
//=======================================================================
%>
  	<tr>
    	<td class="Titolo" colspan="6">Notifica al Difensore</td>
  	</tr>
<%
// Difensore/i
for (int i = 0; i < listaNotAvvSiep.size(); i++) {
	NotificaModel lNotificaDifensore = (NotificaModel) listaNotAvvSiep.get(i);
	if (i==1) {
%>
	<tr><td>&nbsp;</td></tr>
<%
	}
%>
	<tr>
		<td class="l">Avvocato per  Notifica</td>
		<td class="L" colspan="3">
		 	<font class="campo"><%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getCognome()) + " " + StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getNome())%></font>
			&nbsp;Foro di&nbsp;
			<font class="campo">
		  		<%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getForo())%>
			</font>
			&nbsp;Difensore di&nbsp;
			<font class="campo">
		  		<%=StringUtils.toStringJSP(lNotificaDifensore.getAvvSiep().getAvvocato().getDescrTipo())%>
		 	</font>
		</td>
	</tr>
	<tr>
		<td class="L">Autorita preposta alla Notifica</td>
		<td class="L" colspan="3">
		  	<font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsterna().getDescrTipoAutorita())%></font>
			&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP( lNotificaDifensore.getAutoritaEsterna().getDescrSede())%></font>
		</td>
	</tr>
	<tr>  
		<td class="L">Indirizzo</td>
		<td class="L" colspan="3">
			<font class="campo"><%=StringUtils.toStringJSP(lNotificaDifensore.getAutoritaEsterna().getDescrizione())%></font>
		</td>
	</tr>

<%
// ==================================================
// sezione per registrare
// ==================================================
%>
	<tr>
	    <td class="l">Data Notifica</td>
	    <td class="l">
      		<font class="campo">
		        <input type="text" maxlength="2" size="2" 
		               id="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>" 
		               name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>" 
		               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaDifensore.getDataAvvenutaNotifica(), "dd"))%>"         
		               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input type="text" maxlength="2" size="2" 
		               id="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=lNotificaDifensore.getIdNotifica()%>"  
		               name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=lNotificaDifensore.getIdNotifica()%>"  
		               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaDifensore.getDataAvvenutaNotifica(), "MM"))%>"         
		               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input type="text" maxlength="4" size="4" 
		               id="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>"   
		               name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>"   
		               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaDifensore.getDataAvvenutaNotifica(), "yyyy"))%>" 
		               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      		</font>
		</td>    
  	</tr>  
	<tr>
    	<td class="l">
      	<input type="checkbox" onclick="Javascript:abilitaNotifica('<%=lNotificaDifensore.getIdNotifica()%>');" 
			id="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>" 
			name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=lNotificaDifensore.getIdNotifica()%>"
            value="<%=lNotificaDifensore.getIdNotifica()%>">
		</td>    
  	</tr>
	<tr>               
	    <td class="l">Autorita' che ha effettuato la notifica</td>
	    <td class="l">
	      	<select Title="Autorita' che ha effettuato la notifica"  class="small"
		              id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=lNotificaDifensore.getIdNotifica()%>"
		              name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=lNotificaDifensore.getIdNotifica()%>">
	        	<%=comboAutNotifica%>
			</select>
	    </td>
	</tr>  
	<tr>               
	    <td class="l">Sede</td>
	    <td class="L">
      		<input type="text" maxlength="35" size="35" value="" 
	            title="Sede Autorita' che ha effettuato la notifica XX"              
				id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=lNotificaDifensore.getIdNotifica()%>"  
				name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=lNotificaDifensore.getIdNotifica()%>">
        	<a href="Javascript:ListaComuni('LoadInserisciNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_<%=lNotificaDifensore.getIdNotifica()%>');" 
            		id="folderIcon_<%=lNotificaDifensore.getIdNotifica()%>" style="display:none;">
          		<img src="/images/filefolder.gif" border="0" >
        	</a>
		</td>
	    <td class="l">Indirizzo</td>
	    <td class="L">
      		<TEXTAREA title="Indirizzo" cols="30"
                id="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=lNotificaDifensore.getIdNotifica()%>"
                name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=lNotificaDifensore.getIdNotifica()%>">
			</TEXTAREA>
		</td>
	</tr>
<%
} // end for Notifiche avvocati
//=======================================================================
//                  Notifica al Civilmente Obbligato
//=======================================================================
if (lListaNotObbligati.size() > 0) {
%>
	<tr><td class="Titolo" colspan="4">Notifica al Civilmente Obbligato</td></tr>
<%
	// Civilmente Obbligati
  	for (int i = 0; i < lListaNotObbligati.size(); i++) {
    	NotificaModel lNotificaObbligato = (NotificaModel) lListaNotObbligati.get(i);
    	if (lNotificaObbligato.getCivilmenteObbligato() == null) {
      		lNotificaObbligato.setCivilmenteObbligato( new CivilmenteObbligatoModel());
    	}
    	CivilmenteObbligatoModel lObbligatoModel = lNotificaObbligato.getCivilmenteObbligato();
		if (i==1) {
%>
	<tr><td>&nbsp;</td></tr>
<%
		}
%>
	<tr>
		<td class="l">Civilmente Obbligato</td>
        <td class="L" colspan="3">
			<font class="campo"><%=lObbligatoModel.getCognome()%></font>&nbsp;
			<font class="campo"><%=lObbligatoModel.getNome()%></font>&nbsp;
			<font class="label">nato a</font>&nbsp;<font class="campo"><%=lObbligatoModel.getDescComuneNascita()%></font>&nbsp;
			<font class="label">il</font>&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lObbligatoModel.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
		if ("G".equals(lObbligatoModel.getCodPersona())) {
%>
			<font class="label"> in qualita' di Legale Rappresentante di </font>
			<font class="campo"><%=lObbligatoModel.getDenominazione()%></font>
<%
		}
%>      
		</td>
	</tr>
	<tr>
		<td class="l">Autorita Delegata alla Notifica</td>
        <td class="L" colspan="3">
          	<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrTipoAutorita() )%></font>
          	&nbsp;di&nbsp;
          	<font class="campo"><%=StringUtils.toStringJSP( lNotificaObbligato.getAutoritaEsterna().getDescrSede())%></font>
        </td>
	</tr>
	<tr>
		<td class="l">Note</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotificaObbligato.getNote())%></font></td>
	</tr>
<%
// ==================================================
// sezione per registrare
// ==================================================
%>
	<tr>
    	<td class="l">Data Notifica</td>
    	<td class="l">
      		<font class="campo">
				<input type="text" maxlength="2" size="2" 
					id="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>" 
					name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>" 
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaObbligato.getDataAvvenutaNotifica(), "dd"))%>"         
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				-
				<input type="text" maxlength="2" size="2" 
					id="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=lNotificaObbligato.getIdNotifica()%>"  
					name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>_<%=lNotificaObbligato.getIdNotifica()%>"  
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaObbligato.getDataAvvenutaNotifica(), "MM"))%>"         
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
				-
				<input type="text" maxlength="4" size="4" 
					id="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>"   
					name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>"   
					value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificaObbligato.getDataAvvenutaNotifica(), "yyyy"))%>" 
					onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</font>
		</td>    
	</tr>  
	<tr>
    	<td class="l">
      		<input type="checkbox" onclick="Javascript:abilitaNotifica('<%=lNotificaObbligato.getIdNotifica()%>');" 
				id="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>" 
				name="<%=ICostantiOrdineEsecuzione.ABILITA_NOTIFICA%>_<%=lNotificaObbligato.getIdNotifica()%>" 
				value="<%=lNotificaObbligato.getIdNotifica()%>">
    	</td>    
	</tr> 
	<tr>               
		<td class="l">Autorita' che ha effettuato la notifica</td>
    	<td class="l">
      		<select Title="Autorita' che ha effettuato la notifica"  class="small" 
					id="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=lNotificaObbligato.getIdNotifica()%>"
              		name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>_<%=lNotificaObbligato.getIdNotifica()%>">
				<%=comboAutNotifica%>
      		</select>
    	</td>
  	</tr>
	<tr>               
	    <td class="l">Sede</td>
	    <td class="L">
      		<input type="text" maxlength="35" size="35" value="" title="Sede Autorita' che ha effettuato la notifica" 
            		name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=lNotificaObbligato.getIdNotifica()%>" 
             		id="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>_<%=lNotificaObbligato.getIdNotifica()%>">
	        <a href="Javascript:ListaComuni('LoadInserisciNotifica','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>_<%=lNotificaObbligato.getIdNotifica()%>');" 
	            	id="folderIcon_<%=lNotificaObbligato.getIdNotifica()%>" style="display:none;">
				<img src="/images/filefolder.gif" border="0" >
	        </a>
    	</td>
    	<td class="l">Indirizzo</td>
    	<td class="L">
			<TEXTAREA title="Indirizzo" id="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=lNotificaObbligato.getIdNotifica()%>" 
				name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>_<%=lNotificaObbligato.getIdNotifica()%>" cols="30">
			</TEXTAREA>
    	</td>
  	</tr>
<%
	} // end FOR obbligati
} // end if obbligati
%>  
</table>
<table>
	<tr>
    	<td class="lNoBord" colspan="2">
      		<INPUT class="bottone" type="submit" value="Conferma">
    	</td>
	</tr>
</table>  
</form>
<script language="JavaScript" type="text/javascript">
var frmvalidator = new Validator("LoadInserisciNotifica");  
frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>