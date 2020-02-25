<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="fascicolosiep"       scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="codiceAutorita"      scope="request" class="java.lang.String"/>
<jsp:useBean id="modifica"      	  scope="request" class="java.lang.String"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

FascicoloSiepModel lFascMod = fascicolosiep != null ? fascicolosiep : new FascicoloSiepModel();
ArchiviazioneModel lArcMod = archiviazione != null ? archiviazione : new ArchiviazioneModel();

if (lPosizione == null)
	lPosizione = new PosizioneGiuridicaModel();

if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
  	lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
  	<title>[S.I.E.S.] - Gestione Archiviazione </title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	<script language="JavaScript">
   	function ListaComuni(a_formname,a_fieldname) {
       	var desktop;
      	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

   	function Altro() {
		document.f.C.disabled=true;
       	document.f.A.disabled=true;
       	document.f.tipobottone.value='altro';
       	document.f.submit();
   	}

   	function Conferma() {
       	document.f.C.disabled=true;
       	document.f.A.disabled=true;
       	document.f.tipobottone.value='conferma';
       	document.f.submit();
  	}
	</script>
</head>
<body class="corpo">
<table>
	<tr>
      	<td class="LBG">
      		<a href="Javascript:window.print();">
      			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
      		</a>
      	</td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      		<font class="campo">Dettaglio Definizione Procedimento - Perdita di competenza</font>
    	</td>
    </tr>
</table>
<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>
<form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActAggiornaAssorCumuloAltro">
<input type="HIDDEN" name="tipobottone" value="">
<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
<table style="width: 95%;">
	<tr>
		<td class="l" width="25%">Posizione Giuridica </td>
      	<td class="L" colspan=3>
        	<font class="campo">
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
%>
				DETENUTO PER ALTRA CAUSA
<%
} else {
%>
				<%=lPosizione.getDescrPosizioneGiuridica()%>
<%
}
%>
			</font>
		</td>
    </tr>
<%
if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
	if (lAltraCausa.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Detenuto presso</td>
		<td class="L" colspan=5>
			<font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
		if (lAltraCausa.getAltroLuogo() != null) {
%>
	<tr>
		<td class="l">Altro Luogo</td >
		<td class="L" colspan=5>
		  	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>
		</td>
	</tr>
<%
		}
	}
} else if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Detenuto presso</td>
		<td class="L" colspan=5>
			<font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
			&nbsp;di&nbsp;<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
		</td>
	</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
if (lPosizione.getCodPosizioneGiuridica() != null
		&& (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04"))) {
 	if (lLuogoDetenzione.getIstitutoDetenzione() != null) {
%>
	<tr>
	  	<td class="l">Indirizzo</td>
	  	<td class="L" colspan=5>
	    	<font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>
	   	</td>
	</tr>
<%
	}
}
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
		&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))) {
%>	
	<tr>
<%
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO
	} else {
%>
		<td class="l" width="25%">Reclusione</td>
		<td class="l" >
			<font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%></font>&nbsp;
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%></font>&nbsp;
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
		</td>
<%
		if (penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
		<td class="l">Multa</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
		</td>
<%
		}
	}
%>
	</tr>
   	<tr>
<%
	if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0)) {
		// NOTHING TO DO
	} else {
%>
		<td class="l" width="25%">Arresto</td>
      	<td class="l">
			<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%></font>&nbsp;
			<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%></font>&nbsp;
			<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      	</td>
<%
		if (penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
        <td class="l">Ammenda</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
        </td>
<%
		}
	}
}
%>
	</tr>
<%
if (penaresidua != null) {
%>
	<tr>
<%
	if (penaresidua.getDataInizio() != null) {
%>
		<td class="l" width="25%">Pena Espiata dal</td>
		<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
		</td>
<%
	}
	if (penaresidua.getFlagErgastolo() != null) {
		if (penaresidua.getFlagErgastolo().equals("S")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO</font></td>
<%
		} else if (penaresidua.getFlagErgastolo().equals("D")) {
%>
		<td class="l">Pena Detentiva</td>
		<td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
<%
		}
	}
	if ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
			&& !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
    	if (penaresidua.getDataFine() != null) {
			if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
		<td class="l">al</td>
		<td class="L" colspan=2>
		  	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
			} else {
%>
		<td class="l">al</td>
		<td class="lRosso" colspan=2>
		 	<font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
		</td>
<%
			}
		}
	}
%>
	</tr>
<%
}
if (eventonotifica.getEvento().getDataEmissione() != null) {
%>
	<tr>
		<td class="l" width="25%">Data Emissione</td>
		<td class="L" >
		  	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length !=0
		&& eventonotifica.getNotifiche()[0] != null && eventonotifica.getNotifiche()[0].getDataInvio() != null) {
%>
	<tr>
		<td class="l">Data Trasmissione</td>
		<td class="L" >
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%></font>
		</td>
	</tr>
<%
}
if (lArcMod.getCodOggettoDefinizione() != null) {
%>
	<tr>
		<td class="l">Oggetto Definizione</td>
        <td class="l">
          	<font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrOggettoDefinizione())%></font>
        </td>
	</tr>
<%
}
if (lFascMod.getDataUnione() != null) {
%>
	<tr>
		<td class="l">Data Provvedimento di cumulo</td>
		<td class="L">
	  		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascMod.getDataUnione(), "dd-MM-yyyy"))%></font>
	 	</td>
	</tr>
<%
}
if (lFascMod.getCodUfficioUnione() != null && lFascMod.getDescrComuneUfficioUnione() != null) {
%>
	<tr>
		<td class="l">Ufficio che ha emesso il cumulo</td>
		<td class="L">
		  	<font class="campo"> <%=StringUtils.toStringJSP(lFascMod.getDescrTipoUfficioUnione())%></font>&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lFascMod.getDescrComuneUfficioUnione())%></font>
		</td>
	</tr>
<%
}
if (lFascMod.getNumFascicoloUnione() != null && lFascMod.getAnnoFascicoloUnione() != null) {
%>
	<tr>
	 	<td class="l">Numero Procedimento SIEP</td>
	 	<td class="l">
	  		<font class="campo"><%=StringUtils.toStringJSP(lFascMod.getAnnoFascicoloUnione())%></font>/
			<font class="campo"><%=StringUtils.toStringJSP(lFascMod.getNumFascicoloUnione())%></font>
	 	</td>
	</tr>
<%
}
if (lArcMod.getDataDefinizione() != null) {
%>
	<tr>
		<td class="l">Data Definizione</td>
		<td class="L">
		  	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataDefinizione(), "dd-MM-yyyy"))%></font>
		</td>
	</tr>
<%
}
if (lArcMod.getNote() != null) {
%>
	<tr>
	 	<td class="l">Motivazioni</td>
	 	<td class="L">
	   		<font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNote())%></font>
	 	</td>
	</tr>
<%
}
if (magistrato != null) {
%>
	<tr>
	 	<td class="l">Magistrato</td>
	 	<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
	 	</td>
	</tr>
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null) {
	Iterator iter = (Arrays.asList(eventonotifica.getNotifiche())).iterator();
	while (iter.hasNext()) {
		NotificaModel lNotMod = (NotificaModel) iter.next();
       	if (lNotMod != null && lNotMod.getUffCodUfficio() != null && lNotMod.getCodTipoNotifica().equals("E")) {
%>
	<tr>
 		<td class="l">Ufficio recupero crediti presso</td>
 		<td class="l">
   			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%></font>&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
 		</td>
	</tr>
<%
		}
       	if (lNotMod != null && lNotMod.getAutoritaEsterna() != null && lNotMod.getCodTipoNotifica().equals("N")) {
%>
	<tr>
		<td class="l">Autorità di polizia</td>
		<td class="l">
		  	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;di&nbsp;
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
		</td>
	</tr>
<%
			if (lNotMod.getNote() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
		<td class="l">
		  	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
		</td>
	</tr>
<%
			}
		}
     	// MEV_66: aggiunto campo in visualizzazione = tdsm + udsm
       	if (lNotMod != null && lNotMod.getUffCodUfficio() != null
       			&& ("TDS".equals(lNotMod.getUfficio().getCodTipoUfficio())
       					|| "TDSM".equals(lNotMod.getUfficio().getCodTipoUfficio()))) {
%>
	<tr>
		<td class="l"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%></td>
		<td class="l">
		   	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
		</td>
	</tr>
<%
		}
       	if (lNotMod != null && lNotMod.getUffCodUfficio() != null
       			&& ("UDS".equals(lNotMod.getUfficio().getCodTipoUfficio())
     				   || "UDSM".equals(lNotMod.getUfficio().getCodTipoUfficio()))) {
%>
	<tr>
		<td class="l"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%></td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
		</td>
	</tr>
<%
		}
	}
}
%>
</table>
<%
// Non provengo dal dettaglio dei provv o non sono validato
if (!modifica.equals("dettaglio")) {
%>
<table style="width: 95%;">
    <tr>
		<td class="l" width="25%">Altra Autorità</td>
      	<td class="L" colspan="3">
        	<select  Title="Autorita" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
         		<%=codiceAutorita%>
         	</select>
      	</td>
	</tr>
	<tr>
     	<td class="l">Sede</td>
     	<td class="L">
			<input title="Sede Autorita" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>" maxlength="35" size="35">
          	<a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
          		<img src="/images/filefolder.gif" border="0">
        	</a>
      	</td>
      	<td class="l">Indirizzo</td>
      	<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"  cols=30 ></textarea>
       	</td>
	</tr>
    <tr>
      	<td class="l" width="25%">Altra Autorità</td>
       	<td class="l" colspan="3">
       		<font class="campo">
        		<input Title="Altra Autorità" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  size=70 type="text">
       		</font>
       	</td>
	</tr>
	<tr>
  		<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="button" name="C" value="Conferma" onclick="Javascript:Conferma();">
  		</td>
		<td class="lNoBord" colspan="2">
    		<br><INPUT class="bottone" type="submit" name="A" value="Altro Destinatario" onclick="Javascript:Altro();">
  		</td>
	</tr>
</table>
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null) {
%>
<table style="width: 95%;">
<%
	Iterator iter = (Arrays.asList(eventonotifica.getNotifiche())).iterator();
    while (iter.hasNext()) {
		NotificaModel lNotMod = (NotificaModel) iter.next();
       	if (lNotMod != null && lNotMod.getAutoritaEsterna() != null && lNotMod.getCodTipoNotifica().equals("C")) {
%>
	<tr>
		<td class="l" width="25%">Altra Autorità</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;di&nbsp;
           	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
        </td>
	</tr>
<%
			if (lNotMod.getNote() != null) {
%>
	<tr>
		<td class="l">Indirizzo</td>
	  	<td class="L">
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
	   	</td>
	</tr>
<%
           	}
		}
       	if (lNotMod != null && lNotMod.getCodTipoNotifica().equals("C") && lNotMod.getNote() != null && lNotMod.getAutoritaEsterna() == null) {
%>
	<tr>
		<td class="l">Altra Autorità</td>
		<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
		</td>
	</tr>
<%
		}
	}
%>
</table>
<%
}
%>
</form>
</body>
</html>