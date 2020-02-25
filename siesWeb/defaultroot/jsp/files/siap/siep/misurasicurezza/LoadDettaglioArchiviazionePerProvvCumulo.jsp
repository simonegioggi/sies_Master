<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di caricamento per dettaglio archiviazione per provvedimento di cumulo --%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="java.util.List"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Iterator"%>

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

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="fascicolosiep"       scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<%
FascicoloSiepModel fsmSession = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel pgm = posizioneluogoaltra.getPosizioneGiuridica();
LuogoDetenzioneModel ldm = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel acm = posizioneluogoaltra.getAltraCausa();
ArchiviazioneModel am = archiviazione;
FascicoloSiepModel fsm = fascicolosiep;
if (pgm == null)
	pgm = new PosizioneGiuridicaModel();
if (ldm == null)
    ldm = new LuogoDetenzioneModel();
if (acm == null)
    acm = new AltraCausaModel();
%>
<!-- LoadDettaglioArchiviazionePerProvvCumulo -->
<html>
<head>
  	<title>[S.I.E.S.] - Dettaglio Archiviazione per Assorbimento in Cumulo</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
<%--   	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script> --%>
	<script language="JavaScript">
	
	   function stampaSiep(lAzione)
	   {
	      var  hrefStampa = lAzione;
	      var lIndice = hrefStampa.indexOf("?");
	
	      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	
	      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
	   }	   
	</script>
</head>
<body class="corpo">
  	<table>
    	<tr>
      		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      		<td class="LBG"><font class="label">Funzione:</font>&nbsp;&nbsp;
      			<font class="campo">Dettaglio Archiviazione per Assorbimento in Cumulo</font>
      		</td>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
		|| (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)) {
%>

			<!-- BOTTONE DI STAMPA -->
<%--        		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>"> --%>
<%--          		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaArchiviazione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()%>"/> --%>
<%--        		</jsp:include> --%>
       		<!-- BOTTONE DI STAMPA -->
       		<td class="LBG">
       			<a  href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaArchiviazione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&CodMotivo=<%=eventonotifica.getEvento().getCodMotivo()%>')" onclick="javascript:lookUpload();">
        			 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
       			</a>
     		</td>
     		
     		<!-- BOTTONE DI VALIDAZIONE -->
     		<td class="LBG">
<%--        			<a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadArchiviazionePerProvvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S"> --%>
				<%-- 20190604 [SG]: cambiata gestione validazione provvedimento --%>
				<a onclick="javascript:lookUpload();">
        			<img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
       			</a>
     		</td>
     		
			<!-- ICONA DI MODIFICA -->
			<td class="LBG">
	  			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaArchiviazionePerProvvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
      				<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
      			</a>
			</td>
			<!-- ICONA DI CANCELLAZIONE -->
			<td class="LBG">
	  			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActModificaArchiviazionePerProvvCumulo&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&modalita=D&TornaQui=10">
      				<img align="middle" src="/images/delete24.gif" alt="Elimina" width="24" height="24" border="0">
      			</a>
			</td>
			<%-- 20190918 [SG]: collaudo 11.3 elimino torna indietro --%>
			<!-- ICONA DI TORNA INDIETRO -->
<!-- 			<td class="LBG"> -->
<!-- 				  <a href="javascript:history.go(-1);"> -->
<%-- 				   <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"> --%>
<!-- 				  </a> -->
<!-- 			</td> -->
<%
}
%>
		</tr>
	</table>
	<br>
   	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
  	<table cellspacing=0 cellpadding=0 width="95%">
    	<tr>
      		<td class="l" width="25%">Posizione Giuridica</td>
      		<td class="L" colspan="5">
        		<font class="campo">
<%
if (fsmSession.getFlagAltraCausa() != null && "S".equals(fsmSession.getFlagAltraCausa())) {
%>
					DETENUTO PER ALTRA CAUSA
<%
} else {
%>
					<%=pgm.getDescrPosizioneGiuridica()%>
<%
}
%>
        		</font>
      		</td>
    	</tr>
<%
if (fsmSession.getFlagAltraCausa() != null && "S".equals(fsmSession.getFlagAltraCausa())) {
	if (acm.getIstitutoDetenzione() != null) {
%>
		<tr>
			<td class="l">Detenuto presso</td>
			<td class="L" colspan="5"><font class="campo"><%=acm.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
				di<font class="campo"><%=acm.getIstitutoDetenzione().getDescrComune()%></font>
			</td>
		</tr>
<%
		if (acm.getAltroLuogo() != null) {
%>
		<tr>
			<td class="l">Altro Luogo</td >
			<td class="L" colspan="5">
				<font class="campo"><%=StringUtils.toStringJSP(acm.getAltroLuogo())%></font>&nbsp;
			</td>
		</tr>
<%
		}
	}
} else if (ldm.getIstitutoDetenzione() != null) {
%>
		<tr>
			<td class="l">Detenuto presso</td>
           	<td class="L" colspan="5">
            	<font class="campo"><%=ldm.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=ldm.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
		</tr>
<%
}
// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
if (pgm.getCodPosizioneGiuridica() != null
		&& ("02".equals(pgm.getCodPosizioneGiuridica()) || "04".equals(pgm.getCodPosizioneGiuridica()))) {
	if (ldm.getIstitutoDetenzione() != null) {
%>
		<tr>
			<td class="l">Indirizzo</td>
			<td class="L" colspan="5">
				<font class="campo"><%=StringUtils.toStringJSP(ldm.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
			</td>
		</tr>
<%
	}
}
if (penaresidua.getIdPenaResidua() != null
		&& ((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
				&& !"S".equals(penaresidua.getFlagErgastolo())
				&& !"D".equals(penaresidua.getFlagErgastolo())))) {
%>
		<tr>
<%
    	
	if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0)
			&& (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
	} else {
%>
			<td class="l">Reclusione</td>
          	<td class="l" width="30%">
            	<font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
            	<font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
            	<font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
          	</td>
<%
		if (penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0)) != 0) {
%>
            <td class="l" width="25%">Multa</td>
            <td class="l" colspan="3">
            	<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;
            	<font class="l">Euro</font>
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
	} else {
%>
      		<td class="l">Arresto</td>
      		<td class="l" width="30%">
         		<font class="l">Anni&nbsp;</font>
         		<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         		<font class="l">Mesi&nbsp;</font>
         		<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         		<font class="l">Giorni&nbsp;</font>
         		<font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      		</td>
<%
		if (penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0)) != 0) {
%>
        	<td class="l" width="25%">Ammenda</td>
        	<td class="l" colspan="3">
        		<font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;
        		<font class="l">Euro</font>
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
         	<td class="l">Pena Espiata dal</td>
         	<td class="L">
         		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font>
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
	if ((penaresidua.getFlagErgastolo() == null)
			|| (penaresidua.getFlagErgastolo() != null
			&& !"S".equals(penaresidua.getFlagErgastolo())
			&& !"D".equals(penaresidua.getFlagErgastolo()))) {
    	if (penaresidua.getDataFine() != null) {
            if (penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
%>
            <td class="l">al</td>
            <td class="L" colspan="2">
				<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			</td>
<%
			} else {
%>
			<td class="l">al</td>
			<td class="lRosso" colspan="2">
				<font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM"))%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy"))%></font>
			</td>
<%
			}
		}
	}
%>
   		</tr>
   	</table>
<%
}
%>
<%-- 20190604 [SG]: aggiunta sezione mancante! --%>
<%
List lMisure = (List) request.getAttribute("listaMisure");
if (lMisure != null && lMisure.size() > 0) {
%>
	<!-- Misure di Sicurezza già presenti -->
	<table cellspacing="0" cellpadding="0" width="95%">
<%
	Iterator itx = lMisure.iterator();
	while (itx.hasNext()) {
		MisuraSicurezzaModel lMis = (MisuraSicurezzaModel) itx.next();
 %>
		<tr>
			<td class="L" width="25%">Misura di Sicurezza da espiare</td>
			<td class="L" width="30%"><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%></font></td>
			<td class="L" colspan="4">
				Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(), "0")%>&nbsp;</font>
				Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(), "0")%>&nbsp;</font>
				Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(), "0")%></font>
			</td>
		</tr>
<%
	}
%>
	</table>
<%
}
%>
	<br>
	<table cellspacing=0 cellpadding=0 width="95%">
<%
if ("1154".equals(am.getCodOggettoDefinizione())) {
	if (fsm.getCodUfficioUnione() != null && fsm.getDescrComuneUfficioUnione() != null) {
%>
		<tr>
			<td class="l">Ufficio che ha Emesso il Cumulo</td>
          	<td class="L" colspan="3">
            	<font class="campo"> <%=StringUtils.toStringJSP(fsm.getDescrTipoUfficioUnione())%></font>&nbsp;di
            	<font class="campo"><%=StringUtils.toStringJSP(fsm.getDescrComuneUfficioUnione())%></font>
          	</td>
		</tr>
<%
	}
// 20190604 [SG]: aggiunta sezione mancante!
} else {
%>
		<tr>
		    <td class="l">Ufficio Emittente</td>
		    <td class="L" colspan='3'>
				<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrUfficioEmittente())%></font>
		     	di <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrLuogoEmittente()) %></font>
		    </td>
		</tr>
<%
}
if (magistrato != null) {
%>
    	<tr>
     		<td class="l">Magistrato Firmatario</td>
     		<td class="L" colspan="3">
         		<font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         		<font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
     		</td>
   		</tr>
<%
}
if (am.getDataDefinizione() != null) {
%>
		<tr>
          	<td class="l">Data Definizione</td>
          	<td class="L" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(am.getDataDefinizione(), "dd-MM-yyyy"))%></font>
          	</td>
		</tr>
<%
}
%>
		<tr>
<%
if (eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataEmissione() != null) {
%>
			<td class="l" width="25%">Data Emissione</td>
       		<td class="L" width="30%">
          		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy"))%></font>
         	</td>
         	<td class="l" width="25%">Data Trasmissione</td>
         	<td class="L">
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null
		&& eventonotifica.getNotifiche().length > 0 && eventonotifica.getNotifiche()[0] != null
		&& eventonotifica.getNotifiche()[0].getDataInvio() != null) {
%>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(), "dd-MM-yyyy"))%></font>
<%
} else if (eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataTrasmissioneAtti() != null) {
%>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "dd-MM-yyyy"))%></font>
<%
}
%>
			</td>
   		</tr>
<%
if (am.getCodOggettoDefinizione() != null) {
%>
       	<tr>
          	<td class="l">Oggetto Definizione</td>
        	<td class="l" colspan="3">
          		<font class="campo"><%=StringUtils.toStringJSP(am.getDescrOggettoDefinizione())%></font>
       		</td>
		</tr>
<%
}
if (fsm.getDataUnione() != null) {
%>
		<tr>
			<td class="l">Data Provvedimento di Cumulo</td>
          	<td class="L" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fsm.getDataUnione(), "dd-MM-yyyy") )%></font>
          	</td>
		</tr>
<%
}
if (fsm.getNumFascicoloUnione() != null && fsm.getAnnoFascicoloUnione() != null) {
%>
		<tr>
         	<td class="l">Numero Procedimento SIEP</td>
         	<td class="l" colspan="3">
          		<font class="campo"><%=StringUtils.toStringJSP(fsm.getAnnoFascicoloUnione())%></font>/
          		<font class="campo"><%=StringUtils.toStringJSP(fsm.getNumFascicoloUnione())%></font>
        	</td>
		</tr>
<%
}
if (am.getNote() != null) {
%>
		<tr>
          	<td class="l">Note</td>
          	<td class="L" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(am.getNote())%></font>
          	</td>
		</tr>
<%
}
if (eventonotifica != null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	Iterator<NotificaModel> iter = (Arrays.asList(eventonotifica.getNotifiche())).iterator();
    while (iter.hasNext()) {
		NotificaModel lNotMod = (NotificaModel) iter.next();
       	if (lNotMod != null && lNotMod.getUffCodUfficio() != null && "E".equals(lNotMod.getCodTipoNotifica())) {
%>
		<tr>
         	<td class="l">Ufficio Recupero Crediti presso</td>
         	<td class="l" colspan="3">
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrTipoUfficio())%></font>&nbsp;di&nbsp;
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
         	</td>
		</tr>
<%
		}
       	if (lNotMod != null && lNotMod.getAutoritaEsterna() != null && "N".equals(lNotMod.getCodTipoNotifica())) {
%>
		<tr>
         	<td class="l">Autorità di Polizia</td>
         	<td class="l" colspan="3">
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;di&nbsp;
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
         	</td>
		</tr>
<%
         	if (lNotMod.getNote() != null) {
%>
		<tr>
         	<td class="l">Indirizzo</td>
         	<td class="l" colspan="3">
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
         	</td>
		</tr>
<%
			}
		}
        if (lNotMod != null && lNotMod.getUfficio() != null && "TDS".equals(lNotMod.getUfficio().getCodTipoUfficio())) {
%>
        <tr>
         	<td class="l">Tribunale di Sorveglianza</td>
         	<td class="l" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
         	</td>
		</tr>
<%
       	}
       	if (lNotMod != null && lNotMod.getUfficio() != null && "UDS".equals(lNotMod.getUfficio().getCodTipoUfficio())) {
%>
        <tr>
        	<td class="l">Magistrato di Sorveglianza</td>
         	<td class="l" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%></font>
         	</td>
        </tr>
<%
		}
       	if (lNotMod != null && lNotMod.getAutoritaEsterna() != null && "C".equals(lNotMod.getCodTipoNotifica())) {
%>
        <tr>
         	<td class="l">Altra Autorità</td>
         	<td class="l" colspan="3">
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;di&nbsp;
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
         	</td>
		</tr>
<%
			if(lNotMod.getNote() != null) {
%>
        <tr>
         	<td class="l">Indirizzo</td>
         	<td class="l" colspan="3">
           		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
         	</td>
        </tr>
<%
			}
		}
       	if (lNotMod != null && "C".equals(lNotMod.getCodTipoNotifica()) && lNotMod.getNote() != null
       			&& lNotMod.getAutoritaEsterna() == null) {
%>
        <tr>
         	<td class="l">Altra Autorità</td>
         	<td class="l" colspan="3">
            	<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
         	</td>
        </tr>
<%
       	}
	}
}
%>
	</table>
 	<br>
  	<div id="upld" align="left" style="visibility:hidden">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
	<table>
		<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
        <tr>
          	<td class="L">
            	<input class="bottone" type="submit" value="Conferma">
            	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadArchiviazionePerProvvCumulo">
            	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
            	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvCumulo">
          	</td>
		</tr>
	</table>
	</form>
	</div>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& "S".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>
	<div id="restituzione" align="left" style="position:relative; top: -111px;">
	<form method="POST" name="restituzione" action="<%=IWebConstants.PG_MAIN%>">
	<table>
        <tr>
          	<td class="L">
            	<input class="bottone" type="submit" name="restituzione" value="Restituzione Comunicazione Ordine di Consegna">
            	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadInserisciRestituzioneOrdineConsegna">
          	</td>
		</tr>
	</table>
	</form>
	</div>
<%
}
%>
</body>
</html>