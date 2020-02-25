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
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="ambitoRicerca" scope="request" class="java.lang.String" />

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lAction = "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo" ;
%>
<%@page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
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
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti SIGE per Titolo Esecutivo </font></td>
<%
       FascicoloSigeEstesoModel fascicoloUno = (FascicoloSigeEstesoModel) fascicoli.get(0);
%>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
  <tr>
    <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
  </tr>
<%
	if((ambitoRicerca.equals("U")) )
  {%>
    <tr>
      <td class="lVerdeNB">Ambito di Ricerca: Solo Ufficio</td>
    </tr>
<%}else{%>
  	<tr>
  		<td class="lVerdeNB">Ambito di Ricerca: Intero Distretto</td>
		</tr>
<%}%>
  </table>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeTitoloEsecutivo.jsp"/>
   </tr>
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
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Data di Nascita</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Data Udienza</td>
      <td class="int">Data di Definizione</td>
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
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getCognome()%> </font></td>
        <td class="c"><font class="label"><%=fascicolo.getSoggetto().getNome()%> </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label">
<%				if (fascicolo.getUdienzaProcedimento()!= null &&
							fascicolo.getUdienzaProcedimento().getDataUdienzaSige()!= null)
					{%>
        		<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy"),"-")%>
<%       	}else{%>-<%}%>
        </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSige().getDataDefinizione(),"dd-MM-yyyy"),"-")%></font></td>

<%      // Bottone di dettaglio fascicolo.
				if (fascicolo.getFascicoloSige().getSogIdSoggetto() != null )
				{%>
	  			<td class="c">
	    			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fascicolo.getFascicoloSige().getIdFascicoloSige()%><%=retParam%>">
	      			<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Fascicolo" border="0">
	    			</a>
	  			</td>
      <%}%>
      </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>