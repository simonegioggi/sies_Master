<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"%>
<%@ page import="siap.siep.misuracautelarebdmc.action.ICostantiMisuraCautelareBdmc"%>
<jsp:useBean id="misuracautelarebdmc" scope="request" class="siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel"/>

<html>
<head>
  <title> Ricerca MisuraCautelareBdmc </title>
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
      if (document.LoadInserisciMisuraCautelareBdmc.<%="ICostantiMisuraCautelareBdmc.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciMisuraCautelareBdmc.<%="ICostantiMisuraCautelareBdmc.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

      //=============================================================
      // controllo corretteza campo 'Data Inizio' 
      //=============================================================
      var data_to_verify = document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INIZIO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INIZIO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inizio non corretta'); 
        document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INIZIO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Fine' 
      //=============================================================
      var data_to_verify = document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_FINE%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_FINE%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_FINE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Fine non corretta'); 
        document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_FINE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Computo' 
      //=============================================================
      var data_to_verify = document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_COMPUTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_COMPUTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_COMPUTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Computo non corretta'); 
        document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_COMPUTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Inserimento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INSERIMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Inserimento non corretta'); 
        document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo corretteza campo 'Data Aggiornamento' 
      //=============================================================
      var data_to_verify = document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_AGGIORNAMENTO%>.value+'/'+ 
                           document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_AGGIORNAMENTO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Aggiornamento non corretta'); 
        document.LoadRicercaMisuraCautelareBdmc.<%=ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.focus(); 
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
                      <font class="campo">Ricerca MisuraCautelareBdmc</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaMisuraCautelareBdmc">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.siep.misuracautelarebdmc.action.ActRicercaMisuraCautelareBdmc">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Id Misura Cautelare</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getIdMisuraCautelare()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ID_MISURA_CAUTELARE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Fas Sie Id Fascicolo Siep</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getFasSieIdFascicoloSiep()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Sog Id Soggetto</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getSogIdSoggetto()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_SOG_ID_SOGGETTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Eve Id Evento</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getEveIdEvento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_EVE_ID_EVENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Pen Res Id Pena Residua</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getPenResIdPenaResidua()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_PEN_RES_ID_PENA_RESIDUA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getIdPren()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Prog Peri Pres</td>
      <td class="l"> 
        <input type="text" maxlength="38" size="40" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getProgPeriPres()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_PROG_PERI_PRES %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Caricamento</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagCaricamento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_CARICAMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Stato</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagStato()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_STATO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Misura</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodTipoMisura()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_TIPO_MISURA %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inizio</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizio(),"dd")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizio(),"MM")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInizio(),"yyyy")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INIZIO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFine(),"dd")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFine(),"MM")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataFine(),"yyyy")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_FINE %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Num Anni</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumAnni()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_ANNI %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Num Mesi</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumMesi()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_MESI %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Num Giorni</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumGiorni()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_GIORNI %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Ist Det Id Istituto Detenzione</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getIstDetIdIstitutoDetenzione()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Altro Luogo Detenzione</td>
      <td class="l"> 
        <input type="text" maxlength="1000" size="1000" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAltroLuogoDetenzione()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ALTRO_LUOGO_DETENZIONE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Flag Computabile</td>
      <td class="l"> 
        <input type="text" maxlength="1" size="1" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getFlagComputabile()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_COMPUTABILE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Motivo Non Computabile</td>
      <td class="l"> 
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodMotivoNonComputabile()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_MOTIVO_NON_COMPUTABILE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Tipo Ufficio Rifer</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="6" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodTipoUfficioRifer()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_TIPO_UFFICIO_RIFER %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Luogo Ufficio Rifer</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="6" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodLuogoUfficioRifer()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_LUOGO_UFFICIO_RIFER %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Computo</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataComputo(),"dd")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_COMPUTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataComputo(),"MM")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_COMPUTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataComputo(),"yyyy")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_COMPUTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno Fasc Siep</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascSiep()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_FASC_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Siep</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascSiep()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_FASC_SIEP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="l"> 
        <input type="text" maxlength="2000" size="2000" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNote()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NOTE %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoFascBdmc()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeFascBdmc()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioBdmc()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rgnr</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRgnr()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_RGNR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Rgnr</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeRgnr()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_RGNR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Rgnr</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioRgnr()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_RGNR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rege Gip</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeGip()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_GIP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Rege Gip</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeGip()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_GIP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Gip</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioGip()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_GIP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rege Dib</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeDib()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_DIB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Rege Dib</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeDib()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_DIB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Dib</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioDib()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_DIB %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rege Cas</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCas()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CAS %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Rege Cas</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCas()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CAS %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Cas</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCas()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CAS %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rege Cap</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Rege Cap</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Cap</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Rege Casap</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getAnnoRegeCasap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CASAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Numero Rege Casap</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getNumeroRegeCasap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CASAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Ufficio Casap</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioCasap()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CASAP %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodOperatoreInserimento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_OPERATORE_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Inserimento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInserimento(),"dd")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInserimento(),"MM")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataInserimento(),"yyyy")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INSERIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Inserimento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioInserimento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_INSERIMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Cod Operatore Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="100" size="100" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodOperatoreAggiornamento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Data Aggiornamento</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataAggiornamento(),"dd")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"  
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataAggiornamento(),"MM")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
         >&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuracautelarebdmc.getDataAggiornamento(),"yyyy")) %>" 
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Cod Ufficio Aggiornamento</td>
      <td class="l"> 
        <input type="text" maxlength="11" size="11" 
               value="<%=StringUtils.toStringJSP(misuracautelarebdmc.getCodUfficioAggiornamento()) %>"
               name="<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>"  
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
  var frmvalidator  = new Validator("LoadRicercaMisuraCautelareBdmc");

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

  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ID_MISURA_CAUTELARE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_SOG_ID_SOGGETTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_EVE_ID_EVENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_PEN_RES_ID_PENA_RESIDUA %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_PROG_PERI_PRES %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_CARICAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_STATO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_TIPO_MISURA %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INIZIO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_FINE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_ANNI %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_MESI %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUM_GIORNI %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ALTRO_LUOGO_DETENZIONE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_FLAG_COMPUTABILE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_MOTIVO_NON_COMPUTABILE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_TIPO_UFFICIO_RIFER %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_LUOGO_UFFICIO_RIFER %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_COMPUTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_COMPUTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_COMPUTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_FASC_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_FASC_SIEP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NOTE %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_RGNR %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUME_RGNR %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_RGNR %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_GIP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_GIP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_GIP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_DIB %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_DIB %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_DIB %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CAS %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CAS %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CAS %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_REGE_CASAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_NUMERO_REGE_CASAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_CASAP %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_OPERATORE_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_INSERIMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_OPERATORE_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_MESE_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_ANNO_DATA_AGGIORNAMENTO %>",);
  //frmvalidator.addValidation("<%= ICostantiMisuraCautelareBdmc.CAMPO_COD_UFFICIO_AGGIORNAMENTO %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>