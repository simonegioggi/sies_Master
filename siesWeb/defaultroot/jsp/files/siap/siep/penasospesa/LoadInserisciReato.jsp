<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.util.SIESSwitch" %>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page import="f3b.log.LogF3B" %>

<jsp:useBean id="reati" 				scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiPeneDetentive" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       	scope="request" class="java.lang.String"/>

<%
  session.setAttribute("reati",new Vector());
%>


<html>
<head>
<title>[S.I.E.S.] - Gestione Reato </title>

	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <base target="_self">

	<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

	<script language="JavaScript">

	var desktop;
	function ListaReatiPredisposti(a_formname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reatopredisposto.action.ActRicercaReatoPredisposto&formname="+a_formname, "Ricerca_Reati_Predisposti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
	
	function ListaCopiaReati(a_formname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadRicercaReatiFascicoloFramset&formname="+a_formname, "Ricerca_Reati_Fascicolo","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=400");
	}      
	
	function prosegui()
	{
       
	  //opener.document.getElementById("iframereati").style.display="block";
	  //opener.document.getElementById("iframereati").contentWindow.location.reload(true);
	  	var conto=0;
 		for (ind=0; ind<5; ind++){
			//alert (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text);
			if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].value!="-"){
				conto++;
				if (ind==0)
					document.LoadInserisciReato.descrfonte1.value=document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text;
				if (ind==1)
					document.LoadInserisciReato.descrfonte2.value=document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text;
				if (ind==2)
					document.LoadInserisciReato.descrfonte3.value=document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text;
				if (ind==3)
					document.LoadInserisciReato.descrfonte4.value=document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text;
				if (ind==4)
					document.LoadInserisciReato.descrfonte5.value=document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].options[document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].selectedIndex].text;
			}
		} 
		var op = window.dialogArguments;
		if (op!=null){
			op.LoadInserisciRichiestaRevoca.nreati.value= conto;
			op.LoadInserisciRichiestaRevoca.cablati.value= document.LoadInserisciReato.cablati.value;
			op.LoadInserisciRichiestaRevoca.cablati2.value= document.LoadInserisciReato.cablati2.value;
		}else{
			window.parent.opener.document.LoadInserisciRichiestaRevoca.nreati.value= conto;
			window.parent.opener.document.LoadInserisciRichiestaRevoca.cablati.value= document.LoadInserisciReato.cablati.value;
			window.parent.opener.document.LoadInserisciRichiestaRevoca.cablati2.value= document.LoadInserisciReato.cablati2.value;
		}
	  	window.parent.close();
	}      

	//****************************************************************************************************
	//Federica - a9-rr-078
	//aggiunta funzione di controllo dei campi 

	function ControlloObbligatorieta()
	{

		// se selezionato un reato (cosa controllata nella ActInserisciReato), deve esserci 
		// obbligatoriamente almeno un articolo e la fonte
		var presenza_articolo=0;
		var ind;
		for (ind=0; ind<5; ind++)
		{
			if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].value!="-"
			 || document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ARTICOLO%>[ind].value.length>0)
			{
				presenza_articolo=1;
			}

			if ( (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].value!="-"
			   && document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ARTICOLO%>[ind].value.length==0)
			  || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].value=="-"
			   && document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ARTICOLO%>[ind].value.length>0) )
			{
				alert("Fonte/Articolo devono essere entrambi presenti");
			    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].focus();
				return false;
			}

			if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].value=="-"
			 && document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ARTICOLO%>[ind].value.length==0)	
			{
				// se viene digitato qualcosa sulle righe di definizione dell'articolo,
				// è obbligatorio riempirle correttamente
				if ((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_FONTE%>[ind].value.length>0)
				 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>[ind].value.length>0)
				 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>[ind].value!="-")
				 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")
				 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA%>[ind].value.length>0)
				 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_LETTERA%>[ind].value.length>0)
				 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_NUMERO%>[ind].value.length>0))
				{ 
					alert("Fonte/Articolo obbligatori!");
			    	document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[ind].focus();
					return false;
				}
			}

			// controllo obbligatorietà comma 
			if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA%>[ind].value==""
			 && document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")	
			{
				alert("Comma obbligatorio");
			    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA%>[ind].focus();
				return false;
			}

		} // fine for

		if (presenza_articolo=="0") 
		{
			alert("Fonte/Articolo obbligatori!");
		    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_FONTE%>[0].focus();
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
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="04")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="07")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="09")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="10")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="17")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="23")
		   )
		{
			if ((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length==0)		
			 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length==0))
			{
				alert("per il periodo di consumazione indicato devono essere specificate entrambe le date");
			    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.focus();
				return false;
			}
		} // fine if

		// *********************************************************************************************
		// per i seguenti valori deve essere presenti solo la prima data:
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
		 	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="01")
		 || (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="02")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="03")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="05")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="06")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="08")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="11")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="12")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="15")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="18")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="19")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="20")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="22")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="24")
		 ||	(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="25")
		   )
		{
			if 
			((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length==0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value.length>0)
			)	
			{
				alert("per il periodo di consumazione indicato deve essere presente solo la data inizio <DATA1>");
			    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.focus();
				return false;
			}
			
		} // fine if


		if 
			((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>.value.length>0) ||
			(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>.value.length>0)
			)
			{
				if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-") 
				{
					alert("se il periodo consumazione non è presente non è possibile specificare alcuna data");
				    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
				}
				if ((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="16") ||
					(document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="21")) 
				{
					alert("per il periodo di consumazione indicato non è possibile specificare alcuna data");
				    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
				}
			}
			
			// se inserisco il luogo commesso reato è obbligatorio indicare un periodo di consumazione
			if ((document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.value=="-")
			&& (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_DESC_LUOGO%>.value!=""))
		 	{
		 			alert("indicare un periodo di consumazione se si vuole inserire il luogo commesso reato");
				    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE%>.focus();
					return false;
		 	}
		 	
		 	
		return true;

	} // fine funzione
	//****************************************************************************************************
	
</script>
</head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        ReatoModel lReato = new ReatoModel();
        String lAzione = new String();
        //lAzione = "siap.siep.reato.action.ActInserisciReato";
        lAzione = "siap.siep.penasospesa.action.ActLoadInserisciRichiestaRevoca";
%>
          <font class="campo">Inserimento Reato</font>
      </td>
    </tr>
  </table>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciReato">
  <table cellspacing="2" cellpadding="2" width="95%">
    <tr><td class="Titolo" colspan="9">Reato</td></tr>
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="l">Numero Reato</td>
    <td class="c">
      <input size="10" maxlength="10" value="<%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %>" type="text" name="<%= ICostantiReato.CAMPO_PROGR_NUMERO_MANUALE %>">
    </td>
   </tr>   
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="r"><strong>110 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="110 01"></td>
    <td class="r"><strong>56 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="56 01"></td>
    <td class="r">
      <strong>81 CP</strong>&nbsp;
      <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati2" value="81 01 C1">
      <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati2" value="81 01 C2">
    </td>
  </tr>
  </table>
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
	// uguale al campo Articolo-qualificante
    //***************************************
%>
      <td class="int">Comma qualificante</td>
     
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
<%
    for(int i=0; i<5; i++)
    {
%>
    <tr>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lReato.getAnnoFonte()) %>" type="text" name="<%= ICostantiReato.CAMPO_ANNO_FONTE %>"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
          onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="c">
        <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lReato.getNumeroFonte()) %>" type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>"
          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lReato.getArticolo()) %>" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>"
          onFocus="javascript:textboxSelect(this)">
      </td>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
        <input size="10" maxlength="10" title="Comma" value="<%=StringUtils.toStringJSP(lReato.getComma())%>" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>"
         onFocus="javascript:textboxSelect(this)">
      </td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      
      <td class="c">
        <input size="2" maxlength="2" title="Lettera" value="<%=StringUtils.toStringJSP(lReato.getLettera()) %>" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>"
         onFocus="javascript:textboxSelect(this)">
      </td>
      <td class="c">
       <input size="2" maxlength="2" title="Numero" value="<%=StringUtils.toStringJSP(lReato.getNumero()) %>" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>"
         onFocus="javascript:textboxSelect(this)">
      </td>
    </tr>
<%
   }
%>
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="r">
      <strong>61 CP</strong>&nbsp;
      <strong>N1</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N1">
      <strong>N2</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N2">
      <strong>N3</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N3">
      <strong>N4</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N4">
      <strong>N5</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N5">
      <strong>N6</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N6">
      <strong>N7</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N7">
      <strong>N8</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N8">
      <strong>N9</strong>&nbsp;<input  type="checkbox" name="cablati" value="61 01 N9">
      <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N10">
      <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N11">
    </td>
  </tr>
  </table>

  <table cellspacing="2" cellpadding="2">
  <tr>

    <td class="r">
      <strong>112 CP</strong>&nbsp;
      <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C1">
      <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C2">
      <strong>C3</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C3">
      <strong>C4</strong>&nbsp;<input type="checkbox" name="cablati" value="112 01 C4">
    </td>
    <td class="r"><strong>113 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="113 01"></td>
    <td class="r"><strong>114 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="114 01"></td>
    <td class="r"><strong>116 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="116 01"></td>
    <td class="r"><strong>117 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="117 01"></td>
  </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="r">
      <strong>625 CP</strong>&nbsp;
      <strong>N1</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N1">
      <strong>N2</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N2">
      <strong>N3</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N3">
      <strong>N4</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N4">
      <strong>N5</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N5">
      <strong>N6</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N6">
      <strong>N7</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N7">
      <strong>N8</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N8">
      <strong>N9</strong>&nbsp;<input  type="checkbox" name="cablati" value="625 01 N9">
      <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N10">
      <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N11">
    </td>
  </tr>
  </table>
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
				<td class="l"><input size="50" maxlength="300" value="<%=StringUtils.toStringJSP(lReato.getDescLuogo()) %>" type="text" name="<%= ICostantiReato.CAMPO_DESC_LUOGO %>"  ></td>
		</tr>
    <tr>
				<td class="l">Periodo Consumazione</td>
				<td class="l">
        <select name="<%= ICostantiReato.CAMPO_COD_PERIODO_CONSUMAZIONE %>"  >
          <%=PeriodoConsumazione%>
        </select>
        </td>
    </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
    <tr>
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
        	   name="<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
        	   onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size="2" maxlength="2" value="<%=lStrMMFine%>" type="text" size="2" maxlength="2" 
        	   name="<%= ICostantiReato.CAMPO_MESE_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
        	   onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input size="4" maxlength="4" value="<%=lStrAAFine%>" type="text" size="4" maxlength="4" 
        	   name="<%= ICostantiReato.CAMPO_ANNO_DATA_FINE%>" onFocus="javascript:textboxSelect(this)" 
        	   onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        <br>
        <font class="ob">(ammessa data parziale)</font>
      </td>
    </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
    <tr>
				<td class="l">Note</td>
				<td class="l"><TextArea cols="80" rows="5" name="<%= ICostantiReato.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(lReato.getNote()) %></textarea></td>
		</tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone" onclick="javascript:prosegui();">
      </td>
		</tr>
</table>
  <input value="" type="Hidden" name="cablati">
  <input value="" type="Hidden" name="cablati2">
  <input type="HIDDEN" name="<%= ICostantiReato.CAMPO_ID_REATO%>" value="<%=lReato.getIdReato()%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
  <input type="HIDDEN" name="dareati" value="SI">
  <input type="HIDDEN" name="descrfonte1" value="">
  <input type="HIDDEN" name="descrfonte2" value="">
  <input type="HIDDEN" name="descrfonte3" value="">
  <input type="HIDDEN" name="descrfonte4" value="">
  <input type="HIDDEN" name="descrfonte5" value="">

</form>

<script language="JavaScript" type="text/javascript">

	var frmvalidator = new Validator("LoadInserisciReato");
	
<%
	for(int i=0; i<5; i++)
	{
%>
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_ANNO_FONTE%>","<%=i%>","num");
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_ANNO_FONTE%>","<%=i%>","minlength=4");

		// *************************************
		// Federica - a9-rr-078
		// il campo articolo diviene numerico 
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_ARTICOLO%>","<%=i%>","numeric");
		// fine modifica
		// *************************************
		
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_NUMERO_FONTE%>","<%=i%>","alphanumeric");
		
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_COMMA%>","<%=i%>","alphanumeric");
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_LETTERA%>","<%=i%>","alphanumeric");
		frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_NUMERO%>","<%=i%>","alphanumeric");
<%
	}
%>
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_INIZIO%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_INIZIO%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");
	
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_GIORNO_DATA_FINE%>","lt=31");
	
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","gt=1");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_MESE_DATA_FINE%>","lt=12");
	
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","numeric");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiReato.CAMPO_ANNO_DATA_FINE%>","lt=3000");

	// *******************************************************************
	// Federica - a9-rr-078
	// aggiunta funzione di controllo dei campi 
	// Anna frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");
	// fine modifica
	// *******************************************************************
	
	
</script>
</body>
</html>