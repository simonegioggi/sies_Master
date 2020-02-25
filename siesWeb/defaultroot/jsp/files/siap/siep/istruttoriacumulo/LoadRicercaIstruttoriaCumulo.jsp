<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<jsp:useBean id="istruttoriacumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<html>
<head>
  <title> Ricerca IstruttoriaCumulo </title>
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
      if (document.LoadInserisciIstruttoriaCumulo.<%="ICostantiIstruttoriaCumulo.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciIstruttoriaCumulo.<%="ICostantiIstruttoriaCumulo.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Apertura' 
      //=============================================================
      var data_to_verify = document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_APERTURA%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_APERTURA%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_APERTURA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Apertura non corretta'); 
        document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_APERTURA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Chiusura' 
      //=============================================================
      var data_to_verify = document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_CHIUSURA%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_CHIUSURA%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_CHIUSURA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Chiusura non corretta'); 
        document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_CHIUSURA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Aggiornamento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_AGGIORNAMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Aggiornamento non corretta'); 
        document.LoadRicercaIstruttoriaCumulo.<%=ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.focus(); 
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
                      <font class="campo">Ricerca IstruttoriaCumulo</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%= IWebConstants.ROOT_DIR + IWebConstants.PG_MAIN%>" name="LoadRicercaIstruttoriaCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaIstruttoriaCumulo">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Istruttoria Cumulo</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getIdIstruttoriaCumulo()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Fas Sie Id Fascicolo Siep</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getFasSieIdFascicoloSiep()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Eve Id Evento Istr</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getEveIdEventoIstr()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_EVE_ID_EVENTO_ISTR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Eve Id Evento Prov</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getEveIdEventoProv()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_EVE_ID_EVENTO_PROV %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Apertura</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataApertura(),"dd")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_APERTURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataApertura(),"MM")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_APERTURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataApertura(),"yyyy")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_APERTURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Chiusura</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataChiusura(),"dd")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_CHIUSURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataChiusura(),"MM")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_CHIUSURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataChiusura(),"yyyy")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_CHIUSURA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="2000" size="2000" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getNote()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_NOTE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Stato</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getFlagStato()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_FLAG_STATO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getCodUfficioInserimento()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getCodOperatoreAggiornamento()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Aggiornamento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataAggiornamento(),"dd")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataAggiornamento(),"MM")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(istruttoriacumulo.getDataAggiornamento(),"yyyy")) %>" 
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(istruttoriacumulo.getCodUfficioAggiornamento()) %>"
               name="<%= ICostantiIstruttoriaCumulo.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>"  
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
  var frmvalidator  = new Validator("LoadRicercaIstruttoriaCumulo");

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

  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_EVE_ID_EVENTO_ISTR %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_EVE_ID_EVENTO_PROV %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_APERTURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_APERTURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_APERTURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_CHIUSURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_CHIUSURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_CHIUSURA %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_FLAG_STATO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_COD_OPERATORE_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_COD_UFFICIO_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_MESE_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_ANNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiIstruttoriaCumulo.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>