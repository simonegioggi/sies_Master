<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel"%>
<%@ page import="siap.bdmc.sbviewnotifiche.action.ICostantiSbViewNotifiche"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="sbviewnotifiche" scope="request" class="siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel"/>

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
  <title> Gestione SbViewNotifiche </title>
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
      if (document.LoadInserisciSbViewNotifiche.<%="ICostantiSbViewNotifiche.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewNotifiche.<%="ICostantiSbViewNotifiche.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo correttezza campo 'Data Invi Noti' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INVI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INVI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INVI_NOTI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Invi Noti non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INVI_NOTI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Regi Noti' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_REGI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_REGI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_REGI_NOTI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Regi Noti non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_REGI_NOTI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Vali Noti' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_VALI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_VALI_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_VALI_NOTI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Vali Noti non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_VALI_NOTI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Chiu Noti' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_CHIU_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_CHIU_NOTI%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_CHIU_NOTI%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Chiu Noti non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_CHIU_NOTI%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Iniz' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Iniz non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Fine' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Iniz Prec' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ_PREC%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ_PREC%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ_PREC%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Iniz Prec non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ_PREC%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Fine Prec' 
      //=============================================================
      var data_to_verify = document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE_PREC%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE_PREC%>.value+'/'+ 
                           document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE_PREC%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine Prec non corretta'); 
        document.LoadInserisciSbViewNotifiche.<%=ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE_PREC%>.focus(); 
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
        SbViewNotificheModel lSbViewNotifiche = new SbViewNotificheModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.bdmc.sbviewnotifiche.action.ActInserisciSbViewNotifiche"; 
        %>
        <font class="campo">Inserimento SbViewNotifiche</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.bdmc.sbviewnotifiche.action.ActModificaSbViewNotifiche";
          lSbViewNotifiche = sbviewnotifiche;
        %>
        <font class="campo">Modifica SbViewNotifiche</font>
        <%}%>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadInserisciSbViewNotifiche">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="<%=lAzione%>">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Prog Noti</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getProgNoti()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_PROG_NOTI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Noti</td>
      <td class="l"> 
        <input type="text" maxlength="20" size="20" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getCodiNoti()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_CODI_NOTI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Descrizione</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getDescrizione()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_DESCRIZIONE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Invi Noti</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInviNoti(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INVI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInviNoti(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INVI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInviNoti(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INVI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Regi Noti</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataRegiNoti(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_REGI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataRegiNoti(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_REGI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataRegiNoti(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_REGI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Vali Noti</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataValiNoti(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_VALI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataValiNoti(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_VALI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataValiNoti(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_VALI_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="250" size="250" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getNote()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_NOTE %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi Sies</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getCodiUffiSies()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_CODI_UFFI_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Uffi</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getCodiUffi()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_CODI_UFFI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Stat Noti</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getFlagStatNoti()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_FLAG_STAT_NOTI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Chiu Noti</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataChiuNoti(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_CHIU_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataChiuNoti(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_CHIU_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataChiuNoti(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_CHIU_NOTI %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Flag Tras</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getFlagTras()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_FLAG_TRAS %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Stop Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getStopAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_STOP_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Stop Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getStopNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_STOP_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Uten Sies</td>
      <td class="l"> 
        <input type="text" maxlength="20" size="20" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getUtenSies()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_UTEN_SIES %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getIdPren()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_ID_PREN %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Peri</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getProgPeri()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_PROG_PERI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Modi Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getModiAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_MODI_ANNO_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Modi Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getModiNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_MODI_NUME_FASC_BDMC %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Modi</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(lSbViewNotifiche.getFlagModi()) %>"
               name="<%= ICostantiSbViewNotifiche.CAMPO_FLAG_MODI %>"  
               <%=readonly%> 
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Iniz</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataIniz(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataIniz(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataIniz(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFine(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFine(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFine(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Iniz Prec</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInizPrec(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInizPrec(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataInizPrec(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine Prec</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFinePrec(),"dd")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFinePrec(),"MM")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" <%=readonly%> 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSbViewNotifiche.getDataFinePrec(),"yyyy")) %>" 
               name="<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE_PREC %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
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
  var frmvalidator  = new Validator("LoadInserisciSbViewNotifiche");


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

  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_PROG_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_CODI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_DESCRIZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INVI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INVI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INVI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_REGI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_REGI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_REGI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_VALI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_VALI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_VALI_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_CODI_UFFI_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_CODI_UFFI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_FLAG_STAT_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_CHIU_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_CHIU_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_CHIU_NOTI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_FLAG_TRAS %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_STOP_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_STOP_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_UTEN_SIES %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_PROG_PERI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MODI_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MODI_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_FLAG_MODI %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_INIZ_PREC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_INIZ_PREC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_INIZ_PREC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_GIORNO_DATA_FINE_PREC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_MESE_DATA_FINE_PREC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewNotifiche.CAMPO_ANNO_DATA_FINE_PREC %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>