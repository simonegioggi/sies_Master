<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<jsp:useBean id="fascicolo" scope="session"  class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="UtenteConnesso" scope="session"  class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="Stampabile" scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<script language="JavaScript">

   function stampaSiep(lAzione)
   {
      var  hrefStampa = lAzione;
      var lIndice = hrefStampa.indexOf("?");

      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);

      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
   }
   
</script>
	<!-- BOTTONE DI STAMPA -->
	<td class="LBG">
		<a href="Javascript:stampaSiep('<%=request.getParameter("ActionLink")%>')" onclick="javascript:lookUpload();">
			<img  align="middle" src="/images/printPDF.gif" alt="Generazione Stampa PDF" width="24" height="24" border="0">
		</a>
	</td>