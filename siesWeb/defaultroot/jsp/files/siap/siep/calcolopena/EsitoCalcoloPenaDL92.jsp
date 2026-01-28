<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Date"%>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.calcolopena.model.SemestreDL92Model" %>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena"%>

<jsp:useBean id="EsitoCalcolo"   scope="request" class="siap.siep.calcolopena.model.CalcoloPenaDL92Model" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="msgStoricizzazione" scope="request" class="java.lang.String" />


<%
//==============================================================================
// Jsp per il calcolo della pena "virtuale" DL 92/2024  
// La jsp visualizza:
//==============================================================================
%>
<html>
<head>
  <title>[S.I.E.S.] - Calcolo Pena DL92/2024 </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" >
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
  
  function tornaIndietro()
  {
    //document.CalcoloPenaDL92.tipoOutput.value = "indirtro";
    document.CalcoloPenaDL92.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.calcolopena.action.ActLoadCalcoloPenaDL92";
    document.CalcoloPenaDL92.submit();
  }
  
  function stampaSiep()
  {
    document.CalcoloPenaDL92.tipoOutput.value = "stampaTemplate";
    document.CalcoloPenaDL92.submit();
  }
  
  function stampaSiepXls()
  {
    document.CalcoloPenaDL92.tipoOutput.value = "stampaExcel";
    document.CalcoloPenaDL92.submit();
  }    
  
  function calcolaDL92() {      
    document.CalcoloPenaDL92.tipoOutput.value = "dettaglio";
    document.CalcoloPenaDL92.submit();
  }
  
  <%-- MEV-2026_1 --%>
  function salvaDL92() {      
	var msgAlert = "Si stanno storicizzando i calcoli sul procedimento corrente. Si vuole procedere?";
	if (window.confirm(msgAlert)){
      document.CalcoloPenaDL92.tipoOutput.value = "storicizza";
      document.CalcoloPenaDL92.submit();
    }
  }
  
  function storicoDL92() {
	  document.StoricoCalcoloPenaDL92.submit();	  
  }
  <%-- MEV-2026_1 FINE --%>
  
  
  function checkSel (checkObj, idSemestre) {
  if (checkObj.checked) {
      document.getElementById(idSemestre).value = "S";
  }
  else {
      document.getElementById(idSemestre).value = "N";
    }
  }
  
  function showMsg () {
	  <% if (msgStoricizzazione.length()>0) { %>
	  alert("<%=msgStoricizzazione%>");
	  <% } %>	  
  }
  </script>
</head>

<body class="corpo" onload="showMsg();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="../../images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Calcolo pena ipotetica con detrazioni DL. 92/2024</font>
      </td>
      <!-- BOTTONE DI STAMPA -->
      <td class="LBG">
        <a href="Javascript:stampaSiep()" >
          <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
        </a>
      </td> 
      <td class="LBG">
        <a href="Javascript:stampaSiepXls()" >
          <img  align="middle" src="/images/xls.jpg" alt="Generazione Stampa" width="24" height="24" border="0">
        </a>
      </td>            
      <td class="LBG">
        <a href="Javascript:tornaIndietro();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <% if (session.getAttribute("fascicolo") != null) { %>
  <jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <% } %>  

  
  <form method="POST" action="/jsp/Main.jsp" name="CalcoloPenaDL92">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActCalcoloPenaDL92">
    <input type="hidden" name="tipoOutput" value="">    
    
    <%--Per lanciare le stampe inserisco i campi hidden della form di partenza --%>
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>"   value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumAnniReclusione()  , "0") %>"   >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>"   value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumMesiReclusione()  , "0") %>"   >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>" value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumGiorniReclusione()  , "0") %>" >
    
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>"   value="<%=StringUtils.getParteIntera   ( EsitoCalcolo.getImportoMulta()) %>"  >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" value="<%=StringUtils.getParteDecimale ( EsitoCalcolo.getImportoMulta()) %>"   >
    
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>"   value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumAnniArresto(), "0") %>" >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>"   value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumMesiArresto(), "0") %>" >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" value="<%= StringUtils.toStringJSP  ( EsitoCalcolo.getNumGiorniArresto(), "0") %>" >
    
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>"   value="<%=StringUtils.getParteIntera   ( EsitoCalcolo.getImportoAmmenda()) %>"   >
    <input type="hidden" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" value="<%=StringUtils.getParteDecimale ( EsitoCalcolo.getImportoAmmenda()) %>"   >
    
    <input type="hidden" name="<%=ICostantiCalcoloPena.CAMPO_NUM_ANNI_PRESOFFERTO%>"   value="<%= EsitoCalcolo.getNumAnniPresofferto()  %>" >
    <input type="hidden" name="<%=ICostantiCalcoloPena.CAMPO_NUM_MESI_PRESOFFERTO%>"   value="<%= EsitoCalcolo.getNumMesiPresofferto()  %>" >
    <input type="hidden" name="<%=ICostantiCalcoloPena.CAMPO_NUM_GIORNI_PRESOFFERTO%>" value="<%= EsitoCalcolo.getNumGiorniPresofferto()%>" >
    
    <input type="hidden" name="<%=ICostantiCalcoloPena.CAMPO_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(EsitoCalcolo.getPosizioneGiuridica())%>">
    
    <input type="hidden" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataInizioPena(), "dd")) %>" >
    <input type="hidden" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataInizioPena(), "MM")) %>" >
    <input type="hidden" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataInizioPena(), "yyyy")) %>" >   
  
    <input type="hidden" name="numSemestriElaborati" value="<%=StringUtils.toStringJSP(EsitoCalcolo.getListaSemetri().size()) %>" >   
  
    <%
      Vector <SemestreDL92Model> mListaSemestri = EsitoCalcolo.getListaSemetri();
      for (int i = 0; i<mListaSemestri.size(); i++) {
        SemestreDL92Model lSemestreUtile = mListaSemestri.elementAt(i);
        %>
        <input type="hidden" name="<%="prgSemestre_"+lSemestreUtile.getProgressivo()%>"   
                               id="<%="prgSemestre_"+lSemestreUtile.getProgressivo()%>"   
                            value="<%=StringUtils.toStringJSP(lSemestreUtile.getIsCompreso(), "S") %>" >   
        <%
      }
    %>   
  
  </form>
  
  <form method="POST" action="/jsp/Main.jsp" name="StoricoCalcoloPenaDL92">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActLoadStoricoCalcoloPenaDL92">
  </form> 
  
    <!-- ===================================================================== -->
    <!--                   SEZIONE CON I RISULTATO                             -->  
    <!-- ===================================================================== -->
    <div>
    <% 
      // Presofferto
      SemestreDL92Model lCalcoloPresofferto = EsitoCalcolo.getSemestrePresofferto();      
    %>
    <table cellspacing="4" cellpadding="4" width="55%">
      <tr>
        <td class="Titolo" colspan="4"><font class="label">Custodia cautelare (presofferto)</font></td>
      </tr>
      <tr>
        <td class="l" width="80%"><font class="label">Custodia cautelare (presofferto)</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniPresofferto(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiPresofferto(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniPresofferto(),"&nbsp;") %> giorni</font></td>
      </tr>      
      <tr>
        <td class="l"><font class="label">Semestri utili di custodia cautelare per erogazione L.A.:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getNumSemestriMaturati(),"")%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Giorni di L.A. maturati in custodia cautelare:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(),"") %></font></td>
      </tr>
      <tr>
        <td class="l" width="90%"><font class="label">Giorni di custodia cautelare eccedenti i semestri utili per erogazione L.A. (verranno conteggiati per anticipare il primo semestre utile nell'esecuzione pena):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getGiorniResiduiPresofferto(),"") %></font></td>
      </tr>
      
      <%-- ================================================================ --%>
      <%--                        PENA DA ESEGUIRE                          --%>
      <%-- ================================================================ --%>
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">Pena Da eseguire al netto della custodia cautelare (presofferto)</font></td>
      </tr>
      <tr>
        <td class="l" width="80%"><font class="label">Reclusione</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniReclusione(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiReclusione(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniReclusione(),"&nbsp;") %> giorni</font></td>
      </tr>   
      <tr>
        <td class="l" width="80%"><font class="label">Arresto</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniArresto(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiArresto(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniArresto(),"&nbsp;") %> giorni</font></td>
      </tr>  
      <tr>
        <td class="l" width="80%"><font class="label">Quantum Totali</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniDaEspiare(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiDaEspiare(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniDaEspiare(),"&nbsp;") %> giorni</font></td>
      </tr> 
      
      <%
      int lLAApplicate = EsitoCalcolo.getLAApplicate().intValue();
      int lPenaInEccesso = EsitoCalcolo.getLAFungibili().intValue();
      String colorFung = "";
      if (lPenaInEccesso>0) colorFung =  "style='color=red'";
      String colorNonConcessi = "";
      if (EsitoCalcolo.getLANonConcesse().intValue()>0) colorNonConcessi =  "style='color=red'";
      
     // int numGiorniLAMaturataInPenaresidua = lLAApplicate - EsitoCalcolo.getSemestrePresofferto().getLAApplicate().intValue();
      %>
      <tr>
        <td class="l"><font class="label">Semestri utili di pena ipotetica per erogazione L.A.:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getSemestriUtili (),"")%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Giorni di L.A. maturabili e usufruibili in pena ipotetica:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniLAMaturataInPenaresidua(),"") %></font></td>
      </tr>
      <tr>
        <td class="l" width="90%"><font class="label" >Giorni di L.A. maturabili e non usufruibili in pena ipotetica:</font></td>
        <td class="r" colspan="3"><font class="label" <%=colorFung %> ><%=StringUtils.toStringJSP(lPenaInEccesso,"") %></font></td>
      </tr>   
      <tr>
        <td class="l" width="90%"><font class="label" >Giorni di L.A. maturabili e non concessi in pena ipotetica:</font></td>
        <td class="r" colspan="3"><font class="label" <%=colorNonConcessi %> ><%=StringUtils.toStringJSP(EsitoCalcolo.getLANonConcesse(),"")%></font></td>
      </tr>   
      
      <%-- ================================================================ --%>
      <%--       DATI DA INDICARE NEL PROVVEDIMENTO DI ESECUZIONE           --%>
      <%-- ================================================================ --%>  
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class1="Titolo" colspan="4" style="BACKGROUND-COLOR: green; text-align:center; background-image: none;">
            <font class="label" style="COLOR: white">Dati da indicare nei provvedimenti di esecuzione</font>
        </td>
      </tr>
      <tr>
        <td class="r"><font class="label">Pena ipotetica ad esito delle detrazioni dei soli giorni di L.A. usufruibili:</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniPenaIpotetica(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiPenaIpotetica(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniPenaIpotetica(),"&nbsp;") %> giorni</font></td>
      </tr> 
      <tr>
        <td class="r"><font class="label">Semestri utili di pena scontata:</font></td>
        <td class="r" colspan="3"><font class="label"><%=EsitoCalcolo.getSemestriUtiliPenaScontata() %></font></td>
      </tr>            
      <tr>
        <td class="r"><font class="label">Giorni di liberazione anticipata concedibili:</font></td>
        <td class="r" colspan="3"><font class="label"><%=EsitoCalcolo.getLAMaturate().intValue() %></font></td>
      </tr>
      <tr>
        <td class="r"><font class="label">Giorni di liberazione anticipata usufruibili:</font></td>
        <td class="r" colspan="3"><font class="label"><%=EsitoCalcolo.getLAApplicate().intValue() %></font></td>
      </tr>
      
      <%-- ================================================================ --%>
      <%--              CALCOLI CON DATA DI ESECUZIONE                      --%>
      <%-- ================================================================ --%> 
      <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>    
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class1="Titolo" colspan="4" style="BACKGROUND-COLOR: green; text-align:center; background-image: none;">
          <font class="label" style="COLOR: white">CALCOLI CON DATA DI ESECUZIONE</font>
        </td>
      </tr>  
      <tr>
        <td class="r"><font class="label">Data decorrenza pena:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataInizioPena(), "dd/MM/yyyy")) %></font>&nbsp;</td>
      </tr> 
      <tr>
        <td class="r"><font class="label">Data scarcerazione senza calcolare la liberazione anticipata:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneNoLA(), "dd/MM/yyyy")) %></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="r"><font class="label">Data scarcerazione con giorni Liberazione Anticipata applicata per intero (data fine pena calcolata con giorni non usufruibili):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneLAFung(), "dd/MM/yyyy")) %></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="r"><font class="label">Data scarcerazione con giorni Liberazione Anticipata concessi (data fine pena calcolata con giorni di fungibilita'):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneLANoFung(), "dd/MM/yyyy")) %></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="r"><font class="label">Data scarcerazione senza applicare l'ultimo semestre di Liberazione:</font></td>
        <% if (EsitoCalcolo.getLAFungibili().intValue()>0) { %>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazionePenultimoSemestre(), "dd/MM/yyyy")) %></font></td>
        <% } else { %>
        <td class="r" colspan="3"><font class="label">non applicabile</font></td>
        <% } %>
      </tr>                               
      <% } %>               
    </table>
  </div>
  
  <br>
  
  <%-- ==================================================================== --%>
  <%--       SEZIONE CON IL DETTAGLO DEI SEMESTRI                           --%>
  <%-- ==================================================================== --%>  
  <div>
    <table cellspacing="4" cellpadding="4" width="55%">
      <tr>
        <td class="Titolo" colspan="9"><font class="label">Dettaglio per Semestri</font></td>
      </tr>
      <tr>
        <td class="Titolo" nowrap><font class="label">&nbsp;</font></td>
        <td class="Titolo" nowrap><font class="label">&nbsp;</font></td>
        <td class="Titolo" nowrap><font class="label">L.A. APPLICATA</font></td>
        <td class="Titolo" nowrap style="display:none1;"><font class="label">COMPRESA</font></td>
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="Titolo" nowrap><font class="label">Data Maturazione L.A.</font></td>
        <td class="Titolo" nowrap><font class="label">Nuova Data Scadenza</font></td>
        <% } %>
        <td class="Titolo" nowrap colspan="3"><font class="label">RESIUDO PENA</font></td>
      </tr>      
      

      <%-- ================================================================ --%>
      <%--                         Presofferto                              --%>
      <%-- ================================================================ --%>        
      <tr>
        <td class="r" nowrap><font class="label"><%=lCalcoloPresofferto.getNumSemestriMaturati()%></font></td>
        <td class="l" nowrap><font class="label">SEMESTRI ESPIATI IN C.C.</font></td>
        <td class="c" nowrap><font class="label"><%=lCalcoloPresofferto.getLAApplicate() %></font></td>
        <td class="c" nowrap style="display:none1;"><font class="label">&nbsp;</font></td>
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="c" nowrap><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloPresofferto.getDataMaturazioneLA(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <td class="c" nowrap><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloPresofferto.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <% } %>
        <td class="r" nowrap><font class="label"><%=lCalcoloPresofferto.getResiduoNumAnni()+" anni" %></font></td>
        <td class="r" nowrap><font class="label"><%=lCalcoloPresofferto.getResiduoNumMesi()+" mesi" %></font></td>
        <td class="r" nowrap><font class="label"><%=lCalcoloPresofferto.getResiduoNumGiorni()+" giorni" %></font></td>
      </tr> 
      
      <%
      Vector <SemestreDL92Model> mListaSemetri = EsitoCalcolo.getListaSemetri();
      
      for (int i = 0; i<mListaSemetri.size(); i++) {
        SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
        String lPenaStr = lSemestreUtile.getResiduoNumAnni()+" anni - " 
                        + lSemestreUtile.getResiduoNumMesi()+" mesi - "
                        + lSemestreUtile.getResiduoNumGiorni()+" giorni ";
        
        String lColorLastSem="";
        if (lSemestreUtile.getLAApplicate().intValue()<45)
          lColorLastSem =  "style='color=red'";
        
      %>
      <tr>
        <td class="l" nowrap><font class="label"><%=lSemestreUtile.getProgressivo()%>&deg;</font></td>
        <td class="l" nowrap><font class="label">semestre utile per L.A.</font></td>
        <td class="c" nowrap><font class="label" <%=lColorLastSem%> ><%=lSemestreUtile.getLAApplicate() %></font></td>
        
        <td class="c" nowrap style="display:none1;">
            <input type="checkbox" 
                   name="xx" 
                   <%="S".equals(lSemestreUtile.getIsCompreso())?"checked":""%> 
                   onCLick="javascript:checkSel(this, '<%="prgSemestre_"+lSemestreUtile.getProgressivo()%>')">
        </td>
        
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="c" nowrap><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lSemestreUtile.getDataMaturazioneLA(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <td class="c" nowrap><font class="label" <%=lColorLastSem%> ><%=StringUtils.toStringJSP(DateUtils.getDateToString (lSemestreUtile.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <% } %>
        <td class="r" nowrap><font class="label" <%=lColorLastSem%> ><%=lSemestreUtile.getResiduoNumAnni()+" anni"%></font></td>
        <td class="r" nowrap><font class="label" <%=lColorLastSem%> ><%=lSemestreUtile.getResiduoNumMesi()+" mesi " %></font></td>
        <td class="r" nowrap><font class="label" <%=lColorLastSem%> ><%=lSemestreUtile.getResiduoNumGiorni()+" giorni" %></font></td>
      </tr>
      <% } %>     
      
      
      <tr>
        <td class="l"><font class="label"></font></td>
        <td class="l"><font class="label"></font></td>
        <td class="c"><font class="label"><%=EsitoCalcolo.getLAApplicate().intValue() %></font></td>
        <td class="r"><font class="label"></font></td>
        <td class="r"><font class="label"></font></td>
        <td class="r"><font class="label"></font></td>
      </tr> 
    </table>
    
    <table>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td colspan="1">
          <input class="bottone" type="submit" name="INSERISCI" value="Ricalcola"
               onClick="javascript:calcolaDL92();">
        </td>
        <%-- MEV-2026_1 --%>
        <% 
        if (session.getAttribute("fascicolo") != null) {
          FascicoloSiepModel lFascicolo = (FascicoloSiepModel) session.getAttribute("fascicolo");
          if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lFascicolo.getChiaveUfficio())) { 
        %>
	        <td colspan="1">
	          <input class="bottone" type="submit" name="Valida" value="Valida"
	               onClick="javascript:salvaDL92();">
	        </td>
	        <td colspan="1">
	          <input class="bottone" type="submit" name="Storico" value="Storico Calcoli Validati"
	               onClick="javascript:storicoDL92();">
	        </td>
	      <% } %>
        <% } %>
      </tr>
    </table>
    
  </div>
  
  <br>  


<% 
Date lDataScarcerazione = null;
if (EsitoCalcolo.getDataScarcerazioneLAFung() != null)
  lDataScarcerazione = EsitoCalcolo.getDataScarcerazioneLAFung();
else 
  lDataScarcerazione = EsitoCalcolo.getDataScarcerazioneNoLA();
%>
  <div>
    <table cellspacing="4" cellpadding="4" width="55%"> 
      <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_LIBERO)) { %>
      <tr>
        <td class="Titolo" colspan="4"><font class="label">L.A. MATURATA MA NON APPLICATA / PENA ESPIATA IN ECCESSO</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label" <%=colorFung %>><%=EsitoCalcolo.getLAFungibili() %></font></td>
      </tr> 
      <% } else if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) { %> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">DATA SCARCERAZIONE</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lDataScarcerazione, "dd/MM/yyyy")) %></font></td>
      </tr> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">SURPLUS DETENZIONE (GIORNI DI FUNGIBILITA')</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label" <%=colorFung %>><%=EsitoCalcolo.getLAFungibili() %></font></td>
      </tr>            
      <% } %> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">GIORNI L.A. CONCESSI (MATURATI)</font></td>
      </tr>    
      <tr>
        <td class="c"><font class="label"><%=EsitoCalcolo.getLAMaturate() %></font></td>
      </tr>        
      <tr>
        <td class="Titolo" colspan="4"><font class="label">GIORNI L.A. NON CONCESSI (MATURATI)</font></td>
      </tr>    
      <tr>
        <td class="c"><font class="label"  <%=colorNonConcessi%>><%=EsitoCalcolo.getLANonConcesse() %></font></td>
      </tr>            
    </table>
  </div>
 
</body>
</html>

