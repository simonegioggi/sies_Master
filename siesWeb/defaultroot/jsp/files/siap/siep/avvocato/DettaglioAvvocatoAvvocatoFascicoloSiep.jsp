<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>

<jsp:useBean id="avvocato"		scope="request" class="siap.siep.avvocato.model.AvvocatoSiepModel"/>
<jsp:useBean id="lIstMod"		scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="lEve" 			scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="NomeAzione"    scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Avvocato AvvocatoFascicolo SIEP </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio")) {
%>
<script language="JavaScript">
var aForm=null
function Verify() {
	alert("La funzione di Iscrizione Guidata è stata Interrotta");
	aForm=document.getElementById("Abbandona");
	Disabilita();
}

function DisabilitaAltro() {
	aForm=document.getElementById("AltroDif");
	Disabilita();
}

function Disabilita() {
	if (aForm==null)
		aForm=document.getElementById("CapoImput");
	document.Abbandona.A.disabled = true;
	document.CapoImput.C.disabled = true;
	document.AltroDif.D.disabled = true;
	aForm.submit();
}
</script>
<%
}
%>
</head>
<%
AvvocatoSiepModel lAvvocato = new AvvocatoSiepModel(avvocato);
%>
<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
      	<td class=LBG><font class="label">Funzione :</font>&nbsp;&nbsp;
        	<font class="campo">Dettaglio Avvocato</font>
      	</td>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (lTipoFunzione == null || lTipoFunzione.equals("")) {
	if (!lAvvocato.getAvvocatoFascicoloSiepModel().getCodMotivoDesignazione().equals("-")
			&& lAvvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("D'Ufficio")
    		&& lEve != null && lEve.getEvento() != null 
    		&& lEve.getEvento().getIdEvento() != null
    		&& !"S".equals(lEve.getEvento().getFlagDocumentoRegistrato())) {
%>
<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     	<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.avvocato.action.ActStampaAvvocato&codMotivo="
    		 +lAvvocato.getAvvocatoFascicoloSiepModel().getCodMotivoDesignazione()
    		 +"&IdAvvocatoFascicoloSiep="+lAvvocato.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep()
    		 +"&IdAvvocato="+lAvvocato.getAvvocato().getIdAvvocato()
    		 +"&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
   		</jsp:include>
<%
	} else {
%>
    	<td class="LBG">
     		<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
         		<jsp:param name="CampoIdEntita" value="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" />
         		<jsp:param name="ValoreIdEntita" value="<%=lAvvocato.getAvvocato().getIdAvvocato() %>" />
      	</jsp:include>
     	</td>
<%
		// commento la seguente riga perchè vengono 2 bottoni se faccio OE senza avvocato paolo 22/01/2009
   		// <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON....
		if (request.getParameter("NomeAzione") != null
				&& request.getParameter("NomeAzione").equals("siap.siep.avvocato.action.ActRicercaStoricoAvvocatoFascicolo")) {
%>
		<td class="LBG">
			<a href="javascript:history.go(-1);">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
		</td>
<%
		}
	}
}
%>
</tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<input type="hidden" name="idAvvocato" value="<%=lAvvocato.getAvvocato().getIdAvvocato() %>">
<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="l"><font class="label">Cognome</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getCognome()) %></font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Nome</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getNome()) %>&nbsp;</font></td>
	</tr>
  	<tr>
		<td class="l"><font class="label">Comune di Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescLuogoNascita()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Stato di Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescrStatoNascita())%></font></td>
	</tr>
	<tr>
<%		String descLuogoNascitaEstero = "039".equals(lAvvocato.getAvvocato().getCodStatoNascita()) 
										? ""
										: lAvvocato.getAvvocato().getDescLuogoNascitaReginde();
%>
		<td class="l"><font class="label">Luogo di Nascita Estero</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(descLuogoNascitaEstero)%>&nbsp;</font></td>
	</tr>
  	<tr>
		<td class="l"><font class="label">Data di Nascita</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getAvvocato().getDataNascita(),"dd-MM-yyyy")) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Foro</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getForo()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Indirizzo</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getIndirizzo()) %>&nbsp;</font></td>
	</tr>
  	<tr>
		<td class="l"><font class="label">Con Studio in</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescComuneResidenza()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Telefono</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getTelefono()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Fax</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getFax()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">EMail</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getEMail()) %>&nbsp;</font></td>
	</tr>
	<%-- MEV_21: aggiunto campo per chiamata a WS per individuare lista avvocato in RegInde --%>
    <tr>
		<td class="l"><font class="label">Pec</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getPec())%></font>&nbsp;</td>
	</tr>
	<tr>
		<td class="l"><font class="label">Codice Fiscale</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getCodiceFiscale()) %>&nbsp;</font></td>
	</tr>
	<tr>
		<td class="l"><font class="label">Stato Attività Difensore</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescrNonAttivita())%></font>&nbsp;</td>
	</tr>
  	<tr>
		<td class="l"><font class="label">Tipo</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocato().getDescrTipo()) %>&nbsp;</font></td>
	</tr>
<%
if ("DI FIDUCIA".equalsIgnoreCase(lAvvocato.getAvvocato().getDescrTipo())
		|| "D'UFFICIO".equalsIgnoreCase(lAvvocato.getAvvocato().getDescrTipo())) {
%>
    <tr>
<%
	if ("DI FIDUCIA".equalsIgnoreCase(lAvvocato.getAvvocato().getDescrTipo())) {
%>
		<td class="l"><font class="label">Data Nomina</font></td>
<%
    }
    if ("D'UFFICIO".equalsIgnoreCase(lAvvocato.getAvvocato().getDescrTipo())) {
%>
       <td class="l"><font class="label">Data Designazione</font></td>
<%
    }
%>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvvocato.getAvvocatoFascicoloSiepModel().getDataInizioValidita(),"dd-MM-yyyy")) %>&nbsp;</font></td>
    </tr>
<%
}
if ("D'UFFICIO".equalsIgnoreCase(lAvvocato.getAvvocato().getDescrTipo())) {
	if (!"-".equals(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrMotivoDesignazione())) {
%>
	<tr>
	 	<td class="l"><font class="label">Motivo Designazione</font></td>
	 	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrMotivoDesignazione()) %>&nbsp;</font></td>
	</tr>
<%
    }
    if (lAvvocato.getAvvocatoFascicoloSiepModel().getNote() != null) {
%>
	<tr>
	  	<td class="l"><font class="label">Note</font></td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getNote()) %>&nbsp;</font></td>
	</tr>
<%
    }
    if (!"-".equals(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrTipoAutorita())) {
%>
	<tr>
		<td class="l"><font class="label">Autorità per la notifica al Condannato</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrTipoAutorita()) %> di <%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getComuneTipoAutorita()) %>  </font>
			<font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getIndirizzoTipoAutorita()) %></font>
		</td>
	</tr>
<%
    }
    if (!"".equals(lIstMod.getDescrTipoIstituto())) {
%>
	<tr>
		<td class="l"><font class="label">Istituto Detenzione</font></td>
		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lIstMod.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lIstMod.getDescrComune())%>&nbsp;</font></td>
	</tr>
<%
    }
    if (!"-".equals(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrTipoAutoritaDif())) {
%>
	<tr>
	  	<td class="l"><font class="label">Autorità per la notifica al Difensore</font></td>
	  	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getDescrTipoAutoritaDif())%> di <%=StringUtils.toStringJSP(lAvvocato.getAvvocatoFascicoloSiepModel().getComuneTipoAutoritaDif())%></font></td>
	</tr>
<%
    }
}
// lTipoFunzione per capire che si proviene da iscrizione guidata
if (!"".equals(lTipoFunzione) && !"ritornodettaglio".equals(lTipoFunzione)) {
%>
	<tr>
  		<td class="lNoBord">
    		<FORM method="POST" name="AltroDif" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadInserisciAvvocato&lTipoFunzione=<%=lTipoFunzione%>">
				<br><INPUT class="bottone" type="button" name="D" value="Altro Difensore" onclick="Javascript:DisabilitaAltro();">
  			</FORM>
		</td>
		<td class="lNoBord">
  			<FORM method="POST" name="CapoImput" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadInserisciReato&lTipoFunzione=<%=lTipoFunzione%>">
				<br><INPUT class="bottone" type="button" name="C" value="Prosegui" onclick="Javascript:Disabilita();">
  			</FORM>
		</td>
		<td class="lNoBord">
  			<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep&<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>=<%=lAvvocato.getAvvocato().getIdAvvocato()%>&lTipoFunzione=ritornodettaglio">
				<br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
    		</FORM>
  		</td>
	</tr>
<%
}
%>
</table>
<%
if (lEve != null && lEve.getEvento() != null && lEve.getEvento().getIdEvento() != null) {
%>
<div align=left style="visibility:hidden" id="upld" >
<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
<table>
	<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
	<tr>
  		<td class="L">
    		<input class="bottone" type="submit" value="Conferma">
    		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.avvocato.action.ActUploadAvvocato">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= lEve.getEvento().getIdEvento() %>">
			<input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="<%=lAvvocato.getAvvocato().getIdAvvocato() %>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep">
        </td>
	</tr>
</table>
</FORM>
</div>
<br>
<%
}
%>
</body>
</html>