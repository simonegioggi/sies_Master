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
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.sollecitoesitotrasmissione.action.ICostantiSollecitoEsitoTrasmissione"%>


<%// [DATI PER ORA NON UTILIZZATI] %>
<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="dataeditabile"         scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="residenza"             scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"            scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"           scope="request" class="java.util.Vector"/>

<%// Ufficio a cui inviare il sollecito %>
<jsp:useBean id="ufficioSollecito"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioInoltrante"     scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="oggettoProvvedimento"  scope="request" class="siap.sico.decodifiche.model.DecodificheModel"/>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>



<jsp:useBean id="idMessaggioDett"       scope="request" class="java.lang.String"/>
<jsp:useBean id="messaggioRich"         scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="messaggioInoltro"      scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="modalita"      scope="request" class="java.lang.String"/>

<%// In caso di modifica %>
<jsp:useBean id="contenuto"            scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="autoritaEsternaNSede" scope="request" class="java.lang.String"/>

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
    //caricaCombo(strOggetto,';','#',document.LoadInserisciSollecitoEsitoTrasmissione.< %=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value,document.LoadInserisciSollecitoEsitoTrasmissione.< %= ICostantiEvento.CAMPO_COD_MOTIVO%>);
  }

  function Verifica(){
 
    //======================
    // Data Emissione
    //======================
    if (document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        
    if (document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'
       +document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
       
    var data_to_verify =     document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                        +'-'+document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Emissione non valida');
      return false;
    }
    
    //======================
    // Data Trasmissione
    //======================
    if (document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'
       +document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        
    if (document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'
       +document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
                    +'-'+document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
                    +'-'+document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify) ){
      alert('Data di Trasmissione non valida');
      return false;
    }
    
    //======================== 
    if(document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
       && document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
      alert("Il  Magistrato Firmatario è obbligatorio");  
      document.LoadInserisciSollecitoEsitoTrasmissione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()  ;
      return false;
    } 
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
  
  function clearSedeALtroDest(){
    if (document.LoadInserisciSollecitoEsitoTrasmissione.AltroDestinatario.value=="-"){
      document.LoadInserisciSollecitoEsitoTrasmissione.SedeAltroDestinatario.value=""  ;
    }
  }

  </script>
</head>

<body class="corpo" onLoad="javascript:caricatuttecombo()">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">SOLLECITO RISCONTRO TRASMISSIONE PER COMPETENZA ESECUZIONE MISURE DI SICUREZZA</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAttoTrasmesso&IdMessaggio=<%=idMessaggioDett%>">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      --%>
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
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
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
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
--%>
  <%
  //===================================================================
  // 
  //===================================================================
  %>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSollecitoEsitoTrasmissione" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciSollecitoEsitoTrasmissione">
  
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_SOLLECITATO%>"    value="<%=messaggioRich.getIdMessaggio()%>">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_TIPO_OPERAZIONE%>" value="<%=messaggioRich.getCodTipoOperazione()%>">

  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=oggettoProvvedimento.getCode()%>">

  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(ufficioSollecito.getCodTipoUfficio(),"")%>">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(ufficioSollecito.getDescrComune(),"")%>">
  
  <%// %>
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_COD_UFF_INOLTRANTE%>"       value="<%=StringUtils.toStringJSP(messaggioInoltro.getCodUfficioMittente(),"")%>">
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_GIORNO_DATA_INOLTRO%>"      value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioInoltro.getDataInvio(),"dd"),"")%>">
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_MESE_DATA_INOLTRO%>"        value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioInoltro.getDataInvio(),"MM"),"")%>">
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_ANNO_DATA_INOLTRO%>"        value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(messaggioInoltro.getDataInvio(),"yyyy"),"")%>">
  <input type="HIDDEN" name="<%=ICostantiSollecitoEsitoTrasmissione.CAMPO_MES_ID_MESSAGGIO_INOLTRO%>" value="<%=StringUtils.toStringJSP(messaggioInoltro.getIdMessaggio(),"")%>">
  
  
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(evento.getIdEvento(),"")%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  <%
  Date dataEmissione = DateUtils.getSysDate();
  Date dataTrasmissione = DateUtils.getSysDate();
  if (evento.getIdEvento()!=null){
    dataEmissione = evento.getDataEmissione();   
    dataTrasmissione = evento.getDataTrasmissioneAtti();
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
    <td class="Titolo" colspan="2">Sollecito Riscontro</td>
  </tr>
  <tr>
    <td class="l">A Ufficio del Pubblico Ministero</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(ufficioSollecito.getDescrTipoUfficio(),"")%> di <%=StringUtils.toStringJSP(ufficioSollecito.getDescrComune(),"")%></font></td>
  </tr>


  <tr>
    <td class="l">Oggetto</td>
    <td class="L"><font class="campo"><%=oggettoProvvedimento.getDescription()%></td>
  </tr>
  
  <tr>
    <td class="l">Contenuto</td>
    <td  class="L" >
      <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=7 ><%=StringUtils.toStringJSP(contenuto,"")%></TEXTAREA>
    </td> 
  </tr>
 
  <tr>
    <td class="l" >Magistrato Firmatario <font class=ob>(*)</font></td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" 
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    
                      value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" 
                      type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciSollecitoEsitoTrasmissione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>          
    </td>
  </tr>
  
  <tr>
    <td class="l">Altro Destinatario</td>
    <td class="L">    
      <select Title="Altro Destinatario" class="small" name="AltroDestinatario" onchange="javascript:clearSedeALtroDest();">
      <%=autoritaEsternaN%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Sede</td>   
    <td class="L">
      <input type="text" title="Sede Altro Destinatario"  name="SedeAltroDestinatario" maxlength="35" size="35" 
             value="<%=StringUtils.toStringJSP(autoritaEsternaNSede,"")%>">
      <a href="Javascript:ListaComuni('LoadInserisciSollecitoEsitoTrasmissione','SedeAltroDestinatario');">
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

  var frmvalidator  = new Validator("LoadInserisciSollecitoEsitoTrasmissione");
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