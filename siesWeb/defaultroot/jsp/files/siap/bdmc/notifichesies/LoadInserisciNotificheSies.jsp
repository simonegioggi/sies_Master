<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.notifichesies.model.NotificheSiesModel"%>
<%@ page import="siap.bdmc.notifichesies.action.ICostantiNotificheSies"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="notifichesies" scope="request" class="siap.bdmc.notifichesies.model.NotificheSiesModel"/>

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
  <title> Gestione NotificheSies </title>
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
      if (document.LoadInserisciNotificheSies.<%="ICostantiNotificheSies.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciNotificheSies.<%="ICostantiNotificheSies.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Notifica' 
      //=============================================================
      var data_to_verify = document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_NOTIFICA%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_NOTIFICA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Notifica non corretta'); 
        document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Trasmissione' 
      //=============================================================
      var data_to_verify = document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_TRASMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Trasmissione non corretta'); 
        document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadInserisciNotificheSies.<%=ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
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
        NotificheSiesModel lNotificheSies = new NotificheSiesModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.notifichesies.action.ActInserisciNotificheSies"; 
        %>
        <font class="campo">Inserimento NotificheSies</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.notifichesies.action.ActModificaNotificheSies";
          lNotificheSies = notifichesies;
        %>
        <font class="campo">Modifica NotificheSies</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciNotificheSies">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Notifiche Sies (*)</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getIdNotificheSies()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ID_NOTIFICHE_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Siep</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getAnnoSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Siep</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getProgSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_PROG_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ufficio Siep</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getUfficioSiep()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_UFFICIO_SIEP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getAnnoFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ufficio Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getUfficioFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_UFFICIO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="17" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getNumeroFascBdmc()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_NUMERO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Tipo Notifica</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getTipoNotifica()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_TIPO_NOTIFICA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Notifica</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataNotifica(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataNotifica(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataNotifica(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_NOTIFICA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Stato Trasmissione</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getStatoTrasmissione()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_STATO_TRASMISSIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataTrasmissione(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataTrasmissione(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataTrasmissione(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_TRASMISSIONE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getIdPren()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Peri Pres</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getProgPeriPres()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_PROG_PERI_PRES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotificheSies.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiNotificheSies.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(lNotificheSies.getCodUfficioInserimento()) %>"
               name="<%= ICostantiNotificheSies.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
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
  var frmvalidator  = new Validator("LoadInserisciNotificheSies");

  frmvalidator.addValidation("<%= ICostantiNotificheSies.CAMPO_ID_NOTIFICHE_SIES %>","req","Il campo Id Notifiche Sies è obbligatorio");

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