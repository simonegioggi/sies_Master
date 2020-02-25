<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<%@ page import="f3b.web.IWebConstants" %>
	<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
	<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
	<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>

	<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>
	<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>

<%
	//Controllo se il Titolo è stato passato nella request 
	String lTitolo = "Ricerca Procedimento per RGNR  ";
	if (titolo != null &&  titolo.length() > 0)
	{
		lTitolo = titolo;
	}
%>

	<head>
		<title> [S.I.E.S.] - Ricerca Procedimento per RGNR - </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
		<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
		<script language="JavaScript">
		function Verify()
		{
			document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
			document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.sentenza.action.ActRicercaProcedimentoRGNR";
			return true;
		}
		function radio()
		{
			document.f.<%=ICostantiSentenza.CAMPO_ANNO_RGNR_INIZIALE%>.focus() ;
		}
		</script>
	</head>

	<body class="corpo" onload="radio();">
		<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
			<input type="HIDDEN" name="<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>" value="">
			<table>
				<tr>
					<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
					<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font></td>
				</tr>
			</table>
			<br>
			<table>
				<tr>
					<td align="center" class="Titolo"  colspan ="2" >Intervallo R.G.N.R.</td>
				</tr>
				<tr>
					<td class="L"> Anno/Numero Iniziale </td>
					<td class="L">
						<input type="text" title="Anno R.G.N.R." name="<%=ICostantiSentenza.CAMPO_ANNO_RGNR_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
						/
						<input type="text" title="Numero R.G.N.R." name="<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_INIZIALE%>" maxlength="6" size="6">
					</td>
					<td class="L"> Anno/Numero Finale </td>
					<td class="L">
						<input type="text" title="Anno R.G.N.R." name="<%=ICostantiSentenza.CAMPO_ANNO_RGNR_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
						/
						<input type="text" title="Numero R.G.N.R." name="<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_FINALE%>" maxlength="6" size="6">
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="l" >
						<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return Verify();">
					</td>
				</tr>
			</table>
		</form>

		<script language="JavaScript" type="text/javascript">
			var frmvalidator  = new Validator("f");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_INIZIALE%>","req","Indicare l'Anno R.G.N.R. inziale");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno R.G.N.R. è di 4 caratteri");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_INIZIALE%>","numeric");
			
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_INIZIALE%>","maxlen=6","La lunghezza massima per il Numero R.G.N.R. è di 6 caratteri");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_INIZIALE%>","numeric");
			
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_FINALE%>","maxlen=4","La lunghezza massima per l'Anno R.G.N.R. è di 4 caratteri");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_FINALE%>","minlen=4","La lunghezza minima per l'Anno R.G.N.R. è di 4 caratteri");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_RGNR_FINALE%>","numeric");
			
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_FINALE%>","maxlen=6","La lunghezza massima per il Numero R.G.N.R. è di 6 caratteri");
			frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_RGNR_FINALE%>","numeric");
		</script>
	</body>
</html>