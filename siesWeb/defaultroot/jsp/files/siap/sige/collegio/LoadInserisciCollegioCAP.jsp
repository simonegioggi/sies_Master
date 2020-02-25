<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sige.collegio.model.CollegioModel"%>
<%@ page import="siap.sige.collegio.util.CollegioUtils"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.giudicepopolare.action.ICostantiGiudicePopolare"%>

<jsp:useBean id="modalita"			  scope="request" class="java.lang.String"/>
<jsp:useBean id="collegio"   		  scope="request" class="siap.sige.collegio.model.CollegioModel"/>
<jsp:useBean id="elencoSezioni"		  scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoCodiciCollegi" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Collegio </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript">
    	var DataIni= "";

			function init()
			{
		<%
				if( modalita.equalsIgnoreCase("I") )
				{
		%>
					document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.focus();
		<%
				}
		%>
			}
			
			// Elenco popup dei Magistrati.
			function ListaMagistrati(a_formname, a_idfieldnum)
			{
				var desktop;
			 	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&idfieldnum="+a_idfieldnum, 
					 									 	"Ricerca_WMagistrato",
					 									 	"toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
			}
			 
			// Elenco popup dei Giudici Popolari.
			function ListaGiudiciPopolari(a_formname, a_idfieldnum)
			{
			 	var desktop;
			 	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.giudicepopolare.action.ActLoadRicercaGiudicePopolareLista&formname="+a_formname+"&idfieldnum="+a_idfieldnum, 
					 									  "Lista_Giudice_Popolare",
					 									  "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
			} 

			// Data inizio.
      function initDataIni(data)
      {
        DataIni = data;
      }

			// Controlla obbligatorietà del codice Collegio. 
			function checkObblCodice()
			{ 			
			  if (document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.value.length == 0)
			  {
			  	alert("Occorre selezionare il codice.");
			  	document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.focus();
			    return false;
			  }
			  return true;
			}
			
			// Controlla obbligatorietà della Sezione.
			function checkObblSezione()
			{ 
				if (document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>.value.length == 0)
			  {
			  	alert("Occorre selezionare la sezione.");
			  	document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>.focus();
			    return false;
			  }
				return true;
			}		
		
			// Controllo obbligatorietà dei Magistrati.
			function checkObblMagistrati()
			{
				if( document.LoadInserisciCollegio.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>[0].value.length == 0 )
				{
					alert("Occorre selezionre il Presidente.");
					return false;
				}
			
				return true;
			}
		
			// Verifica date di validità
			function checkDataValidita()
			{
				var ritorno = true;
		    var dataInizioValidita = 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
		        											
		    var dataFineValidita = 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_MESE_DATA_FINE_VALIDITA%>.value +'/'+ 
		    		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

		    if (dataInizioValidita.length > 2)
		    {
		    	if (!ControllaData(dataInizioValidita))
		      {
		      	alert('Data di Inizio Validità non corretta');
				  	document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		        ritorno = false;
		      }
		      else if (!CompareDate(DataIni, dataInizioValidita))
		      {
		     		alert('Data di Inizio Validità non può essere anticipata');
				  	document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		        ritorno = false;
		      }
		      else if (dataFineValidita.length > 2)
		      {
		      	if (!ControllaData(dataFineValidita))
		        {
		        	alert('Data di Fine Validità non corretta');
				  		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.focus();
		          ritorno = false;
		        }
		        else if (!CompareDate(dataInizioValidita, dataFineValidita))
		       	{
		        	alert('Data di Inizio Validità non può essere successiva a quella di Fine');
				  		document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
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
		    	document.LoadInserisciCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
		     	ritorno = false;
			  }
				return ritorno;
			}
			
			// Verify
			function Verify()
			{
				// Controllo Obbl. del Codice.
				if( !checkObblCodice() )
					return false;

				// Controllo Obbl. della Sezione.
				<%// Se esistono sezioni esegue il conrollo di obbl.
					if( elencoSezioni.length() != 0 ) {%>
					if( !checkObblSezione() )
						return false;
				<%}%>

				// Controllo Obbl. del Magistrato.
				if( !checkObblMagistrati() )
					return false;

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
      CollegioModel lCollegio = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        lCollegio = new CollegioModel();
        lAzione = "siap.sige.collegio.action.ActInserisciCollegio";
        lCollegio.setDataInizioValidita(DateUtils.getSysDate());
    %>
    		<font class="campo">Inserimento Collegio</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lCollegio = collegio;
        lAzione = "siap.sige.collegio.action.ActModificaCollegio";
        if(lCollegio.getDataInizioValidita() == null)
          lCollegio.setDataInizioValidita(DateUtils.getSysDate());
    %>
    		<font class="campo">Modifica Collegio</font>
    <%
      }
    %>
    		</td>
   		<!-- BOTTONE DI RITORNO -->
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>   		
    	</tr>
    	<script language="JavaScript">
      	initDataIni("<%=StringUtils.toStringJSP(DateUtils.getDateToString( lCollegio.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
    	</script>
    </table>

		<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCollegio">
      <table cellspacing=4 cellpadding=4>
<%
		if( modalita.equals("I") )
		{
%>
			<tr>
    		<td class="l">Numero <font class=ob>(*)</font></td>
      	<td class="l">
      		<select title="codiciCollegio" name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>" >
        		<%=elencoCodiciCollegi%>
        	</select>
      	</td>
    	</tr>

	<%// Presenta elenco sezioni se esse esistono. 
		if( elencoSezioni.length() != 0 ) { %>
			<tr>
    		<td class="l">Sezione <font class=ob>(*)</font></td>
      	<td class="l">
      		<select title="codiciSezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>">
        		<%=elencoSezioni%>
        	</select>
      	</td>
    	</tr>
	<% } %>
<%
		}
		else
		{
%>		
			<tr>
    		<td class="l">Numero</td>
      	<td class="l">
      		<font class="campo"><%=lCollegio.getCodCollegio()%></font>
					<input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>" 
								 value="<%=lCollegio.getCodCollegio()%>" >
      	</td>
    	</tr>

	<% if( elencoSezioni.length() != 0 ) { %>
			<tr>
    		<td class="l">Sezione</td>
      	<td class="l">
      		<font class="campo"><%=lCollegio.getSezione().getDescrizione()%></font>
					<input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" 
								 value="<%=lCollegio.getSezIdSezione()%>">
      	</td>
    	</tr>
	<% } %>
<% 
		}
%>

			<tr>
				<td class="l">Presidente</font></td>
				<td class="L">
					<input title="Cognome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 0) ? 
								     collegio.getCollegioMagistrati()[0].getMagistrato().getCognome() : "" %>">
					<input title="Nome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25" 
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 0) ? 
								     collegio.getCollegioMagistrati()[0].getMagistrato().getNome() : "" %>">
					<input title="CodMagistrato" type="hidden" 
								 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" 
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 0) ? 
								     collegio.getCollegioMagistrati()[0].getMagistrato().getCodMagistrato() : "" %>">
					<a href="Javascript:ListaMagistrati('LoadInserisciCollegio', '0');">
						<img src="/images/filefolder.gif" border=0>
					</a>
				</td>
			</tr>

		 	<tr>
				<td class="l">Consigliere</td>
			  <td class="L">
			  	<input title="Cognome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
								 value="<%=CollegioUtils.isValidSize(collegio.getCollegioMagistrati(), 1) ? 
								     collegio.getCollegioMagistrati()[1].getMagistrato().getCognome() : "" %>">
			    <input title= "Nome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25"
								 value="<%=CollegioUtils.isValidSize(collegio.getCollegioMagistrati(), 1) ? 
								     collegio.getCollegioMagistrati()[1].getMagistrato().getNome() : "" %>">
					<input title="CodMagistrato" type="hidden" 
								 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"
								 value="<%=CollegioUtils.isValidSize(collegio.getCollegioMagistrati(), 1) ? 
								     collegio.getCollegioMagistrati()[1].getMagistrato().getCodMagistrato() : "" %>">
			     <a href="Javascript:ListaMagistrati('LoadInserisciCollegio','1');">
			        	<img src="/images/filefolder.gif" border=0>
			     </a>
			    </td>
			 </tr>

			 <tr>
    		<td class="l">Consigliere</td>
			  <td class="L">
			  	<input title="Cognome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" 
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 2) ? 
								     collegio.getCollegioMagistrati()[2].getMagistrato().getCognome() : "" %>">
			    <input title= "Nome" readonly type="text" 
								 name="<%=ICostantiMagistrato.CAMPO_NOME%>" maxlength="35" size="25"
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 2) ? 
								     collegio.getCollegioMagistrati()[2].getMagistrato().getNome() : "" %>">
					<input title="CodMagistrato" type="hidden" 
								 name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"
								 value="<%=CollegioUtils.isValidSize( collegio.getCollegioMagistrati(), 2) ? 
								     collegio.getCollegioMagistrati()[2].getMagistrato().getCodMagistrato() : "" %>">
         		<a href="Javascript:ListaMagistrati('LoadInserisciCollegio','2');">
           		<img src="/images/filefolder.gif" border=0>
         		</a>
       	</td>
    	</tr>

			<tr>
      	<td class="l">Data Inizio Validità</td>
        	<td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataInizioValidita(),"dd")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataInizioValidita(),"MM")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataInizioValidita(),"yyyy")) %>" 
									 type="text" size="4" maxlength="4" 
									 name="<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)" >
			
			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciCollegio','<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
          
          </td>
        </tr>
        
				<tr>
          <td class="l">Data Fine Validità</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataFineValidita(),"dd")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%=ICostantiCollegio.CAMPO_GIORNO_DATA_FINE_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataFineValidita(),"MM")) %>" 
									 type="text" size="2" maxlength="2" 
									 name="<%= ICostantiCollegio.CAMPO_MESE_DATA_FINE_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCollegio.getDataFineValidita(),"yyyy")) %>" 
									 type="text" size="4" maxlength="4" 
									 name="<%= ICostantiCollegio.CAMPO_ANNO_DATA_FINE_VALIDITA %>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)">

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciCollegio','<%=ICostantiCollegio.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiCollegio.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiCollegio.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
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
        <input type="HIDDEN" name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" 
							 value="<%=lCollegio.getIdCollegio()%>">
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciCollegio");
        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
   </body>
</html>