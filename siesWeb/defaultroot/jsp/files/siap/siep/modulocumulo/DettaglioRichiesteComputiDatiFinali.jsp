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



<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel"   scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="ComputoCumulo" scope="request" class="siap.siep.modulocumulo.model.ComputiCumuloModel"/>


<%
//==============================================================================
// FORM di visualizzazione dettaglio Singola Richiesta con anticipazione
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();

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
      document.formIndietro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formIndietro.submit();
    }
    
    //==========================================================================
    //
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiesteComputiDatiFinali";
        document.formAzioni.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;        
        document.formAzioni.modalita.value = "M";
        document.formAzioni.submit();
      }
      else if (aTipoAzione=='Cancella'){
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInsRichiesteComputiDatiFinali";
          document.formAzioni.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.formAzioni.modalita.value = "C";
          document.formAzioni.submit();
        }
      }    
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
        <font class="campo">Modifica Richiesta al GE con anticipazione degli effetti</font>
      </td>
      <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Modifica')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:eseguiFunzione('Cancella')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>      
      </td>      
      <% } %>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
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

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formAzioni">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="<%=StringUtils.toStringJSP(ComputoCumulo.getIdComputiCumulo()) %>">
    
    <input type="hidden" name="modalita" value="">  
  </form>

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
      <td class="l" width="200px">Estremi Beneficio</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(ComputoCumulo.getDescrTipoAnnotazione()) %></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Provvedimento di Concessione</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(ComputoCumulo.getDescDpr())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Data Richiesta</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ComputoCumulo.getDataRichiesta(),"dd-MM-yyyy"))%>&nbsp;</font>
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Richiesta</td>
      <td class="l" colspan="3"><font class="campo"><%="+".equals(ComputoCumulo.getFlagPiuMeno())?"Concessione":"Revoca"%></font>&nbsp;</td>
    </tr>
    
    <tr>
      <td class="l">Reclusione</td>
      <% 
        String lStrReclusione = "";
        if (!ComputoCumulo.isQuantumReclusioneZero()) { 
          lStrReclusione+=" Anni: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumAnniReclusione(),"0")+"</font>";
          lStrReclusione+=" Mesi: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumMesiReclusione(),"0")+"</font>";
          lStrReclusione+=" Giorni: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumGiorniReclusione(),"0")+"</font>";
        }
      %>
      <td class="l"><%=lStrReclusione%>&nbsp;</td>
   
      <% 
        String lStrMulta = "";
        if (!ComputoCumulo.isMultaZero()) { 
          lStrMulta = "<font class='campo'>"+StringUtils.toEuroFormat (ComputoCumulo.getImportoMulta())+"</font> Euro";
        }
      %>
      <td class="l">Multa</td>
      <td class="l"><%=lStrMulta%>&nbsp;</td>
    </tr>
    
    <tr>
      <td class="l">Arresto</td>
      <% 
      String lStrArresto = "";
      if (!ComputoCumulo.isQuantumArrestoZero()) { 
        lStrArresto+="Anni: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumAnniArresto(),"0")+"</font>";
        lStrArresto+=" Mesi: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumMesiArresto(),"0")+"</font>";
        lStrArresto+=" Giorni: <font class='campo'>"+StringUtils.toStringJSP (ComputoCumulo.getNumGiorniArresto(),"0")+"</font>";
      }
      %>
      <td class="l"><%=lStrArresto%>&nbsp;</td>
 
      <% 
      String lStrAmmenda = "";
      if (!ComputoCumulo.isAmmendaZero()) { 
        lStrAmmenda = "<font class='campo'>"+StringUtils.toEuroFormat (ComputoCumulo.getImportoAmmenda())+"</font> Euro";
      }
      %>
      <td class="l">Ammenda</td>
      <td class="l"><%=lStrAmmenda%>&nbsp;</td>
    </tr>
    
  </table>  
</div>


</form>

</body>

</html>

