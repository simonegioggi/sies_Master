<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.StringUtils"%>
<%@page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<table>
    <tr>
      <td class=l width=4%> </td>
      <td class="int" width=3%>Progr.</td>
      <td class="int" width=5%>Proced.</td>
      <td class="int" width=20%>Soggetto</td>
      <td class="int" width=18%>Posizione Giuridica</td>
      <td class="int" width=25%>Oggetto</td>
      <td class="int" width=15%>Esito</td>
      <td class="int" width=5%>Stampa Verbale</td>
      <td class="int" width=5%>Stampa Decr.Cit.</td>
    </tr>
<%
	Iterator itx = procMagistrato.iterator();
  int i = 0;
  while ( itx.hasNext()) {
    i++;
    ProcedimentixUdienzaModel procedimentoB = (ProcedimentixUdienzaModel)itx.next();
    if ((procedimentoB.getDescrComuneNascitaSog()).equals("-")) {
        procedimentoB.setDescrComuneNascitaSog(procedimentoB.getDescrComuneNascitaEsteroSog());
    }
%>
    <tr>
     <td class=l> </td>
     <td class=l><%=i%></td>

     <td class="l">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=procedimentoB.getIdFasSIGE()%><%=retParam%>" Title="Dettaglio Procedimento SIGE">
            <%=procedimentoB.getChiaveAnnoFasSIGE()%>/<%=procedimentoB.getChiaveProgrFasSIGE()%>
          </a>
     </td>

     <td class=l>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=procedimentoB.getIdSoggetto()%><%=retParam%>" Title="Dettaglio Soggetto">
          <%=procedimentoB.getCognomeSog() + " " + procedimentoB.getNomeSog()%>
        </a>
     </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  //if (procedimentoB.getDescPosizioneGiuridicaSige()!= null){--%>
     <%-- td class=l><%=StringUtils.toStringJSP(procedimentoB.getDescPosizioneGiuridicaSige())%></td--%>
<%--   }else{ --%>
<%   if (procedimentoB.getDescPosizioneGiuridica()!= null){ %>
        <td class=l><%=StringUtils.toStringJSP(procedimentoB.getDescPosizioneGiuridica())%></td>
<%   }else {%>
				<td class=l>-</td>
	 <%}
     //}
%>

<%--
     if( procedimentoB.getIdEvento() != null) {
        if ( procedimentoB.getFlagRinviata() != null && (  procedimentoB.getFlagRinviata().equals ("R") || procedimentoB.getFlagRinviata().equals( "N")))
        {
--%>      
					<%-- td class=l><%=procedimentoB.getDescrOggettoProcedimento()%></td--%> 
				 	<%-- td class=l><%=procedimentoB.getDescStatoUdienza()%></td--%>
<%--
        }
        else
        {
--%>      
					<%-- td class=l><%=procedimentoB.getMotivoProvvedimento()%></td--%>
					<%-- td class=l><%=procedimentoB.getEsitoProvvedimento()%></td--%> 
<%--
        }
     }
     else
     {
        if ( procedimentoB.getFlagRinviata() != null && procedimentoB.getFlagRinviata().equals("P")  )
        {
--%>       
				 <%-- td class=l><%=procedimentoB.getDescrOggettoProcedimento()%></td--%>        
				 <%-- td class=l><%=procedimentoB.getDescStatoUdienza()%></td--%> 
<%--
        }
        else
        {
--%>       
				 <!-- td class=l></td--> 
				 <!-- td class=l></td-->
<%--
        }
     }
--%>
		<td class=l>
			<%=Utils.arrayToString(procedimentoB.getDescrOggettiProcedimento(),"<br>")%>
		</td>
		<td class=l><%=procedimentoB.getEsitoProvvedimento()%></td> 



		
    <td class=l>
       <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>',
																	 '<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaprocedimento.dettaglioruolo.action.ActStampaFlashVerbaleUdienza&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=procedimentoB.getIdFasSIGE()%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=procedimentoB.getIdUdienza()%>');">
       <img src="/images/print.gif" alt="Stampa Verbale Udienza" width="12" height="12" border="0">
       </a>
    </td>
    
    <td class=l>
<%
     if( procedimentoB.getCodEsito() != null && procedimentoB.getCodEsito().equals("0601") ) {
%>
       <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=procedimentoB.getIdEvento()%>');">
       <img src="/images/print.gif" alt="Stampa Decreto Citazione" width="12" height="12" border="0">
       </a>
<%   }else{%>-<%}%>
    </td>
   </tr>
<%
  }
%>
</table>