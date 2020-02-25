<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>

<jsp:useBean id="sezione" scope="request" class="siap.sige.sezione.model.SezioneModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Sezione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Sezione</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiSezione.CAMPO_ID_SEZIONE%>" />
            <jsp:param name="ValoreIdEntita" value="<%=sezione.getIdSezione()%>" />
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
      </table>
    </FORM>

    <table cellspacing=4 cellpadding=4>
    	<tr>
      	<td class="l">Codice</td>
        <td class="l">
        	<font class="campo"><%=sezione.getCodice() %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Descrizione</td>
        <td class="l">
        	<font class="campo"><%=sezione.getDescrizione() %></font>
        </td>
      </tr>
    </table>
   </body>
</html>