<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>

<%
String message = " per l'Udienza del ";
message += request.getParameter(ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA);
message += "-" + request.getParameter(ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA);
message += "-" + request.getParameter(ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA);
message += " Collegio N. " + request.getParameter("Collegio");
message += " ?";

%>

<html>
  <head>
    <title>[S.I.E.S.] - Conferma PreFissazione Udienza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<SCRIPT LANGUAGE="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
// preload images:
if (document.images)
{
	clickme1 = new Image(58,17); clickme1.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif";
	clickme2 = new Image(58,17); clickme2.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01-down.gif";
	clickme3 = new Image(58,17); clickme3.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif";
	clickme4 = new Image(58,17); clickme4.src = "<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01-down.gif";
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
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
// funzione collegata al bottone NO
function stop()
{
	  history.back();
}

</script>
</head>

  <body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPre">
   <br><br><br><br><br>

        <table width="300"  cellspacing="0" align="center" class="tab"  border="1">
        <tr><td>

        <table width="300"  cellspacing="0" align="center" class="tab"  >

         <tr align="center" valign="middle">
             <td align="center"  colspan="2">
               <p>&nbsp;<p>
               <B>Confermi la Prefissazione del Procedimento  <%= " " + request.getParameter("RifProc")%></B>
             </td></tr>

           <tr> <td align="center"  colspan="2" >
                <B><%=message%></B>
              <br></td></tr>

           <tr align="center" valign="middle">
             <td class="l" colspan="2">
             </td></tr>

           <tr><td><br></td></tr>

           <tr align="center" valign="middle" >
             <td align="center" width="50%">
               <a href="javascript:document.ConfermaPre.submit();" onMouseOver="hiLite('img01','clickme2')" onMouseOut="hiLite('img01','clickme1')">
                  <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/ok_01.gif" BORDER="0" ALT="" NAME="img01">
               </a>
             </td>
             <td align="center"  width="50%" >
               <a href="javascript:stop();" onMouseOver="hiLite('img02','clickme4')" onMouseOut="hiLite('img02','clickme3')">
                  <IMG SRC="<%=IWebConstants.IMAGES_DIR %>/bottoni/annulla_01.gif" BORDER="0" ALT="" NAME="img02">
               </a>
             </td>
           </tr>
    </table>
   </td></tr>
    </table>

<%
//   Vengono inseriti tanti campi di input quanti sono i parametri nella request
//   per poterli passare alla Action successiva.
     for (Enumeration r = request.getParameterNames() ; r.hasMoreElements() ;)
     {
         String nomeParam = (String) r.nextElement();
         String valoreParam = request.getParameter(nomeParam);
%>
     <input  type="HIDDEN" name="<%=nomeParam%>" value="<%=valoreParam%>">
<%
     }
%>
  </FORM>
 </body>
</html>