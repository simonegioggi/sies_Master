<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>

<html>
	<head>
	  <title> [S.I.E.S.] - Ricerca Procedimenti Per Provvedimenti non validati/depositati - </title>
	  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	  <script language="JavaScript" src="/html/ControllaData.js"></script>
	  <script language="JavaScript">
	   	function init(){
	      //alert("init ");
	      document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_ANNO_INI%>.focus();
	   	}
    	function Verify() {																																																	
      	var data_to_verify=
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>.value+'/'+ 
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>.value+'/'+
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>.value;

    	  if (! ControllaData(data_to_verify) && data_to_verify.length>2){
        	alert('Data di Deposito iniziale non valida!');
        	return false;
      	}
      	var data_to_verify=
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>.value+'/'+ 
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>.value+'/'+
    	  	document.LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>.value;
      
    		if (! ControllaData(data_to_verify) && data_to_verify.length>2){
        	alert('Data di Deposito finale non valida!');
        	return false;
      	}

      return true;
    }
	  </script>
	
	</head>

	<body class="corpo" onLoad="javascript:init()">
  	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati">
    	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaProcPerProvvNoValidatiNoDeposito">
    	<table>
      	<tr>
      		<td class="LBG">
      			<a href="Javascript:window.print();">
      				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      			</a>
      		</td>
        	<td class="LBG">
        		<font class="label">Funzione :</font> 
        		<font class="campo">Ricerca Procedimenti Per Provvedimenti non validati/depositati</font>
        	</td>
      	</tr>
    	</table>

    	<br>

    	<table width="100%">
      	<tr>
      		<td class="Titolo" >Indicare</td></tr>
          	<td class="c" width="61%" >
	            Stato Procedimento :  
	            Privi di Provvedimento <input type="radio" 
	            										 	   name="<%=ICostantiStatistiche.RADIO_RICERCA_PER_STATO_PROVVEDIMENTO%>" 
	            										 	   value="<%=ICostantiStatistiche.VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_NO%>" 
	            										 	   checked/>
	            &nbsp;&nbsp;&nbsp;&nbsp; 
							provvedimenti emessi non validati <input type="radio" 
															   											 name="<%=ICostantiStatistiche.RADIO_RICERCA_PER_STATO_PROVVEDIMENTO%>" 
															   											 value="<%=ICostantiStatistiche.VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_EMESSI_NOVAL%>" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							provvedimenti non depositati 	<input type="radio" 
															   						 			 name="<%=ICostantiStatistiche.RADIO_RICERCA_PER_STATO_PROVVEDIMENTO%>" 
															   						 			 value="<%=ICostantiStatistiche.VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_NODEP%>" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							provvedimenti depositati non validati	<input type="radio" 
															   										 			 name="<%=ICostantiStatistiche.RADIO_RICERCA_PER_STATO_PROVVEDIMENTO%>" 
															   										 			 value="<%=ICostantiStatistiche.VALUE_RICERCA_PER_STATO_PROVVEDIMENTO_DEP_NOVAL%>" />
					</td>
      	</tr>
    	</table>
  	
  	<br>
  	
    <table width="100%">
      <tr>
      	<td class="Titolo" >Intervallo Estremi Procedimenti</td></tr>
          <td class="c" width="61%" >     
          		Anno/Numero Iniziale 
          		<input Title="Anno Iniziale" 
          					 type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI%>" maxlength="4" size="4" 
          			 		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			        <input Title="Numero Iniziale" 
			         			 type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_INI%>" maxlength="6" size="6" 
			         			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          		&nbsp;&nbsp; Anno/Numero Finale &nbsp;&nbsp;
          		<input Title="Anno Finale" 
          					 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_FINE %>" maxlength="4" size="4" 
          			 		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
			        <input Title="Numero Finale" 
			         			 type="text" name="<%= ICostantiStatistiche.CAMPO_NUM_FINE %>" maxlength="6" size="6" 
			         			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
					</td>
      </tr>
    </table>
    
    <br>
    
    <table width="100%">
    	<tr>
      	<td class="Titolo" >Intervallo Date Iscrizione</td></tr>
          <td class="c" width="61%" >
          		Data Iscrizione Iniziale      
          		<input Title="dalla Data" 
          					 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>" maxlength="2" size="2" 
          			 		 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
			        <input Title="dalla Data" 
			         			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI %>" maxlength="2" size="2" 
			         			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
		          <input Title="dalla Data" 
		          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI %>" maxlength="4" size="4" 
		          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          		&nbsp;&nbsp; Data Iscrizione Finale  &nbsp;&nbsp;
		          <input Title="alla Data" 
		          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" 
		          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
		          <input Title="alla Data" 
		          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" 
		          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
		          <input Title="alla Data" 
		          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE %>" maxlength="4" size="4" 
		          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
         	</td>
       	</td>
      </tr>
    	<tr><td>&nbsp;</td></tr>
		</td>
	</tr>
</table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
<!-- 
N.B.: Di norma vengono visualizzati i procedimenti pendenti e privi di udienza di competenza dell'ufficio con i criteri di ricerca selezionati. Per variare i criteri selezionare una o più delle seguenti opzioni:
 -->          
 		N.B.: La ricerca non include i procedimenti, privi di provvedimenti, definiti manualmente
        </td>
      </tr>
    </table>

      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>

    <table cellspacing=2 cellpadding=2>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td>
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaProcedimentiPerProvvNoValidatiNoDepositati");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");
  </script>
</body>

</html>