<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sius.tenore.model.TenoreModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="tenori" scope="request" class="java.util.Vector"/>
<jsp:useBean id="misuraSicurezza" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="datiSentenza" scope="request" class="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"/>

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
	String lAction = "siap.sius.provvedimento.action.ActModificaProvvedimento";
	String lIdEvento = "";
	String lIdOrdinanza = "";
	String lIdDecreto = "";
	String lIdSentenza = "";
	
	//Estrazione della data minima: data udienza oppure iscrizione fascicolo
	String data1;
	if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio() != null)
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd/MM/yyyy");
	else  if (fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria() != null)
	  	data1 = DateUtils.getDateToString(fascicoloSiusGP.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd/MM/yyyy");
	else
 		data1 = DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataIscrizione(),"dd/MM/yyyy");

	// Modifica Sentenza
	data_emissione = datiSentenza.getEvento().getDataEmissione();
	data_deposito = datiSentenza.getSentenza().getDataDeposito();
	lTenori = datiSentenza.getTenori();
	lIdEvento = datiSentenza.getEvento().getIdEvento().toString();
	lIdSentenza = datiSentenza.getSentenza().getIdDepositoSentenza().toString();
	lIdOrdinanza = "";
	lIdDecreto = "";

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

	      var data_camera = '<%=data1%>';
	      var data_deposito = '<%=data2%>';
	      var data_emissione = document.ModificaSentenza.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModificaSentenza.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModificaSentenza.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	      var data_decorrenza;

	      var nodeDataDec;
		  nodeDataDec = document.getElementById('divDataDecorrenza');
		  if (nodeDataDec.style.visibility == 'visible'){
	    	  data_decorrenza = document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>.value;
	    	  document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
		  }
	      
		  if (! ControllaData(data_emissione))
	      {
	        alert('Data emissione non valida!');
	        ritorno =  false;
	      }
	      // Controllo data di sistema >= Data Emissione .
	      else if( !CompareDate( data_emissione, data_deposito) )
	      {
	        alert("La data di emissione non può essere maggiore della data di deposito o in assenza di essa, della data di Sistema!");
	        ritorno =  false;
	      }
	      // Controllo della data deposito <= data camera di consiglio
	      else if ( data_camera != null && !CompareDate( data_camera, data_emissione) )
	      {
	        alert("La data di emissione non può essere minore della Data Udienza!");
	        ritorno =  false;
	      }
	
	      // Controllo della data decorrenza
	      else if (data_decorrenza != null && data_decorrenza.length>2 && ! ControllaData(data_decorrenza))
		  {
		     alert('Data Decorrenza per la Misura di Sicurezza non valida');
		     return false;
		  }
	
	      return ritorno;
	    }
	
	    function AbilitaDataDecorrenza()
	    {
	 	   var esito = 0;
	 	   for (j = 0; j < document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
	        {
	 		   if ( document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected )  
		        {
	 			    // Data Decorrenza della Misura di sicurezza
		 			// viene visualizzata solo in corrispondenza di:
		 			// inserimento Emissione "Inosservanza delle misure di sicurezza detentive"
		 			// oggetto "Inosservanza delle Misure di Sicurezza  Detentive (art. 214 c.p.)"
		 			// ed Esito "Dispone che ricominci a decorrere il periodo minimi della misura"
	        		if (document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2004':1 } )
	        		{
	        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
	          			nodeDecorrenza.style.visibility='visible';
	          			document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
	        		}
	        		if (document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '-':1, '2005':1, '2006':1, '2007':1 } )
	        		{
	        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
	          			nodeDecorrenza.style.visibility='hidden';
	          			document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='N';
	        		}

	        		// oppure in corrispondenza di:
	        		// inserimento Emissione "Proposta di aggravamento della libertà vigilata per persone in stato di infermità 
	        		// psichica (art.232 c.p.)" e l'oggetto "Proposta di aggravamento della libertà vigilata per persone in stato 
	        		// di infermità psichica (art.232 c.p.)" ed esito "Sostituisce la libertà vigilata con la casa di cura
	        		// e custodia"
	        		if (document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2008':1 } )
	        		{
	        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
	          			nodeDecorrenza.style.visibility='visible';
	          			document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='S';
	        		}
	        		if (document.ModificaSentenza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '-':1, '2009':1, '2010':1, '2011':1, '2012':1, '2013':1 } )
	        		{
	        			nodeDecorrenza=document.getElementById("divDataDecorrenza");
	          			nodeDecorrenza.style.visibility='hidden';
	          			document.ModificaSentenza.<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>.value='N';
	        		}
	    			
		        }
	    	} // fine ciclo for
	    }	
    </script>
  
    
  </head>

  <body class="corpo">

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ModificaSentenza">
      <table cellspacing="2" cellpadding="2"   width=95%>
     	<tr>
        	<td class="Titolo" colspan=8 ><font class="label"> Dati Modificabili </font></td>
    	</tr>
      </table>

      <table cellspacing="2" cellpadding="2" width=95%>
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

	  <table cellspacing="2" cellpadding="2" width=95% id="divDataDecorrenza" style="visibility: hidden">
	     <tr>
	       <td class="l">Data Decorrenza Misura di Sicurezza</td> 
	       <td class="L">
<%
	if(misuraSicurezza.getDataDecorrenza() != null){
%>
        <input value="<%=DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(),"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(),"MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input value="<%=DateUtils.getDateToString(misuraSicurezza.getDataDecorrenza(),"yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%
	} else {
%>
        <input type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" size="2" maxlength="2" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        <input type="text" size="4" maxlength="4" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%		
	}
%>

	        </td>
	      </tr>
	  </table>

         <br>
     <table cellspacing="2" cellpadding="2"   width=95%>
	    <tr>
	        <td class="Titolo" colspan=2 width=50%> Oggetto </td>
	        <td class="Titolo" colspan=2 width=50%> specificare esito per ciascuno oggetto: </td>
	    </tr>
	    <%
	   	for (int i=0; i< lTenori.length;i++)
	    {
	    %>
	       <tr>
	        <td class="l"  colspan=2 ><%=lTenori[i].getDescrOggettoTenore()%>
	           <input Title="ID Tenore" type="hidden" name="<%= ICostantiTenore.CAMPO_ID_TENORE %>" value="<%=lTenori[i].getIdTenore().toString()%>" >
	 
	        </td>
	          <td class="l" colspan=2 width=50%>
	           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaDataDecorrenza();">
	             <%=esiti[i]%>
	          </select>
	        </td>
	      </tr>
	    <%
	    }
	    %>
 
    </table>
    <br> 
			<table>
    		<tr>
      		<td class="label">
        		<input class="bottone" type="submit" name="Conferma" value="Conferma">
        	</td>
      	</tr>
    	</table>
    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC%>" value="<%=lIdOrdinanza%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lIdEvento%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO%>" value="<%=lIdDecreto%>" >
    <input type="HIDDEN" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_DATA_DECORRENZA%>" value="" >
  	<input type="HIDDEN" name="<%=ICostantiDepositoSentenza.CAMPO_ID_DEPOSITO_SENTENZA%>" value="<%=lIdSentenza%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("ModificaSentenza");

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
   frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  
   //Chiama la funzione di Verify().
   frmvalidator.setAddnlValidationFunction("Verify");
 
  </script>

  </body>

</html>