<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>

<jsp:useBean id="ImpAnnullate" scope="request" class="java.util.Vector"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
    if (ImpAnnullate != null && ImpAnnullate.size() > 0)
    {
%>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Annullati : </td>
        <td class="label">
<%
      Iterator itx = ImpAnnullate.iterator();
      String sep = " ";
      while ( itx.hasNext())
      {
         ImpugnazioneSigeModel lImp = (ImpugnazioneSigeModel)itx.next();
%>      <%=sep%>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=lImp.getIdImpugnazioneSige()%>&TornaQui=<%=TornaQui%>">
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