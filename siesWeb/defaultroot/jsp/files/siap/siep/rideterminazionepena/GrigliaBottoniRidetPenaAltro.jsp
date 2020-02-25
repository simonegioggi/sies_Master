<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="aEventoComputo"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>


<html>
<head>
  <title>[S.I.E.S.] - Provvedimenti e Stampe per Rideterminazione Pena Altro </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>
  
  <script>
    function eseguiFunzione(idFunzione)
    {
      var FlagValidato = "<%=aEventoComputo.getFlagDocumentoRegistrato()%>";
      
      if (idFunzione=="OE"){
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.ordineesecuzione.action.ActLoadInserisciOERidetPenaAltro";
      }
      else if (idFunzione=="OECS"){
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
        //document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.ordineesecuzione.action.ActLoadInserisciOECSRidetPenaAltro";
      }
      else if (idFunzione=="RP"){
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadInserisciOSRidetPenaAltro";
      }
      else if (idFunzione=="CNRP"){
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadInsComNuovoResPenaRidetPenaAltro";
      }
      else if (idFunzione=="VP"){
        if (FlagValidato=="S") {
          alert("Attenzione! Il provvedimento risulta già validato");
          return;
        }
        msgConfirm = "Attenzione si è richiesto di validare il computo senza emissione di ulteriore provvedimento";
        if (window.confirm(msgConfirm)) 
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActValidaRidePenaAltro";
        else 
          return; 
      }
      
      document.f.submit();
    }
  </script>
  

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=lbg>
        <font class="label">Funzione :&nbsp;</font><font class="campo">Provvedimenti e Stampe per Rideterminazione Pena</font>
      </td>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadDettaglioRidetPenaNew&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=aEventoComputo.getIdEvento()%>">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <!-- ===================================================================== -->
  <!--                 Sezione con la pena rideterminata                     -->
  <!-- ===================================================================== -->
  <% if (penaresidua != null && penaresidua.getIdPenaResidua() != null) { %>
  <table>
    <tr>
      <td class="Titolo"  colspan=10> Pena Ricalcolata </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoMulta()))%></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Arresto / Ammenda :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoAmmenda()))%></font></td>
    </tr>
  </table>
  
  <table>
    <%if(penaresidua.getDataInizio() != null) {%>
    <tr>
      <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFineReclusione() != null) {%>
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <%
    }
  
    if(penaresidua.getDataInizioArresto() != null) {%>
    <tr>
      <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFine() != null || penaresidua.getDataFinePresunta()!= null) {%>
      <td class="l"><font  class="label">Data Fine Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%if(penaresidua.getDataFine() != null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"),"-")%>
        <%} else if(penaresidua.getDataFinePresunta()!= null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"),"-")%>
        <%}%>
        </font>
      </td>
    </tr>
    <% } %>
    
    <%
    // Fungibilità
    CalendarUtil lCalendarUtil = new CalendarUtil();
    if ( !lCalendarUtil.isZero(fungibilita.getQuantumFungibilita() ) ) {
    %>
    <table>
      <tr>
        <td class="Titolo" colspan=9><font  class="label">Pena Espiata In Eccesso</font></td>
        <%
          String GGFung = fungibilita.getNumGiorni()+"";
          String MMFung = fungibilita.getNumMesi()+"";
          String AAFung = fungibilita.getNumAnni()+"";
        %>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="Campo"><%=AAFung%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="Campo"><%=MMFung%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="Campo"><%=GGFung%></font></td>
      </tr>
    </table>
   <% } %>
    
  </table>
  <% }  // end if penaresidua != null  %>
  

  <!-- ===================================================================== -->
  <!--                 Sezione con i Provvedimenti collegati                 -->
  <!-- ===================================================================== -->
  <br>

  <form method="POST"  action="<%=IWebConstants.PG_MAIN%>"  name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"      value="">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=aEventoComputo.getIdEvento()%>">
  
    <table cellpadding="1" cellspacing="1" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
      <tr>
        <td colspan="2" class="Titolonocap">Stampe</td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      <tr>
        <td class="titolo" colspan="2">Ordini Esecuzione</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:eseguiFunzione('OE');">Ordine di esecuzione</a></td>
        <td width="32%" class="menulines" nowrap><a href="javascript:eseguiFunzione('OECS');">Ordine di esecuzione con sospensione</a></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      <tr>
        <td class="titolo" colspan="2">Ordine Scarcerazione/Comunicazione</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:eseguiFunzione('RP');"> Rideterminazione Pena (Ordine scarcerazione)</a></td>
        <td width="32%" class="menulines" nowrap><a href="javascript:eseguiFunzione('CNRP');"> Comunicazione Nuovo Residuo Pena</a></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
  
      <tr>
        <td class="titolo" colspan="2">Validazioni</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:eseguiFunzione('VP');"> Valida Provvedimento</a></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
    </table>
  </form>    
</body>
</html>