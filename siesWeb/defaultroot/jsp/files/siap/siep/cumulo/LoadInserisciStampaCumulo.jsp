<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>

<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.penacumulo.model.PenaCumuloModel"%>

<jsp:useBean id="PosizioneGiuridicaLuogoAltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"     scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="daticssa"             scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="UffTDS"               scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"               scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaN"      scope="request" class="java.lang.String"/>
<jsp:useBean id="Ist"                  scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="giorniLA"   		   scope="request" class="java.lang.String"/>
<jsp:useBean id="penaCum"              scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel" />
<jsp:useBean id="eventoCumulo"         scope="request" class="siap.sico.evento.model.EventoModel"/>

<% 
	//Collection tipologia =(Collection) request.getAttribute("tipologia");
	
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

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
<%
//==============================================================================
// Form di inserimento del Provvedimento di Cumulo. Il tipo di Provvedimento
// viene recuperato dall'evento associato al fascicolo e determina la 
// scelta dei destinatari
//==============================================================================
%>

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

    function ListaCSSA(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
    // NGG
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

// magistrato competente
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
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
		  	
      if(document.f.inizioPena.value == 'null' && document.f.finePena.value == 'null' && (document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value == '1' || document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value == '2'))
      {
        alert("E' possibile selezionare solamente la tipologia Condannato in stato di libertà oppure verificare il calcolo della pena");

        return false;
      }

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

      if (document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      data_to_verify = document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.f.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di Trasmissione non valida');

        return false;
      }

      if(   document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="-"
         || document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="0")
      {
        document.f.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=""
        document.f.Indirizzo.value=""

        document.f.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>.value=""

        document.f.<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>.value=""
        document.f.Comune.value=""
      }
      else if(document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="1")
      {
        document.f.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=""
        document.f.Indirizzo.value=""

        document.f.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>.value=""

        document.f.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>.value=""
      }
      else if(   document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="2"
              || document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="3")
      {
        document.f.<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>.value=""
        document.f.Comune.value=""
      }

      if(document.f.<%= ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Il Cognome del Magistrato è obbligatorio");
        
        return false;
      }

      if(document.f.<%= ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Nome del Magistrato è obbligatorio");
        
        return false;
      }

      //document.f.tipologia.value=document.f.<//%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value;

      return true;
    }

    function ChangeTipologia()
    {
      var nodeIstituto = document.getElementById('divIstituto');
      var nodeAltri = document.getElementById('divAltri');
      var nodeAvv = document.getElementById('divAvv');
      var nodeConferma = document.getElementById('divConferma');
      
      if(    document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="2"
          || document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="3" )
      {
        nodeAltri.style.visibility='visible';
        nodeAltri.style.top='-50px';
        nodeAvv.style.top='-50px';
        nodeAvv.style.visibility='visible';
        nodeIstituto.style.visibility='hidden';
        nodeConferma.style.top='-50px';
      }
      else if( document.f.<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>.value=="1" )
      {
        nodeAltri.style.visibility='hidden';
        nodeIstituto.style.visibility='visible';
        nodeAvv.style.visibility='visible';
        nodeAvv.style.top='-150px';
        nodeConferma.style.top='-140px';
      }
      else // in libertà
      {
        nodeAltri.style.visibility='hidden';
        nodeIstituto.style.visibility='hidden';
        nodeAvv.style.visibility='visible';
        nodeAvv.style.top='-200px';
        nodeConferma.style.top='-200px';
      }
    }
    
    function toggleComuneCasellario()
		{
			vistaLabel = (document.getElementById("labelCasellario").style.display == 'none') ? 'block' : 'none';
			document.getElementById("labelCasellario").style.display = vistaLabel;

			vistaInput = (document.getElementById("inputCasellario").style.display == 'none') ? 'block' : 'none';
			document.getElementById("inputCasellario").style.display = vistaInput;
		}   
</script>

</head>

<body class="corpo" onload="ChangeTipologia();">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
		<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciStampaCumulo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Stampa Cumulo</font>
      </td>
    </tr>
  </table>
 <br>
 <table width = "90%">
 <tr>
  <td>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
  </td>
</tr>
</table>

<table width = "90%">
  <tr>
    <td class=l width="25%">Posizione Giuridica</td>
    <td class=l colspan=5><font class="campo"><%=PosizioneGiuridicaLuogoAltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%></font></td>
  </tr>

  <input type="hidden" name="inizioPena" value="<%=penaresidua.getDataInizio()%>">
  <input type="hidden" name="finePena" value="<%=penaresidua.getDataFine()%>">

   <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if (!(penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0 &&
            penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0 &&
            penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0))
        {
%>
          <td class="l">Reclusione</td>
          <td class="l">
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<%
    if (!(penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0 &&
        penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0 &&
        penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
</tr>
</table>

<%
//==============================================================================
// VISUALIZZAZIONE LA e RD
//==============================================================================
//if(giorniLA != null && !giorniLA.equals("0"))
if (   (penaCum.getNumGiorniLibAnticipataLA()!=null  && penaCum.getNumGiorniLibAnticipataLA().intValue()>0)
    || (penaCum.getNumGiorniLibAnticipataSPE()!=null && penaCum.getNumGiorniLibAnticipataSPE().intValue()>0)
    || (penaCum.getNumGiorniLibAnticipataINT()!=null && penaCum.getNumGiorniLibAnticipataINT().intValue()>0)
    || (penaCum.getNumGiorniRiduzionePena()!=null    && penaCum.getNumGiorniRiduzionePena().intValue()>0)
   )
{
  String lLADetratta = "";
  if(penaresidua != null && penaresidua.getDataFine() != null)
  {
    lLADetratta = "già detratta";
  } else {
    lLADetratta = "da detrarre";
  }
%>
  <table width="90%">
          <!--  20/05/2014  Nuova L.A. - DL 146/2013 -->
          
      <%if(penaCum.getNumGiorniLibAnticipataLA() != null && !penaCum.getNumGiorniLibAnticipataLA().equals("0"))
      { %>
        <tr>
          <td class="L" width="40%" height="30">
            <font class="label">Liberazione Anticipata Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" height="30">
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataLA(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
   <% }
      if(penaCum.getNumGiorniLibAnticipataSPE() != null && !penaCum.getNumGiorniLibAnticipataSPE().equals("0"))
      { %> 
        <tr>
          <td class="L" width="40%" height="30" >
            <font class="label">Liberazione Anticipata Speciale Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" height="30">
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataSPE(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
   <% }
      if(penaCum.getNumGiorniLibAnticipataINT() != null && !penaCum.getNumGiorniLibAnticipataINT().equals("0"))
      { %>      
        <tr>
          <td class="L" width="40%" height="30" >
            <font class="label">Integrazione Liberazione Anticipata Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" height="30">
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniLibAnticipataINT(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>
   <% }
      if(penaCum.getNumGiorniRiduzionePena() != null && !penaCum.getNumGiorniRiduzionePena().equals("0"))
      { %>      
        <tr>
          <td class="L" width="40%" height="30" >
            <font class="label">Riduzione pena per risarcimento danni Concessa <%=lLADetratta%> in giorni</font>
          </td>
          <td class="L" width="10%" height="30">
            <font class="campo"><%=StringUtils.toStringJSP(penaCum.getNumGiorniRiduzionePena(), "0") %></font>
          </td>
          <td width="40%" > </td>
        </tr>        
  <%  } %>     
  
  
  </table>  
              <!--  End Nuova L.A. -->   
<%
  }
%>

<table width="90%">
<tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l" width="25%">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }
%>

<%
   if (penaresidua.getFlagErgastolo() != null && (penaresidua.getFlagErgastolo().equals("S") || penaresidua.getFlagErgastolo().equals("D")))
   {
%>
           <td class="l">Data Fine Pena</td>
           <td class="lRosso"> <font class="lRosso">MAI</font></td>
<%}else if( penaresidua.getDataFine() != null)
 {%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy"))%></font>
            </td>
<%}%>
</tr>

<tr>
  <td class="l" width="25%">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
    	<td class="l">
    		<input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>" checked>
     		Foglio Complementare
     	</td>
     	<td class="l" id="labelCasellario" style="display:block;">Casellario Giudiziale</td>
     	<td  colspan="2" class="l" id="inputCasellario" style="display:block;">
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaUfficiPerTipo('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
      
</table>

<%
//==============================================================================
// Tipologia
//==============================================================================
%>
<table width="90%">
   <tr>
      <td class="Titolo" colspan=8> Provvedimento di esecuzione di pene concorrenti </td>
   </tr>

<%
	String descTipologia = "";
	String codTipologia = "";
	if(eventoCumulo != null && eventoCumulo.getCodMotivo().equals("0222")){
		descTipologia = "Condannato in stato di libertà";
		codTipologia = "0";
	} else if(eventoCumulo != null && eventoCumulo.getCodMotivo().equals("0223")){
		descTipologia = "Condannato già detenuto";
		codTipologia = "1";
	} else if(eventoCumulo != null && eventoCumulo.getCodMotivo().equals("0224")){
		descTipologia = "Condannato in misura alternativa";
		codTipologia = "2";
	} else if(eventoCumulo != null && eventoCumulo.getCodMotivo().equals("0277")){
		descTipologia = "Generico";
		codTipologia = "3";
	}
%> 
   <tr>
      <td class=l width="25%">Tipologia</td>
      <td class=l colspan=5><font class="campo"><%=descTipologia%></font></td>
   </tr>
   <INPUT TYPE="HIDDEN" name="<%=ICostantiCumulo.CAMPO_FLAG_TIPO_STAMPA%>" value="<%=codTipologia%>">
   <INPUT TYPE="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventoCumulo.getIdEvento() %>">

   <tr>
     <td class="Titolo" colspan=5> Magistrato Competente </td>
   </tr>
  <tr>
    <td class="l">Magistrato Competente <font class=ob>(*)</font></td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>

<%
//==============================================================================
//                           Sezione con i destinatari
//==============================================================================
%>

<div id="divIstituto" style="width: 100%; visibility:visible; position:relative; " >
  <table width="90%">
    <tr>
      <td class="Titolo" colspan =2> Autorità di destinazione </td>
    </tr>
    <tr>
      <td class="l" width=25%>Istituto di Detenzione</td>
      <td class="l">
<%
        if(PosizioneGiuridicaLuogoAltra != null &&  PosizioneGiuridicaLuogoAltra .getLuogoDetenzione()!= null
           && PosizioneGiuridicaLuogoAltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
        {
%>
          <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(PosizioneGiuridicaLuogoAltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(PosizioneGiuridicaLuogoAltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=PosizioneGiuridicaLuogoAltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
<%
        }
        else if(Ist!= null && Ist.getIdIstitutoDetenzione()!= null && !Ist.getDescrTipoIstituto().equals("") && !Ist.getDescrComune().equals(""))
        {
%>
          <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Ist.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(Ist.getDescrComune())%>" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=Ist.getIdIstitutoDetenzione()%>" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
<%
        }
        else
        {
%>
          <input readonly Title="Istituto" name="Comune" value="" size=50>
          <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
          <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0>
          </a>
<%
        }
%>
      </td>
    </tr>
  </table>
</div>
<div id="divAltri" style="visibility:hidden; position:relative; width:100%;">
  <table width="90%">
    <tr>
      <td class="l"width=25%>UEPE </td>
         <td class="l">
        <input readonly Title="UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
        <input type="hidden" Title="UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
        <input type="hidden" Title="UEPE" name="cssaE" value="S">
        <a href="Javascript:ListaCSSA('f','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="L" width=25%>Tribunale di Sorveglianza</td>
        <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <td class="L">
          <input title="Sede Tribunale Sorveglianza" value="<%=UffTDS%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuniTds('f','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
    </tr>
    <tr>
      <td class="l" width=25%>Magistrato di Sorveglianza </td>
      <td class="L">
        <input title="ufficio" value="<%=UffUDS%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" maxlength="35" size="25">
        <a href="Javascript:ListaUDS('f','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
<!--autorità di polizia-->
      <td class="l" width=25%>Autorità di Destinazione </td>
      <td class="L" colspan="3">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N%>">
          <%=codiceAutoritaN%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
<%
      if(autoritaEsternaN != null && autoritaEsternaN.getDescrSede() != null)
      {
%>
        <input title="Sede Autorita Esterna" value="<%=autoritaEsternaN.getDescrSede()%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>"  maxlength="35" size="35">
<%
      }
      else
      {
%>
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>"  maxlength="35" size="35">
<%
      }
%>
        <a href="Javascript:ListaComuni('f','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>
</div>
<div id="divAvv" style="visibility:visible; position:relative; width:100%;">
<table width="90%">
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
     int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        <table width="90%">
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
         <table width="90%">
          <tr><td class="l" width=25%>Autorità Destinazione </td>
          <td class="L" colspan="3">
             <select Title="Autorita Esterna"  name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
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
    </tr>
    <tr><td>&nbsp;</td></tr>
    </table>
<%
    lIdxAvv++;
 }
%>
</table>
</div>

<div id="divConferma" style="visibility:visible; position:relative; width:100%;">
  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</div>

</form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Trasmissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Trasmissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>

