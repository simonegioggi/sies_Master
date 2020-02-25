<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="java.util.Collection"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="soggetto"       scope="session"   class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="fascicoli"      scope="request"   class="java.util.Vector" />
<jsp:useBean id="TornaQui"       scope="request"   class="java.lang.String"/>

<%
  Collection CodStato =(Collection) request.getAttribute("CodStato");

  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lAction = "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo" ;
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.G.E.] - Procedimenti del G.E.</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Fascicoli del Soggetto </font></td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
     <tr> </tr>

     <tr>
       <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
    </tr>

  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="lVerdeNB">Visualizzazione dei procedimenti del soggetto nell'intero Distretto</td>
        </tr>
  </table>
<%
  Iterator itx = fascicoli.iterator();
  String sUfficio = "Procura";
  String prevUfficio = "";
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio</td>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Estremi Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Stato</td>
      <td class="int">Azioni</td>
    </tr>
<%
    while ( itx.hasNext())
    {
    	FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
        if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("021") )
            sUfficio = "Proc. Trib. "+ fascicolo.getDescrComuneUfficio();
          if (((fascicolo.getChiaveUfficio()).substring(6,9)).equalsIgnoreCase("007") )
            sUfficio = "Proc. Generale "+ fascicolo.getDescrComuneUfficio();
%>
      <tr>
        <td class="c"><font class="label">
            <%=fascicolo.getChiaveAnno()%>/
            <%=fascicolo.getChiaveProgr()%>
        </font></td>
        <td class="c"><font class="label"><%=sUfficio%></font></td>
        <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
	  	<td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento() + " " + fascicolo.getSentenza().getDescrTipoAutoritaEmittente() + " " +fascicolo.getSentenza().getDescrLuogoEmittente() %></font></td>
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita() ,"dd-MM-yyyy"),"-")%></font></td>	  	
  	
		<td class="c"><font class="label"><%=StringUtils.toStringJSP(DecodificheUtils.getFiltrobyCode(CodStato, fascicolo.getCodStatoProcedimento()), "-")%></font></td>	  	
	  	
	  	<td class="c">
	    	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%><%=retParam%>" >
	      		<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Fascicolo" border="0">
	    	</a>
	  	</td>

      </tr>
<%
    }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>