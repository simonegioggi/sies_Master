<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.esperto.model.EspertoModel"%>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="modalita"				scope="request" class="java.lang.String"/>
<jsp:useBean id="esperto"         scope="request" class="siap.sius.esperto.model.EspertoModel"/>
<jsp:useBean id="elencoFlagStato" scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione"         scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Esperto </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

    <script language="JavaScript">
      var DataIni= "";   // data Inizio Validità limite inferiore

      function init()
      {
      	document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_COGNOME%>.focus();
      }

      function initDataIni (data)
      {
        DataIni = data;
      }

      function  Verify()
      {
        var ritorno = true;
        var data_to_verify1 = document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
        var data_to_verify2 = document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

        if ((document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciEsperto.<%=ICostantiEsperto.CAMPO_NOME%>.value.length == 0))
        {
          alert("Occorre inserire Cognome e Nome");
          ritorno = false;
        }
        else if (data_to_verify1.length > 2)
        {
          if (! ControllaData(data_to_verify1))
          {
            alert('Data di Inizio Validità scorretta');
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
            	alert('Data di Fine Validità scorretta');
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
      EspertoModel lEsperto = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        lEsperto = new EspertoModel();
        lAzione = "siap.sius.esperto.action.ActInserisciEsperto";
        lEsperto.setDataInizioValidita(DateUtils.getSysDate());
    %>
    	<font class="campo">Inserimento di un Esperto</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lEsperto = esperto;
        if(lEsperto.getDataInizioValidita() == null)
          lEsperto.setDataInizioValidita(DateUtils.getSysDate());
        lAzione = "siap.sius.esperto.action.ActModificaEsperto";
    %>
    		<font class="campo">Modifica di un Esperto</font>
    <%
        }
    %>
    		</td>
    	</tr>
    	<script language="JavaScript">
      	initDataIni( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lEsperto.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
    	</script>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEsperto">
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class="l">Cognome (*)</td>
          <td class="l">
          	<input value="<%=lEsperto.getCognome() %>" type="text" name="<%= ICostantiEsperto.CAMPO_COGNOME %>">
          </td>
        </tr>
        <tr>
          <td class="l">Nome (*)</td>
          <td class="l">
          	<input value="<%=lEsperto.getNome() %>" type="text" name="<%=ICostantiEsperto.CAMPO_NOME%>">
          </td>
        </tr>
        <tr>
          <td class="l">Codice Fiscale</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getCodiceFiscale())%>" type="text" name="<%=ICostantiEsperto.CAMPO_CODICE_FISCALE%>" size="16" maxlength="16">
          </td>
        </tr>
        <tr>
          <td class="l">Indirizzo</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getIndirizzo())%>" type="text" name="<%=ICostantiEsperto.CAMPO_INDIRIZZO%>">
          </td>
        </tr>
        <tr>
          <td class="l">Telefono</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getTelefono())%>" type="text" name="<%=ICostantiEsperto.CAMPO_TELEFONO%>">
          </td>
        </tr>
        <tr>
          <td class="l">Email</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getEmail())%>" type="text" name="<%=ICostantiEsperto.CAMPO_EMAIL %>">
          </td>
        </tr>
        <tr>
          <td class="l">Fax</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getFax())%>" type="text" name="<%=ICostantiEsperto.CAMPO_FAX %>">
          </td>
        </tr>
        <tr>
          <td class="l">Cellulare</td>
          <td class="l">
          	<input value="<%=StringUtils.toStringJSP(lEsperto.getCellulare())%>" type="text" name="<%=ICostantiEsperto.CAMPO_CELLULARE%>">
          </td>
        </tr>

        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiEsperto.CAMPO_FLAG_STATO %>">
            <%=elencoFlagStato%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Data Inizio Validita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataInizioValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiEsperto.CAMPO_GIORNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataInizioValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiEsperto.CAMPO_MESE_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataInizioValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiEsperto.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento di un Esperto da Funzioni Amministrative
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) ){
%>
			<a href="javascript:calendario('LoadInserisciEsperto','<%=ICostantiEsperto.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiEsperto.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiEsperto.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
          </td>
        </tr>
        <tr>
          <td class="l">Data Fine Validita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiEsperto.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiEsperto.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEsperto.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiEsperto.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento/Modifica di un Esperto da Funzioni Amministrative
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) ){
%>
			<a href="javascript:calendario('LoadInserisciEsperto','<%=ICostantiEsperto.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiEsperto.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiEsperto.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
          </td>
        </tr>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiEsperto.CAMPO_ID_ESPERTO%>" value="<%=lEsperto.getIdEsperto()%>" >
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciEsperto");
        frmvalidator.addValidation("<%=ICostantiEsperto.CAMPO_CELLULARE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiEsperto.CAMPO_FAX%>","numeric");
        frmvalidator.addValidation("<%=ICostantiEsperto.CAMPO_TELEFONO%>","numeric");
      </script>
   </body>
</html>