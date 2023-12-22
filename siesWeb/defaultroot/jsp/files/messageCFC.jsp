<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.utente.model.UtenteModel" %>
<jsp:useBean id="UtenteConnesso"      scope="session" class="siap.sico.utente.model.UtenteModel" />


<html>
  <head>
  <!-- INIZIO Definizione Script BOTTONI GRAFICI  -->
  <SCRIPT LANGUAGE="JavaScript">
  	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    // preload images:
    if (document.images)
    {
      clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
      clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
    }

    function hiLite(imgName,imgObjName)
    {
      if (document.images)
      {
          document.images[imgName].src = eval(imgObjName + ".src");
      }
    }
  </SCRIPT>
  <!-- Fine Definizione Script BOTTONI GRAFICI -->

<%
    String jumpPage = "history.go(-1)";
    String newPage  = (String)request.getAttribute(IWebConstants.GOTO_PAGE);

    if(newPage != null)
      jumpPage = "\""+newPage+"\"";
%>
  <SCRIPT LANGUAGE="JavaScript">

  	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    function go()
    {
      if (window.name != 'body')
        {self.close();}
    else   {  
<%
    if(newPage != null)
      out.print("location=" + jumpPage);
    else
      out.print("history.go(-1);");
%>
	}
    }

  	  function openPopup(url) {
  		  newwindow = window.open(url,'name','height=300,width=500,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
  		  if (window.focus) {newwindow.focus()}
  	  }

  </SCRIPT>

<% 
  BigDecimal idEvento = (BigDecimal)request.getAttribute(ICostantiEvento.CAMPO_ID_EVENTO);
  String idUtente=UtenteConnesso.getUserId();
%>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>Messaggio</title>
  </head>

  <BODY class="corpo" onload="openPopup('/siesEsecuzione/cancellazioneContestuale.jsp?idEvento=<%=idEvento%>&idUtente=<%=idUtente%>&tipoOperazione=ANNULLA');return(false);">
  
<%
      String message = (String)request.getAttribute(IWebConstants.MESSAGE_TEXT);
%>

    <p>&nbsp;<p>&nbsp;<p>&nbsp;

    <table width="300"  cellspacing="0" align="center" class="tab" border="1">
      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab">
          <p>&nbsp;<p>
          <B><%= message %></B>
          <p>&nbsp;<p>
        </td>
      </tr>

      <tr align="center" valign="middle">
        <td align="center" colspan="2" class="tab2">
          <a href="javascript:go();" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
            <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01"></a>
        </td>
      </tr>

      <tr align="left" >
        <td colspan="2"  class="tabhead"></td>
      </tr>

    </table>
  </body>

</html>