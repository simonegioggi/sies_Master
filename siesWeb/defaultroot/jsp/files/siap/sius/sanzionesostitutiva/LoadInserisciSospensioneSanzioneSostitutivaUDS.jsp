<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel"%>

<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session"
	class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="listaSanzioniSius" scope="request"
	class="java.util.ArrayList" />

<html>
<head>
<script language="JavaScript1.2">  </script>
<title>[S.I.U.S.] - Sospensione Sanzione Sostitutiva</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

<script language="JavaScript">
	var desktop;
	function ListaUffici(a_formname,a_fieldname)
	{
		var codTipoUfficio=LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO%>.value;
	   	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");

	}
	 function radio()
	{
		 var noderadio =document.getElementById('noderadio');
		if (document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE%>[1].checked)
		{
			noderadio.style.visibility='visible';
		}else{
			noderadio.style.visibility='hidden';
			document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_AA%>.value='';
			document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_MM%>.value='';
			document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_GG%>.value='';
		}
	}

	function pulisciAutorita()
	{
		document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>.value = '';
	}

	</script>


<script language="JavaScript">
    	 function ListaSanzioni(a_formname)
    	{
    	 desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.sanzionesostitutiva.action.ActLoadListaSanzioniSostitutiveUDS&formname="+a_formname+"&"+"<%=ICostantiSanzioneSostitutiva.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" , "Lista_Date", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
    	}
    </script>

<script language="JavaScript">
	    function Verify()
	    {
	      var data_inizio_sospensione=document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE %>.value+'/'+LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>.value+'/'+LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE %>.value;
	      if (! ControllaData(data_inizio_sospensione))
	      {
	        alert('Data inizio sospensione non valida');
	        return false;
	      }
	      var data_fino_al=document.LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA %>.value+'/'+LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>.value;
	      if (data_fino_al != "//")
	      {
	      	if (! ControllaData(data_fino_al))
	      	{
	        	alert('Data Fino al non valida');
	        	return false;
	      	}
	      }
		  var data_inizio_periodo=document.LoadInserisciSospensioneSanzioneSostitutivaUDS.data_inizio_periodo.value;
		  if ( CompareDate(data_inizio_sospensione,data_inizio_periodo))
	      {
	        alert('Data inizio sospensione deve essere superiore alla data inizio/ripresa');
	        return false;
	      }
	      var data_scadenza_totale=document.LoadInserisciSospensioneSanzioneSostitutivaUDS.data_scadenza_totale.value;
		  if ( CompareDate(data_scadenza_totale,data_inizio_sospensione))
	      {
	        alert('Data inizio sospensione deve essere inferiore alla data scadenza');
	        return false;
	      }
		  if ( CompareDate(data_fino_al,data_inizio_sospensione))
	      {
	        alert('Data Scadenza Sospensione deve essere superiore alla data inizio sospensione');
	        return false;
	      }
		  if ( CompareDate(data_scadenza_totale,data_fino_al))
	      {
	        alert('Data Scadenza Sospensione deve essere inferiore alla Data Termine Sanzione');
	        return false;
	      }
	      var trovata = 0;
	      if (LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>.value.length == 0
	           || LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO%>.value.length <= 1)
	      {
	        alert('Selezionare una autorità competente che ha inviato il verbale!');
	        return false;
	      }

 	   	  var tipo_autorita=LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO%>.value;
	   	  // 29/05/2008 Controllo periodo va sempre eseguito
	   	  // if ( tipo_autorita != 'UDS')
	   	  //{
		      if (LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>.value.length == 0
		          && LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_GG%>.value.length == 0
		          && LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_MM%>.value.length == 0
		          && LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_AA%>.value.length == 0)
		      {
		        	alert('Selezionare periodo');
		        	return false;
		      }
		    //}
		    var trovata = 0;
          if (LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>.value.length != 0 )
          		{trovata++;}
          if (LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_GG%>.value.length != 0
        	|| LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_MM%>.value.length != 0
        	|| LoadInserisciSospensioneSanzioneSostitutivaUDS.<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_AA%>.value.length != 0)
          		{trovata++;}
          if (trovata >1)
          {
          		alert('Selezionare un solo periodo!');
          		return false;
	      }
	      return true;
	    }

    </script>
</head>

<body class="corpo" onLoad="javascript:radio();">
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();"><img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class=LBG><font class="label">Funzione : </font>&nbsp; <font
			class="campo">annotazione data inizio sospensione
		/differimento </font></td>
	</tr>
</table>
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name='LoadInserisciSospensioneSanzioneSostitutivaUDS'><jsp:include
	page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>" />
<br>

<% PeriodoAltraSanzioneModel lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius
		.get(0);%>

	<input type="HIDDEN" name="data_scadenza_totale"
		value="<%=StringUtils.toStringJSP(DateUtils.getDateToString
							(lPerMod.getDataScadenza(),"dd-MM-yyyy"), "-")%>">

<%  if (listaSanzioniSius.size() == 1)  {
		lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius
			.get(0);%>
		<input type="HIDDEN" name="data_inizio_periodo"
			value="<%=StringUtils.toStringJSP(DateUtils.getDateToString
							(lPerMod.getDataInizioEsecuzione(),"dd-MM-yyyy"), "-")%>">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
			value="siap.sius.sanzionesostitutiva.action.ActInserisciSospensioneSanzioneSostitutivaUDS">

<%	}else{
		lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius
			.get(listaSanzioniSius.size()-1);%>
		<input type="HIDDEN" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE%>"
	        value="<%=lPerMod.getIdPeriodoAltraSanzione()%>" >
<%		if (lPerMod.getDataScadenza() == null){
			lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius
				.get(listaSanzioniSius.size()-2);%>
			<input type="HIDDEN" name="data_inizio_periodo"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString
								(lPerMod.getDataInizioEsecuzione(),"dd-MM-yyyy"), "-")%>">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
				value="siap.sius.sanzionesostitutiva.action.ActModificaSospensioneSanzioneSostitutivaUDS">
<%		}else{%>
			<input type="HIDDEN" name="data_inizio_periodo"
				value="<%=StringUtils.toStringJSP(DateUtils.getDateToString
								(lPerMod.getDataInizioEsecuzione(),"dd-MM-yyyy"), "-")%>">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
				value="siap.sius.sanzionesostitutiva.action.ActInserisciSospensioneSanzioneSostitutivaUDS">
<%		}
	}%>

	<table width="100%">
		<tr>
			<td class="L"> <%
	 		if (fascicoloSiusGP.getTenori() != null) {
	 		int lSize = fascicoloSiusGP.getTenori().length;
	 		if (lSize == 0)
	 %> -&nbsp; <%
	 for (int x = 0; x < lSize; x++) {
	 %> <font class="label"> <%=fascicoloSiusGP.getTenori()[x]
											.getDescrOggettoTenore()%> <%
	 				if (fascicoloSiusGP.getTenori()[x]
	 				.getCodDettaglioOggetto().length() > 1) {
	 %> </font> <font class="descr"> - <%=fascicoloSiusGP.getTenori()[x]
												.getDescrDettaglioOggetto()%></font> <%
	 			}
	 			}
	 	} else {
	 %> -&nbsp; <%
	 }
	 %>
			</td>
		</tr>
	</table>

	<table width="100%">
		<tr>
			<td class="l"><a
				href="Javascript:ListaSanzioni('LoadInserisciSospensioneSanzioneSostitutivaUDS');">
			Elenco Date Inizio <img src="/images/filefolder.gif" border=0> </a>
			</td>
		</tr>
		<tr>
<% 			lPerMod = (PeriodoAltraSanzioneModel) listaSanzioniSius
			.get(listaSanzioniSius.size()-1);%>
			<td class="l">Data decorrenza sospensione / differimento <font class="ob">(*)</font></td>
			<td class="L"><input type="text" size="2" maxlength="2"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>"
				onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input type="text"
				size="2" maxlength="2"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>"
				onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input type="text"
				size="4" maxlength="4"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>"
				onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillYear(value)"></td>
		</tr>
		<tr>
			<td class="L" width="40%">Periodo sospensione:</td>
			<td>Anni <input title="Anni" size="2" maxlength="2" value=""
				type="text"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_AA%>">
			Mesi <input title="Mesi" size="2" maxlength="2" value="" type="text"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_MM%>">
			Giorni <input title="Giorni" size="2" maxlength="2" value=""
				type="text"
				name="<%= ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_GG%>">
			</td>
		</tr>
		<tr>
			<td class="l">Fino al</td>
			<td class="L">
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> /
        <input value="" type="text" size="2" maxlength="2" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>"	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"	onBlur="javascript:value=FillDM(value)"> /
        <input value=""	type="text" size="4" maxlength="4" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
		</tr>
		<tr>
			<td class="l"><input type="radio" value="0"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE%>"
				onClick="javascript:radio();">da non recuperare <input
				type="radio" value="1"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE%>" CHECKED
				onClick="javascript:radio();">da recuperare</td>
			<td>
			<div id="noderadio" style="visibility:hidden; width:100%;">
			<table width="100%">
				<tr>
					<td>Anni <input value="" type="text" size="2"	maxlength="2"
						name="<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_AA%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)">
					  Mesi <input value="" type="text" size="2"	maxlength="2"
						name="<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_MM%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)">
					  Giorni <input value="" type="text" size="4"	maxlength="4"
						name="<%=ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_GG%>"
						onFocus="javascript:textboxSelect(this)"
						onkeypress="return TicTabNumField(this,event)"></td>
				</tr>
			</table>
			</div>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td class="l" colspan=2>Autorità che ha concesso il differimento:</td>
		</tr>
		<tr>
			<td class="l">Tipo Autorità</td>
			<td class="l"><select title="Destinatario"
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO%>" onchange="pulisciAutorita()">
				<%=tipoAutorita%></select>
				 </td>

		</tr>
		<tr>
			<td class="l">Sede Autorità <font class="ob">(*)</font></td>
			<td class="l"><input Title="Sede "
				name="<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>"

	<% 		if (lPerMod.getDataScadenza() == null && !lPerMod.getDescrLuogoAutorita().equals("-")){%>
					value="<%=lPerMod.getDescrLuogoAutorita() %>"
			<%}else{%>
					value=""
	<%		}%>
				type="text" maxlength="35" size="35">
				<a href="Javascript:ListaUffici('LoadInserisciSospensioneSanzioneSostitutivaUDS','<%=ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA%>');">
				<img src="/images/filefolder.gif" border=0> </a>
			</td>
		</tr>
		<tr>
			<td class="l">Note </td>
<%
			String note = "";
			if (lPerMod.getMotivazione() != null && lPerMod.getFlagMotivo().equals("03")) {
				note = lPerMod.getMotivazione();
}%>
		<td class="L">
			<TEXTAREA title="Note" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MOTIVAZIONE%>" cols=40 rows=5><%=note%></textarea>
		</td>
	</tr></table>

	<input type="HIDDEN" value="03"
		name="<%=ICostantiSanzioneSostitutiva.CAMPO_FLAG_MOTIVO%>">

	<table cellspacing=2 cellpadding=2>
		<tr>
			<td><input class="bottone" type="submit" value="Conferma">
			<input type="HIDDEN"
				name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>"
				value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>">
			</td>
		</tr>
	</table>
</FORM>

<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSospensioneSanzioneSostitutivaUDS");

    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>","req","Il campo Giorno della data inizio esecuzione è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>","req","Il campo Mese della data inizio esecuzione è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_INIZIO_ESECUZIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","req","Il campo Anno della data inizio esecuzione è obbligatoria");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","minlen=4","La lunghezza del campo Anno della data inizio esecuzione deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_AA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_MM%>","numeric");
		frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_GG%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA%>","minlen=4","La lunghezza del campo Anno della data scadenza deve essere di 4 caratteri");

		frmvalidator.addValidation("<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_INIZIO_ESECUZIONE%>","numeric");

		var data_scadenza_totale=document.LoadInserisciSospensioneSanzioneSostitutivaUDS.data_scadenza_totale.value;

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>
