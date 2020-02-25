<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="siap.sius.produzioneatti.action.ICostantiProduzioneAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="colMotivoParere"   scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>

<html>
	<head>
  	<title> [S.I.E.S.] - Ricerca Pareri - </title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    	function Verify()
    	{
    		var ritorno = true;
				var data_iniziale = document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
    		var data_finale = document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>.value+'/'+document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>.value+'/'+document.LoadRicercaPareri.<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>.value;
    		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

     		if (! ControllaData(data_iniziale))
      	{
        	alert('Data iniziale non valida!');
        	ritorno =  false;
      	}
     		else if (! ControllaData(data_finale))
      	{
        	alert('Data finale non valida!');
        	ritorno =  false;
      	}
      	// Controllo data finale >= Data iniziale .
      	else if( !CompareDate( data_iniziale, data_finale ) )
      	{
        	alert('Data finale < Data Iniziale!');
        	ritorno =  false;
      	}
      	return ritorno;
 			}
  	</script>
	</head>

	<body class="corpo" >
  	<FORM method="POST" name="LoadRicercaPareri" action="<%=IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.produzioneatti.action.ActRicercaPareri">

  	<table>
    	<tr>
				<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      	<td class="LBG"><font class="label">Funzione: </font>&nbsp;<font class="campo">Ricerca Richieste Parere</font></td>
    	</tr>
  	</table>

  	<br>
      <table width=80%>
        <tr>
					<td class="Titolo"  colspan ="4" >Intervallo Date di Emissione delle Richieste (*)</td>
        </tr>
        <tr>
          <td class="l" width="20%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="l" width="20%"  >
            <font class="label">Data Finale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>

			<br>

      <table width=80%>
      	<tr>
          <td class="l" width="30%">Tipo parere </td>
          <td class="l">
          	<select title="Tipo parere" name="<%=ICostantiProduzioneAtti.CAMPO_COD_MOTIVO%>">
            	<%=colMotivoParere%>
          	</select>
					</td>
        </tr>

				<tr>
        	<td class="l" >Contenuto degli atti di cui si è richiesto parere</td>
          <td class="l">
          	<select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" >
            	<%=contenuto%>
          	</select>
        	</td>
      	</tr>

				<tr>
        	<td class="l" >Seleziona solo pareri richiesti dall'utente</td>
          <td class="l">
						<input Title="Codice Utente" type="text" name="<%= ICostantiSicoJMS.CAMPO_COD_UTENTE %>" maxlength="11" size="8" >
          	</select>
        	</td>
      	</tr>
    	</table>

			<br>

      <!--
      	Gestione radio buttons per tipo di ordinamento :
        	-> A-P ( Ordinamento per Numero Procedimento ) default
         	-> C-N ( Ordinamento per Cognome/Nome )
       -->
			<table>
				<tr>
    			<td class="l">
      		Ordina Numero Procedimento &nbsp;
      		</td>
      		<td class="l">
          	<input type="radio" name="<%=ICostantiProduzioneAtti.CAMPO_TIPO_ORDINAMENTO%>" value="A-P" checked ></td>
      		</td>
				</tr>
				<tr>
    			<td class="l">
      			Ordina per Cognome Nome &nbsp;
      		</td>
      		<td class="l">
          	<input type="radio" name="<%=ICostantiProduzioneAtti.CAMPO_TIPO_ORDINAMENTO%>" value="C-N"></td>
      		</td>
      	</tr>
			</table>

			<table>
    		<tr>
      		<td class="label">
        		<input class="bottone" type="submit" name="RICERCA" value="Ricerca">
        	</td>
      	</tr>
    	</table>
	</form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaPareri");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>