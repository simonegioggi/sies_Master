<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>

<jsp:useBean id="UtenteConnesso"     	scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="aModelFas"            	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="LuogoUtenteConnesso"	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="tipoProvvedimenti"     scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoTrib"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="MisuraSic"   			scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>

<%
//==============================================================================
// Form per la ricerca del Titolo Esecutivo da Associare alla M.S.
//
// Al primo caricamento vengono visualizzate sia la sezione per la ricerca
// dei fascicoli, che la sezione per inserire i dati di Titolo Esecutivo.
//==============================================================================

String TipoPro="";
String TipoAut="";
if(aModelFas != null && aModelFas.getIdFascicoloSiep()!=null)
{
	TipoPro=aModelFas.getSentenza().getCodTipoProvvedimento();
	TipoAut=aModelFas.getSentenza().getCodTipoAutoritaEmittente();
}

%>

<!-- 	LoadRicercaAssocia_TitoloEsec_aMisuraSic	 -->
<html>
<head>
  	<title> [S.I.E.S.] - Misure Sicurezza - Associa Titolo Esecutivo a Misura di Sicurezza</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  	<script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname,codTipoUfficio) {
		if (codTipoUfficio == 'PM')
			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
		else if (codTipoUfficio == 'PGCAP')
			desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
		else
			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

	function ListaComuniCompleata(a_formname,a_fieldname) {
		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

	// 10/06/2010 Lista Uffici per TIPO_UFFICIO
	function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
	    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

  	function CercaTito() {
		var nodeSiep = document.getElementById("divSiep");
		nodeSiep.style.display='block';
		var nodeMis = document.getElementById("divMis");
		nodeMis.style.display='none';
		var nodeAcco = document.getElementById("divAcco");
		nodeAcco.style.display='block';
		document.f.Ricerca.disabled=false;
		document.f.Ricerca.style.display='block';
	}

  	function InsTito() {
	  	var nodeSiep = document.getElementById("divSiep");
	  	nodeSiep.style.display='none';
	  	var nodeMis = document.getElementById("divMis");
	  	nodeMis.style.display='block';
	  	var nodeAcco = document.getElementById("divAcco");
	  	nodeAcco.style.display='none';
	  	document.f.Ricerca.style.display='none';
  	}
    var ufficiAccorpatiArray = new Array();

<%
Iterator uaIter = ufficiAccorpati.iterator();
int uaIndice = 0;
while (uaIter.hasNext()) {
	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
%>
	ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getDescrizioneNewUfficio()%>","<%=uaModel.getCodTipoUfficio()%>"); 
<%
	uaIndice ++;
}
%>

	function loadUfficiAccorpati(codUfficio) {
		var i=0;
		var ufficioAccorpatoSelect = document.f.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;

		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");

		while (i < ufficiAccorpatiArray.length) {
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[2] == codUfficio) {
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
			}
			i++;
		}
	}
  
    function resetSede() {
		document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value = "";
      	loadUfficiAccorpati('');
    }
    
    //==========================================================================
    //
    //==========================================================================
    function init() {
		var nodeSiep = document.getElementById("divSiep");
		nodeSiep.style.display='block';
		var nodeMis = document.getElementById("divMis");
		nodeMis.style.display='none';
		var nodeAcco = document.getElementById("divAcco");
		nodeAcco.style.display='block';
		var nodeLoad = document.getElementById("divLoad");
<%
if (aModelFas != null && aModelFas.getIdFascicoloSiep() != null && aModelFas.getChiaveAnno() != null
		&& aModelFas.getChiaveProgr() != null) {
%>
		nodeLoad.style.display = 'none';
<%
}
%>
	}	

	function LoadTitolo(idMisura) {
		if (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value.length < 4
				|| document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value < 1900
				|| isNaN(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value)) {
			alert ("Anno Fascicolo SIEP Non Valido");
			document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.focus();
			document.f.Ricerca.disabled=false;
			return false;
      	}
      	if (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value.length <= 0
      			|| document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value < 0
      			|| isNaN(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value)) {
            alert ("Numero Fascicolo SIEP Non Valido");
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.focus();
            document.f.Ricerca.disabled=false;
            return false;
      	}
      	if (document.f.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP %>.value == "-") {
            alert("L' Ufficio Fascicolo SIEP è un campo obbligatorio");
            document.f.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP %>.focus();
            document.f.Ricerca.disabled=false;
            return false;
      	}

      	if (document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value == "") {
            alert("Il luogo per l'Ufficio Fascicolo SIEP è un campo obbligatorio");
            document.f.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus();
            document.f.Ricerca.disabled=false;
            return false;
      	}

		document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.misurasicurezza.action.ActLoadRicercaAssocia_TitoloEsec_aMisuraSic";
		document.f.<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>.value = idMisura;
		document.f.submit();
	}

	function Verify() {
	if (document.f.Modo[1].checked) {
		// Selezione radioButton su: 'Inserimento Titolo Esecutivo'
		if (document.f.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value == ""
				|| document.f.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length < 4
				|| document.f.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value < 1900
				|| isNaN(document.f.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value)) {
			alert ("Anno Fascicolo Non Valido");
	        document.f.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP%>.focus();
	        document.f.Ricerca.disabled=true;
	        return false;
		}

		if (document.f.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP %>.value == ""
				|| document.f.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP %>.value.length <= 0
				|| isNaN(document.f.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP %>.value)) {
			alert ("Numero Fascicolo Non Valido");
	        document.f.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP%>.focus();
	        document.f.Ricerca.disabled=true;
	        return false;
		}
	} else {
		// Selezione radioButton su: 'Ricerca Procedimento SIEP
		if (document.getElementById("divLoad").style.display == "block") {
			// il Bottone 'RICERCA' è: Visibile
			if (window.confirm('Si vuole Associare un Titolo Esecutivo di un Procedimento SIEP senza prima ricercarlo! Confermi?')) {
				if (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value.length < 4
						|| document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value < 1900
						|| isNaN(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.value)) {
					alert ("Anno Fascicolo SIEP Non Valido");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>.focus();
			        return false;
		      	}
				if (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value.length <= 0
						|| document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value < 0
						|| isNaN(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.value)) {
					alert ("Numero Fascicolo SIEP Non Valido");
			        document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>.focus();
			        return false;
				}
			} else {
				return false;
			}
		}
		// MEV_39: aggiunto controllo sul campo chiave fascicolo
<%
if (aModelFas != null && aModelFas.getIdFascicoloSiep() == null) {
%>
		alert("Effettuare la Ricerca del Procedimento SIEP oppure Inserire un Nuovo Titolo Esecutivo");
		return false;
<%
}
%>
	}

	// Tipo Provvedimento
	if (document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF%>.value == "-") {
          alert("Inserire correttamente il campo Tipo Provvedimento");
          document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF%>.focus();
	      return false;
    }
	
	// Anno Provvedimento
	if (document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.value == "") {
		alert("Il campo Anno Provvedimento è obbligatorio");
        document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.focus();
        return false;
    }
	if (document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.value.length < 4
			|| document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.value < 1900
			|| isNaN(document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.value)) {
		alert("Inserire correttamente Il campo Anno Provvedimento");
        document.f.<%=ICostantiSentenza.CAMPO_ANNO_PROVV_RIF%>.focus();
        return false;
	}

	// Numero Provvedimento
	if (document.f.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.value == "") {
		alert("Il campo Numero Provvedimento è obbligatorio");
        document.f.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.focus();
        return false;
    }
	if (document.f.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.value <= 0
			|| isNaN(document.f.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.value)) {
		alert("Inserire correttamente Il campo Numero Provvedimento ");
        document.f.<%=ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF%>.focus();
        return false;
    }

	// Data Provvedimento
	if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length == 1)
	      document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value = '0' + document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
	if (document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length == 1)
	      document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value = '0' + document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;

    var data_to_verify = document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value
    	+ '-' + document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value
    	+ '-' + document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;

    if (!ControllaData(data_to_verify)) {
	      alert("Data provvedimento non valida "+data_to_verify);
	      document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
	      return false;
    }

	// Autorità Emittente: Tipo
	if (document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value == "-") {
		alert("Il campo Tipo Autorità Emittente è obbligatorio");
        document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
        return false;
	}	

	// Autorità Emittente: Luogo
	if (document.f.<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>.value == "") {
		alert("Il campo Luogo Autorità Emittente è obbligatorio");
        document.f.<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
	}

	// Data Irrevocabilità: controllo solo se digitata
	if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value == ""
			&& document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value == ""
			&& document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value == "") {
	} else {
		if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length == 1)
	      	document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value = '0' + document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
		if (document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length == 1)
	      	document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value = '0' + document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

	    var data_to_verify = document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
	    	+ '-' + document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
	    	+ '-' + document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;

	    if (!ControllaData(data_to_verify)) {
		      alert("Data Irrevocabilità non valida");
		      document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.focus();
		      return false;
	    }
	}

 	document.f.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActAssocia_TitoloEsec_MisuraSic';
}
</script>
</head>

<body class="corpo" onLoad="Javascript:init();">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActAssocia_TitoloEsec_MisuraSic">
    <input type="hidden" name="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA %>" value="<%=MisuraSic.getIdMisuraSicurezza() %>" >
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Ricerca Titolo Esecutivo da Associare alla Misura Sicurezza</font>
        </td>
      </tr>
    </table>
 
    <br>
    
    <table width=90%>
     <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	  </td>
	</tr>
</table>
<br>    
<table width="80%">
	<tr>
        <td class="l" width="20%">Natura Misura</td>
        <td class="l"><font class="campo"><%=MisuraSic.getDescrNatura() %></font></td>
    </tr>
    <tr>
        <td class="l" width="20%">Tipo Misura</td>
        <td class="l"><font class="campo"><%=MisuraSic.getDescrTipo() %></font></td>
    </tr>
    <tr>
      <td class="l" width="20%">Durata Misura</td>
        <td class="l">&nbsp;
        	Anni
        	<font class="campo"><%=StringUtils.toStringJSP(MisuraSic.getNumAnni(),"0") %></font>&nbsp;
            Mesi
        	<font class="campo"><%=StringUtils.toStringJSP(MisuraSic.getNumMesi(),"0") %></font>&nbsp;
        	Giorni
        	<font class="campo"><%=StringUtils.toStringJSP(MisuraSic.getNumGiorni(),"0") %></font>
        </td>
    </tr>
<%    if(MisuraSic.getDataFineValidita() != null )
    {%>   
      <tr>
          <td class="l" width="20%">Data Fine Validita</td>
          <td class="L">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(MisuraSic.getDataFineValidita(),"dd-MM-yyyy") )%></font>
          </td>
      </tr>
<%  }
    
    if(MisuraSic.getFlagAnnullaMisura() != null && MisuraSic.getFlagAnnullaMisura().compareTo("A") == 0)
    {%> 
      <tr>    
          <td class="l" width="20%">Stato Misura</td>
          <td class="L"><font class="cRosso"> ANNULLATA </font></td>
      </tr> 
<%  } %>        

</table>    

<table width=50%>
	<tr>
		<td class="L">
			<input type="radio" name="Modo" value="CERCA" 
				onclick="Javascript:CercaTito();" checked > Ricerca Procedimento SIEP
		</td>
		<td class="L">
			<input type="radio" name="Modo" value="INSERISCI" 
				onclick="Javascript:InsTito();"> Inserimento Titolo Esecutivo
		</td>
	</tr>
</table>
<br>
<div id="divSiep" style="position:relative; display:block;" >
<table width=70%>
<%
if (aModelFas != null && aModelFas.getIdFascicoloSiep() != null) {
%>		
	  <tr><td class=Titolo colspan=2>Estremi dell'ulteriore Titolo Esecutivo</td></tr>
	  <tr>
	  	<td class="L" width=20%> Anno/Numero SIEP <font class="ob">(*)</font></td>
	  	<td class="L" width=40%>
	  		<input type="text" title="Anno SIEP" value="<%=StringUtils.toStringJSP(aModelFas.getChiaveAnno(), "")%>"
	  			name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4"
	       		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;/
	    	<input type="text" title="Numero SIEP" value="<%=StringUtils.toStringJSP(aModelFas.getChiaveProgr(), "")%>"
	    		name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>" maxlength="14" size="20">
    		<input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=aModelFas.getIdFascicoloSiep()%>">
	    </td>
	    	
	  </tr>
<%
} else {
%>
	  <tr>
	  	<td class="L" width=20%> Anno/Numero SIEP <font class="ob">(*)</font></td>
	  	<td class="L" width=40%>
	  		<input type="text" title="Anno SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" 
	       		maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">&nbsp;/
	    	<input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="20">
	    </td>
	  </tr>	
<%
}
%>		  
</table>  
</div>
    
<div id="divMis" style="position:relative; display:none;" >
<table width=70%>
	<tr>
    	<td class="L" width=20%> Anno/Numero <font class="ob">(*)</font></td>
    	<td class="L" width=20%>
    		<input type="text" title="Anno" value="" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>" 
           		maxlength="4" size="4" 
           		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    		<input type="text" title="Numero " name="<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP %>" maxlength="14" size="20">
    	</td>
    	
    	<td class="l" width=30%>
      		<select name="FascicoloMIS">
        		<option value="0" >Reg.Mod.38</option>
      		</select>
    	</td>
     </tr>
</table>      	
</div>

<table width=80%>
  <tr>
    <td class=l width=20%>Autorità <font class="ob">(*)</font></td>
    <td class=L width=40%>
      <select Title="Autorità" name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>" onchange="resetSede()">
        <%=TipoTrib%>
      </select>
    </td>
  </tr>

  <tr>
	<td class=l width=20%>Luogo <font class=ob>(*)</font></td>	
<%
		//	String sedePM =( modalita.equals("M")? lSentenza.getDescrSedeNotiziaReato() : UtenteConnesso.getUfficioUtente().getDescrComune());
			String sedePM = UtenteConnesso.getUfficioUtente().getDescrComune();
%>
	<td class="L" width=40%>
	 <input  type="text" Title="Luogo Sede PM" name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" value="<%=sedePM%>" maxlength="35" size="35">
	   <a href="Javascript:ListaComuni('f','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>',
	      	document.f.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP %>[document.f.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP %>.options.selectedIndex].value);">
			<img src="/images/filefolder.gif" border=0>
		</a>
	</td>
  </tr>
</table>  

<div id="divAcco" style="position:relative; display:block;" >
<table width=80%>  
  <tr>
     <td class="L" width=20%>Ufficio Accorpato</td>
     <td class="l" width=40%>
      <select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
        <option value="0" >-</option>
      </select>
     </td>
  </tr>
</table>
</div>  

<div id="divLoad" style="position:relative; display:block;">
<table>      
  <tr>
    <td class=lNoBord>
      <input class="bottone" type="button" name="Ricerca" value="RICERCA" onClick="Javascript:LoadTitolo(<%=MisuraSic.getIdMisuraSicurezza()%>);">
    </td>
  </tr>
</table>
</div>

<%
//==============================================================================
// Seconda chiamata alla form dopo aver inserito gli estremi del fascicolo SIEP;
// In questo caso carico i soli dati del fascicolo selezionato
//==============================================================================
%>
<br><br>

	<!-- 				DATI del PROVVEDIMENTO DA ASSOCIARE			 -->

<%	if(aModelFas != null && aModelFas.getIdFascicoloSiep() != null && aModelFas.getSentenza() != null)
  	{	
  	%>
  	<table width=90%>
	<tr>
		<td class="l" width=20%>Tipo Provvedimento <font class=ob>(*)</font></td>
		<td class="L" width=20%>
			<input readonly Title="Tipo Provvedimento" name="Desc_Provvedimento" 
				value="<%=StringUtils.toStringJSP(aModelFas.getSentenza().getDescrTipoProvvedimento(), "")%>">
				<input type="hidden" name="<%=ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>" value="<%=TipoPro%>">
		</td>
		<td class="l" width=20%>Data Provvedimento <font class="ob">(*)</font></td>
		<td class="L" width=20%>
			<input readonly Title="Data Provv" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getSentenza().getDataProvvedimento(),"dd"),"" )%>" 
					name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input readonly Title="Data Provv" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getSentenza().getDataProvvedimento(),"MM"),"")%>" 
					name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input readonly Title="Data Provv" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getSentenza().getDataProvvedimento(),"yyyy"),"")%>" 
					name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	
	<tr>	
		<td class="l" width=20%>Anno/Numero Provvedimento <font class=ob>(*)</font></td>
		<td class="L" width=20%>
			<input readonly type="text" Title="Anno Provv" value="<%=StringUtils.toStringJSP(aModelFas.getSentenza().getAnnoSentenza(), "")%>" 
				name="<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF %>" maxlength="4" size="4">
			/
			<input readonly type="text" Title="Numero Provv" value="<%=StringUtils.toStringJSP(aModelFas.getSentenza().getNumeroSentenza(), "")%>"  
				name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF %>" maxlength="6" size="6">
		</td>
		<td class="l" width=20%>Definitivo in Data </td>
		<td class="L" width=20%>
			<input Title="Data Irrevocabilita" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getDataIrrevocabilita(),"dd"),"" )%>" 
					name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Irrevocabilita" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getDataIrrevocabilita(),"MM"),"")%>" 
					name="<%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Irrevocabilita" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aModelFas.getDataIrrevocabilita(),"yyyy"),"")%>" 
					name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>		
	</tr>
	<!-- /table>
	<table width=80% -->
	<tr>
		<td class="l" width=20%>Autorità Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input readonly Type="text" Title="Autorità Emittente" name="Desc_Autorita" size="60" maxlength="60"
				value="<%=StringUtils.toStringJSP(aModelFas.getSentenza().getDescrTipoAutoritaEmittente(), "") %>">
			<input type="hidden" name="<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" value="<%=TipoAut%>">
		</td>
	</tr>
	<tr>
		<td class="l" width=20%>Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input readonly Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(aModelFas.getSentenza().getDescrLuogoEmittente())%>" type="text" maxlength="35" size="35"> 
        		<a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',
        				document.f.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
					<img src="/images/filefolder.gif" border=0> 
				</a>
		</td>
	</tr>
	<tr><td> </td></tr>
	<!--/table>
	<table width=90% -->
	<tr>
	  <td class="l" width=20%>Note</td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="<%= ICostantiSentenza.CAMPO_NOTE %>" cols=80 rows=3>
        </textarea>
      </td>
    </tr>  
	</table>	
<%  }
    else
    {	%>
    <table width=90%>
	<tr>
		<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
		<td class="L">
			<select Title="Tipo Provvedimento" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
				<%=tipoProvvedimenti%>
			</select>
		</td>
		<td class="l">Data Provvedimento <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Data Provv" type="text" value="" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Provv" type="text" value="" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Provv" type="text" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
	</tr>
	
	<tr>	
		<td class="l">Anno/Numero Provvedimento <font class=ob>(*)</font></td>
		<td class="L">
			<input type="text" Title="Anno Provv" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_PROVV_RIF %>" maxlength="4" size="4">
			/
			<input type="text" Title="Numero Provv" value="" name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVV_RIF %>" maxlength="6" size="6">
		</td>
		<td class="l">Definitivo in Data <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Data Irrevocabilita" type="text" value="" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Irrevocabilita" type="text" value="" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" 
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Irrevocabilita" type="text" value="" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>		
	</tr>
	
	<tr>
		<td class="l">Autorità Emittente <font class=ob>(*)</font></td>
		<td class="L">
			<select Title="Autorità Emittente" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
				<%=autoritaEmi%>
			</select></td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L">
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>" value="" type="text" maxlength="35" size="35"> 
        		<a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',
        				document.f.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.f.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
					<img src="/images/filefolder.gif" border=0> 
				</a>
		</td>
	</tr>
	<tr><td> </td></tr>
	<tr>
      <td class="l">Note</td>
      <td class="l" colspan="3"> 
        <TEXTAREA cols="80" rows="3" name="<%= ICostantiSentenza.CAMPO_NOTE %>"></textarea>
      </td> 
    </tr>
	</table>
<%	} %>

<%
//========================================================================================================
%>

<div id="LayerSubmit" style="visibility:visible; position:relative; ">
  <table>
    <tr>
     <td class="lNoBord"><Input onClick="Javascript:return Verify();" class=bottone type="submit" name="Associa" value="ASSOCIA"></td>
    </tr>
  </table>
</div>

</form>
</body>
</html>