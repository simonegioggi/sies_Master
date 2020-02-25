<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.notifichesies.model.NotificheSiesModel"%>
<%@ page import="siap.bdmc.notifichesies.action.ICostantiNotificheSies"%>
<jsp:useBean id="notifichesies" scope="request" class="siap.bdmc.notifichesies.model.NotificheSiesModel"/>

<html>
<head>
  <title> Ricerca NotificheSies </title>
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
      if (document.LoadInserisciNotificheSies.<%="ICostantiNotificheSies.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciNotificheSies.<%="ICostantiNotificheSies.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Notifica' 
      //=============================================================
      var data_to_verify = document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_NOTIFICA%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_NOTIFICA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Notifica non corretta'); 
        document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Trasmissione' 
      //=============================================================
      var data_to_verify = document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_TRASMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Trasmissione non corretta'); 
        document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadRicercaNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
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
                      <font class="campo">Ricerca NotificheSies</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaNotificheSies">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.notifichesies.action.ActRicercaNotificheSies">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Notifiche Sies</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getIdNotificheSies()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ID_NOTIFICHE_SIES %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Siep</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getAnnoSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Siep</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getProgSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_PROG_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ufficio Siep</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(notifichesies.getUfficioSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_UFFICIO_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getAnnoFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ufficio Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(notifichesies.getUfficioFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_UFFICIO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="17" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getNumeroFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_NUMERO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Tipo Notifica</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(notifichesies.getTipoNotifica()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_TIPO_NOTIFICA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Notifica</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataNotifica(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataNotifica(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataNotifica(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Stato Trasmissione</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(notifichesies.getStatoTrasmissione()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_STATO_TRASMISSIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataTrasmissione(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataTrasmissione(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataTrasmissione(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getIdPren()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Peri Pres</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(notifichesies.getProgPeriPres()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_PROG_PERI_PRES %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(notifichesies.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifichesies.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(notifichesies.getCodUfficioInserimento()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
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
  var frmvalidator  = new Validator("LoadRicercaNotificheSies");

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

  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ID_NOTIFICHE_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ANNO_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_PROG_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_UFFICIO_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_UFFICIO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_NUMERO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_TIPO_NOTIFICA %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_MESE_DATA_NOTIFICA %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_NOTIFICA %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_STATO_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_MESE_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_TRASMISSIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_PROG_PERI_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_COD_OPERATORE_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_MESE_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_COD_UFFICIO_INSERIMENTO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>