<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.aula.action.ICostantiAula"%>

<jsp:useBean id="elencoSezioni"	scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Aula </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  		<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  </head>

  <body class="corpo">
    <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    		<font class="campo">Ricerca Aula</font>
    	</td>
    </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaAula">
      <table cellspacing=4 cellpadding=4>
		 <% if( elencoSezioni.length() != 0 ) { %>
			<tr>
      			<td class="l">Sezione</td>
      			<td class="l" colspan="3">
        			<select title="Sezione" name="<%=ICostantiAula.CAMPO_ID_SEZIONE%>">
        				<%=elencoSezioni%>
        			</select>
      			</td>
			<tr>
		<%  } %>

		<tr>
			<td class="l">Aula </td>
			<td class="L">
				<input title="Aula" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>" 
				       maxlength="100" size="50">  
			</td>
			<td class="l">Stanza </td>
			<td class="L">
				<input title="Stanza" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>" 
				       maxlength="100" size="50">  
			</td>
		</tr>

		<tr>
			<td class="l">Ingresso</td>
			<td class="L" colspan="3">
				<input title="Ingresso" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>" 
				       maxlength="100" size="50">  
			</td>
		</tr>
	
			<tr>
			<td class="l">Piano</td>
			<td class="L" colspan="3">
				<input title="Piano" type="text" name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>" 
				       maxlength="30" size="20">  
			</td>
		</tr>	
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      
      </table>
        
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.aula.action.ActRicercaAula" >
    </FORM>
   </body>
</html>