<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="contenuto"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"     		scope="request" class="java.util.Date"/>
<jsp:useBean id="fascicoloSiusGP" 			scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="data_fine_misura_dd"   scope="request" class="java.lang.String"/>
<jsp:useBean id="data_fine_misura_MM"   scope="request" class="java.lang.String"/>
<jsp:useBean id="data_fine_misura_yyyy" scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
%>

<%
/* Estrazione della data udienza */
 String data1;
 if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
 else
  data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Indultino </title>
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

      // Se l'esito dell'oggetto Sospensione Condizionata della Pena Detentiva (2245) è di Conferma (1420), il
      // Luogo di Svolgimento della prova è obbligatorio
      var lTenori=document.InserisciOrdinanzaIndultino.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>;
      
      if (typeof (lTenori[0]) =="undefined" ) {
		if (lTenori.value == '2245' && lEsiti.value == '1420')
        {
            if (document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_LUOGO_SVOLGIMENTO_PROVA%>.value == "") {
          	alert ('Luogo di svolgimento della prova obbligatorio!');
            	return false;
            }
         }
      }
      
      for (jTenori = 0; jTenori < lTenori.length; jTenori++) {
          if (lTenori[jTenori].value == '2245')  {
        	  for (jEsiti = 0; jEsiti < lEsiti[jTenori].length ; jEsiti++ )
              {
                
                if ( (lEsiti[jTenori][jEsiti].selected) && (lEsiti[jTenori][jEsiti].value == '1420' ) )
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

  </script>
    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      // Chiamata all'elenco degli UEPE
      function ListaCSSA(a_formname,a_fieldname, a_fieldcode)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
      function updateCkCtrlE() {
				if ( document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked==true ) {
					document.InserisciOrdinanzaIndultino.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked=false;
				}	
			}
      function updateCkCtrlT() {
 				if ( document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[1].checked==true ) {
   				document.InserisciEmissioneDecretoSosp.<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>[0].checked=false;
      	}	
    	}
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
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza L. 2003/207</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaIndultino">
    <table width=35%>
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"> <%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
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
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>">
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
      <td class="l">Magistrato di sorveglianza Competente </td>
      <td class="l">
        <input Title="Magistrato" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP %>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaIndultino','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>');">
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
    <tr>
      <td class="l">Autorità delegata alla vigilanza </td>
      <td class="l">
        <input Title="Servizio terapeutico competente " name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_AUTORITA_VIGILANTE %>" value="" size=35 >
      </td>
    </tr>
   </table>

    <table cellspacing="2" cellpadding="2" width=100%>
     	<tr>
     		<td class="l">
     			Controllo tramite mezzi elettronici 
      		<input value="E" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>"  
      				 	 onClick="javascript:updateCkCtrlE()"/>	
      		&nbsp;&nbsp;&nbsp;&nbsp;
      		Controllo tramite altri strumenti tecnici 
      		<input value="T" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_TIPO_CONTROLLO_ESECUZIONE%>" 
      				 	 onClick="javascript:updateCkCtrlT()"/>	
      	</td>
     	</tr>

  <tr> <td>&nbsp;</td> </tr>
      <tr>
        <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
      </tr>
  <tr> <td>&nbsp;</td> </tr>
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