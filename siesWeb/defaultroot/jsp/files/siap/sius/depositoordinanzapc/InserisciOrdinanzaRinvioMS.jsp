<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
    <title>[S.I.E.S.] - Emissione Ordinanza Rinvio MS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  	<script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify() {
      	var lEsiti = document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      	var ritorno = VerifyCombo(lEsiti, "Esito");  
      	if (ritorno)
      		ritorno = ControlliDate();
      	<%-- MEV_39: aggiunti controlli --%>
      	node = document.getElementById("datarinvio");
  		if (node.style.display == 'none') {
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value = "";
  			document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value = "";
  		}
      	return ritorno;
    }

    function ControlliDate() {
    	var ritorno = true;
    	<%-- MEV_39: aggiunti controlli --%>
		node = document.getElementById("datarinvio");
  		if (node.style.display == 'block') {
     		var data_inizio_periodo = document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>.value;
     		var data_termine_periodo = document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
     		var durata_periodo = document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>.value+'/'+document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>.value;
		    if (data_inizio_periodo > 2) {
		    	ritorno = ControllaData(data_inizio_periodo);
		  	}
	        if (!ritorno) {
	        	// MEV_39: modifica etichetta per U082 e U077
	        	if(document.InserisciOrdinanzaRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U082' ||
	        	   document.InserisciOrdinanzaRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U077'){
	        		alert("Data Decorrenza Differimento Esecuzione non valida");
	        	} else { 
	        		alert("Data Differimento Esecuzione non valida");
		        } 
		        document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
	        	return false;
	        } else {
	            if (data_termine_periodo.length > 2) {
			        ritorno = ControllaData(data_termine_periodo);
		          	if (!ritorno) {
		           		alert("Data Rinvio fino al non valida");
		           		document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.focus();
		           		ritorno = false;
		           	} else {
		           		if (CompareDate(data_termine_periodo, data_inizio_periodo)) {
		           		    // MEV_39: modifica etichetta per U082 e U077
		           			if(document.InserisciOrdinanzaRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U082' ||
		           			   document.InserisciOrdinanzaRinvioMS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value == 'U077'){
		            			alert("La Data Decorrenza Differimento Esecuzione deve precedere la Data Rinvio fino al");
		           			} else { 
		           				alert("La Data Differimento Esecuzione deve precedere la Data Rinvio fino al");
			           		} 
		            		document.InserisciOrdinanzaRinvioMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>.focus();
		            		ritorno =  false;
		            	}
		            }
		        }
	        }
		}
		return ritorno;
	}

	function AbilitaCampiEsiti() {
		<%-- MEV_39: aggiunti controlli --%>
		var node = document.getElementById("datarinvio");
    	var ric = document.getElementById("ricovero");
		if (typeof (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0]) == "undefined") {
   			for (j = 0; j < document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
   				if (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected) {
  					if (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1}
  							|| document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1}) {
  						node.style.display = 'block';
  						ric.style.display = 'none';
          				if (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
          					ric.style.display = 'block';
   					}
   					if (!(document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1836':1, '1900':1, '0259':1})
   							&& !(document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
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
			for (j = 0; j < document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++) {
				for (i = 0; i < document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++) {
					if ((document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected)
							&& (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value in {'1836':1, '1900':1, '0259':1}
									|| document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})) {
						node.style.display = 'block';
						ric.style.display = 'none';
          				if (document.InserisciOrdinanzaRinvioMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in {'1905':1, '2745':1, '0260':1})
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
lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
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
	      		<font class="campo">Emissione Ordinanza Rinvio Esecuzione Misura Sicurezza</font>
	      	</td>
	   	</tr>
	    <tr>
	       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	    </tr>
    </table>

  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaRinvioMS">
    <table cellspacing="2" cellpadding="2" width="95%">
   		<tr>
     		<td class="l" width="25%">Data Emissione</td>
     		<td class="l"><%=DateUtils.getDateToString(data_emissione, "dd/MM/yyyy")%></td>
   		</tr>
	</table>
	<br>
 	<table cellspacing="2" cellpadding="2" width="95%">
    	<tr>
        	<td class="Titolo" colspan="2">Specificare esito per ciascun oggetto:</td>
   		</tr>
    	<tr>
	        <td class="l" width="50%">Oggetto</td>
	        <td class="l">Esito</td>
    	</tr>
<%
for (int i = 0; i < tenori.length; i++) {
%>
       	<tr>
        	<td class="l">
          		<input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE%>" value="<%=tenori[i].getDescrOggettoTenore()%>" readonly size="70%">
          		<input Title="Cod Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>" value="<%=tenori[i].getCodOggettoTenore()%>">
          		<input Title="Cod Dettaglio Oggetto" type="hidden" name="<%=ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=tenori[i].getCodDettaglioOggetto()%>">
        	</td>
          	<td class="l">
           		<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();">
             		<%=esiti[i]%>
          		</select>
        	</td>
      	</tr>
<%
}
%>
	</table>
    <table cellspacing="2" cellpadding="2" width="95%">
 		<tr>
			<td class="l" width="25%">Ulteriore descrizione della decisione</td>
    		<td class="l">
    			<TEXTAREA title="Ulteriore descrizione della decisione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols="70" rows="4"></TEXTAREA>
    		</td>
		</tr>
 	</table>
	<br>
	<%-- MEV_39: aggiunta sezione --%>
 	<table cellspacing="2" cellpadding="2" width="95%" id="datarinvio" style="display: none;">
	  	<tr>
	    	<td class="l" colspan="3">In caso di sospensione indicare:<td>
	  	</tr>
	    <tr>
<%-- MEV_39: modifica etichetta per U082 e U077 --%>
<%
		if (contenuto != null && !contenuto.equals("") && (contenuto.equals("U082") || contenuto.equals("U077"))){
%>
	      	<td class="l" width="25%">Data Decorrenza Differimento Esecuzione</td>
<%
		} else {
%>
			<td class="l" width="25%">Data Differimento Esecuzione</td>
<%
				}
%>
	      	<td class="L" colspan="2">
		        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
		        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
		        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	      	</td>
	     </tr>
	  	 <tr>
	      	<td class="l">Rinvio fino al</td>
	      	<td class="L">
		        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
		        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
		        <input value="" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
	      	</td>
	      	<td class="l">oppure Rinvio nella misura di: 
	      		Anni
       			<input value="" title="Numero Anni sospensione" type="text" size="3" maxlength="2" name="<%=ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>"  > 
	        	Mesi
	        	<input value="" title="Numero Mesi sospensione" type="text" size="3" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS %>"  > 
	        	Giorni
	        	<input value="" title="Numero Giorni sospensione" type="text" size="4" maxlength="2" name="<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS %>"  >
	      	</td>
	   	</tr>
	    <tr id="ricovero" style="display: none;">
			<td class="l">Luogo Ricovero</td>
			<td class="L" colspan="2">
				<TEXTAREA title="Note" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>" cols="80" rows="4"></textarea>
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="2" cellpadding="2" width="95%">
    	<tr>
    		<td>
        		<input class="bottone" type="submit" value="Conferma" >
      		</td>
    	</tr>
	</table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>">
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>">
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione, "dd/MM/yyyy")%>>
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">
  	</form>
  	<script language="JavaScript" type="text/javascript">
	    var frmvalidator = new Validator("InserisciOrdinanzaRinvioMS");
	    frmvalidator.setAddnlValidationFunction("Verify");
	    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_GG_SS%>","numeric");
	    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_MM_SS%>","numeric");
	    frmvalidator.addValidation("<%= ICostantiDepositoDecreto.CAMPO_SOSPENSIONE_AA_SS%>","numeric");
		// Data fine Rinvio
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","lt=31");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","lt=12");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","gt=1900");
		// Data inizio sospensione
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_INIZIO_PERIODO%>","lt=31");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_INIZIO_PERIODO%>","lt=12");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_INIZIO_PERIODO%>","gt=1900");
  	</script>
 	</body>
</html>