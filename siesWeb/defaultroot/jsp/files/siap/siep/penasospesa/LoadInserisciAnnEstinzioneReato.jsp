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
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>


<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Estinzione di Pena </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

<script language="JavaScript">
function Verify()
  {
		    if (!(document.LoadInserisciAnnEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>.value.length > 1))
		    {
		      alert('Specificare Articolo');
		      return false;
		    }
		    if (!(document.LoadInserisciAnnEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Ufficio Giudice Esecuzione');
		      return false;
		    }
		    if (!(document.LoadInserisciAnnEstinzioneReato.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>.value.length > 1))
		    {
		      alert('Specificare Sede Giudice Esecuzione');
		      return false;
		    }
 }
</script>

</head>

  <body class="corpo" onLoad="javascript:inizia()">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
		EventoModel lEvento = new EventoModel();
		String lAzione = new String();
		if( modalita.equals("I") ){
        	lAzione = "siap.siep.penasospesa.action.ActInserisciAnnEstinzioneReato";
%>
			<font class="campo">Inserimento Annotazione Estinzione Reato </font>
<%
       }
       	else if( modalita.equals("M") )
       {
			lAzione = "siap.siep.penasospesa.action.ActModificaAnnEstinzioneReato";
%>
         	<font class="campo">Modifica Annotazione Estinzione Reato</font>
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
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciAnnEstinzioneReato" >
      <table style="width: 95%;">
	    <tr><td class="Titolo" colspan=4>Estremi del Provvedimento</td></tr>
  </table>

    <table>
    <tr>
      <td class="l">Data Emissione <font class=ob>(*)</font></td>
      <td class="L" >
        <input value=""   type="text" size="2" maxlength="2" name="<%= ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value=""   type="text" size="2" maxlength="2" name="<%= ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
         <td class="l">Anno/Numero <font class=ob>(*)</font></td>
        <td class="l">
       <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_GE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           /<input Title="Numero" type="text" name="<%= ICostantiAnnotazioneManuale.CAMPO_NUMERO_GE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        </td>  
    </tr>
  </table>
      <jsp:include page="/jsp/files/siap/siep/penasospesa/IncAutorita.jsp"/>
      <jsp:include page="/jsp/files/siap/siep/penasospesa/IncLoadEstinzioneReato.jsp"/>	  
  		<table style="width: 95%;">
  		<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      	</td>
      	</tr>
      	</table>
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">     	
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciAnnEstinzioneReato");
    frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE %>","req","Il campo Giorno Emissione è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE%>","gt=1");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE%>","lt=31");

  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE%>","req","Il campo Mese Emissione è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE%>","gt=1");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE%>","lt=12");

  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE%>","req","Il campo Anno Emissione è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE%>","lt=2999");
 
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_GE%>","req","Il campo Anno è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_GE%>","numeric");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_GE%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_GE%>","lt=2999");
  
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_NUMERO_GE%>","req","Il campo Numero è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiAnnotazioneManuale.CAMPO_NUMERO_GE%>","numeric");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
  </FORM>
</body>
</html>