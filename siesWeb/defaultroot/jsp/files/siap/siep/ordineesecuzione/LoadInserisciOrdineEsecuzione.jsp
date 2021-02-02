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
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>

<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="StrdataFinePenaA"    scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

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
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

	function Verify() {
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
		var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      	if (!ControllaData(data_to_verify)) {
        	alert('Data di emissione non valida');
			return false;
		}
		<%
		      if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10") && lPosizione.getCodPosizioneGiuridica().equals("16") && lPosizione.getCodPosizioneGiuridica().equals("20")
		          && lPosizione.getCodPosizioneGiuridica().equals("46") && lPosizione.getCodPosizioneGiuridica().equals("47")) || (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
		      {
		        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
		        {
		          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
		          {
		%>
		    if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
					  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
				  if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
					  document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;
		
				  var data_to_verifica = document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;
		
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
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0' +
				document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0' +
				document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
	 	var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
       	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="") {
       		alert("Il Cognome del Magistrato è obbligatorio");
		    return false;
        }
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="") {
			alert("Il Nome del Magistrato è obbligatorio");
		    return false;
		}
		var campo = document.LoadInserisciOrdineEsecuzione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;
		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '10'
				&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '07'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '02'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '04'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '16'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '20'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '46'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '47'
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '12'
		   		// MEV10-s3: aggiunte altre posizioni giuridiche
		     	&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '71'
		   		&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '72'
				&& document.LoadInserisciOrdineEsecuzione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.value != '70') {
			// MERGE v10: aggiunto controllo preventivo
			if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> &&
					document.LoadInserisciOrdineEsecuzione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
				alert("Autorità Destinazione obbligatorio");
		        return false;
	       	}
		}
		<%
		if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") && posizioneluogoaltra != null &&
				posizioneluogoaltra.getAltraCausa() != null && posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica() != null &&
				!posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
		%>
			<!-- MERGE v10: aggiunto controllo preventivo -->
			if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> &&
					document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "") {
				alert("Autorità Destinazione obbligatorio");
		        return false;
       		}
		<%
		}
		if (lPosizione.getCodPosizioneGiuridica().equals("07") 		||
				lPosizione.getCodPosizioneGiuridica().equals("10")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("02")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("04")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("16")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("20")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("46")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("47")  || 
		    	lPosizione.getCodPosizioneGiuridica().equals("12")  ||
		    	lPosizione.getCodPosizioneGiuridica().equals("71")  ||
				// MEV10-s3: aggiunte altre posizioni giuridiche
				lPosizione.getCodPosizioneGiuridica().equals("72")  ||
				lPosizione.getCodPosizioneGiuridica().equals("70")) {
			if (lFascicoloAssociato.getFlagAltraCausa()== null 		||
					(lFascicoloAssociato.getFlagAltraCausa()!=null  &&
					lFascicoloAssociato.getFlagAltraCausa().equals("N"))) {
		%>
				<!-- MERGE v10: aggiunto controllo preventivo -->
			   	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> &&
					   document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
			   		alert("Autorità Destinazione obbligatoria");
			      	return false;
			   	}
		<%
			} // end if lFascicoloAssociato
		}// end if lPosizione
		if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
			if (posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null 	&&
					posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica() != null 	&&
					posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
		%>
				<!-- MERGE v10: aggiunto controllo preventivo -->
		   		if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%> &&
		   				document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
		   			alert("Autorità Destinazione obbligatoria");
		      		return false;
		   		}
		<%
			} // end if posizioneluogoaltra
		} // end if lFascicoloAssociato
		if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
		%>
			if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>.value == "-") {
				alert("Altra autorità di polizia obbligatoria");
		      	return false;
		   	}
		<%
		}
		if ((lFascicoloAssociato.getFlagAltraCausa() != null &&
				lFascicoloAssociato.getFlagAltraCausa().equals("S")) ||
				(lPosizione.getCodPosizioneGiuridica() != null &&
				lPosizione.getCodPosizioneGiuridica().equals("07")) ||
				(lPosizione.getCodPosizioneGiuridica() != null &&
				lPosizione.getCodPosizioneGiuridica().equals("10"))) {
		%>
			var associaBdmc = 1;
		 	<%
		 	// MERGE v10: aggiunti controlli preventivi e diversificata gestione dei controlli
		 	boolean pos0710facS = false;
		 	boolean pos0710facN = false;
		 	if (lFascicoloAssociato.getFlagAltraCausa() != null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) {
		 		if (lPosizione.getCodPosizioneGiuridica() != null &&
		 				("07".equals(lPosizione.getCodPosizioneGiuridica()) ||
								"10".equals(lPosizione.getCodPosizioneGiuridica())))
		 			pos0710facS = true;
		 	%>
				if ((document.LoadInserisciOrdineEsecuzione.annoBdmc && document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "") 				&&
						(document.LoadInserisciOrdineEsecuzione.numeroBdmc && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value == "") 		&&
						(document.LoadInserisciOrdineEsecuzione.sedeBdmc && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value == "") 			&&
						(document.LoadInserisciOrdineEsecuzione.autoritaBdmc && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value == "-")) {
					associaBdmc = 0;
					if (<%= pos0710facS %> && associaBdmc == 0 &&
							document.LoadInserisciOrdineEsecuzione.ComuneBdmc &&
							document.LoadInserisciOrdineEsecuzione.ComuneBdmc.value == "")
						associaBdmc == 0;
					else
						associaBdmc == 1;
				}

				if ((document.LoadInserisciOrdineEsecuzione.annoBdmc && document.LoadInserisciOrdineEsecuzione.annoBdmc.value != "") 				&&
						(document.LoadInserisciOrdineEsecuzione.numeroBdmc && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value != "") 		&&
						(document.LoadInserisciOrdineEsecuzione.sedeBdmc && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value != "") 			&&
						(document.LoadInserisciOrdineEsecuzione.autoritaBdmc && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value != "-")) {
					associaBdmc = 0;
					if (<%= pos0710facS %> && associaBdmc == 0 &&
							document.LoadInserisciOrdineEsecuzione.ComuneBdmc &&
							document.LoadInserisciOrdineEsecuzione.ComuneBdmc.value != "")
						associaBdmc == 0;
					else
						associaBdmc == 1;
				}
		 	<%
		 	} else {
				if (lPosizione.getCodPosizioneGiuridica() != null &&
						"07".equals(lPosizione.getCodPosizioneGiuridica()) ||
						"10".equals(lPosizione.getCodPosizioneGiuridica()))
					pos0710facN = true;
		 	%>
		 		if ((document.LoadInserisciOrdineEsecuzione.annoBdmc && document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "") 					&&
		 				(document.LoadInserisciOrdineEsecuzione.numeroBdmc && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value == "") 			&&
		 				(document.LoadInserisciOrdineEsecuzione.sedeBdmc && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value == "") 				&&
		 				(document.LoadInserisciOrdineEsecuzione.autoritaBdmc && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value == "-")) {
		 			associaBdmc = 0;
		 			if (<%= pos0710facN %> && associaBdmc == 0 &&
		 					document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc 				&&
		 					document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc.value == "" 	&&
			 				document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc 			&&
			 				document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc.value == "")
						associaBdmc == 0;
					else
						associaBdmc == 1;
		 		}
				if ((document.LoadInserisciOrdineEsecuzione.annoBdmc && document.LoadInserisciOrdineEsecuzione.annoBdmc.value != "") &&
						(document.LoadInserisciOrdineEsecuzione.numeroBdmc && document.LoadInserisciOrdineEsecuzione.numeroBdmc.value != "") &&
						(document.LoadInserisciOrdineEsecuzione.sedeBdmc && document.LoadInserisciOrdineEsecuzione.sedeBdmc.value != "") &&
						(document.LoadInserisciOrdineEsecuzione.autoritaBdmc && document.LoadInserisciOrdineEsecuzione.autoritaBdmc.value != "-")) {
					associaBdmc = 0;
					if (<%= pos0710facN %> && associaBdmc == 0 &&
							document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc &&
							document.LoadInserisciOrdineEsecuzione.altroLuogoBdmc.value != "" &&
							document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc &&
							document.LoadInserisciOrdineEsecuzione.comAltroLuogBdmc.value != "")
						associaBdmc == 0;
					else
						associaBdmc == 1;
				}
		 	<%
		 	}
		 	%>
		  	if (associaBdmc == 1) {
		  		alert("Le informazioni relative al fascicolo Bdmc se presenti vanno riempite tutte");
		  		return false;
			}
		  		
			if ((document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value != "-" ||
					document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value != "") &&
					document.LoadInserisciOrdineEsecuzione.annoBdmc.value == "") {
		  		alert("Destinatario per notifica B.D.M.C. puo' essere inserito solo in presenza di un associazione con un Fascicolo B.D.M.C.");
		  		return false;
			}	
		  				
		  	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value != "-" &&
		  			document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value == "") {
		  		alert("Inserire la sede per notifica B.D.M.C.");
		  		return false;
			}
		  	if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC.value == "-" &&
		  			document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC.value != "") {
		  		alert("Inserire l'autorita destinazione per notifica B.D.M.C.");
		  		return false;
			}
		<%
		}
		%>
	} // end verify

    function ListaMagistrati(a_formname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function ListaUDS(a_formname,a_fieldname) {
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function VisualizzaBDMC() {
     	var nodeBDMC = document.getElementById('divDatiBDMC');
      	if (nodeBDMC.style.display=="none") {
        	display = document.getElementById('VisualizzaBDMC').style.display="none";
        	display = document.getElementById('NascondiBDMC').style.display="block";
        	nodeBDMC.style.display="block";
      	} else {
        	display = document.getElementById('VisualizzaBDMC').style.display="block";
        	display = document.getElementById('NascondiBDMC').style.display="none";
        	nodeBDMC.style.display="none";
      	}
    }
  	</script>
	</head>

	<body class="corpo">
  	<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        EventoModel lProvvedimento = new EventoModel();
        String lAzione = new String();

        if( modalita.equals("I") )
        {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.siep.ordineesecuzione.action.ActInserisciOEDetenutoQC";

         if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa()))
         {%>
            <font class="campo">Emissione Ordine di Esecuzione - DETENUTO PER ALTRA CAUSA</font>
         <%}else{%>
            <font class="campo">Emissione Ordine di Esecuzione - <%=lPosizione.getDescrPosizioneGiuridica()%></font>
         <%}
        }
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.siep.ordineesecuzione.action.ActModificaOEDetenutoQC";
%>
          <font class="campo">Modifica di Ordine di Esecuzione </font>
<%
        }
%>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <form method="POST" name="LoadInserisciOrdineEsecuzione" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciOrdineEsecuzione">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
            if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
            {
            	if(posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null){
%>
              		DETENUTO PER ALTRA CAUSA <%=posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica()%>
<%
              	} else {
%>
					DETENUTO PER ALTRA CAUSA 
<%
              	}

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
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
//modifica relativa al tipo istituto
          if(lAltraCausa.getIstitutoDetenzione() != null)
        //   if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
              // if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
             //  }
%>
            </td>
           </tr>
<%

            }

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
        else
   if(lLuogoDetenzione.getIstitutoDetenzione() != null)
       //if(!lLuogoDetenzione.getDescrTipoIstituto().equals("") && lLuogoDetenzione.getDescrTipoIstituto()!= null && !lLuogoDetenzione.getDescrTipoIstituto().equals("-"))
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
              //if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
            //  }
%>
            </td>
          </tr>
<%
        }

        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        // oppure REGIME DI PERMANENZA IN CASA (71)
        if(lPosizione.getCodPosizioneGiuridica() != null && 
           ( lPosizione.getCodPosizioneGiuridica().equals("02") || 
        	 lPosizione.getCodPosizioneGiuridica().equals("04") ||
        	 lPosizione.getCodPosizioneGiuridica().equals("71") ||
       		 // MEV10-s3: aggiunte altre posizioni giuridiche
       		 lPosizione.getCodPosizioneGiuridica().equals("72") ||
       		 lPosizione.getCodPosizioneGiuridica().equals("70"))
           )
        {
          if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Altro Luogo </td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>  maxlength="6" size="6" --%>
        <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>  maxlength="6" size="6" --%>
  <tr>
<%//fine modifica relativa al tipo istituto
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
      <tr>
<%
     if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")
         && !lPosizione.getCodPosizioneGiuridica().equals("16") && !lPosizione.getCodPosizioneGiuridica().equals("20") && !lPosizione.getCodPosizioneGiuridica().equals("46")
         && !lPosizione.getCodPosizioneGiuridica().equals("47")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
       }

       //if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta()!= null)
       //{
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
         <%--td class="l">Data Fine Pena Automatica</td>
         <td class="L"><font class="campo"><%=StrdataFinePenaA%> &nbsp;</font></td--%>
<%
       //}

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
    }

    if(   (!lPosizione.getCodPosizioneGiuridica().equals("07")
        && !lPosizione.getCodPosizioneGiuridica().equals("10")
        && !lPosizione.getCodPosizioneGiuridica().equals("16")
        && !lPosizione.getCodPosizioneGiuridica().equals("20")
        && !lPosizione.getCodPosizioneGiuridica().equals("46")
        && !lPosizione.getCodPosizioneGiuridica().equals("47"))
        || (    lFascicoloAssociato.getFlagAltraCausa() != null
            &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") )
      )
    {
      if( (    penaresidua.getFlagErgastolo() == null)
            || (   penaresidua.getFlagErgastolo() != null
                && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")
              )
           )
      {
        if( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null )
        {
%>
          <td class="l">Data Fine Pena</td>
          <td class="L" colspan=2>
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
<%
        }
        else if( penaresidua.getDataFine() != null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
          {
%>
            <td class="l">Data Fine Pena</td>
            <td class="L" colspan=2>
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }
          else
          {
%>
            <td class="l">Data Fine Pena</td>
            <td class="lRosso" colspan=2>
              <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }
        }
      }
    }
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L"colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
     </table>
     
     <table width=100%>
       <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
       <tr>
         <td class="l">Magistrato</td>
         <td class="L" colspan="3">
           <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
           <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
           <a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzione');">
             <img src="/images/filefolder.gif" border=0>
           </a>
        </td>
        <td>
          <input  type="hidden"  title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>"  name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  >
        </td>
      </tr>
    </table>

    
   <!-- ==================================================================== -->
   <!-- Inizio istruzioni x integrazione Bdmc Carlo                          -->
   <!-- Se detenuto altra causa o Libero                                     -->
   <!-- ==================================================================== -->
   <% if (   ( lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
          || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
          || (lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10")  )
         )
   {  // Libero o detenuto altra causa:
   %>
    <table>   
      <tr>
        <td class="l" id="VisualizzaBDMC" style="display:block" >
          <!--input type="button" name="vedi" value="Espandi BDMC (Banca Dati Misure Cautelari)" onClick="javascript:VisualizzaBDMC();"-->
          <a href="Javascript:VisualizzaBDMC();">Espandi BDMC (Banca Dati Misure Cautelari)</a>
        </td>
        <td class="l" id="NascondiBDMC" style="display:none" >
          <!--input type="button" name="vedi" value="Nascondi BDMC (Banca Dati Misure Cautelari)" onClick="javascript:VisualizzaBDMC();"-->
          <a href="Javascript:VisualizzaBDMC();">Nascondi BDMC (Banca Dati Misure Cautelari)</a>
        </td>
      </tr>
    </table>
   <% } %>
   
<div id="divDatiBDMC" style="display:none" >
   <% if (   ( lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
          || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
          || (lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10"))
         )
   {  // Libero o detenuto altra causa:
   %>
    <table width=100%>
      <tr><td class="Titolo" colspan=6> Fascicolo B.D.M.C.</td></tr>
      <tr>
        <td class="l">Anno/Numero </td>
        <td class="L" >
           <input  title="annoBdmc"  type="text" name="annoBdmc" maxlength="4" size="4">
         / <input  title= "numeroBdmc"    type="text" name="numeroBdmc"        maxlength="6" size="6">
        </td>
      
        <td class="l">Autorità </td>
        <td class="L" >
          <select id="autbdmc" Title="Autorità Bdmc" name="autoritaBdmc">
            <%=autoritaEmi%>
          </select>
        </td>
        <input type="HIDDEN" name="descautoritabdmc">
      </tr>
      <tr>
        <td class="l">Sede  </td>
        <td class="L">
          <input title="Sede Bdmc "  type="text" name="sedeBdmc" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','sedeBdmc');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
    </table>  
   <%} %>
   
    
    <!-- Se Libero ma Detenuto Altra Causa -->
    <% if (   (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") ) 
           && (   ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
               || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10") )
              )
          )  
    { %>
    <table width=100%>
      <tr>
        <td class="l" width=25%>Luogo di Detenzione  </td>
        <td class="l">
          <input readonly Title="Istituto" name="ComuneBdmc" value="" size=50>
          <input type="hidden"  Title="Istituto" name="detenzioneBdmc" value="" size=50>
          <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','detenzioneBdmc','ComuneBdmc');">
          <img src="/images/filefolder.gif" border=0></a></td>
      </tr>
    </table>
    <%} %>
   
    <!-- Se Libero ma NON detenuto altra causa -->
    <% if (   (lFascicoloAssociato.getFlagAltraCausa()!=null && !lFascicoloAssociato.getFlagAltraCausa().equals("S")) 
           && (    ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
                || (lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10"))
              )
          )
    {   %>
    <table width=100%>
      <tr>
        <td class="l" width=20%>Altro Luogo Detenzione</td>
        <td class="L"  >
          <input title="Altro Luogo Altra Causa" value="" type="text" name="altroLuogoBdmc" size="40">
        </td>
        <td class="l">Comune  </td><td class="L">
          <input title="Comune Bdmc "  type="text" name="comAltroLuogBdmc" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','comAltroLuogBdmc');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
    </table>
    <%} %>
   
    <!-- ================================== -->
    <!-- Destinatario per Notifica B.D.M.C. -->
    <!-- ================================== -->
    <% 
    if (   ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07") ) 
        || ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10") )
       )  
    {   %>
    <table width=100%>
      <tr>
        <td class="Titolo" colspan=6>Destinatario per Notifica B.D.M.C. </td>
      </tr>
      <tr>
        <td class="l" width=20%>Autorità Destinazione </td>
        <td class="L" colspan="3">
          <select  Title="Autorita Esterna Bdmc" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>_BDMC">
            <%=(new String(autoritaEsternaE)).replaceAll("selected />"," />") %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede </td>
        <td class="L">
          <input title="Sede Autorita Esterna Bdmc" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>_BDMC');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <TEXTAREA title="Note Bdmc" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>_BDMC"  cols=30></textarea>
        </td>
      </tr>
    </table>
   <%} %>
   <!-- ==================================================================== -->
   <!-- fine istruzioni x integrazione Bdmc Carlo                            -->
   <!-- ==================================================================== -->
</div>   
   
    <table width=100%>
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Esecuzione </td>
    </tr>

<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S") )
{%>
<tr>
	<%if(lAltraCausa.getCodTipoPosGiuridica().equals("74") || lAltraCausa.getCodTipoPosGiuridica().equals("75") 
    		|| lAltraCausa.getCodTipoPosGiuridica().equals("76") || lAltraCausa.getCodTipoPosGiuridica().equals("77"))
	{%>
        	<td class="l" width=20%>Istituto di Detenzione </td>
    <%} else {%>
		<td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font> </td>
	<%}%>
<%if(posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null &&
	 posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica() != null &&
     (posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23") ||
      posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("78")	||
      posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("79") ||
      posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("80") ||
      posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("81") )
	)
{%>
 <td class="L" colspan="3">
       <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        <%=autoritaEsternaE%>
       </select>
      </td>

    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
<%//modifica relativa al tipo istituto%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
<%//fine modifica relativa al tipo istituto%>
       <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30 ></textarea>
      </td>

<%}else{
if(posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null && posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null)
 { 
%>
              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>
<%}else {%>
              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>


       <%}%>
    <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
      </td>
<%}%>

<tr><td>&nbsp;</td></tr>



<%}else{%>
      <td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font> </td>
<%if( lPosizione.getCodPosizioneGiuridica().equals("07") || 
	  lPosizione.getCodPosizioneGiuridica().equals("10") || 
	  lPosizione.getCodPosizioneGiuridica().equals("02") || 
	  lPosizione.getCodPosizioneGiuridica().equals("04") || 
	  lPosizione.getCodPosizioneGiuridica().equals("16") || 
	  lPosizione.getCodPosizioneGiuridica().equals("20") || 
	  lPosizione.getCodPosizioneGiuridica().equals("46") || 
	  lPosizione.getCodPosizioneGiuridica().equals("47") || 
	  lPosizione.getCodPosizioneGiuridica().equals("12") ||
	  lPosizione.getCodPosizioneGiuridica().equals("71") ||
	  // MEV10-s3: aggiunte altre posizioni giuridiche
	  lPosizione.getCodPosizioneGiuridica().equals("72") ||
	  lPosizione.getCodPosizioneGiuridica().equals("70")
	 )
{%>
     <td class="L" colspan="3">
       <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        <%=autoritaEsternaE%>
       </select>
     </td>
    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
     <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=30></textarea>
      </td>

<%}else{
//modifica relativa al tipo istituto
  if(lLuogoDetenzione.getIstitutoDetenzione() == null)
  {%>
              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>
  <%}else {%>

              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>

       <%}%>

      <td class="l">Note</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"  cols=35></textarea>
      </td>
<tr><td>&nbsp;</td></tr>
   <%}%>

    </tr>
<%}%>

<%
	if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
   		<tr>
       		<td class="L">Magistrato di Sorveglianza</td>
        	<td class="L" colspan="3">
         		<input title="Sede Magistrato Sorveglianza" value="" type="text" name="<%= ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>"  maxlength="35" size="35">
         		<a href="Javascript:ListaUDS('LoadInserisciOrdineEsecuzione','<%=ICostantiOrdineEsecuzione.CAMPO_COD_SEDE_UDS %>');">
         			<img src="/images/filefolder.gif" border=0>
         		</a>
        	</td>
    	</tr>
<%
	}
%>
		<tr>
      		<td class="Titolo" colspan=6>Destinatario per Notifica</td>
      	</tr>
<%
		int lIdxAvv = 0;
      	Iterator lItxAvv = avvocati.iterator();
      	while (lItxAvv.hasNext()) {
        	AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
	</table>
	<table width=100%>
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
         	<input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" maxlength="35" size="35">
		</tr>
	</table>
	<table width=100%>
		<tr>
			<td class="l">Autorità Destinazione</td>
          	<td class="L" colspan="3">
				<select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
               		<%=autoritaEsternaN%>
             	</select>
         	</td>
     	</tr>
		<tr>
      		<td class="l">Sede </td><td class="L">
        		<input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
        		if (avvocati.size() > 1) {
%>
          		<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
<%
        		} else {
%>
          		<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
<%
        		}
%>
          			<img src="/images/filefolder.gif" border="0">
        		</a>
      		</td>
        	<td class="l">Note</td>
       		<td class="L">
          		<textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols="35"></textarea>
       		</td>
    	</tr>
    	<tr>
    		<td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  		</tr>
<%
if(lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
   		<tr>
     		<td class="L">Altra Autorità di polizia <font class=ob>(*)</font></td>
     		<td class="L" colspan="3">
       			<select  Title="Altra Autorità di polizia" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
        			<%=autoritaEsternaE%>
       			</select>
     		</td>
    	</tr>
   		<tr>
      		<td class="l">Sede <font class=ob>(*)</font></td>
      		<td class="L">
        		<input title="Sede Altra Autorità di polizia" value="" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>" maxlength="35" size="35">
       			<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
          			<img src="/images/filefolder.gif" border=0>
        		</a>
      		</td>
     		<td class="l">Indirizzo</td>
      		<td class="L">
        		<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_C%>" cols="30"></textarea>
      		</td>
    	</tr>
<%
}
%>
		<br>
  		<tr>
    		<td class="lNoBord" colspan="2">
      			<INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    		</td>
  		</tr>
	</table>
	</form>
	<script language="JavaScript" type="text/javascript">
  	var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  	frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell''Atto è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  	frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

<%
	if ((!lPosizione.getCodPosizioneGiuridica().equals("07")
			&& !lPosizione.getCodPosizioneGiuridica().equals("10")
			&& !lPosizione.getCodPosizioneGiuridica().equals("16")
			&& !lPosizione.getCodPosizioneGiuridica().equals("20")
			&& !lPosizione.getCodPosizioneGiuridica().equals("46")
			&& !lPosizione.getCodPosizioneGiuridica().equals("47"))
			|| (((lFascicoloAssociato.getFlagAltraCausa() != null
				&& lFascicoloAssociato.getFlagAltraCausa().equals("S"))
				&& (penaresidua.getDataFinePresunta()!= null
				&& penaresidua.getDataFine() == null)))) {
		if (((penaresidua.getFlagErgastolo() == null)
				|| (penaresidua.getFlagErgastolo() != null
				&& !penaresidua.getFlagErgastolo().equals("S")
				&& !penaresidua.getFlagErgastolo().equals("D")))) {
			if (dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
%>
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  	frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
			}
	 	}
	}
	if (lPosizione.getCodPosizioneGiuridica().equals("07")
			|| lPosizione.getCodPosizioneGiuridica().equals("10")
			|| lPosizione.getCodPosizioneGiuridica().equals("02")
			|| lPosizione.getCodPosizioneGiuridica().equals("04")
			|| lPosizione.getCodPosizioneGiuridica().equals("16")
			|| lPosizione.getCodPosizioneGiuridica().equals("20")
			|| lPosizione.getCodPosizioneGiuridica().equals("46")
			|| lPosizione.getCodPosizioneGiuridica().equals("47")
			|| lPosizione.getCodPosizioneGiuridica().equals("12")
			|| lPosizione.getCodPosizioneGiuridica().equals("71")
			// MEV10-s3: aggiunte altre posizioni giuridiche
			|| lPosizione.getCodPosizioneGiuridica().equals("72")
			|| lPosizione.getCodPosizioneGiuridica().equals("70")) {
		if (lFascicoloAssociato.getFlagAltraCausa() == null
				|| (lFascicoloAssociato.getFlagAltraCausa() !=null
				&& lFascicoloAssociato.getFlagAltraCausa().equals("N"))) {
%>
  	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
  	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%
		}
	}
	if (lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
		if (posizioneluogoaltra != null && posizioneluogoaltra.getAltraCausa() != null
				&& posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica() != null
				&& posizioneluogoaltra.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
%>
	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","req","Luogo Autorità Destinazione obbligatoria");
	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>","alphabetic");
<%
		}
	}
%>
 	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","req","Luogo Autorità Destinazione obbligatoria");
 	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>","alphabetic");

<%
	if (lPosizione.getCodPosizioneGiuridica().equals("12")) {
%>
  	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","req","Luogo Altra Autorità di polizia obbligatoria");
  	frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>","alphabetic");
<%
	}
	if ((lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
			|| ( lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("07"))
			|| (lPosizione.getCodPosizioneGiuridica() != null && lPosizione.getCodPosizioneGiuridica().equals("10"))) {
%>
   	frmvalidator.addValidation("annoBdmc","numeric");
   	frmvalidator.addValidation("numeroBdmc","numeric");
   	frmvalidator.addValidation("sedeBdmc","alphabetic");
<%
	}
%>
	</script>
	</body>
</html>