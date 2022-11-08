<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="aEventoComputo"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<!-- Autorità per l'esecuzione e la notifica -->
<jsp:useBean id="autoritaEsternaE"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"    scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"            scope="request" class="java.util.Vector"/>

<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>


<!-- Probabilmente da eliminare -->
<jsp:useBean id="StrdataInizioPena"   scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>

<jsp:useBean id="autoritaEmi"              scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<%-- 20191002 [SG]: intervento post collaudo 11.3 -- refactoring pagina --%>
<!-- LoadInserisciOERidetPenaAltro -->
<%
//==============================================================================
// Form per l'inserimento dell'Ordine di Esecuzione a seguito di Rideterminazione 
// Pena Altro
//==============================================================================

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
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      var desktop;
      
      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
      
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

    
      //========================================================================
      // Metodo con i controlli
      //========================================================================
      function Verify()
      {
        //==========================
        // Check Data Emissione
        //==========================
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione non valida');
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
        }
        
        <%
        //==========================================================================
        // Se la data fine pena è presente ed editabile ne controllo la correttezza
        //==========================================================================
        if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07")
               && !lPosizione.getCodPosizioneGiuridica().equals("10")
               && !lPosizione.getCodPosizioneGiuridica().equals("16")
               && !lPosizione.getCodPosizioneGiuridica().equals("20")
               && !lPosizione.getCodPosizioneGiuridica().equals("46")
               && !lPosizione.getCodPosizioneGiuridica().equals("47")
              ) 
           || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
               && lFascicoloAssociato.getFlagAltraCausa().equals("S")
               && penaresidua.getDataFinePresunta()!= null 
               && penaresidua.getDataFine() == null
              )
          )
        {
          // Se non ergastolo
          if (   penaresidua.getFlagErgastolo() == null
              || (   penaresidua.getFlagErgastolo() != null 
                  && !penaresidua.getFlagErgastolo().equals("S") 
                  && !penaresidua.getFlagErgastolo().equals("D")
                 )
             )
             
          {
            if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
            {
            %>
              if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
                  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
              if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
                  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
          
              var data_to_verifica = document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
          
              if (!ControllaData(data_to_verifica) )
              {
                alert('Data fine pena non valida');
                return false;
              }
            <%
            }
          } // fine non ergastolo
        } // fine 
        %>
      
        //==========================
        // Check Data Trasmissione
        //==========================
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
  
        var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
  
        if (!ControllaData(data_to_verify) )
        {
          alert('Data di trasmissione non valida');
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
          return false;
        }
        
        //=======================
        // Controllo Magistrato  
        //=======================
        if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }
        if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }
        
        //======================================================================
        // Controllo Obbligatorietà dei Destinatari
        //======================================================================
        var campo = document.LoadInserisciOrdineEsecuzione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

        if (   document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '10'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '07'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '02'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '04'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '16'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '20'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '46'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '47'
            && document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '12'
           )
        {
          if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
          {
            alert("Autorità Destinazione obbligatoria");
            document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
            return false;
          }
        }

        <% // Se detenuto Altra Causa è obbligatorio l'Istituto di Detenzione
           // 23 - Custodia Cautelare in Regime di Arresti Domiciliari
        if(   lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
           && !lAltraCausa.getCodTipoPosGiuridica().equals("23")
          )
        { 
        %>
          if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>
          		&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
          {
            alert("Autorità Destinazione obbligatorio");
            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
            return false;
          }
        <%}%>


        <%
        // Se libero() e non det altra causa
        if(   lPosizione.getCodPosizioneGiuridica().equals("07") 
           || lPosizione.getCodPosizioneGiuridica().equals("10") 
           || lPosizione.getCodPosizioneGiuridica().equals("02") 
           || lPosizione.getCodPosizioneGiuridica().equals("04")
           || lPosizione.getCodPosizioneGiuridica().equals("16") // Libero in Differimento Pena
           || lPosizione.getCodPosizioneGiuridica().equals("20") // Evaso
           || lPosizione.getCodPosizioneGiuridica().equals("46") // Libero in Sospensione 
           || lPosizione.getCodPosizioneGiuridica().equals("47") // Libero in Sospensione DPR 309/90
           || lPosizione.getCodPosizioneGiuridica().equals("12") // Espiazione Pena in Regime di Detenzione Domiciliare (MIS_ALT)
          )
        {
          if(lFascicoloAssociato.getFlagAltraCausa()== null || (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("N")))
          { // Non det. ALtra Causa
            %>
            if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
            {
              alert("Autorità Destinazione obbligatoria");
              document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
              return false;
            }
            <%
          }
        }%>


        <%
        //======================================================================
        // Se detenuto altra causa in custodia cautelare agli arresti domiciliari
        //======================================================================
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") )
        {
          if(lAltraCausa.getCodTipoPosGiuridica().equals("23"))
          { // 23 - Custodia Cautelare in Regime di Arresti Domiciliari
            %>
            if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
            {
              alert("Autorità Destinazione obbligatoria");
              document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
              return false;
            }
            <%
          }
        }
        %>

        <% // 12 - Espiazione Pena in Regime di Detenzione Domiciliare
        if(lPosizione.getCodPosizioneGiuridica().equals("12"))
        {
        %>
           if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value == "-")
           {
              alert("Altra autorità di polizia obbligatoria");
              document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.focus();
              return false;
           }
        <%
        }
        %>
 
/*
        <%
        //======================================================================
        // Sezione BDMC
        // Se Libero detenuto altra causa
        //======================================================================
        if (   ( lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") ) 
            || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
            || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10") )
           )
        {
        %>
          var associaBdmc = 1;  

          <% if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
            if (document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "" && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value == "" && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value == "" &&  document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value == "-" && document.LoadInserisciOrdineEsecuzione.ComuneBdmc.value == "")
                  associaBdmc = 0;
            if (document.LoadInserisciOrdineEsecuzione.annoBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value != "-" && document.LoadInserisciOrdineEsecuzione.ComuneBdmc.value != "")
                  associaBdmc = 0;
          <%} else {%>
            if (document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "" && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value == "" && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value == "" &&  document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value == "-" && document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc.value == "" && document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc.value == "")
                  associaBdmc = 0;
            if (document.LoadInserisciOrdineEsecuzione.annoBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value != "-" && document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc.value != "" && document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc.value != "")
                  associaBdmc = 0;
          <%}%>
          
          if (associaBdmc == 1)   {
            alert("Le informazioni relative al fascicolo Bdmc se presenti vanno riempite tutte");
            return false;
          }
      
          if ((document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value != "-" || document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value != ""   )&& document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "" )
          {
            alert("Destinatario per notifica B.D.M.C puo' essere inserito solo in presenza di un associazione con un Fascicolo B.D.M.C");
            return false;
          }
          
          if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value != "-" && document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value == "" )
          {
            alert("Inserire la sede per notifica B.D.M.C.");
            return false;
          }
          
          if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value == "-" && document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value != "" )
          {
            alert("Inserire l'autorita destinazione per notifica B.D.M.C.");
            return false;
          }

        <%}%>
*/        
        return true;
      }

      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      //========================================================================
      // Funzione Visualizzare/Nascondere la sezione (div) per la richiesta di
      // restituzione Ordine di Esecuzione
      //========================================================================
      function VisualizzaOE()
      {
        var nodeOE = document.getElementById('divOrdineEsecuzione');

        if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>.checked == true)
        {
           nodeOE.style.display="block";
        }
        else
        {
           nodeOE.style.display="none";
        }
      }
      
      //========================================================================
      // Funzione per il caricamento della lista degli Ordini di Esecuzione
      //========================================================================
      function ListaOrdiniEsecuzione(a_formname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActListaOE&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>", "Lista_Ordini_Esecuzione", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
      }
      

  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
            <font class="campo">Emissione Ordine di Esecuzione Per Rideterminazione Pena - DETENUTO PER ALTRA CAUSA</font>
        <% } else { %>
            <font class="campo">Emissione Ordine di Esecuzione Per Rideterminazione Pena - <%=lPosizione.getDescrPosizioneGiuridica()%></font>
        <% } %>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <form method="POST" name="LoadInserisciOrdineEsecuzione" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciOERidetPenaAltro">
    <input type="HIDDEN" name="IdEventoComputo" value="<%=StringUtils.toStringJSP(aEventoComputo.getIdEvento())%>">
    <input type="HIDDEN" name="codMotivoEventoComputo" value="<%=StringUtils.toStringJSP(aEventoComputo.getCodMotivo())%>">
    
    <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>"  value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" >
    
    <!-- =================================================================== -->
    <!--   Sezione con Posizione Giuridica, Luogo di detenzione e Pena da Espiare -->
    <!-- =================================================================== -->
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
            <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
              DETENUTO PER ALTRA CAUSA <%=lAltraCausa.getDescrTipoPosGiuridica()%>
            <% } else { %>
              <%=lPosizione.getDescrPosizioneGiuridica()%>
            <% } %>
          </font>
        </td>
      </tr>
      
      
      <%
      //========================
      // Luogo di detenzione
      //========================
      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
        if(lAltraCausa.getIstitutoDetenzione() != null)
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
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null)
      {
      %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
          </td>
        </tr>
      <%
      }

      // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
      if(    lPosizione.getCodPosizioneGiuridica() != null 
          && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) 
          && lLuogoDetenzione.getAltroLuogo() != null
        )
      {
      %>
        <tr>
          <td class="l">Altro Luogo </td>
          <td class="L" colspan=5>
            <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
          </td>
        </tr>
      <%
      }
      %>
      
      <%
      //==========================================================
      // Sezione con la pena da espiare se diversa da ergastolo
      //==========================================================
      if(   penaresidua.getIdPenaResidua() != null 
         && (   penaresidua.getFlagErgastolo() == null
             || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) 
            ) 
        )
      {
        // Reclusione se presente
        if ( !penaresidua.isQuantumReclusioneZero() )  {
        %>
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
   
        <% if ( !penaresidua.isQuantumArrestoZero()) { %>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
        <% } %>
      <% } %>



      <tr>
      <%
      //========================================================================
      // Visualizzazione delle date di decorrenza e scadenza.
      // n.b. Le date potrebbero essere presenti se OE contro detenuto agli
      //      arresti domiciliari o Altra Causa
      //========================================================================
      
      // Inizio Pena
      if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07") 
             && !lPosizione.getCodPosizioneGiuridica().equals("10")
             && !lPosizione.getCodPosizioneGiuridica().equals("16") 
             && !lPosizione.getCodPosizioneGiuridica().equals("20") 
             && !lPosizione.getCodPosizioneGiuridica().equals("46")
             && !lPosizione.getCodPosizioneGiuridica().equals("47")
            ) 
         || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) 
        )
      {
        if (penaresidua.getDataInizio() != null)
        {
        %>
          <td class="l">Data Decorrenza Pena</td>
          <td class="L">
             <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "yyyy") )%></font>
          </td>
          
        <%
        }

        // ERGASTOLO
        if (penaresidua.getFlagErgastolo() != null)
        {
          if(penaresidua.getFlagErgastolo().equals("S"))
          {
          %>
            <td class="l">Pena Detentiva</td>
            <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
          <%
          }
          else if(penaresidua.getFlagErgastolo().equals("D"))
          {
          %>
            <td class="l">Pena Detentiva</td>
            <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
          <%
          }
        }
      }



      // Fine Pena
      if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07")
             && !lPosizione.getCodPosizioneGiuridica().equals("10")
             && !lPosizione.getCodPosizioneGiuridica().equals("16")
             && !lPosizione.getCodPosizioneGiuridica().equals("20")
             && !lPosizione.getCodPosizioneGiuridica().equals("46")
             && !lPosizione.getCodPosizioneGiuridica().equals("47")
            )
         || (   lFascicoloAssociato.getFlagAltraCausa() != null
             && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
            )
        )
      {
        if(    (    penaresidua.getFlagErgastolo() == null)
            || (   penaresidua.getFlagErgastolo() != null
                && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")
               )
          )
        {
          if( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null )
          {
          %>
          <td class="l">Data Fine Pena</td>
          <td class="L" colspan=2>
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
          <%
          }
          else if( penaresidua.getDataFine() != null)
          {
          %>
            <td class="l">Data Fine Pena</td>
            <% if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) { %>
            <td class="L" colspan=2>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
            <% } else { %>
            <td class="lRosso" colspan=2>
              <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
            <% } %>  
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
          <%
          }
        }
      }
      %>
      </tr>
<!-- ======================================================================= -->
<!--  FINE sezione con la pena residua                                       -->
<!-- ======================================================================= -->
       
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L"colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
     
     
    <!-- =================================================================== -->
    <!--                            MAGISTRATO                               -->
    <!-- =================================================================== -->
    <table width=100%>
      <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
      <tr>
        <td class="l">Magistrato</td>
        <td class="L" colspan="3">
          <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
          <input readonly title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
           <a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzione');">
             <img src="/images/filefolder.gif" border=0>
           </a>
        </td>
        <td>
           <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        </td>
      </tr>
    </table>
   

   
   
   <!-- ==================================================================== -->
   <!--                    SEZIONE CON I DESTINATARI                         -->
   <!-- Detenuto Altra Causa
   <!--   - Se PG = 23 Custodia Cautelare in Regime di Arresti Domiciliari ==> Autorità per l'esecuzione
   <!--     Altrimenti l'Istituto di Detenzione
   <!-- Libero (QC)  ==> Autorità per l'esecuzione
   <!-- Non Libero   ==> Istituto di Detenzione
   <!-- Se in MIS_ALT 12 ==> Autorità per l'esecuzione
   <!--                  ==> Magistrato di Sorveglianza
   <!--                  ==> Autorità di Polizia
   <!-- ==================================================================== -->
   
    <table width=100%>
      <tr>
        <td class="Titolo" colspan=6>Destinatario per Esecuzione </td>
      </tr>

      <%
      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") ) 
      {
      %>
        
        <% 
        // 23 - Custodia Cautelare in Regime di Arresti Domiciliari
        if(lAltraCausa.getCodTipoPosGiuridica().equals("23")) 
        {
        %>
        <tr>
          <td class="l" width="20%">Autorità Destinazione <font class=ob>(*)</font> </td>
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
            <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
          <td class="l">Indirizzo</td>
          <td class="L">
            <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
          </td>
        </tr>
        <%
        } 
        else
        { // L'istituto di detenzione
        %>
        <tr>
          <td class="l" width="20%">Autorità Destinazione <font class=ob>(*)</font> </td>
        
          <%
          if(posizioneluogoaltra != null && lAltraCausa != null && lAltraCausa.getIstitutoDetenzione() != null)
          { %>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lAltraCausa.getIstitutoDetenzione().getDescrComune())%>" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lAltraCausa.getIstDetIdIstitutoDetenzione()%>" >
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                  <img src="/images/filefolder.gif" border=0></a>
              </td>
          <%
          } else {%>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                  <img src="/images/filefolder.gif" border=0></a>
              </td>
          <%}%>
          
          <td class="l">Note</td>
          <td class="L">
            <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
          </td>
        </tr>
        <%}%>

        <tr><td>&nbsp;</td></tr>

      <%
      }
      else
      {
      //================================
      // Caso non detenuto Altra Causa
      //================================
      %>
        <%
        // Se libero
        if(   lPosizione.getCodPosizioneGiuridica().equals("07") // Libero (prima di OE)
           || lPosizione.getCodPosizioneGiuridica().equals("10") // Libero (dopo OE))
           || lPosizione.getCodPosizioneGiuridica().equals("02") // Custodia Cautelare per Questa Causa in Regime di Arresti Domiciliari
           || lPosizione.getCodPosizioneGiuridica().equals("04") // Arresti Domiciliari ex art. 656/10
           || lPosizione.getCodPosizioneGiuridica().equals("16") // Libero in Differimento Pena
           || lPosizione.getCodPosizioneGiuridica().equals("20") // Evaso
           || lPosizione.getCodPosizioneGiuridica().equals("46") // Libero in Sospensione
           || lPosizione.getCodPosizioneGiuridica().equals("47") // Libero in Sospensione DPR 309/90
           || lPosizione.getCodPosizioneGiuridica().equals("12") // Espiazione Pena in Regime di Detenzione Domiciliare (MIS_ALT)
          )
        {%>
        <tr>
          <td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font> </td>
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
            <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
          <td class="l">Indirizzo</td>
          <td class="L">
            <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30></textarea>
          </td>
        </tr>
        <%
        }
        else
        { // Se diverso da Libero invio a Istituto di Detenzione
        %>
        <tr>
          <td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font> </td>
          <% if(lLuogoDetenzione.getIstitutoDetenzione() == null){%>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                  <img src="/images/filefolder.gif" border=0>
                </a>
              </td>
          <%} else {%>
              <td class="l">
                <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
                <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" >
                <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                  <img src="/images/filefolder.gif" border=0>
                </a>
              </td>
          <%}%>

          <td class="l">Note</td>
          <td class="L">
            <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <%}%>
    <%}%>


  <%
  //============================================================================
  // MAGISTRATO DI SORVEGLIANZA se:
  // MIS_ALT: 12 - Espiazione Pena in Regime di Detenzione Domiciliare
  //============================================================================
  if(lPosizione.getCodPosizioneGiuridica().equals("12"))
  {%>
   <tr>
     <td class="L">Magistrato di Sorveglianza</td>
     <td class="L" colspan="3">
       <input title="Sede Magistrato Sorveglianza" value="" type="text" name="<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>"  maxlength="35" size="35">
       <a href="Javascript:ListaUDS('LoadInserisciOrdineEsecuzione','<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>');">
         <img src="/images/filefolder.gif" border=0>
       </a>
     </td>
   </tr>
  <% } %>


  <!-- ===================================================================== -->
  <!--                      NOTIFICA AGLI AVVOCATI                           -->
  <!-- ===================================================================== -->
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td>
    </tr>
    <tr>
      <td colspan="100%">
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        <table width=100%>
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
            <input type="HIDDEN" title="Codice Avvocato"  name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>"  >
          </tr>
        </table>
        
        <table width=100%>
          <tr>
            <td class="l">Autorità Destinazione </td>
            <td class="L" colspan=3>
              <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
              <%=autoritaEsternaN%>
              </select>
            </td>
          </tr>
          <tr>
            <td class="l">Sede </td>
            <td class="L">
              <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
              <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
            <td class="l">Note</td>
            <td class="L">
              <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35 ></textarea>
            </td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        </table>
<%
    lIdxAvv++;
  }
%>
    </td>
  </tr>


  <%
  //============================================================================
  // Comunicazione alla Polizia in caso di Misura Alternativa
  // - Espiazione Pena in Regime di Detenzione Domiciliare
  //============================================================================
  if(lPosizione.getCodPosizioneGiuridica().equals("12"))
  {%>
   <tr>
     <td class="L">Altra Autorità di polizia <font class=ob>(*)</font></td>
     <td class="L" colspan="3">
       <select  Title="Altra Autorità di polizia" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
        <%=autoritaEsternaE%>
       </select>
     </td>
    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Altra Autorità di polizia" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
     <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"  cols=30></textarea>
      </td>
    </tr>
  <% } %>

<%
//==============================================================================
// Sezione della restituzione dell'ordine di esecuzione
//==============================================================================
%>
  <tr>
    <td class="l" colspan="2">
      <input type="checkbox" name="<%=ICostantiSospensione.CAMPO_RESTITUZIONE_OE%>" onclick="VisualizzaOE();"> &nbsp;&nbsp;Restituzione Ordine di Esecuzione
    </td>
  </tr>
<tr>
  <td colspan="100%">
<div id="divOrdineEsecuzione" style="display:none" >
  <table style="width: 95%;  border: 0;">
    <tr>
      <td class="Titolo" colspan="4">Restituzione Ordine di Esecuzione</td>
    </tr>
    <tr>
      <td colspan="4">
        <table>
          <tr>
            <td class="l">
              <a href="Javascript:ListaOrdiniEsecuzione('LoadInserisciOrdineEsecuzione');">
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
      </td>
    </tr>
    
    <tr>
      <td class="Titolo" colspan="4">Autorità per la Restituzione</td>
    </tr>
    <tr>
      <td class="l" >Destinatario per esecuzione </td>
      <td class="L" colspan="3">
        <select  Title="Autorita Esterna" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>">
          <%=autoritaEsternaE%>
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
</div>
</td>
</tr>

  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</form>



<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");
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

  <%
  //============================================================================
  // Inserisco controlli sul GenValidator se i campi relativi al fine pena sono
  // presenti ed editabili.
  // Se diverso da libero o detenuto altra causa con con fine pena editabile (??)
  //============================================================================
  if(   (   !lPosizione.getCodPosizioneGiuridica().equals("07") 
         && !lPosizione.getCodPosizioneGiuridica().equals("10")
         && !lPosizione.getCodPosizioneGiuridica().equals("16") 
         && !lPosizione.getCodPosizioneGiuridica().equals("20") 
         && !lPosizione.getCodPosizioneGiuridica().equals("46")
         && !lPosizione.getCodPosizioneGiuridica().equals("47")
        ) 
     || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
         && lFascicoloAssociato.getFlagAltraCausa().equals("S")
         && penaresidua.getDataFinePresunta()!= null 
         && penaresidua.getDataFine() == null
        )
    )
  {
    if (   penaresidua.getFlagErgastolo() == null
        || (   penaresidua.getFlagErgastolo() != null 
            && !penaresidua.getFlagErgastolo().equals("S") 
            && !penaresidua.getFlagErgastolo().equals("D")
           )
       )
    {
      if ( dataeditabile.equals("S")  && penaresidua.getDataFinePresunta() != null ) 
      {%>
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
      
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
      
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
        frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
      <%}
   }
}%>



<%
//==============================================================================
//
//==============================================================================
if(   lPosizione.getCodPosizioneGiuridica().equals("07") 
   || lPosizione.getCodPosizioneGiuridica().equals("10") 
   || lPosizione.getCodPosizioneGiuridica().equals("02") 
   || lPosizione.getCodPosizioneGiuridica().equals("04")
   || lPosizione.getCodPosizioneGiuridica().equals("16") 
   || lPosizione.getCodPosizioneGiuridica().equals("20") 
   || lPosizione.getCodPosizioneGiuridica().equals("46")
   || lPosizione.getCodPosizioneGiuridica().equals("47")
   || lPosizione.getCodPosizioneGiuridica().equals("12")
  )
{
  if(lFascicoloAssociato.getFlagAltraCausa()== null || (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("N")))
  {
    %>
    frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
    frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
    <%
  }
}%>


<%
//==============================================================================
//
//==============================================================================
if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") )
{
  if(lAltraCausa.getCodTipoPosGiuridica().equals("23"))
  {
    %>
    frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
    frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
    <%
  }
}%>


 frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","req","Luogo Autorità Destinazione obbligatoria");
 frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","alphabetic");


<%
//==============================================================================
//
//==============================================================================
if (lPosizione.getCodPosizioneGiuridica().equals("12") )
{
%>
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","req","Luogo Altra Autorità di polizia obbligatoria");
  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","alphabetic");
<%
}
%>


</script>
</body>
</html>