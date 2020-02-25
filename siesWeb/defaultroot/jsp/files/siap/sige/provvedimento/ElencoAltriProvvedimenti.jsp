<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@page import="f3b.util.Utils"%>

<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>

<jsp:useBean id="provvedimentiAltri" scope="request" class="java.util.Vector"/>

  <td class="L">
  <table width="100%" >
<%
  if ( provvedimentiAltri.size() == 0 )
  {
%>
    <tr>
        <td class="int" align="left">Non ci sono altri provvedimenti allegati al procedimento.</td>
	</tr>
<%
  } else
  {
%>
  <div align=center>
    <tr>
      <td class="int" width=20%>Data emissione</td>
      <td class="int" width=80%>Tipo</td>
    </tr>
  </div>
<%
    Iterator itx = provvedimentiAltri.iterator();
    while ( itx.hasNext())
    {
    	ProvvedimentoSigeEventoModel lProvEve = (ProvvedimentoSigeEventoModel)itx.next();
%>
    <tr>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"),"-") %></font></td>
      <td class="c"><font class="campo">
<%		if(lProvEve.getProvvedimento().getCodTipoProvvedimentoSige()!=null) { %>      
				<%=StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimentoSige(),"-")%>
<%		} else {	%>
      	<%=StringUtils.toStringJSP(lProvEve.getProvvedimento().getDescrTipoProvvedimento(),"-")%>
<%			if ( Utils.isNullObj(lProvEve.getEventoNotifica().getEvento()) || 
					   Utils.isNullObj(lProvEve.getEventoNotifica().getEvento().getCodEsito()) )
    		{%>&nbsp;<%
    		}	else {	%>  
					<%=lProvEve.getEventoNotifica().getEvento().getDescrEsito()%>
				<%}%>      
			<%}%>      
      </font></td>
    </tr>
  <%
    } // endwhile

  }  // endif provvedimentiAltri.size()
%>
  	</table>
</td>