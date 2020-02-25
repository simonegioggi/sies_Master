<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>
<%@ page import="siap.sius.udienza.model.UdienzaModel" %>
<%@ page import="siap.sius.udienza.model.UdienzaNModel" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>


<jsp:useBean id="udienze" scope="request" class="java.util.Vector" />
<!--jsp:useBean id="num_proc" scope="request" class="java.lang.String" /-->

<%
   // Flag presenza Numero Procedimenti per Udienza
   boolean numPrc = false;
  // if(num_proc != null && num_proc.trim().length() > 0)
   if( request.getAttribute("num_proc") != null)
    numPrc = true;
%>



<html>
<head>
  <title>[S.I.E.S.] - Elenco Udienze</title>
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">

  <script language="JavaScript">

  function insertIT(aValoreGG, aValoreMM, aValoreAA, aValoreLuogo, aIdUdienza, aNumCollegio)
  {
//    alert('insertIT');
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
    if( request.getParameter("campoID") != null )
    {
    %>
//    alert('IdUdienza restituito'+aIdUdienza);
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoID")%>.value=aIdUdienza;
    <%
    }
    if( request.getParameter("campoColl") != null )
    {
    %>
//    alert('collegio'+aNumCollegio);
    window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoColl")%>.value=aNumCollegio;
    <%
    }
    if( request.getParameter("campoLuogo") != null )
    {
    %>
//    alert('campoLuogo restituito'+aValoreLuogo);
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("campoLuogo")%>.value=aValoreLuogo;
    <%
    }
    if( request.getParameter("campo_sub") != null )
    {
    %>
          window.parent.opener.document.<%=request.getParameter("formname")%>.submit();
          window.parent.close();
    <%
    }
    else
    {  %>
       window.parent.close();
<%   }
    %>
  }

  </script>


</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">Elenco Udienze</td>
    </tr>
  </table>

  <table width="100%">
    <tr>
      <td class="int">Data</td>
      <td class="int">Presidente</td>
      <td class="int">Giudice Relatore</td>
      <td class="int">Collegio</td>
      <td class="int">Luogo Udienza</td>
<% 
		if(numPrc) 
		{					
%>
      <td class="int" width=15%>Proc. a ruolo <br> Totali (da rinvio)</td>
<%	
		} 
%>
      <td class="int">Selezione</td>
    </tr>
<%
    Iterator itx = udienze.iterator();
    while ( itx.hasNext())
    {
    	UdienzaModel lUdienza = (UdienzaModel) itx.next();
%>
      <tr>
        <td class="l"><%=DateUtils.getDateToString(lUdienza.getDataUdienza(), "dd-MM-yyyy")%> </td>
        <td class="l"><%=StringUtils.toStringJSP(lUdienza.getDescrPresidente())%></td>
        <td class="l"><%=StringUtils.toStringJSP(lUdienza.getDescrGiudice1())%></td>
        <td class="c"><%=StringUtils.toStringJSP(lUdienza.getNumCollegio())%></td>
        <td class="l"><%=StringUtils.toStringJSP(lUdienza.getLuogoUdienza())%></td>
<% 
		if(numPrc) 
		{
    	UdienzaNModel lUdienza2 = (UdienzaNModel)  lUdienza;
%>
      <td class="l"><%=StringUtils.toStringJSP( lUdienza2.getNumProvvedimenti())%>&nbsp;&nbsp;
      	<%= "(" + StringUtils.toStringJSP( lUdienza2.getNumProvvedimentiDaRinvio()) + ")"%>
      </td>

<%	} %>
        <td class="c">
        	<a href="Javascript:insertIT(	'<%=DateUtils.getDateToString(lUdienza.getDataUdienza(), "dd")%>',
                                       	'<%=DateUtils.getDateToString(lUdienza.getDataUdienza(), "MM")%>',
                                       	'<%=DateUtils.getDateToString(lUdienza.getDataUdienza(), "yyyy")%>',
                                       	'<%=StringUtils.cStrForJS(lUdienza.getLuogoUdienza())%>',
                                       	'<%=lUdienza.getIdUdienza()%>',
                                      	'<%=lUdienza.getNumCollegio()%>');">


          <img align="middle" src="/images/fileselected.gif" border="0"></a>
        </td>
        
        <input type="HIDDEN" name='IdUdi' value='<%=lUdienza.getIdUdienza()%>'>
      </tr>
<%
        }
%>
</table>

</body>
</html>