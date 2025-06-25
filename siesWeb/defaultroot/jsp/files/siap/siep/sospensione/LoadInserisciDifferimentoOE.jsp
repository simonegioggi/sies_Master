<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>

<%
//==============================================================================
// Finestra di visualizzazione del dettaglio del Provvedimento della Sorveglianza
// (differimento) e inserimento dei dati del conseguente Provvedimento dell'Esecuzione.
// Gestisce i seguenti casi
// - Differimento Provvisorio
// - Differimento Definitivo
// - Rigetto Differimento
// - Revoca Differimento
//
// La maschera è composta dalle seguenti sezioni:
// - Dettaglio posizione giuridica e pena residua
// - data emissione e data trasmissione (provvedimento SIEP)
// - Dettaglio Provvedimento della Sorveglianza
// - Dettaglio della Pena Espiata e della Nuova Pena Residua
// - Magistrato competente
// - Destinatari
// - Destinatari per la Notifica
// - Restituzione ordine di esecuzione
// n.b. non tutte le sezioni sono sempre presenti
//==============================================================================
%>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="misuraalternativa"    scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="sospensione"          scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaE"      scope="request" class="java.lang.String"/>

<jsp:useBean id="UfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="flagergastolo"      scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodMotivo"            scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante"   scope="request" class="java.lang.String" />
<jsp:useBean id="nuovapenaresidua"   scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%

//  tipoProvvedimento = "DifferimentoProvv";
//  tipoProvvedimento = "DifferimentoDef";
//  tipoProvvedimento = "DifferimentoRigetto";
//  tipoProvvedimento = "DifferimentoRevoca";

  String dataeditabile = "N";

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

  //============================================================================
  // Determino se bisogna visualizzare solo il dettaglio del provvedimanto
  // della sorveglianza (solo annotazione) o anche la sezione per l'inserimento
  // dei dati del provvedimneto dell'esecuzione
  //============================================================================
  boolean soloAnnotazione = false;
  if (   tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO)
      && !lPosizione.getCodPosizioneGiuridica().equals("17")
     )
  {
    // Nel caso di rigetto solo la posizione 17 (libero in differimento provvisorio)
    // da origine a un provvedimento, altrimenti sola annotazione.
    soloAnnotazione = true;
  }

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sospensione dell'esecuzione della pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      //========================================================================
      // Funzione per il caricamento della lista dei Comuni
      //========================================================================
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      //========================================================================
      // Funzione per il caricamento della lista dei Magistrati
      //========================================================================
      function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      //========================================================================
      // Funzione per il caricamento delle sedi TDS
      //========================================================================
      function ListaComuniTds(formname, fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      //========================================================================
      // Funzione per il caricamento della lista dei Luoghi Detenzione
      //========================================================================
      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

      //========================================================================
      // Funzione per il caricamento della lista dei CSSA
      //========================================================================
	function ListaCSSA(a_formname, a_fieldname, a_field2) {
    	  <%-- MEV10-s3: aggiunto controllo preventivo --%>
    	  var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
		  if (a_typename == "-")  {
			  alert("Selezionare il Destinatario dell'UEPE/USSM");
		  } else {
     		  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename,
     				  "Ricerca_CSSA",
     				  "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    	  }
	}

      //========================================================================
      // Funzione per il caricamento della lista degli Ordini di Esecuzione
      //========================================================================
      function ListaOrdiniEsecuzione(a_formname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActListaOE&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Ordini_Esecuzione", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
      }

      //========================================================================
      // Funzione Visualizzare/Nascondere la sezione (div) per la richiesta di
      // restituzione Ordine di Esecuzione
      //========================================================================
      function VisualizzaOE()
      {
        var nodeOE       = document.getElementById('divOrdineEsecuzione');
        var nodeConferma = document.getElementById('divConferma');

        if(document.LoadInserisciSospensioneDifferimento.<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>.checked == true)
        {
           nodeOE.style.visibility='visible';
           nodeConferma.style.visibility='hidden';
           nodeOE.style.top='-30px';

        }
        else
        {
           nodeOE.style.visibility='hidden';
           nodeConferma.style.visibility='visible';
           nodeOE.style.top='+30px';
        }
      }
      
      //========================================================================
      // Funzione per la cancellazione dell'istituto
      //========================================================================      
      function pulisciIstituto (nomeCampoComune, nomeCampoId){
        var campoDescr = document.getElementsByName(nomeCampoComune)[0];
        var campoId    = document.getElementsByName(nomeCampoId)[0];
        campoDescr.value="";
        campoId.value="";
      }
      
      function delCSSA(a_formName)
      { 
        a_formName.Indirizzo.value="-";
        a_formName.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value="-";
      }


      //========================================================================
      // Funzione per il controllo dei dati prima della submit
      //========================================================================
      function Verify()
      {
        //= DATA EMISSIONE (non obbligatoria)
        var data_to_verify =     document.LoadInserisciSospensioneDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value
                            +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value
                            +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione provvedimento non valida');
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();

          return false;
        }

        //= DATA TRASMISSIONE PROVVEDIMENTO (non obbligatoria)
        var data_to_verify =      document.LoadInserisciSospensioneDifferimento.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value
                             +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value
                             +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data trasmissione provvedimento non valida');
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>.focus();

          return false;
        }

        //======================================================================
        // Controlli sui destinatari delle notifiche (almeno uno obbligatorio)
        //======================================================================
        if(document.LoadInserisciSospensioneDifferimento.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciSospensioneDifferimento.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Magistrato Firmatario è obbligatorio");
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
          return false;
        }

        //========================
        // TDS
        //========================
        var tds = true; // true indica assente
        try {
          var sedeTds = document.LoadInserisciSospensioneDifferimento.<%= ICostantiSospensione.CAMPO_SEDE_TDS %>.value;
          if(sedeTds=="")
            tds = true;
          else
            tds = false;
        }
        catch(err) {
          //alert('TDS Assente nella form');
        }

        //========================
        // Istituto
        //========================
        var istituto = true;
        try {
          var idIstituto = document.LoadInserisciSospensioneDifferimento.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value;
          if(idIstituto=="")
            istituto = true;
          else
            istituto = false;
        }
        catch(err) {
          //alert('Istituto assente nella form');
        }

        //========================
        // UEPE
        //========================
        var cssa = true;
        try {
          var idCSSA = document.LoadInserisciSospensioneDifferimento.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value;
          if(idCSSA=="")
             cssa = true;
          else
             cssa = false;
        }
        catch(err) {
          //alert('UEPE assente nella form');
        }

        //========================
        // Polizia
        //========================
        var polizia = true;
        try {
          if(!document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled)
          {
            polizia = false;
            if (document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
            {
             polizia = true;
            }
          }
        }
        catch(err) {
          //alert('Polizia assente nella form');
        }

        //========================
        // UNEP
        //========================
        var unep = true;
        try {
          for (i=0; i<<%=avvocati.size()%>; i++){
            var indice = document.LoadInserisciSospensioneDifferimento.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>[i].options.selectedIndex;
            var idAutoritaAvv = document.LoadInserisciSospensioneDifferimento.<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>[i].options[indice].value;
            if(idAutoritaAvv!="-") {
              unep = false;
              break;
            }
          }
        }
        catch(err) {
          //alert(err.description);
          //alert('Unep assente nella form');
        }

        //===========================================
        //
        //===========================================
        //if(istituto){alert("Istituto assente");}
        //if(cssa){alert("cssa assente");}
        //if(tds){alert("tds assente");}
        //if(polizia){alert("polizia assente");}
        //if(unep){alert("unep assente");}

        if(istituto && cssa && tds && polizia && unep) {
          alert("Inserire almeno un destinatario!");
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
      <td class=lbg>
         <font  class="label">Funzione :&nbsp;</font>
         <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Provvisorio</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Definitiva</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
         <font class="campo">Rigetto Differimento / Rinvio dell'esecuzione</font>
         <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
         <font class="campo">Revoca Differimento / Rinvio dell'esecuzione</font>
         <% }  %>
      </td>
    </tr>
  </table>

  <br>
     <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSospensioneDifferimento">
<%
   BigDecimal lIdOrdinanzaSius = null;
   if(misuraalternativa != null && misuraalternativa.getEveIdEvento() != null) {
     lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
   }
%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciDifferimentoOE">
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
    <input type="HIDDEN" name="AzioneChiamante"    value="<%=AzioneChiamante%>">

    <input type="HIDDEN" name="idEventoGenerato"     value="<%=misuraalternativa.getEveIdEvento() %> ">    <%// id evento SIUS   %>

    <input type="HIDDEN" name="<%=ICostantiSospensione.TIPO_DIFFERIMENTO%>"    value="<%=tipoProvvedimento%>">     <%// DifferimentoProvv, DifferimentoDef, DifferimentoRigetto, DifferimentoRevoca%>
    <input type="HIDDEN" name="CodTipoProvvedimento" value="<%=CodTipoProvvedimento %>"> <%// tipo provv. SIES %>
    <input type="HIDDEN" name="CodMotivo"            value="<%=CodMotivo %>">            <%// cod Motivo SIES  %>


<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile se non validata(?))
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
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if( penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
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
        <tr>
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

    <% if ( !soloAnnotazione )  {  %>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <% } %>
    <tr height="3"><td></td></tr>
  </table>

<%
//==============================================================================
//           DATI DEL PROVVEDIMENTO DI DIFFERIMENTO DELL'ESECUZIONE
//==============================================================================
%>
  <table style="width: 95%; border: 0;">
    <tr>
    <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% } else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
      <td colspan="4" class="titolo">Riepilogo Dati del Differimento/Rinvio</td>
    <% }  %>
    </tr>

    <tr>
      <td class="l" width=15%>Anno / Numero Sius</td>
      <td class="l" width=20%>
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
      </td>
      <td class="l" width='25%'> Anno / Numero Provvedimento </td>
        <td class="l" width=20%>
         <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
         <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Tipo provvedimento </td>
      <td class="l" colspan="3">
        <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoDecisione())%></font>
      </td>
    </tr>

	<tr>
		<td class="l">Autorità Emittente</td>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
      	<td class="l" colspan="3"> <font class="campo"><%=descrTipoUfficio%> di <%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
	</tr>

    <tr>
      <td class="l">Oggetto Decisione</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;
      </td>
    </tr>

    <% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
    <tr>
      <td class="l">Tipologia</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrNaturaDecisione())%></font>&nbsp;
      </td>
    </tr>
    <% } %>

    <tr>
      <td class="l">Data Emissione Provvedimento </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Annotazioni</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></font>&nbsp;
    </tr>

    <% if (    tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
            || tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)) { %>
    <tr>
      <td class="l">Data Differimento</td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font>&nbsp;
        <%
          if(   misuraalternativa.getCodTipoUfficioScarcerazione()!=null
             && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV")
            )
          {
            if (   lPosizione.getCodPosizioneGiuridica().equals("13") // Affidamento in prova
                || lPosizione.getCodPosizioneGiuridica().equals("27") // Indultino
                //MEV29 Aggiunta gestione delle Misure Provvisorie
                || lPosizione.getCodPosizioneGiuridica().equals("29") // Detenzione Domiciliare Provvisoria
                || lPosizione.getCodPosizioneGiuridica().equals("54") // Affidamento in prova Provvisorio
               )
            {
              out.print("         [Già posto in libertà]");
            }
            else {
              out.print("         [Già scarcerato]");
            }
          }
          else if(   misuraalternativa.getCodTipoUfficioScarcerazione()!=null
                  && misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")
                 )
          {
            if (   lPosizione.getCodPosizioneGiuridica().equals("13") // Affidamento in prova
                || lPosizione.getCodPosizioneGiuridica().equals("27") // Indultino
                //MEV29 Aggiunta gestione delle Misure Provvisorie
                || lPosizione.getCodPosizioneGiuridica().equals("29") // Detenzione Domiciliare Provvisoria
                || lPosizione.getCodPosizioneGiuridica().equals("54") // Affidamento in prova Provvisorio
               )
            {
              out.print("         [Da porre in libertà]");
            }
            else {
              out.print("         [Da scarcerare]");
            }
          }
        %>
      </td>
    </tr>
    <% } %>
    <tr height="3"><td></td></tr>
  </table>

<%
//==============================================================================
// Sezione contenente i dati della Nuova Pena Residua e della Pena Espiata
//==============================================================================
%>

<% if (!soloAnnotazione ) { %>

<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan="8"> Dati della Pena </td>
  </tr>
  <%
    if (   sospensione.getNumAnniPenaEspiata().intValue()!=0
        || sospensione.getNumMesiPenaEspiata().intValue()!=0
        || sospensione.getNumGiorniPenaEspiata().intValue()!=0
       )
    {
  %>
  <tr>
    <td class="l">
      <font class="label">Pena Espiata</font>
    </td>
    <td class="l">
      <font class="label">Anni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
      <font class="label">Mesi</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
      <font class="label">Giorni</font>
      <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>

      <%  // Agiungo la Multa se presente
      if(sospensione.getMultaEspiata()!=null && sospensione.getMultaEspiata().compareTo(new BigDecimal(0))!=0) {
      %>
      <font class="label">Multa </font>
      <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaEspiata())%></font>&nbsp;€&nbsp;
      <% }

      //
      if(sospensione.getAmmendaEspiata()!=null && sospensione.getAmmendaEspiata().compareTo(new BigDecimal(0))!=0) {
      %>
      <font class="label">Ammenda </font>
      <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaEspiata())%></font>&nbsp;€&nbsp;
      <%}%>
    </td>
  </tr>
  <% } %>

  <%
  //=========================================================================
  // Inserisco la Pena Residua solo se non ergastolo e presente
  //=========================================================================
  if (  flagergastolo.equals("N")
      && (   sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
          || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
          || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
          || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
          || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
          || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
         )
  {
  %>
  <tr>
    <td class="l">
      <font class="label">Pena Residua</font>
    </td>
    <td class="l">
      <%  // Reclusione
      if (   sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
          || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
          || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
         )
      {
      %>
        <font class="label">Reclusione : </font>
        <font class="label">Anni</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
        <%
        if(sospensione.getMultaResidua()!=null && sospensione.getMultaResidua().compareTo(new BigDecimal(0))!=0) {
        %>
        <font class="label">Multa </font>
        <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%></font>&nbsp;€&nbsp;
        <% } %>
      <% } %>

      <% // Arresti
      if (   sospensione.getNumAnniPenaResiduaArres().intValue()!=0
          || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
          || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0
         )
      {
      %>
        <font class="label">Arresto : </font>
        <font class="label">Anni</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
        <%
        if(sospensione.getAmmendaResidua()!=null && sospensione.getAmmendaResidua().compareTo(new BigDecimal(0))!=0) {
        %>
        <font class="label">Ammenda </font>
        <font class="campo"><%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%></font>&nbsp;€&nbsp;
        <% } %>
      <% } %>
    </td>
  </tr>
  <% } %>

  <%
  //==========================================================================
  // Se Ergastolo visualizzo la Pena Complessiva e il tipo di ergastolo
  //==========================================================================
  if(flagergastolo.equals("S"))
  {
  %>
  <tr>
    <td class="l">
      <font class="label">Pena Complessiva</font>
    </td>
    <td class="l">
      <font class="campo">ERGASTOLO</font>
    </td>
  </tr>
  <%
  }
  else if(flagergastolo.equals("D"))
  {
  %>
  <tr>
    <td class="l">
      <font class="label">Pena Complessiva</font>
    </td>
    <td class="l">
      <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
    </td>
  </tr>
  <% } %>
  <tr height="3"><td></td></tr>
</table>

<% }%>

<%
//==============================================================================
// Sezione con i dati del Magistrato
//==============================================================================
%>
<% if (!soloAnnotazione) { %>
<table style="width: 95%;">
	<tr>
    	<td class="Titolo" colspan=2>Magistrato Firmatario</td>
  	</tr>
  	<tr>
	    <%-- MEV10-s3: aggiunta proprietà per la larghezza del campo --%>
 		<td class="l" width="30%">Magistrato Firmatario</td>
	    <td class="l">
	      	<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
	      	<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
	      	<input readonly title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>"    type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
	      	<a href="Javascript:ListaMagistrati('LoadInserisciSospensioneDifferimento','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
	        	<img src="/images/filefolder.gif" border=0>
	      	</a>
	    </td>
  	</tr>
  	<tr height="3"><td></td></tr>
</table>
<% } %>

<%
//==============================================================================
// Sezione dei Destinatari. Variano in funzione del tipo di provvedimento e
// della posizione giuridica del soggetto, nessun destinatario è obbligatorio
// ma almeno uno va indicato (se previsti)
// - Differimento Provvisorio
//   - Libero: (07,10,16,20,26,30,46,47) isLibero
//     - Tds, Autorità Competente per la Notifica al Condannato, UNEP (eventualmente autorità per la restituzione OE)
//   - Detenuto (03):
//     - se da  scarcerare: Tds, Istituto di detenzione, UNEP
//     - se già scarcerato: Tds, Autorità Competente per la Notifica al Condannato, UNEP
//   - In Misura Alternativa (12,13,14,27,04)
//     - se Semilibero (14), in Detenzione Domiciliare (12), in Affidamento in Prova (13)
//       Indultino (L. 207/2003) (27)
//       Tds, Istituto di detenzione, UEPE, Autorità di Polizia, UNEP (12,13,14,27)
//     - se agli Arresti Domiciliari (04)
//       - se da scarcerare:  Tds, Autorità Competente per la Notifica al Condannato, UNEP
//       - se già scarcerato: Tds, Autorità Competente per la Notifica al Condannato, UNEP
//   - In tutti gli altri casi, non si indicano destinatari (doc vuoto)
//
// - Differimento Definitivo
//   - Libero in Differimento Provvisorio
//     - Istituto di detenzione, UEPE, Autorità di Polizia, UNEP
//   - Libero (????????????)
//   - Detenuto
//     - Istituto di detenzione, UEPE, Autorità di Polizia, UNEP
//   - In Misura Alternativa
//     - Istituto di detenzione, UEPE, Autorità di Polizia, UNEP
// - Rigetto
//   - Autorità per la notifica
//   - Unep
// - Revoca
//   - Autorità per la notifica
//   - Unep
//   - UEPE (CSSA)
//   - TDS
//
//==============================================================================
%>

<%
if ( !soloAnnotazione ) {
%>

<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan=6>Destinatari</td>
  </tr>
<% if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) )
    //==========================================================================
    // DIFFERIMENTO PROVVISORIO
    //==========================================================================
{ %>
	<%-- MEV10-s3: modificato layout con aggiunta etichette e combo --%>
	<tr> <%// Tds: sempre presente, sempre primo %>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario</td>
        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
     	<td class="l" colspan="3">
      		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiSospensione.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
      		<a href="Javascript:ListaComuniTds('LoadInserisciSospensioneDifferimento','<%= ICostantiSospensione.CAMPO_SEDE_TDS %>');">
        		<img src="/images/filefolder.gif" border="0">
      		</a>
    	</td>
  	</tr>

  <% if (lPosizione.isLibero()){ %>
  <tr>
    <td class="l">Destinatario per esecuzione</td>
    <td class="L" colspan="3">
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
      <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
        <%=codiceAutoritaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
    </td>
  </tr>
  <%} %>


  <% if (   lPosizione.getCodPosizioneGiuridica().equals("04")  // Agli Arresti Domiciliari
         || (   lPosizione.getCodPosizioneGiuridica().equals("03")
             && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV") // Detenuto già scarcerato
            )
        ){ %>
        <tr>
          <td class="l">Destinatario per esecuzione</td>
          <td class="L" colspan="3">
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
            <%--select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
            <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
              <%=codiceAutoritaE%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Sede</td>
          <td class="L">
            <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
              <img src="/images/filefolder.gif" border="0">
            </a>
          </td>
          <td class="l">Indirizzo</td>
          <td class="L">
            <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
          </td>
        </tr>
  <% }
     else if (   misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC")
              && lPosizione.getCodPosizioneGiuridica().equals("03")  // Detenuto da scarcerare
             )
     { %>
        <tr><%// Se presente, propongo    %>
          <td class="l" >Istituto di Detenzione</td>
          <td class="l" colspan="3">
          <%if(posizioneluogoaltra != null && posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null){%>
            <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
            <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
            <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
            <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
          <%}else {%>
            <input readonly Title="Istituto" name="Comune" value="" size=50>
            <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
            <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
            <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
          <%}%>
          </td>
        </tr>
    <%}  // end if detenuto o arresti domiciliari (03/04)%>

    <% if (   lPosizione.getCodPosizioneGiuridica().equals("12")  // Detenzione Domiciliare
           || lPosizione.getCodPosizioneGiuridica().equals("13")  // Affidamento in Prova
           || lPosizione.getCodPosizioneGiuridica().equals("14")  // Semilibertà
           || lPosizione.getCodPosizioneGiuridica().equals("27")  // Indultino
           // MEV 29
           || lPosizione.isMisAlt()
          ){  %>
      <tr>
        <td class="l" >Istituto di Detenzione</td>
        <td class="l" colspan="3">
        <%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null){%>
          <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
        <%}else {%>
          <input readonly Title="Istituto" name="Comune" value="" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0></a>
          <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
        <%}%>
        </td>
      </tr>

	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
        <td class="l" colspan="3">
			<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="-" size="60" >
          	<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="-" size="35" >
          	<a href="Javascript:ListaCSSA('LoadInserisciSospensioneDifferimento','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
            	<img src="/images/filefolder.gif" border="0">
          	</a>
          	<a href="Javascript:delCSSA(LoadInserisciSospensioneDifferimento);">
          		<img src="/images/delete.gif" border=0>
          	</a>
		</td>
	</tr>

      <tr>
        <td class="l">Destinatario per esecuzione</td>
        <td class="L" colspan="3">
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
          <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
            <%=codiceAutoritaE%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede</td>
        <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
            <img src="/images/filefolder.gif" border="0">
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
        </td>
      </tr>
    <%}  // end if in misura alternativa (12,13,14,27) %>
  <% }
    else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) {
    //==========================================================================
    // DIFFERIMENTO DEFINITIVO
    //==========================================================================
    // Nell'inserimento dei destinatari bisogna tener conto anche dell'ordine.
    // In particolare si rileva che:
    // 1) se presente il TDS è sempre primo
    // 2) se presente l'Istituto di Detenzione viene dopo TDS ma prima degli altri destinatari
    // 3) se presente UEPE(CSSA) viene dopo Istituto Detenzione ma prima degli altri destinatari
    // 4) se presente l'Autorità competente viene dopo tutti gli altri destinatari
    // Tenendo presente ciò l'ordine di inserimento è:
    // - TDS, Istituto di Detenzione, UEPE, Autorità per la notifica
  %>
  <%// Il TDS è sempre il PRIMO e presente solo nel caso di arresti domiciliari
    // o misura alternativa sia da scarcerare che già scarcerato
    if (    lPosizione.getCodPosizioneGiuridica().equals("04")  // Arresti Domiciliari
         || lPosizione.getCodPosizioneGiuridica().equals("12")  // Detenzione Domiciliare
         || lPosizione.getCodPosizioneGiuridica().equals("13")  // Affidamento in Prova
         || lPosizione.getCodPosizioneGiuridica().equals("14")  // Semilibertà
         || lPosizione.getCodPosizioneGiuridica().equals("27")  // Indultino
         // MEV 29
         || lPosizione.isMisAlt()
        ){  %>
   		<tr> <%// Tds: sempre presente, sempre primo %>
			<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
		</tr>
		<tr>
			<td class="l" width="30%">Destinatario</td>
        	<td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
   	 	</tr>
  		<tr>
	   		<td class="l">Sede</td>
      		<td class="l" colspan="3">
	       		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiSospensione.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
	        	<a href="Javascript:ListaComuniTds('LoadInserisciSospensioneDifferimento','<%= ICostantiSospensione.CAMPO_SEDE_TDS %>');">
	          		<img src="/images/filefolder.gif" border="0">
	        	</a>
      		</td>
    	</tr>
    <% } %>
  <%// L'Istituto di detenzione se presente è sempre secondo e manca solo nel caso
    // di arresti domiciliari scarcerato o scarcerato
    if (!lPosizione.getCodPosizioneGiuridica().equals("04")                // Arresti Domiciliari
        ){  %>
    <tr><%// Se presente, propongo    %>
      <td class="l" >Istituto di Detenzione </td>
      <td class="l" colspan="3">
      <%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null){%>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
        <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
      <%}else {%>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
        <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDifferimento','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0></a>
        <a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');"><img src="/images/delete.gif" border=0></a>              
      <%}%>
      </td>
    </tr>
    <% } %>
  <%// UEPE presente se detenuto o libero sia da scarcerare che già scarcerato
	if (    lPosizione.isLibero()                              // Libero
         || lPosizione.getCodPosizioneGiuridica().equals("03") // Detenuto
        ){  %>
	    <tr>
			<td class="Titolo" colspan="4">UEPE/USSM</td>
		</tr>
		<tr>
			<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
			<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
		</tr>
		<tr>
			<td class="l">Sede</td>
      		<td class="l" colspan="3">
        		<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="-" size="60" >
        		<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="-" size="35" >
        		<a href="Javascript:ListaCSSA('LoadInserisciSospensioneDifferimento','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          			<img src="/images/filefolder.gif" border="0">
        		</a>
        		<a href="Javascript:delCSSA(LoadInserisciSospensioneDifferimento);">
        			<img src="/images/delete.gif" border=0>
        		</a>
      		</td>
    	</tr>
	<% } %>
  <%// L'Autorità competente sempre presente %>
    <tr>
      <td class="l">Destinatario per esecuzione</td>
      <td class="L" colspan="3">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--select  Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
        <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
          <%=codiceAutoritaE%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
      </td>
    </tr>
  <% }
    else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) {
    //==========================================================================
    // RIGETTO DIFFERIMENTO
    //==========================================================================
    // - solo destinatari per l'esecuzione
    %>
  <tr>
    <td class="l">Destinatario per esecuzione</td>
    <td class="L" colspan="3">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--select Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
      <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
        <%=codiceAutoritaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
    </td>
  </tr>
  <% }
    else if (tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) {
    //==========================================================================
    // REVOCA DIFFERIMENTO
    //==========================================================================
    %>
	    <tr> <%// Tds: sempre presente, sempre primo %>
			<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
		</tr>
		<tr>
			<td class="l" width="30%">Destinatario</td>
	        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
	    </tr>
	  	<tr>
		   	<td class="l">Sede</td>
      		<td class="l" colspan="3">
        		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiSospensione.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        		<a href="Javascript:ListaComuniTds('LoadInserisciSospensioneDifferimento','<%= ICostantiSospensione.CAMPO_SEDE_TDS %>');">
          			<img src="/images/filefolder.gif" border="0">
        		</a>
      		</td>
    	</tr>

    <tr>
      <td class="l">Destinatario per esecuzione</td>
      <td class="L" colspan="3">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--select Title="Autorita Esterna" class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>"--%>
        <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
          <%=codiceAutoritaE%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols="30" ></textarea>
      </td>
    </tr>

	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
      	<td class="l" colspan="3">
        	<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="-" size="60" >
        	<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="-" size="35" >
        	<a href="Javascript:ListaCSSA('LoadInserisciSospensioneDifferimento','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          		<img src="/images/filefolder.gif" border="0">
        	</a>
        	<a href="Javascript:delCSSA(LoadInserisciSospensioneDifferimento);">
        		<img src="/images/delete.gif" border=0>
        	</a>
      	</td>
	</tr>
	<% } %>

  	<tr height="3"><td></td></tr>
</table>
<% } // end if ( soloAnnotazione ) { %>

<%
//==============================================================================
//                         Destinatari Per Notifica
//==============================================================================
%>
<%
if ( !soloAnnotazione ) {
%>
<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan="4">Destinatari Per Notifica</td>
  </tr>
  <%
  int lIdxAvv = 0;
  Iterator lItxAvv = avvocati.iterator();
  while(lItxAvv.hasNext())
  {
    AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lItxAvv.next();
  %>
  <tr>
    <td class="l" colspan="4">Per Avvocato&nbsp;
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
    <input type="HIDDEN" title="Codice Avvocato" maxlength="35" size="35"
           value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>"
           name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  >
  </tr>

  <tr>
    <td class="l">Autorità Destinazione <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select Title="Autorita Esterna" class="small" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>" >
        <%=autoritaEsternaAvv%>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">Sede</td>
    <td class="l">
      <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
      <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>" maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF %>[<%=lIdxAvv%>]');">
        <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
    <td class="l">Note</td>
    <td class="l">
      <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="35"></textarea>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>

  <%
    lIdxAvv++;
  }%>
</table>
<% } // end if ( soloAnnotazione ) { %>

<%
if (   tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
    || tipoProvvedimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) )
{
  if ( lPosizione.isLibero() ){
%>
<table>
  <tr>
    <td class="l">
      <input type="checkbox" name="<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>" onclick="VisualizzaOE();"> &nbsp;&nbsp;Restituzione Ordine di Esecuzione
    </td>
  </tr>
</table>
<%
  }
}
%>

<% if (!soloAnnotazione) {  %>
<div id="divConferma" style="visibility:visible; position:relative; " >
  <table style="width: 95%;">
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</div>
<% } %>


<%
//==============================================================================
// Sezione della restituzione dell'ordine di esecuzione
//==============================================================================
%>
<div id="divOrdineEsecuzione" style="visibility:hidden; position:relative; " >
<table style="width: 95%; border: 0;">
  <tr>
    <td class="Titolo" colspan="4">Restituzione Ordine di Esecuzione</td>
  </tr>
<tr><td colspan="4">
<table>
  <tr>
    <td class="l">
      <a href="Javascript:ListaOrdiniEsecuzione('LoadInserisciSospensioneDifferimento');">
        Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="l"> Data Emissione </td>
    <td class="l">
      <input Title="Data Emissione" name="<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>" type="text" size="10" maxlength="10" READONLY >
    </td>
    <td class="l"> Oggetto </td>
    <td class="l" >
      <input Title="Oggetto" name="descrMotivoOE" type="text" size="50" maxlength="50" READONLY >
    </td>
  </tr>
</table>
  <tr>
    <td class="Titolo" colspan="4">Autorità per la Restituzione</td>
  </tr>
  <tr>
    <td class="l" >Destinatario per esecuzione </td>
    <td class="L" colspan="3">
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>"--%>
      <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>">
        <%=codiceAutoritaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>"  maxlength="35" size="35" READONLY >
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>"  cols="30" READONLY ></textarea>
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>
</table>
</div>

</form>

<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSospensioneDifferimento");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>