<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"    scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="Action"     			scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficioTrib = "";
	String labelUfficioSorv = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficioTrib = "Tribunale per i Minorenni in funzione di Tribunale Sorveglianza";
		labelUfficioSorv = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficioTrib = "Tribunale di Sorveglianza";
		labelUfficioSorv = "Magistrato di Sorveglianza";
	}
%>

<%
// Imposta varibabili per Ordinanza  
String lAction = new String();
lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";

String lFunctionName = new String(); 
lFunctionName = "Emissione Ordinanza Cessazione Misura Sicurezza";

%>

<html>
  <head>
    <title>[S.I.E.S.] - <%=lFunctionName%></title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
    	var desktop;
     	// Chiamata lista Procure
      	function ListaProcure(a_formname,a_fieldname)
      	{
      		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Elenco Tribunali di Sorveglianza", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      	}
        // Chiamata all'elenco degli UDS
        function ListaUDS(a_formname, a_fieldname, a_typename)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename+"&NomeLista="+"Magistrati presso Uffici di Sorveglianza di:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }


      	
    	function Verify()
    	{
    		// Controllo obbligatorietà esiti
      		var lEsiti=document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
      		var ritorno = VerifyCombo(lEsiti,"Esito");
      		if (ritorno == false){
      			return false;
      		}

      		node=document.getElementById("datacessazione");
      		if (node.style.visibility == 'visible') {
	
				// Carica la Data Decorrenza per poi usarla nei controlli  
        		var data_inizio = document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
      			// Controllo validità Data Decorrenza Cessazione
				if (! ControllaData(data_inizio))
				{
					alert('Data Cessazione non valida!');
					return false;
				}

      		}

      		return true;
    	}
    </script>
        <script language="JavaScript">
        function AbilitaCampiEsiti()
        {
     	   var esito = 0;
     	   if (typeof (document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0])=="undefined" ) {
            for (j = 0; j < document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
            {

            if ( document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected )  
            {
           		if (document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1970")
            		{
              		node=document.getElementById("datacessazione");
              		node.style.visibility='visible';
            		}
            		if (document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1970" )
                	{
              		node=document.getElementById("datacessazione");
              		node.style.visibility='hidden';
            		}
            }
        	} // fine ciclo for
     	 } // Fine caso singolo oggetto
          else  	 // Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
          {
       		node=document.getElementById("datacessazione");
      		node.style.visibility='hidden';

      		for (j = 0; j < document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
            {
              for (i = 0; i < document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++ )
              {
                if ( (document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) && 
             		   (document.InserisciOrdinanzaCessazioneMisureSicurezza.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].value == "1970" ))
                {
             		node=document.getElementById("datacessazione");
              		node.style.visibility='visible';
                }
              }
            }
          } // Fine caso più oggetti
     	   
        }	   
        </script>
  
  </head>

  <body class="corpo" >
    <table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    		</td>
      	<td class=LBG>
      		<font class="label">Funzione : </font><font class="campo"><%=lFunctionName%></font>&nbsp;
      	</td>
    	</tr>
    	<tr>
      	<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    	</tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaCessazioneMisureSicurezza">
    <table cellspacing="2" cellpadding="2" width="90%">
   		<tr>
     		<td class="l" width="30%"> Data Emissione</td>
     		<td class="l" width="70%"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   		</tr>
      <tr>
        <td class="l"> Eventuale motivazione</td>
        <td class="l"><Textarea title="Note" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_NATURA_PROVVEDIMENTO%>" cols=88 rows=3></Textarea></td>
      </tr>
    </table>

    <tr> 
    	<td>&nbsp;</td> 
    </tr>
    
    <table cellspacing="2" cellpadding="2" style="width: 90%;">
    
    <tr>
        <td class="Titolo" colspan=6> Specificare esito per ciascun oggetto: </td>
    </tr>

    <tr>
        <td class="l" colspan=2 >Oggetto </td>
        <td class="l" colspan=2 >Esito <font class=ob>(*)</font></td>
    </tr>
    <%
 		// 2008-02-21 commentato poichè come richiesto il controllo è da effettuarsi sul cod contenuto
    //boolean chk_Oggetto = false;
   	for (int i=0; i< tenori.length;i++)
    {
   		// 2008-02-21 commentato poichè come richiesto il controllo è da effettuarsi sul cod contenuto
   	  // controllo per visualizzare i campi di inseriemnto per i giorni da recuperare
   		//if(tenori[i].getCodOggettoTenore().equals("2380") || tenori[i].getCodOggettoTenore().equals("2381") || tenori[i].getCodOggettoTenore().equals("2382"))
   		//	chk_Oggetto = true;
    %>
       <tr>
        <td class="l"colspan=2>
          <input Title="Oggetto" name="<%=ICostantiTenore.CAMPO_DESCR_OGGETTO_TENORE %>" value="<%=tenori[i].getDescrOggettoTenore()%>"  readonly size=60%>
          <input Title="Cod Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_OGGETTO_TENORE %>" value="<%=tenori[i].getCodOggettoTenore()%>" >
          <input Title="Cod Dettaglio Oggetto" type="hidden" name="<%= ICostantiTenore.CAMPO_COD_DETTAGLIO_OGGETTO %>" value="<%=tenori[i].getCodDettaglioOggetto()%>" >
        </td>
          <td class="l"colspan=2>
           <select Title="Cod Esito" name="<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>" onchange="Javascript:return AbilitaCampiEsiti();">
             <%=esiti[i]%>
          </select>
        </td>
      </tr>      
    <%
    }
    %>            
  </table>
    <table cellspacing="2" cellpadding="2">
    <tr>
    	<td>&nbsp;</td>
    </tr>
    <tr>
      <td class="l"><%=labelUfficioTrib%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficioTrib%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>" value="" size=35 >
        <a href="Javascript:ListaProcure('InserisciOrdinanzaCessazioneMisureSicurezza','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFF_TDS_CONCESSO_RIDUZIONE%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
        <tr>
      <td class="l"><%=labelUfficioSorv%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficioSorv%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaCessazioneMisureSicurezza','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
 </table>
 <table cellspacing="2" cellpadding="2" width="90%" id="datacessazione" style="visibility: hidden">
 	<tr> 
 		<td>&nbsp;</td>
 	</tr>
 	 <tr> 
 		<td class="l">In caso di cessazione indicare: </td>
 	</tr>
 	
	<tr>
		<td class="l">Data Cessazione (*) </td>
		<td class="L">
      <input title = "Giorno Data Decorrenza Data Cessazione (*) " value="" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA %>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title = "Mese Data Decorrenza Data Cessazione (*) " value="" type="text" size="2" maxlength="2" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA %>" <%=IWebConstants.UTIL_DATA%>>
			/
			<input title = "Anno Data Decorrenza Data Cessazione (*) " value="" type="text" size="4" maxlength="4" name="<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
	</tr>
 </table>
 		<table cellspacing="2" cellpadding="2" style="width: 90%;">
  		<tr> 
  			<td>&nbsp;</td> 
  		</tr>
    	<tr>
    		<td>
        	<input class="bottone" type="submit" value="Conferma" >
      	</td>
    	</tr>
 		</table>
 
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=contenuto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_COD_TIPO_DECRETO%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoDecreto.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_TIPO_ORDINANZA%>" value="<%=tipo_decreto%>" >
    <input type="HIDDEN" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DATA_EMISSIONE%>" value=<%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%> >
    
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaCessazioneMisureSicurezza");
    // CONTROLLI DATE

		// Data Cessazione
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","lt=31");

		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","gt=1");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","lt=12");

		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","numeric");
		frmvalidator.addValidation("<%= ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","gt=1900");
 
  	frmvalidator.setAddnlValidationFunction("Verify");
  </script>
 </body>
</html>