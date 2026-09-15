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
      <td class="int">Azioni</td>                 
    </tr>

<%
  Iterator itxlista = lFascicoli.iterator();
  while (itxlista.hasNext())
  {
	  FascicoloSiepModel lFasMod = (FascicoloSiepModel)itxlista.next();
%>
    <tr>

        <td class="C">
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
      
      	<td class="C">
      	     	<a href="/jsp/Main.jsp?Action=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFasMod.getIdFascicoloSiep()%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
              </a>
      	</td>
      
    </tr>
<%
  }
%>
</table>