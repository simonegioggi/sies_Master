<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria"        scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>
<jsp:useBean id="TipoPenaAccessoria"    scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPeneAccessorie"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaSentenza"      scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     					scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaOrdinanza"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tenoreOrdinanza"       scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>

<head>
<title>[S.I.E.S.] - Gestione Pena Accessoria </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function Verify()
  {
    if (document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_DPR%>.value.length==1)
      document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_DPR%>.value='0'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_DPR%>.value;
    if (document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_DPR%>.value.length==1)
      document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_DPR%>.value='0'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_DPR%>.value;

    var data_dpr=document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_DPR%>.value+'/'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_DPR%>.value+'/'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_DPR%>.value;
    if (! ControllaData(data_dpr) && data_dpr.length>2)
    {
      alert('Data DPR non valida');
      return false;
    }

    if (document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value.length==1)
      document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value='0'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value;
    if (document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value.length==1)
      document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value='0'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value;

    var data_sentenza=document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciEsecuzionePA.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value;
    if (! ControllaData(data_sentenza) && data_sentenza.length>2)
    {
      alert('Data Sentenza di Revoca non valida');
      return false;
    }

    Inserisci();
  }
  </script>
  <script language="JavaScript">
<%
    if(!lTipoFunzione.equals(""))
    {
%>
      function  Benefici()
      {
        document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInserisciBeneficio&lTipoFunzione=<%=lTipoFunzione%>";
        document.LoadInserisciEsecuzionePA.AggAtt.disabled=true;
        document.LoadInserisciEsecuzionePA.Inserisci.disabled=true;
      }
<%
    }
%>
    function  Inserisci()
    {
<%
      if( modalita.equals("I") )
      {
%>
        document.LoadInserisciEsecuzionePA.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.penaaccessoria.action.ActInserisciPenaAccessoria";
<%    }
      else if( modalita.equals("M") )
      {
%>
        document.LoadInserisciEsecuzionePA.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.penaaccessoria.action.ActModificaPenaAccessoria";
<%
      }

      if(!lTipoFunzione.equals(""))
      {
%>
        document.LoadInserisciEsecuzionePA.AggAtt.disabled=true;
<%
      }
%>
      document.LoadInserisciEsecuzionePA.Inserisci.disabled=true;
    }
</script>

</head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
			 PenaAccessoriaModel lPenaAccessoria = new PenaAccessoriaModel();
			// String lAzione = new String();
			 if( modalita.equals("I") )
			 {
			  // lAzione = "siap.siep.penaaccessoria.action.ActInserisciPenaAccessoria";
%>
			   <font class="campo">Inserimento Pena Accessoria</font>
<%
       }
       else if( modalita.equals("M") )
       {
			   //lAzione = "siap.siep.penaaccessoria.action.ActModificaPenaAccessoria";
         lPenaAccessoria = penaaccessoria;
%>
         <font class="campo">Modifica Pena Accessoria</font>
<%
       }
%>
      </td>
  		<!-- BOTTONE DI RITORNO -->
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEsecuzionePA">
  <table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Tipo di Pena Accessoria</td>
      <td class="l">
        <select class="small" name="<%= ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA %>"  >
          <%=TipoPenaAccessoria%>
        </select>
      </td>
		</tr>
		<tr>
				<td class="l">Tipo Durata</td>
				<td class="l">
        <select name="<%= ICostantiPenaAccessoria.CAMPO_DURATA %>">
          <%=DurataPeneAccessorie%>
        </select>
        </td>
		</tr>
		<tr>
				<td class="l">Durata</td>
        <td class="l">
          Anni <input maxlength=2 size=2 Title="Anni Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumAnni()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_ANNI %>">
          Mesi <input maxlength=2 size=2 Title="Mesi Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumMesi()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_MESI %>">
          Giorni <input maxlength=2  size=2 Title="Giorni Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumGiorni()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_GIORNI %>">
        </td>
		</tr>

    <tr><td class="Titolo" colspan=4>Estremi Ordinanza Applicazione del GE</td></tr>
    <tr>
      <td class="l">Data Ordinanza</td>
      <td class="l">
        <input Title="Giorno Data Ordinanza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaGE(),"dd")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_ORDINANZA_GE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Mese Data Ordinanza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaGE(),"MM")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_ORDINANZA_GE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Anno Data Ordinanza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaGE(),"yyyy")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_ORDINANZA_GE%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Ordinanza</td>
      <td class="l">
        <input Title="Anno Ordinanza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoOrdinanzaGE())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_ORDINANZA_GE%>" maxlength="4" size="4">
        /
        <input Title="Numero Ordinanza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroOrdinanzaGE())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_ORDINANZA_GE%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Ordinanza</td>
        <td class="l">
          <select Title="Autorità Ordinanza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_GE%>">
            <%=autoritaOrdinanza%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Luogo Ordinanza</td>
      <td class="l">
        <input Title="Luogo Ordinanza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_GE%>" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getDescrLuogoUfficioOrdinanzaGE())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_GE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

<%------------%>
    <tr><td class="Titolo" colspan=4>Estremi Ordinanza Condono/Revoca/Sostituzione</td></tr>
		<tr>
      <td class="l">Tenore</td>
      <td class="l">
      	<select name="<%= ICostantiPenaAccessoria.CAMPO_FLAG_CONDONATA %>">
        	<%=tenoreOrdinanza%>
      	</select>
        &nbsp;&nbsp;<font class="l">Data Ordinanza</font>
        <input Title="Giorno data Ordinanza" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaPA(),"dd")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input Title="Mese data Ordinanza" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaPA(),"MM")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input Title="Anno data Ordinanza" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataOrdinanzaPA(),"yyyy")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
		</tr>

    <tr>
      <td class="l">Anno/Numero Ordinanza</td>
      <td class="l">
        <input Title="Anno Ordinanza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoOrdinanzaPA())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_ORDINANZA_PA%>" maxlength="4" size="4">        /
        <input Title="Numero Ordinanza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroOrdinanzaPA())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_ORDINANZA_PA%>" maxlength="6" size="6">
      </td>
		</tr>

    <tr>
      <td class="l">Autorità Emittente</td>
        <td class="l">
          <select Title="Autorità Emittente" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA%>">
            <%=autoritaOrdinanza%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Luogo Ordinanza</td>
      <td class="l">
        <input Title="Luogo Ordinanza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getDescrLuogoUfficioOrdinanzaPA())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

		<tr>
      <td class="l">Data Dpr</td>
      <td class="l">
        <input Title="Giorno data DPR" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataDpr(),"dd")) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_DPR %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input Title="Mese data DPR" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataDpr(),"MM")) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_MESE_DATA_DPR %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input Title="Anno data DPR" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaAccessoria.getDataDpr(),"yyyy")) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_ANNO_DATA_DPR %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	<font class="l">&nbsp;&nbsp; Numero Dpr</font>
        <input Title="Numero DPR" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumDpr()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_DPR %>" size=8 maxlength=8>
      </td>
		</tr>
<%------------%>

    <tr><td class="Titolo" colspan=4>Revoca Condono</td></tr>
		<tr>
      <td class="l">Revoca Condono</td>
      <td class="l">
        <input title="Revoca Condono" type='checkbox' name='<%=ICostantiPenaAccessoria.CAMPO_FLAG_REVOCA_CONDONO%>' value='S' <%=(lPenaAccessoria.getFlagRevocaCondono() != null && lPenaAccessoria.getFlagRevocaCondono().equals("S")) ? "checked" : ""%>>
      </td>
		</tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="l">
        <input Title="Giorno Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataSentenzaRevoca(),"dd")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Mese Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataSentenzaRevoca(),"MM")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_SENTENZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input Title="Anno Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lPenaAccessoria.getDataSentenzaRevoca(),"yyyy")) %>" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="l">
        <input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoSentenzaRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_SENTENZA_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Sentenza" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroSentenzaRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_SENTENZA_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Sentenza</td>
        <td class="l">
          <select Title="Autorità Sentenza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>">
            <%=autoritaSentenza%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Luogo Sentenza</td>
      <td class="l">
        <input Title="Luogo Sentenza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getDescrLuogoSentenzaRevoca())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciEsecuzionePA','<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. PM</td>
      <td class="l">
        <input Title="Anno Re.Ge. PM" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegePmRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_PM_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. PM" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegePmRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_PM_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. GIP</td>
      <td class="L">
        <input Title="Anno Re.Ge. GIP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegeGipRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_GIP_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. GIP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegeGipRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_GIP_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. DIB</td>
      <td class="L">
        <input Title="Anno Re.Ge. DIB" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegeDibRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_DIB_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. DIB" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegeDibRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_DIB_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CAS</td>
      <td class="L">
        <input Title="Anno Re.Ge. CAS" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegeCasRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAS_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. CAS" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegeCasRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CAS_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CAP</td>
      <td class="L">
        <input Title="Anno Re.Ge. CAP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegeCapRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAP_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. CAP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegeCapRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CAP_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Re.Ge. CASAP</td>
      <td class="L">
        <input Title="Anno Re.Ge. CASAP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getAnnoRegeCasapRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CASAP_REVOCA%>" maxlength="4" size="4">
        /
        <input Title="Numero Re.Ge. CASAP" value="<%=StringUtils.toStringJSP(lPenaAccessoria.getNumeroRegeCasapRevoca())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CASAP_REVOCA%>" maxlength="6" size="6">
      </td>
    </tr>
		<tr>
      <td class="l">Falsità di documenti</td>
      <td class="l">
        <input title="Falsit" type='checkbox' name='<%=ICostantiPenaAccessoria.CAMPO_FLAG_DICHIARAZIONE_FALSITA%>' value='S' <%=(lPenaAccessoria.getFlagDichiarazioneFalsita() != null && lPenaAccessoria.getFlagDichiarazioneFalsita().equals("S")) ? "checked" : ""%>>
      </td>
		</tr>
		<tr>
      <td class="l">Note</td>
      <td class="l">
        <Textarea Title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lPenaAccessoria.getNote())%></textarea>
      </td>
		</tr>
    <tr>
      <td>
        <input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      </td>
<%
    if(!lTipoFunzione.equals(""))
    {
%>
      <td>
        <input type="button"  class="bottone"  name="AggAtt" value="Prosegui" onClick="javascript:return Benefici();">
      </td>
<%
    }
%>
    </tr>
  </table>

  <input value="<%=lPenaAccessoria.getIdPenaAccessoria() %>" type="HIDDEN" name="<%= ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA %>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciEsecuzionePA");

  frmvalidator.addValidation("<%= ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA %>","req","Il campo Tipo Pena Accessoria è obbligatorio");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUM_ANNI%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUM_MESI%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUM_GIORNI%>","numeric");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_DPR%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_DPR%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_DPR%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_PM_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_PM_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_PM_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_PM_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_GIP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_GIP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_GIP_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_GIP_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_DIB_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_DIB_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_DIB_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_DIB_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CAS_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAS_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAS_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAS_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CAP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAP_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CAP_REVOCA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_NUMERO_REGE_CASAP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CASAP_REVOCA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CASAP_REVOCA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoria.CAMPO_ANNO_REGE_CASAP_REVOCA%>","lt=3000");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>