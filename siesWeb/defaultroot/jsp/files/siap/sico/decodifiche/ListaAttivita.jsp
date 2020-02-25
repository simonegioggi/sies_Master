<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>


<jsp:useBean id="ListaAttivita" scope="request" class="java.util.Vector" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Attività per Incarico</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript">
      var strDescr='';
      var strCode='';

      // Funzione di caricamento delle Attività selezionate nella form della finestra chiamante
      function CaricaAttivita()
      {
        if (typeof (lAttivita.length) == "undefined")
        {
          if (lAttivita.checked)
          {
            strDescr += lDescAttivita.value +'\n';
            strCode += lAttivita.value +'|';
          }
        }
        else
        {
          for (i = 0; i < lAttivita.length ; i++ )
          {
            if (lAttivita[i].checked)
            {
              strDescr += lDescAttivita[i].value +'\n';
              strCode += lAttivita[i].value +'|';
            }
          }
        }

        insertIT(strDescr, strCode);
      }

      function insertIT(strDescr, strCode )
      {
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=strDescr;
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcodes")%>.value=strCode;
        self.close();
      }
    </script>

  </head>

  <body>
    <table>
      <tr>
        <td class=LBG>Selezionare le Attività previste per l'incarico</td>
      </tr>
    </table>

    <table width="100%">
        <tr>
          <td class=Titolo>Elenco Attività per Incarico</td>
        </tr>
  <%
    // Ciclo di caricamento Attività
    ListIterator itx = ListaAttivita.listIterator();

    while ( itx.hasNext() )
    {
      DecodificheModel lAttivita = (DecodificheModel) itx.next();
   %>
        <tr>
          <td class=l><%=lAttivita.getDescription()%></td>
          <input type="HIDDEN" name=lDescAttivita value="<%=lAttivita.getDescription()%>">
          <td class=l><input type='checkbox' name=lAttivita value="<%=lAttivita.getCode()%>">
<% } %>
        </tr>

       <tr>
    <td>
      <input onclick="Javascript:CaricaAttivita();" class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>
</body>
</html>