<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>


<jsp:useBean id="lPenaManuale"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="lPenaRicalcolata" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="libAntMod"        scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>


<jsp:useBean id="lGiorniLA" scope="request" class="java.lang.String"/>
<jsp:useBean id="lGiorniRD" scope="request" class="java.lang.String"/>

<jsp:useBean id="errFineReclusione" scope="request" class="java.lang.String"/>
<jsp:useBean id="errInizioArresto"  scope="request" class="java.lang.String"/>
<jsp:useBean id="errFinePena"       scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per avvisare l'utente, in fase di inserimento della pena residua manuale,
// dell'incongruenza delle date di decorrenza inserite e chiedere conferma per 
// poter procedere
//==============================================================================
%>
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Pena Residua Manuale </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  function Verify(tipo)
  {
    // serve solo per disabilitari i tasti
    document.WarningPenaMauale.INDIETRO.disabled = true;
    document.WarningPenaMauale.INSERISCI.disabled = true;
    if (tipo=="Conferma")
      document.WarningPenaMauale.submit();
    else 
      history.go(-1);
  }
  </script>
</head>

<body class="corpo">

<FORM name="comandi">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Residua Manuale</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
</FORM>

<%
//==============================================================================
//
//==============================================================================
%>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="WarningPenaMauale">
  <input type="HIDDEN" name="Action" value="siap.siep.penaresidua.action.ActInserisciPenaResiduaManuale" >
  <input type="HIDDEN" name="datiConfermati" value="SI">  <%//serve alla action per non rieffettuare i controlli una volta confermati i dati%>
  <%
  //============================================================================
  // Campi contenenti gli stessi dati inseriti in origine in modo che se 
  // l'utente conferma da lui inseriti posso ripassarli alla action di 
  // inserimento
  // n.b. compaiono solo i dati del caso non ergastolo, in quanto in 
  //      quest'ultimo caso non vengono effettuati controlli e non si arriva
  //      mai a questa finestra
  //============================================================================
  %>
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>"      value="<%=StringUtils.toStringJSP(lPenaManuale.getNumAnniReclusione(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>"      value="<%=StringUtils.toStringJSP(lPenaManuale.getNumMesiReclusione(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>"    value="<%=StringUtils.toStringJSP(lPenaManuale.getNumGiorniReclusione(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>"         value="<%=StringUtils.toStringJSP(lPenaManuale.getNumAnniArresto(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>"         value="<%=StringUtils.toStringJSP(lPenaManuale.getNumMesiArresto(), "")%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>"       value="<%=StringUtils.toStringJSP(lPenaManuale.getNumGiorniArresto(), "")%>">
  <% if (lPenaManuale.getImportoMulta() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>"     value="<%=StringUtils.getParteIntera   (lPenaManuale.getImportoMulta())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>"   value="<%=StringUtils.getParteDecimale (lPenaManuale.getImportoMulta())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_MULTA%>"     value="EUR">
  <%}%>
  <% if (lPenaManuale.getImportoAmmenda() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>"   value="<%=StringUtils.getParteIntera   (lPenaManuale.getImportoAmmenda())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" value="<%=StringUtils.getParteDecimale (lPenaManuale.getImportoAmmenda())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_VALUTA_IMPORTO_AMMENDA%>"   value="EUR">
  <%}%>
  

  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_ORDINARIA%>"      
                      value="<%=request.getParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_ORDINARIA)%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_SPECIALE%>"       
                      value="<%=request.getParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_SPECIALE)%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_INTEGRAZIONE %>"  
                      value="<%=request.getParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_LIBER_ANT_INTEGRAZIONE)%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92 %>" 
                      value="<%=request.getParameter(ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92)%>">

  
  <%// Data inizio%>
  <% if (lPenaManuale.getDataInizio() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>" value="<%=DateUtils.getDayToString   (lPenaManuale.getDataInizio())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>"   value="<%=DateUtils.getMonthToString (lPenaManuale.getDataInizio())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>"   value="<%=DateUtils.getYearToString  (lPenaManuale.getDataInizio())%>">
  <%} else {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>" value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>"   value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>"   value="">
  <%}%>
  
  <%// Data fine reclusione%>
  <% if (lPenaManuale.getDataFineReclusione() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>" value="<%=DateUtils.getDayToString   (lPenaManuale.getDataFineReclusione())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>"   value="<%=DateUtils.getMonthToString (lPenaManuale.getDataFineReclusione())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>"   value="<%=DateUtils.getYearToString  (lPenaManuale.getDataFineReclusione())%>">
  <%} else {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>" value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>"   value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>"   value="">
  <%}%>
  
  <%// Data inizio arresti%>
  <% if (lPenaManuale.getDataInizioArresto() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>" value="<%=DateUtils.getDayToString   (lPenaManuale.getDataInizioArresto())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>"   value="<%=DateUtils.getMonthToString (lPenaManuale.getDataInizioArresto())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>"   value="<%=DateUtils.getYearToString  (lPenaManuale.getDataInizioArresto())%>">
  <%} else {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>" value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>"   value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>"   value="">
  <%}%>
  
  <%// Data Fine Pena %>
  <% if (lPenaManuale.getDataFine() != null) {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" value="<%=DateUtils.getDayToString   (lPenaManuale.getDataFine())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"   value="<%=DateUtils.getMonthToString (lPenaManuale.getDataFine())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"   value="<%=DateUtils.getYearToString  (lPenaManuale.getDataFine())%>">
  <%} else {%>
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"   value="">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"   value="">
  <%}%>
  
  <input type="HIDDEN" name="tipo" value="<%=lPenaManuale.getFlagPenaSospesa()%>">

<table cellspacing=2 cellpadding=2 width="95%">
  <tr>
    <td colspan="2" width="100%" class="l" style="text-align : center;"> 
    <p>Attenzione!! Le date di espiazione specificate differiscono da quelle calcolate in base ai dati inseriti.
    Si ricorda che tutte le date devono essere anticipate per effetto di eventuali giorni di Liberazione Anticipata concessi.
    Le date verranno comunque ricalcolate al prossimo calcolo della pena, ma in caso di eventi interuttivi saranno utilizzate per valutare i quantum di pena espiata e di pena residua.</p>
    <p>Scegliere 'Conferma' per proseguire o 'Indietro' per modificare i dati.</p>
    </td>
  </tr>
<table>

<table cellspacing=2 cellpadding=2 width="80%">
  <tr>
    <!--td colspan="2" width="100%"-->
    <td>
      <!-- ******************************************** -->
      <!--          Tabella con i quantum               -->
      <!-- ******************************************** -->
      <table cellspacing="2" cellpadding="2" width="100%">
        <tr>
          <td class="Titolo" colspan="4">Pena</td>
        </tr>
        <tr>
          <td class="l" width="20%">Reclusione</td>
          <td class="l">
          <% if (   lPenaManuale.getNumAnniReclusione() != null 
                 || lPenaManuale.getNumMesiReclusione() != null
                 || lPenaManuale.getNumGiorniReclusione() != null
                ) 
          { %>
            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumAnniReclusione(), "0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumMesiReclusione(), "0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumGiorniReclusione(), "0")%></font>
          <% } %>
         &nbsp;</td>
        <!--/tr>
        <tr-->
          <td class="l">Multa</td>
          <td class="l">
          <% if (lPenaManuale.getImportoMulta() != null) {%>
           <font class="campo"><%=StringUtils.toEuroFormat(lPenaManuale.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
          <% } %>&nbsp;
          </td>
        </tr>
        <tr>
          <td class="l">Arresto</td>
          <td class="l">
          <% if (   lPenaManuale.getNumAnniArresto() != null
                 || lPenaManuale.getNumMesiArresto() != null
                 || lPenaManuale.getNumGiorniArresto() != null
                ) 
          { %>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumAnniArresto(), "0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumMesiArresto(), "0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenaManuale.getNumGiorniArresto(), "0")%></font>
        <% } %>
           &nbsp;</td>
        <!--/tr>
        <tr-->
          <td class="l">Ammenda</td>
          <td class="l">
          <% if (lPenaManuale.getImportoAmmenda() != null) {%>
           <font class="campo"><%=StringUtils.toEuroFormat(lPenaManuale.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
          <% } %>&nbsp;
          </td>
        </tr>
        
        
        <%
        if (   lGiorniLA!=null && lGiorniLA.length()>0
            && Integer.parseInt(lGiorniLA)>0
           )
        {
        %>
        <tr>
          <td class="l" nowrap>
            <font class="label">Totale Liberazione Anticipata</font>
          </td>
          <td class="l" colspan="3">
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lGiorniLA)%></font>
          </td>
        </tr>
        <% } %>
        
        <%
        if (   lGiorniRD!=null && lGiorniRD.length()>0
            && Integer.parseInt(lGiorniRD)>0
           )
        {
        %>
        <tr>
          <td class="l" nowrap>
            <font class="label">Totale Risarcimento Danno</font>
          </td>
          <td class="l" colspan="3">
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lGiorniRD)%></font>
          </td>
        </tr>
        <% } %>
      </table>
    </td>
  </tr>
</table>
  <%
  //==========================================================================
  //                  Sezione con le date a confronto
  //==========================================================================
  %>
<table cellspacing=2 cellpadding=2>
  <tr>
    <td width="50%">
      <!-- ******************************************** -->
      <!--             DATE INSERITE                    -->
      <!-- ******************************************** -->
      <table>
        <tr>
          <td class="Titolo" colspan="2">Decorrenze Inserite</td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Decorrenza Pena </font>
          </td>
          <td class="l">
            <% if (lPenaManuale.getDataInizio() != null) {%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaManuale.getDataInizio(),"dd-MM-yyyy"))%></font>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Fine Reclusione </font>
          </td>
          <td class="l">
            <% if (lPenaManuale.getDataFineReclusione() != null) {%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaManuale.getDataFineReclusione(),"dd-MM-yyyy"))%></font>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Inizio Arresto </font>
          </td>
          <td class="l">
            <% if (lPenaManuale.getDataInizioArresto() != null) {%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaManuale.getDataInizioArresto(),"dd-MM-yyyy"))%></font>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
      	  <td class=l>
            <font class="label">Data Fine Pena </font>
          </td>
          <td class="l">
            <% if (lPenaManuale.getDataFine() != null) {%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaManuale.getDataFine(),"dd-MM-yyyy"))%></font>
            <%}%>&nbsp;
          </td>
        </tr>
      </table>
    </td>
    <!-- ******************************************** -->
    <!--             DATE RICALCOLATE                 -->
    <!-- ******************************************** -->
    <td width="50%">
      <table>
        <tr>
          <td class="Titolo" colspan="2">Decorrenze Calcolate</td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Decorrenza Pena </font>
          </td>
          <td class="l">
            <% if (lPenaRicalcolata.getDataInizio() != null) {%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataInizio(),"dd-MM-yyyy"))%></font>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Fine Reclusione </font>
          </td>
          <td class="l">
            <% if (lPenaRicalcolata.getDataFineReclusione() != null) {%>
              <% if (errFineReclusione.equals("SI")) {%>
              <font color="red"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFineReclusione(),"dd-MM-yyyy"))%></font>
              <%} else {%>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFineReclusione(),"dd-MM-yyyy"))%></font>
              <%}%>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
          <td class=l>
            <font class="label">Data Inizio Arresto </font>
          </td>
          <td class="l">
            <% if (lPenaRicalcolata.getDataInizioArresto() != null) {%>
              <% if (errInizioArresto.equals("SI")) {%>
              <font color="red"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataInizioArresto(),"dd-MM-yyyy"))%></font>
              <%} else {%>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataInizioArresto(),"dd-MM-yyyy"))%></font>
              <%}%>
            <%}%>&nbsp;
          </td>
        </tr>
        <tr>
      	  <td class=l>
            <font class="label">Data Fine Pena </font>
          </td>
          <td class="l">
            <% if (lPenaRicalcolata.getDataFine() != null) {%>
              <% if (errFinePena.equals("SI")) {%>
              <font color="red"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFine(),"dd-MM-yyyy"))%></font>
              <%} else {%>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFine(),"dd-MM-yyyy"))%></font>
              <%}%>
            <%}%>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="l" colspan="2">
      <input class="bottone" type="button" name="INDIETRO"  value="Indietro" onclick="javascript:Verify('Indietro');">
      <input class="bottone" type="button" name="INSERISCI" value="Conferma" onclick="javascript:Verify('Conferma');">
    </td>
  <tr>
</table>
</form>
</body>
</html>