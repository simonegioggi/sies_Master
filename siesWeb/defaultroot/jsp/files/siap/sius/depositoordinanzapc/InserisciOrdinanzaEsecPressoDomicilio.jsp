<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"             scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"          scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"        scope="request" class="java.util.Date"/>
<jsp:useBean id="fascicoloSiusGP"       scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="data_fine_misura_dd"   scope="request" class="java.lang.String"/>
<jsp:useBean id="data_fine_misura_MM"   scope="request" class="java.lang.String"/>
<jsp:useBean id="data_fine_misura_yyyy" scope="request" class="java.lang.String"/>

<%
TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
String[] esiti  = (String[])request.getAttribute("esiti");

UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
UfficioModel lUffMod = lUteMod.getUfficioUtente();
String CodUff = new String(lUffMod.getCodTipoUfficio());
String labelUfficio = "";
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")){
 	labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
} else {
	labelUfficio = "Magistrato di Sorveglianza";
}

/* Estrazione della data udienza */
String data1;
if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd/MM/yyyy");
else
 	data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(), "dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Esecuzione presso Domicilio</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  	<script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaIndultino.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      if (!VerifyCombo(lEsiti,"Esito") )
        return false;

      // Se l'esito dell'oggetto Esecuzione pena presso domicilio (2630) è di Conferma (1920), il
      // Luogo di Svolgimento della prova è obbligatorio
      var lTenori=document.InserisciOrdinanzaIndultino.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>;
      
      if (typeof (lTenori[0]) =="undefined" ) {
      if (lTenori.value == '2630' && lEsiti.value == '1920')
          {
              if (document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
              alert ('Luogo di svolgimento della prova obbligatorio!');
                return false;
              }
           }
        }

      for (jTenori = 0; jTenori < lTenori.length; jTenori++) {
          if (lTenori[jTenori].value == '2630')  {
            for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++ )
              {
                
                if ( (lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value == '1920' ) )
                {
                  if (document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
                  alert ('Luogo di svolgimento della prova obbligatorio!');
                    return false;
                  }
                }
              }
                  
          }  
       }
      
      var ritorno = true;
      var data_camera = '<%=data1%>';

      // Controllo della data termine misura.
      var data_termine = document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
      if (data_termine.length > 2 )
      {
       if (! ControllaData(data_termine))
       {
        alert('Data Termine Misura non valida!');
        ritorno =  false;
       }
       else if ( !CompareDate( data_camera, data_termine) )
       {
        alert("Data Termine Misura non può precedere " + data_camera + " !");
        ritorno =  false;
       }
      }
      return ritorno;
    }

      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname, a_fieldname, a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Chiamata all'elenco degli UEPE
      function ListaCSSA(a_formname,a_fieldname, a_fieldcode)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // MEV10-s3: aggiunta funzione per recuperare la lista selezionata
      // Chiamata all'elenco degli USSM
      function ListaUSSM (a_formname,a_fieldname) {
      	  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaUSSM&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      //20140603 - P.M. ( SIUS - implemntazione per il D.L. 146 )
      function updateCkCtrlE() {
        if ( document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
          document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
        } 
      }
      
      function updateCkCtrlT() {
        if ( document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
          document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
        } 
      }

<%
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>
	<%-- MEV63: cambiata gestione in caso di inserimento di più oggetti (ramo else) --%>
	function enableForma() {
	    var lEsiti = document.InserisciOrdinanzaIndultino.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
	    var node = document.getElementById('tableInForma');
	    node.style.display = 'none';
	    var len = <%=tenori.length%>;
		if (len == 1) {
			var selectedValue = lEsiti.options[lEsiti.selectedIndex].value;
	    	if (selectedValue in { '1920':1 }) {	  
				    node.style.display = 'block';
				    return;
	        }
		} else {
			for (var x = 0; x < len; x++) {
		        for (var jEsiti = 0; jEsiti < lEsiti.length ; jEsiti++) {
		        	var item = lEsiti[x];
		        	var selectedValue = item.options[item.selectedIndex].value;
		        	if (selectedValue in { '1920':1 }) {	  
		  			    node.style.display = 'block';
		  			    return;
		            }
		        }
			}
		}
	}
<%
}
%>
	</script>
 	</head>

<%
String lAction = new String();
lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
%>

  	<body class="corpo" >
    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Esecuzione presso Domicilio</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaIndultino">
    <table width=35%>
   <tr>
     <td class="l" width==30%> Data Emissione</td>
     <td class="l" width==70%> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
    </table>

    <tr> <td>&nbsp;</td> </tr>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">
    <tr>
        <td class="Titolo" colspan=6 > Specificare esito per ciascun oggetto: </td>
    </tr>
    <tr>
        <td class="l" colspan=2 > Oggetto </td>
        <td class="l" colspan=2 > Esito </td>
    </tr>
    <%
   for (int i=0; i< tenori.length;i++)
    {
    %>
       <tr>
        <td class="l"colspan=2 >
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
			</td>
          	<td class="l"colspan=2 >
          	<%-- MEV63: aggiunta chiamata a funzione per i minori --%>
<%
if (CodUff.equals("TDSM") || CodUff.equals("UDSM")) {
%>
				<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onChange="enableForma();">
<%
} else {
%>
				<select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
<%
}
%>
             		<%=esiti[i]%>
          		</select>
        	</td>
		</tr>
<%
}
%>
	</table>
	<br>
	<table cellspacing="2" cellpadding="2" style="width: 90%;">

    <tr>
      <td class="l"> Esistenza condizioni ostative </td>
      <td class="l">
         <input value="S" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESISTENZA_REATOOSTATIVO%>" > Si   &nbsp; &nbsp;
         <input value="N"  type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESISTENZA_REATOOSTATIVO%>" CHECKED> No
       </td>
    </tr>
    <tr>
      <td class="l"> Avvenuta espiazione condanna per reato ostativo</td>
      <td class="l">
         <input value="S" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESPIAZIONE_REATOOSTATIVO%>" > Si   &nbsp; &nbsp;
         <input value="N"  type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FLAG_ESPIAZIONE_REATOOSTATIVO%>" CHECKED> No
      </td>
    </tr>
    <tr>
      <td class="l">Data Termine Misura (gg-mm-aaaa)</td>
      <td class="L">
        <input value="<%=data_fine_misura_dd%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=data_fine_misura_MM%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=data_fine_misura_yyyy%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
    <tr>
      <td class="l">Luogo svolgimento della prova </td>
      <td class="l">
        <input Title="Luogo svolgimento della prova" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA %>" value="" size=35 >
      </td>
    </tr>
    <tr>
   		<td class="l"><%=labelUfficio%> Competente </td>
      	<td class="l">
        	<input Title="<%=labelUfficio%>" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        	<a href="Javascript:ListaUDS('InserisciOrdinanzaIndultino','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        	<img src="/images/filefolder.gif" border=0></a>
      	</td>
    </tr>
    <tr>
      	<td class="l">UEPE Competente </td>
      	<td class="l">
        	<input Title="UEPE Competente" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP%>" value="" size=35 >
        	<a href="Javascript:ListaCSSA('InserisciOrdinanzaIndultino','<%= ICostantiDepositoOrdinanzaPc.CAMPO_COMUNE_CSSA_COMP %>','<%= ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP %>');">
        	<img src="/images/filefolder.gif" border=0></a>
      	</td>
    </tr>
    <%-- MEV10-s3: aggiunto riferimento all'USSM competente --%>
    <%
	if ("UDSM".equalsIgnoreCase(fascicoloSiusGP.getFascicoloSiusModel().getCodTipoUfficio())) {
	%>
	    <tr>
	        <td class="l">USSM Competente </td>
	        <td class="l">
	          	<input Title="USSM Competente" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_UFFICIO_USSM %>" value="" size=35 >
	  			<a href="Javascript:ListaUSSM('InserisciOrdinanzaIndultino','<%= ICostantiDepositoOrdinanzaPc.CAMPO_UFFICIO_USSM %>');">
	            <img src="/images/filefolder.gif" border=0> </a>        
			</td>
	    </tr>
    <% } %>
    <tr>
      <td class="l">Autorità delegata alla vigilanza </td>
      <td class="l">
        <input Title="Servizio terapeutico competente " name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_AUTORITA_VIGILANTE %>" value="" size=35 >
      </td>
    </tr>
		<tr>
      		<td class="l" colspan="2">
        		Controllo tramite mezzi elettronici 
        		<input value="E" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>"  
           			onClick="javascript:updateCkCtrlE()"/> 
        		&nbsp;&nbsp;&nbsp;
        		Controllo tramite altri strumenti tecnici 
        		<input value="T" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" 
           			onClick="javascript:updateCkCtrlT()"/> 
      		</td>
    	</tr>
  		<tr> <td>&nbsp;</td> </tr>
      	<tr>
        	<td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
      	</tr>
      	<tr><td>&nbsp;</td></tr>
      	<%-- MEV63: aggiunta sezione per minori "esecuzione forma" --%>
   		<table title="tableInForma" id="tableInForma" style="display: none;">
	   		<tr>
	   			<td class="l" colspan="3">Indicare se la misura deve essere eseguita nelle forme della</td>
	   		</tr>
	   		<tr>
	   			<td class="l">
	   				<input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
	   				Permanenza in casa
	   			</td>
	   			<td class="l">
	   				<input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">
	   				Collocamento in Comunità
	   			</td>
	   			<td class="l">
	   				<input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>" size="50">
	   			</td>
	   		</tr>
	   		<tr><td>&nbsp;</td></tr>
	   	</table>
    	<tr>
      		<td>
        		<input class="bottone" type="submit" value="Conferma" >
      		</td>
    	</tr>
	</table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_CSSA_COMP%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaIndultino");
/*    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_DETENZIONE_DOM%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_DETENZIONE_DOM%>","numeric");
 */
   frmvalidator.setAddnlValidationFunction("Verify");
  </script>


 </body>

</html>
