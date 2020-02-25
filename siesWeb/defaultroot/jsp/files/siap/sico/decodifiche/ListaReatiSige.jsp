<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>

<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<jsp:useBean id="ListaOggetti" scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaReati" scope="request" class="java.util.Vector" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Reati per Titolo Esecutivo</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">


    <script language="JavaScript">
      var desktop;
      var strDescr='';
      var strCode='';
 
      // Funzione di caricamento dei Reati e relative Descrizioni selezionati coi checkbox e combobox,  in 4 variabili (descrizioni e codici).

      // Funzione di caricamento degli Oggetti e Dettagli selezionati coi checkbox e combobox,  in 4 variabili (descrizioni e codici).
      function loadOggetti(lOggetti, lDesc)
      {
      	// alert("loadOggetti: inizio");
        if (typeof (lOggetti.length) == "undefined")
        {
          if (lOggetti.checked)
          {
            strDescr += lDesc.value +'\n';
            strCode += lOggetti.value +'|';
          }
        }
        else
        {
          for (i = 0; i < lOggetti.length ; i++ )
          {
            if (lOggetti[i].checked)
            {
              strDescr += lDesc[i].value +'\n';
              strCode += lOggetti[i].value +'|';
            }
          }
        }

        insertIT(strDescr, strCode);
        // alert("loadOggetti: fine");
        self.close();
      }

      function insertIT(strDescr, strCode )
      {
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=strDescr;
        window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcodes")%>.value=strCode;
        // self.close();
      }
      
      function selezionaDeselezionaTutti()
      {
    	  if( flagTutti.checked == true ){
    		  if (typeof (lOggetti.length) == "undefined")
    	      {
    			  lOggetti.checked = true;
    	      } else {
	    		  for (var i = 0; i < lOggetti.length; i++)
	        	  {
	    			  lOggetti[i].checked = true;
	        	  }
    	      }
      	  } 
    	  
    	  if( flagTutti.checked == false ){
    		  if (typeof (lOggetti.length) == "undefined")
    	      {
    			  lOggetti.checked = false;
    	      } else {
	    		  for (var i = 0; i < lOggetti.length; i++)
	        	  {
	    			  lOggetti[i].checked = false;
	        	  }
    	      }
      	  } 
      }    
    </script>

  </head>

  <body>
    <table>
      <tr>
        <td class=LBG>Selezionare Reati tra quelli previsti per il Titolo Esecutivo scelto</td>
      </tr>
    </table>

    <table width="100%">
<%
	if (ListaReati == null || ListaReati.size() < 1) {
%>
		<tr>
		<td>
		<font class="campo">Nessun reato associato al Titolo Esecutivo scelto</font>
		</td>
		</tr>
<%

	} else {
	Iterator lIterReati = ListaReati.iterator();
  
	// Ciclo di Caricamento Reati.
    while ( lIterReati.hasNext() ) {
      ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
      // Sippassa nella request ReatoCircostanzaModel per renderlo accessibile alla jsp dell'include
      request.setAttribute("lReatoCircostanza", lReatoCircostanza);
 %>
		<tr>
		<td class="l">
			<jsp:include page="<%=ICostantiDecodifiche.PG_DETTAGLIO_REATO_SIGE%>"/>
		</td>
 <% 
 	  // Si ricavano dalla request i dati valorizzati nella jsp di include
 	  ReatoModel lReato = (ReatoModel) request.getAttribute("lReato");
 	  String lDesc = (String) request.getAttribute("lDesc");
 %>
		<td class=l>
			<input type='checkbox' name=lOggetti value=<%=(lReato.getIdReato() != null ? lReato.getIdReato().toString() : "")%>>
		</td>
		</tr>
		<input type="HIDDEN" name=lDesc value="<%=lDesc%>">
<% 
      }  // endwhile
%>
        <tr>
			<td class=l>Tutti</td>
			<td class=l><input type="checkbox" name="flagTutti" value="1" onClick="Javascript:selezionaDeselezionaTutti();"></td>
		</tr>
<%
	}  // endelse
%>
	</table>
  <tr>
    <td>
      <input onclick="Javascript:loadOggetti(lOggetti, lDesc);" class="bottone" type="submit" value="Conferma">
    </td>
  </tr>
  </table>
</body>
</html>