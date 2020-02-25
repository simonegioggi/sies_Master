<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.produzioneatti.action.ICostantiProduzioneAtti"%>


<jsp:useBean id="evento"  scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="actRet"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioS"   scope="request" class="java.lang.String"/>
<jsp:useBean id="descTipoUfficioS"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codMotivo"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Richiesta Parere</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
    function lookUpload()
    {
      var node;
      node = document.getElementById('upld');
			node.style.visibility='visible';
    }
    </script>
  </head>

  <%
  if(evento.getFlagDocumentoRegistrato()!= null)
  {
    if (evento.getFlagDocumentoRegistrato().compareTo("N") == 0)
    {
  %>
      <BODY class="corpo" onload="javascript:lookUpload();">
  <%
    }
    else
    {
  %>
      <BODY class="corpo">
  <%
    }
  }
  else
  {
  %>
      <BODY class="corpo">
  <%
  }
  %>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Esito Parere</font>
        </td>
  <%
  if ( (evento.getFlagDocumentoRegistrato()==null ) || (evento.getFlagDocumentoRegistrato().compareTo("N")==0))
    {
  %>
    <td class="LBG">
      <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.produzioneatti.action.ActStampaProduzioneAtti&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=evento.getIdEvento()%>')" onclick="javascript:lookUpload();">
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
    </td>
  <%
    }
%>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

  </table>

  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>


  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Data Richiesta Parere</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

  <tr>
      <td class="l">Destinatario: </td>
      <td class="L"> <font class="campo"><%=descTipoUfficioS%></font> </td>
        <td><input type="hidden" Title="codDestinatario" name="<%=ICostantiProduzioneAtti.CAMPO_COD_DESTINATARIO%>" value="<%=codTipoUfficioS%>" size="5"></td>
  </tr>
  <tr>
    <td class="l">Tipo Parere</td>
    <td class="L" colspan="5">
      <font class="campo"><%=codMotivo%></font>&nbsp;
    </td>
  </tr>

   <tr>
      <td class="l">Data Emissione Parere</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataRicezioneAtti(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

<%
String lEsitoParere = "";
if (evento.getCodEsito().compareTo("0754")==0)
{
   lEsitoParere = "Non esprime parere";
}
else if (evento.getCodEsito().compareTo("0753")==0)
{
   lEsitoParere = "Parzialmente favorevole";
}
else if (evento.getCodEsito().compareTo("0752")==0)
{
   lEsitoParere = "Contrario";
}
else if (evento.getCodEsito().compareTo("0751")==0)
{
   lEsitoParere = "Favorevole";
}
else
{
   lEsitoParere = "";
}
%>

  <tr>
      <td class="l">Esito Parere</td>

       <td class="L">
        <font class="campo"><%=lEsitoParere%></font>&nbsp;
      </td>

  </tr>
 </table>

  <%
    String lAzione;
    // Azione da chiamare per l'inserimento dei dati.
    if (actRet.compareTo("")!= 0 )
    {
      lAzione = actRet;
    }
    else{
      lAzione = "siap.sius.produzioneatti.action.ActLoadFSPEsitoParereInamm";
    }
  %>

  <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
    <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input class="bottone"  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.produzioneatti.action.ActLoadDettaglioEsitoParereInamm">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=evento.getIdEvento()%>">
        </td>
      </tr>
    </table>

    </FORM>
    </div>
    <br>
  </body>

</html>