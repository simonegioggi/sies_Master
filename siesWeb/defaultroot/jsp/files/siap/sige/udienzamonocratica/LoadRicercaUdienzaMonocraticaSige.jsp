<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="elencoGiudici" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> Ricerca Udienza Monocratica </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
		// init per focus sul primo campo
		function init()
		{
	  	document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].focus();
		}

		// Verifica della data Udienza.
		function checkDallaDataUdienza()
		{
			var ritorno = true;
  		var dataUdienza = 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].value +'/'+ 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>[0].value +'/'+ 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>[0].value;

  		if (dataUdienza.length > 2)
  		{
  			if (!ControllaData(dataUdienza))
    		{
    			alert('Data udienza non corretta.');
    			document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].focus();
      		ritorno = false;
    		}
  		}
			return ritorno;
		}

		// Verifica della data Udienza.
		function checkAllaDataUdienza()
		{
			var ritorno = true;
  		var dataUdienza = 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[1].value +'/'+ 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>[1].value +'/'+ 
  				document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>[1].value;

  		if (dataUdienza.length > 2)
  		{
  			if (!ControllaData(dataUdienza))
    		{
    			alert('Data udienza non corretta.');
    			document.LoadRicercaUdienzaMonocraticaSige.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[1].focus();
      		ritorno = false;
    		}
  		}
			return ritorno;
		}
		
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    
		// Esecuzione delle funzioni di verifica.
   	function Verify()
    {  
    	// Controllo della data Udienza.  
    	if( !checkDallaDataUdienza() )
      	return false;

    	// Controllo della data Udienza.  
    	if( !checkAllaDataUdienza() )
      	return false;
    	
      return true;
    }
  </script>
</head>

<body class="corpo" onload="Javascript:init();">
  <table>
    <tr>
      <td class="LBG">
				<a href="Javascript:window.print();">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" 
							 alt="Stampa questa videata" border=0>
				</a>
			</td>
      <td class="LBG">
				<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Ricerca Udienza Monocratica</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaUdienzaMonocraticaSige">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 
				 value="siap.sige.udienzamonocratica.action.ActRicercaUdienzaMonocraticaSige">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Dalla data Udienza</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"   
               name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" 
							 onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4"
               name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
							 onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Alla data Udienza</td>
      <td class="l"> 
        <input type="text" size="2" maxlength="2"  
               name="<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)"
							 onkeypress="return TicTabNumField(this,event)"
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="2" maxlength="2"
               name="<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)"
							 onkeypress="return TicTabNumField(this,event)"
							 onBlur="javascript:value=FillDM(value)">&nbsp;/&nbsp;
        <input type="text" size="4" maxlength="4"
               name="<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>" 
               onFocus="javascript:textboxSelect(this)"
							 onkeypress="return TicTabNumField(this,event)"
							 onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Giudice Udienza</td>
      <td class="l">
        <select title="giudice" name="<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>" >
        	<%=elencoGiudici%>
        </select>
      </td>
    </tr>
    <tr>
      <td>
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaUdienzaMonocraticaSige");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>