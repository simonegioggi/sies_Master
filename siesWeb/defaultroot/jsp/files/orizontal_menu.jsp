<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="java.util.ListIterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="java.math.BigDecimal" %>

<jsp:useBean id="FunRadiceMenuOrz" scope="session" class="f3b.security.model.FunctionModel" />
<jsp:useBean id="FunAntenate" scope="session" class="java.util.LinkedList" />

<%
  ArrayList lFunFiglie = FunRadiceMenuOrz.getDaughtersFunctions();
  int lSize = lFunFiglie.size();
  
  UtenteModel lUteMod=(UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod= lUteMod.getUfficioUtente();
  String codUff = new String(lUffMod.getCodTipoUfficio());
%>

<html>
<head>
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function BoldIT(id)
{
	var currentLink=eval(document.id);
	for (var i=0;i<document.links.length;i++)
	{
		document.links(i).style.color='black';
	}
	document.links(id*1).style.color='blue';
}

var ThisLayerID=1;
<%
  double lNumLayer = Math.ceil((lSize/5.0));
%>
var NumOfLayer = <%=lNumLayer%>;

var node;

function VisibleIT(A_rel_Vis)
{
	if (A_rel_Vis=='+')
	{
		if (!(ThisLayerID==NumOfLayer))
		{
			ThisLayerID ++;
			for (var i=1;i<=NumOfLayer;i++)
				{
					node=document.getElementById('layer'+i);
					node.style.visibility='hidden';
				}
			node=document.getElementById('layer'+ThisLayerID);
			node.style.visibility='visible';
		}
	} else
	{
		if (!(ThisLayerID==1))
		{
			ThisLayerID --;
			for (var i=1;i<=NumOfLayer;i++)
				{
					node=document.getElementById('layer'+i);
					node.style.visibility='hidden';
				}
			node=document.getElementById('layer'+ThisLayerID);
			node.style.visibility='visible';
		}
	}
}
</script>

<script language="JavaScript1.2">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
function over_effect(e,state){
if (document.all)
source4=event.srcElement
else if (document.getElementById)
source4=e.target
if (source4.className=="menulines")
source4.style.borderStyle=state
else{
while(source4.tagName!="TABLE"){
source4=document.getElementById? source4.parentNode : source4.parentElement
if (source4.className=="menulines")
source4.style.borderStyle=state
}
}
}
</script>
</HEAD>
<style>
.menulines{
	border:2.5px solid #F0F0F0;
	text-align : center;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 11px;
	text-decoration : none;
  height:100%;
}

.menulines a{
	text-align : center;
	text-decoration:none;
	color:black;
	font-family: 'Tahoma';
	color : Navy;
	font-size : 11px;
	text-decoration : none;
  width:100%;
  height:100%;
}
</style>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<body class=menu topmargin="0" leftmargin="0">
<table  cellpadding="1" cellspacing="1" width="100%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
  <tr>
    <td width="7%" style="FONT-SIZE: 11px;">
<!--
    Sei in :
-->
    </td>
    <td width="93%">
<%
    ListIterator lListIterAntenati = FunAntenate.listIterator();
    int lSizeAntenati = FunAntenate.size();
    if(lListIterAntenati.hasNext())
    {
      FunctionModel lFun = (FunctionModel)lListIterAntenati.next();
      if(lSizeAntenati==1)
      { %>
        <strong>
<%    } %>

    <%if (lFun.getFunctionId().compareTo(new BigDecimal(90110729)) == 0 && !codUff.equals("TDSM")) {
		// per gli uffici diversi da TDSM la funzione 
 		// "Sentenza Rinvio Udienza" non viene visualizzata
	  } else { %>
      	<a class=menu href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.security.action.ActLoadOrizontalMenu&<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>=<%=lFun.getFunctionId()%>"><font style="FONT-SIZE: 11px;"><%=lFun.getLabelFunction()%></font></a>
<%
	  }
	  if(lSizeAntenati==1)
      { %>
        </strong>
<%    }
    }
    if(lListIterAntenati.hasNext())
    {
      for(int i = 0; i<(lSizeAntenati-1); i++)
      {
        FunctionModel lFun = (FunctionModel)lListIterAntenati.next();
%>
        <font color="#ffffff" style="FONT-SIZE: 12px;"><strong>&raquo;</strong></font>
<%
        if((i+2)==lSizeAntenati)
        { %>
          <strong>
<%      } %>

       <%if (lFun.getFunctionId().compareTo(new BigDecimal(90110729)) == 0 && !codUff.equals("TDSM")) {
			// per gli uffici diversi da TDSM la funzione 
 			// "Sentenza Rinvio Udienza" non viene visualizzata
		 } else { %>
        	<a class=menu href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.security.action.ActLoadOrizontalMenu&<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>=<%=lFun.getFunctionId()%>"><font style="FONT-SIZE: 11px;"><%=lFun.getLabelFunction()%></font></a>
<%
		 }
		if((i+2)==lSizeAntenati)
        { %>
          </strong>
<%      }
      }
    }
%>
    </td>
  </tr>
</table>
<br>
<%
  Iterator lIter = lFunFiglie.iterator();
  for(int i=1; i<=lNumLayer; i++)
  {
%>
    <div style="position: absolute; top: 20px; left: 0px; visibility: <%=(i==1) ? "visible" : "hidden"%>;" id="layer<%=i%>">
      <table cellpadding="1" cellspacing="1" width="100%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
        <tr>
          <td width=5% <% if(lNumLayer > 1)
                          { %>
                            class="menulines"><a href="javascript:VisibleIT('-');"><strong>&laquo;</strong></a>
                       <% }
                          else
                          { %>
                            >
                       <% } %>
          </td>
<%
          for(int j=0; j<5; j++) // 5 è il numero fisso di celle per riga
          {
%>
            <td width="18%" <% if(lIter.hasNext())
                               {
                                 FunctionModel lFun = (FunctionModel)lIter.next();

                                 if (lFun.getFunctionId().compareTo(new BigDecimal(90110729)) == 0 && !codUff.equals("TDSM")) {
	                	  			// per gli uffici diversi da TDSM la funzione 
	                		  		// "Sentenza Rinvio Udienza" non viene visualizzata
	                  	  		} else {
	                  	  	 %>     
                                 
                                 class="menulines"><a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.security.action.ActLoadOrizontalMenu&<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>=<%=lFun.getFunctionId()%>"><%=lFun.getLabelFunction()%></a>
                            <%  }
                               }
                               else
                               { %>
                                 >
                            <% } %>
            </td>
<%
          }
%>
          <td width=5% <% if(lNumLayer > 1)
                          { %>
                            class="menulines"><a href="javascript:VisibleIT('+');"><strong>&raquo;</strong></a>
                       <% }
                          else
                          { %>
                            >
                       <% } %>
          </td>
        </tr>
      </table>
    </div>
<%
  }
  if(lSize!=0)
  {
%>
    <script>
      window.parent.body.location.href='/html/blankGray.htm';
    </script>
<%
  }
  else
  {
    FunctionModel lFun = (FunctionModel)FunAntenate.getLast();
%>
    <script>
      window.parent.body.location.href='/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>';
    </script>
<%
  }
%>

</body>
</html>