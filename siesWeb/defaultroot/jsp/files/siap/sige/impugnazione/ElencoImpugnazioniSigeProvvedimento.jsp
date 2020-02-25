<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>

<jsp:useBean id="provvedimento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>
<jsp:useBean id="impugnazioni" scope="request" class="java.util.Vector"/> <%-- 12/10/2007  --%>
<jsp:useBean id="fascicoloEstesoSige" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="flag_valida"  scope="request" class="java.lang.String"/>
<%--jsp:useBean id="flag_Impugnato"  scope="request" class="java.lang.String"/--%>
<jsp:useBean id="numero_Impugnazioni"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione"  scope="session" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<%@page import="java.math.BigDecimal"%>
<%@page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>
<%
String strTitolo="Elenco Ricorsi per il Provvedimento";
String lTitolo="Ricorso";
if (codTipoImpugnazione.equals("04")) {
  strTitolo = "Elenco Opposizioni per il Provvedimento";
  lTitolo = "Opposizione";
}

%>

<html>
<head>
  <title>[S.I.E.S.] - Lista Impugnazioni per Provvedimento</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;<font class="campo"><%=strTitolo%></font>
      </td>
<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      <!-- BOTTONE DI ISCRIZIONE NUOVO RICORSO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadInserisciImpugnazioneSige&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento <%=lTitolo%>" width="24" height="24" border="0">
        </a>
      </td>

      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
   </table>
<br>
<%
  if (flag_valida.equals(""))
  	flag_valida="SI";

  if (fascicoloEstesoSige != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>
<%}%>
  <br>
  <table>
    <tr>
	    <td class="L">
    	  <font class="label"> Avverso il provvedimento: </font>
    	</td>
	</tr>
    <tr>
<%	
	Collection lCol = (DecodificheManager.getInstance()).getTipoProvvSorveglianza();
%>
    <tr>
	    <td class="L">
    	  <font class="label"> <%=DecodificheUtils.getDescbyCode(lCol,provvedimento.getProvvedimento().getCodTipoProvvedimento())%>&nbsp;di&nbsp;
    	  	<%=provvedimento.getProvvedimento().getDescrTipoProvvedimento()%> del <%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"),"-")%>
    	  </font>
    	</td>
	</tr>

    <tr>
<%
	if ( impugnazioni.size() == 0 )
  	{
%>
    <td class="L">
      <font class="label"> Non ci sono ricorsi per il provvedimento. </font>
    </td>
<%
  	}
  	else
  	{
%>
    <table width="96%">
      <tr>
        <td class="int" >Anno/Numero</td>
        <td class="int" >Tipo</td>
        <td class="int" >Presentato da</td>
        <td class="int" >Data atto</td>
        <td class="int" >Stato</td>
        <td class="int" >Azioni</td>
      </tr>
<%
      Iterator itx = impugnazioni.iterator();
      while ( itx.hasNext())
      {
          ImpugnazioneSigeModel lImp = (ImpugnazioneSigeModel)itx.next();
%>
          <tr align="right">
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getAnnoS7(),"-")%>/<%=StringUtils.toStringJSP(lImp.getProgrS7(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrTipoImpugnazione(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrSoggettoImpugnante(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lImp.getDataRicorso(),"dd-MM-yyyy"),"-") %></td>
            <td class="lRosso" ><%=StringUtils.toStringJSP(lImp.getDescrTenoreDecisione(),"-") %></td>
            <td class="c">
<%
              String isBlob = "SI";
              if(provvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null)
              {
                isBlob="NO";
              }
%>
              <jsp:include page="<%=ICostantiImpugnazioneSige.PG_BUTTONS_ELENCOIMPUGNAZIONISIGE%>">
                <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
                <jsp:param name="ValoreIdEntita" value="<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" />
                <jsp:param name="CampoIdImpugnazione" value="<%=lImp.getIdImpugnazioneSige()%>" />
                <jsp:param name="CodTipoProvvedimento" value="<%=provvedimento.getProvvedimento().getCodTipoProvvedimento()%>" />
                <jsp:param name="CodMotivo" value="<%=provvedimento.getEventoNotifica().getEvento().getCodMotivo()%>" />
                <jsp:param name="FlagAnnullato" value="<%=lImp.getFlagAnnullamento()%>" />
                <jsp:param name="Stampa" value="<%=isBlob%>" />
                <jsp:param name="numeroImpugnazioni" value="<%=numero_Impugnazioni%>" />
              </jsp:include>
            </td>
          </tr>
<%
      } // endwhile

  %>
  </table>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>