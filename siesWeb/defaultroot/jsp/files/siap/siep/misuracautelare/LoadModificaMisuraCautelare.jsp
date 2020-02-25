<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="misuracautelare" scope="request" class="siap.siep.misuracautelare.model.MisuraCautelareModel"/>
<jsp:useBean id="computabilità"   scope="request" class="java.lang.String"/>
<jsp:useBean id="datafine"        scope="request" class="java.lang.String"/>
<% // Combo %>
<jsp:useBean id="tipoMisura"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMotivo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>


<html>
<head>
<title>[S.I.E.S.] - Modifica Misura Cautelare </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript">
  var desktop;
  //==============================================
  //
  //==============================================
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  //==============================================
  //
  //==============================================
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  //==============================================
  //
  //==============================================
  function Verify()
  {
    var misura=document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%>.value;
    if(misura =="-")
    {
      alert("Misura Cautelare Obbligatoria");
      return false;
    }
      
    var giornoDataInizio = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>.value;
    var meseDataInizio   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>.value;
    var annoDataInizio   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>.value;
    
    if(giornoDataInizio.length==1)
      giornoDataInizio='0'+giornoDataInizio;
    if(meseDataInizio.length==1)
      meseDataInizio='0'+meseDataInizio;

    dataInizio=giornoDataInizio+"/"+meseDataInizio+"/"+annoDataInizio;

    if(dataInizio.length > 2)
    {
      if(!ControllaData(dataInizio))
      {
        alert("Data Inizio non valida");
        return false;
      }
    }
    else
    {
      alert("Data Inizio Obbligatoria");
      return false;
    }


    if(LoadInserisciMisuraCautelare.dataFine.value!="")
    {
      var giornoDataFine = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>.value;
      var meseDataFine   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>.value;
      var annoDataFine   = document.LoadInserisciMisuraCautelare.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>.value;
      
      if(giornoDataFine.length==1)
        giornoDataFine='0'+giornoDataFine;
      if(meseDataFine.length==1)
        meseDataFine='0'+meseDataFine;

      dataFine=giornoDataFine+"/"+meseDataFine+"/"+annoDataFine;
      if(dataFine.length > 2)
      {
        if(!ControllaData(dataFine))
        {
            alert("Data Fine non valida");
            return false;
        }
      }
      else
      {
        alert("Data Fine Obbligatoria");
        return false;
      }
      
      if(!CompareDate(dataInizio,dataFine))
      {
        alert("La Data Inizio deve essere minore o uguale della Data Fine");
        return false;
      }
    }
    
    return true;
  }
</script>

</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
          MisuraCautelareModel lMis = new MisuraCautelareModel();
          String lAzione = new String();
          lAzione = "siap.siep.misuracautelare.action.ActModificaMisuraCautelare";
          lMis = misuracautelare;
        %>
        <font class="campo">Modifica Misura Cautelare</font>
      </td>
    </tr>
  </table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraCautelare">
  <input type="HIDDEN" name="Action"      value="<%=lAzione%>" >
  <input type="hidden" name="computabile" value="<%=computabilità%>">
  <input type="hidden" name="dataFine"    value="<%=datafine%>">
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelare.CAMPO_ID_MISURA_CAUTELARE%>" value="<%=lMis.getIdMisuraCautelare()%>">
  <input type="HIDDEN" name="<%=ICostantiMisuraCautelare.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=lMis.getFasSieIdFascicoloSiep()%>">

<%
//==============================================================================
// In Corso al Momento del Passaggio in Giudicato
//==============================================================================
%>
  <%if(computabilità.equals("S") && datafine.equals("")){%>
  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="Titolo"> In Corso al Momento del Passaggio in Giudicato </td>
    </tr>
    <tr>
      <td class="l">
        <table>
          <tr>
            <td class="l">Misura<font class=ob>(*)</font></td>
            <td class="l">
              <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>">
                <%=tipoMisura%>
              </select>
            </td>
            <td class="L" > Da <font class=ob>(*)</font></td>
            <td class="L" >
              <input title="Giorno Data inizio" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"dd")) %>"  type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
               -
              <input  title="Mese Data inizio"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"MM")) %>"type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
               -
              <input  title="Anno Data inizio"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
            
            <td class="l">Istituto</td>
            <% if(lMis.getIstitutoDetenzione() == null) {%>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                <img src="/images/filefolder.gif" border=0></a>
              </td>
            <%}else {%>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lMis.getIstitutoDetenzione().getDescrComune())%>" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lMis.getIstDetIdIstitutoDetenzione()%>" size=50>
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                <img src="/images/filefolder.gif" border=0></a>
              </td>
            <%}%>
          </tr>
        </table>
      
      
        <table>
          <tr>
            <td class="l" >Altro Luogo</td>
            <td class="l">
               <input value="<%=StringUtils.toStringJSP(misuracautelare.getAltroLuogoDetenzione())%>" title="Altro Luogo Detenzione" type="text" name="<%= ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<%}%>

<%
//==============================================================================
// Cessati al Momento del Passaggio in Giudicato, Computabili
//==============================================================================
%>
<%if(computabilità.equals("S") && !datafine.equals("")){%>
  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="Titolo" > Cessati al Momento del Passaggio in Giudicato - Computabili </td>
    </tr>
    
    <tr>
      <td class="l">
        <table>
          <tr>
            <td class="l">Misura<font class=ob>(*)</font></td>
            <td class="l">
              <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>">
                <%=tipoMisura%>
              </select>
            </td>
            <td class="l">Da <font class=ob>(*)</font></td>
            <td class="L">
              <input  type="text"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"dd")) %>"size="2" maxlength="2" title="Giorno Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"MM")) %>" size="2" maxlength="2" title="Mese Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"yyyy")) %>" size="4" maxlength="4" title="Anno Data inizio" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
            <td class="l">a<font class=ob>(*)</font></td>
            <td class="L">
              <input  type="text"  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"dd")) %>" size="2" maxlength="2" title="Giorno Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input   type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"MM")) %>" size="2" maxlength="2" title="Mese Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"yyyy")) %>" size="4" maxlength="4" title="Anno Data fine" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
          </tr>
        </table>
          
        <table>
          <tr>
            <td class="l">Istituto</td>
            <% if(misuracautelare.getIstitutoDetenzione() == null) {%>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
            </td>
            <%}else {%>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(misuracautelare.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(misuracautelare.getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=misuracautelare.getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
            </td>
            <%}%>
          </tr>
          <tr>
            <td class="l" >Altro Luogo </td>
            <td class="l">
              <input value="<%=StringUtils.toStringJSP(misuracautelare.getAltroLuogoDetenzione())%>" title="Altro Luogo Detenzione" type="text" name="<%= ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<%}%>


<%
//==============================================================================
// Cessati al Momento del Passaggio in Giudicato, Non Computabili
//==============================================================================
%>
<%if(computabilità.equals("N")){%>
  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="Titolo" > Cessati al Momento del Passaggio in Giudicato - Non Computabili </td>
    </tr>
    <tr>
      <td class="l">
        <table>
          <tr>
            <td class="l">Misura<font class=ob>(*)</font></td>
            <td class="l">
              <select Title="Tipo Misura Cautelare" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA %>">
                <%=tipoMisura%>
              </select>
            </td>
            <td class="l">Da<font class=ob>(*)</font></td>
            <td class="L">
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"dd")) %>" title="Giorno Data inizio" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"MM")) %>" title="Mese Data inizio" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataInizio(),"yyyy")) %>" title="Anno Data inizio" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
            <td class="l">a<font class=ob>(*)</font></td>
            <td class="L">
              <input type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"dd")) %>" title="Giorno Data fine" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"MM")) %>" title="Mese Data fine" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFine(),"yyyy")) %>" title="Anno Data fine" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
          </tr>
        </table>
        
        <table>
          <tr>
            <td class="l">Istituto</td>
            <td class="l">
              <input readonly Title="Istituto" name="Indirizzo" value="" size=45 >
              <input type="hidden" readonly Title="Istituto" name="<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=45 >
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraCautelare','<%= ICostantiMisuraCautelare.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Indirizzo');">
                <img src="/images/filefolder.gif" border=0></a>
            </td>
          </tr>
          <tr>
            <td class="l" >Altro Luogo </td>
            <td class="l">
              <input value="<%=StringUtils.toStringJSP(misuracautelare.getAltroLuogoDetenzione())%>" type="text" title="Altro Luogo Detenzione" name="<%=ICostantiMisuraCautelare.CAMPO_ALTRO_LUOGO_DETENZIONE %>" maxlength="35" size="35">
            </td>
            <td class="l" >Motivo non Computabilità </td>
            <td class="l">
              <select Title="Motivo Computabilità"   name="<%= ICostantiMisuraCautelare.CAMPO_COD_MOTIVO_NON_COMPUTABILE%>">
                <%=tipoMotivo%>
              </select>
            </td>
          </tr>
        </table>


        <table>
          <tr>
            <td class="l" >Ufficio </td>
            <td class="l">
              <select  title="Ufficio" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_RIFER %>">
                <%=tipoUfficio%>
              </select>
            </td>

            <td class="l">Luogo</td>
            <td class="l">
              <input value="<%=StringUtils.toStringJSP(misuracautelare.getDescrLuogoUfficioRifer())%>" type="text" name="<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>">
              <a href="Javascript:ListaComuni('LoadInserisciMisuraCautelare','<%=ICostantiMisuraCautelare.CAMPO_COD_LUOGO_UFFICIO_RIFER%>');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
          </tr>
        </table>
        
        <table>
          <tr>
            <td class="l" >N° SIEP/RES</td>
            <td class="l">
              <input value="<%=StringUtils.toStringJSP(misuracautelare.getNumRifer())%>" type="text" title="N°SIEP/RES" name="<%= ICostantiMisuraCautelare.CAMPO_NUM_RIFER %>">
            </td>
            <td class="l" >Data Provvedimento</td>
            <td class="l">
              <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFungibilita(),"dd")) %>" title="Giorno Data Provvedimento" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFungibilita(),"MM")) %>" title="Mese Data Provvedimento" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              -
              <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuracautelare.getDataFungibilita(),"yyyy")) %>" title="Anno Data Provvedimento" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
            <td class="l" >Note</td>
            <td class="l">
              <textarea rows=3 cols=25 name="<%= ICostantiMisuraCautelare.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(misuracautelare.getNote())%></textarea>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<%}%>

<table>
  <tr>
    <td colspan=2>
      <br>
      <INPUT  class="bottone" type="submit" name="MODIFICA" value="Conferma">
    </td>
  </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraCautelare");
  
  <%if(computabilità.equals("S") && datafine.equals("")){%>
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_INIZIO%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_INIZIO%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_INIZIO%>","num");
  <%}%>
  
  <%if(computabilità.equals("S") && !datafine.equals("")){%>
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FINE%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FINE%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FINE%>","num");
  <%}%>

  <%if(computabilità.equals("N")){%>
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_FUNGIBILITA%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_FUNGIBILITA%>","num");
      frmvalidator.addValidation("<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_FUNGIBILITA%>","num");
  <%}%>
  frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>