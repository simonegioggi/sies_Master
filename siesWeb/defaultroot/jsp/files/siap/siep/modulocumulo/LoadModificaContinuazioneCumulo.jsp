<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="continuazioneCum"  scope="request" class="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"/>
<jsp:useBean id="autoritaSentenza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRegGen"        scope="request" class="java.lang.String"/>


<%
ContinuazioneCumuloModel lContMod = continuazioneCum;
%>


<html>
<head>
<title>[S.I.E.S.] - Pena Complessiva - Continuazione (Cumulo)</title>

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
  
  function EliminaAssociazione ()
  {
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>.value="";
    
    // Riabilito i campi
    enableCampiTitolo();
  }  
  
  function inizializza(){
    <% if (lContMod.getTitIdTitoloCumulatoCont()!=null) { %>
    disableCampiTitolo();
    <% } else { %>
    enableCampiTitolo();
    <% } %>
  }  

  function disableCampiTitoloJQ (){
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
  
  function enableCampiTitoloJQ (){
    $('#trSelLista').show();
    $('#trAssociazione').hide();
    
    $('tr[trDatiSentenza]').show();
    $('tr[trLabelSentenza]').hide();    
  }
  
    
  function disableCampiTitolo (){
    disableCampiTitoloJQ();
    return;
    document.getElementById("trSelLista").style.display = "none";
    document.getElementById("trAssociazione").style.display = "block";
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.readOnly=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.readOnly=true;

    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.disabled=true;
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.disabled=true;
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.disabled=true;

    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.disabled=true;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.disabled=true;
  
  }
  
  function enableCampiTitolo (){
    enableCampiTitoloJQ();
    return;
    document.getElementById("trSelLista").style.display = "block";
    document.getElementById("trAssociazione").style.display = "none";
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.readOnly=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.readOnly=false;

    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.disabled=false;
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.disabled=false;
  
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.disabled=false;

    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.disabled=false;
    document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.disabled=false;
  }
  
  
  function Verify()
  {
    // Controllo Data Sentenza valida
    if (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==1)
        document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value='0'+document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value;
    if (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value.length==1)
        document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value='0'+document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value;

    var d1 =     document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value
            +'/'+document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value
            +'/'+document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.value;
    if (! ControllaData(d1) && d1.length > 2)
    {
        alert('Data Sentenza Continuazione non valida');
        document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.focus();

        return false;
    }
      
    if ((document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.value == "-" )
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.value.length==0)
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.value.length==0)
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==0)
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value.length==0)
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.value.length==0)
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.value=="-")
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.value=="-" )
     || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.value.length==0 ) )
    {
          alert('Tipo Continuazione, Anno, Numero, Data, Luogo e Autorità Sentenza sono OBBLIGATORI');
          document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();  
          return false;
    }

    if (  (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length==0)       
        &&(document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length>0))                
    {
          alert('Anno R.G.N.R. non presente');
          document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.focus();
          return false;  
    }
          
    if (   (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value.length>0)       
        && (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value.length==0))                
    {
        alert('Numero R.G.N.R. non presente');
        document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.focus();
        return false;  
    }

    if(document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value != "-")
    {
      if (   (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length==0)       
          || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length==0))           
      {
        alert("Anno o Numero Reg. Gen. non presente");
        document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.focus();
        return false;  
      }
    }
      
    if (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value == "-") 
    {
        if (   (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value.length!=0)       
            || (document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value.length!=0))           
        {
            alert("Inserire uno tra GIP/DIB/CAS/CAP/CASAP in TIPO RG");
            document.LoadModificaContinuazioneCumulo.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.focus();
            return false;  
        } 

    }       

  }
  
  //==========================================================================
  // Ritorna alla Griglia dei dati analitici
  //==========================================================================
  function tornaIndietro(action)
  {
    document.LoadModificaContinuazioneCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.LoadModificaContinuazioneCumulo.submit();
  }
  

  
</script>

</head>


<body class="corpo" onload="javascript:inizializza();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Continuazioni con altre sentenze Relative al Titolo Cumulato</font>
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

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaContinuazioneCumulo">
    <input type="HIDDEN" name="Action" value="siap.siep.modulocumulo.action.ActModificaContinuazioneCumulo" >

    <input type="HIDDEN" name="<%=ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM%>"      value="<%=StringUtils.toStringJSP(lContMod.getIdContinuazioneCum())%>">
    <input type="HIDDEN" name="<%=ICostantiContinuazioneCumulo.CAMPO_FLAG_STATO_CON_CUM %>"       value="<%=StringUtils.toStringJSP(lContMod.getFlagStato())%>">
    
    <input type="HIDDEN" name="<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>" value="<%=StringUtils.toStringJSP(lContMod.getPcIdPenaComplessivaCum())%>">
  

    <!-- Campi sempre presenti sulle form dei dati analitici -->
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

    <input type="hidden" name="<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>"  value="<%=StringUtils.toStringJSP(lContMod.getTitIdTitoloCumulatoCont())%>">

    
    <table cellspacing=2 cellpadding=2 width=95%>
      <tr><td class="Titolo" colspan="4">Continuazione con altre sentenze</td></tr>
      <tr>
        <td class="l">Tipo Continuazione</td>
        <td class="l" colspan="3">
          <select Title="Tipo Continuazione" name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_CONTINUAZIONE%>">
            <%=tipoContinuazione%>
          </select>
        </td>
      </tr>
      <% //if (lContMod.getTitIdTitoloCumulatoCont()==null) { %>
      <tr id="trSelLista">
        <td class="l" colspan="4">
          <a href="Javascript:ListaTitoliInIstruttoria('LoadModificaContinuazioneCumulo');">
            Seleziona Titolo In Continuazione <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0></a>
        </td>
      </tr>
      <% //} else { %>
      <tr id="trAssociazione">
        <td class="l" colspan="4">
            Continuazione già associata a titolo in istruttoria
            <a href="Javascript:EliminaAssociazione();" title="Elimina associazione"><img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border=0></a>
        </td>
      </tr>
      <% //} %>
      
      <tr>
        <td class="l">Anno/Numero Sentenza</td>
        <td class="L" colspan="3">
          <input type="text" Title="Anno Sentenza" maxlength="4" size="4"
                 value="<%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>"  
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
                 >
          /
          <input type="text" Title="Numero Sentenza" maxlength="6" size="6"
                 value="<%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>"  
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>">
        </td>
      </tr>
      <tr>
        <td class="l">Data Sentenza</td>
        <td class="l" colspan="3">
          <input Title="Giorno Data Sentenza" type="text"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "dd") )%>" name="<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data Sentenza" type="text"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "MM") )%>" name="<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Data Sentenza" type="text"  value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "yyyy") )%>" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Autorità Sentenza</td>
          <td class="l" colspan="3">
            <select Title="Autorità Sentenza"  name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>">
              <%=autoritaSentenza%>
            </select>
          </td>
      </tr>
      <tr>
        <td class="l">Luogo Sentenza</td>
        <td class="l" colspan="3">
          <input Title="Luogo Sentenza"  name="<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>" value="<%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%>" type="text" maxlength="35" size="35">
          <a id="gifLuogoSentenza" href="Javascript:ListaUfficiPerTipo('LoadModificaContinuazioneCumulo','<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>',document.LoadModificaContinuazioneCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>[document.LoadModificaContinuazioneCumulo.<%= ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA %>.selectedIndex].value);">
            <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      
      <tr>            
        <td class="l">Anno/Numero Re.Ge. PM</td>
        <td class="l">
          <input Title="Anno Re.Ge. PM"  value="<%=StringUtils.toStringJSP(lContMod.getAnnoRegePm())%>" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4">
          /
          <input Title="Numero Re.Ge. PM"  value="<%=StringUtils.toStringJSP(lContMod.getNumRegePm())%>" type="text" name="<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>" maxlength="6" size="6">
        </td>
     
        <td class="l">Anno/Numero Reg.Gen.</td>
        <td class="L">
          <input type="text" Title="Anno Reg.Gen." maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(lContMod.getAnnoRegGen())%>"  
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" Title="Numero Reg.Gen."  maxlength="6" size="6" 
                 value="<%=StringUtils.toStringJSP(lContMod.getNumeroRegGen())%>"  
                 name="<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          &nbsp;

          <select name="<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>" >
            <%=tipoRegGen%>
          </select>
        </td>
      </tr> 
      
<%
//==============================================================================
//
//==============================================================================
%>      
      <tr>
        <td class="l">Anno/Numero Sentenza</td>
        <td class="L" colspan="3">
          <font class="campo" id="<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>_L"></font>/
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
        <td class="l" colspan=1>Motivo<br>Modifica<br>Continuazione</td>
        <td class="l" colspan=3>
          <TextArea cols=80 rows=4 name="<%= ICostantiContinuazioneCumulo.CAMPO_MOTIVO_INS_MOD %>"><%=StringUtils.toStringJSP(lContMod.getMotivoModifica()) %></textarea>
        </td>
      </tr>
        
      <tr>
        <td colspan=2>
          <br><input class="bottone" type="submit" name="INSERISCI" value="Conferma">
        </td>
      </tr>
        
  </table>
  


</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaContinuazioneCumulo");

  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>","lt=3000");
  
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>","lt=3000");
  frmvalidator.addValidation("<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>","numeric");  

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>