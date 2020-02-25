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

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="luogodetenzione"      scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneprecedente"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="nuovapenaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="tipoMisura"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>

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
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='AFFIDAMENTOCUMULO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA_CUMULO%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='DETENZIONE')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='DETENZIONECUMULO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA_CUMULO%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='SEMILIBERTA')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipomisura.value=='SEMILIBERTACUMULO')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
        naturaMA='<%=ICostantiMisuraAlternativa.PROSECUZIONE_PROVVISORIA_CUMULO%>';
      }

      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }

  function Verify()
  {
<%
    if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null)
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
%>
<%
  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
%>
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
      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di trasmissione non valida');
			 return false;
		  }

<%}%>

      var campo = document.LoadInserisciMisuraAlternativa.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;



if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
{
if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }

   if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value == ""
      && document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value == ""
      && document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value == "")
      {
       alert('Data Inizio Misura obbligatoria');
       document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.focus();
			 return false;
      }

    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value;

	  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value;

		  var data_to_verify_re = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;

      if (!ControllaData(data_to_verify_re))
		  {
       alert('Data Inizio Misura non valida');
			 return false;
		  }

}



<%if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
{%>

    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>.value;

	  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>.value;

		  var data_to_verify_altro = document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>.value;

      if (data_to_verify_altro >2 && !ControllaData(data_to_verify_altro))
		  {
       alert('Data Altro Titolo non valida');
			 return false;
		  }


  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il  Magistrato Firmatario è obbligatorio");
           document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }




<%if(tipoMisura.equals("SEMILIBERTA") || tipoMisura.equals("SEMILIBERTACUMULO"))
{%>
 if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
 {
        alert("L'Istituto di Detenzione è obbligatorio");
        return false;
 }
<%
}%>

  if(document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" || document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
  {
    alert("L' UEPE è obbligatorio");
    return false;
  }

if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=="")
      {
        alert("Il Magistrato di Sorveglianza è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.focus();
        return false;
      }


if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=="")
      {
        alert("Il Tribunale di Sorveglianza è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.focus();
        return false;
      }



<%if(avvocati.size() >1)
     {%>
       if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].selectedIndex].value == '-')
			{
          alert("Il campo Autorità per la Notifica  di un Condannato è obbligatorio");
          document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    return false;
		  }
        if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].selectedIndex].value == '-')
           {

                    alert("Il campo Autorità per la  Notifica di un Condannato  è obbligatorio");
                    document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
                    return false;
           }
  <%}else{%>
 if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-')
 {
          alert("Il campo Autorità per la Notifica  di un Condannato  è obbligatorio");
          document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    return false;
  }
<%}
}%>


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

 function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }
function ListaUDS(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }


  </script>
</head>
       <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
     <td class="LBG"><font class="label">Funzione : </font>&nbsp;&nbsp;
<%
   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();

%>
<%if(tipoMisura.equals("DETENZIONE")){%>
    <font class="campo">Prosecuzione Provvisoria Detenzione Domiciliare (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("AFFIDAMENTO")){%>
    <font class="campo">Prosecuzione Provvisoria Affidamento In Prova (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("SEMILIBERTA")) {%>
    <font class="campo">Prosecuzione Provvisoria Semilibertà (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("DETENZIONECUMULO")){%>
    <font class="campo">Prosecuzione Provvisoria Detenzione Domiciliare (CON CUMULO)</font>
<%}else if(tipoMisura.equals("AFFIDAMENTOCUMULO")){%>
    <font class="campo">Prosecuzione Provvisoria Affidamento In Prova (CON CUMULO)</font>
<%}else if(tipoMisura.equals("SEMILIBERTACUMULO")) {%>
    <font class="campo">Prosecuzione Provvisoria Semilibertà (CON CUMULO)</font>
<%}%>
</td>
</tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
<%
   BigDecimal lIdOrdinanzaSius = null;
   if(   misuraalternativa != null
      && misuraalternativa.getEveIdEvento() != null)
   {
     lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
   }
%>
  <INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMAProsecuzione">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">

<%if(tipoMisura.equals("DETENZIONE")){%>
    <input type="HIDDEN" name="tipomisura" value="DETENZIONE">
<%}else if(tipoMisura.equals("AFFIDAMENTO")){%>
    <input type="HIDDEN" name="tipomisura" value="AFFIDAMENTO">
<%}else if(tipoMisura.equals("SEMILIBERTA")){%>
    <input type="HIDDEN" name="tipomisura" value="SEMILIBERTA">
<%}else if(tipoMisura.equals("DETENZIONECUMULO")){%>
    <input type="HIDDEN" name="tipomisura" value="DETENZIONECUMULO">
<%}else if(tipoMisura.equals("AFFIDAMENTOCUMULO")){%>
    <input type="HIDDEN" name="tipomisura" value="AFFIDAMENTOCUMULO">
<%}else if(tipoMisura.equals("SEMILIBERTACUMULO")) {%>
    <input type="HIDDEN" name="tipomisura" value="SEMILIBERTACUMULO">
<%}%>

  <input type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
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

if(misuraalternativa != null && misuraalternativa.getCodTipoUfficioScarcerazione()!= null)
{
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV"))
  {
   scarcerato ="checked";
   if(misuraalternativa.getFlagUfficioInserimento()== null)
   {
    dascarceraredisabilita="disabled";
   }
  }
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC"))
  {
   scarcerare ="checked";
   if(misuraalternativa.getFlagUfficioInserimento()== null)
   {
    scarceratodisabilita="disabled";
   }
  }
  if(misuraalternativa.getCodTipoUfficioScarcerazione().equals("-"))
  {
   scarcerare ="checked";
  }
}



if(misuraalternativa != null && misuraalternativa.getDataScarcerazione()!= null && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV"))
{
   if(misuraalternativa.getFlagUfficioInserimento()== null)
   {
    disabilitaData ="readonly";
   }
}

String checkDetratto = null;
if(lPosizione!= null && lPosizione.getCodPosizioneGiuridica()!= null && lPosizione.getCodPosizioneGiuridica().equals("13")
  && posizioneprecedente != null && posizioneprecedente.getCodPosizioneGiuridica()!= null
  && posizioneprecedente.getCodPosizioneGiuridica().equals("32"))
{
 if( misuraalternativa != null && misuraalternativa.getFlagPeriodoEspiato()!= null &&  misuraalternativa.getFlagPeriodoEspiato().equals("S"))
 {
  checkDetratto = "checked";
 }
}

%>


<table width ="100%">
 <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
       <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%     }
        else
       {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%     }%>
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
%>

<%
    if((!lPosizione.isLibero() ) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <!--td class="l">Data Fine Pena Manuale</td-->
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="">
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
              </td>
<%
          }else
            {%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>

 <%         }
        }
      }
}
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
<%
  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
%>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
<%}%>
</table>
  <table>
    <tr>
      <td class="Titolo" colspan="4"> Dati Decreto Magistrato di Sorveglianza </td>
    </tr>
<%
    if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
    {
%>
      <tr>
        <td class="l"  width=25%>Anno / Numero Sius</td>
<%
      if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
      {
%>
        <td class="l" width=20%><input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4"> /
<%
      }
      else
      {
%>
        <td class="l" width=20%><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
<%
      }

      if(misuraalternativa== null ||  misuraalternativa.getIdMisuraAlternativa()== null)
      {
%>
          <input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6">
        </td>
<%
      }
      else
      {
%>
        <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font></td>
<%
      }
%>
        <td class="l" width=25%> Anno / Numero Decreto </td>
<%
      if( misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
      {
%>
        <td class="l" width=20%> <input value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4"> /
<%
      }
      else
      {
%>
        <td class="l" width=20%><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
<%
      }

     if(misuraalternativa == null ||  misuraalternativa.getIdMisuraAlternativa()== null)
     {
%>
       <input value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6"></td>
<%
     }
     else
     {
%>
       <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font></td>
<%
     }
%>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente </td>
<%
      if(misuraalternativa==null || misuraalternativa.getChiaveUfficioFascicoloSius()==null || misuraalternativa.getIdMisuraAlternativa()==null)
      {
%>
          <td class="l" colspan="3">
            <font class="campo">UFFICIO DI SORVEGLIANZA</font>
            <input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" value="UDS">
          </td>
        </tr>
        <tr>
          <td class="l">Sede Ufficio Emittente<font class=ob>(*)</font></td>
            <td class="l" colspan="3">
            <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text">
            <a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
         </td>
<%
      }
      else
      {
%>
        <td class="l" colspan="3"> <font class="campo"> UFFICIO DI SORVEGLIANZA DI <%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
<%
      }
%>
    </tr>
    <tr>
      <td class="l">Oggetto Decreto </td>
<%
      if( misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa() != null )
      {
%>
        <td class="l" colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
<%
      }
      else
      {
%>
        <td class="L" colspan="3">
          <select Title="Codice Motivo"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
            <%=motivoProvv%>
          </select>
        </td>
<%
      }
%>
    </tr>
    <tr>
      <td class="l">Data Emissione Decreto </td>
<%
      if(misuraalternativa == null  || misuraalternativa.getIdMisuraAlternativa() == null)
      {
%>
        <td class="l" colspan="3">
          <font class="campo">
            <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </font>
        </td>
<%
      }
      else
      {
%>
        <td class="l" colspan="3">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
          </font>
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
          <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">
        </td>
<%
      }
%>
    </tr>
<%
  }
  else
  {
%>
   <tr>
      <td class="l" colspan="3">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciMisuraAlternativa');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Decreto </td>
      <td class="l">
        <input Title="Anno Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">
        <font class="campo">UFFICIO DI SORVEGLIANZA</font>
        <input type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" value="UDS">
      </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente<font class=ob>(*)</font></td>
        <td class="l" colspan="3">
        <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
        <a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
     </td>
    </tr>
    <tr>
      <td class="l">Oggetto Decreto </td>
      <td class="L" colspan="3">
        <select Title="Codice Motivo"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Decreto</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        </font>
      </td>
    </tr>
<%
    }

    if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
    {
%>
      <tr>
        <td class="l">Data decorrenza misura <font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <font class="campo">
            <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
            <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
            <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
          </font>
        </td>
      </tr>
<%
    }
    else if(misuraalternativa != null && misuraalternativa.getDataInizioMisura()!= null)
    {
%>
      <tr>
        <td class="l">Data decorrenza misura</td>
        <td class="l" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataInizioMisura()))%>">
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataInizioMisura()))%>">
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataInizioMisura()))%>">
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
<%

  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
%>
    <table width ="100%">
      <tr>
        <td class="Titolo" colspan="8"> Nuova Data Fine Pena</td>
      </tr>
      <tr>
        <td class="l">Data Decorrenza Misura</td>
        <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
<%
        if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null)
        {
%>
          <td class="l">Data Fine Pena</td>
          <td class="L" colspan=2>
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
<%
        }
        else if( penaresidua.getDataFine() != null)
        {
          if(nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta()))
          {
%>
            <td class="l">Data Fine Pena</td>
            <td class="L" colspan=2>
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>
              </font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }
          else
          {
%>
            <td class="l">Data Fine Pena</td>
            <td class="lRosso" colspan=2>
              <font class="lRosso">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>
              </font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%        }
        }
%>
      </tr>
    </table>
<%
  }
%>
<table width ="100%">
<%
  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
%>
    <tr>
      <td class="Titolo" colspan=6>Estremi Altro Titolo Esecutivo</td>
    </tr>
    <tr>
      <td class="l">Data Sentenza</td>
<%
        if(misuraalternativa != null  && misuraalternativa.getDataAltroTitolo() == null)
        {
%>
          <td class="l">
            <font class="campo">
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </font>
          </td>
<%
        }
        else if(misuraalternativa != null  && misuraalternativa.getDataAltroTitolo() != null)
        {
%>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataAltroTitolo(),"dd-MM-yyyy"))%>
            </font>
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataAltroTitolo()))%>">
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataAltroTitolo()))%>">
            <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataAltroTitolo()))%>">
          </td>
<%
        }
%>
        <td class="l" >Anno / Numero Sentenza</td>
<%
        if(misuraalternativa!= null &&  misuraalternativa.getAnnoAltroTitolo()== null)
        {
%>
          <td class="l"><input value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoAltroTitolo())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO%>" type="text" size="4" maxlength="4"> /
<%
        }
        else if(misuraalternativa!= null &&  misuraalternativa.getAnnoAltroTitolo()!= null)
        {
%>
          <td class="l" ><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getAnnoAltroTitolo())%> /</font>
<%
        }

        if(misuraalternativa!= null &&  misuraalternativa.getNumAltroTitolo()== null)
        {
%>
            <input value="" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ALTRO_TITOLO%>" type="text" size="6" maxlength="6">
          </td>
<%
        }
        else if(misuraalternativa!= null &&  misuraalternativa.getNumAltroTitolo()!= null)
        {
%>
          <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAltroTitolo())%></font></td>
<%
        }
%>
    </tr>
    <tr>
      <td class="l">Autorità Emittente </td>
<%
      if (misuraalternativa != null && misuraalternativa.getNumAltroTitolo()== null)
      {
%>
          <td class="l" colspan="3">
            <select  Title="Autorità Emittente"  class="l" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_AUTORITA_ALTRO_TITOLO%>">
              <%=autoritaEmi%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Luogo Emittente</td>
          <td class="l" colspan="3">
            <input Title="Luogo Emittente" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO%>" size=35 type="text">
            <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
<%
      }
      else if (misuraalternativa != null && misuraalternativa.getCodAutoritaAltroTitolo()!= null)
      {
%>
        <td class="l" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescAutoritaAltroTitolo())%> DI <%=StringUtils.toStringJSP(misuraalternativa.getDescLuogoAltroTitolo())%></font></td>
<%
      }
%>
    </tr>
    <input type="hidden"  Title="presenzanuovopena" name="presenzanuovopenaricalcolata" value="S">
    <tr>
      <td class="Titolo" colspan=6> Magistrato Firmatario </td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario
      <td class="L" colspan="3">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
    </tr>
    <tr>
      <td class="Titolo" colspan=6>Destinatari</td>
   </tr>
</table>
<table width ="100%">
<%
if(tipoMisura.equals("SEMILIBERTA") || tipoMisura.equals("SEMILIBERTACUMULO"))
{%>
<tr>
     <td class="l" width ="30%">Istituto di Detenzione <font class=ob>(*)</font></td>
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td rowspan=2 class="l">Note</td>
<td rowspan=2 class="L">
	<TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_IST%>"  cols=20 rows=5 ></textarea>
</td--%>
</tr>
<%
}
%>
<tr>
      <td class="l"width=30%>UEPE <font class=ob>(*)</font></td>
      <td class="l">
        <input readonly Title="UEPE Competente" name="Indirizzo" value="" size=60 >
        <input type="hidden" Title="UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
  <%if(!tipoMisura.equals("SEMILIBERTA") && !tipoMisura.equals("SEMILIBERTACUMULO"))
    {%>
      <input type="hidden" Title="UEPE" name="cssaE" value="S">
  <%}%>
          <a href="Javascript:ListaCSSA('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
    </tr>
 <tr>
      <td class="l" width=30%>Magistrato di Sorveglianza <font class=ob>(*)</font></td>
      <td class="L">
       <input title="ufficio" value="" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" maxlength="35" size="25">
       <a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
              <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    <tr>
      <td class="l" width ="30%">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
      <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
      <td class="l" colspan="3">
        <input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
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
         <table>
          <tr><td class="l">Autorità Destinazione <font class=ob>(*)</font></td >
          <td class="L">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
        <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=20 rows=5 ></textarea>
       </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
<%
  }
%>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
<%
  if (nuovapenaresidua != null && nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null)
  {
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
%>
<%
  if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
  {
%>
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2050");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2050");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2050");


<%
  }

  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
%>
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");
<%}

  if( misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
  {
%>
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>","alphabetic");
<%
  }
%>

<%
  if(nuovapenaresidua != null && nuovapenaresidua.getIdPenaResidua()!= null)
  {
    if (misuraalternativa != null && misuraalternativa.getAnnoAltroTitolo()== null)
    {
%>
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO%>","numeric","Il Campo Anno Altro titolo è numerico");
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_ALTRO_TITOLO%>","lt=2050");
<%
    }

    if (misuraalternativa != null && misuraalternativa.getNumAltroTitolo()== null)
    {
%>
      frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUM_ALTRO_TITOLO%>","numeric","Il Campo Numero Altro titolo è numerico");
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_COD_LUOGO_ALTRO_TITOLO %>","alphabetic","Il Campo Luogo Altro titolo non è numerico");

      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ALTRO_TITOLO%>","numeric");

      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ALTRO_TITOLO%>","numeric");

      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ALTRO_TITOLO%>","lt=2050");
<%
    }
  }
%>
</script>
</body>
</html>