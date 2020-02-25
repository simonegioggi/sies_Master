<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbpren.model.SbPrenModel"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<jsp:useBean id="autentica" scope="request" class="java.lang.String"/>
<jsp:useBean id="indirizzoBdmc" scope="request" class="java.lang.String"/>
<html>
<head>
  <title> Collegamento Bdmc </title>
   
   <script language="JavaScript">
  function Collega() { 	
       
  	      document.LoadRicercaSbPren.submit();
       
  	}
  	</script>
</head>
<body onLoad="Collega()" class="corpo">
<FORM method="POST"  action="<%=indirizzoBdmc%>/BDMC/isies/login.do" name="LoadRicercaSbPren">

    
   <input type="hidden" name="autenticazione" value="<%=StringUtils.toStringJSP(autentica) %>" />
  </FORM>
</body>
</html>