<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sius.util.SIUSLookupRemote"%>
<%@ page import="siap.sius.avvocato.controller.IAvvocato"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<%
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fascicolo"       scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="UtenteConnesso"  scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>

<%
SoggettoModel soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
// Se fascicolo modificabile, verifico se gli avvocati associti al procedimento appartengono a fori soppressi
boolean isFascicoloModificabile = true;
if (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") == 0
		|| fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("04") == 0
		|| fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") == 0
		|| fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("99") == 0
		|| UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio()) !=0) {
	isFascicoloModificabile = false;  
}

boolean isAvvocatoForoSoppresso = false;
String strAlertAvvocato = "";

if (isFascicoloModificabile) {
	Vector <AvvocatoSiusModel> listaAvvocati = null;
  	try { 
    	IAvvocato lCtrlAvv = SIUSLookupRemote.getAvvocatoRemote();
    	listaAvvocati = lCtrlAvv.ExRicercaAvvocatiByFascicoloNoError(fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius());
 		Collection listaFori = DecodificheManager.getInstance().getForoAll();
   		int contaSoppressi = 0;
    	if (listaAvvocati != null) {
      		for (int i = 0; i < listaAvvocati.size(); i++) {
        		AvvocatoModel lAvvocatoModel = listaAvvocati.elementAt(i).getAvvocato();
        		String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocatoModel.getForo());
        		if ("SOPPRESSO".equals(lStatoForo)) {
          			isAvvocatoForoSoppresso = true;
          			contaSoppressi++;
          			strAlertAvvocato += " L’Avvocato " + StringUtils.toStringJSP(lAvvocatoModel.getCognome()) + " "
						+ StringUtils.toStringJSP(lAvvocatoModel.getNome())
						+ " risulta iscritto al Foro di "
						+ StringUtils.toStringJSP(lAvvocatoModel.getForo())
						+ " soppresso a seguito dell’accorpamento degli uffici giudiziari.";
        		}
      		}
    	}
    	if (isAvvocatoForoSoppresso) {
      		if (contaSoppressi == 1)
        		strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
      		else
        		strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
    	} 
	} catch(Exception e) {
	    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.error("Exception",e);
  	}
}
%>

<table cellspacing="2" cellpadding="2" width="95%">
	<tr>
		<td class="L">
			<font class="label">Procedimento N.</font>
        	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>">
          	<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno()%>
          	/
          	<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr()%>
        	</a>
<%
if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio()) == 0)) {
%>
			&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrComuneUfficio()%>&nbsp;-&nbsp;
<%
}
%>
       		&nbsp;<font class="label"> relativo a: </font>
        	<font class="campo"><%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
		</td>
    </tr>
    <tr>
      	<td class="L" width=80%>
      		<font class="label">Soggetto:</font>
      		<font class="campo">
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%><%=retParam%>">
          			<%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        		</a>
      		</font>&nbsp;
<%
if (soggetto.getDataNascita() != null) {
	if (soggetto.getSesso().compareTo("F") == 0) {
%>
			<font class="label">nata il :</font>&nbsp;
<%
	} else {
%>
          	<font class="label">nato il :</font>&nbsp;
<%
	}
%>
      		<font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
<%
	} else if (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi() != null) {
%>		
			<font class="label">Età Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null) {
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null) {
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
	}
%>   
			<font class="label">in : </font>
			<font class="campo">
<%
	if (soggetto.getDescrComuneNascita().compareTo("-") == 0) {
%>
				<%=soggetto.getDescrStatoNascita()%>
<%
	} else {
%>
        		<%=soggetto.getDescrComuneNascita()+ " ("+soggetto.getCodProvinciaNascita()+")"%>
<%
	}
%>
			</font>
     	</td>
	</tr>
<%
	if (fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP() != null) {
%>
    <tr>
		<td class="L">
        	<font class="label">Procedimento SIEP </font>
        	<font class="campo">
<%
		if (fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
%>
            	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%><%=retParam%>">
              		<%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>
            	</a>
            	&nbsp;&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>
<%
		} else {
%>
				-&nbsp;
<%
		}
%>
        	</font>&nbsp;
<%
		if (fascicolo.getDataInserimento() != null) {
%>
          	<font class="label"> del </font>&nbsp;
          	<font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
<%
		}
%>
		</td>
    </tr>
<%
	}
%>
	<tr>
     	<td class="L">
	        <font class="label">Data Udienza : </font>
	        <font class="campo">
<%
	if (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("10") == 0) {
%>
          		<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrStatoFascicolo()%>
<%
	} else {
%>
          		<%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-")%>
<%
	}
%>
			</font>
<%
	if (fascicoloSiusGP.getUdiPro() != null
			&& fascicoloSiusGP.getUdiPro().getFlagRinviata() != null
			&& fascicoloSiusGP.getUdiPro().getFlagRinviata().equalsIgnoreCase("P")) {
%>
          	<font class="cRosso">  ( Prefissata ) </font>
<%
	}
%>
		</td>
    </tr>
<%
	//==========================================================================
	// Test per segnalare che uno o più avvocati associati al Procedimento risultano
	// iscritti a Fori Soppressi.
	//==========================================================================
	if (isAvvocatoForoSoppresso) {
%>
	<tr>
		<td class="cRosso">
        <blink>Attenzione!!</blink> <%=strAlertAvvocato%>
      </td>
    </tr>
    <% } %>  
</table>
<script type="text/javascript">
function blink() {
	var blinks = document.getElementsByTagName('blink');
  	for (var i = blinks.length - 1; i >= 0; i--) {
    	var s = blinks[i];
    	s.style.visibility = (s.style.visibility === 'visible') ? 'hidden' : 'visible';
  	}
  	window.setTimeout(blink, 500);
}
if (document.addEventListener)
	document.addEventListener("DOMContentLoaded", blink, false);
else if (window.addEventListener)
	window.addEventListener("load", blink, false);
else if (window.attachEvent)
	window.attachEvent("onload", blink);
else
	window.onload = blink;
</script>