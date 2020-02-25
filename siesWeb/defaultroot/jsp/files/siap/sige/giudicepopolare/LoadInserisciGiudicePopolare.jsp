<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.giudicepopolare.model.GiudicePopolareModel"%>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>

<jsp:useBean id="modalita"					scope="request" class="java.lang.String"/>
<jsp:useBean id="giudicepopolare"   scope="request" class="siap.sige.giudicepopolare.model.GiudicePopolareModel"/>
<jsp:useBean id="elencoRuoli" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSessi" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoNazioni" 		scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Giudice Popolare </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

    <script language="JavaScript">
      var DataIni= "";
			var desktop;
			
			// Lista Comuni
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname=" + a_formname + "&fieldname=" + a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      // init per focus sul primo campo
      function init()
      {
    	  document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>.focus();
    	  onChangeStatoNascita();
      }
      
			// data inizio
      function initDataIni(data)
      {
        DataIni = data;
      }
      
      // Verifica obbligatorietà campo nome e cognome
			function checkObblCognomeNome()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>.value.length == 0 ) 
		    {
		    	alert("Cognome obbligatorio.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>.focus();
		      ritorno = false;
		    }
				else if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_NOME%>.value.length == 0 )
				{
				  alert("Nome obbligatorio.");
				  document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_NOME%>.focus();
				  ritorno = false;
				}
				return ritorno;  
			}

			function checkObblSesso()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_SESSO%>.value.length == 0 ) 
		    {
		    	alert("Sesso obbligatorio.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_SESSO%>.focus();
		      ritorno = false;
		    }
				return ritorno;  
			}

			// Check Obbl. Ruolo.
			function checkObblRuolo()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_RUOLO%>.value == '-' ) 
		    {
		    	alert("Ruolo obbligatorio.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_RUOLO%>.focus();
		      ritorno = false;
		    }
				return ritorno;  
			}
			
			// Check Obbl. Sezione.
			function checkObblSezione()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.value == '' ) 
		    {
		    	alert("Sezione obbligatoria.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>.focus();
		      ritorno = false;
		    }
				return ritorno;  
			}
			
			function checkObblStatoNascita()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_STATO_NASCITA%>.value == '-' ) 
		    {
		    	alert("Stato di nascita obbligatorio.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_STATO_NASCITA%>.focus();
		      ritorno = false;
		    }
				return ritorno;  
			}
			
			// Verifica date di validità
			function checkDataValidita()
			{
				var ritorno = true;
		    var dataInizioValidita = 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
		        											
		    var dataFineValidita = 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_FINE_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

		    if (dataInizioValidita.length > 2)
		    {
		    	if (!ControllaData(dataInizioValidita))
		      {
		      	alert('Data di Inizio Validità non corretta');
				  	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		        ritorno = false;
		      }
		      else if (!CompareDate(DataIni, dataInizioValidita))
		      {
		     		alert('Data di Inizio Validità non può essere anticipata');
				  	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		        ritorno = false;
		      }
		      else if (dataFineValidita.length > 2)
		      {
		      	if (!ControllaData(dataFineValidita))
		        {
		        	alert('Data di Fine Validità non corretta');
				  		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
		          ritorno = false;
		        }
		        else if (!CompareDate(dataInizioValidita, dataFineValidita))
		       	{
		        	alert('Data di Inizio Validità non può essere successiva a quella di Fine');
				  		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		          ritorno = false;
		        }
		      }
		    }
		    else if(dataFineValidita.length > 2)
		    {
		    	alert('Non è possibile specificare Data di Fine Validità senza specificare quella di Inizio Validità ');
		     	ritorno = false;
		    }
		    else
		    {
		    	alert('Data inizio validità obbligatoria.');
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		     	ritorno = false;
			  }
				return ritorno;
			}	

			// Verifica data di nascita
			function checkDataNascita()
			{
				var ritorno = true;
		    var dataNascita = 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_NASCITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_NASCITA%>.value +'/'+ 
		    		document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_NASCITA%>.value;
		        											
		    if (dataNascita.length > 2)
		    {
		    	if (!ControllaData(dataNascita))
		      {
		      	alert('Data di nascita non corretta.');
		      	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_NASCITA%>.focus();
		        ritorno = false;
		      }
		    }
		    else
		    {
		    	alert('Data di nascita obbligatoria.');
		      document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_NASCITA%>.focus();
		      ritorno = false;
			  }
			    
				return ritorno;
			}

			function checkObblComuneNascita()
			{
				var ritorno = true;				
				if ( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>.value.length == 0 && 
						 document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_STATO_NASCITA%>.value == '039' ) 
		    {
		    	alert("Comune di nascita obbligatorio.");
		    	document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>.focus();
		      ritorno = false;
		    }
				return ritorno;  
			}

			function onChangeStatoNascita()
			{
				if( document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_STATO_NASCITA%>.value == '039' )
				{
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>.disabled = false;
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COMUNE_ESTERO_NASCITA%>.value = '';
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COMUNE_ESTERO_NASCITA%>.disabled = true;
				}
				else
				{
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COMUNE_ESTERO_NASCITA%>.disabled=false;
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>.value = '';
					document.LoadInserisciGiudicePopolare.<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>.disabled=true;					
				}			
			}	 
			
			// Esecuzione delle funzioni di verifica.
      function Verify()
      {  
				// Controllo obbligatorietà Nome e Cognome.
        if( !checkObblCognomeNome() )
        	return false;
				// Controllo obbligatorietà Sesso.
        if( !checkObblSesso() )
          return false;
				// Controllo data nascita.    		
    		if( !checkDataNascita() )
        	return false;
        // Controllo obbligatorietà Stato nascita.
    		if( !checkObblStatoNascita() )
          return false;
				// Controllo obbligatorietà Comune nascita.
        if( !checkObblComuneNascita() )
          return false;
				// Controllo obbligatorietà Ruolo.
        if( !checkObblRuolo() )
          return false;
			<%// Se esistono sezioni esegue il conrollo di obbl.
				if( elencoSezioni.length() != 0 ) {%>
					if( !checkObblSezione() )
					return false;
			<%}%>
        // Controllo data Validità.
        if( !checkDataValidita() )
    		  return false;
		    
        return true;
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
  </head>

  <body class="corpo" onload="Javascript:init();">
    <table>
    	<tr>
    		<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a>
				</td>

    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      GiudicePopolareModel lGiudicePopolare = null;
      String lAzione = new String();
      
      if( modalita.equals("I") )
      {
        lGiudicePopolare = new GiudicePopolareModel();
        lAzione = "siap.sige.giudicepopolare.action.ActInserisciGiudicePopolare";
        lGiudicePopolare.setDataInizioValidita(DateUtils.getSysDate());
    %>
    		<font class="campo">Inserimento Giudice Popolare</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lGiudicePopolare = giudicepopolare;
        if(lGiudicePopolare.getDataInizioValidita() == null)
          lGiudicePopolare.setDataInizioValidita(DateUtils.getSysDate());
        lAzione = "siap.sige.giudicepopolare.action.ActModificaGiudicePopolare";
    %>
    		<font class="campo">Modifica Giudice Popolare</font>
    <%
        }
    %>
    		</td>
    	</tr>
    	<script language="JavaScript">
      	initDataIni("<%=StringUtils.toStringJSP(DateUtils.getDateToString( lGiudicePopolare.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
    	</script>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciGiudicePopolare">
      <table cellspacing="4" cellpadding="4">
        <tr>
          <td class="l">Cognome <font class=ob>(*)</font></td>
          <td class="l">
          	<input value="<%=lGiudicePopolare.getCognome()%>" 
									 type="text" name="<%=ICostantiGiudicePopolare.CAMPO_COGNOME%>">
          </td>
        </tr>
        <tr>
          <td class="l">Nome <font class=ob>(*)</font></td>
          <td class="l">
          	<input value="<%=lGiudicePopolare.getNome()%>" 
									 type="text" name="<%=ICostantiGiudicePopolare.CAMPO_NOME%>">
          </td>
        </tr>
        <tr>
          <td class="l">Codice Fiscale</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lGiudicePopolare.getCodiceFiscale())%>" 
									 type="text" name="<%=ICostantiGiudicePopolare.CAMPO_CODICE_FISCALE%>" 
									 size="16" maxlength="16">
          </td>
        </tr>
			<tr>
      	<td class="l">Sesso <font class=ob>(*)</font></td>
        <td class="l">
        	<select title="Sesso" name="<%=ICostantiGiudicePopolare.CAMPO_COD_SESSO%>">
            <%=elencoSessi%>
          </select>
        </td>
      </tr>
			
        <tr>
          <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lGiudicePopolare.getDataNascita(),"dd"))%>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_NASCITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lGiudicePopolare.getDataNascita(),"MM"))%>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_NASCITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lGiudicePopolare.getDataNascita(),"yyyy"))%>" 
									 type="text" size="4" maxlength="4" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_NASCITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)">

				<!-- MEV 15 - Revisione SIGE -->
				<a href="javascript:calendario('LoadInserisciGiudicePopolare','<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_NASCITA%>');">
		      	    <img src="/images/calendario.gif" border=0>
		       	</a>
          </td>
        </tr>
   
			 <tr>
					<td class="l">Stato di Nascita <font class=ob>(*)</font></td>
					<td class="L">
        		<select title="Stato di Nascita" 
										name="<%=ICostantiGiudicePopolare.CAMPO_COD_STATO_NASCITA%>" onChange="javascript:onChangeStatoNascita()">
         			<%=elencoNazioni%>
        		</select>
					</td>
			</tr>
    
   		<tr>
        	<td class="l">Comune Nascita <font class=ob>(*)</font></td>
        	<td class="L">
          	<input title="Comune di Nascita" 
									 value="<%=StringUtils.toStringJSP(lGiudicePopolare.getDescrComuneNascita())%>" 
								 	 type="text" name="<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>"
								 	 maxlength="35" size="35" >
          	<a href="Javascript:ListaComuni('LoadInserisciGiudicePopolare','<%=ICostantiGiudicePopolare.CAMPO_COD_COMUNE_NASCITA%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
			
			<tr>
				<td class="l">Comune Nascita Estero</td>
				<td class="L">
					<input title="Comune di Nascita Estero" 
								 value="<%=StringUtils.toStringJSP(lGiudicePopolare.getComuneEsteroNascita())%>" 
								 type="text" maxlength="35" size="35"
        				 name="<%=ICostantiGiudicePopolare.CAMPO_COMUNE_ESTERO_NASCITA%>">
				</td>
			</tr>

      <tr>
      	<td class="l">Indirizzo</td>
        <td class="l">
        	<input value="<%=StringUtils.toStringJSP(lGiudicePopolare.getIndirizzo())%>" 
								 type="text" name="<%=ICostantiGiudicePopolare.CAMPO_INDIRIZZO%>" 
								 maxlength="35" size="35">
        </td>
      </tr>
      
			<tr>
      	<td class="l">Ruolo <font class=ob>(*)</font></td>
        <td class="l">
        	<select title="Ruolo" name="<%=ICostantiGiudicePopolare.CAMPO_COD_RUOLO%>">
            <%=elencoRuoli%>
          </select>
        </td>
       </tr>
	
		<%// Presenta elenco sezioni se esse esistono. 
		if( elencoSezioni.length() != 0 ) { %>
			<tr>
      	<td class="l">Sezione <font class=ob>(*)</font></td>
      		<td class="l">
        		<select title="Sezione" name="<%=ICostantiGiudicePopolare.CAMPO_SEZ_ID_SEZIONE%>">
        			<%=elencoSezioni%>
        		</select>
      		</td>
    	</tr>
		<%}%>

			<tr>
      	<td class="l">Data Inizio Validità <font class=ob>(*)</font></td>
        	<td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataInizioValidita(),"dd")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataInizioValidita(),"MM")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataInizioValidita(),"yyyy")) %>" 
									 type="text" size="4" maxlength="4" 
									 name="<%= ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)" >

				<!-- MEV 15 - Revisione SIGE -->
				<a href="javascript:calendario('LoadInserisciGiudicePopolare','<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
		      	    <img src="/images/calendario.gif" border=0>
		       	</a>
          </td>
        </tr>
        
				<tr>
          <td class="l">Data Fine Validita</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataFineValidita(),"dd")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_FINE_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataFineValidita(),"MM")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%= ICostantiGiudicePopolare.CAMPO_MESE_DATA_FINE_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lGiudicePopolare.getDataFineValidita(),"yyyy")) %>" 
									 type="text" size="4" maxlength="4" 
									 name="<%= ICostantiGiudicePopolare.CAMPO_ANNO_DATA_FINE_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)" >

				<!-- MEV 15 - Revisione SIGE -->
				<a href="javascript:calendario('LoadInserisciGiudicePopolare','<%=ICostantiGiudicePopolare.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiGiudicePopolare.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
		      	    <img src="/images/calendario.gif" border=0>
		       	</a>
          </td>
        </tr>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
        <input type="HIDDEN" name="<%=ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE%>" 
							 value="<%=lGiudicePopolare.getIdGiudicePopolare()%>">
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciGiudicePopolare");
        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
   </body>
</html>