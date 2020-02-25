<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>

<jsp:useBean id="oggetto"             scope="request" class="java.lang.String" />

<%// Pena in decorrenza %>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%// Destinatari%>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAltra" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>


<%
//==============================================================================
// form per l'inserimento del Provvedimento di Rideterminazione Pena 'Altro'
// menu: 'Rideterminazione Pena - Provvedimenti del PM - Altro'
//
// - Posizione giuridica
// - Pena Residua In espiazione/Da espiare
// -
// -
//==============================================================================

	boolean visualizzaDataScarcerazione = false;
	
	Date dataScarcerazione = (Date)request.getAttribute("dataScarcerazione");
	
	BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
	BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  // Per tutti i provvedimenti che lo gestiscono
  // aggiungere campo OBBLIGATORIO editabile CAMPO_CASELLARIO
  //       1. precaricato a '-' se lo stato nascita dell'imputato è blank
  //       2. altrimenti COD_UFFICIO dell'utente collegato
  // ad eccezione dei quattro provvedimento sotto elencati.
  // SOLO per questi 4 provvedimenti e se l'imputato è straniero (stato nascita diverso da ITALIA) --> casellario = ROMA
  //   -- Computo fungibilità
  //   -- Unificazione delle pene concorrenti
  //   -- Rideterminazione della pena
  //   -- Sospensione pena 656 
  SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
  
  UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
   
  String lCasellario = lUfficioUtenteConnesso.getDescrComune();

  if( lSoggettoAssociato != null 
      && 
      	( 
      	   !"039".equals(lSoggettoAssociato.getCodStatoNascita())
      	 )
     )
  {
    lCasellario = "ROMA";
  }

  if( lSoggettoAssociato != null 
      && 
      	(    lSoggettoAssociato.getCodStatoNascita() == null
      	  ||  "".equals(lSoggettoAssociato.getCodStatoNascita()) 
      	  || "-".equals(lSoggettoAssociato.getCodStatoNascita())
      	 )
     )
  {
    lCasellario = "-";
  }
%>

<head>
  <title> [S.I.E.S.] - Annotazioni Manuali - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">

  function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  var data_to_verify;
  var obbligatori;
  var dataesiste;
  
  //============================================================================
  //
  //============================================================================
  function Verify()
  {
  	//Se il "Foglio Complementare" è selezionato, il "Casellario Giudiziale" è obbligatorio
  	if( document.f.<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>.checked == true )
  	{
     		if (   document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
     		    || document.f.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
     		   )
        {
          alert("Il campo Casellario Giudiziale è obbligatorio!");

          return false;
        }		  	
  	} 

    if (document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>[document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.selectedIndex].value=="-")
    {
      alert("Selezionare Oggetto");
      document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO %>.focus();
      return false;
    }

    if (document.f.PM[document.f.PM.selectedIndex].value == "")
    {
      alert ("Selezionare tra + e -");

      return false;
    }

    obbligatori = (document.f.GRec.value!="" || document.f.MRec.value!="" || document.f.ARec.value!="");
    obbligatori = obbligatori || (document.f.Ammenda.value!="")
    obbligatori = obbligatori || (document.f.GArr.value!="" || document.f.MArr.value!="" || document.f.AArr.value!="");
    obbligatori = obbligatori || (document.f.Multa.value!="");

    if (!obbligatori)
    {
      alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");
      return false;
    }

    //===================================
    // Data emissione e data trasmissione
    //===================================
    if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Emissione non valida');
      return false;
    }


    if (document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.f.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.f.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.f.<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Trasmissione non valida');
      return false;
    }

    //===================================
    // Data Scarcerazione
    //===================================
    <% if (PenRes1!=null && PenRes1.getDataFine()!=null
           && visualizzaDataScarcerazione
          ) 
    { %>
    if (document.f.<%= ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
      document.f.<%= ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.f.<%= ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
    if (document.f.<%= ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
      document.f.<%= ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.f.<%= ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;

    var data_to_verify = document.f.<%= ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value+'-'+
                         document.f.<%= ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value+'-'+
                         document.f.<%= ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
                         
    if (!ControllaDataPassaVuota(data_to_verify) )
    {
      alert('Data di Scarcerazione non valida');
      return false;
    }

    //======================================================================
    // Verifico se data fine pena < data sistema e in questo caso se è stata 
    // indicata la data scarcerazione
    //======================================================================
    var data_odierna = '<%=DateUtils.getDateToString(new Date(),"dd-MM-yyyy")%>';
    var data_fine = '<%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd-MM-yyyy")%>';
    //alert ( CompareDate(data_odierna,data_fine)  );
    if (data_to_verify.length==2 && CompareDate(data_odierna,data_fine)==false) {
      //alert('pippo');
      // data odierna > data fine pena
      if (!window.confirm('Attenzione! La data fine pena risulta già trascorsa. Se non si specifica la Data Eventuale Scarcerazione, il sistema\n '+
                          'effettuerà i calcoli della fungibilità alla data odierna. Altrimenti va specificata la data di effettiva scarcerazione. \n'+
                          'Si vuole procedere?')){
        return false;
      }
    }
    <% } %>

//destinatari
  if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
  {
    alert("Il Magistrato Firmatario è obbligatorio");
    document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
    return false;
  }

<%
if(lPosizione.getCodPosizioneGiuridica() != null &&
  (lPosizione.getCodPosizioneGiuridica().equals("01") || lPosizione.getCodPosizioneGiuridica().equals("02")
   || lPosizione.getCodPosizioneGiuridica().equals("03") || lPosizione.getCodPosizioneGiuridica().equals("04")
   || lPosizione.getCodPosizioneGiuridica().equals("23") || lPosizione.isMisSosp()))
{
%>
  if(document.f.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
  {
    alert("Il Campo Istituto di Detenzione è obbligatorio");
    return false;
  }

<%
}

if(   lPosizione.getCodPosizioneGiuridica() != null 
   && (   lPosizione.getCodPosizioneGiuridica().equals("05")
       || lPosizione.getCodPosizioneGiuridica().equals("08") 
       || lPosizione.isLibero() 
       || lPosizione.isMisuraAlternativa()
      )
  )
{
%>
 if (document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>[document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.selectedIndex].value == '-')
  {
    alert("Il Campo Autorità di destinazione è obbligatoria");
       return false;
  }
<%
}

if(lPosizione.isMisuraAlternativa())
{
%>
 if (document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>[document.f.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.selectedIndex].value == '-')
  {
    alert("Il Campo Autorità  di controllo è obbligatorio");
       return false;
  }
<%
}

if(lPosizione.isMisuraAlternativa())
{
%>
  if(document.f.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" || document.f.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
  {
    alert("L' UEPE è obbligatorio");
    return false;
  }

  if(document.f.<%=ICostantiNotifica.CAMPO_SEDE_MDS%>.value=="")
  {
    alert("La Sede del Magistrato di Sorveglianza è obbligatoria");
    return false;
  }

  if(document.f.<%=ICostantiNotifica.CAMPO_SEDE_TDS%>.value=="")
  {
    alert("La Sede del Tribunale di Sorveglianza è obbligatoria");
    return false;
  }
<%
}
%>

    //document.f.inserisci.disabled=true;
    //document.f.submit();
    // il return serve alla funzione javascipt (IWebConstants.JS_VALIDATOR) per disabilitare
  // il tasto di conferma del form
    return true;
  }

//Liste
  var desktop;
  function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

  function ListaUDS(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  function ListaComuniTds(formname,fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function toggleComuneCasellario()
	{
		vistaLabel = (document.getElementById("labelCasellario").style.display == 'none') ? 'block' : 'none';
		document.getElementById("labelCasellario").style.display = vistaLabel;

		//vistaInput = (document.getElementById("inputCasellario").style.display == 'none') ? 'block' : 'none';
		//document.getElementById("inputCasellario").style.display = vistaInput;
	}
</script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciRidetPenaAltro">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena Altro</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua
//==============================================================================
%>
  <table>
    <tr>
      <td class="l">
      Posizione Giuridica :
      <font class="campo">
<%
      if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
%>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
<%
      }
      else
      {
%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
      }
%>
      </font>
    </td>
  </tr>
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio, MAI
// - Se Libero vengono visualizzati i Quantum
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<%
  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
     && PenaComplessiva.getCodTipoPenaDetentiva() != ""
     && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
         || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
        )
    )
  {
%>
    <table style="width: 95%;">
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else if (!PenRes1.getErrorMsg().equals("-"))
  {
%>
  <table style="width: 95%;">
    <tr>
      <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) {%>
      <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
      <% } else  {%>
      <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
      <% } %>
    </tr>

    <% if (PenRes1.getErrorMsg().startsWith("Libero")) { %>
    <tr>
      <td class="l"> Reclusione :
        Anni   <font class=campo><%=PenRes1.getNumAnni()%></font>
        Mesi   <font class=campo><%=PenRes1.getNumMesi()%></font>
        Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
        Multa  <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
        Anni    <font class=campo><%=PenRes2.getNumAnni()%></font>
        Mesi    <font class=campo><%=PenRes2.getNumMesi()%></font>
        Giorni  <font class=campo><%=PenRes2.getNumGiorni()%></font>
        Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>
    <%
    }
    else //non libero
    {
      //========================================================================
      // Visualizzo la pena residua calcolata al volo tra la data odierna e la
      // data fine pena prevista
      //========================================================================
      CalendarUtil lCU = new CalendarUtil();
      PenRes2.setDataFine   (PenRes1.getDataFine());
      PenRes2.setDataInizio (DateUtils.getSysDate());
      PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
    %>
    <tr>
      <td class="l">
        Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
        Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
        Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>
      </td>
      <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class=l>Data Inizio : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%> </font></td>
      <td class=lNoBord>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class=l>Data Fine : <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy") %> </font></td>
    </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>
    <% } %>
</table>
<%
  }  // Fine Pena Residua
%>

<%
//==============================================================================
// Sezione con i dati del provvedimento
//==============================================================================
%>

<table style="width: 95%;">
  <tr>
    <td colspan=8 class="Titolo">Rideterminazione della pena</td>
  </tr>
  <tr>
    	<td class="l">
    		<input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>" checked>
     		Foglio Complementare
     	</td>
     	<td class="l" id="labelCasellario" style="display:block;">Casellario Giudiziale&nbsp;
<!--      	
     	</td>
     	<td class="l" id="inputCasellario" style="display:block;">
 -->
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
  </tr>
  <tr>
    <td class="l" width="33%">Oggetto <font class="ob">(*)</font> :&nbsp;</td>
    <td class="l"><select name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"><%=oggetto%></select></td>
  </tr>
</table>

<table style="width: 95%;">
  <tr><td colspan=6><hr width="100%"></td></tr>
  <tr>
    <td valign="middle" class=c rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="PM">
        <option value=""></option>
        <option value="+">+</option>
        <option value="-">-</option>
      </select>
  </td>
  <td class=titolo colspan=2>Reclusione</td>
  <td width=25>&nbsp;</td>
  <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" name="ARec" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="MRec" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="GRec" maxlength="4" size="4" value="">
      </td>
      <td class="c">
        <font class="label">Multa</font><br>
        <input style="align:right" type="text" name="Multa" maxlength="8" size="6" value="">
        ,
        <input style="align:right" type="text" name="Mul_dec" maxlength="2" size="2" value="">
      </td>
      <td width=25>&nbsp;</td>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" name="AArr" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="MArr" maxlength="2" size="2" value="">&nbsp;
        <input type="text" name="GArr" maxlength="4" size="4" value="">
      </td>
      <td class="c">
        <font class="label">Ammenda</font><br>
        <input style="align:right" type="text" name="Ammenda" maxlength="8" size="6" value="">
        ,
        <input style="align:right" type="text" name="Amm_dec" maxlength="2" size="2" value="">
      </td>
    </tr>
    <tr>
      <td class="c" colspan=1><font class="label">Motivazioni:</font></td>
      <td class="c" colspan=4><textarea cols="60" rows="2" name="motivazioni"></textarea></td>
      <td width=20>&nbsp;</td>
     </tr>
    <!-- DATA SCARCERAZIONE -->
    <% if (   PenRes1!=null && PenRes1.getDataFine()!=null
           && visualizzaDataScarcerazione
          ) 
    { %>
    <tr>
      <td class="l" colspan="100%">
        <font class="label">Data Eventuale Scarcerazione</font>
        &nbsp;&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" 
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getDayToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"  
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getMonthToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"  
               <% if (dataScarcerazione!=null) { %>
               value="<%=DateUtils.getYearToString(dataScarcerazione)%>" 
               <% } else {%>
               value=""
               <% } %>
               onFocus="javascript:textboxSelect(this)" 
               onkeypress="return TicTabNumField(this,event)" 
               onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <% } %>
    <tr><td colspan=6><hr width="100%"></td></tr>
    
  </table>


<%
//==============================================================================
// Data Emissione e Data Trasmissione
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>

    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input title = "Giorno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Trasmissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
</table>

<%
//==============================================================================
//              Sezione con i destinatari del Provvedimento
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <td class="Titolo"  colspan=6> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="Titolo" colspan='8'> Destinatari</td>
  </tr>
</table>

<table style="width: 95%;">
<%
if(lPosizione.getCodPosizioneGiuridica() != null &&
  (lPosizione.getCodPosizioneGiuridica().equals("01") || lPosizione.getCodPosizioneGiuridica().equals("02")
   || lPosizione.getCodPosizioneGiuridica().equals("03") || lPosizione.getCodPosizioneGiuridica().equals("04")
   || lPosizione.getCodPosizioneGiuridica().equals("23") || lPosizione.isMisSosp()))
{
%>
  <tr>
    <td class="l">Istituto di Detenzione <font class=ob>(*)</font></td>
    <td class="l" colspan=3>
    <%if(lLuogoDetenzione!= null && lLuogoDetenzione.getIstitutoDetenzione() != null) {%>
      <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
    <%}else {%>
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
      <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    <%}%>
    </td>
  </tr>
<%
}
if(lPosizione.getCodPosizioneGiuridica() != null &&(lPosizione.getCodPosizioneGiuridica().equals("05")
   || lPosizione.getCodPosizioneGiuridica().equals("08") || lPosizione.isLibero() || lPosizione.isMisuraAlternativa()))
{
%>
  <tr>
    <td class="l">Autorità di destinazione <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <select  Title="Autorita destinazione"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
      <%=autoritaEsternaE%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
    </td>
  </tr>
<%
}

if(lPosizione.isMisuraAlternativa() ||
  (lPosizione.getCodPosizioneGiuridica() != null &&
  (lPosizione.getCodPosizioneGiuridica().equals("01") || lPosizione.getCodPosizioneGiuridica().equals("02")
   || lPosizione.getCodPosizioneGiuridica().equals("03") || lPosizione.getCodPosizioneGiuridica().equals("04")
   || lPosizione.getCodPosizioneGiuridica().equals("23") || lPosizione.isMisSosp())))
{
%>
  <tr>
  <% if(lPosizione.isMisuraAlternativa()) { %>
    <td class="l">Autorità di controllo <font class=ob>(*)</font></td>
  <% } else { %>
    <td class="l">Altra Autorità</td>
  <% } %>
    <td class="L" colspan="3">
      <select  Title="Autorità"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
        <%=autoritaEsternaAltra%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>"  cols=30 ></textarea>
    </td>
  </tr>
<%
}

if(lPosizione.isMisuraAlternativa())
{
%>
  <tr>
    <td class="l">UEPE <font class=ob>(*)</font></td>
    <td class="l" colspan=3>
      <input readonly Title="UEPE Competente" name="Indirizzo" value="" size=60 >
      <input type="hidden" Title="UEPE" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
      <a href="Javascript:ListaCSSA('f','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>

  <tr>
    <td class="l">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
    <td class="L" colspan=3>
      <input title="ufficio" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="25">
      <a href="Javascript:ListaUDS('f','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
       <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>

  <tr>
    <td class="L">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
    <td class="L" colspan=3>
      <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuniTds('f','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
<%
}
%>
</table>

<%
//==============================================================================
//            Sezione con i Destinatario per Notifica (avvocati)
//==============================================================================
%>
<table style="width: 95%;">
  <tr>
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table>
          <tr>
            <td class="l">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
        <table style="width: 95%;">
          <tr><td class="l">Autorità Destinazione </td >
          <td class="L" colspan="3">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
        <td class="l">Note</td>
       <td class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
</table>


<table>
    <tr>
      <td class="lNoBord" colspan="2">
       <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    // Controllo campi Reclusione
    frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
    frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
    frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
    frmvalidator.addValidation("Multa","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
    frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");

    // Controllo campi Arresto
    frmvalidator.addValidation("AArr","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
    frmvalidator.addValidation("MArr","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
    frmvalidator.addValidation("GArr","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
    frmvalidator.addValidation("Ammenda","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
    frmvalidator.addValidation("Amm_dec","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

    // Data Emissione
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");

    // Data Trasmissione
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>