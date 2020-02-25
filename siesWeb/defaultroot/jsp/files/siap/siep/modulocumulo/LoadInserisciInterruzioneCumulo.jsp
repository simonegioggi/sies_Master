<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="aProvvedimento" scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="motivointerruzione" scope="request" class="java.lang.String"/>


<% 

//============================================================================== 
// Form per l'inserimento e la modifica dei Provvedimenti di annotazione 
// Interruzione
//============================================================================== 
ComputiCumuloModel aComputo = new ComputiCumuloModel();

if ( modalita.equals("M") )
  aComputo = aProvvedimento.getListaComputi().elementAt(0);
%> 

<html>
<head>
  <title> Gestione Provvedimento Interruzione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

  <script language="JavaScript" >
    var desktop;
  
      function eseguiFunzione(action)
      {
        document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
        document.f.submit();
      }

    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  

      function Verify()
      {
        //DATA RICEZIONE PROVVEDIMENTO (non obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data interruzione pena non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA %>.focus();

          return false;
        }
      return true;
      }
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <% if( modalita.equals("I") ) { %>
        <font class="campo">Inserimento annotazione Interruzione &nbsp;</font>
        <% } else if( modalita.equals("M") || modalita.equals("NP") ) { %>
        <font class="campo">Modifica annotazione Interruzione &nbsp;</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaInterruzioneCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciInterruzioneCumulo">
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  <input type="HIDDEN" name="operazione" value="">
  
  <input type="HIDDEN" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(aProvvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="HIDDEN" name="<%= ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO %>"                       value="<%=StringUtils.toStringJSP(aComputo.getIdComputiCumulo()) %>">
  <input type="HIDDEN" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>" 					  value="<%=StringUtils.toStringJSP(aProvvedimento.getFlagStato()) %>">

  <input type="HIDDEN" name="<%=ICostantiComputiCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" >


  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td colspan="4" class="titolo">Dati del provvedimento di Interruzione dell'esecuzione della pena</td>
    </tr>
    <tr>
      <td class="l" width="20%">
        Data interruzione esecuzione <font class="ob">(*)</font>
      </td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno interruzione esecuzione" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(),"dd"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese interruzione esecuzione"
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>"
          maxlength="2" size="2"
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(),"MM"))%>"
          <%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno interruzione esecuzione" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataSospensioneInterruzione(),"yyyy"))%>"  
          <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
  </tr>

  <tr>
      <td class="l" width="20%">
        Data comunicazione evento
      </td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno comunicazione evento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"dd"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese comunicazione evento"
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>"
          maxlength="2" size="2"
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"MM"))%>"
          <%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno comunicazione evento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataEmissioneProvv(),"yyyy"))%>"  
          <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
  </tr>
  
  <tr>
      <td class="l" width="20%">
        Data ricezione evento
      </td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno ricezione evento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"dd"))%>"  
          <%=IWebConstants.UTIL_DATA%>>
        /
        <input type="text" Title="Mese ricezione evento"
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>"
          maxlength="2" size="2"
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"MM"))%>"
          <%=IWebConstants.UTIL_DATA%>> /
        <input type="text" Title="Anno ricezione evento" 
          name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="4" size="4"  
          value="<%=StringUtils.toStringJSP (DateUtils.getDateToString(aComputo.getDataRicezioneProvv(),"yyyy"))%>"  
          <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
  </tr>

    <tr>
      <td class="l"> Protocollo </td>
      <td class="l">
        <input type="text" Title="Protocollo" value="<%=StringUtils.toStringJSP(aComputo.getProtocollo())%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_PROTOCOLLO %>" maxlength="35" size="35" >
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità emittente  
      </td>
      <td class="l">
        <input type="text" Title="Autorita Esterna" value="<%=StringUtils.toStringJSP(aComputo.getAltraAutorita())%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRA_AUTORITA %>" maxlength="35" size="35" >
      </td>
    </tr>
    <tr>
      <td class="l">
         Luogo 
      </td>
      <td class="l">
          <input title="Sede Autorita Esterna"  type="text" value="<%=StringUtils.toStringJSP( aComputo.getAltroLuogo() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRO_LUOGO%>"  maxlength="35" size="35">
      </td>
    </tr>
    <tr>
      <td class="l">
        Motivo dell'interruzione <font class="ob">(*)</font>
      </td>
      <td class="l">
        <select Title="Motivo dell'interruzione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>">
        <%=motivointerruzione%>
        </select>
      </td>
    </tr>

  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l" colspan="3">
      <textarea cols="60" rows="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP( aComputo.getNote() )%></textarea>
    </td>
  </tr>
  </table>

  <table cellspacing="2" cellpadding="2" width="95%" align="center">
  <tr><td>&nbsp;</td></tr>
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

</form>
</body>
</html>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");
  
  // Data Interruzione Esecuzione
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","req","Il campo Giorno interruzione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","lt=31");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","req","Il campo Mese interruzione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","req","Il campo Anno interruzione esecuzione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","maxlen=4","La lunghezza massima per l'anno interruzione esecuzione è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","minlen=4","La lunghezza minima per l'anno interruzione esecuzione è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","lt=3000");

    // Data emissione provvedimento
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=31");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

    // Data ricezione provvedimento
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=31");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno ricezione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno ricezione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=3000");
  

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>