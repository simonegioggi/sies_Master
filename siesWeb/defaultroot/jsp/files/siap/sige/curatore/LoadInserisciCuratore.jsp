<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.curatore.model.CuratoreModel"%>
<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>

<jsp:useBean id="modalita"				scope="request" class="java.lang.String"/>
<jsp:useBean id="curatore"        scope="request" class="siap.sige.curatore.model.CuratoreModel"/>
<jsp:useBean id="elencoFlagStato" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Curatore </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

    <script language="JavaScript">
      var DataIni= "";   // data Inizio Validità limite inferiore

      function init()
      {
      	document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_COGNOME%>.focus();
      }

      function initDataIni (data)
      {
        DataIni = data;
      }

      function  Verify()
      {
        var ritorno = true;
        var data_to_verify1 = document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value +'/'+ 
        											document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value +'/'+ 
        											document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
        											
        var data_to_verify2 = document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value +'/'+ 
        											document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_MESE_DATA_FINE_VALIDITA%>.value +'/'+ 
        											document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

        if ((document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciCuratore.<%=ICostantiCuratore.CAMPO_NOME%>.value.length == 0))
        {
          alert("Occorre inserire Cognome e Nome");
          ritorno = false;
        }
        else if (data_to_verify1.length > 2)
        {
          if (! ControllaData(data_to_verify1))
          {
            alert('Data di Inizio Validità non corretta');
            ritorno = false;
          }
          else if (! CompareDate(DataIni, data_to_verify1))
          {
            alert('Data di Inizio Validità non può essere anticipata');
            ritorno = false;
          }
          else if (data_to_verify2.length > 2)
          {
            if (! ControllaData(data_to_verify2))
            {
            	alert('Data di Fine Validità non corretta');
            	ritorno = false;
            }
            else if (! CompareDate(data_to_verify1, data_to_verify2))
            {
              alert('Data di Inizio Validità non può essere successiva a quella di Fine');
              ritorno = false;
            }
          }
        }
        else if (data_to_verify2.length > 2)
        {
          alert('Non è possibile specificare Data di Fine Validità senza specificare quella di Inizio Validità ');
          ritorno = false;
        }
        return ritorno;
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>

  </head>

  <body class="corpo" onload="Javascript:init();">
    <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    	<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      CuratoreModel lCuratore = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        lCuratore = new CuratoreModel();
        lAzione = "siap.sige.curatore.action.ActInserisciCuratore";
        lCuratore.setDataInizioValidita(DateUtils.getSysDate());
    %>
    	<font class="campo">Inserimento di un Curatore</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lCuratore = curatore;
        if(lCuratore.getDataInizioValidita() == null)
          lCuratore.setDataInizioValidita(DateUtils.getSysDate());
        lAzione = "siap.sige.curatore.action.ActModificaCuratore";
    %>
    		<font class="campo">Modifica di un Curatore</font>
    <%
        }
    %>
    		</td>
    	</tr>
    	<script language="JavaScript">
      	initDataIni( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lCuratore.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
    	</script>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciCuratore">
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class="l">Cognome (*)</td>
          <td class="l">
          	<input value="<%=lCuratore.getCognome() %>" type="text" name="<%= ICostantiCuratore.CAMPO_COGNOME %>">
          </td>
        </tr>
        <tr>
          <td class="l">Nome (*)</td>
          <td class="l">
          	<input value="<%=lCuratore.getNome() %>" type="text" name="<%=ICostantiCuratore.CAMPO_NOME%>">
          </td>
        </tr>
        <tr>
          <td class="l">Codice Fiscale</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getCodiceFiscale())%>" type="text" name="<%=ICostantiCuratore.CAMPO_CODICE_FISCALE%>" size="16" maxlength="16">
          </td>
        </tr>
        <tr>
          <td class="l">Indirizzo</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getIndirizzo())%>" type="text" name="<%=ICostantiCuratore.CAMPO_INDIRIZZO%>">
          </td>
        </tr>
        <tr>
          <td class="l">Telefono</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getTelefono())%>" type="text" name="<%=ICostantiCuratore.CAMPO_TELEFONO%>">
          </td>
        </tr>
        <tr>
          <td class="l">Email</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getEmail())%>" type="text" name="<%=ICostantiCuratore.CAMPO_EMAIL %>">
          </td>
        </tr>
        <tr>
          <td class="l">Fax</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getFax())%>" type="text" name="<%=ICostantiCuratore.CAMPO_FAX %>">
          </td>
        </tr>
        <tr>
          <td class="l">Cellulare</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lCuratore.getCellulare())%>" type="text" name="<%=ICostantiCuratore.CAMPO_CELLULARE%>">
          </td>
        </tr>

        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiCuratore.CAMPO_FLAG_STATO %>">
            <%=elencoFlagStato%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Data Inizio Validita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataInizioValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiCuratore.CAMPO_GIORNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataInizioValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiCuratore.CAMPO_MESE_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataInizioValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiCuratore.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciCuratore','<%=ICostantiCuratore.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiCuratore.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiCuratore.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
	      	    <img src="/images/calendario.gif" border=0>
	       	</a>
          </td>
        </tr>
        <tr>
          <td class="l">Data Fine Validita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiCuratore.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiCuratore.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lCuratore.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiCuratore.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('LoadInserisciCuratore','<%=ICostantiCuratore.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiCuratore.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiCuratore.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
	      	    <img src="/images/calendario.gif" border=0>
	       	</a>
          </td>
        </tr>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiCuratore.CAMPO_ID_CURATORE%>" value="<%=lCuratore.getIdCuratore()%>" >
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciCuratore");
        frmvalidator.addValidation("<%=ICostantiCuratore.CAMPO_CELLULARE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiCuratore.CAMPO_FAX%>","numeric");
        frmvalidator.addValidation("<%=ICostantiCuratore.CAMPO_TELEFONO%>","numeric");
      </script>
   </body>
</html>