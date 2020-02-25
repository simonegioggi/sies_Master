<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>
<%@ page import="siap.siepe.attivita.action.ICostantiAttivita"%>
<%@ page import="siap.siepe.assistentesociale.action.ICostantiAssistenteSociale"%>
<%@ page import="siap.siepe.attivita.model.AttivitaModel"%>

<jsp:useBean id="ListaAttivita" scope="request" class="java.lang.String"/>
<jsp:useBean id="assistentesociale"  scope="request" class="siap.siepe.assistentesociale.model.AssistenteSocialeModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="attivita" scope="request" class="siap.siepe.attivita.model.AttivitaModel"/>

<%
String azione = "siap.siepe.attivita.action.ActInserisciAttivita";
String NomeFunzione = "Inserimento Attivita";
if (modalita.equalsIgnoreCase("M"))
{
    azione = "siap.siepe.attivita.action.ActModificaAttivita";
    NomeFunzione = "Modifica Attivita";
}
%>
<html>
  <head>
    <title>[S.I.E.S.] - Emissione Decreto di Licenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

 		<script language="JavaScript">
			// Controllo di verifica.
			function Verify()
    	{
      	var ritorno = false;
      	ritorno = controlloDate();
      	return ritorno;
    	}
			// Elenco Assistenti Sociali.
    	function elencoAssistentiSociali(a_formname)
    	{
      	var desktop;
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.assistentesociale.action.ActLoadRicercaAssistenteSocialeLista&formname="+a_formname,
														"Ricerca Assistente Sociale","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    	}

      // Controllo delle date.
      function controlloDate()
      {
         var ret = true;
         var gg0 = FillDM(document.InserisciAttivita.<%=ICostantiAttivita.CAMPO_GIORNO_DATA_INIZIO%>.value);
         var mm0 = FillDM(document.InserisciAttivita.<%=ICostantiAttivita.CAMPO_MESE_DATA_INIZIO%>.value);
         var aa0 = document.InserisciAttivita.<%=ICostantiAttivita.CAMPO_ANNO_DATA_INIZIO%>.value;

         var dataIni = gg0 + "/" + mm0 + "/" + aa0;

         if (dataIni.length < 10 )
         {
            ret = false;
            alert ("Data di inizio mancante");
         }
         else if (ControllaData (dataIni) == false)
         {
            ret = false;
            alert ("Errore nella data : " + dataIni);
         }
         return ret;
      }
      </script>

      <script language="JavaScript">
      var desktop;
      // Esegue la chiamata all'elenco degli assistenti sociali.
      function elencoAssistentiSociali(a_formname)
    	{
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siepe.assistentesociale.action.ActLoadRicercaAssistenteSocialeLista&formname="+a_formname,"Ricerca_Assistente_Sociale","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>

 	</head>
		<body class="corpo" >

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo"><%=NomeFunzione%></font>&nbsp;
      </td>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciAttivita">
    <jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>

<br>
 <table cellspacing="2" cellpadding="2" style="width: 90%;">
  <tr>
    <td class="l">Attività <font class="ob">(*)</font></td>
    <td class="L">
<%
if (modalita.equalsIgnoreCase("M"))
{
%>
<%=attivita.getDescrTipoAttivita()%>
<% } else { %>
      <select title="Attivita"  class=small name="<%=ICostantiAttivita.CAMPO_COD_TIPO_ATTIVITA%>">
        <%=ListaAttivita%>
      </select>
<% } %>
    </td>
  </tr>

  <tr>
    <td class="l">Assistente Sociale </td>
			<td class="L" >
				<input	readonly 	title="IdAssSoc" 		<% if (assistentesociale.getIdAssistenteSociale() != null) { %>	value="<%=assistentesociale.getIdAssistenteSociale()%>" 	<% } %>	type="hidden" name="<%=ICostantiAssistenteSociale.CAMPO_ID_ASSISTENTE_SOCIALE%>">
				<input	readonly  title="CognomeAssSoc" value="<%=StringUtils.toStringJSP(assistentesociale.getCognome())%>"  type="text" name="<%=ICostantiAssistenteSociale.CAMPO_COGNOME%>" maxlength="35" size="25">
				<input	readonly  title="NomeAssSoc"    value="<%=StringUtils.toStringJSP(assistentesociale.getNome())%>"     type="text" name="<%=ICostantiAssistenteSociale.CAMPO_NOME%>"    maxlength="35" size="25">
				&nbsp;<a href="JavaScript:elencoAssistentiSociali('InserisciAttivita');">Seleziona dalla lista <img src="/images/filefolder.gif" border="0">
      </a>
    </td>
  </tr>

  <tr>
      <td class="l">Data inizio (*)</td>
      <td class="L">(gg/mm/aa)
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"dd"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAttivita.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"MM"))%>" type="text" size="2" maxlength="2" name="<%=ICostantiAttivita.CAMPO_MESE_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(attivita.getDataInizio(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%=ICostantiAttivita.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  <tr>
    <td class="l">Note</td>
    <td class="l">
      <Textarea Title="Note"  name="<%= ICostantiAttivita.CAMPO_NOTE %>" cols=88 rows=5><%=StringUtils.toStringJSP( attivita.getNote()) %></textarea>
    </td>
  </tr>


    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" >
      </td>
    </tr>
 </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>" >
  <input type="HIDDEN" name="<%=ICostantiAttivita.CAMPO_ID_ATTIVITA%>" value="<%=attivita.getIdAttivita()%>" >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciAttivita");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_GIORNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_MESE_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_ANNO_DATA_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiAttivita.CAMPO_ANNO_DATA_INIZIO%>","minlen=4","La lunghezza del campo Anno UEPE deve essere di 4 caratteri");
  </script>


 </body>

</html>