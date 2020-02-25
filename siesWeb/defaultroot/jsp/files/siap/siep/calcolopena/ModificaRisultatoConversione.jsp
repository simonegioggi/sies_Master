<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare" %>
<%@ page import="f3b.web.IWebConstants"%>


<html>
  <head>
    <title>[S.I.E.S.] - Modifica Risultato Conversione - </title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    

    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function insertIT(str,str2,str3)
      {   
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=str;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname2")%>.value=str2;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname3")%>.value=str3;
        window.parent.close();  
        var idMisuraCautelare = '<%= request.getParameter("idMisuraCautelare") %>'
        var fd = str
        var fd2 = str2
        var fd3 = str3
        window.parent.opener.loadModificaRisultatoConversione(fd,fd2,fd3,idMisuraCautelare);     
      }
      
      function annulla()
      {
        window.parent.close();        
      }

      
    </script>
  </head>

  <body class="corpo" onload="focus();">
	
	  <table width="100%" cellpadding=2 cellspacing=2 style="border: 0;">
      	    <tr>
	        	<td class="int" colspan="3">Modifica Risultato Conversione</td>	        
      		</tr>     		
	        <tr>
		      <td class=<%="LBG"%> colspan="3"> 
		      		<font class="campo"> Giorni </font>
		          	<input   title="Ufficio" size="8" type="text" value="0" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI%>" onkeypress="return TicTabNumField(this,event)">       	 
			  </td> 
			</tr>  
			<tr>
		      <td class=<%="LBG"%> colspan="3"> 
		      		<font class="campo"> Mesi </font>
		          	<input   title="Ufficio" size="8" type="text" value="0" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI%>" onkeypress="return TicTabNumField(this,event)">       	 
			  </td> 
			</tr>  
			<tr>
		      <td class=<%="LBG"%> colspan="3"> 
		      		<font class="campo"> Anni </font>
		          	<input   title="Ufficio" size="8" type="text" value="0" name="<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI%>" onkeypress="return TicTabNumField(this,event)">       	 
			  </td> 
			</tr>  
			<tr>
	          <td style="text-align:center;"><a href="Javascript:insertIT(<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_GIORNI%>.value,<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_MESI%>.value,<%= ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA_ANNI%>.value)">
	          	<font class="bottone" > Conferma </font></a>
	          </td>
	           <%-- <td class=<%="LBG"%> style="text-align:center;"><a href="Javascript:annulla()">
	          	<font class="campo"> Annulla </font></a>
	          </td> --%>
	        </tr>
	  </table>
	  
  </body>
  <script language=javascript>
    window.focus();
  </script>
</html>