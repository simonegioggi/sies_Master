<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Enumeration"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Stampa</title>
  </head>

<body class="corpo" onload="document.f.submit();">
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f" >

<%
//   Vengono inseriti tanti campi di input quanti sono i parametri nella request
//   per poterli passare alla Action successiva.
     for (Enumeration r = request.getParameterNames() ; r.hasMoreElements() ;)
     {
         String nomeParam = (String) r.nextElement();
         String valoreParam = request.getParameter(nomeParam);
%>
     <input  type="HIDDEN" name="<%=nomeParam%>" value="<%=valoreParam%>" />
     
<%
     }
%>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActRicercaFSigePerEstremiStatistica" />
<br><br><br><br><br>
<TABLE width="300" height="200" style="border: 3;" align="center" >
 <TR>
   <TD align="center" valign="middle">
   <input type="image" name="imageStampa" src='/images/animated_printer2.gif' >
   </TD>
 </TR>
 <TR>
   <TD align="center" ><font>Generazione Statistica in corso ...</font></td>
 </TR>
</TABLE>

</form>
</body>
</html>