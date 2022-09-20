<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.refertoscarcerazione.action.ICostantiRefertoScarcerazione"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="eventonotifica"             scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="tipoSospensione"           scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="codiceAutoritaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"   scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="luogodetenzione"      scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="verbale"   scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="lcodicePosizione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="referto"         scope="request" class="siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel"/>
<jsp:useBean id="lFlagReferto"         scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoreferto"         scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="eventopro"         scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="idmisuraalternativa"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codicemotivo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    var desktop;

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
  </script>
  <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function ListaDocumentiSius(a_formname)
    {
      var tipoMA;
      if(document.LoadInserisciMisuraAlternativa.tipoSospensione.value=='AFFIDAMENTO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoSospensione.value=='DETENZIONE')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoSospensione.value=='SEMILIBERTA')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoSospensione.value=='INDULTINO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.INDULTINO%>';
      }
			// 05/11/2010 Espiazione Pena presso Domicilio
      else if(document.LoadInserisciMisuraAlternativa.tipoSospensione.value=='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>';
      }

      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.PERDITA_EFFICACIA%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }

  function Verify()
  {
<%
    if((!lPosizione.isLibero()) || (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
    {
     if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
     {
      if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
      {
%>
        if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
        if (document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

          var data_to_verifica = document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

          if (!ControllaData(data_to_verifica) )
          {
            alert('Data fine pena non valida');
            return false;
          }
<%
        }
      }
    }
%>
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

    if (!ControllaData(data_to_verify) )
    {
      alert('Data di Trasmissione non valida');
      return false;
    }

    var campo = document.LoadInserisciMisuraAlternativa.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

    if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
    {
    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.disabled)
    {
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione Ordinanza non valida');
        return false;
      }
    }

    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.disabled)
    {
      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }
    }
  }

<%if(lFlagReferto.equals("N"))
 {%>
    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled)
    {
      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>.value=="")
      {
        alert("L'Istituto di Detenzione è obbligatorio");
        return false;
      }
    }

    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled)
    {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value=="")
      {
        alert("La data di Scarcerazione è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
        return false;
      }

    if (document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value != ""
      ||document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.value != ""
      ||document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value != "" )
    {
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
          document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
          document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

      if (!ControllaData(data_to_verify) )
         {
           alert('Data Scarcerazione non valida');
           document.LoadInserisciMisuraAlternativa.<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
           return false;
         }
    }

    }
<%}%>



  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
  {
    alert("Il  Magistrato Firmatario è obbligatorio");
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
    return false;
  }


<%if(tipoSospensione.equals("DETENZIONE"))
 {%>
   if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
    {
      alert("Il Campo Autorità competente per territorio è obbligatorio");
         return false;
    }
<%}%>
<%if(tipoSospensione.equals("SEMILIBERTA") || 
		 tipoSospensione.equals("INDULTINO")   || 
		 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) )
 {%>

    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
    {
      alert("Il Campo Istituto di Detenzione è obbligatorio");
      return false;
    }
<%}%>

	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "" ||
			document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value == "-") {
		// MEV10-s3: modificato msg
		alert("L'UEPE/USSM è obbligatorio");
		return false;
  	}

	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value == "") {
		// MEV10-s3: modificato msg
      	alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
      	return false;
    }

	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value == "") {
		// MEV10-s3: modificato msg
		alert("Il Tribunale di Sorveglianza è obbligatorio");
      	return false;
    }

<%if(tipoSospensione.equals("SEMILIBERTA") || 
		 tipoSospensione.equals("AFFIDAMENTO") || 
		 tipoSospensione.equals("INDULTINO")   ||
		 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) )
 {%>
   if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-')
    {
      alert("Autorità competente per territorio è obbligatorio");
      return false;
    }
<%}%>
  return true;
}

  function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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

  function radio()
  {
       var nodeprov =document.getElementById('divprov');
       var nodecomu = document.getElementById('divcom');
       var nodedest =document.getElementById('divdest');
       var nodebottone = document.getElementById('divbottone');

       if(document.LoadInserisciMisuraAlternativa.tipo[0].checked)
       {
          nodedest.style.visibility='visible';
          nodecomu.style.visibility='visible';
          nodeprov.style.visibility='hidden';
          nodebottone.style.visibility='visible';

<%
         if(tipoSospensione.equals("SEMILIBERTA"))
         {
%>
            if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="S")
            {
              nodebottone.style.top='-150px';
              nodedest.style.top='-150px';
            }
            else if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
            {
              nodebottone.style.top='-210px';
              nodedest.style.top='-210px';
            }
<%
        }
        else
        {
%>
          if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="S")
          {
          <%if(tipoSospensione.equals("INDULTINO")||
          		 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
            {%>
            	nodebottone.style.top='-190px';
            	nodedest.style.top='-195px';
					<%}else{%>
            	nodebottone.style.top='-260px';
            	nodedest.style.top='-255px';
					<%}%>
          }else if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
          {
            nodebottone.style.top='-240px';
            nodedest.style.top='-255px';
          }
<%
        }
     if(lFlagReferto.equals("N"))
     {
%>
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled=false;
<%
      }
%>
       if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
       {
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.disabled=true;
       }

        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.disabled=true;
      }

     if(document.LoadInserisciMisuraAlternativa.tipo[1].checked)
     {
        nodeprov.style.visibility='visible';
        nodedest.style.visibility='visible';
        nodecomu.style.visibility='hidden';
        nodebottone.style.visibility='visible';

<%
        if(tipoSospensione.equals("SEMILIBERTA"))
        {
          if(lFlagReferto.equals("N"))
          {
%>
            nodebottone.style.top='-180px';
            nodedest.style.top='-180px';
            nodeprov.style.top='-180px';
<%
          }
          else if(lFlagReferto.equals("S"))
          {
%>
             if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="S")
             {
               nodebottone.style.top='-155px';
               nodedest.style.top='-155px';
               nodeprov.style.top='-155px';
             }
             else
             {
                nodebottone.style.top='-150px';
                nodedest.style.top='-150px';
                nodeprov.style.top='-150px';
             }
<%
          }
      }
      else
      {
          if(lFlagReferto.equals("N"))
          {
%>
            nodebottone.style.top='-180px';
            nodedest.style.top='-180px';
            nodeprov.style.top='-180px';
<%
          }
          else if(lFlagReferto.equals("S"))
          {%>
          	if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="S")
            {

<%
         			if(tipoSospensione.equals("INDULTINO") ||
         				 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
		          {%>
		            nodebottone.style.top='-165px';
		            nodedest.style.top='-165px';
		            nodeprov.style.top='-160px';
					  <%}else{%>
		            nodebottone.style.top='-155px';
		            nodedest.style.top='-155px';
		            nodeprov.style.top='-155px';
						<%}%>
             }
             else
             {
								<%if(tipoSospensione.equals("INDULTINO") ||
		          			 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
         				{%>
            			nodebottone.style.top='-165px';
            			nodedest.style.top='-155px';
            			nodeprov.style.top='-152px';
							<%}else{%>
                  nodebottone.style.top='-155px';
                  nodedest.style.top='-155px';
                  nodeprov.style.top='-155px';
							<%}%>
             }
         <%}
   }%>

       <%if(lFlagReferto.equals("N"))
       {%>
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled=true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.disabled=true;
      <%}%>
       if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
       {
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>.disabled=false;
       }
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.disabled=false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.disabled=false;


      }
 }

  </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>

</head>

<body class="corpo" onload="radio();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%

      MisuraAlternativaModel lModel = new MisuraAlternativaModel();
      String lAzione = new String();
      String flagMis = new String();
%>
    <font class="campo">CESSAZIONE SOSPENSIONE PROVVISORIA

<%
    if(tipoSospensione.equals("AFFIDAMENTO"))
    {%>
      AFFIDAMENTO IN PROVA
	<%}
    else if(tipoSospensione.equals("DETENZIONE"))
    {%>
      DETENZIONE DOMICILIARE
	<%}
    else if(tipoSospensione.equals("INDULTINO"))
    {%>
      L.207/2003
	<%}
    else if(tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
    {%>
    	Espiazione Pena presso Domicilio
	<%}else
    {%>
      SEMILIBERTA'
	<%}%>
</font>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMAPerditaEfficacia">
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="">
<%
  if(!tipoSospensione.equals("DETENZIONE") && !tipoSospensione.equals("AFFIDAMENTO") &&
     !tipoSospensione.equals("INDULTINO")  && !tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
  {
%>
    <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">
<%
  }
%>

<%
  if(verbale!= null && verbale.getIdVerbale()!= null)
  {
%>
    <INPUT type="hidden" name="verbale" value="S">
<%
  }
  else
  {
%>
    <INPUT type="hidden" name="verbale" value="N">
<%
  }
%>
  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  <input type="HIDDEN" title="tipoSospensione" value="<%=tipoSospensione%>" type="text" name="tipoSospensione">
<%

  if(lFlagReferto.equals("S"))
  {
%>
    <input type="HIDDEN" title="eventoreferto" value="<%=eventoreferto.getIdEvento()%>" type="text" name="eventoreferto">
<%
  }
%>
 <input type="HIDDEN" title="lFlagReferto" value="<%=lFlagReferto%>" type="text" name="lFlagReferto">

<%
String provvedimento = null;
String comunicazione = null;

if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
{
%>
  <input type="HIDDEN" name="flagmisura" value="N">
<%
  comunicazione ="checked";
}
else
{
%>
  <input type="HIDDEN" name="flagmisura" value="S">
<%
}

if(misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa()!= null)
{
  provvedimento ="checked";
   if(misuraalternativa.getFlagUfficioInserimento()== null && misuraalternativa.getDataScarcerazione() != null)
   {
    provvedimento="disabled";
   }
}
else
{
  comunicazione ="checked";
}
%>

<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=idmisuraalternativa%>">
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
          <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%    }
      else
     {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%   }%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>


                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
          </tr>
<%
          }
        }
%>


   <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
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
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
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

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
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

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input title = "Giorno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA%> >
           -
           <input title = "Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%> >
           -
           <input title = "Anno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
                </td>
<%            }
        }
      }
}
%>


        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
	<tr>
        <td class="l">Data Emissione</td>

        <td class="L" >
          <input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>

        <td class="l">Data Trasmissione</td>

        <td class="L">
          <input title = "Giorno Data Trasmissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>
      </tr>
<tr>
<td class="l" colspan="4">Comunicazione Istituto di Detenzione &nbsp;<input type="radio" name="tipo" value="COMUNICAZIONE"    <%=StringUtils.toStringJSP(comunicazione)%> onclick="radio();">
&nbsp; Provvedimento Magistrato di Sorveglianza  &nbsp; <input type="radio" name="tipo" value="PROVVEDIMENTO"   <%=StringUtils.toStringJSP(provvedimento)%> onclick="radio();">
</td>
</tr>
</table>

<div id="divcom" style="visibility:hidden; position:relative; width:100%;">
<table width=90%>
   <tr>
      <td class="Titolo" colspan='8'> Dati Del Referto Di Scarcerazione</td>
   </tr>

<tr>
<%if(lFlagReferto.equals("S"))
 {%>
     <td class="l" nowrap>Istituto di Detenzione</td>
     <td class="l" colspan="3">
      <font class="campo">
       <%=StringUtils.toStringJSP(referto.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(referto.getIstitutoDetenzione().getDescrComune())%>
      </font>

<%}else {%>
  <td class="l" nowrap>Istituto di Detenzione <font class=ob>(*)</font></td>
  <td class="l" colspan="3">
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>
              <input readonly Title="Istituto" name="istituto" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=35>

<%}else{%>
              <input readonly Title="Istituto" name="istituto" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
  <%}%>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiRefertoScarcerazione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','istituto');">
              <img src="/images/filefolder.gif" border=0></a>

       <%}%>
</td>
</tr>

<tr>
  <td class="l">Anno /Numero Nota</td>
    <td class="l">
  <%if(lFlagReferto.equals("S"))
   {%>
      <font class="campo"> <%=StringUtils.toStringJSP(referto.getAnnoNota())%> /</font>
      <font class="campo"> <%=StringUtils.toStringJSP(referto.getNumNota())%> </font>
      <%}else{%>
        <input Title="Anno Nota" value="" name="<%=ICostantiRefertoScarcerazione.CAMPO_ANNO_NOTA%>" type="text" size="4" maxlength="4"> /
       <input Title="Numero Nota" value="" name="<%=ICostantiRefertoScarcerazione.CAMPO_NUM_NOTA%>" type="text" size="6" maxlength="6">
    <%}%>
    </td>


        <td class="l">Data Emissione Nota </td>
        <td class="l">
  <%if(lFlagReferto.equals("S"))
   {%>

     <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(referto.getDataNota(),"dd-MM-yyyy"))%> </font>

  <%}else{%>
         <font class="campo">
          <input title = "Giorno Data Emissione Nota" <%--value="<%=DateUtils.getSysDate("dd")%>"--%> type="text" size="2" maxlength="2" name="<%=ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_NOTA%>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione Nota" <%--value="<%=DateUtils.getSysDate("MM")%>"--%> type="text" size="2" maxlength="2" name="<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_NOTA%>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione Nota" <%--value="<%=DateUtils.getSysDate("yyyy")%>"--%> type="text" size="4" maxlength="4" name="<%=ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_NOTA%>" <%=IWebConstants.UTIL_DATA_ANNO%> >
         </font>
    <%}%>
       </td>
</tr>
<tr>
  <td class="l">Oggetto Misura Alternativa </td>
  <td class="L" colspan="3">
<%
    if(lFlagReferto.equals("S"))
    {
%>
      <font class="campo"> <%=StringUtils.toStringJSP(eventopro.getDescrMotivo())%> </font>
      <INPUT type="hidden" name="CodMotivoComunicazione" value="<%=eventopro.getCodMotivo()%>">
<%
    }
    else
    {
      if(codicemotivo != null && !codicemotivo.equals(""))
     {
%>
      <font class="campo"><%=motivoProvv%></font>
      <INPUT type="hidden" name="CodMotivoComunicazione" value="<%=codicemotivo%>">
<%
      }else{
%>
      <select  Title="Codice Motivo"  name="CodMotivoComunicazione">
        <%=motivoProvv%>
      </select>
<%   }
}
%>
  </td>
</tr>
<tr>
  <td class="l">Data Scarcerazione <font class=ob>(*)</font></td>
  <td class="l" colspan="3">
<%
    if(lFlagReferto.equals("S"))
    {
%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(referto.getDataScarcerazione(),"dd-MM-yyyy"))%> </font>&nbsp;
<%
    }
    else
    {
%>
      <font class="campo">
        <input title = "Giorno Data Scarcerazione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%> > -
        <input title = "Mese Data Scarcerazione" value="" type="text" size="2" maxlength="2" name="<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA%> > -
        <input title = "Anno Data Scarcerazione" value="" type="text" size="4" maxlength="4" name="<%=ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </font>
<%
  }
%>
  </td>
</tr>
<tr>
  <td class="l">Note</td>
  <td  class="L" colspan="3">
    <TEXTAREA title="Note" name="<%=ICostantiRefertoScarcerazione.CAMPO_NOTE %>" cols=80 rows=2 ><%= StringUtils.toStringJSP(referto.getNote())%></textarea>
  </td>
</tr>
</table>
</div>

<div id="divprov" style="visibility:hidden; position:relative; width:100%;" >
<table width=90%>
   <tr>
      <td class="Titolo" colspan='8'> Dati Del Decreto Del Magistrato di Sorveglianza </td>
   </tr>
   <tr>
      <td class="l" nowrap>
        <a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l" colspan="3">
      &nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Decreto</td>
      <td class="l">
        <input Title="Anno Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
    </tr>
	<tr>
      	<td class="l">Ufficio Emittente</td>
      	<td class="l" colspan="3">
      		<%-- MEV10-s3: sostitiuta stringa con combo --%>
      		<%-- 
      			<font class="campo">UFFICIO DI SORVEGLIANZA</font>
        		<input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" value="UDS">
        	--%>
        	<font class="campo"><%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteUfficioBis)%></font>
      	</td>
      </td>
    </tr>
    <tr>
      <td class="l" nowrap>Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" nowrap>Oggetto Decreto</td>
      <td class="L" colspan="3">
<%
      if(codicemotivo != null && !codicemotivo.equals(""))
      {%>
        <font class="campo"><%=motivoProvv%></font>
        <INPUT type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codicemotivo%>">
		<%}else{%>
         <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
		<%}%>


      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Decreto</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input title = "Giorno Data Emissione Decreto" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Mese Data Emissione Decreto" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Anno Data Emissione Decreto" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
        </font>
      </td>
    </tr>
<%
    if(tipoSospensione.equals("DETENZIONE")  || 
    	 tipoSospensione.equals("AFFIDAMENTO") || 
    	 tipoSospensione.equals("INDULTINO")	 ||
    	 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) )
    {
%>
      <tr>
        <td class="l">Domicilio Imposto</td>
        <td class="l" colspan="3">
          <font class="campo">
            <input Title="Domicilio Imposto" name="<%= ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>" size=35 type="text" onChange="pulisciId();">
          </font>
        </td>
      </tr>
<%
    }
%>
    <tr>
      <td  class="l">Note</td>
      <td  class="L" colspan="3">
        <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>
</table>
</div>
<div id="divdest" style="visibility:hidden; position:relative; width:100%;" >
<table width="100%">
   	<tr>
     	<td class="Titolo" width="100%" colspan=4> Magistrato Firmatario </td>
   	</tr>
	<tr>
		<%-- MEV10-s3: aggiunta proprietà width --%>
   		<td class="l" width="30%">Magistrato Firmatario</td>
   		<td class="L" colspan="3">
        	<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       		<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
       		<a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        		<img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
	</tr>

 <tr>
     <td class="Titolo" width="100%" colspan=4> Destinatari</td>
   </tr>
<%if(tipoSospensione.equals("DETENZIONE"))
 {%>
  <tr>
<!--autorità di polizia-->
          <td class="l" width=30%>Autorità Competente per territorio <font class=ob>(*)</font></td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
             <%=codiceAutoritaE%>

             </select>

           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
           </td>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols=30 ></textarea>
            </td>
    </tr>

<%}%>

<%if(tipoSospensione.equals("SEMILIBERTA") || 
		 tipoSospensione.equals("INDULTINO")	 ||
		 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) )
 {%>
 <tr>
     <td class="l" width=30%>Istituto di Detenzione <font class=ob>(*)</font></td>
  <td class="l" colspan="3">
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
<%}else {%>
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
       <%}%>
</td>
</tr>

<%}%>

<%if(tipoSospensione.equals("SEMILIBERTA"))
 {%>
 <tr>
<!--autorità di polizia-->
          <td class="l" width=30%>Autorità Competente per territorio <font class=ob>(*)</font></td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
             <%=codiceAutoritaC%>
             </select>
           </tr>
<input type="hidden" Title="autoritaC" name="notificaSemiliberta" value="S">
    <tr>
      <td class="l">Sede</td>
     <td class="L">
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
           </td>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>"  cols=30 ></textarea>
            </td>
    </tr>
<%}%>
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="l">Destinatario <font class=ob>(*)</font></td>
		<td class="l" colspan="3"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
        <td class="l" colspan="3">
        	<input readonly title="Sede UEPE Competente" name="Indirizzo" value="" size=60 >
        	<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
			<%if(tipoSospensione.equals("AFFIDAMENTO")) {%>
      			<input type="hidden" name="cssaE" value="S">
 			<%}%>
       		<a href="Javascript:ListaCSSAMinor('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
       			<img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
	</tr>
	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l">Destinatario <font class=ob>(*)</font></td>
      	<td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
      	<td class="L" colspan="3">
        	<input title="ufficio" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" maxlength="35" size="25">
        	<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
        		<img src="/images/filefolder.gif" border=0>
        	</a>
     	</td>
    </tr>
    <tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l">Destinatario <font class=ob>(*)</font></td>
        <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
		<td class="L" colspan="3">
        	<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>" maxlength="35" size="35">
          	<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>');">
          		<img src="/images/filefolder.gif" border=0>
          	</a>
        </td>
	</tr>

<%
  if(tipoSospensione.equals("AFFIDAMENTO") || 
  	 tipoSospensione.equals("INDULTINO")	 ||
		 tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM) )
  {%>
    <tr>
	<!--autorità di polizia-->
      <td class="l" width="30%">Autorità Competente per territorio <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
          <%=codiceAutoritaC%>
        </select>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>"  cols=30 ></textarea>
      </td>
    </tr>
<%
  }
%>
</table>
</div>
<div id="divbottone" style="visibility:visible; position:relative; width:100%;" >
  <table width="100%">
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");

//Controlli Data Emissione
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

<%
  if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
  {
%>
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");
    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.disabled)
    {

//Controlli Data Emissione Nota
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");
    }

//Controlli Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
<%
  }

  if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
  {
    if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
    {
      if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
      {
%>

//Controlli Data Fine Pena
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
      }
    }
  }
%>

<%if(lFlagReferto.equals("N"))
 {
%>
   //Controlli Data Emissione Nota
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_NOTA%>","numeric");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_NOTA%>","gt=1");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_NOTA%>","lt=31");

     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_NOTA%>","numeric");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_NOTA%>","gt=1");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_NOTA%>","lt=12");

     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_NOTA%>","numeric");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_NOTA%>","gt=1900");
     frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_NOTA%>","lt=2099");


    if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>.disabled)
      {
      //Controlli Data Scarcerazione
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","numeric");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","gt=1");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","lt=31");

        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>","numeric");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>","gt=1");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_MESE_DATA_SCARCERAZIONE%>","lt=12");

        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>","numeric");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>","gt=1900");
        frmvalidator.addValidation("<%= ICostantiRefertoScarcerazione.CAMPO_ANNO_DATA_SCARCERAZIONE%>","lt=2099");

    }
<%
  }
%>

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>