<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.siep.modulocumulo.model.CircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiCircostanzaCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" 		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="circostanzaCumulo"			scope="request" class="siap.siep.modulocumulo.model.CircostanzaCumuloModel"/>
<jsp:useBean id="modalita" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="BilanciamentoCircostanze" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="VectorCircosCumulo" 		scope="request" class="java.util.Vector"/>

<%
String flagSentenzaApplicazPena = null;
String descrBilanciamentoCircostanze = null;
String flagGiudizioAbbreviato = null;
String noteBilanciamento = null;

CircostanzaCumuloModel lCircostanza = new CircostanzaCumuloModel();
if( modalita.equals("M") )
{
  lCircostanza = circostanzaCumulo;
}  

%>
<!--  	LoadInserisciCircostanzaCumulo	--> 
<html>
<head>
  <title>[S.I.E.S.] - Gestione Circostanze Aggravanti/Attenuanti Cumulo </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
<script language="JavaScript">

//==========================================================================
// Ritorna alla lista dei Reati/Circostanze Aggravanti/Attenuanti per il Titolo
//==========================================================================
function eseguiFunzione(action)
{
  document.LoadInserCircosCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
  document.LoadInserCircosCumulo.submit();
}

function ControlloObbligatorieta()
{
	// se selezionato un reato (cosa controllata nella ActInserisciReato), deve esserci 
	// obbligatoriamente almeno un articolo e la fonte

	var ind;
	for (ind=0; ind<5; ind++)
	{
		if ((document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value=="442"	
		  && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value=="25")
		 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value=="444"
		  && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value=="25"))	
		{
			alert("Gli Articoli 442 e 444 devono essere inseriti tramite gli appositi flag!");
		    document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].focus();
			return false;
		}

	//	if (document.LoadInserCircosCumulo.< %=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value!="-"
	//	 || document.LoadInserCircosCumulo.< %=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value.length>0)
	//	{
	//		presenza_articolo=1;
	//	}

		if ( (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value!="-"
		   && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value.length==0)
		  || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value=="-"
		   && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value.length>0) )
		{
			alert("Fonte/Articolo devono essere entrambi presenti");
		    document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].focus();
			return false;
		}

		if (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].value=="-"
		 && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>[ind].value.length==0)	
		{
			// se viene digitato qualcosa sulle righe di definizione dell'articolo,
			// è obbligatorio riempirle correttamente con COD_FONTE e ARTICOLO
			if ((document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE%>[ind].value.length>0)
			 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE%>[ind].value.length>0)
			 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE%>[ind].value!="-")
			 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")
			 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>[ind].value.length>0)
			 ||	(document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_LETTERA%>[ind].value.length>0)
			 || (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO%>[ind].value.length>0))
			{ 
				alert("Fonte/Articolo obbligatori!");
		    	document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[ind].focus();
				return false;
			}
		}
		
		// controllo obbligatorietà comma 
		if (document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>[ind].value==""
		 && document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")	
		{
			alert("Comma obbligatorio");
		    document.LoadInserCircosCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>[ind].focus();
			return false;
		}

	} // fine for


	//if (presenza_articolo=="0") 
	// {
	//	alert("Fonte/Articolo obbligatori!");
	//    document.LoadInserCircosCumulo.< %=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>[0].focus();
	//	return false;
	//}
	
	return true;
} 
// fine funzione
//****************************************************************************************************

function deseleziona()
{
	//alert("A che servo???");
}
</script>

</head>
<body class="corpo" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
          <font class="campo">Inserimento Aggravanti soggettive/Attenuanti relative ad un Titolo Cumulato</font>
      </td>
      
      <!-- 		Bottone Torna Indietro	 -->
      <td class="LBG">
         <!-- a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo')" -->
         <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
         </a>
      </td>
    </tr>
  </table>

  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
        </td>
    </tr>
    <tr>
        <td>
          <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
        </td>
    </tr>
  </table>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserCircosCumulo">
<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciCircostanzaCumulo">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->

  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_FLAG_STATO%>" value="">
  	
  <input type="Hidden" name="cablati" value="">
  <input type="Hidden" value="<%=lCircostanza.getIdCircostanzaCumulo() %>" name="<%= ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>">
  <input type="hidden" name="verifyCampiComuni" value="0">
  	
  <table cellspacing="1" cellpadding="2" width="85%">
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
    <td class="l">
      <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>">
        <%=TipiFontiReato%>
      </select>
    </td>
    <td class="l">
      <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>"
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
        onBlur="javascript:value=FillYear(value)">
      	
    </td>
    <td class="l">
      <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
        onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

    </td>
    <td class="l">
      <input size="5" maxlength="5"  title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>"
        onFocus="javascript:textboxSelect(this)">

    </td>
    <td class="l">
      <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
    </td>
    <td class="l">
      <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>" 
	    onFocus="javascript:textboxSelect(this)">
    </td>
    <td class="c">
      <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
        <%=TipiSottonumerazione %>
      </select>
    </td>
    <td class="l">
      <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>" 
        onFocus="javascript:textboxSelect(this)">

    </td>
    <td class="l">
      <input size="2" title="Numero" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
      	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
        onFocus="javascript:textboxSelect(this)">

    </td>
    </tr>
<%
    if( modalita.equals("I") )
    {	
				// Seconda Riga
%>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato %>
          </select>
        </td>
        <td class="l">
           <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
           	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>"
     	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
           <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
           	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
           <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
           	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      <td class="c">
        <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Numero" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <%		//Terza Riga		 %>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
           <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
           	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
		<td class="c">
		  <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <%		// Quarta Riga		 %>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
 		<td class="c">
		  <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <%		// Quinta Riga		 %>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
		<td class="c">
		  <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2"  title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
    </table>
    
    <table cellspacing="2" cellpadding="2" >
      <tr>
        <td class="r">
          <strong>99 CP C1</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C1">
          <strong>C2 N1</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C2 N1">
          <strong>C2 N2</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C2 N2">
          <strong>C2 N3</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C2 N3">
          <strong>C3</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C3">
          <strong>C4</strong>&nbsp;<input type="checkbox" name="cablati" value="99 01 C4">
        </td>
      </tr>
    </table>
    
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="r"><strong>102 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="102 01"></td>
        <td class="r"><strong>103 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="103 01"></td>
        <td class="r"><strong>104 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="104 01"></td>
        <td class="r"><strong>105 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="105 01"></td>
        <td class="r"><strong>108 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="108 01"></td>
      </tr>
    </table>
    
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td class="r"><strong>62 CP</strong>&nbsp;
          <strong>N1</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N1">
          <strong>N2</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N2">
          <strong>N3</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N3">
          <strong>N4</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N4">
          <strong>N5</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N5">
          <strong>N6</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 N6">
        </td>
        <td class="r">
          <strong>62 BIS CP</strong>&nbsp;<input type="checkbox" name="cablati" value="62 01 BIS">
        </td>
      </tr>
    </table>
    
    <script language="JavaScript">
	function flagCampiComuni(){
		document.LoadInserCircosCumulo.verifyCampiComuni.value = 1;		
	}
    </script>
    
    <table cellspacing="2" cellpadding="2" >
		<tr>
	        <td class="l">Sentenza di applicazione pena</td>
	        <%
	        CircostanzaCumuloModel CiReato = null;	
	        if(VectorCircosCumulo.size()>0) 
	      	  CiReato = (CircostanzaCumuloModel)VectorCircosCumulo.get(0);
	        
	        if(CiReato==null)
	        {	        	
	        	%>
	        	<td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' value = 'S' onClick="flagCampiComuni()">        
		        </td>
	        	<%
	        }
 	        else if( CiReato.getFlagSentenzaApplicazPena()== null       ||
 	 				 CiReato.getFlagSentenzaApplicazPena().equals("N")  ||
 					 CiReato.getFlagSentenzaApplicazPena().equals("")  )
			{ %>
				<td class="l">
			          <input type='checkbox' name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' 
			          	value = 'S'<%= (CiReato.getFlagSentenzaApplicazPena() != null && CiReato.getFlagSentenzaApplicazPena().equals("S")) ? "checked" : ""%> 
								onClick="flagCampiComuni()">        
			    </td>
		<%	}
			else
			{	%>
				<td class="l">
					<font class="campo">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0"></font>&nbsp;		         
					<input type="hidden" name="FlagSentenzaApplicazPena" value="V">
				</td>
		<%	}%>

     	</tr>
     	
    	<tr>
	        <td class="l">Bilanciamento circostanze</td>
			<%
			if(CiReato==null || 
			    "-".equals(CiReato.getCodBilanciamentoCircostanze() ) ||
				"".equals(CiReato.getCodBilanciamentoCircostanze() ) 	 )
				//CiReato.getCodBilanciamentoCircostanze().equals("-") || 
				//CiReato.getCodBilanciamentoCircostanze().equals(""))
			{ %>
				 <td class="l">
				 	<select name="<%=ICostantiCircostanzaCumulo.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE%>" onchange="flagCampiComuni()"><%=BilanciamentoCircostanze%>
					</select>
				 </td>		
		<%	}
			//else if(!CiReato.getCodBilanciamentoCircostanze().equals("-") )
			else if(!"-".equals(CiReato.getCodBilanciamentoCircostanze() ) )	
			{	%>
				 <td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(CiReato.getDescrBilanciamentoCircostanze())%>
					</font>&nbsp;
					<input type="hidden" name="CodBilanciamentoCircostanze" value="<%=CiReato.getCodBilanciamentoCircostanze()%>">
				 </td>	
		<%	}	%>
		</tr>
		
	 	<tr>
	 		<td class="l">Annotazioni Bilanciamento circostanze</td>
	 		<td>
	 			<%
	 			String noteB = "";
	 			if(CiReato!=null && CiReato.getNoteBilanciamento()!=null)
	 				noteB = CiReato.getNoteBilanciamento();
	 			%>
	 			<textarea cols="50" rows="5" name="NoteBilanciamento" onchange="flagCampiComuni()"><%=noteB%></textarea> 
	 		</td>
	 	</tr>
	 	
      	<tr>
        	<td class="l">Giudizio abbreviato</td>
			<%
			if(CiReato==null)
			{	%>
				<td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S' onClick="flagCampiComuni()">
		        </td>
		<%	}
			else if(CiReato.getFlagGiudizioAbbreviato() != null &&
					CiReato.getFlagGiudizioAbbreviato().equals("S") )
			{  %>
				<td class="l">
					<font class="campo">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
					</font>&nbsp;
					<input type="hidden" name="FlagGiudizioAbbreviato" value="V">
				</td>
		<%	}
			else
			{	%>
		        <td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S' <%= ( (CiReato.getFlagGiudizioAbbreviato() != null && CiReato.getFlagGiudizioAbbreviato().equals("S") ) ) ? "checked" : ""%> onClick="flagCampiComuni()">
		        </td>
		<%	}	%>
		</tr>
		<br>
		<tr>
        	<td class="l">Motivo Inserimento Circostanza nel titolo cumulato</td>
        	<td class="l"><TextArea cols=80 rows=3 name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>"></textarea></td>
    	</tr>
	</table>
<%}%>
  
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone" >
      </td>

    </tr>
  </table>
  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserCircosCumulo");
<%
    for(int i=0; i<5; i++)
    {
%>
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE%>","<%=i%>","alphanumeric");
	  frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>","<%=i%>","numeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>","<%=i%>","alphanumeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_LETTERA%>","<%=i%>","alphanumeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO%>","<%=i%>","alphanumeric");
<%
    } // fine for
%>

	frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");


 </script>
</form>
</body>
</html>