<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>

<jsp:useBean id="PeriodoAltraMisura" scope="request"
	class="siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel" />
<jsp:useBean id="istitutodetenzione" scope="request"
	class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />
<jsp:useBean id="fascicoloSiusGP" scope="session"
	class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="lEMSModel" scope="request"
	class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />

<html>
<head>
<title>[S.I.U.S.] - Valida Inizio Misure Sicurezza</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    function Verify()
    {
      // Controllo della data scadenza
      var data_scadenza=document.LoadValidaInizioMisuraSicurezzaUDS.<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+LoadValidaInizioMisuraSicurezzaUDS.<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+LoadValidaInizioMisuraSicurezzaUDS.<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_SCADENZA %>.value;
      if (! ControllaData(data_scadenza))
      {
        alert('Data scadenza non valida');
        return false;
      }
      
      var data_misura=document.LoadValidaInizioMisuraSicurezzaUDS.data_misura.value;
      if ( CompareDate(data_scadenza,data_misura))
      {
        alert('Data scadenza deve essere superiore alla data inizio');
        return false;
      }
      return true;
    }
    </script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a>
		</td>
		<td class=LBG><font class="label">Funzione : </font>&nbsp;
 			<font class="campo">Valida Inizio Misure Sicurezza</font></td>
	</tr>

	<tr>
		<jsp:include
			page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>" />
	</tr>
	<br>
	<tr>
		<td class="L"><font class="campo"> 
<%
 		if (fascicoloSiusGP.getTenori() != null) {
 			int lSize = fascicoloSiusGP.getTenori().length;
 			if (lSize == 0)
 %> -&nbsp; <%
 				for (int x = 0; x < lSize; x++) {
 %> 
 					<font class="label"> <%=fascicoloSiusGP.getTenori()[x].getDescrOggettoTenore()%> <%
 					if (fascicoloSiusGP.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
 %> 
 						</font> <font class="descr"> - <%=fascicoloSiusGP.getTenori()[x].getDescrDettaglioOggetto()%></font> <%
 					}
 				}
 		} else {
 %> -&nbsp; <%
 		}
 %> </font></td>
	</tr>
</table>
<table>
	<tr>
		<td class="l">Data Inizio Esecuzione</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(
							PeriodoAltraMisura.getDataInizioEsecuzione(),
							"dd/MM/yyyy"))%></font></td>
	</tr>
	<tr>
		<td class="l" colspan=2>Autorità competente che ha inviato il verbale:</td>
	</tr>
	<%
			if (istitutodetenzione != null) {
			if (istitutodetenzione.getIdIstitutoDetenzione().trim()
			.length() > 0) {
	%>
	<tr>
		<td class="l">Istituto Detenzione</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(istitutodetenzione
									.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutodetenzione
									.getDescrComune())%> </font></td>
	</tr>
	<%
			}
		}
	%>

	<%
	if (!PeriodoAltraMisura.getCodTipoAutorita().equals("-")) {
	%>
	<tr>
		<td class="l">Autorità</td>
		<td class="l"><font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraMisura
								.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(PeriodoAltraMisura
								.getDescrLuogoAutorita())%> </font></td>
	</tr>
	<%
	}
	%>

	<tr>
		<td colspan=2>&nbsp;</td>
	</tr>

	<tr>
		<td class="l">Note</td>
		<td class="L"><font class="campo"> <%=StringUtils.toStringJSP(PeriodoAltraMisura
							.getMotivazione())%> </font></td>
	</tr>
</table>
		<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name='LoadValidaInizioMisuraSicurezzaUDS'>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.misurasicurezza.action.ActValidaInizioMisuraSicurezzaUDS">
<br>
 <table cellspacing=2 cellpadding=2>

	<tr>
      <td class="l">Data Fine Pena <font class="ob">(*)</font></td>
     	<td class="L">
		
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEMSModel
					.getDataTermineAttuale(), "dd") )%>" type="text" size="2" maxlength="2"
					 name="<%= ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_SCADENZA%>" 
					 <%=IWebConstants.UTIL_DATA%>>
				/ 
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEMSModel
					.getDataTermineAttuale(), "MM") )%>" type="text" size="2" maxlength="2"
					name="<%= ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_SCADENZA%>"
					<%=IWebConstants.UTIL_DATA%>>
				/ 
			<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEMSModel
					.getDataTermineAttuale(), "yyyy") )%>" type="text" size="4" maxlength="4" 
					name="<%= ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_SCADENZA %>" 
					<%=IWebConstants.UTIL_DATA_ANNO%>>
		</td>
       </tr>
    <tr> <td>&nbsp;</td></tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Valida Scadenza">
          <input type="HIDDEN" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_PERIODO_ALTRA_MISURA%>"
           value="<%=PeriodoAltraMisura.getIdPeriodoAltraMisura()%>" >
          <input type="HIDDEN" name="data_misura" 
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(
							PeriodoAltraMisura.getDataInizioEsecuzione(),
							"dd/MM/yyyy"))%>">
        </td>
      </tr>
    </table>
   </FORM>
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadValidaInizioMisuraSicurezzaUDS");

    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_SCADENZA%>","req","Il campo Giorno della data scadenza è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_SCADENZA%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_SCADENZA%>","req","Il campo Mese della data scadenza è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_SCADENZA%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_SCADENZA%>","req","Il campo Anno della data scadenza è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_SCADENZA%>","minlen=4","La lunghezza del campo Anno della data scadenza deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>