<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimenti" scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="rifFascicoloSiep" scope="request" class="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="FascSiepTrovato" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="tipologiaNumerazione" scope="request" class="java.lang.String"/>

<jsp:useBean id="TipoProvvedimentiMSic" scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaEmittenteMSic" scope="request" class="java.lang.String"/>

<%
  SoggettoModel soggetto = null;
if (fascicoloSiusGP != null && fascicoloSiusGP.getFascicoloSiusModel() != null && fascicoloSiusGP.getFascicoloSiusModel().getSoggetto() != null)
     soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();

String aChiaveAnno = "";
String aChiaveProgr = "";
String aChiaveUfficio = "";
String aSedeUfficio = "";
Date aDataProvvedimento = null;
String aAnnoSentenza = "";
String aNumeroSentenza = "";
Date aDataIrrevocabilità = null;
String aNote = "";
String aCognomeRif = "";
String aNomeRif = "";
String aComuneNasRif = "";
String aDataNasRif = "";

if (FascSiepTrovato.getChiaveAnno() != null )
{
  aChiaveAnno = FascSiepTrovato.getChiaveAnno().toString();
  aChiaveProgr = FascSiepTrovato.getChiaveProgr().toString();
  aChiaveUfficio = FascSiepTrovato.getChiaveUfficio();
  aSedeUfficio = FascSiepTrovato.getDescrComuneUfficio();
  aDataProvvedimento = FascSiepTrovato.getSentenza().getDataProvvedimento();
  LuogoUtenteConnesso = FascSiepTrovato.getSentenza().getDescrLuogoEmittente();
  aAnnoSentenza = FascSiepTrovato.getSentenza().getAnnoSentenza().toString();
  aNumeroSentenza = FascSiepTrovato.getSentenza().getNumeroSentenza();
	//modifica conseguente alla variazione di SentenzaModel - Vincenzo 21/10/2010
  aDataIrrevocabilità = FascSiepTrovato.getDataIrrevocabilita();
  aNote = "";
  aCognomeRif = FascSiepTrovato.getSoggetto().getCognome();
  aNomeRif = FascSiepTrovato.getSoggetto().getNome();
  aComuneNasRif = FascSiepTrovato.getSoggetto().getDescrComuneNascita();
  aDataNasRif = FascSiepTrovato.getSoggetto().getDataNascita().toString();
}
else
{
  aDataProvvedimento = null;
  LuogoUtenteConnesso = "";
  aAnnoSentenza = "";
  aNumeroSentenza = "";
  aDataIrrevocabilità = null;
  aNote = "";
}

// STUB 21/02/2005 Controllo soggetti.
String aCognome = "";
String aNome = "";
String aComuneNas = "";
String aDataNas = "";
/*
if (fascicolo.getSoggetto() != null )
{
  aCognome = fascicolo.getSoggetto().getCognome();
  aNome = fascicolo.getSoggetto().getNome();
  aComuneNas = fascicolo.getSoggetto().getDescrComuneNascita();
  aDataNas = fascicolo.getSoggetto().getDataNascita().toString();
}
*/

if (soggetto != null )
{
  aCognome = soggetto.getCognome();
  aNome = soggetto.getNome();
  aComuneNas = soggetto.getDescrComuneNascita();
  // MERGE v10: e se la data di nascita non ci fosse???
  if (Utils.isPresent(soggetto.getDataNascita()))
  	aDataNas = soggetto.getDataNascita().toString();
}
%>
<html>
  <head>
    <title>[S.I.E.S.] - Inserimento riferimenti Altri Titoli Esecutivi</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      var desktop;
      var popUpAperto = "";

      function ListaComuniEsecutivo(a_formname,a_fieldname,codTipoUfficio)
      {
    	  popUpAperto = "Esecutivo";
    	  ListaComuni(a_formname,a_fieldname,codTipoUfficio);
      }

      function ListaComuniEmittente(a_formname,a_fieldname,codTipoUfficio)
      {
    	  popUpAperto = "Emittente";
    	  ListaComuni(a_formname,a_fieldname,codTipoUfficio);
      }

      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

    function radioTitolo()
    {
      var nodeDivRicercaProcSiep;
      var nodeDivTitoliNonPresentiSuSiep;
      
      nodeDivRicercaProcSiep=document.getElementById('DivRicercaProcSiep');
      nodeDivTitoliNonPresentiSuSiep=document.getElementById('DivTitoliNonPresentiSuSiep');
 
      if(document.LoadInserisciRifFascicoloSiep.titolo[0].checked) {
    	nodeDivRicercaProcSiep.style.visibility='visible';
    	nodeDivTitoliNonPresentiSuSiep.style.visibility='hidden';
      } else {
      	nodeDivRicercaProcSiep.style.visibility='hidden';
    	nodeDivTitoliNonPresentiSuSiep.style.visibility='visible';
      } 
 
      // ricerca tra i procedimenti della Procura 
      if(document.LoadInserisciRifFascicoloSiep.titolo[0].checked)
      {
    	document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_INSERIMENTO %>.value="PROC_SIEP";
      }
      // titoli non presenti su Siep      
      else if (document.LoadInserisciRifFascicoloSiep.titolo[1].checked )
      {
    	document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_INSERIMENTO %>.value="NO_SIEP";
      }

      loadUfficiAccorpatiByDesc();
    }
    
      function VerifyRicerca()
      {
        // Impostazione action di ricerca.
        document.LoadInserisciRifFascicoloSiep.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.rifasiep.action.ActRicercaFascicoloSiep"

        // Controllo Anno/Numero Fascicolo SIEP.
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length<4 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value<1900 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value) )
        {
          alert ("Anno Fascicolo SIEP Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value.length<=0 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value<0 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value) )
        {
          alert ("Numero Fascicolo SIEP Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        // Controllo obbligatorietà autorità Competente ed Emittente.
        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value=="-")
        {
          alert("Il tipo autorità competente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }

        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value=="")
        {
          alert("Il luogo per l'autorità competente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }

        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value = "-";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value = "";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value = "-";
        document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE%>.value = "";

        loadNumProgOrigin();

        return true;
      }

      function Verify(aModalita)
      {
        // Impostazione action di Inserimento/Modifica.
        if(aModalita=="I")
          document.LoadInserisciRifFascicoloSiep.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.rifasiep.action.ActInserisciRifFascicoloSiep";

        if(aModalita=="M")
          document.LoadInserisciRifFascicoloSiep.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.rifasiep.action.ActModificaRifFascicoloSiep";

        // Controllo Anno/Numero Fascicolo SIEP.
		// Ricerca del Titolo tra i Procedimenti SIEP
		if(document.LoadInserisciRifFascicoloSiep.titolo[0].checked){
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length<4 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value<1900 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value) )
        {
          alert ("Anno Fascicolo SIEP Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value.length<=0 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value<0 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value) )
        {
          alert ("Numero Fascicolo SIEP Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        // Controllo obbligatorietà autorità Competente ed Emittente.
        codUfficio = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value;
        //alert("codUfficio = "+ codUfficio);
        if (codUfficio=="-")
        {
          alert("Il tipo autorità competente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }

        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value=="")
        {
          alert("Il luogo per l'autorità competente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
	    // Inserimento Titolo non presenti su SIEP
	    } else {

	        // se valorizzato il campo Anno Misura di Sicurezza
	        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA%>.value!=""){
		        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>.value.length<4 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>.value<1900 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>.value) )
		        {
		          alert ("Anno Misura di Sicurezza Non Valido");
		          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>.focus();
		          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
		          return false;
		        }
		     }
		     
		     // se valorizzato il campo Numero Misura di Sicurezza
			 if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA%>.value != "" ){
			    if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA%>.value.length<=0 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA %>.value<0 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA %>.value) ) {
		          alert ("Numero Misura di Sicurezza Non Valido");
		          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA%>.focus();
		          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
		          return false;
		        }
		     }
		     
			// anno/numero M.Sic devono essere entrambi null oppure entrambi valorizzati
			if((document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA%>.value == "" && document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA%>.value != "") ||
			   (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA%>.value != "" && document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA%>.value == "") ){
			  alert ("Valorizzare entrambi Anno e Numero Misura di Sicurezza");
	          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>.focus();
	          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
	          return false;
			}

	        // i campi Autorità Competente ed il Luogo devono essere entrambi null oppure entrambi valorizzati.
	        if ((document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>.value =="-" && document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_MISURA_SICUREZZA%>.value != "") ||
	            (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>.value != "-" && document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_MISURA_SICUREZZA%>.value == "") ){ 
	          alert ("Valorizzare entrambi Autorità e Luogo di Competenza");
	          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>.focus();
	          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
	          return false;
	        }

	    	// il campo tipo provvedimento deve essere obbligatorio
	        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_PROVVEDIMENTO_MSIC%>.value=="-") {
	          alert("Il Tipo Provvedimento è un campo obbligatorio");
	          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_PROVVEDIMENTO_MSIC%>.focus();
	          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
	          return false;
	        }
	    	
	    }
	    
		if(document.LoadInserisciRifFascicoloSiep.titolo[0].checked){
        // Controllo della data provvedimento.
        var data_provvedimento=(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value)+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (! ControllaData(data_provvedimento))
        {
          alert('Data provvedimento non valida');
          return false;
        }

        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO %>.value.length<4 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO %>.value<1900 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO %>.value) )
        {
          alert ("Anno Provvedimento Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO %>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO%>.value.length<=0 || document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO %>.value<0 || isNaN(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO %>.value) )
        {
          alert ("Numero Provvedimento Non Valido");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }
        // Controllo della data irrevocabilità.
        var data_definizione=(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value)+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
        if (! ControllaData(data_definizione))
        {
          alert('Data irrevocabilità non valida');
          return false;
        }

        //alert('Data provvedimento ='+data_provvedimento );
        //alert('Data irrevocabilità ='+data_definizione );
        // Controllo della data provvedimento <= data di irrevocabilità
        if (! CompareDate(data_provvedimento, data_definizione))
        {
          alert('Data provvedimento > della data irrevocabilità');
          return false;
        }

        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=="-")
        {
          alert("L' autorità emittente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }

        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE%>.value=="")
        {
          alert("Il luogo emittente è un campo obbligatorio");
          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE%>.focus();
          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
          return false;
        }

		} else {
	        // Controllo della data provvedimento (MIS. SIC.).
	        var data_provvedim=(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO_MSIC%>.value)+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO_MSIC%>.value+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO_MSIC%>.value;
	        if (! ControllaData(data_provvedim))
	        {
	          alert('Data provvedimento non valida');
	          return false;
	        }

	        // Controllo della data irrevocabilità.
	        var data_irrevocabilita=(document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA_MSIC%>.value)+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA_MSIC%>.value+'/'+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA_MSIC%>.value;
	        if (! ControllaDataPassaVuota(data_irrevocabilita))
	        {
		       alert('Data irrevocabilità non valida');
	           return false;
	        }
	
	        //alert('Data provvedimento ='+data_provvedimento );
	        //alert('Data irrevocabilità ='+data_definizione );
	        // Controllo della data provvedimento <= data di irrevocabilità
	        <%-- Ticket#20210624014 - ERrore JS referenziava la variabile "data_definizione" con nome errato
	         anche data_provvedimento deve essere data_provvedim
	        --%>
	        //if (! CompareDate(data_provvedimento, data_definizione))
	        if (! CompareDate(data_provvedim, data_irrevocabilita))	
	        <%-- Ticket#20210624014 - FINE --%>
	        {
	          alert('Data provvedimento > della data irrevocabilità');
	          return false;
	        }
	
	        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC%>.value=="-")
	        {
	          alert("L' autorità emittente è un campo obbligatorio");
	          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC%>.focus();
	          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
	          return false;
	        }
	
	        if (document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE_MSIC%>.value=="")
	        {
	          alert("Il luogo emittente è un campo obbligatorio");
	          document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE_MSIC%>.focus();
	          document.LoadInserisciRifFascicoloSiep.CONFERMA.disabled=false;
	          return false;
	        }
		}
        
        //alert ("IdFascicoloSiep Origine / Trovato = "+document.LoadInserisciRifFascicoloSiep.idFascicoloPrincipale.value+" / "+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>.value);
        //alert ("anno FascicoloSiep Origine / Trovato = "+document.LoadInserisciRifFascicoloSiep.annoFascicoloPrincipale.value+" / "+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value);
        //alert ("progr FascicoloSiep Origine / Trovato = "+document.LoadInserisciRifFascicoloSiep.progrFascicoloPrincipale.value+" / "+document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value);
        if ( document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP %>.value == document.LoadInserisciRifFascicoloSiep.idFascicoloPrincipale.value &&
             document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>.value == document.LoadInserisciRifFascicoloSiep.annoFascicoloPrincipale.value     &&
             document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>.value == document.LoadInserisciRifFascicoloSiep.progrFascicoloPrincipale.value       )
        {
          alert("Il Titolo esecutivo individuato corrisponde al titolo origine. Controllare i dati inseriti! ");
          return false;
        }

        // STUB 17-01/2005 Controllo soggetti.
	//if (<%=fascicolo.getIdFascicoloSiep()%> != null  )
	<%-- Ticket#20210624014 - Il controllo sull'anagrafica va fatto solo nel caso si provenga dalla ricerca
	     altrimenti l'anagrafica del fascicolo SIPE non esiste
	--%>
	if(document.LoadInserisciRifFascicoloSiep.titolo[0].checked)
	<%-- Ticket#20210624014 - FINE --%>
	{
	  var soggettoOri = "<%=aCognome%>"+"<%=aNome%>"+"<%=aComuneNas%>"+"<%=aDataNas%>";
	  var soggettoRif = "<%=aCognomeRif%>"+"<%=aNomeRif%>"+"<%=aComuneNasRif%>"+"<%=aDataNasRif%>";
	  if (soggettoOri != soggettoRif)
	  {
	    if(! confirm("Soggetto del Titolo Esecutivo differente dal soggetto del procedimento corrente. Si vuole continuare ?" ) )
	      return false;
	  }
	}
        return true;
      }
      </script>

  </head>

  <body class="corpo" onLoad="radioTitolo();">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
          String lAction = new String();
          if( modalita.equals("I") )
          {
              lAction = "siap.sius.rifasiep.action.ActInserisciRifFascicoloSiep";
%>
            <font class="campo">Inserimento Riferimenti Altri Titoli Esecutivi </font>
<%
          }
          else if( modalita.equals("M") )
          {
            lAction = "siap.sius.rifasiep.action.ActModificaRifFascicoloSiep";
%>
            <font class="campo">Modifica Riferimenti Altri Titoli Esecutivi</font>
<%
          }
%>
      </td>
    </tr>
  </table>

  <br>

  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="Label">
        <font class="label">N.B. Se il Titolo Esecutivo di riferimento è su altra BDI, ricercare prima il procedimento SIEP con apposita funzione</font>&nbsp;
      </td>
    </tr>
  </table>
  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadInserisciRifFascicoloSiep'>
   <table>
		<tr>
	      <td class="c" colspan="2">Ricerca tra i Procedimenti SIEP&nbsp;
	          <input type="radio" name="titolo" value="siep" checked onClick="radioTitolo();">
	            &nbsp;&nbsp;Titoli non presenti su SIEP &nbsp;
	          <input type="radio" name="titolo" value="nonSiep" onClick="radioTitolo();">
	      </td>
	    </tr>
   </table>
   <br>

<div id="DivRicercaProcSiep" style="width: 100%; visibility:visible; position:absolute;" >
    <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="LBG" colspan="2" >
        <font class="label">Estremi dell'ulteriore Titolo Esecutivo</font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l"width="32%">Anno/Numero SIEP <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_FASCICOLO_SIEP %>" value ="<%=aChiaveAnno%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        /<input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN %>" value ="<%=aChiaveProgr%>" maxlength="14" size="14" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        <input type="hidden" name="<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(aChiaveProgr,"")%>">
      </td>
    </tr>

    <tr>
      <td class="l">Autorità <font class=ob>(*)</font></td>
      <td class="L">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>" onchange="Javascript:ResetField();">
          <%= AutoritaCompetente %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo <font class=ob>(*)</font></td>
      <td class="l">
        <input name="<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>" value ="<%=aSedeUfficio%>" type="text" maxlength="35" size="35" readonly="readonly">
        <a href="Javascript:ListaComuniEsecutivo('LoadInserisciRifFascicoloSiep','<%= ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP %>' , document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>[document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    <tr>
      <td class="l">Ufficio Accorpato</td>
      <td class="l">
         	<select name="<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>">
         	<option value="0" >-</option>
         	</select>
      </td>
    </tr>

    <tr>
      <td colspan="2" class="l">
        <input class="bottone" type="submit" name="Ricerca" value="Ricerca" onClick="javascript:return VerifyRicerca();">
      </td>
    </tr>
    
    </table>

    <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="l">Tipo Provvedimento</td>
      <td class="L">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>"  >
          <%= TipoProvvedimenti %>
        </select>
      </td>
      <td class="l">Data Provvedimento <font class=ob>(*)</font></td>
      <td class="L">
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataProvvedimento,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
        <input  type="text" name="<%= ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataProvvedimento,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
        <input  type="text" name="<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataProvvedimento,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l"width="32%">Anno/Numero Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_PROVVEDIMENTO %>" value ="<%=aAnnoSentenza%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        /<input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_PROVVEDIMENTO %>" value ="<%=aNumeroSentenza%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">Definitivo in Data <font class=ob>(*)</font></td>
      <td class="L">
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataIrrevocabilità,"dd")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
        <input  type="text" name="<%= ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataIrrevocabilità,"MM")) %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
        <input  type="text" name="<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA %>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(aDataIrrevocabilità,"yyyy")) %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Autorità Emittente <font class=ob>(*)</font></td>
      <td class="L" colspan = "3">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>"  >
          <%= AutoritaEmittente %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo Emittente<font class=ob>(*)</font></td>
      <td class="l" colspan = "3">
        <input Title="Luogo" name="<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE%>" value="<%=LuogoUtenteConnesso%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuniEmittente('LoadInserisciRifFascicoloSiep','<%= ICostantiRifFascicoloSiep.CAMPO_COD_LUOGO_EMITTENTE%>', document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
    </table>

</div>


<div id="DivTitoliNonPresentiSuSiep" style="width: 100%; visibility:visible; position:relative; " >
    <table cellspacing=0 cellpadding=0 width=95%>
        
    <tr>
      <td class="l"width="32%">Anno/Numero</td>
      <td class="L" colspan = "3">
        <input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_MISURA_SICUREZZA %>" value ="<%=aChiaveAnno%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        /<input type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_NUMERO_MISURA_SICUREZZA %>" value ="<%=aChiaveProgr%>" maxlength="13" size="13" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        &nbsp;&nbsp;
        <select title="tipologiaNumerazione" class=small name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_NUMERAZIONE%>" >
           <%= tipologiaNumerazione %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità</td>
      <td class="L" colspan = "3">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>" >
          <%= AutoritaCompetente %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo</td>
      <td class="L" colspan = "3">
        <input name="<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_MISURA_SICUREZZA%>" value ="<%=aSedeUfficio%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciRifFascicoloSiep','<%= ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_MISURA_SICUREZZA %>' , document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>[document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_MISURA_SICUREZZA%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
  </table>

  <table cellspacing=0 cellpadding=0 width=95%>  
    <tr>
      <td class="l">Tipo Provvedimento</td>
      <td class="L">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_PROVVEDIMENTO_MSIC%>"  >
          <%= TipoProvvedimentiMSic%>
        </select>
      </td>
      <td class="l">Data Provvedimento <font class=ob>(*)</font></td>
      <td class="L">
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO_MSIC%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO_MSIC%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO_MSIC%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l"width="32%" ></td>
      <td class="l"></td>
      <td class="l">Definitivo in Data</td>
      <td class="L">
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA_MSIC%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA_MSIC%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
        <input  type="text" name="<%=ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA_MSIC%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente <font class=ob>(*)</font></td>
      <td class="L" colspan = "3">
        <select class=medium name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC%>"  >
          <%= AutoritaEmittenteMSic%>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo Emittente<font class=ob>(*)</font></td>
      <td class="l" colspan = "3">
        <input Title="Luogo" name="<%=ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE_MSIC%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciRifFascicoloSiep','<%= ICostantiRifFascicoloSiep.CAMPO_DESCR_LUOGO_EMITTENTE_MSIC%>', document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC%>[document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE_MSIC%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
   </table>
</div>

<div id="DivNote" style="width: 100%; visibility:visible; position:absolute; top:350px;" >
<br/>
    <table cellspacing=0 cellpadding=0 width=95% style="border: 0;">
    <tr>
      <td class="l" width="32%">Note</td>
      <td class="l" colspan ="3">
        <Textarea Title="Note" name="<%= ICostantiRifFascicoloSiep.CAMPO_NOTE %>" cols=80 rows=5><%=aNote%></textarea>
      </td>
    </tr>
    </table>
    <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" name="CONFERMA" type="submit" value="Conferma" onClick="javascript:return Verify('<%=modalita%>');">
        </td>
      </tr>
    </table>

</div>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>">
    <input type="HIDDEN" name="idFascicoloPrincipale" value="<%=fascicolo.getIdFascicoloSiep()%>">
    <input type="HIDDEN" name="annoFascicoloPrincipale" value="<%=fascicolo.getChiaveAnno()%>">
    <input type="HIDDEN" name="progrFascicoloPrincipale" value="<%=fascicolo.getChiaveProgr()%>">
<%
    if (FascSiepTrovato.getChiaveAnno() != null )
    {%>
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=FascSiepTrovato.getIdFascicoloSiep()%>">
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=FascSiepTrovato.getSentenza().getCodLuogoEmittente()%>">
  <%}else{ %>
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" >
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_LUOGO_EMITTENTE%>" >
  <%}
    if( modalita.equals("M") && rifFascicoloSiep.getIdRiferimentoFascicoloSiep() != null)
    {%>
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>" value="<%=rifFascicoloSiep.getIdRiferimentoFascicoloSiep().toString()%>" >
  <%}else{%>
      <input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>" >
  <%}%>

	<input type="HIDDEN" name="<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_INSERIMENTO%>" >

  </FORM>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadInserisciRifFascicoloSiep");

      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","minlen=4","La lunghezza del campo Anno Data Provvedimento deve essere di 4 caratteri");

      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRifFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","minlen=4","La lunghezza del campo Anno Data Irrevocabilità deve essere di 4 caratteri");

      //frmvalidator.setAddnlValidationFunction("Verify");

  var ufficiAccorpatiArray = new Array();

  <%
  Iterator uaIter = ufficiAccorpati.iterator();
  int uaIndice = 0;
  while (uaIter.hasNext())
  {
  	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
  %>
  ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>"
		  ,"<%=uaModel.getIncrProgressivo()%>"
		  ,"<%=uaModel.getCodUfficio()%>"
		  ,"<%=uaModel.getCodUfficioNew()%>"
		  ,"<%=uaModel.getCodTipoUfficio()%>"
		  ,"<%=uaModel.getCodTipoUfficioNew()%>"
		  ,"<%=uaModel.getDescrizioneNewUfficio()%>"); 
  <%
  uaIndice ++;
  }
  %>

  function transCoding(cod){
	  var ret = cod;
	  if (cod=='DIB'){
		  ret = 'Tribunale Ordinario';
	  } else if (cod=='TRIBSD'){
		  ret = 'Sezione Distaccata Tribunale';
	  } else if (cod=='CAS'){
		  ret = 'Corte Assise';
	  } else if (cod=='GIP'){
		  ret = 'Gip presso Tribunale';
	  } else if (cod=='PM'){
		  ret = 'Procura presso Tribunale';
	  } else if (cod=='PGCAP'){
		  ret = 'Procura presso Corte Appello';
	  }
	  return ret;
  }

  function loadUfficiAccorpati(codUfficio){
	  //alert("popUpAperto: "+popUpAperto);
	  if (popUpAperto=="Esecutivo"){
		var i=0;
		var ufficioAccorpatoSelect = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>;
		//alert("loadUfficiAccorpati: "+codUfficio);
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[3]==codUfficio){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[4])+")", ufficio[1]+"-"+ufficio[2]);
			}
			i++;
		}
	  }
	}

  function loadUfficiAccorpatiByDesc(){
		var i=0;
		var ufficioAccorpatoSelect = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>;
		var ufficioBaseDesc = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value;
		var ufficioTipoSelect = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>;
		//alert("choose: "+ufficioTipoSelect.selectedIndex);
		if (ufficioTipoSelect.selectedIndex>0){
			var ufficioTipo = ufficioTipoSelect.options[ufficioTipoSelect.selectedIndex].value;
			ufficioAccorpatoSelect.options.length = 0;
			ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
			while(i<ufficiAccorpatiArray.length){
				var ufficio = ufficiAccorpatiArray[i];
				if (ufficio[6]==ufficioBaseDesc && ufficio[5]==ufficioTipo){
					ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[4])+")", ufficio[1]+"-"+ufficio[2]);
				}
				i++;
			}
		}
		chooseUfficioAccorpato();
	}

  <%
  String uffPar = "";
  String uffReq = request.getParameter(ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO);
  if (uffReq!=null){
	  uffPar=uffReq; 
  }
  %>
  
  function chooseUfficioAccorpato(){
	  var ufficioAccorpato = "<%=uffPar%>";
	  var ufficioAccorpatoSelect = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>;
	  var lunghezza = ufficioAccorpatoSelect.options.length;
	  var indice = 0;
	  while (indice<lunghezza){
		  var valore = ufficioAccorpatoSelect.options[indice].value
		  //alert(indice+") "+valore+" = "+ufficioAccorpato);
		  if (valore==ufficioAccorpato){
			  ufficioAccorpatoSelect.selectedIndex=indice;
			  }
		  indice++;
		  }
	  }

  function ResetField(){
      document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value="";
      var selectUfficioAccorpato = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>;
      selectUfficioAccorpato.options.length = 0;
      selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
  }
  
  function loadNumProgOrigin(){
	  document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP%>.value = "";
	  var numProg = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP_ORIGIN%>.value;
      var ufficioAccorpato = document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_SEDE_UFF_ACCORPATO%>.value;
      var parts=ufficioAccorpato.split("-"); 
      var offSetInt = parseInt(parts[0]);
      if (numProg){
    	  var newProg = parseInt(numProg) + offSetInt;
    	  document.LoadInserisciRifFascicoloSiep.<%=ICostantiRifFascicoloSiep.CAMPO_PROGR_FASCICOLO_SIEP%>.value = newProg;
          }
	  }
  </script>
  </body>
</html>