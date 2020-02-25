<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="tipoOperazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>


<html>
<head>
  <title>[S.I.E.S.] - Riscontro Trasmissioni - Ricerca Atti Trasmessi</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_inizio=document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
      var data_fine=document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadListaMessaggiTrasmessi.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;

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

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La Data di trasmissione finale non può essere inferiore alla data iniziale');
        return false;
      }
  return true;
    }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Atti Trasmessi: </font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadListaMessaggiTrasmessi'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.jms.action.ActListaMessaggiTrasmessi">
      <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo" colspan="6"> Selezione della Data di trasmissione</td>
	</tr>
        <tr>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label">Dalla data </td>
		<td class="label">
		  <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   -
		  <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   -
		  <input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>

		<td class="label">&nbsp; &nbsp; Alla data </td>
		<td class="label">
		  <input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   -
		  <input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   -
		  <input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		</td>
	      </tr>
	    </table>
	  </td>
        </tr>
      </table>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Se le date di Trasmissione non sono valorizzate, il sistema estrae gli atti relativi all'ultimo mese.
        </td>
      </tr>
    </table>

    <BR>
    <table cellspacing=2 cellpadding=2>
			<tr>
	  		<td class="Titolo" colspan="6"> Selezione della tipologia di atto</td>
			</tr>

			<tr>
	  		<td class="l">
            <table cellspacing=2 cellpadding=2>
					<tr>
						<td class="label">&nbsp;Tipo Atto </td>
						<td class="Label">
		  				<select title="tipoAtto" class=small name="<%=ICostantiSicoJMS.CAMPO_COD_TIPO_OPERAZIONE%>"  >
		    			<%= tipoOperazione %>
		  				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
	      	</tr>
	    	</td>
	  </table>
	</tr>
   </table>

      <BR>
      <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo">
	    Selezione del Tipo Esito
	  </td>
	  <%--td class="Titolo">
	    Selezione dell'Ufficio
	  </td--%>
	</tr>
	<tr>
	  <%-- Esito --%>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=0 CHECKED ></td>
		</td>
		<td class="label" >
		  Tutti
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=1></td>
		</td>
		<td class="label" >
		  In attesa di risposta&nbsp;
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=2></td>
		</td>
		<td class="label" >
		  Esito Positivo
		</td>
	      </tr>
	    </table>
	  </td>

	  <%-- Ufficio >
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>" value=0 ></td>
		</td>
		<td class="label" >
		  Tutti
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UFFICIO%>" value=1 CHECKED ></td>
		</td>
		<td class="label" >
		  Ufficio Collegato&nbsp;&nbsp;
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		</td>
		<td class="label" >
		</td>
	      </tr>
	    </table>
	  </td--%>

	</tr>
      </table>

      <BR>
      <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo">
	    Selezione dell'utente che ha effettuato la trasmissione
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
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    </form>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadListaMessaggiTrasmessi");

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