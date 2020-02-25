<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>


<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<jsp:useBean id="ElencoTemplate"     scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoTemplate"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Stampabile"              scope="request" class="java.lang.String"/>


<script language="JavaScript">
    function stampaT(lAzione)
    {
       var node;
       node=document.getElementById('ListaTemplate');

       var  link = "<%=IWebConstants.ACTION_FIELD%>="+lAzione+"&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=";
       var template = node.options[node.options.selectedIndex].value;
       link = link + template;
//       alert(link);
    stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  link);
    }
</script>

<html>
<%
// Elenco template di stampa solo se il documento è stampabile
if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";
if (Stampabile.compareTo("SI") == 0)
{  %>
<%
   if ((ElencoTemplate == null) || (ElencoTemplate.length() == 0))
   {
%>
       <td class="l"> <font color="red"> Modelli di stampa non disponibili !</font> </td>

<% } else {

      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

      //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO))
          {
%>
				<td class="l">
                  <select Title="Modello di stampa" name="ListaTemplate" >
                  <%=ElencoTemplate%>
                  </select>
                 <a href="Javascript:stampaT('<%=lFun.getNameAction()%>')" >
                 <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
                 </a>
               </td>

<%
             if (AutoTemplate.compareTo("")!= 0)
             {
%>
               <td class="l">default:</td>
               <td class="l">  <%=AutoTemplate%> </td>
<%           }
          }
        }
}
%>

<% }
}
%>
</html>