<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="residenze" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />


<% 
boolean modificabile = true;

if (isModificabile.length() > 0)
{
  if (isModificabile.equalsIgnoreCase("SI"))
  {
	  modificabile = true;
  }
  else
  {
	  modificabile = false;

  }
}
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Domicili per Procedimento SIGE </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Domicili per Procedimento SIGE </font>
      </td>
<% if (modificabile) { %>
       <!-- BOTTONE DI INSERIMENTO RESIDENZA -->
       <td class="LBG">
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadInserisciDomicilioFascicoloSige&IdFascicoloSige=<%=FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>" >
           <img  align="middle" src="/images/new24.gif" alt="Assegna nuovo Domicilio" width="24" height="24" border="0">
         </a>
       </td>
<%} %>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  <br>
<%
  BigDecimal lIdSoggetto = (FascicoloSigeEsteso.getSoggetto().getIdSoggetto() );
%>
  <table>
<%
  if ( residenze.size()==0 )
  {%>
    <tr>
      <td class="l">Nessun Domicilio assegnato</td>
	</tr>
<%}else{%>
   	<tr>
		<td class="int">Indirizzo</td>
		<td class="int">CAP</td>
		<td class="int">Luogo/Comune Estero</td>
		<td class="int">Stato</td>
		<td class="int">Data inizio val.</td>
		<td class="int">Data fine val.</td>
		<td class="int" width=5%>Azioni</td>
   	</tr>
<%
  Iterator itx = residenze.iterator();
  while ( itx.hasNext()) {
    ResidenzaAssociataModel residenza = (ResidenzaAssociataModel)itx.next();
%>

    <tr>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getIndirizzo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getCap(),"-")%></td>
<%
      if (residenza.getResidenza().getCodComune().compareTo("-")==0)
      {
%>
        <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescComuneEstero(),"-")%></td>
      <%}else{ %>
        <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescrComune(),"-")%>&nbsp;(<%=StringUtils.toStringJSP(residenza.getResidenza().getCodProvincia(),"-")%>)</td>
      <%}%>
      <td class="l"><%=StringUtils.toStringJSP(residenza.getResidenza().getDescrStato(),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSige().getDataInizioValidita(),"dd-MM-yyyy"),"-")%></td>
      <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSige().getDataFineValidita(),"dd-MM-yyyy"),"-")%></td>

    <%if (StringUtils.toStringJSP(DateUtils.getDateToString(residenza.getResidenzaFascicoloSige().getDataFineValidita(),"dd-MM-yyyy"),"-").equals("-"))
    {%>
      <td class=c>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" />
           <jsp:param name="ValoreIdEntita" value="<%=residenza.getResidenza().getIdResidenza()%>" />
        </jsp:include>
      </td>
    </tr>
    <%}else{%>
      <td class=c>-</td>
    </tr>
    <%}
    }
  }%>
    </table>
  </form>
  </body>
</html>