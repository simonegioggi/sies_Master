<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>


<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimenti con Provvedimenti emessi non depositati entro un determinato numero di giorni  - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  
  	function init() {
  		document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>.focus();
  	}
  	
    function Verify() {
    	
    	var data_emissione_inizio				;
    	var data_emissione_fine					;
    	var data_iscrizione_inizio				;
    	var data_iscrizione_fine				;
    	var usa_data_emissione_inizio		 	= false;
    	var usa_data_emissione_fine				= false;
    	var usa_date_emissione				 	= false;
    	var data_fine							;
    	var numero_giorni						;

    	data_emissione_inizio = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>.value + 
      				   			'/' + 
      				   			document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>.value + 
      				   			'/' + 
      				   			document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>.value;
		if (! ControllaData(data_emissione_inizio) && data_emissione_inizio.length > 2)
	    {
			alert('Data Inizio Emissione Provvedimento non valida');
			return false;
		}
		usa_data_emissione_inizio = data_emissione_inizio.length > 2;
	      
		data_emissione_fine = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>.value + 
							  '/' + 
							  document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>.value + 
							  '/' + 
							  document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>.value;
		if (! ControllaData(data_emissione_fine) && data_emissione_fine.length > 2)
		{
			alert('Data Fine Emissione Priovvedimento non valida');
			return false;
		}
		usa_data_emissione_fine = data_emissione_fine.length > 2;
	      
		data_iscrizione_inizio = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>.value + 
								 '/' + 
								 document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>.value + 
								 '/' + 
								 document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>.value;
		if (! ControllaData(data_iscrizione_inizio))
		{
			alert('Data Inizio Iscrizione non valida');
		    return false;
		}
		
		data_iscrizione_fine = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>.value + 
							   '/' + 
							   document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>.value + 
							   '/' + 
						 	   document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>.value;
		if (! ControllaData(data_iscrizione_fine))
		{
			alert('Data Fine Iscrizione non valida');
			return false;
		}
		
		if (usa_data_emissione_inizio != usa_data_emissione_fine) {
			alert('E\' necessario specificare sia la Data Inizio Emissione Provvedimento che la Data Fine Emissione Provvedimento');
			return false;
		}
	
		usa_date_emissione = usa_data_emissione_inizio && usa_data_emissione_fine;
		
		if (usa_date_emissione) {
			if ( DifferenzaDateInGiorni(data_emissione_inizio, data_emissione_fine) > ((365 * 3) + 1) ) {
				alert('L\'intervallo tra le Date Emissione Provvedimento non deve eccedere i 3 anni');
				return false;
			}
		}

		if ( DifferenzaDateInGiorni(data_iscrizione_inizio, data_iscrizione_fine) > ((365 * 3) + 1) ) {
			alert('L\'intervallo tra le Date Iscrizione non deve eccedere i 3 anni');
			return false;
		}

		data_fine = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value + 
				    '/' + 
				    document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value + 
				    '/' + 
			 	    document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>.value;
		if (!ControllaData(data_fine))
		{
			alert('Data Fine Data Udienza non è valida');
			return false;
		}
		
		numero_giorni = document.LoadRicercaProcProvvEmessiNoDepositoNumGG.<%=ICostantiStatistiche.CAMPO_NUMERO_GIORNI%>.value + '';
		if (numero_giorni.length == 0) {
			alert('Numero giorni non valido');
			return false;
		}
		
		return true;
    }
  </script>

</head>

<body class="corpo" onLoad="javascript:init()">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaProcProvvEmessiNoDepositoNumGG">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaProcProvvEmessiNoDepositoNumGG">
    <table>
      <tr>
      	<td class="LBG">
      		<a href="Javascript:window.print();">
      			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      		</a>
      	</td>
        <td class="LBG">
        	<font class="label">Funzione :</font> 
        	<font class="campo">Ricerca procedimenti pendenti con provvedimenti emessi non depositati entro un determinato numero di giorni </font>
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Visualizza i procedimenti con Data Emissione Provvedimento dal &nbsp;</td>
        <td class="label">
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
 								 onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Visualizza i procedimenti Iscritti dal (intervallo date max 3 anni) (*)&nbsp;</td>
        <td class="label">
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO %>" 
          			 maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE %>" 
          			 maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE %>" 
          			 maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE %>" 
          			 maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Numero giorni minimo intercorsi dalla Data Emissione (*)</td>
        <td class="label">
          <input Title="Numero giorni minimo intercorsi dalla Data Emissione (*)" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_NUMERO_GIORNI%>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          &nbsp;&nbsp; alla Data del (*) &nbsp;&nbsp;
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>" 
          			 maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" 
          			 onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>" 
          			 maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>" 
          			 maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
          			 onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >  
          N.B.: Vengono visualizzati i procedimenti pendenti alla Data indicata del, con provvedimento emesso non ancora depositato, per i quali il numero di giorni, trascorsi dalla data di emissione rispetto alla data indicata, è uguale o superiore a quello specificato nella form
        </td>
      </tr>
    </table>

      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>

      <tr>
        <td>
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  
          			 name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaProcProvvEmessiNoDepositoNumGG");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_EMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_EMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_EMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
</body>

</html>