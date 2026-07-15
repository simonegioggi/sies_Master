<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="f3b.web.IWebConstants"%>
<%@page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@page import="f3b.util.StringUtils"%>
<%@page import="f3b.util.DateUtils"%>
<table>
    <tr>
      <td class=l width=4%> </td>
      <td class="int">Numero SIUS</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Contenuto</td>
      <td class="int">Provvedimento</td>
      <td class="int">Data Emissione</td>
      <td class="int">Motivo Provvedimento</td>
      <td class="int">Esito</td>
    </tr>


<%
  Iterator itxLista = procCorrelati.iterator();
  while ( itxLista.hasNext())
  {
    FascicoloGPModel prCorrelato = (FascicoloGPModel)itxLista.next();
%>
      <tr>
        <td class=l> </td>
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=prCorrelato.getFascicoloSiusModel().getIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" Title="Dettaglio Procedimento SIUS">
            <%=prCorrelato.getFascicoloSiusModel().getChiaveAnno()%>/<%=prCorrelato.getFascicoloSiusModel().getChiaveProgr()%>
          </a>
<%        if (prCorrelato.getFascicoloSiusModel().getNumeroFascicoliUnificati() != null &&
              prCorrelato.getFascicoloSiusModel().getNumeroFascicoliUnificati().intValue() > 0)
          {%>
          <a href="#1" onClick="return effettoTree(<%=jEMA%>)"><img name="image<%=jEMA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Elenco Procedimenti del Tribunale Correlati" ></a>
        <%}%>
        </font></td>
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(prCorrelato.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy"),"-")%></font></td--%>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <td class="c"><font class="label"><%=prCorrelato.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=prCorrelato.getFascicoloSiusModel().getDescrComuneUfficio()%></font></td>
       <!-- Utilizzo setDescrMittente come vettore per Evento.Descr_Tipo_Provvedimento -->
<%     if(prCorrelato.getGeneraleProcedimentoModel().getDescrPosGiuridica()!=null &&
          prCorrelato.getGeneraleProcedimentoModel().getDescrPosGiuridica().compareTo("-")==0 )
       {%>
          <td class="c"><font class="label"><%=prCorrelato.getGeneraleProcedimentoModel().getDescrMittente()%></font></td>
      <%}else{%>
          <td class="c"><font class="label"><%=prCorrelato.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
      <%}%>
        <td class="c"><font class="label"><%=prCorrelato.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
       <!-- Utilizzo setDataDefinizione come vettore per Evento.DataEmissione -->
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(prCorrelato.getGeneraleProcedimentoModel().getDataDefinizione(),"dd/MM/yyyy"),"-")%></font></td>
       <!-- Utilizzo setDescrDefinizione come vettore per Motivo Provvedimento -->
        <td class="c"><font class="label"><%=prCorrelato.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
       <!-- Utilizzo setDescrTipoAtto come vettore per Evento.DescrProvvedimento -->
        <td class="c"><font class="label"><%=prCorrelato.getGeneraleProcedimentoModel().getDescrTipoAtto()%> </font></td>
      </tr>
<%  }
%>
</table>