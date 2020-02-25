<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.reatopredisposto.model.ReatoPredispostoModel"%>
<%@ page import="siap.siep.reatopredisposto.action.ICostantiReatoPredisposto"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<jsp:useBean id="reatopredisposto" scope="request" class="siap.siep.reatopredisposto.model.ReatoPredispostoModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Predisposizione Reati </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        ReatoPredispostoModel lReato = new ReatoPredispostoModel();
        String lAzione = new String();
		int nRow = 0;        
				
        if( modalita.equals("I") )
        {
          lAzione = "siap.siep.reatopredisposto.action.ActInserisciReatoPredisposto";
		  nRow = 5;
%>
          <font class="campo">Inserimento Reato Predisposto</font>
<%
        }
        else if( modalita.equals("M") )
        {
          //lAzione = "siap.siep.reatopredisposto.action.ActModificaReatoPredisposto";
          lAzione = "siap.siep.reatopredisposto.action.ActModificaReatoPredisposto";
          lReato = reatopredisposto;
		  nRow = 1;          
%>
          <font class="campo">Modifica Reato Predisposto</font>
<%
        }
%>
      </td>
    </tr>
  </table>
  <br>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciReato">
  <table cellspacing=2 cellpadding=2>
    <td class="l">Nome Elemento</td>
    <td class="c">
      <input size=10 maxlength=100 title="Nome Elemento" value="<%=StringUtils.toStringJSP(lReato.getNomeElemento()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_NOME_ELEMENTO %>" 
      <%if (modalita.equals("M")) { %> 
      READONLY
      <%}%> >
    </td>
  </table>
  <br>
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo" colspan=9>Reato</td></tr>
  </table>  
<%
	if( modalita.equals("I") ) {   
%>
  <table cellspacing=2 cellpadding=2>
  <tr>
    <td class="r"><strong>110 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="01-110"></td>
    <td class="r"><strong>56 CP</strong>&nbsp;<input type="checkbox" name="cablati2" value="01-56"></td>
    <td class="r">
      <strong>81 CP</strong>&nbsp;
      <strong>C1</strong>&nbsp;<input type="checkbox" name="cablati2" value="01-81-1">
      <strong>C2</strong>&nbsp;<input type="checkbox" name="cablati2" value="01-81-2">
    </td>
  </tr>
  </table>
<%
	}
%>  
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.qualificante</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
    </tr>
<%
    for(int i=0; i<nRow; i++)
    {
%>
    <tr>
      <td class="c">
        <select name="<%= ICostantiReatoPredisposto.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size=4 maxlength=4 title="Anno Fonte" value="<%=StringUtils.toStringJSP(lReato.getAnnoFonte()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_ANNO_FONTE %>">
      </td>
      <td class="c">
        <input size=6 maxlength=6 title="Numero Fonte" value="<%=StringUtils.toStringJSP(lReato.getNumeroFonte()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="c">
        <input size=5 maxlength=5 title="Articolo Fonte" value="<%=StringUtils.toStringJSP(lReato.getArticolo()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_ARTICOLO %>">
      </td>
      <td class="c">
        <select name="<%= ICostantiReatoPredisposto.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
        <strong>C</strong>
        <input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(lReato.getComma())%>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_COMMA %>">
      </td>
      <td class="c">
        <strong>L</strong>
        <input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(lReato.getLettera()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_LETTERA %>">
      </td>
      <td class="c">
       <strong>N</strong>
       <input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(lReato.getNumero()) %>" type="text" name="<%= ICostantiReatoPredisposto.CAMPO_NUMERO %>">
      </td>
    </tr>
<%
   }
%>
</table>
<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
				<td class="l">Note Elemento</td>
				<td class="l"><TextArea 
		      <%if (lReato.getProgrNorma() != null && lReato.getProgrNorma().intValue() != 1) { %> 
				READONLY="readonly"
		      <%}%> 
		      cols=80 rows=5 name="<%= ICostantiReatoPredisposto.CAMPO_NOTE_ELEMENTO %>"><%=StringUtils.toStringJSP(lReato.getNoteElemento()) %></textarea>
		      </td>
		</tr>
	</table>
<br>
	<table>
    <tr>
      <td colspan=2>
        <input type=submit value="Conferma" class=bottone>
      </td>
		</tr>
</table>
  <input type="HIDDEN" name=<%= ICostantiReatoPredisposto.CAMPO_ID_REATO_PREDISPOSTO%> value="<%=lReato.getIdReatoPredisposto()%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciReato");

<%
  for(int i=0;i<nRow;i++)
  {
%>
   
   var nRow = <%=nRow%>;
   	
   frmvalidator.addValidation("<%= ICostantiReatoPredisposto.CAMPO_NOME_ELEMENTO %>","req","Il Nome Elemento è obbligatorio");
   frmvalidator.addValidation("<%= ICostantiReatoPredisposto.CAMPO_NOME_ELEMENTO %>","alphanumeric");
	
	if (nRow == 1) {

	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","maxlength=100");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","num");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","minlength=4");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_ARTICOLO%>","alphanumeric");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_NUMERO_FONTE%>","alphanumeric");
	
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_COMMA%>","alphanumeric");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_LETTERA%>","alphanumeric");
	   frmvalidator.addValidation("<%=ICostantiReatoPredisposto.CAMPO_NUMERO%>","alphanumeric");
	
	}   
	else {
		
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","<%=i%>","maxlength=100");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","<%=i%>","num");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_ANNO_FONTE%>","<%=i%>","minlength=4");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_ARTICOLO%>","<%=i%>","alphanumeric");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_NUMERO_FONTE%>","<%=i%>","alphanumeric");
	
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_COMMA%>","<%=i%>","alphanumeric");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_LETTERA%>","<%=i%>","alphanumeric");
	   frmvalidator.addValidationWithIdx("<%=ICostantiReatoPredisposto.CAMPO_NUMERO%>","<%=i%>","alphanumeric");
	}
<%
  }
%>
 </script>
</body>
</html>