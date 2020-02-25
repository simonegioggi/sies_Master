<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel"%>
<%@ page import="siap.sius.cancassfascsius.action.ICostantiCancAssFascSius"%>

<jsp:useBean id="cancellerie" scope="request" class="java.util.Vector"/>

<%
if (cancellerie != null && cancellerie.size() > 0)
{
boolean nulla = false;
  if (request.getParameter("nulla") != null)
  {
    nulla = true;
  }
boolean nocanc = false;
  if (request.getParameter("nocanc") != null)
  {
	  nocanc = true;
  }
%>
   <tr>
    <td class="l">Cancelleria Assegnataria</td>
    <td class="l">
      <select  name="<%=ICostantiCancAssFascSius.CAMPO_COD_CANCELLERIA_ASSEGNATARIA%>"  class="small">
      <%
        String cod;
        String desc;
        for (int i=0;i<cancellerie.size();i++)
        {
          cod=((CancelleriaAssegnatariaModel)cancellerie.get(i)).getCodCancelleriaAssegnataria();
          desc= cod + " - " + ((CancelleriaAssegnatariaModel)cancellerie.get(i)).getDescCancelleriaAssegnataria();
      %>
         <option value="<%=cod%>"><%=desc%></option>
<%      }
if (nocanc)
{
%>
         <option value="nocanc" > Nessuna cancelleria assegnataria </option>
<%
}
if (nulla)
{
%>
         <option value="" selected> - </option>
<%
}

%>
      </select>
     </td>
   </tr>
<%} %>