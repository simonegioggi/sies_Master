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

<title>[S.I.E.S.] - Gestione Sentenza Straniera</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript">
      function VerifySS()
      {
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

        //Data arrivo atto
       //  var d1=document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
 
        //Data Sentenza
        var d2=document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (! ControllaData(d2))
        {
          alert('Data Sentenza non valida');
          return false;
        }

        if (! CompareDate(d2,d1))
        {
          alert('La Data Sentenza deve essere antecedente alla Data di Arrivo');
          return false;
        }

      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
  </script>
  
<%
	SentenzaModel lSentenza = new SentenzaModel();
	String lAction = new String();
	if (modalita.equals("I")) 
	{
		lAction = "siap.siep.sentenza.action.ActInserisciSentenzaStraniera";
	} 
	else if (modalita.equals("M")) 
	{
		lAction = "siap.siep.sentenza.action.ActModificaSentenzaStraniera";
		lSentenza = new SentenzaModel(sentenza);
	}
%>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenzaStraniera">
  <table cellspacing=2 cellpadding=2>
	
		<tr>

			<td class="l" colspan="2">Data Inserimento &nbsp;&nbsp; <font	class="campo"> 
<%
			if (modalita.equals("M")) 
			{%> 
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataInserimento(), "dd-MM-yyyy"))%> 
		<%}else{%> 
			<%=StringUtils.toStringJSP(DateUtils.getDateToString( DateUtils.getSysDate(), "dd-MM-yyyy"))%> 
		<%}%> 
 			</font>
 			</td>
		</tr>
	
    <tr><td class="Titolo" colspan=4>Dati Sentenza di appello</td></tr>

    <tr>
      <td class="l">Data Sentenza <font class="ob">(*)</font></td>
         <td class="L" colspan=3>
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciSentenzaStraniera','<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>

          </td>
     </tr>

     <tr>
      <td class="l">Anno/Numero Sentenza </td>
      <td class="L">
          <input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" maxlength="4" size="4" >
         /<input Title="Numero Sentenza" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
      </td>
		</tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L" colspan=3>
          <select Title="Autorità Emittente" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" >
          <%= (modalita.equals("M")) ? autoritaEmi : autoritaEmi2%>
          </select>
      </td>
		</tr>
   	<tr>
				<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="L"  colspan=3>
         <input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
            value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35">
          <%-- a href="Javascript:ListaComuni('LoadInserisciSentenzaStraniera','<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>');"> --%>
		      <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenzaStraniera','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
		</tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=3>
          <input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30" >
      </td>
		</tr>

    <tr><td class="Titolo" colspan=4>Sentenza di riferimento</td></tr>
		<tr>
		  <td class="l">Estremi Sentenza Straniera</td>
      <td class="L" colspan=3>
        <textarea cols=80 rows=5 Title="Note" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
      </td>
		</tr>
 		</table>
	<%
	if (modalita.equals("M")) {
%>	
	 <jsp:include page="<%=ICostantiFasSigeSentenza.INC_FASCICOLI_SIGE%>"/>
<%} %>	
	
<table cellspacing=2 cellpadding=2>	
     <tr>
      <td colspan=2>
 	      <br>
        <INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="Action" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>">
	<%-- Hidden aggiunte --%>
<%
	if (modalita.equals("M"))
	{%>
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"MM"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"yyyy"))%>"> 
	<%} else {%>
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"dd"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"MM"))%>"> 
		<input type="HIDDEN" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy"))%>"> 
	<%}%>
	<%-- Fine Hidden aggiunte --%>

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadInserisciSentenzaStraniera");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
<%--     frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic"); --%>
    frmvalidator.setAddnlValidationFunction("VerifySS");
</script>