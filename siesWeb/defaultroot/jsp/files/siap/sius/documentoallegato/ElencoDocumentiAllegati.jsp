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
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel" %>

<jsp:useBean id="allegati"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Elenco Documenti Allegati al Provvedimento</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Ricerca Documenti Allegati al Provvedimento</font>
      </td>
       <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
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
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Provvedimento emesso in data: </font>
        <font class="campo"><%=DateUtils.getDateToString(evento.getDataEmissione(),"dd-MM-yyyy")%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">tipo di provvedimento: </font>
        <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrTipoProvvedimento(),"-")%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">motivo: </font>
        <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrMotivo(),"-")%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">esito: </font>
        <font class="campo"><%=StringUtils.toStringJSP(evento.getDescrEsito(),"-")%></font>&nbsp;
      </td>
    </tr>
  </table>
<br>

<%
  if ( allegati.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Non ci sono documenti allegati al provvedimento. </font>
        </td>
<%
  } else
  {
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaDocumentiAllegati">
  <table width="90%">
  <div align=center>
    <tr>
      <td class="int" width=33%>Data emissione</td>
      <td class="int" width=33%>Tipo documento</td>
      <td class="int" width=20%>Documento<br>Validato</td>
      <td class="int" width=14%>Azioni</td>
    </tr>
  </div>
<%
    Iterator itx = allegati.iterator();
    while ( itx.hasNext())
    {
      DocumentoAllegatoModel lDoc = (DocumentoAllegatoModel)itx.next();
%>
    <tr>
      <td class="l">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDoc.getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>
      <td class="l" ><%=StringUtils.toStringJSP(lDoc.getDescrTipoDocumento(),"-")%></td>
      <td class="c">
<%    String isBlob = "SI";   // flag BLOB pieno
      if (lDoc.getFlagDocumentoRegistrato()!=null)
      {
        if (lDoc.getFlagDocumentoRegistrato().compareTo("S")==0)
        {
%>
          <img src="/images/TickRed.gif">
<%       }
      }
      else
      {
        isBlob="NO";
      }
%>
      </td>

       <td class="l" >
        <jsp:include page="<%=ICostantiDocumentoAllegato.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lDoc.getIdDocumentoAllegato()%>" />
          <jsp:param name="CodTipoProvvedimento" value="<%=lDoc.getCodTipoDocumento()%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
        </jsp:include>
       </td>
    </tr>
  <%
   } // endwhile
  %>
  </table>
  </FORM>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>