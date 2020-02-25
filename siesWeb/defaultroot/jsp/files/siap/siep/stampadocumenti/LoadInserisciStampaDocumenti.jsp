<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.stampadocumenti.model.StampaDocumentiModel"%>
<%@ page import="siap.siep.stampadocumenti.action.ICostantiStampaDocumenti"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="stampadocumenti" scope="request" class="siap.siep.stampadocumenti.model.StampaDocumentiModel"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>

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
  <title> Gestione StampaDocumenti </title>
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
      if (document.LoadInserisciStampaDocumenti.<%="ICostantiStampaDocumenti.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciStampaDocumenti.<%="ICostantiStampaDocumenti.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data' 
      //=============================================================
      var data_to_verify = document.LoadInserisciStampaDocumenti.<%=ICostantiStampaDocumenti.CAMPO_GIORNO_DATA%>.value+'/'+ 
                           document.LoadInserisciStampaDocumenti.<%=ICostantiStampaDocumenti.CAMPO_MESE_DATA%>.value+'/'+ 
                           document.LoadInserisciStampaDocumenti.<%=ICostantiStampaDocumenti.CAMPO_ANNO_DATA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data non corretta'); 
        document.LoadInserisciStampaDocumenti.<%=ICostantiStampaDocumenti.CAMPO_GIORNO_DATA%>.focus(); 
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
        StampaDocumentiModel lStampaDocumenti = new StampaDocumentiModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.stampadocumenti.action.ActInserisciStampaDocumenti"; 
        %>
        <font class="campo">Inserimento StampaDocumenti</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.siep.stampadocumenti.action.ActModificaStampaDocumenti";
          lStampaDocumenti = stampadocumenti;
        %>
        <font class="campo">Modifica StampaDocumenti</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciStampaDocumenti">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Stampa (*)</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStampaDocumenti.getIdStampa()) %>"
               name="<%= ICostantiStampaDocumenti.CAMPO_ID_STAMPA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Utente (*)</td>
      <td class="l"> 
        <input type="text" maxlength="60" size="60" 
               value="<%=StringUtils.toStringJSP(lStampaDocumenti.getIdUtente()) %>"
               name="<%= ICostantiStampaDocumenti.CAMPO_ID_UTENTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data (*)</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStampaDocumenti.getData(),"dd")) %>" 
               name="<%= ICostantiStampaDocumenti.CAMPO_GIORNO_DATA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStampaDocumenti.getData(),"MM")) %>" 
               name="<%= ICostantiStampaDocumenti.CAMPO_MESE_DATA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lStampaDocumenti.getData(),"yyyy")) %>" 
               name="<%= ICostantiStampaDocumenti.CAMPO_ANNO_DATA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Stato</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(lStampaDocumenti.getStato()) %>"
               name="<%= ICostantiStampaDocumenti.CAMPO_STATO %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Num Stampe Richieste</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStampaDocumenti.getNumStampeRichieste()) %>"
               name="<%= ICostantiStampaDocumenti.CAMPO_NUM_STAMPE_RICHIESTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Stampe Effettuate</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lStampaDocumenti.getNumeStampeEffettuate()) %>"
               name="<%= ICostantiStampaDocumenti.CAMPO_NUME_STAMPE_EFFETTUATE %>"  
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
  var frmvalidator  = new Validator("LoadInserisciStampaDocumenti");

  frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ID_STAMPA %>","req","Il campo Id Stampa è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ID_UTENTE %>","req","Il campo Id Utente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_GIORNO_DATA %>","req","Il campo Data è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_MESE_DATA %>","req","Il campo Data è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ANNO_DATA %>","req","Il campo Data è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ID_STAMPA %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ID_UTENTE %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_GIORNO_DATA %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_MESE_DATA %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_ANNO_DATA %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_STATO %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_NUM_STAMPE_RICHIESTE %>",);
  //frmvalidator.addValidation("<%= ICostantiStampaDocumenti.CAMPO_NUME_STAMPE_EFFETTUATE %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>