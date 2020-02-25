<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.remissionedebito.action.ICostantiRemissioneDebito"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="richiestaremissione" scope="request" class="siap.sius.remissionedebito.model.RichiestaRemissioneModel"/>
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<%@page import="siap.sius.remissionedebito.action.ICostantiSiusRemissioneDebito"%>
<html>
	<head>
	    <title> [S.I.E.S.] - Dettaglio Trasmissione Remissione - </title>
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
			        	<font class="campo">Dettaglio Richiesta Remissione Debito</font>
			        </td>
	        		<%--td class="LBG">
	      				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
	      					<jsp:param name="CampoIdEntita" value="<%=ICostantiPenaPecuniaria.CAMPO_ID_RICHIESTA_CONVERSIONE%>" />
             			<jsp:param name="ValoreIdEntita" value="<%=richiestaconversione.getIdRichiestaConversione()%>" />
           			</jsp:include>
							</td--%>
<% 		
							if ( richiestaremissione.getCodUfficioInserimento().compareTo(UtenteConnesso.getUfficioUtente().getCodUfficio()) == 0)
  						{%>
								<!-- BOTTONE DI MODIFICA -->
								<td class="LBG">
									<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.remissionedebito.action.ActLoadModificaRichiestaRemissioneDebito&<%=ICostantiSiusRemissioneDebito.CAMPO_ID_RICHIESTA_REMISSIONE%>=<%=richiestaremissione.getIdRichiestaRemissione()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=richiestaremissione.getFasSiuIdFascicoloSius()%>&TornaQui=<%=TornaQui%>" >
										<img  align="middle" src="/images/modifica24.gif" alt="Modifica Richiesta Conversione Pena Pecuniaria" width="24" height="24" border="0">
									</a>
								</td>

								<!--- BOTTONE DI CANCELLAZIONE -->
			    			<td class="LBG">
			      			<a href="Javascript:conferma2Param('siap.sius.remissionedebito.action.ActCancellaRichiestaRemissioneDebito','<%=ICostantiSiusRemissioneDebito.CAMPO_ID_RICHIESTA_REMISSIONE%>','<%=richiestaremissione.getIdRichiestaRemissione()%>', '<%=ICostantiSiusRemissioneDebito.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>', '<%=richiestaremissione.getFasSiuIdFascicoloSius()%>', 'TornaQui','<%=TornaQui%>');">
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
	    		<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getAnnoPartita()) %> / 
	    																			<%=StringUtils.toStringJSP(richiestaremissione.getNumPartita()) %></font>&nbsp;</td>
	  		</tr>
	  		<tr>
			    <td class="l">Numero Ex Campione Penale</td>
			    <td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getNumExCampione()) %></font>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Prot. Circosrizione Doganale</td>
				<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getProtCircosrizioneDoganale()) %></font>&nbsp;</td>
			</tr>
			<tr>
    			<td class="l">Autorità</td>
    			<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getDescrTipoAutoritaEmittente()) %>&nbsp;<%=StringUtils.toStringJSP(richiestaremissione.getDescrLuogoEmittente()) %></font></td>
  			</tr>
  			<tr>
    			<td class="l">Tipo Provvedimento</td>
    			<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getDescrTipoProvvedimento()) %></font>&nbsp;</td>
  			</tr>
			<tr>
		    	<td class="l">Data Emissione</td>
		    	<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaremissione.getDataEmissione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			</tr>
			<tr>
    			<td class="l">Autorità emittente</td>
    			<td class="l" colspan = "3"><font class="campo"><%=StringUtils.toStringJSP(richiestaremissione.getDescrAutoritaEmittenteProvv() ) %>&nbsp;<%=StringUtils.toStringJSP(richiestaremissione.getDescrLuogoEmittenteProvv()) %></font></td>
  			</tr>
			<tr>
			    <td class="l">Spese di mantenimento in carcere</td>
			    <td class="l"><font class="campo">
			    	<%if (richiestaremissione.getFlagSpeseCarcere().compareTo("S")==0) {%>
			    		<img src="/images/TickRed.gif">&nbsp;&nbsp; <%}%>
			    	<%if (richiestaremissione.getImportoSpeseCarcere() != null) {%>
			    &nbsp;€&nbsp;<%=StringUtils.toEuroFormat(richiestaremissione.getImportoSpeseCarcere() ) %></font>&nbsp; 			    	
			    <% } else {%>
			    &nbsp;-&nbsp; 
			    <%}%>
			</tr>
			<tr>
			    <td class="l">Spese di procedimento</td>
			    <td class="l"><font class="campo">
			    				    	<%if (richiestaremissione.getFlagSpeseProcedimento().compareTo("S")==0) {%>
			    		<img src="/images/TickRed.gif">&nbsp;&nbsp; <%}%>
			    	<%if (richiestaremissione.getImportoSpeseProcedimento() != null) {%>
			    &nbsp;€&nbsp;<%=StringUtils.toEuroFormat(richiestaremissione.getImportoSpeseProcedimento() ) %></font>&nbsp; 			    	
			    <% } else {%>
			    &nbsp;-&nbsp; 
			    <%}%>
			</tr>
			<tr>
			    <td class="l">Note</td>
			    <td class="l" colspan = "3"><font class="campo">
<%          if ((richiestaremissione.getNote()!= null) && (richiestaremissione.getNote().length()>0))
            {%>
              <%=richiestaremissione.getNote()%>
<%          }else{%>
							-&nbsp;<%}%>
			    </font>&nbsp;

			</tr>
			
   	</table>
	</body>
</html>