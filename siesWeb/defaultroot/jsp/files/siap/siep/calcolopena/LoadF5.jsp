<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>

<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.fungibilita.model.FungibilitaModel" %>

<jsp:useBean id="lCalcoloPenaMod"  scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel" />
<jsp:useBean id="lPenComplMod"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="lUltimaPenResVal" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%
//==============================================================================
// Jsp per la visualizzazione del dettaglio della pena in un certo istante
// La jsp visualizza:
// - il dettaglio dei quantum che concorrono alla determinazione della pena 
//   finale da espiare
// - il quantum finale rideterminato (e gli importi)
// - il dettaglio dei giorni di LA computabili
// - la decorrenza della pena (date) detratti le LA
// - la Pena Virtuale (solo se pena non in decorrenza e non ergastolo)
// - l'eventuale pena espiata in eccesso
// - l'eventuale pena già espiata
//==============================================================================
%>

<%
boolean isErgastolo = false;
if (   lPenComplMod!=null 
    && lPenComplMod.getCodTipoPenaDetentiva()!=null
    &&(   lPenComplMod.getCodTipoPenaDetentiva().equals("03") 
       || lPenComplMod.getCodTipoPenaDetentiva().equals("04")
      )
   )
{
  isErgastolo = true;
}
%>

<html>
<head>
  <title>[S.I.E.S.] - F5 </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" >
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="../../images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Riepilogo Generale dei Totali delle quantità Componenti la Pena</font>
      </td>
      <td class="LBG">
        <a href="Javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <!-- ===================================================================== -->
  <!--                          SEZIONE DEI DATI                             -->  
  <!-- ===================================================================== -->
  <form method="POST" action="/jsp/Main.jsp" name="LoadInserisciPenaMauale">

<table cellspacing="2" cellpadding="2">
  <tr>
      <td></td>
      <td colspan="4" class="Titolo">Reclusione</td>
      <td colspan="4" class="Titolo">Arresto</td>
  </tr>
  <tr>
    <%
    //==========================================================================
    //    PENA INIZIALE
    //==========================================================================
    %>
<% if (isErgastolo) { %>
    <td class="int" width="200px">Pena In Esecuzione</td>
    <td class="l" colspan="4"><font class="label">ERGASTOLO</font></td>
    <td class="l" colspan="4"><font class="label">&nbsp;</font></td>
<% 
   }
   else
   {
%>
    <% 
    if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_SENTENZA) 
    {
      PenaComplessivaModel lPenaComplessiva = lCalcoloPenaMod.getPenaInSentenza();
      if (lPenaComplessiva==null) {lPenaComplessiva = new PenaComplessivaModel();}
    %>  
    <td class="int" width="200px">Pena Irrogata in Sentenza</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaComplessiva.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaComplessiva.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaComplessiva.getImportoAmmenda()          ) %></font></td>
    <%
    } 
    else if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO
             || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_CUMULO_NEW
            ) 
    {
      PenaResiduaModel lPenaInCumulo = lCalcoloPenaMod.getPenaIrrogataInCumulo();
      if (lPenaInCumulo==null) lPenaInCumulo = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Irrogata in Cumulo</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaInCumulo.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaInCumulo.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaInCumulo.getImportoAmmenda()          ) %></font></td>
    <%
    }
    else if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE
             || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES
            ) {
      PenaResiduaModel lPenaDopoSospensione = lCalcoloPenaMod.getPenaDopoSospensione();
      if (lPenaDopoSospensione==null) lPenaDopoSospensione = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Residua dopo Interruzione/Sospensione</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoSospensione.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoSospensione.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoSospensione.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA
             || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_CESSAZIONE_MA) 
    {
      PenaResiduaModel lPenaDopoRevocaMA = lCalcoloPenaMod.getPenaDopoRevocaMA();
      if (lPenaDopoRevocaMA==null) lPenaDopoRevocaMA = new PenaResiduaModel();
    %>
      <% if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA) { %>
      <td class="int" width="200px">Pena Residua dopo Revoca MA</td>
      <% } else { %>
      <td class="int" width="200px">Pena Residua dopo Cessazione MA</td>
      <% } %>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoRevocaMA.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaMA.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoRevocaMA.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO) {
      PenaResiduaModel lPenaDopoRevocaIndultino = lCalcoloPenaMod.getPenaDopoRevocaIndultino();
      if (lPenaDopoRevocaIndultino==null) lPenaDopoRevocaIndultino = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Residua dopo Revoca Indultino</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoRevocaIndultino.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDopoRevocaIndultino.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDopoRevocaIndultino.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE
             || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE_RES
            ) 
    {
      PenaResiduaModel lPenaResiduaManuale = lCalcoloPenaMod.getPenaResiduaManuale();
      if (lPenaResiduaManuale==null) lPenaResiduaManuale = new PenaResiduaModel();
      String lTitoloPenaManuale = "";
      if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE){
        lTitoloPenaManuale = "Pena Residua Manuale";
      }
      else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_MANUALE_RES){
        lTitoloPenaManuale = "Pena Residua da Forzatura RES";
      }
    %>
    <td class="int" width="200px"><%=lTitoloPenaManuale%></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaResiduaManuale.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaResiduaManuale.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaResiduaManuale.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_INDULTO) {
      PenaResiduaModel lPenaDaIndulto = lCalcoloPenaMod.getPenaDaIndulto();
      if (lPenaDaIndulto==null) lPenaDaIndulto = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Residua dopo Scarcerazione Indulto</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaIndulto.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaIndulto.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaIndulto.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_RES
             || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_ARCHIVIATA_SIEP
            ) 
    {
      PenaResiduaModel lPenaDaArchiviazione = lCalcoloPenaMod.getPenaDaArchiviazione();
      if (lPenaDaArchiviazione==null) lPenaDaArchiviazione = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Residua Da Archiviazione</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaArchiviazione.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaArchiviazione.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaArchiviazione.getImportoAmmenda()          ) %></font></td>
    <% 
    } 
    else if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_REVOCA_SS) {
      PenaResiduaModel lPenaDaRevocaSS = lCalcoloPenaMod.getPenaDaRevocaSS();
      if (lPenaDaRevocaSS==null) lPenaDaRevocaSS = new PenaResiduaModel();
    %>
    <td class="int" width="200px">Pena Residua dopo Revoca Sanzione Sostitutiva</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaRevocaSS.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaRevocaSS.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaRevocaSS.getImportoAmmenda()          ) %></font></td>
 <% } %>    
<% } %>    
</tr>

<% 
if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_IN_SENTENZA) 
{
  //==========================================================================
  //    BENEFICI CONCESSI IN SENTENZA
  //==========================================================================
  CalendarModel lBeneficiReclusioneInSentenza = lCalcoloPenaMod.getBeneficiReclusioneInSentenza("C");
  CalendarModel lBeneficiArrestiInSentenza    = lCalcoloPenaMod.getBeneficiArrestiInSentenza("C");
%>
<tr>
    <td class="int" width="200px">Benefici concessi in sentenza</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lBeneficiReclusioneInSentenza.getNumAnni()  %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lBeneficiReclusioneInSentenza.getNumMesi()  %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lBeneficiReclusioneInSentenza.getNumGiorni()%></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiReclusioneInSentenza.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lBeneficiArrestiInSentenza.getNumAnni()  %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lBeneficiArrestiInSentenza.getNumMesi()  %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lBeneficiArrestiInSentenza.getNumGiorni()%></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiArrestiInSentenza.getImportoAmmenda())) %></font></td>
</tr>
<%
  //==========================================================================
  //    MISURE CAUTELARI COMPUTABILI
  //==========================================================================
  /* CalendarModel lMCReclusioneInSentenza = lCalcoloPenaMod.getMCReclusioneInSentenza();
  CalendarModel lMCArrestiInSentenza    = lCalcoloPenaMod.getMCArrestiInSentenza();
  CalendarModel totMCComputabili = new CalendarModel();
  CalendarUtil lCalUtilCM = new CalendarUtil();
  totMCComputabili = lCalUtilCM.sommaGiorni(lMCReclusioneInSentenza,lMCArrestiInSentenza); */
  
  CalendarModel totMCComputabili = new CalendarModel();
  totMCComputabili = lCalcoloPenaMod.getMisureCautelariReclusioneInSentenza();
  
  String inCarcere = "Custodia Cautelare in Carcere: ";
  String inArrestiDomiciliari = "Custodia Cautelare in Arr. Domiciliari: ";
  
 /*  if (CalendarUtil.getTotGiorni(lMCReclusioneInSentenza)>0)
  {
    inCarcere = inCarcere + "Anni "+lMCReclusioneInSentenza.getNumAnni()+" ";
    inCarcere = inCarcere + "Mesi "+lMCReclusioneInSentenza.getNumMesi()+" ";
    inCarcere = inCarcere + "Giorni "+lMCReclusioneInSentenza.getNumGiorni()+" ";
  } */
  
 /*  if (CalendarUtil.getTotGiorni(lMCArrestiInSentenza)>0)
  {
    inArrestiDomiciliari = inArrestiDomiciliari + "Anni "+lMCArrestiInSentenza.getNumAnni()+" ";
    inArrestiDomiciliari = inArrestiDomiciliari + "Mesi "+lMCArrestiInSentenza.getNumMesi()+" ";
    inArrestiDomiciliari = inArrestiDomiciliari + "Giorni "+lMCArrestiInSentenza.getNumGiorni()+" ";
  } */
  
  //String descMC = inCarcere +" "+inArrestiDomiciliari;
  String descMC = " ";
%>
<tr>
    <td class="int" width="200px" title="<%=descMC%>">Totale Misure cautelari computabili</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= totMCComputabili.getNumAnni()  %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= totMCComputabili.getNumMesi()  %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= totMCComputabili.getNumGiorni()%></font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
</tr>
<% } %>
<tr height="2px"><td></td></tr>

<%
//==========================================================================
//    RIDETERMINAZIONE PENA PER BENEFICI CONCESSI
//==========================================================================
CalendarModel lBeneficiReclusione = lCalcoloPenaMod.getBeneficiReclusione("-");
CalendarModel lBeneficiArresti    = lCalcoloPenaMod.getBeneficiArresti("-");
%>
<tr>
    <td class="int" width="200px" title="Indulto,amnistia, depenalizzazione,incostituzionalità">Rideterminazione pena per Benefici Concessi</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lBeneficiReclusione.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lBeneficiReclusione.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lBeneficiReclusione.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiReclusione.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lBeneficiArresti.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lBeneficiArresti.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lBeneficiArresti.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiArresti.getImportoAmmenda())) %></font></td>
</tr>
<%
//==========================================================================
//    RIDETERMINAZIONE PENA PER BENEFICI REVOCATI
//==========================================================================
CalendarModel lBeneficiReclusioneR = lCalcoloPenaMod.getBeneficiReclusione("+");
CalendarModel lBeneficiArrestiR    = lCalcoloPenaMod.getBeneficiArresti("+");
// Visualizzo le revoche solo se presenti
CalendarUtil lCalUtil = new CalendarUtil();
if (   ! (lCalUtil.isZero(lBeneficiReclusioneR) && lCalUtil.isZero(lBeneficiArrestiR))
    || lBeneficiReclusioneR.getImportoMulta()>0
    || lBeneficiArrestiR.getImportoAmmenda()>0
   )
{
%>
<tr>
    <td class="int" width="200px" title="Indulto,amnistia, depenalizzazione,incostituzionalità">Rideterminazione pena per Benefici Revocati</td>
    <td class="l"><font class="label">Anni   </font><font color="red"> <%= lBeneficiReclusioneR.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font color="red"> <%= lBeneficiReclusioneR.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font color="red"> <%= lBeneficiReclusioneR.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font color="red"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiReclusioneR.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font color="red"> <%= lBeneficiArrestiR.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font color="red"> <%= lBeneficiArrestiR.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font color="red"> <%= lBeneficiArrestiR.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font color="red"> <%= StringUtils.toEuroFormat ( new BigDecimal(lBeneficiArrestiR.getImportoAmmenda())) %></font></td>
</tr>
<%
}
//==========================================================================
//    RIDETERMINAZIONE PENA PER COMPUTO/FUNGIBILITÀ
//==========================================================================
CalendarModel lComputiReclusione = lCalcoloPenaMod.getComputiReclusione("-");
CalendarModel lComputiArresti    = lCalcoloPenaMod.getComputiArresti("-");
%>
<tr>
    <td class="int" width="200px" title="Presofferto stesso reato, fungibilità altro reato">Rideterminazione pena per Computo/Fungibilità</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lComputiReclusione.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lComputiReclusione.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lComputiReclusione.getNumGiorni() %></font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lComputiArresti.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lComputiArresti.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lComputiArresti.getNumGiorni() %></font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
</tr>
<%
CalendarModel lComputiReclusioneRev = lCalcoloPenaMod.getComputiReclusione("+");
CalendarModel lComputiArrestiRev    = lCalcoloPenaMod.getComputiArresti("+");
%>
<tr>
    <td class="int" width="200px" title="Presofferto stesso reato, fungibilità altro reato">Rideterminazione pena per Computo/Fungibilità Revocati</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lComputiReclusioneRev.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lComputiReclusioneRev.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lComputiReclusioneRev.getNumGiorni() %></font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lComputiArrestiRev.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lComputiArrestiRev.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lComputiArrestiRev.getNumGiorni() %></font></td>
    <td class="l"><font class="label">&nbsp; </font></td>
</tr>
<%
//==========================================================================
//    RIDETERMINAZIONE PENA 'ALTRO'
//==========================================================================
CalendarModel lAltroReclusione = lCalcoloPenaMod.getAltroReclusione("-");
CalendarModel lAltroArresti    = lCalcoloPenaMod.getAltroArresti("-");
%>
<tr>
    <td class="int" width="200px" title="Correzione errore materiale, Rettifica provvedimento determinazione pene concorrenti, Rideterminazione della pena a seguito di conversione...">Rideterminazione pena 'Altro'</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lAltroReclusione.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lAltroReclusione.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lAltroReclusione.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lAltroReclusione.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lAltroArresti.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lAltroArresti.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lAltroArresti.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lAltroArresti.getImportoAmmenda())) %></font></td>
</tr>
<%
//==========================================================================
//    RIDETERMINAZIONE PENA 'ALTRO' (Revoche)
//==========================================================================
CalendarModel lAltroReclusioneR = lCalcoloPenaMod.getAltroReclusione("+");
CalendarModel lAltroArrestiR    = lCalcoloPenaMod.getAltroArresti("+");

// Visualizzo le revoche solo se presenti
CalendarUtil lCalUtil2 = new CalendarUtil();
if (   ! (lCalUtil2.isZero(lAltroReclusioneR) && lCalUtil2.isZero(lAltroArrestiR))
    || lAltroReclusioneR.getImportoMulta()>0
    || lAltroArrestiR.getImportoAmmenda()>0
   )
{
%>
<tr>
    <td class="int" width="200px" title="Correzione errore materiale, Rettifica provvedimento determinazione pene concorrenti, Rideterminazione della pena a seguito di conversione...">Rideterminazione pena 'Altro' (REVOCHE)</td>
    <td class="l"><font class="label">Anni   </font><font color="red"> <%= lAltroReclusioneR.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font color="red"> <%= lAltroReclusioneR.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font color="red"> <%= lAltroReclusioneR.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font color="red"> <%= StringUtils.toEuroFormat ( new BigDecimal(lAltroReclusioneR.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font color="red"> <%= lAltroArrestiR.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font color="red"> <%= lAltroArrestiR.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font color="red"> <%= lAltroArrestiR.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font color="red"> <%= StringUtils.toEuroFormat ( new BigDecimal(lAltroArrestiR.getImportoAmmenda())) %></font></td>
</tr>
<%
}
%>

<%
//==========================================================================
//    RIDETERMINAZIONE PENA 'RES' 
//==========================================================================
CalendarModel lRESReclusione = lCalcoloPenaMod.getComputoResReclusione("-");
CalendarModel lRESArresti    = lCalcoloPenaMod.getComputoResArresti("-");
CalendarUtil lCalUtilRES = new CalendarUtil();
if (   ! (lCalUtilRES.isZero(lRESReclusione) && lCalUtilRES.isZero(lRESArresti))
    || lRESReclusione.getImportoMulta()>0
    || lRESArresti.getImportoAmmenda()>0
   )
{   
%>
<tr>
    <td class="int" width="200px" title="Provvedimenti di Rideterminazione pena RES">Rideterminazione pena 'RES' (DETRAZIONI)</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lRESReclusione.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lRESReclusione.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lRESReclusione.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lRESReclusione.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lRESArresti.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lRESArresti.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lRESArresti.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lRESArresti.getImportoAmmenda())) %></font></td>
</tr>
<% } %>


<%
//==========================================================================
//    RIDETERMINAZIONE PENA 'RES' 
//==========================================================================
lRESReclusione = lCalcoloPenaMod.getComputoResReclusione("+");
lRESArresti    = lCalcoloPenaMod.getComputoResArresti("+");
if (   ! (lCalUtilRES.isZero(lRESReclusione) && lCalUtilRES.isZero(lRESArresti))
    || lRESReclusione.getImportoMulta()>0
    || lRESArresti.getImportoAmmenda()>0
   )
{   
%>
<tr>
    <td class="int" width="200px" title="Provvedimenti di Rideterminazione pena RES">Rideterminazione pena 'RES' (AUMENTI)</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lRESReclusione.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lRESReclusione.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lRESReclusione.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lRESReclusione.getImportoMulta())) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= lRESArresti.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= lRESArresti.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= lRESArresti.getNumGiorni() %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( new BigDecimal(lRESArresti.getImportoAmmenda())) %></font></td>
</tr>
<% } %>




<tr><td colspan="9"><hr></td></tr>


<%
//==============================================================================
//                          PENA DA ESPIARE (ricalcolata)
//==============================================================================
boolean isQuantumDifferenti = false;
PenaResiduaModel lPenaDaEspiare = new PenaResiduaModel();

if (!isErgastolo)
{
  Date lDataInizioPena = (Date)request.getAttribute("lDataInizioPena");
  Date lDataDiSistema = null;
  lPenaDaEspiare = lCalcoloPenaMod.getPenaDaEspiare(lDataInizioPena,lDataDiSistema,null);
%>
  <tr>
    <td class="int" width="200px">Pena da espiare </td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumAnniReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumMesiReclusione()  , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumGiorniReclusione(), "0") %></font></td>
    <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaEspiare.getImportoMulta()            ) %></font></td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumAnniArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumMesiArresto()     , "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lPenaDaEspiare.getNumGiorniArresto()   , "0") %></font></td>
    <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lPenaDaEspiare.getImportoAmmenda()          ) %></font></td>
  </tr>
  
<%
//==============================================================================
// In test! effettuo un confronto tra i quantum dell'ultima pena validata e 
// la pena calcolata
//==============================================================================
if (lUltimaPenResVal!=null && lUltimaPenResVal.getIdPenaResidua()!=null)
{
  CalendarModel lRecCalcolata = lPenaDaEspiare.getQuantumReclusione();
  CalendarModel lArrCalcolata = lPenaDaEspiare.getQuantumArresto();
  
  CalendarModel lRecUltimaVal = lUltimaPenResVal.getQuantumReclusione();
  CalendarModel lArrUltimaVal = lUltimaPenResVal.getQuantumArresto();

  if (   CalendarUtil.getTotGiorni(lRecCalcolata)!=CalendarUtil.getTotGiorni(lRecUltimaVal)
      || CalendarUtil.getTotGiorni(lArrCalcolata)!=CalendarUtil.getTotGiorni(lArrUltimaVal)
     )
  {
    // quantum differenti
    isQuantumDifferenti = true;
    %>
    <tr>
      <td colspan="9" class="l" style="text-align : center;"> 
      <font color="red">
        <p>Attenzione!! La pena residua rideterminata in base ai dati a sistema differisce dall'ultima pena validata.</p>
      </font>
      <!--p>Scegliere 'Conferma' per proseguire o 'Indietro' per modificare i dati.</p-->
      </td>
    </tr>
    <tr>
      <td class="int" width="200px">Ultima Pena Validata</td>
      <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumAnniReclusione()  , "0") %></font></td>
      <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumMesiReclusione()  , "0") %></font></td>
      <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumGiorniReclusione(), "0") %></font></td>
      <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lUltimaPenResVal.getImportoMulta()            ) %></font></td>
      <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumAnniArresto()     , "0") %></font></td>
      <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumMesiArresto()     , "0") %></font></td>
      <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumGiorniArresto()   , "0") %></font></td>
      <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lUltimaPenResVal.getImportoAmmenda()          ) %></font></td>
    </tr>
  <%}
}
} // end isErgastolo
%>
</table>




<%
//Date lDataSospensione = DateUtils.getDate(2006,5,6);  //yyyy,mm,dd
if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE
    || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE_RES
    || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_DA_INDULTO
   ) 
{
  Date lDataSospensione = lCalcoloPenaMod.getDataUltimaSospensione();
  if (lDataSospensione!=null)
  {
//    lCalcoloPenaMod.calcolaPenaDaSospensione(null,lDataSospensione);
//    PenaResiduaModel lPenaSosp = lCalcoloPenaMod.getPenaResiduaRicalcolata();
  %>
<table>
  <tr>
    <td class="l"><font class="label">Data Interruzione Pena : </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataSospensione,"dd-MM-yyyy"))%></font></td>
  </tr>
</table>
<%
  }
}
%>

<%
// Se revoca indultino o Affidamento in Prova inserisco la data 'Revoca Dal'
if (   lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_INDULTINO
    || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_REVOCA_MA
    || lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_CESSAZIONE_MA
   ) 
{
  Date lDataRevoca = lCalcoloPenaMod.getDataRevocataDal();
  if (lDataRevoca!=null)
  {
  %>
<table>
  <tr>
    <td class="l"><font class="label">Misura Revoca/Cessata Dal: </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataRevoca,"dd-MM-yyyy"))%></font></td>
  </tr>
</table>
<%
  }
}
%>

<%
//==============================================================================
//                            DECORRENZA PENA
//==============================================================================
if (lPenaDaEspiare.getDataInizio()!=null) 
{
%>
<br>
<table>
  <tr>
    <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataInizio(),"dd-MM-yyyy"))%></font></td>
    <% if (lPenaDaEspiare.getDataFineReclusione()!=null){%>
    <td class="l"><font class="label">Data Fine Reclusione : </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataFineReclusione(),"dd-MM-yyyy"))%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Data Inizio Arresto : </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataInizioArresto(),"dd-MM-yyyy"))%></font></td>
    <%}%>
    <td class="l"><font class="label">Data Fine Pena: </font></td>
    <% if (!isErgastolo){ %>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataFine(),"dd-MM-yyyy"))%></font></td>
    <% } else { %>
    <td class="l"><font class="campo">MAI</font></td>
    <% } %>
  </tr>
</table>
<%
}
%>

<%
//==========================================================================
//    LIBERAZIONE ANTICIPATA
//==========================================================================
%>
<%-- table cellspacing="2" cellpadding="2" border="0">
  <tr>
    <% if (lPenaDaEspiare.getDataInizio()!=null) {%>
    <td class="l"><font class="label">Liberazione Anticipata concessa già detratta in giorni</font></td>
    <%} else {%>
    <td class="l"><font class="label">Liberazione Anticipata concessa da detrarre in giorni</font></td>
    <%}%>
    <td class="l"><font class="campo"><%=lCalcoloPenaMod.getLiberazioneAnticipata()%></font></td>
  </tr>
</table --%>
   <!--   Nuova L.A. - DL 146/2013  GESTIONE L.A.,  L.A. SPECIALE, INTEGRAZIONE L.A.   -->  
<%  
  int la_giaconcesse= lCalcoloPenaMod.getLiberazioneAnticipataGiaConcesse();
  if (la_giaconcesse!=0)  
  {
    int LA_conc= lCalcoloPenaMod.getLiberazioneAnticipataGiaConcesseLA();
    int LS_conc= lCalcoloPenaMod.getLiberazioneAnticipataGiaConcesseLS();
    int LI_conc= lCalcoloPenaMod.getLiberazioneAnticipataGiaConcesseLI();
%>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="l"><font class="label">Liberazione Anticipata concessa già detratta in giorni</font></td>
        <td class="l"><font class="campo"><%=la_giaconcesse%></font></td>
        
<%    if(LS_conc != 0 || LI_conc != 0) { %>    
        <td class="l"><font class="label"> Di Cui : </font></td>
<%    } %>        
      </tr>
<%    if(LS_conc != 0 || LI_conc != 0)
    {
      if(LA_conc != 0)
      { %>      
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <td class="l" ><font class="campo"><%=LA_conc%></font></td>
            <td class="l" ><font class="label"> di Liberazione Anticipata </font></td>
          </tr>     
     <% } 
  
      if(LS_conc != 0)
      { %> 
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <td class="l" ><font class="campo"><%=LS_conc%></font></td>
            <td class="l" ><font class="label"> di Liberazione Anticipata Speciale</font></td>
          </tr>         
     <% }
    
      if(LI_conc != 0)
      { %> 
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <td class="l"><font class="campo"><%=LI_conc%></font></td>
            <td class="l"><font class="label"> di Integrazione Liberazione Anticipata</font></td>
          </tr>
  <%    }
    } %>
    </table>    
<%  }
  
    int la_daconcedere=lCalcoloPenaMod.getLiberazioneAnticipataDaConcedere();
  if (la_daconcedere!=0) 
  { 
      int LA_daconc = lCalcoloPenaMod.getLiberazioneAnticipataDaConcedereLA();
      int LS_daconc = lCalcoloPenaMod.getLiberazioneAnticipataDaConcedereLS();
      int LI_daconc = lCalcoloPenaMod.getLiberazioneAnticipataDaConcedereLI();
  %>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="l"><font class="label">Liberazione Anticipata concessa da detrarre in giorni</font></td>
        <% if (la_daconcedere>0) { %>
        <td class="l"><font class="campo"><%=la_daconcedere%></font></td>
        <% } else { %>
        <td class="l"><font color="red"><%=la_daconcedere%></font></td>
        <% } %>

<%    if(LS_daconc != 0 || LI_daconc != 0)
    { %>    
        <td class="l"><font class="label"> Di Cui : </font></td>
<%    }  %>       
      </tr>
      
<%    if(LS_daconc != 0 || LI_daconc != 0)
    {
      if(LA_daconc != 0)
      { %>      
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <% if (LA_daconc>0) { %>
            <td class="l" ><font class="campo"><%=LA_daconc%></font></td>
            <% } else { %>
            <td class="l" ><font color="red"><%=LA_daconc%></font></td>
            <% } %>
            <td class="l" ><font class="label"> di Liberazione Anticipata </font></td>
          </tr>     
     <% } 
    
      if(LS_daconc != 0)
      { %>      
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <% if (LS_daconc>0) { %>
            <td class="l" ><font class="campo"><%=LS_daconc%></font></td>
            <% } else { %>
            <td class="l" ><font color="red"><%=LS_daconc%></font></td>
            <% } %>
            <td class="l" ><font class="label"> di Liberazione Anticipata Speciale</font></td>
          </tr>     
     <% }
      
      if(LI_daconc != 0)
      { %>      
          <tr>
            <td class="l" style="text-align:right"><font class="label"> giorni&nbsp;&nbsp;&nbsp;</font></td>
            <% if (LI_daconc>0) { %>
            <td class="l" ><font class="campo"><%=LI_daconc%></font></td>
            <% } else { %>
            <td class="l" ><font color="red"><%=LI_daconc%></font></td>
            <% } %>
            <td class="l" ><font class="label"> di Integrazione Liberazione Anticipata </font></td>
          </tr>     
<%      }
    } %>
    </table>
     
 <%  } %>
<!--  END   DL  146/2013     -->

<table cellspacing="2" cellpadding="2">
<%  
  int s_giaconcessi= lCalcoloPenaMod.getScomputiGiaConcessi();
  if (s_giaconcessi!=0)  
  {
%>
  <tr>
    <td class="l"><font class="label">Revoca permesso giorni</font></td>
    <td class="l"><font class="campo"><%=Math.abs(s_giaconcessi)%></font></td>
    <td class="l"><font class="label">già scomputati</font></td>
  </tr>
<%
  }
  int s_daconcedere=lCalcoloPenaMod.getScomputiDaConcedere();
  if (s_daconcedere!=0) {
%>
  <tr>
    <td class="l"><font class="label">Revoca permesso giorni</font></td>
    <td class="l"><font class="campo"><%=Math.abs(s_daconcedere)%></font></td>
    <td class="l"><font class="label">da scomputare</font></td>
  </tr>
<%   } %>
</table>


<%
//==============================================================================
// Aggiungere Visualizzazione Giorni di riduzione concessi DL92/2014
//==============================================================================
int DL92_DaDetrarre = lCalcoloPenaMod.getRimediRisarcitoriDaConcedere();
int DL92_Detratta   = lCalcoloPenaMod.getRimediRisarcitoriGiaConcessi();
%>

<% if (DL92_DaDetrarre>0 || DL92_Detratta>0) { %>
<br>
<table cellspacing="2" cellpadding="2">
  <% if (DL92_DaDetrarre>0 ) { %>
  <tr>
    <td class="l"><font class="label">Riduzione pena per risarcimento danni D.L. 92/2014 concessa da detrarre in giorni: </font></td>
    <td class="l"><font class="campo"><%=DL92_DaDetrarre%></font></td>
  </tr>
  <% } %>
  <% if (DL92_Detratta>0 ) { %>
  <tr>
    <td class="l"><font class="label">Riduzione pena per risarcimento danni D.L. 92/2014 concessa già detratta in giorni: </font></td>
    <td class="l"><font class="campo"><%=DL92_Detratta%></font></td>
  </tr>
  <% } %>
</table>
<% } %>

<%
//==============================================================================
// In test! effettuo un confronto tra le date di decorrenza dell'ultima pena 
// validata e la pena calcolata
//==============================================================================
boolean isErrDataInizio         = false;
boolean isErrDataFineReclusione = false;
boolean isErrDataInizioArresto  = false;
boolean isErrDataFine           = false;
if (lUltimaPenResVal!=null && lUltimaPenResVal.getIdPenaResidua()!=null && !isErgastolo)
{
 
  // Confronto le date
  if (   (lPenaDaEspiare.getDataInizio()!=null && lUltimaPenResVal.getDataInizio()==null)
      || (lPenaDaEspiare.getDataInizio()==null && lUltimaPenResVal.getDataInizio()!=null)
      || (lPenaDaEspiare.getDataInizio()!=null && lUltimaPenResVal.getDataInizio()!=null
          && !DateUtils.isEquals(lPenaDaEspiare.getDataInizio(), lUltimaPenResVal.getDataInizio())  
         )
     )
  {
    // date inizio differenti
    isErrDataInizio = true;
  }

  if (   (lPenaDaEspiare.getDataFineReclusione()!=null && lUltimaPenResVal.getDataFineReclusione()==null)
      || (lPenaDaEspiare.getDataFineReclusione()==null && lUltimaPenResVal.getDataFineReclusione()!=null)
      || (lPenaDaEspiare.getDataFineReclusione()!=null && lUltimaPenResVal.getDataFineReclusione()!=null
          && !DateUtils.isEquals(lPenaDaEspiare.getDataFineReclusione(), lUltimaPenResVal.getDataFineReclusione())  
         )
     )
  {
    // date fine reclusione differenti
    isErrDataFineReclusione = true;
  }

  if (   (lPenaDaEspiare.getDataInizioArresto()!=null && lUltimaPenResVal.getDataInizioArresto()==null)
      || (lPenaDaEspiare.getDataInizioArresto()==null && lUltimaPenResVal.getDataInizioArresto()!=null)
      || (lPenaDaEspiare.getDataInizioArresto()!=null && lUltimaPenResVal.getDataInizioArresto()!=null
          && !DateUtils.isEquals(lPenaDaEspiare.getDataInizioArresto(), lUltimaPenResVal.getDataInizioArresto())  
         )
     )
  {
    // date inizio arresto differenti
    isErrDataInizioArresto = true;
  }

  if (   (lPenaDaEspiare.getDataFine()!=null && lUltimaPenResVal.getDataFine()==null)
      || (lPenaDaEspiare.getDataFine()==null && lUltimaPenResVal.getDataFine()!=null)
      || (lPenaDaEspiare.getDataFine()!=null && lUltimaPenResVal.getDataFine()!=null
          && !DateUtils.isEquals(lPenaDaEspiare.getDataFine(), lUltimaPenResVal.getDataFine())  
         )
     )
  {
    // date fine differenti
    isErrDataFine = true;
  }

  if (isErrDataInizio || isErrDataFineReclusione || isErrDataInizioArresto || isErrDataFine)
  {
  %>
  <table>
    <tr>
      <td colspan="4" class="l" style="text-align : center;" > 
      <font color="red"><p>Attenzione!! Le date di decorrenza dell'ultima pena residua rideterminata in base<br> 
         ai dati a sistema differiscono dalle date di decorrenza dell'ultima pena validata.</p></font>
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
      <td class="l">
      <% if (isErrDataInizio) {%> <font color="red"> 
      <% } else {%> <font class="campo"> <%}%>
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataInizio(),"dd-MM-yyyy"))%></font></td>
      <% if (lPenaDaEspiare.getDataFineReclusione()!=null){ %>
      <td class="l"><font class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <% if (isErrDataFineReclusione) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <% if (isErrDataInizioArresto) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataInizioArresto(),"dd-MM-yyyy"))%></font></td>
      <%}%>
      <td class="l"><font class="label">Data Fine Pena: </font></td>
      <td class="l">
        <% if (isErrDataFine) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataFine(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
  </table>
  <%
  }
}
%>  


<%
//==============================================================================
// Se esistono delle discrepanza tra la Pena a sistema e la pena calcolata
// fornisco la possibilità all'utente di caricare i dati come Pena Residua
// Manuale.
// La differenza potrebbe essere dovuta a dati migrati insufficienti a sistema 
// per la nuova procedura di calcolo che non riesce a caricarli
//==============================================================================
boolean caricaPenaManualeEnabled = false;
if (lUltimaPenResVal!=null && lUltimaPenResVal.getIdPenaResidua()!=null && !isErgastolo && caricaPenaManualeEnabled)
{
  if (isQuantumDifferenti || isErrDataInizio || isErrDataFineReclusione || isErrDataInizioArresto || isErrDataFine)
  {
    CalendarModel lRecUltimaVal = lUltimaPenResVal.getQuantumReclusione();
    CalendarModel lArrUltimaVal = lUltimaPenResVal.getQuantumArresto();
  %>
  <FORM method="POST" name="PenaResiduaManuale" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penaresidua.action.ActLoadInserisciPenaResiduaManuale">
    <input type="HIDDEN" name="FromF5" value="S">
    
    <!-- Campi da precaricare nella form Di pena residua manuale -->
    <!-- Reclusione -->
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_RECLUSIONE%>"    value="<%=lRecUltimaVal.getNumAnni()%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_RECLUSIONE%>"    value="<%=lRecUltimaVal.getNumMesi()%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_RECLUSIONE%>"  value="<%=lRecUltimaVal.getNumGiorni()%>">
    <!-- Multa -->
    <% if (lUltimaPenResVal.getImportoMulta() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_MULTA%>"   value="<%=StringUtils.getParteIntera(lUltimaPenResVal.getImportoMulta())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_MULTA%>" value="<%=StringUtils.getParteDecimale(lUltimaPenResVal.getImportoMulta())%>">
    <% } %>  
    
    <!-- Arresti -->
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_ANNI_ARRESTO%>"   value="<%=lArrUltimaVal.getNumAnni()%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_MESI_ARRESTO%>"   value="<%=lArrUltimaVal.getNumMesi()%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_NUM_GIORNI_ARRESTO%>" value="<%=lArrUltimaVal.getNumGiorni()%>">

    <!-- Ammenda -->
    <% if (lUltimaPenResVal.getImportoAmmenda() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_INTERO_IMPORTO_AMMENDA%>"   value="<%=StringUtils.getParteIntera   (lUltimaPenResVal.getImportoAmmenda())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_DECIMALE_IMPORTO_AMMENDA%>" value="<%=StringUtils.getParteDecimale (lUltimaPenResVal.getImportoAmmenda())%>">
    <% } %>     

    <!-- Liberazione Anticipata-->
    <input type="HIDDEN" name="LibAntGiorni" value="<%=lCalcoloPenaMod.getLiberazioneAnticipataGiaConcesse()%>">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--input type="HIDDEN" name="LibAntGiorni" value="<%=lCalcoloPenaMod.getLiberazioneAnticipataDaConcedere()%>"--%>

    <!-- Decorrenza pena -->
    <% if (lUltimaPenResVal.getDataInizio() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>" value="<%=DateUtils.getDayToString   (lUltimaPenResVal.getDataInizio())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>"   value="<%=DateUtils.getMonthToString (lUltimaPenResVal.getDataInizio())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>"   value="<%=DateUtils.getYearToString  (lUltimaPenResVal.getDataInizio())%>">
    <%} else {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>" value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_DECORRENZA_PENA%>"   value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_DECORRENZA_PENA%>"   value="">
    <%}%>

    <% if (lUltimaPenResVal.getDataFineReclusione() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>" value="<%=DateUtils.getDayToString   (lUltimaPenResVal.getDataFineReclusione())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>"   value="<%=DateUtils.getMonthToString (lUltimaPenResVal.getDataFineReclusione())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>"   value="<%=DateUtils.getYearToString  (lUltimaPenResVal.getDataFineReclusione())%>">
    <%} else {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE_RECLUSIONE%>" value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE_RECLUSIONE%>"   value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE_RECLUSIONE%>"   value="">
    <%}%>

    <% if (lUltimaPenResVal.getDataInizioArresto() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>" value="<%=DateUtils.getDayToString   (lUltimaPenResVal.getDataInizioArresto())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>"   value="<%=DateUtils.getMonthToString (lUltimaPenResVal.getDataInizioArresto())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>"   value="<%=DateUtils.getYearToString  (lUltimaPenResVal.getDataInizioArresto())%>">
    <%} else {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO_ARRESTO%>" value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO_ARRESTO%>"   value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO_ARRESTO%>"   value="">
    <%}%>

    <% if (lUltimaPenResVal.getDataFine() != null) {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" value="<%=DateUtils.getDayToString   (lUltimaPenResVal.getDataFine())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"   value="<%=DateUtils.getMonthToString (lUltimaPenResVal.getDataFine())%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"   value="<%=DateUtils.getYearToString  (lUltimaPenResVal.getDataFine())%>">
    <%} else {%>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"   value="">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"   value="">
    <%}%>

    
    <table>
      <tr>
        <td class="l"colspan=4>
          <font color="red">
            <p>Attenzione!! La pena residua rideterminata in base ai dati a sistema differisce dall'ultima pena validata.</p>
            <p>Scegliere 'Conferma' per proseguire o 'Indietro' per modificare i dati.</p>
          </font>
        <td>
      </tr>
      <tr>
        <td class="l"colspan=4>
          <INPUT class="bottone" type="submit" name="conferma" value="Pena Residua Manuale">
        </td>
      </tr>
    </table>
  </form>

  <%  
  }    
}
%>

<%
//==========================================================================
//    PENA VIRTUALE
//==========================================================================

if (   lPenaDaEspiare.getDataInizio()==null
    && lCalcoloPenaMod.getLiberazioneAnticipata()!=0
    && !isErgastolo
   ) 
{
  CalendarModel lPenaVirtuale = lCalcoloPenaMod.getPenaVirtuale(null);
%>
  <br>
  <table cellspacing=0 cellpadding=0 >
    <tr>
       <td class="Titolo" colspan=3>Pena Virtuale</td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumAnni()),"0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumMesi()),"0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lPenaVirtuale.getNumGiorni()),"0")%></font>&nbsp;&nbsp;
        <font class="label"> (detratti <font class="campo"><%=lCalcoloPenaMod.getLiberazioneAnticipata()%></font> giorni di LA)</font>
      </td>
    </tr>
  </table>
<%
}
%>


<%
//==============================================================================
//                                PENA ESPIATA
//==============================================================================
CalendarModel lPenaEspiata = null;
//if (lCalcoloPenaMod.getTipoPenaIniziale() == ICostantiCalcoloPena.PENA_SOSPENSIONE) 
//{
//  lPenaEspiata = lCalcoloPenaMod.getPenaEspiata();
//}  
//else
//{
  lPenaEspiata = lCalcoloPenaMod.getQuantumPenaGiaEspiata();
//}
%>
<br>
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="l" width="200px">Pena Espiata</td>
    <td class="l"><font class="label">Anni  </font><font class="campo"> <%= lPenaEspiata.getNumAnni()   %></font></td>
    <td class="l"><font class="label">Mesi  </font><font class="campo"> <%= lPenaEspiata.getNumMesi()   %></font></td>
    <td class="l"><font class="label">Giorni</font><font class="campo"> <%= lPenaEspiata.getNumGiorni() %></font></td>
    <% if (lCalcoloPenaMod.getLAsuPenaEspiata()>0) { %>
    <td class="l"><font class="label"> (di cui <%=lCalcoloPenaMod.getLAsuPenaEspiata()%> giorni di LA)</font></td>
    <% } %>
  </tr>
</table>


<%
//==============================================================================
//                          PENA ESPIATA IN ECCESSO
// Visualizzata solo se presente
//==============================================================================
CalendarModel lQuantumFungibilita = new CalendarModel();
/*
if ( lCalcoloPenaMod.getFungibilitaCalcolata()!=null ) 
{
//  CalendarModel lQuantumFungibilita = lCalcoloPenaMod.getFungibilitaCalcolata().getQuantumFungibilita();
  lQuantumFungibilita = lCalcoloPenaMod.getFungibilitaCalcolata().getQuantumFungibilita();
}
*/

if ( lCalcoloPenaMod.getPeneEspiateInEccesso()!=null && lCalcoloPenaMod.getPeneEspiateInEccesso().size()>0) 
{
  FungibilitaModel lFungibilitaModel = (FungibilitaModel) lCalcoloPenaMod.getPeneEspiateInEccesso().elementAt(0);
  lQuantumFungibilita = lFungibilitaModel.getQuantumFungibilita();
}


CalendarUtil lCalUtil3 = new CalendarUtil();
if ( !lCalUtil3.isZero(lQuantumFungibilita) )
{
%>
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="l" width="200px">Pena espiata in eccesso</td>
    <td class="l"><font class="label">Anni   </font><font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumAnni()  ), "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumMesi()  ), "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font color="red"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumGiorni()), "0") %></font></td>
  </tr>
</table>
<% 
} 
else 
{
%>
<table cellspacing="2" cellpadding="2">
  <tr>
    <td class="l" width="200px">Pena Espiata in eccesso</td>
    <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumAnni()  ), "0") %></font></td>
    <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumMesi()  ), "0") %></font></td>
    <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( new BigDecimal (lQuantumFungibilita.getNumGiorni()), "0") %></font></td>
  </tr>
</table>
<% } %>
</form>

 
</body>
</html>