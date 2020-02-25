<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<%@ page import="siap.sius.ulterioreistanza.action.ICostantiUlterioreIstanza" %>
<%@ page import="siap.sius.ulterioreistanza.model.UlterioreIstanzaModel" %>

<jsp:useBean id="ulterioriistanze" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Elenco Ulteriori Istanze</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
  <table>
    <tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    		</a>
    	</td>
      <td class="LBG">
      	<font class="label">Funzione :</font>
      	<font class="campo">Elenco Ulteriori Istanze</font> 
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
<%
  if( ulterioriistanze.size() == 0 )
  {
%>
  <table>
  	<td class="LBG">
    	<font class="label"> Non ci sono ulteriori istanze riferite al procedimento indicato.</font>
    </td>
  </table>
<% 
  }
  else
  {
%>
  <table>
    <div align="center">
      <tr>
        <td class="int" width="15%">Data Atto</td>
        <td class="int" width="15%">Tipo Atto</td>
        <td class="int" width="15%">Tipo Mittente Atto</td>
        <td class="int" width="10%">Mittente</td>
        <td class="int" width="10%">Contenuto</td>
        <td class="int" width="10%">Data Arrivo Cancelleria</td>
        <td class="int" width="05%">Azioni</td>
      </tr>
    </div>
<%
  Iterator itx = ulterioriistanze.iterator();
  while ( itx.hasNext())
  {
    UlterioreIstanzaModel ulterioreistanza = (UlterioreIstanzaModel)itx.next();
%>
    <tr>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ulterioreistanza.getDataRichiesta(),"dd-MM-yyyy"),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(ulterioreistanza.getDescrTipoAtto(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(ulterioreistanza.getDescrTipoMittenteAtto(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(ulterioreistanza.getDescrMittente(),"-")%> </td>
      <td class="l"><%=StringUtils.toStringJSP(ulterioreistanza.getDescrOggettoProcedimento(),"-")%> </td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(ulterioreistanza.getDataArrivoCancelleria(),"dd-MM-yyyy"),"-")%></td>
     	
     	<td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUlterioreIstanza.CAMPO_ID_ULTERIORE_ISTANZA%>" />
          <jsp:param name="ValoreIdEntita" value="<%=ulterioreistanza.getIdUlterioreIstanza()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  } // End while
 } // End if
%>
    </table>
  </form>
  <br>
  </body>
</html>