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
  <title>[S.I.E.S.] - Dettaglio Notizie Adempimento degli Obblighi </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script>
    function RichiediRevoca(){
      	document.getElementById('TipoRevo').value="noObblighi";

      	document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penasospesa.action.ActLoadInserisciRichiestaRevoca";
		document.f.submit();
      }
      
  </script>
</head>

<body class="corpo">
<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Annotazione Adempimento degli Obblighi </font>
    </td>

    <%
    //=========================================================================  
    // Visualizzazione del BOTTONE DI STAMPA se evento non ancora validato
    //=========================================================================  
    if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
        || (   eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null
            && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
           ) 
       )
    {
    %>
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiestaGenerica&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
    <%
    } 
    %>
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
  <tr>
  <td class="l">Oggetto</td>
  <td class="l">
    <font class="campo">Adempimento obblighi imposti (ex art.165 cp)
    </font>
  </td>
</tr>
<tr>
</tr>
	 <tr>
      <td class="l">Decisione</td>
      <td class="l">     
      <font class="campo">
    <%=annotazioneManuale.getMotivazioni()%>
    </font>
      </td>
      </tr>


</table>

<br>

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- <% --%>
<!-- =========================================================================   -->
<!-- Visualizzazione del BOTTONE DI Richiesta Revoca se evento validato -->
<!-- =========================================================================   -->
<!-- if (  eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null -->
<!-- && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0  -->
<!-- && annotazioneManuale.getMotivazioni().contains("non ha")) -->
<!-- { -->
<!-- %> -->
<%-- 
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="TipoRevo" value="">
<table>
      <td class="L">
        <INPUT class="bottone" type="button" name="Revoca" value="Richiesta Revoca" onClick="javascript:RichiediRevoca();">
      </td>
</table>
</form>
 --%>
<%-- <% //} %> --%>

<br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadRichiestaGenerica">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.penasospesa.action.ActDettaglioAnnAdempObblighi">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>