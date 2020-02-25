<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto" scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione" scope="request" class="java.util.Date"/>
<jsp:useBean id="esecuzionemisurasicurezza" scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />

<jsp:useBean id="naturaMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.lang.String"/>
<jsp:useBean id="misuresicurezza" scope="request" class="java.util.Vector" />

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");

	// MERGE v10: per i maggiorenni la tabella "tableInForma" non deve essere visibile
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	boolean isUffMinor = false;
	if ("TDSM".equals(CodUff) || "UDSM".equals(CodUff))
		isUffMinor = true;
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza Riesame Misura Sicurezza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

  <script language="JavaScript">
    // STUB 21/07/2004 Controllo obbligatorietà esiti.
    function Verify()
    {
      var lEsiti=document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      var ritorno = VerifyCombo(lEsiti,"Esito");

      // Controllo della data decorrenza
      var data_decorrenza = document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>.value;

      if (! ControllaDataPassaVuota(data_decorrenza))
      {
        alert('Data decorrenza non valida! ' + data_decorrenza);
        return  false;
      }
      
     // Controllo della data fine misura
     var data_fine_misura = document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;

     if (! ControllaDataPassaVuota(data_fine_misura))
     {
       alert('Data fine misura non valida! ' + data_fine_misura);
       return  false;
     }
    
     // Controllo della data di proroga
     var data_proroga = document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PROROGA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PROROGA%>.value+'/'+document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA%>.value;

     if (! ControllaDataPassaVuota(data_proroga))
     {
       alert('Data proroga non valida! ' + data_proroga);
       return  false;
     }

     node=document.getElementById("tiponuovamisura");
     if (node.style.visibility == 'visible') {
         if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE%>.value=='-')
         {
             alert('Il campo Tipo Nuova Misura è obbligatorio');
             return false;
         }
   	 }

     node=document.getElementById("tiponuovamisura");
     if (node.style.visibility == 'visible') {
         if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE%>.value=='-')
         {
             alert('Il campo Tipo Nuova Misura è obbligatorio');
             return false;
         }
   	 }

     // Misura di Sicurezza Rideterminata A Seguito Unificazione
	 // controllo obbligatorietà
     nodeMSRidet=document.getElementById("misSicRideterminata");
     if (nodeMSRidet.style.visibility == 'visible') {
         if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA%>.value=='-')
         {
             alert('Il campo Tipo Misura di Sicurezza Rideterminata è obbligatorio');
             return false;
         }
   	 }

     if (nodeMSRidet.style.visibility == 'visible') {
    	 if ( document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO%>.value=='-')
         {
    		if( document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO%>.value!="" ||
    	        document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO%>.value!="" ||
    		    document.InserisciOrdinanzaRiesameMisuraSicurezza.<%= ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO%>.value!="")
            {
    			alert('Il campo Tipo Misura di Sicurezza Rideterminata è obbligatorio');
             	return false;
            }
         }
   	 }
     
     // Controllo della data decorrenza
     nodeDataDecorrenza = document.getElementById("dataDecorrenza");
     if (nodeDataDecorrenza.style.visibility == 'visible') {
   	 var data_decorrenzaMisSic = document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>.value+'/'+InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>.value+'/'+InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>.value;
        if (! ControllaData(data_decorrenzaMisSic) && data_decorrenzaMisSic.length>2)
        {
      		alert('Data Decorrenza per la Misura di Sicurezza non valida');
         	return false;
        }
     }
      	          
     	return ritorno;
     	
    }
  </script >
  <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

  	  // In fase di Emissione Ordinanza di un procedimento di "Riesame Pericolosità
   	  // Sociale (U067)" e oggetto "Unificazione delle misure di sicurezza (art. 209 C.P.)" (2442), 
   	  // prevedere l'iscrizione delle misure di sicurezza rideterminate
      function visualizzaMisSicRideterminate()
      {
	    var nodeMSRid;
	    var visualizza = false;

		//caso singolo oggetto
		if(document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value == "2442")
		{
			visualizza = true;
		}
		// caso più oggetti
		else if(typeof (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.value) == "undefined")
		{
			for (j = 0; j < document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>.length ; j++ )
			{	
				if(document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_OGGETTO_TENORE%>[j].value == "2442" )
				{	
					visualizza = true;
				}
			}
		}
		
			
	    nodeMSRid = document.getElementById('misSicRideterminata');

        if(visualizza){
        	nodeMSRid.style.visibility='visible';
        } else {
        	nodeMSRid.style.visibility='hidden';
        }
      }       
   </script>
    
   <script language="JavaScript">
   function AbilitaCampiEsiti()
   {
	   nodeInForma=document.getElementById('tableInForma');
	   nodeInForma.style.visibility='hidden';
	   var esito = 0;
	   if (typeof (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0])=="undefined" ) {
       for (j = 0; j < document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
       {

       if ( document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected )  
       {
      		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1740")
       		{
         		node=document.getElementById("datarevoca");
         		node.style.visibility='visible';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1740" )
           	{
         		node=document.getElementById("datarevoca");
         		node.style.visibility='hidden';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1743")
       		{
         		node=document.getElementById("prorogamisura");
         		node.style.visibility='visible';
         		if (<%= isUffMinor %>)
         			nodeInForma.style.visibility='visible';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1743")
       		{
         		node=document.getElementById("prorogamisura");
         		node.style.visibility='hidden';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1741" ||
       			//Questa sezione non deve comparire quando l'esito è "Unifica le Misure di Sicurezza"
           		//document.InserisciOrdinanzaRiesameMisuraSicurezza.%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1747" ||
       			document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1960")
       		{
         		node=document.getElementById("nuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("tiponuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("selnuovamisura");
         		node.style.visibility='visible';
         		if (<%= isUffMinor %>)
         			nodeInForma.style.visibility='visible';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1741" &&
				//Questa sezione non deve comparire quando l'esito è "Unifica le Misure di Sicurezza"
           		//document.InserisciOrdinanzaRiesameMisuraSicurezza.%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1747" &&
       			document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1960" )
           	{
         		node=document.getElementById("nuovamisura");
         		node.style.visibility='hidden';
         		node=document.getElementById("tiponuovamisura");
         		node.style.visibility='hidden';
         		node=document.getElementById("selnuovamisura");
         		node.style.visibility='hidden';
       		}


			// Data Decorrenza della Misura di sicurezza
			// viene visualizzata solo in corrispondenza di:
			// inserimento Emissione "Inosservanza delle misure di sicurezza detentive"
			// oggetto "Inosservanza delle Misure di Sicurezza  Detentive (art. 214 c.p.)"
			// ed Esito "Dispone che ricominci a decorrere il periodo minimi della misura"
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '2004':1 } )
       		{
         		nodeDecorrenza = document.getElementById("dataDecorrenza");
         		nodeDecorrenza.style.visibility = 'visible';
       		}
       		if (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value in { '-':1, '2005':1, '2006':1, '2007':1 } )
       		{
         		nodeDecorrenza = document.getElementById("dataDecorrenza");
         		nodeDecorrenza.style.visibility = 'hidden';
       		}   		
       }
   	   } // fine ciclo for
	 } // Fine caso singolo oggetto
     else  	 // Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
     {
  		node=document.getElementById("nuovamisura");
 		node.style.visibility='hidden';
  		node=document.getElementById("datarevoca");
 		node.style.visibility='hidden';
 		node=document.getElementById("tiponuovamisura");
 		node.style.visibility='hidden';
 		node=document.getElementById("selnuovamisura");
 		node.style.visibility='hidden';
 		node=document.getElementById("prorogamisura");
 		node.style.visibility='hidden';
 		
 		 
       for (j = 0; j < document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
       {
         for (i = 0; i < document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++ )
         {
           if ( (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) && 
        		(document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value == "1740" ))
           {
        		node=document.getElementById("datarevoca");
         		node.style.visibility='visible';
           }
           if ( (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) && 
        		( (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1741") ||
        		  //Questa sezione non deve comparire quando l'esito è "Unifica le Misure di Sicurezza"
              	  //(document.InserisciOrdinanzaRiesameMisuraSicurezza.%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1747") ||
        		  (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1960")) )   {
        		node=document.getElementById("nuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("tiponuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("selnuovamisura");
         		node.style.visibility='visible';
         		if (<%= isUffMinor %>)
         			nodeInForma.style.visibility='visible';
           }
           if ( (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) && 
        		   (document.InserisciOrdinanzaRiesameMisuraSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1743") )   {
        		node=document.getElementById("prorogamisura");
         		node.style.visibility='hidden';
         		if (<%= isUffMinor %>)
         			nodeInForma.style.visibility='visible';
           }
         }
       }
     } // Fine caso più oggetti
	   
   }	   
   </script>

 </head>
 <%
  String lAction = new String();
  lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
 %>

  <body class="corpo" onload="visualizzaMisSicRideterminate()">

    <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font> <font class="campo">Emissione Ordinanza Riesame Misura Sicurezza</font>&nbsp;
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaRiesameMisuraSicurezza">
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
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>
    <%
    }
    %>
    </table>
<br>
 <table cellspacing="2" cellpadding="2" width="90%">

 		<tr>
			<td class="l">Ulteriore descrizione della decisione</td>
    	<td class="l"><TEXTAREA title="Ulteriore Descrizione della decisione" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE %>" cols="70" rows="4" ></textarea></td>
		</tr>
		</table>

 <%
    	if (esecuzionemisurasicurezza != null)
    	{
%>
        <input type="hidden" name="<%=ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS%>" value="<%=esecuzionemisurasicurezza.getIdEsecuzioneMisuraSicurezza()%>" >

				<table cellspacing="2" cellpadding="2" width="90%">
					<tr>
        		<td class="Titolo" colspan=6 > Misura di Sicurezza in Esecuzione</td>
    			</tr>

  					<tr>
						<td>
						<font class="label">Tipo</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getCodTipoMisura() != null && esecuzionemisurasicurezza.getDescrTipoMisura() != null) { %>
			    			<input Title="Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getDescrTipoMisura()%>"   readonly size=43%>
							<%} else {%><input Title="Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_COD_TIPO_ESECUZIONE %>" value="-"   readonly size=43%><%}%>
						</td>
						<td>
						<font class="label">Durata: Anni</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumAnniMisura() != null) { %>
			    			<input Title="Anni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumAnniMisura()%>"   readonly size=5%>
							<%} else {%><input Title="Anni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_ESECUZIONE %>" value="0"  readonly size=5%><%}%>
						</td>
						<td>
						<font class="label">Mesi</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumMesiMisura() != null) { %>
			    			<input Title="Mesi" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_MESI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumMesiMisura()%>"   readonly size=5%>
							<%} else {%><input Title="Mesi" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_MESI_ESECUZIONE %>" value="0" readonly size=5%><%}%>
						</td>
						<td>
						<font class="label">Giorni</font>&nbsp;
<%						if (esecuzionemisurasicurezza.getNumGiorniMisura() != null) { %>
			    			<input Title="Giorni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_ESECUZIONE %>" value="<%=esecuzionemisurasicurezza.getNumGiorniMisura()%>"   readonly size=5%>
							<%} else {%><input Title="Giorni" name="<%=ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_ESECUZIONE %>" value="0"   readonly size=5%><%}%>
						</td>
					</tr>
				</table>
<%
		}   // Fine Dati Misura Sicurezza in Esecuzione
%>

 <%
		int indice = -1;
    	Iterator itx = misuresicurezza.iterator();
    	while (itx.hasNext())
    	{
			indice++;
    		MisuraSicurezzaModel lMisuraSicurezza = (MisuraSicurezzaModel)itx.next();
%>
        <input type="hidden" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" value="<%=lMisuraSicurezza.getIdMisuraSicurezza()%>" >
<%
				String lTitoloMisuraSicurezza = ""; 
				if (lMisuraSicurezza.getFasSieIdFascicoloSiep() != null ) 
					lTitoloMisuraSicurezza = "Misura di Sicurezza ereditata da SIEP";
				else
					lTitoloMisuraSicurezza = "Misura di Sicurezza inserita dall' UDS";
%>

				<table cellspacing="2" cellpadding="2" width="90%">
					<tr>
        		<td class="Titolo" colspan=6 > <%=lTitoloMisuraSicurezza%></td>
    			</tr>

  					<tr>
						<td>
						<font class="label">Natura</font>&nbsp;
<%						if (lMisuraSicurezza.getCodNatura() != null && lMisuraSicurezza.getDescrNatura() != null) { %>
			    			<input Title="Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>" value="<%=lMisuraSicurezza.getDescrNatura()%>"   readonly size=15%>
							<%} else {%><input Title="Natura" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_NATURA %>" value="-"   readonly size=15%><%}%>
						</td>
						<td>
						<font class="label">Tipo</font>&nbsp;
<%						if (lMisuraSicurezza.getCodTipo() != null && lMisuraSicurezza.getDescrTipo() != null) { %>
			    			<input Title="Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>" value="<%=lMisuraSicurezza.getDescrTipo()%>"   readonly size=43%>
							<%} else {%><input Title="Tipo" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO %>" value="-"   readonly size=43%><%}%>
						</td>
						<td>
						<font class="label">Durata: Anni</font>&nbsp;
<%						if (lMisuraSicurezza.getNumAnni() != null) { %>
			    			<input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI %>" value="<%=lMisuraSicurezza.getNumAnni()%>"   readonly size=5%>
							<%} else {%><input Title="Anni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_ANNI %>" value="0"  readonly size=5%><%}%>
						</td>
						<td>
						<font class="label">Mesi</font>&nbsp;
<%						if (lMisuraSicurezza.getNumMesi() != null) { %>
			    			<input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI %>" value="<%=lMisuraSicurezza.getNumMesi()%>"   readonly size=5%>
							<%} else {%><input Title="Mesi" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_MESI %>" value="0" readonly size=5%><%}%>
						</td>
						<td>
						<font class="label">Giorni</font>&nbsp;
<%						if (lMisuraSicurezza.getNumGiorni() != null) { %>
			    			<input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI %>" value="<%=lMisuraSicurezza.getNumGiorni()%>"   readonly size=5%>
							<%} else {%><input Title="Giorni" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_NUM_GIORNI %>" value="0"   readonly size=5%><%}%>
						</td>
					</tr>
<%
		}   // Fine elenco Misure Sicurezza
%>  
		        
				
				<table cellspacing="2" cellpadding="2" width="90%" id="nuovamisura" style="visibility: hidden">

  					<tr>
  					<% EsecuzioneMisuraSicurezzaModel lNuovaMisuraSicurezza = new  EsecuzioneMisuraSicurezzaModel(); %>
						<td>
						<font class="label" id="tiponuovamisura" style="visibility: hidden">Nuova Misura</font>&nbsp;
          					<select title="TipoNuovaMisura" name="<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE %>"  id="selnuovamisura" style="visibility: hidden">
          					<%=tipoMisuraSicurezza%>
          					</select>
          				</td>
          			</tr>
          				<tr>
          				<td>
      					<font class="label">Data Decorrenza</font>
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      					</td>
          				<td>
						<font class="label">Durata: Anni</font>&nbsp;
          					<input title="AnniNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumAnniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_NUOVA_MISURA_ESECUZIONE %>"  >
						</td>
						<td>
						<font class="label">Mesi</font>&nbsp;
          					<input title="MesiNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumMesiMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_MESI_NUOVA_MISURA_ESECUZIONE %>"  >
						</td>
						<td>
						<font class="label">Giorni</font>&nbsp;
          					<input title="GiorniNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumGiorniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_NUOVA_MISURA_ESECUZIONE %>"  >
						</td>
						</tr>
						
  				</table>

				<table cellspacing="2" cellpadding="2" width="90%" id="misSicRideterminata" style="visibility: hidden">
					<tr>
	        			<td class="Titolo" colspan=6 >Misura di Sicurezza Rideterminata A Seguito Unificazione</td>
	    			</tr>

  					<tr>
  						<% MisuraSicurezzaModel lMisSicRideterminata = new  MisuraSicurezzaModel(); %>
						<td>
							<font class="label">Natura</font>&nbsp;
          					<select title="NaturaMisuraRideterminata" name="<%= ICostantiEsecuzioneMS.CAMPO_COD_NATURA_MISURA_RIDETERMINATA %>" >
          					<%=naturaMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Tipo</font>&nbsp;
          					<select title="TipoMisuraRideterminata" name="<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA %>" >
          					<%=tipoMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Durata: Anni</font>&nbsp;
          					<input title="Anni Misura Rideterminata" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumAnni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA %>"  >
						</td>
						<td>
							<font class="label">Mesi</font>&nbsp;
          					<input title="Mesi Misura Rideterminata" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumMesi()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA %>"  >
						</td>
						<td>
							<font class="label">Giorni</font>&nbsp;
          					<input title="Giorni Misura Rideterminata" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumGiorni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA %>"  >
						</td>
					</tr>

  					<tr>
						<td>
							<font class="label">Natura</font>&nbsp;
          					<select title="Natura Misura Rideterminata" name="<%= ICostantiEsecuzioneMS.CAMPO_COD_NATURA_MISURA_RIDETERMINATA_TWO %>" >
          					<%=naturaMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Tipo</font>&nbsp;
          					<select title="Tipo Misura Rideterminata" name="<%= ICostantiEsecuzioneMS.CAMPO_COD_TIPO_MISURA_RIDETERMINATA_TWO %>" >
          					<%=tipoMisuraSicurezza%>
          					</select>
						</td>
						<td>
							<font class="label">Durata: Anni</font>&nbsp;
          					<input title="Anni Misura Rideterminata" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumAnni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_ANNI_MISURA_RIDETERMINATA_TWO %>"  >
						</td>
						<td>
							<font class="label">Mesi</font>&nbsp;
          					<input title="Mesi Misura Rideterminata" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumMesi()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_MESI_MISURA_RIDETERMINATA_TWO %>"  >
						</td>
						<td>
							<font class="label">Giorni</font>&nbsp;
          					<input title="GiorniMisuraRideterminataTwo" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lMisSicRideterminata.getNumGiorni()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiEsecuzioneMS.CAMPO_NUM_GIORNI_MISURA_RIDETERMINATA_TWO %>"  >
						</td>
					</tr>

					<tr><td>&nbsp;</td></tr>
					<%
						RedirectTo lRedir = new RedirectTo();
						lRedir.setPage(IWebConstants.PG_MAIN);
					 	lRedir.setParameter("TornaQui", "20");  //Link 20 indica Action Chiamante
					 	lRedir.setAction("siap.sius.misurasicurezza.action.ActRicercaSiusMisuraSicurezza");
				 	%>
					<tr>
					 	<td class="l">
				     		<font class="cRosso">
				     			<a class="cliccabile" href="<%=lRedir%>">
				           			Dettaglio Misura Sicurezza
				         		</a>
				       		</font>
				     	</td> 
				    </tr>

  				</table>
  				  				
				<table cellspacing="2" cellpadding="2" width="90%" id="datarevoca" style="visibility: hidden">

          				<tr>
          				<td>
      					<font class="label">Data Decorrenza Revoca</font>
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc. CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      					</td>
						</tr>
  				</table>
  				
  				<table cellspacing="2" cellpadding="2" width="90%" id="prorogamisura" style="visibility: hidden">

          				<tr>
          				<td>
      					<font class="label">Data Decorrenza</font>
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PROROGA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PROROGA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      					</td>
          				<td>
						<font class="label">Durata: Anni</font>&nbsp;
          					<input title="AnniNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumAnniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_ANNI_PROROGA %>"  >
						</td>
						<td>
						<font class="label">Mesi</font>&nbsp;
          					<input title="MesiNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumMesiMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_MESI_PROROGA %>"  >
						</td>
						<td>
						<font class="label">Giorni</font>&nbsp;
          					<input title="GiorniNuovaMisura" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumGiorniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_PROROGA %>"  >
						</td>
						</tr>
  				</table>
 
				<table cellspacing="2" cellpadding="2" width="90%" id="dataDecorrenza" style="visibility: hidden">
  					<tr>
  						<% MisuraSicurezzaModel lMisuraSicurezza = new  MisuraSicurezzaModel(); %>
				        <td class="L">Data Decorrenza per la Misura di Sicurezza</td>
				        <td class="L">
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezza.getDataDecorrenza(),"dd")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				          /
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezza.getDataDecorrenza(),"MM")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
				          /
				          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMisuraSicurezza.getDataDecorrenza(),"yyyy")) %>" type="text" name="<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
				        </td>
					</tr>
  				</table>
  </table>
  <tr> <td>&nbsp;</td> </tr>
  <table cellspacing="2" cellpadding="2" width="90%">
   
  <tr> <td>&nbsp;</td> </tr>
      <tr>
        <td class="l">Inserimento Prescrizioni <input value="06" type="checkbox" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_CK_PRESCRIZIONI%>"></td>
      </tr>
  <tr>
  <td>    
  <table id="tableInForma" style="visibility:hidden">
        <tr>
        <td class="l" width="50%">Indicare se la misura deve essere eseguita nelle forme della </td>
        <td class="l" width="40%"><input value="1" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>"> Permanenza in casa </td>
        <td width="10%">&nbsp;</td>
        </tr>
        <tr>
        <td width="50%">&nbsp;</td>
        <td width="40%" class="l"><input value="2" type="radio" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_FORMA_MISURA%>">Collocamento in Comunità</td>
        <td width="10%" class="l"><input value="" type="text" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NOME_COMUNITA%>"></td>
        </tr>
        
        </table>     
  </td>
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
    var frmvalidator = new Validator("InserisciOrdinanzaRiesameMisuraSicurezza");

    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_PROROGA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_PROROGA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_PROROGA%>","numeric");

    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_GIORNO_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_MESE_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiSiusMisuraSicurezza.CAMPO_ANNO_DATA_DECORRENZA%>","minlen=4","La lunghezza del campo Anno della Data Decorrenza per la Misura di Sicurezza deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>