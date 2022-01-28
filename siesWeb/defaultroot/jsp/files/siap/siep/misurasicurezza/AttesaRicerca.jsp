<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>


<jsp:useBean id="next_action" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="pag" 				    scope="request" class="java.lang.String"/>
<jsp:useBean id="CountRisultati"        scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>

  <BODY class="corpo" onload="document.c.submit();">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
	<table>
      <tr>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo"><%=titolo%></font>
        </td>
     	<td> <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=next_action%>"></td>
     	<td> <input type="HIDDEN" name="vai" value="pippo"></td>  
     	<% if (!"".equals(pag)) { %>  
     	<td> <input type="HIDDEN" name="pag" value="<%=pag%>"></td>   
     	<% } %>
     	<% if (!"".equals(CountRisultati)) { %> 
    	<td> <input type="HIDDEN" name="CountRisultati" value="<%=CountRisultati%>"></td>   
    	<% } %> 
      </tr>
    </table>
   <br>
   <br>
   
<div align=center id="ciao" >
 <table bgcolor="#EEEEEE">
   <tr>
	 <td>
       <img src="/images/rotelle3.gif">
     </td>
     <td>
       <font size=+1 color=navy>
          Attendere... Caricamento in corso.
       </font>
     </td>
   </tr>
 </table>
</div>

</FORM>
</body>
</html>