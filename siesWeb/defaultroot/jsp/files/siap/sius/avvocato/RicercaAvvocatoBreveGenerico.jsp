<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<html>
	<head>
  	<title>[S.I.E.S.] - Lista Comuni</title>
    	<link rel="STYLESHEET" type="text/css" href="/css/style.css">

<% 
	if (! modalita.equals("NoPop"))
  {
%>
     <script language="JavaScript">
     	function insertIT(id,cognome,nome)
     	{
       	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
       	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome + " " + nome;
       	window.parent.close();
     	}
     </script>
<%
  }
%>
	</head>

<body class=corpo>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Avvocati</font></td>
    </tr>
  </table>


<% 
 	if (modalita.equals("NoPop"))
  {
%>

 <br>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
 <br>

<% 
	} 
%>
 <table width="100%">
  <tr>
  	<td class=int>Nome</td>
  	<td class=int>Foro</td>
  	<td class=int>Indirizzo</td>
  	<td class=int>E-mail</td>
  	<td class=int>Telefono</td>
  	<td class=int>Fax</td>
  <% 
  if (! modalita.equals("NoPop"))
  { 
  %>
    <td class=int>Azioni</td>
  <% 
  } 
  %>
  </tr>
 	<%
		Iterator itx = avvocato.iterator();
    while ( itx.hasNext())
    {
        AvvocatoModel lAvv = (AvvocatoModel)itx.next();
 	%>
    <tr>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></td>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getEMail(),"-")%></td>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getTelefono(),"-")%></td>
    	<td class=l><%=StringUtils.toStringJSP(lAvv.getFax(),"-")%></td>

        <%--  SI INSERISCE L'AZIONE CHE SI VUOLE ESEGUIRE RELATIVA ALLA COLONNA Azioni--%>
        <%--% if (! modalita.equals("NoPop"))
        { %>
        <%--td class=c><a href="Javascript:insertIT('<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>','<%=StringUtils.cStrForJS(lAvv.getForo())%>','<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>','<%=StringUtils.cStrForJS(lAvv.getTelefono())%>','<%=StringUtils.cStrForJS(lAvv.getFax())%>','<%=StringUtils.cStrForJS(lAvv.getEMail())%>');"><img align="middle" src="/images/fileselected.gif" border=0></a></td--%>
        <%--% } %--%>

        </tr>

	<%
    }
	%>

</table>

</body>
</html>