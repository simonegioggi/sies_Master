<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>

<jsp:useBean id="ListaTitoliEsecutivi" scope="request" class="java.util.Vector" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Titoli Esecutivi</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript">
      var strDescr='';
      var strCode='';

      // Funzione di caricamento dei Titoli Esecutivi selezionati
      function caricaTitoliEsecutivi()
      {
    	  
      	<%-- Ticket#202303020115 --%>
    	var lTitoliEsecutivi = document.getElementsByName("lTitoliEsecutivi");
    	var lDescSentenza = document.getElementsByName("lDescSentenza");
    	<%-- Ticket#202303020115 - FINE --%>
    	
        if (typeof (lTitoliEsecutivi.length) == "undefined")
        {
          if (lTitoliEsecutivi.checked)
          {
            strDescr += lDescSentenza.value + '\n';
            strCode += lTitoliEsecutivi.value + '|';
          }
        }
        else
        {
          for (i = 0; i < lTitoliEsecutivi.length ; i++ )
          {
            if (lTitoliEsecutivi[i].checked)
            {
              strDescr += lDescSentenza[i].value + '\n';
              strCode += lTitoliEsecutivi[i].value + '|';
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

      function selezionaDeselezionaTutti()
      {
       	<%-- Ticket#202303020115 --%>
       	var lTitoliEsecutivi = document.getElementsByName("lTitoliEsecutivi");
       	var flagTutti = document.getElementsByName("flagTutti")[0];
       	<%-- Ticket#202303020115 - FINE --%>
        	
    	  if( flagTutti.checked == true ){
    		  if (typeof (lTitoliEsecutivi.length) == "undefined")
    	      {
    			  lTitoliEsecutivi.checked = true;
    	      } else {
	    		  for (var i = 0; i < lTitoliEsecutivi.length; i++)
	        	  {
	    			  lTitoliEsecutivi[i].checked = true;
	        	  }
    	      }
      	  } 
    	  
    	  if( flagTutti.checked == false ){
    		  if (typeof (lTitoliEsecutivi.length) == "undefined")
    	      {
    			  lTitoliEsecutivi.checked = false;
    	      } else {
	    		  for (var i = 0; i < lTitoliEsecutivi.length; i++)
	        	  {
	    			  lTitoliEsecutivi[i].checked = false;
	        	  }
    	      }
      	  } 
      }
    </script>
  </head>

  <body>
    <table>
      <tr>
        <td class=LBG>Selezionare Titoli Esecutivi</td>
      </tr>
    </table>

    <table width="100%">
<%
	if (ListaTitoliEsecutivi == null || ListaTitoliEsecutivi.size() < 1) {
%>
		<tr>
			<td>
				<font class="campo">Nessun Titolo Esecutivo associato al Procedimento</font>
			</td>
		</tr>
<%

	} else {
		Iterator lIterTitoliEsecutivi = ListaTitoliEsecutivi.iterator();
  
		// Ciclo di Caricamento Titoli Esecutivi.
   		while ( lIterTitoliEsecutivi.hasNext() ) {
   			SentenzaSigeModel lSentenzaSige = (SentenzaSigeModel)lIterTitoliEsecutivi.next();
 %>
		<tr>
			<td class=l><%=lSentenzaSige.getLabelSentenza()%></td>
				<input type="HIDDEN" name=lDescSentenza value="<%=lSentenzaSige.getLabelSentenza()%>">
			<td class=l>
				<input type="checkbox" name="lTitoliEsecutivi" value="<%=lSentenzaSige.getIdSentenza()%>" ></td>
		</tr>
<% 
        }  // end while
%>
        <tr>
			<td class=l>Tutti</td>
			<td class=l><input type="checkbox" name="flagTutti" value="1" onClick="Javascript:selezionaDeselezionaTutti();"></td>
		</tr>
<%        
	}  // end else
%>
	  <tr>
	    <td>
	      <input onclick="Javascript:caricaTitoliEsecutivi();" class="bottone" type="submit" value="Conferma">
	    </td>
	  </tr>
  </table>
</body>
</html>