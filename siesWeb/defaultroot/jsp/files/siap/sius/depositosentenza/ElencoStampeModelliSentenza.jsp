<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>


<html>
<head>
<script language="JavaScript1.2">
<!--
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
//-->
</script>

  <title>[S.I.E.S.] - Generazione Modelli Sentenza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/conferma.js"></script>

</head>


<body class="corpo">
<FORM name="comandi" >
 <table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;
      <font class="campo">Generazione Modelli Sentenza</font>
    </td>
   </tr>
 </table> 
</FORM>
 <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
 </table>
</body>

<STYLE>

.menulines{
	border:2.5px solid #BEC6FC;
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

</STYLE>

<link rel="STYLESHEET" type="text/css" href="/css/style.css">
<%
// Elenco stampe possibili
  Collection lListaStampe = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
  Iterator itx = lListaStampe.iterator();
%>

<body class=menu topmargin="0" leftmargin="0">
  <br>
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Selezionare il documento richiesto:<font class=ob></font></td>
      </tr>
    </table>
  <br>

  <div style="position: absolute; left: 0px; visibility: visible;" id="layer1">

  <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
<%
  int i = 0;
  while( itx.hasNext() ) // Ciclo per creazione griglia con nomi stampe
  {
      FunctionModel lFunModel = (FunctionModel)itx.next();
%>
      <tr>
        <td width="6%" >
        <td width="22%" class="menulines" nowrap >
          <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFunModel.getNameAction()%>');"><%=lFunModel.getLabelFunction()%></a>
        </td>
        <td width="22%" class="menulines" nowrap >
          <%if (itx.hasNext()) {i++;lFunModel = (FunctionModel)itx.next();%>
             <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFunModel.getNameAction()%>');"><%=lFunModel.getLabelFunction()%></a>
          <%}else{%> <td width="18%"></td> <% }%>
        </td>
        <td width="22%" class="menulines" nowrap >
          <%if (itx.hasNext()) {i++;lFunModel = (FunctionModel)itx.next();%>
             <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFunModel.getNameAction()%>');"><%=lFunModel.getLabelFunction()%></a>
         <%}else{%><td width="18%"></td> <% }%>
        </td>
        <td width="6%" >
      </tr>
<%
      i++;
  }
%>
  </table>
  </div>

</body>
</html>