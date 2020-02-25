<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%--@ taglib prefix="s" uri="/struts-tags" --%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="oggettoIstanza"   scope="request" class="java.lang.String"/>
<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="Cumulato"            scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="autoritaEmiCumuloSentenzaDecreto" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmiCumulo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCumulo"      scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Asociazione Nuova Istanza a Procedimento</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
   


   function Verify()
   {
    
  }

  </script>
  </head>
 <body class="corpo" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Conversione Registro Istanza in Procedimento</font>
      </td>
    </tr>
  </table>
 
  <table>
      <td class="L">
        <font class="label">Registro Istanza : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>"
           title="Procedimento">    <%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>
          </a>
  </table>
 
    <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeDettaglioSoggetto.jsp"/>
    <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeDettaglioSentenza.jsp"/>
   <br>
  
  <!-- ******************FORM****************** -->
  
<form name="f"  method="POST" action="<%= IWebConstants.PG_MAIN%>" >
   <input type="hidden"  name="Action" value="siap.siep.nuovaistanza.action.ActConvertiRIinFascicoloSIEP" />    
  	<table>
	  	<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeListaIstanzeNoCheck.jsp">
	  	<jsp:param name="formname" value="f" />
	  	</jsp:include> 
	</table>
	<br>
<%
if(listaIstanze.size()==1 )
{
%>	
	<br> 
<%
}
%>


  <table>
    <tr>
     <td class="lNoBord"><Input class=bottone type="submit" value="Converti"></td>
    </tr>
  </table>
</form>
  
</html>
  