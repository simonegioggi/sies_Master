<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoObbligo"     scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"     scope="request" class="java.lang.String"/>

<%
  	SentenzaModel sentenza = fascicolo.getSentenza();
	String uffi_ge=new String();
	String luo_ge=new String();
	String sez_ge=new String();
  	if (sentenza.getDescrTipoAutoritaEmittente() != null){
		uffi_ge=sentenza.getDescrTipoAutoritaEmittente();
		luo_ge=sentenza.getDescrLuogoEmittente();
		sez_ge=sentenza.getNumSezioneAutoritaEmittente();
	}
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Determinazione Termini </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname)
  {
  	var desktop;
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
	
function LeggiDestinatari()
  {
	var myselect=document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>;
	var myselect_sede=document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>;
	var myselect_sez=document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>;
	var titi='<%=uffi_ge%>';
	var titi2='<%=luo_ge%>';
	var titi3='<%=sez_ge%>';
	for (var i=0; i<myselect.length; i++){ //loop through all form elements
 		if (myselect.options[i].text==titi){
  			myselect.options[i].selected=i;
  			myselect_sede.value=titi2;
  			if (titi3!='null')
  				myselect_sez.value=titi3;
  			break
  		}
  	}
  }
	
</script>

<script language="JavaScript">
function Verify()
  {
		    if (!(document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>.value.length > 1))
		    {
		      alert('Specificare Tipologia Obbligo');
		      return false;
		    }
		    if (!(document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Ufficio Giudice Esecuzione');
		      return false;
		    }
		    if (!(document.LoadInserisciRichiestaDeterminazioneTermini.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Sede Giudice Esecuzione');
		      return false;
		    }
 }
</script>

</head>

  <body class="corpo" onload="LeggiDestinatari();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
		String lAzione = new String();
		if( modalita.equals("I") ){
        	lAzione = "siap.siep.penasospesa.action.ActInserisciRicDeterminazioneTermini";
%>
			<font class="campo">Inserimento Richiesta Determinazione Termini </font>
<%
       }
       	else if( modalita.equals("M") )
       {
			lAzione = "siap.siep.penasospesa.action.ActModificaRicDeterminazioneTermini";
%>
         	<font class="campo">Modifica Richiesta Determinazione Termini</font>
<%
       }
%>
      </td>
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaEsecuzione.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaDeterminazioneTermini" >
    <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
    <table style="width: 95%;">
	    <tr><td class="Titolo" colspan=4>Dati Atto</td></tr>
  </table>
  <table style="width: 95%;">
		<tr>
        <td class="l">Tipologia Obbligo</td>
        <td class="l">
        <select name="<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>">
        <%=tipoObbligo%>
        </select>
        </td>
		</tr>
		<tr>
        <td class="l">Oggetto</td>
        <td class="l">
        <select name="<%=ICostantiPenaSospesa.CAMPO_COD_MOTIVO%>">
        <%=oggetto%>
        </select>
        </td>
		</tr>
		<tr>
	      	<td class="l">Note</td>
	      	<td class="l">
	        	<Textarea Title="Note" name="<%= ICostantiPenaSospesa.CAMPO_NOTE %>" cols=80 rows=5></textarea>
	      	</td>
		</tr>
		  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/penasospesa/IncDestinatariGe.jsp"/>
  <br>
 		<table style="width: 95%;">
  		<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      	</td>
      	</tr>
      	</table>
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">     	
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaDeterminazioneTermini");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
  //////
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2999");
  frmvalidator.setAddnlValidationFunction("Verify");
  
  
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
  </FORM>
</body>
</html>