<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
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
<%-- MAC20191129017-MG-04/12/2019: aggiunti oggetti --%>
<%@ page import="siap.siep.util.MinorMask"%>
<jsp:useBean id="daticssa"            scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>
<%-- FINE MAC20191129017-MG-04/12/2019: aggiunti oggetti --%>

<jsp:useBean id="aEventoComputo"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aEventoAltraAut"     scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aCampoNota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<!-- Autorità per l'esecuzione e la notifica -->
<jsp:useBean id="autoritaEsternaE"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAltra" scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"            scope="request" class="java.util.Vector"/>

<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>


<!-- Probabilmente da eliminare -->
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>

<jsp:useBean id="autoritaEmi"              scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<!-- LoadInsOSNuovoResPenaRidetPenaAltro -->
<%
//==============================================================================
// Form per l'inserimento dell'Ordine di scarcerazione Nuovo Residuo Pena a 
// seguito di Rideterminazione Pena Altro
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
      

    
      //========================================================================
      // Metodo con i controlli
      //========================================================================
      function Verify()
      {
        //==========================
        // Check Data Emissione
        //==========================
        if (document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione non valida');
          document.LoadInserisciComunicazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
        }
        
      
        //==========================
        // Check Data Trasmissione
        //==========================
        if (document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
          document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        if (document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
          document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
  
        var data_to_verify = document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
  
        if (!ControllaData(data_to_verify) )
        {
          alert('Data di trasmissione non valida');
          document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus();
          return false;
        }
        
        //=======================
        // Controllo Magistrato  
        //=======================
        if(document.LoadInserisciComunicazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }
        if(document.LoadInserisciComunicazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }
        
        //======================================================================
        // Controllo Obbligatorietà dei Destinatari. Solo uno obbligatorio
        //======================================================================
        var campo = document.LoadInserisciComunicazione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;
        var lContaDest = 0;
        <% if (   lPosizione.getCodPosizioneGiuridica().equals("01")
               || lPosizione.getCodPosizioneGiuridica().equals("02")
               || lPosizione.getCodPosizioneGiuridica().equals("03")
               || lPosizione.getCodPosizioneGiuridica().equals("23")
               || lPosizione.isMisSosp()
             ) 
        {  %>
        if(document.LoadInserisciComunicazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
        {
          //alert("Istituto di Detenzione obbligatorio");
          //return false;
        } else { lContaDest = lContaDest +1;}
        <% } %>
        
        <% // Autorità per l'esecuzione
        if(lPosizione.isMisuraAlternativa())
        {
        %>
        if(document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
        {
          //alert("Autorità di destinazione obbligatoria");
          //document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
          //return false;
        } else { lContaDest = lContaDest +1;}
        
        if(document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "")
        {
          //alert("Sede Autorità di destinazione obbligatoria");
          //document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
          //return false;
        } else { lContaDest = lContaDest +1;}       
        <% } %>


        <!-- Autorità per la Notifica/Controllo -->
        if(document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value == "-")
        {
          <% if (lPosizione.isMisuraAlternativa()) { %>
          //alert("Autorità di controllo obbligatoria");
          <% } else { %>
          //alert("Altra Autorità obbligatoria");
          <% } %>
          //document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.focus();
          //return false;
        } else { lContaDest = lContaDest +1;}

        if(document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>.value == "")
        {
          <% if (lPosizione.isMisuraAlternativa()) { %>
          //alert("Sede Autorità di controllo obbligatoria");
          <% } else { %>
          //alert("Sede Altra Autorità obbligatoria");
          <% } %>          
          //document.LoadInserisciComunicazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>.focus();
          //return false;
        } else { lContaDest = lContaDest +1;}

        <% if (lPosizione.isMisuraAlternativa()){ %>
          // Obbligatori anche UEPE, Ufficio di Sorveglianza e Tribunale di Sorveglianza
          if(document.LoadInserisciComunicazione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "")
          {
            //alert("UEPE Obbligatorio");
            // n.b. non posso mettere il focus in quanto il campo è hidden
            //return false;
          } else { lContaDest = lContaDest +1;}

          if(document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value == "")
          {
            //alert("Magistrato di Sorveglianza Obbligatorio");
            //document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.focus();
            //return false;
          } else { lContaDest = lContaDest +1;}
          
          if(document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value == "")
          {
            //alert("Tribunale di Sorveglianza Obbligatorio");
            //document.LoadInserisciComunicazione.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.focus();
            //return false;
          } else { lContaDest = lContaDest +1;}
        <% } %>
        
        if (lContaDest==0)
        {
          alert("Inserire al meno un destinatario");
          return false;
        }
      
 
        return true;
      }

      //=====================================
      //
      //=====================================
      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
      
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
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

      function ListaComuniTds(formname,fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      <%-- MAC20191129017-MG-04/12/2019: eliminata funzione non più utilizzata 
      function ListaCSSA(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      } --%>

  </script>
  <%-- MAC20191129017-MG-04/12/2019: aggiunto include --%>
   <jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Ordine di Scarcerazione per Nuova Scadenza Pena
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%-- - <%=lPosizione.getDescrPosizioneGiuridica()%> --%>
        </font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <form method="POST" name="LoadInserisciComunicazione" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciOSRidetPenaAltro">
    <input type="HIDDEN" name="IdEventoComputo" value="<%=StringUtils.toStringJSP(aEventoComputo.getIdEvento())%>">
    
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
            <%-- MAC20191128013-MG-18/12/2019: utilizzata la variabile lAltraCausa sulla quale ci sono i controlli sul null --%>
              DETENUTO PER ALTRA CAUSA <%=lAltraCausa.getDescrTipoPosGiuridica()%>
            <%-- FINE MAC20191128013-MG-18/12/2019 --%>
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
     
     
<!-- ======================================================================= -->
<!--                      Dati del Decreto di Computo                        -->
<!-- ======================================================================= -->
    <!-- =================================================================== -->
    <!--      SEZIONE CON I DATI DEL DECRETO DI COMPUTO/ALTRO UFFICIO        -->
    <!-- =================================================================== -->
    <table width=95%>
      <tr>
        <td class="Titolo" colspan=10> Provvedimento</td>
      </tr>
      <tr>
        <td class="l">Provvedimento :&nbsp;</td>
        <td class="L">
          <% if (  aEventoAltraAut.getIdEvento()==null) {%>
          <font class="campo">D'ufficio</font>
          <% } else { %>
          <font class="campo">In esecuzione di provvedimento altro Ufficio</font>
          <% } %>
        </td>
        <% if (  aEventoAltraAut.getIdEvento()==null) {%>
        <td class="l">Oggetto :&nbsp;</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(aEventoComputo.getDescrMotivo())%></font>
        </td>
        <% } %>
      </tr>  
      <tr>
        <td class="l">Nota :&nbsp;</td>
        <td class="L" colspan="100%">
          <font class="campo"><%=StringUtils.toStringJSP(aCampoNota.getDescr())%></font>
        </td>
      </tr>
    </table>
     
    <% if (  aEventoAltraAut.getIdEvento()!=null) {%>
    <!-- Sezione con i dati del provvedimentio Altra Autorità -->
    <table style="width: 95%;">
      <tr>
        <td colspan="100%" class="titolo">Dati Provvedimento Altra Autorità</td>
      </tr>

      <tr>
        <td class="l" >Provvedimento emesso da</td>
        <td class="l" colspan="100%">
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrUfficioEmittente(),"")%></font>
          di 
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrLuogoEmittente(),"")%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Data ricezione provvedimento</td>
        <td class="l" colspan="100%"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataRicezioneAtti(), "dd-MM-yyyy") )%></font></td>
      </tr>
      <tr>
        <td class="l">Data emissione provvedimento</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataEmissione(), "dd-MM-yyyy") )%></font></td>
        <td class="l">Anno / Numero Provvedimento</td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getAnnoProtocollo(),"")%></font>/
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getProgrProtocollo(),"")%></font>
        </td>
      </tr>
  
      <tr>
        <td class="l">Tipo Provvedimento</td>
        <td class="l" colspan="100%">
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrTipoProvvedimento(),"")%></font>
        </td>
      </tr>

      <tr>
        <td class="l">Oggetto Provvedimento</td>
        <td class="l" colspan="100%">
          <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrMotivo(),"")%></font>
        </td>
      </tr>
    </table>
    <% } %>


     
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
           <a href="Javascript:ListaMagistrati('LoadInserisciComunicazione');">
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
   <!-- Detenuto o in Sospensione Misura Alternativa
   <!--   ==> Istituto di Detenzione
   <!--   ==> Autorità Competente per la Notifica (C) 
   <!-- Arresti Domiciliari (04)
   <!--   ==> Autorità Competente per la Notifica (C)
   <!-- In Misura Alternativa
   <!--   ==> Autorità Esterna per l'esecuzione (E)
   <!--   ==> Autorità Esterna per il Controllo (C)
   <!--   ==> UEPE (CSSA)
   <!--   ==> Magistrato di Sorveglianza
   <!--   ==> Tribunale di Sorveglianza
   <!-- ==================================================================== -->
   
    <table width=100%>
      <tr>
        <td class="Titolo" colspan=6>Destinatari </td>
      </tr>
      <% if (   lPosizione.getCodPosizioneGiuridica().equals("01")
             || lPosizione.getCodPosizioneGiuridica().equals("02")
             || lPosizione.getCodPosizioneGiuridica().equals("03")
             || lPosizione.getCodPosizioneGiuridica().equals("23")
             || lPosizione.isMisSosp()
           ) 
      {  %>
      <tr>
        <td class="l" width=20%>Istituto di Detenzione </td>
        <% if(lLuogoDetenzione.getIstitutoDetenzione() == null){%>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciComunicazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
        <%} else {%>
            <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" >
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciComunicazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
                <img src="/images/filefolder.gif" border=0>
              </a>
            </td>
        <%}%>

        <td class="l">Note</td>
        <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
        </td>
      </tr>
      <% } %>
      
      <% // Autorità per l'esecuzione
      if(lPosizione.isMisuraAlternativa())
      {
      %>
        <tr>
          <td class="l">Autorità per l'esecuzione </td>
          <td class="L" colspan="3">
            <select  Title="Autorita destinazione"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
            <%=autoritaEsternaE%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Sede</td>
          <td class="L">
            <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciComunicazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
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
      %> 
        
      <% 
      //========================================================================
      // Autorità per la Notifica/per il controllo 
      // n.b. è sempre COD_TIPO_NOTIFICA = C cambia solo l'etichetta ed è 
      //      sempre presente
      //========================================================================
      %>
      <tr>
      <% if(lPosizione.isMisuraAlternativa()) { %>
        <td class="l">Autorità di controllo </td>
      <% } else { %>
        <td class="l">Altra Autorità</td>
      <% } %>
        <td class="L" colspan="3">
          <select  Title="Autorità"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
            <%=autoritaEsternaAltra%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede</td>
        <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciComunicazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"  cols=30 ></textarea>
        </td>
      </tr>
      

      <%
      if(lPosizione.isMisuraAlternativa() )
      { // UEPE, MDS e TDS
      %>
      <%-- MAC20191129017-MG-04/12/2019: modificato layout --%>
      <tr>
		<td class="Titolo" colspan="6">UEPE/USSM</td>
	  </tr>
	  
	  <tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
		<td class="l" colspan="3"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l" colspan="3">
			<input readonly title="Sede UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
	        <input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
	      	<input type="hidden" name="cssaE" value="S">
	       	<a href="Javascript:ListaCSSAMinor('LoadInserisciComunicazione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
	        	<img src="/images/filefolder.gif" border=0>
	       	</a>
		</td>
	</tr>
	
	<tr>
		<td class="Titolo" colspan="6">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
        <td class="L" colspan="3">
      		<input type="hidden" name="magSorv" value="S">
       		<input type="hidden" name="notificaMagistrato" value="C">
       		<input title="ufficio" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_MDS %>" maxlength="35" size="25">
       		<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciComunicazione','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
	
	 <tr>
		<td class="Titolo" colspan="6">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
      <tr>
	   	<td class="l">Sede</td>
	   	<td class="L" colspan="3">
          <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciComunicazione','<%= ICostantiNotifica.CAMPO_SEDE_TDS %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
	
      <%-- FINE MAC20191129017-MG-04/12/2019: modificato layout --%>
      
    
      <%      
      } 
      %>


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
              <a href="Javascript:ListaComuni('LoadInserisciComunicazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
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


  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</form>



<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciComunicazione");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");


  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

</script>
</body>
</html>