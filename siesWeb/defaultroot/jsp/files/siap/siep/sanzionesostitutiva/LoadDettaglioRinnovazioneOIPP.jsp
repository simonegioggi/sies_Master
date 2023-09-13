<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.lang.String" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="rinnovo" scope="request" class="siap.siep.rinnovo.model.RinnovoModel"  />
<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"  />

<html>
<head>
  <title>[S.I.E.S.] - Rinnovazione Notifica comma 8 bis</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <form name="LoadOmessaNotifica" method="POST" action="/jsp/Main.jsp">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <%if(rinnovo.getCodTipoRinnovo().equals("P")) {%>
            <font class="campo">Dettaglio Rinnovazione Notifica comma 8 bis Forza di Polizia</font>
          <%}else if(rinnovo.getCodTipoRinnovo().equals("U")){%>
            <font class="campo">Dettaglio Rinnovazione Notifica comma 8 bis Ufficiali Giudiziari</font>
          <%}%>
        </td>
        <%if ("N".equals(rinnovo.getFlagDocumentoRegistrato()) || rinnovo.getFlagDocumentoRegistrato()==null ) {%>
         <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
           <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaRinnovazioneOIPP&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+rinnovo.getIdRinnovo()%>"/>
         </jsp:include>
        <%}%>
        <td class="LBG">
	        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovazioneOIPP&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ordineIngiunzione.getEvento().getIdEvento()%>">
	          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
        </td>
      </tr>
    </table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

		<table>
		  <tr>
		    <td class="L" colspan="5">
		      <font class="campo">
		            <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
		            &nbsp;
		            <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
		            &nbsp;emesso in data&nbsp;
		            <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
		      </font>
		    </td>
		  </tr>
		</table>

    <table width="90%">
      <tr>
        <td class="l">Risposta Pervenuta in Data</td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
      </tr>
      <tr>
        <td class="l">Autorità delegata alla notifica</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font> di
            <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
        </td>
      </tr>     
     
      <%if(rinnovo.getNote() != null){%>
      <tr>
        <td class="l">Indirizzo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNote())%></font> </td>
      </tr>
      <%}%>
      
      <%if(rinnovo.getNuovoLuogoNotifica() != null) {%>
      <tr>
        <td class="l">Luogo Nuova Notifica</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNuovoLuogoNotifica())%></font> </td>
      </tr>
      <% } %>
    </table>
  </form>

  <br>
 
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <tr>
          <td class="L">Valida Documento</td>
          <td class="L">
            <input type=checkbox name="<%=ICostantiRinnovo.CAMPO_VALIDA%>" value=1>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Richiesta Certificato da Salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiRinnovo.CAMPO_DOC_BLOB%>"></font>
          </td>
        </tr>
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActUploadRinnovazioneOIPP">
            <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="<%=rinnovo.getIdRinnovo()%>">
            <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRinnovazioneOIPP">
          </td>
        </tr>
      </table>
</form>
</div>
</body>
</html>




