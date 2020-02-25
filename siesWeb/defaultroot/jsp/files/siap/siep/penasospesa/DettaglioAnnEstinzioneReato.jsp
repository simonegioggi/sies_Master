<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.camponota.model.CampoNotaModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="annotazioneManuale" 	scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="strDescrTipoUfficio"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="strDescrComune"     			scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Annotazione Estinzione Reato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Annotazione Estinzione Reato</font>
    </td>
  </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table  width="95%">
  	    <tr><td class="Titolo" colspan="2">Estremi del Provvedimento</td></tr>
    <tr> 
      <td class="l" width="30%"> Data Provvedimento</td>
      <td class="l">
			<font class="campo">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(annotazioneManuale.getDataGE(),"dd-MM-yyyy"))%> 
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero Provvedimento</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(annotazioneManuale.getAnnoGe())%>
        		/
        		<%=StringUtils.toStringJSP(annotazioneManuale.getNumeroGe())%>
        	&nbsp;</font>
      </td>
    </tr>
    <tr>
      <td class="l" width="30%">Ufficio Emittente</td>
      <td class="l">
			<font class="campo">
	          <%=StringUtils.toStringJSP(strDescrTipoUfficio)%>
				&nbsp;di&nbsp;
	          <%=StringUtils.toStringJSP(strDescrComune)%>
	        	&nbsp;
	        </font>
      </td>
    </tr>
</table>
  <table  width="95%">
  	    <tr><td class="Titolo" colspan="2">Estinzione Reato</td></tr>
   <tr>
  <td class="l">Articolo</td>
  <td class="l">
    <font class="campo">
    <%=eventonotifica.getEvento().getLegge()%>
    </font>
  </td>
</tr>
   <tr>
  <td class="l">Motivazione</td>
  <td class="l">
    <font class="campo">
    <%=eventonotifica.getEvento().getDescrMotivo()%>
    </font>
  </td>
</tr>
  <tr>
  <td class="l">Note</td>
  <td class="l">
    <font class="campo">
    <%=StringUtils.toStringJSP(annotazioneManuale.getMotivazioni())%>
    </font>
  </td>
</tr>
  </table>
<%    if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
        || (   eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null
                && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
               ) 
           )
        { %>
 <br>
  <div align=left id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichiestaGenerica">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.penasospesa.action.ActDettaglioAnnotazioneTermini">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <%} %>
  <br>
</body>
</html>