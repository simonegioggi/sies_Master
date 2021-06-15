<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- 20190301 [SG]: restyling della pagina --%>
<%@ page language="java" import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="nazioni" scope="request" class="java.lang.String" />
<jsp:useBean id="contenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="CodUDSTDS" scope="request" class="java.lang.String" />
<%
String lNota = "N.B.: Il sistema ricerca i soggetti con procedimenti pendenti e definiti di competenza dell'ufficio senza limitazione del periodo di pervenimento in cancelleria e per qualsiasi tipo di contenuto. Per variare i criteri selezionare una o più delle seguenti opzioni:";
String lCheck ="";
if (TipoUfficioConnesso.substring(1,3).compareTo("DS") != 0) {
	lNota = "N.B.: Il sistema ricerca i soggetti con procedimenti pendenti e definiti per qualsiasi tipo di contenuto. Per variare i criteri selezionare una o più delle seguenti opzioni:";
	lCheck="CHECKED";
}
%>
<html>
<head>
<title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti - Attività
	di Sportello</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="/html/ControllaData.js"></script>
<script language="JavaScript">
function Verify() {
	var data_to_verify = document.LoadRicercaSportello.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value + '/'
		+ document.LoadRicercaSportello.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value + '/'
		+ document.LoadRicercaSportello.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
	if (!ControllaData(data_to_verify) && data_to_verify.length > 2) {
        alert('Data di nascita non valida');
        return false;
      }
      return true;
    }

    var desktop;
function ListaComuni(a_formname,a_fieldname) {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
<!-- 20210524	MEV Scheda-21 -->
function ListaComuniNascita(a_formname,a_fieldname) {
  	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
	}      
function cancellaCodComuneReale() {
   	document.LoadRicercaSportello.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
  }   	
    
  </script>

</head>

<body class="corpo">
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
		name="LoadRicercaSportello">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaSportello">
		<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
		<table>
			<tr>
				<td class="LBG"><a href="Javascript:window.print();"> <img
						align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
						alt="Stampa questa videata" border="0">
				</a></td>
				<td class="LBG"><font class="label">Funzione:</font>&nbsp;<font
					class="campo">Ricerca Soggetti con Procedimenti di
						Sorveglianza - Attività di Sportello</font></td>
			</tr>
		</table>

		<br>
		<table cellspacing="2" cellpadding="2">
			<tr>
				<td class="l">Cognome</td>
				<td class="l"><input title="Cognome Soggetto" type="text"
					name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30"
					maxlength="30"></td>
			</tr>

			<tr>
				<td class="l">Nome</td>
				<td class="l"><input title="Nome Soggetto" type="text"
					name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30"
					maxlength="30"></td>
			</tr>

			<tr>
				<td class="l">Comune di nascita</td>
				<td class="l"><input Title="Comune di nascita"
					name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" value=""
					type="text" maxlength="35" size="35" onChange="cancellaCodComuneReale();"> 
					<a href="Javascript:ListaComuniNascita('LoadRicercaSportello','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
						<img src="/images/filefolder.gif" border="0">
					</a></td>
			</tr>


			<tr>
				<td class="l">Stato di Nascita</td>
				<td class="L"><select title="Stato di Nascita"
					name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
						<%= nazioni %>
				</select></td>
			</tr>

			<tr>
				<td class="l">Data di nascita</td>
				<td class="l"><input title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					title="Data di nascita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
			</tr>

			<tr>
				<td class="l">Paternità</td>
				<td class="l"><input title="Paternita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_PATERNITA%>" size="30"
					maxlength="30"></td>
			</tr>

			<tr>
				<td class="l">Codice CUI</td>
				<td class="L">
					<%-- 20190301 [SG]: modifica per il cui; NO CAMPO_COD_CS SI CAMPO_COD_AFIS, LUNGHEZZA = 7 --%>
					<input title="Codice CUI" type="text"
					name="<%=ICostantiSoggetto.CAMPO_COD_AFIS%>" maxlength="7" size="7">
				</td>
			</tr>

		</table>

		<br>
		<table cellspacing="2" cellpadding="2">
			<tr>
				<td class="lVerdeNB"><%=lNota%></td>
			</tr>
		</table>

		<%
if ("DS".equals(TipoUfficioConnesso.substring(1,3)) && CodUDSTDS.length() > 2) {
%>
		<br>
		<table cellspacing="2" cellpadding="2">
			<tr>
				<td class="Cliccabile">Modifica competenza territoriale dei
					procedimenti visualizzati</td>
			</tr>
			<tr>
				<td class="label">
					<%
	if ("UDS".equals(TipoUfficioConnesso)) {
%> Visualizza solo i procedimenti dell'Ufficio <%
	} else {
%> Visualizza solo i procedimenti del Tribunale <%
	}
%>
				</td>
				<td class="label"><input type=radio
					name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value="0">
				</td>
			</tr>

			<tr>
				<td class="label">
					<%
	if ("UDS".equals(TipoUfficioConnesso)) {
%> Visualizza anche i procedimenti del Tribunale <%
	} else {
%> Visualizza anche i procedimenti dell'Ufficio (Sede) <%
	}
%>
				</td>
				<td class="label"><input type=radio
					name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value="1"
					CHECKED></td>
			</tr>
		</table>
		<%
} else {
%>
		<input type="HIDDEN"
			name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value="0">
		<%
}
%>
		<br>
		<table cellspacing="2" cellpadding="2">
			<tr>
				<td class="Cliccabile">Modifica Tipologia dei procedimenti
					visualizzati</td>
			</tr>
		</table>

		<table cellspacing="2" cellpadding="2">
			<tr>
				<td class="label">Visualizza solo i procedimenti relativi a
					&nbsp;&nbsp; <select title="contenuto" class=small
					name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
						<%= contenuto %>
				</select>
				</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
			</tr>

			<tr>
				<td class="Cliccabile">Seleziona il Periodo di arrivo in
					cancellaria dei procedimenti visualizzati</td>
			</tr>

			<tr>
				<td class="label">Visualizza i procedimenti pervenuti in
					cancelleria dal&nbsp;&nbsp; <input Title="dalla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					Title="dalla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					Title="dalla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> &nbsp;&nbsp; al
					&nbsp;&nbsp; <input Title="alla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					Title="alla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)">/ <input
					Title="alla Data" type="text"
					name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)">
				</td>
			</tr>

			<tr>
				<td><br>
				<br> <INPUT onclick="Javascript:return Verify();"
					class="bottone" type="submit" name="RICERCA" value="Ricerca">
				</td>
			</tr>

		</table>

	</form>
	<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaSportello");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");


    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=2010");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
</body>

</html>