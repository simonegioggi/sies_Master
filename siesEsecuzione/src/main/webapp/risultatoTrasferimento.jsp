<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- MEV 16 + 31: restyling della pagina --%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Risultato del Trasferimento</title>
        <link rel="stylesheet" type="text/css" href="table.css"/>
        <%-- MEV 23010 - Aggiunto script per abilitare/disabilitare dettaglio errore --%>
		<script language="text/javascript">
			function detail() {
				var display = document.getElementById("dettaglioErrore").style.display;
    			if (display == 'none')
    				display = 'block';
    			else 
    				display = 'none';
    			document.getElementById("dettaglioErrore").style.display = display;
			}

			<%--MEV 16 CUMULO: aggiunte funzioni per gestione invio cumulo --%>
			function abilitaInvioFC(radio) {
				var sinonimo = radio.value;
				radio = document.getElementsByName("results");
				if (radio) {
					if (radio.length) {
						for (var i = 0; i < radio.length; i++) {
							var v = radio[i].value;
							document.getElementById("trasmissioneFC_" + v).style.display = (v == sinonimo) ? 'block' : 'none';
						}
					}
				}
			}
			function openPopup(url) {
				newwindow = window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
				if (window.focus)
			  		newwindow.focus();
	  		}
		</script>
    </head>
	<body>
		<%-- Viene visualizzato il semplice esito o il dettaglio in base al parametro visualizzazioneSemplice --%>
		<c:choose>
		    <c:when test="${not empty visualizzazioneSemplice}">
		      <b><c:out value="${response.esito}"/></b>
		    </c:when>
		    <c:otherwise>
		    	<fieldset>
					<table id="box-table-a" border="1">
						<thead>
							<tr>
								<th colspan="5" align="left" bgcolor="#000000">
									<%-- MEV 16 + 31: aggiunto controllo per gestire casistica FC --%>
									<c:choose>
									    <c:when test="${response.titoloEsecutivo.tipologiaUfficio == 'TDS' 			||
									    					response.titoloEsecutivo.tipologiaUfficio == 'TDSM' 	||
									    					response.titoloEsecutivo.tipologiaUfficio == 'TMIDS'	||
									    					response.titoloEsecutivo.tipologiaUfficio == 'UDS' 		||
									    					response.titoloEsecutivo.tipologiaUfficio == 'UDSM'}">
									       	<font size="+2" color="#000000">
									       		RISULTATO DELLA TRASMISSIONE DAL SIUS AL SISTEMA INFORMATIVO DEL CASELLARIO (SIC)
											</font>
									    </c:when>
									    <c:otherwise>
											<font size="+2" color="#000000">
									       		RISULTATO DELLA TRASMISSIONE DAL SIEP AL SISTEMA INFORMATIVO DEL CASELLARIO (SIC)
											</font>
									    </c:otherwise>
									</c:choose>
									<%-- FINE MEV 16 + 31 --%>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty response.soggetto}">
								<tr>
									<td><b>Soggetto</b></td><td><c:out value="${response.soggetto.output}"/></td>
								</tr>
							</c:if>
							<c:if test="${not empty response.titoloEsecutivo}">
								<%-- MEV 16 + 31: aggiunto controllo per gestire casistica FC per PM - PMM --%>
								<%-- MEV 16 CUMULO: modificato controllo e gestita nuova dicitura x cumulo (solo SIEP) --%>
								<c:choose>
								    <c:when test="${response.titoloEsecutivo.tipologiaUfficio == 'TDS' 				||
									    					response.titoloEsecutivo.tipologiaUfficio == 'TDSM' 	||
									    					response.titoloEsecutivo.tipologiaUfficio == 'TMIDS'	||
									    					response.titoloEsecutivo.tipologiaUfficio == 'UDS' 		||
									    					response.titoloEsecutivo.tipologiaUfficio == 'UDSM'}">
								       	<tr>
											<td><b>Procedimento SIUS</b></td><td><c:out value="${response.titoloEsecutivo.annoFascicolo}"/>/<c:out value="${response.titoloEsecutivo.numeroFascicolo}"/></td>
										</tr>
										<tr>
											<td><b>Provvedimento SIUS</b></td><td><c:out value="${response.titoloEsecutivo.output}"/></td>
										</tr>
								    </c:when>
								    <c:otherwise>
								        <tr>
											<td><b>Procedimento</b></td><td>N.&nbsp;<c:out value="${response.titoloEsecutivo.annoFascicolo}"/>/<c:out value="${response.titoloEsecutivo.numeroFascicolo}"/>&nbsp;SIEP</td>
										</tr>
										<c:choose>
											<c:when test="${cup == 'CUMULO'}">
												<tr>
													<td><b>Provvedimento Esecuzione di pene concorrenti</b></td><td>Articolo 663 C.P.P. del&nbsp;<c:out value="${response.titoloEsecutivo.dataEmissioneFormat}"/></td>
												</tr>
											</c:when>
											<c:otherwise>
												<tr>
													<td><b>Provvedimento SIEP</b></td><td><c:out value="${response.titoloEsecutivo.output}"/></td>
												</tr>
											</c:otherwise>
										</c:choose>
								    </c:otherwise>
								</c:choose>
								<%-- FINE MEV 16 + 31 --%>
							</c:if>
							<%-- MEV 16 CUMULO: aggiunta dicitura tipo provvedimento --%>
							<c:if test="${not empty response.titoloGiudiziarioList}">
								<c:forEach items="${response.titoloGiudiziarioList}" var="titoloGiudiziario">
									<tr>
										<td><b>Titolo Esecutivo</b></td>
										<td><c:out value="${titoloGiudiziario.descTipoProvvedimento}"/>&nbsp;
											<c:out value="${titoloGiudiziario.output}"/>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
					<br/>
					<br/>
					<center>
						<font size="+1">
							<c:choose>
					   	 		<c:when test="${cup == 'CUMULO'}">
					   	 			<c:choose>
							   	 		<c:when test="${not empty response.descCodiceEsito}">
		    								<c:out value="${response.descCodiceEsito}"/>
		    							</c:when>
							    		<c:otherwise>
							    			<c:out value="${response.esito}"/>
							    		</c:otherwise>
							    	</c:choose>
    							</c:when>
					    		<c:otherwise>
					    			<c:out value="${response.esito}"/>
					    		</c:otherwise>
					    	</c:choose>
							<c:if test="${not empty response.descrizioneProvvedimento}">
								<table>
									<tr><td align="right">CODICE UNIVOCO</td><td><c:out value="${response.descrizioneProvvedimento.codiceUnivoco}"/></td></tr>
									<tr><td align="right">CONTENUTO</td><td><c:out value="${response.descrizioneProvvedimento.oggetto}"/></td></tr>
									<tr><td align="right">OGGETTO</td><td><c:out value="${response.descrizioneProvvedimento.motivo}"/></td></tr>
									<tr><td align="right">ESITO</td><td><c:out value="${response.descrizioneProvvedimento.esito}"/></td></tr>
								</table>
							</c:if>
						</font>
						<c:if test="${not empty response.estratto}">
							<a href="load?id=<c:out value="${response.id}"/>"><img border="0" src="images/pdf.jpg" alt="Visualizza"/></a>
						</c:if>
						<br/><br/>
						<%-- MEV 23010 - Aggiunto dettaglio errore --%>
						<c:if test="${not empty response.dettaglioErrore}">
							<input class="submit" type="submit" value="DETTAGLIO ERRORE >>" onClick="detail();"/>
						</c:if>
					</center>
		
					<%-- MEV 16 + 31: Aggiunto elenco sinonimi --%>
					<c:if test="${not empty response.elencoSinonimi}">
						<br/>
						<table id="box-table-sin" border="1">
							<thead>
								<tr>
									<c:choose>
							   	 		<c:when test="${cup == 'CUMULO'}">
		    								<th colspan="11" bgcolor="#000000">
		    							</c:when>
							    		<c:otherwise>
							    			<th colspan="9" bgcolor="#000000">
							    		</c:otherwise>
							    	</c:choose>
										<b>:: Elenco Sinonimi ::&nbsp;(in arancione sono evidenziate le differenze rispetto al soggetto richiesto)</b>
									</th>
								</tr>
							</thead>
							<tbody>
								<%-- MEV 16 CUMULO: gestione diversa per i cumuli --%>
								<c:choose>
								    <c:when test="${cup == 'CUMULO'}">
								    	<tr>
								    		<th width="3%"></th>
								    		<th width="3%"></th>
									    	<th width="6%"><b>A/R</b></th>
										    <th width="13%"><b>Cognome</b></th>
										    <th width="13%"><b>Nome</b></th>
										    <th width="14%"><b>Luogo Nascita</b></th>
										    <th width="13%"><b>Data Nascita</b></th>
										    <th width="6%"><b>Sesso</b></th>
										    <th width="11%"><b>Paternit&agrave;</b></th>
										    <th width="6%"><b>CF/CUI</b></th>
											<th><b>Certificato di Controllo</b></th>
									    </tr>
								    </c:when>
								    <c:otherwise>
								    	<tr>
									    	<th width="6%"><b>A/R</b></th>
										    <th width="13%"><b>Cognome</b></th>
										    <th width="13%"><b>Nome</b></th>
										    <th width="14%"><b>Luogo Nascita</b></th>
										    <th width="13%"><b>Data Nascita</b></th>
										    <th width="6%"><b>Sesso</b></th>
										    <th width="11%"><b>Paternit&agrave;</b></th>
										    <th width="15%"><b>CF/Cod Identificativo</b></th>
											<th width="9%"><b>Certificato di Controllo</b></th>
									    </tr>
								    </c:otherwise>
								</c:choose>
							    <c:forEach items="${response.elencoSinonimi}" var="sinonimo">
									<tr>
										<!-- RADIO BUTTON -->
										<c:if test="${cup == 'CUMULO'}">
							   	 			<td>
							   	 				<input type="radio" name="results" value="${sinonimo.idSinonimo}" 
   													onclick="abilitaInvioFC(this);"/>
											</td>
										</c:if>
										<!-- MEV INTEGRAZIONE SIES ADN: aggiunta impostazione di variabile userAdn (x4) -->
							   	 		<c:if test="${cup == 'CUMULO'}">
							   	 			<td>
							   	 				<c:choose>
								    				<c:when test="${action == 'INSERT'}">
									   	 				<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=INSERT&idEvento=<c:out value="${idEvento}"/>&idUtente=<c:out value="${idUtente}"/>&userAdn=<c:out value="${userAdn}"/>&tipoWS=siepToNsc&idSoggetto=<c:out value="${response.soggetto.chiaveSies}"/>&idSentenza=<c:out value="${idSentenza}"/>&idFascicoloSiep=<c:out value="${idFascicoloSiep}"/>&idSinonimo=<c:out value="${sinonimo.idSinonimo}"/>');return(false);">
															<img id="trasmissioneFC_${sinonimo.idSinonimo}" border="0" width="24" height="24" align="middle"
																src="images/insertWS.png" alt="Trasmissione Foglio Complementare al SIC" style="display: none;"/>
														</a>
													</c:when>
													<c:otherwise>
														<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=UPDATE&idEvento=<c:out value="${idEvento}"/>&idUtente=<c:out value="${idUtente}"/>&userAdn=<c:out value="${userAdn}"/>&tipoWS=siepToNsc&idSoggetto=<c:out value="${response.soggetto.chiaveSies}"/>&idSentenza=<c:out value="${idSentenza}"/>&idFascicoloSiep=<c:out value="${idFascicoloSiep}"/>&idSinonimo=<c:out value="${sinonimo.idSinonimo}"/>');return(false);">
															<img id="trasmissioneFC_${sinonimo.idSinonimo}" border="0" width="24" height="24" align="middle"
																src="/images/updateWS.png" alt="Modifica Foglio Complementare sul SIC" style="display: none;"/>
														</a>
													</c:otherwise>
												</c:choose>
											</td>
										</c:if>
										<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualFlagAliasRichiamo}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.flagAliasRichiamoSinonimo}"/>
			    						</td>

										<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualCognome}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.cognomeSinonimo}"/>
			    						</td>

			    						<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualNome}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.nomeSinonimo}"/>
									    </td>

									    <c:choose>
								   	 		<c:when test="${'03900' == sinonimo.codiceNazioneNascitaSinonimo}">
				    							<c:choose>
										   	 		<c:when test="${'false' == sinonimo.isEqualLuogoNascita}">
					    								<td style="background-color: orange; width:100%">
					    							</c:when>
										    		<c:otherwise>
										    			<td>
										    		</c:otherwise>
										    	</c:choose>
				    								<c:out value="${sinonimo.descLuogoNascitaSinonimo}"/>
											    </td>
									   		</c:when>
								    		<c:otherwise>
								    			<c:choose>
										   	 		<c:when test="${'false' == sinonimo.isEqualLuogoNascita}">
					    								<td style="background-color: orange; width:100%">
					    							</c:when>
										    		<c:otherwise>
										    			<td>
										    		</c:otherwise>
										    	</c:choose>
							    				<c:choose>
							   	 					<c:when test="${not empty sinonimo.descComuneEsteroNascitaSinonimo}">
							   	 						<c:out value="${sinonimo.descComuneEsteroNascitaSinonimo}"/>&nbsp;
									    				(<c:out value="${sinonimo.descNazioneNascitaSinonimo}"/>)
									    			</c:when>
									   		    	<c:otherwise>
									    				<c:out value="${sinonimo.descNazioneNascitaSinonimo}"/>
										    		</c:otherwise>
												</c:choose>
										   		</td>
								    		</c:otherwise>
										</c:choose>

			    						<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualDataNascita}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.dataNascitaSinonimoFormat}"/>
			    						</td>

			    						<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualSesso}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.sessoSinonimo}"/>
			    						</td>

			    						<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualPaternita}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.paternitaSinonimo}"/>
			    						</td>

										<%-- questo controllo vale sia per CF che per CI --%>
			    						<c:choose>
								   	 		<c:when test="${'false' == sinonimo.isEqualCodiceFiscale}">
			    								<td style="background-color: orange; width:100%">
			    							</c:when>
								    		<c:otherwise>
								    			<td>
								    		</c:otherwise>
								    	</c:choose>
			    							<c:out value="${sinonimo.codiceFiscaleSinonimo}"/>
			    						</td>

			    						<c:choose>
								   	 		<c:when test="${not empty sinonimo.certificatoControlloSinonimo}">
								   	 			<td>
													<a href="load?id=<c:out value="${sinonimo.idCertificatoControlloSinonimo}"/>">
														<img border="0" src="images/pdf.jpg" alt="Visualizza"/>
													</a>
												</td>
								   	 		</c:when>
								   	 		<c:otherwise>
												<TD>NESSUN PROVVEDIMENTO</TD>
											</c:otherwise>
										</c:choose>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<br/>
					</c:if>
					<%-- FINE MEV 16 + 31 --%>

					<%-- MEV 16 CUMULO: Aggiunto elenco provvedimenti cumulabili --%>
					<c:if test="${cup == 'CUMULO'}">
						<c:if test="${not empty response.elencoProvvedimentiNSC}">
							<br/>
							<table id="box-table-cum" border="1" width="100%">
								<thead>
									<tr>
	    								<th colspan="4" bgcolor="#000000">
	    									<center>
												<b>:: Elenco Provvedimenti NSC ::</b>
											</center>
										</th>
									</tr>
								</thead>
								<tbody>
									<tr>
								    	<th width="16%"><b>Titolo Esecutivo</b></th>
									    <th width="28%"><b>Presente sul SIC</b></th>
									    <th width="28%"><b>Stato Titolo Esecutivo sul SIC</b></th>
									    <th width="28%"><b>Al Nome di</b></th>
								    </tr>
									<c:forEach items="${response.elencoProvvedimentiNSC}" var="provvedimentoNSC">
										<tr>
											<td><c:out value="${provvedimentoNSC.dataProvvedimentoFormat}"/></td>
											<td><c:out value="${provvedimentoNSC.presenteSIC}"/></td>
											<td><c:out value="${provvedimentoNSC.statoTitoloEsecSIC}"/></td>
											<td><c:out value="${provvedimentoNSC.alNomeDi}"/></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<br/>
						</c:if>
					</c:if>
					<%-- FINE MEV 16 CUMULO --%>

					<center>
						<%-- MEV 16 CUMULO: aggiunto pulsante invio FC --%>
						<c:if test="${cup == 'CUMULO'}">
							<c:if test="${isTrasferibile == 'SI'}">
								CONFERMA INVIO FC >>>&nbsp;
								<c:choose>
				    				<c:when test="${action == 'INSERT'}">
										<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=INSERT&idEvento=<c:out value="${idEvento}"/>&idUtente=<c:out value="${idUtente}"/>&userAdn=<c:out value="${userAdn}"/>&tipoWS=siepToNsc&idSoggetto=<c:out value="${response.soggetto.chiaveSies}"/>&idSentenza=<c:out value="${idSentenza}"/>&idFascicoloSiep=<c:out value="${idFascicoloSiep}"/>&idSoggettoNSC=<c:out value="${response.soggetto.chiaveNSC}"/>&azioneTrasfCumulo=<c:out value="${response.descEsitoCumulo}"/>');return(false);">	
											<img id="trasmissioneFC" border="0" width="24" height="24" align="middle" src="images/insertWS.png" alt="Trasmissione Foglio Complementare al SIC"/>
										</a>
									</c:when>
									<c:otherwise>
										<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=UPDATE&idEvento=<c:out value="${idEvento}"/>&idUtente=<c:out value="${idUtente}"/>&userAdn=<c:out value="${userAdn}"/>&tipoWS=siepToNsc&idSoggetto=<c:out value="${response.soggetto.chiaveSies}"/>&idSentenza=<c:out value="${idSentenza}"/>&idFascicoloSiep=<c:out value="${idFascicoloSiep}"/>&idSoggettoNSC=<c:out value="${response.soggetto.chiaveNSC}"/>&azioneTrasfCumulo=<c:out value="${response.descEsitoCumulo}"/>');return(false);">
											<img id="trasmissioneFC" border="0" width="24" height="24" align="middle" src="/images/updateWS.png" alt="Modifica Foglio Complementare sul SIC"/>
										</a>
									</c:otherwise>
								</c:choose>
								&nbsp;&nbsp;&nbsp;
							</c:if>
						</c:if>
						<%-- FINE MEV 16 CUMULO --%>
						<input class="submit" type="submit" value="CHIUDI" onClick="window.close();"/>
					</center>
					<br/>
					<div id="dettaglioErrore" style="display:none;"><c:out value="${response.dettaglioErrore}"/>"></div>
					<br/><br/><a href="Javascript:window.print();"><img border="0" src="images/print.jpg" alt="Stampa"/></a><br/>
					<font size="-2">IL SERVIZIO DI HELP DESK E' ATTIVO DAL LUNEDI' AL VENERDI'<br/>
					DALLE 8:00 ALLE 18:00 ED IL SABATO DALLE 8:30 ALLE 14:00<br/>
					TEL: 06/97996200
					</font>
				</fieldset>
		    </c:otherwise>
		</c:choose>
	</body>
</html>