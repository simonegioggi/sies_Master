<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.ProfileModel"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page
	import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page
	import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="UtenteConnesso" scope="session"
	class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="provvedimento" scope="request"
	class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel" />
<jsp:useBean id="evento" scope="request"
	class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="misura" scope="request"
	class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" />

<jsp:useBean id="tipoinserimento" scope="request"
	class="java.lang.String" />
<jsp:useBean id="idsoggetto" scope="request" class="java.lang.String" />
<jsp:useBean id="idordinanza" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimenti" scope="request"
	class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="naturaMisuraSicurezza" scope="request"
	class="java.lang.String" />
<jsp:useBean id="VectipoMisuraSicurezza" scope="request"
	class="java.util.Vector" />
<jsp:useBean id="NumerazioneManualeMisureProvvFS" scope="request"
	class="java.lang.String" />
<% // S/N indica l'esistenza di almeno un procedimento di classe IV per l'anno corrente %>
<jsp:useBean id="EsisteFascicoloClasseIVAnnoCorrente" scope="request"
	class="java.lang.String" />
<jsp:useBean id="AnnoCorrente" scope="request" class="java.lang.String" />


<!--    LoadIscrizioneProcedimentoMisuraFuoriSentenza    -->

<%
// Tipo Iscrizione : Misura FUORI SENTENZA
  String TipoIscrMisura = "FUORI_SENTENZA";

//La action è la stessa per i 2 tipi di iscrizione : PROVVISORIA e FUORI SENTENZA
  String lAzione = new String();    
  lAzione = "siap.siep.misurasicurezza.action.ActIscrizioneProcApplicazioneMisuraProvvisoria";

  // gestione Inserimento Misure Sicurezza
  String lcod = "";
  String lnat = "";
  
	if(misura != null && misura.getIdMisuraSicurezza()!=null)
	{ 
      lcod = misura.getCodTipo();
      lnat = misura.getCodNatura();
    }
    
    String lLuogoEsec = "";
    if(misura != null && misura.getIdMisuraSicurezza()!=null && misura.getLuogoEsecuzioneMisura()!=null)
    { 
   	 	lLuogoEsec = misura.getLuogoEsecuzioneMisura();
    }
  
  String strOggetto ="";
  int element = 0;
  int eledaModificare = 0;
    Iterator itx = VectipoMisuraSicurezza.iterator();
    while(itx.hasNext())
    {
         DecodificheModel lDecMod = (DecodificheModel)itx.next();
  
         strOggetto += lDecMod.getFiltro() +";";
         strOggetto += lDecMod.getCode()+";";
         strOggetto += lDecMod.getDescription()+"#";
         if(lDecMod.getFiltro().equals(lnat))
         {  
             element++;
             if(lDecMod.getCode().equals(lcod))
             {
               eledaModificare = element;
             }
         }       
     }
%>
<html>
<head>
<title>Gestione Misure Sicurezza disposte Fuori Sentenza-
	Iscrizione Procedimento</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript"
	src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>

<script language="JavaScript">
  
   // Gestione Misure Sicurezza
  var strOggetto = "<%=strOggetto%>";
  var TipodaModificare ="<%= eledaModificare%>";
  
  function caricaComboMod (valueTextStr, sep1, sep2, filtro, selField)
  {
    caricaCombo(valueTextStr, sep1, sep2, filtro, selField);
	selField.options.selectedIndex = TipodaModificare-1;
  }
  
  function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
      // valueTextStr = stringa nel formato richiesto
      // sep1 = separatore interno alla coppia di valori
      // sep2 = separatore tra coppie
      // filtro = valore su cui fare il test
      // selField = oggetto combo da caricare
  
      clearDropDown(selField);
  
      var aPairs = valueTextStr.split(sep2);
  
      if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
      {
        aPairs[aPairs.length - 1] = null;
        aPairs.length--;
      }
  
      for (var i=0; i < aPairs.length; i++)
      {
        aValueText = aPairs[i].split(sep1);
        if (filtro=='null' || filtro==aValueText[0])
        {
          oItem = new Option;
          oItem.value = aValueText[1];
          oItem.text = aValueText[2];
          selField.options[selField.options.length] = oItem;
        }
      }
  
      selField.options.selectedIndex = 0;
      //se il valore del filtro è "-" disabilito il campo
      if(filtro=='-'){
        selField.disabled=true;
      }
      else{
        selField.disabled=false;
      }

  }
  
  function clearDropDown (selField)
  {
      while (selField.options.length > 0)
      selField.options[0] = null;
  }
  
  // 10/06/2010 Lista Uffici per TIPO_UFFICIO
  var desktop;
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
  {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaComuni(a_formname,a_fieldname)
  {
  var desktop;
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  <!-- 20210524	MEV Scheda-21 -->
  function ListaComuniNascita(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
  }      
  
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
     function controllaEtaSoggetto()
      {
        var tipoUff = "<%=UtenteConnesso.getUfficioUtente().getCodTipoUfficio()%>";
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value);
        var mese = 1;
        var giorno = 1;
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);


        if (tipoUff == "PMM" || tipoUff ==  "DIBM")
        {
           // caso Tribunale dei Minori
           if (oggi < data_compleanno14 )
             ritorno = window.confirm('Il soggetto non ha compiuto i 14 anni! Confermi il suo inserimento?');
           if ( oggi > data_compleanno18)
             ritorno = window.confirm('Il soggetto ha più di 18 anni! Confermi il suo inserimento?');
          
        }
        else
        {
           if (oggi < data_compleanno18)
              ritorno = window.confirm('Il soggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');
        }

    
        return ritorno;
      }
      
      function cancellaCodComuneReale() {
      
        document.LoadInserisciSentenza.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";        
      }    
    

    //==========================================================================
    //
    //==========================================================================
    function Verify() 
    {
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      
      if(!document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled)
      {
          if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value=='')
          {
              alert('Il campo Cognome Soggetto è obbligatorio');
              document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
              return false;
          }
          
          if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.value=='')
          {
              alert('Il campo  Nome Soggetto è obbligatorio');
              document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.focus();
              return false;
          }
          
          if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
              document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
          if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
              document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
          if(document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'N')
          {
              var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
              if (data_to_verify=="//"){
                alert('Data di nascita obbligatoria');
                document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.focus();
                return false;
              }
              else if (! ControllaData(data_to_verify))
              {
                alert('Data di nascita non valida');
                document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.focus();
                return false;
              }
          }
        
          if (document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
          {
              document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
              if (document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value == "" || 
                document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value == "-" )
              {
                  alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
                  document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.focus();
                  return false;
              }
          }
          else 
          {
            document.LoadInserisciSentenza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';
            cancellaCodComuneReale();
          }
       }
       
       var lRitorno = true
       if(!document.LoadInserisciSentenza.<%=ICostantiSoggetto.CAMPO_NOME%>.disabled)
       {
         lRitorno = controllaEtaSoggetto();
       } 
/***************************PROVVEDIMENTO (sentenza)*********************************************************/

        if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
      
        //Data Provvedimento
        var d2=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (d2=="//"){
          alert('Data Provvedimento obbligatoria');
          document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
          return false;
        }
        else if (! ControllaData(d2))
        {
            alert('Data Provvedimento non valida');
            document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.focus();
            return false;
        }
    
        if(document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.value == "-" )
        {
            alert('Tipo Provvedimento non valido');
            document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>.focus();
            return false;       
        }
      
        if(document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value == "-")
        {
            alert('Tipo Autorità Emittente non valido');
            document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.focus();
            return false;       
        } 
        
        if(document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>.value == "")
        {
            alert('Luogo Emittente Obbligatorio');
            document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>.focus();
            return false;       
        }
 

       
    /***************************  MISURA SICUREZZA  *********************************************************/
    
       if(document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value == "-")
       {
            alert('Natura Misura Sicurezza NON Valida');
            document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA  %>.focus();
            return false;        
       }  
    
       if(!document.LoadInserisciSentenza.<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>.disabled)
       {
           if(document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>.value == "-" ||
            document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>.value == "")
         {
              alert('Tipo Misura Sicurezza NON Valido');
              document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>.focus();
              return false;        
         }           
       }
       
       // Manca il controlla sulla durata
       
 /*************************** DATE ISCRIZIONE e ARRIVO ATTO *********************************************************/
       
    if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value.length==1)
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value;
    if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value.length==1)
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value;
    
    var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>.value;
    if (data_to_verify=='//'){
      alert('Data ISCRIZIONE obbligatoria');
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.focus();            
      return false;
    }
    else if (! ControllaData(data_to_verify))
    {
      alert('Data ISCRIZIONE non valida');
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.focus();            
      return false;
    }
    
    if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value.length==1)
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value;
    if (document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value.length==1)
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value='0'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value;
    
    var data_to_verify=document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
    if (data_to_verify=='//'){
      alert('Data ARRIVO ATTO obbligatoria');
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.focus();            
      return false;
    }
    else if (! ControllaData(data_to_verify))
    {
      alert('Data ARRIVO ATTO non valida');
      document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.focus();            
      return false;
    }

    // EVENTUALE NUMERAZIONE MANUALE
    if ( typeof (document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>)!="undefined")  
    {
      if (document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value=="")
      {
        alert("Indicare il valore di NUMERO Procedimento SIEP ");
        document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
      else {
        // Verifica delle congruenza del numero digitato con la classe scelta
        var minrange = 40001;
        var maxRange = 49999;

        // Controllo il progressivo    
        if ((document.LoadInserisciSentenza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value < minrange) ||
            (document.LoadInserisciSentenza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value > maxRange) )
        {
          alert ("Il Numero Procedimento SIEP per la Classe IV deve essere compreso tra "+minrange+" e "+maxRange);
          document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
          return false;
        }      
      }
    }

    if ( typeof (document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>)!="undefined")  
    {
    	//alert("CAMPO_CHIAVE_ANNO NON è Undefined");
	    if (document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value=="")
	    {
	      alert("Indicare il valore di ANNO procedimento SIEP");
	      document.LoadInserisciSentenza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
	      return false;
	    }
    }    
    
	return lRitorno;
 
}   // CHIUDE Verify()
    
 </script>
</head>
<body class="corpo"
	onload="javascript:caricaComboMod(strOggetto,';','#',document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>);">

	<!--  body class="corpo" -->

	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"> <img
					align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border="0">
			</a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<font class="campo">Iscrizione Procedimento Esecuzione Misura
					Sicurezza Fuori Sentenza </font></td>
		</tr>
	</table>

	<FORM method="POST" action="Main.jsp" name="LoadInserisciSentenza">
		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"
			value="<%=lAzione%>"> <input type="HIDDEN"
			name="tipoinserimento" value="<%=tipoinserimento%>"> <input
			type="hidden"
			name="<%=ICostantiMisuraSicurezza.CAMPO_TIPO_ISCRIZIONE_MISURA %>"
			value="<%=TipoIscrMisura%>"> <input type="HIDDEN"
			name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"
			value="<%=idsoggetto%>"> <input type="hidden"
			name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_ID_DEPOSITO_ORDINANZA_PC %>"
			value="<%=idordinanza%>"> <input type="HIDDEN"
			name="<%=ICostantiMisuraSicurezza.CAMPO_NUMERAZIONE_MANUALE_MISURE_PROVV_FS%>"
			value="<%=NumerazioneManualeMisureProvvFS %>">

		<%--
/* 
 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
 * Numero MEV : SIES v10
 * Autore    : gioggi
 * Data      : 28/gen/2016
 * Branch    : MEV_SIES v10
 */
<% if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente) ){ %>
    <input type="HIDDEN" name="numerazioneManuale" value="S">
<% } %>
//***** FINE INTERVENTO MEV_SIES v10 *****//
--%>

		<%
   if("soggetto".equals(tipoinserimento))
   { // link per dettglio soggetto Precaricato
 %>
		<input type="hidden"
			name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS %>"
			value="<%=evento.getFasSiuIdFascicoloSius() %>">
		<jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp" />
		<br>
		<% } %>

		<%	if ("S".equals(NumerazioneManualeMisureProvvFS) && 
		!"N".equals(EsisteFascicoloClasseIVAnnoCorrente) )
	{
	%>
		<table>
			<tr>
				<td class="Titolo" colspan=2>Estremi Procedimento SIEP da
					Iscrivere</td>
			</tr>
			<tr>
				<td class="l">Anno e Numero Procedimento <font class="ob">(*)</font></td>
				<td><input type="text" title="Anno Procedimento"
					name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4"
					size="4" onkeypress="return TicTabNumField(this,event)"
					onFocus="javascript:textboxSelect(this)"
					onBlur="javascript:value=FillYear(value)"> / <input
					type="text" title="Numero Procedimento"
					name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"
					maxlength="13" size="15"
					onkeypress="return TicTabNumField(this,event)"></td>
			</tr>
		</table>
		<br>
		<%	} %>

		<!--      DATI SOGGETTO      -->
		<jsp:include
			page="/jsp/files/siap/siep/nuovaistanza/IncludeIstanzaSoggetto.jsp" />

		<table cellspacing=2 cellpadding=2 width="90%">
			<!--        DATI del PROVVEDIMENTO SORVEGLIANZA        -->

			<tr>
				<td class="Titolo" colspan=4>Provvedimento della Sorveglianza</td>
			</tr>
			<tr>
				<td class="l">Data Provvedimento <font class="ob">(*)</font></td>
				<td class="L"><input Title="Data Provv" type="text"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getDataCameraConsiglio(),"dd") ) %>"
					name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data Provv" type="text"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getDataCameraConsiglio(),"MM")) %>"
					name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data Provv" type="text"
					value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getDataCameraConsiglio(),"yyyy")) %>"
					name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
				<td class="l">Anno/Numero Provvedimento</td>
				<td class="L"><input Title="Anno Provv"
					value="<%=StringUtils.toStringJSP(provvedimento.getAnnoS3(),"") %>"
					type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"> /
					<input Title="Numero Provv"
					value="<%=StringUtils.toStringJSP(provvedimento.getNumS3(),"") %>"
					type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>"
					maxlength="6" size="6"></td>
			</tr>
			<tr>
				<td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
				<td class="L"><select Title="Tipo Prevvedimento Riferimento"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_PROVV_RIF %>">
						<%=tipoProvvedimenti%>
				</select></td>
			</tr>

			<tr>
				<td class="l">Autorità Emittente<font class=ob>(*)</font></td>
				<td class="L"><select Title="Autorità Emittente"
					name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
						<%=autoritaEmi%>
				</select></td>
			</tr>
			<tr>
				<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
				<td class="L"><input Title="Luogo Emittente"
					name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
					value="<%=StringUtils.toStringJSP(evento.getDescrLuogoEmittente() ,"")%>"
					type="text" maxlength="35" size="35"> <a
					href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenza.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenza.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
						<img src="/images/filefolder.gif" border=0>
				</a></td>
			</tr>
		</table>

		<!--        MISURA di SICUREZZA da Inserire        -->
		<table cellspacing=2 cellpadding=2 width="90%">
			<tr>
				<td class="Titolo" colspan=4>Misura Sicurezza</td>
			</tr>
			<tr>
				<td class="l">Natura Misura<font class=ob>(*)</font></td>
				<td class="l"><select title="Natura Misura"
					name="<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>"
					onChange="javascript:caricaCombo(strOggetto,';','#',document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_NATURA %>.value, document.LoadInserisciSentenza.<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>);">
						<%=naturaMisuraSicurezza%>
				</select></td>
			</tr>
			<tr>
				<td class="l">Tipo Misura <font class="ob">(*)</font></td>
				<td class="l"><select title="Tipo Misura"
					name="<%= ICostantiMisuraSicurezza.CAMPO_COD_TIPO %>"></select></td>
			</tr>
			<tr>
				<td class="l">Durata Misura<font class=ob>(*)</font></td>

				<td class="l">Anni <input title="Anni" size=2 maxlength=2
					value="<%=StringUtils.toStringJSP(misura.getNumAnni(),"")%>"
					type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_ANNI %>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"> Mesi <input
					title="Mesi" size=2 maxlength=2
					value="<%=StringUtils.toStringJSP(misura.getNumMesi(),"") %>"
					type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_MESI%>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"> Giorni <input
					title="Giorni" size=2 maxlength=2
					value="<%=StringUtils.toStringJSP(misura.getNumGiorni(), "") %>"
					type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_NUM_GIORNI %>"
					onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)">
				</td>
			</tr>
			<tr>
				<td class="l">Luogo Esecuzione Misura</td>
				<td class="L"><textarea
						name="<%=ICostantiMisuraSicurezza.CAMPO_LUOGO_ESECUZIONE_MISURA %>"
						rows="2" cols="80"><%=StringUtils.toStringJSP(lLuogoEsec ,"")%></textarea>
				</td>
			</tr>
		</table>

		<!--        Dati sul Nuovo Procedimento Da Eseguire      -->
		<table cellspacing=2 cellpadding=2 width="90%">
			<tr>
				<td class="Titolo" colspan=2>Provvedimento da Eseguire</td>
			</tr>
			<% if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente) ){ %>
			<tr>
				<td class="l" colspan="2"><font color="red">Attenzione!
						Si sta procedendo all'iscrizione del primo procedimento di Classe
						IV per l'anno corrente. <br>Il progressivo indicato in questa
						fase sarà il valore dal quale partirà la numerazione automatica
						per i successivi procedimenti di classe IV. <br>Una volta
						indicato il progressivo iniziale, sarà possibile acquisire il
						pregresso per l'anno corrente (assegnare numerazione manuale) solo
						per procedimenti con numerazione inferiore a quella indicata i
						questa fase. <br>Come prima iscrizione è necessario quindi
						registrare o un nuovo procedimento assegnandogli opportuno
						progressivo secondo quanto prevede l'attuale registro Misure di
						Sicurezza, oppure registrare l'ultimo procedimento presente sul
						registro Misure di Sicurezza.
				</font></td>
			</tr>
			<% } %>

			<tr>
				<td class="l">Data Iscrizione <font class="ob">(*)</font></td>
				<td class="L"><input Title="Data Iscrizione" type="text"
					value=""
					name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data Iscrizione" type="text" value=""
					name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data Iscrizione" type="text" value=""
					name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
			</tr>
			<tr>
				<td class="l">Data Arrivo Atto <font class="ob">(*)</font></td>
				<td class="L"><input Title="Data arrivo atto" type="text"
					value=""
					name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data arrivo atto" type="text" value=""
					name="<%= ICostantiSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>"
					maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillDM(value)"> - <input
					Title="Data arrivo atto" type="text" value=""
					name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"
					maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
					onkeypress="return TicTabNumField(this,event)"
					onBlur="javascript:value=FillYear(value)"></td>
			</tr>
			<% if ("N".equals(EsisteFascicoloClasseIVAnnoCorrente) ){ %>
			<tr>
				<td class="l">Anno e Numero Procedimento <font class="ob">(*)</font></td>
				<td class="l"><input type="text" title="Anno Procedimento"
					name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4"
					size="4" value="<%=StringUtils.toStringJSP(AnnoCorrente,"")%>"
					readonly onkeypress="return TicTabNumField(this,event)"
					onFocus="javascript:textboxSelect(this)"
					onBlur="javascript:value=FillYear(value)"> / <input
					type="text" title="Numero Procedimento"
					name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"
					maxlength="13" size="15"
					onkeypress="return TicTabNumField(this,event)"></td>
			</tr>
			<% } %>
			<tr>
				<td class="l">Note</td>
				<td class="l"><TEXTAREA cols="80" rows="3"
						name="<%= ICostantiSentenza.CAMPO_NOTE %>"></textarea>
					<%-- maxlength="2000" --%></td>
			</tr>

			<tr>
				<td class="l"><input class="bottone" type="submit"
					name="conferma" value="Conferma"></td>
			</tr>
		</table>
	</form>
	<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciSentenza");



/**************************soggetto****************************************/
//soggetto
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");
  
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");
  
  
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
  
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_AFIS %>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NAZIONALITA%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");
  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha");
  /**************************fine soggetto****************************************/

  /**************************Sentenza****************************************/    
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
  
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");
  
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  
<%--   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic"); --%>
<%--   frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","req", "Luogo Autorità Emittente Obbligatorio"); --%>

  /**************************FINE Sentenza ****************************************/  
 
  frmvalidator.setAddnlValidationFunction("Verify"); 
</script>
</body>
</html>