<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<% // penaresidua = pena corrente %>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<% // isPenaRideterminata = 'S' se la pena è stata rideterminata %>
<% // nuovapenaresidua = pena rideterminata %>
<jsp:useBean id="isPenaRideterminata" scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovapenaresidua"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="isInsProvvSorv"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoCessazione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS"    scope="request" class="java.lang.String"/>
<jsp:useBean id="comboTipoProvvSorv" scope="request" class="java.lang.String"/>
<jsp:useBean id="comboOggettiMDS"    scope="request" class="java.lang.String"/>
<%//====  MISURA INSERITA  =====%>
<jsp:useBean id="misuraalternativa" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="UfficioEmittente"  scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<%//====  DESTINATARI  =====%>
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="codiceAutoritaE"       scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"              scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"    scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

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

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2){
      var left = (screen.width/2)-(500/2);
      var top = (screen.height/2)-(500/2);
   
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500, top="+top+", left="+left+"");
    }  
    

    function ListaComuni(a_formname,a_fieldname) {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3) {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function ListaComuniTds(formname,fieldname) {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUDS(a_formname,a_fieldname) {
    	// MEV10-s3: aggiunto parametro di passaggio
		var a_typename = document.getElementById('<%=MinorMask.ComboMagistratoId%>').value;
      	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename,
      			"Ricerca_UDS",
      			"toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function VisualizzaAvvocati(){
      var node = document.getElementById('divavvocati');
  
      if(document.LoadInserisciMisuraAlternativa.checkAvvocati.checked == true) {
         node.style.display='block';
      }
      else {
         node.style.display='none';
      }
    }
  
    function ListaDocumentiSius(a_formname)
    {
      var tipoMA;
      var naturaMA = "<%=ICostantiMisuraAlternativa.CESSAZIONE_51BIS_MDS%>";
      
      if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='AFFIDAMENTO') {
        tipoMA='<%=ICostantiMisuraAlternativa.AFFIDAMENTO_IN_PROVA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='DETENZIONE') {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='SEMILIBERTA') {
        tipoMA='<%=ICostantiMisuraAlternativa.SEMILIBERTA%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='INDULTINO')  {
        tipoMA='<%=ICostantiMisuraAlternativa.INDULTINO%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='DETENZIONE_DOMICILIARE_TERMINE') {
        tipoMA='<%=ICostantiMisuraAlternativa.DETENZIONE_DOMICILIARE_TERMINE%>';
      }
      else if(document.LoadInserisciMisuraAlternativa.tipoCessazione.value=='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>')
      {
        tipoMA='<%=ICostantiMisuraAlternativa.ESP_PRESSO_DOM%>';
      }

      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }
    
    
    //===============================
    //
    //===============================    
    function VisualizzaCalcoli(){  
      if(document.LoadInserisciMisuraAlternativa.checkPenaRideterminata.checked == true) {
         document.getElementById("idTR1").style.display = "block";
         document.getElementById("idTR2").style.display = "block";
         try {
          document.getElementById("idTR3").style.display = "block";
         } catch(err){  }
      }
      else {
         document.getElementById("idTR1").style.display = "none";
         document.getElementById("idTR2").style.display = "none";
         try {
          document.getElementById("idTR3").style.display = "none";
         } catch(err){  }
      }
    }
    
    

    function pulisciId()
    {
      document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
    }
    
    
    function handleChangeRadio(radioObj){
      if (radioObj.value=='misura'){
        //alert("in misura");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value = "";

        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.disabled = true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.disabled = true;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.disabled = true;
      }
      else if (radioObj.value=='detenuto'){
        //alert("detenuto");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value = "";
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value = "";

        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.disabled = false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.disabled = false;
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.disabled = false;
      }
    }

    //===============================
    //
    //=============================== 
    function Verify()
    {
// non Libero
// detenuto altra causa e la pena in decorrenza ha valorizzato solo la data fine presunta
// ovvero quella calcolata in fase di rideterminazione decorrenza scadenza
<%if(   !lPosizione.isLibero()
     || ( lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())
           && penaresidua.getDataFinePresunta()!= null
           && penaresidua.getDataFine() == null
        ) 
    )
{
  if (!penaresidua.isErgastolo())
  {
    if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
    {%>
    if ( typeof(document.LoadInserisciMisuraAlternativa.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>) != "undefined" ) 
    {
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
    }
<%}
 }
}%>

    <% if ("S".equals(isInsProvvSorv)) {%>
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>.value=="-")
      {
        alert("Selezionare l'Ufficio Emittente");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.focus();
        return false;
      }
      

      if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }
      

      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>.value=="-")
      {
        alert("Selezionare il Tipo Provvedimento");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE %>.focus();
        return false;
      }
      
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=="-")
      {
        alert("Selezionare l'Oggetto del Provvedimento");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiEvento.CAMPO_COD_MOTIVO %>.focus();
        return false;
      }
       
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
      if (!ControllaDataPassaVuota(data_to_verify) )
      {
        alert('Data di emissione del Provvedimento non valida');
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>.focus();
        return false;
      }

      // Misura cessata dal: se specificata deve essere valida
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>.value
                      +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>.value
                      +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>.value;
      if (!ControllaDataPassaVuota(data_to_verify) )
      {
        alert('Data di Cessazione Misura non valida');
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA %>.focus();
        return false;
      }
      
      // Pena rideterminata: va indicata o la data o i quantum. Non entrambi
      if (data_to_verify!="--")
      {
        if (   document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>.value != ""
            || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>.value != ""
            || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>.value != "" 
            || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>.value != ""
            || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>.value != ""
            || document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>.value != ""
           )
        {
          alert('Mettere o Data Inizio Cessazione o Quantum Calcolo Pena residua');
          return false;
        } 
      }
      
      if (typeof(document.LoadInserisciMisuraAlternativa.tipo) != "undefined") {
        if(document.LoadInserisciMisuraAlternativa.tipo[1].checked == true)
        {
          if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value;
          if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value.length==1)
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value;
    
          var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>.value
                          +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>.value
                          +'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>.value;
                        
          if (!ControllaData(data_to_verify) )
          {
            alert('Data di Ingresso in carcere non valida');
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO %>.focus();
            return false;
          }
        }
      }
    <% } else {%>
      // Data Emissione
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
       alert('Data di emissione non valida');
       document.LoadInserisciMisuraAlternativa.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();
       return false;
      }

      // Data Trasmissione
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
      
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di Trasmissione non valida');
        document.LoadInserisciMisuraAlternativa.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>.focus();
        return false;
      }

      //
      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Magistrato Firmatario è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }  

      if (typeof(document.LoadInserisciMisuraAlternativa.tipo) != "undefined") {
        //alert("Tipo presente in maschera");
        //if(document.LoadInserisciMisuraAlternativa.tipo[1].checked==true)
        if (typeof(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>) != "undefined")
        {
          if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
          {
            alert("L'Istituto di Detenzione è obbligatorio");
            return false;
          }
        }
  
        //if(document.LoadInserisciMisuraAlternativa.tipo[0].checked==true)
        if (typeof(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>) != "undefined") 
        {
          if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
          {
            alert("L'Autorità di destinazione è obbligatoria");
            document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E %>.focus();
            return false;
          }
        }
      }
      else {
        //alert("Tipo NON presente in maschera. Detenuto");
        if (typeof(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>)!= "undefined") {
          if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
          {
            alert("L'Istituto di Detenzione è obbligatorio");
            return false;
          }
        }
      }

      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value=="")
      {
        alert("L'Ufficio di Sorveglianza è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.focus();    
        return false;
      }

      var nodeavvocati  = document.getElementById('divavvocati');
      if (nodeavvocati.style.display=='none'){
        // Se il nodo avvocati non è visibile resetto i campi prima della submit
        // altrimenti vengono comunque notificati
        <% if (avvocati.size()==1) {%>
          document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value="";
        <% } else { %>
        for (var i=0; i<<%=avvocati.size()%>; i++){ 
          document.LoadInserisciMisuraAlternativa.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>[i].value="";
        }
        <% } %>
      }
      else {
        //alert("nodo avvocati visibile");
      }
    <% } %>

    return true;
  }


  </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>


<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;  
        <%  if(tipoCessazione.equals("AFFIDAMENTO"))  {%>
            <font class="campo">Cessazione Art.51 Bis Affidamento In Prova</font>
        <%  }  else if(tipoCessazione.equals("DETENZIONE"))  {%>
            <font class="campo">Cessazione Art.51 Bis Detenzione Domiciliare</font>
        <%  }  else if(tipoCessazione.equals("SEMILIBERTA"))  {%>
            <font class="campo">Cessazione Art.51 Bis Semilibertà </font>
        <%  }  else if(tipoCessazione.equals("INDULTINO"))  {%>
           <font class="campo">Cessazione Art.51 Bis L.207/2003 </font>
        <%  }  else if(tipoCessazione.equals("DETENZIONE_DOMICILIARE_TERMINE"))  {%>
            <font class="campo">Cessazione Art.51 Bis Detenzione Domiciliare a Termine</font>
        <%  }  else if(tipoCessazione.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))  {%>
           <font class="campo">Cessazione Art.51 Bis Espiazione Pena presso Domicilio</font>
        <%  }%>
      </td>
    </tr>
  </table>
  
  
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMACessazione51Bis">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(misuraalternativa.getEveIdEvento())%>">
  <INPUT type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>">
  <INPUT type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" >

  <INPUT type="HIDDEN" name="tipoCessazione" value="<%=tipoCessazione%>">
  <INPUT type="HIDDEN" name="isInsProvvSorv" value="<%=isInsProvvSorv%>">

  <INPUT type="HIDDEN" name="isPenaRideterminata" value="<%=isPenaRideterminata%>">


  <% //n.b. purtroppo serve alla ActMisuraALternativa.setNotificheMisuraAlternativa %>
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>">


<%
//==============================================================================
// Posizione Giuridica - Luogo di Detenzione - Tipo Pena - Quantum - Decorrenza Scadenza
//==============================================================================
%>
<table>
  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
      <font class="campo">
      <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) { %>
        DETENUTO PER ALTRA CAUSA
      <% } else { %>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
      <% } %>
      </font>
    </td>
  </tr>

<%//================================== %>

<% 
// Detenuto altra causa
if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa())) 
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
           <%  }
  }
}
else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
{  // non detenuto altra causa
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) {%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
              <% } %>
            </td>
          </tr>
<% } %>
        
        
<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI (02, 04)
  if(   (   lPosizione.getCodPosizioneGiuridica().equals("02") 
         || lPosizione.getCodPosizioneGiuridica().equals("04")
        )
     && lLuogoDetenzione.getIstitutoDetenzione() != null
    )
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
%>


<%
//================================================
// Se non ergastolo visualizzo i quantum di pena
//================================================
if(    penaresidua.getIdPenaResidua() != null 
    && !penaresidua.isErgastolo()
  )
{ %>
        <%if (!penaresidua.isQuantumReclusioneZero() ) { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
        <% } %>

        <% if ( !penaresidua.isQuantumArrestoZero() ) { %>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        <tr>
        <% } %>
<% } %>

<%
//=========================================
// Decorrenza - Tipo Pena
//=========================================
%>
    <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
      <td class="l">Data Decorrenza Pena</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if ("S".equals(penaresidua.getFlagErgastolo())) { %>
      <td class="l">Pena Detentiva</td>
      <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
      <% } else if ("D".equals(penaresidua.getFlagErgastolo())) { %>
      <td class="l">Pena Detentiva</td>
      <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
      <% } %>


<%
//==============================================================================
// Fine Pena
//==============================================================================
if(   !lPosizione.isLibero()
   || (   lFascicoloAssociato.getFlagAltraCausa()!=null 
       && lFascicoloAssociato.getFlagAltraCausa().equals("S") 
      ) 
  )
{
  if  (!penaresidua.isErgastolo() && penaresidua.getDataFine() != null ) {
    if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
    %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%></font>
          </td>
    <% } else { %>
           <td class="l">Data Fine Pena</td>
           <td class="lRosso" colspan=2>
             <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%></font>
          </td>
    <% 
    }
  } // end !penaresidua.isErgastolo()
}
%>


<%
//==============================================================================
//
//==============================================================================
%>
<% if ("N".equals(isInsProvvSorv)) {%>
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
<% } %>  
</table>

<%
//==============================================================================
// Dati dell'ordinanza o decreto
//==============================================================================
%>
<table width="100%">
  <tr>
      <td class="Titolo" colspan='8'> Dati del Provvedimento di Cessazione della Sorveglianza</td>
  </tr>
  <% if ("S".equals(isInsProvvSorv)) { %>
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
      <input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             type="text" size="4" maxlength="4" onChange="pulisciId();">
      /
      <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" 
             onkeypress="return TicTabNumField(this,event)"
             type="text" size="6" maxlength="6" onChange="pulisciId();">
    </td>
    <td class="l"> Anno / Numero Provvedimento</td>
    <td class="l">
      <input Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" 
             onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
             type="text" size="4" maxlength="4" onChange="pulisciId();">
      /
      <input Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" 
             onkeypress="return TicTabNumField(this,event)"
             type="text" size="6" maxlength="6" onChange="pulisciId();">
    </td>
  </tr>
  <tr>
    <td class="l">Ufficio Emittente</td>
    <td class="l" colspan="3">
      <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, tipoUfficioSIUS)%>
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
    <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
    <td class="l" colspan="3">
      <select title="Tipo Provvedimento"  name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
        <%=comboTipoProvvSorv%>
      </select>  
    </td>
  </tr>
      
  <tr>
    <td class="l">Oggetto Provvedimento</td>
    <td class="L" colspan="3">
      <select  Title="Codice Motivo" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
        <option value = "-"  />-
        <%=comboOggettiMDS%>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="l">Data Emissione Provvedimento</td>
    <td class="l" colspan="3">
      <font class="campo">
        <input title="Giorno Data Emissione Ordinanza" value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
        <input title="Mese Data Emissione Ordinanza"   value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
        <input title="Anno Data Emissione Ordinanza"   value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
      </font>
    </td>
  </tr>
  <% } else { %>
  <tr>
    <td class="l" width=25%>Anno / Numero Sius</td>
    <td class="l" width=20%>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
    </td>
   
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l" width=25%> Anno / Numero Decreto </td>
    <% } else { %>
    <td class="l" width=25%> Anno / Numero Ordinanza </td>
    <% } %>     

    <td class="l" width=20%>
       <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
       <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
    </td>
  </tr>
    
	<tr>
	    <td class="l">Ufficio Emittente </td>
	    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
    	<td class="l" colspan="3"> <font class="campo"> <%=descrTipoUfficio%> DI <%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
  	</tr>

  <tr>
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l">Oggetto Decreto </td>
    <% } else { %> 
    <td class="l">Oggetto Ordinanza </td>
    <% } %>    
    <td class="l" colspan="3"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
  </tr>
    
  <tr>
    <% if("02".equals(misuraalternativa.getCodTipoDecisione() )){ %>
    <td class="l">Data Emissione Decreto </td>
    <% } else { %> 
    <td class="l">Data Emissione Ordinanza </td>
    <% } %>
    <td class="l" colspan="3">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
      </font>
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd"))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"MM"))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"yyyy"))%>">
    </td>
  </tr> 
  
  <% } %>

  <tr>
    <td  class="l">Note</td>
    <td  class="L" colspan="3">
      <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2><%=StringUtils.toStringJSP(misuraalternativa.getNote(),"")%></textarea>
    </td>
  </tr>
</table>

<table width ="100%">
  <% if ("S".equals(isInsProvvSorv)) { %>
  <tr>
    <td class="l" colspan="2">
      <input type="checkbox" name="checkPenaRideterminata" onclick="VisualizzaCalcoli();"> &nbsp;&nbsp;Pena Rideterminata
    </td>
  </tr>
  
  <tr id="idTR1" style="display:none; position:relative;">
    <td class="l">Misura Alternativa Cessata dal</td>
    <td class="l" colspan="2">
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
        <input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_REVOCA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      </font>
    </td>
  </tr>
  <tr id="idTR2" style="display:none; position:relative;">
    <td class="l">Pena residua rideterminata</td>
    <td class="l" colspan="2">   RECLUSIONE
      Anni &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
      </font>
      Mesi &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
      </font>
      Giorni &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_RECLUSIONE%>" onChange="pulisciId();">
      </font>
      ARRESTO
      Anni &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_REVOCA_ARRESTO%>" onChange="pulisciId();">
      </font>
      Mesi &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_MESI_REVOCA_ARRESTO%>" onChange="pulisciId();">
      </font>
      Giorni &nbsp;
      <font class="campo">
        <input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_REVOCA_ARRESTO%>" onChange="pulisciId();">
      </font>
    </td>
  </tr>
  <% } else { %>
  
  <% } %>
</table>

<% if (!"03".equals(lPosizione.getCodPosizioneGiuridica())) { %>
  <% if ( isInsProvvSorv.equals("S") ) { %>
  <table width ="100%">
    <tr>
      <td class="l">Esecuzione contro Soggetto in Misura&nbsp;
        <input type="radio" name="tipo" value="misura" checked onclick="handleChangeRadio(this);" >&nbsp;&nbsp;
        Esecuzione contro Soggetto detenuto&nbsp;
        <input type="radio" name="tipo" value="detenuto" onclick="handleChangeRadio(this);" >
      </td>
      <td class="l">Data Ingresso in carcere &nbsp;
        <input title = "Giorno Data Ingresso in carcere" value="" type="text" size="2" maxlength="2" disabled="disabled" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" <%=IWebConstants.UTIL_DATA%> > -
        <input title = "Mese Data Ingresso in carcere"   value="" type="text" size="2" maxlength="2" disabled="disabled" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>" <%=IWebConstants.UTIL_DATA%> > -
        <input title = "Anno Data Ingresso in carcere"   value="" type="text" size="4" maxlength="4" disabled="disabled" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </td>
    </tr>
  </table>
  <% } else { %>
  <table width ="100%">
    <%
      String checkInMisura="";
      String checkDetenuto="";
      String statusData="readonly";
      if ("PROC".equals(misuraalternativa.getCodTipoUfficioScarcerazione())){
        checkInMisura = "checked";
        statusData = "disabled='disabled'"; // se esague proc disabilito i campi
      }
      else
        checkDetenuto = "checked";
    %>
    <tr>
      <td class="l">Esecuzione contro Soggetto in Misura&nbsp;
        <input type="radio" name="tipo" value="misura"  <%=checkInMisura%> disabled="disabled">&nbsp;&nbsp;
        Esecuzione contro Soggetto detenuto&nbsp;
        <input type="radio" name="tipo" value="detenuto" <%=checkDetenuto%> disabled="disabled">
      </td>
      <td class="l">Data Ingresso in carcere &nbsp;
        <input type="text" size="2" maxlength="2" title="Giorno Data Ingresso in carcere" 
               name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"dd"))%>"
               <%=statusData%> > -
        <input type="text" size="2" maxlength="2" title="Mese Data Ingresso in carcere"   
               name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>"
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"MM"))%>"
               <%=statusData%>> -
        <input type="text" size="4" maxlength="4" title="Anno Data Ingresso in carcere" 
               name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>" 
               value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"yyyy"))%>"              
               <%=statusData%>>
      </td>
    </tr>
  </table>
  <% } %>
<% } %>

<% if ( isInsProvvSorv.equals("N") && "S".equals(isPenaRideterminata) ) { %>
  <table width ="100%">
    <tr>
      <td class="Titolo" colspan="8"> Pena Residua da Espiare </td>
    </tr>
    <tr>
      <td class="l">Reclusione</td>
      <td class="l">
        Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniReclusione())%>
        </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiReclusione())%>
        </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniReclusione())%></font>
      </td>
      <td class="l" colspan="2">Arresto</td>
      <td class="l" colspan="3">
        Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniArresto())%>
        </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiArresto())%>
        </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniArresto())%></font>
      </td>
  </tr>
  
  <tr>
    <% if (nuovapenaresidua.getDataInizio() != null) { %>
    <td class="l">Data Decorrenza Pena</td>
    <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
    <% } %> 

    <% if (nuovapenaresidua.getDataFine() == null && nuovapenaresidua.getDataFinePresunta() != null){%>
    <td class="l">Data Fine Pena</td>
    <td class="L" colspan=2>
      <input type="text" size="2" maxlength="2" 
             name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "dd") )%>" 
             onFocus="javascript:textboxSelect(this)"  onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      -
      <input type="text" size="2" maxlength="2" 
             name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "MM") )%>" 
             onFocus="javascript:textboxSelect(this)"  onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      -
      <input type="text" size="4" maxlength="4" 
             name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" 
             value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFinePresunta(), "yyyy") )%>"
             onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <% } else if( nuovapenaresidua.getDataFine() != null) { %>
       <td class="l">Data Fine Pena</td>
       <% if(nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())) { %>
       <td class="L" colspan="2">
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%> </font>
       <% } else { %>
       <td class="lRosso" colspan="2">
         <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%> </font>
       <% } %>
         <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
         <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
         <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
       </td>
    <% } %>
  </tr>
</table>
<%}%>

<% if ( isInsProvvSorv.equals("N") ) { %>
<table width ="100%">
  	<tr>
   		<td class="Titolo" colspan=6>Magistrato Firmatario</td>
  	</tr>
  	<tr>
  		<%-- MEV10-s3: aggiunta larghezza campo --%>
    	<td class="l" width="30%">Magistrato Firmatario
    	<td class="L">
      		<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      		<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      		<input readonly title="Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      		<a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        		<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
  	</tr>
</table>

<table width="100%">
  <tr>
    <td class="Titolo" colspan=6>Destinatari</td>
  </tr>
</table>

<%
//==============================================================================
// Istituto di detenzione
//==============================================================================
// <div id="istituto" width="100%" style="display:block; position:relative;" >
%>
<% 
if (   "SORV".equals(misuraalternativa.getCodTipoUfficioScarcerazione())
    || "-".equals(misuraalternativa.getCodTipoUfficioScarcerazione())
    || misuraalternativa.getCodTipoUfficioScarcerazione() == null 
    || tipoCessazione.equals("SEMILIBERTA")
   ) 
{ %>
<table width="100%">
  <tr>
    <td class="l" width ="30%">Istituto di Detenzione <font class=ob>(*)</font>
      <input type="hidden" name="notificaE" value="istituto">
    </td>
    <td class="l">
    <% if(   posizioneluogoaltra != null 
          && posizioneluogoaltra.getLuogoDetenzione()!= null 
          && posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null
         )
    {%>
      <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
      <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>">
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
    <%}else {%>
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" >
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a>
    <%}%>
    </td>
  </tr>
</table>
<% } //</div>%>

<%
//==============================================================================
// Autorità esterna per l'esecuzione
//==============================================================================
//<div id="autorita" style="width: 100%; display:block; position:relative;">
%>
<% if ("PROC".equals(misuraalternativa.getCodTipoUfficioScarcerazione())) { %>
<table width ="100%">
  <tr>
    <td class="l" width ="30%">Autorità Destinazione <font class=ob>(*)</font>
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
      <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
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
<% } //</div> %>

<%
//==============================================================================
// Ufficio di Sorveglianza
//==============================================================================
//<div id="divsor" style="width: 100%; display:block;  position:relative;" >
%>
<table  width ="100%">
	<tr>
   		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
  	</tr>
  	<tr>
	  	<%-- MEV10-s3: sostitiuta stringa con combo --%>
		<%-- <td class="l" width ="30%">Ufficio di Sorveglianza <font class=ob>(*)</font></td> --%>
	    <td class="l" width ="30%">Destinatario <font class=ob>(*)</font></td>
	    <td class="L" ><%=MinorMask.comboMagistratoTrattino()%></td>
   	</tr>
   	<tr>
   		<td class="l">Sede</td>
    	<td class="L">
      		<input type="text" title="ufficio" maxlength="35" size="25" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>" 
            		value="<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%>">
      		<a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
        		<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
  	</tr>
</table>
<% //</div> %>

<%
//==============================================================================
// Avvocati
//==============================================================================
%>
<table width="100%">
  <tr>
    <td class="Titolo" colspan=6>Destinatari per Notifica</td>
  </tr>
  <tr>
    <td class="l">
      <input type="checkbox" name="checkAvvocati" onclick="VisualizzaAvvocati();"> &nbsp;&nbsp;Notifiche atti (Difensore)
    </td>
  </tr>
</table>

<div id="divavvocati" style="width: 100%; display:none; position:relative;" >
<%
  int lIdxAvv = 0;
  Iterator lItxAvv = avvocati.iterator();
  while(lItxAvv.hasNext())
  {
    AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>  
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
      <tr>
        <td class="l">Autorità Destinazione <font class=ob>(*)</font></td >
        <td class="L" colspan="3">
          <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
            <%=autoritaEsternaAvv%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede </td><td class="L">
          <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
          <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
            <img src="/images/filefolder.gif" border="0">
          </a>
        </td>
        <td class="l">Note</td>
        <td class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>" cols=35></textarea>
        </td>
     </tr>
     <tr><td>&nbsp;</td>  </tr>
   </table>
<%
    lIdxAvv++;
  }
%>
</div>
<% } %>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
    </td>
  </tr>
</table>

</FORM>


<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
  
  <% if ( isInsProvvSorv.equals("N") ) { %>
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
  <% } else { %>
  // Anno e Numero SIUS
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

  // Anno e Numero Ordinanza
  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2099");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");

  // Controlli Data Emissione Ordinanza
  //frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");

  //frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","lt=12");

  //frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");
  
  //Controlli Data Ingresso in Carcere
  <% if (!"03".equals(lPosizione.getCodPosizioneGiuridica())) { %>  
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO%>","lt=31");
  
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO%>","lt=12");
  
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO%>","lt=2099");
  <% } %>
<% } %>


 frmvalidator.setAddnlValidationFunction("Verify");
</script>



</body>
</html>