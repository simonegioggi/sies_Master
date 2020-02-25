<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="atti"     scope="request" class="java.util.Vector"/>

<html>
<head>
  <title>[S.I.E.S.] - Lista Atti Istruttori Richiesti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

<link rel="STYLESHEET" type="text/css" href="/css/style.css">

  <br>
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">

    <table cellspacing=2 cellpadding=2>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label"> Elenco Richieste Atti Istruttori </font>
        </td>
      </tr>
    </table>
  <br>
<%
  if ( atti.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Non ci sono richieste per il procedimento. </font>
        </td>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="100%">
  <div align=center>
    <tr>
      <td class="int" width="50%">Tipo atto istruttorio richiesto</td>
      <td class="int" width="20%">Data richiesta</td>
      <td class="int" width="23%">Data ricezione</td>
      <td class="int" width="7%">Azioni</td>
    </tr>
  </div>
  </table>
<%
    Iterator itx = atti.iterator();
    int i=0;

    while ( itx.hasNext())
    {
      EventoModel atto = (EventoModel)itx.next();
%>
    <table width="100%" cellpadding="0" cellspacing="0">
    <tr>
      <td class="l" width="50%"><%=atto.getDescrMotivo()%></td>
      <td class="c" width="20%">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="c" width="23%">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataRicezioneAtti(),"dd-MM-yyyy"),"-")%>
      </td>
       <td class="l" width="7%">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=atto.getIdEvento()%>" />
        </jsp:include>
       </td>
    </tr>
    </table>
    <input  value="" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" type = "HIDDEN">
  <%
    i++;
   } // endwhile
  %>
    </FORM>
<%
  }  // endif atti.size()
%>
  </body>
</html>