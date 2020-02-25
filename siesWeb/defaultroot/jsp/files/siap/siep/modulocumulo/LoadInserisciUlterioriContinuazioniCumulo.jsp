<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="ListaTitoli"       scope="request" class="java.util.Vector"/>

<jsp:useBean id="modalita"          scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaSentenza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRegGen"        scope="request" class="java.lang.String"/>


<%
BigDecimal lIdPenaComplessivaCum    = (BigDecimal) request.getAttribute("lIdPenaComplessivaCum");
%>

<html>
<head>
<title>[S.I.E.S.] - Pena Complessiva Cumulo (Ulteriori Continuazioni)</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>

<script language="JavaScript">

  var desktop;
  
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaTitoliInIstruttoria (a_formname)
  {
    <%
    String lStrParametri = "";
    lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
    lStrParametri +="&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+TitoloInCumulo.getIdTitoloCumulato();
    %>
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActListaTitoliPerContinuazione&formname="+a_formname+"<%=lStrParametri%>", "Lista_Titoli_In_Istruttoria", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
  }  
  
  function inizializza(){
    enableCampiTitolo();
  }
  
  function enableCampiTitolo (){
    $('#trSelLista').show();
    $('#trAssociazione').hide();
    
    $('tr[trDatiSentenza]').show();
    $('tr[trLabelSentenza]').hide();    
  }
  
  function disableCampiTitolo (){
    $('#trSelLista').hide();
    $('#trAssociazione').show();
    $('tr[trDatiSentenza]').hide();

    
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>]').eq(0).val());
    
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>]:eq(0) option:selected').text());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>]').eq(0).val());

    $('#<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>]').eq(0).val());
    $('#<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>_L').text($('[name=<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>]:eq(0) option:selected').text());


    $('tr[trLabelSentenza]').show();
  }  
  
  function EliminaAssociazione ()
  {
    document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>.value="";
    
    // Riabilito i campi
    enableCampiTitolo();
  } 

  //===========================
  //  
  //===========================
  function Verify()
  {
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.value=="-")
    {
      alert('Tipo continuazione obbligatorio');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();
      return false;
    }

    if (   document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.value.length==0
        || document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.value.length==0
       )
    {
      alert('Indicare anno e numero sentenza');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.focus();
      return false;
    }


    // Controllo Data Sentenza valida
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==1)
        document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value='0'+document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value;
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value.length==1)
        document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value='0'+document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value;

    var dataSentenza =     document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value
                      +'/'+document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value
                      +'/'+document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.value;
    if (!ControllaData(dataSentenza))
    {
      alert('Data Sentenza Continuazione non valida ');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.focus();
      return false;
    }
    
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.value=="-")
    {
      alert('Indicare il tipo Autorità Emittente');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.focus();
      return false;
    }

    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.value.length==0)
    {
      alert('Indicare la Sede Autorità Emittente');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.focus();
      return false;
    }
    
    // Anno numero RGNR - non obbligatori ma se indicati vanno valorizzati entrambi
    if (   (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length==0)       
        && (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length>0))                
    {
      alert('Anno R.G.N.R. non presente');
      document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.focus();
      return false;  
    }
              
    if ((document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length>0)       
       && (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length==0))                
    {
        alert('Numero R.G.N.R. non presente');
        document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.focus();
        return false;  
    }
          
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value != "-")
    {
      if (   document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length==0
          || document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length==0)
      {
          alert("Anno o Numero Reg. Gen. non presente");
          document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.focus();
          return false;  
      }
    }
    
    if (document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value == "-") 
    {
      if (   document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length!=0       
          || document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length!=0)           
      {
          alert("Inserire uno tra GIP/DIB/CAS/CAP/CASAP in TIPO RG");
          document.LoadInserisciUlterioriContinuazioniCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.focus();
          return false;  
      }
    }
  } // Chiude verify
  
  //==========================================================================
  // Ritorna alla Griglia dei dati analitici
  //==========================================================================
  function tornaIndietro(action)
  {
    document.LoadInserisciUlterioriContinuazioniCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.LoadInserisciUlterioriContinuazioniCumulo.submit();
  }

  
</script>

</head>


<body class="corpo" onload="inizializza();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Inserimento Continuazioni con altre sentenze (Cumulo)</font>
      </td>

      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaPenaComplessivaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
      
      
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>
      
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciUlterioriContinuazioniCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciUlterioriContinuazioniCumulo">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>" value="<%=StringUtils.toStringJSP(lIdPenaComplessivaCum)%>">

  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>"  value="">
   
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo" colspan=4>Continuazione con altre sentenze</td></tr>
    <tr>
      <td class="l">Tipo Continuazione</td>
      <td class="l" colspan="3">
        <select Title="Tipo Continuazione" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>">
          <%=tipoContinuazione%>
        </select>
      </td>
    </tr>

    <tr id="trSelLista">
      <td class="l" colspan="4">
        <a href="Javascript:ListaTitoliInIstruttoria('LoadInserisciUlterioriContinuazioniCumulo');">
          Seleziona Titolo In Continuazione <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
      </td>
    </tr>
    <tr id="trAssociazione">
      <td class="l" colspan="4">
          Deassocia Continuazione da titolo in istruttoria
          <a href="Javascript:EliminaAssociazione();" title="Elimina associazione"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
      </td>
    </tr>


    
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="L" colspan="3">
        <input Title="Anno Sentenza" value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input Title="Numero Sentenza" value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>" maxlength="6" size="6"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="l" colspan="3">
        <input Title="Giorno Data Sentenza" type="text" value="" name="<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>" maxlength="2" size="2"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Mese Data Sentenza" type="text" value="" name="<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>" maxlength="2" size="2"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Anno Data Sentenza" type="text" value="" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>" maxlength="4" size="4"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Sentenza</td>
        <td class="l" colspan="3">
          <select Title="Autorità Sentenza" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>">
            <%=autoritaSentenza%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Luogo Sentenza</td>
      <td class="l" colspan="3">
        <input Title="Luogo Sentenza" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>" value="" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciUlterioriContinuazioniCumulo','<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>',document.LoadInserisciUlterioriContinuazioniCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>[document.LoadInserisciUlterioriContinuazioniCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero R.G.N.R.</td>
      <td class="L">
        <input Title="Anno R.G.N.R." value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input Title="Numero R.G.N.R." value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>" maxlength="6" size="6"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      </td>

      <td class="l">Anno/Numero Reg.Gen.</td>
      <td class="L">
        <input Title="Anno Reg.Gen." value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>" maxlength="4" size="4"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input Title="Numero Reg.Gen." value="" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>" maxlength="6" size="6"
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        &nbsp;

        <select name="<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>">
          <%=tipoRegGen%>
        </select>
      </td>
    </tr>
    
    <%// Sezione con le etichette %>
      <tr>
        <td class="l">Anno/Numero Sentenza</td>
        <td class="L" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>_L"></font>&nbsp;/
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Data Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>_L"></font>-
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>_L"></font>-
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Autorità Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>_L"></font>
        </td>
      </tr>
      <tr>
        <td class="l">Luogo Sentenza</td>
        <td class="l" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>_L"></font>
        </td>
      </tr>
      
      <tr>            
        <td class="l">Anno/Numero Re.Ge. PM</td>
        <td class="l">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>_L"></font>
          /
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>_L"></font>
        </td>
     
        <td class="l">Anno/Numero Reg.Gen.</td>
        <td class="L">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>_L"></font>&nbsp;
          /
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>_L"></font>
          &nbsp;
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>_L"></font>
        </td>
      </tr>    
    <tr>
      <td class="l" colspan=1>Motivo<br>Inserimento/Modifica<br>Continuazione</td>
      <td class="l" colspan=3><TextArea cols=80 rows=4 name="<%= ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD %>"></textarea></td>
    </tr>
  </table>
<br>

<table>
  <tr>
    <td>
      <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
    </td>
  </tr>
</table>

  
</form>


<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciUlterioriContinuazioniCumulo");
  

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>