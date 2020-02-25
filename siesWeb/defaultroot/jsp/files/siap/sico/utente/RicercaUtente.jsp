<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sico.utente.model.UtenteModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="utenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />
<jsp:useBean id="field2" scope="request" class="java.lang.String" />
<jsp:useBean id="field3" scope="request" class="java.lang.String" />




<html>
  <head>
    <title>[S.I.E.S.] - Lista Utenti</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <% if (! modalita.equals("NoPop"))
    {
    %>
    <script language="JavaScript">
        function insertIT(cognome,nome)
        { 
          window.parent.opener.document.<%=formname%>.<%=field2%>.value=cognome;
          window.parent.opener.document.<%=formname%>.<%=field3%>.value=nome;
          window.parent.close();
          return;
        }

      function checkListaComuni()
    {
      if ("<%=utenti.size()%>" == 0)
        alert('Attenzione! Nessun Funzionario trovato.');
      if ("<%=utenti.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 Funzionari individuati. Perfezionare la ricerca!');
    }
    </script>
    <%
    }
    %>
  </head>

  <body class=corpo onload="checkListaComuni();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Funzionari Ufficio</font></td>
      </tr>
    </table>
<%if(utenti.size()>0){%>
    <Table width="100%">
    <tr>
    <td class=int>Cognome</td>
    <td class=int>Nome</td>
    <!-- td class=int>Data Nascita</td-->
    <% if (! modalita.equals("NoPop"))
    { %>
      <td class=int>Seleziona</td>
    <%
    } %>
    </tr>
    <%
    Iterator itx = utenti.iterator();


    while ( itx.hasNext() )
    {
      UtenteModel lUser = (UtenteModel)itx.next();

    %>
      <tr>        
        <td class=l><%=StringUtils.toStringJSP(lUser.getCognome(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lUser.getNome(),"-")%></td>

      <%
      if (! modalita.equals("NoPop"))
      { %>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lUser.getCognome())%>','<%=StringUtils.cStrForJS(lUser.getNome())%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
      </tr>
    <%
    }
    %>
    </table>
<%}%>
  </body>
</html>