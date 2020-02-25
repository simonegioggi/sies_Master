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
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

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
<title>[S.I.E.S.] - Gestione Richiesta Estinzione di Pena </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

<script language="JavaScript">
function Verify()
  {
		    if (!(document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>.value.length > 1))
		    {
		      alert('Specificare Articolo');
		      return false;
		    }
		    if (!(document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Ufficio Giudice Esecuzione');
		      return false;
		    }
		    if (!(document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Sede Giudice Esecuzione');
		      return false;
		    }
 }
function LeggiDestinatari()
  {
	var myselect=document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>;
	var myselect_sede=document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>;
	var myselect_sez=document.LoadInserisciRichiestaEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>;
	var titi='<%=StringUtils.cStrForJS(uffi_ge)%>';
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

</head>

  <body class="corpo" onload="Javascript:LeggiDestinatari(); caricatuttecombo();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
		EventoModel lEvento = new EventoModel();
		String lAzione = new String();
		if( modalita.equals("I") ){
        	lAzione = "siap.siep.penasospesa.action.ActInserisciRicEstinzioneReato";
%>
			<font class="campo">Inserimento Richiesta Estinzione Reato </font>
<%
       }
       	else if( modalita.equals("M") )
       {
			lAzione = "siap.siep.penasospesa.action.ActModificaRicEstinzioneReato";
%>
         	<font class="campo">Modifica Richiesta Estinzione Reato</font>
<%
       }
%>
      </td>
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaEstinzioneReato" >
  
     <jsp:include page="/jsp/files/siap/siep/penasospesa/IncLoadEstinzioneReato.jsp"/>
 
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
  var frmvalidator  = new Validator("LoadInserisciRichiestaEstinzioneReato");
		frmvalidator.setAddnlValidationFunction("Verify");
</script>
  </FORM>
</body>
</html>