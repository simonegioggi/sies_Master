<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.sico.utente.action.ICostantiUtente" %>
<%@ page import="siap.siep.pagoPA.action.ICostantiErroriSiesPagopa" %>
<%@ page import="siap.siep.pagoPA.model.ErroriSiesPagopaModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="ListaErrori"          scope="request" class="java.util.Vector" />
<jsp:useBean id="CriteriRicerca"       scope="request" class="siap.siep.pagoPA.model.ErroriSiesPagopaModel" />

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Errori PagoPa- </title>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  function Verify()
  {    
    return true;
  }
  
  function eliminaErrore (idErr) {
	  if (window.confirm('Confermi la cancellazione del log di errore?')) {
		    window.location.href="/jsp/Main.jsp?Action=siap.siep.pagoPA.action.ActCancellaErroriPagopa&<%=ICostantiErroriSiesPagopa.CAMPO_ID_ERRORI_SIES_PAGOPA%>=" +idErr;
		}
  }
    
  </script>
  <style>
    td.int,td.c {
      padding-left: 10px;
      padding-right: 10px;
    }
  </style>
</head>

<body class="corpo">

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="dettaglioErroriBatchPagoPa">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPaBatch.action.ActVisualizzaBatchPagoPa">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">CRUSCOTTO ERRORI COLLEGAMENTI WEB SIES-PST</font>&nbsp;</td>
        <% if ("90".equals(UtenteConnesso.getUserProfile().getProfileId().toString())) { %>
        <td class="LBG">
          <a href="<%= IWebConstants.PG_MAIN%>?Action=siap.siep.pagoPA.action.ActLoadVerificaErroriPagopa">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>  
        <% } %>              
      </tr>
    </table>

    <br>
    
    <% if ("90".equals(UtenteConnesso.getUserProfile().getProfileId().toString())) { %>
    <table>
      <tr><td class="titolo" colspan="4">Criteri Selezionati</td></tr>
      <tr>
        <td class="L">Data Errore Iniziale:</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInserimento(),"dd-MM-yyyy"),"__-__-____")%></font></td>
        <td class="L">Data Errore Finale:</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(CriteriRicerca.getDataInserimentoAl(),"dd-MM-yyyy"),"__-__-____")%></font></td>
      </tr>
      <tr>
        <td class="L">Funzione in Errore:</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(CriteriRicerca.getDescrizioneFunzione())%></font></td>
      </tr>
      <tr>
        <td class="L">Tipologia Utente:</td>
        <% if (CriteriRicerca.getCodUtente()!=null && CriteriRicerca.getCodUtente().length()>0 ) {%>
        <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(CriteriRicerca.getCodUtente())%></font></td>
        <% } else { %>
        <td class="l" colspan="3"><font class="campo">Tutti</font></td>
        <% } %>
      </tr>
    </table> 
    <% } %>
    
    <br>        
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
    <br>
    
    <%
    // Visualizzo tabella con l'esecuzione degli ultimi 10 lanci
    %>
    
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="int">Numero SIEP</td>
        <td class="int">Cognome Nome</td>
        <td class="int">Estremi Provvedimento</td>
        <td class="int">Utente</td>
        <td class="int">Funzione in Errore</td>
        <td class="int">Data Errore</td>
        <td class="int">Tipo Errore</td>
        <td class="int">Azioni</td>
      </tr>
      
      <% 
      for (int i = 0; i< ListaErrori.size(); i++) 
      { 
        ErroriSiesPagopaModel erroreModel = (ErroriSiesPagopaModel) ListaErrori.elementAt(i);

        String parametri = "&IdEvento="+erroreModel.getIdEvento()+"&idFascicoloSiep="+erroreModel.getIdFascicoloSiep()+"&fromListaErrori=SI";

      %>
      <tr>
		    <td class="c">
		      <% if ("90".equals(UtenteConnesso.getUserProfile().getProfileId().toString())) { %>
	        <%=StringUtils.toStringJSP(erroreModel.getChiaveAnno()) + " / " + StringUtils.toStringJSP(erroreModel.getChiaveProgr())%>
		      <% } else { %>
	        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=erroreModel.getIdFascicoloSiep()%>" title="Procedimento">
            <%=StringUtils.toStringJSP(erroreModel.getChiaveAnno()) + " / " + StringUtils.toStringJSP(erroreModel.getChiaveProgr())%>
          </a>	      
		      <% } %>
		    </td>        
        <td class="c"><%=StringUtils.toStringJSP(erroreModel.getCognome())%>&nbsp;<%=StringUtils.toStringJSP(erroreModel.getNome())%></td>
        <td class="c"><%=StringUtils.toStringJSP(erroreModel.getDescMotivoEvento())%></td>
        <td class="c"><%=StringUtils.toStringJSP(erroreModel.getCodUtente())%></td>
        <td class="c"><%=StringUtils.toStringJSP(erroreModel.getDescrizioneFunzione())%></td>
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(erroreModel.getDataInserimento(),"dd-MM-yyyy - HH:mm:ss") ,"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(erroreModel.getErroreEsecuzione())%></td>
        
        <td class="c">
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=erroreModel.getAzioneContestoJava()+parametri%>">
            <img alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"></a>
          <a  href="javascript:eliminaErrore('<%=erroreModel.getIdErroriSiesPagopa()%>')">
            <img alt="Cancella" src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border="0"></a>
        </td>
      </tr>
      <% } %>

    </table>    
    
    
  </form>

 <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("dettaglioErroriBatchPagoPa");  
  frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>

</html>




