<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- MEV_65: refactoring --%>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />
<style>
A.pager, A.pager:ACTIVE, A.pager:FOCUS, A.pager:HOVER, A.pager:LINK, A.pager:VISITED {
        font-family: 'Tahoma';
        color : Navy;
        font-size : 11px;
        text-decoration : none;
        cursor:normal;
}
</style>

<%
//BigDecimal CountRisultati = (BigDecimal) request.getParameter("CountRisultati");
BigDecimal CountRisultati = (BigDecimal) request.getAttribute("CountRisultati");

int NumeroTotalePagine=new BigDecimal(CountRisultati.doubleValue()/IWebConstants.RESULT_PER_PAGE_ESITO).intValue();
int ThisPage=Integer.parseInt(request.getAttribute(IWebConstants.NUM_PAGE).toString());
%>

<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%
  int PagesPerLayer=25;
  double lNumLayer = Math.ceil((CountRisultati.doubleValue()/(PagesPerLayer*IWebConstants.RESULT_PER_PAGE_ESITO)));
//  double MaxPage= Math.floor(CountRisultati.doubleValue()/IWebConstants.RESULT_PER_PAGE_ESITO)+1;
  int MaxPage = CountRisultati.intValue()/IWebConstants.RESULT_PER_PAGE_ESITO; // troncamento
  int resto = CountRisultati.intValue()%IWebConstants.RESULT_PER_PAGE_ESITO;
  if (resto > 0) MaxPage +=1;

%>
var node;
var NumOfLayer = <%=lNumLayer%>;
var NumOfRecord=<%=CountRisultati.doubleValue()%>;
var MaxPage = <%=MaxPage%>;
var ThisPage = <%=ThisPage%>;
var ThisLayer= parseInt(ThisPage/<%=PagesPerLayer%>)+1;
if (ThisLayer-1==ThisPage/<%=PagesPerLayer%>)
   ThisLayer--;
if (parseInt(NumOfLayer)!=NumOfLayer)
   NumOfLayer++;

<%
// Sostituisce allo spazio il carattere "%20" per evitare errori nella costruzione del link
RequestForPaging = RequestForPaging.replaceAll(" ","%20");
%>

function VisibleIT(nextPage)
{

		node=document.getElementById('layer');
		var tmp;
		var Lbound=parseInt((parseInt(nextPage)-1)*<%=PagesPerLayer%>)+1;
		var Ubound=(parseInt(nextPage))*<%=PagesPerLayer%>;
                if (Ubound>MaxPage)
                     Ubound=MaxPage;

		var Lpage=nextPage-1;
		if(Lpage==0)
			Lpage=1;
		var Upage=parseInt(nextPage)+1;
		if (Upage>NumOfLayer)
                     Upage=NumOfLayer;
		tmp="<table cellpadding=1 cellspacing=3  border=1 bordercolor=white>";
        tmp+="<tr bordercolor=#DEDEDE>";
   // Luigi 26-2-2004
     tmp+="<td bgcolor=#000080 colspan=29 align=center><font color=#FFFFFF>Pagina <b><%=ThisPage%></b> di <%=MaxPage%> ,  per un totale di <b><%=CountRisultati.intValue()%></b> Risultati</font></td>"
        tmp+="</tr>";
        <%
		if (MaxPage > 1)
    	{%>
        	tmp+="<tr bordercolor=#DEDEDE>";
            if(nextPage>1)
				tmp+="<td width=15 align=center style=\"cursor:pointer\"><a class=pager href=javascript:VisibleIT('"+Lpage+"');><strong>&laquo;&laquo;</strong></a></td>";
			if (ThisPage > 1)
    		{
       			tmp+="<td width=15 align=center style=\"cursor:pointer\"><a href=<%=RequestForPaging%>&<%=IWebConstants.NUM_PAGE%>=<%=ThisPage-1%>&CountRisultati=<%=CountRisultati%> class=pager><strong>Precedente</strong></a></td>";
   			}
			for (var i=parseInt(Lbound);i<=parseInt(Ubound);i++)
			{
				if (i==ThisPage)
				{
					tmp+="<td width=15 align=center bgcolor=#000080 style=\"cursor:pointer\"><font color=#FFFFFF>"+i+"</font></td>";
				}else
				{
					tmp+="<td width=15 align=center style=\"cursor:pointer\"><a href=<%=RequestForPaging%>&<%=IWebConstants.NUM_PAGE%>="+i+"&CountRisultati=<%=CountRisultati.intValue()%> class=pager>"+i+"</a></td>";
				}
			}
			if (ThisPage< MaxPage)
			{
				tmp+="<td width=15  align=center style=\"cursor:pointer\"><a href=<%=RequestForPaging%>&<%=IWebConstants.NUM_PAGE%>=<%=ThisPage+1%>&CountRisultati=<%=CountRisultati%> class=pager><strong>Successiva</strong></a></td>";
			}
            if(NumOfLayer!=nextPage)
		    	tmp+="<td width=15 align=center style=\"cursor:pointer\"><a class=pager href=javascript:VisibleIT('"+Upage+"');><strong>&raquo;&raquo;</strong></a></td>";
		tmp+="</tr>"
		<% } %>
		tmp+="</table>";
		node.innerHTML=tmp;
}
</script>

<table width=100%>
  <tr>
    <td width=5%>&nbsp;</td>
    <td width=95%>
       <div id="layer">
         <table><tr><td></td></tr></table>
       </div>
    </td>
  </tr>
</table>
<script language="JavaScript">


VisibleIT(ThisLayer);


</script>