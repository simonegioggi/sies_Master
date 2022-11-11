<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="reato" 				scope="request" class="siap.siep.reato.model.ReatoModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiCommaQualificante" scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="IdReato" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Reato </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" > </script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>" > </script>

	<script language="JavaScript">

	//****************************************************************************************************
	//Federica - a9-rr-078
	//aggiunta funzione di controllo dei campi 
	function ControlloObbligatorieta()
	{
		// controllo obbligatorietà articolo e fonte
		if (document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.value=="-"
		 || document.LoadModificaReato.<%=ICostantiReato.CAMPO_ARTICOLO%>.value.length==0)
		{
			alert("Fonte/Articolo obbligatori!");
		    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.focus();
			return false;
		}
		
		if (
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.value!="-"
		  && document.LoadModificaReato.<%=ICostantiReato.CAMPO_ARTICOLO%>.value.length==0)	
		 || (document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.value=="-"
		  && document.LoadModificaReato.<%=ICostantiReato.CAMPO_ARTICOLO%>.value.length>0)
		   )
		{
			alert("Fonte/Articolo devono essere entrambi presenti");
		    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_FONTE%>.focus();
			return false;
		}
			
		// controllo obbligatorietà comma
		if (document.LoadModificaReato.<%=ICostantiReato.CAMPO_COMMA%>.value==""
		 && document.LoadModificaReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>.value!="-")	
		{
			alert("Comma obbligatorio");
		    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COMMA%>.focus();
			return false;
		}

		// *********************************************************************************************
		//               controllo tra il campo periodo_consumazione e le relative date
		// *********************************************************************************************
		// per i seguenti valori devono essere presenti entrambe le date:
		// valore  4 =  Accertato in Data [data1] e Permanente Sino al [data2]       
		// valore  7 =  Accertato in Data [data1] e in Data [data2]                  
		// valore  9 =  Commesso in Data [data1] e Permanente Sino al [data2]   
		// valore 10 =  Commesso in Data [data1] e in Data [data2] 
		// valore 17 =  Accertato dalla data  [data1] e fino alla data  [data2]   
		// valore 23 =  Commesso dalla data [data1]  e fino alla data  [data2]   
		if (
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="04")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="07")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="09")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="10")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="17")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="23")
		   )
		{
			if ((document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length==0)	
			 || (document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length==0))
			{
				alert("per il periodo di consumazione indicato devono essere specificate entrambe le date");
			    document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.focus();
				return false;
			}
		} // fine if

		// *********************************************************************************************
		// per i seguenti valori devono essere presenti entrambe le date:
		// valore  1 =  Commesso in Data [data1]                                          
		// valore  2 =  Accertato in Data [data1]                                                  
		// valore  3 =  Accertato in Data [data1] e Tuttora Permanente                
		// valore  5 =  In Epoca Anteriore e Prossima al [data1]                     
		// valore  6 =  In Epoca Successiva e Prossima al [data1]                     
		// valore  8 =  Commesso in Data [data1] e Tuttora Permanente                 
		// valore 11 =  Commesso Fino al [data1]                                                                           
		// valore 12 =  Il [data1]                                                                                                    
		// valore 15 =  Accertato dalla data [data1]                                                   
		// valore 18 =  Accertato fino alla data  [data1]                    
		// valore 19 =  Accertato in epoca anteriore e prossima alla data  [data1]                   
		// valore 20 =  Accertato in epoca successiva e prossima alla data  [data1]              
		// valore 22 =  Commesso dalla data  [data1]                                                                         
		// valore 24 =  Commesso in epoca anteriore e prossima alla data  [data1]                                                                                                    
		// valore 25 =  Commesso in epoca successiva e prossima alla data  [data1]                                                
		if (
		 	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="01")
		 || (document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="02")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="03")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="05")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="06")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="08")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="11")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="12")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="15")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="18")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="19")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="20")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="22")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="24")
		 ||	(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="25")
		   )
		{	
			if 
			((document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length==0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value.length>0)
			)	
			{
				alert("per il periodo di consumazione indicato deve essere presente solo la data inizio <DATA1>");
			    document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.focus();
				return false;
			}

		} // fine if
if 
			((document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>.value.length>0) ||
			(document.LoadModificaReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value.length>0)
			)
			{
				if (document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-") 
				{
					alert("se il periodo consumazione non è presente non è possibile specificare alcuna data");
				    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
				}
				if ((document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="16") ||
					(document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="21")) 
				{
					alert("per il periodo di consumazione indicato non è possibile specificare alcuna data");
				    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
				}
			}
			
			// se inserisco il luogo commesso reato è obbligatorio indicare un periodo di consumazione
			if ((document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-")
			&& (document.LoadModificaReato.<%=ICostantiReato.CAMPO_DESC_LUOGO%>.value!=""))
		 	{
		 			alert("indicare un periodo di consumazione se si vuole inserire il luogo commesso reato");
				    document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
		 	}
		return true;

	} // fine funzione
	//****************************************************************************************************

	<% // AMBROSINO 03-02-2011 - Su segnalazione di Alfieri, viene disabilitata la data che non ha motivo 
	   // 						 di essere inserita a seconda della scelta del CAMPO_COD_PERIODO_CONSUMAZIONE %>
	
	function mettidata()
 	{
 		var valueSel = document.LoadModificaReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value;

      	var nodedata1 = document.getElementById('divdata1');
      	var nodedata2 = document.getElementById('divdata2');
      
      	if(valueSel == '01' || valueSel == '02' || valueSel == '03' || valueSel == '05' || valueSel == '06' || 
      	   valueSel == '08' || valueSel == '11' || valueSel == '12' || valueSel == '15' || valueSel == '18' || 
      	   valueSel == '19' || valueSel == '20' || valueSel == '22' || valueSel == '24' || valueSel == '25') 
      	{
           	nodedata1.style.visibility='visible';
           	nodedata2.style.visibility='visible';
           	
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
			
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>.disabled = true;	
			
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value = "";			        	
      	}
      	
	  	if(valueSel == '04' || valueSel == '07' || valueSel == '09' || valueSel == '10' || 
	  	   valueSel == '17' || valueSel == '23')
		{
            nodedata1.style.visibility='visible';
            nodedata2.style.visibility='visible';

			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.disabled = false;   
            
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>.disabled = false;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>.disabled = false;	
        }
        
        if(valueSel == '-' || valueSel == '16' || valueSel == '21')
		{
            nodedata1.style.visibility='visible';
            nodedata2.style.visibility='visible';

			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.disabled = true;   
            
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>.disabled = true;
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>.disabled = true;	
			
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value = "";   
            
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>.value = "";
			document.LoadModificaReato.<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value = "";
        }       

	} <% // Chiude Function mettidata()  %>
	
<% //**************************************************************************************************** %>

	
</script>
</head>

<body class="corpo" onLoad="mettidata()">
  <table>
  <tr>
  <td class="LBG"><a href="Javascript:window.print();"><img align="middle"
  			src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
       ReatoModel lReato = new ReatoModel();
       String lAzione = new String();
       BigDecimal lIdReato=null;
       if( modalita.equals("I") )
       {
         lAzione = "siap.siep.reato.action.ActInserisciReato";
         lIdReato=new BigDecimal(IdReato);
%>
         <font class="campo">Inserimento Reato</font>
<%
       }
       else if( modalita.equals("M") )
       {
         lAzione = "siap.siep.reato.action.ActModificaReato";
         lReato = reato;
         lIdReato=lReato.getIdReato();
%>
         <font class="campo">Modifica  Reato</font>
<%
       }
       else if( modalita.equals("SIGE") )
       {
         lAzione = "siap.sige.reato.action.ActModificaReatoSige";
         lReato = reato;
         lIdReato=lReato.getIdReato();
%>
         <font class="campo">Modifica  Reato</font>
<%
       }   
%>
   	</td>
	</tr>
</table>

  <br>
  <%if( !modalita.equals("SIGE") ) { %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <%} else {%>
   <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
	 <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <br>
  <%}%>

	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaReato">
<%
  //Se il reato è quello principale
  if(lReato.getProgrCircostanza().intValue() == 1)
  {
%>
    <table cellspacing="2" cellpadding="2">
      <td class="l">Numero Reato</td>
      <td class="c">
      <input size="10" maxlength="10" value="<%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %>" 
      		 type="text" name="<%= ICostantiReato.CAMPO_PROGR_NUMERO_MANUALE %>">
      </td>
    </table>
<%
  }
%>
  <table cellspacing="1" cellpadding="2" width="95%">
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art. qualificante</td>
      <td class="int">Comma</td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
	//uguale al campo Articolo-qualificante
    //***************************************
%>
      <td class="int">Comma qualificante</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
 
     </tr>
    
    <tr>
      <td class="l">
        <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="l">
        <input size="4" maxlength="4" title="Anno" value="<%=StringUtils.toStringJSP(lReato.getAnnoFonte()) %>" 
        			type="text" name="<%= ICostantiReato.CAMPO_ANNO_FONTE %>"
        			onkeypress="return TicTabNumField(this,event)" 
        			onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">
        <input size="6" maxlength="6" title="Numero" value="<%=StringUtils.toStringJSP(lReato.getNumeroFonte()) %>" 
        			type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>"
        			onkeypress="return TicTabNumField(this,event)"> 
         			
      </td>
      <td class="l">
        <input size="5" maxlength="5" title="Articolo" value="<%=StringUtils.toStringJSP(lReato.getArticolo()) %>" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>">
      </td>
      <td class="l">
      <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
      </td>
      <td class="l">
        <input size="10" maxlength="10" title="Comma" value="<%=StringUtils.toStringJSP(lReato.getComma()) %>" 
        			type="text" name="<%= ICostantiReato.CAMPO_COMMA %>">
      </td>
 <%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiCommaQualificante%>
        </select>
      </td>
      
      <td class="l">
        <input size="2" maxlength="2" title="Lettera" 
        	value="<%=StringUtils.toStringJSP(lReato.getLettera()) %>" type="text" 
        	name="<%= ICostantiReato.CAMPO_LETTERA %>">
      </td>
      <td class="l">
        <input size="2" maxlength="2" title="Numero" 
        	value="<%=StringUtils.toStringJSP(lReato.getNumero()) %>" type="text" 
        	name="<%= ICostantiReato.CAMPO_NUMERO %>"> 
      </td>
    </tr>
  </table>
  
<%
  //Se il reato è quello principale
  if(lReato.getProgrCircostanza().intValue() == 1)
  {
%>
  <table cellspacing="2" cellpadding="2" width="95%">
    <tr><td class="Titolo" colspan="4">Reato</td></tr>
		<tr>
				<td class="l">Tipo Reato</td>
				<td class="l">
        <select name="<%= ICostantiReato.CAMPO_COD_TIPO_REATO %>"  >
          <%=TipiReato%>
        </select>
        </td>
		</tr>
    <tr>
				<td class="l">Luogo Reato</td>
				<td class="l"><input size="50" maxlength="300" 
						value="<%=StringUtils.toStringJSP(lReato.getDescLuogo()) %>" 
						type="text" name="<%= ICostantiReato.CAMPO_DESC_LUOGO %>"  >
				</td>
	</tr>
    <tr>
				<td class="l">Periodo Consumazione</td>
				<td class="l">
        <select name="<%= ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE %>" onChange="mettidata()" >
          <%=PeriodoConsumazione%>
        </select>
        </td>
    </tr>
  </table>
  
  <table cellspacing="2" cellpadding="2">
  	<tr>
    	<div id="divdata1" style="visibility:hidden; position:relative; width:100%;">  	
      		<td class="l">&lt;Data1&gt;</td>
      		<td class="l">
<%
		        String lStrGGInizio = StringUtils.toStringJSP( lReato.getGiornoInizio());
		        if (lStrGGInizio.length() == 1)
		          lStrGGInizio = "0"+lStrGGInizio;
		
		        String lStrMMInizio = StringUtils.toStringJSP( lReato.getMeseInizio());
		        if (lStrMMInizio.length() == 1)
		          lStrMMInizio = "0"+lStrMMInizio;
		
		        String lStrAAInizio = StringUtils.toStringJSP( lReato.getAnnoInizio());
		%>
		        <input size="2" maxlength="2" value="<%=lStrGGInizio%>" type="text" size="2" maxlength="2" 
		        	name="<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input size="2" maxlength="2" value="<%=lStrMMInizio%>" type="text" size="2" maxlength="2" 
		        	name="<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input size="4" maxlength="4" value="<%=lStrAAInizio%>" type="text" size="4" maxlength="4" 
		        	name="<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        		<br>
        			<font class="ob">(ammessa data parziale)</font>
     		</td>
     	</div>
     	
     	<div id="divdata2" style="visibility:hidden; position:relative; width:100%;">	
      		<td class="l">&lt;Data2&gt;</td>
      		<td class="l">
<%
		        String lStrGGFine = StringUtils.toStringJSP( lReato.getGiornoFine());
		        if (lStrGGFine.length() == 1)
		          lStrGGFine = "0"+lStrGGFine;
		
		        String lStrMMFine = StringUtils.toStringJSP( lReato.getMeseFine());
		        if (lStrMMFine.length() == 1)
		          lStrMMFine = "0"+lStrMMFine;
		
		        String lStrAAFine = StringUtils.toStringJSP( lReato.getAnnoFine());
		%>
		        <input size="2" maxlength="2" value="<%=lStrGGFine%>" type="text" size="2" maxlength="2" 
		        	name="<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input size="2" maxlength="2" value="<%=lStrMMFine%>" type="text" size="2" maxlength="2" 
		        	name="<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		        -
		        <input size="4" maxlength="4" value="<%=lStrAAFine%>" type="text" size="4" maxlength="4" 
		        	name="<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
		        	onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        		<br>
        			<font class="ob">(ammessa data parziale)</font>
      		</td>
      	</div>
      		
    </tr>
  </table>
  
  <table cellspacing="2" cellpadding="2">
    <tr>
		<td class="l">Note</td>
		<td class="l">
			<TextArea cols="80" rows="5" name="<%= ICostantiReato.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lReato.getNote()) %></textarea>
		</td>
	</tr>
  </table>
<%
  }
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone">
      </td>
	</tr>
  </table>
  
  <input type="HIDDEN" value="<%=lReato.getProgrReato().toString()%>" 
  					   name="<%=ICostantiReato.CAMPO_PROGR_REATO%>">
  <input type="HIDDEN" value="<%=lReato.getProgrCircostanza().toString()%>" 
  					   name="<%=ICostantiReato.CAMPO_PROGR_CIRCOSTANZA%>">
  <input type="HIDDEN" value="<%=lReato.getFasSieIdFascicoloSiep() != null ? lReato.getFasSieIdFascicoloSiep().toString() : ""%>" 
  					   name="<%=ICostantiReato.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>">
  <input type="HIDDEN" value="<%=lIdReato%>" name="<%=ICostantiReato.CAMPO_ID_REATO%>">
  <input type="HIDDEN" value="<%=lAzione%>"  name="<%=IWebConstants.ACTION_FIELD%>"> 
  <input type="HIDDEN" value="<%=TornaQui%>" name="<%=IWebConstants.LINK_RITORNO%>"> 

</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator = new Validator("LoadModificaReato");

  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_FONTE%>", "num");
  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_FONTE%>", "minlength=4");

  // *********************************************************************************
  // Federica - a9-rr-078
  // il campo articolo diviene numerico 
  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ARTICOLO%>","numeric");
  // fine modifica
  // *********************************************************************************
  
  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_NUMERO_FONTE%>", "alphanumeric");

  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_COMMA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_LETTERA%>", "alphanumeric");
  frmvalidator.addValidation("<%=ICostantiReato.CAMPO_NUMERO%>", "alphanumeric");

<%
  //Se il reato è quello principale
  if(lReato.getProgrCircostanza().intValue() == 1)
  {
%>
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","lt=2099");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","lt=31");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","gt=1");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","lt=12");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","lt=2099");

	// *******************************************************************
	// Federica - a9-rr-078
	// aggiunta funzione di controllo dei campi 
	frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");
	// fine modifica
	// *******************************************************************

<%
  }
%>
</script>

 </body>
</html>