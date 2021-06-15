<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.decodifiche.model.ComuneModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<jsp:useBean id="ListaComuni" scope="request" class="java.util.Vector" />
<jsp:useBean id="comune" scope="request" class="java.lang.String" />
<html>
<head>
<title>[S.I.E.S.] - Lista Comuni Nascita</title>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<script language="JavaScript">
    function insertIT(str, codcomune)
    {
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value = str;
      
      if (window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%> != null) {
      		      		
      		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = codcomune;
      		//window.parent.opener.status = "CodComune=" + window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value;

      }
      	
      window.parent.close();
    }

    function checkListaComuniNascita()
    {
      if ("<%=ListaComuni.size()%>" == 0)
        alert('Attenzione! Nessun Comune trovato.');
      if ("<%=ListaComuni.size()%>" == 200)
        alert('Attenzione! Visualizzati solo i primi 200 comuni individuati. Perfezionare la ricerca!');
    }
    </script>

</head>

<body class="corpo"
	onload="javascript:focus();checkListaComuniNascita();">
	<table>
		<tr>
			<td class=LBG>Elenco storico Comuni <%        if ((comune !=" ") || comune.length()>1 ) // Stabilisco che ho fatto la ricerca per filtra comune.
          {%> con filtro '<%=comune%>' <%} else
          {%> della provincia di <%=( (ComuneModel)ListaComuni.elementAt(0) ).getCodProvincia( )%>
				<%}%>
			</td>
		</tr>
	</table>

	<Table width="100%">
		<tr>
			<td class=l>Prov.</td>
			<td class=l>Denominazione</td>
			<td class=l>Data Fine Validità</td>
			<td class=a></td>
		</tr>

		<%
      Iterator itx = ListaComuni.iterator();
      while ( itx.hasNext())
      {
        ComuneModel lComune = (ComuneModel)itx.next();
%>
		<tr>
			<td class=l><%=lComune.getCodProvincia()%></td>
			<td class=l><%=lComune.getDescrizione()%></td>
			<td class=l><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComune.getDataFineValiditaComune(),"dd-MM-yyyy"),"&nbsp;")%></td>
			<td class=c><a
				href="Javascript:insertIT('<%=StringUtils.cStrForJS(lComune.getDescrizione())%>', '<%=StringUtils.cStrForJS(lComune.getCodComune())%>')"><img
					align="middle" src="/images/fileselected.gif" border=0></a></td>
		</tr>
		<%
      }
%>
	</table>
</body>
</html>