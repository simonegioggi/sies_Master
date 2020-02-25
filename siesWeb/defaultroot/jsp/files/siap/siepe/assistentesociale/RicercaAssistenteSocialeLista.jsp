<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale" %>
<%@ page import="siap.siepe.assistentesociale.model.AssistenteSocialeModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="assistentisociali" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Assistenti Sociali</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
			// Funzione che inserisce i dati nella form e esegue la chiusura della popup.
    	function insertIT(cod,codfis,cognome,nome)
			{
				window.parent.opener.document.<%=formname%>.<%=ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE%>.value=cod;
      	window.parent.opener.document.<%=formname%>.<%=ICostantiAssistenteSociale.CAMPO_COGNOME%>.value=cognome;
				window.parent.opener.document.<%=formname%>.<%=ICostantiAssistenteSociale.CAMPO_NOME%>.value=nome;
      	window.parent.close();
      	return;
      }
    </script>
    <%
    }
    %>
  </head>

  <body class=corpo>
    <table>
      <tr>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td --%>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Assistenti Sociali Ufficio</font></td>
      </tr>
    </table>

    <Table width="100%">
    	<tr>
    		<td class="int">Cod. Fiscale</td>
    		<td class="int">Nominativo</td>
				<td class="int">Stato Disponibilità</td>
    		<%
				if (! modalita.equals("NoPop"))
    		{
				%>
      		<td class=int>Seleziona</td>
    		<%
    		}
				%>
    	</tr>
    <%
    Iterator itx = assistentisociali.iterator();

    while ( itx.hasNext() )
    {
      AssistenteSocialeModel lAssMod = (AssistenteSocialeModel)itx.next();
    %>
			<tr>
        <td class="l"><%=StringUtils.toStringJSP(lAssMod.getCodiceFiscale(),"-")%></td>
        <td class="l"><%=StringUtils.toStringJSP(lAssMod.getCognome(),"-") + " " + StringUtils.toStringJSP(lAssMod.getNome(),"-")%></td>
				<td class="l"><%=StringUtils.toStringJSP(lAssMod.getDescrFlagStato(),"-")%></td>
      <%
      if (! modalita.equals("NoPop"))
      {
				String lCodiceFiscaleJS = StringUtils.cStrForJS(StringUtils.toStringJSP(lAssMod.getCodiceFiscale()) );
				String lCognomeJS = StringUtils.cStrForJS(StringUtils.toStringJSP(lAssMod.getCognome()) );
				String lNomeJS = StringUtils.cStrForJS(StringUtils.toStringJSP(lAssMod.getNome()) );
			%>
        <td class=c><a href="Javascript:insertIT('<%=lAssMod.getIdAssistenteSociale()%>','<%=lCodiceFiscaleJS%>','<%=lCognomeJS%>','<%=lNomeJS%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
    	<%
      }
      %>
			</tr>
    <%
    }
    %>
    </table>
  </body>
</html>