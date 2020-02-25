<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento" %>
<%@ page import="siap.siep.provvedimentopm.model.ProvvedimentoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants" %>
<jsp:useBean id="provvedimenti" scope="request" class="java.util.Vector" />

<html>
<head>
	<title>[S.I.E.S.] - Lista Comuni</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class=corpo>
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG>Elenco Provvedimenti</td>
    </tr>
 </table>

 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>

 <Table width="100%">
  <tr>
  <td class=int>Tipo</td>
  <td class=int>Motivo</td>
  <td class=int>Data Emissione</td>
  <td class=int>Data Trasmissione</td>
  <td class=int>Autorità Emittente</td>
  <td class=int>Azioni</td>
  </tr>
 <%

  	Iterator itx = provvedimenti.iterator();
    ProvvedimentoModel prov= new ProvvedimentoModel();
  	while ( itx.hasNext())
  	{
    prov = (ProvvedimentoModel)itx.next();
	%>
	<tr>
        <td class=l><%=StringUtils.toStringJSP(prov.getDescrTipo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(prov.getDescrMotivo(),"-")%></td>
        <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(prov.getData(),"dd-MM-yyyy"),"-")%></td>
        <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(prov.getDataTrasmissioneAtti(),"dd-MM-yyyy"),"-")%></td>
        <td class=c><%=StringUtils.toStringJSP(prov.getMagCodMagistrato(),"-")%></td>
        <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimento.CAMPO_ID_PROVVEDIMENTO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=prov.getIdProvvedimento()%>" />
        </jsp:include>
      </td>
        </tr>
	<%
	}
%>
</table>

</body>
</html>