<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>
<jsp:useBean id="scambiosanzione" scope="request" class="siap.siep.scambiosanzione.model.ScambioSanzioneModel"/>

<html>
<head>
  <title> Ricerca ScambioSanzione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
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
      // controllo corretteza campo 'Data Inizio' 
      //=============================================================
      var data_to_verify = document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_INIZIO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_INIZIO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inizio non corretta'); 
        document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Fine' 
      //=============================================================
      var data_to_verify = document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_FINE%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_FINE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine non corretta'); 
        document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Aggiornamento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_AGGIORNAMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Aggiornamento non corretta'); 
        document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Emissione' 
      //=============================================================
      var data_to_verify = document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Emissione non corretta'); 
        document.LoadRicercaScambioSanzione.<%=ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE%>.focus(); 
        return false; 
      } 

    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca ScambioSanzione</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaScambioSanzione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scambiosanzione.action.ActRicercaScambioSanzione">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Scambio Sanzione</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getIdScambioSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Decisione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodTipoDecisione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_DECISIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Natura Sanzione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodNaturaSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_NATURA_SANZIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Sanzione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodTipoSanzione()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inizio</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInizio(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInizio(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInizio(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataFine(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataFine(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataFine(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="2000" size="2000" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getNote()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_NOTE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Registro</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getAnnoRegistro()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_REGISTRO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Registro</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="13" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getNumeroRegistro()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_NUMERO_REGISTRO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Chiave Anno Fascicolo Sius</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getChiaveAnnoFascicoloSius()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Chiave Progr Fascicolo Sius</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="13" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getChiaveProgrFascicoloSius()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Sorveglianza</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioSorveglianza()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_SORVEGLIANZA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Emittente</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioEmittente()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_EMITTENTE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioInserimento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodOperatoreAggiornamento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Aggiornamento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataAggiornamento(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataAggiornamento(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataAggiornamento(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getCodUfficioAggiornamento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"dd")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_GIORNO_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"MM")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_MESE_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(scambiosanzione.getDataEmissione(),"yyyy")) %>" 
               name="<%= ICostantiScambioSanzione.CAMPO_ANNO_DATA_EMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Eve Id Evento</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(scambiosanzione.getEveIdEvento()) %>"
               name="<%= ICostantiScambioSanzione.CAMPO_EVE_ID_EVENTO %>"  
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
  var frmvalidator  = new Validator("LoadRicercaScambioSanzione");

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