<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel"%>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza"%>

<jsp:useBean id="TipiFontiReato" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiNazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="BilanciamentoCircostanze" 	scope="request" class="java.lang.String"/>

<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Ricerca Soggetto - </title>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript">
function Verify() 
{
	
	var DataInizio=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value;
	var DataFine=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value;

	var Nazio=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_NAZIONE%>[document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_NAZIONE%>.selectedIndex].value; 
	
	var fontedesc = document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.selectedIndex].text;
	var sottonumerazdesc=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>[document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>.selectedIndex].text;
	var commaqualdesc=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>[document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>.selectedIndex].text;

	
	var fonteCircodesc = document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.selectedIndex].text;
	var sottonumerazCircodesc=document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE%>[document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE%>.selectedIndex].text;
	var commaqualCircodesc=document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>[document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>.selectedIndex].text;


	var Naziodesc=document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_NAZIONE%>[document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_COD_NAZIONE%>.selectedIndex].text;  
	
	document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_DESC_FONTE%>.value = fontedesc;
	document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_DESC_SOTTONUMERAZIONE%>.value = sottonumerazdesc;
	document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_DESC_COMMA_QUAL%>.value = commaqualdesc;
	
	document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_DESC_FONTE%>.value = fonteCircodesc;
	document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_DESC_SOTTONUMERAZIONE%>.value = sottonumerazCircodesc;
	document.LoadElencoProcReato.<%=ICostantiCircostanza.CAMPO_DESC_COMMA_QUAL%>.value = commaqualCircodesc;
	
	document.LoadElencoProcReato.<%=ICostantiReato.CAMPO_DESC_NAZIONE%>.value = Naziodesc;
	
	//if(Fonte=="-" && AnnoFonte=="" && NumeroFonte=="" && Articolo=="" && Sottonumerazione=="-" && Comma=="" && Lettera=="" && Numero=="")  {
	//	alert("Inserire almeno un campo di Ricerca");
	//	return false;
	//}

	//if(Fonte=="-")  {
	//	alert("Fonte obbligatoria");
	//	return false;
	//}

    if(!ControllaDataPassaVuota(DataInizio)){
      alert('Data Reato dal non valida');
      return false;
    }
    if(!ControllaDataPassaVuota(DataFine)){
      alert('Data Reato al non valida');
      return false;
    }
    if (DataFine.length>2 && DataInizio.length<=2){
        alert('Data Reato dal non valorizzata');
        return false;
    }

}
</script>


 </head>

<body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadElencoProcReato">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActElencoProcReato">

	<input type="hidden" name="<%=ICostantiReato.CAMPO_DESC_FONTE %>" value="">
	<input type="hidden" name="<%=ICostantiReato.CAMPO_DESC_SOTTONUMERAZIONE %>" value="">
	<input type="hidden" name="<%=ICostantiReato.CAMPO_DESC_COMMA_QUAL %>" value="">
	<input type="hidden" name="<%=ICostantiReato.CAMPO_DESC_NAZIONE %>" value="">
	<input type="hidden" name="<%=ICostantiCircostanza.CAMPO_DESC_FONTE %>" value="">
	<input type="hidden" name="<%=ICostantiCircostanza.CAMPO_DESC_SOTTONUMERAZIONE %>" value="">
	<input type="hidden" name="<%=ICostantiCircostanza.CAMPO_DESC_COMMA_QUAL %>" value="">
	
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Procedimento per Reato</font></td>
      </tr>
    </table>

    <table cellspacing=2 cellpadding=2 width = "90%">
      <tr><td class="Titolonocap" colspan="2">Ricerca valida solo nell'Ufficio</td></tr>
      <tr><td><br></td></tr>
    </table>
    
    <tr>
	  <td class="L" style="text-align:left" >
		<font style="color:green; font-size: 12pt;" class="label"> REATI</font>
	  </td>
	</tr> 

<!-- 		REATI		 --> 
    <table cellspacing="1" cellpadding="2">
	    <tr>
	      <td class="int">Fonte</td>
	      <td class="int">Anno</td>
	      <td class="int">Numero</td>
	      <td class="int">Articolo</td>
	      <td class="int">Articolo qualificante</td>
	      <td class="int">Comma</td>
	      <td class="int">Comma qualificante</td>
	      <td class="int">Lettera</td>
	      <td class="int">Numero</td>
	    </tr>
    	
   		<tr>
    		<td class="l"><!--  Fonte Reato -->
      			<select name="<%= ICostantiReato.CAMPO_COD_FONTE %>"><%=TipiFontiReato%></select>
     		</td>
     		<td class="l"><!--  Anno Fonte Reato -->
        		<input size=4 maxlength=4 title="Anno Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_ANNO_FONTE %>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        	</td>
			<td class="l"><!--  Numero Fonte Reato -->
      			<input size="6" maxlength="6" title="Numero Fonte" value=""	type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    		</td>
		    <td class="l"><!--  Articolo Reato -->
		      <input size="5" maxlength="5"  title="Articolo Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>"
		        onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Articolo BTQual Reato -->
		      <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
		        <%=TipiSottonumerazione %>
		      </select>
		    </td>
		    <td class="l"><!--  Comma Reato -->
		      <input size="2" title="Comma" maxlength="2" value="" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>" 
			    onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Comma BTQual Reato -->
		      <select name="<%= ICostantiReato.CAMPO_COMMA_QUALIFICANTE %>">
		        <%=TipiSottonumerazione %>
		      </select>
		    </td>		     
		    <td class="l"><!--  Lettera Reato -->
		      <input size="2" title="Lettera" maxlength="2" value="" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>" 
		        onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Numero Reato -->
		      <input size="2" title="Numero" maxlength="2" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>"
		        onFocus="javascript:textboxSelect(this)">
		    </td>
    	</tr>
    	<tr><td><br></td></tr> 
	</table> 

	<table cellspacing="1" cellpadding="2" width="60%">
      <tr>
        <td class="l">Data Reato dal</td>
        <td class="L">
        	<input type="text" title="Giorno Reato dal" name="<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Mese Reato dal" name="<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Anno Reato dal" name="<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Reato al</td>
        <td class="L">
        	<input type="text" title="Giorno Reato al" name="<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Mese Reato al" name="<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Anno Reato al" name="<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>   
 <br>
    <tr>
	  <td class="L" style="text-align:left" >
		<font style="color:green; font-size: 12pt;" class="label"> CIRCOSTANZE</font>
	  </td>
	</tr> 

<!-- 		CIRCOSTANZA		 --> 
    <table cellspacing="1" cellpadding="2">
	    <tr>
	      <td class="int">Fonte</td>
	      <td class="int">Anno</td>
	      <td class="int">Numero</td>
	      <td class="int">Articolo</td>
	      <td class="int">Articolo qualificante</td>
	      <td class="int">Comma</td>
	      <td class="int">Comma qualificante</td>
	      <td class="int">Lettera</td>
	      <td class="int">Numero</td>
	    </tr>

	    <tr>
    		<td class="l"><!--  Fonte Circo -->
      			<select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>"><%=TipiFontiReato%></select>
     		</td>
     		<td class="l"><!--  Anno Fonte Circo -->
        		<input size=4 maxlength=4 title="Anno Fonte" value="" type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        	</td>
			<td class="l"><!--  Numero Fonte Circo -->
      			<input size="6" maxlength="6" title="Numero Fonte" value=""	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
    		</td>
		    <td class="l"><!--  Articolo Circo -->
		      <input size="5" maxlength="5"  title="Articolo Fonte" value="" type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>"
		        onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Articolo BTQual Circo -->
		      <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
		        <%=TipiSottonumerazione %>
		      </select>
		    </td>
		    <td class="l"><!--  Comma Circo -->
		      <input size="2" title="Comma" maxlength="2" value="" type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>" 
			    onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Comma BTQual Circo -->
		      <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
		        <%=TipiSottonumerazione %>
		      </select>
		    </td>			     
		    <td class="l"><!--  Lettera Circo -->
		      <input size="2" title="Lettera" maxlength="2" value="" type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>" 
		        onFocus="javascript:textboxSelect(this)">
		    </td>
		    <td class="l"><!--  Numero Circo -->
		      <input size="2" title="Numero" maxlength="2" value="" type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
		        onFocus="javascript:textboxSelect(this)">
		    </td>
    	</tr>
    	<tr><td><br></td></tr> 
	</table>  
      
	<table cellspacing="1" cellpadding="2" width="60%">      
      <tr>
      	 <td class="l">Bilanciamento circostanze</td>
		 <td class="l">
			<select name="<%=ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE%>" onchange="flagCampiComuni()"><%=BilanciamentoCircostanze%>
			</select>
		 </td>
	  </tr>
	  <tr><td><br></td></tr>
	</table>	 		
     
      
    <table cellspacing="1" cellpadding="2" width="60%">
      <tr>
        <td class="l" width="10%">Nazionalità</td>
        <td class="L" width="50%">
        <select name="<%= ICostantiReato.CAMPO_COD_NAZIONE%>"><%=TipiNazione%> </select>
        </td>
      </tr>
    </table>
<br>      
    <table>
      <tr>
      	<td class="Titolo" colspan="2" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 		Tipologia Procedimenti : 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      </tr>
       		<tr>
      			<td class="label">
      			<input type="radio" name="<%=ICostantiReato.CAMPO_CERCA_DEFINITI %>" value="DEFINITI" > Definiti
      			</td>

      			<td class="label">
      			&nbsp;&nbsp;<input type="radio" name="<%= ICostantiReato.CAMPO_CERCA_DEFINITI %>" value="PENDENTI" CHECKED> In Corso
      			</td>
 
      			<td class="label">
      			&nbsp;&nbsp;<input type="radio" name="<%= ICostantiReato.CAMPO_CERCA_DEFINITI %>" value="TUTTI"> Tutti
      			</td>
      		</tr>
	</table>
			<tr><td> </td></tr>
      	<table>
      		<tr>
      			<td class="label">
      			<input type="checkbox" name="<%=ICostantiReato.CAMPO_CERCA_CUMULATI %>" value="CUMULATI" > Solo con Procedimenti di Cumulo
      			</td>
      		</tr>
		</table>
			
      <tr>
        <td colspan="2">
        <br><br>
          <INPUT onclick="javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

  </form>
<script language="JavaScript" type="text/javascript">
var frmvalidator  = new Validator("LoadElencoProcReato");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_FONTE%>","numeric");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","numeric","Il campo Data Reato dal può avere solo caratteri numerici");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","numeric","Il campo Data Reato dal può avere solo caratteri numerici");
frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","gt=1");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","lt=12");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","numeric","Il campo Data Reato dal può avere solo caratteri numerici");
frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","numeric","Il campo Data Reato al può avere solo caratteri numerici");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","lt=31");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>","numeric","Il campo Data Reato al può avere solo caratteri numerici");
frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>","gt=1");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>","lt=12");

frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>","numeric","Il campo Data Reato al può avere solo caratteri numerici");
frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>","gt=1900");
frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>","lt=3000");

</script>
</body>

</html>