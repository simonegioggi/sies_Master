<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>

<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>



<%// Pena  %>
<jsp:useBean id="aPenaConvertita"     scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="aPenaRideterminata"  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="aIdEveAnnotazione" scope="request" class="java.lang.String"/>

<%// Destinatari%>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAltra" scope="request" class="java.lang.String"/>
  
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>



<%
//==============================================================================
// Form per l'inserimento dell'OE per Rideterminazione Pena a seguito di Revoca
// o Conversione di Sanzione Sostitutiva nel caso di Presenza di Cumulo
//
//==============================================================================

boolean visualizzaDataScarcerazione = false;

Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>

<head>
  <title> [S.I.E.S.] - Rideterminazione Pena Revoca Sanzioni Sostitutive - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">

  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  var data_to_verify;
  var obbligatori;
  var dataesiste;
  
  //============================================================================
  //
  //============================================================================
  function Verify()  
  {
    //===================================
    // Data Emissione 
    //===================================
    if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Emissione non valida');
      return false;
    }

    //===================================
    // Data Trasmissione
    //===================================
    if (document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.f.<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Trasmissione non valida');
      return false;
    }


    //==========================================================================
    // Destinatari
    //==========================================================================
    if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il Magistrato Firmatario è obbligatorio");
      document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
      return false;
    }


    return true;
  }

  //========
  //Liste
  //========
  var desktop;
  function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

  function ListaUDS(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  function ListaComuniTds(formname,fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

</script>

</head>

<body class="corpo">
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciRideterminazionePenaRevocaSS">
  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica(),"")%>">
  <input type="hidden" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" value="<%=aIdEveAnnotazione%>">
  
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena a seguito Revoca/Coversione Sanzione Sostitutiva</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua
//==============================================================================
%>
  <table>
    <tr>
      <td class="l"> Posizione Giuridica :
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
        <% } else { %>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
      </font>
    </td>
  </tr>
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio, MAI
// - Se Libero vengono visualizzati i Quantum
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<%
  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
     && PenaComplessiva.getCodTipoPenaDetentiva() != ""
     && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
         || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
        )
    )
  {
%>
    <table style="width: 95%;">
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else 
  {
%>
  <table style="width: 95%;">
    <tr>
      <td class="Titolonocap" colspan="6" width="80%"> Pena residua da espiare </td>
    </tr>
      
    <% 
    CalendarModel lReclusione = aPenaRideterminata.getQuantumReclusione();
    CalendarModel lArresti    = aPenaRideterminata.getQuantumArresto();
    %>
    
    <tr>
      <td class="l"> Reclusione :
        Anni   <font class=campo><%=lReclusione.getNumAnni()%></font>
        Mesi   <font class=campo><%=lReclusione.getNumMesi()%></font>
        Giorni <font class=campo><%=lReclusione.getNumGiorni()%></font>
        Multa  <font class=campo><%=StringUtils.toEuroFormat(aPenaRideterminata.getImportoMulta())%></font>
      </td>
      <td class="l"> Arresto :
        Anni    <font class=campo><%=lArresti.getNumAnni()%></font>
        Mesi    <font class=campo><%=lArresti.getNumMesi()%></font>
        Giorni  <font class=campo><%=lArresti.getNumGiorni()%></font>
        Ammenda <font class=campo><%=StringUtils.toEuroFormat(aPenaRideterminata.getImportoAmmenda())%></font>
      </td>
    </tr>
    <% if (aPenaRideterminata.getDataInizio()!=null) { %>
    <tr>
      <td class=l>Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getDataInizio(),"dd/MM/yyyy"),"")%> </font></td>
      <td class=l>Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaRideterminata.getDataFine(),"dd/MM/yyyy"),"") %> </font></td>
    </tr>
    <% } %>
</table>
<%
  }  // Fine Pena Residua
%>

<%
//==============================================================================
//                       Sezione con i dati del provvedimento
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td colspan=8 class="Titolo">Pena convertita</td>
  </tr>
  <% 
  CalendarModel lReclusionePC = aPenaConvertita.getQuantumReclusione();
  CalendarModel lArrestiPC    = aPenaConvertita.getQuantumArresto();
  %>
    
  <tr>
    <td class="l"> Reclusione :
      Anni   <font class=campo><%=lReclusionePC.getNumAnni()%></font>
      Mesi   <font class=campo><%=lReclusionePC.getNumMesi()%></font>
      Giorni <font class=campo><%=lReclusionePC.getNumGiorni()%></font>
    </td>
    <td class="l"> Arresto :
      Anni    <font class=campo><%=lArrestiPC.getNumAnni()%></font>
      Mesi    <font class=campo><%=lArrestiPC.getNumMesi()%></font>
      Giorni  <font class=campo><%=lArrestiPC.getNumGiorni()%></font>
    </td>
  </tr>
</table>

<table style="width: 95%;">
  <tr>
    <td colspan=8 class="Titolo">Rideterminazione della pena</td>
  </tr>
  
  <tr>
    <td class="l" width="33%">Oggetto :&nbsp;</td>
    <td class="l">Rideterminazione della pena a seguito di conversione da <%%></td>
  </tr>
</table>

<%
//==============================================================================
// Data Emissione e Data Trasmissione
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>

    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input title = "Giorno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
</table>

<%
//==============================================================================
//              Sezione con i destinatari del Provvedimento
// - Magistrato Firmatario
// - Autorità di Destinazione
// - Avvocati
//
// Se posizione giuridica = isMisuraAlternativa() (11,12,13,14,15,25,29,41,42,43,44)
//   oppure
// - 27 = Sospensione Pena ex L. 207/03 (mis_alt)
// - 45 = Sospensione Pena Ex L. 207/03 in Estensione Provvisoria 51 Bis (mis_alt)
// Vanno visualizzati anche:
// - Autorità Competente per il territorio
// - UEPE
// - Magistrato di Sorveglianza
// - Tribunale di Sorveglianza
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>

<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan="8"> Destinatari</td>
  </tr>

<%
//==============================================================================
//    Se in misura alternativa
//==============================================================================
if(   lPosizione.getCodPosizioneGiuridica() != null 
   && (   lPosizione.isMisuraAlternativa()
       || lPosizione.getCodPosizioneGiuridica().equals("27")
       || lPosizione.getCodPosizioneGiuridica().equals("45")
      )
  )
{
%>
  <tr>
    <td class="l">Autorità competente per il territorio <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Autorita Competente"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
      <%=autoritaEsternaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols="30" ></textarea>
    </td>
  </tr>
  
  <tr>
    <td class="l">UEPE <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <input readonly Title="UEPE Competente" name="Indirizzo" value="" size="60" >
      <input type="hidden" Title="UEPE" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size="35" >
      <a href="Javascript:ListaCSSA('f','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>

  <tr>
    <td class="l">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
    <td class="L" colspan=3>
      <input title="ufficio" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="25">
      <a href="Javascript:ListaUDS('f','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
       <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>

  <tr>
    <td class="L">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
    <td class="L" colspan=3>
      <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuniTds('f','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
<%
} else {
%>
  <tr>
    <td class="l">Autorità di Destinazione <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Autorita di Destinazione"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
      <%=autoritaEsternaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita di Destinazione"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols="30" ></textarea>
    </td>
  </tr>
<% } %>

</table>

<%
//==============================================================================
//            Sezione con i Destinatario per Notifica (avvocati)
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <tr>
      <td class="Titolo" colspan="6">Destinatario per Notifica </td>
    </tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table>
          <tr>
            <td class="l">Per Avvocato&nbsp;
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
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
        <table style="width: 95%;">
          <tr>
            <td class="l">Autorità Destinazione </td>
            <td class="L" colspan="3">
              <select Title="Autorita Esterna" class="small" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>" >
              <%=autoritaEsternaAvv%>
              </select>
            </td>
          </tr>
          <tr>
            <td class="l">Sede </td>
            <td class="L">
              <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('f','<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF %>[<%=lIdxAvv%>]');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
            <td class="l">Note</td>
            <td class="L">
              <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30></textarea>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
</table>


<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("f");

    // Data Emissione
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");

    // Data Trasmissione
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>