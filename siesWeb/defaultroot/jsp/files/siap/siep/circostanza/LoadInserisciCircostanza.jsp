<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.siep.circostanza.model.CircostanzaModel"%>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<jsp:useBean id="circostanza" 				scope="request" class="siap.siep.circostanza.model.CircostanzaModel"/>
<jsp:useBean id="modalita" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="BilanciamentoCircostanze" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="circostanze" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Circostanza Soggetto </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
<script language="JavaScript">
function VerifyChiamate(id)
{
 if(id==1)
  {
<% 
if( modalita.equals("I") )
     {
     if(modoSIGE) {
  %>   	 
        document.LoadInserisciCircostanza.<%=IWebConstants.ACTION_FIELD%>.value="siap.sige.circostanza.action.ActInserisciCircostanzaSige";
     
 <% }else{ %>
      	document.LoadInserisciCircostanza.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.circostanza.action.ActInserisciCircostanza";
<%   }}
     else if( modalita.equals("M") )
     {
%>
     document.LoadInserisciCircostanza.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.circostanza.action.ActModificaCircostanza";
<%
     }
%>
  }else if(id==2)
  {
     document.LoadInserisciCircostanza.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva";
  }
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
		if ((document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value=="442"	
		  && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value=="25")
		 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value=="444"
		  && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value=="25"))	
		{
			alert("Gli Articoli 442 e 444 devono essere inseriti tramite gli appositi flag!");
		    document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].focus();
			return false;
		}

		if (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value!="-"
		 || document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value.length>0)
		{
			presenza_articolo=1;
		}

		if ( (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value!="-"
		   && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value.length==0)
		  || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value=="-"
		   && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value.length>0) )
		{
			alert("Fonte/Articolo devono essere entrambi presenti");
		    document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].focus();
			return false;
		}

		if (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].value=="-"
		 && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>[ind].value.length==0)	
		{
			// se viene digitato qualcosa sulle righe di definizione dell'articolo,
			// è obbligatorio riempirle correttamente
			if ((document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_ANNO_FONTE%>[ind].value.length>0)
			 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_NUMERO_FONTE%>[ind].value.length>0)
			 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE%>[ind].value!="-")
			 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")
			 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>[ind].value.length>0)
			 ||	(document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_LETTERA%>[ind].value.length>0)
			 || (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_NUMERO%>[ind].value.length>0))
			{ 
				alert("Fonte/Articolo obbligatori!");
		    	document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[ind].focus();
				return false;
			}
		}
		
		// controllo obbligatorietà comma 
		if (document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>[ind].value==""
		 && document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")	
		{
			alert("Comma obbligatorio");
		    document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>[ind].focus();
			return false;
		}

	} // fine for


	//if (presenza_articolo=="0") 
	// {
	//	alert("Fonte/Articolo obbligatori!");
	//    document.LoadInserisciCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>[0].focus();
	//	return false;
	//}
	
	return true;
} 
// fine funzione
//****************************************************************************************************

function deseleziona()
{

}
</script>

</head>
<body class="corpo" onload="deseleziona()">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
         CircostanzaModel lCircostanza = new CircostanzaModel();
         //String lAzione = new String();
         if( modalita.equals("I") )
         {
          // lAzione = "siap.siep.circostanza.action.ActInserisciCircostanza";
%>
          <font class="campo">Inserimento Aggravanti soggettive/Attenuanti</font>
<%
         }
         else if( modalita.equals("M") )
         {
          // lAzione = "siap.siep.circostanza.action.ActModificaCircostanza";
           lCircostanza = circostanza;
%>
           <font class="campo">Modifica Aggravanti soggettive/Attenuanti</font>
<%
         }
%>
      </td>
    </tr>
  </table>

  <br>
  <%if (!modoSIGE) { %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
   <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
	 <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%} %>
  <br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciCircostanza">
  <table cellspacing="1" cellpadding="2">
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Articolo qualificante</td>
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
    <tr>
    <td class="l">
      <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>">
        <%=TipiFontiReato%>
      </select>
    </td>
    <td class="l">
      <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
      	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
        onBlur="javascript:value=FillYear(value)">
      	
    </td>
    <td class="l">
      <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
        onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

    </td>
    <td class="l">
      <input size="5" maxlength="5"  title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>"
        onFocus="javascript:textboxSelect(this)">

    </td>
    <td class="l">
      <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
        <%=TipiSottonumerazione %>
      </select>
    </td>
    <td class="l">
      <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>" 
	    onFocus="javascript:textboxSelect(this)">
    </td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
      <td class="c">
        <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>

    <td class="l">
      <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>" 
        onFocus="javascript:textboxSelect(this)">

    </td>
    <td class="l">
      <input size="2" title="Numero" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
      	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
        onFocus="javascript:textboxSelect(this)">

    </td>
    </tr>
<%
    if( modalita.equals("I") )
    {
%>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato %>
          </select>
        </td>
        <td class="l">
           <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
           	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
     	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
           <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
           	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
           <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
           	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
      <td class="c">
        <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Numero" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
           <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
           	type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
		<td class="c">
		  <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
 <%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
		<td class="c">
		  <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
      <tr>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>">
            <%=TipiFontiReato%>
          </select>
        </td>
        <td class="l">
          <input size="4" maxlength="4" title="Anno Fonte" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>"
    	    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

        </td>
        <td class="l">
          <input size="6" maxlength="6" title="Numero" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

        </td>
        <td class="l">
          <input size="5" maxlength="5" title="Articolo" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
            <%=TipiSottonumerazione %>
          </select>
        </td>
        <td class="l">
          <input size="2" title="Comma" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
<%
    //***************************************
	//Federica - a9-rr-078
	//aggiunto campo Comma-Qualificante 
    //***************************************
%>
		<td class="c">
		  <select name="<%= ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE %>">
		    <%=TipiSottonumerazione %>
		  </select>
		</td>

        <td class="l">
          <input size="2"  title="Lettera" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
        <td class="l">
          <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero())%>" 
          	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

        </td>
      </tr>
    </table>
    <table cellspacing="2" cellpadding="2">
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
		document.LoadInserisciCircostanza.verifyCampiComuni.value = 1;		
	}
    </script>
    <table cellspacing="2" cellpadding="2">
		<tr>
	        <td class="l">Sentenza di applicazione pena</td>
	        <%
	        CircostanzaModel CiReato = null;	
	        if(circostanze.size()>0) CiReato = (CircostanzaModel)circostanze.get(0);
	        if(CiReato==null){	        	
	        	%>
	        	<td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanza.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' value = 'S' onClick="flagCampiComuni()">        
		        </td>
	        	<%
	        }
 	        else if(
				CiReato.getFlagSentenzaApplicazPena()== null 
				||  CiReato.getFlagSentenzaApplicazPena().equals("N") 
				||	CiReato.getFlagSentenzaApplicazPena().equals("")
				)
			{%>

			<td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanza.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' 
		          	value = 'S'<%= (CiReato.getFlagSentenzaApplicazPena() != null && CiReato.getFlagSentenzaApplicazPena().equals("S")) ? "checked" : ""%> 
							onClick="flagCampiComuni()">        
		        </td>
		<%}
			else{%>
				<td class="l">
					<font class="campo">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0"></font>&nbsp;		         
<%
//Federica - a9-rr-078
// <input type="hidden" name="FlagSentenzaApplicazPena" value="<%=CiReato.getFlagSentenzaApplicazPena()
%><%	//   ">
%>
					<input type="hidden" name="FlagSentenzaApplicazPena" value="V">
				</td>
			<%}%>

     	</tr>
    	<tr>
	        <td class="l">Bilanciamento circostanze</td>
			<%
			if(CiReato==null || 
				CiReato.getCodBilanciamentoCircostanze().equals("-") || 
				CiReato.getCodBilanciamentoCircostanze().equals(""))
			{%>
			 <td class="l">
			 	<select name="<%=ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE%>" onchange="flagCampiComuni()"><%=BilanciamentoCircostanze%>
				</select>
			 </td>		
			<%
			}
			else if(!CiReato.getCodBilanciamentoCircostanze().equals("-")){%>
			 <td class="l">
				<font class="campo">
					<%=StringUtils.toStringJSP(CiReato.getDescrBilanciamentoCircostanze())%>
				</font>&nbsp;
				<input type="hidden" name="CodBilanciamentoCircostanze" value="<%=CiReato.getCodBilanciamentoCircostanze()%>">
			<%}%>
			</td>
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
			if(CiReato==null){
				%>
				<td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanza.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S' onClick="flagCampiComuni()">
		        </td>
				<%
			}
			else if(CiReato.getFlagGiudizioAbbreviato() != null 
					&& CiReato.getFlagGiudizioAbbreviato().equals("S")){
			%>
			<td class="l">
				<font class="campo">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
				</font>&nbsp;
<%
//Federica - a9-rr-078
// <input type="hidden" name="FlagGiudizioAbbreviato" value="<%=CiReato.getFlagGiudizioAbbreviato()%>
<%	//  ">
%>
				<input type="hidden" name="FlagGiudizioAbbreviato" value="V">
			</td>
			<%}else{%>
		        <td class="l">
		          <input type='checkbox' name='<%=ICostantiCircostanza.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S' <%= ( (CiReato.getFlagGiudizioAbbreviato() != null && CiReato.getFlagGiudizioAbbreviato().equals("S") ) ) ? "checked" : ""%> onClick="flagCampiComuni()">
		        </td>
			<%}%>
		</tr>
	</table>
<%}%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone"  onClick="Javascript:return VerifyChiamate(1);" name="Inserisci">
      </td>

<%if(!lTipoFunzione.equals(""))
{%>
      <td colspan="2">
        <input type="submit"  class="bottone"  name="AggAtt" value="Prosegui" onClick="javascript:return VerifyChiamate(2);">        
      </td>
<%}%>
    </tr>
  </table>
  <input type="Hidden" name="cablati" value="">
  <input type="Hidden" value="<%=lCircostanza.getIdCircostanza() %>" name="<%= ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA %>">
  <input type="Hidden" name="Action" value="">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
  <input type="hidden" name="verifyCampiComuni" value="0">

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciCircostanza");
<%
    for(int i=0; i<5; i++)
    {
%>
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_ANNO_FONTE%>","<%=i%>","num");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_NUMERO_FONTE%>","<%=i%>","alphanumeric");

<%	  // **********************************************************************************************
	  // Federica - a9-rr-078
	  // il campo articolo diviene numerico %>
	  frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_ARTICOLO%>","<%=i%>","numeric");
<%	  // fine modifica
	  // ********************************************************************************************** %>

      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_COMMA%>","<%=i%>","alphanumeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_LETTERA%>","<%=i%>","alphanumeric");
      frmvalidator.addValidationWithIdx("<%=ICostantiCircostanza.CAMPO_NUMERO%>","<%=i%>","alphanumeric");
<%
    } // fine for
%>

<%	// *******************************************************************
	// Federica - a9-rr-078
	// aggiunta funzione di controllo dei campi %>
	frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");
<%	// fine modifica
	// *******************************************************************%>

 </script>
</form>
</body>
</html>