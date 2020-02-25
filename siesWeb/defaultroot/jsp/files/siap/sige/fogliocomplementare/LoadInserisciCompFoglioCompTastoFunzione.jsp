<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sige.fogliocomplementare.action.ICostantiFoglioComp" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="documentoAllegato"     scope="request" class="siap.sige.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="evento" 				scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UtenteConnesso"        scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="modalita" 			 	scope="request" class="java.lang.String" />
<jsp:useBean id="motivoNonInvio"        scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="provvSige"             scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<%
String annoDataCompilazione=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(),"yyyy"));
String meseDataCompilazione=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(),"MM"));
String giornoDataCompilazione=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(),"dd"));

String annoDataInsManuale=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan() ,"yyyy"));
String meseDataInsManuale=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan() ,"MM"));
String giornoDataInsManuale=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan() ,"dd"));
%>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<html>
	<head>
  	<title>[S.I.E.S.] - Inserimento Foglio Complementare</title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<%
String lAzione = "siap.sige.fogliocomplementare.action.ActInserisciCompFoglioComp";
if( modalita.equalsIgnoreCase("M") ) {
    lAzione = "siap.sige.fogliocomplementare.action.ActModificaCompFoglioComp";
}
  
String descrizioneProvvedimento="";
 
  if (provvSige != null && provvSige.getProvvedimento() != null){ 
	if(provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("02")){
		descrizioneProvvedimento="DECRETO n°";
	} else if (provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("03")){
		descrizioneProvvedimento="ORDINANZA n°";
	}
}	

%>

  <script language="JavaScript">
  
  function calendario(a_formname,a_field_year,a_field_month,a_field_day)    {
      desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }

  function Verify()
  {
    if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value.length==1)
        document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value;

    if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value.length==1)
        document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value;

    // Controllo validità data Emissione.
    var dataCompilTrasm = document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value +'/'+
                          document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value +'/'+
                          document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;

    if (! ControllaData(dataCompilTrasm))
    {
      alert('Data Compilazione/Trasmissione non valida!');
      return false;
    }

    // Controllo validità data Inserimento Manuale.
    var dataInsmanuale = document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
                         document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
                         document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
    if (! ControllaDataPassaVuota(dataInsmanuale))
    {
      alert('Data Inserimento Manuale non valida!');
      return false;
    }
              	    
    return true;
  }

  function VerifyConferma()   
  {
  }  
  </script>
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
           <font class="label">Funzione :</font>&nbsp;
           <font class="campo">Inserimento Foglio Complementare</font>
        </td>
  		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br>

   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>

<br />
<%-- 
    <!-- LISTA DEI FOGLI ANNULLATI -->
  <jsp:include page="<%=ICostantiFoglioComp.PG_LISTA_CFC_ANNULLATI%>">
  	<jsp:param name="ActionLink" value="siap.sige.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp" />
  </jsp:include>
--%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCompFoglioComp">
  
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo" colspan=6>Estremi Provvedimento</td>
    </tr>

    <tr>
      <td class="l">
      <%=descrizioneProvvedimento %>
      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveAnno())%></font>
      <font class="l">/</font>
      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveProgr())%></font>
      <font class="l">del</font>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSige.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getIdEvento()%>')">
	        <img id="generaStampa" align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
	  </a>
    </td>
    </tr>

    <tr>
      <td class="l">Data Compilazione/Trasmissione <font class=ob>(*)</font></td>
      <td class="L">
        <input value="<%=giornoDataCompilazione %>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE %>"
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
        				onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=meseDataCompilazione %>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE %>"
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
        				onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=annoDataCompilazione %>" type="text" size="4" maxlength="4" name="<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE %>"
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"
        				onBlur="javascript:value=FillYear(value)">
        				
      <!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciCompFoglioComp','<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>','<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>','<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>  				
      </td>
    </tr>

    <tr>
      <td class="l">Data Inserimento Manuale</td>
      <td class="L">
        <input value="<%=giornoDataInsManuale %>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE %>"  
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
        				onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=meseDataInsManuale %>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE %>"  
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
        				onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=annoDataInsManuale %>" type="text" size="4" maxlength="4" name="<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE %>"  
        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
        				onBlur="javascript:value=FillYear(value)">
        <a href="javascript:calendario('LoadInserisciCompFoglioComp','<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>','<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>','<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>');">
       			<img src="/images/calendario.gif" border=0>
       </a>  				
      </td>
    </tr>    
    
    <tr>
      <td>
        <font class="L"> </font>&nbsp;
      </td>
    </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onClick="javascript:return VerifyConferma();">
      </td>
    </tr>

  </table>
  <input type="hidden" name="Provenienza" value="tastoFunzione" />
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP( evento.getIdEvento(), "" )%>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  >
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP( documentoAllegato.getIdDocumentoAllegato(), "" )%>" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>"  >
  <input type="HIDDEN" value="<%=documentoAllegato.getDataTrasmissione()%>" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>" >
  <input type="HIDDEN" value="" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>" >
      
  </FORM>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciCompFoglioComp");
    // Controllo data Compilazione/Trasmissione.
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Compilazione/Trasmissione deve essere di 4 caratteri");

    // Controllo data inserimento manuale.
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>","minlen=4","La lunghezza del campo Anno Data Inserimento Manuale deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>