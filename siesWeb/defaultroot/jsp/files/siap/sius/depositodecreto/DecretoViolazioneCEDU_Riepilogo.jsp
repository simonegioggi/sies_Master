<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="depositoDecretoMotivazioni"  scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<%
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.debug(" Decreto Violazione CEDU - Dati Generali del decreto e del Provvedimento ");
%>

<%
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficio = "Ufficio di Sorveglianza";
	}
%>	
<!-- 	DecretoViolazioneCEDU_Riepilogo	 -->

    <table>
      	<tr>
    		<td class="l"> Tipo di Decreto</td>
    		<td class="l"> <font class="campo"><%=DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoDecreto(), depositoDecretoMotivazioni.getDepositoDecreto().getCodTipoDecreto())%></font></td>
  		</tr>
    	<tr>
    		<td class="l"> Data Emissione</td>
    		<td class="l"><font class="campo"> <%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataEmissione(),"dd/MM/yyyy")%></font></td>
  		</tr>
<% 		if (depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito() != null)
		{ %>
			  <tr>
			    <td class="l"> Anno / Numero del Decreto</td>
			    <td class="l">
			       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecretoMotivazioni.getDepositoDecreto().getIdEventoGenerato()%>&TornaQui=<%=TornaQui%>">
			           <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getAnnoS72(),"" )%>
			           /
			           <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumS72(),"" )%>
			        </a>
			    </td>
			  </tr>
			  <tr>
			    <td class="l"> Data Deposito in Cancelleria</td>
			    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(depositoDecretoMotivazioni.getDepositoDecreto().getDataDeposito(),"dd/MM/yyyy")%></font></td>
			  </tr>
<% 		} %> 	
  		<tr>
    	<td class="l"> Stato del provvedimento</td>

<% 		if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null && depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
		{ %>
    		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 		} 
		else if (depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato() != null && depositoDecretoMotivazioni.getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
   		{
%>
    		<td class="l"><font class="campo">Validato</font></td>
<% 		} 
		else
   		{
%>
    		<td class="l"><font class="campo">Da Validare </font></td>
<% 		} %>
		</tr>
		
<% 		if(depositoDecretoMotivazioni.getDepositoDecreto().getNumGiorniRiduzionePena() != null && depositoDecretoMotivazioni.getDepositoDecreto().getNumGiorniRiduzionePena().intValue() > 0)
   		{	%>
			<tr>
      			<td class="L"><font class="label"> Totale giorni Riduzione pena </font></td>
      			<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getNumGiorniRiduzionePena(), "-")%></font></td>
    		</tr>
<% 		}
   		
   		if(depositoDecretoMotivazioni.getDepositoDecreto().getSommaRisarcimentoDanni() != null && depositoDecretoMotivazioni.getDepositoDecreto().getSommaRisarcimentoDanni().intValue() > 0 )
   		{	%>
			<tr>
      			<td class="L"><font class="label"> Somma Liquidata a titolo Risarcimento  </font></td>
      			<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getSommaRisarcimentoDanni(), "-")%>&nbsp;&euro;</font></td>
    		</tr>
<% 		}
   		if( depositoDecretoMotivazioni.getDepositoDecreto().getDescrUfficioCompetente() != null && !depositoDecretoMotivazioni.getDepositoDecreto().getDescrUfficioCompetente().equals("-") &&
   			depositoDecretoMotivazioni.getDepositoDecreto().getCodUfficioCompetente() != null && !depositoDecretoMotivazioni.getDepositoDecreto().getCodUfficioCompetente().equals("-")  ) 
   		{ %>
	   	    <tr>
	   	      <td class="L"><font class="label"> <%=labelUfficio%> Competente </font></td>
	   	      <td class="L"><font class="campo"> <%=StringUtils.toStringJSP(depositoDecretoMotivazioni.getDepositoDecreto().getDescrUfficioCompetente() , "-")%></font></td>
	   	    </tr>
<% 		}%>
   
   </table>
	<br>   