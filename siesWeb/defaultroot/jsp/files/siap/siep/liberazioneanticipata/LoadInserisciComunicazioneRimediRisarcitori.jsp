<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione"%>

<% // Dati del Fascicolo%>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<% // flagErgastolo = N,S,D %>
<jsp:useBean id="flagErgastolo"       scope="request" class="java.lang.String"/>


<% // Dati del Provvedimento della Sorveglianza   %>
<jsp:useBean id="EventoSIUS"          scope="request" class="siap.sico.evento.model.EventoModel"/> 
<jsp:useBean id="DepositoDecreto"     scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="DepositoOrdinanzaPc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>  <% // Vector <LicenzaPeriodiLibAnticipataModel>   %>
<jsp:useBean id="UfficioSIUS"         scope="request" class="siap.sico.ufficio.model.UfficioModel"/> 


<% // Destinatari  %>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaN"     scope="request" class="java.lang.String"/>


<%
//==============================================================================
// Form x l'inserimento della Comunicazione per soggetto Libero, in Ergastolo
// o con pena già espiata (fone pena<data sistema)
//==============================================================================
%>


<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>


<%
//===========================================================
// Totale giorni concessi e relativi periodi
//===========================================================
BigDecimal lNumeroGiorniRiduzione = null;
String lSommaLiquidata = null;


String lStrPeriodi_RD_C = "";
String lStrPeriodi_SL_C = "";
String lStrPeriodi_RD_R = "";
String lStrPeriodi_RD_I = "";
String lStrPeriodi_RD_N = "";

Iterator itx = LicenzePeriodi.iterator();
while (itx.hasNext())
{
  LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
  
  if( "RD".equals(lLicPer.getLicenza().getCodTipoLicenza())){
    if ("C".equals(lLicPer.getLicenza().getFlagConcesso())){
      lNumeroGiorniRiduzione = lLicPer.getLicenza().getNumeroGiorni();
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_C += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
    else if ("R".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_R += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
    else if ("I".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_I += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }   
    }
    else if ("N".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_N += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
  }
  else if( "SL".equals(lLicPer.getLicenza().getCodTipoLicenza())){
    lSommaLiquidata = StringUtils.toEuroFormat(lLicPer.getLicenza().getSommaRisarcDanni());

    PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
    for (int i=0;i<lPeriodi.length;i++){
      lStrPeriodi_SL_C += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy") +" - "+ 
                          DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy")+"; ";
    }
  }
}
%>


<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;

    function ListaComuni(a_formname,a_fieldname) {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname)  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function ListaMagistrati(a_formname)  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    
    function ListaComuniTds(formname,fieldname){
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUDS(a_formname,a_fieldname) {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function Verify()
    {
      var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
    
      // Data Emissione
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
      else if (CompareDate(data_to_verify,dataOdierna)== false) {
        alert ("La Data Emissione non può essere una data futura");
        document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;          
      }

      // Data Trasmissione
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazioneLiberoLoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      data_to_verify = document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di trasmissione non valida');
        document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
        return false;
      }
      else if (CompareDate(data_to_verify,dataOdierna)== false) {
        alert ("La Data Trasmissione non può essere una data futura");
        document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
        return false;          
      }

      // Magistrato
      if(document.LoadInserisciComunicazioneLibero.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Selezionare il Magistrato firmatario");
        return false;
      }
      
      // Destinatari Tutti obbligatori
      // Autorità di destinazione
      if (typeof (document.LoadInserisciComunicazioneLibero.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>)!="undefined") {
        if(document.LoadInserisciComunicazioneLibero.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
        {
          alert("Autorità Destinazione obbligatoria");
          document.LoadInserisciComunicazioneLibero.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
          return false;
        }
      }
      
      // Istituto di detenzione
      if (typeof (document.LoadInserisciComunicazioneLibero.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>)!="undefined") {
        if(document.LoadInserisciComunicazioneLibero.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
        {
          alert("Istituto di Detenzione obbligatorio");
          document.LoadInserisciComunicazioneLibero.Comune.focus();
          return false;
        }
      }
      
      // TDS
      if (typeof (document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>)!="undefined") {
        if(document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>.value == "")
        {
          alert("Tribunale di Sorveglianza obbligatorio");
          document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>.focus();
          return false;
        }
      }
      
      // MDS
      if (typeof (document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>)!="undefined") {
        if(document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>.value == "")
        {
          alert("Ufficio di Sorveglianza obbligatorio");
          document.LoadInserisciComunicazioneLibero.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>.focus();
          return false;
        }
      }
      
      
      return true;
    }


  </script>
</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Comunicazione A Seguito Provvedimento Rimedi Risarcitori D.L. 92/2014 Per condannato <% if ("N".equals(flagErgastolo)) {%>Libero<% } else { %>in Ergastolo<% } %></font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  
  
  <FORM method="POST" name="LoadInserisciComunicazioneLibero" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActInserisciComunicazioneRimediRisarcitori">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(EventoSIUS.getIdEvento())%>">
    <input type="HIDDEN" name="isErgastolo" value="<%=("N".equals(flagErgastolo))?"N":"S"%>">
    <input type="HIDDEN" name="isLibero" value="<%=(lPosizione.isLibero())?"S":"N"%>">

<%
//==============================================================================
// Sezione con i dati della posizione giuridica e pena residua
//==============================================================================
%>
<%
//============================================================================
// POSIZIONE GIURIDICA
// LUOGO DI DETENZIONE
//============================================================================
%>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
            DETENUTO PER ALTRA CAUSA
        <% } else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
      </td>
    </tr>
    
    <%
    // Luogo di detenzione
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa()))
    {  //Detenuto Altra Causa
       if( lAltraCausa.getIstitutoDetenzione()!= null )
       {
       %>
       <tr>
         <td class="l">Detenuto presso </td>
         <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
         </td>
       </tr>       
       <%
       }
               
       if (lAltraCausa.getAltroLuogo()!=null)
       {
       %>
        <tr>
          <td class="l">Altro Luogo </td >
          <td class="L" colspan=5>
            <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
          </td>
        </tr>
        <%
       }
     }
     else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
     { // detenuto questa causa
     %>
      <tr>
       <td class="l">Detenuto presso </td>
       <td class="L" colspan=5>
        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
    <% } %>


    <%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(   lPosizione.getCodPosizioneGiuridica() != null 
       && (   lPosizione.getCodPosizioneGiuridica().equals("02") 
           || lPosizione.getCodPosizioneGiuridica().equals("04")
          ) 
      )
    {
        if(lLuogoDetenzione.getIstitutoDetenzione() != null)
        {
        %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
        <%
          }
    }
    %>
</table>


<% // PENA RESIDUA %>

<% if(  !penaresidua.isErgastolo()  )  { %>
<table>
  <% if ( !penaresidua.isQuantumReclusioneZero() ) {%>
  <tr>
    <td class="l">Reclusione</td>
    <td class="l" colspan=2>
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
    </td>
    <td class="l">Multa</td>
    <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
   </tr>
  <% } %>
   
   
  <% if (!penaresidua.isQuantumArrestoZero()) {%>
  <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
  </tr>
  <% } %>
</table>
<% } %>

<%
// Decorrenza Scadenza
%>
<table>
  <tr>
    <% if (penaresidua.getDataInizio() != null) { %>
    <td class="l">Data Decorrenza Pena</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
    <% } %>
  
    <% 
    if (penaresidua.getFlagErgastolo() != null) 
    {
      if(penaresidua.getFlagErgastolo().equals("S")) 
      {
      %>
         <td class="l">Pena Detentiva</td>
         <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
      <% } else if(penaresidua.getFlagErgastolo().equals("D")) { %>
         <td class="l">Pena Detentiva</td>
         <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
      <% }
    }
    %>    
    
    <%
    if ( !penaresidua.isErgastolo() && penaresidua.getDataFine()!=null)
    {
      if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
      {
      %>
        <td class="l">Data Fine Pena</td>
        <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
      <% } else { %>
        <td class="l">Data Fine Pena</td>
        <td class="lRosso" >
          <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
      <%
      }
    }
    %>
  </tr>
</table>

<%
//==============================================================================
// Sezione con i dati della posizione giuridica e pena residua
//==============================================================================
%>
  <table>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L" colspan=2>
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
  </table>
  
 
<%
//==============================================================================
//           Sezione con i dati del provvedimento della Sorveglianza
//==============================================================================
%> 

<%
LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel)LicenzePeriodi.firstElement();
LicenzaLibAnticipataModel lLicenzaModel = lLicenzaPeriodiModel.getLicenza();
%>
  <table>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan="4"> Dati <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %> </td>
    </tr>
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(lLicenzaModel.getAnnoSius())%> /<%=StringUtils.toStringJSP(lLicenzaModel.getNumeroSius())%>
        </font>
      </td>
      <td class="l"> Anno / Numero <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %> </td>
      <td class="l">
        <%
        String lAnnoProvvedimento = "";
        String lNumeroProvvedimento = "";
        
        if (DepositoDecreto!=null && DepositoDecreto.getIdDepositoDecreto()!=null){
          lAnnoProvvedimento   = ""+DepositoDecreto.getAnnoS72();
          lNumeroProvvedimento = ""+DepositoDecreto.getNumS72();
        }
        else if (DepositoOrdinanzaPc!=null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc()!=null){
          lAnnoProvvedimento   = ""+DepositoOrdinanzaPc.getAnnoS3();
          lNumeroProvvedimento = ""+DepositoOrdinanzaPc.getNumS3();
        }
        %>
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnoProvvedimento)%>/<%=StringUtils.toStringJSP(lNumeroProvvedimento)%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"> Autorità emittente </td>
      <td class="l" colspan="3">
        <font class="campo">
          <%=StringUtils.toStringJSP(EventoSIUS.getDescrUfficioEmittente())%>&nbsp;
        </font>
        di
        <font class="campo">
          <%=StringUtils.toStringJSP(EventoSIUS.getDescrLuogoEmittente())%>
        </font>
      </td>
    </tr>
    <%
      Date lDataEmissione = null;
      if (DepositoDecreto!=null && DepositoDecreto.getIdDepositoDecreto()!=null){
        lDataEmissione = DepositoDecreto.getDataEmissione();
      }
      else if (DepositoOrdinanzaPc!=null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc()!=null){
        lDataEmissione = DepositoOrdinanzaPc.getDataCameraConsiglio();
      }
    %>
    <% if(lDataEmissione != null) { %>
    <tr>
      <td class="l" height="20" >Data Emissione <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %></td>
      <td class="l" colspan="3">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataEmissione,"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <% } %>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="L" nowrap> Totale giorni riduzione pena concessi: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lNumeroGiorniRiduzione,"")%></font></td>
    </tr>
    <tr>
      <td class="L" nowrap> Periodi valutati per riduzione pena: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_C,"")%> </font></td>
    </tr>
    
    
    <% if (lSommaLiquidata!=null && lSommaLiquidata.length()>0) { %>
    <tr>
      <td class="L" nowrap> Somma liquidata a titolo risarcimento danno: &euro; </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lSommaLiquidata,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    
    <% if (lStrPeriodi_SL_C.length()>0) { %>
    <tr>
      <td class="L"> Periodi valutati per liquidazione somma: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_SL_C,"&nbsp;")%> </font></td>
    </tr>
    <% } %>

    <% if (lStrPeriodi_RD_R.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi Rigettati: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_R,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    <% if (lStrPeriodi_RD_I.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi Inammissibili: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_I,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    <% if (lStrPeriodi_RD_N.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi N.L.P./N.D.P.: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_N,"&nbsp;")%> </font></td>
    </tr>
    <% } %>


  </table>
  
<%
//==============================================================================
//                    Sezione con Magistrato e destinatari
// - Autorità per l'esecuzione (se libero o pena scaduta)
// - Istituto di detenzione + Aut di sorveglianza che ha emesso il provvedimanto )se Ergastolo)
//==============================================================================
%> 
  <br>
  <table style="width: 95%;">
    <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
    <tr>
      <td class="l">Magistrato <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciComunicazioneLibero');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
    </tr>
  </table>

  <table style="width: 95%;">
    <%
    if ("N".equals(flagErgastolo)) 
    { 
      //==================================
      // Libero o pena scaduta
      // - Destinatario per Esecuzione
      //==================================
    %>
      <tr><td class="Titolo" colspan=6>Destinatario per Esecuzione</td></tr>
      <tr>
        <td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font></td>
        <td class="L" colspan="3">
          <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
            <%=autoritaEsternaE%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede <font class=ob>(*)</font></td>
        <td class="L">
          <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=30></textarea>
        </td>
      </tr>
    
    <% 
    } else { 
      //=============================
      // ERGASTOLO:
      // - Istituto di detenzione
      // - TDS o UDS
      //=============================
    %>
      <tr><td class="Titolo" colspan=6>Destinatari</td></tr>
      <tr>
        <td class="l">Istituto di Detenzione <font class=ob>(*)</font></td>
        <% if(   lLuogoDetenzione != null 
              && lLuogoDetenzione.getIstitutoDetenzione()!= null 
              && lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()!= null )
          {
        %>
        <td class="l" colspan="4">
          <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size="50">
          <input type="hidden"  name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstitutoDetenzione().getIdIstitutoDetenzione()%>" >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciComunicazioneLibero','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <% } else { %>
        <td class="l" colspan="4">
          <input readonly Title="Istituto" name="Comune" value="" size="50">
          <input type="hidden"  name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciComunicazioneLibero','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <% } %>
      </tr>
    
      <% if( "TDS".equals(UfficioSIUS.getCodTipoUfficio()) ) { %>
      <tr>
        <td class="L">Tribunale di Sorveglianza <font class=ob>(*)</font>
          <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
          <input type="hidden" name="tds" value="S">
        </td>
        <td class="L" colspan="3">
          <input type="text" title="Sede Tribunale Sorveglianza" maxlength="35" size="35"
                 value="<%=StringUtils.toStringJSP(UfficioSIUS.getDescrComune(),"")%>" 
                 name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>"  >
          <a href="Javascript:ListaComuniTds('LoadInserisciComunicazioneLibero','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <% } else { %>
      <tr>
          <td class="L">Ufficio di Sorveglianza <font class=ob>(*)</font>
            <input type="hidden" value="UDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>">
            <input type="hidden" name="uds" value="S">
          </td>
          <td class="L" colspan="3">
            <input type="text" title="Sede Ufficio Sorveglianza" maxlength="35" size="35"
                   value="<%=StringUtils.toStringJSP(UfficioSIUS.getDescrComune(),"")%>"  
                   name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>"  >
            <a href="Javascript:ListaUDS('LoadInserisciComunicazioneLibero','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
      </tr>
      <% } %>
   <% } %>
      

    <tr><td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
          <tr>
            <td class="l" colspan=4>Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </td>
          </tr>
          <tr>
            <td class="l">Autorità Destinazione </td >
            <td class="L" colspan=3>
              <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
                <%=autoritaEsternaN%>
              </select>
            </td>
          </tr>
          <tr>
            <td class="l">Sede </td><td class="L">
              <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
              <% if(avvocati.size() > 1){ %>
                <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
                              <img src="/images/filefolder.gif" border=0>
            </a>
              <% } else { %>
                <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
                              <img src="/images/filefolder.gif" border=0>
            </a>
              <% } %>
          </td>
          <td class="l">Note</td>
          <td class="L">
            <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35></textarea>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>

<%
      lIdxAvv++;
    }
%>
    <tr>
      <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciComunicazioneLibero");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
  </script>
</body>
</html>