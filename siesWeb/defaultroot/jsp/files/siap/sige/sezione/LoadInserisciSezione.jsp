<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.sezione.model.SezioneModel"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>

<jsp:useBean id="modalita"						scope="request" class="java.lang.String"/>
<jsp:useBean id="sezione"   					scope="request" class="siap.sige.sezione.model.SezioneModel"/>
<jsp:useBean id="elencoCodiciSezioni"	scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Sezione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">
      function init()
      {
      	document.LoadInserisciSezione.<%=ICostantiSezione.CAMPO_CODICE%>.focus();
      }

      function  Verify()
      {
        var ritorno = true;

        if ((document.LoadInserisciSezione.<%=ICostantiSezione.CAMPO_CODICE%>.value.length == 0)
            || (document.LoadInserisciSezione.<%=ICostantiSezione.CAMPO_DESCRIZIONE%>.value.length == 0))
        {
          alert("Occorre inserire codice e descrizione");
          ritorno = false;
        }
        return ritorno;
      }
    </script>

  </head>

  <body class="corpo" onload="Javascript:init();">
    <table>
    <tr>
    	<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      SezioneModel lSezione = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        lSezione = new SezioneModel();
        lAzione = "siap.sige.sezione.action.ActInserisciSezione";
    %>
    	<font class="campo">Inserimento di una Sezione</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lSezione = sezione;
        lAzione = "siap.sige.sezione.action.ActModificaSezione";
    %>
    		<font class="campo">Modifica Sezione</font>
    <%
        }
    %>
    		</td>
    	</tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSezione">
      <table cellspacing=4 cellpadding=4>

<%
		if( modalita.equals("I") )
		{
%>
			<tr>
    		<td class="l">Codice (*)</td>
      	<td class="l">
      		<select title="codici" name="<%= ICostantiSezione.CAMPO_CODICE %>" >
        		<%=elencoCodiciSezioni%>
        	</select>
      	</td>
    	</tr>
<%
	}
	else
	{
%>		
			<tr>
    		<td class="l">Codice</td>
      	<td class="l">
      		<font class="campo"><%=lSezione.getCodice()%></font>
					<input type="HIDDEN" name="<%=ICostantiSezione.CAMPO_CODICE%>" value="<%=lSezione.getCodice()%>" >
      	</td>
    	</tr>
<% 
	}
%>
          </td>
        </tr>
        <tr>
          <td class="l">Descrizione (*)</td>
          <td class="l">
          	<input value="<%=lSezione.getDescrizione()%>" maxlength="200" size="100" type="text" name="<%=ICostantiSezione.CAMPO_DESCRIZIONE%>">
          </td>
        </tr>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiSezione.CAMPO_ID_SEZIONE%>" value="<%=lSezione.getIdSezione()%>" >
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciSezione");
        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
   </body>
</html>