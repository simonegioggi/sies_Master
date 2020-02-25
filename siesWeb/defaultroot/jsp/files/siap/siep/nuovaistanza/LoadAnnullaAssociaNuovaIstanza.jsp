<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="fascicoloSiep" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
  <head>
  <title>[S.I.E.S.] - Annullamento Associazione Registro Istanza - Fascicolo Siep </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" >

	<%
	boolean confAnnulla = false;
	%>
	function confermaAnnullamento()
	{
		if (confirm ('Il collegamento del Registro Istanza <%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>  al Fascicolo Siep <%=fascicoloSiep.getChiaveAnno()%>/<%=fascicoloSiep.getChiaveProgr()%> sarà annullato. Proseguire ?'))
			LoadAnnullaAssociaNuovaIstanza.submit();
		else
			LoadRicaricaRegistro.submit();
	}

	</script>
	
  </head>

 <body class="corpo" onLoad="Javascript: confermaAnnullamento();">
   <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadAnnullaAssociaNuovaIstanza'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActAnnullaAssociaRIaFascicoloSIEP">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=fascicolo.getIdFascicoloSiep()%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>" value="<%=fascicolo.getFasSieIdFascicoloSiep()%>">
   </form>
      <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicaricaRegistro'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActLoadDettaglioFascicolo">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=fascicolo.getIdFascicoloSiep()%>">
   </form>
</body>
</html>