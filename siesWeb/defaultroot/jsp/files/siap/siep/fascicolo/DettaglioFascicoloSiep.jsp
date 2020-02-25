<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.riepilogoprovvedimento.model.RiepilogoProvvedimentoModel" %>
<%@ page import="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.calcolopena.model.CalcoloPenaModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.sico.util.SICOLookupRemote" %>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche" %>
<%@ page import="siap.sico.decodifiche.model.ComuneModel" %>
<%@ page import="siap.sico.decodifiche.controller.IComune" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>

<jsp:useBean id="dettagliofascicolo" 		scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="lTipoFunzione"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="penacumulo"              	scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel" />
<jsp:useBean id="ulteriorisanzionicumulo" 	scope="request" class="java.util.Vector" />
<jsp:useBean id="continuazioni"           	scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="fascicoloCollMod" 			scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="richiestaconversione" 		scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel" />
<jsp:useBean id="vectfasc" 					scope="request" class="java.util.Vector" />
<jsp:useBean id="annotazioneMan" 			scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />
<jsp:useBean id="NuoFascMod" 				scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lRegistroIstanze"      	scope="request" class="java.lang.String"/>
<jsp:useBean id="da_classe_III"      		scope="request" class="java.lang.String"/>
<jsp:useBean id="dataEmissioneCertificato" 	scope="request" class="java.util.Date" />
<jsp:useBean id="certificatoPenale"        	scope="request" class="java.lang.String" />
<%-- MEV_39: aggiunti useBean --%>
<jsp:useBean id="dsc"        				scope="request" class="java.lang.String" />
<jsp:useBean id="gr"        				scope="request" class="java.lang.String" />

<%
//==============================================================================
//            jsp di visualizzazione del dettaglio del fascicolo
//==============================================================================
%>

<%
FascicoloSiepModel lFascicolo = dettagliofascicolo.getFascicoloSiep();
%>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Dettaglio Procedimento </title>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%
// lTipoFunzione valorizzato solo si proviene da iscrizione guidata
if (!"".equals(lTipoFunzione) && !"ritornodettaglio".equals(lTipoFunzione)) {
%>
<script language="JavaScript">
var aForm = null;
function Verify() {
	alert("La funzione di Iscrizione Guidata è stata Interrotta");
	aForm = document.getElementById("Abbandona");
	Disabilita();
}

function Disabilita() {
	if (aForm == null)
    	aForm = document.getElementById("Dif");
 	document.Abbandona.A.disabled = true;
 	document.Dif.D.disabled = true;
	aForm.submit();
}
</script>
<%
}
%>

<%
//==============================================================================
// Aggiungo js per gestire la sezione espandi (solo se non iscrizione guidata)
//lTipoFunzione valorizzato solo si proviene da iscrizione guidata
//==============================================================================
if ("".equals(lTipoFunzione)) {
%>
<script language="JavaScript">
var vedo = false;
// Per i fascicoli di classe IV il default è mostrare tutti i dettagli.
var classe = <%=lFascicolo.getChiaveProgr().intValue()%>;
if (classe >= 40000 && classe <  50000)
	vedo = true;
var node;
function espandi() {
	node = document.getElementById('elenco');
	if (vedo) {
		node.style.visibility='hidden';
		vedo=false;
		node=document.getElementById('vedi');
		node.value="Espandi";
  	} else {
	    node.style.visibility='visible';
	    vedo=true;
	    node=document.getElementById('vedi');
	    node.value="Nascondi";
  	}
}

//==========================================================================
// Determina i quantum di pena residui alla data di systema
//==========================================================================
function CalcoloResiduoPena() {
	var calcoloURL = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActCalcolaPenaResiduaAl";
	desktop = window.open(calcoloURL, "Pena_Residua_Al", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=no, resizable=no, width=400, height=200, location=no");
}

function disattiva(a_action, a_entityname, a_entityvalue) {
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue;
	if (window.confirm('Disattivare Etichetta Minorenne?')) {
		window.location.href=str;
	}
}
</script>
<%
}

if (siap.util.SIESSwitch.isRegeSiesOn()) {
%>
<script language="JavaScript">
var desktop;
function ShowDispositivo() {
	var posx = screen.width/2 -150;
	var posy = screen.height/2-150;
	//alert(posx +" - " + posy);
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notefascicolo.action.ActDettaglioDispositivo&note=<%=dettagliofascicolo.getNoteFascicolo()%>","DettaglioDispositivo","toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=300,height=200,left="+posx+",top="+posy+"");
}
</script>
<%
}
%>

<script language="JavaScript">
function VisualizzaCertificato(lAzione){
	var hrefStampa = lAzione;
	var lIndice = hrefStampa.indexOf("?");
	var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	stampa2("/jsp/files/Stampa.jsp", parametri);
}
</script>
</head>

<body class="corpo">
<form name="comandi">
<input type="hidden" name="isSentenza" value="true" />
<table>
  	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
		<td class="LBG">
 <%
 if (lRegistroIstanze != null && lRegistroIstanze.equals("S")) {
 %>         
			<font class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Registro Istanza</font>
<%
} else {
%>
            <font class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Procedimento</font>
 
<%
}
%>   
		</td>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--
        	<td class="LBG">
          		<jsp:include page="IWebConstants.PG_TOOLBAR_HEADER>" />
         	</td>
         --%>
<%
// lTipoFunzione per capire che si proviene da iscrizione guidata
if ("".equals(lTipoFunzione)) {
	if (lRegistroIstanze != null && lRegistroIstanze.equals("S")) {
%>
		<td class="LBG">
	        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_REGISTRO_ISTANZE%>">
	           	<jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
	           	<jsp:param name="ValoreIdEntita" value="<%=lFascicolo.getIdFascicoloSiep()%>" />
	           	<jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
	        </jsp:include>
      	</td>   
<%
	} else {
%>
		<td class="LBG">
            <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
               <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
               <jsp:param name="ValoreIdEntita" value="<%=lFascicolo.getIdFascicoloSiep()%>" />
               <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
            </jsp:include>
		</td>   
<%
	} 
}
if (request.getParameter("TornaQui") == null  && request.getParameter("StoTornando") == null) {
	if ((request.getParameter("NomeAzione") == null)
			|| (request.getParameter("NomeAzione") != null
                    && !request.getParameter("NomeAzione").equals("siap.siep.fascicolo.action.ActInserisciFascicolo"))) {
%>
		<td class="LBG">
			<a href="javascript:history.go(-1);">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
		</td>
<%
	}
} else {
%>
      	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
<%
}
%>
	</tr>
</table>
</form>
<br>
<%
if (lFascicolo != null && certificatoPenale != null && certificatoPenale.equals("SI")) {
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
    	<td class="rNoBord">  
      		<font class="campo">
        		<input type="button" name="VisualizzaCertificato" value="Visualizza Certificato Penale del <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissioneCertificato,"dd-MM-yyyy"))%>" onClick="javascript:VisualizzaCertificato('/jsp/Main.jsp?Action=siap.sico.webservice.action.ActLoadCertificatoCasellarioGiudiziale&IDFascicolo=<%=lFascicolo.getIdFascicoloSiep()%>&TipoFascicolo=SIEP')">
	      	</font>
	    </td>
	</tr>
</table>
<%
}
if (lRegistroIstanze != null && lRegistroIstanze.equals("S")) {
%>         
<jsp:include page="/jsp/files/siap/siep/nuovaistanza/DettaglioFascicoloRegistroIstanza.jsp"/>
<%
} else {
%>
	<%-- MEV_39: aggiunto controllo e dicitura se fasc è di classe IV --%>
<%
	int classe = lFascicolo.getChiaveProgr().intValue();
    if (classe >= 40000 && classe < 50000) {
    	if (Utils.isPresent(dsc) && Utils.isPresent(gr)) {
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="C">
  			<font class="label">Data Scadenza Comunicazione: </font>
  			<font class="campo"><%=dsc%></font>&nbsp;
			<font class="label">N° Giorni Residui: </font>
			<font class="campo"><%=gr%></font>
   		</td>
   	</tr>
</table>
<br>
<%
    	}
    }
%>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaCompleto.jsp"/>
<%
}
%>
<br>
<%
//==============================================================================
// Se da iscrizione guidata visualizzo i bottoni PROSEGUI e ABBANDONA
//==============================================================================
if (!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio")) {
%>
<table cellspacing="2" cellpadding="2">
  	<tr>
    	<td class="lNoBord" colspan="2">
      		<FORM method="POST" name="Dif" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadInserisciAvvocato&lTipoFunzione=<%=lTipoFunzione%>">
        		<br><INPUT class="bottone" type="button" name="D" value="Prosegui" onclick="Javascript:Disabilita();">
      		</FORM>
    	</td>
    	<td class="lNoBord" colspan="2">
      		<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicolo.getIdFascicoloSiep()%>&lTipoFunzione=ritornodettaglio">
        		<br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
      		</FORM>
    	</td>
  	</tr>
</table>
<%
}

//************************************//
// 1.ANNA per Pene Sospese gestione classe III  
//************************************//
FascicoloSiepModel fascicolo = dettagliofascicolo.getFascicoloSiep();
if (fascicolo.getCodStatoFascicolo() != null
		&& (fascicolo.getCodStatoFascicolo().equals("01"))
		&& (fascicolo.getCodMotivoArchiviazione().equals("12"))
		&& (fascicolo.getChiaveProgr().intValue() >= 30000 && fascicolo.getChiaveProgr().intValue() < 40000)) {
	String strDescrUfficio = "";
	String strDescrComune = "";
%>
<table cellspacing="0" cellpadding="0" width="95%">
 	<tr>
   		<td class="l" width="18%"><font class="label">Beneficio Revocato con: </font></td>
<%
	if (annotazioneMan != null && annotazioneMan.getIdAnnotazioneManuale() != null) {
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
		Collection Uffi_Emi = lDecodifiche.ExRicercaDecodifiche(lModel);
		//String strDescrUfficio = "";
		java.util.Iterator itxOggetto = Uffi_Emi.iterator();
		while (itxOggetto.hasNext()) {
   			DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
   			if (lDecMod.getCode().equals(annotazioneMan.getCodTipoUfficioSiep()))
		   		strDescrUfficio += lDecMod.getDescription();
		}
		IComune comctrl = SICOLookupRemote.getComuneRemote();
		ComuneModel comunemod = comctrl.ExRicercaComuneByKey(annotazioneMan.getCodLuogoUfficioSiep());
		strDescrComune = comunemod.getDescrizione();
  		if (annotazioneMan.getAnnoSentenzaSiap() != null && annotazioneMan.getNumeroSentenzaSiap() != null) {
%>
		<td class="l">
		  	<font class="campo">
		  		sentenza n°&nbsp;<%=annotazioneMan.getAnnoSentenzaSiap()%>/<%=annotazioneMan.getNumeroSentenzaSiap()%>
				&nbsp;<%=strDescrUfficio%>&nbsp;di&nbsp;<%=strDescrComune%>
			</font>
		</td>
	</tr>
    <tr>
		<td class="l"><font class="label"></font></td>
      	<td class="l">Procedimento iscritto al n°&nbsp;
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=NuoFascMod.getIdFascicoloSiep()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(NuoFascMod.getChiaveAnno())%>/<%=StringUtils.toStringJSP(NuoFascMod.getChiaveProgr())%>
			</a>
      	</td>
	</tr>
<%
		} else {
%>
		<td class="l">
			<font class="campo">
				ordinanza n°&nbsp;<%=annotazioneMan.getAnnoGe()%>/<%=annotazioneMan.getNumeroGe()%>
            	&nbsp;<%=strDescrUfficio%>&nbsp;di&nbsp;<%=strDescrComune%>
            </font>
  		</td>
   	</tr>
    <tr>
		<td class="l" width="20%"><font class="label"></font></td>
    	<td class="l">Procedimento iscritto al n°&nbsp;
     		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=NuoFascMod.getIdFascicoloSiep()%>" title="Procedimento">
       			<%=StringUtils.toStringJSP(NuoFascMod.getChiaveAnno())%>/<%=StringUtils.toStringJSP(NuoFascMod.getChiaveProgr())%>
     		</a>
       	</td>
	</tr>
<%
		}
	}
%>
	</tr>
</table>
<%
}

//************************************//
// 1.ANNA per Pene Sospese gestione classe I collegata a classe III  
//************************************//
if (da_classe_III != null)
	if (da_classe_III.equals("SI")) {
		String strDescrUfficio="";
        String strDescrComune="";
%>
<table cellspacing="0" cellpadding="0" width="95%">
	<tr>
		<td class="l" width="18%"><font class="label">Benefici:</font></td>
		<td class="l">Pena Sospesa n°&nbsp;
			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=NuoFascMod.getIdFascicoloSiep()%>" title="Procedimento">
				<%=StringUtils.toStringJSP(NuoFascMod.getChiaveAnno())%>/<%=StringUtils.toStringJSP(NuoFascMod.getChiaveProgr())%>&nbsp;SIEP
		    </a>
      	</td>
	</tr>
<%
		if (annotazioneMan != null && annotazioneMan.getIdAnnotazioneManuale() != null) {
	  	  	    DecodificheModel lModel = new DecodificheModel();
		  	    IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		  	    lModel.setContesto("TIPO_UFFICIO_EMITTENTE");
		  	    Collection Uffi_Emi = lDecodifiche.ExRicercaDecodifiche(lModel);
		  	    //String strDescrUfficio ="";
		  	    java.util.Iterator itxOggetto = Uffi_Emi.iterator();
		  	    while(itxOggetto.hasNext())
		  	    {
		  	       DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
		  	       if(lDecMod.getCode().equals(annotazioneMan.getCodTipoUfficioSiep()))
		  	    		   strDescrUfficio += lDecMod.getDescription();
		  	    }
		  	    
		  	    
		  	    IComune comctrl=SICOLookupRemote.getComuneRemote();
		  	    ComuneModel comunemod=comctrl.ExRicercaComuneByKey(annotazioneMan.getCodLuogoUfficioSiep());
		  	    strDescrComune=comunemod.getDescrizione();
        	  	if (annotazioneMan.getAnnoSentenzaSiap()!=null && annotazioneMan.getNumeroSentenzaSiap()!=null){
          %>
	        <tr>
	          	<td class="l"><font class="label">Beneficio Revocato con:</font>        
	          	</td>
	          	<td class="l">
		            <font class="campo"> sentenza n°
		            <%=annotazioneMan.getAnnoSentenzaSiap()%> / <%=annotazioneMan.getNumeroSentenzaSiap()%>
		            &nbsp; <%=strDescrUfficio%> di <%=strDescrComune%></font>
	          	</td>
	        </tr>
	           <%}else{ %>
	        <tr>
	          	<td class="l"><font class="label">Beneficio Revocato con:</font>        
	          	</td>
          		<td class="l">
<%-- 20190723 [SG]: modificata gestione ordinanza --%>
<%
if (annotazioneMan.getAnnoGe() != null && annotazioneMan.getNumeroGe() != null) {
%>
		            <font class="campo"> ordinanza n°
		            <%=annotazioneMan.getAnnoGe()%>/<%=annotazioneMan.getNumeroGe()%></font>
<%
} else {
%>
					<font class="campo"> ordinanza</font>
<%
}
%>
		            &nbsp; <%=strDescrUfficio%> di <%=strDescrComune%>
          		</td>
        	</tr>
	           <%} %>
           <%} %>
        
        </table>
        	<% 
        	
        }
%>


<%
//==============================================================================
// SEZIONE ESPANDI CONTENENTE LE SEGUENTI INFORMAZIONI:
// - RESIDENZA
// - DOMICILIO
// - REATI
// - SANZIONE SOSTITUTIVA
// - Aggravanti soggettive/Attenuanti
// - Sentenza di applicazione pena
// - PENE ACCESSORIE
// - BENEFICI
// - REVOCA BENEFICI
// - MISURE CAUTELARI
// - MAGISTRATO ASSEGNATARIO
// - MISURE SICUREZZA
// - ULTERIORI SANZIONI IN CUMULO
// - Riepilogo generale dei totali delle quantità componenti la pena relevate su RES
//==============================================================================
// lTipoFunzione valorizzato solo si proviene da iscrizione guidata
%>
<%if(lTipoFunzione.equals("")) 
{
	// Per i fascicoli di classe IV il default è mostrare tutti i dettagli.
    if(dettagliofascicolo.getFascicoloSiep().getChiaveProgr().intValue() >= 40000  && 
 	   dettagliofascicolo.getFascicoloSiep().getChiaveProgr().intValue() < 50000)
    { %>
		<input type="button" name="vedi" value="Nascondi" onClick="javascript:espandi();">
		  <div id="elenco" style="visibility:visible; width:100%;">
<%  } else { %>
		<input type="button" name="vedi" value="Espandi" onClick="javascript:espandi();">
		  <div id="elenco" style="visibility:hidden; width:100%;">
<% } %>
       
<%
//==========================================================================
//                   Sezione con i dati della Residenza/Domicilio
//==========================================================================
%>
<table style="width: 95%;  border: 0;">
<%
if(dettagliofascicolo.getResidenza()!= null && dettagliofascicolo.getDomicilio()!= null) {%>
  <tr>
    <td class="Titolo"  colspan="3">Residenza / Domicilio</td>
  </tr>
<%}%>

<%
//==============================================================================
//                               RESIDENZA
//==============================================================================
ResidenzaModel lResidenza = dettagliofascicolo.getResidenza();
if(lResidenza != null)
{
%>
        <tr>
          <td class="l"><font class="label">Residenza: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>&nbsp;
<%
              String lStato = lResidenza.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA"))
              {
%>               <%=StringUtils.toStringJSP(lResidenza.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lResidenza.getCodProvincia())%>)
<%
              }
              else
              {
%>              <%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>
                  -
                <%=StringUtils.toStringJSP(lStato)%>
<%            }%>
            </font>
          </td>
        </tr>
<%
 }  // end Residenza
%>


<%
//==============================================================================
//                                 DOMICILIO
//==============================================================================
ResidenzaModel lDomicilio = dettagliofascicolo.getDomicilio();
if(lDomicilio != null)
{
%>
        <tr>
          <td class="l"><font class="label">Domicilio: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lDomicilio.getIndirizzo())%>&nbsp;
<%
              String lStato = lDomicilio.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA"))
              {
%>
                <%=StringUtils.toStringJSP(lDomicilio.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lDomicilio.getCodProvincia())%>)
<%
              }
              else
              {
%>              <%=StringUtils.toStringJSP(lDomicilio.getDescComuneEstero())%> -
                <%=StringUtils.toStringJSP(lStato)%>
<%
              }
%>
            </font>
          </td>
        </tr>
<%
}  // end Domicilio
%>
</table>
    
<% 
//==========================================================================
//                               REATI
//==========================================================================
List lReatiCirostanze = dettagliofascicolo.getReatiCircostanze();
if(lReatiCirostanze != null && lReatiCirostanze.size() != 0)
{
%>
    <table cellspacing="1" cellpadding="1" width="95%">
      <tr>
        <td class="Titolo">Reati</td>
      </tr>
<%
      Iterator lIterReati = lReatiCirostanze.iterator();
      while(lIterReati.hasNext())
      {
        ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
        ReatoModel lReato = lReatoCircostanza.getReato();
        ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

        boolean lFlagAnnoNumero = false;
        if(   lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals("")
           && lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("") 
          )
        {
          lFlagAnnoNumero = true;
        }
%>
        <tr>
          <td class="l">
<%
          if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
          {%>
            <font class="label">
            <%out.println("Reato " + lReato.getProgrNumeroManuale()+": ");%>
            </font>
          <%
          } 
          else 
          {
            out.println("Reato " + lReato.getProgrReato()+": ");
          }
          %>
          
          <font class="campo">
          <%
            if(lFlagAnnoNumero)
            {
              if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                out.println(lReato.getDescrFonte()+" ");
              if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
                out.println(lReato.getAnnoFonte());
              if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
                out.println("/"+lReato.getNumeroFonte());
            }

            if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
              out.println("art."+lReato.getArticolo());
            if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
              out.println(" "+lReato.getDescrSottonumerazione());

            if(!lFlagAnnoNumero)
            {
              if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                out.println(lReato.getDescrFonte());
            }

            if(lReato.getComma() != null && !lReato.getComma().equals(""))
              out.println(" c. "+lReato.getComma());
            //**************************************************************************************************
            //Federica - a9-rr-078
            //aggiunto campo Comma-Qualificante 
            if(lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("") && !lReato.getDescrCommaQualificante().equals("-"))
              out.println(" "+lReato.getDescrCommaQualificante());
            //**************************************************************************************************
            
            if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
              out.println(" l. "+lReato.getLettera());
            if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
              out.println(" n. "+lReato.getNumero());

            //CIRCOSTANZE
            if(lCircostanze != null)
            {
              ReatoModel lCirc = null;
              for(int i=0; i<lCircostanze.length; i++)
              {
                lCirc = lCircostanze[i];
%>
                      ,
<%
                boolean lFlagAnnoNumeroCirc = false;
                if( lCirc.getAnnoFonte() != null
                    && !lCirc.getAnnoFonte().equals("")
                    && lCirc.getNumeroFonte() != null
                    && !lCirc.getNumeroFonte().equals("") )
                {
                  lFlagAnnoNumeroCirc = true;
                }
                if(lFlagAnnoNumeroCirc)
                {
                  if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                    out.println(lCirc.getDescrFonte()+" ");
                  if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
                    out.println(lCirc.getAnnoFonte());
                  if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                    out.println("/"+lCirc.getNumeroFonte());
                }

                if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
                  out.println("art."+lCirc.getArticolo());
                if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
                  out.println(" "+lCirc.getDescrSottonumerazione());

                if(!lFlagAnnoNumeroCirc)
                {
                  if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                    out.println(lCirc.getDescrFonte());
                }

                if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
                  out.println(" c. "+lCirc.getComma());
                //**************************************************************************************************
                //Federica - a9-rr-078
                //aggiunto campo Comma-Qualificante 
                if(lCirc.getDescrCommaQualificante() != null && !lCirc.getDescrCommaQualificante().equals("") && !lCirc.getDescrCommaQualificante().equals("-"))
                  out.println(" "+lCirc.getDescrCommaQualificante());
                //**************************************************************************************************
                
                if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
                  out.println(" l. "+lCirc.getLettera());
                if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
                  out.println(" n. "+lCirc.getNumero());
              }
            } // end if CIRCOSTANZE
%>
                </font>
        <%

        if(lReato.getStringaConsumazione()!= null) { %>
          <!--  <font class="label">Data</font> -->
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
        <% }

        if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
          <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
        <% } 

        if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
          <font class="label">Luogo</font>&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
        <% }
        
        %>
          </td>
        </tr>
<%
        } // end While sui reati
%>
</table>
        
      
<%
//==============================================================================
// Continuazione reati
//==============================================================================
if (continuazioni.size() > 0) 
{
%>
<table cellspacing="1" cellpadding="1" width="95%">
  <tr>
    <td class="titolo">Continuazione Reati</td>
  </tr>
<%
		String strCont = "";
		String progReatoCont = "";
		String tipoCont = "";
		Collection coll = continuazioni.values();
		Iterator itxColl = coll.iterator();
		int conta = 0;

		Vector vectCont = new Vector();

		while (itxColl.hasNext()) {
			vectCont = (Vector)itxColl.next();
			Iterator itxVect = vectCont.iterator();
			conta = 0;
			%>
			<tr><td class="l">
			<%
			while (itxVect.hasNext()) {

				strCont = (String)itxVect.next();
				String[] arrStr = strCont.split("@!");

				if (conta == 0) {

					if (strCont.equalsIgnoreCase("C2"))
						tipoCont = "CONTINUAZIONE";
					else if (strCont.equalsIgnoreCase("C1"))
						tipoCont = "CONCORSO FORMALE";

					%><%=tipoCont%> tra i reati di cui ai nr. <%
				}
				else {
					%><font class="campo"><%=StringUtils.toStringJSP(arrStr[1])%> </font><%
					progReatoCont = arrStr[0];
				}

				conta++;
			}
			%>
			</td></tr>
			  <%
		}
		%>
		</table>
		<%
	}
    %>

<%
}  // END REATI
%>



<%
//========================================================================
//                  Aggravanti soggettive/Attenuanti
//========================================================================
List lCirostanze = dettagliofascicolo.getCircostanze();
if(lCirostanze != null && lCirostanze.size() != 0)
{
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td class="Titolo">Aggravanti soggettive/Attenuanti</td>
    </tr>
    <%
      Iterator lIterCircostanze = lCirostanze.iterator();
      while(lIterCircostanze.hasNext())
      {
        CircostanzaModel lCircostanza = (CircostanzaModel)lIterCircostanze.next();
        boolean lFlagAnnoNumero = false;
        if( lCircostanza.getAnnoFonte() != null
            && !lCircostanza.getAnnoFonte().equals("")
            && lCircostanza.getNumeroFonte() != null
            && !lCircostanza.getNumeroFonte().equals("") )
        {
          lFlagAnnoNumero = true;
        }
%>
    <tr>
      <td class="l">
        <font class="campo">
<%
                  //REATO
                  if(lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte()+" ");
                    if(lCircostanza.getAnnoFonte() != null && !lCircostanza.getAnnoFonte().equals(""))
                      out.println(lCircostanza.getAnnoFonte());
                    if(lCircostanza.getNumeroFonte() != null && !lCircostanza.getNumeroFonte().equals(""))
                      out.println("/"+lCircostanza.getNumeroFonte());
                  }

                  if(lCircostanza.getArticolo() != null && !lCircostanza.getArticolo().equals(""))
                    out.println("art."+lCircostanza.getArticolo());
                  if(lCircostanza.getDescrSottonumerazione() != null && !lCircostanza.getDescrSottonumerazione().equals("") && !lCircostanza.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lCircostanza.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte());
                  }

                  if(lCircostanza.getComma() != null && !lCircostanza.getComma().equals(""))
                    out.println(" c. "+lCircostanza.getComma());
                  //**************************************************************************************************
                  //Federica - a9-rr-078
                  //aggiunto campo Comma-Qualificante 
                  if(lCircostanza.getDescrCommaQualificante() != null && !lCircostanza.getDescrCommaQualificante().equals("") && !lCircostanza.getDescrCommaQualificante().equals("-"))
                    out.println(" "+lCircostanza.getDescrCommaQualificante());
                  //**************************************************************************************************
                  
                 if(lCircostanza.getLettera() != null && !lCircostanza.getLettera().equals(""))
                    out.println(" l. "+lCircostanza.getLettera());
                  if(lCircostanza.getNumero() != null && !lCircostanza.getNumero().equals(""))
                    out.println(" n. "+lCircostanza.getNumero());
%>
                </font>
<%
               /*   SentenzaModel lSent = (SentenzaModel)lFascicolo.getSentenza();
                  if(lSent!= null && lSent.getDescrBilanciamentoCircostanze() != null && !lSent.getDescrBilanciamentoCircostanze().equals(""))
                    out.println("     "+lSent.getDescrBilanciamentoCircostanze());*/
%>
              </td>
            </tr>
<%
        }
%>
        </table>

<% 
CircostanzaModel aCirc = null;
Vector allCirc = new Vector(dettagliofascicolo.getCircostanze());
if(allCirc.size()>0)
	aCirc = (CircostanzaModel)allCirc.get(0);
	
	//SentenzaModel lSentenza = (SentenzaModel)lFascicolo.getSentenza();
if(aCirc!=null){
if(   ( aCirc.getFlagSentenzaApplicazPena() != null && aCirc.getFlagSentenzaApplicazPena().equals("S") )
   || ( !aCirc.getCodBilanciamentoCircostanze().equals("-") )
   || ( aCirc.getFlagGiudizioAbbreviato() != null && aCirc.getFlagGiudizioAbbreviato().equals("S") )
  )
{
%>
<table cellspacing="2" cellpadding="2">
  <%
  if(aCirc.getFlagSentenzaApplicazPena() != null && aCirc.getFlagSentenzaApplicazPena().equals("S"))
  {
  %>
    <tr>
      <td class="l">Sentenza di applicazione pena</td>
      <td class="l">
        <font class="campo">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
        </font>&nbsp;
      </td>
    </tr>
  <% }%>

  <%
  if(!aCirc.getCodBilanciamentoCircostanze().equals("-"))
  {
  %>
    <tr>
      <td class="l">Bilanciamento circostanze</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(aCirc.getDescrBilanciamentoCircostanze())%>
        </font>&nbsp;
      </td>
    </tr>
  <%}%>
  
  <%
  if(aCirc.getNoteBilanciamento()!=null && !aCirc.getNoteBilanciamento().equals(""))
  {
  %>
    <tr>
      <td class="l">Annotazioni Bilanciamento circostanze</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(aCirc.getNoteBilanciamento())%>
        </font>&nbsp;
      </td>
    </tr>
  <%}%>
      
  <%
  if(aCirc.getFlagGiudizioAbbreviato() != null && aCirc.getFlagGiudizioAbbreviato().equals("S"))
  {
  %> 
    <tr>
      <td class="l">Giudizio abbreviato</td>
      <td class="l">
        <font class="campo">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
        </font>&nbsp;
      </td>
    </tr>
  <%}%>
  
</table>
<%
}
  }
} //fine Circostanze
%>

<%
//==============================================================================
//                             PENE ACCESSORIE
//==============================================================================
if(   lFascicolo.getFlagCumulante() != null 
   && lFascicolo.getFlagCumulante().equals("S") 
   && penacumulo != null && penacumulo.getPenaAccessoria() != null 
   && !penacumulo.getPenaAccessoria().equals(""))
{
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td class="Titolo" colspan="3">Pene Accessorie</td>
    </tr>
    <tr>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(penacumulo.getPenaAccessoria())%></font>
      </td>
    </tr>
  </table>
<%
}
else
{
  List lPeneAccessorie = dettagliofascicolo.getPeneAccessorie();
  if(lPeneAccessorie != null && lPeneAccessorie.size() != 0)
  {
  %>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr><td class="Titolo" colspan="3">Pene Accessorie</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Tipo</font></center>
      </td>
      <td class="l">
        <center><font class="label">Durata</font></center>
      </td>
      <td class="l">
        <center><font class="label">Condonata</font></center>
      </td>
    </tr>
    <%
    Iterator lIterPeneAccessorie = lPeneAccessorie.iterator();
    while (lIterPeneAccessorie.hasNext())
    {
	    PenaAccessoriaModel lPenAcc = (PenaAccessoriaModel)lIterPeneAccessorie.next();
    %>
      <tr>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(lPenAcc.getDescrTipoPenaAccessoria(), "-")%></font>
        </td>
        <td class="l">
          <font class="campo">
          <%
          if(lPenAcc.getDescrDurata() != null && !lPenAcc.getDescrDurata().equals("-") && !lPenAcc.getDescrDurata().equals(""))
          {
            out.println(lPenAcc.getDescrDurata());
          }
          else if((lPenAcc.getNumAnni()!=null && lPenAcc.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lPenAcc.getNumMesi()!=null && lPenAcc.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lPenAcc.getNumGiorni()!=null && lPenAcc.getNumGiorni().compareTo(new BigDecimal(0))!=0))
          {
          %>
                AA:&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumAnni(), "0")%>&nbsp;
                MM:&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumMesi(), "0")%>&nbsp;
                GG:&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumGiorni(), "0")%>
          <%
          }
          else
          {
            out.println("-");
          }
          %>
          </font>
        </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(lPenAcc.getFlagCondonata(), "-")%></font>
        </td>
      </tr>
<%
	  } // end while
%>
  </table>
<%
} // end if size()>0
} // fine pene accessorie
%>  
  
    
<%    
//==============================================================================    
//                                  BENEFICI
//==============================================================================    
List lBenefici = dettagliofascicolo.getBenefici();

int TotBene = 0;
int TotRBene = 0;
Iterator lIterBenefici1 = lBenefici.iterator();
	
while (lIterBenefici1.hasNext())
 {
	BeneficioModel lBene1 = (BeneficioModel)lIterBenefici1.next();

  	String Natur = lBene1.getCodNaturaBeneficio();
  	if(Natur.compareTo("C") == 0)
  	 	TotBene = TotBene + 1;
  	else
  		if(Natur.compareTo("R") == 0)
  			TotRBene = TotRBene + 1;

  }
	
 if(lBenefici != null && lBenefici.size() != 0)
 {
	if(TotBene>0)
	{	
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr><td class="Titolo" colspan="4">Benefici</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Tipo</font></center>
      </td>
      <td class="l">
        <center><font class="label">Subordinata</font></center>
      </td>
      <td class="l">
        <center><font class="label">DPR</font></center>
      </td>
       <td class="l">
        <center><font class="label">Pena</font></center>
      </td>
    </tr>
    <%
    
    Iterator lIterBenefici = lBenefici.iterator();
    while (lIterBenefici.hasNext())
    {
      	BeneficioModel lBene = (BeneficioModel)lIterBenefici.next();

      	String Natur = lBene.getCodNaturaBeneficio();
      	if(Natur.compareTo("R") != 0)
      	{	  
    %>
    		<tr>
      			<td class="l">
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrNaturaBeneficio(), "-")%></font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoBeneficio(), "-")%></font>
      			</td>
      			<td class="l">
        		<%if(lBene.getDescrTipoSospSubordinata()!= null && !lBene.getDescrTipoSospSubordinata().equals(""))
        		{%>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoSospSubordinata(),"-")%></font>
        		<%}%>
      			</td>
      
      			<td class="l">
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrDpr(), "-")%></font>
      			</td>
      
      			<td class="l">
        	<%
        		if((lBene.getNumAnniReclusione()!=null && lBene.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiReclusione()!=null && lBene.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniReclusione()!=null && lBene.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
        		{
        	%>
        			<font class="campo">Reclusione</font>
        			<font class="label">Anni</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniReclusione(), "0")%></font>
        			<font class="label">Mesi</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiReclusione(), "0")%></font>
        			<font class="label">Giorni</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniReclusione(), "0")%></font>
        	<%
        		}
              
        		if(lBene.getImportoMulta()!=null && lBene.getImportoMulta().compareTo(new BigDecimal(0))!=0)
        		{
        	%>
        			<font class="label">Multa </font>
        			<font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoMulta())%></font>&nbsp;€&nbsp;
        	<%
        		}
        
        		if((lBene.getNumAnniArresto()!=null && lBene.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiArresto()!=null && lBene.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniArresto()!=null && lBene.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
        		{
        	%>
        			<font class="campo">Arresto</font>
        			<font class="label">Anni</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniArresto(), "0")%></font>
        			<font class="label">Mesi</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiArresto(), "0")%></font>
        			<font class="label">Giorni</font>
        			<font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniArresto(), "0")%></font>
        	<%
        		}
        
        		if(lBene.getImportoAmmenda()!=null && lBene.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
        		{
        	%>
        			<font class="label">Ammenda </font>
        			<font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoAmmenda())%></font>&nbsp;€&nbsp;
        	<%
        		}
        	%>
            	&nbsp;
      			</td>
    		</tr>
		<%
      	 }  // end if Natur
      	 
	}  // end while
%>
  </table>
<%
  } // Chiude le if
} // end Benefici
%>      

<%    
//==============================================================================    
//                         BENEFICI REVOCATI
//==============================================================================    

Iterator lIterBenefici = lBenefici.iterator();

if(TotRBene > 0)
{ 
%>	
		<table cellspacing="0" cellpadding="0" width="95%">
    	<tr><td class="Titolo" colspan="4">Revoche</td></tr>
<% 
}
while (lIterBenefici.hasNext())
{
  	BeneficioModel lBene = (BeneficioModel)lIterBenefici.next();
  	
  	String Natur = lBene.getCodNaturaBeneficio();
  	if(Natur.compareTo("R") == 0)
  	{	  
%>
      	<tr>
      		<td class="L" width="100%"><font class="label">Revoca Beneficio : </font>
   			<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoBeneficio())%></font>&nbsp;
        	<font class="campo">di cui a</font>&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoProvvedimento())%></font>&nbsp;
      		<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lBene.getRifDataProvvedimento(),"dd-MM-yyyy"))%></font>
      		</td>
      	</tr>
      	
      	<tr>
      		<td class="L" width="100%"><font class="campo">emessa da</font>&nbsp;
      		<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoAutoritaEmittente())%></font>&nbsp;
      		<font class="campo">di</font>&nbsp;
      		<font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrLuogoAutoritaEmittente())%></font><br>
      		</td>
      	</tr>
<%
      	 }  // end if Natur
      	 
	}  // end while
%>
    </table>      	
   <br>
<%    
//==============================================================================      
//                             MISURE CAUTELARI
//==============================================================================      

 List lMisureCautelari = dettagliofascicolo.getMisureCautelari();
if(lMisureCautelari != null && lMisureCautelari.size() != 0)
{
%>

  <table cellspacing="0" cellpadding="0" width="95%">
    <tr><td class="Titolo" colspan="4">Misure Cautelari</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Misura</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Inizio</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Fine</font></center>
      </td>
    
      <td class="l">
        <center><font class="label">Totale</font></center>
      </td>
  
     
      
    </tr>

    <%
    
      CalendarUtil cu=new CalendarUtil();
      CalendarModel cm;
      CalendarModel ctot=new CalendarModel();

    Iterator lIterMisureCautelari = lMisureCautelari.iterator();
    while (lIterMisureCautelari.hasNext())
    {
	    MisuraCautelareModel lMisCau = (MisuraCautelareModel)lIterMisureCautelari.next();
      String classFont = "CVerde";
      String isComputabile = "";
 
   if (lMisCau.getDataFine()!=null && lMisCau.getDataInizio()!=null)
        {
          cm=new CalendarModel();
          cm.setDataFine(lMisCau.getDataFine());
          cm.setDataInizio(lMisCau.getDataInizio());
          //cm=cu.ricalcolaGAM(cu.CalcolaNumGiorniMesiAnni(cm));
          int numA = 0;
          int numM = 0;
          int numG = 0;
          if(lMisCau.getNumAnni()!=null)
          	numA = Integer.parseInt(lMisCau.getNumAnni().toString());          
          if(lMisCau.getNumMesi()!=null)
          	numM = Integer.parseInt(lMisCau.getNumMesi().toString());          
          if(lMisCau.getNumGiorni()!=null)
          	numG = Integer.parseInt(lMisCau.getNumGiorni().toString());
        
          //isComputabile="Anni " +cm.getNumAnni()+" Mesi "+cm.getNumMesi()+" Giorni "+cm.getNumGiorni();
          isComputabile="Anni " +numA+" Mesi "+numM+" Giorni "+numG;
          
          if (lMisCau.getFlagComputabile().equals("S"))
          {
            ctot=cu.sommaGiorni(ctot,cm);
            classFont="CVerde";
          } 
          else
          {
            isComputabile="NON COMPUTABILE";
            classFont="C";
          }
        } 
        else
        {
          if (lMisCau.getFlagComputabile().equals("S"))
            classFont="CVerde";
            
          isComputabile="-";
        }

    
    %>
    <tr>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(lMisCau.getDescrTipoMisura(), "-")%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataInizio(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <% if (lMisCau.getFlagModificaManuale()!=null && lMisCau.getFlagModificaManuale().equals("S")) {  classFont="cRosso"; %>
    	  	<td class="<%=classFont%>"><%=isComputabile%></td>
      <% } else { %>
      		<td class="<%=classFont%>"><%=isComputabile%></td>
      <% } %>
    </tr>
    <% } %>
  </table>
  
  
 
<%
}  // end misure cautelari
%>

<%
//==============================================================================
//                             PENA VIRTUALE
//==============================================================================
// new: visualizzata solo se pena non in decorrenza e non ergastolo, rappresenta 
//      i quantum di pena residua - LA ancora da detrarre.
//      In questo modo l'operatore può vedere quanto 'effettivamente' resterebbe
//      da espiare al condannato ai fini dell'applicazione dei benefici. 
//      Infatti se la pena entra in decorrenza il quantum effettivo da espiare 
//      è quello a sistema ma anticipato per effetto delle LA.
//
boolean isErgastolo = false;
PenaResiduaModel lPenResMod = dettagliofascicolo.getPenaResidua();
if(   lPenResMod!=null && lPenResMod.getFlagErgastolo()!=null
   && (lPenResMod.getFlagErgastolo().equals("S") || lPenResMod.getFlagErgastolo().equals("D"))
  ) 
{
  isErgastolo = true;  
}

if(   lPenResMod!= null
   && !isErgastolo
   && lPenResMod.getDataInizio()== null
   && dettagliofascicolo.getGiorniLibNonConcessa()!=null
   && dettagliofascicolo.getGiorniLibNonConcessa().compareTo(new BigDecimal(0))!=0
  )
{
  CalcoloPenaModel lCalcoloPenaModel = dettagliofascicolo.getCalcoloPenaModel();
  CalendarModel lPenaVirtuale = lCalcoloPenaModel.getPenaVirtuale(lPenResMod);
  BigDecimal lLA = dettagliofascicolo.getGiorniLibNonConcessa();
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
       <td class="Titolo" colspan="3" title="Pena residua detratti i giorni di Liberazione Anticipata concessi e ancora da detrarre">Pena Virtuale</td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumAnni()),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumMesi()),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumGiorni()),"0")%></font>&nbsp;&nbsp;
        <font class="label"> (detratti <font class="campo"><%=StringUtils.toStringJSP(lLA,"0")%></font> giorni di LA)</font>
      </td>
    </tr>
  </table>
<%
}
%>

<%
//==============================================================================
//                        MAGISTRATO ASSEGNATARIO
//==============================================================================
//modifica 27-02-04

  MagistratoCompetenteMagistratoModel lMagMod = dettagliofascicolo.getMagistratoCompetente();
  if(lMagMod != null && lMagMod.getMagistratoCompetente()!= null && lMagMod.getMagistrato()!= null)
  {
%>
        <table cellspacing="0" cellpadding="0" width="95%">
          <tr>
             <td class="Titolo" colspan="3">Magistrato Assegnatario</td>
          </tr>
          <tr>
            <td class="l">
              <center><font class="label">Cognome Nome</font></center>
            </td>
            <td class="l">
              <center><font class="label">Data Assegnazione</font></center>
            </td>
          </tr>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lMagMod.getMagistrato().getCognome(),"-") + " " + StringUtils.toStringJSP(lMagMod.getMagistrato().getNome(),"-")%></font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMagMod.getMagistratoCompetente().getDataInizio(),"dd-MM-yyyy"),"-")%></font>
            </td>
          </tr>
        </table>
<%
  }
%>
      
<%
//==============================================================================    
//                            MISURE SICUREZZA
//==============================================================================    
if(   lFascicolo.getFlagCumulante() != null 
   && lFascicolo.getFlagCumulante().equals("S") 
   && penacumulo != null && penacumulo.getMisuraSicurezza() != null  
   && !penacumulo.getMisuraSicurezza().equals("")
  )
{
  // Fascicolo cumulante con misure di sicurezza
%>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr><td class="Titolo" colspan="3">Misure Sicurezza</td></tr>
    <tr>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(penacumulo.getMisuraSicurezza())%></font>
      </td>
    </tr>
  </table>
<%
} 
else 
{
  List lMisureSicurezza = dettagliofascicolo.getMisureSicurezza();
  if(lMisureSicurezza != null && lMisureSicurezza.size() != 0)
  {
  %>
  <table cellspacing="0" cellpadding="0" width="95%">
    <tr>
      <td class="Titolo" colspan="3">Misure Sicurezza</td>
    </tr>
    <%
      if(lMisureSicurezza != null && lMisureSicurezza.size() != 0)
      {
    %>
          <tr>
            <td class="l">
              <center><font class="label">Natura Misura</font></center>
            </td>
            <td class="l">
              <center><font class="label">Tipo Misura</font></center>
            </td>
            <td class="l">
              <center><font class="label">Durata Misura</font></center>
            </td>
          </tr>
<%
        Iterator lIter = lMisureSicurezza.iterator();
        while (lIter.hasNext())
        {
          MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel) lIter.next();
          boolean lIsMigratoSenzaValori = false;
      
          if (lMisSicu.isDurataZero() && "-".equals(lMisSicu.getCodTipo()) && lMisSicu.getCodOperatoreInserimento().startsWith("res-"))
            lIsMigratoSenzaValori = true;
          else
            lIsMigratoSenzaValori = false;
        %>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
            </td>
            <td class="l">
<%--               <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(), "-")%></font> --%>
				<%-- MEV_39: la tipologia di MS viene resa cliccabile --%>
				<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza&IdMisuraSicurezza=<%=lMisSicu.getIdMisuraSicurezza()%>" title="Dettaglio Misura di Sicurezza">
	          		<%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(), "-")%>
	        	</a>
            </td>
            <td class="l">
              <font class="campo">
                AA:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;
                MM:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;
                GG:&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%>
              </font>
              &nbsp;
              <% if (lIsMigratoSenzaValori) {%>
              <a href="Javascript:conferma('siap.siep.misurasicurezza.action.ActCancellaMisuraSicurezza','IdMisuraSicurezza','<%=lMisSicu.getIdMisuraSicurezza()%>');">
                <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
              </a>
              <% } %>
            </td>
          </tr>
        <%
        }  // end while
      } // end if
%>
      </table>
<%
    }
  }

//==============================================================================
//                  ULTERIORI SANZIONI IN CUMULO
//==============================================================================

	if (ulteriorisanzionicumulo != null && ulteriorisanzionicumulo.size() != 0) {
%>
	<table cellspacing="0" cellpadding="0" width="95%">
      	<tr>
        	<td class="Titolo" colspan="3">Ulteriori sanzioni</td>
      	</tr>
 		<tr>
        	<td class="l">
          		<center><font class="label">Tipo Ulteriore Sanzione</font></center>
        	</td>
        	<td class="l">
          		<center><font class="label">Durata Ulteriore Sanzione</font></center>
        	</td>
        	<td class="l">
          		<center><font class="label">Sanzione</font></center>
        	</td>
		</tr>
<%
		Iterator lIter = ulteriorisanzionicumulo.iterator();
     	while (lIter.hasNext()) {
       		UlterioreSanzioneCumuloModel lUltMod = (UlterioreSanzioneCumuloModel) lIter.next();
%>
		<tr>
        	<td class="l">
          		<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getDescrTipoUlterioreSanzione(),"-")%></font>
        	</td>
<%
          	if (lUltMod != null && lUltMod.getNumAnni() != null && lUltMod.getNumMesi() != null && lUltMod.getNumGiorni() != null) {
%>
          	<td class="l">
            	<font class="campo">
              		AA:&nbsp;<%=StringUtils.toStringJSP(lUltMod.getNumAnni(), "0")%>&nbsp;
             		MM:&nbsp;<%=StringUtils.toStringJSP(lUltMod.getNumMesi(), "0")%>&nbsp;
              		GG:&nbsp;<%=StringUtils.toStringJSP(lUltMod.getNumGiorni(), "0")%>
            	</font>
          	</td>
<%
          	} else {
%>
			<td class="l">
            	<font class="campo">-</font>
          	</td>
<%
          	}
          	if (lUltMod.getSanzione() != null) {
%>
            <td class="l">
              	<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getSanzione(),"0")%></font>
            </td>
<%
			} else {
%>
            <td class="l">
				<font class="campo">-</font>
			</td>
<%
			}
%>
		</tr>
<%
		}
%>
	</table>
<%
	} // end ulteriori sanzioni in cumulo

//==============================================================================
// Riepilogo generale dei totali delle quantità componenti la pena relevate su RES
//==============================================================================

	List lListEve = dettagliofascicolo.getEventi();
	if (lListEve != null && lListEve.size() != 0) {
		EventoNotificaModel lEveNotificaMod = (EventoNotificaModel) lListEve.get(0);
		RiepilogoProvvedimentoModel lRiePro = null;
		EventoModel lEveMod = lEveNotificaMod.getEvento();
	  	if ("01".equals(lEveMod.getCodTipoEvento())
	  			&& "04".equals(lEveMod.getCodTipoProvvedimento())
	  			&& "7777".equals(lEveMod.getCodMotivo())) { // Se evento migrato da RES (7777)
			lRiePro = lEveNotificaMod.getRiepilogoProvvedimento();
	  	}
  		if (lRiePro != null) {
%>
    <table cellspacing="0" cellpadding="0" width="95%">
      <tr>
        <td class="Titolo">Riepilogo generale dei totali delle quantità componenti la pena relevate su RES</td>
      </tr>
      <tr>
        <td>
          <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
              <td class="l" colspan="5"><font class="label">Pena Iniziale (o pena residua)</font></td>
            </tr>
            <tr>
              <td class="l" width="18%"><font class="label">Ergastolo</font></td>
              <td class="l" width="22%"><font class="label">Reclusione</font></td>
              <td class="l" width="18%"><font class="label">Multa</font></td>
              <td class="l" width="22%"><font class="label">Arresto</font></td>
              <td class="l"><font class="label">Ammenda</font></td>
            </tr>
            <tr>
              <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getFlagErgastolo(), "N")%></font></td>
              <td class="l">
                <font class="campo">
                  AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniReclusione(), "0")%>&nbsp;
                  MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiReclusione(), "0")%>&nbsp;
                  GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniReclusione(), "0")%>
                </font>
              </td>
              <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoMulta())%>&nbsp;</font></td>
              <td class="l">
                <font class="campo">
                  AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniArresto(), "0")%>&nbsp;
                  MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiArresto(), "0")%>&nbsp;
                  GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniArresto(), "0")%>
                </font>
              </td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoAmmenda())%>&nbsp;</font></td>
						</tr>
		  			</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l"><font class="label">Presofferti</font></td>
		 				</tr>
						<tr>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniPresofferto(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiPresofferto(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniPresofferto(), "0")%>
		  					</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l" width="18%"><font class="label">Riduzioni: </font></td>
							<td class="l" width="22%"><font class="label">Reclusione</font></td>
							<td class="l" width="18%"><font class="label">Multa</font></td>
							<td class="l" width="22%"><font class="label">Arresto</font></td>
							<td class="l"><font class="label">Ammenda</font></td>
		  				</tr>
						<tr>
							<td class="l">&nbsp;</td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniReclusioneBenefici(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiReclusioneBenefici(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniReclusioneBenefici(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoMultaBenefici())%>&nbsp;</font></td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniArrestoBenefici(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiArrestoBenefici(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniArrestoBenefici(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoAmmendaBenefici())%>&nbsp;</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l" width="18%"><font class="label">Aumenti: </font></td>
							<td class="l" width="22%"><font class="label">Reclusione</font></td>
							<td class="l" width="18%"><font class="label">Multa</font></td>
							<td class="l" width="22%"><font class="label">Arresto</font></td>
							<td class="l"><font class="label">Ammenda</font></td>
		  				</tr>
						<tr>
							<td class="l">&nbsp;</td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniAumentiPenaReclus(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiAumentiPenaReclus(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniAumentiPenaReclus(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoMultaAumentiPena())%>&nbsp;</font></td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniAumentiPenaArres(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiAumentiPenaArres(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniAumentiPenaArres(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoAmmendaAumentiPena())%>&nbsp;</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<!--tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l" colspan="4"><font class="label">Pena da espiare (al netto di presofferti. riduzini e aumenti)</font></td>
		 				</tr>
						<tr>
							<td class="l"><font class="label">Reclusione</font></td>
							<td class="l"><font class="label">Multa</font></td>
							<td class="l"><font class="label">Arresto</font></td>
							<td class="l"><font class="label">Ammenda</font></td>
		  				</tr>
						<tr>
							<td>
							<font class="campo">
		  					</font>
							</td>
							<td><font class="campo"></font></td>
							<td>
							<font class="campo">
		  					</font>
							</td>
							<td><font class="campo"></font></td>
						</tr>
					</table>
				</td>
			</tr-->
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l"><font class="label">Interruzioni Di Pena</font></td>
							<td class="l"><font class="label">Liberazioni Anticipate</font></td>
		 				</tr>
						<tr>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniInterruzione(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiInterruzione(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniInterruzione(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo">GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniLibAnticipata(), "0")%></font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<!--td class="l"><font class="label">Posizione Giuridica Condannato</font></td-->
							<td class="l"><font class="label">Decorrenza Pena</font></td>
							<td class="l"><font class="label">Dies A Quo</font></td>
							<td class="l"><font class="label">Scadenza Pena</font></td>
		 				</tr>
						<tr>
							<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
							<%--td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.get(), "NON DEFINITA")%>&nbsp;</font></td--%>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRiePro.getDataInizioPena(), "dd-MM-YYYY"))%>&nbsp;</font></td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getDiesAQuo(), "N")%></font></td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRiePro.getDataFinePena(), "dd-MM-YYYY"))%>&nbsp;</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l" colspan="5"><font class="label">Pena Residua (a seguito di sospensione/interruzione)</font></td>
		 				</tr>
						<tr>
							<td class="l" width="18%">&nbsp;</td>
							<td class="l" width="22%"><font class="label">Reclusione</font></td>
							<td class="l" width="18%"><font class="label">Multa</font></td>
							<td class="l" width="22%"><font class="label">Arresto</font></td>
							<td class="l"><font class="label">Ammenda</font></td>
		  				</tr>
						<tr>
							<td class="l">&nbsp;</td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniPenaResiduaReclus(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiPenaResiduaReclus(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniPenaResiduaReclus(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoMultaResidua())%>&nbsp;</font></td>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniPenaResiduaArres(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiPenaResiduaArres(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniPenaResiduaArres(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(lRiePro.getImportoAmmendaResidua())%>&nbsp;</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table cellspacing="0" cellpadding="0" width="100%">
		  				<tr>
		  					<td class="l"><font class="label">Pena Espiata In Eccesso</font></td>
							<td class="l"><font class="label">Scadenza Detenzione Domiciliare</font></td>
		 				</tr>
						<tr>
							<td class="l">
							<font class="campo">
       						AA:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumAnniFungibilita(), "0")%>&nbsp;
                			MM:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumMesiFungibilita(), "0")%>&nbsp;
                			GG:&nbsp;<%=StringUtils.toStringJSP(lRiePro.getNumGiorniFungibilita(), "0")%>
		  					</font>
							</td>
							<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRiePro.getDataFineDetDomiciliare(), "dd-MM-YYYY"))%>&nbsp;</font></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
<%
     	}
 	}
	if (siap.util.SIESSwitch.isRegeSiesOn()) {
		if (dettagliofascicolo.getNoteFascicolo() != null
				&& dettagliofascicolo.getNoteFascicolo().length() > 1) {
%>
<table>
   	<tr>
       	<td class="L">
   			<a class="cliccabile" href="Javascript:ShowDispositivo();">Dispositivo</a>
       	</td>
	</tr>
</table>
<%
		}
    }
%>
</div>
<%
} // end if(lTipoFunzione.equals("")
%>
<br>
</body>
</html>