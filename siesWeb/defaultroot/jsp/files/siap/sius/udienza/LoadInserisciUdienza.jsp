<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>

<jsp:useBean id="elencoProcuratori"		scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoAssistenti"    scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="udienza"             scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="elencoPresidenti"    scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoGiudici1"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoGiudici2"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoEsperti1"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoEsperti2"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="checkInsFissUdienza"  scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto riferimento al codice tipo ufficio --%>
<jsp:useBean id="codTipoUfficio" scope="request" class="java.lang.String"/>

<html>
	<head>
		<title>[S.I.E.S.] - GestioneUdienza </title>

		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">	

		<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
		<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  function  Verify()
  {
    var ritorno = true;
    dataU = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value+'/'+document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value+'/'+document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;
    if (dataU.length < 3 || ControllaData(dataU) == false)
    {
      alert('Data Udienza è un campo obbligatorio ');
      ritorno = false;
    }
    else
       if ((document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>.value != '-')&&
        ((document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_1%>.value)||
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_2%>.value) ||
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PG%>.value)) ||
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_1%>.value != '-')&&
        ((document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_1%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_2%>.value) ||
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_1%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PG%>.value)) ||
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_2%>.value != '-')&&
        ((document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_GIUDICE_2%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_PG%>.value)))
    {
      alert('Il collegio deve essere formato da magistrati tra loro differenti ');
      ritorno = false;
    }
    else
    if (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_ID_ESPERTO_1%>.value != 0 &&
        (document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_ID_ESPERTO_1%>.value == document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_COD_ID_ESPERTO_2%>.value))
    {
      alert('Gli esperti devono essere differenti tra loro');
      ritorno = false;
    }
	
		// Se non ci sono errori esegue il controllo orario inizio e fine    
    if( ritorno )
    	ritorno = controlloOrologio();
    
    return ritorno;
  }
  function DisableData()
  {
    //document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>.disabled=true;
    return;
  }
  
  function SelezionaCollegio( i)
  {
    if (i >= 0)
    if (i < document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>.options.length)
        document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>.options[i].selected = true;
    return;
  }

  function calendario(a_formname,a_field_year,a_field_month,a_field_day)
  {
    desktop = window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");

  }

  /* controllo correttezza di un campo di tipo ora */
  function controlloOra(ora, minuti)
  {
  	//alert ("controlloOra: " + ora);
    var ritorno = false;
    //alert ("ora ->" + ora);
    // L'ora è opzionale
    if( ora == "")
    	ritorno = true;
    else if (ora >= 0 && ora < 24)
      ritorno = controlloMinuti(minuti);
    else
      alert("L'ora deve essere espressi da un numero compreso tra 0 e 23");
    return ritorno;
    
   }

    /* controllo correttezza di un campo di tipo minuti */
    function controlloMinuti(min)
    {
       //alert ("controlloMinuti: " + min);
       var ritorno = false;
       //alert ("minuti ->" + min);
       if (min == "")
           ritorno = true;
       else if (min >= 0 && min < 60)
           ritorno = true;
       else
           alert("I minuti devono essere espressi da un numero compreso tra 0 e 59");
       return ritorno;
    }

   /* Controllo dei campi ore e minuti */
   function controlloOrologio()
   {
      var ret 			= true;
      var oraInizio = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_ORA_INIZIO%>.value;    
      var minInizio = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_INIZIO%>.value;
      var oraFine 	= document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_ORA_FINE%>.value;
      var minFine 	= document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE%>.value;
			var oraFineCC = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_ORA_FINE_CC%>.value;
			var minFineCC = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE_CC%>.value;
			
			// Controlla l'ora e minuti d'inizio
      ret = controlloOra(oraInizio, minInizio);
			
			// Controlla l'ora e minuti di fine, nel caso in cui
			// il conrollo precedente sia andato a buon fine.
			if( ret == true )
      	ret = controlloOra(oraFine,minFine);

			// Controlla l'ora e minuti di fine Camera Cosiglio, 
			// nel caso in cui il conrollo precedente sia andato a buon fine.
			if( ret == true )
      	ret = controlloOra(oraFineCC,minFineCC);

			// Se il campo OraInizio è valorizzato e il campo minInizio no
			// provvede a valorizzare quest'ultimo con 00
			if (oraInizio != "" && minInizio == "")
			{
      	document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_INIZIO%>.value = "00";
      	minInizio = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_INIZIO%>.value;
      }

			// Se il campo OraFine è valorizzato e il campo minFine no
			// provvede a valorizzare quest'ultimo con 00
      if (oraFine != "" && minFine == "")
      {
      	document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE%>.value = "00";
      	minFine = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE%>.value;
			}
			
			// Se il campo oraFineCC è valorizzato ed il campo minFineCC no
			// provvede a valorizzare quest'ultimo con 00
      if (oraFineCC != "" && minFineCC == "")
      {
      	document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE_CC%>.value = "00";
      	minFine = document.LoadInserisciUdienza.<%=ICostantiUdienza.CAMPO_MIN_FINE_CC%>.value;
			}
			
			// Controllo compilazione dei campi orario	
			var orarioInizio 	= oraInizio + minInizio;
			var orarioFine  	= oraFine + minFine; 
			var orarioFineCC  = oraFineCC + minFineCC; 
			
			if( orarioInizio.length != orarioFine.length )
			{
				alert("Non può esistere un orario inzio senza un orario fine e viceversa. ");
				ret = false;
			}
						
			// Esegue il controllo dell'orario d'inizio non sia >= del'orario di fine
			if( oraInizio != "" && minInizio 	!= "" && 
			    oraFine 	!= "" && minFine		!= "" && 
			    ret == true )
			{
				var orarioInizio = oraInizio + minInizio ;
				var orarioFine	 = oraFine + minFine ;
				
				if( orarioInizio >= orarioFine )
				{
					ret = false;
					alert("L'orario di fine deve essere maggiore dell'orario d'inizio.");
				}
			}				
			
			if( orarioFineCC.length != "" && orarioFineCC.length != orarioFine.length && ret == true )
			{
				alert("Non può esistere un orario fine Camera Consiglio senza un orario di fine e viceversa. ");
				ret = false;
			}
			
			// Esegue il controllo dell'orario di fine Camera di Consiglio 
			// sia >= dell'orario di fine.
			if( oraFine 	!= "" && minFine 		!= "" && 
			    oraFineCC != "" && minFineCC 	!= "" && 
			    ret == true )
			{
				var orarioFine 		= oraFine + minFine ;
				var orarioFineCC	= oraFineCC + minFineCC ;
				
				if( orarioFine > orarioFineCC )
				{
					ret = false;
					alert("L'orario di fine Camera Consiglio deve essere maggiore o uguale dell'orario di fine.");
				}
			}
								
      return ret;
   }
  </script>
</head>


<body class="corpo"  onload="Javascript:SelezionaCollegio( <%=StringUtils.toStringJSP(udienza.getNumCollegio() )%> - 1 );">
  <table>
  	<tr>
  		<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    	<td class="LBG">
    	<font class="label"> Funzione :</font>&nbsp;
<%
    UdienzaModel lUdienza= null;
    String lAzione = new String();

    if( modalita.equals("I") )
    {
      lUdienza = new UdienzaModel();
      lAzione = "siap.sius.udienza.action.ActInserisciUdienza";
%>
      <font class="campo">Inserimento di un'Udienza</font>
<%
    }
    else if( modalita.equals("M") )
    {
      lUdienza = new UdienzaModel(udienza);
      lAzione = "siap.sius.udienza.action.ActModificaUdienza";
%>
    <font class="campo">Modifica di un'Udienza</font>
<%
    }
    else if( modalita.equals("X") )
    {
      lUdienza = new UdienzaModel(udienza);
      lAzione = "siap.sius.udienza.action.ActInserisciUdienza";
%>
    <font class="campo">Inserimento con copia di un'Udienza</font>
<%
    }
%>
    </td>
    </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciUdienza">
    <table cellspacing=4 cellpadding=4>

    <tr>
      <td class="l">Data Udienza <font class=ob>(*)</font></td>
      <td class="l">
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"dd")) %>" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2" <%  if( modalita.equals("M") ) { %> disabled="disabled" <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"MM")) %>" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>" maxlength="2" size="2" <%  if( modalita.equals("M") ) { %> disabled="disabled" <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lUdienza.getDataUdienza(),"yyyy")) %>" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4" <%  if( modalita.equals("M") ) { %> disabled="disabled" <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
            <a href="javascript:calendario('LoadInserisciUdienza','<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>','<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>','<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>');">
             <img src="/images/calendario.gif" border=0>
            </a>
      </td>
    </tr>

    <tr>
      <td class="l">N.ro collegio</td>
      <td class="l">
        <select title="collegio" name="<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>" >
           <option value = "1" > 1
           <option value = "2" > 2
           <option value = "3" > 3
           <option value = "4" > 4
           <option value = "5" > 5
           <option value = "6" > 6
           <option value = "7" > 7
        </select>
      </td>
    </tr>


    <tr>
      <td class="l">Presidente</td>
      <td class="l">
        <select title="Presidente" name="<%=ICostantiUdienza.CAMPO_COD_PRESIDENTE%>"   >
          <%= elencoPresidenti %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Giudice relatore</td>
      <td class="l">
        <select title="Giudice1" name="<%=ICostantiUdienza.CAMPO_COD_GIUDICE_1%>"  >
          <%= elencoGiudici1 %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Giudice relatore</td>
      <td class="l">
        <select title="Giudice2" name="<%=ICostantiUdienza.CAMPO_COD_GIUDICE_2%>"  >
          <%= elencoGiudici2 %>
        </select>
      </td>
    </tr>

    <tr>
    	<%-- MEV10-s3: cambiata etichetta in funzione dell'utenza collegata --%>
    	<% if ("TDSM".equals(codTipoUfficio)) { %>
      		<td class="l">Procuratore della Repubblica presso il Tribunale dei Minorenni</td>
      	<% } else { %>
      		<td class="l">Procuratore Gen.</td>
      	<% } %>
      	<td class="l">
        	<select title="ProcuratoreG" name="<%=ICostantiUdienza.CAMPO_COD_PG%>"  >
          		<%= elencoProcuratori %>
        	</select>
      	</td>
    </tr>

    <tr>
      <td class="l">Esperto 1</td>
      <td class="l">
        <select title="Esperto1" name="<%=ICostantiUdienza.CAMPO_COD_ID_ESPERTO_1%>"   >
          <%= elencoEsperti1 %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Esperto 2</td>
      <td class="l">
        <select title="Esperto2" name="<%=ICostantiUdienza.CAMPO_COD_ID_ESPERTO_2%>"   >
          <%= elencoEsperti2 %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Assistente</td>
      <td class="l">
        <select title="Assistente" name="<%=ICostantiUdienza.CAMPO_COD_ID_ASSISTENTE%>"   >
          <%= elencoAssistenti %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Num. Max. Fascicoli</td>
      <td class="l"><input value="<%=StringUtils.toStringJSP( lUdienza.getNumeroMaxFascicoli(), "" )%>" type="text" name="<%= ICostantiUdienza.CAMPO_NUMERO_MAX_FASCICOLI %>"  ></td>
    </tr>
    
    <tr>
      <td class="l">Luogo</td>
      <td class="l"><input value="<%=StringUtils.toStringJSP( lUdienza.getLuogoUdienza(), "" )%>" type="text" name="<%= ICostantiUdienza.CAMPO_LUOGO_UDIENZA %>"  size="75" maxlength="99"></td>
    </tr>
    
    <tr>
      <td class="l">Orario Inizio (ora:min)</td>
      <td class="l">
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getOraInizio(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_ORA_INIZIO%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      	:
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getMinInizio(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_MIN_INIZIO%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      </td>
    </tr>
    
    <tr>
      <td class="l">Orario Fine (ora:min)</td>
      <td class="l">
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getOraFine(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_ORA_FINE%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      	:
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getMinFine(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_MIN_FINE%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      </td>
    </tr>
    
    <tr>
      <td class="l">Orario Fine Camera Consiglio (ora:min)</td>
      <td class="l">
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getOraFineCC(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_ORA_FINE_CC%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      	:
      	<input value="<%=StringUtils.toStringJSP( lUdienza.getMinFineCC(), "" )%>" type="text" name="<%=ICostantiUdienza.CAMPO_MIN_FINE_CC%>" size="2" maxlength="2" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      </td>
    </tr>
  	
  	<tr>
    	<td>
      	<input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
    	</td>
  	</tr>

  </table>

  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  	<input type="HIDDEN" value="<%=StringUtils.toStringJSP( lUdienza.getIdUdienza(), "" )%>" name="<%= ICostantiUdienza.CAMPO_ID_UDIENZA %>"  >
    <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_CHECK_INS_FISS_UDIENZA%>" value="<%=checkInsFissUdienza%>" >
  </form>
    <script language="JavaScript" type="text/javascript">
    	var frmvalidator  = new Validator("LoadInserisciUdienza");
    	frmvalidator.addValidation("<%= ICostantiUdienza.CAMPO_NUMERO_MAX_FASCICOLI %>","numeric","numero non valido in : Num. Max Fascicoli");
    	frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>