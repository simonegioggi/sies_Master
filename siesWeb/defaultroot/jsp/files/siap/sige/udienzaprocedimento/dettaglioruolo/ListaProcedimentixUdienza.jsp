<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.Utils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="procedimenti" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="data_udi" 						scope="request" class="java.util.Date"/>
<jsp:useBean id="udienza" 						scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="TornaQui" 						scope="request" class="java.lang.String"/>

<jsp:useBean id="ruoloMagistrato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProcuratore" 		scope="request" class="java.lang.String"/>

<jsp:useBean id="tipo" 								scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProc" 						scope="request" class="java.lang.String"/>
<jsp:useBean id="lStatoProcedimento" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteConnesso" 			scope="session" class="siap.sico.utente.model.UtenteModel"/>
<!-- intervento 11.2.1 -->
<jsp:useBean id="listaIdUdienze" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoRito" 				scope="request" class="java.lang.String"/>


<%
	// Presenza del Link per il bottone di ritorno.
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<script language="JavaScript">
    function lookUpload() {
      var node;
      node = document.getElementById('upld');
      node.style.visibility='visible';
    }
</script >

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - ListaProcedimentixUdienza</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
      <td class="LBG">
      <font class="label">Funzione :</font>
      <font class="campo"> Elenco Procedimenti Fissati all'Udienza del <%=DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy")%> </font></td>
     </td>

     <td class="LBG" nowrap>
       <jsp:include page="<%=ICostantiUdienzaProcedimentoSige.PG_COMBOTEMPLATE_RUOLOUDIENZA%>">
          <jsp:param name="CampoIdEntitaPP" value="tipo" />
          <jsp:param name="ValoreIdEntitaPP" value="<%=tipo%>" />
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_DATA_UDIENZA%>" />          
          <jsp:param name="ValoreIdEntita" value="<%=DateUtils.getDateToString(udienza.getDataUdienza(),"yyyyMMdd")%>" /> 
          
          <jsp:param name="CampoIdEntitaProvv" value="tipoProc" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=tipoProc%>" />
          <jsp:param name="ChiaveQuattro" value="lStatoProcedimento" />
          <jsp:param name="ValoreQuattro" value="<%=lStatoProcedimento%>" />
         
          <jsp:param name="ChiaveMagis" value="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" />
          <jsp:param name="ValoreMagis" value="<%=udienza.getCodMagistratoAss()%>" />   
          
          <jsp:param name="ChiavelistaIdUdienze" value="listaIdUdienze" />
          <jsp:param name="ValorelistaIdUdienze" value="<%=listaIdUdienze%>" />
          
       </jsp:include>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </td>
    </tr>
  </table>
	
	<jsp:include page="<%=ICostantiUdienzaProcedimentoSige.PG_SINTESIDETTAGLIOUDIENZA%>"/>
  <br>

 	<table>
  	<div align=center>
    	<tr>
      	<td class="int" width=3%>Progr.</td>
      	<td class="int" width=5%>Proced.</td>
      	<td class="int" width=22%>Soggetto</td>
      	<td class="int" width=20%>Posizione Giuridica</td>
      	<td class="int" width=25%>Oggetto</td>
      	<td class="int" width=15%>Esito</td>
      	<td class="int" width=5%>Stampa Verbale</td>
      	<td class="int" width=5%>Stampa Decr.Cit.</td>
    	</tr>
		</div>

<%
  Iterator<ProcedimentixUdienzaModel> itx = procedimenti.iterator();
  int i = 0;
  while ( itx.hasNext()) {
    i++;
    ProcedimentixUdienzaModel procedimento = (ProcedimentixUdienzaModel)itx.next();
    if ((procedimento.getDescrComuneNascitaSog()).equals("-")) {
        procedimento.setDescrComuneNascitaSog(procedimento.getDescrComuneNascitaEsteroSog());
    }
%>
    <tr>
     <td class=l><%=i%></td>

     <td class="l">
     	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=procedimento.getIdFasSIGE()%><%=retParam%>" Title="Dettaglio Procedimento SIUS">
      	<%=procedimento.getChiaveAnnoFasSIGE()%>/<%=procedimento.getChiaveProgrFasSIGE()%> 
      	<%=" " + procedimento.getDescrStatoFasSIGE()%>
      </a>
     </td>

     <td class=l>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=procedimento.getIdSoggetto()%><%=retParam%>" Title="Dettaglio Soggetto">
          <%=procedimento.getCognomeSog() + " " + procedimento.getNomeSog()%>
        </a>
     </td>

<%   if (procedimento.getDescPosizioneGiuridica()!= null){%>
     	<td class=l>
     		<%=StringUtils.toStringJSP(procedimento.getDescPosizioneGiuridica())%>
     	</td>
<%	 }else{ %>
			<td class=l>-</td>
	<% } %>
<%
     //if(procedimento.getIdEvento() != null) {
%>
				
<%        
//       if ( procedimento.getFlagRinviata() != null && 
//           ( procedimento.getFlagRinviata().equals ("R") || procedimento.getFlagRinviata().equals("N")))
//        {
%>       
	<td class=l><%=Utils.arrayToString(procedimento.getDescrOggettiProcedimento(),"<br>")%></td>
	<td class=l><%=procedimento.getEsitoProvvedimento()%></td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- td class=l><%=procedimento.getDescStatoUdienza()%></td--%>
<%//			} else {    %>       
<%-- td class=l><%=procedimento.getMotivoProvvedimento()%></td--%>
<%
//        }
//     } else {
//        if ( procedimento.getFlagRinviata() != null && 
//             procedimento.getFlagRinviata().equals("P")  ) {
%>       
<%-- td class=l><%=Utils.arrayToString(procedimento.getDescrOggettiProcedimento(),"<br>")%></td--%> 
<%-- td class=l><%=procedimento.getDescStatoUdienza()%></td--%> 
<%
//        } else {
%>       
				<!--  td class=l></td--> 
       	<!--  td class=l></td--> 
<%
//        }
//     }
%>
    <td class=l>
       <%-- Ticket#20230224014 - errata stampa su dettaglio ruolo udienza - Si toglie idMagistrato per evistare che stapi tuti i provvedimenti invece del solo fascicolo  --%>
       <%-- <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaprocedimento.dettaglioruolo.action.ActStampaFlashVerbaleUdienza&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=procedimento.getIdFasSIGE()%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=procedimento.getIdUdienza()%>&<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>=<%=udienza.getCodMagistratoAss()%>');"> --%>
       <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sige.udienzaprocedimento.dettaglioruolo.action.ActStampaFlashVerbaleUdienza&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=procedimento.getIdFasSIGE()%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=procedimento.getIdUdienza()%>');">
       <%-- Ticket#20230224014 - FINE --%>
       <img src="/images/print.gif" alt="Stampa Verbale Udienza" width="12" height="12" border="0">
       </a>
    </td>
        
    <td class=l>
<%
    if( procedimento.getCodEsito() != null && procedimento.getCodEsito().equals("0601") ) {
%>
       <a href="Javascript:stampa2('<%=ISIAPCostantiWeb.PG_STAMPA%>','<%=IWebConstants.ACTION_FIELD%>=siap.sico.evento.action.ActLoadDocumento&IdEvento=<%=procedimento.getIdEvento()%>');">
       	<img src="/images/print.gif" alt="Stampa Decreto Citazione" width="12" height="12" border="0">
       </a>
<%   } else {%>-<%}%>
    </td>
    </tr>
<%
  }
%>
    </table>
  </form>
  <br>
  </body>
</html>