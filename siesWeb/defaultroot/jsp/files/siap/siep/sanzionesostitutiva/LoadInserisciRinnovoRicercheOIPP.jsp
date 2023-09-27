<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>


<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"  />
<jsp:useBean id="soggetto"  scope="session" class="siap.sico.soggetto.model.SoggettoModel"  />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="ordineIngiunzione" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="notifica"          scope="request" class="siap.siep.notifica.model.NotificaModel"  />
<jsp:useBean id="verbale"           scope="request" class="siap.siep.verbale.model.VerbaleModel"  />
<jsp:useBean id="listaRinnovi"      scope="request" class="java.util.Vector"  />

<jsp:useBean id="tipoAutoritaAltra" scope="request" class="java.lang.String"  />

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");
PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
%>

<% 
   int contaRinnovi = listaRinnovi.size();
   int contaDaValidare = 0;
   for (int i = 0; i < listaRinnovi.size(); i++) 
   {
     RinnovoModel lRinnovo = (RinnovoModel) listaRinnovi.elementAt(i);
     if (lRinnovo.getFlagDocumentoRegistrato()==null 
          || lRinnovo.getFlagDocumentoRegistrato().equals("N") )
       contaDaValidare++; 
   }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ordine Ingiunzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>  
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript">
      var desktop;
      var verbaleVVR = '0';
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }    

      function radio()
      {
        var nodeFor = document.getElementById('divForz');
        var nodeDel = document.getElementById('divdelegato');
        
        var nodeUff = document.getElementById('divUff');
        var nodeRi  = document.getElementById('divRicerca');
        var nodeNo  = document.getElementById('divNotifica');

        var mese   = <%=DateUtils.getSysDate("MM")%>;
        var giorno = <%=DateUtils.getSysDate("dd")%>;
        var anno   = <%=DateUtils.getSysDate("yyyy")%>;

        if (mese<10)
          mese ='0'+mese;
        if (giorno<10)
          giorno='0'+giorno;

        var lverbale = <%=verbale.getIdVerbale()%>;

        if(lverbale == null && verbaleVVR=='0') {
            if (window.confirm("Non Esiste Nessun Verbale Vane Ricerche Associato all'Ordine di Ingiunzione.\n\t Vuoi andare al Verbale Vane Ricerche ?"))
            {
               var str = "/jsp/Main.jsp?Action=siap.siep.verbale.action.ActLoadInserisciVerbaleVaneRicerche&FlagOmesseOIPP=S";
               window.location.href=str;
            }
            else {
            	document.LoadInserisciRinnovoRicercheOIPP.Notifica[1].checked=true;
            	//document.LoadInserisciRinnovoRicercheOIPP.Notifica[0].disabled=true;
              verbaleVVR='1';
            }
        }
        
        if(document.LoadInserisciRinnovoRicercheOIPP.Notifica[0].checked)
        {
          <%-- Omessa Notifica Forza di Polizia --%>
          nodeUff.style.display='none';
          nodeRi.style.display='none';
          nodeNo.style.display='none';

          nodeFor.style.display = "block";
          
          if(lverbale == null) {
              if (window.confirm("Non Esiste Nessun Verbale Vane Ricerche Associato all'Ordine di Ingiunzione. Non è possibile registrare 'Omessa Notifica Forza di Polizia'\n\t Vuoi andare al Verbale Vane Ricerche ?"))
              {
                 var str = "/jsp/Main.jsp?Action=siap.siep.verbale.action.ActLoadInserisciVerbaleVaneRicerche&FlagOmesseOIPP=S";
                 window.location.href=str;
              }
              else {
                document.LoadInserisciRinnovoRicercheOIPP.Notifica[1].checked=true;
                radio();
                //document.LoadInserisciRinnovoRicercheOIPP.Notifica[1].fireEvent("onchange");
                return;
              }
          }
                    
          if(document.LoadInserisciRinnovoRicercheOIPP.Rinnovo[0].checked)
          {
            nodeDel.style.display = 'none';

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value = giorno;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value = mese;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value = anno;

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.disabled = true; 
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value = "";      
          } else {
            nodeDel.style.display = 'block';

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.disabled = false;
            
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value = giorno;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value = mese;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value = anno;

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value = "";
          }
        } else {
          <%-- Omessa Notifica Ufficiali Giudiziari --%>
          document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value = giorno;
          document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value = mese;
          document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>.value = anno;

          nodeFor.style.display = 'none';
          nodeDel.style.display = 'none';
          nodeUff.style.display = 'block';

          if(document.LoadInserisciRinnovoRicercheOIPP.Ufficiali[0].checked)
          {
            <%-- Rinnovo notifica --%>
            nodeNo.style.display='block';
            nodeRi.style.display='none';

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value = giorno;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value = mese;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value = anno;

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value = "";
          } else {
            <%-- Attivazione ricerca --%>
            nodeNo.style.display='none';
            nodeRi.style.display='block';

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.disabled = false;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value = giorno;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value = mese;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value = anno;

            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.disabled = true;
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value = "";
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value = "";
          }
        }
      }

    
      function ListaRinnovi(){
        var nodeListaRinnovi = document.getElementById('divListaRinnovi');
        if ( nodeListaRinnovi.style.display=='block')
          nodeListaRinnovi.style.display='none';
        else
          nodeListaRinnovi.style.display='block';
      }
    
      function CancellaRinnovo(idRinnovo){
        if (window.confirm("Confermi l'eliminazione del Rinnovo?")) {
          document.cancellaRinnovo.<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>.value = idRinnovo;
          document.cancellaRinnovo.submit();
        }
      }
      
      var lastRadioEsitoVal="";
      function radioEsito() {    
    	  /* Si incarta IE sugli altri radio
	    	radio = document.getElementsByName('<%=ICostantiRinnovo.CAMPO_ESITO%>'); 
	    	for (i = 0; i < radio.length; i++) {
	    		if (radio[i].checked) {
	    			if (radio[i].value==lastRadioEsitoVal) {
	    				radio[i].checked = false;
	    				lastRadioEsitoVal="";
	    			}
	    			else 
	    				lastRadioEsitoVal = radio[i].value;
	    		}
	    	}
	    	*/
      }
      
      function downloadStampaRinnovo (idRinnovo)
      {
        var lAzione = "<%=IWebConstants.ACTION_FIELD%>=siap.siep.sanzionesostitutiva.action.ActLoadDocumentoRinnovoRicercheOIPP";
        var parametri = lAzione+"&<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>="+idRinnovo;
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
      }
      
      function Verify() {        
    	  var lverbale = <%=verbale.getIdVerbale()%>;
    	  
        if(document.LoadInserisciRinnovoRicercheOIPP.Notifica[0].checked)
        {
          if(document.LoadInserisciRinnovoRicercheOIPP.Rinnovo[0].checked)
          {
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value;
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value;

            var data_to_verify = document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>.value;

            if (!ControllaDataPassaVuota(data_to_verify) )
            {
              alert('Data Rinnovo non valida');
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>.focus();
              return false;
            }
          } else {
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value;
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value;

            var data_to_verify = document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>.value;

            if (!ControllaDataPassaVuota(data_to_verify) )
            {
              alert('Data Rinnovo non valida');
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>.focus();
              return false;
            }
            
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value=='-') {
              alert("Indicare l'Autorità di polizia delegata");
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.focus();
              return false;
            }
/*            
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>.value!='22') {
                if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.value=='') {
                  alert("Indicare la sede dell'Autorità di polizia delegata");
                  document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>.focus();
                  return false;
                }              
            }
*/            
          }
        } else {            
          if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value.length==1)
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value;
          if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value.length==1)
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value;

          var data_to_verify = document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.value
                          +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>.value
                          +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>.value;

          if (!ControllaDataPassaVuota(data_to_verify) )
          {
            alert('Data Relata non valida');
            document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>.focus();
            return false;
          }

          if(document.LoadInserisciRinnovoRicercheOIPP.Ufficiali[0].checked)
          {          
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value;
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value;

            var data_to_verify = document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>.value;

            if (!ControllaDataPassaVuota(data_to_verify) )
            {
              alert('Data Rinnovo non valida');
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>.focus();
              return false;
            }
            
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>.value=='') {
              alert('Indicare gli ufficiali delegati');
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>.focus();
              return false;
            }
            
          }
          else {
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value;
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value.length==1)
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value='0'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value;

            var data_to_verify = document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>.value
                            +'-'+document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>.value;

            if (!ControllaDataPassaVuota(data_to_verify) )
            {
              alert('Data Attivazione ricerca non valida');
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>.focus();
              return false;
            }
            
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG%>.value=='-') {
              alert("Indicare l'Autorità di polizia delegata");
              document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG%>.focus();
              return false;
            }
            
/*
            if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG%>.value!='22') {
              if (document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>.value=='') {
                alert("Indicare la sede dell'Autorità di polizia delegata");
                document.LoadInserisciRinnovoRicercheOIPP.<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>.focus();
                return false;
              }              
            }
*/            
          }
        }
        
        return true;   
      }
      
      function gestisciCampi(){
          <% if (contaDaValidare>0) { %>
          $('#divInserimento').find("input, select, textarea").attr('disabled','disabled');
          $('#divInserimento').find("img").hide();
          <% } %>
      }      
    </script>
  </head>
  
<body class="corpo" onLoad="radio();gestisciCampi();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rinnovo Ricerche per Omesse Notifiche</font>
      </td>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovoRicercheOIPP">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>         
    </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="cancellaRinnovo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActCancellaRinnovoRicercheOIPP">
  <input type="HIDDEN" name="<%=ICostantiRinnovo.CAMPO_ID_RINNOVO%>" value="">
</form>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRinnovoRicercheOIPP">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciRinnovoRicercheOIPP">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=ordineIngiunzione.getEvento().getIdEvento()%>">
  <input type="hidden" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="<%=notifica.getIdNotifica()%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan="5">
        <font class="campo">
        <%
          if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        %>
          DETENUTO PER ALTRA CAUSA - <%=lAltraCausa.getDescrTipoPosGiuridica()%>
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
        <input type="HIDDEN" title="Codice Posizione" 
               value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" 
               name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"   >
      </td>
    </tr>
  </table>
  
  <table>
    <tr>
      <td class="L" colspan="5">
        <font class="campo">
              <%=ordineIngiunzione.getEvento().getDescrTipoProvvedimento()%>
              &nbsp;
              <%=ordineIngiunzione.getEvento().getDescrMotivo()%>
              &nbsp;emesso in data&nbsp;
              <%=DateUtils.getDateToString(ordineIngiunzione.getEvento().getDataEmissione(), "dd-MM-yyyy")%>
        </font>
      </td>
    </tr>
  </table>

<br>

<% if (contaDaValidare > 0) {  %>
  <table>
    <tr>
      <td class="l" style="color:red;">
        Attenzione, esiste un rinnovo ancora da validare
      </td>
    </tr>
  </table>
<% } %>  
  
  <div id="divListaRinnovi" style="width: 100%; display:block;" >
    <table width="100%">
      <tr>
        <td class="int">Tipo Rinnovo</td>
        <td class="int">Data Rinnovo Notifica</td>
        <td class="int">Autorità incaricata delle ricerche</td>
        <td class="int">Validato</td>
        <td class="int" style="width:50px;">Azioni</td>
      </tr>
      
      <% if (contaRinnovi == 0) {  %>
      <tr>
        <td class="L" colspan="5">Nessun Sollecito presente per il provvedimento selezionato</td>
      </tr>
      <% } %>
            
      <% 
      for (int i = 0; i < listaRinnovi.size(); i++) 
      {
        RinnovoModel lRinnovo = (RinnovoModel) listaRinnovi.elementAt(i);
        String lActDettaglio = "siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRinnovoRicercheOIPP";
        lActDettaglio += "&"+ICostantiRinnovo.CAMPO_ID_RINNOVO+"="+lRinnovo.getIdRinnovo();
        
        String descRinnovo = "";
        if ("A".equals(lRinnovo.getCodTipoRinnovo()))
          descRinnovo = "Rinnovo Notifica Ufficiali Giudiziari - "+lRinnovo.getDescrTipoRinnovo();          
        else 
          descRinnovo = lRinnovo.getDescrTipoRinnovo();
          
      %>
      <tr>
        <td class="c"><%=StringUtils.toStringJSP(descRinnovo)%></td>
        <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRinnovo.getDataRinnovo(),"dd-MM-yyyy"),"")%></td>
        <td class="c"><%=StringUtils.toStringJSP(lRinnovo.getDescrTipoAutoritaRinnovo())%> di <%=StringUtils.toStringJSP(lRinnovo.getDescrLuogoRinnovo())%></td>
        <td class="c">
        <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <img  alt="Validato" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0"></a>
        <% } else { %>
        &nbsp;<font style="color:red;">(da validare)</font>
        <% } %>
        </td>
        <td class="r" nowrap>
          <% if (!"S".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:CancellaRinnovo('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Cancella" src="<%=IWebConstants.IMAGES_DIR%>delete.gif" border="0"></a>
          <% } %>
          <% if ("S".equals(lRinnovo.getFlagDocumentoRegistrato()) || "N".equals(lRinnovo.getFlagDocumentoRegistrato())) { %>
          <a href="Javascript:downloadStampaRinnovo('<%=lRinnovo.getIdRinnovo()%>')">
            <img  alt="Stampa" src="<%=IWebConstants.IMAGES_DIR%>print.gif" border="0"></a>
          <% } %>
          <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>">
            <img  alt="Dettaglio" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" border="0"></a>
        </td>
      </tr>
      <% } %>
    </table>
  </div>
  <br>

<div id="divInserimento">
  <table  width=100%>
    <tr>
      <td class="c">Omessa Notifica Forza di Polizia &nbsp;<input type="radio" name="Notifica" value="FP" checked  onClick="radio();">
                    Omessa Notifica Ufficiali Giudiziari  &nbsp; <input type="radio" name="Notifica" value="UG"  onClick="radio();">
      </td>
    </tr>
  </table>
  
<%
// =====================================================================
//  Omessa Notifica Forza di Polizia - Rinnovo altra autorità 
//  DIV con l'autorità  di polizia delegato
// =====================================================================
%>
<div id="divForz" style="width: 100%; display:none;" >
  <table width="100%">
    <tr>
      <td class="l" width="35%">Data pervenimento del verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"),"non disponibile")%>&nbsp;</font></td>
    </tr>

    <tr>
      <td class="l" >Data verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"),"non disponibile")%>&nbsp;</font> </td>
    </tr>

    <tr>
      <td class="l">Autorità  che ha redatto il verbale</td>
      <% if (verbale.getIdVerbale()!=null) {%>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario(),"non disponibile")%></font> di
                    <font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario(),"non disponibile")%></font>
      </td>
      <% } else { %>
      <td class="l">
      	<font class="campo">non disponibile</font> 
      </td>      
      <% } %>
    </tr>

    <tr>
      <td colspan="2">
        <table width="100%">
          <tr>
            <td class="l" >Rinnovo stessa autorità in data &nbsp;<input type="radio" name="Rinnovo" value="RS" checked  onClick="radio();">
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
            <td class="l">Rinnovo altra autorità in data &nbsp;<input type="radio" name="Rinnovo" value="RA" checked  onClick="radio();">
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
              <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  
  <%
  // =====================================================================
  //  Omessa Notifica Forza di Polizia - Rinnovo altra autorità
  //  DIV con l'autorità  di polizia delegato
  // =====================================================================
  %>
  <div id="divdelegato" style="width: 100%; display:none; " >
    <table width="100%">
      <tr>
        <td class="l">Autorità  di polizia delegata</td>
        <td class="l" colspan="3">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO%>">
            <%=tipoAutoritaAltra%>
          </select>
        </td>
      </tr>
      <tr>
         <td class="l">Luogo</td>
         <td class="L">
            <input title="Luogo" type="text" name="<%=  ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciRinnovoRicercheOIPP','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO %>');">
              <img src="/images/filefolder.gif" border="0">
            </a>
         </td>
         <td class="l">Indirizzo</td>
         <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiRinnovo.CAMPO_NOTE%>" cols="30"></textarea>
         </td>
      </tr>
    </table>
  </div>   
</div>  

 


<%
// ==============================================================
//  Omessa Notifica Ufficiali Giudiziari
// ==============================================================
%>
<div id="divUff" style="width: 100%; display:none; " >
  <table width=100%>
    <tr>
      <td class="l">Relata di notifica da Ufficiali Giudiziari di</td>
      <td class="L">
      <%
          if(   notifica.getAutoritaEsterna() != null 
             && notifica.getAutoritaEsterna().getCodSede() != null 
             && notifica.getAutoritaEsterna().getCodTipoAutorita() != null
             && notifica.getAutoritaEsterna().getCodTipoAutorita().equals("22")
            )
      {%>
        <input title="Luogo" type="text"  value="<%=notifica.getAutoritaEsterna().getDescrSede()%>" name="<%=ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>"  maxlength="35" size="35">
      <%} else {%>
        <input title="Luogo" type="text" name="<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>"  maxlength="35" size="35">
      <%}%>
        <a href="Javascript:ListaComuni('LoadInserisciRinnovoRicercheOIPP','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO_UG%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      
      <td class="L">
        con esito positivo&nbsp;<input type="radio" name="<%= ICostantiRinnovo.CAMPO_ESITO%>" value="<%= ICostantiRinnovo.VAL_CAMPO_ESITO_POSITIVO%>" onClick="radioEsito();">
        con esito negativo&nbsp;<input type="radio" name="<%= ICostantiRinnovo.CAMPO_ESITO%>" value="<%= ICostantiRinnovo.VAL_CAMPO_ESITO_NEGATIVO%>" onClick="radioEsito();">
      </td>
      
      <td class="l" >in data
        <input value="" type="text" size="2" maxlength="2"  name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
        <input value="" type="text" size="2" maxlength="2"  name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
        <input value="" type="text" size="4" maxlength="4"  name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RELATA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  
  <table width="100%">
    <tr>
      <td class="l" >Rinnovo notifica in data &nbsp;<input type="radio" name="Ufficiali" value="RN" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_RN%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Attivazione ricerca in data &nbsp;<input type="radio" name="Ufficiali" value="AR" checked  onClick="radio();">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_GIORNO_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiRinnovo.CAMPO_MESE_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiRinnovo.CAMPO_ANNO_DATA_RINNOVO_AR%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
  </table>
  
  
  <%
  // =====================================================================
  //  Omessa Notifica Ufficiali Giudiziari - Rinnovo notifica 
  // =====================================================================
  %>
  <div id="divNotifica" style="width: 100%; display:none; " >
    <table width="100%">
      <tr>
        <td class="l">Ufficiali Giudiziari delegati in</td>
        <td class="L">
          <input title="Luogo" type="text" name="<%=ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciRinnovoRicercheOIPP','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
      </tr>
      <tr>
        <td class="l">Luogo Nuova Notifica</td>
        <td class="L">
          <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA%>"  maxlength="35" size="35">
        </td>
      </tr>
    </table>
  </div>

  <%
  // =====================================================================
  //  Omessa Notifica Ufficiali Giudiziari - Attivazione ricerca
  // =====================================================================
  %>
  <div id="divRicerca" style="width: 100%; display:none; " >
    <table width="100%">
      <tr>
        <td class="l">Autorità  di polizia delegata</td>
        <td class="l">
          <select title="TipoAutorita" name="<%=ICostantiRinnovo.CAMPO_COD_TIPO_AUTORITA_RINNOVO_UG%>">
            <%=tipoAutoritaAltra%>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Luogo</td>
        <td class="L">
          <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciRinnovoRicercheOIPP','<%= ICostantiRinnovo.CAMPO_COD_LUOGO_RINNOVO_UG_AR%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr>
        <td class="l">Luogo Nuova Notifica</td>
        <td class="L">
          <input title="Luogo" type="text" name="<%= ICostantiRinnovo.CAMPO_LUOGO_NUOVA_NOTIFICA_UG_AR%>"  maxlength="35" size="35">
        </td>
      </tr>
    </table>
  </div>
  
</div>




<div id="divBottone" style="width: 100%;" >
  <table>
    <tr>
      <td>
        <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActInserisciOmessaNotifica">
        <input class="bottone" type="submit" name="INSERISCI" value="Conferma" >
      </td>
    </tr>
  </table>
</div>

</div>
</form>



  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciRinnovoRicercheOIPP");  
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>





