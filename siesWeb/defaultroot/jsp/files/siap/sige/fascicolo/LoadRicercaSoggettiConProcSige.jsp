<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="nazioni" scope="request" class="java.lang.String" />
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String" />

<%
String lNota = "N.B.: Di norma la ricerca è limitata all'ufficio di appartenenza dell'utente, con possibilità di estendere la ricerca in ambito distrettuale agli altri uffici SIGE.";
%>
<html>
<head>
<title>[S.I.E.S.] - Ricerca Soggetti con Procedimenti SIGE -</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src="/html/ControllaData.js"></script>
<script language="JavaScript">
    function Verify()
    {
      var data_to_verify=document.LoadRicercaSoggettiConProcSige.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConProcSige.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConProcSige.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data di nascita non valida');
        return false;
      }
      return true;
    }
  </script>
<script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    <!-- 202105204	MEV Scheda-21 -->
    function ListaComuniNascita(a_formname,a_fieldname) {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
    }
    function cancellaCodComuneReale() {
      	document.LoadRicercaSoggettiConProcSige.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
  </script>

</head>

<body class="corpo">
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
		name="LoadRicercaSoggettiConProcSige">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.fascicolo.action.ActRicercaSoggettiConProcSige">
		<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
		<table>
			<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img
						align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
						alt="Stampa questa videata" border=0></a></td>
				<td class="LBG"><font class="label">Funzione :</font> <font
					class="campo">Ricerca Soggetti con Procedimenti SIGE</font></td>
			</tr>
		</table>

		<br>

		<table cellspacing=2 cellpadding=2>
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
					type="text" maxlength="35" size="35" onChange="cancellaCodComuneReale();" > 
					<a href="Javascript:ListaComuniNascita('LoadRicercaSoggettiConProcSige','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
						<img src="/images/filefolder.gif" border=0>
					</a>&nbsp;&nbsp;
				</td>
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
					onBlur="javascript:value=FillYear(value)"> <!-- MEV 15 - Revisione SIGE -->
					<a
					href="javascript:calendario('LoadRicercaSoggettiConProcSige','<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>');">
						<img src="/images/calendario.gif" border=0>
				</a></td>
			</tr>

			<tr>
				<td class="l">Paternità</td>
				<td class="l"><input title="Paternita" type="text"
					name="<%= ICostantiSoggetto.CAMPO_PATERNITA%>" size="30"
					maxlength="30"></td>
			</tr>

			<tr>
				<td class="l">Codice CUI</td>
				<td class="L"><input title="Codice CUI" type="text"
					name="<%=ICostantiSoggetto.CAMPO_COD_CS%>" maxlength="7" size="7">
				</td>
			</tr>

		</table>

		<br>

		<table cellspacing=2 cellpadding=2>
			<tr>
				<td class="lVerdeNB"><%=lNota%></td>
			</tr>
		</table>

		<br>

		<table cellspacing=2 cellpadding=2>

			<tr>
				<td class="Cliccabile">Modifica competenza territoriale dei
					procedimenti visualizzati</td>
			</tr>
			<tr>
				<td class="label">Visualizza solo i procedimenti dell'Ufficio
				</td>
				<td class="label"><input type=radio
					name="<%=ICostantiFascicoloSige.CAMPO_UFFICIO_DISTRETTO%>" value=0
					CHECKED></td>
				</td>
			</tr>

			<tr>
				<td class="label">Visualizza i procedimenti dell' intero
					Distretto &nbsp;&nbsp;&nbsp;</td>
				<td class="label"><input type=radio
					name="<%=ICostantiFascicoloSige.CAMPO_UFFICIO_DISTRETTO%>" value=1></td>
				</td>
			</tr>

			<tr>
				<td><br>
				<br> <INPUT onclick="Javascript:return Verify();"
					class="bottone" type="submit" name="RICERCA" value="Ricerca">
				</td>
			</tr>

		</table>

	</FORM>

	<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaSoggettiConProcSige");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");


    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=2010");

  </script>
</body>

</html>