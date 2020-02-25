<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.action.ICostantiFascicoloSiepBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolosiepbdmc" scope="request" class="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>


<jsp:useBean id="descrLuogoEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<jsp:useBean id="codUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="codProv" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>

<% 
//=========================================================================== 
// Inserire la condizione in base alla quale i campi non sono modificabili 
// (se esiste) 
//=========================================================================== 
String readonly = ""; 
//if ( modalita.equals("M" && ?????) ){ 
//  readonly  = "readonly=readonly"; 
//} 
%> 

<html>
<head>
  <title> Gestione Associazione Fascicolo Siep-Bdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================

    function getAutbdmc(){
    	var objsel = document.getElementById("autbdmc");
    	if (objsel.selectIndex <0) return false;
    	document.all.<%=ICostantiFascicoloSiepBdmc.CAMPO_DESCR_AUTEMI_BDMC%>.value = objsel.options[objsel.selectedIndex].text; 
    	return true;
    }
  
     function ListaComuni(a_formname,a_fieldname){
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciFascicoloSiepBdmc.<%="ICostantiFascicoloSiepBdmc.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciFascicoloSiepBdmc.<%="ICostantiFascicoloSiepBdmc.CAMPO_"%>.focus(); 
        return false; 
      } 
      */
	
      <%  if( modalita.equalsIgnoreCase("M"))  {%> 
	      if (document.LoadInserisciFascicoloSiepBdmc.<%=ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE%>.value=="" ) { 
	        alert("Inserire l'indicatore di Trasmissione!"); 
	        document.LoadInserisciFascicoloSiepBdmc.<%=ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE%>.focus(); 
	        return false; 
	      } 
      <%}%>


      <%  if( modalita.equalsIgnoreCase("I")){%> 
      var msgConfirm = "Si vuole procedere con l'inserimento dei dati?"; 
      <%} else if( modalita.equalsIgnoreCase("M")) { %> 
      var msgConfirm = "Si vuole procedere con la modifica dei dati?"; 
      <%}%> 
      if (window.confirm(msgConfirm)) 
        return true; 
      else 
        return false; 
    } 
  </script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        FascicoloSiepBdmcModel lFascicoloSiepBdmc = new FascicoloSiepBdmcModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.fascicolosiepbdmc.action.ActInserisciFascicoloSiepBdmc"; 
        %>
        <font class="campo">Inserimento Associazione Fascicolo Siep-Bdmc</font>
        <%
        }
        else  if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.fascicolosiepbdmc.action.ActModificaFascicoloSiepBdmc";
          lFascicoloSiepBdmc = fascicolosiepbdmc;
          %>
        <font class="campo">Modifica Associazione Fascicolo Siep-Bdmc</font>
        <%}%>
      </td>
    </tr>
  </table>
<FORM method="POST" action="Main.jsp" name="LoadInserisciFascicoloSiepBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Anno / Numero Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveAnnoBdmc()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_BDMC %>"  
               <%=readonly%> 
               > 
		<font class="campo">/</font>
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveProgrBdmc()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
	<tr>
      <td class="l">Autorità Bdmc (*)</td>
      <td class="L" >
          <select id="autbdmc" Title="Autorità Bdmc" name="<%= ICostantiFascicoloSiepBdmc.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
          		<%=autoritaEmi%>
          </select>
      </td>
		<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_DESCR_AUTEMI_BDMC%>">
	</tr>
	<tr>
      <td class="l">Luogo Ufficio Bdmc (*)</td>
      <td class="L">
         <input Title="Luogo Ufficio Bdmc" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_LUOGO_EMITTENTE%>"
            value="<%=descrLuogoEmittente%>" type="text" maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciFascicoloSiepBdmc','<%= ICostantiFascicoloSiepBdmc.CAMPO_COD_LUOGO_EMITTENTE %>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
	</tr>
    <tr>
      <td class="l"> Ufficio Siep (*)</td>
      <td class="L"> 
        <span><%=descrUfficio + " - " + descrComune + " (" + codProv + ")"%></span>
        <input type="hidden" 
	      name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_SIEP %>"  
	      value="<%=codUfficio%>">
      </td> 
    </tr>
    <tr>
      <td class="l">Anno / Numero Siep (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveAnnoSiep()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_SIEP %>"  
               <%=readonly%> 
               > 
		<font class="campo">/</font>
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getChiaveProgrSiep()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>

 <%   
 if( modalita.equals("M") ) {
 %>    	
	
	
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataTrasmissione(),"dd"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_MESE_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataTrasmissione(),"MM"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_ANNO_DATA_TRASMISSIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataTrasmissione(),"yyyy"))%>">

	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_GIORNO_DATA_DISATTIVAZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataDisattivazione(),"dd"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_MESE_DATA_DISATTIVAZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataDisattivazione(),"MM"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_ANNO_DATA_DISATTIVAZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataDisattivazione(),"yyyy"))%>">

	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_GIORNO_DATA_INSERIMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataInserimento(),"dd"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_MESE_DATA_INSERIMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataInserimento(),"MM"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_ANNO_DATA_INSERIMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataInserimento(),"yyyy"))%>">

	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataAggiornamento(),"dd"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_MESE_DATA_AGGIORNAMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataAggiornamento(),"MM"))%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_ANNO_DATA_AGGIORNAMENTO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicoloSiepBdmc.getDataAggiornamento(),"yyyy"))%>">

	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_OPERATORE_INSERIMENTO%>" value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getCodOperatoreInserimento()) %>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_UFFICIO_INSERIMENTO%>" value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getCodUfficioInserimento()) %>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_OPERATORE_AGGIORNAMENTO%>" value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getCodOperatoreAggiornamento()) %>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_UFFICIO_AGGIORNAMENTO%>" value="<%=StringUtils.toStringJSP(lFascicoloSiepBdmc.getCodUfficioAggiornamento()) %>">
    <tr>
      <td class="l">Trasmissione</td>
      <td class="L" >
          <select id="trasm" Title="Trasmissione" name="<%= ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE %>">
	      <% if (StringUtils.toStringJSP(lFascicoloSiepBdmc.getFlagTrasmissione()).equalsIgnoreCase("S")){%>
				<option value="S" selected>SI
				<option value="N">NO
	      <%}%>
	      <% if (StringUtils.toStringJSP(lFascicoloSiepBdmc.getFlagTrasmissione()).equalsIgnoreCase("N")){%>
				<option value="S">SI
				<option value="N"  selected>NO
	      <%}%>
	      <% if (StringUtils.toStringJSP(lFascicoloSiepBdmc.getFlagTrasmissione()).equalsIgnoreCase("")){%>
				<option value="" selected>
				<option value="S">SI
				<option value="N">NO
	      <%}%>
          </select>
      </td>
	</tr>
<%   
    }
%>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma" onclick= "Javascript:getAutbdmc();">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciFascicoloSiepBdmc");

//  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_ID_FASCICOLO_BDMC %>","req","Il campo Id Fascicolo Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_BDMC %>","req","Il campo Chiave Anno Bdmc è obbligatorio");
//  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_BDMC %>","req","Il campo Chiave Ufficio Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_BDMC %>","req","Il campo Chiave Progr Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_SIEP %>","req","Il campo Chiave Anno Siep è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_SIEP %>","req","Il campo Chiave Ufficio Siep è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_SIEP %>","req","Il campo Chiave Progr Siep è obbligatorio");
//  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE %>","req","Il campo Trasmissione è obbligatorio");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  //frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_ID_FASCICOLO_BDMC %>",);
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_BDMC %>","numeric");
  //frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_BDMC %>",);
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_BDMC %>","numeric");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_SIEP %>","numeric");
  //frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_SIEP %>",);
  frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_SIEP %>","numeric");
 
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>