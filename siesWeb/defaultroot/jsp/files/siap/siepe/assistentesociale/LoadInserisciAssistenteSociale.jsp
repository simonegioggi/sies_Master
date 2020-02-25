<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siepe.assistentesociale.model.AssistenteSocialeModel"%>
<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale"%>

<jsp:useBean id="modalita"          scope="request" class="java.lang.String"/>
<jsp:useBean id="assistentesociale" scope="request" class="siap.siepe.assistentesociale.model.AssistenteSocialeModel"/>
<jsp:useBean id="elencoFlagStato"   scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - GestioneAssistenteSociale </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

    <script language="JavaScript">
      var DataIni= "";   // data Inizio Validità limite inferiore

      function init()
      {
      	document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_COGNOME%>.focus();
      }

      function initDataIni (data)
      {
        DataIni = data;
      }

      function  Verify()
      {
        var ritorno = true;
        var data_to_verify1 = document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
        var data_to_verify2 = document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

        if ((document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciAssistenteSociale.<%=ICostantiAssistenteSociale.CAMPO_NOME%>.value.length == 0))
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

    </script>

  </head>

  <body class="corpo" onload="Javascript:init();">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <%
      AssistenteSocialeModel lAssistenteSociale = null;
      String lAzione = new String();
      if( modalita.equals("I") )
      {
        lAssistenteSociale = new AssistenteSocialeModel();
        lAzione = "siap.siepe.assistentesociale.action.ActInserisciAssistenteSociale";
        lAssistenteSociale.setDataInizioValidita(DateUtils.getSysDate());
    %>

    <font class="campo">Inserimento di un AssistenteSociale</font>
    <%
      }
      else if( modalita.equals("M") )
      {
        lAssistenteSociale = assistentesociale;
        if(lAssistenteSociale.getDataInizioValidita() == null)
          lAssistenteSociale.setDataInizioValidita(DateUtils.getSysDate());
        lAzione = "siap.siepe.assistentesociale.action.ActModificaAssistenteSociale";
    %>
    <font class="campo">Modifica di un AssistenteSociale</font>
    <%
        }
    %>
    </td>
    </tr>
    <script language="JavaScript">
      initDataIni( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lAssistenteSociale.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
    </script>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAssistenteSociale">
      <table cellspacing=4 cellpadding=4>
        <tr>
          <td class="l">Cognome (*)</td>
          <td class="l"><input value="<%=lAssistenteSociale.getCognome() %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_COGNOME %>"  ></td>
        </tr>
        <tr>
          <td class="l">Nome (*)</td>
          <td class="l"><input value="<%=lAssistenteSociale.getNome() %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_NOME %>"  ></td>
        </tr>
        <tr>
          <td class="l">Codice Fiscale</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP( lAssistenteSociale.getCodiceFiscale()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_CODICE_FISCALE %>" size="16" maxlength="16"></td>
        </tr>
        <tr>
          <td class="l">Indirizzo</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP( lAssistenteSociale.getIndirizzo()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_INDIRIZZO %>"  ></td>
        </tr>
        <tr>
          <td class="l">Telefono</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lAssistenteSociale.getTelefono()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_TELEFONO %>"  ></td>
        </tr>
        <tr>
          <td class="l">Email</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lAssistenteSociale.getEmail()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_EMAIL %>"  ></td>
        </tr>
        <tr>
          <td class="l">Fax</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lAssistenteSociale.getFax()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_FAX %>"  ></td>
        </tr>
        <tr>
          <td class="l">Cellulare</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lAssistenteSociale.getCellulare()) %>" type="text" name="<%= ICostantiAssistenteSociale.CAMPO_CELLULARE %>"  ></td>
        </tr>

        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiAssistenteSociale.CAMPO_FLAG_STATO %>">
            <%=elencoFlagStato%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Data Inizio Validità</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataInizioValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteSociale.CAMPO_GIORNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataInizioValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteSociale.CAMPO_MESE_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataInizioValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiAssistenteSociale.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
        <tr>
          <td class="l">Data Fine Validità</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteSociale.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteSociale.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteSociale.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiAssistenteSociale.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE%>" value="<%=lAssistenteSociale.getIdAssistenteSociale()%>" >
      </form>

      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciAssistenteSociale");
        frmvalidator.addValidation("<%=ICostantiAssistenteSociale.CAMPO_CELLULARE%>","numeric");
        frmvalidator.addValidation("<%=ICostantiAssistenteSociale.CAMPO_FAX%>","numeric");
        frmvalidator.addValidation("<%=ICostantiAssistenteSociale.CAMPO_TELEFONO%>","numeric");
      </script>
   </body>
</html>