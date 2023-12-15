<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.pagoPA.action.ICostantiErroriSiesPagopa"%>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="TipoEvento"     scope="request" class="java.lang.String" />

<html>
<head>
  <title>[S.I.E.S.] - VERIFICA ERRORI SU PAGOPA </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

  <script language="JavaScript">
    function Verify()
    {
      var data_inizio = document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_DAL%>.value
                   +'/'+document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_MESE_DATA_DAL%>.value
                   +'/'+document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_ANNO_DATA_DAL%>.value;
      var data_fine   = document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_AL%>.value
                   +'/'+document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_MESE_DATA_AL%>.value
                   +'/'+document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_ANNO_DATA_AL%>.value;

      if (!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data iniziale non valida');
        document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_DAL%>.focus();
        return false;
      }
      
      if (!ControllaDataPassaVuota(data_fine))
      {
        alert('Data finale non valida');
        document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_AL%>.focus();
        return false;
      }

      if (!(data_inizio.length==2 || data_fine.length==2) )
      {
        if(!CompareDate(data_inizio,data_fine))
        {
          alert('La Data di ricerca finale non può essere inferiore alla data iniziale');
          document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_AL%>.focus();
          return false;
        }
      }

      if (document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE%>[1].checked) {
    	  var codUtente = document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_COD_UTENTE%>.value;
        if(codUtente.length==0 )
        {
           alert('Indicare il codice utente');
           document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_COD_UTENTE%>.focus();
           return false;
        }    	  
      }
      
      return true;
    }
    
    
    function changeRadio(){
    	if (document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE%>[0].checked)
    		  document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_COD_UTENTE%>.disabled = true;
    	else 
    		  document.LoadVerificaErroriPagopa.<%=ICostantiErroriSiesPagopa.CAMPO_COD_UTENTE%>.disabled = false;
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;<font class="campo">VERIFICA ERRORI SU PAGOPA </font>
      </td>
    </tr>
  </table>
    
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadVerificaErroriPagopa'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPA.action.ActVerificaErroriPagopa">

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Titolo" colspan="6"> Seleziona Periodo da verificare </td>
      </tr>
      <tr>
        <td class="label">Dalla data &nbsp;</td>
        <td class="label">
          <input type="text" maxlength="2" size="2" Title="Data di ricerca inizio"  
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_DAL %>"           
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
          <input type="text" maxlength="2" size="2" Title="Data di ricerca inizio"  
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_MESE_DATA_DAL %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           -
          <input type="text" maxlength="4" size="4" Title="Data di ricerca inizio"  
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_ANNO_DATA_DAL %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>

        <td class="label"> Alla data </td>
        <td class="label">
          <input type="text" maxlength="2" size="2" Title="Data di ricerca fine" 
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_GIORNO_DATA_AL %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
          <input type="text" maxlength="2" size="2" Title="Data di ricerca fine"  
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_MESE_DATA_AL %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
           -
          <input type="text" maxlength="4" size="4" Title="Data di ricerca fine" 
                 name="<%= ICostantiErroriSiesPagopa.CAMPO_ANNO_DATA_AL %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>
      <tr>
        <td class="label">Funzione in Errore &nbsp;</td>
        <td class="label" colspan="3">      
          <select Title="Tipo Evento" class1="small" name="<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_EVENTO%>" >
               <%=TipoEvento%> 
          </select>
        </td>
      </tr>             
    </table>

    <br>
    
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="Titolo" colspan="3">
          Seleziona Utente da verificare
        </td>
      </tr>
      <tr>
        <td class="label" >
          <input type="radio" checked  onclick="changeRadio();"
                 name="<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE%>" 
                 value="<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE_TUTTI%>" >
        </td>
        <td class="label" >
          Tutti
        </td>
      </tr>
      <tr>
        <td class="label" >
          <input type="radio" onclick="changeRadio();"
                 name="<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE%>" 
                 value="<%=ICostantiErroriSiesPagopa.CAMPO_TIPO_UTENTE_CODICE%>">
        </td>
        <td class="label" >
          Utente con codice :
        </td>
        <td>
          <input type="text" maxlength="11" size="8" Title="Codice Utente" name="<%= ICostantiErroriSiesPagopa.CAMPO_COD_UTENTE %>"  disabled>
        </td>
      </tr>
    </table>

    <BR>
    
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadVerificaErroriPagopa");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>