<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.sezione.action.ICostantiSezione"%>

<jsp:useBean id="elencoMagistrati" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>


<html>
<head>
  <title> [S.I.E.S.] Ricerca Udienza Collegiale </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
		// init per focus sul primo campo
		function init()
		{
	  	document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].focus();
		}

    function ListaCollegi(a_formname)
    {
      var desktop;
      desktop = 
          window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.collegio.action.ActLoadRicercaCollegioLista&formname="+a_formname, "Ricerca_Collegio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
    }
			
		// Verifica della data Udienza.
		function checkDallaDataUdienza()
		{
			var ritorno = true;
  		var dataUdienza = 
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].value +'/'+ 
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>[0].value +'/'+ 
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>[0].value;

  		if (dataUdienza.length > 2)
  		{
  			if (!ControllaData(dataUdienza))
    		{
    			alert('Data udienza non corretta.');
    			document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[0].focus();
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
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[1].value +'/'+ 
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA%>[1].value +'/'+ 
  				document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA%>[1].value;

  		if (dataUdienza.length > 2)
  		{
  			if (!ControllaData(dataUdienza))
    		{
    			alert('Data udienza non corretta.');
    			document.LoadRicercaUdienzaCollegiale.<%=ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA%>[1].focus();
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
        <font class="campo">Ricerca Udienza Collegiale</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaUdienzaCollegiale">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 
				 value="siap.sige.udienzacollegiale.action.ActRicercaUdienzaCollegiale">
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

    <!-- elimino il filtro per collegio per richiesta della 11.2.1 -->
<%--     <tr>
      <td class="l">Collegio</td>
			<td class="l">
        <input type="text" maxlength="10" size="10" 
               name="<%=ICostantiCollegio.CAMPO_COD_COLLEGIO%>"  
               readonly>

        <input type="hidden" maxlength="10" size="10" 
               name="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>"  
               readonly>  
      	 <a href="Javascript:ListaCollegi('LoadRicercaUdienzaCollegiale');">
        	Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
    </tr> --%>

   <!-- aggiungo il filtro per magistrato per richiesta della 11.2.1 -->   
    <tr>
      <td class="l">Magistrato Presidente</td>
      <td class="l">
        <select title="magistrato" name="<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>">
        	<%=elencoMagistrati%>
        </select>
      </td>
   </tr>
<!-- il filtro sezione deve diventare una combo per richiesta della 11.2.1 -->   
<% if( elencoSezioni.length() != 0 ) { %>
				<tr>
      <td class="l">Sezione</td>
			<td class="l">
        		<select title="Sezione" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>">
        			<%=elencoSezioni%>
        		</select>
      </td>
<% } %>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--  
    <tr>
      <td class="l">Magistrato</td>
      <td class="l">
        <select title="magistrato" name="<%=ICostantiUdienzaSige.CAMPO_COD_GIUDICE%>">
        	<%=elencoMagistrati%>
        </select>
      </td>
    </tr>
--%>
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
  var frmvalidator  = new Validator("LoadRicercaUdienzaCollegiale");

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