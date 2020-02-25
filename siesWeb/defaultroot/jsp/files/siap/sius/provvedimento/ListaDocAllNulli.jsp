<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato"%>


<jsp:useBean id="DocAllNulli" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
    if (DocAllNulli != null && DocAllNulli.size() > 0)
    {
%>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Annullati : </td>
        <td class="label">
<%
      Iterator itx = DocAllNulli.iterator();
      String sep = " ";
      while ( itx.hasNext())
      {
         DocumentoAllegatoModel lDocAll = (DocumentoAllegatoModel)itx.next();
%>      <%=sep%>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=request.getParameter("ActionLink")%>&<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>=<%=lDocAll.getIdDocumentoAllegato()%>&TornaQui=<%=TornaQui%>">
        <%=lDocAll.getAnnoFoglioComplementare()%>/<%=lDocAll.getProgrFoglioComplementare()%>
</a>
<%
        sep = " , ";
      }
%>
       </td>
      </tr>
    </table>
 <br>
<%
    }
%>