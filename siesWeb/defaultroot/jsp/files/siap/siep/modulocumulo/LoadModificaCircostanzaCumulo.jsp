<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.CircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiCircostanzaCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" 		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    		scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="circostanza" 				scope="request" class="siap.siep.modulocumulo.model.CircostanzaCumuloModel"/>
<jsp:useBean id="TipiFontiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiCommaQualificante" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="BilanciamentoCircostanze" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 					scope="request" class="java.lang.String"/>

<%
CircostanzaCumuloModel lCircostanza = new CircostanzaCumuloModel();
lCircostanza = circostanza;
%>

<!--  	LoadModificaCircostanzaCumulo	-->
 
<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Circostanze aggravanti soggettive/attenuanti </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

//==========================================================================
//Ritorna alla lista dei Reati/Circostanze Aggravanti/Attenuanti per il Titolo
//==========================================================================
function eseguiFunzione(action)
{
	document.LoadModificaCircostanzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
	document.LoadModificaCircostanzaCumulo.submit();
}

function GrigettaCampi()
{
	// articoli 442 e 444 non possono essere modificati
	if ((document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value=="442"	
	  && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="25")		
	 || (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value=="444"
	  && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="25"))		
	{
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_LETTERA%>.disabled=true;
		document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO%>.disabled=true;
	}    
}

function ControlloObbligatorieta()
{
	// NON è possibile variare l'articolo impostandolo a 444 o 442 con Codice_Fonte = C.P.P. (cod fonte =25)
	if(document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.disabled==false)
	{
		if ((document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value=="442"	
		  && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="25")		
		 || (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value=="444"
		  && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="25"))		
		{
			alert("Gli articoli 442 e 444 devono essere inseriti tramite gli appositi flag!");
		    document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.focus();
			return false;
		}
	}
		
	// obbligo articolo e fonte
	if ( 
		(  (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value!="-")
	    && (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value.length==0))
	   || ((document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="-")
	    && (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value.length>0) ))
	{
		alert("Fonte/Articolo devono essere entrambi presenti");
	    document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.focus();
		return false;
	}

	if (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.value=="-"
	 && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>.value.length==0)	
	{
		alert("Fonte/Articolo obbligatori!");
    	document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COD_FONTE%>.focus();
		return false;
	}
	// controllo obbligatorietà comma 
	if (document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>.value==""
	 && document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE%>.value!="-")	
	{
		alert("Comma obbligatorio");
	    document.LoadModificaCircostanzaCumulo.<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>.focus();
		return false;
	}

	return true;

} // Fine Funzione ControlloObbligatorieta() 

//****************************************************************************************************
</script>

</head>
<body class="corpo" onload="GrigettaCampi()">
  <table>
	<tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      	<font class="campo">Modifica Aggravanti soggettive/Attenuanti relative ad un Titolo Cumulato</font>
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

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadModificaCircostanzaCumulo">
	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActModificaCircostanzaCumulo">
	<input type="Hidden" name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_TIPO_CIRCOSTANZA %>"  value="<%=lCircostanza.getCodTipoCircostanza() %>" >
  	<input type="Hidden" name="<%= ICostantiCircostanzaCumulo.CAMPO_ID_CIRCOSTANZA_CUMULO %>" value="<%=lCircostanza.getIdCircostanzaCumulo() %>" >
  	<input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_FLAG_STATO %>" 			  value="<%=lCircostanza.getFlagStato() %>" >
  	
  	<input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>"  value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"		  value="<%=lCircostanza.getTitIdTitoloCumulato() %>" >
  	
    <input type="hidden" name="verifyCampiComuni" value="0">

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
      <td class="l">
        <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_FONTE %>" ><%=TipiFontiReato %> </select>
      </td>
      <td class="l">
         <input size="4" maxlength="4" value="<%=StringUtils.toStringJSP(lCircostanza.getAnnoFonte()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE %>" 
         	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
            onBlur="javascript:value=FillYear(value)">

      </td>
      <td class="l">
         <input size="6" maxlength="6" value="<%=StringUtils.toStringJSP(lCircostanza.getNumeroFonte()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE %>"
            onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">

      </td>
      <td class="l">
         <input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lCircostanza.getArticolo()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_ARTICOLO %>" 
            onFocus="javascript:textboxSelect(this)">

      </td>
      <td class="l">
        <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getComma()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA %>"
            onFocus="javascript:textboxSelect(this)">

      </td>

      <td class="c">
        <select name="<%= ICostantiCircostanzaCumulo.CAMPO_COMMA_QUALIFICANTE %>">
          <%=TipiCommaQualificante%>
        </select>
      </td>

      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getLettera()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_LETTERA %>"
            onFocus="javascript:textboxSelect(this)">

      </td>
      <td class="l">
         <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lCircostanza.getNumero()) %>" 
         	type="text" name="<%= ICostantiCircostanzaCumulo.CAMPO_NUMERO %>"
            onFocus="javascript:textboxSelect(this)">

      </td>
    </tr>
  </table>

<!--   Fuzione JavaScript	 -->  
<script language="JavaScript">
function flagCampiComuni()
{
	document.LoadModificaCircostanzaCumulo.verifyCampiComuni.value = 1;
}
</script>
<!--  	End Funzione JS	 --> 


  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Sentenza di applicazione pena</td>
      <td class="l">
        <input type='checkbox' DISABLED name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_SENTENZA_APPLICAZ_PENA%>' value = 'S' <%= ( (lCircostanza.getFlagSentenzaApplicazPena() != null && lCircostanza.getFlagSentenzaApplicazPena().equals("S")) ) ? "checked" : ""%> >
      </td>
    </tr>
    <tr>
      <td class="l">Bilanciamento circostanze</td>
      <td class="l">
        <select name="<%=ICostantiCircostanzaCumulo.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE%>" onChange="flagCampiComuni()">
          <%=BilanciamentoCircostanze%>
        </select>
        </td>
    </tr>
    <tr>
 		<td class="l">Annotazioni Bilanciamento circostanze</td>
 		<td>
 			<textarea cols="50" rows="5" name='<%=ICostantiCircostanzaCumulo.CAMPO_NOTE_BILANCIAMENTO%>' onChange="flagCampiComuni()"><%=(lCircostanza.getNoteBilanciamento()==null)?"":lCircostanza.getNoteBilanciamento()%></textarea> 
 		</td>
 	</tr>
    <tr>
      <td class="l">Giudizio abbreviato</td>
      <td class="l">
        <input type='checkbox' DISABLED name='<%=ICostantiCircostanzaCumulo.CAMPO_FLAG_GIUDIZIO_ABBREVIATO%>' value = 'S'<%= ( (lCircostanza.getFlagGiudizioAbbreviato() != null && lCircostanza.getFlagGiudizioAbbreviato().equals("S")) ) ? "checked" : ""%> >
      </td>
    </tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
       	<td class="l">Motivo Inserimento Circostanza nel titolo cumulato</td>
       	<td class="l"><TextArea cols=80 rows=3 name="<%=ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>"><%=StringUtils.toStringJSP(lCircostanza.getMotivoModifica()) %></textarea></td>
   	</tr>
   	<tr><td>&nbsp;</td></tr>
    <tr><td><input type="submit" value="Conferma" class="bottone"></td></tr>
 </table>

 <!-- 	Funzione Javascript ci controllo dati della form	 -->
 <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaCircostanzaCumulo");

    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_ANNO_FONTE%>","num");
    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO_FONTE%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_ARTICOLO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_COMMA%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_LETTERA%>","alphanumeric");
    frmvalidator.addValidation("<%=ICostantiCircostanzaCumulo.CAMPO_NUMERO%>","alphanumeric");
       
    frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");

 </script>
 <!-- 		End Funzione javascript		 -->

  </form>
 </body>
</html>