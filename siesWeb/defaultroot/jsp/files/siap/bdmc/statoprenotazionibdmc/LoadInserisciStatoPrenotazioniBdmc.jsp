<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"%>
<%@ page import="siap.bdmc.statoprenotazionibdmc.action.ICostantiStatoPrenotazioniBdmc"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="statoprenotazionibdmc" scope="request" class="siap.bdmc.statoprenotazionibdmc.model.StatoPrenotazioniBdmcModel"/>

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
  <title> Gestione StatoPrenotazioniBdmc </title>
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
      if (document.LoadInserisciStatoPrenotazioniBdmc.<%="ICostantiStatoPrenotazioniBdmc.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciStatoPrenotazioniBdmc.<%="ICostantiStatoPrenotazioniBdmc.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Trasmissione' 
      //=============================================================
      var data_to_verify = document.LoadInserisciStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadInserisciStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadInserisciStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Trasmissione non corretta'); 
        document.LoadInserisciStatoPrenotazioniBdmc.<%=ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus(); 
        return false; 
      } 

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
        StatoPrenotazioniBdmcModel lStatoPrenotazioniBdmc = new StatoPrenotazioniBdmcModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.statoprenotazionibdmc.action.ActInserisciStatoPrenotazioniBdmc"; 
        %>
        <font class="campo">Inserimento StatoPrenotazioniBdmc</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.statoprenotazionibdmc.action.ActModificaStatoPrenotazioniBdmc";
          lStatoPrenotazioniBdmc = statoprenotazionibdmc;
        %>
        <font class="campo">Modifica StatoPrenotazioniBdmc</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciStatoPrenotazioniBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Stato Prenotazioni Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getStatoPrenotazioniBdmc()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_STATO_PRENOTAZIONI_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Misura Cautelare Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getIdMisuraCautelareBdmc()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_MISURA_CAUTELARE_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoPrenotazioniBdmc.getDataTrasmissione(),"dd")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoPrenotazioniBdmc.getDataTrasmissione(),"MM")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoPrenotazioniBdmc.getDataTrasmissione(),"yyyy")) %>" 
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Esito Id</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getEsitoId()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_ID %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Esito Msg</td>
      <td class="l"> 
        <input type="text" maxlength="1000" size="1000" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getEsitoMsg()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_MSG %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Prenotazione</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getIdPrenotazione()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_PRENOTAZIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Peri Pres</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getProgPeriPres()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_PROG_PERI_PRES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Tipo Trasmissione</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lStatoPrenotazioniBdmc.getTipoTrasmissione()) %>"
               name="<%= ICostantiStatoPrenotazioniBdmc.CAMPO_TIPO_TRASMISSIONE %>"  
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
  var frmvalidator  = new Validator("LoadInserisciStatoPrenotazioniBdmc");

  frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_STATO_PRENOTAZIONI_BDMC %>","req","Il campo Stato Prenotazioni Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_MISURA_CAUTELARE_BDMC %>","req","Il campo Id Misura Cautelare Bdmc è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_STATO_PRENOTAZIONI_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_MISURA_CAUTELARE_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_GIORNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_MESE_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ANNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_ID %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ESITO_MSG %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_ID_PRENOTAZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_PROG_PERI_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiStatoPrenotazioniBdmc.CAMPO_TIPO_TRASMISSIONE %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>