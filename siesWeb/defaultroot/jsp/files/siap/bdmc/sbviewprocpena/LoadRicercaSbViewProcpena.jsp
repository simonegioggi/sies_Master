<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>
<jsp:useBean id="sbviewprocpena" scope="request" class="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"/>

<html>
<head>
  <title> Ricerca SbViewProcpena </title>
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
      if (document.LoadInserisciSbViewProcpena.<%="ICostantiSbViewProcpena.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewProcpena.<%="ICostantiSbViewProcpena.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Pass Giud' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_PASS_GIUD%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_PASS_GIUD%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Pass Giud non corretta'); 
        document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Sent 1gra' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_1GRA%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_1GRA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent 1gra non corretta'); 
        document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Sent 2gra' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_2GRA%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_2GRA%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent 2gra non corretta'); 
        document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Sent Gipp Gupp' 
      //=============================================================
      var data_to_verify = document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_GIPP_GUPP%>.value+'/'+ 
                           document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_GIPP_GUPP%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Sent Gipp Gupp non corretta'); 
        document.LoadRicercaSbViewProcpena.<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP%>.focus(); 
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
                      <font class="campo">Ricerca SbViewProcpena</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaSbViewProcpena">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewprocpena.action.ActRicercaSbViewProcpena">
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
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_PMPM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Pmpm</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_PMPM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Pmpm</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiPmpm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_PMPM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_GIPP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_GIPP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Gipp</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiGipp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_GIPP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_DIBB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_DIBB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Dibb</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiDibb()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_DIBB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiUffiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_UFFI_COAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Regi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeRegiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_REGI_COAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Regi Coap</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoRegiCoap()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_REGI_COAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Pass Giud</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_PASS_GIUD %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_GIGU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_GIGU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_GIGU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Gigu</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaGigu()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_GIGU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent 1gra</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_1GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno Sent 1gra</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent1gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Sent 1gra</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent1gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent 2gra</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_2GRA %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno Sent 2gra</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSent2gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_2GRA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Sent 2gra</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSent2gra()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_2GRA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"dd")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"MM")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"yyyy")) %>" 
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENT_GIPP_GUPP %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Nume Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeSentGippGupp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_GIPP_GUPP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Sent Gipp Gupp</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoSentGippGupp()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_GIPP_GUPP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_DIBA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_DIBA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Diba</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_DIBA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anni Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnniPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNI_PENA_APPE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Mesi Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getMesiPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_MESI_PENA_APPE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Gior Pena Appe</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getGiorPenaAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_GIOR_PENA_APPE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Diba</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreDiba()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_DIBA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Recl Arre Appe</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagReclArreAppe()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_RECL_ARRE_APPE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0089</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0089()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0089 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0090</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0090()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0090 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0091</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0091()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0091 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0092</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0092()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0092 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0093</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0093()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0093 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0094</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0094()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0094 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0095</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0095()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0095 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0096</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0096()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0096 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0097</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0097()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0097 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0098</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0098()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0098 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 0099</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti0099()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_0099 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Arti 62</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArti62()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ARTI_62 %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti 0062 Comm</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getArti0062Comm()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ARTI_0062_COMM %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Art 62bi</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagArt62bi()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_ART_62BI %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Misu Cust</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiMisuCust()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_MISU_CUST %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Isti Pena</td>
      <td class="l"> 
        <input type="text" maxlength="5" size="5" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiIstiPena()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_ISTI_PENA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Desc Luog</td>
      <td class="l"> 
        <input type="text" maxlength="200" size="200" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getDescLuog()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_DESC_LUOG %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getIdPren()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getCodiSedeInst()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_CODI_SEDE_INST %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_NUME_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Info Sele</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="10" 
               value="<%=StringUtils.toStringJSP(sbviewprocpena.getFlagInfoSele()) %>"
               name="<%= ICostantiSbViewProcpena.CAMPO_FLAG_INFO_SELE %>"  
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
  var frmvalidator  = new Validator("LoadRicercaSbViewProcpena");

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