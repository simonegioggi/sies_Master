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
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra" 						scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratocompetente"         				scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="penaresidua"        						scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="codiceAutoritaE"   						scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       					scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="luogodetenzione"      						scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="motivoProvv"   							scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"   							scope="request" class="java.lang.String"/>
<jsp:useBean id="eventoDecreto"   							scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="tipoSospensione"   						scope="request" class="java.lang.String"/>
<jsp:useBean id="idmisuraalternativa"   					scope="request" class="java.lang.String"/>
<jsp:useBean id="distretto" 								scope="request" class="java.lang.String"/>
<jsp:useBean id="codicemotivo"      						scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioEmittenteArrestiDomiciliari"    scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"            						scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"  						scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" 							scope="request" class="java.lang.String"/>
<jsp:useBean id="revocaDopoSospensioneProvvisoria" 			scope="request" class="java.lang.String"/>

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
      var naturaMA;
      if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='AFFIDAMENTO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='AFFIDAMENTO51BIS')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA_51_BIS%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='DETENZIONE')
      {
		<%-- MERGE v10 COLLAUDO: modificata casistica per gestione arresti domiciliari --%>
		tipoMA='<%=ICostantiMisuraAlternativa.ARRESTI_DOMICILIARI%>';
		naturaMA='<%=ICostantiMisuraAlternativa.REVOCA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='DETENZIONE51BIS')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA_51_BIS%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='SEMILIBERTA')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='SEMILIBERTA51BIS')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA_51_BIS%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='INDULTINO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.INDULTINO%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='INDULTINO51BIS')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.INDULTINO%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA_51_BIS%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>';
        naturaMA='<%=ICostantiMisuraAlternativa.SOSPENSIONE_PROVVISORIA%>';
      }

      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }

    function Verify()
	  {

<%if((!lPosizione.isLibero()) || (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
{
 if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
  {%>
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
<%}
 }
}%>


		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }

      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      var campo = document.LoadInserisciMisuraAlternativa.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

	// MEV10-s3: aggiunto controllo preventivo
  	if (document.LoadInserisciMisuraAlternativa.flagmisura &&
		  document.LoadInserisciMisuraAlternativa.flagmisura.value=="N") {
  		if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
      if (!ControllaData(data_to_verify) )
		  {
        alert('Data di emissione Decreto non valida');
			  return false;
		  }


    if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="" || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=='-')
    {
      alert("La Sede dell'Ufficio Emittente è obbligatoria");
      document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();

      return false;
    }

}
	// MEV10-s3: aggiunto controllo preventivo
	if (document.LoadInserisciMisuraAlternativa.tipo) {
    	if (!document.LoadInserisciMisuraAlternativa.tipo[0].checked &&
    			!document.LoadInserisciMisuraAlternativa.tipo[1].checked) {
    		alert("Indicare se eseguita dal Magistrato di Sorveglianza o dalla Procura");
      		return false;
    	}
	}

<%
  if(tipoSospensione.equals("AFFIDAMENTO") || tipoSospensione.equals("DETENZIONE")
     || tipoSospensione.equals("SEMILIBERTA") || tipoSospensione.equals("INDULTINO"))
  {
%>
	//MEV10-s3: aggiunti controlli preventivi
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%> &&
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value.length==1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%> &&
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value.length==1)
		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value;
	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%> &&
			document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%> &&
  		document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>) {
		var data_to_verify_ing = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
    	if (!ControllaDataPassaVuota(data_to_verify_ing) ) {
  	  		alert('Data ingresso in carcere non valida');
  			return false;
 		}
	}
<%}%>

  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
  {
    alert("Il  Magistrato Firmatario è obbligatorio");
       document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
    return false;
  }

	// MEV10-s3: aggiunto controllo preventivo
	if(document.LoadInserisciMisuraAlternativa.tipo &&
			document.LoadInserisciMisuraAlternativa.tipo[1].checked == true) {
 		if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
 		{
			alert("L'Istituto di Detenzione è obbligatorio");
			return false;
		}
	}

	// MEV10-s3: aggiunto controllo preventivo
	if(document.LoadInserisciMisuraAlternativa.tipo &&
			document.LoadInserisciMisuraAlternativa.tipo[0].checked == true) {
		if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
		{
			alert("L'Autorità di destinazione è obbligatoria");
			document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.focus();
			return false;
		}
	}

	if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value == "") {
		// MEV10-s3: modificato msg
		alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.focus();

		return false;
	}

	if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value=="")
	{
		// MEV10-s3: modificato msg
		alert("Il Tribunale di Sorveglianza è obbligatorio");
		document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.focus();
		return false;
	} 
	
	return true;
}

function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

 function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
 function ListaUDS(a_formname,a_fieldname)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }

 function VerificaCampiObbligatori () { 
 	<%--
  	if((document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO%>.value=="" ||
 			document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO%>.value=="-")
 			&& (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE%>.value=="" ||
 	    			document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE%>.value=="-"))
 	{
 		alert("L'Oggetto Decisione è obbligatorio");

 		return false;
 	}
 	
 	var campoConUfficioSorveglianza = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.value;
 	if((campoConUfficioSorveglianza.value=="" || campoConUfficioSorveglianza.value=="-") && (campoConUfficioSorveglianza.value=="" || campoConUfficioSorveglianza.value=="-"))
 	{
 		alert("L'Ufficio Emittente è obbligatorio");
 		return false;
 	}
 	--%>
 	
 	var campoSedeTdsEmitt = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value;
 	if((campoSedeTdsEmitt=="" || campoSedeTdsEmitt=="-") && (campoSedeTdsEmitt=="" || campoSedeTdsEmitt=="-"))
 	{
 		alert("La Sede Ufficio Emittente è obbligatoria");
 		return false;
 	}
 	
 	<%-- 20170907: [SG] prevenzione errore js --%>
 	var campoCodTipoAutorita;
 	if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>)
 		campoCodTipoAutorita = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.value;
 	if ((campoCodTipoAutorita=="" || campoCodTipoAutorita=="-") && (campoCodTipoAutorita=="" || campoCodTipoAutorita=="-")) {
 		alert("L'Autorità competente per territorio è obbligatoria");
 		return false;
 	}
 		
 	var campoSedeTds = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value;
 	if((campoSedeTds=="" || campoSedeTds=="-") && (campoSedeTds=="" || campoSedeTds=="-")) {
 		alert("Il Tribunale di Sorveglianza è obbligatorio");
 		return false;
 	}
 	
 	campoCodTipoAutorita = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value;
 	if ((campoCodTipoAutorita=="" || campoCodTipoAutorita=="-") && (campoCodTipoAutorita=="" || campoCodTipoAutorita=="-")) {
 		// MEV10-s3: modificato msg
		alert("L'Ufficio / Magistrato di Sorveglianza è obbligatorio");
 		return false;
 	}
 	
 	var campoCodUds = document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>.value;
 	if ((campoCodUds=="" || campoCodUds=="-") && (campoCodUds=="" || campoCodUds=="-")) {
 		alert("L'Ufficio di Destinazione è obbligatorio");
 		return false;
 	}
 	return true;
   }
 
function radio() {
      var nodeAutorita;
       var nodeIstituto;
        nodeAutorita=document.getElementById('autorita');
        nodeIstituto=document.getElementById('istituto');
        nodedivsor= document.getElementById('divsor');

	// MEV10-s3: aggiunto controllo preventivo
    if (document.LoadInserisciMisuraAlternativa.tipo) {
        if(document.LoadInserisciMisuraAlternativa.tipo[0].checked)
        {
          nodeAutorita.style.visibility='visible';
          nodedivsor.style.visibility='visible';
          nodeIstituto.style.visibility='hidden';

          nodedivsor.style.top="-35px";

          document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled =true;
          document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled =false;

<%if(tipoSospensione.equals("AFFIDAMENTO") || tipoSospensione.equals("DETENZIONE") || tipoSospensione.equals("SEMILIBERTA")|| tipoSospensione.equals("INDULTINO"))
{%>
          document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value="";
          document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value="";
          document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value="";
<%}%>
        }
        else
        {
         document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled =false;
         document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled =true;

          nodeIstituto.style.visibility='visible';
          nodeAutorita.style.visibility='hidden';
          nodedivsor.style.visibility='visible';

          nodedivsor.style.top="-78px";

       }
    }
}
</script>
    
  <jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>
       <body class="corpo" onLoad="radio();">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();
%>
<%
  if(tipoSospensione.equals("DETENZIONE"))
  {
%>
    <font class="campo">Revoca Arresti Domiciliari e Rigetta applicazione misura alternativa</font>
<%
  }
  else if(tipoSospensione.equals("AFFIDAMENTO"))
  {
%>
    <font class="campo">Sospensione Provvisoria Affidamento In Prova</font>
<%
  }
  else if(tipoSospensione.equals("SEMILIBERTA"))
  {
%>
    <font class="campo">Sospensione Provvisoria Semilibertà</font>
<%
  }
  else if(tipoSospensione.equals("INDULTINO"))
  {
%>
   <font class="campo">Sospensione Provvisoria Art.51 Ter L.207/2003 </font>
<%
  }
  else if(tipoSospensione.equals("AFFIDAMENTO51BIS"))
  {
%>
    <font class="campo">Sospensione Provvisoria 51 Bis Affidamento In Prova (SENZA CUMULO)</font>
<%
  }
  else if(tipoSospensione.equals("DETENZIONE51BIS"))
  {
%>
    <font class="campo">Sospensione Provvisoria 51 Bis Detenzione Domiciliare (SENZA CUMULO)</font>
<%
  }
  else if(tipoSospensione.equals("SEMILIBERTA51BIS"))
  {
%>
    <font class="campo">Sospensione Provvisoria 51 Bis Semilibertà (SENZA CUMULO)</font>
<%
  }
  else if(tipoSospensione.equals("INDULTINO51BIS"))
  {
%>
   <font class="campo">Sospensione Provvisoria Art.51 Bis L.207/2003 (SENZA CUMULO) </font>
<%
  }
  else if(tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
  {
%>
   <font class="campo">Sospensione Provvisoria Espiazione Pena presso Domicilio </font>
<%
  }
  else if(tipoSospensione.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
  {
%>
   <font class="campo">Sospensione Provvisoria Art.51 Bis Espiazione Pena presso Domicilio (SENZA CUMULO) </font>
<%
  }

%>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInsRevocaArrestiDomiciliari">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">

<%
  if(tipoSospensione.equals("DETENZIONE"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="DETENZIONE">
<%
  }
  else if(tipoSospensione.equals("AFFIDAMENTO"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="AFFIDAMENTO">
<%
  }
  else if(tipoSospensione.equals("SEMILIBERTA"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="SEMILIBERTA">
<%
  }
  else if(tipoSospensione.equals("INDULTINO"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="INDULTINO">
<%
  }
  else if(tipoSospensione.equals("AFFIDAMENTO51BIS"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="AFFIDAMENTO51BIS">
<%
  }
  else if(tipoSospensione.equals("DETENZIONE51BIS"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="DETENZIONE51BIS">
<%
  }
  else if(tipoSospensione.equals("SEMILIBERTA51BIS"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="SEMILIBERTA51BIS">
<%
  }
  else if(tipoSospensione.equals("INDULTINO51BIS"))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="INDULTINO51BIS">
<%
  }
  else if(tipoSospensione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>">
<%
  }
  else if(tipoSospensione.equals(ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="<%=ICostantiMisuraAlternativa.SOSP_ESP_PRESSO_DOM_51BIS%>">
<%
  }
  else if(tipoSospensione.equals(ICostantiMisuraAlternativa.RIPR_ESP_PRESSO_DOM))
  {
%>
    <input type="HIDDEN" name="tipomisura" value="<%=ICostantiMisuraAlternativa.RIPR_ESP_PRESSO_DOM%>">
<%
  }
%>

  <INPUT type="hidden" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="HIDDEN" name="revocaDopoSospensioneProvvisoria" value="<%=revocaDopoSospensioneProvvisoria%>">
<%

String sorv = null;
String proc = null;

if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
{%>
  <input type="HIDDEN" name="flagmisura" value="N">
 <%
sorv ="checked";

}else{%>
  <input type="HIDDEN" name="flagmisura" value="S">

<%}%>


<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=idmisuraalternativa%>">
<input type="HIDDEN" name="codiceMotivo" value="<%=eventoDecreto.getCodMotivo()%>">


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
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

<%
               if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null)
               {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               }
%>
            </td>
           </tr>
<%
               if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td>
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
              if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null)
              {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
              }
%>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
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
           <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
           }  }
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
           <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>


      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
      }
    }
%>
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
           <input title = "Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA%> >
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
           }else
            {%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

 <%         }
        }
      }
}
%>
 <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">

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
          <input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>
      </tr>
</table>
<table width ="90%">
   <tr>
      <td class="Titolo" colspan='8'> Dati Decreto del Magistrato di Sorveglianza </td>
   </tr>
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
      <td class="l"> Anno / Numero Decreto</td>
      <td class="l">
        <input Title="Anno Decreto" type="text" size="4" maxlength="4"
               name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input Title="Numero Decreto" type="text" size="6" maxlength="6"
               name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" 
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
    </tr> 
    <tr>		
	    <td class="l">Ufficio Emittente <font class=ob>(*)</font></td>               
	    <td class="l" colspan="3">	      
	      <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff)%>
	    </td>	   	     
	</tr>
    
    <tr>
      <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Oggetto Ordinanza</td>
      <td class="L" colspan="3">
<%
      if(codicemotivo != null && !codicemotivo.equals(""))
{
%>
        <font class="campo"><%=motivoProvv%></font>
        <INPUT type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codicemotivo%>">
<%
}else{
%>
        <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
<%}%>
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Ordinanza</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input title = "Giorno Data Emissione Decreto" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Mese Data Emissione Decreto" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Anno Data Emissione Decreto" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
        </font>
      </td>
    </tr>
    <tr>
      <td  class="l">Note</td>
      <td  class="L" colspan="3">
        <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>
</table>

<table width ="100%">
   	<tr>
     	<td class="Titolo" colspan=6>Magistrato Firmatario</td>
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
      	<td class="Titolo" colspan=6>Destinatari</td>
   	</tr>
</table>

<%
  if(revocaDopoSospensioneProvvisoria!= null && revocaDopoSospensioneProvvisoria.equals("direttamente"))
  {
%>
<table width ="100%">
	<tr>
	 <!--autorità esterna e-->
          <td class="l" width ="30%">Autorità competente per territorio <font class=ob>(*)</font>
           <input type="hidden" name="notificaE" value="autorita">

          </td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
             <%=codiceAutoritaE%>

             </select>
             <input type="hidden" name="notificaPolizia" value="E">
           </td>
    </tr>
    <tr>
      <td class="l" width ="30%">Sede</td>
     <td class="L">
       <input title="Sede Autorita Esterna" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E %>"  cols=30></textarea>
            </td>
    </tr>
</table>
<%}%>

<%
  if(revocaDopoSospensioneProvvisoria!= null && revocaDopoSospensioneProvvisoria.equals("dopoSospensione"))
  {
%>
<table width="100%">
<tr>
     <td class="l" width ="30%">Istituto di Detenzione <font class=ob>(*)</font>
           <input type="hidden" name="notificaE" value="istituto">
</td>
 <td class="l">
<%if(posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione()!= null && posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)
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
<%}%>

<div id="divsor" style="width: 100%; visibility:visible;  position:relative;" >
<table  width ="100%">
	<%-- MEV10-s3: modificato layout con aggiunta etichette --%>
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
       		<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
      			<img src="/images/filefolder.gif" border=0>
      		</a>
      	</td>
   	</tr>

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
      		<input type="hidden" name="magSorv" value="S">
       		<input type="hidden" name="notificaMagistrato" value="C">
       		<input title="ufficio" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" maxlength="35" size="25">
     		<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
            	<img src="/images/filefolder.gif" border=0>
            </a>
      	</td>
	</tr>
</table>
</div>

<table width="100%">
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica</td></tr>
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
          <tr><td class="l" width="30%">Ufficio di Destinazione <font class=ob>(*)</font></td>
          <td class="L">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede di Destinazione</td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
     </tr>
     <tr>
      <td class="l">Note</td>
      <td  class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>" cols=50 ></textarea>
      </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
 }
%>
  </tr>
  </table>
	<tr>
		<td class="lNoBord" colspan="2">
		  <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onclick="Javascript: return VerificaCampiObbligatori();">
		</td>
	</tr>

</table>
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
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  <%if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
     {%>
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2050");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

//Controlli Data Emissione Decreto
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2050");

//Controlli Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");
<%}%>
<%
if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)

 {%>
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
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
  }
 }
}%>

<%-- <%
  if(tipoSospensione.equals("AFFIDAMENTO") || tipoSospensione.equals("DETENZIONE")
     || tipoSospensione.equals("SEMILIBERTA") || tipoSospensione.equals("INDULTINO"))
  {
%>
//Controlli Data Ingresso in Carcere
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","lt=2050");
<%}%> --%>

 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>","alphabetic");

 frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>