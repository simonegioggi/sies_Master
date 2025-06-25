<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="autoritaEmi2" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmiCumulo" scope="request" class="java.lang.String"/>

<!-- listaTipoProvvOrdinanza lista usata solo a livello grafico voluta dal cliente 
	INFORMAZIONE NON VIENE SALVATA SUL DB DALLA LISTA listaTipoProvvOrdinanza MA DAL RADIO BUTTON -->
<jsp:useBean id="listaTipoProvvCumulo" scope="request" class="java.lang.String"/>

<!-- segnalazioni 4: modificato il titolo -->
<title>[S.I.E.S.] - Inserimento Cumulo</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
	// segnalazioni 4: modificato il nome funzione
	function VerifyCUMULO() {
		if (document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
			document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          	document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          	document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
		// segnalazioni 4: aggiunti controlli preventivi
		if (document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%> &&
        		document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
          	document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
        if (document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%> &&
        		document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
          	document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

        // Data Emissione Provvedimento
        var d2 = document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (!ControllaData(d2)) {
          	alert('Data Emissione Provvedimento non valida');
          	return false;
        }
	}

	function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
		desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	}
</script>

<%
	SentenzaModel lSentenza = new SentenzaModel();
	String lAction = new String();
	if (modalita.equals("I")) {
		lAction = "siap.siep.sentenza.action.ActInserisciSentenzaAltriTitoli";
	}
	else if (modalita.equals("M")) {
		// segnalazioni 4: modificata action di modifica
		lAction = "siap.siep.sentenza.action.ActModificaSentenzaAltriTitoli";
		lSentenza = new SentenzaModel(sentenza);
	}
%>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenzaCumulo">
	<table cellspacing=2 cellpadding=2>
   		<tr><td class="Titolo" colspan=4>Estremi del provvedimento</td></tr>
 		<tr>
        	<td class="l">Tipo di Provvedimento</td>
        	<td class="l">
          		<select title="tipoProvvedimento">
			  		<%=listaTipoProvvCumulo%>          
		  		</select>
        	</td>
    	</tr>
    	<tr>
      		<td class="l">Data Emissione Provvedimento <font class="ob">(*)</font></td>
         	<td class="L" colspan=3>
	            <input Title="Data Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            -
	            <input Title="Data Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            -
	            <input Title="Data Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	
				<!-- MEV 15 - Revisione SIGE -->
				<a href="javascript:calendario('LoadInserisciSentenzaCumulo','<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>');">
	       			<img src="/images/calendario.gif" border=0>
	        	</a>
          	</td>
		</tr>

		<tr>
      		<td class="l">Anno/Numero SIEP<font class="ob">(*)</font></td>
      		<td class="L">
          		<input Title="Anno Provvedimento" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoProvvedimento()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_PROVVEDIMENTO %>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
         		/<input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroProvvedimento()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_PROVVEDIMENTO %>" maxlength="6" size="6" onkeypress="return TicTabNumField(this,event)">
      		</td>
		</tr>

    	<tr>
      		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      		<td class="L" colspan=3>
	        	<select id="autoritaEmittente" Title="Autorità Emittente" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" >
	          		<%= autoritaEmiCumulo %>
	          	</select>
      		</td>
		</tr>

   		<tr>
	  		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      		<td class="L"  colspan=3>
         		<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
            		value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35">
 		      		<a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenzaCumulo','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenzaCumulo.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          				<img src="/images/filefolder.gif" border=0>
          			</a>
      		</td>
		</tr>
<%--
    	<tr>
     		<td class="l">Sezione Emittente </td>
      		<td class="L" colspan=3>
          		<input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30" >
      		</td>
		</tr>
--%>
		<tr>
	  		<td class="l">Note</td>
      		<td class="L" colspan=3>
        		<textarea cols=80 rows=5 Title="Note" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
      		</td>
		</tr>
	</table>

	<%
	if (modalita.equals("M")) {
	%>	
		<jsp:include page="<%=ICostantiFasSigeSentenza.INC_FASCICOLI_SIGE%>"/>
	<%
	}
	%>

	<table cellspacing=2 cellpadding=2>	
		<tr>
			<td colspan=2>
			<br>
				<!-- segnalazioni 4: modificato il nome della funzione js invocata -->
	        	<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma" onclick="Javascript: return VerifyCUMULO();">
	      	</td>
		</tr>
	</table>

	<input type="HIDDEN" name="Action" value="<%=lAction%>" >
	<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>">

	<%-- Hidden aggiunte --%>
	<%
	if (modalita.equals("M")) {
	%>
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"MM"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"yyyy"))%>"> 
	<%
	} else {
	%>
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"dd"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"MM"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy"))%>"> 
	<%
	}
	%>
	<%-- Fine Hidden aggiunte --%>

	<input type="HIDDEN" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_PROVVEDIMENTO%>" value="13">
</form>
<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("LoadInserisciSentenzaCumulo");

	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della Data Emissione Provvedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della Data Emissione Provvedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della Data Emissione Provvedimento è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
<%-- 	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic"); --%>

	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_PROVVEDIMENTO %>","req","L'Anno SIEP è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_PROVVEDIMENTO %>","numeric");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_PROVVEDIMENTO %>","gt=1900");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_PROVVEDIMENTO %>","req","Il Numero SIEP è obbligatorio");
	frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_PROVVEDIMENTO %>","alfanumeric");

	// segnalazioni 4: modificato il nome funzione
	frmvalidator.setAddnlValidationFunction("VerifyCUMULO");
</script>