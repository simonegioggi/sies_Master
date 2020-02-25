<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.penaresidua.util.PenaResiduaUtil"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>

<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="f3b.log.LogF3B" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="lFlagPage"              scope="request" class="java.lang.String" />

<%
Boolean lIsCalcoloPenaAbInitio = (Boolean) request.getAttribute("lIsCalcoloPenaAbInitio");
%>

<%
// Informazioni riportate nella prima sezione della form: pena di partenza + quantum aggregati
%>
<jsp:useBean id="PenaComplessivaSentenza" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="ultimaPenaValidata"      scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<jsp:useBean id="BeneficiConcessiArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiRevocatiArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiConcessiReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiRevocatiReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="AnnManConcessiArresto"      scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="AnnManRevocatiArresto"      scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="AnnManConcessiReclusione"   scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="AnnManRevocatiReclusione"   scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="MisCauComputabiliReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="PenaComplessiva"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="Fungibilita"             scope="request" class="siap.siep.fungibilita.model.FungibilitaModel" />
<jsp:useBean id="PenaGiaEspiata"          scope="request" class="siap.sico.calendar.model.CalendarModel" />

<%
// Informazioni riportate nella seconda sezione della form: pena rideterminata + pena espiata + fungibilità
%>
<jsp:useBean id="ChiedereValidazionePena" scope="request" class="java.lang.String" />
<jsp:useBean id="VedoJSP"                 scope="request" class="java.lang.String" />
<jsp:useBean id="vedoDataIntermedia"      scope="request" class="java.lang.String" />
<jsp:useBean id="FlagAltraCausa"          scope="request" class="java.lang.String" />
<jsp:useBean id="CodPosizioneGiuridica"   scope="request" class="java.lang.String" />
<jsp:useBean id="Segnalazione"            scope="request" class="java.lang.String" />
<jsp:useBean id="IdFungibilita"           scope="request" class="java.lang.String" />
<jsp:useBean id="ForzaSysdateManuale"     scope="request" class="java.lang.String" />



<%

  /*
    Questa maschera serve per mostrare il dettaglio della pena rideterminata
    dopo le operazioni di calcolo effettuate dopo l'inserimento di un beneficio
    o di una richiesta.
   
    Il paramentro lFlagPage indica la provenienza della chiamata e assume i 
    seguenti valori:
    Rideterminazione pena / Richieste al GE (Depenalizzazione, Incostituzionalità, Amnistia/Indulto)
      lFlagPage = GE
    Rideterminazione pena / Provvedimenti del PM
      lFlagPage = A - Altro Titolo (Fungibilità altro reato Misura Cautelare)
      lFlagPage = S - Senza Titolo (Fungibilità altro reato Pena Detentiva)
      lFlagPage = D - Stesso Titolo (Presofferto)
    Decisioni del GE / Applicazione Benefici
      lFlagPage = AMNI   - Aministia
      lFlagPage = DEPEN  - Depenalizzazione
      lFlagPage = INCOST - Incostituzionalità
      
    Le action di provenienza sono:
    - siap.siep.calcolopena.ActCalcoloPenaGE
    - siap.siep.calcolopena.ActCalcoloPenaComputo
    
    I dati riportati in maschera dovrebbero consentire all'utente di comprendere
    il calcolo effettuato per cui form è suddivia in tre sezioni contenenti:
    - quantum di partenza
    - quantum computato
    - pena rideterminata
    - pena espiata/da espiare/espiata in eccesso
    - sezione con le date
    - sezione relativa alla fungibilità
    
    La visualizzazione della situazione di partenza varia in funzione del tipo
    di calcolo effettuato: 
    - abInizio
    - non abInizio
    
    Nel caso di Richieste al GE e decisioni del GE, i calcoli dovrebbero essere 
    effettuati sulla pena in sentenza per cui vengono riportati i dati in 
    sentenza ( quelli su cui vengono fatti i calcoli ): 
    - Pena Complessiva in Sentenza
    - Benefici Concessi
    - Benefici Revocati
    - Misure Cautelari
    
    - Benefici Concessi con Provvedimento (A/R)
    
    Viene infine visualizzata la pena rideterminata. Tale pena ha un significato
    differente in funzione del tipo di calcolo:
    - nel caso di concessione benefici viene visualizzata la pena in sentenza
      rideterminata
    - nel caso dei computi viene visualizzata la pena residua rideterminata
    
    Nel caso di calcolo non abInizio...
    
    
  */
  
  //-- Per la gestione periodi nelle Annotazioni Manuali Computo Custodia Cautelare
  List lListaAnnotazioni = new ArrayList();
  boolean lIsComputo = false;
  if(lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D"))
  {
    lListaAnnotazioni = (List)request.getAttribute("ListaAnnotazioni");
    lIsComputo = true;
  }
  //--

  //============================================================================
  // DataInizioPena     = data inizio ultima pena validata se esiste
  // DataFineReclusione = 
  // DataInizioArresto  =
  // DataFinePena       = 
  //============================================================================
  Date DataInizioPena     = (Date)request.getAttribute("DataInizioPena");
  Date DataFineReclusione = (Date)request.getAttribute("DataFineReclusione");
  Date DataInizioArresto  = (Date)request.getAttribute("DataInizioArresto");
  Date DataFinePena       = (Date)request.getAttribute("DataFinePena");

  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("DataInizioPena     = "+DataInizioPena);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("DataFineReclusione = "+DataFineReclusione);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("DataInizioArresto  = "+DataInizioArresto);
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("DataFinePena       = "+DataFinePena);


	Date dataSistemaPerCalcoli = null;
	Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");
  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  siesLogger.debug("dataScarcerazione       = "+dataScarcerazione);
	
  if (dataScarcerazione==null) {
     dataSistemaPerCalcoli = DateUtils.getSysDate();
  }
  else{
     dataSistemaPerCalcoli = dataScarcerazione;
  }
  
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("dataSistemaPerCalcoli = "+dataSistemaPerCalcoli);
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("dataSistemaPerCalcoli = "+DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd") );
	
  
  BigDecimal IdEvento = (BigDecimal)request.getAttribute("IdEvento");
  
  
  CalendarUtil lCalendarUtil = new CalendarUtil();
  
%>
<html>
  <!--  VediNuovoCalcoloPena -->
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
        if(document.f.GPV.value!="" && document.f.MPV.value!="" && document.f.APV.value!="")
        {
          if (document.f.GPV.value.length==1)
            document.f.GPV.value='0'+document.f.GPV.value;
          if (document.f.MPV.value.length==1)
            document.f.MPV.value='0'+document.f.MPV.value;

      		var data_to_verify = document.f.GPV.value+'/'+document.f.MPV.value+'/'+document.f.APV.value;
          if (data_to_verify.length>4)
          {
            if (!ControllaData(data_to_verify) )
            {
              alert('Data di Decorrenza non valida');
              return false;
            }
            else
              return true;
          }
        }
        else
          return true;
	    }
    </script>
    <title>[S.I.E.S.] - Calcolo Pena</title>
  </head>
  
  
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Calcolo Pena</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    
<%
//==============================================================================
//         SEZIONE PER LA VISUALIZZAZIONE DEI QUANTUM DI PARTENZA
//==============================================================================
%>    
    <table>
<%
      if(lIsCalcoloPenaAbInitio.booleanValue() == true)
      { //
        if(!lIsComputo)
        { 
        //======================================================================
        // Richiesta o Concessioni benefici (amnistia, indulto, depenalizzazione
        // , incostituzionalità)
        //======================================================================
        /*
          In questo caso visualizzo:
          - la pena complessiva in sentenza
          - benefici concessi in sentenza
          - benefici revocati
          - misure cautelari
        */
%>
          <%
          //====================================================================
          //                 PENA COMPLESSIVA IN SENTENZA
          //====================================================================
          %>
          <tr>
            <td class="Titolo" colspan=9><font  class="label">Pena Complessiva in Sentenza</font></td>
          </tr>
          <tr>
            <td class="l"><font class="label">Reclusione / Multa :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumAnniReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumMesiReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumGiorniReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessivaSentenza.getImportoMulta())%></font></td>
          </tr>
          <tr>
            <td class="l"><font class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumAnniArresto(),"0")%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumMesiArresto(),"0")%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumGiorniArresto(),"0")%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessivaSentenza.getImportoAmmenda())%></font></td>
          </tr>
          
          
          <%
          //====================================================================
          //                 BENEFICI CONCESSI IN SENTENZA
          //====================================================================
          if (! (   lCalendarUtil.isZero(BeneficiConcessiReclusione) 
                 && BeneficiConcessiReclusione.getImportoMulta()==0 
                 && lCalendarUtil.isZero(BeneficiConcessiArresto) 
                 && BeneficiConcessiReclusione.getImportoAmmenda()==0
                )
             )
          {
          %>
            <tr>
              <td class="Titolo" colspan=9><font  class="label">Benefici Concessi in Sentenza</font></td>
            </tr>
            <% if (!(lCalendarUtil.isZero(BeneficiConcessiReclusione) && BeneficiConcessiReclusione.getImportoMulta()==0 )) { %>
              <tr>
                <td class="l"><font class="label">Reclusione / Multa :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiConcessiReclusione.getImportoMulta()))%></font></td>
              </tr>
            <% } %>


            <% if (!( lCalendarUtil.isZero(BeneficiConcessiArresto) && BeneficiConcessiArresto.getImportoAmmenda()==0)) { %>
              <tr>
                <td class="l"><font class="label">Arresto / Ammenda :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiConcessiArresto.getImportoAmmenda())) %></font></td>
              </tr>
            <% } %>
          <%}%>

          <%
          //====================================================================
          //                 BENEFICI REVOCATI IN SENTENZA
          //====================================================================
          if ( !(   lCalendarUtil.isZero(BeneficiRevocatiReclusione) 
                 && BeneficiRevocatiReclusione.getImportoMulta()==0 
                 && lCalendarUtil.isZero(BeneficiRevocatiArresto) 
                 && BeneficiRevocatiArresto.getImportoAmmenda()==0
                )
             )
          {
          %>
            <tr>
                <td class="Titolo" colspan=9><font  class="label">Benefici Revocati in Sentenza</font></td>
            </tr>

            <% if (!(lCalendarUtil.isZero(BeneficiRevocatiReclusione) && BeneficiRevocatiReclusione.getImportoMulta()==0 )) { %>
              <tr>
                <td class="l"><font class="label">Reclusione / Multa :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiRevocatiReclusione.getImportoMulta()))%></font></td>
              </tr>
            <% } %>

            <% if (!( lCalendarUtil.isZero(BeneficiRevocatiArresto) && BeneficiRevocatiArresto.getImportoAmmenda()==0)) { %>
              <tr>
                <td class="l"><font class="label">Arresto / Ammenda :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiRevocatiArresto.getImportoAmmenda()))%></font></td>
              </tr>
            <% } %>
          <% } %>

          <%
          //====================================================================
          //                 MISURE CAUTELARI COMPUTABILI
          //====================================================================
          if (!(lCalendarUtil.isZero(MisCauComputabiliReclusione) && lCalendarUtil.isZero(MisCauComputabiliArresto) ))
          {
          %>
            <tr>
              <td class="Titolo" colspan=9><font  class="label">Misure Cautelari Computabili</font></td>
            </tr>
            <% if (!(lCalendarUtil.isZero(MisCauComputabiliReclusione))) { %>
              <tr>
                <td class="l"><font class="label">Reclusione : </font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumGiorni()%></font></td>
              </tr>
            <% } %>
            
            <% if (!( lCalendarUtil.isZero(MisCauComputabiliArresto))) { %>
              <tr>
                <td class="l"><font class="label">Arresto :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumGiorni()%></font></td>
              </tr>
            <% } %>
          <% } %>
          
<%// ***********************************************************************  %>
<%// ***********************************************************************  %>
<%// ***********************************************************************  %>
          <%
          //====================================================================
          //              BENEFICI CONCESSI CON PROVVEDIMENTI
          //====================================================================
          if ( !(   lCalendarUtil.isZero(AnnManConcessiReclusione) 
                 && AnnManConcessiReclusione.getImportoMulta()==0 
                 && lCalendarUtil.isZero(AnnManConcessiArresto) 
                 && AnnManConcessiReclusione.getImportoAmmenda()==0))
          {
          %>
            <tr>
              <td class="Titolo" colspan=9><font class="label">Benefici Concessi con Provvedimento</font></td>
            </tr>
            <%
            if (!(lCalendarUtil.isZero(AnnManConcessiReclusione) && AnnManConcessiReclusione.getImportoMulta()==0 ))
            {
            %>
              <tr>
                <td class="l"><font class="label">Reclusione / Multa :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManConcessiReclusione.getImportoMulta()))%></font></td>
              </tr>
            <%
            }

            if (!( lCalendarUtil.isZero(AnnManConcessiArresto) && AnnManConcessiArresto.getImportoAmmenda()==0))
            {
            %>
              <tr>
                <td class="l"><font class="label">Arresto / Ammenda :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManConcessiArresto.getImportoAmmenda())) %></font></td>
              </tr>
            <%
            }
          }


          //====================================================================
          //              BENEFICI REVOCATI CON PROVVEDIMENTI
          //====================================================================
          if (! (   lCalendarUtil.isZero(AnnManRevocatiReclusione) 
                 && AnnManRevocatiReclusione.getImportoMulta()==0 
                 && lCalendarUtil.isZero(AnnManRevocatiArresto) 
                 && AnnManRevocatiArresto.getImportoAmmenda()==0
                )
             )
          {
          %>
            <tr>
              <td class="Titolo" colspan=9><font  class="label">Benefici Revocati con Provvedimento</font></td>
            </tr>
            <%
            if (!(lCalendarUtil.isZero(AnnManRevocatiReclusione) && AnnManRevocatiReclusione.getImportoMulta()==0 ))
            {
            %>
              <tr>
                <td class="l"><font class="label">Reclusione / Multa :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManRevocatiReclusione.getImportoMulta()))%></font></td>
              </tr>
            <%
            }

            if (!( lCalendarUtil.isZero(AnnManRevocatiArresto) && AnnManRevocatiArresto.getImportoAmmenda()==0))
            {
            %>
              <tr>
                <td class="l"><font class="label">Arresto / Ammenda :</font></td>
                <td class="l"><font class="label">Anni</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumAnni()%></font></td>
                <td class="l"><font class="label">Mesi</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumMesi()%></font></td>
                <td class="l"><font class="label">Giorni</font></td>
                <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumGiorni()%></font></td>
                <td class="l"><font class="label">Importo</font></td>
                <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManRevocatiArresto.getImportoAmmenda()))%></font></td>
              </tr>
            <%
            }
          }
        }
        else if(!lListaAnnotazioni.isEmpty())
        { 
        	// E' un computo (A,D,S) ed ho le annotazioni, il calcolo viene fatto
        	// sulle annotazioni
          %>
          <tr>
            <td class="Titolo" colspan=9><font class="label">Periodi Computati con Provvedimento</font></td>
          </tr>
          <%
          for (Iterator lIter = lListaAnnotazioni.iterator(); lIter.hasNext(); )
          {
            AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();
          %>
            <tr>
              <td class="l">Dalla Data :
                <font class="campo">
                  <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneDa(), "dd-MM-yyyy"),"-")%>&nbsp;
                </font>
              </td>
              <td class="l">Alla Data :
                <font class="campo">
                  <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneA(), "dd-MM-yyyy"),"-")%>&nbsp;
                </font>
              </td>
            </tr>
          <%
          }// end for
        }
      }
      else if(lIsComputo) // calcolo non abIizio
      { // Se il calcolo non è abInizio, visualizzo la prima sezione solo se 
    	  // trattasi di computo e in questo caso visualizzo solo i periodi 
    	  // computati. 
    	  //
    	  // Nel caso di Benefici non visualizzo la sezione con i dati della 
    	  // sentenza in quanto il calcolo non è partito da tali dati.
    	  // Dovrei comunque visualizzare i benefici imputati.
      %>
        <tr>
          <td class="Titolo" colspan=9><font class="label">Periodi Computati con Provvedimento</font></td>
        </tr>
      <%
        for (Iterator lIter = lListaAnnotazioni.iterator(); lIter.hasNext(); )
        {
          AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();
        %>
          <tr>
            <td class="l">Dalla Data :
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneDa(), "dd-MM-yyyy"),"-")%>&nbsp;
              </font>
            </td>
            <td class="l">Alla Data :
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneA(), "dd-MM-yyyy"),"-")%>&nbsp;
              </font>
            </td>
          </tr>
        <%
        }  // end for
      }
      else {
  		//========================================================================
  		// Calcolo nonAbInizio, Richieste o Decisione del GE, visualizzo l'ultima
  		// pena validata, che è stata utilizzata come punto di partenza per i 
  		// calcoli, e i quantum computati con Richiesta/Provvedimento
  		//========================================================================
  		// 09/2006 aggiunta la visualizzazione dei dati di partenza per il calcolo
  		// anche nel caso di benefici non abInizio. In questo caso coincide con
  		// l'ultima pena validata
    	  if ( ultimaPenaValidata.getIdPenaResidua()!=null ) {
    	    //
    	    //PenaResiduaModel penaResiduaAl = PenaResiduaUtil.calcolaPenaNuovaDataFine(dataSistemaPerCalcoli, ultimaPenaValidata, false);
    	    PenaResiduaModel penaResiduaAl = ultimaPenaValidata;
    	  %>
          <tr>
            <% if (dataScarcerazione==null) { %>
            <td class="Titolo" colspan=9><font  class="label">Pena Residua Da Espiare</font></td>
            <% } else { %>
            <td class="Titolo" colspan=9><font  class="label">Pena Residua Da Espiare al</font> <font class="cRossoCumulo"><%=DateUtils.getDateToString(dataScarcerazione,"dd-MM-yyyy") %></font></td>
            <% } %>
          </tr>
          <tr>
            <td class="l"><font class="label">Reclusione / Multa :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumAnniReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumMesiReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumGiorniReclusione(),"0")%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(penaResiduaAl.getImportoMulta())%></font></td>
          </tr>
          <tr>
            <td class="l"><font class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumAnniArresto(),"0")%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumMesiArresto(),"0")%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaResiduaAl.getNumGiorniArresto(),"0")%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(penaResiduaAl.getImportoAmmenda())%></font></td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        <% } %>
        
        <%
        //======================================================================
        //              SEZIONE CON I QUANTUM RICHIESTI/CONCESSI
        //======================================================================
        %>
        <!--tr>
          <td class="Titolo" colspan=9><font class="label">Quantum Computati con Provvedimento</font></td>
        </tr-->
        <%
        if ( !(   lCalendarUtil.isZero(AnnManConcessiReclusione) 
               && AnnManConcessiReclusione.getImportoMulta()==0 
               && lCalendarUtil.isZero(AnnManConcessiArresto) 
               && AnnManConcessiReclusione.getImportoAmmenda()==0
              )
           )
        {
        %>
          <tr>
            <% if (lFlagPage.equals("GE")) { %>
            <td class="Titolo" colspan=9><font class="label">Benefici Concessi con Richiesta</font></td>
            <% } else {%>
            <td class="Titolo" colspan=9><font class="label">Benefici Concessi con Provvedimento</font></td>
            <% } %>
          </tr>
          
          <% if (!(lCalendarUtil.isZero(AnnManConcessiReclusione) && AnnManConcessiReclusione.getImportoMulta()==0 )) { %>
            <tr>
              <td class="l"><font class="label">Reclusione / Multa :</font></td>
              <td class="l"><font class="label">Anni</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumAnni()%></font></td>
              <td class="l"><font class="label">Mesi</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumMesi()%></font></td>
              <td class="l"><font class="label">Giorni</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiReclusione.getNumGiorni()%></font></td>
              <td class="l"><font class="label">Importo</font></td>
              <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManConcessiReclusione.getImportoMulta()))%></font></td>
            </tr>
          <% } %>
          
          <% if (!( lCalendarUtil.isZero(AnnManConcessiArresto) && AnnManConcessiArresto.getImportoAmmenda()==0)) { %>
            <tr>
              <td class="l"><font class="label">Arresto / Ammenda :</font></td>
              <td class="l"><font class="label">Anni</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumAnni()%></font></td>
              <td class="l"><font class="label">Mesi</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumMesi()%></font></td>
              <td class="l"><font class="label">Giorni</font></td>
              <td class="l"><font class="campo"><%=AnnManConcessiArresto.getNumGiorni()%></font></td>
              <td class="l"><font class="label">Importo</font></td>
              <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManConcessiArresto.getImportoAmmenda())) %></font></td>
            </tr>
          <% } %>        
        <% } %>
        
        
        <%  
        if ( !(    lCalendarUtil.isZero(AnnManRevocatiReclusione) 
                && AnnManRevocatiReclusione.getImportoMulta()==0 
                && lCalendarUtil.isZero(AnnManRevocatiArresto) 
                && AnnManRevocatiArresto.getImportoAmmenda()==0
              )
           )
        {
        %>
          <tr>
            <% if (lFlagPage.equals("GE")) { %>
            <td class="Titolo" colspan=9><font class="label">Benefici Revocati con Richiesta</font></td>
            <% } else {%>
            <td class="Titolo" colspan=9><font class="label">Benefici Revocati con Provvedimento</font></td>
            <% } %>
          </tr>
          <% if (!(lCalendarUtil.isZero(AnnManRevocatiReclusione) && AnnManRevocatiReclusione.getImportoMulta()==0 )) { %>
          <tr>
            <td class="l"><font class="label">Reclusione / Multa :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumAnni()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumMesi()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiReclusione.getNumGiorni()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManRevocatiReclusione.getImportoMulta()))%></font></td>
          </tr>
          <% } %>
          
          <% if (!( lCalendarUtil.isZero(AnnManRevocatiArresto) && AnnManRevocatiArresto.getImportoAmmenda()==0)) { %>
          <tr>
            <td class="l"><font class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumAnni()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumMesi()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="l"><font class="campo"><%=AnnManRevocatiArresto.getNumGiorni()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(AnnManRevocatiArresto.getImportoAmmenda()))%></font></td>
          </tr>
          <% } %>
        <% } %>
        
<%  

}	// end if(lIsCalcoloPenaAbInitio.booleanValue() == true)

%>
  </table>
  
<%
//==============================================================================
// FINE SEZIONE CON I QUANTUM DI PARTENZA
//==============================================================================
%>  
  
  
<%
//==============================================================================
// SEZIONE CON I QUANTUM RIDETERMINATI
// - questa sezione è invariante rispetto al tipo di computo (richieste al GE,
//   Decisioni del GE, computi)
// Viene visualizzata:
// - la pena rideterminata (>0 o <0 evidenziati in rosso)
// - pena già espiata, pena espiata in eccesso (fungibilità, entrambi passati sulla request)
// - pena da espiare calcolata al volo solo se pena in espiazione (datainizio<>null 
//    e <data di systema (pene con decorrenza futura)
// - le date di decorrenza
// - eventuale sezione per gestione fungibilità
//==============================================================================
%>  
  <br>
  <table>
    <tr>
      <td class="Titolo" colspan=9><font class="label">Pena Rideterminata</font></td>
    </tr>
<%
    /*
     Segnalazione = S se i quantum ricalcolati sono negativi o nulli e il 
                      soggetto è in espiazione (ho data inizio). 
     In questo caso PenaComplessiva contiene il quantum rideterminato, quindi 0 o
     <0 mentre le date sono solo DataInizio e DataFinePena dell'ultimo record pena
     residua validato (quelle previste prima del calcolo)
    */
      if (   Segnalazione.equals("S") 
          && (   PenaComplessiva.getNumAnniReclusione().intValue()!=0 
              || PenaComplessiva.getNumMesiReclusione().intValue()!=0 
              || PenaComplessiva.getNumGiorniReclusione().intValue()!=0 
              || PenaComplessiva.getNumAnniArresto().intValue()!=0 
              || PenaComplessiva.getNumMesiArresto().intValue()!=0 
              || PenaComplessiva.getNumGiorniArresto().intValue()!=0
             )
         )
      { // Quantum negativi visualizzo i dati evidenziandoli in rosso
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmenda())%></font></td>
        </tr>
      <%
      }
      else
      {  // Quantum nulli o positivi: visualizzo i dati senza evidenziarli in rosso
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmenda())%></font></td>
        </tr>
    <%}%>
    </table>
    
    <%
    //==========================================================================
    // SEZIONE CONTENENTE PENA GIA' ESPIATA E PENA ESPIATA IN ECCESSO
    // la sezione viene visualizzata solo se la pena è in espiazione 
    // (datainizio <>null) e > data di sistema. Infatti possono esserci
    // casi con data inizio valorizzata ma futura (es 10/10/2011)
    // In questo caso non ha senso parlare di pena da espiare o già espiata
    //==========================================================================
    %>
    <% if (PenaComplessiva.getDataInizio()!=null){ %>    
    <table>
      <tr>
        <td width=100% colspan=2>
          <table width=100%>
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
            <%
            if(   Fungibilita.getNumAnni() != null 
               && Fungibilita.getNumMesi() !=null 
               && Fungibilita.getNumGiorni()!= null)
            {
              if (   Fungibilita.getNumAnni().intValue()!=0 
                  || Fungibilita.getNumMesi().intValue()!=0 
                  || Fungibilita.getNumGiorni().intValue()!=0
                 )
              {             
            %>
            <tr>
              <td class="Titolo" colspan=9><font  class="label">Pena Espiata In Eccesso</font></td>
              <%
                String GGFung = Fungibilita.getNumGiorni()+"";
                String MMFung = Fungibilita.getNumMesi()+"";
                String AAFung = Fungibilita.getNumAnni()+"";
              %>
              <td class="l"><font class="label">Anni</font></td>
              <td class="l"><font class="Campo"><%=AAFung%></font></td>
              <td class="l"><font class="label">Mesi</font></td>
              <td class="l"><font class="Campo"><%=MMFung%></font></td>
              <td class="l"><font class="label">Giorni</font></td>
              <td class="l"><font class="Campo"><%=GGFung%></font></td>
            </tr>
            <% } %> 
           <% } %> 
           
           <% 
           // Calcolo la pena da espiare come intervallo tra la data di calcolo e
           // il fine pena rideterminato.
           // La sezione non viene visualizzata se:
           // - i quantum rideterminati sono negativi, in questo caso la data 
           //   fine non viene ricalcolata ma resta quella inizialmente prevista,
           //   non ha senso utilizzarla
           // - la data inizio è una data futura. In questo caso il quantum da
           //   espiare coincide con il quantum rideterminato.
           CalendarModel lReclusioneRidet = new CalendarModel();
           CalendarModel lArrestiRidet    = new CalendarModel();
        
           // Reclusione/Arresto
           lReclusioneRidet = PenaComplessiva.getQuantumReclusione();
           lArrestiRidet    = PenaComplessiva.getQuantumArresto();

           if (   DataFinePena!=null 
               && lCalendarUtil.isPositiveTime(lReclusioneRidet)
               && lCalendarUtil.isPositiveTime(lArrestiRidet)
               && DateUtils.isLower (DataInizioPena,dataSistemaPerCalcoli) // check decorrenza futura
               && !DateUtils.isLower (DataFinePena, dataSistemaPerCalcoli) // data fine deve essere >= data scarcerazione
              )
           {
            
             CalendarModel lCalPenaDaEspiare = new CalendarModel();
            
  
             //lCalPenaDaEspiare.setDataInizio ( DateUtils.getSysDate() );
             lCalPenaDaEspiare.setDataInizio ( dataSistemaPerCalcoli);
             
             //lCalPenaDaEspiare.setDataFine   ( PenaComplessiva.getDataFinePresunta() );
             lCalPenaDaEspiare.setDataFine   ( DataFinePena );
           
             lCalPenaDaEspiare=lCalendarUtil.ricalcolaGAM(lCalendarUtil.CalcolaNumGiorniMesiAnni(lCalPenaDaEspiare, true));
           
             if ( lCalendarUtil.isPositiveTime(lCalPenaDaEspiare) ){
               String attributo = "class=\"Campo\"";
               if ( lCalendarUtil.isZero(lCalPenaDaEspiare) )
                 attributo = "color=\"red\"";
             %>
            <tr>
              <td class="Titolo" colspan=9><font  class="label">Pena da Espiare</font></td>
              <td class="l"><font class="label" >Anni</font></td>
              <td class="l"><font <%=attributo%>><%=lCalPenaDaEspiare.getNumAnni()%></font></td>
              <td class="l"><font class="label">Mesi</font></td>
              <td class="l"><font <%=attributo%>><%=lCalPenaDaEspiare.getNumMesi()%></font></td>
              <td class="l"><font class="label">Giorni</font></td>
              <td class="l"><font <%=attributo%>><%=lCalPenaDaEspiare.getNumGiorni()%></font></td>
            </tr>
           <% } %>
           <% } %>
          </table>
        </td>
      </tr>
    </table>
    <% } %>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>    
<%--    
    <table>
      <tr>
        <td width=100% colspan=2>
        <%
        if(Fungibilita.getNumAnni() != null 
           && Fungibilita.getNumMesi() !=null 
           && Fungibilita.getNumGiorni()!= null)
        {
         if (Fungibilita.getNumAnni().intValue()!=0 
             || Fungibilita.getNumMesi().intValue()!=0 
             || Fungibilita.getNumGiorni().intValue()!=0)
         {
        %>
       <table width=100%>
         <tr>
           <td class="Titolo" colspan=9><font class="label">Pena Già Espiata</font></td>
           <td class="l"><font class="label">Anni</font></td>
           <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumAnni()%></font></td>
           <td class="l"><font class="label">Mesi</font></td>
  			   <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumMesi()%></font></td>
           <td class="l"><font class="label">Giorni</font></td>
  			   <td class="l"><font class="Campo"><%=PenaGiaEspiata.getNumGiorni()%></font></td>
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
      </td>
    </tr>
  </table>
--%>  
  
    <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciNuovaPenaValidata">
      <input type="HIDDEN" name="IdPenaResidua" value="<%=PenaComplessiva.getIdPenaResidua()%>">
      <input type="HIDDEN" name="lFlagPage"             value="<%=lFlagPage%>">
      <input type="HIDDEN" name="FlagAltraCausa"        value="<%=FlagAltraCausa%>">
      <input type="HIDDEN" name="CodPosizioneGiuridica" value="<%=CodPosizioneGiuridica%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(IdEvento)%>">


      <% if (DataInizioPena!=null) { %>
      <input type="HIDDEN" name="Gdatainiziopena" value="<%=DateUtils.getDayToString(DataInizioPena)%>">
      <input type="HIDDEN" name="Mdatainiziopena" value="<%=DateUtils.getMonthToString(DataInizioPena)%>">
      <input type="HIDDEN" name="Adatainiziopena" value="<%=DateUtils.getYearToString(DataInizioPena)%>">
      <% } %>
      
      <% if (DataFinePena!=null) { %>
      <input type="HIDDEN" name="Gdatafinepenapresunta" value="<%=DateUtils.getDayToString(DataFinePena)%>">
      <input type="HIDDEN" name="Mdatafinepenapresunta" value="<%=DateUtils.getMonthToString(DataFinePena)%>">
      <input type="HIDDEN" name="Adatafinepenapresunta" value="<%=DateUtils.getYearToString(DataFinePena)%>">
      <% } %>
      
      <input type="HIDDEN" name="Grec"  value="<%=PenaComplessiva.getNumGiorniReclusione()%>">
      <input type="HIDDEN" name="Mrec"  value="<%=PenaComplessiva.getNumMesiReclusione()%>">
      <input type="HIDDEN" name="Arec"  value="<%=PenaComplessiva.getNumAnniReclusione()%>">
      <input type="HIDDEN" name="Multa" value="<%=PenaComplessiva.getImportoMulta()%>">
      
      <input type="HIDDEN" name="Garr"    value="<%=PenaComplessiva.getNumGiorniArresto()%>">
      <input type="HIDDEN" name="Marr"    value="<%=PenaComplessiva.getNumMesiArresto()%>">
      <input type="HIDDEN" name="Aarr"    value="<%=PenaComplessiva.getNumAnniArresto()%>">
      <input type="HIDDEN" name="Ammenda" value="<%=PenaComplessiva.getImportoAmmenda()%>">
      
      
      <table>
    <%
    //==========================================================================
    // VedoJSP = S se : lDataInizioPena!=null, in questo caso ho rideterminato 
    // anche le date per cui le visualizzo e consento all'utente di modificare
    // la data fine
    //==========================================================================
       
    if ( VedoJSP.equals("S") ) {  
    %>
      <tr>
        <td class="l">Data Decorrenza Pena: </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <%
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("vedoDataIntermedia = "+vedoDataIntermedia);
        
	      if ( vedoDataIntermedia.equals("S") ) {
	      
          if (DataFineReclusione!=null) { %>
          <input type="HIDDEN" name="Gdatafinereclusione" value="<%=DateUtils.getDayToString   (DataFineReclusione)%>">
          <input type="HIDDEN" name="Mdatafinereclusione" value="<%=DateUtils.getMonthToString (DataFineReclusione)%>">
          <input type="HIDDEN" name="Adatafinereclusione" value="<%=DateUtils.getYearToString  (DataFineReclusione)%>">
          <% } %>
          
          <% if (DataInizioArresto!=null){ %>
          <input type="HIDDEN" name="Gdatainizioarresto" value="<%=DateUtils.getDayToString   (DataInizioArresto)%>">
          <input type="HIDDEN" name="Mdatainizioarresto" value="<%=DateUtils.getMonthToString (DataInizioArresto)%>">
          <input type="HIDDEN" name="Adatainizioarresto" value="<%=DateUtils.getYearToString  (DataInizioArresto)%>">
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
      <%
      if (DataInizioArresto!=null) {
      %>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioArresto,"dd-MM-yyyy"))%>
          </font>
        </td>
      <%
      }
    } // fine if vedoDataIntermedia.equals("S") 
    %>
    
    
    <% 
    // DataFinePena è pari a:
    // - null se la pena non è in decorrenza (inizio pena = null)
    // - fine pena calcolata se quantum >0 e pena in espiazione (anche se presente fungibilità)
    // - la stessa di partenza se quantum < 0
    // - la data inizio pena se quantum = 0
    if (DataFinePena!=null) // pena sicuramente in decorrenza
    {
    %>
      <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
<%
//==============================================================================
//                 GESTIONE FUNGIBILITA' E FINE PENA MANUALE
// Si possono verificare 3 casi
// - Caso 1: quantum negativi e pena in corsio di espiazione (non futura)
//           
// - Caso 3: Quantum positivi e data fine pena ricalcolata > data di calcolo. 
//           In questo caso non ho fungibilità, l'utente può modificare la data
//           fine pena manuale (?)
// - 
//==============================================================================


      // Commentato 24-09-2006, la data fine pena manuale non è più la data di 
      // sistema ma la data calcolata. Questo perchè modificare la data manuale
      // comporta la modifica dei quantum
      //if (ForzaSysdateManuale.equals("S"))
      //  DataFinePena=new Date(); // sysdate

      // Nuova sezione (02-10-2006) inserita per gestire la fungibilità, viene 
      // data all'operatore la possibilità di considerare o meno la fungibilità
      // ATTENZIONE!! Questo potrebbe non funziona se i quantum ricalcolati sono < 0
      //              in questo caso la data fine pena non viene ricalcolata
      //              ma resta quella di partenza
      
      CalendarModel lReclusioneRidet = new CalendarModel();
      CalendarModel lArrestiRidet    = new CalendarModel();
  
      // Reclusione/Arresto
      lReclusioneRidet = PenaComplessiva.getQuantumReclusione();
      lArrestiRidet    = PenaComplessiva.getQuantumArresto();
      
      
      //========================================================================
      // Primo caso: quantum negativi e DataInizioPena < DataSistemaPerCalcoli
      //             cioè non pena futura
      //========================================================================
      if (   (   !lCalendarUtil.isPositiveTime(lReclusioneRidet)
              || !lCalendarUtil.isPositiveTime(lArrestiRidet)
             )
          && ( 
               Integer.parseInt(DateUtils.getDateToString(DataInizioPena,"yyyyMMdd")) <
               Integer.parseInt(DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd"))
             )
         )
      {
        // Quantum rideterminati negativi. In questo caso ho una fungibilità 
        // data dalla somma del quantum già espiato + il quantum negativo
        // calcolato
        String dataDiCalcolo = "";
        if (dataScarcerazione==null) {
          dataDiCalcolo = "Odierna";
        }
        else{
          dataDiCalcolo = "di Scarcerazione specificata";
        }
        
      %>
        <input type="hidden" name="GPV" value="<%=DateUtils.getDayToString   (DataFinePena)%>">
        <input type="hidden" name="MPV" value="<%=DateUtils.getMonthToString (DataFinePena)%>">
        <input type="hidden" name="APV" value="<%=DateUtils.getYearToString  (DataFinePena)%>">
        
        <input type="hidden" name="GPS" value="<%=DateUtils.getDayToString   (dataSistemaPerCalcoli)%>">
        <input type="hidden" name="MPS" value="<%=DateUtils.getMonthToString (dataSistemaPerCalcoli)%>">
        <input type="hidden" name="APS" value="<%=DateUtils.getYearToString  (dataSistemaPerCalcoli)%>">
        <br>
        <tr>
          <td class="l" colspan="4" width="500">
            <font class="cRossoCumulo">Attenzione! Pena Rideterminata Negativa, i quantum di pena verranno azzerati,
                                       scegliere come si vuole procedere per la Data Fine Pena e la Fungibilità: </font>
          </td>  
        </tr>      
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="1" checked >Data Fine Pena di Partenza con fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data <%=dataDiCalcolo%> senza fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="3">Data Fine Pena di Partenza senza fungibilità
          </td>
        </tr>
      <%
      }
//      else if ( Integer.parseInt(DateUtils.getDateToString(DataFinePena,"yyyyMMdd")) < 
//                Integer.parseInt(DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd"))
//              )
      //========================================================================
      // Caso 2: quantum positivi con data sistema (calcolo) > data fine pena
      //         Ho fungibilità legata al fatto che la data fine pena calcolata
      //         è già passata
      //========================================================================
      else if ( DateUtils.isGreater(dataSistemaPerCalcoli,DataFinePena) )
      {
        String dataDiCalcolo = "";
        if (dataScarcerazione==null) {
          dataDiCalcolo = "Odierna";
        }
        else{
          dataDiCalcolo = "di Scarcerazione specificata";
        }
      
      %>
        <input type="hidden" name="GPV" value="<%=DateUtils.getDayToString   (DataFinePena)%>">
        <input type="hidden" name="MPV" value="<%=DateUtils.getMonthToString (DataFinePena)%>">
        <input type="hidden" name="APV" value="<%=DateUtils.getYearToString  (DataFinePena)%>">
        
        <input type="hidden" name="GPS" value="<%=DateUtils.getDayToString   (dataSistemaPerCalcoli)%>">
        <input type="hidden" name="MPS" value="<%=DateUtils.getMonthToString (dataSistemaPerCalcoli)%>">
        <input type="hidden" name="APS" value="<%=DateUtils.getYearToString  (dataSistemaPerCalcoli)%>">
        <br>
        <tr>
          <td class="l" colspan="4" width="500">
            <font class="cRossoCumulo">Attenzione la Data di Fine Pena Rideterminata è Inferiore alla Data <%=dataDiCalcolo%>, 
                                       scegliere come si vuole procedere per la Data Fine Pena e la Fungibilità:: </font>
          </td>  
        </tr>      
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="1" checked >Data fine pena calcolata con fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data <%=dataDiCalcolo%> senza fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="3">Data fine pena calcolata senza fungibilità
          </td>
        </tr>
      <%
      }
      else 
      //========================================================================
      // Caso 3: non ho fungibilità, quantum positivi e data fine pena calcolata
      //         < data di calcolo. In questo caso propongo come data fine pena
      //         manuale il fine pena calcolato
      //========================================================================
      { // DataFinePena>=Data per i Calcoli
        if (ChiedereValidazionePena.equals("S")) { %>
          <tr>
            <td class="l"colspan=4><font  class="label">Data Fine Pena Manuale : </font>
              <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(DataFinePena)%>">
              /
              <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(DataFinePena)%>">
              /
              <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(DataFinePena)%>">
              &nbsp;&nbsp;&nbsp;
            </td>
          </tr>
        <%
        } else {
        %>
          <input type="hidden" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString   (DataFinePena)%>">
          <input type="hidden" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString (DataFinePena)%>">
          <input type="hidden" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString  (DataFinePena)%>">
        <%
        }
      } //fine if DataFinePena<dataSistemaPerCalcoli
      
      
    } // fine DataFinePena!=null
  } // fine VedoJSP.equals("S") 
//}
%>
      <tr>
        <input type="HIDDEN" name="IdFungibilita" value="<%=IdFungibilita%>">
        <td class="l"colspan=4><INPUT class="bottone" type="submit" name="conferma" value="Conferma"></td>
      </tr>
    </table>
    </form>
  </body>
<%
  if (VedoJSP.equals("S") && Segnalazione.equals("N"))
  {
    if (DataFinePena!=null || ForzaSysdateManuale.equals("S"))
    {
%>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("f");

        frmvalidator.addValidation("GPV","maxlen=2","La lunghezza massima per il Giorno Pena Validata è di 2 caratteri");
        frmvalidator.addValidation("GPV","numeric","Il campo Giorno Pena Validata deve essere numerico");
        frmvalidator.addValidation("GPV","gt=1","Il campo Giorno Pena Validata deve essere maggiore di 0");
        frmvalidator.addValidation("GPV","lt=31","Il campo Giorno Pena Validata deve essere minore di 31");
        frmvalidator.addValidation("MPV","maxlen=2","La lunghezza massima per il Mese Pena Validata è di 2 caratteri");
        frmvalidator.addValidation("MPV","numeric","Il campo Mese Pena Validata deve essere numerico");
        frmvalidator.addValidation("MPV","gt=1","Il campo Mese Pena Validata deve essere maggiore di 0");
        frmvalidator.addValidation("MPV","lt=12","Il campo Mese Pena Validata deve essere minore di 12");
        frmvalidator.addValidation("APV","maxlen=4","La lunghezza massima per l'Anno Pena Validata è di 4 caratteri");
        frmvalidator.addValidation("APV","minlen=4","La lunghezza minima per l'Anno Pena Validata è di 4 caratteri");
        frmvalidator.addValidation("APV","numeric","Il campo Anno Pena Validata deve essere numerico");
        frmvalidator.addValidation("APV","gt=1900","Il campo Anno Pena Validata deve essere maggiore di 1900");
        frmvalidator.addValidation("APV","lt=2100","Il campo Anno Pena Validata deve essere minore di 2100");

        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
<%
    }
  }
%>
</html>