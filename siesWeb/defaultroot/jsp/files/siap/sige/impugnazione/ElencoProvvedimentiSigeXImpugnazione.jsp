<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige" %>

<jsp:useBean id="codTipoImpugnazione"  scope="session" class="java.lang.String"/>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%
    Vector <ProvvedimentoSigeEventoModel> provvedimenti=(Vector <ProvvedimentoSigeEventoModel>)request.getAttribute("provvedimenti"); 
    String strTitolo="Elenco Provvedimenti per Ricorso";
    if (codTipoImpugnazione.equals (ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE) )
  		strTitolo = "Elenco Provvedimenti per Opposizione";
%>

<html>
<head>
  <title>[S.I.E.S.] - Lista Provvedimenti per Ricorso/Impugnazione</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;<font class="campo"><%=strTitolo%></font>
      </td>
      
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br />
 <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
</table>

  <br />
<%  
  if (provvedimenti.size() == 0 )
  {
%>
    <td class="LBG">
      <font class="label"> Non ci sono provvedimenti depositati allegati al fascicolo. </font>
    </td>
<%
  }
  else
  {
%>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
    <table width="96%">
    <div align=center>
      <tr>
        <td class="int" >Data emissione</td>
        <td class="int" >Tipo provvedimento</td>
        <td class="int" >Numero provvedimento</td>

        <td class="int" >Azioni</td>
      </tr>
    </div>
<%
      for (ProvvedimentoSigeEventoModel lProv  : provvedimenti) {
%>
          <tr>
            <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
            </td>
            <td class="c" ><%=StringUtils.toStringJSP(lProv.getProvvedimento().getDescrTipoProvvedimentoSige() ,"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lProv.getProvvedimento().getChiaveAnno(),"-")%>/<%=StringUtils.toStringJSP(lProv.getProvvedimento().getChiaveProgr(),"-")%></td>

            <td class="c" >
              <jsp:include page="<%=ICostantiImpugnazioneSige.PG_BUTTONS_RICERCA_PROVVEDIMENTI_SIGE%>">
                <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
                <jsp:param name="ValoreIdEntita" value="<%=lProv.getProvvedimento().getIdProvvedimentoSige()%>" />
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