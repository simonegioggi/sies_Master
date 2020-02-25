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
  <title>[S.I.E.S.] - Ricerche </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  <script language="Javascript">
		function VisualizzaMessaggio(){
			alert("Funzione in fase di sviluppo");
		}	
  </script>			

  <script language="JavaScript1.2">
    <!--
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
    //-->
  </script>
  
  <STYLE>
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
  </STYLE>  	
  </head>

<body class="corpo">
    <table>
    <tr>
        <td class="lbg">
          <font class="label">Funzione :&nbsp;</font><font class="campo"><%= lFunzione%></font>
        </td>
    </tr>
    </table>

    <br>
    
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	  <tr>
         <td colspan=3 class="Titolonocap">Foglio Complementare</td>
      </tr>
      <tr>
		<td width="32%" class="menulines" nowrap>
		   <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActLoadRicercaFogliComplementari">Fogli Complementare Redatti</a>
    	</td>
		<td width="32%" class="menulines" nowrap>
		   <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActLoadRicercaOrdinanzePriveDiFC">Ordinanze Prive di Foglio Complementare</a>
    	</td>
    	<td width="32%">
          &nbsp;
        </td>
      </tr>    
  
  </table>
</body>
</html>