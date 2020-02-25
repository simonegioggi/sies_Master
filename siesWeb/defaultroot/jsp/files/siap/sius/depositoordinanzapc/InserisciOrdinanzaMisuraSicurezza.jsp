<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>

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
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="misuresicurezza" 	scope="request" class="java.util.Vector" />
<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");

	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	// MERGE v10: per i maggiorenni la tabella "tableInForma" non deve essere visibile
	boolean isUffMinor = false;
	if ("TDSM".equals(CodUff) || "UDSM".equals(CodUff)) {
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
		isUffMinor = true;
	} else {
		labelUfficio = "Magistrato di Sorveglianza";
	}

	Integer sizeMisure = misuresicurezza.size();
	String numMisure = sizeMisure + "";
%>

<html>
	<head>
    	<title>[S.I.E.S.] - Emissione Ordinanza Misura Sicurezza</title>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    	<script language="JavaScript" src="/html/verifyCombo.js"></script>

  		<script language="JavaScript">
  			var desktop;

    		// STUB 21/07/2004 Controllo obbligatorietà esiti.
    		function Verify() {    			
      			var lEsiti = document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      			var ritorno = VerifyCombo(lEsiti,"Esito");
      			// SG: 20170421 modifica migliorativa per errore js
      			if (!ritorno)
      				return ritorno;
    			node = document.getElementById("nuovamisura");
      			if (node.style.visibility == 'visible') {
            		if (document.InserisciOrdinanzaMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA%>.value=='-') {
            			alert('Il campo Tipo Nuova Misura è obbligatorio');
              			return false;
            		}
    			}

      			nodetwo = document.getElementById("nuovamisuratwo");
      			if (nodetwo.style.visibility == 'visible') {
            		if (document.InserisciOrdinanzaMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA_TWO%>.value!='-' &&
            				document.InserisciOrdinanzaMisuraSicurezza.<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO%>.value=='-') {
            			alert('Il campo Tipo Nuova Misura è obbligatorio');
             			return false;
            		}
    			} 

        		// Controllo della data decorrenza
        		nodeDataDecorrenza=document.getElementById("dataDecorrenza");
        		if (nodeDataDecorrenza.style.visibility == 'visible') {
	        		var data_decorrenza=document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>.value;
	        		if (!ControllaData(data_decorrenza) && data_decorrenza.length > 2) {
			          	alert('Data Decorrenza per la Misura di Sicurezza non valida');
			          	return false;
	        		}
        		}

        		// 16/02/2015 Controllo presenza unica misura di sicurezza afferente all'ordinanza.
        		var numeroMisure = <%=numMisure%>;        		
        		// SG: 20170421 modifica migliorativa per errore js --> ci possono essere più esiti
        		if (numeroMisure > 1) {
        			// Size della Combo = 1
        		    if (typeof (lEsiti[0][0]) == "undefined") {
        		        if (lEsiti[lEsiti.selectedIndex].value in { '-':1, '1194':1, '1195':1, '1192':1, '1196':1, '2744':1 }) {
        		        	alert('Procedimento con più misure di sicurezza. Eliminare le misure superflue selezionando: Dettaglio Misure di Sicurezza');
        					return false;
        		        }
					} else {
        		        for (j = 0; j < lEsiti.length ; j++) {
        		        	for (i = 0; i < lEsiti[j].length ; i++) {
        		        		if ((lEsiti[j][i].selected) && (lEsiti[j][i].value in { '-':1, '1194':1, '1195':1, '1192':1, '1196':1, '2744':1 })) {
        		        			alert('Procedimento con più misure di sicurezza. Eliminare le misure superflue selezionando: Dettaglio Misure di Sicurezza');
        							return false;
        		            	}
        		          	}
						}
					}
				}
        		if (numeroMisure == 0  &&
        				document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value != 'U023') {
        			// Size della Combo = 1
        		    if (typeof (lEsiti[0][0]) == "undefined") {
        		        if (lEsiti[lEsiti.selectedIndex].value in { '-':1, '1194':1, '1195':1, '1192':1, '1196':1, '2744':1 }) {
        		        	alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        					return false;
        		        }
					} else {
        		        for (j = 0; j < lEsiti.length ; j++) {
        		        	for (i = 0; i < lEsiti[j].length ; i++) {
        		        		if ((lEsiti[j][i].selected) && (lEsiti[j][i].value in { '-':1, '1194':1, '1195':1, '1192':1, '1196':1, '2744':1 })) {
        		        			alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        							return false;
        		            	}
        		          	}
						}
					} 
        		}

        		// 20/02/2015 Controllo presenza misura di sicurezza afferente all'ordinanza.
        		//@emma 16072018 intervento post COLLAUDO 11.2 (la misura di sicurezza è obbligatoria solo per alcuni codici esito)
        		if (numeroMisure == 0 &&
        				document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U023') {        			
        			// Size della Combo = 1        			
        		    if (typeof (lEsiti[0][0]) == "undefined") {        		    	 
        		        if (lEsiti[lEsiti.selectedIndex].value in {'-':1, '1190':1, '1191':1,'1198':1, '1990':1, '1991':1, '1992':1, '1993':1, '1206':1, '2720':1}) {
        		        	alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        					return false;
        		        }
					} else {
        		        for (j = 0; j < lEsiti.length ; j++) {
        		        	for (i = 0; i < lEsiti[j].length ; i++) {
        		        		if ((lEsiti[j][i].selected) && (lEsiti[j][i].value in {'-':1, '1190':1, '1191':1,'1198':1, '1990':1, '1991':1, '1992':1, '1993':1, '1206':1, '2720':1})) {
        		        			alert("Procedimento privo di misura di sicurezza: procedere all'inserimento selezionando: Dettaglio Misure di Sicurezza");
        							return false;
        		            	}
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
      		function AbilitaNuovaMisura() {
      			nodeTableInforma = document.getElementById("tableInforma");
	   			nodeTableInforma.style.visibility = 'hidden';
	   			node = document.getElementById("nuovamisura");
	   			node.style.visibility = 'hidden';
	   			nodetwo = document.getElementById("nuovamisuratwo");
	   			nodetwo.style.visibility = 'hidden';
	   			nodeDecorrenza = document.getElementById("dataDecorrenza");
	   			nodeDecorrenza.style.visibility = 'hidden';
	   			var esito = 0;
	   			if (typeof (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
	   				for (j = 0; j < document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
	   					if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
	   						if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '1198':1, '1206':1 }) {
         						node.style.visibility = 'visible';
       						}

       						if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2008':1 }) {
					     		node.style.visibility = 'visible';
					   			document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA%>.value = '04';
					   			document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA%>.value = '01'; 
       						}

				       		if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '1190':1, '1198':1, '1206':1, '1990':1 }) {
				       			if (<%= isUffMinor %>)
				       				nodeTableInforma.style.visibility = 'visible';
				       		}

				       		if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '1990':1 }) {
				         		node.style.visibility = 'visible';
				       			nodetwo.style.visibility = 'visible';
				       		}

							// Data Decorrenza della Misura di sicurezza
							// viene visualizzata solo in corrispondenza di:
							// inserimento Emissione "Inosservanza delle misure di sicurezza detentive"
							// oggetto "Inosservanza delle Misure di Sicurezza  Detentive (art. 214 c.p.)"
							// ed Esito "Dispone che ricominci a decorrere il periodo minimi della misura"
				       		if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2004':1, '2008':1 }) {
				         		nodeDecorrenza.style.visibility = 'visible';
				       		}
       					}
   	   				} // fine ciclo for
	 			} // Fine caso singolo oggetto
	 			// Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
     			else {
       				for (j = 0; j < document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
						for (i = 0; i < document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
							if ((document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) &&
       								(document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '1198':1, '1206':1 })) {
				         		node.style.visibility = 'visible';
           					}

				           	if ((document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) &&
				        		   (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '2004':1, '2008':1 })) {
				         		nodeDecorrenza.style.visibility = 'visible';
				       	   	}

				           	if ((document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) &&
				        		   (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '1190':1 })) {
				         		node.style.visibility = 'visible';
				       			nodetwo.style.visibility = 'visible';
				           	}
				
				           	if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '1190':1, '1198':1, '1206':1,'1990':1 }) {
				           		if (<%= isUffMinor %>)
				      				nodeTableInforma.style.visibility = 'visible';
				      		}
				
				           	if ((document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) &&
				           			(document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in { '2008':1 })) {
				         		node.style.visibility = 'visible';
				       			document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA%>.value = '04'; 
				          	}
						}
					}
				} // Fine caso più oggetti
			}

      		// MERGE v10: aggiunta funzione per abilitare il campo descrittivo
      		function abilitaDisabilitaComunita() {
      			if (document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>[1].checked == true)
      				document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>.disabled = false;
    			else
      				document.InserisciOrdinanzaMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>.disabled = true;
      		}
   		</script>
 	</head>
 	<%
  	String lAction = new String();
  	lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 	%>

  	<body class="corpo">
    	<table>
    		<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
				<td class=LBG><font class="label">Funzione: </font> <font class="campo">Emissione Ordinanza Misura Sicurezza</font></td>
    		</tr>
		    <tr>
		    	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
		    </tr>
    	</table>

  		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaMisuraSicurezza">
    		<table width=35%>
				<tr>
				  	<td class="l" width=30%> Data Emissione</td>
				  	<td class="l" width=70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
				</tr>
    		</table>
    		<tr> <td>&nbsp;</td> </tr>
     		<table cellspacing="2" cellpadding="2" width=80%>
    			<tr>
        			<td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    			</tr>
			    <tr>
			        <td class="l" colspan=2 > Oggetto </td>
			        <td class="l" colspan=2 > Esito </td>
			    </tr>
    			<%
   				for (int i=0; i< tenori.length;i++) {
   				%>
       			<tr>
        			<td class="l"colspan=2 >
						<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size=60%>
						<input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>">
						<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
        			</td>
          			<td class="l"colspan=2 >
           				<select Title="Cod Esito" class=small name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript: AbilitaNuovaMisura();">
             				<%=esiti[i]%>
          				</select>
        			</td>
      			</tr>
    			<%
    			}
    			%>
    		</table>
			<br>
 			<table cellspacing="2" cellpadding="2" width="90%">
		 		<tr>
					<td class="l">Ulteriore descrizione della decisione</td>
		    		<td class="l">
		    			<TEXTAREA title="Ulteriore Descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea>
		    		</td>
				</tr>
 				<%
				int indice = -1;
		    	Iterator itx = misuresicurezza.iterator();
		    	while (itx.hasNext()) {
					indice++;
    				MisuraSicurezzaModel lMisuraSicurezza = (MisuraSicurezzaModel)itx.next();
				%>
        			<input type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=lMisuraSicurezza.getIdMisuraSicurezza()%>" >
				<%
					String lTitoloMisuraSicurezza = ""; 
					if (lMisuraSicurezza.getFasSieIdFascicoloSiep() != null)
						lTitoloMisuraSicurezza = "Misura di Sicurezza ereditata da SIEP";
					else
						lTitoloMisuraSicurezza = "Misura di Sicurezza inserita dall' UDS";
				%>
					<table cellspacing="2" cellpadding="2" width="90%">
						<tr>
        					<td class="Titolo" colspan=6 > <%=lTitoloMisuraSicurezza%></td>
   						</tr>
  						<tr>
							<td>
								<font class="label">Natura</font>
								<% if (lMisuraSicurezza.getCodNatura() != null && lMisuraSicurezza.getDescrNatura() != null) { %>
									<%-- MERGE v10: aggiunto campo nascosto --%>
									<input Title="Codice Natura" type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA%>" value="<%=lMisuraSicurezza.getCodNatura()%>">
			    					<input Title="Descrizione Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DESCR_COD_NATURA%>" value="<%=lMisuraSicurezza.getDescrNatura()%>" readonly size=15%>
								<% } else { %>
									<input Title="Codice Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>" value="-" readonly size=15%>
								<% } %>
							</td>
							<td>
								<font class="label">Tipo</font>
								<% if (lMisuraSicurezza.getCodTipo() != null && lMisuraSicurezza.getDescrTipo() != null) { %>
									<%-- MERGE v10: aggiunto campo nascosto --%>
									<input Title="Codice Tipo" type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO%>" value="<%=lMisuraSicurezza.getCodTipo()%>">
			    					<input Title="Descrizione Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DESCR_COD_TIPO %>" value="<%=lMisuraSicurezza.getDescrTipo()%>" readonly size=43%>
								<% } else { %>
									<input Title="Codice Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>" value="-" readonly size=43%>
								<% } %>
							</td>
							<td>
								<font class="label">Durata: Anni</font>
								<% if (lMisuraSicurezza.getNumAnni() != null) { %>
			    					<input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI %>" value="<%=lMisuraSicurezza.getNumAnni()%>" readonly size=5%>
								<% } else { %>
									<input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI %>" value="0" readonly size=5%>
								<% } %>
							</td>
							<td>
								<font class="label">Mesi</font>
								<% if (lMisuraSicurezza.getNumMesi() != null) { %>
			    					<input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI %>" value="<%=lMisuraSicurezza.getNumMesi()%>" readonly size=5%>
								<% } else { %>
									<input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI %>" value="0" readonly size=5%>
								<% } %>
							</td>
							<td>
								<font class="label">Giorni</font>
								<% if (lMisuraSicurezza.getNumGiorni() != null) { %>
			    					<input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI %>" value="<%=lMisuraSicurezza.getNumGiorni()%>"   readonly size=5%>
								<% } else { %>
									<input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI %>" value="0" readonly size=5%>
								<% } %>
							</td>
						</tr>
					</table>
				<%
				} // Fine elenco Misure Sicurezza
				%>
				<table cellspacing="2" cellpadding="2" width="90%" id="nuovamisura" style="visibility: hidden">
					<tr>
	        			<td class="Titolo" colspan=6 > Descrizione Nuova Misura di Sicurezza</td>
	    			</tr>
  					<tr>
  						<% MisuraSicurezzaModel lNuovaMisuraSicurezza = new  MisuraSicurezzaModel(); %>
						<td>
							<font class="label">Natura </font>
          					<select title="NaturaNuovaMisura" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA %>">
          						<%=naturaMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Tipo</font>
          					<select title="TipoNuovaMisura" class=small name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA %>">
          						<%=tipoMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Durata: Anni</font>
          					<input title="Anni Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumAnni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA %>">
						</td>
						<td>
							<font class="label">Mesi</font>
          					<input title="Mesi Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumMesi()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA %>">
						</td>
						<td>
							<font class="label">Giorni</font>
          					<input title="Giorni Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumGiorni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA %>">
						</td>
					</tr>
  				</table>
				<table cellspacing="2" cellpadding="2" width="90%" id="nuovamisuratwo" style="visibility: hidden">
  					<tr>
						<td>
							<font class="label">Natura </font>
       						<select title="NaturaNuovaMisuraTwo" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA_NUOVA_MISURA_TWO %>" >
          						<%=naturaMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Tipo</font>
          					<select title="TipoNuovaMisuraTwo" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_NUOVA_MISURA_TWO %>" >
          						<%=tipoMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Durata: Anni</font>
          					<input title="Anni Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumAnni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI_NUOVA_MISURA_TWO %>"  >
						</td>
						<td>
							<font class="label">Mesi</font>
          					<input title="Mesi Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumMesi()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI_NUOVA_MISURA_TWO %>"  >
						</td>
						<td>
							<font class="label">Giorni</font>
          					<input title="Giorni Nuova Misura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumGiorni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI_NUOVA_MISURA_TWO %>"  >
						</td>
					</tr>
  				</table>
				<table cellspacing="2" cellpadding="2" width="90%" id="dataDecorrenza" style="visibility: hidden">
  					<tr>
				        <td class="L">Data Decorrenza per la Misura di Sicurezza</td>
				        <td class="L">
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lNuovaMisuraSicurezza.getDataDecorrenza(),"dd")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				          /
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lNuovaMisuraSicurezza.getDataDecorrenza(),"MM")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				          /
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lNuovaMisuraSicurezza.getDataDecorrenza(),"yyyy")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
				        </td>
					</tr>
  				</table>
				<tr> <td>&nbsp;</td> </tr>
				<%
				RedirectTo lRedir = new RedirectTo();
				lRedir.setPage(IWebConstants.PG_MAIN);
			 	lRedir.setParameter("TornaQui", "20");  // Link 20 indica Action Chiamante
			 	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
			 	%>
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
			<tr> <td>&nbsp;</td> </tr>
			<table cellspacing="2" cellpadding="2" width="90%">
		    	<tr>
		      		<td class="l"><%=labelUfficio%> Competente</td>
		      		<td class="l">
		        		<input Title="<%=labelUfficio%>" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=15% >
	        			<a href="Javascript: ListaUDS('InserisciOrdinanzaMisuraSicurezza','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
      						<img src="/images/filefolder.gif" border=0>
   						</a>
       				</td>
   				</tr>
				<tr> <td>&nbsp;</td> </tr>
				<tr>
				  	<td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
				</tr>
				<tr> <td>&nbsp;</td> </tr>
		      	<tr>
			        <td>
			        	<table id="tableInForma" style="visibility:hidden">
			        		<tr>
			        			<td class="l" width="50%">Indicare se la misura deve essere eseguita nelle forme della </td>
			        			<td class="l" width="40%"><input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>" onclick="Javascript: abilitaDisabilitaComunita();"> Permanenza in casa </td>
			        			<td width="10%">&nbsp;</td>
			        		</tr>
			        		<tr>
			        			<td width="50%">&nbsp;</td>
			        			<td width="40%" class="l"><input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>" onclick="Javascript: abilitaDisabilitaComunita();">Collocamento in Comunità</td>
			        			<td width="10%" class="l"><input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>" disabled="disabled"></td>
		        			</tr>
						</table>
					</td>
   				</tr>
				<tr> <td>&nbsp;</td> </tr>
   				<tr>
   					<td>
     					<input class="bottone" type="submit" value="Conferma" >
   					</td>
   				</tr>
			</table>
		    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
		    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
		    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">
		</form>
  		<script language="JavaScript" type="text/javascript">
    		var frmvalidator = new Validator("InserisciOrdinanzaMisuraSicurezza");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>","minlen=4","La lunghezza del campo Anno della Data Decorrenza per la Misura di Sicurezza deve essere di 4 caratteri");
    		frmvalidator.setAddnlValidationFunction("Verify");
  		</script>
	</body>
</html>