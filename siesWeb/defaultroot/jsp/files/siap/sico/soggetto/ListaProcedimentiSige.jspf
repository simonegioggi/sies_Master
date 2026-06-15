<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeSentenzaModel" %>

<table align="center">
    <tr>
      <td class="int">Numero SIGE</td>
      <td class="int">Ufficio</td>
      <td class="int">Tipo Atto</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Titolo Esecutivo</td>
      <td class="int">Azioni</td>
    </tr>

<%
  Iterator itxlista = lFascicoli.iterator();
  while (itxlista.hasNext())
  {
	  FascicoloSigeSentenzaModel lFasMod = (FascicoloSigeSentenzaModel)itxlista.next();
%>
    <tr>

        <td class=C>
          <%=StringUtils.toStringJSP(lFasMod.getFascicoloEstesoSige().getFascicoloSige().getChiaveAnno()) + "/" + StringUtils.toStringJSP(lFasMod.getFascicoloEstesoSige().getFascicoloSige().getChiaveProgr())%>
        </td>
         <td class="c">
            <%=StringUtils.toStringJSP(lFasMod.getFascicoloEstesoSige().getFascicoloSige().getDescrUfficio())%>
         </td>
         <td class="c">
       		<%
       			if(lFasMod.getFascicoloEstesoSige().getRichiestaSige() != null && !lFasMod.getFascicoloEstesoSige().getRichiestaSige().equals("")){
       		%>     
            	<%=StringUtils.toStringJSP(lFasMod.getFascicoloEstesoSige().getRichiestaSige().getDescrTipoAtto(), "-")%>
         	<%
       			} else {
         	%>
         			-
         	<%
       			}
         	%>
         </td>
         <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getFascicoloEstesoSige().getFascicoloSige().getDataIscrizione(),"dd-MM-yyyy"))%>
         </td>
         <td class="c">
            <%
            	if(lFasMod.getSentenza() != null && !lFasMod.getSentenza().equals("")){
            %>
            		<%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lFasMod.getSentenza().getDescrLuogoEmittente())%>
         	<%
            	} else {
         	%>
         			&nbsp;
         	<%
            	}
         	%>
         </td>
          <td class="c">
            <%
            	if(lFasMod.getSentenza() != null && !lFasMod.getSentenza().equals("")){
            %>
            		<%=StringUtils.toStringJSP(lFasMod.getSentenza().getAnnoSentenza()) + "/" + StringUtils.toStringJSP(lFasMod.getSentenza().getNumeroSentenza()) %>
         	<%
            	} else {
         	%>
         			&nbsp;
         	<%
            	}
         	%>
         </td>              
      
      <td class=C>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_BUTTONS_DETTAGLIO_FASCICOLO_SIGE%>">
           <jsp:param name="CampoIdEntita"  value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=lFasMod.getFascicoloEstesoSige().getFascicoloSige().getIdFascicoloSige()%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
</table>