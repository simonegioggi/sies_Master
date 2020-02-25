<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.html.Option"%>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>  
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS"%>
<%@ page import="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="contenuto"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo_decreto"		scope="request" class="java.lang.String"/>
<jsp:useBean id="data_emissione"  scope="request" class="java.util.Date"/>
<jsp:useBean id="esecuzionemisurasicurezza" 	scope="request" class="siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel" />
<jsp:useBean id="tipoMisuraSicurezza" scope="request" class="java.lang.String"/>

<%
	TenoreModel[] tenori = (TenoreModel[])request.getAttribute("tenori");
	String[] esiti	= (String[])request.getAttribute("esiti");
	
	UtenteModel lUteMod = (UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	UfficioModel lUffMod = lUteMod.getUfficioUtente();
	String CodUff = new String(lUffMod.getCodTipoUfficio());
	String labelUfficio = "";
	if(CodUff.equals("TDSM") || CodUff.equals("UDSM")){
		labelUfficio = "Ufficio di Sorveglianza presso il Tribunale per Minorenni";
	} else {
		labelUfficio = "Ufficio di Sorveglianza";
	}
%>

<html>
  <head>
    <title>[S.I.E.S.] - Emissione Ordinanza per Inosservanza Obblighi Misura Sicurezza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/verifyCombo.js"></script>

    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname, a_fieldname, a_typename)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename+"&NomeLista="+"Magistrati presso Uffici di Sorveglianza di:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

    </script>

    <script language="JavaScript">

    function Verify() {
    	// Controllo obbligatorietà esiti
      	
    	var lEsiti=document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
    	var ritorno = VerifyCombo(lEsiti,"Esito");
      	
      	// Controllo della data decorrenza
        var data_decorrenza = document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
        
        if (! controllaDataDecorrenza()) {
            alert('Data decorrenza non valida! ' + data_decorrenza);
            return  false;
        }
        
        return ritorno;
    }
    
    function controllaDataDecorrenza () {
    	var data_decorrenza = document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value+'/'+document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value;
    	var ret = ControllaData(data_decorrenza);
    	if (ret == true)
    		return true;
    	
    	var lEsiti=document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>;
    	var selEsito = lEsiti.options[lEsiti.selectedIndex].value;
    	if (selEsito =="2754" || selEsito =="2768") {
      	  if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value != "" || 
      		  document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value   != "" || 
      		  document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value != "")
      	      return false;
        }
    	return true;
    }
    </script>
       <script language="JavaScript">
   function AbilitaCampiEsiti()
   {
	   var esito = 0;
	   if (typeof (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[0][0])=="undefined" ) {
       for (j = 0; j < document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
       {

       if ( document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].selected )  
       {
       		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1730" ||
       				document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1731" ||
       				document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1732" ||
       				document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1733" ||
       				document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="2754" ||
       				document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="2768" )
       		{
         		node=document.getElementById("nuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("tiponuovamisura");
         		node.style.visibility='visible';
         		node=document.getElementById("selnuovamisura");
         		node.style.visibility='visible';
         		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1730") {
         			node.value="Colonia Agricola";
             		node=document.getElementById("codselnuovamisura");
             		node.value="03";
         		}
         		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1731") {
         			node.value="Casa di Lavoro";
             		node=document.getElementById("codselnuovamisura");
             		node.value="01";
         		}
         		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1732") {
         			node.value="Casa Cura e Custodia";
             		node=document.getElementById("codselnuovamisura");
             		node.value="04";
         		}
         		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1733") {
         			node.value="Ospedale Psichiatrico Giudiziario";
             		node=document.getElementById("codselnuovamisura");
             		node.value="06";
         		}
         		
         		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="2754" ||
         			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="2768"
         		   ) {
         			node=document.getElementById("nuovamisura");
             		node.style.visibility='visible';
             		node=document.getElementById("tiponuovamisura");
             		node.style.visibility='hidden';
             		node=document.getElementById("selnuovamisura");
             		node.style.visibility='hidden';
             		
             		document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>.value='';
             		document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>.value='';
             		document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>.value='';
         		}
       		}
       		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1730" &&
       			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1731" &&
       			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1732" &&
       			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="1733" &&
       			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="2754" &&
       			document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value !="2768" )
           	{
         		node=document.getElementById("nuovamisura");
         		node.style.visibility='hidden';
         		node=document.getElementById("tiponuovamisura");
         		node.style.visibility='hidden';
         		node=document.getElementById("selnuovamisura");
         		node.style.visibility='hidden';
     			node.value="";
         		node=document.getElementById("codselnuovamisura");
         		node.value="";
     		
       		}
       		
        }
   	   } // fine ciclo for
	 } // Fine caso singolo oggetto
     else  	 // Nel caso di più oggetti, l'input di nuova misura è visibile se almeno un esito è di trasformazione
     {
  		node=document.getElementById("nuovamisura");
 		node.style.visibility='hidden';
 		node=document.getElementById("tiponuovamisura");
 		node.style.visibility='hidden';
 		node=document.getElementById("selnuovamisura");
 		node.style.visibility='hidden';
		node.value="";
     	node=document.getElementById("codselnuovamisura");
     	node.value="";
 
       for (j = 0; j < document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>.length ; j++ )
       {
         for (i = 0; i < document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].length ; i++ )
         {
           if ( (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j][i].selected) && 
        		  ( (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1730") ||
        				  (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1731") ||
        				  (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1732") ||
        				  (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1733") )  ) {
             		node=document.getElementById("nuovamisura");
             		node.style.visibility='visible';
             		node=document.getElementById("tiponuovamisura");
             		node.style.visibility='visible';
             		node=document.getElementById("selnuovamisura");
             		node.style.visibility='visible';
             		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1730") {
             			node.value="Colonia Agricola";
                 		node=document.getElementById("codselnuovamisura");
                 		node.value="01";
             		}
             		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1731") {
             			node.value="Casa di Lavoro";
                 		node=document.getElementById("codselnuovamisura");
                 		node.value="01";
             		}
             		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1732") {
             			node.value="Casa Cura e Custodia";
                 		node=document.getElementById("codselnuovamisura");
                 		node.value="04";
             		}
             		if (document.InserisciOrdinanzaInosservanzaObblighiMS.<%=ICostantiTenore.CAMPO_COD_ESITO_TENORE%>[j].value =="1733") {
             			node.value="Ospedale Psichiatrico Giudiziario";
                 		node=document.getElementById("codselnuovamisura");
                 		node.value="06";
             		}
           }
         }
       }
     } // Fine caso più oggetti
	   
   }	   
   </script>
    
  </head>

  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Emissione Ordinanza Inosservanza Obblighi Misura Sicurezza</font>&nbsp;
      <%
        String lAction = new String();
        lAction = "siap.sius.depositoordinanzapc.action.ActInserisciOrdinanzaUDS";
      %>
      </td>
    </tr>
    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InserisciOrdinanzaInosservanzaObblighiMS">
    <table cellspacing="2" cellpadding="2" width="90%">
   <tr>
     <td class="l" width="30%"> Data Emissione</td>
     <td class="l" width="70%"><%=DateUtils.getDateToString(data_emissione,"dd/MM/yyyy")%></td>
   </tr>
      <tr>
        <td class="l">Motivo della richiesta</td>
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
   for (int i=0; i< tenori.length;i++)
    {
	   // Filtraggio degli esiti per l'ordinanza
	   // Deve essere basato anche sulla misura in corso
	   if (tenori[i].getCodOggettoTenore().equals("2430")) {
		   	Option lOption = new Option(DecodificheManager.getInstance().getEsitoTenore() , esiti[i]);
		   if (esecuzionemisurasicurezza != null && esecuzionemisurasicurezza.getCodTipoMisura() != null 
				   && esecuzionemisurasicurezza.getCodTipoMisura().equals("2352")) {  // In caso di Libertà Vigilata posso convertire!
		   		lOption.setFilter( new String[] {"1730", "1731", "1732", "1733", "1734", "1736", "1737", "1738", "1739"} );  
		   }
		   else
		   		lOption.setFilter( new String[] {"1734", "1736", "1737", "1738", "1739"} );  
	   	esiti[i] = lOption.toString();
	   }
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
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Motivazioni del provvedimento</td>
      <td class="l">
        <Textarea title="Motivazioni" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ULTERIORE_DESCRIZIONE%>" cols=68 rows=3></Textarea></td>
      </td>
    </tr>
    <tr>
      <td class="l"><%=labelUfficio%> Competente </td>
      <td class="l">
        <input Title="<%=labelUfficio%>" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>" value="" size=35 >
        <a href="Javascript:ListaUDS('InserisciOrdinanzaInosservanzaObblighiMS','<%=ICostantiDepositoOrdinanzaPc.CAMPO_COD_UFFICIO_MAGISTRATO_COMP%>','<%=CodUff%>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </td>
    </tr>
 </table>
 				<table cellspacing="2" cellpadding="2" width="90%" id="nuovamisura" style="visibility: hidden">

  					<tr>
  					<% EsecuzioneMisuraSicurezzaModel lNuovaMisuraSicurezza = new  EsecuzioneMisuraSicurezzaModel(); %>
						<td>
						<font class="label" id="tiponuovamisura" style="visibility: hidden">Nuova Misura</font>&nbsp;
			    			<input Title="Descr Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_DESCR_TIPO_NUOVA_MISURA_ESECUZIONE %>"  id="selnuovamisura" style="visibility: hidden" readonly size=43%>
			    			<input Title="Tipo" name="<%=ICostantiEsecuzioneMS.CAMPO_COD_TIPO_NUOVA_MISURA_ESECUZIONE %>"  id="codselnuovamisura" style="visibility: hidden">
          				</td>
          			</tr>
          				<tr>
          				<td>
      					<font class="label">Data Decorrenza</font>
        				<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
        				<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      					</td>
          				<td>
						<font class="label">Durata: Anni</font>&nbsp;
          					<input title="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA%>" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumAnniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNI_NUOVA_MISURA%>"  >
						</td>
						<td>
						<font class="label">Mesi</font>&nbsp;
          					<input title="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA%>" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumMesiMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA%>"  >
						</td>
						<td>
						<font class="label">Giorni</font>&nbsp;
          					<input title="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA%>" size=5% maxlength=2 value="<%=StringUtils.toStringJSP(lNuovaMisuraSicurezza.getNumGiorniMisura()) %>" type="text" ONKEYPRESS="return TicTabNumField(this,event)" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA%>"  >
						</td>
						</tr>
						
  				</table>
 
 <table cellspacing="2" cellpadding="2" style="width: 90%;">

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
    
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("InserisciOrdinanzaInosservanzaObblighiMS");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_FINE_MISURA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_MESI_NUOVA_MISURA%>","lt=11", "Il campo Mesi non deve essere maggiore di 11");
    frmvalidator.addValidation("<%=ICostantiDepositoOrdinanzaPc.CAMPO_GIORNI_NUOVA_MISURA%>","lt=29", "Il campo Giorni non deve essere maggiore di 29");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

 </body>
</html>