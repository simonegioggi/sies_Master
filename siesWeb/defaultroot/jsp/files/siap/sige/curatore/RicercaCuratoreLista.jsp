<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>
<%@ page import="siap.sige.curatore.model.CuratoreModel"%>

<jsp:useBean id="curatori" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="formname" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Lista Curatori</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(cod,codfis,cognome,nome)
        {
          window.parent.opener.document.<%=formname%>.<%=ICostantiCuratore.CAMPO_ID_CURATORE%>.value=cod;
          window.parent.opener.document.<%=formname%>.<%=ICostantiCuratore.CAMPO_COGNOME%>.value=cognome;
          window.parent.opener.document.<%=formname%>.<%=ICostantiCuratore.CAMPO_NOME%>.value=nome;
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
    Iterator itx = curatori.iterator();
    while ( itx.hasNext() )
    {
      CuratoreModel lCuratore = (CuratoreModel)itx.next();
    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lCuratore.getCodiceFiscale(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lCuratore.getCognome(),"-") + " " + StringUtils.toStringJSP(lCuratore.getNome(),"-")%></td>
   <%
      if (! modalita.equals("NoPop"))
     	{ 
   %>
        <td class=c>
					<a href="Javascript:insertIT('<%=lCuratore.getIdCuratore()%>','<%=lCuratore.getCodiceFiscale()%>',
												'<%=StringUtils.cStrForJS(lCuratore.getCognome())%>',
												'<%=StringUtils.cStrForJS(lCuratore.getNome())%>');"> 
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