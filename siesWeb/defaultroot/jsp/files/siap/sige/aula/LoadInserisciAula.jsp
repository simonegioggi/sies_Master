<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sige.aula.model.AulaUdienzaModel"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>

<jsp:useBean id="modalita"		  scope="request" class="java.lang.String"/>
<jsp:useBean id="aulaUdienza"     scope="request" class="siap.sige.aula.model.AulaUdienzaModel"/>
<jsp:useBean id="elencoSezioni"	  scope="request" class="java.lang.String"/>
<jsp:useBean id="aulaPredefinita" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Aula </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">

		// Controlla obbligatorietà della Sezione.
		function checkObblSezione()
		{ 
			if (document.LoadInserisciAula.<%=ICostantiAula.CAMPO_ID_SEZIONE%>.value.length == 0)
		  {
		  	alert("Occorre selezionare la sezione.");
		  	document.LoadInserisciAula.<%=ICostantiAula.CAMPO_ID_SEZIONE%>.focus();
		    return false;
		  }
			return true;
		}		
		
		function Verify()
		{
			// Controllo Obbl. della Sezione.
			<%
			    // Se esistono sezioni esegue il conrollo di obbl.
				if( elencoSezioni.length() != 0 ) {%>
					if( !checkObblSezione() )
						return false;
			<%	}  %>
			
			return true;
	    }

    </script>

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
    <%
      AulaUdienzaModel lAulaUdienza = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
    	lAulaUdienza = new AulaUdienzaModel();
        lAzione = "siap.sige.aula.action.ActInserisciAula";
    %>
    	<font class="campo">Inserimento Aula</font>
    <%
      }
      else if( modalita.equals("M") )
      {
    	lAulaUdienza = aulaUdienza;
        lAzione = "siap.sige.aula.action.ActModificaAula";
    %>
    	<font class="campo">Modifica Aula</font>
    <%
      }
    %>
        </td>

   		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>   		
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciAula">
      <table cellspacing=4 cellpadding=4>

<% if( modalita.equals("I") ){ %>
		<tr>
   			<td class="l">Sezione <font class=ob>(*)</font></td>
   			<td class="l" colspan="3">
   				<select title="codiciSezione" name="<%=ICostantiAula.CAMPO_ID_SEZIONE%>">
     				<%=elencoSezioni%>
     			</select>
   			</td>
   		</tr>
<% } else { 

		if( elencoSezioni.length() != 0 ) { %>
			<tr>
    			<td class="l">Sezione </td>
      			<td class="l">
      				<font class="campo"><%=lAulaUdienza.getSezione().getDescrizione()%></font>
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_SEZIONE%>" 
								 value="<%=lAulaUdienza.getIdSezione()%>">
      			</td>
    		</tr>
	<%  } %>

<% } %>

		<tr>
			<td class="l">Aula <font class=ob>(*)</font></td>
			<td class="L">
				<input title="Aula" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>" 
				       value="<%=lAulaUdienza.getDescrizioneAula()%>" maxlength="100" size="50">  
			</td>
			<td class="l">Stanza </td>
			<td class="L">
				<input title="Stanza" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>" 
				       value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneStanza())%>" maxlength="100" size="50">  
			</td>
		</tr>

		<tr>
			<td class="l">Ingresso</td>
			<td class="L" colspan="3">
				<input title="Ingresso" type="text" name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>" 
				       value="<%=StringUtils.toStringJSP(lAulaUdienza.getDescrizioneIngresso())%>" maxlength="100" size="50">  
			</td>
		</tr>

		<tr>
			<td class="l">Piano</td>
			<td class="L">
				<input title="Piano" type="text" name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>" 
				       value="<%=StringUtils.toStringJSP(lAulaUdienza.getNumeroPiano())%>" maxlength="30" size="20">  
			</td>
			<td class="l">Predefinita</td>
			<td class="L">
				<select title="Predefinita" name="<%=ICostantiAula.CAMPO_FLAG_PREDEFINITA%>">
            		<%= aulaPredefinita %>
          		</select>
			</td>
		</tr>

		<tr>
        	<td>
            	<input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          	</td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
        <input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>" value="<%=lAulaUdienza.getIdAula()%>">
  </FORM>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciAula");
        
        frmvalidator.addValidation("<%= ICostantiAula.CAMPO_DESCRIZIONE_AULA %>", "req", "L'Aula è obbligatoria");
 
      </script>
   </body>
</html>