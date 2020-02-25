<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="f3b.util.DateUtils"%>
  
 <jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>
 <jsp:useBean id="tipoRicorso" scope="request" class="java.lang.String"/>
  <jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>
 <jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
 <jsp:useBean id="esperto" scope="request" class="java.lang.String"/>
 
 <% 
 // Il default è ricerca Ordinanze prive di Foglio Complementare
 String testo1 = "Estremi Ordinanza";
 String testoDate = "Date Deposito";
 String valoreDate = ICostantiStatistiche.DATA_DEPOSITO_INTERVALLO;
 String testo2 = "Tutte";
 String testo3 = "Validate";
 String testo4 = "Non Validate";
 String testo5 = "Annullate";
  %>
  
  <script language="JavaScript" src="<%=ICostantiStatistiche.RICERCA_ORDINANZA_JS%>"></script>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaOrdinanzePriveDiFC" >
    <table  width=96%>
      <tr> <td class="Titolo" colspan=4>Indicare</td></tr>
    </table>
   
    <table width=96% >
      <tr>
      	<td class="c" width="20%" >Intervallo: </td>
      	<td class="c" width="60%" >
            <%=testo1%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_INTERVALLO%>" value="<%=ICostantiStatistiche.ESTREMI_PROVVEDIMENTO_INTERVALLO %>"  onClick="VisualizzaEstremiOrdinanza();"  checked >&nbsp;
			<%=testoDate%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_INTERVALLO%>" value="<%=valoreDate%>" onClick="VisualizzaDateDeposito();" ></td>
      </tr>
      <tr>
      	<td class="c" width="20%" >Stato Validazione: </td>
       	<td class="c" width="60%" >
            <%=testo2%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.TUTTI%>" checked> 
			<%=testo3%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.VALIDATI %>" >&nbsp;
 			<%=testo4%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.NON_VALIDATI %>" >&nbsp;
 			<%=testo5%> <input type="radio" name="<%=ICostantiStatistiche.CAMPO_STATO_VALIDAZIONE%>" value="<%=ICostantiStatistiche.ANNULLATI %>" >
 		</td>
     </tr>
    </table>

    <div id="contenitore" style="position: relative; top: 0; left: 0;  " >         
    <div id="EstremiOrdinanza" style="position:relative;  top: 0; left: 0; " >  
      <table  width=95%>
       <tr> <td class="Titolo" colspan=4>Intervallo <%=testo1%></td></tr>
      <tr>
        <td class="c" >
          <font class="label">Anno/Numero Iniziale (*) </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Ordinanza Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Ordinanza Iniziale" name="<%=ICostantiStatistiche.CAMPO_NUM_INI %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="c" >
          <font class="label">Anno/Numero Finale (*) </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Ordinanza Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Ordinanza Finale" name="<%=ICostantiStatistiche.CAMPO_NUM_FINE %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        </tr>      
         </table>
   </div>     
      <div id="DateDeposito" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
       <table width=96%>
        <tr>
		<td class="Titolo"  colspan ="4" >Intervallo <%=testoDate%> </td>
        </tr>
        <tr>
          <td class="c" width="10%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="c" width="10%"  >
            <font class="label">Data Finale </font>
          </td>
          <td class="l" width="30%" >
            <input type="text" title="Giorno Finale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE %>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>

      </div> 
   </div>
        <br>
        <table>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="Validazione();">
         </td>
        </tr>
    </table>
    <br>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaOrdinanzePriveDiFC">
    <input type="HIDDEN" name="<%=ICostantiStatistiche.CAMPO_MODALITA_RICERCA%>" value="<%=modalitaRicerca%>">
  </FORM>
 
 
   <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("RicercaOrdinanzePriveDiFC");

	// Viene usata questa funzione per definire i campi da controllare che cambiano dalle opzioni scelte.
     function Validazione()
     {
     	
      	if (isEstremiOrdinanza())
     	{
     		frmvalidator.clearAllValidations();
     		// alert ("isEstremiOrdinanza");
     		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI %>","req","Il campo Anno Inizio è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INI%>","minlen=4","La lunghezza del campo Anno Inizio deve essere di 4 caratteri");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_INI %>","req","Il campo Numero Inizio  è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_INI%>","numeric");
    
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE %>","req","Il campo Anno Fine è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>","minlen=4","La lunghezza del campo Anno Fine deve essere di 4 caratteri");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_FINE %>","req","Il campo Numero Fine  è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_NUM_FINE%>","numeric");  	 	
       	
      		frmvalidator.setAddnlValidationFunction("VerificaBase");    	
     	}
     	else
     	{
     		frmvalidator.clearAllValidations();
     	    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>","req","Il campo Giorno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","req","Il campo Giorno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>","minlen=2","La lunghezza del campo giorno deve essere di 2 caratteri");

    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","req","Il campo Mese è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","req","Il campo Mese è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>","minlen=2","La lunghezza del campo mese deve essere di 2 caratteri");

    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","req","Il campo Anno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","req","Il campo Anno è obbligatorio");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","numeric");
    		frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");
      	
      		frmvalidator.setAddnlValidationFunction("VerificaDateDeposito");
      	}      	
     	
     }
  </script>
   <script language="JavaScript" type="text/javascript">
     function VerificaDateDeposito()
  {
    var ritorno = true;
	var data_iniziale = document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>.value + '/' + document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>.value + '/'+ document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>.value;
			
	var data_finale = document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>.value + '/' + document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>.value + '/'+ document.RicercaOrdinanzePriveDiFC.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>.value;
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
     else if( !CompareDate( data_iniziale, data_sistema ) )
     {
        alert('Data iniziale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
     else if( !CompareDate( data_finale, data_sistema ) )
     {
     	alert('Data finale non può essere superiore alla data di sistema!');
        ritorno =  false;
     }
          		
     return ritorno;
 	}
 
  
   </script>
   
   
   