<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siepe.richiesta.model.RichiestaModel"%>
<%@ page import="siap.siepe.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>

<jsp:useBean id="elencoTipiRichiestaSoggetto"			scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoTipiRichiestaUfficio"			scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoTipiRichiedenteSoggetto" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoTipiRichiedenteUfficio" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoDestinatari" 							scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" 												scope="request" class="java.lang.String"/>
<jsp:useBean id="richiesta" 											scope="request" class="siap.siepe.richiesta.model.RichiestaModel"/>
<jsp:useBean id="tipoChecked"          						scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" 												scope="request"	class="java.lang.String"/>

<html>
<head>
	<title>[S.I.E.S.] - Inserimento Richiesta </title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	<script language="JavaScript" src="/html/verifyCombo.js"></script>

	<!-- JS per elenco delle sedi d'ufficio  -->
	<script language="JavaScript">
		// Elenco delle sedi ufficio.
		function ListaComuni(a_formname,a_fieldname)
		{
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
		}
	</script>

	<!-- JS per controllo date  -->
	<script language="JavaScript">
			// Controllo di verifica.
			function Verify()
    	{
      	var ritorno = true;

					if( !controlloDate() )
					{
						ritorno = false;
						return ritorno;
					}
					else if ( !checkMandatoryField() )
					{
						ritorno = false;
						return ritorno;
					}

				return ritorno;
    	}
  		// Controllo delle date.
   		function controlloDate()
   		{
      	var ret = true;
      	var gg = FillDM(document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_GIORNO_DATA_RICHIESTA%>.value);
      	var mm = FillDM(document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_MESE_DATA_RICHIESTA%>.value);
      	var aa = document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_ANNO_DATA_RICHIESTA%>.value;

      	var dataRichiesta = gg + "/" + mm + "/" + aa;

				// Controlli sulla data
      	if (dataRichiesta.length < 10 )
      	{
      		ret = false;
					alert ("Data di Richiesta mancante");
      	}
      	else if (ControllaData (dataRichiesta) == false)
      	{
      		ret = false;
					alert ("Errore nella data : " + dataRichiesta);
      	}
				return ret;
   		}

      //																																							//
      // Controllo dell'obbligatorietà dei campi, verificando se il tipo richiedente	//
			// sia un soggetto oppure l'ufficio.																						//
			//																																							//
			function checkMandatoryField()
			{
				// Esegue il controllo di obbligatorietà del campo del Richidente.
        if ( 	( document.LoadInserisciRichiesta.tipo[0].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[0].value == '-' ) ||
							( document.LoadInserisciRichiesta.tipo[1].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[1].value == '-' ) )
        {
        		alert("Il Richiedente è un campo obbligatorio !");
          	return false;
        }

				// Esegue il controllo di obbligatorietà dl campo del tipo di Richiesta.
        if	( ( document.LoadInserisciRichiesta.tipo[0].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[0].value == '-' ) ||
						  ( document.LoadInserisciRichiesta.tipo[1].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[1].value == '-' ) )
        {
        		alert("Il Tipo Richiesta è un campo obbligatorio !");
          	return false;
        }

        // Esegue il controllo di obbligatorietà del campo del Destinatario.
        if	( ( document.LoadInserisciRichiesta.tipo[0].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].value == '-' ) ||
						  ( document.LoadInserisciRichiesta.tipo[1].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].value == '-' ) )
        {
        		alert("Il Destinatario è un campo obbligatorio !");
          	return false;
        }

        // Esegue il controllo di obbligatorietà del campo della Sede.
        if	( ( document.LoadInserisciRichiesta.tipo[0].checked &&  document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[0].value == '' ) ||
						  ( document.LoadInserisciRichiesta.tipo[1].checked && document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[1].value == '' ) )
        {
        		alert("La Sede è un campo obbligatorio !");
          	return false;
        }

				return true;

			}
	</script>

	<!-- JS per la gestione del cambioTipo con le DIV -->
	<script language="JavaScript">
		// funzione che esegue lo switcth tra div. Il parametro passato come Boolean indica
		// se innescare la pulizia deicampi ( false = no; true = si )
		function cambiaTipo( flagPulizia )
  	{
			var nodeDivSoggetto;
			var nodeDivUfficio;

      nodeDivSoggetto = document.getElementById("divSoggetto");
      nodeDivUfficio = document.getElementById("divUfficio");

			if(document.LoadInserisciRichiesta.tipo[0].checked)
      {
      	if( flagPulizia )
					pulisci();

				nodeDivSoggetto.style.visibility='visible';
  			nodeDivSoggetto.disabled='';// Disattiva la disabilitazione

      	document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[0].disabled=false;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[0].disabled=false;
      	document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].disabled=false;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[0].disabled=false;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_NOTE%>[0].disabled=false;

				nodeDivUfficio.style.visibility='hidden';
				nodeDivUfficio.disabled = 'x'; // Attiva la disabilitazione

      	document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[1].disabled=true;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[1].disabled=true;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].disabled=true;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[1].disabled=true;
      	document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_NOTE%>[1].disabled=true;
      }
      else if (document.LoadInserisciRichiesta.tipo[1].checked )
      {
      	if( flagPulizia )
					pulisci();

        nodeDivSoggetto.style.visibility='hidden';
        nodeDivSoggetto.disabled='x'; // Attiva la disabilitazione

        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[0].disabled=true;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[0].disabled=true;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].disabled=true;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[0].disabled=true;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_NOTE%>[0].disabled=true;

        nodeDivUfficio.style.visibility='visible';
        nodeDivUfficio.disabled=''; // Disattiva la disabilitazione

        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[1].disabled=false;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[1].disabled=false;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].disabled=false;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[1].disabled=false;
        document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_NOTE%>[1].disabled=false;
      }
      else
			{
      	if( flagPulizia )
					pulisci();

				nodeDivSoggetto.style.visibility='visible';
      	nodeDivUfficio.style.visibility='hidden';
      }

			document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_GIORNO_DATA_RICHIESTA%>.focus();
    }

		// Esegue la pulizia dei campi
		function pulisci()
    {
			<%
      if( modalita.equalsIgnoreCase("I")) // Abilita la pulizia dei campi solo se è in fase d'inserimento
      {
			%>
    		document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_GIORNO_DATA_RICHIESTA%>.value='';
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_MESE_DATA_RICHIESTA%>.value='';
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_ANNO_DATA_RICHIESTA%>.value='';

				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[0].selectedIndex=0;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>[1].selectedIndex=0;

				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[0].selectedIndex=0;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>[1].selectedIndex=0;

    		document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].selectedIndex=0;
				document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].selectedIndex=0;

    		document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_SEDE%>[0].value='';
    		document.LoadInserisciRichiesta.<%=ICostantiRichiesta.CAMPO_NOTE%>[1].value='';
			<%
      }
			%>
    }
	</script>

</head>
	<body class="corpo" onLoad="cambiaTipo(false);">
  	<table>
    	<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<%
        RichiestaModel lRichiesta = new RichiestaModel();
        String lAzione = new String();

        if( modalita.equals("I") )
        {
        	lAzione = "siap.siepe.richiesta.action.ActInserisciRichiesta";
				%>
  				<font class="campo">Inserimento Richiesta</font>
  			<%
				}
				else if( modalita.equals("M") )
				{
					lAzione = "siap.siepe.richiesta.action.ActModificaRichiesta";
      		lRichiesta = richiesta;
  			%>
        	<font class="campo">Modifica Richiesta</font>
  			<%
				}
				%>
			</td>

		</tr>
	</table>

	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRichiesta">
		<jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
 		<br>
		<table cellspacing="2" cellpadding="2" width="90%">
			<!-- Data Richiesta -->
			<tr>
  			<td class="l">Data Richiesta(*) &nbsp; <!--</td>-->
      	<!--<td class="l">-->
        	<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lRichiesta.getDataRichiesta(),"dd") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiRichiesta.CAMPO_GIORNO_DATA_RICHIESTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    			/
    			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lRichiesta.getDataRichiesta(),"MM") )%>" type="text" size="2" maxlength="2" name="<%=ICostantiRichiesta.CAMPO_MESE_DATA_RICHIESTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
    			/
    			<input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lRichiesta.getDataRichiesta(),"yyyy") )%>" type="text" size="4" maxlength="4" name="<%=ICostantiRichiesta.CAMPO_ANNO_DATA_RICHIESTA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      	</td>
    	</tr>

      <tr>
				<td class="Titolo" >Richiesta presentata da</td></tr>
      	<td class="c">Soggetto &nbsp;
        	<input type="radio" name="tipo" value="soggetto" <%= ( tipoChecked.equals("") || tipoChecked.equals("S") ) ? "checked" : "" %>  onClick="cambiaTipo(true);">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ufficio &nbsp; <input type="radio" name="tipo" value="ufficio"  <%= ( tipoChecked.equals("U") ) ? "checked" : "" %> onClick="cambiaTipo(true);">
       	</td>
    	</tr>

			<tr>
				<td>
					<!--[ Gestione della parte di richiesta Effettuata dal soggetto ]-->
					<!-- Tipo Richiedente Soggetto -->
					<div id="divSoggetto" style="visibility:hidden; position:relative; top:0px; width:100%;">
						<table cellspacing="2" cellpadding="2" width="90%">
						<tr>
							<td class="l" >Presentata da (*)&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td class="l" >
    						<select title="tipoRichiedente" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>">
    							<%=elencoTipiRichiedenteSoggetto%>
    						</select>
							</td>
  					</tr>
      			<!-- Tipo Richiesta -->
						<tr>
    					<td class="l">Tipo Richiesta (*)</td>
    					<td class="l">
    						<select title="tipoRichiesta" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>">
    							<%=elencoTipiRichiestaSoggetto%>
    						</select>
            	</td>
  					</tr>
          <tr>
						<td class="l" >Destinatario (*)</td>
						<td class="l">
    					<select title="tipoDestinatario" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
    					<%=elencoDestinatari%>
    					</select>
						</td>
  			</tr>

				<!-- Sede dell'ufficio destinatario -->

				<tr>
					<td class="l">Sede (*)</td>
    			<td class="l">
    				<input Title="Sede " name="<%=ICostantiRichiesta.CAMPO_SEDE%>" value="<%=lRichiesta.getDescrSedeUfficioDestinatario()%>" type="text" maxlength="35" size="35">
        			<a href="Javascript:ListaComuni('LoadInserisciRichiesta','<%=ICostantiRichiesta.CAMPO_SEDE%>[0]');">
        			<img src="/images/filefolder.gif" border=0> </a>
    			</td>
  			</tr>

				<!-- Campo delle note -->
				<tr>
    			<td class="l">Note </td>
    			<td class="l"><TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE %>" cols="40" rows="4" ><%=StringUtils.toStringJSP(lRichiesta.getNote())%></textarea></td>
  			</tr>

				<!-- Pulsante di invio dati -->
				<tr>
      		<td>
      			<input class="bottone" type="submit" value="Conferma" >
      		</td>
    		</tr>

      	</table>
      	</div>
      	<!-- [Fine] -->
				</td>
			</tr>

    	<tr>
      	<td>

				<!--[ Gestione della parte di richiesta Effettuata dall'ufficio ]-->
				<!-- Tipo Richiedente Ufficio -->
				<div id="divUfficio" style="visibility:hidden; position:relative; top:-223px; width:100%;">
				<table cellspacing="2" cellpadding="2" width="90%">
					<tr>
						<td class="l">Presentata da (*)&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="l">
    					<select title="tipoRichiedente" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIEDENTE%>">
    						<%=elencoTipiRichiedenteUfficio%>
    					</select>
						</td>
  				</tr>
					<!-- Tipo Richiesta -->
					<tr>
    				<td class="l">Tipo Richiesta (*)</td>
    				<td class="l">
    					<select title="tipoRichiesta" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_RICHIESTA%>">
    						<%=elencoTipiRichiestaUfficio%>
    					</select>
						</td>
  				</tr>

        	<tr>
          <td class="l" >Destinatario (*)</td>
          <td class="l">
    				<select title="tipoDestinatario" name="<%=ICostantiRichiesta.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
    					<%=elencoDestinatari%>
    				</select>
					</td>
  		</tr>

			<!-- Sede dell'ufficio destinatario -->
			<tr>
				<td class="l">Sede (*)</td>
    		<td class="l">
    			<input Title="Sede " name="<%=ICostantiRichiesta.CAMPO_SEDE%>" value="<%=lRichiesta.getDescrSedeUfficioDestinatario()%>" type="text" maxlength="35" size="35">
        		<a href="Javascript:ListaComuni('LoadInserisciRichiesta','<%=ICostantiRichiesta.CAMPO_SEDE%>[1]');">
        		<img src="/images/filefolder.gif" border=0> </a>
    		</td>
  		</tr>

			<!-- Campo delle note -->
			<tr>
    		<td class="l">Note </td>
    		<td class="l"><TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE %>" cols="40" rows="4" ><%=StringUtils.toStringJSP(lRichiesta.getNote())%></textarea></td>
  		</tr>

			<!-- Pulsante di invio dati -->
			<tr>
      	<td>
      		<input class="bottone" type="submit" value="Conferma" >
      	</td>
    	</tr>

      </table>
    	</div>
      <!-- [Fine] -->
			</td>

			</tr>

			<!-- Destinatario -->
		</table>
	  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
		<input type="HIDDEN" name="<%=ICostantiRichiesta.CAMPO_ID_RICHIESTA%>" value="<%=lRichiesta.getIdRichiesta()%>" >
		<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">

	</form>

	<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("LoadInserisciRichiesta");
		frmvalidator.setAddnlValidationFunction("Verify");
  	frmvalidator.addValidation("<%=ICostantiRichiesta.CAMPO_GIORNO_DATA_RICHIESTA%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiRichiesta.CAMPO_MESE_DATA_RICHIESTA%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiRichiesta.CAMPO_ANNO_DATA_RICHIESTA%>","numeric");
  	frmvalidator.addValidation("<%=ICostantiRichiesta.CAMPO_ANNO_DATA_RICHIESTA%>","minlen=4","La lunghezza del campo Anno Richiesta deve essere di 4 caratteri");
 	</script>

	</body>
</html>