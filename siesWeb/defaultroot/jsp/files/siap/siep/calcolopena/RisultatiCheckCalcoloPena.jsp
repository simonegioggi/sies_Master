<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.calcolopena.model.CheckCalcoloPenaModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.calcolopena.model.CalcoloPenaModel" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="ListaCheckCalcoloPenaModel" scope="request" class="java.util.Vector" />

<%
//==============================================================================
// Jsp per la visualizzazione del dettaglio della pena in un certo istante
// La jsp visualizza:
//==============================================================================
%>

<%
int totErrFascicoli = 0;
int totErrCheck = 0;
int totErrQuantumDate = 0;
int totErrSoloQuantum = 0;
int totErrSoloDate = 0;

//==============================================================================
// Effettuo un precontrollo in modo da poter stampare in testa alla pagina un 
// riassunto dei risultati
//==============================================================================
Vector lFascicoliConErrore = new Vector();
for (int i=0; i<ListaCheckCalcoloPenaModel.size(); i++)
{
  CheckCalcoloPenaModel lCheckModel = (CheckCalcoloPenaModel) ListaCheckCalcoloPenaModel.elementAt(i);
  
  lCheckModel.confrontaPene();
  
  if (lCheckModel.isErrCheck())
  {
    totErrCheck++;
    FascicoloSiepModel lFascicolo = lCheckModel.getFascicoloSiep();
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //siesLogger.error("Errore Fascicolo: "+lFascicolo.getChiaveAnno()+"/"+lFascicolo.getChiaveProgr());
    lFascicoliConErrore.add(lFascicolo);
  }
  else{
    if (lCheckModel.isErrQuantum() || lCheckModel.isErrDate())
    {
      totErrFascicoli++;
    }
    if (lCheckModel.isErrQuantum() && lCheckModel.isErrDate())
    {
      totErrQuantumDate++;
    }
    if (lCheckModel.isErrQuantum() && !lCheckModel.isErrDate())
    {
      totErrSoloQuantum++;
    }
    if (!lCheckModel.isErrQuantum() && lCheckModel.isErrDate())
    {
      totErrSoloDate++;
    }
  }
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
        <font class="campo">Riepilogo Generale dei Controlli sul Calcolo della Pena</font>
      </td>
    </tr>
  </table>
  
<!-- ===================================================================== -->
<!--                          SEZIONE DEI DATI                             -->  
<!-- ===================================================================== -->
<form method="POST" action="/jsp/Main.jsp" name="LoadInserisciPenaMauale">

<table style="border: 0;">
  <tr>
    <td></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Tot Fascicoli Controllati</font></td>
    <td class="l"><font class="campo"><%=ListaCheckCalcoloPenaModel.size()%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Fascicoli con errori</font></td>
    <td class="l"><font class="campo"><%=totErrCheck%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Fascicoli con discrepanze</font></td>
    <td class="l"><font class="campo"><%=totErrFascicoli%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Fascicoli con discrepanze Quantum e Date</font></td>
    <td class="l"><font class="campo"><%=totErrQuantumDate%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Fascicoli con discrepanze solo Quantum</font></td>
    <td class="l"><font class="campo"><%=totErrSoloQuantum%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Fascicoli con discrepanze solo Date</font></td>
    <td class="l"><font class="campo"><%=totErrSoloDate%></font></td>
  </tr>
</table>

<%
//==============================================================================
// Sezione con i fascicoli per i quanli si è verificato un errore
//==============================================================================
if (lFascicoliConErrore.size()>0)
{
%>
<table style="border: 0;">
  <tr>
    <td class="l"><font class="label">Fascicoli con errori</font></td>
  </tr>
<% 
for (int i=0; i<lFascicoliConErrore.size();i++)
{
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)lFascicoliConErrore.elementAt(i);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.error("Errore Fascicolo: "+lFascicolo.getIdFascicoloSiep()+" - "+lFascicolo.getChiaveAnno()+"/"+lFascicolo.getChiaveProgr());
%>  
  <tr>
    <td class="l"><font class="label">Fascicolo N. <%=lFascicolo.getChiaveAnno()%>/<%=lFascicolo.getChiaveProgr()%></font></td>
  </tr>
<%}%>
</table>  
<%
}
%>
  
  
  

<table style="border: 0;">
<%
//==============================================================================
//                          PENA DA ESPIARE (ricalcolata)
//==============================================================================
int totErroriFascicoli = 0;
for (int i=0; i<ListaCheckCalcoloPenaModel.size(); i++)
{
  //if (i==10) break;

  CheckCalcoloPenaModel lCheckModel = (CheckCalcoloPenaModel) ListaCheckCalcoloPenaModel.elementAt(i);
  
  if (   lCheckModel.isErrCheck() 
      || !(lCheckModel.isErrQuantum() || lCheckModel.isErrDate())
     )
  {
    continue;
  }
  
  FascicoloSiepModel lFascicolo       = lCheckModel.getFascicoloSiep();
  PenaResiduaModel   lUltimaPenResVal = lCheckModel.getUltimaPenaValidata();
  CalcoloPenaModel   lCalcoloPenaMod  = lCheckModel.getCalcoloPenaModel();
  
  Date lDataInizioPena = lUltimaPenResVal.getDataInizio();

  //============================================================================
  // Calcolo la pena residua 
  //============================================================================
  //PenaResiduaModel lPenaDaEspiare = lCalcoloPenaMod.getPenaDaEspiare(lDataInizioPena,null);
  
  PenaResiduaModel lPenaDaEspiare = lCheckModel.getPenaRicalcolata();
  
%>
<!-- 
    NUOVO FASCICOLO
-->
<tr>
  <td width="100%">
  
<table cellspacing="2" cellpadding="2" style="border: 0" width="100%">
  <tr>
    <td colspan="100%">
      <font class="label">Procedimento : N.</font>
      <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicolo.getIdFascicoloSiep()%>" title="Procedimento">
        <%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno())%>
        /
        <%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr())%>
      </a>&nbsp;
      <% if(lFascicolo.getFlagCumulante() != null && lFascicolo.getFlagCumulante().equals("S")) { %>
        <font class="cRossoCumulo"> &nbsp;C&nbsp; </font>&nbsp;
      <% } %>
      <% if(lFascicolo.getCodOperatoreInserimento() != null && lFascicolo.getCodOperatoreInserimento().startsWith("res-")) { %>
        <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font>&nbsp;
      <% } %>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--font class="cRossoCumulo"> &nbsp;<%=lCalcoloPenaMod.getTipoPenaIniziale()%>&nbsp; </font--%>&nbsp;
    </td>
  </tr>


  <tr>
    <td></td>
    <td colspan="4" class="Titolo">Reclusione</td>
    <td colspan="4" class="Titolo">Arresto</td>
  </tr>
  <tr>
    <td class="int" width="150px">Pena da espiare Calcolata</td>
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


  if (lCheckModel.isErrQuantum() )
  {
    // quantum differenti
    %>
    <tr>
      <td colspan="9" width="100%" class="l" style="text-align : center;"> 
        <font color="red">
          <p>Attenzione!! La pena residua rideterminata in base ai dati a sistema differisce dall'ultima pena validata.</p>
        </font>
      </td>
    </tr>
    <tr>
      <td class="int" width="150px">Ultima Pena Validata</td>
      <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumAnniReclusione()  , "0") %></font></td>
      <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumMesiReclusione()  , "0") %></font></td>
      <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumGiorniReclusione(), "0") %></font></td>
      <td class="l"><font class="label">Multa  </font><font class="campo"> <%= StringUtils.toEuroFormat ( lUltimaPenResVal.getImportoMulta()            ) %></font></td>
      <td class="l"><font class="label">Anni   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumAnniArresto()     , "0") %></font></td>
      <td class="l"><font class="label">Mesi   </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumMesiArresto()     , "0") %></font></td>
      <td class="l"><font class="label">Giorni </font><font class="campo"> <%= StringUtils.toStringJSP  ( lUltimaPenResVal.getNumGiorniArresto()   , "0") %></font></td>
      <td class="l"><font class="label">Ammenda</font><font class="campo"> <%= StringUtils.toEuroFormat ( lUltimaPenResVal.getImportoAmmenda()          ) %></font></td>
    </tr>
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
  <tr>
    <td colspan="9">
<table cellspacing="2" cellpadding="2" style="border: 0;" width="100%">
  <tr>
    <td class="l"><font class="label">Decorrenza Pena : </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataInizio(),"dd-MM-yyyy"))%></font></td>
    <td class="l"><font class="label">Fine Reclusione : </font></td>
    <% if (lPenaDaEspiare.getDataFineReclusione()!=null){%>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataFineReclusione(),"dd-MM-yyyy"))%></font></td>
    <% } else {%>
    <td class="l"><font class="campo">__-__-____</font></td>
    <% } %>
    <td class="l"><font class="label">Inizio Arresto : </font></td>
    <% if (lPenaDaEspiare.getDataInizioArresto()!=null){%>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataInizioArresto(),"dd-MM-yyyy"))%></font></td>
    <% } else {%>
    <td class="l"><font class="campo">__-__-____</font></td>
    <% } %>
    <td class="l"><font class="label">Fine Pena: </font></td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDaEspiare.getDataFine(),"dd-MM-yyyy"))%></font></td>
  </tr>
  <%
  //==========================================================================
  //    LIBERAZIONE ANTICIPATA
  //==========================================================================
  %>
  <tr>
    <% if (lPenaDaEspiare.getDataInizio()!=null) {%>
    <td class="l" colspan="100%">
      <font class="label">Liberazione Anticipata concessa già detratta in giorni: </font><font class="campo"><%=lCalcoloPenaMod.getLiberazioneAnticipata()%></font>
    </td>
    <%}else{%>
    <td class="l" colspan="100%">
      <font class="label">Liberazione Anticipata concessa da detrarre in giorni: </font><font class="campo"><%=lCalcoloPenaMod.getLiberazioneAnticipata()%></font>
    </td>
    <%}%>
  </tr>

<%
//==============================================================================
// In test! effettuo un confronto tra le date di decorrenza dell'ultima pena 
// validata e la pena calcolata
//==============================================================================
if (lCheckModel.isErrDate())
{
  boolean isErrDataInizio         = lCheckModel.isErrDataInizio();
  boolean isErrDataFineReclusione = lCheckModel.isErrDataFineReclusione();
  boolean isErrDataInizioArresto  = lCheckModel.isErrDataInizioArresto();
  boolean isErrDataFine           = lCheckModel.isErrDataFine();
  
%>
    <tr>
      <td colspan="100%" class="l" style="text-align : center;" > 
      <font color="red"><p>Attenzione!! Le date di decorrenza dell'ultima pena residua rideterminata in base<br> 
         ai dati a sistema differiscono dalle date di decorrenza dell'ultima pena validata.</p></font>
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Decorrenza Pena : </font></td>
      <td class="l">
      <% if (isErrDataInizio) {%> <font color="red"> 
      <% } else {%> <font class="campo"> <%}%>
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataInizio(),"dd-MM-yyyy"))%></font></td>
      <td class="l"><font class="label">Fine Reclusione : </font></td>
      <td class="l">
        <% if (isErrDataFineReclusione) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <% if (lUltimaPenResVal.getDataFineReclusione()!=null){ %>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataFineReclusione(),"dd-MM-yyyy"))%>
        <% } else {%>
        __-__-____
        <% } %>
        </font>
      </td>
      <td class="l"><font class="label">Inizio Arresto : </font></td>
      <td class="l">
        <% if (isErrDataInizioArresto) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <% if (lUltimaPenResVal.getDataInizioArresto()!=null){ %>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataInizioArresto(),"dd-MM-yyyy"))%></font>
        <% } else {%>
        __-__-____
        <% } %>
        </td>
      <td class="l"><font class="label">Fine Pena: </font></td>
      <td class="l">
        <% if (isErrDataFine) {%> <font color="red"> 
        <% } else {%> <font class="campo"> <%}%>
        <% if (lUltimaPenResVal.getDataFine()!=null){ %>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lUltimaPenResVal.getDataFine(),"dd-MM-yyyy"))%></font>
        <% } else {%>
        __-__-____
        <% } %>
      </td>
    </tr>
  <%
}%>
</table>
  </td>
</tr>
<%
}  // end check Date

%>

</table>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
<%} // end for sui fascicoli %>



</table> 


</form>
  
</body>
</html>