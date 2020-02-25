<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: nuova pagina di inserimento rinvio esec MS --%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.provvedimento.action.ICostantiProvvedimento"%>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     scope="request" class="java.util.Date"/>

<%
TenoreModel[] tenori = (TenoreModel[]) request.getAttribute("tenori");
String[] esiti = (String[]) request.getAttribute("esiti");
%>

<html>
  	<head>
	    <title>[S.I.E.S.] - Emissione Decreto Rinvio MS</title>
	    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  		<script language="JavaScript">
    	function Verify() {
      		var lEsiti = document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      		var ritorno = VerifyCombo(lEsiti, "Esito");  
      		if (ritorno)
      			ritorno = ControlliDate();
      		node = document.getElementById("datarinvio");
      		if (node.style.display == 'none') {
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value = "";
      			document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value = "";
      		}
   			return ritorno;
    	}
    	function ControlliDate() {
    		var ritorno = true;
			node = document.getElementById("datarinvio");
  			if (node.style.display == 'block') {
				var data_sospensione = document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>.value;
				var data_scadenza_sospensione = document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>.value;
				var durata_periodo = document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value+'/'+document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value;
	    		if (data_sospensione > 2) {
	    			ritorno =  ControllaData(data_sospensione);
	  			}
        		if (!ritorno) {
        			// MEV_39: modifica etichetta per U082 e U077
					if(document.InserisciDecretoRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U082' ||
					   document.InserisciDecretoRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U077'){
						alert("Data Decorrenza Differimento Esecuzione non valida");
        			} else { 
        				alert("Data Differimento Esecuzione non valida");
                	}
					document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.focus();
					return false;
        		} else {
		            if (data_scadenza_sospensione.length > 2) {
		            	ritorno = ControllaData(data_scadenza_sospensione);
	          			if (!ritorno) {
			           		alert("Data Rinvio fino al non valida");
			           		document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>.focus();
			           		ritorno = false;
	           			} else {
	           		 		if (CompareDate(data_scadenza_sospensione, data_sospensione)) {
	           		 			// MEV_39: modifica etichetta per U082 e U077
	           		 			if(document.InserisciDecretoRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U082' ||
	           		 			   document.InserisciDecretoRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U077'){
			            			alert("La Data Decorrenza Differimento Esecuzione deve precedere la Data Rinvio fino al");
	           		 			} else {  
	           		 				alert("La Data Differimento Esecuzione deve precedere la Data Rinvio fino al");
		           		 		}
			            		document.InserisciDecretoRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>.focus();
			            		ritorno = false;
	            			}
	            		}
	        		}
        		}
   			}
     		return ritorno;
		}
        function AbilitaCampiEsiti() {
        	var node = document.getElementById("datarinvio");
        	var ric = document.getElementById("ricovero");
     	   	if (typeof (document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
     	   		for (j = 0; j < document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
     	   			if ( document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
     	   				if (document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1}
     	   						|| document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1}) {
              				node.style.display = 'block';
              				ric.style.display = 'none';
              				if (document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
              					ric.style.display = 'block';
            			}
            			if (!(document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1})
            					&& !(document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
              				node.style.display = 'none';
              				ric.style.display = 'none';
            			}
            		}
        		} // fine ciclo for
     	 	} // Fine caso singolo oggetto
     		// Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
          	else {
      			node.style.display = 'none';
      			ric.style.display = 'none';
      			for (j = 0; j < document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
      				for (i = 0; i < document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
      					if ((document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected)
      							&& (document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in {'1836':1, '1900':1, '0259':1}
      									|| document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
              				node.style.display = 'block';
              				ric.style.display = 'none';
              				if (document.InserisciDecretoRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
              					ric.style.display = 'block';
                		}
              		}
            	}
			} // Fine caso più oggetti
		}
 		</script>
 	</head>
<%
String lAction = new String();
lAction = "siap.sius.depositodecreto.action.ActInserisciEmissioneDecretoDeposito";
%>
	<body class="corpo">
    	<table>
    		<tr>
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
					</a>
				</td>
				<td class=LBG>
					<font class="label">Funzione: </font>
					<font class="campo">Emissione Decreto Rinvio Esecuzione Misura Sicurezza</font>
				</td>
    		</tr>
    		<tr>
       			<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    		</tr>
		</table>

  		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciDecretoRinvioMS">
    		<table width="35%">
	  			<tr>
	    			<td class="l" width="30%">Data Emissione</td>
	    			<td class="l"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
	  			</tr>
    		</table>
			<table cellspacing="2" cellpadding="2" width="90%">
    			<tr><td>&nbsp;</td></tr>
    			<tr>
        			<td class="Titolo" colspan="6">Specificare esito per ciascun oggetto:</td>
    			</tr>
    			<tr>
				    <td class="l" colspan="2">Oggetto</td>
				    <td class="l" colspan="2">Esito</td>
				</tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
       			<tr>
        			<td class="l"colspan="2">
						<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="60%">
						<input Title="Cod Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
						<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
        			</td>
          			<td class="l"colspan="2">
           				<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();">
             				<%=esiti[i]%>
          				</select>
       				</td>
      			</tr>
<%
}
%>
			</table>
    		<table cellspacing="2" cellpadding="2" width="90%">
 				<tr>
					<td class="l">Ulteriore descrizione della decisione</td>
    				<td class="l">
    					<TEXTAREA title="Ulteriore descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4"></TEXTAREA>
   					</td>
				</tr>
 			</table>
			<br>
			<table cellspacing="2" cellpadding="2" width="100%" id="datarinvio" style="display: none;">
  				<tr>
    				<td class="l" colspan="3">In caso di sospensione indicare:<td>
  				</tr>
    			<tr>
<%-- MEV_39: modifica etichetta per U082 e U077--%>
<%
				if (contenuto != null && !contenuto.equals("") && (contenuto.equals("U082") || contenuto.equals("U077"))){
%>
					<td class="l">Data Decorrenza Differimento Esecuzione</td>
<%
				} else {
%>
      				<td class="l">Data Differimento Esecuzione</td>
<%
				}
%>
      				<td class="L" colspan="2">
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
						<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      				</td>
     			</tr>
  	 			<tr>
      				<td class="l">Rinvio fino al</td>
      				<td class="L">
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
						<input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
						<input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      				</td>
      				<td class="l">oppure Rinvio nella misura di: 
      					Anni
        				<input value="" title="Numero Anni sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>"> 
       					Mesi
        				<input value="" title="Numero Mesi sospensione" type="text" size="3" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>"> 
        				Giorni
        				<input value="" title="Numero Giorni sospensione" type="text" size="4" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>">
      				</td>
    			</tr>
    			<tr id="ricovero" style="display: none;">
          			<td class="l">Luogo Ricovero</td>
          			<td class="L" colspan="2">
           				<TEXTAREA title="Note" name="<%=ICostantiDepositoDecreto.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" cols="80" rows="4"></TEXTAREA>
          			</td>
      			</tr>
			</table>
			<table cellspacing="2" cellpadding="2" width="90%">
				<tr><td>&nbsp;</td></tr>
    			<tr>
    				<td><input class="bottone" type="submit" value="Conferma"></td>
    			</tr>
			</table>
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
			<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
			<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>">
			<input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%>>
  		</form>
	  	<script language="JavaScript" type="text/javascript">
		    var frmvalidator = new Validator("InserisciDecretoRinvioMS");
		    frmvalidator.setAddnlValidationFunction("Verify");
		    frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>","numeric");
		    frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>","numeric");
			// Data fine Rinvio
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","gt=1");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SCADENZA_SOSPENSIONE_SS%>","lt=31");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","gt=1");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SCADENZA_SOSPENSIONE_SS%>","lt=12");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SCADENZA_SOSPENSIONE_SS%>","gt=1900");
			// Data inizio sospensione
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","gt=1");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_GIORNO_SOSPENSIONE_SS%>","lt=31");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","gt=1");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_MESE_SOSPENSIONE_SS%>","lt=12");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>","numeric");
			frmvalidator.addValidation("<%=ICostantiDepositoDecreto.CAMPO_ANNO_SOSPENSIONE_SS%>","gt=1900");
	  	</script>
	</body>
</html>