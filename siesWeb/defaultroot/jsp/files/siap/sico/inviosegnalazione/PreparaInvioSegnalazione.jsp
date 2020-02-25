<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV10-s3: aggiunta pagina invio segnalazione --%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="UtenteConnesso" 		scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="titoloPersona"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="funzionalita"   		scope="request" class="java.lang.String"/>
<jsp:useBean id="azione"   				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoSegnalazione"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="gravitaSegnalazione"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="versione"  			scope="request" class="java.lang.String"/>

<html>
	<head>
		<title>[S.I.E.S.] - Invio Segnalazione tramite email</title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
		<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
		<script type="text/javascript">
			function eliminaAllegato(id) {
				var val = document.getElementById(id);
				if (val != null)
					val.outerHTML = val.outerHTML;
				val = null;
			}
			function validaForm(aForm) {
				var esito = true;
				var msg = "";
				var annoProc = aForm.annoProcedimento.value;
				var numeroProc = aForm.numeroProcedimento.value;
				var annoProcSearch = aForm.annoProcedimento.value.search("[^0-9]");
				var numeroProcSearch = aForm.numeroProcedimento.value.search("[^0-9]");
			    if ((annoProc.length > 0 && annoProcSearch >= 0) || isNaN(annoProc)) {
			    	msg += "Il campo Anno Procedimento è numerico\n";
			        esito = false;
			    }
			  	if (annoProc != "" && annoProc < 0) {
			  		msg += "Anno Procedimento Non Valido\n";
		          	esito = false;
			    }
// 			  	if (annoProc.length <= 0) {
// 			  		msg += "Anno Procedimento Obbligatorio\n";
// 		          	esito = false;
// 			    }
			  	if ((numeroProc.length > 0 && numeroProcSearch >= 0) || isNaN(numeroProc)) {
			  		msg += "Il campo Numero Procedimento è numerico\n";
			        esito = false;
			    }
			  	if (numeroProc != "" && numeroProc <= 0) {
			  		msg += "Numero Procedimento Non Valido\n";
		         	esito = false;
			    }
// 			  	if (numeroProc.length <= 0) {
// 			  		msg += "Numero Procedimento Obbligatorio\n";
// 		          	esito = false;
// 			    }
			  	if (aForm.oggettoSegnalazione.value.length <= 0) {
			  		msg += "Oggetto Segnalazione Obbligatorio\n";
		         	esito = false;
			    }
			  	if (aForm.descSegnalazione.value.length <= 0) {
			  		msg += "Descrizione Segnalazione Obbligatoria\n";
		         	esito = false;
			    }
			  	if (msg != "")
			  		alert(msg);
			  	return esito;
			}
		</script>
	</head>
	<body class="corpo">
		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="f" enctype="multipart/form-data">
			<input value="siap.sico.inviosegnalazione.action.InvioSegnalazione" type="hidden"
				name="<%=IWebConstants.ACTION_FIELD%>" />
			<!-- UFFICIO -->
			<table style="border: 2.5px solid black; width: 100%;">
			    <tr>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" colspan="3">
						UFFICIO
			      	</td>
			    </tr>
			    <tr>
			    	<td class="L" width="33%"><font class="label">Tipologia Ufficio:</font></td>
			    	<td class="L" width="33%"><font class="label">Sede Ufficio:</font></td>
			    	<td class="L"><font class="label">Telefono Ufficio:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"><font class="campo"><%=UtenteConnesso.getUfficioUtente().getDescrTipoUfficio()%></font></td>
			    	<td class="L"><font class="campo"><%=UtenteConnesso.getUfficioUtente().getDescrComune() + " ( "
			    					+ UtenteConnesso.getUfficioUtente().getDescProvincia() + " )"%></font></td>
			    	<td class="L"><font class="campo"><%=(UtenteConnesso.getUfficioUtente().getTelefono() != null) ? UtenteConnesso.getUfficioUtente().getTelefono() : ""%></font></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" width="66%" colspan="2"><font class="label">Email Ufficio:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" colspan="2"><font class="campo"><%=(UtenteConnesso.getUfficioUtente().getEMail() != null) ? UtenteConnesso.getUfficioUtente().getEMail() : ""%></font></td>
			    </tr>
			</table>
			<!-- REFERENTE -->
			<table style="border: 2.5px solid black; width: 100%;">
			    <tr>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" colspan="4">
						REFERENTE
			      	</td>
			    </tr>
			    <tr>
			    	<td class="L" width="25%"><font class="label">Titolo Referente:</font></td>
			    	<td class="L" width="25%"><font class="label">Cognome Referente:</font></td>
			    	<td class="L" width="25%"><font class="label">Nome Referente:</font></td>
			    	<td class="L"><font class="label">Telefono Referente:</font></td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<select class="small" Title="Titolo Referente" name="titoloReferente">
       						<%=titoloPersona%>
     					</select>
     				</td>
			    	<td class="L"><input maxlength="40" name="cognomeReferente"></td>
			    	<td class="L"><input maxlength="40" name="nomeReferente"></td>
			    	<td class="L"><input maxlength="15" name="telefonoReferente"></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" colspan="3"><font class="label">Email Referente:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" colspan="3"><input maxlength="100" name="emailReferente" size="60"></td>
			    </tr>
			</table>
			<!-- RICHIEDENTE -->
			<table style="border: 2.5px solid black; width: 100%;">
			    <tr>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" colspan="4">
						RICHIEDENTE
			      	</td>
			    </tr>
			   <tr>
			    	<td class="L" width="25%"><font class="label">Titolo Richiedente:</font></td>
			    	<td class="L" width="25%"><font class="label">Cognome Richiedente:</font></td>
			    	<td class="L" width="25%"><font class="label">Nome Richiedente:</font></td>
			    	<td class="L"><font class="label">Telefono Richiedente:</font></td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<select class="small" Title="Titolo Richiedente" name="titoloRichiedente">
       						<%=titoloPersona%>
     					</select>
     				</td>
			    	<td class="L"><font class="campo"><%=UtenteConnesso.getCognome()%></font></td>
			    	<td class="L"><font class="campo"><%=UtenteConnesso.getNome()%></font></td>
			    	<td class="L"><font class="campo"><%=(UtenteConnesso.getTelefono() != null) ? UtenteConnesso.getTelefono() : ""%></font></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" colspan="3"><font class="label">Email Richiedente:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"></td>
			    	<td class="L" colspan="3"><font class="campo"><%=(UtenteConnesso.getEmail() != null) ? UtenteConnesso.getEmail() : ""%></font></td>
			    </tr>
			</table>
			<!-- SEGNALAZIONE -->
			<table style="border: 2.5px solid black; width: 100%;">
				<tr>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" colspan="4">
						SEGNALAZIONE
			      	</td>
			    </tr>
			    <%
			    	String stato = "N.D.";
			    	String codTipoUff = new String(UtenteConnesso.getUfficioUtente().getCodTipoUfficio());
					if (UtenteConnesso.getUserProfile().getProfileId().intValue() != 99 &&
							UtenteConnesso.getUserProfile().getProfileId().intValue() != 90) {
	  					if (codTipoUff.equals("UEPE") || codTipoUff.equals("UEPESS")) {
	  						stato = "SIEPE";
	  					} else if (codTipoUff.startsWith("TDS") || codTipoUff.startsWith("UDS")) {
	  						stato = "SIUS";
						} else if (codTipoUff.equals("PGCAP") || codTipoUff.equals("PM") || codTipoUff.equals("PMM")) {
							stato = "SIEP";
	 					} else {
	 						stato = "SIGE";
	 					}
					}
				%>
				<tr>
					<td class="L" width="25%"><font class="label">Sottosistema:</font></td>
			    	<td class="L"><font class="label">Funzionalità:</font></td>
			    	<td class="L" width="25%"><font class="label">Azione:</font></td>
			    	<td class="L" width="25%"><font class="label">Tipologia Segnalazione:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"><font class="campo"><%=stato%></font></td>
			    	<td class="L">
			    		<select class="small" Title="Funzionalità" name="funzionalita">
       						<%=funzionalita%>
     					</select>
     				</td>
     				<td class="L">
			    		<select class="small" Title="Azione" name="azione">
       						<%=azione%>
     					</select>
     				</td>
     				<td class="L">
			    		<select class="small" Title="Tipologia Segnalazione" name="tipoSegnalazione">
       						<%=tipoSegnalazione%>
     					</select>
     				</td>
			    </tr>
			   <tr>
					<td class="L" width="25%"><font class="label">Versione Software:</font></td>
			    	<td class="L" width="25%"><font class="label">Gravità Segnalazione:</font></td>
			    	<td class="L" width="25%"><font class="label">Anno Procedimento:</font></td>
			    	<td class="L"><font class="label">Numero Procedimento:</font></td>
			    </tr>
			    <tr>
			    	<td class="L"><font class="campo"><%=versione%></font></td>
			    	<td class="L">
			    		<select class="small" Title="Gravità Segnalazione" name="gravitaSegnalazione">
       						<%=gravitaSegnalazione%>
     					</select>
     				</td>
			    	<td class="L"><input maxlength="4" name="annoProcedimento" size="5"></td>
			    	<td class="L"><input maxlength="6" name="numeroProcedimento" size="7"></td>
			    </tr>
				<tr>
					<td class="L" colspan="4"><font class="label">Oggetto Segnalazione</font></td>
				</tr>
				<tr>
					<td class="L" colspan="4"><input maxlength="70" name="oggettoSegnalazione" size="100"></td>
				</tr>
				<tr>
					<td class="L" colspan="4"><font class="label">Descrizione Segnalazione</font></td>
				</tr>
				<tr>
					<td class="L" colspan="4"><textarea cols="100" rows="5" name="descSegnalazione"></textarea></td><%-- maxlength="200" --%>
				</tr>
			</table>
			<!-- COPIA -->
			<table style="border: 2.5px solid black; width: 100%;">
				<tr>
					<td class="L">
						<input type="hidden" name="autoInvio">
						<input type="checkbox" name="autoInvio">
						<font class="label">Inviare copia email nella propria casella di posta</font>
					</td>
				</tr>
			</table>
			<!-- ALLEGATI -->
			<table style="border: 2.5px solid black; width: 100%;">
				<tr>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32" width="90%">
						ALLEGATI (Dimensione totale massima dei 5 files pari a due megabyte)
			      	</td>
			      	<td align="center" style="background-color: #BEC6FC; color: white; font-weight: bold;
			      		font-family: 'Tahoma'; border: 2.5px solid #F0F0F0;" height="32">
						AZIONI
			      	</td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<input type="file" name="allegato1" id="allegato1" value="Sfoglia..." size="80">
			    	</td>
			    	<td class="L">
			    		<a href="Javascript:eliminaAllegato('allegato1')">
		          			<center><img src="../../images/delete24.gif" width="32" height="32" alt="Elimina Allegato" border="0"></center>
		        		</a>
		        	</td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<input type="file" name="allegato2" id="allegato2" value="Sfoglia..." size="80">
			    	</td>
			    	<td class="L">
			    		<a href="Javascript:eliminaAllegato('allegato2')">
		          			<center><img src="../../images/delete24.gif" width="32" height="32" alt="Elimina Allegato" border="0"></center>
		        		</a>
		        	</td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<input type="file" name="allegato3" id="allegato3" value="Sfoglia..." size="80">
			    	</td>
			    	<td class="L">
			    		<a href="Javascript:eliminaAllegato('allegato3')">
		          			<center><img src="../../images/delete24.gif" width="32" height="32" alt="Elimina Allegato" border="0"></center>
		        		</a>
		        	</td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<input type="file" name="allegato4" id="allegato4" value="Sfoglia..." size="80">
			    	</td>
			    	<td class="L">
			    		<a href="Javascript:eliminaAllegato('allegato4')">
		          			<center><img src="../../images/delete24.gif" width="32" height="32" alt="Elimina Allegato" border="0"></center>
		        		</a>
		        	</td>
			    </tr>
			    <tr>
			    	<td class="L">
			    		<input type="file" name="allegato5" id="allegato5" value="Sfoglia..." size="80">
			    	</td>
			    	<td class="L">
			    		<a href="Javascript:eliminaAllegato('allegato5')">
		          			<center><img src="../../images/delete24.gif" width="32" height="32" alt="Elimina Allegato" border="0"></center>
		        		</a>
		        	</td>
			    </tr>
			</table>
			<!-- FUNZIONALITA' -->
			<table style="border: 2.5px solid black; width: 100%;">
				<tr>
					<td class="L" width="33%"><center><input class="bottone" type="submit" name="inviaEmail" 	value="Invia email" 	onclick="Javascript:return validaForm(this.form)"></center></td>
					<td class="L" width="33%"><center><input class="bottone" type="reset"  name="svuotaModulo" 	value="Svuota Modulo"></center></td>
					<td class="L" >			  <center><input class="bottone" type="button" name="chiudi" 		value="Chiudi" 			onclick="Javascript:window.close()"></center></td>
				</tr>
			</table>
		</FORM>
	</body>
</html>