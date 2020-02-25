<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<%@page import="siap.sius.penapecuniaria.action.ICostantiSiusPenaPecuniaria"%>
<html>
	<head>
	    <title> [S.I.E.S.] - Dettaglio Trasmissione Conversione - </title>
	    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
			<script language="JavaScript">
			/* versioni della funzione con + 2 parametri */
			/* a_destnname, a_destvalue  : nome e valore del Request parameter che definisce l'azione da eseguire dopo la cancellazione */
			function conferma2Param(a_action, a_entityname, a_entityvalue, a_entityname2, a_entityvalue2, a_destnname, a_destvalue )
			{
				str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue + "&" + a_entityname2 + "=" +a_entityvalue2 +  "&" + a_destnname + "=" +a_destvalue;
    		if (window.confirm('Confermi la cancellazione ?'))
    		{
					window.location.href=str;
    		}
			}
			</script>
			
	</head>
	<BODY class="corpo">
		<FORM name="comandi" >
	    	<table>
	      		<tr>
	      			<td class="LBG"><a href="Javascript:window.print();">
	      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
	      			</td>
	        		<td class="LBG">
			        	<font class="label">Funzione :</font>&nbsp;
			        	<font class="campo">Dettaglio Richiesta Conversione Pena Pecuniaria</font>
			        </td>
	        		<%--td class="LBG">
	      				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
	      					<jsp:param name="CampoIdEntita" value="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" />
             			<jsp:param name="ValoreIdEntita" value="<%=richiestaconversione.getIdRichiestaConversione()%>" />
           			</jsp:include>
							</td--%>
<% 		
							if ( richiestaconversione.getCodUfficioInserimento().compareTo(UtenteConnesso.getUfficioUtente().getCodUfficio()) == 0)
  						{%>
								<!-- BOTTONE DI MODIFICA -->
								<td class="LBG">
									<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.penapecuniaria.action.ActLoadModificaRichiestaConversionePP&<%=ICostantiSiusPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>=<%=richiestaconversione.getIdRichiestaConversione()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=richiestaconversione.getFasSiuIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" >
										<img  align="middle" src="/images/modifica24.gif" alt="Modifica Richiesta Conversione Pena Pecuniaria" width="24" height="24" border="0">
									</a>
								</td>

								<!--- BOTTONE DI CANCELLAZIONE -->
			    			<td class="LBG">
			      			<a href="Javascript:conferma2Param('siap.sius.penapecuniaria.action.ActCancellaRichiestaConversionePP','<%=ICostantiSiusPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>','<%=richiestaconversione.getIdRichiestaConversione()%>', '<%=ICostantiSiusPenaPecuniaria.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>', '<%=richiestaconversione.getFasSiuIdFascicoloSius()%>', 'TornaQui','<%=TornaQui%>');">
			        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella Richiesta Conversione Pena Pecuniaria" width="24" height="24" border="0">
			      			</a>
			    			</td>
							<%}%>

      				<!-- BOTTONE DI RITORNO -->
        				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
						</tr>
	    	</table>
		</FORM>

      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  		<br>
		<table>
	  		<tr>
	    		<td class="l">Anno/Numero Partita</td>
	    		<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita()) %> / 
	    																			<%=StringUtils.toStringJSP(richiestaconversione.getNumPartita()) %></font>&nbsp;</td>
	  		</tr>
	  		<tr>
			    <td class="l">Numero Ex Campione Penale</td>
			    <td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumExCampione()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Prot. Circosrizione Doganale</td>
				<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getProtCircosrizioneDoganale()) %></font>&nbsp;</td>
			</tr>
			<tr>
    			<td class="l">Autorità</td>
    			<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrTipoAutoritaEmittente()) %>&nbsp;<%=StringUtils.toStringJSP(richiestaconversione.getDescrLuogoEmittente()) %></font></td>
  			</tr>
			<tr>
		    	<td class="l">Data Ricezione Atto</td>
		    	<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
		    	<td class="l">Data Iscrizione Atto</td>
		    	<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
		    	<td class="l">Data Richiesta Impossibilità Esazione</td>
		    	<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
			    <td class="l">Multa</td>
			    <td class="l">Importo <font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta()) %></font>&nbsp;
			    <td class="l">Data Prescrizione&nbsp;&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"dd-MM-yyyy"))%> &nbsp;
					<% if(richiestaconversione.getFlagImprescrittibileMulta().equals("S")) 
						 	 {%>&nbsp;<font class="campo">Imprescrittibile<font class="campo">&nbsp;<%}%>
					</td>
			</tr>
			<tr>
			    <td class="l">Ammenda</td>
			    <td class="l">Importo <font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda()) %></font>&nbsp;
			    <td class="l">Data Prescrizione &nbsp;&nbsp;<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"dd-MM-yyyy"))%> </font>&nbsp;
					<% if (richiestaconversione.getFlagImprescrittibileAmmenda().equals("S"))
								{%>&nbsp;<font class="campo">Imprescrittibile<font class="campo">&nbsp;</td>
	    		<%}%>
			</tr>

			<tr>
			    <td class="l">Note</td>
			    <td class="l" colspan = "3"><font class="campo">
<%          if ((richiestaconversione.getNote()!= null) && (richiestaconversione.getNote().length()>0))
            {%>
              <%=richiestaconversione.getNote()%>
<%          }else{%>
							-&nbsp;<%}%>
			    </font>&nbsp;

			</tr>
			
   	</table>
	</body>
</html>