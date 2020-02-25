<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.circostanza.model.CircostanzaModel"%>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<jsp:useBean id="circostanza" 				scope="request" class="siap.siep.circostanza.model.CircostanzaModel"/>
<jsp:useBean id="TipiFontiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 		scope="request" class="java.lang.String"/>

<%//Federica - a9-rr-078 %>
<jsp:useBean id="TipiCommaQualificante" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="BilanciamentoCircostanze" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
  // SentenzaModel lSentenza = ((FascicoloSiepModel)session.getAttribute("fascicolo")).getSentenza();

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
<%
//****************************************************************************************************
//Federica - a9-rr-078
//aggiunta funzione di controllo dei campi
%>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
<script language="JavaScript">

function GrigettaCampi()
{
	// articoli 442 e 444 non possono essere modificati
	if ((document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value=="442"	
	  && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="25")		
	 || (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value=="444"
	  && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="25"))		
	{
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ANNO_FONTE%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_NUMERO_FONTE%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_LETTERA%>.disabled=true;
		document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_NUMERO%>.disabled=true;
	}    
}

function ControlloObbligatorieta()
{
	// se un qualsiasi campo risulta disabilitato, vuol dire che si sta trattando un articolo
	// 444 o 442, altrimenti non è possibile variare l'articolo impostando 444 o 442
	if(document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.disabled==false)
	{
		if ((document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value=="442"	
		  && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="25")		
		 || (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value=="444"
		  && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="25"))		
		{
			alert("Gli articoli 442 e 444 devono essere inseriti tramite gli appositi flag!");
		    document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.focus();
			return false;
		}
	}
		
	// obbligo articolo e fonte
	if ( 
		(  (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value!="-")
	    && (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value.length==0))
	   || ((document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="-")
	    && (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value.length>0) ))
	{
		alert("Fonte/Articolo devono essere entrambi presenti");
	    document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.focus();
		return false;
	}

	if (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.value=="-"
	 && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_ARTICOLO%>.value.length==0)	
	{
		alert("Fonte/Articolo obbligatori!");
    	document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COD_FONTE%>.focus();
		return false;
	}
	// controllo obbligatorietà comma 
	if (document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>.value==""
	 && document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE%>.value!="-")	
	{
		alert("Comma obbligatorio");
	    document.LoadModificaCircostanza.<%=ICostantiCircostanza.CAMPO_COMMA%>.focus();
		return false;
	}

	return true;
} 
//fine funzione
//****************************************************************************************************
</script>

</head>
<body class="corpo" onload="GrigettaCampi()">
        <table>
        <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
         <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
         CircostanzaModel lCircostanza = new CircostanzaModel();
         String lAzione = new String();
         if( modalita.equals("I") )
         {
           lAzione = "siap.siep.circostanza.action.ActInserisciCircostanza";
%>
        <font class="campo">Inserimento Aggravanti soggettive/Attenuanti</font>
         <%
           }
           else if( modalita.equals("M") )
           {
        	   if(modoSIGE)
        	          lAzione = "siap.sige.circostanza.action.ActModificaCircostanzaSige";
        	   else
        		      lAzione = "siap.siep.circostanza.action.ActModificaCircostanza";
           
        	   lCircostanza = circostanza;
          %>			    
           <font class="campo">Modifica Aggravanti soggettive/Attenuanti</font>
          <%}%>			 
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

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaCircostanza">
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
	//uguale al campo Articolo-qualificante
    //***************************************
%>
      <td class="int">Comma qualificante</td>

      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
    <tr>
      <td class="l">
        <select name="<%= ICostantiCircostanza.CAMPO_COD_FONTE %>" ><%=TipiFontiReato %> </select>
      </td>
      <td class="l">
         <input size="4" maxlength="4" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte()) %>" 
         	type="text" name="<%= ICostantiCircostanza.CAMPO_ANNO_FONTE %>" 
         	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

      </td>
      <td class="l">
         <input size="6" maxlength="6" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte()) %>" 
         	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

      </td>
      <td class="l">
         <input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo()) %>" 
         	type="text" name="<%= ICostantiCircostanza.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

      </td>
      <td class="l">
        <select name="<%= ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma()) %>" 
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
          <%=TipiCommaQualificante%>
        </select>
      </td>

      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera()) %>" 
         	type="text" name="<%= ICostantiCircostanza.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

      </td>
      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero()) %>" 
         	type="text" name="<%= ICostantiCircostanza.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

      </td>
    </tr>
  </table>
   <script language="JavaScript">
	function flagCampiComuni(){
		document.LoadModificaCircostanza.verifyCampiComuni.value = 1;
	}
    </script>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Sentenza di applicazione pena</td>
      <td class="l">
        <input type='checkbox' DISABLED name='<%=ICostantiCircostanza.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' value = 'S' <%= ( (lCircostanza.getFlagSentenzaApplicazPena() != null && lCircostanza.getFlagSentenzaApplicazPena().equals("S")) ) ? "checked" : ""%> >
<% // Federica - a9-rr-078 -       onClick="flagCampiComuni()"> 
%>
      </td>
    </tr>
    <tr>
      <td class="l">Bilanciamento circostanze</td>
      <td class="l">
        <select name="<%=ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE%>" onChange="flagCampiComuni()">
          <%=BilanciamentoCircostanze%>
        </select>
        </td>
       </tr>
    <tr>
 		<td class="l">Annotazioni Bilanciamento circostanze</td>
 		<td>
 			<textarea cols="50" rows="5" name='<%=ICostantiCircostanza.CAMPO_NOTE_BILANCIAMENTO%>' onChange="flagCampiComuni()"><%=(lCircostanza.getNoteBilanciamento()==null)?"":lCircostanza.getNoteBilanciamento()%></textarea> 
 		</td>
 	</tr>
    <tr>
      <td class="l">Giudizio abbreviato</td>
      <td class="l">
        <input type='checkbox' DISABLED name='<%=ICostantiCircostanza.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S'<%= ( (lCircostanza.getFlagGiudizioAbbreviato() != null && lCircostanza.getFlagGiudizioAbbreviato().equals("S")) ) ? "checked" : ""%> >
<% // Federica  - a9-rr-078 -        onClick="flagCampiComuni()">
%>
      </td>
    </tr>
    <tr><td><input type="submit" value="Conferma" class="bottone"></td></tr>
 </table>

    <input value="<%= lCircostanza.getCodTipoCircostanza() %>" type="Hidden" name="<%= ICostantiCircostanza.CAMPO_COD_TIPO_CIRCOSTANZA %>">
    <input value="<%=lCircostanza.getIdCircostanza() %>" type="Hidden" name="<%= ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA %>"  >
    <input type="hidden" name="Action" value="<%= lAzione %>">
    <input type="hidden" name="verifyCampiComuni" value="0">

<%	// *******************************************************************
	// Federica - a9-rr-078
	// aggiunta funzione di controllo dei campi %>

 <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaCircostanza");

    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_ANNO_FONTE%>","num");
    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_NUMERO_FONTE%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_ARTICOLO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_COMMA%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_LETTERA%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanza.CAMPO_NUMERO%>","alphanumeric");
       
    frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");

 </script>

<%	// fine modifica
	// *******************************************************************%>

  </form>
 </body>
</html>