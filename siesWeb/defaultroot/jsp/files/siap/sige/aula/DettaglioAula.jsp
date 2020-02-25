<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.aula.action.ICostantiAula"%>

<jsp:useBean id="aula" scope="request" class="siap.sige.aula.model.AulaUdienzaModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Aula </title>
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
            <font class="campo">Dettaglio Aula</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%= ICostantiAula.PG_TOOLBAR_HEADER_AULA %>">
            	<jsp:param name="CampoIdEntita" value="<%=ICostantiAula.CAMPO_ID_AULA%>" />
            	<jsp:param name="ValoreIdEntita" value="<%=aula.getIdAula()%>" />
           		<jsp:param name="CampoIdEntitaIdSezione" value="<%=ICostantiAula.CAMPO_ID_SEZIONE%>" />
           		<jsp:param name="ValoreIdEntitaIdSezione" value="<%=aula.getIdSezione()%>" />
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    	  <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
      </table>
    </FORM>

  <table cellspacing=4 cellpadding=4>
      <tr>
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getSezione().getDescrizione(), "-")%>
			</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Aula</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getDescrizioneAula())%>
			</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Stanza</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getDescrizioneStanza())%>
			</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Ingresso</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getDescrizioneIngresso())%>
			</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Piano</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getNumeroPiano())%>
			</font>
        </td>
      </tr>

      <tr>
      	<td class="l">Aula Predefinita</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(aula.getDescrAulaPredefinita())%>
			</font>
        </td>
      </tr>

    </table>
   </body>
</html>