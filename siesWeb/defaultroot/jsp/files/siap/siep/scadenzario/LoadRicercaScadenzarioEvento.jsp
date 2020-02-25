<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="vettore"    scope="request" class="java.util.Vector"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    function Verify()
	  {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value.length != 0)
           && (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value.length == 0) )
      {
        alert("Valorizzare Anno ricerca");
        return false;
      }
      if( (document.f.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value.length == 0)
           && (document.f.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value.length != 0) )
      {
        alert("Valorizzare Numero  ricerca");
        return false;
      }
    }
  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Ordine Esecuzione per Avvenuta Notifica</font></td>
    </tr>
  </table>

  <br>
<div align=center>
  <table>

    <tr>
      <td class="int">Anno Protocollo</td>
      <td class="int">Data Emissione</td>
      <td class="int">Motivo</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = vettore.iterator();
  while ( itx.hasNext())
  {
    EventoModel evento = (EventoModel)itx.next();
%>
    <tr>
      <td class="l"><%=evento.getAnnoProtocollo() %>/<%=evento.getProgrProtocollo()%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</td>
      <td class="l"><%=evento.getDescrMotivo()%>&nbsp;</td>

     <td class="l">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=evento.getIdEvento()%>"/>
        </jsp:include>
      </td>
  </tr>
<%
  }
%>
  </table>
</div>
</body>
</html>