<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.util.MinorMask"%>


<%-- Posizione giuridica corrente --%>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>

<jsp:useBean id="UfficioEmittente"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="tipoUfficioSIUS"     scope="request" class="java.lang.String"/>

<jsp:useBean id="motivoProvv"         scope="request" class="java.lang.String"/>  
<jsp:useBean id="tipoprovvedimento"   scope="request" class="java.lang.String"/>  
<jsp:useBean id="codiceAutorita"      scope="request" class="java.lang.String"/>


<jsp:useBean id="penaresidua"      scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"    scope="request" class="java.lang.String"/>
<jsp:useBean id="flagergastolo"    scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovapenaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"      scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>

<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%-- Per gestire la modifica --%>
<jsp:useBean id="tipoOperazione"  scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="misuraalternativaToChange"     scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="sedeUfficioEmittente"  scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<jsp:useBean id="NotificaIstDetenzione"    scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="daticssa"                 scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="DestTribunaleSorv"        scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="DestUfficioSorv"          scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="NotificaAutoritaEsternaC" scope="request" class="siap.siep.notifica.model.NotificaModel"/>

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
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
    var desktop;

    function ListaDocumentiSius(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname
    		  +"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>"
    		  +"&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.CONCESSIONE_SOSPENSIONE678%>"
      , "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
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

    function ListaUDS(a_formname,a_fieldname) {
      var a_typename = document.getElementById('<%=MinorMask.ComboMagistratoId%>').value;
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,
              "Ricerca_UDS",
              "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function ListaCSSA(a_formname,a_fieldname,a_field2) {
      var a_typename = document.getElementById('<%=MinorMask.ComboCSSAId%>').value;
      if (a_typename == "-")  {
        alert("Selezionare il Destinatario dell'UEPE/USSM");
      } else {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&typename="+a_typename, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    }


	  function Verify()
	  {
      if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        return false;
      }

      if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
      if (!ControllaData(data_to_verify) )
      {
         alert('Data di trasmissione non valida');
         return false;
      }

      if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value=="-")
      {
        alert("Il Tipo Provvedimento e' obbligatorio");
        document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.focus();
        return false;
      }
      
      if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="-")
      {
        alert("L'Autorita' Emittente e' obbligatoria");
        document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
        return false;
      }
      
      if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Autorita' Emittente e' obbligatoria");
        document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }

      if (  document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value == ""
          ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value == ""
          ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value == "" )
      {
        alert("La data emissione e' obbligatoria");
        document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.focus();
        return false;
      }
      else {
         if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
             document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
         if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
             document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

         var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value
                        +'-'+ document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value
                        +'-'+ document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
    
         if (!ControllaData(data_to_verify) )
         {
           alert('Data emissione non valida');
           document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.focus();
           return false;
         }   	  
      }
      
      
      
      if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value == ""
        ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value == ""
        ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value == "" )
      {
        alert("La data di Sospensione esecuzione e' obbligatoria");
        document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
        return false;
      }

      if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value != ""
        ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value != ""
        ||document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value != "" )
      {
        if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
            document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value;
        if (document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
            document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value;

        var data_to_verify = document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.value
                       +'-'+ document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>.value
                       +'-'+ document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>.value;
   
        if (!ControllaData(data_to_verify) )
        {
          alert('Data Sospensione esecuzione non valida');
          document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>.focus();
          return false;
        }
      }
      
      //
      if (   document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" 
          && document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il  Magistrato Firmatario e' obbligatorio");
        document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }

      var istituto = true;
      if(!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled) {
        istituto = false;
        if(document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="") {
         istituto = true;
        }
      }

      var cssa = true;
      if(!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled)
      {
        cssa = false;
        if(   document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" 
           || document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
        {
          cssa = true;
        }
      }

      var uds = false;
      if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value=="") {
         uds = true;
      }

      var tds = false;
      if(document.LoadInserisciSospensioneDecisioniSorv.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value=="")    {
         tds = true;
      }

      var polizia = true;
      if(!document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled)
      {
        polizia = false;
        if (document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciSospensioneDecisioniSorv.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-'){
          polizia = true;
        }
      }

      if(istituto && cssa && uds && tds && polizia)
      {
        alert("Inserire almeno un destinatario!");
        return false;
      }
      
      return true;
  }



  function radio()
  {
    var pos = document.LoadInserisciSospensioneDecisioniSorv.CodPosizioneGiuridica.value;

    var nodeistituto = document.getElementById('divistituto');
    var nodecssa     = document.getElementById('divcssa');
    var nodesor      = document.getElementById('divsorveglianza');
    var nodeautorita = document.getElementById('divautoritacompetente');

    nodeistituto.style.display='block';
    nodecssa.style.display='block';
    nodesor.style.display='block';
    nodeautorita.style.display='block';  
  }

  function caricaCombo() {	  
	   <% if ("UDS".equals(sedeUfficioEmittente.getCodTipoUfficio()) || "UDSM".equals(sedeUfficioEmittente.getCodTipoUfficio()) ) { %>
	    $('#<%=MinorMask.ComboMagistratoId%>').val('<%=sedeUfficioEmittente.getCodTipoUfficio()%>').change();
	    <% } else if ("TDS".equals(sedeUfficioEmittente.getCodTipoUfficio()) || "TDSM".equals(sedeUfficioEmittente.getCodTipoUfficio()) ) { %>  
	    $('#<%=MinorMask.ComboTribunaleId%>').val('<%=sedeUfficioEmittente.getCodTipoUfficio()%>').change();
	    <% } %>
	    
	    <% 
	    if(daticssa.getIdCSSA()!=null) { 
	      String tipoCSSA = daticssa.getTipo();
	      if ( ("UEPESS").equals(tipoCSSA) )
	        tipoCSSA = "UEPE";
	    %>
	    $('#<%=MinorMask.ComboCSSAId%>').val('<%=tipoCSSA%>').change();
	    <% } %>
	    
	    <% if(DestTribunaleSorv.getCodUfficio().length()>0) { %>
	    $('#<%=MinorMask.ComboTribunaleId%>').val('<%=DestTribunaleSorv.getCodTipoUfficio()%>').change();
	    <% } %>
	    
	    <% if(DestUfficioSorv.getCodUfficio().length()>0) { %>
	    $('#<%=MinorMask.ComboMagistratoId%>').val('<%=DestUfficioSorv.getCodTipoUfficio()%>').change();
	    <% } %>
	    
	    <% if(NotificaAutoritaEsternaC.getAutoritaEsterna()!=null) { %>
	    $('#<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>').val('<%=NotificaAutoritaEsternaC.getAutoritaEsterna().getCodTipoAutorita()%>').change();
	    <% } %> 
	  }
  
</script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>

<%
String tipoOperazioneView = tipoOperazione;
if ("MODIFICA".equals(tipoOperazione))
  tipoOperazioneView = tipoOperazione;
%>

<body class="corpo" onLoad="radio();caricaCombo();">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo"><%=tipoOperazioneView%> Applicazione Sospensione dell'esecuzione della pena (ART.678 C.1 TER C.P.P.)</font>
    </td>
  </tr>
</table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSospensioneDecisioniSorv">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensioneDecisioniSorv678">
  <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativaToChange.getEveIdEvento())%>">
  <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" >
  <%--Ridondante ma serve alla ActMisuraAlternativa.setNotificheMisuraAlternativa --%>
  <input type="HIDDEN" name="posizionegiuridica" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" >
  <input type="HIDDEN" name="tipoOperazione" value="<%=tipoOperazione%>">

  <% if(eventonotifica.getEvento()!=null && eventonotifica.getEvento().getIdEvento()!=null) {%>
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(eventonotifica.getEvento().getIdEvento()) %>">
  <% } %>

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
        <% if( "S".equals(lFascicoloAssociato.getFlagAltraCausa()))  {%>
              DETENUTO PER ALTRA CAUSA
        <%} else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
       </font>
      </td>
    </tr>

<%
        if("S".equals(lFascicoloAssociato.getFlagAltraCausa()))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
                 <% } %>
             </td>
           </tr>

            <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
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
              <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
              <% } %>
            </td>
          </tr>
        <% } %>
        
<%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if ("02".equals(lPosizione.getCodPosizioneGiuridica()) || "04".equals(lPosizione.getCodPosizioneGiuridica()) )
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


<% if (penaresidua.getIdPenaResidua() != null && !penaresidua.isErgastolo() ) {%>
    <% if (!penaresidua.isQuantumReclusioneZero()) { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <% if (!penaresidua.isMultaZero()) { %>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
    <% } %>

    <% if (!penaresidua.isQuantumArrestoZero()){ %>
    <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <%if(!penaresidua.isAmmendaZero()){%>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      <% } %> 
    <tr>
    <% } %> 
<% } %>

<tr>
  <% if (penaresidua.getDataInizio() != null)  { %>
  <td class="l">Data Decorrenza Pena</td>
  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  <% } %>

  <% if ("S".equals(penaresidua.getFlagErgastolo())) { %>
  <td class="l">Pena Detentiva</td>
  <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
  <% } else if ("D".equals(penaresidua.getFlagErgastolo())) {%>
  <td class="l">Pena Detentiva</td>
  <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
  <% } %>

<%
    if (   !lPosizione.isLibero() 
    		||"S".equals(lFascicoloAssociato.getFlagAltraCausa()))
    {
      if  (!penaresidua.isErgastolo())
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
          }
          else if (penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
            <% } else { %>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              </td>
<%
           }
         }
      }
    }
%>

  <input type="HIDDEN" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
</tr>

<%
Date dataEmissioneProvv = DateUtils.getSysDate();
Date dataTrasmissione   = DateUtils.getSysDate();

if (eventonotifica.getEvento().getDataEmissione()!=null)
	dataEmissioneProvv = eventonotifica.getEvento().getDataEmissione();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length>0) {
	dataTrasmissione = eventonotifica.getNotifiche()[0].getDataInvio();
}
%>

  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissioneProvv,"dd"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissioneProvv,"MM"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissioneProvv,"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"dd"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"MM"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
      <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataTrasmissione,"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>

<%--
======================================================================================
    Provvedimento della SORVERGLIANZA
======================================================================================
--%>

<table style="width: 95%;">
  <tr>
    <td class="Titolo" colspan="4"> Dati Provvedimento di Sospensione del Magistrato/Tribunale di Sorveglianza </td>
  </tr>

   <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciSospensioneDecisioniSorv');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input type="text" Title="Anno Fascicolo Sius" size="4" maxlength="4" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" 
               value="<%=StringUtils.toStringJSP(misuraalternativaToChange.getChiaveAnnoFascicoloSius())%>"
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input type="text" size="6" maxlength="6"  Title="Numero Sius" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" 
               value="<%=StringUtils.toStringJSP(misuraalternativaToChange.getChiaveProgrFascicoloSius())%>"
               onkeypress="return TicTabNumField(this,event)"
               onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Provvedimento</td>
      <td class="l">
        <input type="text" size="4" maxlength="4" Title="Anno Provvedimento" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
               value="<%=StringUtils.toStringJSP(misuraalternativaToChange.getAnnoRegistro())%>"
               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
               onChange="pulisciId();">
        /
        <input type="text" size="6" maxlength="6" Title="Numero Provvedimento" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" 
               value="<%=StringUtils.toStringJSP(misuraalternativaToChange.getNumeroRegistro())%>"
               onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Tipo provvedimento </td>
      <td class="l" colspan="3">
       <select Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
         <%=tipoprovvedimento%>
       </select>
      </td>
    </tr>
    <tr>
      <td class="l">Autorita' Emittente</td>
      <td class="l" colspan="3">
          <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='pulisciId();'"
                                    , ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, tipoUfficioSIUS)%>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Autorita' Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input type="text" Title="Luogo Ufficio Sorveglianza" size="35"
                 name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" 
                 value="<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%>"
                 onChange="pulisciId();">
          <a href="Javascript:ListaComuniEmitUTMinor('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Oggetto Provvedimento</td>
      <td class="L" colspan="3">
        <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Provvedimento <font class=ob>(*)</font></td>
	    <% 
	      Date dataEmissione = null; 
	      if (misuraalternativaToChange.getDataDecisione()!=null)
	        dataEmissione = misuraalternativaToChange.getDataDecisione();
	    %>      
      <td class="l" colspan="3">
      <font class="campo">
        <input type="text" size="2" maxlength="2" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"dd"))%>" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
               onChange="pulisciId();"> -
        <input type="text" size="2" maxlength="2"  
               name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"MM"))%>"    
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
               onChange="pulisciId();"> -
        <input type="text" size="4" maxlength="4" 
               name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataEmissione,"yyyy"))%>"         
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" 
               onChange="pulisciId();">
      </font>
      </td>
    </tr>

    <tr>
      <td class="l">Motivazioni</td>
      <td class="L" colspan="3">
        <TEXTAREA title="Motivazioni" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2><%=StringUtils.toStringJSP(misuraalternativaToChange.getNote())%></textarea>
      </td>
    </tr>
</table>


<%
String scarcerato = null;
String scarcerare = null;

if("SORV".equals(misuraalternativaToChange.getCodTipoUfficioScarcerazione())) {
  scarcerato ="checked";
}
else if("PROC".equals(misuraalternativaToChange.getCodTipoUfficioScarcerazione())) {
  scarcerare ="checked";
}
else
	scarcerare = "checked";

Date dataSospensione = null; 
if (misuraalternativaToChange.getDataScarcerazione()!=null)
  dataSospensione = misuraalternativaToChange.getDataScarcerazione();
%>
<table>
  <tr>
      <td  class="l">Data Sospensione Esecuzione <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSospensione,"dd"))%>"  name="<%=ICostantiSospensione.CAMPO_GIORNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input type="text" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSospensione,"MM"))%>"  name="<%= ICostantiSospensione.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input type="text" size="4" maxlength="4" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(dataSospensione,"yyyy"))%>"  name="<%= ICostantiSospensione.CAMPO_ANNO_DATA_INIZIO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Da Scarcerare &nbsp;<input type="radio" name="tipo" value="scarcerare"  <%=StringUtils.toStringJSP(scarcerare)%> onclick="radio();">
             &nbsp; Libero per avvenuta Scarcerazione  &nbsp; <input type="radio" name="tipo" value="scarcerato"  <%=StringUtils.toStringJSP(scarcerato)%> onclick="radio();">
      </td>
  </tr>
</table>

<% 
//=======================================================================
// Sezione Magistrato e destinatari se sul secondo giro
//=======================================================================
%>
<table width ="95%">
  <tr>
    <td class="Titolo" colspan=6>Magistrato Firmatario</td>
  </tr>
  <tr>
    <td class="l" width="30%">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>

<table width ="95%">
  <tr>
    <td class="Titolo" >Destinatari</td>
  </tr>
</table>

<div id="divistituto" style="display:none; " >
  <table style="width: 95%;">
    <tr>
      <td class="l" width ="30%">Istituto di Detenzione</td>
      <td class="l" colspan="3">
      <%
      String lDescIstituto = "";
      String lIdIstituto = "";
      if (NotificaIstDetenzione.getIdIstitutoDetenzione()!=null && NotificaIstDetenzione.getIdIstitutoDetenzione().length()>0){
        lIdIstituto   = NotificaIstDetenzione.getIdIstitutoDetenzione();
        lDescIstituto = StringUtils.toStringJSP(NotificaIstDetenzione.getDescrTipoIstituto(),"")+" di "+StringUtils.toStringJSP(NotificaIstDetenzione.getDescrComune(),"");
      }
      %>
         <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lDescIstituto)%>" size="50">
         <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" 
                value="<%=StringUtils.toStringJSP(lIdIstituto)%>" >
         <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciSospensioneDecisioniSorv','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
         <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
  </table>
</div>

<div id="divcssa" style="display:none;" >
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan="4">UEPE/USSM</td>
    </tr>
    <tr>
      <td class="l" width="30%">Destinatario</td>
      <td class="l" colspan="3"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="l" colspan="3">
        <%
        String indirizzoCSSA = "";
        if (daticssa.getIdCSSA() != null) {
          indirizzoCSSA = daticssa.getComune() + " - " + daticssa.getIndirizzo();
        }
        %>      
        <input readonly Title="Sede UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(indirizzoCSSA)%>" size=60 >
        <input type="hidden" Title="Sede UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
        <a href="Javascript:ListaCSSA('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>
</div>

<div id="divsorveglianza" style="display:block; " >
<%
String lSedeUfficioMDS = null;
String lSedeUfficioTDS = null;
if ("UDS".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
  lSedeUfficioMDS = sedeUfficioEmittente.getDescrComune();
} else if ("TDS".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
  lSedeUfficioTDS = sedeUfficioEmittente.getDescrComune();
}

// In caso di modifica ricarico i dati
if (DestTribunaleSorv.getCodUfficio().length()>0)
  lSedeUfficioTDS = DestTribunaleSorv.getDescrComune();
if (DestUfficioSorv.getCodUfficio().length()>0)
  lSedeUfficioMDS = DestUfficioSorv.getDescrComune();
%>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
    </tr>
    <tr>
      <td class="l" width="30%">Destinatario</td>
      <td class="l" colspan="3"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L" colspan="3">
        <input type="text" title="Sede Ufficio Sorveglianza" maxlength="35" size="25"
               name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" 
               value="<%=StringUtils.toStringJSP(lSedeUfficioMDS)%>" >
        <a href="Javascript:ListaUDS('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

    <tr>
      <td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
    </tr>
    <tr>
      <td class="l" width="30%">Destinatario</td>
      <td class="L" colspan="3"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="l" colspan="3">
        <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <input type="text" title="Sede Tribunale Sorveglianza" maxlength="35" size="35"
               name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>" 
               value="<%=StringUtils.toStringJSP(lSedeUfficioTDS)%>" >
        <a href="Javascript:ListaComuniTds('LoadInserisciSospensioneDecisioniSorv','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>
</div>

<div id="divautoritacompetente" style="display:none;" >
  <table style="width: 95%;">
    <tr>
      <td class="l" width=30%>Autorita' di polizia</td>
      <td class="L" colspan="3">
        <select Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>"
                id="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">>
          <%=codiceAutorita%>
        </select>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
          <input title="Sede Autorita Esterna"  type="text" 
                 name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>"  
                 value="<%=(NotificaAutoritaEsternaC.getAutoritaEsterna()!=null ? NotificaAutoritaEsternaC.getAutoritaEsterna().getDescrSede() : "") %>"
                 maxlength="35" size="35">
           <a href="Javascript:ListaComuni('LoadInserisciSospensioneDecisioniSorv','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
        <td class="l">Indirizzo</td>
        <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>"  cols=30 ><%=StringUtils.toStringJSP(NotificaAutoritaEsternaC.getNote())%></textarea>
        </td>
      </tr>
  </table>
</div>


<br>

<table style="width: 95%;">
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT type="submit" class="bottone" name="I" value="Conferma" >
    </td>
  </tr>
</table>

</FORM>

<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("LoadInserisciSospensioneDecisioniSorv");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>