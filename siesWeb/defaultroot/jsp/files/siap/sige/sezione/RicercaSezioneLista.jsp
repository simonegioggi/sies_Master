<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>
<%@ page import="siap.sige.sezione.model.SezioneModel"%>

<jsp:useBean id="sezioni" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="formname" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Lista Sezioni</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(cod,codfis,cognome,nome)
        {
          window.parent.opener.document.<%=formname%>.<%=ICostantiSezione.CAMPO_ID_SEZIONE%>.value=cod;
          window.parent.opener.document.<%=formname%>.<%=ICostantiSezione.CAMPO_CODICE%>.value=cognome;
          window.parent.opener.document.<%=formname%>.<%=ICostantiSezione.CAMPO_DESCRIZIONE%>.value=nome;
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
        <td class="LBG">
					<font class=label>Funzione :</font>&nbsp;
					<font class="campo">Elenco Curatori Ufficio</font></td>
      </tr>
    </table>

    <Table width="100%">
    <tr>
    <td class=int>Cod.Fiscale</td>
    <td class=int>Nominativo</td>
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
    Iterator itx = sezioni.iterator();
    while ( itx.hasNext() )
    {
      SezioneModel lSezione = (SezioneModel)itx.next();
    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lSezione.getCodice())%></td>
        <td class=l><%=StringUtils.toStringJSP(lSezione.getDescrizione())%></td>
   <%
      if (! modalita.equals("NoPop"))
     	{ 
   %>
        <td class=c>
					<a href="Javascript:insertIT('<%=lSezione.getIdSezione()%>','<%=lSezione.getCodice()%>',
												'<%=StringUtils.cStrForJS(lSezione.getDescrizione())%>');">
						<img align="middle" src="/images/fileselected.gif" border=0>
					</a>
				</td>
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