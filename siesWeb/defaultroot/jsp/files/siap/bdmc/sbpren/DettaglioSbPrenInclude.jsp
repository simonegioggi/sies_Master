<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.bdmc.sbpren.model.SbPrenModel"%>

<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="provvedimentoBDMC" scope="session" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>

<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";

%>
<html>


<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  <td class="LBGISI" width=<%=largh%> valign="middle">
  <a class="campoLow" title="Dettaglio Prenotazione" >
         PRENOTAZIONE
       </a>
     </td>
    <td>
<% SbPrenModel sbprenInclude = provvedimentoBDMC.getSbPren();  %>
<table width="100%">
     <tr> 
     <td class="l" >Data Prenotazione </td>
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbprenInclude.getDataPren(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Numero Prenotazione</td>
      <td class="L">
        <font class="campo"><%=sbprenInclude.getIdPren() %></font>&nbsp;
      </td>
      </tr>
      <tr> <td class="l">Anno/Numero Fasc. BDMC</td>
      <td class="L">
        <font class="campo"><%=sbprenInclude.getAnnoFascBdmc() %></font>&nbsp;
        /<font class="campo"><%=StringUtils.toStringJSP(sbprenInclude.getNumeFascBdmc())%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Autorità</td>
      <td class="L">
        <font class="campo"><%=UfficioUtils.getDescTipoUffByCodUfficio(sbprenInclude.getCodiSedeInst())%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(sbprenInclude.getNote())%></font>&nbsp;
      </td>
      </tr>
   </table></td></tr>
</table>
<br>
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  <td class="LBGISI" width=<%=largh%> valign="middle">
  <a class="campoLow" title="Dettaglio Soggetto" >
         SOGGETTO
       </a>
     </td>
    <td>
<table width="50%">
<tr> <td class="L" colspan=4 width="50%">
<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <font class="campo">
          <%=sbprenInclude.getCognSogg()%>&nbsp;<%=sbprenInclude.getNomeSogg()%>
      </font>
<%    if (sbprenInclude.getFlagSess().compareTo("F")==0)
       {%>  <font class="label">&nbsp; nata il </font><%}else{
%>          <font class="label">&nbsp; nato il </font><%}%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbprenInclude.getDataNasc(),"dd-MM-yyyy"),"-")%></font>
       <font class="label">&nbsp; in  </font>
<%   if (sbprenInclude.getLuogNasc().compareTo("-")==0)
       {%> <font class="campo"><%=sbprenInclude.getCodiStat() %> </font>
<%     }else{%><font class="campo"><%=sbprenInclude.getLuogNasc() %></font>
<%     }%>
      </td></tr>
    </table></td></tr>
</table>
<BR>
</html>