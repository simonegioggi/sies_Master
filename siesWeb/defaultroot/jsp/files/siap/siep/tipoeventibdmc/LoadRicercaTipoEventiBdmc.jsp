<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"%>
<%@ page import="siap.siep.tipoeventibdmc.action.ICostantiTipoEventiBdmc"%>
<jsp:useBean id="tipoeventibdmc" scope="request" class="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"/>

<html>
<head>
  <title> Ricerca TipoEventiBdmc </title>
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
      /* Esempio:
      if (document.LoadInserisciTipoEventiBdmc.<%="ICostantiTipoEventiBdmc.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciTipoEventiBdmc.<%="ICostantiTipoEventiBdmc.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca TipoEventiBdmc</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaTipoEventiBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siep.tipoeventibdmc.action.ActRicercaTipoEventiBdmc">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Tipo Eventi Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(tipoeventibdmc.getIdTipoEventiBdmc()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_ID_TIPO_EVENTI_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Evento</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(tipoeventibdmc.getCodTipoEvento()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_TIPO_EVENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Provvedimento</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(tipoeventibdmc.getCodProvvedimento()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_PROVVEDIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Motivo</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(tipoeventibdmc.getCodMotivo()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_MOTIVO %>"  
               > 
      </td> 
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaTipoEventiBdmc");

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

  //frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_ID_TIPO_EVENTI_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_TIPO_EVENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_PROVVEDIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_MOTIVO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>