<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel"%>
<%@ page import="siap.siep.notiziareato.action.ICostantiNotiziaReato"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.security.model.ProfileModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="combosntrat"     scope="request" class="java.lang.String"/>
<jsp:useBean id="fotocombosntrat" scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"  scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="notiziareato"    scope="request" class="siap.siep.notiziareato.model.NotiziaReatoModel"/>
<jsp:useBean id="autorita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="codFunzione"     scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.A.P.] - GestioneNotiziaReato </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">

	// Funzione popup Lista Comuni
	var desktop;
	function ListaComuni(a_formname,a_fieldname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}


	function Verify()
    {
		// Controllo inserimento di almeno di un campo
		if (
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ACQUISIZIONE_DIRETTA%>.options[document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ACQUISIZIONE_DIRETTA%>.selectedIndex].value=='-' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_DESCRIZIONE_FONTE%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FONTE%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_NUM_REG_AUTORITA%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_LUOGO_PROVENIENZA%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_NUMERO_RICEVUTA%>.value==''&&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>.value=='-'&&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>.value=='' &&

			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>.value=='' &&
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>.value==''
						
		   )
		{
			 alert('Deve essere inserito almeno un campo');
			 return false;
		}
		
		// Controllo lunghezza dei campi giorno e mese delle date
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value;

		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value;
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value;

		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value=
			'0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value;
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value;
		
		if(document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value;
		if(document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>.value.length==1)
			document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>.value='0'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>.value;
		

		//Controllo validità Data Pervenimento
		var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
		if (! ControllaDataPassaVuota(d1))
		{
			 alert('Data Pervenimento non valida');
			 return false;
		}

		//Controllo validità Data Acquisizione
		var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>.value;
		if (! ControllaDataPassaVuota(d1))
		{
			 alert('Data Acquisizione non valida');
			 return false;
		}

		//Controllo validità Data Fatto
		var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO%>.value;
		if (! ControllaDataPassaVuota(d1))
		{
			 alert('Data Fatto non valida');
			 return false;
		}
		
		//Controllo validità Data Arresto
		var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>.value;
		if (! ControllaDataPassaVuota(d1))
		{
			 alert('Data Arresto non valida');
			 return false;
		}
		
		//Controllo validità Data Fermo
		var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>.value;
		if (! ControllaDataPassaVuota(d1))
		{
			 alert('Data Fermo non valida');
			 return false;
		}
		//Controlli validità Data Fototesgnalazione solo se il campo FlagFotosegnalato è S
		if(document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>.value=="S"){		
			var d1=document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>.value+'/'+document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>.value;
			if (! ControllaDataPassaVuota(d1))
			{
				 alert('Data Fotosegnalazione non valida');
				 return false;
			}
		}
		
		//Controllo i campi obbligatori nel caso in cui FlagGotosegnalato=S	
		if(document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>.value=="S"){
			// Data	
			if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>.value==''){
				 	alert('Il campo Data Fotosegnalazione è obbligatorio');			 
				 	return false;
			}			
			//Autorità
			if (
				document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_AUTORITA_FOTO%>.value=='-' || 
				document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_AUTORITA_FOTO%>.value==''){
					alert('Il campo Autorità Fotosegnalazione è obbligatorio');			 
				 	return false;
			}
			// Comune	
			if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FOTO%>.value==''){
				 	alert('Il campo Comune Fotosegnalazione è obbligatorio');			 
				 	return false;
			}
		}
				
		//CONTROLLO ALTRI CAMPI OBBLIGATORI
		// Comune FOnte	
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FONTE%>.value==''){
			 	alert('Il campo Comune Fonte è obbligatorio');			 
			 	return false;
		}
		// Data	Fatto
		if (document.LoadInserisciNotiziaReato.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>.value==''){
			 	alert('Il campo Data Fatto è obbligatorio');			 
			 	return false;
		}			
		
		// il return serve alla funzione javascipt (IWebConstants.JS_VALIDATOR) per disabilitare
		// il tasto di conferma del form
		return true;
	}

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
</script>


</head>
	<body class="corpo">
		<table cellspacing="0" cellpadding="0">
			<tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
				<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<!-- Verifica modalità Inserimento o Modifica -->
				<%
				NotiziaReatoModel lNotiziaReato = new NotiziaReatoModel();
				String lAzione = new String();
				
				
				if( modalita.equals("I") )
				{
					lAzione = "siap.siep.notiziareato.action.ActInserisciNotiziaReato";
				%>
					<font class="campo">Inserimento Notizia di Reato</font>
				<%
				}
					else if( modalita.equals("M") )
				{
					lAzione = "siap.siep.notiziareato.action.ActModificaNotiziaReato";
					lNotiziaReato = notiziareato;
				%>
					<font class="campo">Modifica Notizia di Reato</font>
				<%
				}
				%>
				<!-- FINE - Verifica modalità Inserimento o Modifica -->
				<jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>"></jsp:include>
				</td>
			</tr>
		</table>

		<!-- Visualizzazione Dettagli Procedimento -->
		
		<%
		// Si ricava il profilo dell'utente connesso
		ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();
		if( lProfilo.isSige()) {
		%>
			<br>
    			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
    		<br>
    	<%
		}
    	else{%>
    		<br>
      			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    		<br>
    	<% }
    	%>
		
		
    	

		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciNotiziaReato">
		<table cellspacing=4 cellpadding=4>

			<tr>
				<td class="l">Data Pervenimento</td>
				<td class="l">
					<input title = "Giorno Data Pervenimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataPervenimento(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%>>
					-
					<input title = "Mese Data Pervenimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataPervenimento(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%>>
					-
					<input title = "Anno Data Pervenimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataPervenimento(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
				</td>
			</tr>

            <tr>
				<td class="l">Data Acquisizione</td>
				<td class="l">
	                 <input title = "Giorno Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataAcquisizione(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE %>" <%=IWebConstants.UTIL_DATA%>>
					 -
	                 <input title = "Mese Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataAcquisizione(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE %>" <%=IWebConstants.UTIL_DATA%>>
					 -
					 <input title = "Anno Data Acquisizione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataAcquisizione(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
                 </td>
			</tr>

            <tr>
				<td class="l">Acquisizione Diretta</td>
				<td class="l">
					<select title="Acquisizione Diretta" name="<%=ICostantiNotiziaReato.CAMPO_ACQUISIZIONE_DIRETTA%>">
         			<%= combosntrat %>
         			</select>
				</td>
			</tr>

            <tr>
				<td class="l">Data Fatto<font class="ob">&nbsp;(*)</font></td>
				<td class="l">
                	<input title = "Giorno Data Fatto" 
                	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFatto(), "dd") )%>"
                	 type="text" size="2" maxlength="2" 
                	 name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO %>" 
                	 <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Mese Data Fatto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFatto(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO %>" <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Anno Data Fatto" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFatto(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
                </td>
			</tr>

            <tr>
				<td class="l">Descrizione Fonte</td>
				<td class="l"><input size="35" maxlength="30" value="<%=StringUtils.toStringJSP(lNotiziaReato.getDescrizioneFonte()) %>" type="text" name="<%= ICostantiNotiziaReato.CAMPO_DESCRIZIONE_FONTE %>"  ></td>
			</tr>

			<tr>
				<td class="l">Comune Fonte<font class="ob">&nbsp;(*)</font></td>
				<td class="l">
					<!-- Controllo visualizzazione Descrizione del Comune -->
					<%
					String DescComune = new String();
					if(!lNotiziaReato.getDescrComuneFonte().equals("-"))
					{
						DescComune = StringUtils.toStringJSP(lNotiziaReato.getDescrComuneFonte());
					}
					else
					{
						DescComune = "";
					}
					%>
					<input title="Comune Fonte" value="<%=DescComune%>"
					 type="text" name="<%= ICostantiNotiziaReato.CAMPO_COD_COMUNE_FONTE %>"  maxlength="35" size="35" >
					<a href="Javascript:ListaComuni('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FONTE%>');">
          				<img src="/images/filefolder.gif" border=0>
					</a>
				</td>
			</tr>

            <tr>
				<td class="l">Numero Registro Autorità</td>
				<td class="l"><input size="35" maxlength="25" value="<%=StringUtils.toStringJSP(lNotiziaReato.getNumRegAutorita()) %>" type="text" name="<%= ICostantiNotiziaReato.CAMPO_NUM_REG_AUTORITA %>"  ></td>
			</tr>

            <tr>
				<td class="l">Luogo Provenienza</td>
				<td class="l"><input size="35" maxlength="78" value="<%=StringUtils.toStringJSP(lNotiziaReato.getLuogoProvenienza()) %>" type="text" name="<%= ICostantiNotiziaReato.CAMPO_LUOGO_PROVENIENZA %>"  ></td>
			</tr>

            <tr>
				<td class="l">Numero Ricevuta</td>
				<td class="l"><input size="9" maxlength="9" value="<%=StringUtils.toStringJSP(lNotiziaReato.getNumeroRicevuta()) %>" type="text" name="<%= ICostantiNotiziaReato.CAMPO_NUMERO_RICEVUTA %>"  ></td>
			</tr>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%-- tr>
				<td class="l">Arrestato</td>
				<td class="l">
					<select title="Arresto" 
						name="<%=ICostantiNotiziaReato.CAMPO_FLAG_ARRESTATO%>" 
						value=" <%=lNotiziaReato.getFlagArrestato()%>">
         			<%= arrcombosntrat %>
         			</select>
				</td>					
			</tr--%>
			<tr>
				<td class="l">Data Arresto</td>
				<td class="l">
					<input title = "Giorno Data Arresto" 
						value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataArresto(), "dd") )%>" 
						type="text" size="2" maxlength="2" 
						name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO %>" <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Mese Data Arresto" 
					 	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataArresto(), "MM") )%>" 
					 	type="text" size="2" maxlength="2" 
					 	name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>" <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Anno Data Arresto" 
					 	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataArresto(), "yyyy") )%>" 
					 	type="text" size="4" maxlength="4" 
					 	name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
				</td>
			</tr>
			<tr>
				<td class="l">Data Fermo</td>
				<td class="l">
					<input title = "Giorno Data Fermo" 
						value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFermo(), "dd") )%>" 
						type="text" size="2" maxlength="2" 
						name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>" <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Mese Data Fermo" 
					 	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFermo(), "MM") )%>" 
					 	type="text" size="2" maxlength="2" 
					 	name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>" <%=IWebConstants.UTIL_DATA%>>
					-
				 	<input title = "Anno Data Fermo" 
					 	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFermo(), "yyyy") )%>" 
					 	type="text" size="4" maxlength="4" 
					 	name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>" <%=IWebConstants.UTIL_DATA_ANNO%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
				</td>					
			</tr>
			
			<!-- CAMPI PER INTEGRAZIONE REGE-SIES -->
			<tr>
				<td class="l">Fotosegnalato</td>
				<td class="l">
					<script>
						function visCampiFoto(){				
							var form = document.LoadInserisciNotiziaReato;
							var campo = form.<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>;
							
							if(campo.value=="S"){		
								document.all["blockFoto"].style.display="block";						
								//form.chkFoto.value=1;			
							}
							else{		
								document.all["blockFoto"].style.display="none";			
								form.chkFoto.value=0;
								//pulisco i campi eventualmente compilati
								pulisciFoto();
								
							}
						}				
						function pulisciFoto(){
							var form = document.LoadInserisciNotiziaReato;
							var campo = form.<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>;
							if(campo.value!="S"){
							//if(confirm("Ripulire i campi compilati?")){
								form.<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FOTO%>.value = "";
								form.<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO%>.value = "";
								form.<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>.value = "";
								form.<%=ICostantiNotiziaReato.CAMPO_AUTORITA_FOTO%>.value = "-";
								form.<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FOTO%>.value = "";
								//form.chkFoto.value=0;
							} 
						}				
					</script>
					<select title="Fotosegnalato" 
						name="<%=ICostantiNotiziaReato.CAMPO_FLAG_FOTOSEGNALATO%>" 
						onchange="visCampiFoto()">
         			<%= fotocombosntrat %>
         			</select>
         			<input type="hidden" name="chkFoto" value=0>
				</td>
			</tr>
			
			<tr>
			  	<td colspan="2" style="border:0; padding:0">			  					
					<div 
						<%if(StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFoto(), "dd")).equals("")){%>
							style="display:none" 
						<%}
						else{%> 
							style="display:block"
						<%} %>
						id="blockFoto"
					>
						<table cellpadding="4" cellspacing="4">
							<tr>
								<td class="l">Data Fotosegnalazione <font class="ob">(*)</font></td>
								<td class="l"> 									
									<input title = "Giorno Data Fotosegnalazione" 
                						value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFoto(), "dd") )%>"
                	 					type="text" size="2" maxlength="2" 
                	 					name="<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>" 
                	 					<%=IWebConstants.UTIL_DATA%>>-	
									<input title = "Mese Data Fotosegnalazione" 
                						value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFoto(), "MM") )%>"
                	 					type="text" size="2" maxlength="2" 
                	 					name="<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO %>" 
                	 					<%=IWebConstants.UTIL_DATA%>>-
                	 				<input title = "Anno Data Fotosegnalazione" 
                						value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotiziaReato.getDataFoto(), "yyyy") )%>"
                	 					type="text" size="2" maxlength="4" 
                	 					name="<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FOTO %>" 
                	 					<%=IWebConstants.UTIL_DATA%>>
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Notizia di Reato da Fase Istruttoria
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90030000) ){
%>
			<a href="javascript:calendario('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_ANNO_DATA_FOTO%>','<%=ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO%>','<%=ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
								</td>
							</tr>
								
							<tr>
								<td class="l">Autorit&agrave; Fotosegnalazione  <font class="ob">(*)</font></td>
								<td class="L">
									<select title="Autorita" 
									name="<%=ICostantiNotiziaReato.CAMPO_AUTORITA_FOTO%>">
									<%=autorita%>
									</select>								
								</td>						
							</tr>
								
							<tr>						
								<td class="l">Comune Fotosegnalazione <font class="ob">(*)</font></td>						
								<td class="L">
								<!-- Controllo visualizzazione Descrizione del Comune -->
								<%
									String DescComuneFoto = new String();
									if(lNotiziaReato.getDescComuneFoto()!=null){
										if(!lNotiziaReato.getDescComuneFoto().equals("-"))
										{
											DescComuneFoto = StringUtils.toStringJSP(lNotiziaReato.getDescComuneFoto());
										}
										else
										{
											DescComuneFoto = "";
										}
									}									
									%>
									<input title="Sede Autorita Fotosegnalazione" value="<%=DescComuneFoto%>"
									 type="text" 
									 name="<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FOTO%>"  maxlength="35" size="35" >
									<a href="Javascript:ListaComuni('LoadInserisciNotiziaReato','<%=ICostantiNotiziaReato.CAMPO_COD_COMUNE_FOTO%>');">
										<img src="/images/filefolder.gif" border="0">
									</a>
									
								</td>						
							</tr>
						</table>
					</div>		
				</td>
			</tr>
			
			
			<tr>
		      <td colspan=2>
		    	<input type="HIDDEN" name="<%=ICostantiNotiziaReato.CAMPO_ID_NOTIZIA_REATO%>" value="<%=lNotiziaReato.getIdNotiziaReato()%>">
				<input type="HIDDEN" name="Action" value="<%=lAzione%>">
		        <input class=bottone type="submit" value="Conferma">
		        <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
		      </td>
			</tr>

		</table>
		</form>
		<script language="JavaScript" type="text/javascript">

    		var frmvalidator  = new Validator("LoadInserisciNotiziaReato");
    		// CAMPI OBBLIGATORI
    		

			// CONTROLLI DATE

			// Data Pervenimento
			frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");

			// Data Acquisizione
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ACQUISIZIONE%>","lt=31");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_ACQUISIZIONE%>","lt=12");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ACQUISIZIONE%>","gt=1900");

			// Data Fatto
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FATTO%>","lt=31");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FATTO%>","lt=12");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FATTO%>","gt=1900");
		    
		    // Data Arresto
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_ARRESTO%>","lt=31");

		    /* frmvalidator.addValidation("< %= ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>","numeric");
		    frmvalidator.addValidation("< %= ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>","gt=1");
		    frmvalidator.addValidation("< %= ICostantiNotiziaReato.CAMPO_MESE_DATA_ARRESTO%>","lt=12");*/

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_ARRESTO%>","gt=1900");
		    		    
		    // Data Fermo
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FERMO%>","lt=31");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FERMO%>","lt=12");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FERMO%>","gt=1900");
		    
		    // Data Fotosegnalazione
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_GIORNO_DATA_FOTO%>","lt=31");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO%>","gt=1");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_MESE_DATA_FOTO%>","lt=12");

		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FOTO%>","numeric");
		    frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_ANNO_DATA_FOTO%>","gt=1900");

<%-- 			frmvalidator.addValidation("<%= ICostantiNotiziaReato.CAMPO_COD_COMUNE_FONTE %>","alpha"); --%>

			frmvalidator.setAddnlValidationFunction("Verify");

		</script>
	</body>
</html>