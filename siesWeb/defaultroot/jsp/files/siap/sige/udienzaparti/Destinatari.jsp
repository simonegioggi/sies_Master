<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>

<% if(notifiche.size() > 0){ %>

<table cellspacing=2 cellpadding=2  width="100%">
  <tr>
    <td class="Titolo" colspan=2> Destinatari</td>
  </tr>
<%
  Iterator itx2 = notifiche.iterator();
  while ( itx2.hasNext())
  {
    NotificaModel notifica = (NotificaModel)itx2.next();
%>  
	<tr>
<%
      if(notifica.getUfficio()!= null) // UFFICIO
      {%>
        <td class="l" >
        	<font class="campo">
        		<%=notifica.getUfficio().getDescrTipoUfficio()%>
        	</font>&nbsp;di&nbsp; 
        	<font class="campo"><%=notifica.getUfficio().getDescrComune()%>
        	</font>
        </td>

<%        if(notifica.getNote()!=null) { %>
 						<td class="l"><%=notifica.getNote() %></td>
				<%}%>

<%
      } else if ( notifica.getAutoritaEsterna() != null ) // Autorità Esterna
        {
%>
          <td class="l">
          <font class="campo"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font>
          &nbsp;di&nbsp;
          <font class="campo"><%=notifica.getAutoritaEsterna().getDescrSede()%></font>
          &nbsp;&nbsp;
<%        if(notifica.getNote()!=null) { %>
            <font class="campo">-&nbsp;&nbsp;<%=notifica.getNote() %></font>
<%        }%>
          </td>
<%
      } else if(notifica.getIstitutoDetenzione() != null) // Istituto Detenzione
        {
%>
          <td class="l">
          <font class="campo"><%=notifica.getIstitutoDetenzione().getDescrTipoIstituto() %> </font>
          &nbsp;di&nbsp;
          <font class="campo"><%=notifica.getIstitutoDetenzione().getDescrComune()  %></font>
          </td>
<%    } else {
		  // Sistema Notifiche Telematiche
%>
          <td class="l">
          <font class="campo">Sistema Notifiche Telematiche</font>
          </td>
<%	
	  }

      if(notifica.getAvvIdAvvocatoFascicoloSige()==null) // SOGGETTO
      {
%>
          <td class="l">Per la notifica al Soggetto </td>
<%
      } 
      else if(notifica.getAvvIdAvvocatoFascicoloSige()!=null) // Avvocato SIGE
      {
    	  if( notifica.getAvvParteUdienza() != null )
        {%>
          <td class="l"> Per la notifica all' Avvocato &nbsp;:&nbsp;
          	<font class="campo">
          		<%=StringUtils.toStringJSP(notifica.getAvvParteUdienza().getCognome() +" "+notifica.getAvvParteUdienza().getNome())%>
          	</font>&nbsp; 
          </td>
<%      	}
      	} else if(notifica.getAvvIdAvvocatoFascicoloSius()!=null) { // Avvocato SIUS
      	
        if( notifica.getAvvSius() !=null)
        {%>
          <td class="l">Per la notifica all' Avv. 
          	<font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSius().getAvvocato().getCognome() +" "+notifica.getAvvSius().getAvvocato().getNome())%></font>&nbsp; 
          </td>
<%      }
      } else if(notifica.getCssIdCssa()!=null) // UEPE
      {
        if( notifica.getCSSA() !=null)
        {
%>
          <td class="l" colspan="2"><font class="campo">UEPE &nbsp;</font>
           <font class="campo"><%=StringUtils.toStringJSP(notifica.getCSSA().getIndirizzo() +" "+notifica.getCSSA().getComune())%></font>&nbsp;
          </td>
<%      }
      }
%>
    </tr>
<%}%>
  <tr>
      <td  colspan=2>&nbsp;</td>
  </tr>
<% } %>

</table>



