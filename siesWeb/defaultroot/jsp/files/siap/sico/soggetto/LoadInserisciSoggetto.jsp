<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.security.model.ProfileModel"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoCittadinanza" scope="request"	class="java.lang.String"/>
<jsp:useBean id="sesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="dataNascitaPresunta" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="codFunzione" scope="request" class="java.lang.String"/>

<html>
<head>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<title>[S.I.E.S.] - Gestione Soggetto - Inserimento</title>
<script language="JavaScript">
var desktop;
function ListaComuni(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
}
<!-- 20210524	MEV Scheda-21 -->
function ListaComuniNascita(a_formname,a_fieldname) {
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
}      

function calendario(a_formname,a_field_year,a_field_month,a_field_day) {
	desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
}
</script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
</head>
<BODY class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img
					align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class=LBG><font class="label">Funzione :</font>&nbsp; <%
          SoggettoModel lSoggetto = null;
          String lAction = new String();

          if( modalita.equals("I") )
          {
            lSoggetto = new SoggettoModel();
            lAction = "siap.sico.soggetto.action.ActInserisciSoggetto";
      %> <font class="campo">Inserimento Soggetto</font> <%
          }
          else if( modalita.equals("M") )
          {
            lSoggetto =  new SoggettoModel(soggetto);
 			
            // Viene differenziata la funzione di modifica nel caso SIGE
        	ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();
			if (lProfilo != null && lProfilo.isSige())
	         //   Paolo 29/3/2010 lAction = "siap.sico.soggetto.action.ActModificaSoggettoSige";
      				lAction = "siap.sico.soggetto.action.ActModificaSoggetto";
			else
            	lAction = "siap.sico.soggetto.action.ActModificaSoggetto";
      %> <font class="campo">Modifica Soggetto</font> <%
          }
      %></td>
		</tr>
	</table>
	<form method="POST" action="<%=IWebConstants.PG_MAIN%>"
		name="LoadInserisciSoggetto" id="LoadInserisciSoggetto">
		<table cellspacing=2 cellpadding=2>
			<tr>
				<td class="l">Cognome <font class=ob>(*)</font></td>
				<td class="L"><input title="Cognome"
					value="<%=lSoggetto.getCognome() %>" type="text"
					name="<%= ICostantiSoggetto.CAMPO_COGNOME %>" maxlength="35"
					size="35"></td>
				<td class="l">Nome <font class=ob>(*)</font></td>
				<td class="L"><input title="Nome"
					value="<%=lSoggetto.getNome() %>" type="text"
					name="<%= ICostantiSoggetto.CAMPO_NOME %>" maxlength="35" size="35"></td>
			</tr>
			<tr>
				<td class="l">Sesso <font class=ob>(*)</font></td>
				<td class="L"><select title="Sesso"
					name="<%=ICostantiSoggetto.CAMPO_SESSO%>">
						<%= sesso %>
				</select></td>
			</tr>
			<tr>
				<td class="l">Data di nascita</td>
				<td class="L">
					<%
	if(modalita.equals("I")) {
%> <input type="text" title="Giorno Data di nascita"
					name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Mese Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Anno Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> <%
	} else if(modalita.equals("M")) {
		if(lSoggetto.getDataNascita() == null) {
%> <input type="text" title="Giorno Data di nascita"
					name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" value=""
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Mese Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"
					value="<%=StringUtils.toStringJSP( lSoggetto.getMeseNascita() )%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Anno Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>"
					value="<%= StringUtils.toStringJSP( lSoggetto.getAnnoNascita() )%>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> <%
		} else {
%> <input type="text" title="Giorno Data di nascita"
					name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd")) %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Mese Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"MM")) %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Anno Data di nascita"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"yyyy")) %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> <%
		}
	}
%> <%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Iscrizione Soggetto da Iscrizione Manuale
	  if( codFunzione != null && 
	      (codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000) || codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020000) )
	    ){
%> <a
					href="javascript:calendario('LoadInserisciSoggetto','<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>');">
						<img src="/images/calendario.gif" border=0>
				</a> <%		  
	  }
%>
				</td>
				<td class="l">Data Presunta</td>
				<td class="L"><select title="Data presunta"
					name="<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>">
						<%= dataNascitaPresunta %>
				</select></td>
			</tr>

			<%-- MERGE v10 COLLAUDO: modifica alla gestione del codice --%>
			<%	    
		// Si ricava il profilo e l'ufficio dell'utente connesso
		ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();
		UfficioModel lUfficio = UtenteConnesso.getUfficioUtente();
		String codTipoUfficio = lUfficio.getCodTipoUfficio();

		// MEV_57: esclusi anche sige minorenni
		boolean isSigeMinorenni = lProfilo.isSige() && ("CAPSM".equals(codTipoUfficio) || "DIBM".equals(codTipoUfficio) || "GIPM".equals(codTipoUfficio) || "GUPM".equals(codTipoUfficio) || "PMM".equals(codTipoUfficio));
		if (!lProfilo.isSius() && !isSigeMinorenni) { %>
			<tr>
				<td class="l">Età Presunta</td>
				<td class="l"><input type="text"
					name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI %>"
					value="<%=StringUtils.toStringJSP(lSoggetto.getEtaPresuntaAnni()) %>"
					title="Età Presunta - Anni"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
					anni &nbsp;&nbsp;/&nbsp;&nbsp; <input type="text"
					name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI %>"
					value="<%=StringUtils.toStringJSP(lSoggetto.getEtaPresuntaMesi()) %>"
					title="Età Presunta - Mesi"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
					mesi</td>
			</tr>
			<%  } %>

			<%
    // Data commesso reato deve essere visibile solo per 
    // gli utenti SIUS (Minorenni), no per gli utenti SIEP
    // MEV_57: aggiunti anche gli uffici sige minorenni
	if ((!lProfilo.isSiep() && ("TDSM".equals(codTipoUfficio) || "UDSM".equals(codTipoUfficio))) || isSigeMinorenni) {
%>
			<tr>
				<td class="l">Età Presunta</td>
				<td class="l"><input type="text"
					name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI %>"
					value="<%=StringUtils.toStringJSP(lSoggetto.getEtaPresuntaAnni()) %>"
					title="Età Presunta - Anni"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
					anni &nbsp;&nbsp;/&nbsp;&nbsp; <input type="text"
					name="<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI %>"
					value="<%=StringUtils.toStringJSP(lSoggetto.getEtaPresuntaMesi()) %>"
					title="Età Presunta - Mesi"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)" maxlength="2" size="2">
					mesi</td>

				<td class="l">Data commesso reato</td>

				<%
		if(modalita.equals("I")) {
	%>
				<td class="L"><input type="text"
					title="Giorno Data commesso reato"
					name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Mese Data commesso reato"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_COMMESSO_REATO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Anno Data commesso reato"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_COMMESSO_REATO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
				<% 
		} else if(modalita.equals("M")) {
	%>
				<td class="L"><input type="text"
					title="Giorno Data commesso reato"
					name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataReatoSius(),"dd")) %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Mese Data commesso reato"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_COMMESSO_REATO %>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataReatoSius(),"MM")) %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> / <input
					type="text" title="Anno Data commesso reato"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_COMMESSO_REATO %>"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataReatoSius(),"yyyy")) %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
				<%
		}
	%>
			</tr>
			<%
    } 
	%>

			<tr>
				<td class="l">Comune Nascita <font class=ob>(*)</font></td>
				<td class="L"><input title="Comune di Nascita"
					value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita()) %>"
					type="text"
					name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>"
					maxlength="35" size="35" onChange="cancellaCodComuneReale();">
					<a
					href="Javascript:ListaComuniNascita('LoadInserisciSoggetto','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
						<img src="/images/filefolder.gif" border=0>
				</a></td>
			</tr>

			<%  // Nel campo Nazionalità mettiamo i valori del nuovo campo Stato Cittadinanza  %>
			<tr>
				<td class="l">Stato Cittadinanza</td>
				<td class="L"><select title="Stato Cittadinanza"
					name="<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>">
						<%= StatoCittadinanza %>
				</select></td>
				<td class="l">Stato di Nascita</td>
				<td class="L"><select title="Stato di Nascita"
					name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
						<%= nazioni %>
				</select></td>
			</tr>

			<tr>
				<td class="l">Luogo di Nascita Estero</td>
				<td class="L"><input title="Luogo di Nascita Estero"
					value="<%=StringUtils.toStringJSP(lSoggetto.getDescComuneNascitaEstero()) %>"
					type="text"
					name="<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>"></td>
			</tr>

			<tr>
				<td class="l">Paternità</td>
				<td class="L"><input title="Paternità"
					value="<%=StringUtils.toStringJSP(lSoggetto.getPaternita() )%>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_PATERNITA %>"
					maxlength="35" size="35"></td>
			</tr>
			<tr>
				<td class="l">Cognome Madre</td>
				<td class="L"><input title="Cognome della madre"
					value="<%=StringUtils.toStringJSP(lSoggetto.getCognomeMadre()) %>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>"
					maxlength="35" size="35"></td>

				<td class="l">Nome Madre</td>
				<td class="L"><input title="Nome della madre"
					value="<%=StringUtils.toStringJSP(lSoggetto.getNomeMadre() )%>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>"
					maxlength="35" size="35"></td>
			</tr>
			<tr>
				<td colspan=4 class=l>&nbsp;</td>
			</tr>
			<tr>
				<td class="l">Codice Fiscale</td>
				<td class="L"><input title="Codice Fiscale"
					id=<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>
					value="<%=StringUtils.toStringJSP(lSoggetto.getCodFiscale()) %>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>"
					maxlength="16" size="18"></td>

				<td class="l">Atto Nascita</td>
				<td class="L"><input title="Atto di nascita"
					value="<%=StringUtils.toStringJSP(lSoggetto.getAttoNascita() )%>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>"
					maxlength="10" size="10"></td>
			</tr>

			<tr>
				<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
				<%--<td class="l">Codice Fascicolo Rosso</td>
		<td class="L">
			<input title="Codice Fascicolo Rosso"
					value="< %=StringUtils.toStringJSP(lSoggetto.getCodCs()) %>"
					type="text"
        			name="< %= ICostantiSoggetto.CAMPO_COD_CS %>"  maxlength="7" size="7">
        </td>
		--%>
				<td class="l">Codice CUI</td>
				<td class="L"><input title="Codice CUI"
					value="<%=StringUtils.toStringJSP(lSoggetto.getCodAfis()) %>"
					type="text" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>"
					maxlength="7" size="7"></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td class="l">Note</td>
				<td class="L" colspan=3><TEXTAREA title="note"
						name="<%= ICostantiSoggetto.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lSoggetto.getNote() )%></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2"><input onclick="Javascript:return Verify();"
					class=bottone type="submit" value="Conferma"> <input
					type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
					value="<%=lAction%>"> <input type="HIDDEN"
					name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"
					value="<%=lSoggetto.getIdSoggetto()%>"> <input
					type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
					<input type="HIDDEN"
					name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
				</td>
			</tr>
		</table>
	</form>

	<script language="JavaScript">
function cancellaCodComuneReale() {
	document.LoadInserisciSoggetto.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
}

function controllaEtaSoggetto() {
        var tipoUff = "<%=UtenteConnesso.getUfficioUtente().getCodTipoUfficio()%>";
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value);
        
        var mese = 1;
        var giorno = 1;
        if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
                    
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);
           //alert ("14esimo compleanno ->" + data_compleanno14.toString());
           //alert ("18esimo compleanno ->" + data_compleanno18.toString());
           //alert("Tipo Ufficio " + tipoUff);
	    if (anno!="0") { 
	        if (tipoUff == "PMM" || tipoUff ==  "DIBM") {
	           // caso Tribunale dei Minori
	           if (oggi < data_compleanno14 )
	             ritorno = window.confirm('Il soggetto non ha compiuto i 14 anni! Confermi il suo inserimento?');
	           if ( oggi > data_compleanno18)
	             ritorno = window.confirm('Il soggetto ha più di 18 anni! Confermi il suo inserimento?');
	        } else {
	           if (oggi < data_compleanno18)
	              ritorno = window.confirm('Il soggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');
	        }
	    } 
        return ritorno;
}

function Verify() {
	var ritorno = true;
	
	if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039') {
		document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
		if (document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value.length==0) {
			alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
			document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.focus;
			return false;
		}
	} else {
		document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';
		cancellaCodComuneReale();
	}
        
	if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
		document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
	if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
		document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
	
	// data_to_commesso_reato esiste solo per SIUS e non per SIEP
	// controllo se data_to_commesso_reato esiste nella form
    if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>!='undefined')
    {	
		var data_to_commesso_reato=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.value+'/'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_COMMESSO_REATO%>.value+'/'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_COMMESSO_REATO%>.value;
    } 
	
	var data_to_verify=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
    if(document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'N' ||
    		document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == '-') {
    	// data presunta = N
    	// data di nascita è obbligatoria se non sono stati impostati i campi "Età presunta" e "Data commesso reato"
		// i campi "Età presunta" e "Data commesso reato" sono obbligatori se non è stata impostata la data di nascita
		// MEV_57: aggiunti controlli preventivi
		<%-- MEV_66: aggiunti controlli sulla consistenza del campo età presunta --%>
		var eta_presunta_anni = "";
		var eta_presunta_mesi = "";
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%> != 'undefined')
			eta_presunta_anni = document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%>.value;
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%> != 'undefined')
			eta_presunta_mesi = document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>.value;
		if (!ControllaData(data_to_verify) ){
			//data_to_commesso_reato esiste solo per SIUS e non per SIEP
			//controllo se data_to_commesso_reato esiste nella form
			if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>!='undefined')
	        {
				if((eta_presunta_anni=="" && eta_presunta_mesi=="") || !ControllaData(data_to_commesso_reato)){					
					alert('Età presunta e Data commesso reato sono obbligatori se non è stata inserita la data di nascita');
					return false;				
				}
	        } else {
				if((eta_presunta_anni=="" && eta_presunta_mesi=="")){					
					alert('Età presunta è obbligatoria se non è stata inserita la data di nascita');
					return false;				
				}
	        }			
		}				
		if(eta_presunta_anni=="" && eta_presunta_mesi==""){
			if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>!='undefined')
	        {
				//data_to_commesso_reato esiste solo per SIUS e non per SIEP
				//controllo se data_to_commesso_reato esiste nella form
				if (!ControllaData(data_to_commesso_reato) ){					
					if (!ControllaData(data_to_verify) ){
					 	alert('La data di nascita è obbligatoria se non sono stati impostati i campi Età presunta e Data commesso reato');
						return false;
					}
				}
			} else if (!ControllaData(data_to_verify) ){						
					 	alert('La data di nascita è obbligatoria se non è stato impostato il campo Età presunta');
						return false;
					}        			
		} 
		/* if (! ControllaData(data_to_verify)) {
			alert('Data di nascita non valida');
			return false;
		   } */
	} else if(document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'S') {
    	//data presunta = S
    	//data di nascita è obbligatoria se non sono stati impostati i campi "Età presunta" e "Data commesso reato"
		//i campi "Età presunta" e "Data commesso reato" sono obbligatori se non è stata impostata la data di nascita
		<%-- MEV_66: aggiunti controlli sulla consistenza del campo età presunta --%>
		var eta_presunta_anni = "";
		var eta_presunta_mesi = "";
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%> != 'undefined')
			eta_presunta_anni=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%>.value;
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%> != 'undefined')
			eta_presunta_mesi=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>.value;
		var eta_data_nascita_anni=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
		if (!ControllaData(data_to_verify) ){
			//data_to_commesso_reato esiste solo per SIUS e non per SIEP
			//controllo se data_to_commesso_reato esiste nella form
			if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>!='undefined')
	        {
				if((eta_presunta_anni=="" && eta_presunta_mesi=="") || !ControllaData(data_to_commesso_reato)){					
					alert('Età presunta e Data commesso reato sono obbligatori se non è stata inserita la data di nascita');
					return false;				
				}
	        } 	
		}		
		if(eta_presunta_anni=="" && eta_presunta_mesi==""){
			if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_COMMESSO_REATO%>!='undefined')
	        {
				//data_to_commesso_reato esiste solo per SIUS e non per SIEP
				//controllo se data_to_commesso_reato esiste nella form
				if (!ControllaData(data_to_commesso_reato) ){					
					if (eta_data_nascita_anni==""){		
					 	alert("Inserire almeno il campo anni della data di nascita se non sono stati impostati i campi Età presunta e Data commesso reato");
						return false;
					}
				}
			} else if (eta_data_nascita_anni=="" || parseInt(eta_data_nascita_anni,10)==0){	
<%-- Ticket 20210310014: Aggiunto controllo che anno della data nascita se valorizzato sia diverso da 0 --%>      
					 	alert("Inserire almeno il campo anni della data di nascita se non è stato impostato il campo Età presunta. Valore 0 non consentito.");
						return false;
					}        			
		}   
	} 

    var eta_data_nascita_giorni=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
    var eta_data_nascita_mesi=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
    var eta_data_nascita_anni=document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
    if ((!eta_data_nascita_giorni=="" && eta_data_nascita_mesi=="" && !eta_data_nascita_anni=="") ||
    	(eta_data_nascita_giorni=="" && !eta_data_nascita_mesi=="" && !eta_data_nascita_anni=="")	){	
    	alert('Data di nascita non valida');
    	return false;
    } else if (!eta_data_nascita_giorni=="" && !eta_data_nascita_mesi=="" && !eta_data_nascita_anni==""){
    	 if (! ControllaData(data_to_verify)) {
 			alert('Data di nascita non valida');
 			return false;
 		  } 
    }
    
	ritorno =  controllaEtaSoggetto();
	
	if (ritorno) {
		// MEV_57: aggiunti controlli preventivi
		<%-- MEV_66: aggiunti controlli sulla consistenza del campo età presunta --%>
		var eta_presunta = "";
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%> != 'undefined'
				&& typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%> != 'undefined')
			eta_presunta = document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%>.value+":"+document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>.value;
		if (data_to_verify.length > 2 && eta_presunta.length > 1) {
			alert("Il campo data di nascita è valorizzata non si può inserire l'eta presunta");
			ritorno =  false;
		}
	}
	
	if (ritorno) {		
		// MEV_57: aggiunti controlli preventivi		 
		<%-- MEV_66: aggiunti controlli sulla consistenza del campo età presunta --%>
		if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%> != 'undefined') {
<%-- 		if (document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>.value != '') { --%>
			if (typeof document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%> != 'undefined') {
<%-- 			if (document.LoadInserisciSoggetto.<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_ANNI%>.value != '') { --%>
				if (document.LoadInserisciSoggetto.<%=ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>.value >= 12){
					alert("Il campo mesi dell'età presunta deve essere minore di 12");
					ritorno = false;
				}
			} else {
				alert("Il campo anni dell'età presunta non è valorizzato");
				ritorno = false;
			}
		}
	}

	return ritorno;
}
</script>

<script language="JavaScript">
var frmvalidator  = new Validator("LoadInserisciSoggetto");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","req","Il campo Nome Soggetto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");

frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","req","Il campo Cognome Soggetto è obbligatorio");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");


<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000"); --%>

frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>","alphanumeric");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_AFIS %>","alphanumeric");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NAZIONALITA %>","alphanumeric");  


frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>","alphanumeric");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");
<%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
<%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%-- frmvalidator.addValidation("<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>","alpha"); --%>
frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha");

<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>","numeric"); --%>
<%-- // frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ETA_PRESUNTA_MESI%>","lt=12"); --%>
  
// frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>