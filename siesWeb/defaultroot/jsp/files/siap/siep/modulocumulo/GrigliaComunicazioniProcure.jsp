<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel" %>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaTitoli"  scope="request" class="java.util.Vector"/>

<jsp:useBean id="UfficiSorveglianza"  scope="request" class="java.util.Vector"/>

<jsp:useBean id="ufficiDestinatariComunicazioni"  scope="request" class="java.util.ArrayList"/>

<jsp:useBean id="ProvvedimentoCumulo"  scope="request" class="siap.sico.evento.model.EventoModel"/>

<jsp:useBean id="titoliGiaComunicati"  scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="nextAction"			scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Elenco dei Provvedimenti iscritti in cumulo e relative Procure a cui inviare
// le comunicazioni
//
// In aggiunta gli uffici di Sorveglianza indicati in form
//==============================================================================

    //LogF3B.getLogger().info("ufficiDestinatariComunicazioni = "+ufficiDestinatariComunicazioni.toString());
%>

<html>
<head>
  <title> [S.I.E.S.] - Comunicazioni </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <!--script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script-->
  
  <script language="JavaScript">    
  var desktop;
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function Verify()
    {
    // Data Emissione Comunicazione
    if (document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
    {
      document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+
      document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    }   
    if (document.formName.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
    {
      document.formName.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+
      document.formName.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
    }
       
    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    
    var data_chiusura_istruttoria = '<%=DateUtils.getDateToString(IstruttoriaCumulo.getDataChiusura(),"dd/MM/yyyy")%>';
    var data_emissione = document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+
                       document.formName.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+
                       document.formName.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_emissione) )
    {
           alert('Data di emissione non valida');
           document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
    }
    // Controllo : data di Emissione deve essere >= Data di chiusura Istruttoria
    if( !CompareDate( data_chiusura_istruttoria, data_emissione) )
    {
      alert('Data Comunicazione non può essere inferiore alla data chiusura istruttoria!');
      document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
      return false;
    }
    // Controllo : data di sistema deve essere >= Data di Emissione.
    if( !CompareDate( data_emissione, data_sistema) )
    {
      alert('Data Emissione non può essere superiore alla data odierna!');
      document.formName.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
      return false;
    }
    
      // Controllo selezione di almeno un CheckBox di Ufficio destinatario.
      var lCheck = new Boolean(false);
      
      
      
    for (var i=0; i<document.getElementsByName("<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>").length ; i++) {
      if(   document.getElementsByName("<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>")[i].disabled == false 
         && document.getElementsByName("<%=ICostantiUfficio.CAMPO_COD_UFFICIO%>")[i].checked == true ) 
      {
        lCheck = true;
      }
    } 
      
    if (lCheck==false) {
        alert('Selezionare almeno un Ufficio destinatario');
        return false;
    }
      
    }
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
        <font class="campo">Comunicazioni alle Procure e Uffici di Sorveglianza</font>&nbsp;
      </td>
      <td class="LBG"><!-- Tasto indietro -->
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>       
      <%-- td class="LBG"><!-- Tasto indietro alla Griglia Delle Comunicazioni -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActGrigliaComunicazioni')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td--%>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciComunicazioniProcure">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <!--input type="hidden" name="nextAction" value="siap.siep.modulocumulo.action.ActLoadComunicazioniEsecSorv"-->
  <input type="hidden" name="nextAction" value="<%=nextAction%>">
    
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/ElencoComunicazioni.jsp"/>
  <br>
  
    <%
    //==========================================================================
    // Tabella con le eventuali trasmissioni già effetuate
    //==========================================================================
    %>
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
  <tr>
      <td class="titolo" colspan="4">Nuove Comunicazioni</td>
    </tr>
  <tr>
<% if ("A".equals(ProvvedimentoCumulo.getFlagDocumentoRegistrato())) {%>
<td class="l" colspan="1"><font class="cRosso">Provvedimento di cumulo annullato, non è possibile emettere ulteriori comunicazioni</font>
</td>
<% } else { %>
      <td class="l" colspan="1">Data emissione Comunicazione
        <input type="text" Title="Giorno Emissione provvedimento"
          name=<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %> maxlength="2" size="2" 
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(DateUtils.getSysDate(),"dd"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento"   
          name=<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>  maxlength="2" size="2"
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(DateUtils.getSysDate(),"MM"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" 
          name=<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>  maxlength="4" size="4" 
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy"))%>"  
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<% } %>
    </tr>
  
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int" rowspan="2">Autorità</td>
      <td class="int" rowspan="2">Anno/Numero <br>SIEP</td>
      <td class="int" colspan="2">&nbsp;Comunicazione &nbsp;</td>
    </tr>
    <tr>
      <!--td class="int" colspan="8">&nbsp;</td-->
      <td class="int">Invia</td>
      <td class="int">Inviata</td>
    </tr>

<%
int contaCheck = 0;
%>

    
    <tr>
      <td class="titolo" colspan="4">Procure</td>
    </tr>
<% if (ListaTitoli==null || ListaTitoli.size()==0) {%> 
    <tr>
      <td class="l" colspan="4">Nessuno dei procedimenti cumulati appartiene a Procure esterne</td>
    </tr>
<% } %>

    <%
    // Individuazione AutoritaSiepCumulante
    String AutoritaSiepCumulante = "";
    
      Iterator it1 = ListaTitoli.iterator();
   
      while ( it1.hasNext())  {
        TitoloCumulatoModel lTitCum = (TitoloCumulatoModel)it1.next();
        if (   lTitCum.getProcedimentoCumulato()!=null 
            && lTitCum.getProcedimentoCumulato().getIdFascicoloSiepOrigine()!=null ) {
          ProcedimentoCumulatoModel lProcCum = lTitCum.getProcedimentoCumulato();
          if (lProcCum!=null &&
            lProcCum.getIdFascicoloSiepOrigine().equals(IstruttoriaCumulo.getFasSieIdFascicoloSiep() ) ){
              AutoritaSiepCumulante = StringUtils.toStringJSP(lProcCum.getDescrTipoUfficioFasCumulato()+" di "+StringUtils.toStringJSP(lProcCum.getDescrLuogoUfficioFasCumulato() ) );
          }
         }
      }
        
    
    // Caricamento Titoli      
    String lastAutoritaSiep = "";
      int id_record = 0;
      Iterator itx = ListaTitoli.iterator();
      
      while ( itx.hasNext()) 
      {
        id_record = id_record+1;
    
        TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel)itx.next();
        
        boolean isTitoloCumulante = false;
        if (   lTitoloModel.getProcedimentoCumulato()!=null 
            && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine()!=null
            && lTitoloModel.getProcedimentoCumulato().getIdFascicoloSiepOrigine().compareTo(IstruttoriaCumulo.getFasSieIdFascicoloSiep())==0
           )
        {
          continue; // salto ovviamente il cumulante
        }
      
        String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente()+" di "+lTitoloModel.getDescrLuogoEmittente();
        
        if (lTitoloModel.getNumSezioneAutoritaEmittente()!=null)
           AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();
  
        String nSiep = "";
        String AutoritaSiep = "";
        ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
        if (lProcedimentoCumulatoModel!=null)
        {
          if (AutoritaSiepCumulante.length()<1){
          if (IstruttoriaCumulo.getFasSieIdFascicoloSiep().equals(lProcedimentoCumulatoModel.getIdFascicoloSiepOrigine()) )
                  AutoritaSiepCumulante = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
        }
          if ("S".equals(lProcedimentoCumulatoModel.getFlagAccorpato()) ){
            UfficioModel lUfficioOrigine = lProcedimentoCumulatoModel.getUfficioOrigine();
  
            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrOrigine();
            nSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
          }
          else {
            nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
          }
          
          AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
        }
        else {
          continue;
        }
    %>
		<tr>
          <% if (AutoritaSiep.equals(lastAutoritaSiep)) { %>
          <td class="l" ></td>
          <% } else {%>
          <td class="l" >&nbsp;<%=AutoritaSiep%></td>
          <% } %>
          
          <% lastAutoritaSiep = AutoritaSiep; %>
          
          <% if (lProcedimentoCumulatoModel!=null)  { %>
          <td class="c" nowrap>
            <%=nSiep%>
          </td>
<%--           <% } else { %> --%>
<!--           <td class="c"  nowrap>&nbsp;</td> -->
          <% } %>
          
          <!--  Colonna Azioni  -->
          <td class="c" nowrap>
            <% if ("A".equals(ProvvedimentoCumulo.getFlagDocumentoRegistrato())) {%>
            &nbsp;
            <% } else if (AutoritaSiep.equals(AutoritaSiepCumulante)) { %>
              <%-- 
              <input type="checkbox" name="<%=(id_record==1)?StringUtils.toStringJSP(ICostantiUfficio.CAMPO_COD_UFFICIO):"" %>" value="" checked="checked" disabled="disabled" >
              --%>
              
              &nbsp;
            <% } else { 
              // 27/08/2018 Verifica se una comunicazione è stata inviata all'AutoritàSiep riferita al fascicolo cumulato.

              if (titoliGiaComunicati.contains(lTitoloModel.getIdTitoloCumulato() ) ) { 
//              if (ufficiDestinatariComunicazioni.contains(lProcedimentoCumulatoModel.getCodUfficioFasCumulato() ) ) { 
%>
                &nbsp;
               <% } else { 
contaCheck++;
            %>
                <input type="checkbox" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO %>" value="<%=StringUtils.toStringJSP(lProcedimentoCumulatoModel.getCodUfficioFasCumulato() )%>-<%=lProcedimentoCumulatoModel.getTitIdTitoloCumulato()%>">
             <% } %>
            <% } %>
          </td>
          <td class="c" nowrap>
            <% // 27/08/2018 Se una comunicazione è stata inviata all'AutoritàSiep, si mostra il visto.
              if (titoliGiaComunicati.contains(lTitoloModel.getIdTitoloCumulato() ) ) { 
//          if (ufficiDestinatariComunicazioni.contains(lProcedimentoCumulatoModel.getCodUfficioFasCumulato() ) ) { 
%>
              <img src="<%=IWebConstants.IMAGES_DIR%>V.gif">
            <% } else { %>
            &nbsp;
            <% } %>
          </td>
        </tr>
    <% } // end while su iterator %>
    
    <%
    //==========================================================================
    // Aggiungo se presenti come destinatari gli uffici di sorveglianza    
    //==========================================================================
    %>
    <tr><td colspan="4">&nbsp;</td></tr>
    
    <tr>
      <td class="titolo" colspan="4">Uffici di Sorveglianza</td>
    </tr>
<%  if(UfficiSorveglianza==null || UfficiSorveglianza.size() == 0 ) { %>    
    <tr>
      <td class="l" colspan="4">&nbsp;Nessun ufficio di Sorveglianza indicato nel provvedimento di cumulo  </td>
    </tr>
<%  }  %>     
    
<%  

//TitoloCumulatoModel lTitoloCum = (TitoloCumulatoModel)ListaTitoli.get(0);
      
    Iterator itxUS = UfficiSorveglianza.iterator();
    while ( itxUS.hasNext()) { 
      UfficioModel lUffNot = (UfficioModel)itxUS.next();
      if (lUffNot!=null && lUffNot.getDescrTipoUfficio()!=null)
      {
     %>
          <tr>
            <td class="l" >&nbsp;<%=lUffNot.getDescrTipoUfficio()%>&nbsp;di&nbsp;<%=lUffNot.getDescrComune()%> </td>
            <td class="c" >&nbsp;</td>
            <td class="c" nowrap>
<% if ("A".equals(ProvvedimentoCumulo.getFlagDocumentoRegistrato())) {%>
&nbsp;
<%          // 27/08/2018 Verifica se una comunicazione è stata inviata all'Ufficio di Sorveglianza.
            } else if (ufficiDestinatariComunicazioni.contains(lUffNot.getCodUfficio() ) ) { %>
                &nbsp;
              <% } else { 
              contaCheck++;
              %>
              <input type="checkbox" name="<%=ICostantiUfficio.CAMPO_COD_UFFICIO %>" value="<%=lUffNot.getCodUfficio()%>-0">
             <% } %>
            </td>
            <td class="c" nowrap>
            <% // 27/08/2018 Se una comunicazione è stata inviata all'Ufficio di Sorveglianza, si mostra il visto.
               if (ufficiDestinatariComunicazioni.contains(lUffNot.getCodUfficio() ) ) { %>
              <img src="<%=IWebConstants.IMAGES_DIR%>V.gif">
            <% } else { %>
            &nbsp;
            <% } %>
            </td>
          </tr>
   <% }
      }
    %>
    
  </table>


<% if (!"A".equals(ProvvedimentoCumulo.getFlagDocumentoRegistrato()) && contaCheck>0 ) {%>

  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td style="text-align:left">
        <input class="bottone" type="submit" name="conferma"  style="width:200" title="Conferma" value="Conferma">
      </td>      
    </tr>
  </table>
<% } %>

  </form>

</body>
</html>
  
 <% if (!"A".equals(ProvvedimentoCumulo.getFlagDocumentoRegistrato())) {%>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione della comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione della comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione della comunicazione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
<% } %>

