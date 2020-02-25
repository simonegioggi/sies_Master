<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria" %>

<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUSTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="collaboratore" scope="request" class="java.lang.String"/>

<%
	// Flag che abilita il filtro sul Collaboratore di Giustizia
	boolean abilitaCollaboratore = false;
	if (collaboratore != null && collaboratore.equalsIgnoreCase("SI"))
			abilitaCollaboratore = true;
%>

<html>
<head>
	<title> [S.I.E.S.] - Ricerca Procedimento Per Estremi Atto - </title>
  	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
		// Funzioni utilizzate per abilitare o disabilitare i campi per la modifica dell'Ufficio.
		// Vengono chiamate dalla radio-button del Collaboratore di Giustizia perchè 
		// per questo filtro la ricerca deve essere ristretta al solo Ufficio dell'utente.
		function bloccaUfficio() {
			checkTipoUfficio();
			document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.disabled = true;
			document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.disabled = true;
			flagBloccaUfficio = true;
		}
		function sbloccaUfficio() {
		  	document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.disabled = false;
		  	document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.disabled = false;
		  	flagBloccaUfficio = false;
		}
		// MERGE v10: ripristinata questa funzione
		function checkTipoUfficio() {
			if ("<%=TipoUfficioConnesso%>" == 'TDS') {
		        document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=1;
		        document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
	      	} else if ("<%=TipoUfficioConnesso%>" == 'UDS') {
	      		document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=2;
	        	document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
	     	} else if ("<%=TipoUfficioConnesso%>" == 'TDSM') {
	      		document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=3;
	        	document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
	     	} else if ("<%=TipoUfficioConnesso%>" == 'UDSM') {
	      		document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=4;
	        	document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
	     	}
	    }
		// Inizializza la RadioBotton del Collaboratore (se esiste)
		function resetCollaboratore() {
		  	<% if (abilitaCollaboratore) { %>
		      document.f.filtroCollaboratore[0].checked = true;
			<% } %>  
			return;
		}

    var desktop;
    var flagBloccaUfficio = false;
    
    // Lista Procure per Distretti ( TDS )oppure Lista UDS
    function ListaTDS_UDS(a_formname,a_fieldname)
    {
      if (flagBloccaUfficio)
      	return;
    	
      var valore = document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      var i = document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex;
      if ( i == 0 || i == 2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    }

    function radio()
    {
      var nodeIntervDataIscr;
      var nodeIntervDataArrivo;
      var nodeIntervDataAtto;
	    var nodeEstremiPendenti;
	    var nodeEstremiDefiniti;
	    var nodeEstremiTutti;

	    checkTipoUfficio();

      nodeIntervDataIscr=document.getElementById('intervalloDataIscr');
      nodeIntervDataArrivo=document.getElementById('intervalloDataArrivo');
      nodeIntervDataAtto=document.getElementById('intervalloDataAtto');
	    nodeEstremiPendenti=document.getElementById('estremiPendenti');
	    nodeEstremiDefiniti=document.getElementById('estremiDefiniti');
	    nodeEstremiTutti=document.getElementById('estremiTutti');

      if(document.f.tipoRicerche[0].checked)
      {
        document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>[0].selected;

        pulisciCampiTipoRicerca();
        nodeIntervDataIscr.style.visibility='visible';
        nodeIntervDataArrivo.style.visibility='hidden';
        nodeIntervDataAtto.style.visibility='hidden';
        document.f.valoreRadio.value='0';
        document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
      }
      else if (document.f.tipoRicerche[1].checked )
      {
        pulisciCampiTipoRicerca();
        nodeIntervDataIscr.style.visibility='hidden';
        nodeIntervDataArrivo.style.visibility='visible';
        nodeIntervDataAtto.style.visibility='hidden';
        document.f.valoreRadio.value='1';
        document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE%>.focus();
      }
      else if (document.f.tipoRicerche[2].checked )
      {
        pulisciCampiTipoRicerca();
        nodeIntervDataIscr.style.visibility='hidden';
        nodeIntervDataArrivo.style.visibility='hidden';
        nodeIntervDataAtto.style.visibility='visible';
        document.f.valoreRadio.value="2";
        document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE%>.focus();
      }
      
      if(document.f.<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>[0].checked)
      {
        pulisciCampiStatoProcedimento();
        nodeEstremiTutti.style.visibility='visible';
        nodeEstremiPendenti.style.visibility='hidden';
				//alert('nodeEstremiPendenti.style.visibility = '+nodeEstremiPendenti.style.visibility);
        nodeEstremiDefiniti.style.visibility='hidden';
        document.f.valoreStatoProcedimento.value='0';
      }
      else if(document.f.<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>[1].checked)
      {
        pulisciCampiStatoProcedimento();
        nodeEstremiTutti.style.visibility='hidden';
        nodeEstremiPendenti.style.visibility='visible';
        nodeEstremiDefiniti.style.visibility='hidden';
        document.f.valoreStatoProcedimento.value='1';
      }
      else if(document.f.<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>[2].checked)
      {
        pulisciCampiStatoProcedimento();
        nodeEstremiTutti.style.visibility='hidden';
        nodeEstremiPendenti.style.visibility='hidden';
        nodeEstremiDefiniti.style.visibility='visible';
        document.f.valoreStatoProcedimento.value="2";
      }
    }

    function pulisciCampiTipoRicerca()
    {
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_FINALE%>.value='';
    }

    function pulisciCampiStatoProcedimento()
    {
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_FINALE%>.value='';
      document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_FINALE%>.value='';
    }

    function Verify()
    {
      if(document.f.tipoRicerche[0].checked)
      {
        // Controllo dell'intervallo date iscrizione.
        var gg_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
        var gg_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
      }else if(document.f.tipoRicerche[1].checked)
      {
        // Controllo dell'intervallo date arrivo in cancelleria.
        var gg_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_INIZIALE%>.value;
        var gg_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_FINALE%>.value;
      }else if(document.f.tipoRicerche[2].checked)
      {
        // Controllo dell'intervallo date atto.
        var gg_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_INIZIALE%>.value;
        var gg_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_FINALE%>.value;
      }

      var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
      var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

			// 22/12/2008 Tipo Ufficio Obbligatorio.
      if (document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex==0)
      {
        alert ("Selezionare il Tipo Ufficio");
        return false;
      }

			// 22/12/2008 Sede Ufficio Obbligatorio.
      var DescrUfficio = document.f.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value;
      if ( DescrUfficio.length==0 )
      {
        alert ("Selezionare la Sede Ufficio");
        return false;
      }

      if (dataIni.length != 2 && dataIni.length != 10)
      {
        alert ("Data di inizio periodo errata");
        return false;
      }
      if (dataFine.length != 2 && dataFine.length != 10)
      {
        alert ("Data di fine periodo errata");
        return false;
      }
      else if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) )
      {
        // entrambe le date valorizzate
        alert ("Errore nella data : " + dataIni);
        return false;
      }
      else if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
      {
        alert ("Errore nella data : " + dataFine);
        return false;
      }
      else if ((dataFine.length == 10)               &&
               ( dataIni.length == 10)               &&
               CompareDate(dataIni,dataFine)== false )
      {
        alert ("Data di Fine minore di Data inizio periodo");
        return false;
      }
      else if ((dataFine.length == 10)                       &&
                CompareDate(dataFine, data_sistema)== false)
      {
        alert ("Data di Fine maggiore di Data sistema");
        return false;
      }

      if(document.f.statoProcedimento[1].checked)
      {
        // Controllo della data fine pendenza.
        var gg_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA%>.value);
        var aa_fi = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA%>.value;
      	var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
	      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

	      if ( dataFine.length < 10 )
	      {
	        alert ("Valorizzare la Data fine Pendenza");
					document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
	        return false;
	      }
      	if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
      	{
        	alert ("Errore nella data fine pendenza : " + dataFine);
        	return false;
      	}
      	if ((dataFine.length == 10)	 &&   CompareDate(dataFine, data_sistema)== false)
      	{
        	alert ("Data di Fine Pendenza maggiore della data di sistema");
        	return false;
      	}
      	
      }else if(document.f.statoProcedimento[2].checked)
      {
        // Controllo dell'intervallo date definizione.
        var gg_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>.value;
        var gg_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_FINALE%>.value;
				var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
      	var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
	      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

	      if ( dataIni.length < 10 )
	      {
	        alert ("Valorizzare la Data inizio Definizione");
					document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>.focus();
	        return false;
	      }

      	if (dataIni.length != 2 && dataIni.length != 10)
      	{
        	alert ("Data di inizio periodo definizione errata");
        	return false;
      	}

      	if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) )
      	{
        	alert ("Errore nella data : " + dataIni);
        	return false;
      	}

	      if ( dataFine.length < 10 )
	      {
	        alert ("Valorizzare la Data fine Definizione");
					document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE%>.focus();
	        return false;
	      }

      	if (dataFine.length != 2 && dataFine.length != 10)
      	{
        	alert ("Data di fine periodo definizione errata");
        	return false;
      	}

      	if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
      	{
        	alert ("Errore nella data : " + dataFine);
        	return false;
      	}
       	// entrambe le date valorizzate
      	if ((dataFine.length == 10)             &&
               	( dataIni.length == 10)              &&
               	CompareDate(dataIni,dataFine)== false )
      	{
        	alert ("Data di Fine minore di Data inizio periodo definizione");
        	return false;
      	}
      	if ((dataFine.length == 10)                       &&
                	CompareDate(dataFine, data_sistema)== false)
      	{
        	alert ("Data di Fine definizione maggiore di Data sistema");
        	return false;
      	}
      }

      /* OK Date presenti */
      
      // Si riabilitano i campi di input per l'ufficio
      sbloccaUfficio();
     }

    function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2)
    {
      var TipoUff = document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
      
  </script>
</head>

<body class="corpo" onLoad="radio(); resetCollaboratore();">

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaProcedimentoPerEstremi">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento Per Estremi Atto</font>
      </td>
    </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="label">Indicare gli estremi dell'atto :</td>
    </tr>
    <tr>
      <td class="l">Tipo Ufficio <font class=ob>(*)</font></td>
      <td class="L">
        <select title="tipoUfficioSIUSTrattino" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" >
          <%= tipoUfficioSIUSTrattino %>
        </select>
      </td>
    </tr>
 
    <tr>
      <td class="l">Sede  <font class=ob>(*)</font></td>
      <td class="l">
         <input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>"  type="text" maxlength="35" size="35">
          <a  href="Javascript:ListaTDS_UDS('f','<%= ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO %>');" >
          <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
 
    <tr>
      <td class="l">Tipo Atto </td>
      <td class="L">
        <select title="tipoAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" >
          <%= tipoAtto %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Magistrato </td>
      <td class="L" >
        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" >
          <%= magistrato %>
        </select>
      </td>
    </tr>
    <jsp:include page="<%=ICostantiCancelleriaAssegnataria.PG_CANCELLERIE_COMBO%>">
      <jsp:param name="nulla" value="nulla"/>
      <jsp:param name="nocanc" value="nocanc"/>
   </jsp:include>
    <table width=90%>
  <tr>
 		<%if (abilitaCollaboratore) {%>     
      <tr><td class="Titolo" >Tipo di Procedimento</td></tr>
      <tr>
	 				<td class="c">Tutti &nbsp;<input type="radio" name="filtroCollaboratore" value="tutti" checked  onClick="sbloccaUfficio();">
						&nbsp;&nbsp;&nbsp;&nbsp;Procedimenti collegati a Collaboratore &nbsp; <input type="radio" name="filtroCollaboratore" value="SI" onClick="bloccaUfficio();">
						&nbsp;&nbsp;&nbsp;&nbsp;Procedimenti non collegati a Collaboratore &nbsp; <input type="radio" name="filtroCollaboratore" value="NO" onClick="bloccaUfficio();">
	 			</td>
	 		</tr>
	<%}%>
 <tr><td>&nbsp;</td></tr>	 
      <tr><td class="Titolo">Stato Procedimento</td></tr>
     <tr>
	 			<td class="c">
	 					Tutti &nbsp;<input type="radio" name="<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>" value="tutti" onClick="radio();" checked>
		       &nbsp;&nbsp;&nbsp;&nbsp;Solo Pendenti &nbsp; <input type="radio" name="<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>" value="pendenti" onClick="radio();">
		       &nbsp;&nbsp;&nbsp;&nbsp;Solo Definiti &nbsp; <input type="radio" name="<%=ICostantiFascicoloSius.RADIO_STATO_PROCEDIMENTO%>" value="definiti" onClick="radio();">

	 			</td>
 		</tr>
 		
	<div id="estremiTutti" style="position:relative;  top: 0; left: 0; visibility:hidden;">  
		<table width="90%">
			<tr><td>&nbsp;</td></tr>
	      	<tr><td>&nbsp;</td></tr>
		</table>
  	</div>

   	<div id="estremiPendenti" style="position:relative; top: -40; left: 0; visibility:hidden;">  
     	<table width="90%">
	    	<tr>
	      		<td colspan="2" class="Titolo">Indicare la data di fine Pendenza </td>
	    	</tr>
      		<tr>
        		<td class="l">
          			<font class="label">Data Fine Pendenza<font class=ob>(*)</font></font>
       				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          			<input type="text" title="Giorno Fine Pendenza" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Mese Fine Pendenza" name="<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Anno Fine Pendenza" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENDENZA%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        		</td>
       		</tr>
    	</table>
  	</div>

  	<div id="estremiDefiniti" style="position:relative; top:-90; left: 0; visibility:hidden;">
    	<table width="90%">
	    	<tr>
	      		<td colspan="2" class="Titolo">Intervallo Date Definizione </td>
	    	</tr>
      		<tr>
        		<td class="l">
          			<font class="label">Data Iniziale <font class=ob>(*)</font></font>
       				<input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
          			<font class="label">Data Finale <font class=ob>(*)</font></font>
          			<input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DEFINIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_DEFINIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          			<input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_DEFINIZIONE_FINALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        		</td>
       		</tr>
    	</table>
  	</div>
	</table>

    <table width=90% style="position:relative; top:-85;">
      <tr><td class="Titolo" >Tipo Ricerca</td></tr>
<tr>
         <td class="c">Data Iscrizione &nbsp;<input type="radio" name="tipoRicerche" value="iscrizione" checked  onClick="radio();">
                       &nbsp;&nbsp;&nbsp;&nbsp;Data arrivo in Cancelleria &nbsp; <input type="radio" name="tipoRicerche" value="arrivo"  onClick="radio();">
                       &nbsp;&nbsp;&nbsp;&nbsp;Data atto &nbsp; <input type="radio" name="tipoRicerche" value="atto"  onClick="radio();">
         </td>
      </tr>
    </table>

    <div id="intervalloDataIscr" style="width: 100%; visibility:hidden; position:relative; top: -86;" >
      <table width=90%>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date di Iscrizione</td>
        </tr>
        <tr>
          <td class="L" width="20%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="L" >
            <font class="label">Data Finale </font>
          </td>
          <td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
      </table>
    </div>

    <div id="intervalloDataArrivo" style="width: 100%; visibility:hidden; position:relative; top: -135px;" >
      <table width=90%>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date di Arrivo in cancelleria</td>
        </tr>
        <tr>
          <td class="L" width="20%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_INIZIALE%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="L" >
            <font class="label">Data Finale </font>
          </td>
          <td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ARRIVO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ARRIVO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ARRIVO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>
    </div>

    <div id="intervalloDataAtto" style="width: 100%; visibility:hidden; position:relative; top: -184px;">
      <table width=90%>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date Atto</td>
        </tr>
        <tr>
          <td class="L" width="20%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="L" >
            <font class="label">Data Finale </font>
          </td>
          <td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ATTO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ATTO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ATTO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
      </table>
    </div>
  </table>

  <table width=90% style="visibility:visible; position:relative; top: -164px;">
  <tr><td class="L">
      &nbsp;&nbsp;<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return Verify();">
      </td></tr>
  </table>

  <input type="HIDDEN" name="valoreRadio" value="">
  <input type="HIDDEN" name="valoreStatoProcedimento" value="">

		</form>
<!-- MERGE v10: commento codice di troppo -->
<!-- <script language="JavaScript" type="text/javascript"> -->
<!-- var frmvalidator = new Validator("f"); -->
<!-- </script> -->
	</body>
</html>