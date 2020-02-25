<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.fungibilita.action.ICostantiFungibilita" %>

<%
// Informazioni riportate nella prima sezione della form: pena di partenza + quantum aggregati
%>
<jsp:useBean id="PenaResidua"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="Fungibilita"       scope="request" class="siap.siep.fungibilita.model.FungibilitaModel" />
<jsp:useBean id="PenaGiaEspiata"    scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="UltimaPenaResidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<jsp:useBean id="TotaleGiorniLibAnt"     scope="request" class="java.lang.String" />
<jsp:useBean id="lEveIdEventoOrdinanza"  scope="request" class="java.lang.String" />


<%
  //Date dataSistemaPerCalcoli = DateUtils.getSysDate();
  
  Date dataSistemaPerCalcoli = null;
  Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");

  if (dataScarcerazione==null) {
     dataSistemaPerCalcoli = DateUtils.getSysDate();
  }
  else{
     dataSistemaPerCalcoli = dataScarcerazione;
  }   
  
  Date DataInizioPena     = PenaResidua.getDataInizio();
  Date DataFineReclusione = PenaResidua.getDataFineReclusione();
  Date DataInizioArresto  = PenaResidua.getDataInizioArresto();
  Date DataFinePena       = PenaResidua.getDataFine();
  
  Fungibilita.calcolaStringaFungibilita();

  BigDecimal lGiorniFruiti = Fungibilita.getNumGiorniFruiti();
  BigDecimal lGiorniNonFruiti = Fungibilita.getNumGiorniNonFruiti();

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <title>[S.I.E.S.] - Calcolo Pena DL92/2014</title>
  </head>

  <body class="corpo">
    <!-- Vedi Calcolo Pena Lib Ant-->
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Calcolo Pena DL92/2014</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActConfermaFungibilitaLibAnt">

      <input type="HIDDEN" name="GiornoDataSysCalcoli" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli, "dd"))%>">
      <input type="HIDDEN" name="MeseDataSysCalcoli"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli, "MM"))%>">
      <input type="HIDDEN" name="AnnoDataSysCalcoli"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli, "yyyy"))%>">
      
      <input type="HIDDEN" name="IdFungibilita"         value="<%=StringUtils.toStringJSP(Fungibilita.getIdFungibilita())%>">
      <input type="HIDDEN" name="IdPenaResidua"         value="<%=StringUtils.toStringJSP(PenaResidua.getIdPenaResidua())%>">
      <input type="HIDDEN" name="lEveIdEventoOrdinanza" value="<%=StringUtils.toStringJSP(lEveIdEventoOrdinanza)%>">

      <input type="HIDDEN" name="isDL92" value="S">
      
      <input type="HIDDEN" name="<%=ICostantiFungibilita.CAMPO_NUM_GIORNI_FRUITI%>" value="<%=StringUtils.toStringJSP(lGiorniFruiti,"")%>">
      <input type="HIDDEN" name="<%=ICostantiFungibilita.CAMPO_NUM_GIORNI_NON_FRUITI%>" value="<%=StringUtils.toStringJSP(lGiorniNonFruiti,"")%>">
      
    <table>
      <tr>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua Da Espiare</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiArresto(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
    </table>
<%
    if(   Fungibilita.getNumAnni() != null
       && Fungibilita.getNumMesi() != null
       && Fungibilita.getNumGiorni()!= null)
    {
      if (   Fungibilita.getNumAnni().intValue()!=0
         ||  Fungibilita.getNumMesi().intValue()!=0
         ||  Fungibilita.getNumGiorni().intValue()!=0)
      {
%>
       <table>
         <tr>
           <td class="Titolo" colspan=9><font class="label">Giorni di Detrazione Concessi</font></td>
           <td class="l"><font class="label">Giorni</font></td>
           <td class="l"><font class="Campo"><%=TotaleGiorniLibAnt%></font></td>
         </tr>
         <tr>
           <td class="Titolo" colspan=9><font  class="label">Giorni Fruiti</font></td>
           <td class="l"><font class="label">Giorni</font></td>
           <td class="l"><font class="Campo"><%=StringUtils.toStringJSP(lGiorniFruiti,"")%></font></td>
         </tr>
         <tr>
           <td class="Titolo" colspan=9><font  class="label">Giorni Non Fruiti</font></td>
           <td class="l"><font class="label">Giorni</font></td>
           <td class="l"><font class="Campo"><%=StringUtils.toStringJSP(lGiorniNonFruiti,"")%></font></td>
         </tr>
<tr><td><br></td></tr>         
         <tr>
           <td class="Titolo" colspan=9><font class="label">Pena Già Espiata</font></td>
           <td class="l"><font class="label">Anni</font></td>
           <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumAnni()%></font></td>
           <td class="l"><font class="label">Mesi</font></td>
           <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumMesi()%></font></td>
           <td class="l"><font class="label">Giorni</font></td>
           <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumGiorni()%></font></td>
           <% if (dataScarcerazione!=null) {%>
           <td class="l"><font class="label">al</font></td>
           <td class="l"><font class="cRossoCumulo"><%=DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy") %></font></td>
           <% } %>
         </tr>
         <tr>
           <td class="Titolo" colspan=9><font  class="label">Pena Espiata In Eccesso</font></td>
<%
             String GGFung=Fungibilita.getNumGiorni()+"";
             String MMFung=Fungibilita.getNumMesi()+"";
             String AAFung=Fungibilita.getNumAnni()+"";
%>
           <td class="l"><font class="label">Anni</font></td>
           <td class="l"><font class="Campo"><%=AAFung%></font></td>
           <td class="l"><font class="label">Mesi</font></td>
           <td class="l"><font class="Campo"><%=MMFung%></font></td>
           <td class="l"><font class="label">Giorni</font></td>
           <td class="l"><font class="Campo"><%=GGFung%></font></td>
         </tr>
      </table>
<%
      }
    }
%>

  <table>
    <tr>
      <td class="l"><font  class="label">Data Decorrenza Pena:</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
        </font>
      </td>
      <% if (UltimaPenaResidua!=null && UltimaPenaResidua.getDataFine()!=null ) { %>
      <td class="l"><font  class="label">Data Scadenza Pena: </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(UltimaPenaResidua.getDataFine(),"dd-MM-yyyy"))%>
        </font>
      </td>
      <% } %>
      
      <% if (DataFineReclusione!=null) { %>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineReclusione,"dd-MM-yyyy"))%>
          </font>
        </td>
      <% } %>
    </tr>
    
    
    <tr>
      <% if (DataInizioArresto!=null) { %>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioArresto,"dd-MM-yyyy"))%>
          </font>
        </td>
      <% } %>

      <% if (DataFinePena != null) { %>
        <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </font>
        </td>
    </tr>
    </table>
      
<%
//==============================================================================
// Opzioni di scelta
//==============================================================================
      if ( DateUtils.isGreater(dataSistemaPerCalcoli, DataFinePena) )
      {
        String dataDiCalcolo = "";
        if (dataScarcerazione==null) {
          dataDiCalcolo = "Odierna";
        }
        else{
          dataDiCalcolo = "di Scarcerazione specificata";
        }        
%>
  <table>
        <input type="HIDDEN" name="GiornoDataFineCalcolata" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena, "dd"))%>">
        <input type="HIDDEN" name="MeseDataFineCalcolata" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena, "MM"))%>">
        <input type="HIDDEN" name="AnnoDataFineCalcolata" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena, "yyyy"))%>">

        <br>
        <tr>
          <td class="l" colspan="4" width="500">
            <font class="cRossoCumulo">Attenzione la Data di Fine Pena Rideterminata è Inferiore alla Data <%=dataDiCalcolo%>,
                                       scegliere come si vuole procedere per la Data Fine Pena e la Fungibilità: </font>
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3">
            <font class="label">Data Fine Pena : </font>
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="1" checked >Data <%=dataDiCalcolo%> con n. <%=StringUtils.toStringJSP(Fungibilita.getNumGiorniNonFruiti(),"")%> giorni di riduzione pena non usufruiti
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3">
            <font class="label">Data Fine Pena : </font>
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="3">Data fine pena calcolata detraendo tutti i giorni di riduzione pena
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3">
            <font class="label">Data Fine Pena : </font>
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data <%=dataDiCalcolo%> senza utilizzo dei giorni di riduzione pena
          </td>
        </tr>
      </table>

<%
      }
    }
%>
    <table>
      <tr>
        <td class="l"colspan=4>
          <input class="bottone" type="submit" name="conferma" value="Conferma">
        </td>
      </tr>
    </table>
    </form>
  </body>
</html>