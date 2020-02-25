<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.web.html.Option"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sius.tenore.model.TenoreModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>


<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="datiOrdinanza" scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="depositoDecretoMotivazioni" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"/>
<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>

<% 
	String[] esiti = (String[])request.getAttribute("esiti");
%>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
<%
	// Variabili
	Date data_emissione =null;
	Date data_deposito = null;
	TenoreModel[] lTenori = null;
	String lAction = "siap.sius.depositoordinanzapc.action.ActModificaRimessioneAtti";
	String lIdEvento = "";
	String lIdOrdinanza = "";
	String lIdDecreto = "";



	//Estrazione della data minima: data udienza oppure iscrizione fascicolo
	String data1;
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
	else
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");


	// Modifica Ordinanza Rimessione Atti
	data_emissione = datiOrdinanza.getEvento().getDataEmissione();
	data_deposito = datiOrdinanza.getOrdinanza().getDataDeposito();
	lTenori = datiOrdinanza.getTenori();
	lIdEvento = datiOrdinanza.getEvento().getIdEvento().toString();
	lIdOrdinanza = datiOrdinanza.getOrdinanza().getIdDepositoOrdinanzaPc().toString();
	lIdDecreto = "";
	// Preleva elenco delle autorità destinatarie per la notifica al soggetto.
    Option lOptionAut = new Option();
	String elencoAutorita;
    lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(),75);
    elencoAutorita = lOptionAut.toString();

 // Estrazione della data massima: data di deposito o data di sistema
 String data2;
 if( data_deposito != null)
	  data2 = DateUtils.getDateToString(data_deposito,"dd/MM/yyyy");
 else
  	  data2 = DateUtils.getSysDate("dd/MM/yyyy");
%>
 <html>
 
  <head>
    <script language="JavaScript">
    function Verify()
    {

     return VerificaDate();
    }
    </script>
    
    <script language="JavaScript">
    function VerificaDate()
    {
        var ritorno = true;
        //alert("VerificaDate");
        var data_camera = '<%=data1%>';
        var data_deposito = '<%=data2%>';
        var data_emissione = document.ModificaRimessioneAtti.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaRimessioneAtti.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaRimessioneAtti.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
 
      // Controllo della data emissione.
       // alert("data emissione ->" + data_emissione);

     if (! ControllaData(data_emissione))
      {
        alert('Data emissione non valida!');
        ritorno =  false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_emissione, data_deposito) )
      {
        alert("La data di emissione non può essere maggiore della Data di Sistema!");
        ritorno =  false;
      }
      // Controllo della data deposito <= data camera di consiglio
     //alert("data_camera ->" + data_camera);
      else if ( !CompareDate( data_camera, data_emissione) )
      {
        alert("La data di emissione non può essere minore della Data Udienza!");
        ritorno =  false;
      }
     return ritorno;
    }
    </script>
    
    <script language="JavaScript">
      var desktop;
     // Chiamata funzione lista dei comuni.
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
    
  </head>

  <body class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaRimessioneAtti">
    <table cellspacing="2" cellpadding="2"   width=95%>
     <tr >
        <td class="Titolo" colspan=8 ><font class="label"> Dati Modificabili </font></td>
    </tr>
    </table>
    

      <table cellspacing="2" cellpadding="2"   width=95%>
    <tr>
      <td class="l">Data Emissione<font class="ob"> (*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getDateToString(data_emissione,"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(data_emissione,"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(data_emissione,"yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
     </table>
    
    <br>
    <table cellspacing="2" cellpadding="2"   width=95%>
    <tr>
        <td class="Titolo" colspan=2 width=50%> Oggetto </td>
    </tr>
    <%
   for (int i=0; i< lTenori.length;i++)
    {
    %>
       <tr>
        <td class="l"  colspan=2 ><%=lTenori[i].getDescrOggettoTenore()%>
           <input Title="ID Tenore" type="hidden" name="<%= ICostantiTenore.CAMPO_ID_TENORE %>" value="<%=lTenori[i].getIdTenore().toString()%>" >
        </td>
      </tr>
    <%
    }
    String textMotivazioni ="";
    if (datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento() != null)
    	textMotivazioni = datiOrdinanza.getOrdinanza().getCodNaturaProvvedimento();
    %>
    <tr>
	<td class="l">Motivazioni</td>
    <td class="l"><TEXTAREA title="Motivazioni" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO %>" 
    			cols="88" rows="4" ><%=textMotivazioni%></textarea></td>
  </tr>
  
  </table>
  <table cellspacing="1" cellpadding="1" > 
   <tr>
    <td class="l" >Dispone la sospensione del procedimento per rimessione degli atti:</td>
   </tr>
   </table>
   <table cellspacing="2" cellpadding="2">
   <%
   String filtroRemissioneAttiAltro = "";
   String checkCorteCostituzionale = "";
   String checkCorteEuropea = "";
   String checkAltro = "";
   
   if (datiOrdinanza.getOrdinanza().getOggettoProcedimento() != null) {
	   if (datiOrdinanza.getOrdinanza().getOggettoProcedimento().compareTo("alla Corte Costituzionale per giudizio di legittimità costituzionale") == 0)
		   checkCorteCostituzionale = "checked";
	   else if (datiOrdinanza.getOrdinanza().getOggettoProcedimento().compareTo("alla Corte Giustizia Europea per giudizio di legittimità in materia di interpretazione trattati internazionali") == 0)
		   checkCorteEuropea = "checked";
	   else {
		   checkAltro = "checked";
		   filtroRemissioneAttiAltro = datiOrdinanza.getOrdinanza().getOggettoProcedimento().replaceFirst("a ", "");
	   }
   
   }
   
   %>
	<tr><td class="c"><input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="cortecostituzionale" <%=checkCorteCostituzionale%>  ></td><td class="l">alla Corte Costituzionale per giudizio di legittimità costituzionale </td></tr>
	<tr><td class="c"><input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="corteeuropea" <%=checkCorteEuropea%> ></td><td class="l">alla Corte Giustizia Europea per giudizio di legittimità in materia di interpretazione trattati internazionali </td></tr>
	<tr><td class="c"> <input type="radio" name="<%= ICostantiDepositoOrdinanzaPc.FILTRO_RIMESSIONE %>" value="altro" <%=checkAltro%> ></td><td class="l">a 
		<input name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ALTRO_RIMESSIONI %>" value = "<%=filtroRemissioneAttiAltro%>" size="88" maxlength="88" > </td></tr>
   </table>
   <table cellspacing="2" cellpadding="2">
   <tr>
    <td class="l" colspan=3 >Autorità Destinatarie Notifica</td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Presidenza Consiglio dei Ministri </td>
        <td class="l"> <input type="checkbox" name="PresidenzaConsiglioDeiMinistri">
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Corte Giustizia Europea  </td>
        <td class="l"> <input type="checkbox" name="CorteGiustiziaEuropea">
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Corte Costituzionale </td>
        <td class="l"> <input type="checkbox" name="CorteCostituzionale">
      </td>
   </tr>
   <tr>
      <td class="l" width="25%"> </td>
      <td class="l" colspan=1>
        Presidente Giunta Regionale </td>
        <td class="l"> <input title="Regione" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_PRESIDENZA_GIUNTA%>" value="" type="text" maxlength="40" size="40">
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=2>
        Per la notifica al Soggetto:
      </td>
   </tr>
   <tr>
   		<td class="l" colspan=1> </td>
        <td class="l" colspan=2>Autorità Destinazione 
           <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
            <%= elencoAutorita %>
          </select>
        </td>
            </tr>
    <tr>
    	<td> </td>
        <td class="l" colspan=2>Sede  
             <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaComuni('LoadModificaRimessioneAtti','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
     </tr>
     <tr>
     		<td> </td>
            <td class="l" colspan=2>Indirizzo         
              <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="60" size="60">
            </td>
     </tr>
   <tr>
    <td class="l" colspan=3>Uffici Destinatari Comunicazione</td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
         Presidente del Senato </td>
         <td class="l"><input type="checkbox" name="PresidenteSenato">
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
        Presidente della Camera dei Deputati </td>
        <td class="l"><input type="checkbox" name="PresidenteCamera">
      </td>
   </tr>
   <tr>
      <td> </td>
      <td class="l" colspan=1>
        Presidente Consiglio dei Ministri </td>
        <td class="l"><input type="checkbox" name="PresidenteConsigliMinistri">
      </td>
   </tr>
   <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verifica();">
    </td>
  </tr>

  </table>
    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=lIdOrdinanza%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lIdEvento%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=lIdDecreto%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("ModificaRimessioneAtti");

   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno  della Data Emissione è obbligatorio");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese  della Data Emissione è obbligatorio");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=13");

   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno della Data Emissione è obbligatorio");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

     //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
 
  </script>

  </body>

</html>