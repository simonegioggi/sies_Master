<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

<table align="center">
    <tr>
      <td class="int">Anno/Numero</td>
      <td class="int">Uff.Esecuzione</td>
      <td class="int">Data Emissione</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Dett.</td>
      <td class="int">Sel</td>                  
    </tr>

<%
  Iterator itxlista = lFascicoli.iterator();
  while (itxlista.hasNext())
  {
	  FascicoloSiepModel lFasMod = (FascicoloSiepModel)itxlista.next();
%>
    <tr>

        <td class=C>
          <%=StringUtils.toStringJSP(lFasMod.getChiaveAnno()).length()>1
           ? StringUtils.toStringJSP(lFasMod.getChiaveAnno())+"/"+StringUtils.toStringJSP(lFasMod.getChiaveProgr()) : "-"%>
        </td>
         <td class="c">
            <%=StringUtils.toStringJSP(lFasMod.getCodTipoUfficio())%> di <%=StringUtils.toStringJSP(lFasMod.getDescrComuneUfficio())%>
         </td>
         <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"))%>
         </td>
         <td class="c">
            <%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrLuogoEmittente())%>
         </td>
          <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
         </td>              
      
      <td class=C>
        <jsp:include page="<%=ICostantiNuovaIstanza.PG_BUTTONS_FASCICOLI_NUOVAISTANZA%>">
           <jsp:param name="CampoIdEntita"  value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lFasMod.getIdFascicoloSiep()%>" />
           <jsp:param name="FlagValidato" value="<%=lFasMod.getFlagValidato()%>" />
        </jsp:include>
      </td>
      <td class=C>
<%		if (lFasMod.getChiaveProgr().intValue()< 90000 ||
				  lFasMod.getChiaveProgr().intValue()> 99999 ) {%>
          <input type="radio" title="Selezione Procedimento SIEP" name="radioins"  onClick ="Javascript:fascicolo('<%=lFasMod.getIdFascicoloSiep()%>')">
      <%}else{%>&nbsp;<%}%>
      </td>
    </tr>
<%
  }
%>
</table>