<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbpren.model.SbPrenModel"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<jsp:useBean id="sbpren" scope="request" class="siap.bdmc.sbpren.model.SbPrenModel"/>

<html>
<head>
  <title> Ricerca Prenotazione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      
      if ((document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_ID_PREN%>.value=="" ) && (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_COGN_SOGG%>.value=="" ) && (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_NOME_SOGG%>.value=="" ))  { 
        alert("Inserire un criterio di ricerca : \n a) Numero Prenotazione \n b) Cognome e Nome"); 
        document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_ID_PREN%>.focus(); 
        return false; 
      } 
      if ( (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_COGN_SOGG%>.value!="" ) && (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_NOME_SOGG%>.value=="" ))  { 
        alert("Inserire il Nome"); 
        document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_ID_PREN%>.focus(); 
        return false; 
      }
      if ( (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_COGN_SOGG%>.value=="" ) && (document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_NOME_SOGG%>.value!="" ))  { 
        alert("Inserire il Cognome"); 
        document.LoadRicercaSbPren.<%=ICostantiSbPren.CAMPO_ID_PREN%>.focus(); 
        return false; 
      }
    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca Prenotazione</font>
      </td>
    </tr>
  </table>
<FORM method="POST" action="Main.jsp" name="LoadRicercaSbPren">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbpren.action.ActRicercaSbPren">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Numero Prenotazione</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15"  
               value="<%=StringUtils.toStringJSP(sbpren.getIdPren()) %>"
               name="<%= ICostantiSbPren.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cognome</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(sbpren.getCognSogg()) %>"
               name="<%= ICostantiSbPren.CAMPO_COGN_SOGG %>"  
               > 
      </td> 
    </tr>
   
    <tr>
      <td class="l">Nome</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(sbpren.getNomeSogg()) %>"
               name="<%= ICostantiSbPren.CAMPO_NOME_SOGG %>"  
               > 
      </td> 
    </tr>
    
    
    <tr>
      <td align="center">
      <br>
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
  <br>
  <a class="menu"> <b><font style="FONT-SIZE: 13px;"> NB: La ricerca può essere effettuata inserendo il numero di Prenotazione e/o la coppia Cognome e Nome </font> </b></a> 
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaSbPren");

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
 // frmvalidator.addValidation("","alpha","Il <%= ICostantiSbPren.CAMPO_COGN_SOGG %> è un campo alfabetico");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ID_PREN %>","numeric","Il campo Numero Prenotazione deve essere numerico");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_COGN_SOGG %>","alpha","Il campo Cognome deve essere alfabetico");
  frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_NOME_SOGG %>","alpha","Il campo Nome deve essere alfabetico");
  
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>