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

<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSoggAttModel"%>
<%@ page import=" siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import=" siap.sico.decodifiche.controller.DecodificheManager"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="descrIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDal" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAl" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lAction = "siap.siepe.fascicolo.action.ActLoadDettaglioFascicoloSiepe" ;
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti SIEPE</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti per Soggetto </font></td>
<%
       FascicoloSoggAttModel fascicoloUno = (FascicoloSoggAttModel) fascicoli.get(0);
%>
      <!-- BOTTONE DI STAMPA ELENCO PROCEDIMENTI DEL SOGGETTO -->
      <%--td class="LBG">
        <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.siepe.fascicolo.action.ActStampaProcSiepeDelSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=SIEPE_ST_???<%=retParam%>');">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa Procedimenti del soggetto" width="24" height="24" border="0">
        </a>
      </td--%>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicoloUno.getSoggettoModel().getIdSoggetto()%>" />
        </jsp:include>
      </td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
     <tr> </tr>
     <tr> </tr>

     <tr>
       <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
    </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
<%
    if(!(lIncludeArchiviati.equals("") && codIncarico.equals("-") &&
       (dataDal.equals("")) && (dataAl.equals("")) ))
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
			if(!lIncludeArchiviati.equals(""))
      {
%>
        <tr>
          <td class="lVerdeNB">Anche i procedimenti definiti</td>
        </tr>
<%    }
      if(!codIncarico.equals("-"))
      {
%>
        <tr>
          <td class="lVerdeNB">Solo i procedimenti di : <%=descrIncarico%></td>
        </tr>
<%    }
      if(!(dataDal.equals(""))||!(dataAl.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Invio :&nbsp;&nbsp;
<%
          if(!(dataDal.equals("")))
          {%>
            Dal <%=dataDal%>&nbsp;&nbsp;
<%        }
          if(!(dataAl.equals("")))
          {%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAl%>
            </td>
<%        }
       %></tr><%
      }
    }
%>
  </table>
  <br>
<%
  Iterator itx = fascicoli.iterator();
  String sUfficio = "";
  String prevUfficio = "";
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIEPE</td>
      <td class="int">Tipo Incarico</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Numero Fascicolo</td>
      <td class="int">Incaricato</td>
      <td class="int">Esito Incarico</td>
      <td class="int">Azioni</td>
    </tr>
<%
    while ( itx.hasNext())
    {
      FascicoloSoggAttModel fascicolo = (FascicoloSoggAttModel)itx.next();
      sUfficio = fascicolo.getFascicoloSiepeRicercaModel().getDescrTipoUfficio()+fascicolo.getFascicoloSiepeRicercaModel().getDescrComuneUfficio();
      if (!(sUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=sUfficio;
%>
        <tr>
          <td class="lVerdeNB" colspan="7">&nbsp;</td>
        </tr>
        <tr>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <td class="lVerdeNB" colspan="7">Elenco Procedimenti di : <%=fascicolo.getFascicoloSiepeRicercaModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiepeRicercaModel().getDescrComuneUfficio()%></td>
        </tr>
<%
      }
%>
      <tr>
        <td class="c"><font class="label">
            <%=fascicolo.getFascicoloSiepeRicercaModel().getChiaveAnno()%>
            /
            <%=fascicolo.getFascicoloSiepeRicercaModel().getChiaveProgr()%>
        </font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiepeRicercaModel().getDescrIncarico()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiepeRicercaModel().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label">
          <%=StringUtils.toStringJSP(fascicolo.getFascicoloSiepeRicercaModel().getAnnoUepe(),"-")%>/
          <%=StringUtils.toStringJSP(fascicolo.getFascicoloSiepeRicercaModel().getNumUepe(),"-")%>/
          <%=StringUtils.toStringJSP(fascicolo.getFascicoloSiepeRicercaModel().getProgrUepe(),"-")%>
        </font></td>
        <td class="c"><font class="label">-</font></td>
        <td class="c"><font class="label">-</font></td>

<%      // Bottone di dettaglio provvedimento.
	      if (fascicolo.getSoggettoModel().getIdSoggetto() != null )
	      {
%>
	      <td class="c">
	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>=<%=fascicolo.getFascicoloSiepeRicercaModel().getIdFascicoloSiepe()%><%=retParam%> " >
	          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
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