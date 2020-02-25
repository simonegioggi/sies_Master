<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="f3b.security.model.FunctionModel"%>

<jsp:useBean id="nome_funzione" scope="request" class="java.lang.String" />
<jsp:useBean id="titolo" scope="request" class="java.lang.String" />

<% 

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
String lFunzione = 	"Ricerche";
String lTitolo = "Procedimento";
if (nome_funzione != null && nome_funzione.length() > 1)
		lFunzione = nome_funzione;
if (titolo != null && titolo.length() > 1)
		lTitolo = titolo;
	

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

<script language="JavaScript">

  function submFunzione(NomeAction)
  {
   var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=";
   aLink += NomeAction
   document.location.href = aLink;
   
  }
  </script>
  
  <script language="JavaScript">
// per link vuoto
function submvuoto()
 {
 }

// per link a messaggio "funzione da implementare"
function submDaImplementare()
 {
   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.web.ActionUnderConstruction";
 }

function submProcedimentiNonValidati()
 {
   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActRicercaFascicoliNonValidatiPerMessaggio";
 }



</script>

	<script language="JavaScript1.2">
 		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>

  <style>
	  .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </style>
  <title>[S.I.E.S.] - Ricerche </title>
</head>

<body class="corpo">
    <table>
    <tr>
        <td class="lbg">
          <font class="label">Funzione :&nbsp;</font><font class="campo"><%= lFunzione%></font>
        </td>
    </tr>

    </table>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
    <tr><td class="Titolo" colspan=4><%=lTitolo%></td></tr>
    <tr>
    <%
      //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
	  	int numBottone = 0;
	    Iterator lIterFunzione = lFunFiglie.iterator();
	    FunctionModel lFun = null;
	    while(lIterFunzione.hasNext())
	    {
	      lFun = (FunctionModel)lIterFunzione.next();
	      if (lFun.getVisualizzazionType().equalsIgnoreCase(ICostantiFunzioni.FUNZIONE_BOTTONE))
	      {
	    	  numBottone++;
	   %>
	   	<td width="32%" class="menulines" nowrap><a href="javascript:submFunzione('<%=lFun.getNameAction()%>');"><%=lFun.getLabelFunction()%></a></td>
	   <%
	      } 
	      if ( (numBottone % 3) == 0)
	      {
	    	  // Salto riga ogni 3 bottoni
	   	   %>	  
	   	    </tr>
	   	    <tr>
	       <%	   	     	  
	      }
	    }
    }
	  %>
    </tr>
  </table>
</body>
</html>