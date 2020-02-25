<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Ricerche Altre BDI - Lista Esiti Ricerca Soggetto su altre BDI </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_inizio=document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
      var data_fine=document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaEsitiRicercaSoggAltreBDI.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data iniziale non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data finale non valida');
        return false;
      }

      if(!(data_inizio.length==2) )
      {
      	// Controllo data inizio non precedente di una settimana.
        var data_minima='<%= DateUtils.getDateToString( (DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getDayBefore(DateUtils.getSysDate())))))))),"dd/MM/yyyy")%>'
      	if(!CompareDate(data_minima,data_inizio))
	      {
	        alert('Le ricerche effettuate 7 giorni addietro sono scadute');
	        return false;
	      }
      }


      if(!(data_inizio.length==2 || data_fine.length==2) )
      {
      	if(!CompareDate(data_inizio,data_fine))
	      {
	        alert('La Data di ricerca finale non può essere inferiore alla data iniziale');
	        return false;
	      }
      }
      return true;
    }

  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Esiti Ricerca Soggetto su altre BDI </font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadListaEsitiRicercaSoggAltreBDI'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.jms.action.ActListaEsitiRicercaSoggAltreBDI">

  <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo" colspan="6"> Selezione della Data di ricerca </td>
	</tr>
        <tr>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label">Dalla data &nbsp;</td>
		<td class="label">
		  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   -
		  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   -
		  <input Title="Data di ricerca inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>

		<td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Alla data </td>
		<td class="label">
		  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   -
		  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   -
		  <input Title="Data di ricerca fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>
	      </tr>
	    </table>
	  </td>
        </tr>
  </table>

  <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo">
	    Selezione dell'utente che ha effettuato la ricerca
	  </td>
	</tr>
	<tr>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=0 ></td>
		</td>
		<td class="label" >
		  Tutti
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=1 CHECKED ></td>
		</td>
		<td class="label" >
		  Utente Collegato
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=2></td>
		</td>
		<td class="label" >
		  Utente con codice :&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td><input Title="Codice Utente" type="text" name="<%= ICostantiSicoJMS.CAMPO_COD_UTENTE %>" maxlength="11" size="8" ></td>
	      </tr>
	    </table>
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

      var frmvalidator  = new Validator("LoadListaEsitiRicercaSoggAltreBDI");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","lt=3000");
    </script>

  </body>
</html>