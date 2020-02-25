<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>
<%@ page import="siap.sius.udienza.model.UdienzaModel" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>

<jsp:useBean id="udienze" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="tipo" 				scope="request" class="java.lang.String" />
<jsp:useBean id="tiporicerca" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProc" 		scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Visualizza Procedimenti fissati per Udienza</title>
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">

  <script language="JavaScript">

  function insertIT(aValoreGG, aValoreMM, aValoreAA, aValoreLuogo, aIdUdienza)
  {
   // alert('insertIT');
  <%
    if( request.getParameter("campoGG") != null )
    {
    %>
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoGG")%>.value=aValoreGG;
    <%
    }

    if( request.getParameter("campoMM") != null )
    {
    %>
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoMM")%>.value=aValoreMM;
    <%
    }
    if( request.getParameter("campoAA") != null )
    {
    %>
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoAA")%>.value=aValoreAA;
    <%
    }
    if( request.getParameter("campoLuogo") != null )
    {
    %>
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoLuogo")%>.value=aValoreLuogo;
    <%
    }
    if( request.getParameter("campoID") != null )
    {
    %>
    //alert('IdUdienza restituito'+aIdUdienza);
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoID")%>.value=aIdUdienza;
    <%
    }
    %>
    window.parent.close();
    //self.close();
  }

  </script>
</head>

<body class="corpo" >
	<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaSelezioneProcedimentixUdienza">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento">

  	<table width=100%>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    		</td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;<font class="campo">Visualizza Procedimenti fissati per Udienza</font>
      	</td>
    	</tr>
  	</table>

  	<table width="100%">
    	<tr>
      	<td class="int">Data</td>
      	<td class="int">Presidente</td>
      	<td class="int">Giudici</td>
      	<td class="int">Collegio</td>
      	<td class="int">Selezione</td>
    	</tr>
<%
    	Iterator itx = udienze.iterator();
    	while ( itx.hasNext())
    	{
      	UdienzaModel lUdienza = (UdienzaModel)itx.next();
%>
      	<tr>
        	<td class="l"><%=DateUtils.getDateToString(lUdienza.getDataUdienza(), "dd-MM-yyyy")%> </td>
        	<td class="l"><%=StringUtils.toStringJSP(lUdienza.getDescrPresidente())%></td>
        	<td class="l"><%=StringUtils.toStringJSP(lUdienza.getDescrGiudice1())%> - <%=StringUtils.toStringJSP(lUdienza.getDescrGiudice2())%></td>
        	<td class="l"><%=StringUtils.toStringJSP(lUdienza.getNumCollegio())%></td>
        	<td class="c">
        		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento&tipo=<%=tipo%>&<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>=<%=lUdienza.getIdUdienza()%>&tiporicerca=<%=tiporicerca%>&tipoProc=<%=tipoProc%>" >
          	<img align="middle" src="/images/fileselected.gif" border="0"></a>
        	</td>
        	<input type="HIDDEN" name='IdUdi' value='<%=lUdienza.getIdUdienza()%>'>
      	</tr>
<%
        }
%>
		</table>
	</form>
</body>
</html>