<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.fogliocomplementare.action.ICostantiFoglioComp" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>


<jsp:useBean id="documentoAllegato"     scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="evento"                scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UtenteConnesso"        scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="provenienza"           scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoNonInvio"        scope="request" class="java.lang.String"/>
<jsp:useBean id="provvSige"             scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>

	
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

<html>
<head>
  <title>[S.I.E.S.] - Compilazione Foglio Complementare</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<%
  String lAzione = new String();
  lAzione = "siap.sige.fogliocomplementare.action.ActModificaCompFoglioComp";
  
  String lNota = "N.B.: Il Foglio Complementare è già stato creato. Inserire eventualmente le indicazioni relative all'inserimento manuale e premere il tasto Conferma.";
  String lRet=IWebConstants.PG_MAIN + "?Action=siap.sige.fogliocomplementare.action.ActLoadElencoCompFoglioComp&IdFascicoloSige=" + FascicoloSigeEsteso.getFascicoloSige().getIdFascicoloSige() + "&StoTornando=1";
%>

  <script type="text/javascript">

	  function VerifyConferma()   
      {

        // Controllo obbligatorietà Data Inserimento Manuale oppure Descrizione
        // se viene selezionato dalla lista "Inserito Manualmente su NSC".
	    if(document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '01'){
		    var dataInserimentoManuale=document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
		                               document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
		                               document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
		    var descriz = document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
	                               
		    if( dataInserimentoManuale != null && dataInserimentoManuale.length<3 && descriz == ""){
	          	alert ("Valorizzare Descrizione oppure Data Inserimento Manuale");
	          	document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE %>.focus();
	          	return false;
		    }
				        
	    }

        // Controllo obbligatorietà Descrizione se viene selezionato dalla lista "Altro".
	    if(document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>.value == '02'){
            var descrizione = document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value;
            if (descrizione == "" )
	        {
	          	alert ("Descrizione è un campo obbligatorio");
	          	document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO %>.focus();
	          	return false;
	        }
	    }
	    
      }
        
	  function Verify()
	  {
	    if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value.length==1)
	        document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value;

	    if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value.length==1)
	        document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value='0'+document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value;

	    // Controllo validità data Emissione.
	    var dataCompilTrasm=document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value +'/'+
	                      document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>.value +'/'+
	                      document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;

	    if (! ControllaData(dataCompilTrasm))
	    {
	      alert('Data Compilazione/Trasmissione non valida!');
	      return false;
	    }

	    // Controllo validità data Inserimento Manuale.
	    var dataInsmanuale=document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value +'/'+
	                       document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value +'/'+
	                       document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value;
        if (! ControllaDataPassaVuota(dataInsmanuale))
	    {
	      alert('Data Inserimento Manuale non valida!');
	      return false;
	    }
	              	    
	    return true;
	  }

	  function AbilitaCampi()
	  {

		  // se il Foglio Complementare è stato inviato i campi "Motivazione non Inviato",
		  // "Descrizione" e "Data Inserimento Manuale" non sono editabili
		  var dataTrasmissione = document.LoadInserisciCompFoglioComp.<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>.value;
		  if (dataTrasmissione.length >5)
		  {  
			  document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>.disabled = true;
	  		  document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = true;
 		  	  document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
		  	  document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
		  	  document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
	      }		  
		  
		 if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>.value=='-')
	  	 {
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;

	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
	  		//ISCRITTO MANUALMENTE DA NSC
		 } else if (document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>.value=='01')
	  	 {
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
	  		
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = false;
	  	} else
	  	{
	  	    //ALTRO
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.value = "";
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.value = "";
	  			  		
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>.disabled = false;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>.disabled = true;
	  		document.LoadInserisciCompFoglioComp.<%=ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>.disabled = true;
	  	}
		  
	  }
	  
  </script>
</head>
<%  if(provenienza != null && provenienza.equals("InsertFC")) { %>
  		<body class="corpo" onload="AbilitaCampi()">
<%  } else { %>
		<body class="corpo">
<%  } %>

  <link rel="STYLESHEET" type="text/css" href="/css/style.css">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Foglio Complementare</font>
      </td>
      <td class="LBG">
      	<a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.avvocato.action.ActStampaFoglioComplementare&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=<%="SIGE_ST_006"%> ')">
        	<img id="generaStampa" align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      	</a>
      </td>
    
      <td class="LBG">
          <a href="<%=lRet%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
      </td>

  
    </tr>
   </table>
<br>
<%
  if (FascicoloSigeEsteso != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>
<%
  }
%>
<br>
    <!-- LISTA DEI FOGLI ANNULLATI -->
    <jsp:include page="<%=ICostantiFoglioComp.PG_LISTA_CFC_ANNULLATI%>">
          <jsp:param name="ActionLink" value="siap.sige.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp"/>
     </jsp:include>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciCompFoglioComp">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo" colspan=6>Estremi Provvedimento</td>
    </tr>

<% if (provvSige != null && provvSige.getProvvedimento() != null){ %>
    <tr>
      <td class="l">
<%
		if(provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("02")){
%>			
			DECRETO n°
<%
		} else if (provvSige.getProvvedimento().getCodTipoProvvedimentoSige() != null && provvSige.getProvvedimento().getCodTipoProvvedimentoSige().equals("03")){
%>      
      		ORDINANZA n°
<%
		} else {
%>
			- 
<%			
		}
%>      		
	      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveAnno())%></font>
	      <font class="l">/</font>
	      <font class="campo"><%=StringUtils.toStringJSP(provvSige.getProvvedimento().getChiaveProgr())%></font>
	      <font class="l">del</font>
	      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvSige.getProvvedimento().getDataEmissione(),"dd-MM-yyyy"))%></font>
   	  </td>
    </tr>
<% }

   if(documentoAllegato.getDataAnnullamento() != null)
   {
%>
 </tr>
      <td class="l"><font class="cRosso"> Foglio Complementare ANNULLATO</font></td>
 </tr>
      <tr>
        <td class="l"> Data di annullamento </td>
        <td class="L"><%=DateUtils.getDateToString(documentoAllegato.getDataAnnullamento(),"dd-MM-yyyy")%>
        </td>
      </tr>
      <tr>
        <td class="l">Motivo annullamento</td>
        <td class="l"><%=(documentoAllegato.getMotivoAnnullamento() == null ) ? "-" : documentoAllegato.getMotivoAnnullamento()%>
        </td>
      </tr>
<%
   }
%>

    <tr>
      <td class="l">Anno/Numero F.C.
      <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getAnnoFoglioComplementare()  )%></font>
      <font class="l">/</font>
      <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getProgrFoglioComplementare() )%></font>
    </tr>

<%  if(provenienza != null && provenienza.equals("InsertFC")) { %>
        <!-- Sono nell'inserimento del Foglio Complementare -->
	    <tr>
	      <td class="l">Data Compilazione/Trasmissione <font class=ob>(*)</font></td>
	      <td class="L">
	        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillDM(value)"> /
	        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillDM(value)"> /
	        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataEmissione(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillYear(value)">
	      </td>
	    </tr>
  </table>   
  <br/>
  
  <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          <%=lNota%>
        </td>
      </tr>
  </table>
  <br/>
  
  <table cellspacing=2 cellpadding=2>	
	    <tr>
	      <td class="l">Motivazione non Inviato</td>
	      <td class="L">
	          <select title="motivoNonInvio" class=small name="<%=ICostantiFoglioComp.CAMPO_COD_MOTIVO_NON_INVIO%>" onChange="AbilitaCampi()" >
           		  <%= motivoNonInvio %>
        	  </select> 	      
          </td>
          <td class="l">Descrizione</td>
          <td class="L" >
     		  <input value="" type="text" size="30" maxlength="250" name="<%=ICostantiFoglioComp.CAMPO_DESCR_MOTIVO_NON_INVIO%>" > 
    	  </td>
	    </tr>

	    <tr>
	      <td class="l">Data Inserimento Manuale</td>
	      <td class="L">
	        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillDM(value)"> /
	        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillDM(value)"> /
	        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE %>"  
	        				onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  
	        				onBlur="javascript:value=FillYear(value)">
	      </td>
	    </tr>

	    <tr>
	      <td colspan="2">
	        <font class="L"> </font>&nbsp;
	      </td>
	    </tr>
	
	    <tr>
	      <td colspan="2">
	        <input class="bottone" type="submit" value="Conferma Inserimento Manuale" onClick="javascript:return VerifyConferma();">
	      </td>
	    </tr>
    	        
<%  } else { %>
    <!-- Sono nel Dettaglio Foglio Complementare -->
    <tr>
      <td class="l">Data Compilazione/Trasmissione </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataTrasmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l">Motivazione non Inviato </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrMotivazioneNonInvio())%></font>&nbsp;
      </td>
      <td class="l">Descrizione </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(documentoAllegato.getDescrizioneNonInvio())%></font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l">Data Inserimento Manuale </td>
      <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(documentoAllegato.getDataInsMan(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

<%  } %>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP( evento.getIdEvento(), "" )%>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  >
  <input type="HIDDEN" value="<%=StringUtils.toStringJSP( documentoAllegato.getIdDocumentoAllegato(), "" )%>" name="<%=ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO%>"  >
  <input type="HIDDEN" value="<%=documentoAllegato.getDataTrasmissione()%>" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_FOGLIO_TRASMESSO%>" >
  <input type="HIDDEN" value="<%=documentoAllegato.getCodMotivazioneNonInvio()%>" name="<%=ICostantiDocumentoAllegato.CAMPO_FLAG_MOTIVO_NON_INVIO%>" >
  
  </FORM>
<%  if(provenienza != null && provenienza.equals("InsertFC")) { %>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciCompFoglioComp");
    // Controllo data Compilazione/Trasmissione.
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_MESE_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_TRASMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Compilazione/Trasmissione deve essere di 4 caratteri");

    // Controllo data inserimento manuale.
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_GIORNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_MESE_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFoglioComp.CAMPO_ANNO_DATA_INS_MANUALE%>","minlen=4","La lunghezza del campo Anno Data Inserimento Manuale deve essere di 4 caratteri");
    
    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
<%  } %>
  </script>
  </body>
</html>