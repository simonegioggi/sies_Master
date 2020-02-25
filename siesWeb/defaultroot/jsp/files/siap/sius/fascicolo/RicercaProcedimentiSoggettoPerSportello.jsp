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

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import=" siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import=" siap.sico.decodifiche.controller.DecodificheManager"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggetto" scope="session" class="siap.sico.soggetto.model.SoggettoModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="ufOTribunale" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="descrContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lAction = "siap.sius.provvedimento.action.ActDettaglioProvvedimentoByIdEvento" ;
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Sorveglianza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti per Soggetto - Attività di Sportello</font></td>
<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>
      <!-- BOTTONE DI STAMPA ELENCO PROCEDIMENTI DEL SOGGETTO -->
      <td class="LBG">
        <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActStampaProcedimentiDelSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=SIUS_ST_004<%=retParam%>');">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa Procedimenti del soggetto" width="24" height="24" border="0">
        </a>
      </td>


      <!-- TOOLBAR HEADER -->
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=fascicoloUno.getFascicoloSiusModel().getSogIdSoggetto()%>" />
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
    <tr>
      <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
    </tr>
<%
    if(ufOTribunale.equals(""))
    {
      if (tipoUfficio.equals("UDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Solo i procedimenti   222 dell' Ufficio</td>
       </tr>
<%
      }
      else if (tipoUfficio.equals("TDS"))
      {
%>
       <tr>
          <td class="lVerdeNB">Solo i procedimenti del Tribunale</td>
       </tr>
<%      }
    }else{
      if (tipoUfficio.equals("UDS"))
      {%>
       <tr>
          <td class="lVerdeNB">Anche i procedimenti del Tribunale</td>
       </tr>
		<%}
			else if (tipoUfficio.equals("TDS"))
			{%>
       <tr>
          <td class="lVerdeNB">Anche i procedimenti dell' Ufficio (Sede)</td>
       </tr>
		<%}
    }


    if(!codContenuto.equals("-"))
    {
%>
      <tr>
        <td class="lVerdeNB">Solo i procedimenti di : <%=descrContenuto%></td>
      </tr>
<%    }
    if(!(dataDalInCancelleria.equals(""))||!(dataAlInCancelleria.equals("")))
    {
%>
      <tr>
        <td class="lVerdeNB">Procedimenti con Data arrivo in cancelleria :&nbsp;&nbsp;
<%
        if(!(dataDalInCancelleria.equals("")))
        {
%>
          Dal <%=dataDalInCancelleria%>&nbsp;&nbsp;
<%        }
        if(!(dataAlInCancelleria.equals("")))
        {
%>
          &nbsp;Al&nbsp;&nbsp;<%=dataAlInCancelleria%>
          </td>
<%        }
     %></tr><%
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
      <td class="int">Numero SIUS</td>
      <td class="int">Contenuto</td>
      <td class="int">Data Udienza</td>
      <td class="int">Provvedimento<br /> <font class="crosso">Stato Procedimento</font></td>
      <td class="int">Data Deposito</td>
      <td class="int">Oggetto Provvedimento</td>
      <td class="int">Esito Provvedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
      sUfficio = fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()+fascicolo.getFascicoloSiusModel().getDescrComuneUfficio();
      if (!(sUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=sUfficio;
%>
        <tr>
          <td class="lVerdeNB" colspan="7">&nbsp;</td>
        </tr>
        <tr>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <td class="lVerdeNB" colspan="7">Elenco Procedimenti di : <%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%></td>
        </tr>
<%
      }
%>
      <tr>
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>" Title="<%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%> - Dettaglio Procedimento" >
            <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>
            /
            <%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
          </a>

        </font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"),"-")%></font></td>
        <%if((fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo().compareToIgnoreCase("01") == 0)
           ||(fascicolo.getGeneraleProcedimentoModel().getDataAggiornamento() == null ) ) { %>
        <td class="c"><font class="crosso"><%= DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoFascicolo(),     fascicolo.getFascicoloSiusModel().getDescrStatoFascicolo())%></font></td>
<% } else { %>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%> <br /> <%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataAggiornamento(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrTipoAtto()%></font></td>

<%      // Bottone di dettaglio provvedimento.
	if (fascicolo.getFascicoloSiusModel().getSogIdSoggetto() != null )
	{
%>
	  <td class="c">
	    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=fascicolo.getFascicoloSiusModel().getSogIdSoggetto()%> <%=retParam%>" >
	      <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
	    </a>
	  </td>
      <%} }%>

      </tr>
<%
  }
%>
    </table>
  </FORM>
  <br>

  </body>
</html>