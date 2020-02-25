<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel"%>
<%@ page import="siap.siep.notiziareato.action.ICostantiNotiziaReato"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IdIstruttoriaCumulo" scope="request" class="java.lang.String"/>
<jsp:useBean id="sededi"   scope="request" class="java.lang.String"/>
<jsp:useBean id="questure"   scope="request" class="java.lang.String"/>
<jsp:useBean id="datairrevocabilita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="elenconotiziareato" scope="request" class="java.util.Vector" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="IdRegeFile" scope="request" class="java.lang.String" />


<html>
	<head>
    	<title>[S.I.E.S.] - Gestione Richiesta Codice CUI </title>
    	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    	<script language="JavaScript">

			// Funzione popup Lista Comuni
		    var desktop;
		    function ListaComuni(a_formname,a_fieldname)
		    {
		     desktop = window.open("<%=IWebConstants.ROOT_DIR%><%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
		    }

			function Verify()
			{
				// Controllo che sia stato scelto almeno uno dei destinatari
				if(document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_GABINETTO%>.checked!=true 
					&& document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_QUESTURA%>.checked!=true){
					alert('Selezionare almeno un destinatario');
					return false;
				}
				
				if(document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[0].value == "" &&
        document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[1].value == ""){
					alert('Il campo sede è obbligatorio');
					return false;
				}
				
				// Controllo lunghezza dei campi giorno e mese delle date
				if (document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
					document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
				if (document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
					document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

				var data_to_verify = document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

				if (!ControllaData(data_to_verify) )
				{
					alert('Data Richiesta non valida');
					return false;
				}

				// Controllo che la Data Emissione non sia Minore della data Irrevocabilità del Procedimento
    if(!CompareDate("<%=datairrevocabilita%>",data_to_verify))
				{
					alert("La Data Richiesta del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
				  	document.LoadInserisciRichiestaCodiceCui.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
				  	return false;
				}

				return true;
			}
			
function hideSede()
   {
        var nodeSebeG;
        var nodeSebeQ;
        nodeSebeG=document.getElementById('sediGabinetto');
        nodeSebeQ=document.getElementById('sediQuestura');
      
        if(document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_QUESTURA %>.checked
        && !document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_GABINETTO %>.checked)
        {
          nodeSebeQ.style.display='block';
          nodeSebeG.style.display='none';
          document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[0].value = "";
        }
        else if (document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_GABINETTO%>.checked )
        {
          nodeSebeG.style.display='block';
          nodeSebeQ.style.display='none';
          document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[1].value = "";
        }
         else if (!document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_QUESTURA %>.checked
        && !document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_GABINETTO %>.checked)
        {
          nodeSebeG.style.display='none';
          nodeSebeQ.style.display='none';
          document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[0].value = "";          
        }
        
}

function pulisciSede()
   {
        var nodeSebeG;
        var nodeSebeQ;
        nodeSebeG=document.getElementById('sediGabinetto');
        nodeSebeQ=document.getElementById('sediQuestura');
      
        document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_QUESTURA %>.checked = false;
        document.LoadInserisciRichiestaCodiceCui.<%=ICostantiIstruttoria.FLAG_GABINETTO %>.checked =false;
        
        nodeSebeQ.style.display='none';
        nodeSebeG.style.display='none';
        document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[0].value = "";
        document.LoadInserisciRichiestaCodiceCui.SedeAutoritaDestinatario[1].value = "";
       
}
		</script>
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>

	<body class="corpo" onLoad="javascript:pulisciSede()" >
	<table>
	 	<tr>
	 		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	   		<td class="LBG">
	   			<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo"> Richiesta Codice CUI e Cartellino Dattiloscopico</font>
	   		</td>
		</tr>
	</table>
	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
  	<FORM method="POST" name="LoadInserisciRichiestaCodiceCui" action="<%= IWebConstants.PG_MAIN%>">
  		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciRichiestaCodiceCui">
  		<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>"	value="<%= IdIstruttoriaCumulo%>" >
		<table>
 			<tr><td class="Titolo" colspan=6>Destinatari  </td></tr>
 			<tr>
 				<td class="l" colspan="2">
 					<input value="96" type="HIDDEN" name="<%=ICostantiIstruttoria.AUT_QUESTURA_DIVISIONE_ANTICRIMINE%>">
 					<input type="checkbox" 
 						value="Questura Divisione Anticrimine"
 						name="<%=ICostantiIstruttoria.FLAG_QUESTURA %>"  onClick="javascript:hideSede();">&nbsp;
 					Questura Divisione Anticrimine 					
 					<br>
 					<input value="91" type="HIDDEN" name="<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>">
 					<input type="checkbox" 
 						value="Gabinetto di Polizia Scientifica"
 						name="<%=ICostantiIstruttoria.FLAG_GABINETTO%>" onClick="javascript:hideSede();">&nbsp;
 					Gabinetto di Polizia Scientifica
 				</td>
 			</tr> 			
    		<tr>
				<td class="l">Sede di <font class="ob">(*)</font></td>
				<td>
					<div id="sediGabinetto" style="display:none; width:100%;">
						<table  width=100% >
							<tr>
								<td class="L">
								<select title="Sede di" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>">
									<%=sededi%>
								</select>
								</td>
							</tr> 
						</table>
					</div>
					<div id="sediQuestura" style="display:none; width:100%;">
						<table  width=100% >
						<tr><td class="L">
							<select title="Sede di" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>">
								<%=questure%>
							</select>					
						</td></tr>
						</table>
					</div>		
				</td>
    		</tr>    				
    		<tr>
				<td class="l">Indirizzo Destinatario </td>
				<td class="L">
					<input title = "Indirizzo Destinatario" value="" type="text" size="30" 
					name="<%= ICostantiIstruttoria.INDIRIZZO_DESTINATARIO %>" >
				</td>
			</tr> 
    		<tr><td class="Titolo" colspan=6>Oggetto</td></tr>
   			<%
   			boolean flag_notizie=false;
			if(elenconotiziareato.size()>0)flag_notizie=true;
			%>
    		<tr>
				<td class="l" colspan="2">
					<table>
		    		<tr>
						<td class=c 
							<% if(flag_notizie){%>
							colspan=10
							<%}%>
							>
							NOTIZIE DI REATO&nbsp;
							<script>
							function newNR(){
								document.location.href('<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.notiziareato.action.ActLoadInserisciNotiziaReato&<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>=<%=AzioneChiamante%>');
							}
							</script>
							<br>
							<a href="#" onclick="newNR()">Inserisci Notizia di Reato</a>
						</td>
					</tr>
				
			<%	//boolean flag_notizie=false;
				//if(elenconotiziareato.size()>0){
				//	flag_notizie=true;
				if(flag_notizie){
				%>	
		    		<tr>		    		
		    			<td class="int">
		    				Num. Reg. Autorità
		    				<!-- campo controllo -->
		    				<input name="flag_notizie" type="hidden" value="1">
		    			</td>
		    			<td class="int">Descr. Fonte</td>
		    			<td class="int">Luogo provenienza</td>
		    			<td class="int">Comune Fonte</td>
		    			<td class="int">Fotosegnalato</td>
		    			<td class="int">Data Arresto</td>	
		    			<td class="int">Azioni</td>			    			
		    		</tr>
		    		<%
					String notizie = "";
					Iterator itx = elenconotiziareato.iterator();
					while ( itx.hasNext())
					{
						notizie = "";
						NotiziaReatoModel nreato = (NotiziaReatoModel)itx.next();
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getNumRegAutorita()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getDescrizioneFonte()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getLuogoProvenienza()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getDescrComuneFonte()) + "&nbsp;</td>";
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(nreato.getFlagFotosegnalato()) + "&nbsp;</td>";	
						notizie += "<td class=\"c\">&nbsp;" + StringUtils.toStringJSP(DateUtils.getDateToString(nreato.getDataArresto(), "dd-MM-yyyy")) + "&nbsp;</td>";			
					%>
						<tr>
							<%=notizie%>
							<td class="l">
								<!-- Inserimento bottoni per la visualizzione, la modifica e la cancellazione -->
		        				<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
			           				<jsp:param name="CampoIdEntita" value="<%=ICostantiNotiziaReato.CAMPO_ID_NOTIZIA_REATO%>" />
									<jsp:param name="ValoreIdEntita" value="<%=nreato.getIdNotiziaReato()%>" />
									<jsp:param name="CampoIdEntitaProvv" value="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" />						
									<jsp:param name="ValoreIdEntitaProvv" value="<%=AzioneChiamante %>" />
		        				</jsp:include>
	      					</td>
      					</tr>
						<%						
					}
				}
				else{%>
					<tr>
						<td class="int" width="1000">						
							NESSUNA NOTIZIA DI REATO INSERITA
						</td>
					</tr>
				<%	
				}
				%>
	    	</table>
		    	<%if(IdRegeFile!=null && IdRegeFile.length()>0)
				{				//Ci sono notizie di reato su ReGe
				
				  //http://siapstress:8080/jsp/Main.jsp?Action=siap.regesies.regesentenza.action.ActDettaglioProvvedimento&IdFile=015UNP00G003506A28112007151239
				%>
   		<table align="center" style="border: 2;">
   		<tr><td class="c" bgcolor=yellow>
   		Esistono delle Notizie di Reato provenienti dal sistema Rege ancora non importatate su questo procedimento.<br>
   		Importare le Notizie di reato tramite questo link: <br>
   		<table align="center" style="border: 2;"><tr><td class="c" bgcolor=yellow>
   		<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesentenza.action.ActDettaglioProvvedimento&IdFile=<%=IdRegeFile%>">Importa Notizie di Reato da Rege</a>				
		</td></tr>	
		</table></td></tr>	
		</table>		
	<% }%>
				</td>
    		</tr>
    		<tr>
				<td class="l">	Annotazioni 
					<%if(!flag_notizie){%>
						<font class="ob">(*)</font>
					<%}%>
				</td>
			    <td class="L" >
					<TEXTAREA title="Annotazioni" name="<%= ICostantiIstruttoria.CAMPO_NOTE %>" cols="60" rows="5"></textarea>
			    </td>
    		</tr>
    		
			<tr>
				<td class="l">Data Richiesta </td>
				<td class="L">
					<input title = "Giorno Data Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> >
					/
					<input title = "Mese Data Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> >
					/
					<input title = "Anno Data Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
				</td>
			</tr>
			<tr>
			<tr>
				<td class="Titolo" colspan=6>Scelta Documento da Stampare  </td>
			</tr>
			<tr>
		    	<td class="l" colspan=5>
		    		Richiesta Codice CUI
		    		<input type="radio" checked  name="ListaTemplate" value="0"/>
				</td>
		    </tr>
		    <tr>
		    	<td class="l" colspan=5>
		    		Richiesta Cartellino Fotosegnaletico
		    		<input type="radio" name="ListaTemplate" value="1"/>
				</td>
		    </tr>
		    <tr>
				<td class="lNoBord" colspan="2">
					<br><br>
					<INPUT class="bottone" type="submit" name="I" value="Conferma">
					<input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>				
				</td>
			</tr>			
		</table>

	</form>
	<script language="JavaScript" type="text/javascript">

		var frmvalidator  = new Validator("LoadInserisciRichiestaCodiceCui");

		// AUTORITA' DESTINATARIO
		//frmvalidator.addValidation("< %= ICostantiIstruttoria.AUTORITA_DESTINATARIO%>","req","Il campo Autorità Destinatario è obbligatorio");

		// LUOGO DESTINAZIONE
		//frmvalidator.addValidation("<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>","req");

		// ANNOTAZIONI (già NOTIZIE DI REATO)
		<%if(!flag_notizie){%>frmvalidator.addValidation("<%= ICostantiIstruttoria.CAMPO_NOTE%>","req");<%}%>
		frmvalidator.addValidation(
			"<%= ICostantiIstruttoria.CAMPO_NOTE%>",
			"maxlen=2000", "Il Campo Annotazioni non può superare i 2000 caratteri");

		// DATA RICHIESTA
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Data Richiesta è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Data Richiesta è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Data Richiesta è obbligatorio");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
		frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");

		// RICHIAMA LA FUNZIONE JS VERIFY PER GLI ALTRI CONTROLLI
		frmvalidator.setAddnlValidationFunction("Verify");

	</script>

	</body>
</html>