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

<%@ page import="siap.siep.fungibilita.model.FungibilitaModel" %>


<%@ page import="f3b.log.LogF3B" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="lCalcoloPenaModel"   scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel" />
<jsp:useBean id="lPenaDiPartenza"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="lPenaRideterminata"  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


<jsp:useBean id="lGiorniScomputati"   scope="request" class="java.lang.String" />
<jsp:useBean id="lTotLAAttuale"       scope="request" class="java.lang.String" />
<jsp:useBean id="lTotScomputiAttuali" scope="request" class="java.lang.String" />
<jsp:useBean id="lTotGiorniRDConcessi"  scope="request" class="java.lang.String"/>

<jsp:useBean id="lTotAttualeLA"       scope="request" class="java.lang.String" />
<jsp:useBean id="lTotAttualeLS"       scope="request" class="java.lang.String" />
<jsp:useBean id="lTotAttualeLI"       scope="request" class="java.lang.String" />
<jsp:useBean id="lTotScomputiAttualiLA" scope="request" class="java.lang.String" />
<jsp:useBean id="lTotScomputiAttualiLS" scope="request" class="java.lang.String" />
<jsp:useBean id="lTotScomputiAttualiLI" scope="request" class="java.lang.String" />
<jsp:useBean id="lGiorniScomputatiLA"   scope="request" class="java.lang.String" />
<jsp:useBean id="lGiorniScomputatiLS"   scope="request" class="java.lang.String" />
<jsp:useBean id="lGiorniScomputatiLI"   scope="request" class="java.lang.String" />


<jsp:useBean id="IdEventoStr" scope="request" class="java.lang.String" />
  


<%
// Informazioni riportate nella prima sezione della form: pena di partenza + quantum aggregati
%>

<jsp:useBean id="ForzaSysdateManuale"     scope="request" class="java.lang.String" />

<%

  /*
    Questa maschera serve per mostrare il dettaglio della pena rideterminata
    dopo le operazioni di calcolo legate a uno scomputo permesso/Licenza o 
    ridimensionamento LA.

    Le action di provenienza sono:
    - siap.siep.libertaanticipata.action.ActCalcoloPenaScompRidimLA

    I dati riportati in maschera dovrebbero consentire all'utente di comprendere
    il calcolo effettuato per cui form è suddivia in tre sezioni contenenti:
    - quantum di partenza
    - Giorni computati
    - pena rideterminata
    - pena espiata/da espiare/espiata in eccesso
    - sezione con le date
    - sezione relativa alla fungibilità

  */

  //-- Per la gestione periodi nelle Annotazioni Manuali Computo Custodia Cautelare





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
<html>
  <!--  VediCalcoloPenaScomputiRidimLA -->

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
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Rideterminazione Fine Pena per Ridimensionamento LA</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>


<%
//==============================================================================
//         SEZIONE PER LA VISUALIZZAZIONE DEI QUANTUM DI PARTENZA
// - Pena di partenza (ultima validata o calcolata)
// - giorni scomputati
//==============================================================================
%>
    <table>
      <%
      //====================================================================
      //                        PENA IN DECORRENZA
      //====================================================================
      %>
      <tr>
        <% if ( lPenaDiPartenza.getDataInizio()!=null ){ %>
        <td class="Titolo" colspan=9><font  class="label">Pena Residua da Espiare al <%=DateUtils.getDateToString(lPenaDiPartenza.getDataInizio(),"dd-MM-yyyy") %></font></td>
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
</table>
  <br>
  
<table>   
      <% if (!lTotLAAttuale.equals("0")) { %>
      <tr>
        <td class="l">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=lTotLAAttuale%></font>
        </td>
      </tr>
      <% } %>
      
<!--  20/05/2014  Nuova L.A.  DL 146  : 
          L.A. "ordinaria"  -->
   <% if (!lTotAttualeLS.equals("0") || !lTotAttualeLI.equals("0"))
      { %>
        <tr>
      <td class="L">&nbsp;
        <font class="label"> ( di cui : </font>&nbsp;
      
   <%   if(!lTotAttualeLA.equals("0") )
      { %>
        <font class="campo"><%= StringUtils.toStringJSP(lTotAttualeLA)%></font>
        <font class="label">di Liberazione Anticipata ;&nbsp;</font>&nbsp;
    <%  } 
       
      if(!lTotAttualeLS.equals("0"))
      { %>
        <font class="campo"><%= StringUtils.toStringJSP(lTotAttualeLS)%></font>
        <font class="label">di Liberazione Anticipata Speciale;&nbsp;</font>&nbsp;
  <%    } 
       
      if(!lTotAttualeLI.equals("0"))
      { %>
        <font class="campo"><%= StringUtils.toStringJSP(lTotAttualeLI)%></font>
        <font class="label">di Integrazione Liberazione Anticipata;&nbsp;</font>&nbsp;
  <%    } %>
    
      <font class="label"> ) </font>
      </td>
      </tr>
   <%} %> 

<!--    End DL 146  -->
          
      <% if (!lTotScomputiAttuali.equals("0")) { %>
      <tr>
        <td class="l">
          <font class="label">Giorni di Licenza/Permesso già scomputati:</font>
          <font class="campo"><%=Math.abs( Integer.parseInt(lTotScomputiAttuali))%></font>
        </td>
      </tr>
      <% } %>
      
     <% if (!lTotGiorniRDConcessi.equals("0")) { %>
      <tr>
        <td class="l">
          <font class="label">Giorni di Risarcimento D.L. 92/2014 già detratti:</font>
          <font class="campo"><%=Math.abs( Integer.parseInt(lTotGiorniRDConcessi))%></font>
        </td>
      </tr>
     <% } %>
     
    <br>  
      <% if ( lPenaDiPartenza.getDataInizio()!=null ) 
      { %>
      <tr>      
        <td>
          <table>
            <tr>
              <td class="l"><font class="label">Data Decorrenza Pena: </font></td>
              <td class="l">
                <font class="campo">
                  <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDiPartenza.getDataInizio(),"dd-MM-yyyy"))%>
                </font>
              </td>
              <%
      
              if ( lPenaDiPartenza.getDataFineReclusione()!=null || lPenaDiPartenza.getDataInizioArresto()!=null ) 
              {
                  if (lPenaDiPartenza.getDataFineReclusione()!=null)
                  { %>
                  <td class="l"><font class="label">Data Fine Reclusione : </font></td>
                  <td class="l">
                    <font class="campo">
                      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDiPartenza.getDataFineReclusione(),"dd-MM-yyyy"))%>
                    </font>
                  </td>
                <%  } %>
            </tr>
      
            <tr>
                <%if (lPenaDiPartenza.getDataInizioArresto()!=null)
                { %>
                  <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
                  <td class="l">
                    <font class="campo">
                      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDiPartenza.getDataInizioArresto(),"dd-MM-yyyy"))%>
                    </font>
                  </td>
                  <%
                }
              } // fine if date intermedie
              %>
      
              <td class="l"><font  class="label">Data Fine Pena: </font></td>
              <td class="l">
                <font class="campo">
                  <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaDiPartenza.getDataFine(),"dd-MM-yyyy"))%>
                </font>
              </td>
            </tr> 
          </table>    
        </td>
      </tr>
      <% } %> 
      
    
</table>         
<br>

  <table>
    <tr>
      <td class="Titolo" colspan="100%"> Totale giorni scomputati con provvedimento</td>
    </tr>
    <tr>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="l"><font class="campo"><%=lGiorniScomputati%></font>&nbsp;</td>

<%  if(!lGiorniScomputatiLS.equals("0")  || !lGiorniScomputatiLI.equals("0") )
  {
  %>    
      <td class="L">&nbsp;
        <font class="label"> ( di cui : </font>&nbsp;
      
 <%   if(!lGiorniScomputatiLA.equals("0") )
    { %>
      <font class="campo"><%= StringUtils.toStringJSP(lGiorniScomputatiLA)%></font>
      <font class="label">di Liberazione Anticipata ;&nbsp;</font>&nbsp;
  <%  } 
     
    if(!lGiorniScomputatiLS.equals("0"))
    { %>
      <font class="campo"><%= StringUtils.toStringJSP(lGiorniScomputatiLS)%></font>
      <font class="label">di Liberazione Anticipata Speciale;&nbsp;</font>&nbsp;
<%    } 
     
    if(!lGiorniScomputatiLI.equals("0"))
    { %>
      <font class="campo"><%= StringUtils.toStringJSP(lGiorniScomputatiLI)%></font>
      <font class="label">di Integrazione Liberazione Anticipata;&nbsp;</font>&nbsp;
<%    } %>
    
     <font class="label"> ) </font> 
      </td>
<%  } %>
  </tr> 
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
// - LA e scomputi rideterminati
// - Decorrenza Scadenza
//==============================================================================
%>
    <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciFinePenaRidimLA">
      
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IdEventoStr%>">


  <table>
    <tr>
      <td class="Titolo" colspan="9"><font class="label">Pena Rideterminata</font></td>
    </tr>
<%
//==============================================================================
//             Sezione di visualizzazione delle date di decorrenza
// Vengono visualizzati
//==============================================================================
//PenaResiduaModel lNuovaPenaResidua = lCalcoloPenaModel.
    Date DataInizioPena = lPenaRideterminata.getDataInizio();

    Date DataFinePena   = lPenaRideterminata.getDataFine(); 
    // n.b. lPenaRideterminata è la pena ottenuta semplicemente abFine
%>

    <%
    //==========================================================================
    // Sezione con le date di decorrenza e l'eventuale trattamento della
    // fungibilità
    // Viene visualizzata SOLO se la pena è in decorrenza (DataInizioPena!=null)
    //
    //==========================================================================
    
    if ( lPenaRideterminata.getDataInizio()!=null ) {
    %>
      <tr>
        <td class="l">Data Decorrenza Pena: </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <%
        
        Date DataFineIsolamentoDiurno = lPenaRideterminata.getDataFineIsolamentoDiurno();
        Date DataFinePresunta         = lPenaRideterminata.getDataFinePresunta();
        Date DataFineReclusione       = lPenaRideterminata.getDataFineReclusione();
        Date DataFineSS               = lPenaRideterminata.getDataFineSS();

    
        Date DataInizioArresto          = lPenaRideterminata.getDataInizioArresto();
        Date DataInizioIsolamentoDiurno = lPenaRideterminata.getDataInizioIsolamentoDiurno();
        Date DataInizioSS               = lPenaRideterminata.getDataInizioSS();

    
        if ( DataFineReclusione!=null || DataInizioArresto!=null ) 
        {
          if (DataFineReclusione!=null) { %>
          <input type="HIDDEN" name="Gdatafinereclusione" value="<%=DateUtils.getDayToString   (DataFineReclusione)%>">
          <input type="HIDDEN" name="Mdatafinereclusione" value="<%=DateUtils.getMonthToString (DataFineReclusione)%>">
          <input type="HIDDEN" name="Adatafinereclusione" value="<%=DateUtils.getYearToString  (DataFineReclusione)%>">
          <% } %>

          <% if (lPenaRideterminata.getDataInizioArresto()!=null){ %>
          <input type="HIDDEN" name="Gdatainizioarresto" value="<%=DateUtils.getDayToString   (DataInizioArresto)%>">
          <input type="HIDDEN" name="Mdatainizioarresto" value="<%=DateUtils.getMonthToString (DataInizioArresto)%>">
          <input type="HIDDEN" name="Adatainizioarresto" value="<%=DateUtils.getYearToString  (DataInizioArresto)%>">
          <% } 
          %>

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
</table>


<table>
    <%
    //==========================================================================
    // Aggiungo i giorni di LA computati sui calcoli (se presenti)
    //
    //==========================================================================
    int residuoLA=0;
    int residuoLAS=0;
    int residuoLAI=0;
  
    if (lCalcoloPenaModel!=null && lCalcoloPenaModel.getLiberazioneAnticipata()>0)
    {
      int residuo=lCalcoloPenaModel.getLiberazioneAnticipata();
      // residuo-=Integer.parseInt(lGiorniScomputati);
    %>
  
      <tr>
        <td class="l" colspan="4">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=residuo%></font>
        </td>
      </tr>

      <%  
      residuoLA = lCalcoloPenaModel.getTipoLiberazioneAnticipata("LA");
      residuoLAS = lCalcoloPenaModel.getTipoLiberazioneAnticipata("LS");
      residuoLAI = lCalcoloPenaModel.getTipoLiberazioneAnticipata("LI");
  
      if(residuoLAS > 0  || residuoLAI > 0 )
      {
      %>
      <tr>
        <td class="L" colspan="4">&nbsp;
          <font class="label"> ( di cui : </font>&nbsp;   
    
          <% if(residuoLA > 0) { %>
          <font class="campo"><%= StringUtils.toStringJSP(residuoLA)%></font>
          <font class="label">di Liberazione Anticipata ;&nbsp;</font>&nbsp;
          <% } %>
     
          <% if(residuoLAS > 0) { %>
            <font class="campo"><%= StringUtils.toStringJSP(residuoLAS)%></font>
            <font class="label">di Liberazione Anticipata Speciale;&nbsp;</font>&nbsp;
         <% } %>
   
          <% if(residuoLAI > 0) { %>
            <font class="campo"><%= StringUtils.toStringJSP(residuoLAI)%></font>
            <font class="label">di Integrazione Liberazione Anticipata;&nbsp;</font>&nbsp;
          <% } %>
      
          <font class="campo"> ) </font>
        </td>
      </tr>     
    
      <% }  %>
  

  <% } %>

   <% if (!lTotGiorniRDConcessi.equals("0")) { %>
    <tr>
      <td class="l" colspan="4">
        <font class="label">Giorni di Risarcimento D.L. 92/2014 già detratti:</font>
        <font class="campo"><%=Math.abs( Integer.parseInt(lTotGiorniRDConcessi))%></font>
      </td>
    </tr>
   <% } %>

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
      lReclusioneRidet = lPenaRideterminata.getQuantumReclusione();
      lArrestiRidet    = lPenaRideterminata.getQuantumArresto();
      
      %>
      
      <% if (lPenaRideterminata.getDataInizio()!=null) { %>
      <input type="HIDDEN" name="Gdatainiziopena" value="<%=DateUtils.getDayToString(lPenaRideterminata.getDataInizio())%>">
      <input type="HIDDEN" name="Mdatainiziopena" value="<%=DateUtils.getMonthToString(lPenaRideterminata.getDataInizio())%>">
      <input type="HIDDEN" name="Adatainiziopena" value="<%=DateUtils.getYearToString(lPenaRideterminata.getDataInizio())%>">
      <% } %>

      <% if (lPenaRideterminata.getDataFine()!=null) { %>
      <input type="HIDDEN" name="Gdatafinepenapresunta" value="<%=DateUtils.getDayToString(lPenaRideterminata.getDataFine())%>">
      <input type="HIDDEN" name="Mdatafinepenapresunta" value="<%=DateUtils.getMonthToString(lPenaRideterminata.getDataFine())%>">
      <input type="HIDDEN" name="Adatafinepenapresunta" value="<%=DateUtils.getYearToString(lPenaRideterminata.getDataFine())%>">
      <% } %>

      <input type="HIDDEN" name="Grec"  value="<%=lPenaRideterminata.getNumGiorniReclusione()%>">
      <input type="HIDDEN" name="Mrec"  value="<%=lPenaRideterminata.getNumMesiReclusione()%>">
      <input type="HIDDEN" name="Arec"  value="<%=lPenaRideterminata.getNumAnniReclusione()%>">
      <input type="HIDDEN" name="Multa" value="<%=lPenaRideterminata.getImportoMulta()%>">

      <input type="HIDDEN" name="Garr"    value="<%=lPenaRideterminata.getNumGiorniArresto()%>">
      <input type="HIDDEN" name="Marr"    value="<%=lPenaRideterminata.getNumMesiArresto()%>">
      <input type="HIDDEN" name="Aarr"    value="<%=lPenaRideterminata.getNumAnniArresto()%>">
      <input type="HIDDEN" name="Ammenda" value="<%=lPenaRideterminata.getImportoAmmenda()%>">

      <input type="HIDDEN" name="FlagPenaSospesa" value="<%=StringUtils.toStringJSP(lPenaRideterminata.getFlagPenaSospesa())%>">
      
      
      <input type="HIDDEN" name="FlagAltraCausa" value="">
      
      <%
      
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(" Quantum di Reclusione -------------> "+ lReclusioneRidet);
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(" Quantum di Arresto    -------------> "+ lArrestiRidet);

      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(" DataInizioPena -------------> "+ Integer.parseInt(DateUtils.getDateToString(DataInizioPena,"yyyyMMdd")));
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug(" dataSistema    -------------> "+ Integer.parseInt(DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd")));
      

      
      // d.f. 01/06/2016 quantum negativi in caso di rideterminazione/revoca LA 
      //                 è difficile viene rideterminato solo il fine pena e mai
      //                 i quantum
%>
<%--
// PER ORA COMMENTATA
      if (   (   !lCalendarUtil.isPositiveTime(lReclusioneRidet)
              || !lCalendarUtil.isPositiveTime(lArrestiRidet)
             )
          && ( // pena in decorrenza con dataInizio < data scarcerazione (non pena futura)
               Integer.parseInt(DateUtils.getDateToString(DataInizioPena,"yyyyMMdd")) <
               Integer.parseInt(DateUtils.getDateToString(dataSistemaPerCalcoli,"yyyyMMdd"))
             )
         )
      {
      
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
        
     <table>   
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
      else if (DateUtils.isGreater(dataSistemaPerCalcoli,DataFinePena))
--%>
<%
      FungibilitaModel lFung = lCalcoloPenaModel.getFungibilitaCalcolata();
      //ogF3B.getLogger().debug("lFung = "+lFung);
      
      if (lFung!=null && lFung.getIdFungibilita()!=null)
      {
      
        //forzata per test alla data 
        //DataFinePena = lCalcoloPenaModel.getPenaResiduaRicalcolata().getDataFine();
        
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
        
        <% // 01/06/2016 aggiunta visualizzazione delle Fungibilità ricalcolata %>
        <tr>
          <td class="l" width="200px">Pena espiata in eccesso</td>
          <td class="l"><font class="label">Anni   </font><font color="red"> <%=StringUtils.toStringJSP(lFung.getNumAnni(),"0")%></font></td>
          <td class="l"><font class="label">Mesi   </font><font color="red"> <%=StringUtils.toStringJSP(lFung.getNumMesi(),"0")%></font></td>
          <td class="l"><font class="label">Giorni </font><font color="red"> <%=StringUtils.toStringJSP(lFung.getNumGiorni(),"0")%></font></td>
        </tr>        
        
        
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
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </td>
          <td class="l">
            <input type="radio" name="flagFungibilita" value="2">Data fine pena calcolata senza fungibilità
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
      else
      { // DataFinePena>=Data per i Calcoli
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
       }
//   } //fine if DataFinePena<dataSistemaPerCalcoli


    } // fine DataFinePena!=null
  } //  DataInizioPena!=null
//}
%>
      <tr>

      <% 
      FungibilitaModel lFungibilita = lCalcoloPenaModel.getFungibilitaCalcolata();
      
      if (lFungibilita!=null && lFungibilita.getIdFungibilita()!=null) { %>
      <input type="HIDDEN" name="IdFungibilita" value="<%=StringUtils.toStringJSP(lFungibilita.getIdFungibilita(),"")%>">
      <% } %>
      
        
      <% if (lPenaRideterminata.getDataInizio()!=null) { %>
        <td class="l"colspan=4>
          <INPUT class="bottone" type="submit" name="conferma" value="Conferma">
        </td>
        <%} %>
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
    if ( lFinePenaManuale )
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