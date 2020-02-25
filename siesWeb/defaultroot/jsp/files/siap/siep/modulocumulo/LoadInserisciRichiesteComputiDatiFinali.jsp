<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel"   scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<%// SE in modifica%>
<jsp:useBean id="ComputoCumulo" scope="request" class="siap.siep.modulocumulo.model.ComputiCumuloModel"/>

<%//Combo%>
<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />
<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>


<%
//==============================================================================
//  FORM di Inserimento delle richieste con antcipazione degli effetti in 
//  Dati FInali Cumuo
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = new DatiFinaliCumuloModel(); 
//if( modalita.equals("M")){
  lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
//}
%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
    var sysDate = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>";
    
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formIndietro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formIndietro.submit();
    }
    
    //==========================================================================
    //
    //==========================================================================
    function Verify()
    {
      
      if (document.formName.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.value == "-") {
        alert('Selezionare il tipo di Beneficio');        
        document.formName.<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>.focus();        
        return false;
      }
      
      if (document.formName.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>.value == "-") {
        alert('Selezionare il Provvedimento di Concessione ');        
        document.formName.<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>.focus();        
        return false;
      }
      
      // Data Richiesta
      if (document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.value.length==1)
        document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.value='0'+document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.value;
      if (document.formName.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA%>.value.length==1)
        document.formName.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA%>.value='0'+document.formName.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA%>.value;
      
      var dataRich  =     document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.value
                     +'/'+document.formName.<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA%>.value
                     +'/'+document.formName.<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RICHIESTA%>.value;
      
      if (! ControllaDataPassaVuota(dataRich))
      {
        alert('Data Richiesta non valida');
        document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.focus();
        return false;
      }

      

      if (dataRich!="//" && !CompareDate(dataRich,sysDate))
      {
        alert('La Data Richiesta non può essere una data futura');
        document.formName.<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>.focus();
        return false;
      }      
      
      
      if (document.formName.<%=ICostantiComputiCumulo.CAMPO_FLAG_PIU_MENO%>.value == "") {
        alert('Selezionare se la richesta è in Aumento o Diminuzione');        
        document.formName.<%=ICostantiComputiCumulo.CAMPO_FLAG_PIU_MENO%>.focus();        
        return false;
      }

      if (   (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>INT.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>INT.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>DEC.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>DEC.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO%>.value == "0"
             )
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value == "0"
             )              
          && (   document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value == ""
              || document.formName.<%=ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value == "0"
             ) 
         )
      {
        alert("Indicare i quantum e/o gli importi richiesti.");
        document.formName.<%=ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE%>.focus();
        return false;
      }
      
      
      return true;
    }
    
    
    //================================================
    // Funzione richiamata al caricamento della form
    //================================================
    $(document).ready(function(){
      //Inizilaizzazione delle check

    });
  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if ("I".equals(modalita)) {%>
        <font class="campo">Inserimento Richiesta al GE con anticipazione degli effetti</font>&nbsp;
        <% } else { %>
        <font class="campo">Modifica Richiesta al GE con anticipazione degli effetti</font>&nbsp;
        <% } %>
      </td>
      <td class="LBG"><!-- Tasto indietro  -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formIndietro">
  <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form>
  
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInsRichiesteComputiDatiFinali">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
  <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">
  
  <input type="hidden" name="modalita" value="<%=modalita%>">

  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="<%=StringUtils.toStringJSP(ComputoCumulo.getIdComputiCumulo()) %>">

<%
//==============================================================================
// 
//==============================================================================
%>
<div id="divPosizionamento" align="center" >
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td colspan="4" class="Titolonocap">Richiesta al Giudice dell' Esecuzione</td>
    </tr>
    <tr>
      <td class="l">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
      <td class="l">
        <select name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>">
          <option value = "-"  />-
          <%=TipoAnnotazioneManuale%>
        </select>
      </td>
      <td class="l">Provvedimento di Concessione <font class="ob">(*)</font> :&nbsp;</td>
      <td class="l">
        <select name="<%=ICostantiComputiCumulo.CAMPO_COD_DPR%>">
          <%=listaDPR%>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Data Richiesta</td>
      <td class="l" colspan="3">
        <input type="text"  maxlength="2" size="2"
               name="<%=ICostantiComputiCumulo.CAMPO_GIORNO_DATA_RICHIESTA%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ComputoCumulo.getDataRichiesta(),"dd")) %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiComputiCumulo.CAMPO_MESE_DATA_RICHIESTA%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ComputoCumulo.getDataRichiesta(),"MM")) %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="4" size="4"  
               name="<%=ICostantiComputiCumulo.CAMPO_ANNO_DATA_RICHIESTA%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ComputoCumulo.getDataRichiesta(),"yyyy")) %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>

<table width="97%">
  <tr>
    <td colspan="6">
      <hr width="100%">
    </td>
  </tr>
  <tr>
    <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="<%=ICostantiComputiCumulo.CAMPO_FLAG_PIU_MENO%>">
        <option value=""></option>
        <option value="+" <%= "+".equals(ComputoCumulo.getFlagPiuMeno())?"selected":""%> >+</option>
        <option value="-" <%= "-".equals(ComputoCumulo.getFlagPiuMeno())?"selected":""%> >-</option>
      </select>
    </td>
    <td class="titolo" colspan=2>Reclusione</td>
    <td width="25">&nbsp;</td>
    <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
    <td class="c">
      <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Giorni</font><br>
      <input type="text" maxlength="2" size="2"
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumAnniReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>" 
             ONKEYPRESS="return TicTabNumField(this,event)">&nbsp;
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumMesiReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_MESI_RECLUSIONE %>"  
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
      <input type="text" maxlength="4" size="4"
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumGiorniReclusione()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>"        
             ONKEYPRESS="return TicTabNumField(this,event)">
    </td>
    <td class="c">
      <font  class="label">Multa</font>
      <br>
      <input type="text" maxlength="7" size="7" style="text-align:right"
             value="<%=StringUtils.getParteIntera(ComputoCumulo.getImportoMulta()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA%>INT" 
             ONKEYPRESS="return TicTabNumField(this,event)" >
      ,
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.getParteDecimale(ComputoCumulo.getImportoMulta()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_MULTA %>DEC"  
             ONKEYPRESS="return TicTabNumField(this,event)" >
    </td>
    <td width="25">&nbsp;</td>
    <td class=c>
      <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Giorni</font><br>
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumAnniArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_ANNI_ARRESTO %>" 
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumMesiArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_MESI_ARRESTO %>" 
             ONKEYPRESS="return TicTabNumField(this,event)" >&nbsp;
      <input type="text" maxlength="4" size="4" 
             value="<%=StringUtils.toStringJSP(ComputoCumulo.getNumGiorniArresto()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_NUM_GIORNI_ARRESTO %>"
             ONKEYPRESS="return TicTabNumField(this,event)" >
    </td>
    <td class="c">
      <font  class="label">Ammenda</font><br>
      <input type="text" maxlength="7" size="7"  style="text-align:right"
             value="<%=StringUtils.getParteIntera(ComputoCumulo.getImportoAmmenda()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA%>INT" 
             ONKEYPRESS="return TicTabNumField(this,event)" >
      ,
      <input type="text" maxlength="2" size="2" 
             value="<%=StringUtils.getParteDecimale(ComputoCumulo.getImportoAmmenda()) %>"
             name="<%= ICostantiComputiCumulo.CAMPO_IMPORTO_AMMENDA %>DEC"        
             ONKEYPRESS="return TicTabNumField(this,event)" >
    </td>
  </tr>
  <tr>
    <td class="c" colspan=5>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="60" rows="2" name="<%=ICostantiComputiCumulo.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(ComputoCumulo.getNote()) %></textarea>
    </td>
    <td width=20>&nbsp;</td>
  </tr>
</table>
  
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td>
        <input type="submit" name="Conferma" value="Conferma">
      </td>
    </tr>
  </table>
  
</div>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

