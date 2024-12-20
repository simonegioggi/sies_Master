<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>


<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"  />

<jsp:useBean id="rinnovo" scope="request" class="siap.siep.rinnovo.model.RinnovoModel"  />
<jsp:useBean id="avvocato" scope="request" class="siap.siep.avvocato.model.AvvocatoModel"  />

<html>
<head>
  <title>[S.I.E.S.] - Richiesta Informazioni comma 5 </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript">
    function CancellaRichiesta(idRinnovo){
      if (window.confirm("Confermi l'eliminazione della Richiesta?")) {
        document.cancellaRichiesta.<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>.value = idRinnovo;
        document.cancellaRichiesta.submit();
      }
    }
  </script>
</head>


<body class="corpo">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Richiesta Informazioni Comma 5 &nbsp;</font>
      </td>
      
      <%if ( "N".equals(rinnovo.getFlagDocumentoRegistrato()) || rinnovo.getFlagDocumentoRegistrato()==null ) { %>
         <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
           <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaRichInfoComma5&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+rinnovo.getIdRinnovo()%>"/>
         </jsp:include>
        <td class="LBG">
          <a href="Javascript:CancellaRichiesta('<%=rinnovo.getIdRinnovo()%>')">
            <img align="middle" src="/images/delete24.gif" alt="cancella" width="24" height="24" border="0"></a>      
        </td>           
      <%}%>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRichInfoComma5&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=ordineIngiunzione.getEvento().getIdEvento()%>">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>
    </tr>
  </table>

<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="cancellaRichiesta">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActCancellaRichInfoComma5">
    <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="">
  </form>
  
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

<br>
 
<% if (rinnovo.getCodTipoRinnovo().equals("D")) { %>
<table width="30%">
    <tr>
      <td class="l" width=25%>Avvocato</td>
      <td class="l">
        <font class="campo"><%=avvocato.getCognome()%>&nbsp;<%=avvocato.getNome()%></font>
        &nbsp; Foro di &nbsp;<font class="campo"><%=avvocato.getForo()%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Tipo Difensore</td>
      <td class="l">
        <font class="campo"><%=avvocato.getDescrTipo()%></font>
      </td>
    </tr>

    <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy"))%></font>
       </td>
    </tr>
    
    <tr>
      <td class="l">Autorita' di polizia delegata</td>
      <td class="l" colspan="3">
       <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font>
      di <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font></td>
    </tr>
    <tr>
       <td class="l">Indirizzo</td>
       <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNote())%>&nbsp;</font>
       </td>
   </tr>
</table>
<% }%>

<% if(rinnovo.getCodTipoRinnovo().equals("I")) {%>
<table width="30%">
  <tr>
    <td class="l" >Data Richiesta</td>
    <td class="l">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(rinnovo.getDataRinnovo(),"dd-MM-yyyy"))%></font>
    </td>
  </tr>

  <tr>
    <td class="l">Autorita' di polizia delegata</td>
    <td class="l" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrTipoAutoritaRinnovo())%></font>
      di <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getDescrLuogoRinnovo())%></font>
    </td>
  </tr>

  <tr>
    <td class="l">Indirizzo</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(rinnovo.getNote())%>&nbsp;</font>
    </td>
  </tr>
</table>
<%}%>

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
          <td class="l" rowspan="2">Richiesta da Salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiRinnovo.CAMPO_DOC_BLOB%>"></font>
          </td>
        </tr>
        <tr>
          <td class="L">
            <input class="bottone"  type="submit" value="Conferma" >
            <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="<%=rinnovo.getIdRinnovo()%>">

            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActUploadRichInfoComma5">
            <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRichInfoComma5">
          </td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>
