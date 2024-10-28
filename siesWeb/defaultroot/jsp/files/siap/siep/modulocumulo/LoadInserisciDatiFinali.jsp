<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel"   scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="comboAutorita"     scope="request" class="java.lang.String"/>

<jsp:useBean id="descrLuogoGE"     scope="request" class="java.lang.String"/>

<%
//==============================================================================
//     FORM di inserimento/modifica Dati Finali Cumulo
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = new DatiFinaliCumuloModel(); 
if( modalita.equals("M")){
  lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
}
else {
  // solo in fase di primo inserimento Dati Finali, serve per preselezionare il 
  // GE delle sentenza del Cumulante
  lDatiFinaliCumulo.setDescrLuogoUfficioEmittente(descrLuogoGE);
}
%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
        
    function eseguiSubmit (azione) {
      if (azione=='submit') {
        $("form[name='formName']").submit();
      }
      else {
        $("form[name='formName']").find("input[type='button']").prop('disabled',true);
        $("form[name='formAnnulla']").submit();
      }
    }

    function radio(){
      // Abilita/disabilita i dati del ordinanza
      var nomeRadio = '<%= ICostantiDatiFinaliCumulo.CAMPO_TIPO_UFFICIO_EMISSIONE %>';
      
      //$("input[name=tipo]:radio").
      //alert ("value = " + $("form [type=radio][name="+nomeRadio+"]:checked").val());
      
      if ( $("form [type=radio][name="+nomeRadio+"]:checked").val()=="03") {
        $('#tabEstremiProvv').show();
        $('#tabEstremiProvv input').prop('disabled',false);
        $('#tabEstremiProvv select').prop('disabled',false);
      }
      else {
        $('#tabEstremiProvv').hide();
        $('#tabEstremiProvv input').val('');
        //$('#tabEstremiProvv select option[0]').prop('selected', true);

        $('#tabEstremiProvv select').each(
            function(index){
              this.selectedIndex=0;
            }
        );
        
        //attr('selected','selected');

        
        $('#tabEstremiProvv input').prop('disabled',true);
        $('#tabEstremiProvv select').prop('disabled',true);
      }
    }
    
    //==========================================================================
    // Funzione di verifica dati 
    //==========================================================================
    function Verify() { 
      // Data Emissione
      var data_to_verify =     document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value
                          +'/'+document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value
                          +'/'+document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      if (!ControllaData(data_to_verify)){
        alert('Data Emissione non corretta');      
        document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if(!CompareDate(data_to_verify, data_od))
      {
        alert('La Data Emissione NON può essere superiore alla Data Odierna');
        document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
        return false;
      }

      if ($('input:radio[name=<%=ICostantiDatiFinaliCumulo.CAMPO_TIPO_UFFICIO_EMISSIONE %>][value=03]').prop('checked')){
        //alert('Emesso con ordinanza');        
        
        if ($('#tabEstremiProvv select').val()=='-'){
          alert("Selezionare l'autorità emittente");
          $('#tabEstremiProvv select').focus();
          return false;        
        }
        
        var sedeAutorita = $('#tabEstremiProvv input[type=text][name=<%=ICostantiDatiFinaliCumulo.CAMPO_DESCR_LUOGO_UFFICIO_EMITTENTE %>]');
        if (sedeAutorita.val()==''){
          alert("Selezionare la Sede autorità emittente");
          sedeAutorita.focus();
          return false;
        }
      }

      $("form[name='formName']").find("input[type='button']").prop('disabled',true);
      return true; 
    } 
    
    // script eseguito sulla onload della pagina
    $(document).ready(function(){
      <% if (!"03".equals(lDatiFinaliCumulo.getTipoUfficioEmissione())) { %>
      $('#tabEstremiProvv').hide();
      $('#tabEstremiProvv input').prop('disabled',true);
      $('#tabEstremiProvv select').prop('disabled',true);
      <% } else { %>
      //$('input:radio]').prop('checked',true);
      $('input:radio[name=<%=ICostantiDatiFinaliCumulo.CAMPO_TIPO_UFFICIO_EMISSIONE %>][value=03]').prop('checked',true);
      $('#tabEstremiProvv select').val('<%=lDatiFinaliCumulo.getCodTipoUfficioEmittente()%>');
      <% } %>
      
      /*
      $('.dataGG,.dataMM,.dataAA').blur(function () {
          if ($(this).prop("maxlength")==2 ) $(this).val(FillDM($(this).val()));
          if ($(this).prop("maxlength")==4 ) $(this).val(FillYear($(this).val()));          
        } 
      );
      $('.dataGG,.dataMM,.dataAA').focus(function () {textboxSelect(this);} );
      $('.dataGG,.dataMM,.dataAA').keypress(function () {return TicTabNumField(this,event);} );
      */
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
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Dati Finali Cumulo</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Dati Finali Cumulo</font>
        <%}%>   
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
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


  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formAnnulla">
    <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  </form>
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDatiFinaliCumulo">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">
    
    <input type="hidden" name="modalita" value="<%=modalita%>">

<%
//==============================================================================
// 
//==============================================================================
%>
<div id="divPosizionamento" align="left" style="padding-left: 25px;">
  <table cellspacing=2 cellpadding=4 width="95%">
    <tr>
      <td class="l">
        <input type="radio" name="<%= ICostantiDatiFinaliCumulo.CAMPO_TIPO_UFFICIO_EMISSIONE %>"  value="04" checked onclick="radio();">
        &nbsp;Emesso da Ufficio
        &nbsp;
        <input type="radio" name="<%= ICostantiDatiFinaliCumulo.CAMPO_TIPO_UFFICIO_EMISSIONE %>"  value="03" onclick="radio();">
        &nbsp;Emesso con ordinanza  
      </td>
      <td class="l">Data Emissione &nbsp;<font class="ob">(*)</font>
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDatiFinaliCumulo.getDataProvvedimento(),"dd")) %>" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input type="text" size="2" maxlength="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDatiFinaliCumulo.getDataProvvedimento(),"MM")) %>" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_MESE_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDatiFinaliCumulo.getDataProvvedimento(),"yyyy")) %>" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>

  <table id="tabEstremiProvv" cellspacing=2 cellpadding=4 width="95%">
    <tr><td class="Titolo" colspan=4>Estremi del Provvedimento</td></tr>
    <tr>
      <td class="l">
        Anno/Numero provvedimento
      </td>
      <td class="l">
        <input type="text" maxlength="4" size="4" Title="Anno Provvedimento"
               value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getAnnoProvvedimento()) %>"
               name="<%= ICostantiDatiFinaliCumulo.CAMPO_ANNO_PROVVEDIMENTO %>"  
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               >
        /
        <input type="text" maxlength="9" size="10" Title="Numero Provvedimento"
               value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getNumeroProvvedimento()) %>"
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_NUMERO_PROVVEDIMENTO %>"
               onkeypress="return TicTabNumField(this,event)" 
               >
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità emittente <font class="ob">(*)</font>
      </td>
    
      <td class="l">
        <select Title="Autorità emittente" name="<%=ICostantiDatiFinaliCumulo.CAMPO_COD_TIPO_UFFICIO_EMITTENTE %>">
          <%=comboAutorita%>
        </select>
       </td>
       <td class="l">&nbsp;</td>
       <td class="l">&nbsp;</td>
     </tr>
     <tr>
      <td class="l">Sede &nbsp;<font class="ob">(*)</font></td>
      <td class="l">
        <input type="text" title="Sede Autorita Emittente" maxlength="35" size="35"
               value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getDescrLuogoUfficioEmittente()) %>" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_DESCR_LUOGO_UFFICIO_EMITTENTE %>"  
               >
        <a href="Javascript:ListaUfficiPerTipo('formName','<%=ICostantiDatiFinaliCumulo.CAMPO_DESCR_LUOGO_UFFICIO_EMITTENTE %>'
                                              ,document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_COD_TIPO_UFFICIO_EMITTENTE %>[document.formName.<%=ICostantiDatiFinaliCumulo.CAMPO_COD_TIPO_UFFICIO_EMITTENTE %>.options.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0></a>
      </td>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="l">
        <input type="text" maxlength="200" size="50" Title="Sezione Autorità Emittente"
               value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getSezioneUfficioEmittente()) %>"
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_SEZIONE_UFFICIO_EMITTENTE %>"  
               >
      </td>
    </tr>
  </table>
  
  
  
  <table>
    <tr>
      <td>
        <input type="button" class="bottone" name="I" value="Conferma" onClick="eseguiSubmit('submit')">
      </td>
      <% if (modalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) { %>
      <td>
        <input type="button" class="bottone" name="I" value="Annulla" onClick="eseguiSubmit('annulla')">
      </td>
      <% } %>
    </tr>
  </table>
  
</div>

  
  </form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");

  frmvalidator.addValidation("<%= ICostantiDatiFinaliCumulo.CAMPO_ANNO_PROVVEDIMENTO %>","minlen=4","Indicare correttamente l'anno del provvedimento");
  frmvalidator.addValidation("<%= ICostantiDatiFinaliCumulo.CAMPO_ANNO_PROVVEDIMENTO %>","gt=1900","Indicare correttamente l'anno del provvedimento deve essere maggiore del 1900");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

