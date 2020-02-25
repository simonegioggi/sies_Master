<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="java.util.Date"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>


<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%
  String nomeM = "";
  String cognomeM = "";
  String codiceM = "";
  if( eventonotifica.getMagistrato() != null )
  { 
    cognomeM = eventonotifica.getMagistrato().getCognome();
    nomeM = eventonotifica.getMagistrato().getNome();
    codiceM = eventonotifica.getMagistrato().getCodMagistrato();
  } 
  Date data_trasmissione_iniziale = null;
  
  if( eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length>0)
  { 
    data_trasmissione_iniziale =  eventonotifica.getNotifiche()[0].getDataInvio();
  }
  
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  
     
    function stampaSiep(lAzione)
    {
        var  hrefStampa = lAzione;
        var lIndice = hrefStampa.indexOf("?");
    
        var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
    }
      
    function Verify()
    {
      var CodiceTipoEvento = <%=eventonotifica.getEvento().getCodTipoEvento() %>;
      // Controlla che il tipo Evento non sia di tipo "Pena Accessoria" codici tipo evento 16,17,18
      // per togliere il controllo di obbligatorietà sul magistrato
      if(CodiceTipoEvento != "16" && CodiceTipoEvento != "17" && CodiceTipoEvento != "18") {
        if(document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" || 
          document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Cognome e Nome del Magistrato sono obbligatori");
          return false;
        }
      }
      
      // Carica la Data Emissione e la Data Trasmissione per poi usarle nei controlli 
      var data_emissione = document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;         
      var data_trasmissione = document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
    
      // Controllo validità Data Emissione
      if (!ControllaData(data_emissione))
      {
        alert('Data Emissione non valida');
        return false;
      }
    
      // Controllo validità Data Trasmissione 
      // (n.b. presente solo se l'evento prevede destinatari, campo not null sulla tabelle NOTIFICHE)
      <% if (data_trasmissione_iniziale!=null){ %>

        if (!ControllaData(data_trasmissione))
        {
          alert('Data Trasmissione non valida');
          return false;
        }
    
        // 
        if (!CompareDate(data_emissione,data_trasmissione))
        {
          alert('La data "Data Trasmissione" deve essere maggiore della "Data Emissione"');
          return false;
        }
      <%}%>
    
      
      // Finestra di dialogo per confermare la modifica del magistrato e della data emissione
      var magistrato = document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value+' '+document.ModificaMagistratoFirmatarioValidazione.<%=ICostantiMagistrato.CAMPO_NOME%>.value;
      if (data_trasmissione=="//"){
        flag = confirm("Data Emissione: "+data_emissione+" \nMagistrato: "+magistrato); 
      }
      else {
        flag = confirm("Data Emissione: "+data_emissione+", Data Trasmissione: "+data_trasmissione+"\nMagistrato: "+magistrato); 
      }
        
      if (flag==true) 
        {return true;} 
      else 
        {return false;}   
    }
    
    function ListaMagistrati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }  
  </script>
  
</head>
<body class="corpo">
  <!-- INTESTAZIONE -->
  <FORM name="comandi">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Modifica Magistrato Firmatario</font>
        </td> 
      </tr>
    </table>
    <!-- Visualizzazione Dettagli Procedimento -->
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
  </FORM>
  <FORM name="ModificaMagistratoFirmatarioValidazione" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
  <table cellspacing=4 cellpadding=4>
    <tr>
      <td class="l"colspan="2">Visualizza il documento 
        <a href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.sico.evento.action.ActLoadDocumento&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>')">
          <img src="/images/doc_32.gif" width="24" height="24" alt="Apri Documento" border="0">
        </a>
      </td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Data Emissione</td>
        <td class="l">
          <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
          -
          <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
          -
          <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
    </tr>
    
    <% 
    // Nascondo la riga con la data trasmissione per gli eventi che non prevedono 
    // notifiche (vedi definizione procedimento --> Visto del PM)
    if (data_trasmissione_iniziale==null){%>
    <tr style="display:none">
    <% } else {%>
    <tr>
    <% } %>
      <td class="l">Data Trasmissione</td>
        <td class="l">
          <input title = "Giorno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(data_trasmissione_iniziale, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
          -
          <input title = "Mese Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(data_trasmissione_iniziale, "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
          -
          <input title = "Anno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(data_trasmissione_iniziale, "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario</td>
      <td class="l">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(cognomeM)%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(nomeM)%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>" maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('ModificaMagistratoFirmatarioValidazione');">
            <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(codiceM)%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>     
    </tr>
  </table>
  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <br>
        <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActModificaMagistratoFirmatarioValidazione">
        <input type=HIDDEN name="<%=ICostantiEvento.CAMPO_VALIDA%>" value=1>
<%
        if( "NV".equals( request.getParameter("TornaNonValidati")) )
        {
%>
              <input type="HIDDEN" name="TornaNonValidati" value="TornaNonValidati">
<%          
        }
%>
      </td>
    <tr>
      <td class="l">Indica il percorso locale del documento da salvare</td>
      <td class="L">
        <font class="campo">
          <input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
      </td>
    </tr>
    <tr>
      <td>
        <BR /><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
  </FORM> 
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("ModificaMagistratoFirmatarioValidazione");

    // CONTROLLI DATE

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