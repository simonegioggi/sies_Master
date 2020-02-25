<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sige.impugnazione.model.ImpugnazioneSigeModel"%>

<jsp:useBean id="numero_Impugnazioni"  scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione"  scope="session" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<%
Vector <ImpugnazioneSigeModel> impugnazioni = (Vector <ImpugnazioneSigeModel>)request.getAttribute("impugnazioni");
String strTitolo="Elenco Ricorsi per il Fascicolo";
String lTitolo="Ricorso";
String lNoImpugnazioni="Non ci sono Ricorsi per il fascicolo.";
if (codTipoImpugnazione.equals("04")) {
  // @emma 09072018 modifica titolo post COLLAUDO 11.2	
  strTitolo = "Elenco Opposizioni per il Provvedimento";
  lTitolo = "Opposizione";
  lNoImpugnazioni="Non ci sono Opposizioni per il fascicolo.";
}

%>

<html>
<head>
  <title>[S.I.E.S.] - Lista Impugnazioni per Fascicolo</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
</head>

<body class="corpo">

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;<font class="campo"><%=strTitolo%></font>
      </td>

      <!-- BOTTONE DI ISCRIZIONE NUOVO RICORSO -->
      <%-- 
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.impugnazione.action.ActLoadInserisciImpugnazioneSige&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento <%=lTitolo%>" width="24" height="24" border="0">
        </a>
      </td>
--%>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
   </table>
<br>

   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
 </table>

  <br>
  <table>
   

    <tr>
<%
	if ( impugnazioni.size() == 0 )
  	{
%>
    <td class="L">
      <font class="label"> <%=lNoImpugnazioni%> </font>
    </td>
<%
  	}
  	else
  	{
%>
    <table width="96%">
      <tr>
        <td class="int" >Provvedimento</td>
        <td class="int" >Anno/Numero</td>
        <td class="int" >Tipo</td>
        <td class="int" >Presentato da</td>
        <td class="int" >Data atto</td>
        <td class="int" >Tenore Decisione</td>
        <td class="int" >Azioni</td>
      </tr>
<%
      for (ImpugnazioneSigeModel lImp  : impugnazioni) {
    	  ProvvedimentoSigeEventoModel provvedimento=lImp.getProvvedimentoSige();
     	  
    	  String isBlob = "SI"; 
          if(provvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() == null)
        	  isBlob="NO";
%>
          <tr align="right">
            <td class="c" ><%=(provvedimento.getProvvedimento().getChiaveAnno()==null?"-":provvedimento.getProvvedimento().getChiaveAnno()) +"/"+(provvedimento.getProvvedimento().getChiaveProgr()==null?"-":provvedimento.getProvvedimento().getChiaveProgr()) %></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getAnnoS7(),"-")%>/<%=StringUtils.toStringJSP(lImp.getProgrS7(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrTipoImpugnazione(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(lImp.getDescrSoggettoImpugnante(),"-")%></td>
            <td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lImp.getDataRicorso(),"dd-MM-yyyy"),"-") %></td>
            <td class="lRosso" ><%=StringUtils.toStringJSP(lImp.getDescrTenoreDecisione(),"-") %></td>
            <td class="c">

              <jsp:include page="<%=ICostantiImpugnazioneSige.PG_BUTTONS_ELENCOIMPUGNAZIONISIGE%>">
                <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
                <jsp:param name="ValoreIdEntita" value="<%=provvedimento.getProvvedimento().getIdProvvedimentoSige()%>" />
                <jsp:param name="CampoIdImpugnazione" value="<%=lImp.getIdImpugnazioneSige()%>" />
                <jsp:param name="isModificabile" value="<%=lImp.isModificabile() %>" />
                <jsp:param name="canSetResult" value="<%=lImp.canSetResult() %>" />
                <jsp:param name="isAnnullabile" value="<%=lImp.isAnnullabile() %>" />
                <jsp:param name="Stampa" value="<%=isBlob%>" />
                <jsp:param name="numeroImpugnazioni" value="<%=numero_Impugnazioni%>" />
              </jsp:include>
            </td>
          </tr>
<%
      }
 %>
  </table>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>