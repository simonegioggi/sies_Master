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
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="ordinanze"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>


<html>
<head>
  <title>[S.I.E.S.] - Lista Ordinanze</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Deposito Ordinanze</font>
      </td>
    </tr>
   </table>
<br>
<%
  if (fascicoloSiusGP != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
      </tr>
   </table>
<%
  } // endif fascicoloSiusGP
%>
<br>
<%
  if ( ordinanze.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Non ci sono ordinanze allegate al fascicolo. </font>
        </td>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="96%">
  <div align=center>
    <tr>
      <td class="int" width=14%>Data emissione</td>
      <td class="int" width=59%>Motivo provvedimento</td>
      <td class="int" width=20%>Esito provvedimento</td>
      <td class="int" width=7%>Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = ordinanze.iterator();
    while ( itx.hasNext())
    {
      EventoModel lProv = (EventoModel)itx.next();
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></td>
      <td class="l" ><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></td>
       <td class="l">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lProv.getIdEvento()%>" />
        </jsp:include>
       </td>
    </tr>
  <%
   } // endwhile
  %>
  </table>
    <input type="HIDDEN" name= >
    </FORM>
<%
  }  // endif decreti.size()
%>
  </body>
</html>