<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>


<%@ page import="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.action.ICostantiFascicoloSiepBdmc"%>

<jsp:useBean id="fascicolosiepbdmc" scope="request" class="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"/>


<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>

<jsp:useBean id="descrLuogoEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<jsp:useBean id="codUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="codProv" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>


<html>
<head>
  <title> Ricerca Associazione Fascicolo Siep-Bdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >

    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================

    function getAutbdmc(){
    	var objsel = document.getElementById("autbdmc");
    	var objsel1 = document.getElementById("luogodbmc");

    	if (objsel.selectIndex <0) return false;
    	
	   	if ((objsel.options[objsel.selectedIndex].text != "-" && objsel1.value != "") || (objsel.options[objsel.selectedIndex].text == "-" && objsel1.value == "")) {
    		document.all.<%=ICostantiFascicoloSiepBdmc.CAMPO_DESCR_AUTEMI_BDMC%>.value = objsel.options[objsel.selectedIndex].text; 
			// submit
			strUrl= "Main.jsp";
 			document.LoadRicercaFascicoloSiepBdmc.action=strUrl;
 			document.all.<%=ISIAPCostantiWeb.ACTION_FIELD%>.value ="siap.bdmc.fascicolosiepbdmc.action.ActRicercaFascicoloSiepBdmc";
 			document.LoadRicercaFascicoloSiepBdmc.submit();
	    	return true;
    	}
    	else if ((objsel.options[objsel.selectedIndex].text != "-" && objsel1.value == "") || (objsel.options[objsel.selectedIndex].text == "-" && objsel1.value != "")){
			alert("Per selezionare l'ufficio BDMC è obbligatorio inserire l'autorità emittente e il luogo");
		    return false;
    	}
    	else
	    	return false;
    }
  
     function ListaComuni(a_formname,a_fieldname){
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
	return true;
    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca Associazione Fascicolo Siep-Bdmc</font>
      </td>
    </tr>
  </table>

<!-- <FORM method="POST" action="Main.jsp" name="LoadRicercaFascicoloSiepBdmc">  -->
<FORM method="POST" action="" name="LoadRicercaFascicoloSiepBdmc">

  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.fascicolosiepbdmc.action.ActRicercaFascicoloSiepBdmc">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Estremi del fascicolo BDMC</td>
    </tr>
    <tr>
      <td class="l">Anno / Numero</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveAnnoBdmc()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_BDMC %>"  
               > 
		<font class="campo">/</font>               
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveProgrBdmc()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_BDMC %>"  
               > 
      </td> 
    </tr>
	<tr>
      <td class="l">Autorità Bdmc</td>
      <td class="L" >
          <select id="autbdmc" Title="Autorità Bdmc" name="<%= ICostantiFascicoloSiepBdmc.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
          		<%=autoritaEmi%>
          </select>
      </td>
		<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_DESCR_AUTEMI_BDMC%>">
	</tr>
	<tr>
      <td class="l">Luogo Ufficio Bdmc</td>
      <td class="L">
         <input id="luogodbmc" Title="Luogo Ufficio Bdmc" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_COD_LUOGO_EMITTENTE%>"
            value="<%=descrLuogoEmittente%>" type="text" maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadRicercaFascicoloSiepBdmc','<%= ICostantiFascicoloSiepBdmc.CAMPO_COD_LUOGO_EMITTENTE %>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
	</tr>
    <tr>
      <td class="l">Estremi del fascicolo SIEP</td>
    </tr>
    <tr>
      <td class="l">Anno / Numero</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveAnnoSiep()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_SIEP %>"  
               > 
		<font class="campo">/</font>               
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(fascicolosiepbdmc.getChiaveProgrSiep()) %>"
               name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ufficio</td>
      <td class="L"> 
        <span><%=descrUfficio + " - " + descrComune + " (" + codProv + ")"%></span>
        <input type="hidden" 
	      name="<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_SIEP %>"  
	      value="<%=codUfficio%>">
		<input type="HIDDEN" name="<%=ICostantiFascicoloSiepBdmc.CAMPO_DESCR_AUTEMI_SIEP%>" value = "<%=descrUfficio + " - " + descrComune + " (" + codProv + ")"%>">
      </td> 
    </tr>
    <tr>
      <td class="l">Trasmissione</td>
      <td class="L" >
          <select id="trasm" Title="Trasmissione" name="<%= ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE %>">
				<option value="">-
				<option value="S">SI
				<option value="N">NO
          </select>
      </td>
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma" onclick= "Javascript:getAutbdmc();">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaFascicoloSiepBdmc");

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
	frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_BDMC %>","maxlen=4");
  //frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_BDMC %>",);
  	frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_BDMC %>","numeric","Il Numero Progressivo bdmc è un campo numerico");
  	frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_ANNO_SIEP %>","maxlen=4");
  //frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_UFFICIO_SIEP %>",);
  	frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_CHIAVE_PROGR_SIEP %>","numeric","Il Numero Progressivo siep è un campo numerico");
  	frmvalidator.addValidation("<%= ICostantiFascicoloSiepBdmc.CAMPO_FLAG_TRASMISSIONE %>","alphanumeric");

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>