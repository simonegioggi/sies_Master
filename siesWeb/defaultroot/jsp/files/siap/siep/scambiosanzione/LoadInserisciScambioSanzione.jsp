<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="scambiosanzione" scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>

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
  <title> Gestione ScambioSanzione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciScambioSanzione.<%="ICostantiScambioSanzione.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciScambioSanzione.<%="ICostantiScambioSanzione.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Inizio' 
      //=============================================================
      var data_to_verify = document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_INIZIO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_INIZIO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inizio non corretta'); 
        document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Fine' 
      //=============================================================
      var data_to_verify = document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_FINE%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_FINE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine non corretta'); 
        document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Aggiornamento' 
      //=============================================================
      var data_to_verify = document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_AGGIORNAMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Aggiornamento non corretta'); 
        document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Emissione' 
      //=============================================================
      var data_to_verify = document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Emissione non corretta'); 
        document.LoadInserisciScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus(); 
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
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        ScambioSanzioneModel lScambioSanzione = new ScambioSanzioneModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.scambiosanzione.action.ActInserisciScambioSanzione"; 
        %>
        <font class="campo">Inserimento ScambioSanzione</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.siep.scambiosanzione.action.ActModificaScambioSanzione";
          lScambioSanzione = scambiosanzione;
        %>
        <font class="campo">Modifica ScambioSanzione</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciScambioSanzione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Scambio Sanzione (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getIdScambioSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Decisione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodTipoDecisione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Natura Sanzione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodNaturaSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Sanzione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodTipoSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inizio</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInizio(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInizio(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInizio(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataFine(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataFine(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataFine(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="2000" size="2000" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getNote()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_NOTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Registro</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getAnnoRegistro()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Registro</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="13" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getNumeroRegistro()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Chiave Anno Fascicolo Sius</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getChiaveAnnoFascicoloSius()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Chiave Progr Fascicolo Sius</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="13" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getChiaveProgrFascicoloSius()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Sorveglianza</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioSorveglianza()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_SORVEGLIANZA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Emittente</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioEmittente()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioInserimento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodOperatoreAggiornamento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Aggiornamento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataAggiornamento(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataAggiornamento(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataAggiornamento(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getCodUfficioAggiornamento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataEmissione(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataEmissione(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScambioSanzione.getDataEmissione(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Eve Id Evento</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lScambioSanzione.getEveIdEvento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_EVE_ID_EVENTO %>"  
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
  var frmvalidator  = new Validator("LoadInserisciScambioSanzione");

  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE %>","req","Il campo Id Scambio Sanzione è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_SORVEGLIANZA %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_EVE_ID_EVENTO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>