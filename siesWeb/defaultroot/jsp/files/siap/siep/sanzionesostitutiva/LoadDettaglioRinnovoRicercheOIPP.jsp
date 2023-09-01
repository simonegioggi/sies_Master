<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.lang.String" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"  />

<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"  />
<jsp:useBean id="rinnovo" scope="request" class="siap.siep.rinnovo.model.RinnovoModel"  />

<html>
<head>
  <title>[S.I.E.S.] - Omessa Notifica Ordini di Ingiunzione</title>
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
          <% if("R".equals(rinnovo.getCodTipoRinnovo())) {%>
          <font class="campo">Dettaglio Rinnovo Ricerche per Omessa Notifica Forza di Polizia</font>
          <%} else if("A".equals(rinnovo.getCodTipoRinnovo()) || "N".equals(rinnovo.getCodTipoRinnovo())){%>
          <font class="campo">Dettaglio Rinnovo Ricerche per Omessa Notifica Ufficiali Giudiziari</font>
          <%}%>
        </td>

        <%if (   rinnovo.getFlagDocumentoRegistrato()==null
              || "N".equals(rinnovo.getFlagDocumentoRegistrato())
             )
        {%>
        <!-- BOTTONE DI STAMPA -->
        <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaRinnovoRicercheOIPP&idrinnovo="+rinnovo.getIdRinnovo()%>"/>
        </jsp:include>
        <%}%>
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovoRicercheOIPP&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ordineIngiunzione.getEvento().getIdEvento()%>">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
        </td>
      </tr>
    </table>
</form>

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


<table>
  <% if("R".equals(rinnovo.getCodTipoRinnovo())) {%>
  <tr>
    <td class="l" width="30%">Data pervenimento del verbale</td>
    <td class="l"><font class="campo"><%=DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy")%></font> </td>
  </tr>

  <tr>
    <td class="l" >Data verbale</td>
    <td class="l"><font class="campo"><%=DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy")%></font> </td>
  </tr>

  <tr>
    <td class="l">Autorità che ha redatto il verbale</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())%></font> di
      <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font>
    </td>
  </tr>

    <% if(   rinnovo.getCodTipoAutoritaRinnovo()!=null 
          && rinnovo.getCodTipoAutoritaRinnovo().equals(verbale.getCodTipoUfficioFirmatario())
          && rinnovo.getCodLuogoRinnovo()!=null 
          && rinnovo.getCodLuogoRinnovo().equals(verbale.getCodLuogoUfficioFirmatario())
         ) {%>
      <tr>
        <td class="l" >Rinnovo stessa autorità in data </td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
      </tr>
    <%} else {%>
      <tr>
        <td class="l">Rinnovo altra autorità in data</td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
      </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font> di
          <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
        </td>
      </tr>
      <%if(rinnovo.getNote() != null){%>
           <tr>
             <td class="l">Indirizzo</td>
             <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNote())%></font> </td>
           </tr>
      <% } %>
    <% } %>


<%} else if("A".equals(rinnovo.getCodTipoRinnovo()) || "N".equals(rinnovo.getCodTipoRinnovo())){%>

    <tr>
       <td class="l" width="30%">Relata di notifica da Ufficiali Giudiziari di</td>
       <td class="L"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario())%></font></td>
       <td class="l" >in data</td>
       <td class="l">
          <font class="campo"><%=DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy")%></font> </td>
       </td>
    </tr>

    <% if ("N".equals(rinnovo.getCodTipoRinnovo())) { %>
      <tr>
        <td class="l" >Con Esito</td>
        <% if ("P".equals(rinnovo.getEsito())) { %>
        <td class="l"><font class="campo">Positivo</font></td>
        <% } else if ("N".equals(rinnovo.getEsito())) { %>
        <td class="l"><font class="campo">Negativo</font></td>
        <% } %>
      <tr>
      <tr>
        <td class="l" >Rinnovo notifica in data</td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
      <tr>
      <tr>
        <td class="l">Ufficiali Giudiziari delagati in</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font></td>
      </tr>
    <%} else if ("A".equals(rinnovo.getCodTipoRinnovo())) {%>
      <tr>
        <td class="l">Attivazione ricerca in data</td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy")%></font> </td>
      </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font> di
          <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
        </td>
      </tr>
    <% } %>


    <% if(rinnovo.getNuovoLuogoNotifica() != null) {%>
     <tr>
       <td class="l">Luogo Nuova Notifica</td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNuovoLuogoNotifica())%></font> </td>
     </tr>
<%    }
 }%>
</table>



 <br>
  <div align="left" style="visibility:hidden" id="upld">
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
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActUploadRinnovoRicercheOIPP">
            <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="<%=rinnovo.getIdRinnovo()%>">
            <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRinnovoRicercheOIPP">
          </td>
        </tr>
      </table>
</form>
</div>
</body>
</html>