<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="f3b.security.model.ProfileModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel" %>

<jsp:useBean id="fascicoliUfficio" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoliAltriUffici" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="soggettonuovo" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="soggettovecchio" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />

<jsp:useBean id="chiaveUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="contaUffici" scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel"/>


   <tr>
      <td class="int">Numero SIGE</td>
      <td class="int">Ufficio</td>
       <td class="int">Data Iscrizione</td>
       <td class="int">Stato</td>
       <td class="int">Modifica</td>
   </tr>

<%
    Iterator itxaltri = fascicoliAltriUffici.iterator();
    int y = 0;
    while ( itxaltri.hasNext())
    {
       	FascicoloSigeModel fascicolo = (FascicoloSigeModel) itxaltri.next();
       	fascicolo.decodifica();
 %>
    <tr>
   <tr>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnno() ,"")%>/<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr() ,"")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getDescrUfficio()%> </font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getDescrStatoFascicolo()%></font></td>
     <td class="c"> <input type="checkbox" name="<%="fascicolo"%>" value="" disabled  />
   </td>
    </tr>
 <%
  y++;
  }
%>