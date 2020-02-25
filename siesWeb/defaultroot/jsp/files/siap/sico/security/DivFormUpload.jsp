<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%
String lAzione = "";
String lAzioneDettaglio = "";
String lId = "";
  if (request.getParameter("Azione") != null)
      lAzione = request.getParameter("Azione");
  if (request.getParameter("AzioneDettaglio") != null)
      lAzioneDettaglio = request.getParameter("AzioneDettaglio");
  if (request.getParameter("Id") != null)
      lId = request.getParameter("Id");
%>


 <div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
  <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />

    <tr>
      <td class="L">
       <input class="bottone"  type="submit" value="Conferma">
       <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
       <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=lId%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="<%=lAzioneDettaglio%>">
      </td>
    </tr>
  </table>

  </FORM>
  </div>

</body>
</html>