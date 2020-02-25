<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.stralcio.action.ICostantiStralcio" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="eventoStralcio" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="fascicoloStralcio" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="fascicoloDestStralcio" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="dataStralcio" scope="request" class="java.lang.String"/>
<jsp:useBean id="annoFascicoloDestStralcio" scope="request" class="java.lang.String"/>
<jsp:useBean id="progrFascicoloDestStralcio" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagInsert" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Stralcio </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript" >
    function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
      node.style.visibility='visible';
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : </font>
        <font class="campo">Dettaglio Stralcio Oggetti</font>&nbsp;
      </td>
<%
      if( eventoStralcio.getFlagDocumentoRegistrato() == null  ||
          eventoStralcio.getFlagDocumentoRegistrato().compareTo("N")==0 )
      {
        if (!flagInsert.trim().equals("Y") )
        {
%>
          <!-- BOTTONE DI CANCELLAZIONE DA ELENCO -->
          <td class="LBG">
            <a href="Javascript:conferma1('siap.sius.stralcio.action.ActCancellaStralcio','<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloStralcio.getFascicoloSiusModel().getIdFascicoloSius()%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>=<%=fascicoloStralcio.getFascicoloSiusModel().getChiaveAnno()%>','<%=ICostantiStralcio.CAMPO_ID_EVENTO_STRALCIO%>','<%=eventoStralcio.getIdEvento()%>','<%=ICostantiStralcio.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.fascicolo.action.ActLoadDettaglioFascicolo');">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
            </a>
          </td>
          <!-- BOTTONE DI RITORNO -->
          <td class="LBG">
            <a href="javascript:history.go(-1);">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
       <%}
      else{%>
          <!-- BOTTONE DI CANCELLAZIONE DA INSERIMENTO -->
          <td class="LBG">
            <a href="Javascript:conferma1('siap.sius.stralcio.action.ActCancellaStralcio','<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>=<%=fascicoloStralcio.getFascicoloSiusModel().getChiaveAnno()%>&<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>=<%=fascicoloStralcio.getFascicoloSiusModel().getChiaveProgr()%>','<%=ICostantiStralcio.CAMPO_ID_EVENTO_STRALCIO%>','<%=eventoStralcio.getIdEvento()%>','<%=ICostantiStralcio.ACTION_DOPO_CANCELLAZIONE%>','siap.sius.stralcio.action.ActLoadInserisciStralcio');">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
            </a>
          </td>

      <%}
      }%>
    </tr>
  </table>

  <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
		<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Label" colspan="2"><font class="label">Dati di stralcio :&nbsp;</font></td>
   </tr>
    <tr>
      <td class="L"><font class="label" >Data Stralcio</font></td>
      <td class="L"><font class="campo"><%=DateUtils.getDateToString(eventoStralcio.getDataEmissione(),"dd-MM-yyyy")%></font></td>
   </tr>
  </table>
		<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo" colspan="2"><font class="label">Oggetti stralciati nel procedimento&nbsp;&nbsp;</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicoloDestStralcio.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>">
          <%=fascicoloDestStralcio.getFascicoloSiusModel().getChiaveAnno()%>
          /
          <%=fascicoloDestStralcio.getFascicoloSiusModel().getChiaveProgr()%>&nbsp;
        </a>
			</td>

    </tr>
<%
    int lSize = fascicoloDestStralcio.getTenori().length;
    for( int x=0; x<lSize; x++ )
    {
    if ((fascicoloDestStralcio.getTenori()[x].getData()!= null) &&
        (fascicoloDestStralcio.getTenori()[x].getData().compareTo(eventoStralcio.getDataEmissione())==0))
    	{
%>
    	<tr>
      	<td class="l" colspan="2">
        <font class="label">
            <%=fascicoloDestStralcio.getTenori()[x].getDescrOggettoTenore()%><BR>
        </font></td>
    	</tr>
<%		}
		}
    if (!(lSize>0)) {%>
    	<tr>
      		<td class="Label" colspan="2">&nbsp;-&nbsp;</td>
      	</tr><%}
%>
    <tr>
      <td class="Label" colspan="2"><font class="label">&nbsp;</font></td>
    </tr>
  </table>

 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
    <tr>
      <td class="L">
       <input class="bottone"  type="submit" value="Conferma">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
       <input type="HIDDEN" name="IdEvento"  value="<%=eventoStralcio.getIdEvento()%>">
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.stralcio.action.ActLoadDettaglioStralcio">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>
</html>