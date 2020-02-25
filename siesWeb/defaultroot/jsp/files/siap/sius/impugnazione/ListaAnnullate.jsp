<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sius.impugnazione.model.ImpugnazioneModel"%>
<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>

<jsp:useBean id="InpAnnullate" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
    if (InpAnnullate != null && InpAnnullate.size() > 0)
    {
%>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Annullati : </td>
        <td class="label">
<%
      Iterator itx = InpAnnullate.iterator();
      String sep = " ";
      while ( itx.hasNext())
      {
         ImpugnazioneModel lImp = (ImpugnazioneModel)itx.next();
%>      <%=sep%>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=request.getParameter("ValoreIdEvento")%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("ValoreTipoProv")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=lImp.getIdImpugnazione()%>&TornaQui=<%=TornaQui%>">
        <%=lImp.getAnnoS7()%>/<%=lImp.getProgrS7()%>
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