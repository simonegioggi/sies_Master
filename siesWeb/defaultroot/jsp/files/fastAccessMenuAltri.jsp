<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="FunRadiceMenuSceltaRapida" scope="session" class="f3b.security.model.FunctionModel" />

<%
//==============================================================================
// JSP di visualizzazione del menù di scelta rapida per utenti non SIEP
//==============================================================================
%>

<HTML>
<HEAD>

<META http-equiv=Content-Type content="text/html; charset=windows-1252">

<link rel="STYLESHEET" type="text/css" href="/css/menu.css">
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  var icontahelp = 0;
  function openHelp()
  {
    var desktop;
   
    //==========================================================================
    //n.b. Sulla request viene passato un parametro fittizio icontahelp per 
    //     evitare che la pagina resti nella cache del browser o del Proxy.
    //     Infatti ci si è accorti che passando per il proxy la pagina restituita
    //     era sempre la stessa. Aggiungendo un parametro random si genera una
    //     GET sempre diversa per cui la pagina restituita non viene "meccia" mai 
    //     con quella nella cache.
    //==========================================================================
    var icontahelp=Math.random();
    
    desktop=window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.helponline.action.ActLoadDettaglioHelponline&ContaHelp="+ icontahelp, "HelpOnLine", "toolbar=no,location=yes,status=no,menubar=no,resizable=yes,scrollbars=yes,top=0,left=0");
    desktop.window.resizeTo(screen.availWidth,screen.availHeight);
    desktop.focus();
  }
</script>

<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  function BoldIT(id){
    var currentLink=eval(document.id);
    for (var i=0;i<document.links.length;i++) {
      document.links(i).style.color='navy';
    }
    document.links(id*1).style.color='blue';
  }
  function ViewCalcoloRapidoDellaPena()
  {
  	var desktop;
  	desktop=window.open("/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActLoadCalcolatrice", "Calcolatrice","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=850,height=580,top=0,left=0");
  }
--%>
</script>

<script language="JavaScript1.2">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  function over_effect(e,state) {
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
<link rel="STYLESHEET" type="text/css" href="/css/style.css">

<!--BODY class=menu marginheight="0" marginleft="0" topmargin="0" leftmargin="0"-->
<body class="menu" topmargin="5" leftmargin="0" style="{ border-top-style : solid;border-top-color : White; border-top-width : 1px; } ">
  <!-- table border="1" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')" -->
  <!-- width="100%"  -->
  <table cellpadding="1" cellspacing="1" align="left"
         onMouseover="over_effect(event,'outset')" 
         onMouseout="over_effect(event,'solid')" 
         onMousedown="over_effect(event,'inset')" 
         onMouseup= "over_effect(event,'outset')">
    <tr>
      <td width="10px">&nbsp;</td>
      <td width="100%">&nbsp;</td>
      <td class="menulines" >
          <a href="Javascript:openHelp();" >
            <img align="middle" src="/images/P_Help.GIF" width="32" height="32" alt="" border="0" title="Help On Line">
          </a>
      </td>
    </tr>
  </table>
</BODY>
</HTML>