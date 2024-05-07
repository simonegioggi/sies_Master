<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ListIterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager" %>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenutoEsecuzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="collContenuto" scope="request" class="java.util.Vector"/>
<jsp:useBean id="oggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="posGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idLuogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="residenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="domicilio" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloEsecuzione" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="idFascicoloOrigine" scope="request" class="java.lang.String"/>

<%
Date dataFinePena = (Date)request.getAttribute("dataFinePena");
String lDisable = "";  // 25/03/2007
%>
<html>
<FORM name="FormTestS22" >
<%
// Prelevati i Contenuti UDS. Occorre caricare in oggetti HTML Hidden i LOW_VALUE
// e gli HIGH_VALUE della collection per gestirli nella funzione JavaScript TestS22.
ListIterator itx = collContenuto.listIterator();
while (itx.hasNext()) {
    DecodificheModel lContenuti = (DecodificheModel)itx.next();
%>
	<input type="HIDDEN" name='LowValue' value='<%=lContenuti.getCode()%>'>
    <input type="HIDDEN" name='HighValue' value='<%=lContenuti.getFiltro()%>'>
<%
}
%>
</FORM>

<head>
	<title>[S.I.E.S.] - Gestione Procedimenti SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname) {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	}

	// Chiamata funzione lista Oggetti.
	function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet) {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
            aLink += "&formname="+a_formname;
            aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
	}

	var resultS22;
	function TestS22(Code) {
        // Ripulisce i codici Oggetto, il TextBox con la descrizione degli oggetti e i campi S22.
<%--    STUB 21/04/2004 document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value=''; --%>
<%--    STUB 21/04/2004 document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>.value=''; --%>
<%--    document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.value=''; --%>
<%--    document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.value=''; --%>

        // Se Code = LowValue[i]!='U004' && HighValue[i]='S22' Occorre far visualizzare i Campi nascosti.
        if (typeof (Code) == "undefined")
			alert('selezionare il contenuto');
        else {
          	// Il tag div S22 viene nascosto per default e visualizzato alla condizione HighValue = S22.
          	S22_a.style.visibility='hidden';
          	S22_aa.style.visibility='hidden'; //19/07/2007
          	S22_ems.style.visibility='hidden'; //29/047/2011
          	S22_ab.style.visibility='hidden';
          	S22_b.style.visibility='hidden';
          	S22_EPS.style.visibility='hidden'; //19/07/2007
          	resultS22 = '';
          	for (i = 0; i < document.FormTestS22.LowValue.length; i++) {
            	if (Code == document.FormTestS22.LowValue[i].value) {
          		
              		// Imposta il Tipo Registro (da utilizzare in fase di inserimento).
             		document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_REGISTRO%>.value=document.FormTestS22.HighValue[i].value;
              		// STUB 30/03/2004 if(document.FormTestS22.LowValue[i].value != 'U004'   &&
              		if (document.FormTestS22.HighValue[i].value =='S22') {
                		if (document.FormTestS22.LowValue[i].value == 'U004') {
							S22_ab.style.visibility='visible';
							S22_ab.style.top='-20px';
							S22_a.style.visibility='hidden';
							S22_aa.style.visibility='hidden'; // 19/07/2007
							S22_EPS.style.visibility='hidden'; 
							S22_ems.style.visibility='hidden';
							resultS22='U004';
                		} else {
							S22_a.style.visibility='visible';
							S22_a.style.top='+4px';
							S22_aa.style.visibility='hidden'; // 19/07/2007
							S22_EPS.style.visibility='hidden'; 
							S22_ems.style.visibility='hidden';
							S22_ab.style.visibility='hidden';
							resultS22='S22';
                		}
                		S22_b.style.visibility='visible';
                		break;
              		}
              		// 18/07/2007 SANZIONI SOSTITUTIVE
              		if (document.FormTestS22.HighValue[i].value =='S12') {
                		if (document.FormTestS22.LowValue[i].value == 'U019') {
							S22_ab.style.visibility='visible';
							S22_ab.style.top='-20px';
							S22_a.style.visibility='hidden';
							S22_aa.style.visibility='hidden';
							S22_ems.style.visibility='hidden';
							S22_EPS.style.visibility='hidden'; 
							resultS22='U019';
               			} else {
							S22_a.style.visibility='hidden';
							S22_ab.style.visibility='hidden';
							S22_aa.style.visibility='visible';
							S22_ems.style.visibility='hidden';
							S22_EPS.style.visibility='hidden';
							resultS22='S12';
                		}
                		S22_b.style.visibility='visible';
                		break;
              		}
              		
              	  // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE
                    if (document.FormTestS22.HighValue[i].value =='S30') {
                      if (document.FormTestS22.LowValue[i].value == 'U126') {
					                S22_ab.style.visibility='visible';
					                S22_ab.style.top='-20px';
					                S22_a.style.visibility='hidden';
					                S22_aa.style.visibility='hidden';
					                S22_ems.style.visibility='hidden';
					                S22_EPS.style.visibility='hidden';
					                resultS22='U126';
					            } else {
					                S22_a.style.visibility='hidden';
					                S22_ab.style.visibility='hidden';
					                S22_aa.style.visibility='hidden';
					                S22_ems.style.visibility='hidden';
					                S22_EPS.style.visibility='visible';
					                resultS22='S30';
                      }
                      S22_b.style.visibility='visible';
                      break;
                    }             		
                 // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE - FINE
              		
              		// 29/04/2011 MISURE SICUREZZA
              		if (document.FormTestS22.HighValue[i].value =='S09') {
                		if (document.FormTestS22.LowValue[i].value == 'U024') {
							S22_ab.style.visibility='visible';
							S22_ab.style.top='-20px';
							S22_a.style.visibility='hidden';
							S22_aa.style.visibility='hidden';
							S22_ems.style.visibility='hidden';
							S22_EPS.style.visibility='hidden';
							resultS22='U024';
                		} else {
							S22_a.style.visibility='hidden';
							S22_ab.style.visibility='hidden';
							S22_aa.style.visibility='hidden';
							S22_ems.style.visibility='visible';
							S22_EPS.style.visibility='hidden';
							resultS22='S09';
                		}
                		S22_b.style.visibility='visible';
                		break;
              		}
            	}
          	}
		}
	}

	function Verify() {
        if (document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
        if (document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;

        if (document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value;
        if (document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value;

        // Controllo obbligatorietà campi S22.
        var progrS22=document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.value;
        var annoS22=document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.value;

        if (resultS22 == 'S22' && annoS22 == "") {
          	alert("Campo Anno del Procedimento di Esecuzione della Misura Alternativa Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U004' && annoS22 == "") {
          	alert("Campo Anno dell'ordinanza Obbligatorio");
         	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }

        // 19/07/2007 Controlli Sanzioni Sostitutive
        if (resultS22 == 'S12' && annoS22 == "") {
          	alert("Campo Anno del Procedimento di Esecuzione della Sanzione Sostitutiva Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U019' && annoS22 == "") {
          	alert("Campo Anno dell'Ordinanza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }
        
        // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE
        if (resultS22 == 'S30' && annoS22 == "") {
            alert("Campo Anno del Procedimento di Esecuzione della Pena Sostitutiva Obbligatorio");
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
            return false;
        }
        if (resultS22 == 'U126' && annoS22 == "") {
            alert("Campo Anno dell'Ordinanza Obbligatorio");
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
            return false;
        }
        // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE - FINE
        
        // 29/04/2011 Controlli Misure Sicurezza
        if (resultS22 == 'S09' && annoS22 == "") {
          	alert("Campo Anno del Procedimento di Esecuzione della Misura Sicurezza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U024' && annoS22 == "") {
          	alert("Campo Anno dell'Ordinanza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>.focus();
          	return false;
        }

        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>","numeric");

        if (resultS22 == 'S22' && progrS22 == "") {
          	alert("Campo Progressivo del Procedimento di Esecuzione della Misura Alternativa Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U004' && progrS22 == "") {
          	alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }

        // 19/07/2007 Controlli Sanzioni Sostitutive
        if (resultS22 == 'S12' && progrS22 == "") {
          	alert("Campo Progressivo del Procedimento di Esecuzione della Sanzione Sostitutiva Obbligatorio");
         	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U019' && progrS22 == "") {
          	alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }

        // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE
        if (resultS22 == 'S30' && progrS22 == "") {
            alert("Campo Progressivo del Procedimento di Esecuzione della Pena Sostitutiva Obbligatorio");
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
            return false;
        }
        if (resultS22 == 'U126' && progrS22 == "") {
            alert("Campo Progressivo dell'Ordinanza Obbligatorio");
            document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
            return false;
        }     
        // MEV_2023-35 - 05/2024 PENE SOSTITUTIVE - FINE
        
        // 24/04/2011 Controlli Misure Sicurezza
        if (resultS22 == 'S09' && progrS22 == "") {
          	alert("Campo Progressivo del Procedimento di Esecuzione della Misura Sicurezza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }
        if (resultS22 == 'U024' && progrS22 == "") {
          	alert("Campo Progressivo dell'Ordinanza Obbligatorio");
          	document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>.focus();
          	return false;
        }

        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>","numeric");

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;
        var modalita='<%=modalita%>';
        if (tipoAtto == '-' && modalita != 'M') {
          	alert("Il Campo Tipo Atto è obbligatorio");
          	return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if (data_atto != '//') {
        	if (!ControllaData(data_atto)) {
	            alert('Data atto non valida');
	            return false;
          	}
       		// Controllo della data atto <= data di sistema
          	if (!CompareDate(data_atto, data_sistema)) {
	            alert('Data atto > della data odierna');
	            return false;
          	}
		}

        // Controllo della data fine pena solo se valorizzata.
        var data_finepena=document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>.value;
        if (data_finepena != '//') {
        	if (!ControllaData(data_finepena)) {
	            alert('Data fine pena non valida');
	            return false;
          	}
          	// Controllo della data fine pena => data di sistema
          	// STUB 17/01/2005 aggiunta richiesta di proseguimento.
          	if (!CompareDate( data_sistema, data_finepena)) {
            	if (!confirm("Data fine pena < Data odierna! Si vuole continuare?"))
            		return false;
          	}
		}

        // Controllo obbligatorietà contenuto.
        var contenuto=document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloUDS.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;

        if (contenuto =="-") {
          	alert("Il Campo Contenuto è obbligatorio");
          	return false;
        }

        // Controllo della data arrivo solo se valorizzata.
        var data_arrivo=document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
        if (data_arrivo != '//') {
          	// Controllo di validità della data arrivo.
          	if (!ControllaData(data_arrivo)) {
	            alert('Data di arrivo in cancelleria non valida');
	            return false;
          	}
          	// Controllo della data arrivo <= data di sistema
          	if (!CompareDate(data_arrivo, data_sistema)) {
	            alert('Data di arrivo > della data odierna');
	            return false;
          	}
          	// Controllo della data atto <= data arrivo
          	if (data_atto != '//') {
            	if (!CompareDate(data_atto, data_arrivo)) {
					alert('Data atto > data arrivo in cancelleria');
					return false;
            	}
          	}
		}
      	return true;
	}
	</script>
</head>

<body class="corpo" onload="Javascript:TestS22(document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value );">
	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
    			</a>
    		</td>
      		<td class=LBG>
      			<font class="label">Funzione:</font>&nbsp;
<%
Date lDataIscrizione = new Date();
String lAction = new String();
FascicoloGPModel lFascicolo = new FascicoloGPModel();
if (modalita.equals("IU") || modalita.equals("IE")) {
	lAction = "siap.sius.fascicolo.action.ActInserisciFascicoloUDS";
%>
				<font class="campo">Iscrizione Procedimento</font>
<%
}
// STUB 02/04/2004 La modalità IM corrisponde all'iscrizione delle EMA da soggetto e proviene da ActLoadInsFascicoloDaSoggettoUDS.
else if (modalita.equals("IS") || modalita.equals("IM")) {
	lAction = "siap.sius.fascicolo.action.ActInsFascicoloDaSoggettoUDS";
%>
       			<font class="campo">Iscrizione Procedimento da Soggetto</font>
<%
} else if (modalita.equals("M")) {
	lFascicolo = (FascicoloGPModel) session.getAttribute("fascicoloSiusGP");
	lAction = "siap.sius.fascicolo.action.ActModificaFascicoloUDS";
	if (lFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") == 0
			|| lFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") == 0
			|| lFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") == 0) {
		lDisable = "DISABLED";
        lAction = "siap.sius.generaleprocedimento.action.ActModificaNoteProcedimento";
%>
          		<font class="campo">Modifica Note Procedimento</font>
<%
	} else {
		lDisable = "";
		lAction = "siap.sius.fascicolo.action.ActModificaFascicoloUDS";
%>
          		<font class="campo">Modifica Procedimento</font>
<%
	}
}
%>
			</td>
  			<!-- BOTTONE DI RITORNO -->
    		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    	</tr>
  	</table>

<%
if (modalita.equals("IU") || modalita.equals("IE")) {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
}
if (modalita.equals("IS") || modalita.equals("IM")) {
%>
    <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
<%
}
if (modalita.equals("M")) {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/SintesiProcedimentoSius.jsp"/>
<%
}
%>

	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicoloUDS">
  	<table cellspacing=0 cellpadding=0 width=95%>
    	<tr>
      		<td class="L">
        		<font class="label">Fine pena</font>
<%
if (modalita.equals("M")) {
%>
          		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataFinePena(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly <%=lDisable%>>
          		/
          		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataFinePena(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly <%=lDisable%>>
          		/
          		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataFinePena(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly <%=lDisable%>>
<%
}
// La dataFinePena può essere valorizzata solo in alcuni casi con modalità = "IF"
else if (dataFinePena != null) {
%>
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly>
<%
} else if (modalita.equals("IM")) {
%>
        		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(fascicoloEsecuzione.getGeneraleProcedimentoModel().getDataFinePena(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2">
        		/
        		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(fascicoloEsecuzione.getGeneraleProcedimentoModel().getDataFinePena(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2">
        		/
        		<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(fascicoloEsecuzione.getGeneraleProcedimentoModel().getDataFinePena(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4">
<%
}
else if (modalita.equals("IS") || modalita.equals("IM")) {
%>
          		<input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          		/
          		<input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          		/
          		<input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%
} else {
%>
				<input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly>
				/
				<input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly>
				/
				<input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly>
<%
}
%>
        		<font class="l">&nbsp;&nbsp;Pos. Giuridica</font>
<%
if (modalita.equals("M")) {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" disabled>
           			<%= posizioneGiuridica %>
          		</select>
          		<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=lFascicolo.getGeneraleProcedimentoModel().getCodPosGiuridica()%>">
<%
} else if (modalita.equals("IS") || modalita.equals("IE")) {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>">
					<%= posizioneGiuridica %>
				</select>
				<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=lFascicolo.getGeneraleProcedimentoModel().getCodPosGiuridica()%>">
<%
}
// Per i fascicoli EMA/ESS figli preimposto alla posizione giuridica SIUS del padre
else if (modalita.equals("IM")) {
    		
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>">
					<%= posizioneGiuridica %>
				</select>
				<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodPosGiuridica()%>">
                    
<%
}
// La Posizione Giuridica è prevalorizzata solo se modalità = "IF"
else if (!posGiuridica.equals("")) {
%>
				<select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  disabled>
					<%= posizioneGiuridica %>
				</select>
				<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=posGiuridica%>"  >
<%
} else {
%>
				<select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  disabled>
					<%= posizioneGiuridica %>
				</select>
				<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="-">
<%
}
%>
      		</td>
    	</tr>
<%
// STUB 09/01/2004 Gestione del luogo detenzione.
if (!luogoDetenzione.equals("")) {
%>
      	<tr>
        	<td class="L">
				<font class="label">Detenuto in &nbsp;&nbsp;&nbsp;</font>
				<font class="campo"><%=luogoDetenzione%> &nbsp;&nbsp;&nbsp;&nbsp;</font>
				<input type=checkbox name="<%=ICostantiFascicoloSius.CAMPO_VALIDA_LUOGO_DET%>" value=1 title="Valida il Luogo Detenzione" <%=lDisable%>>
        	</td>
      	</tr>
<%
}
%>
	</table>
  	<br>
  	<table cellspacing="2" cellpadding="2">
  		<tr>
    <td class="l">Tipo Atto <font class=ob>(*)</font></td>
    		<td class="L">
      <select title="tipoAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>" <%=lDisable%>>
        			<%=tipoAtto%>
      			</select>
    		</td>
  		</tr>
  		<tr>
			<td class="l">Data atto</td>
			<td class="L">
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lDisable%>>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lDisable%>>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" <%=lDisable%>>
    		</td>
  		</tr>
  		<tr>
    		<td class="l">Mittente</td>
    		<td class="L">
      <select title="mittenteAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MITTENTE_ATTO%>" <%=lDisable%> >
        			<%=mittenteAtto%>
      			</select>
      			&nbsp;&nbsp;
<%
if (modalita.equals("M")) {
%>
        		<input Title="descrMittente" value="<%=lFascicolo.getGeneraleProcedimentoModel().getDescrMittente()%>" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50" <%=lDisable%>>
<%
} else {
%>
        		<input Title="descrMittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50">
<%
}
%>
    		</td>
  		</tr>
  		<tr>
    		<td class="l">Sede Mittente</td>
    		<td class="l">
      			<input Title="Sede Mittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE%>"
      				value="<%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrSedeMittente())%>" type="text" maxlength="35" size="35" <%=lDisable%>>
<%
if (lDisable.compareTo("DISABLED") != 0) {
%>
	      		<a href="Javascript:ListaComuni('LoadInserisciFascicoloUDS','<%= ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE %>');">
	      			<img src="/images/filefolder.gif" border="0">
	      		</a>
<%
}
%>
   			</td>
  		</tr>
  		<tr>
			<td class="l">Contenuto <font class=ob>(*)</font></td>
			<td class="L">
      			<%    if( modalita.equals("IE") || modalita.equals("IM"))
      {%>
      	<select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
        <%= contenutoEsecuzione %>
        </select>
    <%}else
      {%>
        <select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" onChange="Javascript:TestS22(document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value );" <%=lDisable%> >
        <%= contenuto %>
        </select>
    <%}%>
    		</td>
  		</tr>
  		<tr>
			<td class="l">Oggetto</td>
			<td class="l">
      			<Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly <%=lDisable%>>
<%
// Nel caso di modifica devo ricaricare le variabili per la gestione degli oggetti(Tenore).
String lCodOggetto = "";
String lCodDettagli = ""; //STUB 21/04/2004
if (modalita.equals("M")) {
	String strDescOggetto = new String();
  	if (lFascicolo.getTenori().length > 0) {
	    strDescOggetto = lFascicolo.getTenori()[0].getDescrOggettoTenore() + "\n";
	    lCodOggetto = lFascicolo.getTenori()[0].getCodOggettoTenore() + "|";
	    int lSize = lFascicolo.getTenori().length;
	    for (int x = 1; x < lSize; x++) {
      		lCodOggetto += lFascicolo.getTenori()[x].getCodOggettoTenore()+"|";
      		strDescOggetto += lFascicolo.getTenori()[x].getDescrOggettoTenore()+"\n";
      		// STUB 21/04/2004 Aggiunti i Codici dettaglio.
      		if (lFascicolo.getTenori()[x].getCodDettaglioOggetto() != null
      				&& lFascicolo.getTenori()[x].getCodDettaglioOggetto().length() > 1) {
        		lCodDettagli += lFascicolo.getTenori()[x].getCodOggettoTenore() + lFascicolo.getTenori()[x].getCodDettaglioOggetto() + "|";
      		}
    	}
  	}
  	if (!(strDescOggetto.indexOf("\n") > 0)) {
  		if (strDescOggetto.length() > 0) {
%>
				<%=strDescOggetto%>
<%
		} else {
%>
				-&nbsp;
<%
		}
	} else {
		while (strDescOggetto.indexOf("\n") > 0) {
%>
				<%=strDescOggetto.substring(0, strDescOggetto.indexOf("\n") + 1)%>
<%
			strDescOggetto = strDescOggetto.substring(strDescOggetto.indexOf("\n") + 1);
		}
%>
				<%=strDescOggetto%>
<%
	}
} else {
%>
      			<%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento())%>
<%
}
%>
      			</Textarea>
<%
if (lDisable.compareTo("DISABLED") != 0) {
%>
	      		<a href="Javascript:ListaOggetti('LoadInserisciFascicoloUDS',document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value);">
  	    			<img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border="0">
  	    		</a>
    	  		&nbsp;
      			<a href="Javascript:ListaOggetti('LoadInserisciFascicoloUDS','-', '<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloUDS.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value);">
      				<img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border="0">
      			</a>
<%
}
%>
    		</td>
  		</tr>
 		<%-- Tag div per gestione registro S22 La variabile VisS22 viene impostata a 'hidden'/'visible'--%>
 		<tr>
    		<td width=22%
<%
if (modalita.equals("IE") || modalita.equals("IM")) {
%>
				class="lVerdeNB"
<%
} else {
%>
				class="label"
<%
}
%>
    		>
	      		<div id=S22_a style="visibility:hidden; position:relative;">
	        		Anno/Progressivo del Procedimento di Esecuzione della Misura Alternativa
	        		<font class=ob>(*)</font>
	      		</div>
	      		<div id=S22_aa style="visibility:hidden; position:absolute; top:+256px;" >
	        		Anno/Progressivo del Procedimento di Esecuzione della Sanzione Sostitutiva
	        		<font class=ob>(*)</font>
	      		</div>
	      		<%-- MEV_2023-35 --%>
	      		<div id=S22_EPS style="visibility:hidden; position:absolute; top:+256px;" >
              Anno/Progressivo del Procedimento di Esecuzione della Pena Sostitutiva
              <font class=ob>(*)</font>
            </div>
            <%-- MEV_2023-35 - FINE --%>
	      		<div id=S22_ems style="visibility:hidden; position:absolute; top:+256px;" >
	        		Anno/Progressivo del Procedimento di Esecuzione della Misura Sicurezza
	        		<font class=ob>(*)</font>
	      		</div>
	      		<div id=S22_ab style="visibility:hidden; position:relative; " >
			        Anno/Numero Ordinanza
			        <font class=ob>(*)</font>
	      		</div>
	   		</td>
	    	<td class="label">
	      		<div id=S22_b style="visibility='hidden';">
	        		<input Title="Anno" type="text" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_S22%>" maxlength="4" size="4"
	        		onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"
<%
if (modalita.equals("IE") || modalita.equals("IM")) {
%>
						value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveAnno()%>" READONLY
<%
}
%>
	        		>/
	        		<input Title="Numero" type="text" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_S22%>" maxlength="6" size="6"
	        		onkeypress="return TicTabNumField(this,event)"
<%
if (modalita.equals("IE") || modalita.equals("IM")) {
%>
						value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveProgr() %>" READONLY
<%
}
%>
        			>
      			</div>
    		</td>
  		</tr>
  		<tr>
    		<td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
    		<td class="L">
<%
if (modalita.equals("M")) {
%>
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lDisable%>>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lDisable%>>
				/
				<input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataArrivoCancelleria(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" <%=lDisable%>>
<%
} else {
%>
				<input  type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				/
				<input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				/
				<input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%
}
%>
    		</td>
  		</tr>
<%
if (modalita.equals("M")) {
%>
    	<tr>
			<td class="l">Magistrato </td>
			<td class="L">
        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" DISABLED>
					<%=magistrato%>
		    	</select>
		    	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" value="<%=lFascicolo.getGeneraleProcedimentoModel().getCodAutoritaDelegata()%>">
		    </td>
		</tr>
<%
} else if (modalita.equals("IM")) {
%>
	    <tr>
	      	<td class="l">Magistrato</td>
	      	<td class="L">
	        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>">
	          		<%=magistrato%>
	        	</select>
	        	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" value="<%=fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodAutoritaDelegata()%>">
	        </td>
	    </tr>
	<%} else { 
 %>
    	<tr>
      		<td class="l">Magistrato</td>
      		<td class="L">
        		<select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" >
          			<%=magistrato%>
        		</select>
        	</td>
   		</tr>
<%
}
%>
  		<tr>
	    	<td class="l">Note</td>
	    	<td class="l">
	      		<Textarea Title="Note" name="<%=ICostantiFascicoloSius.CAMPO_NOTE%>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getAnnotazione())%></textarea>
	    	</td>
		</tr>
  		<tr>
    		<td>
      			<input class="bottone" type="submit" value="Conferma">
    		</td>
  		</tr>
	</table>
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_SEDE_MITTENTE%>" >
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=lCodOggetto%>" >
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=lCodDettagli%>" >
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_REGISTRO%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.LUOGO_DETENZIONE%>" value="<%=luogoDetenzione%>" >
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
	<input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_FASCICOLO_SIUS_ORIGINE%>" value="<%=idFascicoloOrigine%>">
<%
if (modalita.equals("IM")) {
%>
  	<input type="HIDDEN" name="CampoIdFascicoloPadreEsecuzioneHidden" value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getIdFascicoloSius()%>">
<%
}
%>
	</form>

  	<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicoloUDS");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");
  	</script>

</body>
</html>