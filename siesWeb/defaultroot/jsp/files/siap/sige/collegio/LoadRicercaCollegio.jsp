<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>

<!-- intervento per 11.2.1 rimuovo elenco collegi ed aggiungo elenco giudici -->
<jsp:useBean id="elencoGiudici"	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni"	scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Load Ricerca Collegio </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
    // [SG] 20190507: commentata funzione
	// 	function init() {
	<%-- 		document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>.focus(); --%>
	// 	}

			// Controllo formale della data di inizio validità.
	function checkDate() {
		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		var dataCreazioneCollegio = document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
  			document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
  			document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
		if (dataCreazioneCollegio.length > 2) {
			if (!ControllaData(dataCreazioneCollegio)) {
				alert('Data Creazione Collegio non corretta.');
    				document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.focus();
				return false;
			}
			if (!CompareDate(dataCreazioneCollegio, data_sistema)) {
		        alert('Data Creazione Collegio non può essere superiore alla data di sistema!');
		        return false;
	     	}
		}
		var dataUdienzaDal = document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value
				+ '/' + document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value
				+ '/'+ document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
		var dataUdienzaAl = document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value
				+ '/' + document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_FINALE%>.value
				+ '/'+ document.LoadRicercaCollegio.<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
		if (dataUdienzaDal.length > 2 || dataUdienzaAl.length > 2) {
	     	if (!ControllaData(dataUdienzaDal)) {
	        	alert('Data Udienza Dal non valida!');
	        	return false;
	     	} else if (!ControllaData(dataUdienzaAl)) {
		     	alert('Data Udienza Al non valida!');
		     	return false;
	     	} else if (!CompareDate(dataUdienzaDal, dataUdienzaAl)) {
		        alert('Data Udienza Al < Data Udienza Dal!');
		        return false;
	     	} else if (!CompareDate(dataUdienzaDal, data_sistema)) {
		        alert('Data Udienza Dal non può essere superiore alla data di sistema!');
		        return false;
	     	} else if (!CompareDate(dataUdienzaAl, data_sistema)) {
		     	alert('Data Udienza Al non può essere superiore alla data di sistema!');
		     	return false;
    			}
		  	}
		return true;
			}

	function Verify() {
        // Controllo formale della data di validità.
		if (!checkDate())
        	return false;
      		    
        return true;
      }

	function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
  </head>

<body class="corpo"><%-- onload="Javascript:init();" --%>
    <table>
    <tr>
			<td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
				</a>
			</td>
    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    		<font class="campo">Ricerca Collegio</font>
    	</td>
    </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaCollegio">
      <table cellspacing=4 cellpadding=4>
				<tr>
    			<td class="l">Giudice (Presidente Collegio)</td>
      		<td class="l">
      			<select title="codici" name="<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>">
        		<%=elencoGiudici%>
        		</select>
      		</td>
    		</tr>
				<% if( elencoSezioni.length() != 0 ) { %>
				<tr>
      		<td class="l">Sezione</td>
      		<td class="l">
        		<select title="Sezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>">
        			<%=elencoSezioni%>
        		</select>
      		</td>
<!--  
					&nbsp;
					<td class="l">oppure Tutti</td>
          <td class="l">
						<input type="checkbox" name="flagTutti" value=1 onClick="Javascript:onClickCheckBox();">
					</td>
    		</tr>
-->
				<tr>
				<% } %>
      		<td class="l">Data Creazione Collegio</td>
        		<td class="l"> 
            	<input type="text" size="2" maxlength="2" title="Giorno Data Creazione Collegio"
									 name="<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            	/
            	<input type="text" size="2" maxlength="2" title="Mese Data Creazione Collegio"
									 name="<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillDM(value)">
            	/
            	<input type="text" size="4" maxlength="4" title="Anno Data Creazione Collegio"
									 name="<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>" 
									 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
									 onBlur="javascript:value=FillYear(value)">

						<!-- MEV 15 - Revisione SIGE -->
						<a href="javascript:calendario('LoadRicercaCollegio','<%=ICostantiCollegio.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiCollegio.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiCollegio.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
				      	    <img src="/images/calendario.gif" border=0>
				       	</a>
          	</td>
        </tr>				
        
        <!-- inizio intervento per 11.2.1 -->
				<tr>
        	<td class="l">Data Udienza Dal</td>
        	<td class="l"> 
          		<input type="text" title="Giorno Data Udienza Dal" name="<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          		<input type="text" title="Mese Data Udienza Dal" name="<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          		<input type="text" title="Anno Data Udienza Dal" name="<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          		<a href="javascript:calendario('LoadRicercaCollegio','<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>','<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_INIZIALE%>','<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>');">
					<img src="/images/calendario.gif" border=0>
		  		</a> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          		<font class="label">Data Udienza Al</font>
          <td class="l">
				<input type="text" title="Giorno Data Udienza Al" name="<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
				<input type="text" title="Mese Data Udienza Al" name="<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
				<input type="text" title="Anno Data Udienza Al" name="<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           		<a href="javascript:calendario('LoadRicercaCollegio','<%=ICostantiCollegio.CAMPO_ANNO_ISCRIZIONE_FINALE%>','<%=ICostantiCollegio.CAMPO_MESE_ISCRIZIONE_FINALE%>','<%=ICostantiCollegio.CAMPO_GIORNO_ISCRIZIONE_FINALE%>');">
					<img src="/images/calendario.gif" border=0>
		  		</a> 
					</td>
				</tr>	
        <!-- fine intervento per 11.2.1 -->

		<tr>
			<td class="l">Data fine validità non valorizzata</td>
			<td class="l"><input type=checkbox name="flagDataFine@Null" value=1></td>
		</tr>	
        <tr>
			<td><input class="bottone" type="submit" value="Conferma"></td>
        </tr>
      </table>
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.collegio.action.ActRicercaCollegio">
      </form>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadRicercaCollegio");
        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
   </body>
</html>