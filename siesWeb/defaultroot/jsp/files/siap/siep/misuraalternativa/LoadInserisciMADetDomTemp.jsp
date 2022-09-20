<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
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
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="eventonotifica"     scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="codiceAutoritaE"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv" scope="request" class="java.lang.String"/>
<jsp:useBean id="Cssa"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"  scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="luogodetenzione"    scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="sedepoliziaNotN"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="sedepoliziaNotC"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="verbale"   scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="lcodicePosizione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="lFlagAffi"         scope="request" class="java.lang.String"/>
<jsp:useBean id="idmisuraalternativa"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipmis"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"   scope="request" class="java.lang.String"/>
<jsp:useBean id="azione"   scope="request" class="java.lang.String"/>
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

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

    function ListaComuni(a_formname,a_fieldname) {
      	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function ListaDocumentiSius(a_formname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=tipoMisura%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>=<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId() {
      	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }

    function Verifica() {

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

      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }

    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value;

	  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value;

		  var data_to_verify_sca = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value;

      if (!ControllaDataPassaVuota(data_to_verify_sca))
		  {
        alert('Scadenza della misura non valida');
        return false;
		  }

    if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value==""
     && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value==""
     && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value==""
     && document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value=="")
      {
        alert("Scadenza della misura o Quantità della misura Obbligatori!");
        return false;
      }

    if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value!=""
     && (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>.value!=""
     || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>.value!=""
     || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>.value!=""))
      {
        alert("Inserire Scadenza della misura o in alternativa Quantità della misura!");
        return false;
      }

    }

<%
  if( (!lPosizione.isLibero() && lFlagAffi.equals("N"))
      && !lPosizione.getCodPosizioneGiuridica().equals("12")
      && !lPosizione.getCodPosizioneGiuridica().equals("13")
      && !lPosizione.getCodPosizioneGiuridica().equals("29"))
  {
%>

    if(document.LoadInserisciMisuraAlternativa.tipo[1].checked == true)
    {
      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value=="")
      {
        alert("La data di Scarcerazione è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.focus();
        return false;
      }
    }

    if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value != ""
      ||document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value != ""
      ||document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value != "" )
    {
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
          document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
          document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
        alert('Data Scarcerazione non valida');
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
        return false;
		  }
    }
<%
  }
%>

  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
  {
    alert("Il  Magistrato Firmatario è obbligatorio");
    document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
    return false;
  }

  if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled)
  {
    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
    {
<%
      if ((lPosizione.isLibero()) && (verbale== null || verbale.getIdVerbale()== null))
      {
%>
        alert("Il Campo Destinatario per esecuzione è obbligatorio");
<%
      }
      else
      {
%>
        alert("Il Campo Autorità competente per territorio è obbligatorio");
<%
      }
%>
      return false;
    }
  }

  if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled)
  {
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
    {
      alert("Il Campo Istituto di Detenzione è obbligatorio");
      return false;
    }
  }

  if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled)
  {
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" || document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
    {
    	// MEV10-s3: modificato msg
      	alert("L'UEPE/USSM è obbligatorio");
      	return false;
    }
  }

if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled)
{
  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=="")
    {
		// MEV10-s3: modificato msg
		alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
      	return false;
    }
}

if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled)
{
    if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=="")
    {
    	// MEV10-s3: modificato msg
      	alert("Il Tribunale di Sorveglianza è obbligatorio");
      	return false;
    }
}

if(!document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled)
{
   if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-')
    {
      alert("Autorità competente per territorio è obbligatorio");
      return false;
    }
}

}

	function ListaCSSA(a_formname,a_fieldname,a_field2) {
		<%-- MEV10-s3: aggiunto controllo preventivo --%>
		var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
		if (a_typename == "-")  {
	         alert("Selezionare il Destinatario dell'UEPE/USSM");
	    } else {
    		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
	    }
  	}

	// magistrato competente
  	function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  	}

  	function ListaUDS(a_formname,a_fieldname) {
   		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}

  	function ListaComuniTds(formname,fieldname) {
		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}

  	function DisabilitaAvvocato() {
          if(document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length)
            {
              document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[0].disabled=true;
              document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[1].disabled=true;

              document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].disabled=true;
              document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].disabled=true;

              document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[0].disabled=true;
              document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[1].disabled=true;

              document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[0].disabled=true;
              document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[1].disabled=true;
            }
            else
             {
                document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.disabled=true;
                document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=true;
                document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>.disabled=true;
                document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=true;
             }
  	}

  	function AbilitaAvvocato() {
    	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[0].disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[1].disabled=false;
			
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].disabled=false;
			
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[0].disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>[1].disabled=false;
			
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[0].disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>[1].disabled=false;
	    } else {
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>.disabled=false;
			document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.disabled=false;
	    }
  	}

   function radio() {
       var pos = document.LoadInserisciMisuraAlternativa.CodPosizioneGiuridica.value;
       var affi = document.LoadInserisciMisuraAlternativa.lFlagAffi.value;

       var nodesor =document.getElementById('divsor');
       var nodecssa = document.getElementById('divcssa');
       var nodeistituto =document.getElementById('divistituto');
       var nodeautoritaE = document.getElementById('divautoritacompetenteE');
       var nodeautoritaC = document.getElementById('divautoritacompetenteC');
       var nodebottone = document.getElementById('divbottone');
       var nodeavvocati = document.getElementById('divavocati');

    	if ((pos == "07") || (pos == "10") || (pos == "16") || ( pos == "17")
     			|| ( pos == "20") || ( pos == "46") || ( pos == "47") || ( pos == "26")
     			|| ( pos == "30") || (affi == "S")) {
      		if (affi == "S") {
				nodeautoritaE.style.visibility='visible';
				nodecssa.style.visibility='visible';
				nodesor.style.visibility='visible';
				nodeautoritaC.style.visibility='hidden';
				nodeistituto.style.visibility='hidden';
				nodeavvocati.style.visibility='hidden';
				nodebottone.style.visibility='visible';
				nodecssa.style.top='-35px';
				nodesor.style.top='-37px';
				if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
				 	nodebottone.style.top='-290px';
				} else {
				 	nodebottone.style.top='-190px';
				}
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=true;
				DisabilitaAvvocato();
      		} else {
				nodecssa.style.visibility='hidden';
				nodesor.style.visibility='hidden';
				nodeautoritaC.style.visibility='hidden';
				nodeistituto.style.visibility='hidden';
				nodeautoritaE.style.visibility='visible';
				nodebottone.style.visibility='visible';
				nodeavvocati.style.visibility='visible';
				nodebottone.style.top='-205px';
				nodeavvocati.style.top='-205px';
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=false;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=true;
				document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true;
				AbilitaAvvocato();
      		}
		} else { //Tutti gli altri casi di posizione giuridica...
 			if (pos == "03" || pos == "14") {
        		if (document.LoadInserisciMisuraAlternativa.tipo[0].checked) {
		        	nodeistituto.style.visibility='visible';
		          	nodecssa.style.visibility='visible';
		          	nodesor.style.visibility='visible';
		          	nodeautoritaC.style.visibility='visible';
		          	nodeautoritaE.style.visibility='hidden';
		          	nodebottone.style.visibility='visible';
		          	nodeavvocati.style.visibility='hidden';
		          	nodecssa.style.top='-70px';
		          	nodesor.style.top='-70px';
		          	nodeistituto.style.top='-70px';
		          	nodeautoritaC.style.top='-70px';
		          	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
		            	nodebottone.style.top='-300px';
		          	} else {
		            	nodebottone.style.top='-200px';
		          	}
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false;
		          	DisabilitaAvvocato();
		         	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value="";
		         	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>.value="";
		         	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value="";
				} else if (document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
					nodeautoritaE.style.visibility='hidden';
					nodecssa.style.visibility='visible';
					nodesor.style.visibility='visible';
					nodeautoritaC.style.visibility='visible';
					nodeistituto.style.visibility='hidden';
					nodeavvocati.style.visibility='hidden';
					nodebottone.style.visibility='visible';
					if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
						nodebottone.style.top='-310px';
					} else {
						nodebottone.style.top='-240px';
					}
					// 20170914: [SG] modificata distanza in px
					nodesor.style.top='-100px';
					nodecssa.style.top='-100px';
					nodeautoritaC.style.top='-100px';
//           		nodesor.style.top='-140px';
//           		nodecssa.style.top='+25px';
//           		nodeautoritaC.style.top='-140px';
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=false;
		          	document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false;
		          	DisabilitaAvvocato();
				}
  			} else {
  				if (pos == "12" || pos == "13" || pos == "29") {
<%
       				if ("DIFFERIMENTO_PENA".equals(tipoMisura)) {
%>
		          		nodeautoritaE.style.visibility='visible';
						nodecssa.style.visibility='visible';
						nodesor.style.visibility='visible';
						nodeautoritaC.style.visibility='hidden';
						nodeistituto.style.visibility='hidden';
						nodeavvocati.style.visibility='visible';
		          		nodebottone.style.visibility='visible';
						nodecssa.style.top='-35px';
						nodesor.style.top='-35px';
						nodeavvocati.style.top='-110px';
						nodebottone.style.top='-90px';
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=true;
		          		document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false;
		          		AbilitaAvvocato();
<%
					} else {
%>
						nodeautoritaE.style.visibility='hidden';
						nodecssa.style.visibility='visible';
						nodesor.style.visibility='visible';
						nodeautoritaC.style.visibility='visible';
						nodeistituto.style.visibility='hidden';
						nodeavvocati.style.visibility='hidden';
		          		nodebottone.style.visibility='visible';
						nodecssa.style.top='-100px';
						nodesor.style.top='-100px';
						nodeautoritaC.style.top='-100px';
		          		if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.length) {
		            		nodebottone.style.top='-300px';
		          		} else {
		           	 		nodebottone.style.top='-200px';
		          		}
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=false;
						document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false;
						DisabilitaAvvocato();
<%
					}
%>
     			} else {
		     		nodeautoritaE.style.visibility='visible';
					nodecssa.style.visibility='visible';
					nodesor.style.visibility='visible';
					nodeautoritaC.style.visibility='hidden';
					nodeistituto.style.visibility='hidden';
					nodeavvocati.style.visibility='hidden';
					nodebottone.style.visibility='visible';
					nodecssa.style.top='-35px';
					nodesor.style.top='-37px';
					nodebottone.style.top='-240px';
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.disabled=false;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=true;
					document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=true;
					document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=true;
					DisabilitaAvvocato();
     			}
  			}
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
    <font class="campo"><%=tipmis%></font>
</td>
</tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=azione%>">
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="HIDDEN" name="lFlagAffi" value="<%=lFlagAffi%>">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
  <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">

<%
  String scarceratodisabilita = null;
  String dascarceraredisabilita = null;
  String disabilitaData = null;

  String scarcerato = null;
  String scarcerare = null;

  if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
  {
%>
    <input type="HIDDEN" name="flagmisura" value="N">
<%
    scarcerare ="checked";
  }
  else
  {
%>
    <input type="HIDDEN" name="flagmisura" value="S">
<%
  }

if(misuraalternativa!= null && misuraalternativa.getCodTipoUfficioScarcerazione()!= null)
{
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV"))
   {
    scarcerato ="checked";
    if(misuraalternativa.getFlagUfficioInserimento()== null && misuraalternativa.getDataScarcerazione() != null)
    {
     dascarceraredisabilita="disabled";
    }
   }
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC"))
   {
    scarcerare ="checked";
    if(misuraalternativa.getFlagUfficioInserimento()== null && misuraalternativa.getDataScarcerazione() != null)
    {
      scarceratodisabilita="disabled";
    }
   }
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("-"))
  {
   scarcerare ="checked";
  }
}

if(misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV"))
{
   if(misuraalternativa.getFlagUfficioInserimento()== null && misuraalternativa.getDataScarcerazione()!= null )
    {
      disabilitaData ="readonly";
    }
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
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
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
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>

        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
</table>
  <table width=90%>
    <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza Tribunale di Sorveglianza </td>
    </tr>
<%
    if( misuraalternativa.getIdMisuraAlternativa() != null )
    {
%>
      <tr>
        <td class="l">Anno /Numero SIUS</td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
          <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
        </td>
        <td class="l"> Anno / Numero Ordinanza</td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
          <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Emittente </td>
        <td class="l" colspan="3">
          <font class="campo"> TRIBUNALE DI SORVEGLIANZA DI <%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Oggetto Ordinanza </td>
        <td class="l" colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
      </tr>
      <tr>
        <td class="l">Data Emissione Ordinanza </td>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(), "dd-MM-yyyy"))%>
          </font>
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">
        </td>
      </tr>
      <tr>
        <td class="l">Luogo di espiazione della misura</td>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>&nbsp;
          </font>
      </td>
      </tr>
<%
    if( verbale.getDataEmissione()!= null )
    {
%>
      <tr>
        <td class="l">Data Sottoscrizione Verbale Obblighi</td>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
<%
    }

    if( misuraalternativa.getDataInizioMisura()!= null )
    {
%>
      <tr>
        <td class="l">Data Inizio Misura</td>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(), "dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
<%
    }

    if( misuraalternativa.getNumAnniMisura() != null || misuraalternativa.getNumGiorniMisura()!= null || misuraalternativa.getNumMesiMisura()!= null)
    {
%>
      <tr>
        <td class="l">Quantità della misura</td>
          <td class="l" colspan="3">
           Anni&nbsp;
           <font class="campo">
             <%=StringUtils.toStringJSP(misuraalternativa.getNumAnniMisura(),"0")%>
           </font>
           Mesi&nbsp;
           <font class="campo">
             <%=StringUtils.toStringJSP(misuraalternativa.getNumMesiMisura(),"0")%>
            </font>
            Giorni&nbsp;
            <font class="campo">
              <%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniMisura(),"0")%>
            </font>
          </td>
        </tr>
<%
    }

    if( misuraalternativa.getDataFineMisura()!= null )
    {
%>
      <tr>
        <td class="l">Data scadenza misura</td>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
<%
    }
%>
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan="3">
        <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2 ><%= StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
      </td>
    </tr>
  </table>
<table>
<%
  if( (!lPosizione.isLibero() && lFlagAffi.equals("N"))
      && !lPosizione.getCodPosizioneGiuridica().equals("12")
      && !lPosizione.getCodPosizioneGiuridica().equals("13")
      && !lPosizione.getCodPosizioneGiuridica().equals("29"))
  {
%>
    <tr>
      <td class="l">Da scarcerare &nbsp;<input type="radio" name="tipo" value="scarcerare"   <%=StringUtils.toStringJSP(dascarceraredisabilita)%>  <%=StringUtils.toStringJSP(scarcerare)%> onclick="radio();">
      &nbsp; Scarcerato  &nbsp; <input type="radio" name="tipo" value="scarcerato"  <%=StringUtils.toStringJSP(scarceratodisabilita)%> <%=StringUtils.toStringJSP(scarcerato)%> onclick="radio();"></td>
      <td class="l">Data Scarcerazione  &nbsp;
        <input value="<%=DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" <%=disabilitaData%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>"  <%=disabilitaData%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getDateToString(misuraalternativa.getDataScarcerazione(),"yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>"  <%=disabilitaData%> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
<%
    }
%>
  </table>
<%
  }
  else
  {
%>
   <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius"  type="text" size="4" maxlength="4"
               name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input Title="Numero Sius" type="text" size="6" maxlength="6" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>"                
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Ordinanza</td>
      <td class="l">
        <input Title="Anno Ordinanza" type="text" size="4" maxlength="4" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input Title="Numero Ordinanza" type="text" size="6" maxlength="6"
               name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>"  
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">
      	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:ListaComuniEmitTdsMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');"><img src="/images/filefolder.gif" border=0></a>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Oggetto Ordinanza </td>
      <td class="L" colspan="3">
        <select Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Ordinanza </td>
      <td class="l" colspan="3">
        <font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo di espiazione della misura</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo della Detenzione Domiciliare" name="<%= ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>" size=35 type="text" onChange="pulisciId();">
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Quantità della misura</td>
      <td class="l" colspan="3">
        Anni&nbsp;<input Title="Anni Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
        Mesi&nbsp;<input Title="Mesi Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)">
        Giorni&nbsp;<input Title="Giorni Misura" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="l">Data scadenza misura</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan="3">
        <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>
  </table>
  <table>
<%
    if(   (!lPosizione.isLibero() && lFlagAffi.equals("N"))
        && !lPosizione.getCodPosizioneGiuridica().equals("12")
        && !lPosizione.getCodPosizioneGiuridica().equals("13")
        && !lPosizione.getCodPosizioneGiuridica().equals("29"))
    {
%>
      <tr>
        <td class="l">Da scarcerare &nbsp;
          <input type="radio" name="tipo" value="scarcerare" checked onclick="radio();">
          &nbsp; Scarcerato &nbsp;
          <input type="radio" name="tipo" value="scarcerato" onclick="radio();">
        </td>
        <td class="l">Data Scarcerazione &nbsp;
          <input type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input type="text" size="2" maxlength="2" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input type="text" size="4" maxlength="4" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
<%
    }
  }
%>
</table>
<table width="100%">
  <tr>
    <td class="Titolo" width="100%" colspan=6> Magistrato Firmatario </td>
  </tr>
  <tr>
  	<%-- MEV10-s3: aggiunta proprietà width --%>
    <td class="l" width="30%">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
  <tr>
    <td class="Titolo" width="100%" colspan=6>Destinatari</td>
  </tr>
 </table>
<div id="divautoritacompetenteE" style="width: 100%; visibility:hidden; position:relative; " >
<table width="100%">
  <tr>
<!--autorità di polizia-->
<%
    if ((lPosizione.isLibero()) && (verbale== null || verbale.getIdVerbale()== null))
    {
%>
      <td class="l" width=30%>Destinatario per esecuzione <font class=ob>(*)</font></td>
<%
    }
    else
    {
%>
      <td class="l" width=30%>Autorità Competente per territorio <font class=ob>(*)</font></td>
      <%}%>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
             <%=codiceAutoritaE%>
             </select>
           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">
<%if(autoritaEsternaE != null && autoritaEsternaE.getDescrSede() != null)
   {%>
      <input title="Sede Autorita Esterna" value="<%=autoritaEsternaE.getDescrSede()%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
<%}else
    {%>
          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
  <%}%>
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
</table>
</div>

<div id="divIstituto" style="width: 100%; visibility:hidden; position:relative; " >
<table width="100%">
 <tr>
     <td class="l" width=30%>Istituto di Detenzione <font class=ob>(*)</font></td>
  <td class="l">
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
</table>
</div>

<div id="divcssa" style="visibility:hidden; position:relative; width:100%;">
<table width="100%">
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
    <tr>
    	<td class="l">Sede</td>
      	<td class="L">
      		<input readonly Title="Sede UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
      		<input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
      		<a href="Javascript:ListaCSSA('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
        		<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
  	</tr>
</table>
</div>
<div id="divsor" style="width: 100%; visibility:hidden; position:relative; " >
<table width="100%">
  	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="L">
       		<input title="ufficio" value="<%=UffUDS%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" maxlength="35" size="25">
     		<a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
         		<img src="/images/filefolder.gif" border=0>
       		</a>
      	</td>
	</tr>

   	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
        <td class="L"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
		<td class="L">
			<input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        	<input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        	<a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>');">
        		<img src="/images/filefolder.gif" border=0>
       		</a>
       	</td>
	</tr>
</table>
</div>
<div id="divautoritacompetenteC" style="visibility:hidden; position:relative; width:100%;">
<table width="100%">
 <tr>
<!--autorità di polizia-->
          <td class="l" width=30%>Autorità Competente per territorio <font class=ob>(*)</font></td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
             <%=codiceAutoritaC%>

             </select>

           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">
<%if(autoritaEsternaC.getDescrSede()!= null){%>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>"  maxlength="35" size="35">
<%}else{%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>"  maxlength="35" size="35">

<%}%>
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
</table>
</div>
<div id="divavocati" style="visibility:hidden; position:relative; width:100%;">
<table width="100%">
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
        <table>
          <tr>
            <td class="l" width="100%">Per Avvocato&nbsp;
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
         <table>
          <tr><td class="l" width="35%">Autorità Destinazione </td >
          <td class="L" colspan="3">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>

     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
        <td class="l">Note</td>
       <td  class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=30 ></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
 }
%>
  </tr>
</table>
</table>
</div>
<div id="divbottone" style="visibility:visible; position:relative; width:100%;">
<table width="100%">
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
    </td>
  </tr>
</table>
</div>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

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

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>","numeric","Quantità della misura : Il Campo Anni è numerico");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>","numeric","Quantità della misura : Il Campo Mesi è numerico");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>","numeric","Quantità della misura : Il Campo Giorni è numerico");
<%
  }

  if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
  {
   if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
   {
    if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
    {
%>
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");
<%
    }
   }
  }

  if(!lPosizione.isLibero() && lFlagAffi.equals("N") && !lPosizione.getCodPosizioneGiuridica().equals("12")
     && !lPosizione.getCodPosizioneGiuridica().equals("13")
     && !lPosizione.getCodPosizioneGiuridica().equals("29"))
  {
%>
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>","lt=2099");
<%
  }
%>
</script>
</body>
</html>