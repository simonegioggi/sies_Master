<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<jsp:useBean id="Stampabile" scope="request" class="java.lang.String"/>

  <script language="JavaScript">

    function stampaSiep(lAzione)
   {
      var  hrefStampa = lAzione;
      var lIndice = hrefStampa.indexOf("?");

     //alert (lAzione);
      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
     // alert (ciccio);
      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
   }
 </script>


   <!-- BOTTONE DI STAMPA -->
      <a href="Javascript:stampaSiep('<%=request.getParameter("ActionLink")%>')">
       <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
      </a>

