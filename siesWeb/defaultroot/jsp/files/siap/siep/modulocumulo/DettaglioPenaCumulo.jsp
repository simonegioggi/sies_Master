<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaRideterminataCumulo"%>

<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.siep.fungibilita.model.FungibilitaModel"%>

<%@ page import="siap.sico.util.CalendarUtil" %>


<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel "%>

<jsp:useBean id="IstruttoriaCumulo"        scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>


<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>

<jsp:useBean id="CalcoloPenaModel" scope="request" class="siap.siep.calcolopena.model.CalcoloPenaModel"/>
<jsp:useBean id="PenaDaEspiare"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="TotaleComputi"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="ComputiConcessiReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="ComputiConcessiArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="ComputiRevocatiReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="ComputiRevocatiArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel"/>


<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">  
    function eseguiSubmit (azione) {
      var action = "";
      
      if (azione=='submit') {      
        if (Verify()){
          //action = "siap.siep.modulocumulo.action.ActLoadInserisciProvvedimentoCumulo";
          action = "siap.siep.modulocumulo.action.ActAggiornaFinePenaCumulo";
          
          document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
          document.formName.submit();
        }
      }
      else if (azione=='avanti'){
        action = "siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo";
      
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.formName.submit();
      }
    }
    
    function Verify () {
      // Data Emissione
      var data_to_verify =     document.formName.<%=ICostantiPenaRideterminataCumulo.CAMPO_GIORNO_DATA_FINE%>.value
                          +'/'+document.formName.<%=ICostantiPenaRideterminataCumulo.CAMPO_MESE_DATA_FINE%>.value
                          +'/'+document.formName.<%=ICostantiPenaRideterminataCumulo.CAMPO_ANNO_DATA_FINE%>.value;
      if (!ControllaData(data_to_verify)){
        alert ('Data Fine Pena Manuale non corretta');      
        document.formName.<%=ICostantiPenaRideterminataCumulo.CAMPO_GIORNO_DATA_FINE%>.focus();
        return false;
      }
      
      return true; 
    
    
    }
    
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
        <font class="campo">Dati Finali Cumulo - Pena Da Eseguire</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>
  
  
<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="<%=modalita%>">
  

  <%
  //============================================================================
  PenaRideterminataCumuloModel lPenaRideterminataCumulo = new PenaRideterminataCumuloModel(); 
  
  if (datiFinaliAggregatoModel.getPenaRideterminataCumulo()!=null) {
    lPenaRideterminataCumulo = datiFinaliAggregatoModel.getPenaRideterminataCumulo();  
  }
  
  PenaResiduaModel lPenaResiduaIniziale = CalcoloPenaModel.getPenaResiduaManuale();
  FungibilitaModel lFungModel           = CalcoloPenaModel.getFungibilitaCalcolata();
  CalendarModel    lPenaGiaEspiata      = CalcoloPenaModel.getPenaEspiata();    
    
  
  %>
  <table>
    <tr>
      <td class="Titolo" colspan=9><font  class="label">Pena Residua da Espiare</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Reclusione / Multa :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumGiorniReclusione())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaIniziale.getImportoMulta()) %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Arresto / Ammenda :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lPenaResiduaIniziale.getNumGiorniArresto())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaResiduaIniziale.getImportoAmmenda()) %></font></td>
    </tr>
    
    <%
    //====================================================================
    //          BENEFICI CONCESSI CON RICHIESTA ANTICIPAZIONE
    //====================================================================
    boolean isComputiPresenti = false;
    CalendarUtil lCalendarUtil = new CalendarUtil();
    if ( !(   lCalendarUtil.isZero (ComputiConcessiReclusione)
           && ComputiConcessiReclusione.getImportoMulta()==0
           && lCalendarUtil.isZero (ComputiConcessiArresto)
           && ComputiConcessiArresto.getImportoAmmenda()==0))
    {
      isComputiPresenti = true;
    %>
    <tr>
      <td class="Titolo" colspan="9"><font class="label">Richieste al GE con Anticipazione - Benefici Concessi</font></td>
    </tr>
      <%
      if (!(lCalendarUtil.isZero(ComputiConcessiReclusione) && ComputiConcessiReclusione.getImportoMulta()==0 ))
      {
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiReclusione.getNumAnni()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiReclusione.getNumMesi()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiReclusione.getNumGiorni()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(ComputiConcessiReclusione.getImportoMulta()))%></font></td>
        </tr>
      <%
      }

      if (!( lCalendarUtil.isZero(ComputiConcessiArresto) && ComputiConcessiArresto.getImportoAmmenda()==0))
      {
      %>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiArresto.getNumAnni()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiArresto.getNumMesi()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="l"><font class="campo"><%=ComputiConcessiArresto.getNumGiorni()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(ComputiConcessiArresto.getImportoAmmenda())) %></font></td>
        </tr>
      <%
      }
    }
    %>
    
    
    <%
    //====================================================================
    //              BENEFICI REVOCATI CON ANTICIPAZIONE
    //====================================================================
    if (! (   lCalendarUtil.isZero (ComputiRevocatiReclusione)
           && ComputiRevocatiReclusione.getImportoMulta()==0
           && lCalendarUtil.isZero (ComputiRevocatiArresto)
           && ComputiRevocatiArresto.getImportoAmmenda()==0
          )
       )
    {
      isComputiPresenti = true;
    %>
      <tr>
        <td class="Titolo" colspan=9><font  class="label">Richieste al GE con Anticipazione - </font><font class="cRosso">Benefici Revocati</font></td>
      </tr>
      <%
      if (!(lCalendarUtil.isZero (ComputiRevocatiReclusione) && ComputiRevocatiReclusione.getImportoMulta()==0 ))
      {
      %>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiReclusione.getNumAnni()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiReclusione.getNumMesi()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiReclusione.getNumGiorni()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(ComputiRevocatiReclusione.getImportoMulta()))%></font></td>
        </tr>
      <%
      }

      if (!( lCalendarUtil.isZero(ComputiRevocatiArresto) && ComputiRevocatiArresto.getImportoAmmenda()==0))
      {
      %>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiArresto.getNumAnni()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiArresto.getNumMesi()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="l"><font class="campo"><%=ComputiRevocatiArresto.getNumGiorni()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(ComputiRevocatiArresto.getImportoAmmenda()))%></font></td>
        </tr>
      <%
      }
      %>
    <% } // end if computi %>    
    
    <% 
    // Visualizzo la pena rideterminata solo se presenti computi, ovvero pena 
    // ricalcolata a seguito dei computi
    
    if ( isComputiPresenti) 
    {
    %>    
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td class="Titolo" colspan="9"><font class="label">Pena Rideterminata</font></td>
    </tr>
      
    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumGiorniReclusione())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaDaEspiare.getImportoMulta()) %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Arresto / Ammenda :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaDaEspiare.getNumGiorniArresto())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaDaEspiare.getImportoAmmenda()) %></font></td>
    </tr>
    <% } %>
  </table>
  
  <%
  //==========================================================================
  // Aggiungo i giorni di LA computati sui calcoli (se presenti)
  //
  //==========================================================================
  if (   CalcoloPenaModel!=null 
      && (   CalcoloPenaModel.getLiberazioneAnticipata()>0
          || CalcoloPenaModel.getTotaleScomputi()!=0
         )
     )
  {
  %>
  <br>
  <table>
    <%
      String lDescGiorniDetratti = "da detrarre";
      if (PenaDaEspiare.getDataInizio()!=null && !lPenaResiduaIniziale.isErgastolo() )
        lDescGiorniDetratti = "già detratta";
    %>
      <% if (lPenaRideterminataCumulo.getNumeroGiorniLA()!=null) {%>
      <tr>
        <td class="L" colspan="1">
          <font class="label">Liberazione Anticipata Concessa <%=lDescGiorniDetratti%> in giorni:</font>&nbsp;
        </td>
        <td class="L" colspan="1">
          <font class="campo"><%=lPenaRideterminataCumulo.getNumeroGiorniLA()%></font>
        </td>
      </tr>
      <% } %>
      <% if (lPenaRideterminataCumulo.getNumeroGiorniLS()!=null) {%>
      <tr>
        <td class="L" colspan="1">
          <font class="label">Liberazione Anticipata Speciale Concessa <%=lDescGiorniDetratti%> in giorni:</font>&nbsp;
        </td>
        <td class="L" colspan="1">
          <font class="campo"><%=lPenaRideterminataCumulo.getNumeroGiorniLS()%></font>
        </td>
      </tr>
      <% } %>
      <% if (lPenaRideterminataCumulo.getNumeroGiorniLI()!=null) {%>
      <tr>
        <td class="L" colspan="1">
          <font class="label">Integrazione Liberazione Anticipata Concessa <%=lDescGiorniDetratti%> in giorni:</font>&nbsp;
        </td>
        <td class="L" colspan="1">
          <font class="campo"><%=lPenaRideterminataCumulo.getNumeroGiorniLI()%></font>
        </td>
      </tr>
      <% } %>
      <% if (lPenaRideterminataCumulo.getNumeroGiorniRiduzione()!=null) {%>
      <tr>
        <td class="L" colspan="1">
          <font class="label">Riduzione pena per risarcimento danni Concessa <%=lDescGiorniDetratti%> in giorni:</font>&nbsp;
        </td>
        <td class="L" colspan="1">
          <font class="campo"><%=lPenaRideterminataCumulo.getNumeroGiorniRiduzione()%></font>
        </td>
      </tr>    
      <% } %>
      <% if (lPenaRideterminataCumulo.getNumeroGiorniScomputo()!=null) {%>
      <tr>
        <td class="L" colspan="1">
          <font class="label">Giorni di permesso da Scomputare:</font>&nbsp;
        </td>
        <td class="L" colspan="1">
          <font class="campo"><%=lPenaRideterminataCumulo.getNumeroGiorniScomputo()%></font>
        </td>
      </tr>    
      <% } %>
  </table>
<% } %>  


    <%
    //==========================================================================
    // Sezione con le date di decorrenza e l'eventuale trattamento della
    // fungibilità
    // Viene visualizzata SOLO se la pena è in decorrenza (DataInizioPena!=null)
    //
    //==========================================================================
    Date DataInizioPena     = PenaDaEspiare.getDataInizio();
    Date DataFineReclusione = PenaDaEspiare.getDataFineReclusione();
    Date DataInizioArresto  = PenaDaEspiare.getDataInizioArresto();
    Date DataFinePena       = PenaDaEspiare.getDataFine();
    %>

    <%
    if ( DataInizioPena!=null && !lPenaResiduaIniziale.isErgastolo()) 
    {
    %>
  <br>
    <table>
      <tr>
        <td class="l">Data Decorrenza Pena: </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <%
        if ( DataFineReclusione!=null || DataInizioArresto!=null ) 
        {
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
        // - la stessa di partenza se quantum < 0 da verificare
        // - la data inizio pena se quantum = 0
        //==========================================================================
        if (DataFinePena!=null)
        {
        %>
    <% if (IstruttoriaCumulo.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA) ){ %>
        <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
    <% } else { %>
        <td class="l"><font  class="label">Data Fine Pena : </font></td>
    <% } %>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <% } %>
    </tr>

    <% if (IstruttoriaCumulo.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA) ){ %>
    <tr>
      <td class="l"colspan=4><font  class="label">Data Fine Pena Manuale : </font>
        <input type="text" maxlength="2" size="2" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd")) %>" 
               name="<%=ICostantiPenaRideterminataCumulo.CAMPO_GIORNO_DATA_FINE%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input type="text" maxlength="2" size="2"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"MM")) %>" 
               name="<%=ICostantiPenaRideterminataCumulo.CAMPO_MESE_DATA_FINE%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        
        /
        <input type="text" maxlength="4" size="4" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"yyyy")) %>" 
               name="<%=ICostantiPenaRideterminataCumulo.CAMPO_ANNO_DATA_FINE%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>  
    <% } %>
  </table>
  <% } %>

  <%
  if (lPenaResiduaIniziale.isErgastolo()) 
  {
  %>
  <br>
    <table>
      <tr>
        <td class="titolo" colspan="4">Pena da eseguire</td>
      </tr>

      <tr>
        <td class="L">Ergastolo:</td>
        <td class="L" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getDescrTipoPenaDetentiva()) %></font>&nbsp;
        </td>
      </tr> 
      
      <% if ("04".equals(lPenaRideterminataCumulo.getCodTipoPenaDetentiva())) { %>
      <tr>
        <td class="L">Durata Isolamento Diurno:</td>
        <td class="L" colspan="3">
           Anni <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniIsolamentoDiurno(),"-") %></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiIsolamentoDiurno(),"-") %></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniIsolamentoDiurno(),"-") %></font>
         </td>
      </tr>
      <% } %>

      <% if (DataInizioPena!=null) { %>
      <tr>
        <td class="l">Data Decorrenza Pena: </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
          </font>
        </td>
        <td class="l"><font  class="label">Data Fine Pena: </font></td>
        <td class="l">
          <font class="cRosso">MAI</font>
        </td>
      </tr>
      <% } %>
    </table>    
  <% } %>

<table width="800px">
  <tr>
    <td>
      <% 
      if (   IstruttoriaCumulo.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)
          && DataInizioPena!=null && !lPenaResiduaIniziale.isErgastolo()
         ) 
      { 
      %>
      <input type="button" class="bottone" name="Conferma" value="Conferma" onClick="eseguiSubmit('submit')">
      <% } else { %>
      <input type="button" class="bottone" name="Avanti" value="Avanti" onClick="eseguiSubmit('avanti')">
      <% } %>
      &nbsp;
    </td>
  </tr>
</table>

</form>
</div>
</body>