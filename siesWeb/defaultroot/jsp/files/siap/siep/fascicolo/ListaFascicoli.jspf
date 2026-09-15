<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>


<table align="center">
    <tr>
      <td class="int">Anno/Numero</td>
      <td class="int">Autorità</td>
      <td class="int">Nome</td>
      <td class="int">Cognome</td>
      <td class="int">Data Nascita</td>
      <td class="int">Luogo Nascita</td>
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
            <%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrLuogoEmittente())%>
         </td>
         <td class="c">
            <%=StringUtils.toStringJSP(lFasMod.getSoggetto().getCognome()) %>
         </td>
          <td class="c">
            <%=StringUtils.toStringJSP(lFasMod.getSoggetto().getNome())%>
         </td>              
       <td class=C>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getSoggetto().getDataNascita(),"dd-MM-yyyy"))%>
        </td>  
        <td class=C>        
         <%=StringUtils.toStringJSP(lFasMod.getSoggetto().getDescrComuneNascita())%>
        </td>
      
      <td class=C>
        <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lFasMod.getIdFascicoloSiep()%>" />
           <jsp:param name="FlagValidato" value="<%=lFasMod.getFlagValidato()%>" />
        </jsp:include>
      </td>
      <td class=C>
          <input type="radio" title="Selezione Procedimento SIEP" name="radioins"  onClick ="Javascript:fascicolo('<%=lFasMod.getIdFascicoloSiep()%>')">
      </td>
    </tr>
<%
  }
%>
</table>