<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>

<html>
	<head>
		<title>[S.I.E.S.] - <%=strFunzione%></title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
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
  
  <script language="Javascript">
				function VisualizzaMessaggio()
				{
						alert("Funzione in fase di sviluppo");
				}	
				
				function CaricaModuloWebNsc()
				{
						var desktop;
						desktop=window.open("/jsp/Main.jsp?Action=siap.sico.webservice.action.ActCollegamentoNscSsl", "Interoperabilita", "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width="+ screen.availWidth +",height=" + screen.availHeight + ",top=0,left=0");
				}	
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
	</head>

	<body class="corpo">
  		<table>
    			<tr>
				      <td class="LBG">
				        <font class="label">Funzione :</font>&nbsp;
				        <font class="campo"><%=strFunzione%></font>
				      </td>
				      <td class="LBG">
				      </td>
	     			</tr>
  		</table>
  
  <br>	
  
  	<table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
		      <tr>
			        <td colspan=3 class="Titolonocap">Interoperabilità con NSC</td>
		      </tr>
		      <tr>
			        <td width="32%" class="menulines" nowrap>
			        	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			          	<%-- <a href="< %=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.webservice.action.ActCollegamentoNscSsl">Modulo Web NSC-SIES</a>--%>
			           	<a href="Javascript:CaricaModuloWebNsc()">Modulo Web NSC-SIES</a>
			        </td>
			        <td width="32%" class="menulines" nowrap>
			          <a href="Javascript:VisualizzaMessaggio()">Visualizzazione Messaggi da NSC</a>
			        </td>
			        <td width="32%" class="menulines" nowrap>
			          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.webservice.action.ActLoadRicercaTrasmissioni">Visualizzazione Dati Trasmissioni</a>
			        </td>
		      </tr>
		      <tr>
		      		<td width="32%" class="menulines" nowrap>
			          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.webservice.action.ActLoadRicercaTitoloEsecutivoTrasferito">Visualizzazione Procedimenti Trasferiti</a>
			        </td>
			    </tr>
	  </table>

	</body>
</html>