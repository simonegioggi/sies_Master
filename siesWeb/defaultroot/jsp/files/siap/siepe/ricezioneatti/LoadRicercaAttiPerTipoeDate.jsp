<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siepe.ricezioneatti.action.ICostantiRicezioneAtti"%>

<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="statoRicezione" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.P.E.] - R - Ricerca Atti Per Tipo e Date</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  	function init()
  	{
    	document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_ATTO%>.focus();
  	}
    function Verify()
    {
      var data_inizio=document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>.value+'/'+document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>.value+'/'+document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>.value;
      var data_fine=document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>.value+'/'+document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>.value+'/'+document.LoadRicercaAttiPerTipoeDate.<%=ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La data finale non può essere inferiore alla data di inizio');
        return false;
      }
  return true;
    }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo" onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Atto per Tipo e Date</font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaAttiPerTipoeDate'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.ricezioneatti.action.ActRicercaAttiPerTipoeDate">
      <table cellspacing=2 cellpadding=2>
      	<tr>
        	<td class="Cliccabile" colspan="2">
          	Seleziona le caratteristiche dell'atto ricevuto
        	</td>
      	</tr>
        <tr>
          <td class="l">Tipo Atto</td>
          <td class="l">
        	  <select title="tipoAtto" name="<%=ICostantiRicezioneAtti.CAMPO_COD_TIPO_ATTO%>" >
      	      <%= tipoAtto %>
    	      </select>
  	      </td>
	      </tr>
        <tr>
          <td class="l">Stato Ricezione</td>
          <td class="l">
        	  <select title="statoRicezione" name="<%=ICostantiRicezioneAtti.CAMPO_STATO_RICEZIONE%>" >
      	      <%= statoRicezione %>
    	      </select>
  	      </td>
	      </tr>
      </table>
      <BR>
    	<table cellspacing=2 cellpadding=2>
      	<tr>
        	<td class="Cliccabile">
          	Seleziona il Periodo di arrivo delle richieste visualizzate
        	</td>
      	</tr>
      </table>
      <table cellspacing=2 cellpadding=2>
        <tr>
          <td class="label" >Visualizza le richieste pervenute dal </td>
          <td colspan ='2'>
            <input Title="Data iniziale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
            <input Title="Data iniziale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
            <input Title="Data iniziale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
          <td class="label"> al </td>
          <td colspan ='2'>
            <input Title="Data finale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
            <input Title="Data finale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
            <input Title="Data finale" type="text" name="<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>

      </table>

    	<br>

      <table cellspacing=2 cellpadding=2>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
          </td>
        </tr>
      </table>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    </form>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadRicercaAttiPerTipoeDate");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_INIZIO%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_INIZIO%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_INIZIO%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>","minlen=2","La lunghezza minima per il giorno è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_GIORNO_DATA_RICEZIONE_FINE%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>","maxlen=2","La lunghezza massima per il mese è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>","minlen=2","La lunghezza minima per il mese di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_MESE_DATA_RICEZIONE_FINE%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiRicezioneAtti.CAMPO_ANNO_DATA_RICEZIONE_FINE%>","lt=3000");
    </script>

  </body>
</html>