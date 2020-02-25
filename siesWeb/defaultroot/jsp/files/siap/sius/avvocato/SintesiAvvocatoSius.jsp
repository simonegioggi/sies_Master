<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>
<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="avvocatosius"   scope="request" class="siap.sius.avvocato.model.AvvocatoSiusModel"/>


  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="L">
      <font class="label">Avvocato Sius:  </font>

    <%  if (avvocatosius != null)
        {
    %>
        <font class="campo">
    <%    if (avvocatosius.getAvvocato() != null)
          {
    %>
            <%=StringUtils.toStringJSP(avvocatosius.getAvvocato().getCognome())%>
            <%=StringUtils.toStringJSP(avvocatosius.getAvvocato().getNome())%>
    <%    }else {
    %>
             --
    <%    } %>
        &nbsp;</font>
    <% if (request.getParameter("AvvSiusRitorno") != null)
    {
    %>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.avvocato.action.ActLoadInserisciAvvocato&acdest=<%=request.getParameter("AvvSiusRitorno")%>">
         Assegnazione/Cambio Avvocato
        </a>
    <%} } %>
    </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    </table >
