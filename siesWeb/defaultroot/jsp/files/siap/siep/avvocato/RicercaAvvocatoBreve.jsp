<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<html>
<head>
	<title>[S.I.E.S.] - Lista Comuni</title>
	<link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <% if (! modalita.equals("NoPop"))
  {
  %>
	<script language="JavaScript">
	function insertIT(id,cognome,nome)
	{
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
       if(nome == "-")
         {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome + " " + "";
         }else
          {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome + " " + nome;
          }
          //window.parent.opener.document.<-%=request.getParameter("formname")%->.<-%=ICostantiAvvocato.CAMPO_COGNOME%->.value=cognome + " " + nome;
          window.parent.close();
	}
	</script>
 <%
  }
 %>
	<script language="JavaScript">

 function avvocati()
    {
      if ("<%=avvocato.size()%>" == 0)
        alert('Attenzione! Nessun Difensore trovato.');
      if ("<%=avvocato.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 Difensori individuati. Perfezionare la ricerca!');
    }
	</script>

</head>

<body class=corpo onload="avvocati();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
    </tr>
  </table>
 <% if (modalita.equals("NoPop"))
  {
  %>
 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>
  <% } %>
<%if(avvocato.size()>0){%>
 <Table width="100%">
   <tr>
  <td class=int  width=40%>Nome</td>
  <td class=int  width=10%>Foro</td>
  <td class=int  width=40%>Indirizzo</td>
  <% if (! modalita.equals("NoPop"))
  { %>
    <td class=int width=10%>Seleziona</td>
  <% } %>
  </tr>
 <%
  	Iterator itx = avvocato.iterator();

  	while ( itx.hasNext())
  	{
    	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
	%>
	<tr>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " + StringUtils.toStringJSP(StringUtils.toStringJSP(lAvv.getNome()))%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo())%>
        </td>
        <% if (! modalita.equals("NoPop"))
        { %>
        <td class=c><a href="Javascript:insertIT('<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>');"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
        </tr>
	<%
	}
%>
</table>
<%}%>
</body>
</html>