<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date" %>
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
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>

<%@ page import="f3b.log.LogF3B"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="lFlagPage"              scope="request" class="java.lang.String" />


<jsp:useBean id="lCalcoloPenaModel"  scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel" />
<jsp:useBean id="lPenaDiPartenza"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


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

<jsp:useBean id="PenaRideterminata"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="Fungibilita"             scope="request" class="siap.siep.fungibilita.model.FungibilitaModel" />
<jsp:useBean id="PenaGiaEspiata"          scope="request" class="siap.sico.calendar.model.CalendarModel" />

<%
// Informazioni riportate nella seconda sezione della form: pena rideterminata + pena espiata + fungibilità
%>
<jsp:useBean id="FlagAltraCausa"          scope="request" class="java.lang.String" />
<jsp:useBean id="CodPosizioneGiuridica"   scope="request" class="java.lang.String" />
<jsp:useBean id="IdFungibilita"           scope="request" class="java.lang.String" />
<jsp:useBean id="ForzaSysdateManuale"     scope="request" class="java.lang.String" />

<jsp:useBean id="lIdAnnotazioneManualeUltimaInserita" scope="request" class="java.lang.String"/>



<%

Boolean lIsCalcoloPenaAbInitio = (Boolean) request.getAttribute("lIsCalcoloPenaAbInitio");
Boolean lIsSoloImporti = (Boolean) request.getAttribute("lIsSoloImporti");


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
    Rideterminazione pena / Altro
      lFlagPage = ALTRO
   Rideterminazione pena / Avvenuto Pagamento PP
      lFlagPage = ANNOTAPP      
    Decisioni del GE / Applicazione Benefici
      lFlagPage = AMNI   - Aministia
      lFlagPage = DEPEN  - Depenalizzazione
      lFlagPage = INCOST - Incostituzionalità

    Le action di provenienza sono:
    - siap.siep.calcolopena.ActCalcoloPenaGE (old)
    - siap.siep.calcolopena.ActCalcoloPenaComputo

    I dati riportati in maschera dovrebbero consentire all'utente di comprendere
    il calcolo effettuato per cui form è suddivia in tre sezioni contenenti:
    - quantum di partenza
    - quantum computato
    - pena rideterminata
    - pena espiata/da espiare/espiata in eccesso
    - sezione con le date
    - sezione relativa alla fungibilità

    - Benefici Concessi con Provvedimento (A)

    Nel caso dei Computi, non vengono riportati i dati della pena iniziale, ma
    solo i quantum computati e la pena rideterminata.

  */

  //-- Per la gestione periodi nelle Annotazioni Manuali Computo Custodia Cautelare
  List lListaAnnotazioni = new ArrayList();
  boolean lIsComputo = false;
  if(lFlagPage.equals("A") || lFlagPage.equals("S") || lFlagPage.equals("D"))
  {
    lListaAnnotazioni = (List)request.getAttribute("ListaAnnotazioni");
    lIsComputo = true;
  }
  
  boolean lIsComputoAltro = false;
  if(lFlagPage.equals("ALTRO") || lFlagPage.equals("ANNOTAPP"))
  {
    lListaAnnotazioni = (List)request.getAttribute("ListaAnnotazioni");
    lIsComputoAltro = true;
  }

  Date DataInizioPena     = PenaRideterminata.getDataInizio();
  Date DataFineReclusione = PenaRideterminata.getDataFineReclusione();
  Date DataInizioArresto  = PenaRideterminata.getDataInizioArresto();
  Date DataFinePena       = PenaRideterminata.getDataFine();


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
	siesLogger.debug("dataSistemaPerCalcoli = "+DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd") );


  BigDecimal IdEvento = (BigDecimal)request.getAttribute("IdEvento");


  CalendarUtil lCalendarUtil = new CalendarUtil();
  boolean lFinePenaManuale = false;

%>
<%@page import="f3b.log.LogF3B;"%>
<html>
  <!--  VediNuovoCalcoloPenaNew -->

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
// La visualizzazione si differenzia se trattasi di Computi, o Richieste/Decisioni del GE
// - Computi: vengono visualizzati solo i computi concessi con provvedimento
// - Richieste/Decisioni
//   - Pena di partenza (ultima validata o calcolata)
//   - quantum richiesti/concessi con provvedimneto
//==============================================================================
%>
    <table>
<%
    if(!lIsComputo)
    {
    //======================================================================
    // Richiesta o Concessioni benefici (amnistia, indulto, depenalizzazione
    // , incostituzionalità)
    // In questo caso visualizzo:
    //  - la pena complessiva di partenza
    //  - i quantum richiesti/revocati
    //======================================================================
%>
      <%
      //====================================================================
      //                        PENA IN DECORRENZA
      //====================================================================
      %>
      <tr>
        <% if ( ultimaPenaValidata.getDataInizio()!=null ){ %>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua da Espiare al <%=DateUtils.getDateToString(ultimaPenaValidata.getDataInizio(),"dd-MM-yyyy") %></font></td>
        <% } else { %>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua da Espiare</font></td>
        <% } %>
      </tr>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumAnniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumMesiReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumGiorniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaDiPartenza.getImportoMulta())%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumAnniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumMesiArresto(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaDiPartenza.getNumGiorniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaDiPartenza.getImportoAmmenda())%></font></td>
      </tr>


      <%
      //====================================================================
      //                 BENEFICI CONCESSI IN SENTENZA
      //====================================================================

      //====================================================================
      //                 BENEFICI REVOCATI IN SENTENZA
      //====================================================================

      //====================================================================
      //                 MISURE CAUTELARI COMPUTABILI
      //====================================================================

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
          <% if (lFlagPage.equals("GE")) { %>
          <td class="Titolo" colspan=9><font class="label">Benefici Concessi con Richiesta</font></td>
          <% } else if (lIsComputoAltro)  {%>
          <td class="Titolo" colspan=9><font class="label">Quantum Concessi con Provvedimento</font></td>
          <% } else { %>
          <td class="Titolo" colspan=9><font class="label">Benefici Concessi con Provvedimento</font></td>
          <% } %>
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
          <% if (lFlagPage.equals("GE")) { %>
          <td class="Titolo" colspan=9><font class="label">Benefici Revocati con Richiesta</font></td>
          <% } else if (lIsComputoAltro)  {%>
          <td class="Titolo" colspan=9><font class="label">Quantum Revocati con Provvedimento</font></td>
          <% } else {%>
          <td class="Titolo" colspan=9><font class="label">Benefici Revocati con Provvedimento</font></td>
          <% } %>
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
      //========================================================================
      // E' un computo (A,D,S) ed ho le annotazioni, il calcolo viene fatto
      // sulle annotazioni
      //========================================================================
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
          <td class="L">Pari a:
            Anni
            <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"-")%></font>
            Mesi
            <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"-")%></font>
            Giorni
            <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"-")%></font>
          </td>
        </tr>
      <%
      }// end for
    }%>

  </table>

<%
//==============================================================================
// FINE SEZIONE CON I QUANTUM DI PARTENZA
//==============================================================================
%>

<%
//==============================================================================
//                        SEZIONE CON I QUANTUM RIDETERMINATI
// Dati visualizzati:
// - i quantum di pena rideterminati (in rosso se negativi e pena in decorrenza)
// - se pena in espiazione con data inizio < datasistemapercalcoli
//   - la pena già espiata (passata sulla request: PenaGiaEspiata)
//   - la pena espiata in eccesso (passata sulla request: Fungibilita)
//   - la pena da espiare (calcolata al volo tra la data fine pena e la data di systema
//==============================================================================
%>
  <br>
  <table>
    <tr>
      <td class="Titolo" colspan="9"><font class="label">Pena Rideterminata</font></td>
    </tr>
      <%
      if (   DataInizioPena!=null
          && !lCalendarUtil.isPositiveTime(PenaRideterminata.getQuantumReclusione()) // <0
          && !lCalendarUtil.isPositiveTime(PenaRideterminata.getQuantumArresto())    // <0
         )
      { // Quantum negativi visualizzo i dati evidenziandoli in rosso
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaRideterminata.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaRideterminata.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaRideterminata.getImportoAmmenda())%></font></td>
        </tr>
      <%
      }
      else
      {
        // Quantum nulli o positivi: visualizzo i dati senza evidenziarli in rosso
        // n.b. potrebbero essere <0 ma con pena non in decorrenza
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaRideterminata.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaRideterminata.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaRideterminata.getImportoAmmenda())%></font></td>
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

    <% if (   PenaRideterminata.getDataInizio()!=null
           && DateUtils.isGreater(dataSistemaPerCalcoli, PenaRideterminata.getDataInizio())
           && !lIsSoloImporti.booleanValue()
           )
    { %>
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
            if ( !lCalendarUtil.isZero(Fungibilita.getQuantumFungibilita() ) ) {
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

           <%
           // Calcolo la pena residua come intervallo tra la data di calcolo e
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
           lReclusioneRidet = PenaRideterminata.getQuantumReclusione();
           lArrestiRidet    = PenaRideterminata.getQuantumArresto();


           if (   DataFinePena!=null
               && lCalendarUtil.isPositiveTime(lReclusioneRidet)
               && lCalendarUtil.isPositiveTime(lArrestiRidet)
               && DateUtils.isLower  (DataInizioPena,dataSistemaPerCalcoli)
               && !DateUtils.isLower (DataFinePena, dataSistemaPerCalcoli) // data fine deve essere >= data scarcerazione
              )
           {

             CalendarModel lCalPenaDaEspiare = new CalendarModel();


             lCalPenaDaEspiare.setDataInizio ( dataSistemaPerCalcoli);
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
    <%
    } // end sezione di visualizzazione: pena espiata, pena espiata in eccesso, pena da espiare
    %>

<%
//==============================================================================
//             Sezione di visualizzazione delle date di decorrenza
// Vengono visualizzati
//==============================================================================
%>

    <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciNuovaPenaValidata">
      <input type="HIDDEN" name="IdPenaResidua"         value="<%=PenaRideterminata.getIdPenaResidua()%>">
      <input type="HIDDEN" name="lFlagPage"             value="<%=lFlagPage%>">
      <input type="HIDDEN" name="FlagAltraCausa"        value="<%=FlagAltraCausa%>">
      <input type="HIDDEN" name="CodPosizioneGiuridica" value="<%=CodPosizioneGiuridica%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(IdEvento)%>">
      <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=lIdAnnotazioneManualeUltimaInserita%>">

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

      <input type="HIDDEN" name="Grec"  value="<%=PenaRideterminata.getNumGiorniReclusione()%>">
      <input type="HIDDEN" name="Mrec"  value="<%=PenaRideterminata.getNumMesiReclusione()%>">
      <input type="HIDDEN" name="Arec"  value="<%=PenaRideterminata.getNumAnniReclusione()%>">
      <input type="HIDDEN" name="Multa" value="<%=PenaRideterminata.getImportoMulta()%>">

      <input type="HIDDEN" name="Garr"    value="<%=PenaRideterminata.getNumGiorniArresto()%>">
      <input type="HIDDEN" name="Marr"    value="<%=PenaRideterminata.getNumMesiArresto()%>">
      <input type="HIDDEN" name="Aarr"    value="<%=PenaRideterminata.getNumAnniArresto()%>">
      <input type="HIDDEN" name="Ammenda" value="<%=PenaRideterminata.getImportoAmmenda()%>">

      <input type="HIDDEN" name="FlagPenaSospesa" value="<%=StringUtils.toStringJSP(PenaRideterminata.getFlagPenaSospesa())%>">

      <table>
    <%
    //==========================================================================
    // Sezione con le date di decorrenza e l'eventuale trattamento della
    // fungibilità
    // Viene visualizzata SOLO se la pena è in decorrenza (DataInizioPena!=null)
    //
    //==========================================================================
    if ( DataInizioPena!=null ) {
    %>
      <tr>
        <td class="l">Data Decorrenza Pena: </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <%

        if ( DataFineReclusione!=null || DataInizioArresto!=null ) {
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
          <td class="l"><font class="label">Data Fine Reclusione : </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineReclusione,"dd-MM-yyyy"))%>
            </font>
          </td>
          <% } %>
      </tr>

      <tr>
          <%
          if (DataInizioArresto!=null) { %>
          <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioArresto,"dd-MM-yyyy"))%>
            </font>
          </td>
          <%
          }
        } // fine if date intermedie
        %>


        <% // DataFinePena potrebbe essere a null se quantum <0

    //==========================================================================
    // Sezione con la data fine pena e il trattamento della fungibilità.
    // DataFinePena è pari a:
    // - null se la pena non è in decorrenza (inizio pena = null)
    // - fine pena calcolata se quantum >0 e pena in espiazione (anche se presente fungibilità)
    // - la stessa di partenza se quantum < 0
    // - la data inizio pena se quantum = 0
    //==========================================================================
        if (DataFinePena!=null)
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
    //==========================================================================
    // Aggiungo i giorni di LA computati sui calcoli (se presenti)
    //
    //==========================================================================
    if (lCalcoloPenaModel!=null && lCalcoloPenaModel.getLiberazioneAnticipata()>0)
    {
    %>
    <tr>
      <td class="l" colspan="9">
        <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
        <font class="campo"><%=lCalcoloPenaModel.getLiberazioneAnticipata()%></font>
      </td>
    </tr>
    <%
    }
    %>
<%
//==============================================================================
// Sezione contenente l'eventuale fungibilità con richiesta di comportamento
// Ho fungibilità se:
// - quantum rideterminati =0 e pena in espiazione
// - quantum rideterminati <0 e pena in espiazione
// - quantum rideterminati >0 ma data fine pena < data di scarcerazione ( e quindi< pena già espiata)
//
// n.b. se quantum rideterminati <0 ma pena non in espiazione, non viene
//      visualizzato nulla
//
// ATTENZIONE questa sezione viene visualizzata solo se DataInizioPena!=null
//            (pena in decorrenza) e DataFinePena!=null
//==============================================================================
      // Nuova sezione (02-10-2006) inserita per gestire la fungibilità, viene
      // data all'operatore la possibilità di considerare o meno la fungibilità
      //

      CalendarModel lReclusioneRidet = new CalendarModel();
      CalendarModel lArrestiRidet    = new CalendarModel();

      // Reclusione/Arresto
      lReclusioneRidet = PenaRideterminata.getQuantumReclusione();
      lArrestiRidet    = PenaRideterminata.getQuantumArresto();
      

      if (   (   !lCalendarUtil.isPositiveTime(lReclusioneRidet)
              || !lCalendarUtil.isPositiveTime(lArrestiRidet)
             )
          && ( // pena in decorrenza con dataInizio < data scarcerazione (non pena futura)
               Integer.parseInt(DateUtils.getDateToString(DataInizioPena,"yyyyMMdd")) <
               Integer.parseInt(DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd"))
             )
         )
      {
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug(" sono dentro ");
      
        //======================================================================
        // Quantum rideterminati negativi. In questo caso ho una fungibilità
        // data dalla somma del quantum già espiato + il quantum negativo
        // calcolato. La data fine pena in questo caso è quella di partenza
        // (ultima pena validata) poichè non è stato possibile ricalcolarla
        //======================================================================
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
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="1" checked >Data <%=dataDiCalcolo%> con fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data Fine Pena di Partenza senza fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="3">Data <%=dataDiCalcolo%> senza fungibilità
          </td>
        </tr>
      <%
      }
      //========================================================================
      // data fine pena calcolata < data scarcerazione, il soggetto dovrebbe
      // già essere stato liberato, in questo caso la fungibilità calcolata è
      // pari alla differenza tra le date
      //========================================================================
      else if (    DateUtils.isGreater(dataSistemaPerCalcoli,DataFinePena)
                && !lIsSoloImporti.booleanValue()
              )
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
                                       scegliere come si vuole procedere per la Data Fine Pena e la Fungibilità: </font>
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="1" checked >Data <%=dataDiCalcolo%> con fungibilità
          </td>
        </tr>
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSistemaPerCalcoli,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="3">Data <%=dataDiCalcolo%> senza fungibilità
          </td>
        </tr>        
        <tr>
          <td class="l" colspan="3"><font  class="label">Data Fine Pena : </font>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data fine pena calcolata senza fungibilità
          </td>
        </tr>

      <%
      }
      else
      { // DataFinePena>=Data per i Calcoli
        if (!lIsSoloImporti.booleanValue())
        {
          lFinePenaManuale = true;
      %>
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
          <input type="hidden" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(DataFinePena)%>">
          <input type="hidden" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(DataFinePena)%>">
          <input type="hidden" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(DataFinePena)%>">
      <%
        }
       }
//   } //fine if DataFinePena<dataSistemaPerCalcoli


    } // fine DataFinePena!=null
  } //  DataInizioPena!=null
//}
%>
      <tr>
      	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" name="IdFungibilita" value="<%=IdFungibilita%>"--%>
        <input type="HIDDEN" name="IdFungibilita" value="<%=Fungibilita.getIdFungibilita()%>">
        <td class="l"colspan=4>
          <INPUT class="bottone" type="submit" name="conferma" value="Conferma">
        </td>
      </tr>
    </table>
    </form>
  </body>

<%
//==============================================================================
// Script inseriti solo se ho delle date di decorrenza e sto visualizzando la
// sezione con la data manuale. In questo caso devo effettuare i controlli su
// tale data inserita dall'utente
//==============================================================================
    if ( lFinePenaManuale && !lIsSoloImporti.booleanValue())
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
%>
</html>