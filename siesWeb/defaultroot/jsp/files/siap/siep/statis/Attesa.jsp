<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.statis.action.ICostantiStatis" %>

<jsp:useBean id="next_action" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="UffScelto" scope="request" class="java.lang.String"/>

<jsp:useBean id="ggIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="mmIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="aaIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="ggFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="mmFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="aaFin" scope="request" class="java.lang.String"/>
<jsp:useBean id="soloaaIni" scope="request" class="java.lang.String"/>
<jsp:useBean id="soloaaFin" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>

  <BODY class="corpo" onload="document.c.submit();">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
         <br>
	   <table>
      <tr>
         <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo"><%=titolo%></font>
       </td>
     <td> <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=next_action%>"></td>
    <td> <input type="HIDDEN" name="vai" value="pippo"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_COD_ACCORPATO_1%>" value="<%=UffScelto%>"></td>
    
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_GIORNO_INIZIALE%>" value="<%=ggIni%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_MESE_INIZIALE%>" value="<%=mmIni%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_ANNO_INIZIALE%>" value="<%=aaIni%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_GIORNO_FINALE%>" value="<%=ggFin%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_MESE_FINALE%>" value="<%=mmFin%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_ANNO_FINALE%>" value="<%=aaFin%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_INIZIALE%>" value="<%=soloaaIni%>"></td>
    <td> <input type="HIDDEN" name="<%=ICostantiStatis.CAMPO_SOLO_ANNO_FINALE%>" value="<%=soloaaFin%>"></td>
    
     </tr>
    </table>
    <br>
   <br>

</table>
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