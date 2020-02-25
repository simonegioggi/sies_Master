<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<jsp:useBean id="ListaOggetti" scope="request" class="java.util.Vector" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Oggetti Sige</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript">
      var strDescr='';
      var strCode='';
      var codContPrec='';
      var codCont='';

      // Funzione di caricamento degli Oggetti selezionati
      function caricaOggetti()
      {
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
              codCont = lCodContenuto[i].value;
            }
            if(codContPrec.length>0 && codCont!=codContPrec){            	
            	strDescr='';
            	strCode='';
            	codCont='';
            	codContPrec='';            	
            	alert('Attenzione! Sono stati selezionati oggetti di diverso contentuto!');
            	return;
          }
            codContPrec = codCont;
        }
        }

        insertIT(strDescr, strCode);
      
      }

      function insertIT(strDescr, strCode )
      {
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=strDescr;
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcode")%>.value=strCode;
        window.opener.document.InserisciTenoreSige.CodContenuto.value=codCont;
        window.opener.document.InserisciTenoreSige.CodContenutoSelected.value=codCont;
        self.close();
      }

    </script>

  </head>

  <body>
     <table>
      <tr>
        <td class=LBG>Selezionare gli oggetti</td>
      </tr>
    </table>

    <table width="100%">
<%
    String preContenuto="";
    
    ListIterator itx = ListaOggetti.listIterator();

    // Ciclo di Caricamento Oggetti SIGE.
    while ( itx.hasNext() )
    {
      OggettiModel lOggetti = (OggettiModel)itx.next();

      // Si pone l'intestazione del Contenuto.
      if (!lOggetti.getCodContenuto().equals(preContenuto))
      {
        preContenuto = lOggetti.getCodContenuto();
%>
        <tr>
            <td class=Titolo><%=lOggetti.getDescContenuto()%></td>
        </tr>
<%
      }
%>
		<tr>
			<td class=l><%=lOggetti.getDescOggetto()%></td>
				<input type="HIDDEN" name="lDescOggetto" value="<%=lOggetti.getDescOggetto()%>">
				<input type="HIDDEN" name="lCodContenuto" value="<%=lOggetti.getCodContenuto()%>">
			<td class=l>
				<input type="checkbox" name="lOggetti" value="<%=lOggetti.getCodOggetto()%>" >
			</td>
		</tr>
<%  } //end while %>
	    <tr>
	        <td>
	      		<input onclick="Javascript:caricaOggetti();" class="bottone" type="submit" value="Conferma">
	        </td>
	    </tr>

    </table>
</body>
</html>