<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.motivoevento.action.ICostantiMotivoEvento"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>


<jsp:useBean id="penaComplessivaSanzioneSostitutiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="tipoIstituto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="StrdataFinePenaA"    scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="istanza"            scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="decreto"       scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="UfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="motivorevoca"   scope="request" class="java.lang.String"/>
<jsp:useBean id="motivorevocapm"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffDestEntSor"        scope="request" class="java.lang.String"/>


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

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Revoca Decreto di Sospensione </title>
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

		  function Verify()
		  {
		  	//Se il "Foglio Complementare" è selezionato, il "Casellario Giudiziale" è obbligatorio
		  	if( document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>.checked == true )
		  	{
       		if (   document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
       		    || document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
       		   )
          {
            alert("Il campo Casellario Giudiziale è obbligatorio!");

            return false;
          }		  	
		  	} 

	    var selectindex = document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>.selectedIndex;

    if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
    if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
      document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

    var data_to_verify_provv = document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (!ControllaData(data_to_verify_provv) )
		{
      alert('Data di Emissione Provvedimento non valida');
	   	return false;
		}

     if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
         document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
     if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
         document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

     var data_to_verify= document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

     if (!ControllaData(data_to_verify_provv) )
      {
        alert('Data  Trasmissione Provvedimento non valida');
        return false;
      }

    if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>[selectindex].value=='0002')
    {
     if(document.LoadInserisciOrdineEsecuzione.flagmisura.value=="N")
     {
       if (document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
         document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
       if (document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
         document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

       var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
       if (!ControllaData(data_to_verify) )
       {
        alert('Data di emissione Ordinanza non valida');
        return false;
       }


       if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value == '-' )
 	   {
 		  alert("Ufficio Emittente è obbligatorio");
 		 
 		  return false;
 	   }
       
       if(document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
       {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciOrdineEsecuzione.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
       }
     }
    }

      if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }
        if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

    var dataIrrev ='<%=DateUtils.getDateToString(lFascicoloAssociato.getDataIrrevocabilita(),"dd/MM/yyyy")%>';
    if (ControllaData(dataIrrev) )
    {
      if(CompareDate(data_to_verify_provv,dataIrrev))
      {
        alert("Data Provvedimento inferiore alla data di Irrevocabilità");
        return false;
      }
    }


    if(!document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled)
    {
     if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
       {
          alert("Il campo Istituto Detenzione è obbligatorio!");
          return false;
       }
     }
    if(!document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled)
    {
      if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == '-')
        {
          alert("Il campo Autorità di polizia competente per territorio è obbligatorio!");
          document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
	        return false;
       }
    }


    if(!document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled)
    {
      if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value=="")
      {
        alert("La Sede Autorità Destinazione è obbligatoria!");
        return false;
      }
    }

}


   function radio()
    {

       var pos=document.LoadInserisciOrdineEsecuzione.CodPosizioneGiuridica.value;
       var altraca =document.LoadInserisciOrdineEsecuzione.altracausa.value;
	   var codTipoPosGiuridica = document.LoadInserisciOrdineEsecuzione.codTipoPosGiuridica.value;
       
       var nodeor =document.getElementById('ordinanza');
       var nodenoor =document.getElementById('noordinanza');
       var noderevpm =document.getElementById('revocapm');
       var nodeist =document.getElementById('istituto');
       var nodeautori =document.getElementById('autorita');
       var nodetrib =document.getElementById('tribunale');

       var nodemagistrato =document.getElementById('magistrato');
       var nodedifensore =document.getElementById('difensore');
       var selectindex = document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>.selectedIndex;

       //Reiezione Istanza
       if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>[selectindex].value=='0002')
        {
           nodeor.style.visibility='visible';
           noderevpm.style.visibility='hidden';
          if((pos == '49') || (altraca == 'S' && (codTipoPosGiuridica != '78' && codTipoPosGiuridica != '79' && codTipoPosGiuridica != '80' && codTipoPosGiuridica != '81')))
          {
           nodeist.style.visibility='visible';
           nodeautori.style.visibility='hidden';
           nodetrib.style.visibility='hidden';

            nodemagistrato.style.top='-60px';
            nodeist.style.top='-60px';
            nodedifensore.style.top='-190px';

           document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled=true;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled=true;
           document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.disabled=true;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiNotifica.CAMPO_SEDE_TDS %>.disabled=true;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
          }
          else
          {
           nodeist.style.visibility='hidden';
           nodeautori.style.visibility='visible';
           nodetrib.style.visibility='hidden';

           nodemagistrato.style.top='-70px';
           nodeautori.style.top='-125px';
           nodedifensore.style.top='-160px';

           document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled=false;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled=false;
           document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.disabled=false;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiNotifica.CAMPO_SEDE_TDS %>.disabled=true;
           document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;

          }
        }
        else if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>[selectindex].value=='0003')
        {
		//Omessa Istanza
          nodeor.style.visibility='hidden';
          noderevpm.style.visibility='hidden';

          if(altraca == 'S' && (codTipoPosGiuridica != '78' && codTipoPosGiuridica != '79' && codTipoPosGiuridica != '80' && codTipoPosGiuridica != '81'))
          {
            nodeist.style.visibility='visible';
            nodeautori.style.visibility='hidden';
            nodetrib.style.visibility='hidden';

            nodemagistrato.style.top='-300px';
            nodeist.style.top='-300px';
            nodedifensore.style.top='-420px';

            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled=true;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled=true;
            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.disabled=true;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiNotifica.CAMPO_SEDE_TDS %>.disabled=true;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;
          }
          else
          {
            nodeist.style.visibility='hidden';
            nodeautori.style.visibility='visible';
            nodetrib.style.visibility='hidden';

            nodemagistrato.style.top='-300px';
            nodeautori.style.top='-350px';
            nodedifensore.style.top='-380px';


            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiNotifica.CAMPO_SEDE_TDS %>.disabled=true;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;
          }

        }
        else if(document.LoadInserisciOrdineEsecuzione.<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>[selectindex].value=='0001')
        {
		//Revoca PM
            noderevpm.style.visibility='visible';
            nodeor.style.visibility='hidden';

            nodeist.style.visibility='hidden';
            nodeautori.style.visibility='visible';
            nodetrib.style.visibility='visible';

            nodemagistrato.style.top='-230px';
            nodeautori.style.top='-285px';
            nodetrib.style.top='-285px';
            nodedifensore.style.top='-285px';

            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiNotifica.CAMPO_SEDE_TDS %>.disabled=false;
            document.LoadInserisciOrdineEsecuzione.<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;

            noderevpm.style.top='-225px';
        }
    }

//FUNZIONI
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaAvvocati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function ListaComuniTds(formname,fieldname)
    {
      var codTipoSede = document.LoadInserisciOrdineEsecuzione.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value
      if(codTipoSede=="" || codTipoSede=='-')
      {
    	alert("Ufficio Emittente è obbligatorio");       
      } else {
          var desktop;
          desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    }

    
    function ListaComuniEnteTds(formname,fieldname)
    {
      var codTipoSede = document.LoadInserisciOrdineEsecuzione.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_ENTE_SORVEGLIANZA%>.value
      if(codTipoSede=="" || codTipoSede=='-')
      {
        alert("Destinatario Ente di Sorveglianza è obbligatorio!");
      } else {
          var desktop;
          desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname+"&typename="+codTipoSede, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");

      }
    }
    
    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function ListaDocumentiSius(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.RIGETTO%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>=<%=ICostantiMisuraAlternativa.RIGETTO%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciOrdineEsecuzione.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }
    // NGG
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
    		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
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
<body class="corpo" onload="radio();">
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
      lAzione = "siap.siep.ordineesecuzione.action.ActInserisciRevocaSospensioneSimeone";
%>
      <font class="campo">Revoca Decreto Sospensione (legge 165/98) </font>
<%
    }
    else if( modalita.equals("M") )
     {
        lProvvedimento = new EventoModel(evento);
       lAzione = "siap.siep.ordineesecuzione.action.ActInserisciRevocaSospensioneSimeone";
%>
          <font class="campo">Modifica Revoca Decreto Sospensione (legge 165/98)</font>
<%
    }
%>

      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciOrdineEsecuzione" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciRevocaSospensioneSimeone">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>" value="">

<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
  {%>
    <INPUT type="HIDDEN" name="altracausa" value="<%=lFascicoloAssociato.getFlagAltraCausa()%>">
    <INPUT type="HIDDEN" name="codTipoPosGiuridica" value="<%=lAltraCausa.getCodTipoPosGiuridica()%>">
<%}else{%>
    <INPUT type="HIDDEN" name="altracausa" value="">
    <INPUT type="HIDDEN" name="codTipoPosGiuridica" value="">
<%}

  if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
  {%>
   <input type="HIDDEN" name="flagmisura" value="N">
<%}else
  {%>
   <input type="HIDDEN" name="flagmisura" value="S">
<%}%>


    <table width='100%'>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          	<font class="campo">
		        <%// Se detenuto altra causa, visualizzo Istituito di detenzione o Indirizzo
		        if (   lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")
		          && lPosizione!=null && lPosizione.getCodPosizioneGiuridica()!= null 
		          && lAltraCausa!=null && lAltraCausa.getCodAutorita()!=null )
		      	{ %>
		          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
				<% } else { %>
		          <%=lPosizione.getDescrPosizioneGiuridica()%>
				<% } %>
	        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
//modifica relativa al tipo istituto
           if(lAltraCausa.getIstitutoDetenzione() != null)
          {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
             </td>
           </tr>
           <%if (lAltraCausa.getAltroLuogo()!=null)
            { %>
            <tr>
             <td class="l">Altro Luogo </td >
             <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp; </td>
          <%} %>
          <%}
       } else
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
         {%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5><font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
               di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
          <%if (lLuogoDetenzione.getAltroLuogo()!=null)
            { %>
            <tr>
             <td class="l" >Altro Luogo </td >
             <td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp; </td>

          <%} %>
         </tr>
       <%}%>

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
    <% if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
          {}else{%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
<%
      }
    }
%>

      <tr>
<%
     if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
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
}
%>

<%
if(!lPosizione.isLibero() || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
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
</tr>
 <tr>
        <td class="l">Data Emissione </td>
        <td class="L" colspan=2 >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        </td>
        <td class="l">Data Trasmissione </td>
        <td class="L" colspan=2 >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

</table>
<table width='100%'>
<tr>
    <td class="l"  width='30%'>Motivo Revoca</td>
    <td class="L" colspan="3">
        <select  Title="Motivo Revoca "  name="<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA%>" onChange="radio();">
         <%=motivorevoca%>
         </select>
      </td>
 </tr>
 		<tr>
    	<td class="l">
    		<input type="checkbox" onClick="toggleComuneCasellario()" name="<%=ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE%>">
     		Foglio Complementare
     	</td>
     	<td class="l" id="labelCasellario" style="display:none;">Casellario Giudiziale</td>
     	<td class="l" id="inputCasellario" style="display:none;">
       	<input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       	<a href="Javascript:ListaUfficiPerTipo('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>','DIB');">
         	<img src="/images/filefolder.gif" border=0>
       	</a>
     	</td>
    </tr>
   <tr>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
</table>
<div id="ordinanza" style="visibility:hidden; position:relative; " >
<table width ="100%">
   <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza del Tribunale di Sorveglianza </td>
   </tr>
   <tr>
      <td class="l">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciOrdineEsecuzione');">
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
      <td class="l"> Anno / Numero Ordinanza</td>
      <td class="l">
        <input Title="Anno Orinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Ordinanza" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
        <td class="l"> 
            <select title="UfficioDestinazione" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>">
            	<%=tipUffDestEntSor%>
           	</select>
         </td>
         
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:ListaComuniTds('LoadInserisciOrdineEsecuzione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
   <tr>
      <td class="l">Oggetto Ordinanza</td>
      <td class="L" colspan="3">
        <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
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
      <td  class="l">Note</td>
       <td  class="L"  colspan="3">
         <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2 ><%=StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
      </td>
    </tr>
</table>
</div>
<div id="revocapm" style="visibility:hidden; position:relative; " >
<table width ="100%">
<tr>
      <td class="l">Motivo Revoca PM</td>
        <td class="L" colspan="3">
         <select  Title="Motivo Revoca PM"  name="<%=ICostantiMotivoEvento.CAMPO_COD_MOTIVO_REVOCA_PM%>">
          <%=motivorevocapm%>
         </select>
        </td>
</tr>

<tr>
      <td  class="l">Motivazioni</td>
      <td  class="L"  colspan="3">
       <TEXTAREA title="Note" name="<%=ICostantiMotivoEvento.CAMPO_MOTIVAZIONI%>" cols=80 rows=2></textarea>
      </td>
</tr>
</table>
</div>
<div id="magistrato" style="visibility:visible; position:relative; " >
     <table width="100%">
     <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
     <tr>
     <td class="l" width='30%'>Magistrato</td>
        <td class="L" colspan="3">
         <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
         <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
           <a href="Javascript:ListaMagistrati('LoadInserisciOrdineEsecuzione');">
            <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
      <td>
      <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
      </tr>
</table>
</div>

<div id="istituto" style="visibility:hidden; position:relative; " >
     <table width="100%">
   <tr><td class="Titolo" colspan=6>Destinatari</td></tr>
   <tr>
    <td class="l" width='30%'>Istituto Detenzione <font class=ob>(*)</font></td>
  <%if(posizioneluogoaltra!= null && posizioneluogoaltra.getAltraCausa() != null && posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null)
   {%>
       <td class="l">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getAltraCausa().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>

  <%}else
     {%>
              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineEsecuzione','<%= ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>

   <%}%>
    </tr>
</table>
</div>

<div id="autorita" style="visibility:hidden; position:relative; " >
     <table width="100%">
   <tr><td class="Titolo" colspan=6>Destinatari</td></tr>
    <tr>
     <td class="l" width='30%'>Autorità di polizia competente per territorio <font class=ob>(*)</font></td>
     <td class="L" colspan="3">
       <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
        <%=autoritaEsternaE%>
       </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
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
    </tr>
</table>
</div>
<div id="tribunale" style="visibility:hidden; position:relative; " >
 <table width="100%">
 	<tr>
 		<td class="Titolo" colspan=6>Ente di Sorveglianza</td>
 	</tr>
 	<tr>
 	     <td class="l">Destinatario</td >
         <td class="l"> 
            <select title="UfficioDestinazione" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_ENTE_SORVEGLIANZA %>">
            	<%=tipUffDestEntSor%>
           	</select>
         </td>
     </tr>
     <tr>
         <td class="l">Sede </td>
         <td class="l"  colspan="3">
            <input title="Sede Tribunale Sorveglianza"  type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuniEnteTds('LoadInserisciOrdineEsecuzione','<%= ICostantiNotifica.CAMPO_SEDE_TDS %>');">
               <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
    </tr>
 </table>
</div>


  
<div id="difensore" style="visibility:visible; position:relative; " >
     <table width="100%">
    <tr><td class="Titolo" colspan=6>Notifica al Difensore</td></tr>
<%
      int lIdxAvv = 0;
			int lNumAvvocati = avvocati.size();
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table width='100%'>
          <tr>
            <td class="l" width="20%">Per Avvocato </td>
            <td class="L">
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
         <table width='100%'>
          <tr><td class="l" width="20%">Autorità Destinazione </td>
          <td class="L" colspan='3'>
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaN%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
				if( lNumAvvocati < 2 )
				{
%>
        	<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
<%
				}
				else
				{
%>
        	<a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
<%
				}
%>
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td  class="l">Note</td>
       <td  class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=30 ></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
<%
    lIdxAvv++;
  }
%>

<table width='100%'>
<%
	if(istanza!=null && istanza.getIdEvento()!=null)
	{
%>
    <tr><td class="Titolo" colspan=6> Istanza </td></tr>
    <tr>
       <td class="l">Data</td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(istanza.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
     </tr>
     <tr>
     <td class="l">Oggetto</td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istanza.getDescrMotivo()) %> &nbsp;</font></td>
    </tr>
<%}%>
</table>

   <td class="lNoBord" colspan="2">
   <%if(decreto.getDataEmissione()==null)
{%>
      <input type="hidden" name="datadecreto" value="N">
<%}else{%>

       <input type="hidden" name="datadecreto" value="S">
<%}%>
     

      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
  </tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");

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
<%}%>

<%if(istanza!=null && istanza.getIdEvento()!=null)
{%>
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");
<%}%>

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");

</script>
</body>
</html>