<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<jsp:useBean id="TipiReato" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiPeneDetentive" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiCommaQualificante" scope="request" class="java.lang.String"/>
<jsp:useBean id="Valute" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       	scope="request" class="java.lang.String"/>
<jsp:useBean id="idReato"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

<html>
<head>

<%
 	// Gestione Reato SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;
%>

<title>[S.I.E.S.] - Gestione Reato </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<%
//****************************************************************************************************
//Federica - a9-rr-078
//aggiunto controllo delle date 
%>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">

function VerifyChiamate(id)
{
 if(id==1)
  {
	 <%  if (modoSIGE) {%>
      
          document.LoadInserisciReato.<%=IWebConstants.ACTION_FIELD%>.value="siap.sige.reato.action.ActInserisciUlterioriReatiSige";
     <% }     else      { %>
          document.LoadInserisciReato.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.reato.action.ActInserisciUlterioriReati";
      <% }          %>
  }
  else if(id==2)
  {
          document.LoadInserisciReato.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.reato.action.ActLoadInserisciPenaReato";
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

			// controllo obbligatorietà comma 
			if (document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA%>[ind].value==""
			 && document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>[ind].value!="-")	
			{
				alert("Comma obbligatorio");
			    document.LoadInserisciReato.<%=ICostantiReato.CAMPO_COMMA%>[ind].focus();
				return false;
			}

		} // fine for
	
		return true;
	
	} // fine funzione
	//****************************************************************************************************

</script>

</head>
  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Inserimento Ulteriori Norme</font>
      </td>
    </tr>
  </table>
<%
	ReatoModel lReato = new ReatoModel();
	if (!modoSIGE) 
	{%>
		<br>
			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
		<br>
<%} else {%>
		<br>
			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
			<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
		<br>
<%}%>

  	<jsp:include page="/jsp/files/siap/siep/reato/DettaglioReatoAssociato.jsp"/>
  <br>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciReato">
  <table cellspacing="2" cellpadding="2" width="95%">
    <tr><td class="Titolo" colspan="9">Capo di Imputazione</td></tr>
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
	//uguale al campo Articolo-qualificante
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
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size="4" maxlength="4" title="Anno Fonte" type="text" 
               name="<%= ICostantiReato.CAMPO_ANNO_FONTE %>"
           	   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
       </td>

      <td class="c">
        <input size="6" maxlength="6" title="Numero Fonte" type="text" 
        	   name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>" 
           	   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <input size="5" maxlength="5" title="Articolo Fonte" type="text" 
               name="<%= ICostantiReato.CAMPO_ARTICOLO %>">
      </td>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
        <input size="10" maxlength="10" title="Comma" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>">
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

      <td class="c">
        <input size="2" maxlength="2" title="Lettera" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>">
      </td>
      <td class="c">
       <input size="2" maxlength="2" title="Numero" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>">
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
      <strong>N1</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N1">
      <strong>N2</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N2">
      <strong>N3</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N3">
      <strong>N4</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N4">
      <strong>N5</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N5">
      <strong>N6</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N6">
      <strong>N7</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N7">
      <strong>N8</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N8">
      <strong>N9</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N9">
      <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N10">
      <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="61 01 N11">
    </td>
  </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="r"><strong>56 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="56 01"></td>
    <td class="r">
      <strong>81 CP</strong>&nbsp;
      <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati" value="81 01 C1">
      <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati" value="81 01 C2">
    </td>
  </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
  <tr>
    <td class="r"><strong>110 CP</strong>&nbsp;<input type="checkbox" name="cablati" value="110 01"></td>
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
      <strong>N1</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N1">
      <strong>N2</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N2">
      <strong>N3</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N3">
      <strong>N4</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N4">
      <strong>N5</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N5">
      <strong>N6</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N6">
      <strong>N7</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N7">
      <strong>N8</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N8">
      <strong>N9</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N9">
      <strong>N10</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N10">
      <strong>N11</strong>&nbsp;<input type="checkbox" name="cablati" value="625 01 N11">
    </td>
  </tr>
  </table>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone"  onClick="Javascript:return VerifyChiamate(1);" name="Inserisci">
      </td>

<%if(!lTipoFunzione.equals(""))
{%>
      <td colspan="2">
        <input type="submit"  class="bottone"  name="Pena" value="Pena Reato" onClick="javascript:return VerifyChiamate(2);">
      </td>
<%}%>
		</tr>
</table>
  <input type="HIDDEN" name="cablati" 							value=""> 
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" 	value="">
  <input type="HIDDEN" name="lTipoFunzione" 					value="<%=lTipoFunzione%>">
  <input type="HIDDEN" name="<%=ICostantiReato.CAMPO_ID_REATO%>" value="<%=idReato%>">
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" 	value="<%=TornaQui%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciReato");

<%
  for(int i=0;i<5;i++)
  {
%>
   frmvalidator.addValidationWithIdx("<%=ICostantiReato.CAMPO_ANNO_FONTE%>","<%=i%>","numeric");
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
	// *******************************************************************
	// Federica - a9-rr-078
	// aggiunta funzione di controllo dei campi 
		frmvalidator.setAddnlValidationFunction("ControlloObbligatorieta");
	// fine modifica
	// *******************************************************************
	
 </script>
</body>
</html>