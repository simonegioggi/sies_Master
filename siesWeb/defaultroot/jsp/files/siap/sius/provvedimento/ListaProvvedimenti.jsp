<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento" %>
<%@ page import="siap.sico.evento.model.EventoDepositoModel"%>

<jsp:useBean id="provvedimenti"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />

<html>
<head >
    <title>[S.I.E.S.] - Lista Provvedimenti Depositati</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
</head>

    <script language="JavaScript">
    // Funzione che inserisce i dati nella form chiamante ed esegue la chiusura della popup.
    function insertIT(ID_Evento, data_emissione)
      {
       window.parent.opener.document.<%=formname%>.<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>.value = ID_Evento;
      window.parent.opener.document.<%=formname%>.RifProvRev.value = data_emissione;

      window.parent.close();
      return;

    }
    </script>


  <table >
<%
  if ( provvedimenti.size() == 0 )
  {
%>
        <td class="int" align="left">Non ci sono provvedimenti allegati al procedimento.</td>
<%
  }
   else
  {
%>
  <div align=center>
    <tr>
      <td class="int" width=10%>Data emissione</td>
      <td class="int" width=14%>Tipo </td>
      <td class="int" width=26%>Motivo </td>
      <td class="int" width=18%>Esito </td>
      <td class="int" width=12%>Data Deposito</td>
    		<%
				if (! modalita.equals("NoPop"))
    		{
				%>
      		<td class=int>Seleziona</td>
    		<%
    		}
				%>

    </tr>
  </div>
<%
    Iterator itx = provvedimenti.iterator();
    String lEstremiFoglioComp = "";
    int i=0;
    while ( itx.hasNext())
    {
      EventoDepositoModel lProv = (EventoDepositoModel)itx.next();
%>
    <tr>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-") %></font>     </td>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrTipoProvvedimento(),"-")%></font>     </td>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrMotivo(),"-")%></font>     </td>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lProv.getDescrEsito(),"-")%></font>     </td>
      <td class="c"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataDeposito(),"dd-MM-yyyy"),"-")%></font>     </td>
      <%
      if (! modalita.equals("NoPop"))
      {
      %>
        <td class=c><a href="Javascript:insertIT('<%=lProv.getIdEvento()%>', '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProv.getDataEmissione(),"dd-MM-yyyy"),"-")%>' );"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
    	<%
      }
      %>


    </tr>
<%
    } // endwhile
  %>
<%
  }  // endif provvedimenti.size()
%>
  </table>
</html>