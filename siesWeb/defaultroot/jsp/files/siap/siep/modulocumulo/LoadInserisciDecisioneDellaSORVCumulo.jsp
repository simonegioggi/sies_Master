<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>


<%@page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="UfficioEmittente"      scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"           scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaSORV"         scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TitoliRichiesta"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="ProvvSORVCum"          scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>
<jsp:useBean id="TipoMisuraSicurezza"   scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>

<!--      LoadInserisciDecisioneDellaSORVCumulo        -->
<%
//==============================================================================
//  Form con le funzioni di Inserimento/Modifica della decisione della SORV.
//  a seguito di Richiesta del PM di:
//  - Revoca LA
//  - Unificazione MS
//==============================================================================

int TotRic = TitoliRichiesta.size();

RichiesteInviateCumModel lRicInv = null;
if(RichiestaSORV!=null && RichiestaSORV.getRichiesteInviateCum()!=null)
{
  lRicInv = (RichiesteInviateCumModel)RichiestaSORV.getRichiesteInviateCum();
  //LogF3B.getLogger().debug(" --XX-- Richiesta Inviata = "+lRicInv);
}
String appTitle = "";
String appStrInConf ="";
String appStrInDiff ="";
if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("020")==0) {
  appTitle = " Revoca L.A.";
  appStrInConf ="in conformita' alla richiesta del PM";
  appStrInDiff ="in difformita' alla richiesta del PM";
}
if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("022")==0) {
  appTitle = " Unificazione M.S.";
  appStrInConf ="Unifica le misure";
  appStrInDiff ="Non unifica le misure";
}
String operazione = "";
if (modalita.trim().compareTo("I")==0)
  operazione="Inserimento";
if (modalita.trim().compareTo("M")==0)
  operazione="Modifica";
  
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    
  //Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste alla SORV
    //==========================================================================
    function tornaIndietro(action)
    {
      document.LoadInsDecSORV.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInsDecSORV.submit();
    }
    
    function Verify() 
    { 
      // Tipo Provvedimento
      var tipoProvv = document.LoadInsDecSORV.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value;
      //alert ("tipoProvv = "+tipoProvv);
      if (tipoProvv=='-') {
        alert('Indicare il tipo di Provvedimento');
        document.LoadInsDecSORV.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
        return false;
      }       

      // Data Emissione Procedimento
      if (document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
         document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
      if (document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
         document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

      var data_to_verify = document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
      if (data_to_verify=='//' )
      {
        alert('Indicare la Data Emissione Procedimento');
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
        return false;
      }
       
      if (!ControllaData(data_to_verify) )
      {
        alert('Data Emissione Procedimento NON valida');
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
        return false;
      }  

      // Anno e Numero Provvedimento SIUS
      if(   document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' 
         || document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''  )
      {
        alert('Digitare Anno e Numero Provvedimento SIUS');
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
        return false;
      }

      // Anno / Numero SIUS
      if(   document.LoadInsDecSORV.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value == '' 
         || document.LoadInsDecSORV.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value == ''  )
      {
        alert('Indicare Anno e Numero SIUS');
        document.LoadInsDecSORV.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
        return false;
      }
      
      // Ufficio Emittente
      if(document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-')
      {
        alert("Indicare l'Ufficio di Sorveglianza");
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
        return false;
      } 
       
      // Sede
      if(document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == "" )
      {
        alert("Indicare la Sede dell'Ufficio di Sorveglianza");
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
      }

      <% if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("020")==0) { %> 
      // giorni di LA
      if(   document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV_D%>.value == "" 
         && document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV_D%>.value == "" 
         && document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV_D%>.value == "" 
        )
      {
        alert("Indicare il numero di giorni di LA da revocare");
        document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV_D%>.focus();
        return false;
      }
      <% } %>
 
      // [Aggiungere Misure sicurezza]
      
      return true; 
    }
    
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=operazione %> Decisione della Sorveglianza su Richiesta <%=appTitle %> </font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste alla SORV -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInsDecSORV">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDellaSORVCumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaSORV.getIdRichiestePmInCumulo() %>" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="modalita" value="<%=modalita%>" >

<%  // =======================================================
  //    Dati sui Titoli coinvolti nella Richiesta 
  // =======================================================
%>

  <table cellpadding="2" cellspacing="2" width="98%" align="center" style="border:0;">
     <tr><td class="Titolo" colspan="100%">In relazione ai seguenti Titoli:</td></tr>
<%        
  String DescAuto="";
  String AnnoNumero ="";
  Iterator itx = TitoliRichiesta.iterator();
  while(itx.hasNext())
  { 
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
    
    AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
    DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%> 
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        <font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        <font class="label"> Emessa da </font>
        <font class="campo"><%=DescAuto%></font>&nbsp;
        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>
    <tr><td></td></tr>   
<% }  %>

  <%
  //========================================================================
  //  Dati esclusivi della Richiesta
  //========================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td> 
<%  if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("020")==0) { %>  
      <tr>
        <td class="l" width="200px">Revoca nella misura di giorni :</td>
        <td class="l" >
          Liberazione Anticipata: 
          <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getNumGiorniRevocaLA() )%></font>
          - Liberazione Anticipata Speciale: 
          <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getNumGiorniRevocaLS() )%></font>
          - Integrazione Liberazione Anticipata: 
          <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getNumGiorniRevocaLI() )%></font>
        </td>
      </tr>
<%  } %>      
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni() )%>&nbsp;</font>
      </td>
    </tr>

<%  if(lRicInv!=null && lRicInv.getIdRichiesteInviateCum()!=null )
  { %>  
      <table width="95%" align="center" style="display:block">
        <tr><td> </td></tr>
        <tr>
            <td class="l" colspan="1" width="200px">Inviata a : </td>
            <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrUfficioDest(),"")%></font>
              di <font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrLuogoDest(),"")%></font>
            </td>
            <td class="l" colspan="1">in data: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRicInv.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
        </tr>
<%        if( !("null").equals(lRicInv.getContenuto()) )
        {   %>
          <tr>
            <td class="l" colspan="1" width="200px">Contenuto </td>
            <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"-")%></font></td>
          </tr>
 <%       } %>           
      </table>
<%  } %>  


  </table>
  
    <br>

  
<!--              Inizio Dati Inseribili/Modificabili                    -->  
  <%
  //========================================================================
  //  Dati esclusivi della Decisione da Inserire / Modificare
  //========================================================================
  %>  
<tr>
  <td colspan="100%" align="center">

  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati Provvedimento della Sorveglianza</td></tr>      

    <tr>
      <td class="l" width="15%" >Tipo <font class=ob>(*)</font></td>

      <td class="l">
        <select  name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO %>" Title="Tipo Provvedimento" >
          <%=TipoProvvedimento%>
        </select>
      </td>

      <td class="l" width="15%" >Data emissione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text"  title="Giorno di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "dd"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           /
        <input type="text"  title="Mese di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "MM"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
        <input type="text"  title="Anno di emissione documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvSORVCum.getDataD(), "yyyy"), "") %>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
       
    <tr>      
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"  type="text" size="4" maxlength="4"   
                value="<%=StringUtils.toStringJSP(ProvvSORVCum.getAnnoProvv(), "") %>"
                name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" 
                onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumeroProvv(), "") %>" 
                name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" type="text" size="6" maxlength="6" 
                onkeypress="return TicTabNumField(this,event)">
      </td>
      
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" type="text" size="4" maxlength="4" 
               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getAnnoSIUS(), "") %>" 
               name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>"  
               <%=IWebConstants.UTIL_DATA_ANNO%>>
        /
        <input Title="Numero Sius" type="text" size="6" maxlength="6"
               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumeroSIUS(), "") %>" 
               name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" >
      </td>
    </tr>    

    <tr>
      <td class="l">Ufficio <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Autorità Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
          <%=UfficioEmittente%>
        </select>
      </td>
      <td class="l" width="15%" >Sede <font class="ob">(*)</font> &nbsp; </td>
      <td class="l" >
        <input title="Sede Autorita"  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>"  
               value="<%=StringUtils.toStringJSP(ProvvSORVCum.getDescrLuogoEmittente(), "") %>" maxlength="30" size="30">
        <a href="Javascript:ListaUfficiComuni('LoadInsDecSORV','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
                 document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecSORV.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>    
  </table>
    
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan=3>
<% 
        String checkC = "";
        String checkD = "";
        String checkR = "";
        String checkI = "";
      
        if("C".equals(ProvvSORVCum.getFlagConforme()) ){
          checkC = "checked";
        }
        else if("D".equals(ProvvSORVCum.getFlagConforme()) ) { 
          checkD = "checked";
        }
        else if("R".equals(ProvvSORVCum.getFlagConforme()) ) { 
          checkR = "checked";
        }
        else if("I".equals(ProvvSORVCum.getFlagConforme()) ) { 
          checkI = "checked";
        }
        else { checkC = "checked"; }
%>  
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="C" <%=checkC%> ><%=appStrInConf%>&nbsp;&nbsp;  <!-- Conforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="D" <%=checkD%> ><%=appStrInDiff%>&nbsp;&nbsp;  <!-- Difforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="R" <%=checkR%> >rigetta &nbsp;&nbsp;  <!-- Rigetta -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="I" <%=checkI%> >dichiara inammissibile &nbsp;&nbsp; <!-- Inammissibile -->  
          
        </td>
      </tr>
      
<% // Scarico decisione Revoca LA %> 
<%    if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("020")==0) { %>      
      <tr>
        <td class="l" colspan="1" width="300px">Liberazione Anticipata revocata nella misura di giorni</td>
        <td class="l" colspan="1">  
          <table>
            <tr>
              <td class="L">Liberazione Anticipata</td>
              <td class="L">
                <input type="text" size="5" maxlength="4"
                       name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV_D%>" 
                       value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniRevocaLaD(),"") %>"
                       onkeypress="return TicTabNumField(this,event)">
              </td>
            </tr>
            <tr>
              <td class="L">Liberazione Anticipata Speciale</td>
              <td class="L">
                <input type="text" size="5" maxlength="4"
                       name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LS_REV_D%>" 
                       value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniRevocaLsD(),"") %>"
                       onkeypress="return TicTabNumField(this,event)">
              </td>
            </tr>
            <tr>
              <td class="L">Integrazione Liberazione Anticipata</td>
              <td class="L">
                <input type="text" size="5" maxlength="4"
                       name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LI_REV_D%>" 
                       value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniRevocaLiD(),"") %>"
                       onkeypress="return TicTabNumField(this,event)">                              
              </td>
            </tr>
          </table>
        </td>
        
        <%--
        <td class="l" colspan="1">
          <input type="text" size="5" maxlength="4"
                 name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_LA_REV_D%>" 
                 value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniRevocaLaD(),"") %>"
                 onkeypress="return TicTabNumField(this,event)">
        </td>
        --%>
      </tr>
<%    } %>

<% // Scarico decisione unificazione delle Misure %>
<% if (RichiestaSORV.getCodTipoAnnotazione().trim().compareTo("022")==0) { %>      
    <tr>
      <td class="l">Tipo Misura  <font class="ob">(*)</font></td>
      <td class="l">
        <select title="Tipo Misura" name="<%= ICostantiMisuraSicurezzaCumulo.CAMPO_COD_TIPO  %>">
          <%=TipoMisuraSicurezza%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Durata Misura  <font class="ob">(*)</font></td>
      <td class="l">
        &nbsp;&nbsp;
         Anni
         <input title="Anni" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
                value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumAnniMsD()) %>"  
                name="<%= ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_MS %>"  >
         Mesi
         <input title="Mesi" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
                value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumMesiMsD()) %>"  
                name="<%= ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_MS%>"  >
         Giorni 
         <input title="Giorni" size="2" maxlength="2" type="text" ONKEYPRESS="return TicTabNumField(this,event)" 
                value="<%=StringUtils.toStringJSP(ProvvSORVCum.getNumGiorniMsD()) %>" 
                name="<%= ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_MS %>"  >
       </td>
     </tr>
<%    } %>
      
    <tr>
      <td class="l" colspan="1">Motivazioni</td>
      <td class="l" colspan="1">
        <textarea cols="90" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(ProvvSORVCum.getMotivazioniD()) %></textarea>
      </td>
    </tr>
      
    </table>
    
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>
     
<!--          end dati inseribili / modificabili                          -->
  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecSORV");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>


