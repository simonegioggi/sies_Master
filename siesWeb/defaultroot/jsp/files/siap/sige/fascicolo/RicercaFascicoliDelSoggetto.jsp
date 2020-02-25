<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>

<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String" />

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lAction = "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo" ;
%>
<%@page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
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
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Fascicoli per Soggetto </font></td>
<%
       FascicoloSigeEstesoModel fascicoloUno = (FascicoloSigeEstesoModel) fascicoli.get(0);
%>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicoloUno.getSoggetto().getIdSoggetto()%>" />
        </jsp:include>
      </td>

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
<%
    if(!(codDistretto.equals("")) )
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(codDistretto.length()==1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizzazione dei procedimenti dell'intero Distretto</td>
        </tr>
<%    }
    }
%>
  </table>
<%
  Iterator itx = fascicoli.iterator();
  String sUfficio = "";
  String prevUfficio = "";
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIGE</td>
      <td class="int">Ufficio</td>
      <td class="int">Tipo Atto</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Azioni</td>
    </tr>
<%
    while ( itx.hasNext())
    {
      FascicoloSigeEstesoModel fascicolo = (FascicoloSigeEstesoModel)itx.next();
%>
      <tr>
        <td class="c"><font class="label">
            <%=fascicolo.getFascicoloSige().getChiaveAnno()%>/
            <%=fascicolo.getFascicoloSige().getChiaveProgr()%>
        </font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSige().getDescrUfficio()%> </font></td>
        <td class="c"><font class="label"><%=fascicolo.getRichiestaSige().getDescrTipoAtto()%> </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>

<%      // Bottone di dettaglio fascicolo.
	if (fascicolo.getFascicoloSige().getSogIdSoggetto() != null )
	{
%>
	  <td class="c">
	    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fascicolo.getFascicoloSige().getIdFascicoloSige()%><%=retParam%>">
	      <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Fascicolo" border="0">
	    </a>
	  </td>
      <%} %>

      </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>