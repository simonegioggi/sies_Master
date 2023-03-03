<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<jsp:useBean id="ListaOggetti" scope="request" class="java.util.Vector" />
<jsp:useBean id="Funzione" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Oggetti Sige per Contenuto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript">
      var strDescr='';
      var strCode='';

      // Funzione di caricamento degli Oggetti selezionati
      function caricaOggetti()
      { 
    	<%-- Ticket#202303020115 --%>
    	var lOggetti = document.getElementsByName("lOggetti");
    	var lDescOggetto = document.getElementsByName("lDescOggetto");
    	<%-- Ticket#202303020115 - FINE --%>
    	
        if (typeof (lOggetti.length) == "undefined")
        {
          if (lOggetti.checked)
          {
            strDescr += lDescOggetto.value + '\n';
            strCode += lOggetti.value + '|';
          }
        }
        else
        {
          for (i = 0; i < lOggetti.length ; i++ )
          {
            if (lOggetti[i].checked)
            {
              strDescr += lDescOggetto[i].value + '\n';
              strCode += lOggetti[i].value + '|';
            }
          }
        }

        insertIT(strDescr, strCode);
      
      }

      function insertIT(strDescr, strCode )
      {
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=strDescr;
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcode")%>.value=strCode;
        self.close();
      }

    </script>

  </head>

  <body>
    <table>
       <tr>
       		<td class="LBG"><%=Funzione%></td>
       </tr>
    </table>

    <table width="100%">
<%
    ListIterator itx = ListaOggetti.listIterator();

    // Ciclo di Caricamento Oggetti SIGE.
    while ( itx.hasNext() )
    {
      OggettiModel lOggetti = (OggettiModel)itx.next();
%>
		<tr>
			<td class=l><%=lOggetti.getDescOggetto()%></td>
				<input type="HIDDEN" name="lDescOggetto" value="<%=lOggetti.getDescOggetto()%>">
			<td class=l>
				<input type="checkbox" name="lOggetti" value="<%=lOggetti.getCodOggetto()%>" >
			</td>
		</tr>
<%	} //end while %>
        <tr>
            <td>
      			<input onclick="Javascript:caricaOggetti();" class="bottone" type="submit" value="Conferma">
            </td>
        </tr>

    </table>
</body>
</html>