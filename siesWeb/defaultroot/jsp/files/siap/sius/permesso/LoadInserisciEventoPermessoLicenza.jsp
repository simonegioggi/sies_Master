<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso"%>
<%@ page import="siap.sius.permesso.action.ICostantiEventoPermessoLicenza"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>

<jsp:useBean id="tipiEventi"				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipiConseguenze"		scope="request" class="java.lang.String"/>
<%--jsp:useBean id="permessoDepDecr"	scope="request" class="siap.sius.permesso.model.PermessoDepositoDecretoModel"/--%>
<jsp:useBean id="permessoDepDecr"		scope="request" class="siap.sius.permesso.model.DepositoDecretoMotivazioniLicenzaModel"/>
<jsp:useBean id="eventoPermesso"		scope="request" class="siap.sius.permesso.model.EventoPermessoLicenzaModel"/>
<jsp:useBean id="modalita"					scope="request" class="java.lang.String"/>
<%
	// Verifica ed imposta l'azione e le descrizioni in funzione
	// della modalità passata come parametro in request
	String lAction = new String(),lDescrModalita = new String();			
	if( modalita.equalsIgnoreCase("M") )// Modifica
	{
		lDescrModalita = "Modifica";
  	lAction = "siap.sius.permesso.action.ActModificaEventoPermessoLicenza";
	}
	else if( modalita.equalsIgnoreCase("I") ) // Inserisce
	{
		lDescrModalita = "Inserisce";
		lAction = "siap.sius.permesso.action.ActInserisciEventoPermessoLicenza";
	}

	//LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenzaLibAnticipata();
	LicenzaLibAnticipataModel lLic = permessoDepDecr.getLicenza();
	
	// Distinguo tra il permesso e la licenza.
	String lDescrTipoLicenza = "",lCodTipoLicenza = "";
 	lCodTipoLicenza = permessoDepDecr.getLicenza().getCodTipoLicenza();
  if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.LICENZA) )
  	lDescrTipoLicenza = "Licenza";
  else if( lCodTipoLicenza.equalsIgnoreCase(ICostantiLicenzaLibanticipata.PERMESSO_PREMIO) )
  	lDescrTipoLicenza = "Permesso";
%>

<html>
	<head>
    <title>[S.I.E.S.] - <%=lDescrModalita%> evento durante Fruizione <%=lDescrTipoLicenza%></title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
			// Controllo di verifica.
			function Verify()
    	{
      	var ritorno = false;
      	ritorno = controlloDate();
      	return ritorno;
    	}
    	
    	// Controllo delle date.
      function controlloDate()
      {
         var ret 		= true;
         var docRef	= document.LoadInserisciEventoPermessoLicenza;
         var gg 		= FillDM(docRef.<%=ICostantiEventoPermessoLicenza.CAMPO_GIORNO_DATA_SEGNALAZIONE%>.value);
         var mm 		= FillDM(docRef.<%=ICostantiEventoPermessoLicenza.CAMPO_MESE_DATA_SEGNALAZIONE%>.value);
         var aa 		= docRef.<%=ICostantiEventoPermessoLicenza.CAMPO_ANNO_DATA_SEGNALAZIONE%>.value;
	
         var dataSegnalazione = gg + "/" + mm + "/" + aa;
         var dataSistema 			= '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
         var dataDeposito 	 	= '<%=DateUtils.getDateToString(permessoDepDecr.getDepositoDecreto().getDataDeposito(), "dd/MM/yyyy")%>';

         if (dataSegnalazione.length < 10 )
         {
            ret = false;
            alert ('Data di segnalazione mancante');
         }
         else if (!ControllaData (dataSegnalazione))
         {
            ret = false;
            alert ('Data di segnalazione non valida : ' + dataSegnalazione);
         }
				 else if (!CompareDate( dataSegnalazione,dataSistema))
      	 {
        		ret = false;
        		alert('Data di segnalazione maggiore della data attuale!');
      	 }
				 else if (!CompareDate( dataDeposito,dataSegnalazione ))
      	 {
        		ret = false;
        		alert('Data di segnalazione minore della data deposito!');
      	 }
				
				 if(!ret)
				 	docRef.<%=ICostantiEventoPermessoLicenza.CAMPO_GIORNO_DATA_SEGNALAZIONE%>.focus();	
         
         return ret;
      }
     </script>
  </head>
  
	<body class="corpo" 
				onLoad="javascript:document.LoadInserisciEventoPermessoLicenza.<%=ICostantiEventoPermessoLicenza.CAMPO_COD_TIPO_EVENTO%>.focus()">
	<table>
  	<tr>
    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    		</a>
    	</td>
      <td class="LBG"><font class="label">Funzione : </font>
      	<font class="campo">
      		<%=lDescrModalita%> evento durante Fruizione <%=lDescrTipoLicenza%>
      	</font>
      	&nbsp;
      </td>
      
      <!-- Inserisce il pulsante di ritorno -->
			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      
    </tr>
  </table>
	
	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
  <jsp:include page="<%=ICostantiPermesso.PG_SINTESI_DATI_PERMESSOLICENZA%>"/>    	
	
	<FORM method=post action="<%= IWebConstants.PG_MAIN %>" name ="LoadInserisciEventoPermessoLicenza" >
 		<table>  		
			<tr>
    		<td class="l">Tipo evento osservato durante la fruizione <font class="ob">(*)</font></td>
      	<td class="L">
        	<select title="tipo evento" name="<%=ICostantiEventoPermessoLicenza.CAMPO_COD_TIPO_EVENTO%>">
          	<%=tipiEventi%>
         	</select>
      	</td>
    	</tr>			
			<tr>
      	<td class="l">Descrizione evento </td>
      	<td class="L">
        	<input value="<%=StringUtils.toStringJSP(eventoPermesso.getDescrEvento())%>" 
        				 type="text" size="80" maxlength="100" 
        				 name="<%=ICostantiEventoPermessoLicenza.CAMPO_DESCR_EVENTO%>" 
        				 onFocus="javascript:textboxSelect(this)">
        		</td>
    	</tr>
			<tr>
     		<td class="l">Data segnalazione evento<font class="ob">(*)</font> (gg/mm/aa) </td>
     			<td class="L">
       			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(eventoPermesso.getDataSegnalazione(), "dd"))%>" 
       						 type="text" size="2" maxlength="2" 
       						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_GIORNO_DATA_SEGNALAZIONE%>" 
       						 onFocus="javascript:textboxSelect(this)" 
       						 onkeypress="return TicTabNumField(this,event)"  
       						 onBlur="javascript:value=FillDM(value)" > /
       			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(eventoPermesso.getDataSegnalazione(), "MM"))%>" 
       						 type="text" size="2" maxlength="2" 
       						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_MESE_DATA_SEGNALAZIONE%>" 
       						 onFocus="javascript:textboxSelect(this)" 
       						 onkeypress="return TicTabNumField(this,event)"  
       						 onBlur="javascript:value=FillDM(value)" > /
       			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(eventoPermesso.getDataSegnalazione(), "yyyy"))%>" 
       						 type="text" size="4" maxlength="4" 
       						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_ANNO_DATA_SEGNALAZIONE%>" 
       						 onFocus="javascript:textboxSelect(this)" 
       						 onkeypress="return TicTabNumField(this,event)"  
       						 onBlur="javascript:value=FillYear(value)">
       		</td>
   		</tr>

			<tr>
     		<td class="l">Mittente Segnalazione </td>
     			<td class="L">
       			<input value="<%=StringUtils.toStringJSP(eventoPermesso.getMittenteSegnalazione())%>" 
       						 type="text" size="80" maxlength="100" 
       						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_MITTENTE_SEGNALAZIONE%>" 
       						 onFocus="javascript:textboxSelect(this)">
       		</td>
   		</tr>
			<tr>
   			<td class="l">Conseguenze evento osservato durante la fruizione <font class="ob">(*)</font></td>
     		<td class="L">
       		<select title="tipi conseguenze" name="<%=ICostantiEventoPermessoLicenza.CAMPO_COD_TIPO_CONSEGUENZA%>">
         		<%=tipiConseguenze%>
        		</select>
     		</td>
   		</tr>
			<tr>
     		<td class="l">Descrizione conseguenze </td>
     			<td class="L">
       			<input value="<%=StringUtils.toStringJSP(eventoPermesso.getDescrConseguenze())%>" 
       						 type="text" size="80" maxlength="100" 
       						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_DESCR_CONSEGUENZE%>" 
       						 onFocus="javascript:textboxSelect(this)">
       		</td>
   		</tr>

			<tr>
     		<td class="L">
     			<input class="bottone" type="submit" value="Conferma">
      			<input type="HIDDEN" 
      						 name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" 
      						 value="<%=lAction%>">
      			<input type="HIDDEN" 
      						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_LIC_ID_LICENZA_LIBANTICIPATA%>" 
      						 value="<%=lLic.getIdLicenzaLibanticipata()%>">
      			<input type="HIDDEN" 
      						 name="<%=ICostantiEventoPermessoLicenza.CAMPO_ID_EVENTO_PERMESSO_LICENZA%>" 
      						 value="<%=eventoPermesso.getIdEventoPermessoLicenza()%>">
     		</td>
   		</tr>	
		</table>
	</FORM>
		
	<script language="JavaScript" type="text/javascript">
   	var frmvalidator = new Validator("LoadInserisciEventoPermessoLicenza");
   	frmvalidator.setAddnlValidationFunction("Verify");
   	// Controllo campo titolo relazione.
   	//frmvalidator.addValidation("<--%= ICostantiRelazione.CAMPO_NOTE%>","req", "Il campo titolo relazione è obbligatorio");	
 	</script>
		
	</body>
</html>