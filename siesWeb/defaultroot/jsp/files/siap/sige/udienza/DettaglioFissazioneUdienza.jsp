<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige" %>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="eventonotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="udienza"         scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="provvedimentoSige" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="Cancellabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="rinvio"  				scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizioVal"        scope="request" class="java.lang.String"/>
<jsp:useBean id="IdEvento"        scope="request" class="java.lang.String"/>
<jsp:useBean id="IdUdienzaSige"        scope="request" class="java.lang.String"/>
<jsp:useBean id="IdUdienzaProcedimentoSige"        scope="request" class="java.lang.String"/>
<jsp:useBean id="descrMagPresidente"        scope="request" class="java.lang.String"/>


<%
EventoNotificaModel lEve = eventonotifica;
String FlagDocReg = lEve.getEvento().getFlagDocumentoRegistrato();

if (FlagDocReg == null)
	FlagDocReg="N";
	
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

ProvvedimentoSigeModel provvSige = new ProvvedimentoSigeModel();
if (provvedimentoSige!=null && provvedimentoSige.getProvvedimento()!=null){
	provvSige = provvedimentoSige.getProvvedimento(); 
}


//Fascicolo SIGE in sessione
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();

RedirectTo lRedir = new RedirectTo();
String idUdiSige = (udienza!=null && udienza.getIdUdienzaSige()!=null)?udienza.getIdUdienzaSige().toString():"";
String descUdienza = "";
String descLink = "Inserimento Udienza";
if ("C".equals(tipoGiudizioVal)){
	// Collegiale
	lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
	descUdienza = "Udienza Collegiale";
	descLink = "Inserimento Udienza - Visualizza Collegio";
} else if ("M".equals(tipoGiudizioVal)){
	// Monocratica
	lRedir.setAction("siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige");
	descUdienza = "Udienza Monocratica";
}
lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige);
lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
lRedir.setParameter("TornaQui", TornaQui);
String lLinkUdienza = lRedir.toString();

lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=O&IdEvento="+IdEvento+"&IdUdienzaSige="+IdUdienzaSige+"&IdUdienzaProcedimentoSige="+IdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
lRedir.setParameter("TornaQui", TornaQui);
String lLinkParteOffesa = lRedir.toString();

lRedir.setAction("siap.sige.udienzaparti.action.ActLoadVisualizzaParti&codTipoParte=C&IdEvento="+IdEvento+"&IdUdienzaSige="+IdUdienzaSige+"&IdUdienzaProcedimentoSige="+IdUdienzaProcedimentoSige+"&ChiaveAnno=" + lFascicolo.getChiaveAnno() + "&ChiaveProgr=" + lFascicolo.getChiaveProgr());
lRedir.setParameter("TornaQui", TornaQui);
String lLinkParteCivile = lRedir.toString();

String aula="&nbsp;";
String ingresso="&nbsp;";
String piano="&nbsp;";
String sezione="&nbsp;";
String stanza="&nbsp;";

AulaUdienzaModel aulaUdienzaModel=udienza.getAulaUdienzaModel();
if (aulaUdienzaModel != null) {
	aula=(aulaUdienzaModel.getDescrizioneStanza()==null?"&nbsp;":aulaUdienzaModel.getDescrizioneAula());
	ingresso=(aulaUdienzaModel.getDescrizioneIngresso()==null?"&nbsp;":aulaUdienzaModel.getDescrizioneIngresso());
	piano=(aulaUdienzaModel.getNumeroPiano()==null?"&nbsp;":aulaUdienzaModel.getNumeroPiano());
	stanza=(aulaUdienzaModel.getDescrizioneStanza()==null?"&nbsp;":aulaUdienzaModel.getDescrizioneStanza());
}

SezioneModel sezioneModel=udienza.getSezioneModel();
if (sezioneModel != null) {
	sezione=(sezioneModel.getDescrizione()==null?"&nbsp;":sezioneModel.getDescrizione());
}

String oraInizio = (udienza.getOraInizio()== null || udienza.getOraInizio().equalsIgnoreCase("NULL")?"":udienza.getOraInizio());
String minInizio = (udienza.getMinInizio()== null || udienza.getMinInizio().equalsIgnoreCase("NULL")?"":udienza.getMinInizio());
String oraFine = (udienza.getOraFine()== null || udienza.getOraFine().equalsIgnoreCase("NULL")?"":udienza.getOraFine());
String minFine = (udienza.getMinFine()== null || udienza.getMinFine().equalsIgnoreCase("NULL")?"":udienza.getMinFine());

String orarioInizio=oraInizio +":"+minInizio;
String orarioFine=oraFine +":"+minFine;
%>
<html>
  	<head>
    <title> [S.I.E.S.] - Dettaglio Fissazione Udienza - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >
	function init() {
<%-- 20171013: [SG] rimosso tale messaggio --%>
<%-- 		<% --%>
// 		if (NumeroUdienzeMagistrato!=null && !"".equals(NumeroUdienzeMagistrato)) {
// 			String mess = "";
// 			if (NumeroUdienzeMagistrato.trim().equals(1)){
// 				mess = "Udienza del magistrato assegnatario trovata";
// 			} else {
// 				mess = "Trovate "+NumeroUdienzeMagistrato+" udienze del magistrato assegnatario, è stata selezionata la più recente";
// 			}
<%-- 		%>    	 --%>
<%-- 		alert ("<%=mess%>");	 --%>
<%-- 		<% --%>
// 		}
<%-- 		%>    	 --%>
		var flagDocReg = '<%=FlagDocReg%>';
		if (flagDocReg == 'N'){
			ControlloUpload();
		} else {
			ControlloRinvio();
		}
   	}

	function ControlloUpload() {
        //var nodeUp=document.getElementById('upld');
        //nodeUp.style.visibility='visible';
        ControlloRinvio();
	}

	function ControlloRinvio() {
        // var nodeDet=document.getElementById('dettaglio');
        // nodeDet.style.visibility='hidden';
        var rinvio = '<%=rinvio%>';
        if (rinvio == 'S'){
      		alert('Attenzione! Esiste un provvedimento di Rinvio Udienza!');
        }
	}

    //
    // Funzione JS per chiamta azione inserimento parti offese/civili
    //   
	function InserisciParte( tipoParte ){
		var lLink;
		if( tipoParte == 'O' ){
			lLink = "<%=lLinkParteOffesa%>";
		} else {
			lLink = "<%=lLinkParteCivile%>";
		}		 
		window.location=lLink;	
	}

  	//
    // Funzione JS per chiamta azione inserimento udienza
    //   
	function InserisciUdienza( tipoRito ){
  		window.location="<%=lLinkUdienza%>";	
	}
	
	//
    // Funzione JS per chiamta azione deposito decreto
    //   
	function InserisciDepositoDecreto (){
		// 20191108 [SG]: TICKET#20191029016
		// L'utente poi prosegue con l'emissione del decreto di fissazione udienza. Valida e Deposita.
		// AL DEPOSITO SENZA NESSUN DESTINATARIO IL SISTEMA automaticamente RIPORTA L'UNEP.
		// aggiungo parametro di passaggio in query string --> &Aggiungi=yes
		lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=provvSige.getIdEventoGenerato()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvSige.getIdProvvedimentoSige()%>&TornaQui=<%=TornaQui%>&isUdienza=true&Aggiungi=yes";
		window.location=lLink;
	}
	
  	</script>

  </head>

 	<body class="corpo" onload="javascript:init();">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Fissazione Udienza</font>
        </td>
<%
if (request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE) != null  )	{%>
		<jsp:include page="<%=ICostantiUdienzaSige.PG_BUTTONS%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
			<jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)%>" />
		</jsp:include>
<%}

if ((lEve.getEvento().getFlagDocumentoRegistrato()==null) || (lEve.getEvento().getFlagDocumentoRegistrato()!=null) && (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))  {%>
		<!-- BOTTONE DI STAMPA -->
		<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIGE%>">
			<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
			<jsp:param name="ValoreIdEntita" value="<%=lEve.getEvento().getIdEvento()%>" />
		</jsp:include>
<%}%>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  </tr>
  </table>

<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>

<jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>"/>

<jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>"/>

	<br>
	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI CIVILI</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="C"/>
		</jsp:include>
<%	if( "SI".equalsIgnoreCase(Modificabile) ) {%>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('C');">Gestione Parti Civili</a>
			</td>
		</tr>
<%}%>
	</table>
	<br>
	<table cellspacing=2 cellpadding=2 width="95%">
		 <tr><td class="Titolo" colspan=6 >PARTI OFFESE</td></tr>
		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_VISUALSUMMARY_PARTI_UDIENZA%>">
			<jsp:param name="tipoParte" value="O"/>
		</jsp:include>
<%	if( "SI".equalsIgnoreCase(Modificabile) ) {%>
		<tr>
			<td class="label">
				<a class="cliccabile" href="Javascript:InserisciParte('O');">Gestione Parti Offese</a>
			</td>
		</tr>
<%}%>
	</table>
	<br>

  <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo" colspan="4">Udienza</td>
    </tr>

    <tr>
      <td class="l" width="20%">Data Emissione</td>
      <td class="L" width="80%">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

<% 	if (provvSige.getDataDeposito() != null && "NO".equalsIgnoreCase(Modificabile)){ %>
  	<tr>
    	<td class="l" width="20%">
    	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadInserisciDataDeposito&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=provvSige.getIdEventoGenerato()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvSige.getIdProvvedimentoSige()%>&TornaQui=<%=TornaQui%>">
           Dettaglio Deposito Decreto
        </a>
    	</td>
    	<td class="l" width="80%">
        
    	</td>
 	</tr>
  	<tr>
    	<td class="l" width="20%"> Data Deposito in Cancelleria</td>
    	<td class="l" width="80%"> <font class="campo"><%=DateUtils.getDateToString(provvSige.getDataDeposito(),"dd-MM-yyyy")%></font></td>
  	</tr>
<%}%>

    <tr>
      <td class="l" width="25%">Data Udienza</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy"))%></font>
      </td>
      </tr>
    <tr>  
<%
if ("M".equalsIgnoreCase(tipoGiudizioVal))  {
	  if(udienza != null && udienza.getCodGiudice()!= null){
%>        
	      <td class="l" width="20%">Giudice</td>
	      <td class="L" width="80%">
	        <font class="campo">
	        		<%=StringUtils.toStringJSP(udienza.getDescrGiudice()) %>
	      	</font>
	      </td>
<%
	  } else { 
%>        <td width="25%">&nbsp;</td> 
		  <td width="100%">&nbsp;</td> 
<%
	  }	  
%>      
    </tr>
    
    <tr>
      <td class="l" width="20%">Sezione</td>
      <td class="L" width="80%">
        <font class="campo"><%=sezione%></font>
      </td>

    </tr>
<%
}
%>    
    
     <tr>
           <td colspan="4">
      <table width="100%">
      <tr>
      <td class="l" width="25%">Aula </td>
      <td class="l" width="25%"><font class="campo"><%=aula%></font></td>
      <td class="l" width="25%" >Stanza </td>
      <td class="l" width="25%"><font class="campo"><%=stanza%></font></td>
      </tr>
      </table>
      </td>
      </tr>
      
      <tr>
      <td colspan="4">
      <table width="100%">
      <tr>
      <td class="l" width="25%">Ingresso </td>
      <td class="l" width="25%"><font class="campo"><%=ingresso%></font></td>
      <td class="l" width="25%" >Piano </td>
      <td class="l" width="25%"><font class="campo"><%=piano%></font></td>
      </tr>
      </table>
      </td>
      
    </tr>
    
    <tr>
      <td colspan="4">
      <table width="100%">
      <tr>
      <td class="l" width="25%">Ora Inizio </td>
      <td class="l" width="25%"><font class="campo"><%=orarioInizio%></font></td>
      <td class="l" width="25%" >Ora Fine </td>
      <td class="l" width="25%"><font class="campo"><%=orarioFine%></font></td>
      </tr>
      </table>
      </td>
     </tr>    
    <tr>
      <td class="l" nowrap width="20%">Luogo Svolgimento Udienza</td>
      <td class="L" width="80%">
        <font class="campo"><%=StringUtils.toStringJSP(udienza.getLuogoUdienza())%></font>&nbsp;
      </td>
    </tr>

<%
String lNote = "-";
if( eventonotifica.getCampoNote() != null && eventonotifica.getCampoNote().length > 0 ){
	lNote = eventonotifica.getCampoNote()[0].getDescr();
}
%>
    <tr>
     <td class="l"  width="20%">Note</td>
      <td class="L" width="80%">
        <font class="campo"><%=StringUtils.toStringJSP( lNote )%></font>&nbsp;
      </td>
    </tr>

<%-- 22/07/2009 Dati Udienza Collegiale --%>
<%	
if( udienza.getColIdCollegio() != null ) {
	String descsez = (udienza.getCollegio()!=null && udienza.getCollegio().getSezione()!=null) ? udienza.getCollegio().getSezione().getDescrizione():"";
	String cgmag = "";
	String nmmag = "";
	if (udienza.getCollegio()!=null && udienza.getCollegio().getCollegioMagistrati()!=null && udienza.getCollegio().getCollegioMagistrati().length>0 && udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato()!=null){
		cgmag = udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome();
		nmmag = udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome();
	}
%>
  		<tr>
  			<td class="l"  width="25%">
  			<%=descUdienza%>&nbsp;
  			</td>
   			<td class="L" width="100%">
   				<font class="l">Sezione</font>
     			<font class="campo"><%=StringUtils.toStringJSP( descsez )%></font>
     			&nbsp;&nbsp;&nbsp;
   				<font class="l">Presidente</font>
   				<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
     			<%-- <font class="campo"><%=StringUtils.toStringJSP( cgmag )%>&nbsp;<%=StringUtils.toStringJSP( nmmag )%></font> --%>
     			<font class="campo"><%=StringUtils.toStringJSP( descrMagPresidente )%></font>
     			
   			</td>
 		</tr>
<%	if( "SI".equalsIgnoreCase(Modificabile) ) {%>
		<tr>
			<td class="label">
  				<a class="cliccabile" href="Javascript:InserisciUdienza();"><%=descLink%></a>
			</td>
		</tr>
<%}%>

<%
}
%>
    
  </table>
	<br>
	
  <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo">Oggetti</td>
    </tr>

<%
			Iterator itx = tenori.iterator();
			String lIdTenore = "";
			String lCodTenore = "";
			   
			while ( itx.hasNext()) 			{
				TenoreSigeModel lTenore = (TenoreSigeModel)itx.next();
				//if (lTenore != null &&  !lTenore.getIdTenoreSige().toString().equalsIgnoreCase(lIdTenore)) 
				if (lTenore != null &&  !lTenore.getCodOggettoSige().equalsIgnoreCase(lCodTenore))	{
					lIdTenore = lTenore.getIdTenoreSige().toString();
					lCodTenore = lTenore.getCodOggettoSige();
%>					
   	<tr>
		<td class="L"><font class="label"><%=lTenore.getDescrContenutoSige()%> - <%=lTenore.getDescrOggettoSige()%></font></td>
	</tr>
<%
				}
			}
%>

  </table>
	<br />
			
  <jsp:include page="<%=ICostantiUdienzaSige.PG_LOAD_DESTINATARI%>"/>
<%
  if (FlagDocReg.equalsIgnoreCase("S") && provvSige.getDataDeposito()==null) {
%>	  
  
  <table>
    <tr>
      <td>
        <input class="bottone" type="button" value="Deposito Decreto" onclick="InserisciDepositoDecreto()" >
      </td>
    </tr>
    </table>
<%
  }
%>    
  
	<form name="dettaglio">
 		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
 	</form>
 
   <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.udienza.action.ActUploadFissazioneUdienza">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"   value="<%= lEve.getEvento().getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.udienza.action.ActUploadFissazioneUdienza">
          </td>
        </tr>
     </table>
    </FORM>
    
    
    
    
    
   </div>
   <br>
  </body>

</html>