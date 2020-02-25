<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.sico.util.CalendarUtil"%> 

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="posizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        		scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="tipoAutorita"          scope="request" class="java.lang.String"/>
<jsp:useBean id="checkCalcoloPena"      scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la produzione della Comunicazione Nuovo Residuo Pena delle 
// Sanzioni Sostitutive
//==============================================================================
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione       = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel         lAltraCausa      = posizioneluogoaltra.getAltraCausa();

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
  <!-- Load Inserisci Richiesta Revoca Espulsione  -->
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Verifica che i dati digitati in maschera siano coerenti prima di 
    // sottomettere la richiesta
    //==========================================================================
    function Verifica(){
      //==============================================
      // Verifica la correttezza della Data Emissione
      //==============================================
      if (     document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value!=""
            || document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").value!=""	
            || document.getElementById("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>").value!=""	
          )
      {
        if (document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value.length<2 && document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value.length!=0)
          document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value="0"+document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value;
        if (document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").length<2 && document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").length!=0)
          document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").value="0"+document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").value;
          
        var dataEmissione=document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").value +"/"+document.getElementById("<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>").value+"/"+document.getElementById("<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>").value;
        
        if (! ControllaData(dataEmissione))
        {
          alert('Data Emissione non valida');
          document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").focus();
          return false;
        }
      }      
      else 
      {
          alert('Data Emissione Obbligatoria');
          document.getElementById("<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>").focus();
          return false;
      }   


      //==================================
      // Data Trasmissione
      //==================================
      if (     document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value!=""
            || document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").value!=""	
            || document.getElementById("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>").value!=""	
          )
      {
        if (document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value.length<2 && document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value.length!=0)
          document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value="0"+document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value;
        if (document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").length<2 && document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").length!=0)
          document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").value="0"+document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").value;
          
        var dataTrasmissione=document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").value +"/"+document.getElementById("<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>").value+"/"+document.getElementById("<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>").value;
        
        if (! ControllaData(dataTrasmissione))
        {
          alert('Data Trasmissione non valida');
          document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").focus();
          return false;
        }
      }      
      else 
      {
          alert('Data Trasmissione Obbligatoria');
          document.getElementById("<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>").focus();
          return false;
      }
      
     
      //===============================
      // Magistrato
      //===============================
      if(document.getElementById("<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>").value == "")
      {
         alert("Magistrato Obbligatorio");
         document.getElementById("<%=ICostantiMagistrato.CAMPO_COGNOME%>").focus();
         return false;
      }
      
      //================================
      // Sede Magistrato Di Sorveglianza
      //================================
      if(trimStringa(document.getElementById("<%=ICostantiUfficio.CAMPO_COD_COMUNE%>").value) == "")
      {
         alert("Sede Magistrato di Sorveglianza Obbligatoria");
         document.getElementById("<%=ICostantiUfficio.CAMPO_COD_COMUNE%>").value = "";
         document.getElementById("<%=ICostantiUfficio.CAMPO_COD_COMUNE%>").focus();
         return false;
      }
    
      //==================================
      // Altra Autorità (non obbligatoria)
      //==================================
      if(document.getElementById("<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>").value != "-")
      {
        if(document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>").value == "")
        {
           alert("Sede Altra Autorità obbligatoria");
           document.getElementById("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>").focus();
           return false;
        }
      }
      
      return true;
    }
  
    //==========================================================================
    // 
    //==========================================================================
    var desktop;
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio) {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    //==========================================================================
    // Chiede all'utente se rieffettuare il primo calcolo pena
    //==========================================================================
    function checkCalcoloPena()
    {
      var msgConfirm = "Se non si è provveduto a rieffettuare il calcolo pena, i computi eventualmente inseriti potrebbero non essere stati computati.\nRieffettuare il calcolo pena?"; 
      var lAzioneChiamante = "<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>=siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena";
      var lAction = "<%= IWebConstants.PG_MAIN%>"+"?"+"<%=IWebConstants.ACTION_FIELD%>"+"=siap.siep.calcolopena.action.ActLoadCalcoloPena&"+lAzioneChiamante;
      var lHref = lAction;
      //alert("lHref = "+lHref);
            
      if (window.confirm(msgConfirm))
      { // Ridirigo sul Primo calcolo pena
        document.location.href=lAction; 
      }
      else
        return false; 
    }
  </script>
</head>

<%  if (checkCalcoloPena.equals("S")){%>
<body class="corpo"  onload="checkCalcoloPena();">
<%} else {%>
<body class="corpo">
<%}%>
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Comunicazione Nuovo Residuo Pena</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  

  <%
  //============================================================================
  // Sezione con il dettaglio della pena e posizione giuridica
  //============================================================================
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
    //================================================
    // Detenuto altra causa
    //================================================
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
    {
       if( lAltraCausa.getIstitutoDetenzione()!= null) 
       { %>
       <tr>
         <td class="l">Detenuto presso </td>
         <td class="L" colspan="5"><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
         </td>
       </tr>

         <% if (lAltraCausa.getAltroLuogo()!=null) { %>
         <tr>
           <td class="l">Altro Luogo </td>
           <td class="L" colspan="5">
             <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
           </td>
         </tr>
         <% } 
       } %>
    <% 
    }
    else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
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
    %>
    
    <% 
    //===============================================================
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    //===============================================================
    if(   lPosizione.getCodPosizioneGiuridica() != null 
       && (   lPosizione.getCodPosizioneGiuridica().equals("02") 
           || lPosizione.getCodPosizioneGiuridica().equals("04") 
          ) 
      )
    {
        if(lLuogoDetenzione.getAltroLuogo() != null) 
        { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
            </tr>
        <%
        }
    }
    %>
    
    
<tr>
<%
//==============================================================================
//              Sezione per la visualizzazione della PENA
// Se non ergastolo: viene visualizzata la pena
//==============================================================================
    if(   penaresidua.getIdPenaResidua() != null
       && (    penaresidua.getFlagErgastolo() == null
            || (   penaresidua.getFlagErgastolo() != null 
                && !penaresidua.getFlagErgastolo().equals("S") 
                && !penaresidua.getFlagErgastolo().equals("D")
               ) 
          ) 
      )
    {
        //=====================
        // RECLUSIONE se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(penaresidua.getQuantumReclusione())>0)
        {
        %>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
        %>
        </tr>
   
        <tr>
        <%
        //=====================
        // ARRESTI se >0
        //=====================
        if ( CalendarUtil.getTotGiorni(penaresidua.getQuantumArresto())>0)
        {
        %>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        <%
        }
    }
    %>
    </tr>
    
    
    <%
    //============================================
    // Decorrenza/Scadenza
    //============================================
    %>
    <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
        <td class="l">Data Decorrenza Pena</td>
        <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if ( penaresidua.getFlagErgastolo() != null)
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
      %>

      
      
      <%
      // Detenuta Altra Causa
      if(   !lPosizione.isLibero()
         || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
             && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
            ) 
        )
      { 
        if  (   (penaresidua.getFlagErgastolo() == null) 
             || (   penaresidua.getFlagErgastolo() != null 
                 && !penaresidua.getFlagErgastolo().equals("S") 
                 && !penaresidua.getFlagErgastolo().equals("D")
                )
              )
        {
          if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="L" colspan=2>
                <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
            <%
            }
            else
            {
            %>
              <td class="l">Data Fine Pena</td>
              <td class="lRosso" colspan=2>
                <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
<%          }
          }
        }
      }
%>
  </tr>
  
  <tr>
    <td class="l">Sanzione Sostitutiva Da Espiare</td>
    <td class="l">
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(),"0")%></font>
      <!--font class="campo"> SS da espiare da recuperare da pena residua</font-->
    </td>
  </tr>
</table>
    
<%
//==============================================================================
//                         DATI DELLA COMUNICAZIONE
//==============================================================================
%>    

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciComNuovoResiduoPena">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciComunicazioneNuovoResiduoPena">
  <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>"  >

  <table width="100%">
    <tr><td>&nbsp;</td></tr>
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
  //============================================================================
  //                                  DESTINATARI
  //============================================================================
  %>
  <table width="100%">
    <tr>
      <td class="Titolo" colspan="100%" > Magistrato Firmatario </td>
    </tr>
  
    <tr>
      <td class="l">Magistrato Firmatario</td>
      <td class="L" colspan="100%">
        <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciComNuovoResiduoPena','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    
    <tr>
      <td class="Titolo" colspan="100%" > Destinatari </td>
    </tr>
    <tr>
      <td class="l">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Magistrato Sorveglianza" value="" type="text" name="<%= ICostantiUfficio.CAMPO_COD_COMUNE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('LoadInserisciComNuovoResiduoPena','<%= ICostantiUfficio.CAMPO_COD_COMUNE %>','UDS');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="MDS<%=ICostantiNotifica.CAMPO_NOTE %>"  cols="30"></textarea>
      </td>
    </tr>
    
    <tr>
      <td class="l" width="20%">Altra Autorità </td>
      <td class="L">
        <select  Title="Altra Autorita" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
          <%=tipoAutorita%>
        </select>
      </td>
      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="AA<%=ICostantiNotifica.CAMPO_NOTE %>"  cols="30"></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede </td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciComNuovoResiduoPena','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    
  </table>
    
    
  <table>
    <tr>
      <td>
        <INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>
  </table>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciComNuovoResiduoPena");
  frmvalidator.setAddnlValidationFunction("Verifica");
</script>

    
</form>

</body>