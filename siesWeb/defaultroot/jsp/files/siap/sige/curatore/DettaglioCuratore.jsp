<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>

<jsp:useBean id="curatore" scope="request" class="siap.sige.curatore.model.CuratoreModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Curatore </title>
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
            <font class="campo">Dettaglio Curatore</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiCuratore.CAMPO_ID_CURATORE%>" />
            <jsp:param name="ValoreIdEntita" value="<%=curatore.getIdCuratore()%>" />
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
      </table>
    </FORM>

    <table cellspacing=4 cellpadding=4>
    	<tr>
      	<td class="l">Cognome</td>
        <td class="l">
        	<font class="campo"><%=curatore.getCognome() %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Nome</td>
        <td class="l">
        	<font class="campo"><%=curatore.getNome() %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Codice Fiscale</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getCodiceFiscale(),"-") %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Indirizzo</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getIndirizzo(),"-") %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Telefono</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getTelefono(),"-") %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Email</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getEmail(),"-") %></font>
        </td>
      </tr>
      <tr>
        <td class="l">Fax</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getFax(),"-") %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Cellulare</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getCellulare(),"-") %></font>
        </td>
      </tr>
      <tr>
      	<td class="l">Disponibilità</td>
        <td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(curatore.getDescrFlagStato()) %></font>
        </td>
      </tr>
     	<tr>
     		<td class="l">Data Inizio Validita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>
      <tr>
      	<td class="l">Data Fine Validita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>
    </table>
   </body>
</html>