<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV_39: aggiunta pagina di scelta classe procedimento --%>
<%@ page import="f3b.web.IWebConstants" %>

<jsp:useBean id="flagStatisticaIV" scope="request" class="java.lang.String"/>

<% 

String readonly = ""; 

if ( "OFF".equals(flagStatisticaIV) ){ 
  readonly  = "disabled=disabled"; 
}
%>
<!-- StatisticheEstrazioneDati -->
<html>
<head>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" type="text/javascript">
	function selezionaClasse() {
		if (document.sed.tipo[0].checked) { // Classe I - Pena Detentiva
			gestioneDiv("block", "none", "none");
		} else if (document.sed.tipo[1].checked) { // Classe II - Pena Pecuniaria
			gestioneDiv("none", "none", "none");
		} else if (document.sed.tipo[2].checked) { // Classe III  - Pena Sospesa
			gestioneDiv("none", "none", "none");
		} else if (document.sed.tipo[3].checked) { // Classe IV - Misure Sicurezza
			gestioneDiv("none", "block", "none");
			document.sed.classe.value = "IV";
		} else if (document.sed.tipo[4].checked) { // Classe V - Persona Giuridica
			gestioneDiv("none", "none", "none");
		} else if (document.sed.tipo[5].checked) { // Classe VI - Giudice di pace
			gestioneDiv("none", "none", "none");
		} else if(document.sed.tipo[6].checked) { // Classe VII - Conversione Pene Pecuniarie
			gestioneDiv("none", "none", "block");
			document.sed.classe.value = "VII";
		} else if(document.sed.tipo[7].checked) { // Registro Istanze
			gestioneDiv("none", "none", "none");
		}
	}

	function verify() {
		if (document.sed.tipo[0].checked == false
				&& document.sed.tipo[3].checked == false
				&& document.sed.tipo[6].checked == false) {
			alert("Attenzione! Selezionare una Classe Procedimento di tipo I, IV o VII.");
			return false;
		}

		// indirizzamento di default
		if (document.sed.<%=IWebConstants.ACTION_FIELD%>.value == "") {
			if (document.sed.tipo[0].checked) {
				document.sed.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statis.action.ActLoadTempiIscrizioni";
			} else if (document.sed.tipo[3].checked) {
				document.sed.classe.value = "IV";
				document.sed.tipologia.value = "Riepilogo Iscrizioni e Tipologia Misura";
				document.sed.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII";
			} else if (document.sed.tipo[6].checked) {
				document.sed.classe.value = "VII";
				document.sed.tipologia.value = "Riepilogo Iscrizioni ed Attività";
				document.sed.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII";
			} else
				document.sed.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.web.ActionUnderConstruction";
		}

		return true;
	}

	function gestioneDiv(a, b, c) {
		document.getElementById("uno").style.display = a;
		document.getElementById("quattro").style.display = b;
		document.getElementById("sette").style.display = c;
	}
	</script>
</head>

<BODY class="corpo">
  	<table>
  		<tr>
	      	<td class="LBG">
	      		<a href="window.print();">
	      			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
	      		</a>
	      	</td>
	        <td class="LBG">
          		<font class="label">Funzione:</font>&nbsp;
          		<font class="campo">STATISTICHE - ESTRAZIONE DATI</font>
        	</td>
     	</tr>
   	</table>
	<br>
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="sed">
	<table style="width: 95%;">
		<tr>
			<td class="Titolo" colspan="16">Selezionare la Classe Procedimento da Elaborare</td>
		</tr>
     	<tr>
			<td class="c">Classe I</td>
			<td class="c"><input type="radio" name="tipo" value="1" onclick="selezionaClasse();"></td>
			<td class="c">Classe II</td>
			<td class="c"><input type="radio" name="tipo" value="2" onclick="selezionaClasse();"></td>
			<td class="c">Classe III</td>
			<td class="c"><input type="radio" name="tipo" value="3" onclick="selezionaClasse();"></td>
			<td class="c">Classe IV</td>
			<td class="c"><input type="radio" name="tipo" value="4" onclick="selezionaClasse();" <%=readonly%>></td>
			<td class="c">Classe V</td>
			<td class="c"><input type="radio" name="tipo" value="5" onclick="selezionaClasse();"></td>
			<td class="c">Classe VI</td>
			<td class="c"><input type="radio" name="tipo" value="6" onclick="selezionaClasse();"></td>
			<td class="c">Classe VII</td>
			<td class="c"><input type="radio" name="tipo" value="7" onclick="selezionaClasse();"></td>
			<td class="c">Classe VIII</td>
			<td class="c"><input type="radio" name="tipo" value="8" onclick="selezionaClasse();"></td>
     	</tr>
  	</table>
	<div id="uno" style="position: relative; display: none;">
		<br>
		<table style="width: 95%;">
			<tr>
				<td class="l">Tempi Iscrizione Fascicoli</td>
				<td class="c"><input type="radio" name="uno" value="1" checked="checked"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statis.action.ActLoadTempiIscrizioni';">
				</td>
			</tr>
			<tr>
				<td class="l">Tempi Emissione Provvedimenti</td>
				<td class="c"><input type="radio" name="uno" value="2"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statis.action.ActLoadTempiEmissione';">
				</td>
			</tr>
			<tr>
				<td class="l">Attivita' Magistrati</td>
				<td class="c"><input type="radio" name="uno" value="3"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statis.action.ActLoadAttivitaMagistrati';">
				</td>
			</tr>
			<tr>
				<td class="l">Riepilogo Ispettivo</td>
				<td class="c"><input type="radio" name="uno" value="4"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statis.action.ActLoadSelezionaUfficio';">
				</td>
			</tr>
		</table>
	</div>
	<div id="quattro" style="position: relative; display: none;">
		<br>
		<table style="width: 95%;">
			<tr>
				<td class="l">Riepilogo Iscrizioni e Tipologia Misura</td>
				<td class="c"><input type="radio" name="quattro" value="1" checked="checked"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Riepilogo Iscrizioni e Tipologia Misura'">
				</td>
			</tr>
			<tr>
				<td class="l">Procedimenti Pendenti nel Periodo</td>
				<td class="c"><input type="radio" name="quattro" value="2"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Procedimenti Pendenti nel Periodo'">
				</td>
			</tr>
			<tr>
				<td class="l">Movimento Procedimenti (Riepilogo Procedimenti Pendenti)</td>
				<td class="c"><input type="radio" name="quattro" value="3"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Movimento Procedimenti (Riepilogo Procedimenti Pendenti)'">
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td class="l">Riepilogo Ispettivo</td> -->
<!-- 				<td class="c"><input type="radio" name="quattro" value="4" -->
<%-- 					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Riepilogo Ispettivo'"> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
				<td class="l">Attivita' Magistrati</td>
				<td class="c"><input type="radio" name="quattro" value="5"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Attività Magistrati'">
				</td>
			</tr>
		</table>
	</div>
	<div id="sette" style="position: relative; display: none;">
		<br>
		<table style="width: 95%;">
			<tr>
				<td class="l">Riepilogo Iscrizioni ed Attivita'</td>
				<td class="c"><input type="radio" name="sette" value="1" checked="checked"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Riepilogo Iscrizioni ed Attività'">
				</td>
			</tr>
			<tr>
				<td class="l">Tempi Iscrizione Fascicoli</td>
				<td class="c"><input type="radio" name="sette" value="2"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Tempi Iscrizione Fascicoli'">
				</td>
			</tr>
			<tr>
				<td class="l">Riepilogo Procedimenti Pendenti</td>
				<td class="c"><input type="radio" name="sette" value="3"
					onclick="document.sed.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.statistiche.action.ActLoadRicercaProcedimentiClasseIVeVII'; document.sed.tipologia.value='Riepilogo Procedimenti Pendenti'">
				</td>
			</tr>
		</table>
	</div>
	<br>
	<table>
    	<tr>
      		<td>
      			<input type="HIDDEN" name="classe" value="">
      			<input type="HIDDEN" name="tipologia" value="">
      			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
        		<input type=submit value="Conferma" class="bottone" name="btnconf">
      		</td>
		</tr>
	</table>
	</FORM>
	<script language="JavaScript" type="text/javascript">
		var frmvalidator  = new Validator("sed");
		frmvalidator.setAddnlValidationFunction("verify");
	</script>
</body>
</html>