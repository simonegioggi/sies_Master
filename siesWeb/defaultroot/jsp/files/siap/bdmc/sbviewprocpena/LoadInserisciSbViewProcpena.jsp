<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewprocpena" scope="request" class="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"/>

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
  <title> Gestione SbViewProcpena </title>
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
      if (document.LoadInserisciSbViewProcpena.<%="ICostantiSbViewProcpena.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewProcpena.<%="ICostantiSbViewProcpena.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Pass Giud' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_PASS_GIUD%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_PASS_GIUD%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Pass Giud non corretta'); 
        document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Sent 1gra' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_1GRA%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_1GRA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent 1gra non corretta'); 
        document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Sent 2gra' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_2GRA%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_2GRA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent 2gra non corretta'); 
        document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Sent Gipp Gupp' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_GIPP_GUPP%>.value+'/'+ 
                           document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_GIPP_GUPP%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent Gipp Gupp non corretta'); 
        document.LoadInserisciSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP%>.focus(); 
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
        SbViewProcpenaModel lSbViewProcpena = new SbViewProcpenaModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.sbviewprocpena.action.ActInserisciSbViewProcpena"; 
        %>
        <font class="campo">Inserimento SbViewProcpena</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.sbviewprocpena.action.ActModificaSbViewProcpena";
          lSbViewProcpena = sbviewprocpena;
        %>
        <font class="campo">Modifica SbViewProcpena</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSbViewProcpena">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Codi Uffi Pmpm</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_PMPM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Pmpm</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_PMPM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Pmpm</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_PMPM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_GIPP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_GIPP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_GIPP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_DIBB %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_DIBB %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_DIBB %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiUffiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_COAP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeRegiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_COAP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoRegiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_COAP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Pass Giud</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataPassGiud(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataPassGiud(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataPassGiud(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_GIGU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_GIGU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_GIGU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_GIGU %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent 1gra</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent1gra(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent1gra(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent1gra(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno Sent 1gra</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSent1gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Sent 1gra</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSent1gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent 2gra</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent2gra(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent2gra(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSent2gra(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno Sent 2gra</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSent2gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_2GRA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Sent 2gra</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSent2gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_2GRA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSentGippGupp(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSentGippGupp(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewProcpena.getDataSentGippGupp(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Nume Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeSentGippGupp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_GIPP_GUPP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoSentGippGupp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_GIPP_GUPP %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_DIBA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_DIBA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_DIBA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnniPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_APPE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getMesiPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_APPE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getGiorPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_APPE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Diba</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_DIBA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Appe</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagReclArreAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_APPE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0089 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0089()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0089 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0090 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0090()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0090 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0091 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0091()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0091 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0092 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0092()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0092 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0093 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0093()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0093 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0094 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0094()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0094 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0095 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0095()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0095 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0096 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0096()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0096 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0097 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0097()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0097 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0098 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0098()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0098 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0099 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti0099()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0099 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 62 (*)</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArti62()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_62 %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0062 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getArti0062Comm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ARTI_0062_COMM %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Art 62bi</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagArt62bi()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ART_62BI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Misu Cust (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiMisuCust()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_MISU_CUST %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Isti Pena</td>
      <td class="l"> 
        <input type="text" maxlength="5" size="5" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiIstiPena()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_ISTI_PENA %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Desc Luog</td>
      <td class="l"> 
        <input type="text" maxlength="200" size="200" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getDescLuog()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_DESC_LUOG %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren (*)</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getIdPren()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst (*)</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getCodiSedeInst()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_SEDE_INST %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc (*)</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Info Sele</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(lSbViewProcpena.getFlagInfoSele()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_INFO_SELE %>"  
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
  var frmvalidator  = new Validator("LoadInserisciSbViewProcpena");

  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0089 %>","req","Il campo Flag Arti 0089 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0090 %>","req","Il campo Flag Arti 0090 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0091 %>","req","Il campo Flag Arti 0091 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0092 %>","req","Il campo Flag Arti 0092 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0093 %>","req","Il campo Flag Arti 0093 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0094 %>","req","Il campo Flag Arti 0094 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0095 %>","req","Il campo Flag Arti 0095 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0096 %>","req","Il campo Flag Arti 0096 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0097 %>","req","Il campo Flag Arti 0097 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0098 %>","req","Il campo Flag Arti 0098 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0099 %>","req","Il campo Flag Arti 0099 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_62 %>","req","Il campo Flag Arti 62 è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_MISU_CUST %>","req","Il campo Codi Misu Cust è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ID_PREN %>","req","Il campo Id Pren è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_SEDE_INST %>","req","Il campo Codi Sede Inst è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_FASC_BDMC %>","req","Il campo Nume Fasc Bdmc è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_FASC_BDMC %>","req","Il campo Anno Fasc Bdmc è obbligatorio");

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

  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_PMPM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_PMPM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_PMPM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_GIPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_GIPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_GIPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_DIBB %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_DIBB %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_DIBB %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_COAP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_COAP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_COAP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_PASS_GIUD %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_PASS_GIUD %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_GIGU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_GIGU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_GIGU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_GIGU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_1GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_1GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_2GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_2GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_2GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_2GRA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_GIPP_GUPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_GIPP_GUPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_GIPP_GUPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_GIPP_GUPP %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_DIBA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_DIBA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_DIBA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_APPE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_APPE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_APPE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_DIBA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_APPE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0089 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0090 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0091 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0092 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0093 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0094 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0095 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0096 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0097 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0098 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0099 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_62 %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ARTI_0062_COMM %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_ART_62BI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_MISU_CUST %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_ISTI_PENA %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_DESC_LUOG %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_CODI_SEDE_INST %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_FLAG_INFO_SELE %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>