<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="next_action" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>

  <BODY class="corpo" onload="document.c.submit();">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <!-- br-->
	<table>
      <tr>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo"><%=titolo%></font>
        </td>
     	<td> <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=next_action%>"></td>
    	<td> <input type="HIDDEN" name="vai" value="pippo"></td>
    
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