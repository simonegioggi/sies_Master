<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina --%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>

<jsp:useBean id="misuresicurezza" 			scope="request" class="java.util.Vector" />
<jsp:useBean id="naturaMisuraSicurezza" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="ordinanza"          		scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="decreto"          			scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="esecuzionemisurasicurezza" scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />
<jsp:useBean id="datiOrdinanza" 			scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="datiNuovaMS"				scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>

<%
TenoreModel[] tenori = datiOrdinanza.getTenori();
String[] esiti = (String[]) request.getAttribute("esiti");

Integer sizeMisure = misuresicurezza.size();
String numMisure = "" + sizeMisure;

String numEsecMS = "0";
if (esecuzionemisurasicurezza != null && esecuzionemisurasicurezza.getIdEsecuzioneMisuraSicurezza() != null)
	numEsecMS = "1";
%>

<html>
	<head>
    	<title>[S.I.E.S.] - Modifica Ordinanza Appello Contro Provvedimento su Misura Sicurezza</title>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    	<script language="JavaScript" src="/html/verifyCombo.js"></script>

  		<script language="JavaScript">
  			var desktop;

    		// STUB 21/07/2004 Controllo obbligatorietà esiti.
    		function Verify() {
      			var lEsiti = document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      			var ritorno = VerifyCombo(lEsiti, "Esito");
      			// SG: 20170421 modifica migliorativa per errore js
      			if (!ritorno)
      				return ritorno;

        		// Controllo della data di proroga
        		var nodeProrogaMisura = document.getElementById("prorogamisura");
				var data_proroga = document.ModificaOrdinanzaAppelloControProvvMS.giornoDataDecorrenzaNuovaMS.value+'/'+document.ModificaOrdinanzaAppelloControProvvMS.meseDataDecorrenzaNuovaMS.value+'/'+document.ModificaOrdinanzaAppelloControProvvMS.annoDataDecorrenzaNuovaMS.value;
				if (!ControllaDataPassaVuota(data_proroga)) {
        	       	alert('Data Decorrenza non valida! ' + data_proroga);
        	       	document.ModificaOrdinanzaAppelloControProvvMS.giornoDataDecorrenzaNuovaMS.focus();
        	       	return false;
				}

        		// Controllo presenza unica misura di sicurezza afferente all'ordinanza
        		var numeroMisure = <%=numMisure%>;
        		var numEsecMS = <%=numEsecMS%>;
        		// SG: 20170421 modifica migliorativa per errore js --> ci possono essere più esiti
        		if (numeroMisure > 1) {
        			// Size della Combo = 1
        		    if (typeof (lEsiti[0][0]) == "undefined") {
        		        if (lEsiti[lEsiti.selectedIndex].value in { '-':1, '0251':1, '0252':1, '0125':1, '0126':1, '0127':1, '0128':1, '0129':1, '0130':1 }) {
        		        	alert('Procedimento con più misure di sicurezza. Eliminare le misure superflue selezionando: Dettaglio Misure di Sicurezza');
        					return false;
        		        }
					} else {
        		        for (j = 0; j < lEsiti.length ; j++) {
        		        	for (i = 0; i < lEsiti[j].length ; i++) {
        		        		if ((lEsiti[j][i].selected) && (lEsiti[j][i].value in { '-':1, '0251':1, '0252':1, '0125':1, '0126':1, '0127':1, '0128':1, '0129':1, '0130':1 })) {
        		        			alert('Procedimento con più misure di sicurezza. Eliminare le misure superflue selezionando: Dettaglio Misure di Sicurezza');
        							return false;
        		            	}
        		          	}
						}
					}
				}

        		if (numeroMisure == 0 && numEsecMS == 0) {
        			// Size della Combo = 1
        		    if (typeof (lEsiti[0][0]) == "undefined") {
        		        if (lEsiti[lEsiti.selectedIndex].value in { '-':1, '0251':1, '0252':1, '0125':1, '0126':1, '0127':1, '0128':1, '0129':1, '0130':1 }) {
        		        	alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        					return false;
        		        }
					} else {
        		        for (j = 0; j < lEsiti.length ; j++) {
        		        	for (i = 0; i < lEsiti[j].length ; i++) {
        		        		if ((lEsiti[j][i].selected) && (lEsiti[j][i].value in { '-':1, '0251':1, '0252':1, '0125':1, '0126':1, '0127':1, '0128':1, '0129':1, '0130':1 })) {
        		        			alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        							return false;
        		            	}
        		          	}
						}
					}
        		}

    			// Size della Combo = 1
    		    if (typeof (lEsiti[0][0]) == "undefined") {
    		        if (lEsiti[lEsiti.selectedIndex].value == '0251'
    		        		&& document.ModificaOrdinanzaAppelloControProvvMS.codiTipoNuovaMisura.value == "-") {
    		        	alert("Attenzione! Campo Nuova Misura obbligatorio.");
    		        	document.ModificaOrdinanzaAppelloControProvvMS.codiTipoNuovaMisura.focus();
    					return false;
    		        }
				} else {
    		        for (j = 0; j < lEsiti.length ; j++) {
    		        	for (i = 0; i < lEsiti[j].length ; i++) {
    		        		if (lEsiti[j][i].selected && lEsiti[j][i].value == '0251'
        		        			&& document.ModificaOrdinanzaAppelloControProvvMS.codiTipoNuovaMisura.value == "-") {
            		        	alert("Attenzione! Campo Nuova Misura obbligatorio.");
            		        	document.ModificaOrdinanzaAppelloControProvvMS.codiTipoNuovaMisura.focus();
            					return false;
            		        }
    		          	}
					}
				}

				return ritorno;
    		}

      		// Chiamata all'elenco degli UDS
      		function ListaUDS(a_formname,a_fieldname, a_typename) {
      			desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      		}

      		// MERGE v10: modificata gestione della funzione
      		function abilitaNuovaMisura() {
	   			var nodeProrogaMisura = document.getElementById("prorogamisura");
   				nodeProrogaMisura.style.display = 'none';
	   			if (typeof (document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
	   				for (j = 0; j < document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
	   					if (document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
				       		if (document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value == "0251")
			       				nodeProrogaMisura.style.display = 'block';
				       		else
				       			pulisciCampi();
       					}
   	   				} // fine ciclo for
	 			} // Fine caso singolo oggetto
	 			// Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
     			else {
     				// 20191018 [SG]: aggiunto codice
     				var test = false;
       				for (j = 0; j < document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.length ; j++) {
						for (i = 0; i < document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
				           	if (document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[i].value == "0251") {
			           			test = true;
			           			break;
							}
						}
					}
       				if (test)
       					nodeProrogaMisura.style.display = 'block';
       				else
       					pulisciCampi();
				} // Fine caso più oggetti
			}

      		function pulisciCampi() {
      			document.ModificaOrdinanzaAppelloControProvvMS.codiTipoNuovaMisura.value = "-";
      			document.ModificaOrdinanzaAppelloControProvvMS.giornoDataDecorrenzaNuovaMS.value = "";
      			document.ModificaOrdinanzaAppelloControProvvMS.meseDataDecorrenzaNuovaMS.value = "";
      			document.ModificaOrdinanzaAppelloControProvvMS.annoDataDecorrenzaNuovaMS.value = "";
				document.ModificaOrdinanzaAppelloControProvvMS.anniDurataNuovaMS.value = "";
				document.ModificaOrdinanzaAppelloControProvvMS.mesiDurataNuovaMS.value = "";
				document.ModificaOrdinanzaAppelloControProvvMS.giorniDurataNuovaMS.value = "";
      		}

      		function valorizzaCampiProvvImp() {
	      	    <% if (ordinanza != null && ordinanza.getIdDepositoOrdinanzaPc() != null) { %>
      	  		document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(ordinanza.getDataInserimento(), "dd")%>";
	       		document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(ordinanza.getDataInserimento(), "MM")%>";
	       		document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(ordinanza.getDataInserimento(), "yyyy")%>";
	       		document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "<%=ordinanza.getDescrUfficioInserimento()%>";
	      	    <% } else if (decreto != null && decreto.getIdDepositoDecreto() != null) { %>
      	  		document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "dd")%>";
       			document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "MM")%>";
       			document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>.value = "<%=DateUtils.getDateToString(decreto.getDataEmissione(), "yyyy")%>";
       			document.ModificaOrdinanzaAppelloControProvvMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>.value = "<%=decreto.getDescrUfficioInserimento()%>";
    	    	<% } %>
      	    }
   		</script>
 	</head>

<%
String lAction = new String();
lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
%>

  	<body class="corpo" onLoad="Javascript: valorizzaCampiProvvImp(); abilitaNuovaMisura();">
    	<table>
    		<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
				<td class=LBG><font class="label">Funzione: </font> <font class="campo">Modifica Ordinanza Appello Contro Provvedimento su Misura Sicurezza</font></td>
    			<!-- BOTTONE DI RITORNO -->
      			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    		</tr>
		    <tr>
		    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
		    </tr>
    	</table>

  		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaOrdinanzaAppelloControProvvMS">
    		<table cellspacing="2" cellpadding="2" width="95%">
				<tr>
				  	<td class="l" width="25%">Data Emissione</td>
					<td class="l">
						<input value="<%=DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(), "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>"> /
						<input value="<%=DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(), "MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"> /
						<input value="<%=DateUtils.getDateToString(datiOrdinanza.getEvento().getDataEmissione(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>">
      				</td>
				</tr>
    		</table>
    		<br>
     		<table cellspacing="2" cellpadding="2" width="95%">
    			<tr>
        			<td class="Titolo" colspan="2">Specificare esito per ciascun oggetto</td>
    			</tr>
			    <tr>
			        <td class="c" width="40%">Oggetto</td>
			        <td class="c">Esito</td>
			    </tr>
    			<%
   				for (int i = 0; i < tenori.length; i++) {
   				%>
       			<tr>
        			<td class="l">
						<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="60">
						<input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>">
<%-- 						<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>"> --%>
						<input Title="ID Tenore" type="hidden" name="<%=ICostantiTenore.CAMPO_ID_TENORE%>" value="<%=tenori[i].getIdTenore().toString()%>">
        			</td>
          			<td class="l">
           				<select Title="Cod Esito" class="small" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript: abilitaNuovaMisura();">
             				<%=esiti[i]%>
          				</select>
        			</td>
      			</tr>
    			<%
    			}
    			%>
    		</table>
			<br>
 			<table cellspacing="2" cellpadding="2" width="95%">
		 		<tr>
					<td class="l" width="25%">Ulteriore descrizione della decisione</td>
		    		<td class="l">
		    			<TEXTAREA title="Ulteriore Descrizione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols="70" rows="4"><%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getUlterioreDescrizione())%></textarea>
		    		</td>
				</tr>
			</table>

			<% if ((ordinanza != null && ordinanza.getIdDepositoOrdinanzaPc() != null)
						|| (decreto != null && decreto.getIdDepositoDecreto() != null)) { %>
			<br>
			<table cellspacing="2" cellpadding="2" width="95%">
				<tr>
    				<td class="Titolo" colspan="4">Estremi Provvedimento Impugnato<td>
  				</tr>
    			<tr>
					<td class="l" width="25%">Data Emissione</td>
					<td class="l">
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_TRASMISSIONE%>" readonly> /
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_TRASMISSIONE%>" readonly> /
						<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_TRASMISSIONE%>" readonly>
      				</td>
      				<td class="l">Ufficio Sorveglianza</td>
      				<td class="l">
						<input Title="Magistrato" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size="35" readonly>
						<a href="Javascript:ListaUDS('ModificaOrdinanzaAppelloControProvvMS','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
							<img src="/images/filefolder.gif" border="0">
						</a>
					</td>
    			</tr>
    		</table>
   			<% } %>

			<% if (esecuzionemisurasicurezza != null && esecuzionemisurasicurezza.getIdEsecuzioneMisuraSicurezza() != null ) { %>
			<br>
       		<input type="hidden" name="<%=ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS%>" value="<%=esecuzionemisurasicurezza.getIdEsecuzioneMisuraSicurezza()%>" >
			<table cellspacing="2" cellpadding="2" width="95%">
				<tr>
       				<td class="Titolo" colspan="4">Misura di Sicurezza in Esecuzione</td>
   				</tr>
				<tr>
					<td class="L">
						<font class="label">Tipo</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getCodTipoMisura() != null && esecuzionemisurasicurezza.getDescrTipoMisura() != null) { %>
		    			<input Title="Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getDescrTipoMisura()%>" readonly size="90">
<%						} else {%>
						<input Title="Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE %>" value="-" readonly size="90"><%}%>
					</td>
					<td class="L">
						<font class="label">Durata: Anni</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumAnniMisura() != null) { %>
		    			<input Title="Anni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumAnniMisura()%>" readonly size="5">
<%						} else { %>
						<input Title="Anni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_ESECUZIONE %>" value="0"  readonly size="5"><%}%>
					</td>
					<td class="L">
						<font class="label">Mesi</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumMesiMisura() != null) { %>
		    			<input Title="Mesi" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_MESI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumMesiMisura()%>" readonly size="5">
<%						} else { %>
						<input Title="Mesi" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_MESI_ESECUZIONE %>" value="0" readonly size="5"><%}%>
					</td>
					<td class="L">
						<font class="label">Giorni</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumGiorniMisura() != null) { %>
		    			<input Title="Giorni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumGiorniMisura()%>" readonly size="5">
<%						} else { %>
						<input Title="Giorni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_ESECUZIONE %>" value="0" readonly size="5"><%}%>
					</td>
				</tr>
			</table>
<%
		} // Fine Dati Misura Sicurezza in Esecuzione
%>

		<%
		int indice = -1;
    	Iterator itx = misuresicurezza.iterator();
    	while (itx.hasNext()) {
			indice++;
			MisuraSicurezzaModel lMisuraSicurezza = (MisuraSicurezzaModel)itx.next();
		%>
   			<input type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=lMisuraSicurezza.getIdMisuraSicurezza()%>" >
		<%
			String lTitoloMisuraSicurezza = "Misura di Sicurezza in Esecuzione";
// 			if (lMisuraSicurezza.getFasSieIdFascicoloSiep() != null)
// 				lTitoloMisuraSicurezza = "Misura di Sicurezza ereditata da SIEP";
// 			else
// 				lTitoloMisuraSicurezza = "Misura di Sicurezza inserita dall'UDS";
		%>
			<br>
			<table cellspacing="2" cellpadding="2" width="95%">
				<tr>
   					<td class="Titolo" colspan="2"> <%=lTitoloMisuraSicurezza%></td>
				</tr>
				<tr>
					<td class="L" width="25%">
						<font class="label">Natura</font>
						<% if (lMisuraSicurezza.getCodNatura() != null && lMisuraSicurezza.getDescrNatura() != null) { %>
							<%-- MERGE v10: aggiunto campo nascosto --%>
							<input Title="Codice Natura" type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA%>" value="<%=lMisuraSicurezza.getCodNatura()%>">
	    					<input Title="Descrizione Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DESCR_COD_NATURA%>" value="<%=lMisuraSicurezza.getDescrNatura()%>" readonly size="25">
						<% } else { %>
							<input Title="Codice Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>" value="-" readonly size="25">
						<% } %>
					</td>
					<td class="L">
						<font class="label">Tipo</font>
						<% if (lMisuraSicurezza.getCodTipo() != null && lMisuraSicurezza.getDescrTipo() != null) { %>
							<%-- MERGE v10: aggiunto campo nascosto --%>
							<input Title="Codice Tipo" type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO%>" value="<%=lMisuraSicurezza.getCodTipo()%>">
	    					<input Title="Descrizione Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DESCR_COD_TIPO %>" value="<%=lMisuraSicurezza.getDescrTipo()%>" readonly size="100">
						<% } else { %>
							<input Title="Codice Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>" value="-" readonly size="100">
						<% } %>
					</td>
				</tr>
				<tr>
					<td class="L" colspan="2">
						<font class="label">Durata: Anni</font>
						<% if (lMisuraSicurezza.getNumAnni() != null) { %>
	    					<input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI%>" value="<%=lMisuraSicurezza.getNumAnni()%>" readonly size="5">
						<% } else { %>
							<input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI%>" value="0" readonly size="5">
						<% } %>
						<font class="label">Mesi</font>
						<% if (lMisuraSicurezza.getNumMesi() != null) { %>
	    					<input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI%>" value="<%=lMisuraSicurezza.getNumMesi()%>" readonly size="5">
						<% } else { %>
							<input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI%>" value="0" readonly size="5">
						<% } %>
						<font class="label">Giorni</font>
						<% if (lMisuraSicurezza.getNumGiorni() != null) { %>
	    					<input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI%>" value="<%=lMisuraSicurezza.getNumGiorni()%>" readonly size="5">
						<% } else { %>
							<input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI%>" value="0" readonly size="5">
						<% } %>
					</td>
				</tr>
			</table>
			<%
			} // Fine elenco Misure Sicurezza
			%>

			<table cellspacing="2" cellpadding="2" width="95%" id="prorogamisura" style="display: none;">
				<tr>
					<td class="L" width="25%">
						<font class="label">Nuova Misura</font>
					</td>
					<td class="L">
   						<select title="Tipo Nuova Misura" name="codiTipoNuovaMisura">
   							<%=tipoMisuraSicurezza%>
   						</select>
       				</td>
       			</tr>
				<tr>
					<td class="L" colspan="2">
						<font class="label">Data Decorrenza</font>&nbsp;
						<input type="text" size="2" maxlength="2" name="giornoDataDecorrenzaNuovaMS" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiNuovaMS.getDataDecorrenza(), "dd"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
						<input type="text" size="2" maxlength="2" name="meseDataDecorrenzaNuovaMS" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiNuovaMS.getDataDecorrenza(), "MM"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
						<input type="text" size="4" maxlength="4" name="annoDataDecorrenzaNuovaMS" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(datiNuovaMS.getDataDecorrenza(), "yyyy"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<font class="label">Durata: Anni</font>&nbsp;
						<input title="Anni Nuova Misura" size="5" maxlength="2" type="text" name="anniDurataNuovaMS" value="<%=StringUtils.toStringJSP(datiNuovaMS.getNumAnni(), "0")%>" ONKEYPRESS="return TicTabNumField(this,event)">
						<font class="label">Mesi</font>&nbsp;
						<input title="Mesi Nuova Misura" size="5" maxlength="2" type="text" name="mesiDurataNuovaMS" value="<%=StringUtils.toStringJSP(datiNuovaMS.getNumMesi(), "0")%>" ONKEYPRESS="return TicTabNumField(this,event)">
						<font class="label">Giorni</font>&nbsp;
						<input title="Giorni Nuova Misura" size="5" maxlength="2" type="text" name="giorniDurataNuovaMS" value="<%=StringUtils.toStringJSP(datiNuovaMS.getNumGiorni(), "0")%>" ONKEYPRESS="return TicTabNumField(this,event)">
					</td>
				</tr>
			</table>
			<br>
			<%
			RedirectTo lRedir = new RedirectTo();
			lRedir.setPage(IWebConstants.PG_MAIN);
		 	lRedir.setParameter("TornaQui", "20");  // Link 20 indica Action Chiamante
		 	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
		 	%>
			<table cellspacing="2" cellpadding="2" width="95%">
				<tr>
					<td class="l">
					   	<font class="cRosso">
					   		<a class="cliccabile" href="<%=lRedir%>">
					      		Dettaglio Misura Sicurezza
					    	</a>
					  	</font>
					</td> 
				</tr>
			</table>
			<br>
			<table cellspacing="2" cellpadding="2" width="95%">
   				<jsp:include page="<%=ICostantiPrescrizione.PG_INCLUDE_PRESCRIZIONI%>">
      				<jsp:param name="EveIdEvento" value="<%=datiOrdinanza.getEvento().getIdEvento()%>" />
      				<jsp:param name="nextaction" value="siap.sius.depositoordinanzapc.action.ActLoadDettaglioOrdinanza"/>
   				</jsp:include>
   				<tr>
   					<td>
     					<input class="bottone" type="submit" value="Conferma" >
   					</td>
   				</tr>
			</table>
		    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
<%-- 		    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value=""> --%>
		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=datiOrdinanza.getOrdinanza().getCodTipoOrdinanza()%>">
<%-- 		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>"> --%>
		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=datiOrdinanza.getOrdinanza().getIdDepositoOrdinanzaPc()%>">
		    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=datiOrdinanza.getEvento().getIdEvento()%>">
<%-- 		    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value=""> --%>
<%-- 		    <input type="HIDDEN" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>" value=""> --%>
		</form>
  		<script language="JavaScript" type="text/javascript">
    		var frmvalidator = new Validator("ModificaOrdinanzaAppelloControProvvMS");
		    var nodeProrogaMisura = document.getElementById("prorogamisura");
		    frmvalidator.addValidation("giornoDataDecorrenzaNuovaMS","numeric");
		    frmvalidator.addValidation("meseDataDecorrenzaNuovaMS","numeric");
		    frmvalidator.addValidation("annoDataDecorrenzaNuovaMS","numeric");
		    frmvalidator.addValidation("annoDataDecorrenzaNuovaMS","minlen=4","La lunghezza del campo Anno della Data Decorrenza per la Misura di Sicurezza deve essere di 4 caratteri");
		    frmvalidator.setAddnlValidationFunction("Verify");
  		</script>
	</body>
</html>