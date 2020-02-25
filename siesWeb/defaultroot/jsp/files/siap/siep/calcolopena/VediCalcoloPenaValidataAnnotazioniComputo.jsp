<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>

<jsp:useBean id="lFlagPage" scope="request" class="java.lang.String" />

<%
//==============================================================================
//
// Form di visualizzazione della pena corrente e scelta delle stampe nel caso di 
// computi:
// - Presofferto
// - Fungibilità Misura Cautelare
// - Fungibilità Pena Detentiva
// - 
//==============================================================================
%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

<script language="JavaScript">
  function Verify()
  {
    if (document.f.annotazioni[document.f.annotazioni.selectedIndex].value=="-")
    {
      alert("Selezionare il Procedimento");
      return false;
    }
  }

  function submPeneEspiate(id)
  {
   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadInserisciComputoCustodiaCautelare&flagPage="+id;
    //document.f.submit();
  }

  function submFung(id)
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadInserisciFungibilita&flagPage="+id;
  }

  function submRid(id)
  {
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadEmissioneProvvedimento&flagPage="+id;
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
	  	font-size : 11px;
	  	text-decoration : none;
	  	height:100%;
	  	font-weight : normal;
	  }

	  .menulines a
	  {
	  	text-align : center;
	  	text-decoration:none;
	  	color:black;
	  	font-family: 'Tahoma';
	  	color : Navy;
	  	font-size : 11px;
	  	width:100%;
	  	height:100%;
	  }
  </style>
  <title>[S.I.E.S.] - Provvedimenti e Stampe per annotazioni Manuali </title>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=lbg>
        <font class="label">Funzione :&nbsp;</font><font class="campo">Provvedimenti e Stampe per Rideterminazione Pena</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <jsp:include page="/jsp/files/siap/siep/calcolopena/IntestazionePenaValidataAnnotazioni.jsp"/>
    <br>
    
    <table cellpadding="1" cellspacing="1" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
      <tr>
        <td colspan=2 class="Titolonocap">Stampe</td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      <%
      //========================================================================
      //                       COMPUTO CUSTODIA CAUTELARE
      //========================================================================
      %>
      <tr>
        <td colspan=2 class="Titolonocap">Computo custodia cautelare</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:submPeneEspiate('<%=lFlagPage%>');">Stesso reato</a></td>
        <td width="32%" class="menulines" nowrap><a href="javascript:submPeneEspiate('<%=lFlagPage%>');">Altro reato (fungibilità)</a></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      <%
      //========================================================================
      //                         COMPUTO PENA DETENTIVA
      //========================================================================
      %>
      <tr>
        <td colspan=2 class="Titolonocap">Computo pena detentiva</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:submFung('<%=lFlagPage%>');">Espiata per altro reato (fungibilità)</a></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      <%
      //========================================================================
      //                         ORDINE DI ESECUZIONE
      //========================================================================
      %>
      <tr>
        <td colspan=2 class="Titolonocap">Ordine di esecuzione</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:submRid('RP')">Rideterminazione pena</a></td>
      </tr>
    </table>
  </form>
</body>
</html>