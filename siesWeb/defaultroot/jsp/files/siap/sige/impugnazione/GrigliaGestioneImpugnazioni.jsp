<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="f3b.web.IWebConstants" %>
<jsp:useBean id="codTipoImpugnazione"  scope="session" class="java.lang.String"/>
<jsp:useBean id="TornaQui"  scope="request" class="java.lang.String"/>


<%
String labelTipoImpugnazione = "Opposizioni";

if (codTipoImpugnazione.equals("01"))
	labelTipoImpugnazione = "Ricorsi";
%>


<html>
  <head>
  <title>[S.I.E.S.] - Ricerche </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

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
          <font class="label">Funzione :&nbsp;</font><font class="campo">Elenco Procedimenti per <%=labelTipoImpugnazione%></font>
        </td>
    </tr>
    </table>

    <br />
    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
	  <tr>
         <td colspan=3 class="Titolonocap"><%=labelTipoImpugnazione %></td>
      </tr>
      <tr>
		<td width="33%" class="menulines" nowrap>
		   <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadProvveddimentiOpposizioni&TornaQui=<%=TornaQui%>">Inserimento <%=labelTipoImpugnazione%></a>
    	</td>
		<td width="33%" class="menulines" nowrap>
		   <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadElencoImpugnazioniByIdFascicoloEsitoDecisione&TornaQui=<%=TornaQui%>">Esito Decisione</a>
    	</td>
    	<td width="33%" class="menulines" nowrap>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadElencoImpugnazioniByIdFascicolo&TornaQui=<%=TornaQui%>">Elenco <%=labelTipoImpugnazione%> per il Provvedimento</a>
        </td>
      </tr>    
  
  </table>
</body>
</html>