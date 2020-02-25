<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel"%>
<%@ page
	import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<jsp:useBean id="regesentenza" scope="request"
	class="siap.regesies.regesentenza.model.RegeSentenzaModel" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request"
	class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />

<head>
<title>[S.I.E.S.] - Gestione Decreto</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
        var desktop;
        function ListaComuni(a_formname,a_fieldname)
        {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    </script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
    function Verify()
    {
      var d1=document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
      if (! ControllaData(d1))
      {	alert('Data di arrivo atto non valida');
         return false;
      }
      var d2=document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      if (! ControllaData(d2))
      {	alert('Data Decreto non valida');
         return false;
      }
      var d3=document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>.value;
      if (! ControllaData(d3))
      {
        alert('Data di Iscrizione non valida');
        return false;
      }
      if (CompareDate(d3,d2))
      {
        alert('La Data di Iscrizione deve essere successiva alla Data Decreto');
        document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>.focus()
        return false;
      }
      
      var d4=document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if (! ControllaData(d4))
      {	alert('Data irrevocabità non valida');
         return false;
      }
      if (! CompareDate(d2,d1))
      {	alert('La Data Decreto deve essere antecedente alla Data di Arrivo');
         return false;
      }
      if (! CompareDate(d2,d4))
      {	alert('La Data Decreto deve essere antecedente alla Data di Irrevocabilità');
         return false;
      }
      return true;
    }
  </script>
</head>

<body class="corpo">
<%
RegeSentenzaModel lSentenza = new RegeSentenzaModel();
%>
<table>
	<tr>
		<td class="LBG"><a href="Javascript:window.print();">
			<img align="middle"
			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
			alt="Stampa questa videata" border=0></a></td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; <font
			class="campo">Modifica Estremi Decreto Penale Rege</font>
		<td class="LBG"><jsp:include
			page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
			<jsp:param name="CampoIdEntita"
				value="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>" />
			<jsp:param name="ValoreIdEntita" value="<%=lSentenza.getIdFile()%>" />
		</jsp:include></td>
		<%
			String lAction = new String();
			lAction = "siap.regesies.regesentenza.action.ActModificaRegeDecreto";
			lSentenza = new RegeSentenzaModel(regesentenza);
		%>
		</td>
	</tr>
</table>
<br>
<jsp:include
	page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>" />
<br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name="LoadInserisciDecreto">

<table cellspacing=2 cellpadding=2>

	<tr>
		<td class="l">Data Arrivo Atto<font class="ob">(*)</font></td>
		<td class="L"><input Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"MM")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"yyyy")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>

		<td class="l">Data Inserimento &nbsp;&nbsp; <font class="campo">
		<%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "dd"))%>
		- <%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "MM"))%>
		- <%=StringUtils.toStringJSP(DateUtils.getDateToString(
							lSentenza.getDataProvvedimento(), "yyyy"))%>
		</font></td>
	</tr>
	<tr>
		<td class="l">Data Iscrizione <font class="ob">(*)</font></td>
		<td class="L"><input Title="Data Iscrizione" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"dd")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"MM")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Arrivo Atto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"></td>
	</tr>
	<tr>
		<td class="l">Anno/Numero R.G.N.R.<font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Re.Ge. PM"
			value="<%=StringUtils.intZerotoString( lSentenza.getAnnoRegePm()) %>"
			type="text" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_PM %>"
			maxlength="4" size="4"> /<input Title="Numero Re.Ge. PM"
			value="<%=lSentenza.getNumeroRegePm() %>" type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUMERO_REGE_PM %>"
			maxlength="6" size="6"></td>

		<td class="l"></td>
		<td class="L"></td>

	</tr>

	<tr>
		<td class="l">Anno/Numero Reg.Gen. GIP </font></td>
		<td class="L"><input Title="Anno Re.Ge. GIP"
			value="<%=StringUtils.intZerotoString( lSentenza.getAnnoRegeGip()) %>"
			type="text" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_GIP %>"
			maxlength="4" size="4"> /<input Title="Numero Re.Ge. GIP"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRegeGip()) %>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUMERO_REGE_GIP  %>"
			maxlength="6" size="6"></td>
	<tr>
		<td class="Titolo" colspan=4>Decreto Penale da Eseguire</td>
	</tr>
	</tr>
	<tr>
		<td class="l">Data Decreto <font class="ob">(*)</font></td>
		<td class="L" colspan=3><input Title="Data Decreto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Decreto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Decreto" type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Decreto <font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Decreto"
			value="<%=StringUtils.intZerotoString( lSentenza.getAnnoSentenza()) %>"
			type="text" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA %>"
			maxlength="4" size="4"> /<input Title="Numero Decreto"
			value="<%=lSentenza.getNumeroSentenza() %>" type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA %>"
			maxlength="6" size="6"></td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L" colspan=3><select Title="Autorità Emittente"
			name="<%= ICostantiRegeSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
			<%=autoritaEmi%>
		</select></td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3><input Title="Luogo Emittente"
			name="<%=ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
			value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text"
			maxlength="35" size="35"> <a
			href="Javascript:ListaComuni('LoadInserisciDecreto','<%= ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE %>');">
		<img src="/images/filefolder.gif" border=0> </a></td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente </font></td>
		<td class="L" colspan=3><input Title="Sezione Autorità Emittente"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>"
			maxlength="30" size="30"></td>
	</tr>



	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Sentenza Cassazione</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza</font></td>
		<td class="L" colspan=3><input Title="Anno Sentenza Cassazione"
			value="<%=StringUtils.intZerotoString( lSentenza.getAnnoSentenzaCassazione())%>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>"
			maxlength="4" size="4"> /<input
			Title="Numero Sentenza Cassazione"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione()) %>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>"
			maxlength="6" size="6"></td>
	</tr>

	<tr>
		<td class="l">Anno/Numero Raccolta Generale</font></td>
		<td class="L" colspan=3><input Title="Anno Raccolta Generale"
			value="<%=StringUtils.intZerotoString( lSentenza.getAnnoRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>"
			maxlength="4" size="4"> /<input
			Title="Numero Raccolta Generale"
			value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%>"
			type="text"
			name="<%= ICostantiRegeSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>"
			maxlength="6" size="6"></td>
	</tr>

	<tr>

		<td class="l">Dispositivo Cassazione</font></td>
		<td class="L" colspan=3><select Title="Dispositivo Cassazione"
			name="<%= ICostantiRegeSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>">
			<%=tipoDecisioneCassazione%>
		</select></td>
	</tr>


	<!---------------------------------------------------------->
	<tr>
		<td class="l">Data Irrevocabilità <font class="ob">(*)</font></td>
		<td class="L" colspan=3><input Title="Data Irrevocabilità "
			type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Irrevocabilità " type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"MM")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>"
			maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillDM(value)"> - <input
			Title="Data Irrevocabilità " type="text"
			value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"yyyy")) %>"
			name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"
			maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
			onkeypress="return TicTabNumField(this,event)"
			onBlur="javascript:value=FillYear(value)"></td>
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiRegeSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
		</td>
	</tr>
	<tr>
		<td colspan=2><br>
		<INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
		</td>
	</tr>
</table>

<input type="HIDDEN" name="Action" value="<%=lAction%>"> <input
	type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>"
	value="<%=lSentenza.getIdFile()%>"></form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciDecreto");


  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>","req","Il campo Giorno di Arrivo dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>","req","Il campo Mese di Arrivo dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>","req","Il campo Anno di Arrivo dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_PM %>","req","Il campo Anno Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_REGE_PM%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_NUMERO_REGE_PM %>","req","Il campo Numero Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA %>","req","Il campo Mese Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il campo Giorno Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req","Il campo Giorno della Data irrevocabilità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","req","Il campo Mese della Data irrevocabilità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req","Il campo Anno della Data irrevocabilità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","lt=2999");
   frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","req","Il Giorno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","req","Il Mese della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","req","L'Anno della Data irrevocabilità è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","numeric");
  
  frmvalidator.setAddnlValidationFunction("Verify");


    </script>
</body>
</html>