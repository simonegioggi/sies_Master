<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>


<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>


<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>


<jsp:useBean id="ufficioPM"             scope="request" class="java.lang.String"/>
<jsp:useBean id="richiesta"             scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"               scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="residenza"             scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"            scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"           scope="request" class="java.util.Vector"/>

<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>


<jsp:useBean id="modalita"         scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"           scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="dataTrasmissioneStr" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioPM"    scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"        scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeAltroDest"    scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoUDS"                   scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"                 scope="request" class="java.lang.String"/>
<jsp:useBean id="istitutoDetenzioneDest"    scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>



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
    




String lMessaggio = null;
if (   listaMisure.size()==0 
    && (penacumulo.getMisuraSicurezza()==null || penacumulo.getMisuraSicurezza().length()==0)
   ) 
{
  lMessaggio = "Attenzione!! Sul procedimento non sono iscritte Misure di Sicurezza da eseguire";
}
else if (listaMisure.size()>0){
  boolean isDatiMisureIncompleti = false;
  Iterator lIterMis = listaMisure.iterator();
  while (lIterMis.hasNext())
  {
    MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
    if (  "-".equals(lMisSicu.getCodTipo()) )
    {
      isDatiMisureIncompleti = true;
    }
  }
  if (isDatiMisureIncompleti)
    lMessaggio = "Attenzione!! Verificare i dati delle Misure di Sicurezza iscritte sul Procedimento. Il Tipo misura non è specificato. Si suggerisce di provvedere alla correzione prima di trasmettere il procedimento.";
}
    
    
    
     
%>

<html>
<head>
<title>[S.I.E.S.] - TRASMISSIONE PER COMPETENZA</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
 
  function clearDropDown (selField){
    while (selField.options.length > 0)
    selField.options[0] = null;
  }

  function caricatuttecombo(){
    //caricaCombo(strOggetto,';','#',document.LoadInserisciTrasmissioneCompetenza.< %=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciTrasmissioneCompetenza.< %= ICostantiEvento.CAMPO_COD_MOTIVO%>);
  }

  function Verifica(){
    if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.value=="-" )
    {
      alert("Indicare l'Ufficio del Pubblico Ministero destinatario degli atti");  
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.focus()  ;
      return false;
    }
    else if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.value=="") {
      alert("Indicare la sede dell'Ufficio del Pubblico Ministero destinatario degli atti");  
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>.focus()  ;
      return false;
    }
  
  
    //CONTROLLO CHE SIA STATO SELEZIONATA LA TIPOLOGIA DELL'ATTO
    if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
      alert("E' obbligatorio selezionare la tipologia dell'atto");
      return false;   
    }
    
    //======================
    // Data Emissione
    //======================
    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        
    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
       
    var data_to_verify =     document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Emissione non valida');
      return false;
    }
    
    var dataOdierna = "<%=DateUtils.getSysDate("dd")%>-<%=DateUtils.getSysDate("MM")%>-<%=DateUtils.getSysDate("yyyy")%>";
    if(!CompareDate(data_to_verify,dataOdierna))
    {
      alert('La Data Emissione non può essere superiore alla data odierna');
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
      return false;
    }
    
    //======================
    // Data Trasmissione
    //======================
    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'
       +document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        
    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'
       +document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Trasmissione non valida');
      return false;
    }
    
    if(!CompareDate(data_to_verify,dataOdierna))
    {
      alert('La Data Trasmissione non può essere superiore alla data odierna');
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
      return false;
    }
    //========================
    if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>
      .options[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.selectedIndex]
      .text=='-')
    {
      alert("E' obbligatorio selezionare un valore del campo 'Oggetto Atto'");
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
      return false;   
    }     
  
    if(   document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
       && document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Firmatario è obbligatorio");  
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()  ;
      return false;
    } 
    
    if(   document.LoadInserisciTrasmissioneCompetenza.tipoUDS.value!="-" 
       && document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value=="")
    {
      alert("Se selezionato, indicare la sede del Magistrato di sorveglianza");  
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.focus()  ;
      return false;
    } 
    
    return true;
  }


  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3){
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }
  
  function ListaComuni(a_formname,a_fieldname){
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    var desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }
  
  function pulisciIstituto (nomeCampoComune, nomeCampoId){
    var campoDescr = document.getElementsByName(nomeCampoComune)[0];
    var campoId    = document.getElementsByName(nomeCampoId)[0];
    campoDescr.value="";
    campoId.value="";
  }
  
  
  function clearComuneSorv(){
    if (document.LoadInserisciTrasmissioneCompetenza.tipoUDS.value=="-"){
      document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value=""  ;
    }
  }
  
  function clearSedeALtroDest(){
    if (document.LoadInserisciTrasmissioneCompetenza.AltroDestinatario.value=="-"){
      document.LoadInserisciTrasmissioneCompetenza.SedeAltroDestinatario.value=""  ;
    }
  }
  
  function VisualizzaMessaggio() {
  <% if (lMessaggio!=null) { %>
    alert ('<%=lMessaggio%>');
  <% } %>    
  }
  
  
  </script>
</head>

<body class="corpo" onLoad="javascript:caricatuttecombo();VisualizzaMessaggio();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">TRASMISSIONE PER COMPETENZA ESECUZIONE MISURE DI SICUREZZA</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/ --%>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  

<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Residenza attuale
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile (?) o meno)
// - Misure di sicurezza: in sentenza o in cumulo
//==============================================================================
%>

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
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
      // Se Detenuto altra causa: Istituto di detenzione o Altro Luogo
      if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        if( lAltraCausa.getIstitutoDetenzione()!= null ) { %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
              <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
               di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
            </td>
          </tr>

          <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
          <tr>
            <td class="l">Altro Luogo </td>
            <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
          <% }
        } // fine istituto di detenzione
      }
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null ) { %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
          </td>
        </tr>
      <%}%>


      <%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(   lPosizione.getCodPosizioneGiuridica() != null
           && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
          <% }
        }
      %>
      
      <%
      //========================================================================
      // Residenza se presente
      //========================================================================      
      %>
      <% if (residenza!=null && residenza.getIdResidenza()!=null) { %>
      <tr>
        <td class="l">Residenza</td>
        <td class="L"> <font class="campo" ><%=StringUtils.toStringJSP(residenza.toStringaResidenza(),"&nbsp;")%></font></td>
      </tr>
      <% } %>

      <%
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
           )
        {}
        else
        { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
        <%}%>

        <%
        //===============
        // Arresti
        //===============
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {}
        else
        { %>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
      <% }
      }  // fine IF sulla pena residua
      %>


      <%
      //==========================================================================
      // Inserisco il rigo con Data Inizio e Tipo Ergastolo (se presente)
      //==========================================================================
      %>
      <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
           <td class="l">Data Decorrenza Pena</td>
           <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if (penaresidua.getFlagErgastolo() != null) {
           if(penaresidua.getFlagErgastolo().equals("S")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
           <% }
           else if(penaresidua.getFlagErgastolo().equals("D")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
           <% }
         }
      %>

      <%
      //========================================================================
      // Inserisco la data Fine pena
      // - se non libero o comunque detenuto pre altra causa
      // - se non in ergastolo
      // - se data editabile (se lapena residua recuperata è non validata)
      //   inserisco i campi altrimenti solo label
      //========================================================================
      if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) ) {
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
          %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           </td>
          <% }
          else if( penaresidua.getDataFine() != null) {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
            %>
               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td>
            <%
            }else{%>
               <td class="l">Data Fine Pena</td>
               <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td><%
            }
          }
        }
      } %>
    </tr>
  </table>
  <%
  //===================================================================
  // Misure di sicurezza
  //===================================================================
  %>
  <table>
  <% if (penacumulo!=null && penacumulo.getIdPenaCumulo()!=null) { %>
  <tr><td class="Titolo" colspan="3">Misure Sicurezza in Cumulo</td></tr>
  <tr>
    <td class="l">
      <font class="campo">        
      <%=StringUtils.toStringJSP(penacumulo.getMisuraSicurezza())%>
      </font>
    </td>
  </tr>
  <% } else if (listaMisure != null && listaMisure.size() != 0){ %>
  <tr>
    <td class="Titolo" colspan="3">Misure Sicurezza</td>
  </tr>
  <tr>
    <td class="l">
      <center><font class="label">Natura Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Tipo Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Durata Misura</font></center>
    </td>
  </tr>
      <%
      Iterator lIterMis = listaMisure.iterator();
      while (lIterMis.hasNext())
      {
        MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
      %>
        <tr>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
          </td>
          <td class="l" >
            <% if ("-".equals(lMisSicu.getDescrTipo()) ||  lMisSicu.getDescrTipo()==null ) { %>
            <img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione" border="0" title="Tipo Misura non Specificato. Provvedere alla correzione.">
            <% } else { %>
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
            <% } %>
          </td>
          <td class="l">            
              <font class="l">AA:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;</font>
              <font class="l">MM:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;</font>
              <font class="l">GG:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%></font>
            </font>
          </td>
        </tr>
      <% } %>
  <% } %>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciTrasmissioneCompetenza" >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciTrasmissioneCompetenza">
    <input type="HIDDEN" name="modalita" value="<%=modalita%>">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(evento.getIdEvento(),"")%>">

  <%
  //===================================================================
  // 
  //===================================================================
  Date dataEmissione = DateUtils.getSysDate();
  Date dataTrasmissione = DateUtils.getSysDate();
  if (evento.getIdEvento()!=null){
    dataEmissione = evento.getDataEmissione();   
  }
  
  if (dataTrasmissioneStr.length()>0){
    dataTrasmissione = DateUtils.getDate(dataTrasmissioneStr,"dd/MM/yyyy");
  }
  %>
  <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"dd"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"MM"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"yyyy"),"")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
  
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"dd"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"MM"),"")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"yyyy"),"")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>


<%
//==============================================================================
//      Ufficio Competente all'emissione del Provvedimento
//==============================================================================
%>
<table width=90%>
  <tr>
      <td class="Titolo" colspan='8'>Ufficio Competente all'Esecuzione delle Misure</td>
  </tr>
  <tr>
    <td class="l">Ufficio del Pubblico Ministero <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select title="Sede Ufficio Pubblico Ministero"  name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>">
        <%=ufficioPM %>
      </select>
    </td>
  </tr>
  <tr>  
    <td class="l">Luogo <font class=ob>(*)</font></td>
    <td class="L" colspan="3">      
      <input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>"  maxlength="35" size="30"
             value="<%=StringUtils.toStringJSP(sedeUfficioPM,"")%>">
      <a href="Javascript:ListaUfficiComuni('LoadInserisciTrasmissioneCompetenza','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>'
                                           ,document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>
<%
//===================================================================
//    DATI ATTO
//===================================================================
%>
<table width=90%>
  <tr>
    <td class="Titolo" colspan='8'> Dati Atto </td>
  </tr>
  <tr>
    <td class="l" width="25%">Tipologia Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciTrasmissioneCompetenza.<%= ICostantiEvento.CAMPO_COD_MOTIVO%>);" Title="Tipologia Atto" >
        <%=richiesta%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Oggetto Atto <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
        <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
        <option value = "-"  />-
        <%=oggetto%>
        </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Contenuto</td>
    <td  class="L" colspan="3">
      <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ><%=StringUtils.toStringJSP(contenuto,"")%></textarea>
    </td> 
  </tr>
 
  <tr>
    <td class="l" width="15%">Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L" colspan="7">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" 
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciTrasmissioneCompetenza','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>          
    </td>
  </tr>
  
  <tr>
    <td class="l" width=30%>Magistrato di Sorveglianza</td>
    <td class="L">
      <select Title="Magistrato di Sorveglianza" class="small" name="tipoUDS" onchange="javascript:clearComuneSorv();">
      <%=tipoUDS%>
      </select>
      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>" maxlength="35" size="25">
      <a href="Javascript:ListaUfficiComuni('LoadInserisciTrasmissioneCompetenza','<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>'
                                           ,document.LoadInserisciTrasmissioneCompetenza.tipoUDS[document.LoadInserisciTrasmissioneCompetenza.tipoUDS.options.selectedIndex].value);">
         <img src="/images/filefolder.gif" border=0>
      </a> 
    </td>
  </tr>
  
  <% if (   "03".equals(lPosizione.getCodPosizioneGiuridica()) //Detenuto
         || "14".equals(lPosizione.getCodPosizioneGiuridica()) //Semilibero 
         || "06".equals(lPosizione.getCodPosizioneGiuridica()) //Internato
         || "09".equals(lPosizione.getCodPosizioneGiuridica()) //Internato
        ) 
  { %>
  <tr>
    <td class="l" width=30%>Istituto di Detenzione</td>
    <td class="L">
      <% 
      if (    istitutoDetenzioneDest!=null 
           && istitutoDetenzioneDest.getIdIstitutoDetenzione()!=null
           && istitutoDetenzioneDest.getIdIstitutoDetenzione().length()>0
          )
      {
      // Sono in modifica e ho l'istituto selezionato tra i destinatari (notifiche)
      %>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(istitutoDetenzioneDest.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutoDetenzioneDest.getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=istitutoDetenzioneDest.getIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneCompetenza','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a>
      <%
      } else {
      %>  
        <%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null) {%>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneCompetenza','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a>
        <%}else {%>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciTrasmissioneCompetenza','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a>
        <%}%>
      <%}%>
      <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>
    </td>
  </tr>
  <% } %>
  
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L" colspan="7">    
      <select Title="Altro Destinatario" class="small" name="AltroDestinatario" onchange="javascript:clearSedeALtroDest();">
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede</td>   
    <td class="L" colspan="3">
      <input title="Sede Altro Destinatario" type="text" name="SedeAltroDestinatario" maxlength="35" size="35"
             value="<%=StringUtils.toStringJSP(sedeAltroDest,"")%>">
      <a href="Javascript:ListaComuni('LoadInserisciTrasmissioneCompetenza','SedeAltroDestinatario');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td> 
  </tr>  
  <tr>
    <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    </td>
  </tr>
</table>

</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciTrasmissioneCompetenza");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
  
  // DATA TRASMISSIONE
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

</script>
</body>
</html>