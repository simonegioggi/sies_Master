<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"%>
<%@ page import="siap.siep.tipoeventibdmc.action.ICostantiTipoEventiBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoeventibdmc" scope="request" class="siap.siep.tipoeventibdmc.model.TipoEventiBdmcModel"/>

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
  <title> Gestione TipoEventiBdmc </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
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

      <%  if( modalita.equals("I") )  {%> 
      var msgConfirm = "Si vuole procedere con l'inserimento dei dati?"; 
      <%} else if( modalita.equals("M") ) { %> 
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
        TipoEventiBdmcModel lTipoEventiBdmc = new TipoEventiBdmcModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.tipoeventibdmc.action.ActInserisciTipoEventiBdmc"; 
        %>
        <font class="campo">Inserimento TipoEventiBdmc</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.siep.tipoeventibdmc.action.ActModificaTipoEventiBdmc";
          lTipoEventiBdmc = tipoeventibdmc;
        %>
        <font class="campo">Modifica TipoEventiBdmc</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciTipoEventiBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Tipo Eventi Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lTipoEventiBdmc.getIdTipoEventiBdmc()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_ID_TIPO_EVENTI_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Evento (*)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodTipoEvento()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_TIPO_EVENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Provvedimento (*)</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodProvvedimento()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_PROVVEDIMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Motivo (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(lTipoEventiBdmc.getCodMotivo()) %>"
               name="<%= ICostantiTipoEventiBdmc.CAMPO_COD_MOTIVO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciTipoEventiBdmc");

  frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_ID_TIPO_EVENTI_BDMC %>","req","Il campo Id Tipo Eventi Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_TIPO_EVENTO %>","req","Il campo Cod Tipo Evento è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_PROVVEDIMENTO %>","req","Il campo Cod Provvedimento è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiTipoEventiBdmc.CAMPO_COD_MOTIVO %>","req","Il campo Cod Motivo è obbligatorio");

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