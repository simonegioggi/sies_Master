<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.Utils"%>
<%@ page import="siap.siep.util.MinorMask"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<jsp:useBean id="tipoprovvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"             scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunti useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%-- Dati del Provvedimento della Sorveglianza --%>
<jsp:useBean id="EventoSIUS"          	scope="request" class="siap.sico.evento.model.EventoModel"/> 
<jsp:useBean id="DepositoDecreto"     	scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="DepositoOrdinanzaPc" 	scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="LicenzePeriodi"      	scope="request" class="java.util.Vector"/>
<%-- MERGE v10: aggiunti useBean per il rimedio risarcitorio --%>
<jsp:useBean id="LicenzePeriodiRR"      scope="request" class="java.util.Vector"/>
<jsp:useBean id="EventoSIUSRR"         	scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="DepositoDecretoRR"     scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="DepositoOrdinanzaPcRR" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>

<%
//=============================================================================
// Totale giorni concessi e relativi periodi per il RIMEDIO RISARCITORIO
//=============================================================================
BigDecimal lNumeroGiorniRiduzioneRR = null;
String lSommaLiquidataRR = null;
String lStrPeriodi_RD_C_RR = "";
String lStrPeriodi_SL_C_RR = "";
String lStrPeriodi_RD_R_RR = "";
String lStrPeriodi_RD_I_RR = "";
String lStrPeriodi_RD_N_RR = "";

Iterator itxRR = LicenzePeriodiRR.iterator();
while (itxRR.hasNext()) {
	LicenzaPeriodiLibAnticipataModel lplamRR = (LicenzaPeriodiLibAnticipataModel) itxRR.next();
  	if ("RD".equals(lplamRR.getLicenza().getCodTipoLicenza())) {
  		if ("C".equals(lplamRR.getLicenza().getFlagConcesso())) {
  			lNumeroGiorniRiduzioneRR = lplamRR.getLicenza().getNumeroGiorni();
      		PeriodoLibAnticipataModel[] lPeriodiRR = lplamRR.getPeriodi();
      		for (int i = 0; i < lPeriodiRR.length; i++) {
        		lStrPeriodi_RD_C_RR += DateUtils.getDateToString(lPeriodiRR[i].getDataInizio(), "dd/MM/yyyy") + " - " +
                	DateUtils.getDateToString(lPeriodiRR[i].getDataFine(), "dd/MM/yyyy") + "; ";
      		}
   		} else if ("R".equals(lplamRR.getLicenza().getFlagConcesso())) {
   			PeriodoLibAnticipataModel[] lPeriodiRR = lplamRR.getPeriodi();
      		for (int i = 0; i < lPeriodiRR.length; i++) {
        		lStrPeriodi_RD_R_RR += DateUtils.getDateToString(lPeriodiRR[i].getDataInizio(),"dd/MM/yyyy") + " - " +
					DateUtils.getDateToString(lPeriodiRR[i].getDataFine(), "dd/MM/yyyy") + "; ";
      		}
    	} else if ("I".equals(lplamRR.getLicenza().getFlagConcesso())) {
    		PeriodoLibAnticipataModel[] lPeriodiRR = lplamRR.getPeriodi();
      		for (int i = 0; i < lPeriodiRR.length; i++) {
      			lStrPeriodi_RD_I_RR += DateUtils.getDateToString(lPeriodiRR[i].getDataInizio(), "dd/MM/yyyy") + " - " +
                	DateUtils.getDateToString(lPeriodiRR[i].getDataFine(), "dd/MM/yyyy") + "; ";
      		}
    	} else if ("N".equals(lplamRR.getLicenza().getFlagConcesso())) {
      		PeriodoLibAnticipataModel[] lPeriodiRR = lplamRR.getPeriodi();
      		for (int i = 0; i < lPeriodiRR.length; i++) {
      			lStrPeriodi_RD_N_RR += DateUtils.getDateToString(lPeriodiRR[i].getDataInizio(), "dd/MM/yyyy") + " - " +
                	DateUtils.getDateToString(lPeriodiRR[i].getDataFine(), "dd/MM/yyyy") + "; ";
      		}
    	}
  	} else if ("SL".equals(lplamRR.getLicenza().getCodTipoLicenza())) {
  		lSommaLiquidataRR = StringUtils.toEuroFormat(lplamRR.getLicenza().getSommaRisarcDanni());
    	PeriodoLibAnticipataModel[] lPeriodiRR = lplamRR.getPeriodi();
    	for (int i = 0; i < lPeriodiRR.length; i++) {
      		lStrPeriodi_SL_C_RR += DateUtils.getDateToString(lPeriodiRR[i].getDataInizio(), "dd/MM/yyyy") + " - " + 
				DateUtils.getDateToString(lPeriodiRR[i].getDataFine(), "dd/MM/yyyy") + "; ";
   		}
  	}
}
%>
<%-- FINE MERGE v10 --%>

<%
//==============================================================================
// form per l'inserimento del Provvedimento SIUS di Concessione Rimedi Risarcitori
// DL82/2014
//
// La form consente di selezionare dalla lista il decreto/ordinanza SIUS se
// depositata oppure di inserire manualmente i dati simulando il provvedimento
// SIUS
//
// menu: 'Decisioni della sorveglianza - Altre Decisioni - Rimedi Risarcitori D.L. 26 giugno 2014,n. 92'
//
//==============================================================================
// Dati per la costruzione delle DIV
// Sono presenti 5 DIV con i periodi DAL - AL con 10 periodi per tipologia
// DIV_RD_C = Giorni di riduzione concessi
// DIV_SL_C = Somme Liquidate concesse

// DIV_RD_R = Giorni di riduzione Rigettati
// DIV_RD_I = Giorni di riduzione Inammissibili
// DIV_RD_N = Giorni di riduzione NLP/NDP

String [] lNomiDivPeriodi = {"DIV_RD_C","DIV_SL_C","DIV_RD_R","DIV_RD_I","DIV_RD_N"};
int lNumPeriodi = 10; // numero di periodi (DAL-AL) previsti per ogni div

BigDecimal idFascicoloSiep = null;
FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) request.getSession().getAttribute("fascicolo");
if (lFascicoloModel!=null)
  idFascicoloSiep = lFascicoloModel.getIdFascicoloSiep();

//===========================================================
// Totale giorni concessi e relativi periodi
//===========================================================
BigDecimal lNumeroGiorniRiduzione = null;
String lSommaLiquidata = null;

String lStrPeriodi_RD_C = "";
String lStrPeriodi_SL_C = "";
String lStrPeriodi_RD_R = "";
String lStrPeriodi_RD_I = "";
String lStrPeriodi_RD_N = "";

Iterator itx = LicenzePeriodi.iterator();
while (itx.hasNext())
{
  LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
  
  if( "RD".equals(lLicPer.getLicenza().getCodTipoLicenza())){
    if ("C".equals(lLicPer.getLicenza().getFlagConcesso())){
      lNumeroGiorniRiduzione = lLicPer.getLicenza().getNumeroGiorni();
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_C += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
    else if ("R".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_R += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
    else if ("I".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_I += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }   
    }
    else if ("N".equals(lLicPer.getLicenza().getFlagConcesso())){
      PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
      for (int i=0;i<lPeriodi.length;i++){
        lStrPeriodi_RD_N += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy")+" - "+
                            DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy") +"; ";
      }
    }
  }
  else if( "SL".equals(lLicPer.getLicenza().getCodTipoLicenza())){
    lSommaLiquidata = StringUtils.toEuroFormat(lLicPer.getLicenza().getSommaRisarcDanni());

    PeriodoLibAnticipataModel[] lPeriodi = lLicPer.getPeriodi();
    for (int i=0;i<lPeriodi.length;i++){
      lStrPeriodi_SL_C += DateUtils.getDateToString(lPeriodi[i].getDataInizio(),"dd/MM/yyyy") +" - "+ 
                          DateUtils.getDateToString(lPeriodi[i].getDataFine(),"dd/MM/yyyy")+"; ";
    }
  }
}
%>

<html>
<head>
  <title> [S.I.E.S.] - Reclamo Rimedi Risarcitori D.L. 92/2014 </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    var lNumPeriodi = <%=lNumPeriodi%>;
    var lNomiDivPeriodi = ["DIV_RD_C", "DIV_SL_C", "DIV_RD_R","DIV_RD_I","DIV_RD_N"];
  
    function Inizia()
    {
      // per ora non fa nulla
      return;
    }
    
    function ListaProvvedimentiConcessione(a_formname){
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaReclamoRimediRisarcitori&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=idFascicoloSiep%>"
                            , "Lista_Recalmo_Rimedi_Risarcitori"
                            , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=600, height=500");
 
      return;
    }
    
    // Seleziona gli uffici in base alla tipologia
    function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==========================================================
    // Funzione che visualizza o nasconde le DIV con i periodi
    //==========================================================
    function ViewLayer(Object, idDiv)
    {
      if (Object.checked){
        // ho selezionato un check, devo chiudere eventuali DIV aperte e 
        // visualizzare la DIV corrispondente al check selezionato.
        for (i=0;i<lNomiDivPeriodi.length;i++){
          if (lNomiDivPeriodi[i]!=idDiv){
            // Disabilito i check
            var idCheck = "check_"+lNomiDivPeriodi[i];
            document.getElementById(idCheck).disabled = true;
          }
        }
        // visualizzo la DIV 
        document.getElementById(idDiv).style.display='block';
      }
      else {
        // ho deselezionato un check, devo chiudere la DIV corrispondente e 
        // riabilitare i check
        if (chiudiDivPeriodi(idDiv)==true){
          for (i=0;i<lNomiDivPeriodi.length;i++){
            if (lNomiDivPeriodi[i]!=idDiv){
              var idCheck = "check_"+lNomiDivPeriodi[i];
              document.getElementById(idCheck).disabled = false;
            }
          }
        }
        else {
          Object.checked=true; // dati errati nella DIV rimetto il segno di spunta;
        }
      }
    }
    
    //==========================================================================
    // Chiude una DIV effettuando i controlli sui dati in essa presenti
    //==========================================================================
    function chiudiDivPeriodi(idDiv){
      // prima di nascondere la DIV va verificato se i dati inseriti sono 
      // corretti. In caso negativo si deve inviare alert, non chiudere la DIV
      // e restituire false
      // Se i dati sono corretti va chiusa la DIV e modificato il colore del 
      // check corrispondente: rosso se la DIV contiene dati.
      var numPeriodiInseriti = 0;
      var lNomeCheck = "check_"+idDiv+"_label";
       
      var dataOdierna = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
       
      for (i=0; i<lNumPeriodi; i++){
        // data dal
        var lNomeCampoGG_Dal   = "<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Dal   = "<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Dal = "<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;

        var lNomeCampoGG_Al   = "<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Al   = "<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Al = "<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>"+"_"+idDiv+"_"+i;

        if (document.getElementById(lNomeCampoGG_Dal).value.length==1)
          document.getElementById(lNomeCampoGG_Dal).value='0'+document.getElementById(lNomeCampoGG_Dal).value;
        if (document.getElementById(lNomeCampoMM_Dal).value.length==1)
          document.getElementById(lNomeCampoMM_Dal).value='0'+document.getElementById(lNomeCampoMM_Dal).value;
          
        var data_dal = document.getElementById(lNomeCampoGG_Dal).value
                  +'/'+document.getElementById(lNomeCampoMM_Dal).value
                  +'/'+document.getElementById(lNomeCampoAAAA_Dal).value;
                  
        //alert("data_dal = "+data_dal);
        if (!ControllaDataPassaVuota(data_dal))
        {
          alert('Data inizio periodo '+data_dal+' non valida');
          document.getElementById(lNomeCampoGG_Dal).focus();
          return false;
        } 
        
        if (document.getElementById(lNomeCampoGG_Al).value.length==1)
          document.getElementById(lNomeCampoGG_Al).value='0'+document.getElementById(lNomeCampoGG_Al).value;
        if (document.getElementById(lNomeCampoMM_Al).value.length==1)
          document.getElementById(lNomeCampoMM_Al).value='0'+document.getElementById(lNomeCampoMM_Al).value;
          
        var data_al = document.getElementById(lNomeCampoGG_Al).value
                 +'/'+document.getElementById(lNomeCampoMM_Al).value
                 +'/'+document.getElementById(lNomeCampoAAAA_Al).value;
                 
        if (!ControllaDataPassaVuota(data_al))
        {
          alert('Data fine periodo '+data_al+' non valida');
          document.getElementById(lNomeCampoGG_Al).focus();
          return false;
        } 
        
        if (data_dal!='//' && data_al!='//')
        {        
          if (CompareDate(data_dal,dataOdierna)== false) {
            alert ("Data inizio periodo non può essere una data futura");
            document.getElementById(lNomeCampoGG_Dal).focus();
            return false;          
          }
          if (CompareDate(data_al,dataOdierna)== false) {
            alert ("Data fine periodo non può essere una data futura");
            document.getElementById(lNomeCampoGG_Al).focus();
            return false;          
          }
        
          if (CompareDate(data_dal,data_al)== false) {
            alert ("Data di Fine minore di Data inizio periodo");
            document.getElementById(lNomeCampoGG_Al).focus();
            return false;
          }
          //else if (CompareDate(dataFine, data_emissione)== false){
          //  alert ("Data di Fine periodo maggiore di Data emissione");
          //  document.getElementById(lNomeCampoGG_Dal).focus();
          //}
          else {
            numPeriodiInseriti++;
          }
        }
        else if (data_dal!='//' && data_al=='//')
        {
          alert ("Data di fine periodo mancante");
          document.getElementById(lNomeCampoGG_Al).focus();
          return false;
        }
        else if (data_dal=='//' && data_al!='//')
        {
          alert ("Data di inizio periodo mancante");
          document.getElementById(lNomeCampoGG_Dal).focus();
          return false;
        }
      }

      if (numPeriodiInseriti>0){
        // setto rosso il check
        document.getElementById(lNomeCheck).style.color="Red";
      }
      else {
        document.getElementById(lNomeCheck).style.color="Navy";
      }
      
      // chiudo la DIV
      document.getElementById(idDiv).style.display='none';
      
      return true;
    }
    
    //==================================================
    // Verifica la presenza di dati in una DIV periodi
    //==================================================
    function checkPresenzaPeriodi(idDiv){
      for (i=0; i<lNumPeriodi; i++){
        // data dal
        var lNomeCampoGG_Dal   = "<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Dal   = "<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Dal = "<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>"+"_"+idDiv+"_"+i;

        var lNomeCampoGG_Al   = "<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoMM_Al   = "<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>"+"_"+idDiv+"_"+i;
        var lNomeCampoAAAA_Al = "<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>"+"_"+idDiv+"_"+i;
        
        if(   document.getElementById(lNomeCampoGG_Dal).value.length>0
           || document.getElementById(lNomeCampoMM_Dal).value.length>0
           || document.getElementById(lNomeCampoAAAA_Dal).value.length>0
           || document.getElementById(lNomeCampoGG_Al).value.length>0
           || document.getElementById(lNomeCampoMM_Al).value.length>0
           || document.getElementById(lNomeCampoAAAA_Al).value.length>0
          )
        {
          return true;
        }
      }
      
      return false;
    }
    
    //=================================
    //
    //=================================
    function Verify()
    {      
      //=======================================================
      // Controllo sui campi del Provvedimento Sorveglianza
      //=======================================================
      // Data Emissione Provvedimento      
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (data_to_verify=='//' )
      {
        alert('Indicare la Data di emissione Provvedimento');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione Provvedimento non valida');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }      
      
      // Anno e numero Provvedimento
      if ( document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value.length==0)
      {
        alert('Anno Provvedimento obbligatorio');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.focus();
        return false;
      }
      
      if ( document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value.length==0)
      {
        alert('Numero Provvedimento obbligatorio');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.focus();
        return false;
      }   
      
      // Tipo provvedimento
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex==0)
      {        
        alert("Selezionare Tipo Provvedimento");
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
        return false;
      }  
      
      // Anno e numero SIUS
      if ( document.ReclamoRimediRisarcitori.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>.value.length==0)
      {
        alert('Anno SIUS obbligatorio');
        document.ReclamoRimediRisarcitori.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>.focus();
        return false;
      }
      
      if ( document.ReclamoRimediRisarcitori.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>.value.length==0)
      {
        alert('Numero SIUS obbligatorio');
        document.ReclamoRimediRisarcitori.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>.focus();
        return false;
      }     
      
      // Autorità emittente 
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex==0)
      {        
        alert("Selezionare Autorità Emittente");
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
        return false;
      }
      
      // Sede 
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.value=="")
      {
        alert("Selezionare Sede Autorità Emittente");
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
      }
      
      
      // Data Ricezione Provv AA
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value.length==1)
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value='0'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value;
      if (document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value.length==1)
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value='0'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value;

      var data_to_verify = document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.value+'/'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>.value+'/'+document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>.value;
      if (data_to_verify=='//' )
      {
        alert('Indicare la Data di ricezione Provvedimento');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
        return false;
      }      
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di ricezione Provvedimento non valida');
        document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>.focus();
        return false;
      }

      //====================================================
      // Controllo che sia stato specificato almeno un dato
      //====================================================
      // E' obbligatorio inserire almeno uno tra i RD_C e SL_C, non è possibile
      // inserire solo i periodi NON concessi (non ha senso per SIEP)
      // Se sono stati inseriti i GG_RD sono obbligatori i periodi e VV
      // Se sono stati inseriti i SL sono obbligatori i periodi e VV
      
      // Se ci sono DIV aperte procedo alla chiusura per attivare i controlli
      for (i=0;i<lNomiDivPeriodi.length;i++){
        var idCheck = "check_"+lNomiDivPeriodi[i];
        if (document.getElementById(idCheck).checked==true){
          //alert("E' presente una DIV aperta la chiudo");
          if (chiudiDivPeriodi(lNomiDivPeriodi[i])==false){
            //alert("Errore nei dati della DIV");
            return false;
          }
          else {
            //alert("Div chiusa tolgo il check");
            document.getElementById(idCheck).checked=false;
            document.getElementById(idCheck).onclick();
          }
        }
      }
      
      var isPeriodi_RD_C_presenti = checkPresenzaPeriodi('DIV_RD_C');
      var isPeriodi_SL_C_presenti = checkPresenzaPeriodi('DIV_SL_C');
      //var isPeriodi_RD_R_presenti = checkPresenzaPeriodi('DIV_RD_R');
      //var isPeriodi_RD_I_presenti = checkPresenzaPeriodi('DIV_RD_I');
      //var isPeriodi_RD_N_presenti = checkPresenzaPeriodi('DIV_RD_N');
      
      var totGG_RD_presenti = true;
      if (   document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.value.length==0
          || parseInt(document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>.value)==0
         )
      {
        //alert("Non sono stati inseriti giorni");
        totGG_RD_presenti = false;
      }

      var tot_SL_presente = true;
      if (  (   document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.value.length==0
             || parseInt(document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>.value)==0
            )
          &&(   document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU%>.value.length==0
             || parseInt(document.ReclamoRimediRisarcitori.<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU%>.value)==0
            )
         )
      {
        //alert("Non è stata inserita a somma liquidata");
        tot_SL_presente = false;
      }
      
      if (!totGG_RD_presenti && !tot_SL_presente){
        alert("Attenzione non sono stati indicati ne i giorni di riduzione ne la somma liquidata a titolo risarcitorio. Almeno uno dei due dati è obbligatorio.")
        return false;
      }
      
      if (totGG_RD_presenti && !isPeriodi_RD_C_presenti){
        alert("Sono stati indicati i giorni di riduzione pena ma non i relativi periodi di computo");
        return false;
      }
      if ( !totGG_RD_presenti && isPeriodi_RD_C_presenti){
        alert("Sono stati indicati i periodi di computo dei giorni di riduzione ma non il totale di giorni concessi");
        return false;
      }
      
      if (tot_SL_presente && !isPeriodi_SL_C_presenti){
        alert("E' stata indicata la somma liquidata ma non i relativi periodi di computo");
        return false;
      }
      if (!tot_SL_presente && isPeriodi_SL_C_presenti){
        alert("Sono stati indicati i periodi di computo ma non la corrispondente somma liquidata");
        return false;
      }
      return true;
    }
  </script>
</head>

<body class="corpo" onLoad="Javascript:Inizia();">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Emissione Provvedimento Reclamo Rimedi Risarcitori D.L. 92/2014</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<!-- ============================================================================== -->
<!--                  Dati del Provvedimento dela Sorveglianza  -->
<!-- ============================================================================== -->
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="ReclamoRimediRisarcitori">
	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActInserisciReclamoRimediRisarcitori">
	<input type="hidden" name="NumPeriodi" value="<%=lNumPeriodi%>">

  	<table style="width: 95%;">
    	<tr>
      		<td class="titolo">Provvedimento di Concessione della Sorveglianza</td>
    	</tr>
	</table>

	<!-- MERGE v10: aggiunta sezione per il rimedio risarcitorio -->
	<% 
	if (!LicenzePeriodiRR.isEmpty()) {
	%>
		<table style="width: 95%;">
			<%
      		LicenzaPeriodiLibAnticipataModel lplam =
      			(LicenzaPeriodiLibAnticipataModel) LicenzePeriodiRR.firstElement();
			%>
      		<tr><td>&nbsp;</td></tr>
     	 	<tr>
        		<td class="L" width="30%">
          			Anno / Numero SIUS
        		</td>
        		<td class="L">
          			<font class="campo">
            			<%=StringUtils.toStringJSP(lplam.getLicenza().getAnnoSius())%>&nbsp;
          			</font>
          			/
          			<font class="campo">
            			<%=StringUtils.toStringJSP(lplam.getLicenza().getNumeroSius())%>&nbsp;
          			</font>
        		</td>
      		</tr>
      		<tr>
        		<td class="L">
          			Anno / Numero
          			<% if ("02".equals(EventoSIUSRR.getCodTipoProvvedimento())) { %>
          				Decreto
          			<% } else { %>
          				Ordinanza
          			<% } %>
        		</td>
        		<%
		        String lAnnoProvvedimentoRR = "";
		        String lNumeroProvvedimentoRR = "";
        		if (DepositoDecretoRR != null && DepositoDecretoRR.getIdDepositoDecreto() != null) {
          			lAnnoProvvedimentoRR   = "" + DepositoDecretoRR.getAnnoS72();
          			lNumeroProvvedimentoRR = "" + DepositoDecretoRR.getNumS72();
       			} else if (DepositoOrdinanzaPcRR != null && DepositoOrdinanzaPcRR.getIdDepositoOrdinanzaPc() != null) {
		          	lAnnoProvvedimentoRR   = "" + DepositoOrdinanzaPcRR.getAnnoS3();
		          	lNumeroProvvedimentoRR = "" + DepositoOrdinanzaPcRR.getNumS3();
        		}
        		%>
        		<td class="L">
          			<font class="campo">
            			<%=StringUtils.toStringJSP(lAnnoProvvedimentoRR)%>&nbsp;
          			</font>
          			/
          			<font class="campo">
            			<%=StringUtils.toStringJSP(lNumeroProvvedimentoRR)%>&nbsp;
          			</font>
        		</td>
      		</tr>
			<tr>
				<td class="l">Autorità emittente</td>
				<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    		<%
	    		String descrTipoUfficio = StringUtils.toStringJSP(EventoSIUSRR.getDescrUfficioEmittente());
	    		if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    				"UDSM".equals(EventoSIUSRR.getCodTipoUfficioEmittente())) {
	    			descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    		}
	    		%>
        		<td class="l">
		          	<font class="campo">
		            	<%=descrTipoUfficio%>&nbsp;
		          	</font>
          			di&nbsp;
		          	<font class="campo">
		            	<%=StringUtils.toStringJSP(EventoSIUSRR.getDescrLuogoEmittente())%>
		          	</font>
				</td>
			</tr>
      		<tr>
        		<td class="L">Data Emissione</td>
        		<td class="L">
			        <%
			        Date lDataEmissioneRR = null;
			        if (DepositoDecretoRR != null && DepositoDecretoRR.getIdDepositoDecreto() != null)
			          	lDataEmissioneRR = DepositoDecretoRR.getDataEmissione();
			        else if (DepositoOrdinanzaPcRR != null && DepositoOrdinanzaPcRR.getIdDepositoOrdinanzaPc() != null)
			         	lDataEmissioneRR = DepositoOrdinanzaPcRR.getDataCameraConsiglio();
			        %>
	          		<font class="campo">
	            		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataEmissioneRR, "dd-MM-yyyy"))%>
	          		</font>
        		</td>
      		</tr>
  			<tr><td>&nbsp;</td></tr>
  			<% if (Utils.isPresent(StringUtils.toStringJSP(lNumeroGiorniRiduzioneRR, ""))) { %>
    		<tr>
      			<td class="L">Totale giorni riduzione pena concessi:</td>
      			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(lNumeroGiorniRiduzioneRR, "")%>
      				</font>
      			</td>
		    </tr>
		    <tr>
		      	<td class="L">Periodi valutati per riduzione pena:</td>
		      	<td class="L">
		      		<font class="campo">
		      			<%=StringUtils.toStringJSP(lStrPeriodi_RD_C_RR, "")%>
		      		</font>
		      	</td>
    		</tr>
    		<% }
    		if (Utils.isPresent(StringUtils.toStringJSP(lSommaLiquidataRR, ""))) { %>
   			<tr>
      			<td class="L">Somma liquidata a titolo risarcimento danno:</td>
      			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(lSommaLiquidataRR, "")%>
      				</font> &euro;
      			</td>
    		</tr>
    		<tr>
      			<td class="L">Periodi valutati per liquidazione somma:</td>
      			<td class="L">
      				<font class="campo">
      					<%=StringUtils.toStringJSP(lStrPeriodi_SL_C_RR, "")%>
      				</font>
      			</td>
    		</tr>
    		<% }
			if (Utils.isPresent(StringUtils.toStringJSP(lStrPeriodi_RD_R_RR, ""))) { %>
	   			<tr>
	      			<td class="Titolo" colspan="2">Periodi non concessi Rigettati:</td>
	    		</tr>
	    		<tr>
	      			<td class="L" colspan="2">
	      				<font class="campo">
	      					<%=StringUtils.toStringJSP(lStrPeriodi_RD_R_RR, "")%>
	      				</font>
	      			</td>
	    		</tr>
		  	<% }
			if (Utils.isPresent(StringUtils.toStringJSP(lStrPeriodi_RD_I_RR, ""))) { %>
	   			<tr>
	      			<td class="Titolo" colspan="2">Periodi non concessi Inammissibili:</td>
	    		</tr>
	    		<tr>
	      			<td class="L" colspan="2">
	      				<font class="campo">
	      					<%=StringUtils.toStringJSP(lStrPeriodi_RD_I_RR, "")%>
	      				</font>
	      			</td>
	    		</tr>
		  	<%
		  	}
			if (Utils.isPresent(StringUtils.toStringJSP(lStrPeriodi_RD_N_RR, ""))) { %>
	    		<tr>
	      			<td class="Titolo" colspan="2">Periodi non concessi N.L.P./N.D.P.:</td>
	    		</tr>
	    		<tr>
	      			<td class="L" colspan="2">
	      				<font class="campo">
	      					<%=StringUtils.toStringJSP(lStrPeriodi_RD_N_RR, "")%>
	      				</font>
	      			</td>
	    		</tr>
		  	<%
		  	}
			%>
		</table>
	<%
	} else {
	%>
		<table style="width: 95%;">
			<tr>
      			<td class="l">Nessun Provvedimento di Concessione della Sorveglianza presente.</td>
    		</tr>
		</table>
	<% } %>
 		<br>
   	<!-- FINE MERGE v10 -->

	<table style="width: 95%;">
    	<tr>
      		<td class="l" colspan="4">
        		<a href="Javascript:ListaProvvedimentiConcessione('ReclamoRimediRisarcitori');">
          			Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
          		</a>
      		</td>
    	</tr>

<%
	if (!LicenzePeriodi.isEmpty()) {
		LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel) LicenzePeriodi.firstElement();
      	Date lDataEmissione = null;
      	if (DepositoDecreto != null && DepositoDecreto.getIdDepositoDecreto() != null) {
        	lDataEmissione = DepositoDecreto.getDataEmissione();
      	} else if (DepositoOrdinanzaPc != null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc() != null) {
        	lDataEmissione = DepositoOrdinanzaPc.getDataCameraConsiglio(); 
      	}
%>
	    <tr>
	      <td class="l">Data emissione provvedimento</td>
	      <td class="l">
	        <input type="text" Title="Giorno Emissione provvedimento"
	               name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2"
	               value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataEmissione,"dd")) %>"
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Mese Emissione provvedimento"  
	               name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"   maxlength="2" size="2" 
	               value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataEmissione,"MM")) %>"
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Anno Emissione provvedimento" 
	               name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>"   maxlength="4" size="4" 
	               value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataEmissione,"yyyy")) %>"
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	      </td>
<%
        String lAnnoProvvedimento = "";
        String lNumeroProvvedimento = "";
        
        if (DepositoDecreto!=null && DepositoDecreto.getIdDepositoDecreto()!=null){
          lAnnoProvvedimento   = ""+DepositoDecreto.getAnnoS72();
          lNumeroProvvedimento = ""+DepositoDecreto.getNumS72();
        }
        else if (DepositoOrdinanzaPc!=null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc()!=null){
          lAnnoProvvedimento   = ""+DepositoOrdinanzaPc.getAnnoS3();
          lNumeroProvvedimento = ""+DepositoOrdinanzaPc.getNumS3();
        }
%>

	      <td class="l">Anno / Numero Provvedimento</td>
	      <td class="l">
	         <input Title="Anno Provvedimento"   value="<%=StringUtils.toStringJSP(lAnnoProvvedimento)%>" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
	         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(lNumeroProvvedimento)%>" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
	      </td>
	    </tr>   

	    <tr>
	      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
	      <td class="l">
	        <select Title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
	          <%=tipoprovvedimento%>
	        </select>
	      </td>
	      <td class="l">Anno / Numero SIUS</td>
	      <td class="l">
	        <input Title="Anno Fascicolo Sius" value="<%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoSius())%>" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
	        /
	        <input Title="Numero Sius" value="<%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroSius())%>" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>" type="text" size="6" maxlength="6">
	      </td>
	    </tr>    

		<tr>
			<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
	      	<td class="l">
	      		<!-- MERGE v10: modificato codice : spostato il controllo nella action corrispondente -->
				<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
<%-- 			    <% --%>
<%-- 			    	if ("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) {--%>
<%-- 			    %> --%>
<%-- 		        	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE, autorita)%> --%>
<%-- 		        <% } else { %> --%>
		        <select Title="Autorità Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>">
					<%=autorita%>
		        </select>
<%-- 			    <% } %> --%>
	      	</td>
	
	      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
	        <input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35" value="<%=StringUtils.toStringJSP(EventoSIUS.getDescrLuogoEmittente())%>">
	        <a href="Javascript:ListaUfficiComuni('ReclamoRimediRisarcitori','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
	          <img src="/images/filefolder.gif" border="0">
	        </a>
	      </td>
	    </tr>

	    <tr>
	      <td class="l">Data ricezione provvedimento</td>
	      <td class="l" colspan="3">
	        <input type="text" Title="Giorno Ricezione" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
	               value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "dd")%>" 
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Mese Ricezione" name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
	               value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "MM")%>"
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Anno Ricezione" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>" maxlength="4" size="4"  
	               value="<%=DateUtils.getDateToString(DateUtils.getSysDate(), "yyyy")%>"
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	      </td>
	    </tr>
	    
	  <br>

    	<tr>
      		<td colspan=4 class="titolo">Oggetto: Reclamo Rimedi Risarcitori D.L.26 giugno 2014 n.92</td>
    	</tr>

	    <% if (lNumeroGiorniRiduzione!=null) { %>
		    <tr>
		      <td class="L" nowrap> Totale giorni riduzione pena concessi: </td>
		      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lNumeroGiorniRiduzione,"")%></font></td>
		    </tr>
		<% } %>
	    
	    <% if (lStrPeriodi_RD_C!=null && lStrPeriodi_RD_C.length()>0) { %>
		    <tr>
		      <td class="L" nowrap> Periodi valutati per riduzione pena: </td>
		      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_C,"")%> </font></td>
		    </tr>
		<% } %>
	    <% if (lSommaLiquidata!=null && lSommaLiquidata.length()>0) { %>
		    <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
		    <tr>
		      <td class="L" nowrap> Somma liquidata a titolo risarcimento danno: </td>
		      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lSommaLiquidata,"")%> </font> &euro;</td>
		    </tr>
		    <tr>
		      <td class="L" nowrap> Periodi valutati per liquidazione somma: </td>
		      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_SL_C,"")%> </font></td>
		    </tr>
	    <% } %>
	    <!-- IdEvento del Provvedimento di Sorveglianza (SIUS) selezionato dalla lista (pop-up) -->
	    <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=EventoSIUS.getIdEvento() %>">
<%
    } else {
%>    
	    <!-- IdEvento viene valorizzato nella action 'ActInserisciReclamoRimediRisarcitori' dopo aver eseguito
	    	 l'inserimento dei dati valorizzati nel form -->                                                                                                        
  		<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="">
	    
	    <tr>
	      <td class="l">Data emissione provvedimento</td>
	      <td class="l">
	        <input type="text" Title="Giorno Emissione provvedimento" value="" 
	               name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" 
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Mese Emissione provvedimento" value=""   
	               name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"   maxlength="2" size="2" 
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Anno Emissione provvedimento" value="" 
	               name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	      </td>
	      <td class="l">Anno / Numero Provvedimento</td>
	      <td class="l">
	         <input Title="Anno Provvedimento"   value="" name="<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>"  type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
	         <input Title="Numero Provvedimento" value="" name="<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>" type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
	      </td>
	    </tr>   
	    
	    <tr>
	      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
	      <td class="l">
	        <select Title="Tipo Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" >
	          <%=tipoprovvedimento%>
	        </select>
	      </td>
	      <td class="l">Anno / Numero SIUS</td>
	      <td class="l">
	        <input Title="Anno Fascicolo Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>" type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
	        /
	        <input Title="Numero Sius" value="" name="<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>" type="text" size="6" maxlength="6">
	      </td>
	    </tr>    
	
		<tr>
			<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
	      	<td class="l">
				<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
			    <%
			    	if ("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) {
			    %>
		        	<%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "", ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE, autorita)%>
		        <% } else { %>
			        <select Title="Autorità Emittente" name="<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>">
			          <%=autorita%>
			        </select>
			    <% } %>
	      	</td>
	
	      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
	        <input title="Sede Autorita"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
	        <a href="Javascript:ListaUfficiComuni('ReclamoRimediRisarcitori','<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>',document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>[document.ReclamoRimediRisarcitori.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
	          <img src="/images/filefolder.gif" border="0">
	        </a>
	      </td>
	    </tr>
	    <tr>
	      <td class="l">Data ricezione provvedimento</td>
	      <td class="l" colspan="3">
	        <input type="text" Title="Giorno Ricezione" value="" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Mese Ricezione" value=""   name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        /
	        <input type="text" Title="Anno Ricezione" value="" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>" maxlength="4" size="4"  
	               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	      </td>
	    </tr>
  </table>
  
  <br>
  
  <%
  // DIV di contenimento per allineare le DIV dei periodi
  %>
  <div id="contenitore" style="position: relative; top: 0; left: 0;" >

  
  <table>
    <tr>
      <td class="l">Riduzione Pena: Totale giorni riduzione concessi</td>
      <td class="l" style="text-align:right">
        <input type="text" value="" style="text-align: right;" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_NUM_GIORNI_CEDU%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">&nbsp;<span id="check_DIV_RD_C_label" style="color=navy;font-weight:bold;">in relazione ai periodi</span>&nbsp;
        <input type="checkbox" id="check_DIV_RD_C" name="check_DIV_RD_C" 
               value="1" onclick="Javascript:ViewLayer(this,'DIV_RD_C');">
      </td>
    </tr>
    <tr>
      <td class="l">Somma liquidata a titolo risarcimento danno Euro</td>
      <td class="l">
        <input type="text" value="" style="text-align: right;" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_INTERO_IMPORTO_CEDU%>" maxlength="5" size="5"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        ,
        <input type="text" value="" name="<%=ICostantiDepositoOrdinanzaPc.CAMPO_DECIMALE_IMPORTO_CEDU%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
      <td class="l">&nbsp;<span id="check_DIV_SL_C_label" style="color=navy;font-weight:bold;">in relazione ai periodi</span>&nbsp;
        <input type="checkbox" id="check_DIV_SL_C" name="check_DIV_SL_C" 
               value="1" onclick="Javascript:ViewLayer(this,'DIV_SL_C');">
      </td>
    </tr>
  </table>
  <br>
  <table cellspacing="2" cellpadding="2" width="40%">
    <tr>
      <td class="Titolo" colspan=6> Periodi NON concessi&nbsp;&nbsp;&nbsp; </td>
    </tr>
    <tr>
      <td class="l" width="10%" nowrap><span id="check_DIV_RD_R_label" style="color=navy;font-weight:bold;">Rigettati</span>
        <input type="checkbox" id="check_DIV_RD_R" name="check_DIV_RD_R" value="1" 
               onclick="Javascript:ViewLayer(this,'DIV_RD_R');">
      </td>
      <td class="l" width="10%" nowrap><span id="check_DIV_RD_I_label" style="color=navy;font-weight:bold;">Inammissibili</span>
        <input type="checkbox" id="check_DIV_RD_I" name="check_DIV_RD_I" value="1" 
               onclick="Javascript:ViewLayer(this,'DIV_RD_I');">
      </td>
      <td class="l" width="10%" nowrap><span id="check_DIV_RD_N_label" style="color=navy;font-weight:bold;">N.L.P./N.D.P.</span>
        <input type="checkbox" id="check_DIV_RD_N" name="check_DIV_RD_N" value="1" 
               onclick="Javascript:ViewLayer(this,'DIV_RD_N');">
      </td>
    </tr>

<%  } // else di if(!LicenzePeriodi.isEmpty()) %> 
  </table>
  <br>
  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <INPUT class="bottone" type="submit" value="Conferma" name="bottConferma">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
  <%
  //============================================================================
  // Costruzione delle 5 DIV con i periodi
  //============================================================================
  %>
  <%
  String topOffSetDIV = "-175";  // 
  String leftOffSetDIV = "620";  //
  
  for (int i=0; i<lNomiDivPeriodi.length; i++)
  {
  %>
  <div id="<%=lNomiDivPeriodi[i]%>" style="position: relative; top: <%=topOffSetDIV%>; left: <%=leftOffSetDIV%>; display:none;" >
    <table>
    <%
    // COSTRUZIONE DEI CAMPI DATA DAL - AL
    for (int k=0; k<lNumPeriodi; k++)
    {
    %>
      <tr>
        <td class="l">
          Dal
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_INIZIO%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
                &nbsp;&nbsp;
          Al
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_GIORNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="2" size="2" value="" <%=IWebConstants.UTIL_DATA%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_MESE_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
          /
          <input type="text" maxlength="4" size="4" value="" <%=IWebConstants.UTIL_DATA_ANNO%>
                 id="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" 
                 name="<%=ICostantiLibertaAnticipata.CAMPO_ANNO_DATA_FINE%>_<%=lNomiDivPeriodi[i]%>_<%=k%>" >
        </td>
      </tr>
    <%
    }  // chiude ciclo for (int k=0; k<NumDate; k++)
    %>
    </table>
  </div>  
  <% } // end for NumCheck %>
  </div> <%// end div contenitore%>
		</form>
		<script language="JavaScript" type="text/javascript">
			var frmvalidator  = new Validator("ReclamoRimediRisarcitori");
			// Attivare le funzione di controllo
			frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>","gt=2000");
			frmvalidator.addValidation("<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>","lt=<%=DateUtils.getSysDate("yyyy")%>");
			frmvalidator.addValidation("<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>","gt=2000");
			frmvalidator.addValidation("<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>","lt=<%=DateUtils.getSysDate("yyyy")%>");
			frmvalidator.setAddnlValidationFunction("Verify");
		</script>
	</body>
</html>