<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.GregorianCalendar" %>

<%

String[]  Mesi = { "", "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre" };

String mese=request.getParameter("mesi");
String oggi=DateUtils.getDayToString(DateUtils.getSysDate());
if (mese==null)
   mese="";
String anno="";
if (mese.equals(""))
{
  anno=DateUtils.getYearToString(DateUtils.getSysDate());
  mese=DateUtils.getMonthToString(DateUtils.getSysDate());
}else
{
  mese=request.getParameter("mesi");
  anno=request.getParameter("anno");
}

String data="01/"+mese + "/" + anno;
String selected;

%>
<html>
<head>
<title>Calendario</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function InsertIT(anno,mese,giorno) {
    if (mese.length==1)
	  mese="0"+mese;
	if (giorno.length==1)
	  giorno="0"+giorno;
	window.opener.document.<%= request.getParameter("formname") %>.<%= request.getParameter("fieldyear") %>.value=anno;
	window.opener.document.<%= request.getParameter("formname") %>.<%= request.getParameter("fieldmonth") %>.value=mese;
	window.opener.document.<%= request.getParameter("formname") %>.<%= request.getParameter("fieldday") %>.value=giorno;
	window.close();
}
</script>

</head>

<body leftmargin="5" class=corpo>
<div align="center"><form name="frmCalendario" action="Calendario.jsp">
<input type="hidden" name="formname" value="<%= request.getParameter("formname") %>">
<input type="hidden" name="fieldyear" value="<%= request.getParameter("fieldyear") %>">
<input type="hidden" name="fieldmonth" value="<%= request.getParameter("fieldmonth") %>">
<input type="hidden" name="fieldday" value="<%= request.getParameter("fieldday") %>">
<table class="bordo">
  <tr>
    <td colspan=4 align="right">
      <select name="mesi" onchange="frmCalendario.submit();">
<%    for (int Index=1;Index<=12;Index++)
      {
        if (Index==Integer.parseInt(mese))
        {
          selected="selected";
        }else
          selected="";
%>
      <option value='<%=Index%>' <%=selected%>><%=Mesi[Index]%></option>
<%    }
%>
      </select>&nbsp;
    </td>
    <td colspan=4>&nbsp;
      <select name="anno" onchange="frmCalendario.submit();">
<%    for (int cont=1900;cont<=2099;cont++)
      {
        if (Integer.parseInt(anno)==cont)
        {
          selected="selected";
        }else
          if (anno.equals("") && cont==Integer.parseInt(DateUtils.getYearToString(DateUtils.getSysDate())))
          {
            selected="selected";
            }else
              selected="";

%>    <option value='<%=cont%>' <%=selected%>><%=cont%></option>
<%
      }
%>
      </select>
    </td>
  </tr>
  <tr>
    <td colspan=7 height="3"></td>
  </tr>
  <tr>
	<td class="titolo"><b>Lun</b></td>
	<td class="titolo"><b>Mar</b></td>
	<td class="titolo"><b>Mer</b></td>
	<td class="titolo"><b>Gio</b></td>
	<td class="titolo"><b>Ven</b></td>
	<td class="titolo"><b>Sab</b></td>
	<td class="titolo"><b>Dom</b></td>
  </tr>
  <tr>
<%
	//col=weekday(data,vbmonday)
String styleStr;
        GregorianCalendar Gc=new GregorianCalendar(Integer.parseInt(anno),Integer.parseInt(mese)-1,1);
        int col=Gc.get(GregorianCalendar.DAY_OF_WEEK)-1;

        if (col==0) col=7;

	if (col>1)
          out.print("<td colspan=" +(col-1) + ">&nbsp;</td>" );
	for (int i=1;i<=Integer.parseInt(DateUtils.getDayToString(DateUtils.getEndOfMonth(anno,mese)));i++)
        {
		if (col>7)
                {
			col=1;
			out.print( "</tr>");
			out.print( "<tr>");
		}
		out.print("<td align='center' class='R'>");
                styleStr="text-decoration: none; color: Blue;";
                if (i==Integer.parseInt(oggi) && anno.equals(DateUtils.getYearToString(DateUtils.getSysDate())) && mese.equals(DateUtils.getMonthToString(DateUtils.getSysDate())))
                         styleStr="text-decoration: none; color: white; background-color : navy;";
		%><a href="Javascript:InsertIT('<%= anno %>','<%= mese %>','<%= i %>');" style="<%=styleStr%>"><%= i %></a><%
		out.print("</td>");
		col++;
        }
%>
</tr>
</table>
<br>
<font color="Navy">
<strong>Cliccare sul giorno da selezionare</strong>
</font>
</form></div>
</body>
</html>