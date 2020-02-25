<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.html.Option"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>

<%// Pena residua corrente%>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%// Reati%>
<jsp:useBean id="reati"         scope="request" class="java.util.Vector" /> <% // Reati sul fascicolo %>
<jsp:useBean id="ReaAntEffetti" scope="request" class="java.util.Vector" /> <% // Richieste con anticipazione %>
<jsp:useBean id="ReaRichiesti"  scope="request" class="java.util.Vector" /> <% // Richieste senza anticipazione %>
<jsp:useBean id="RichiesteAlGE1" scope="request" class="java.util.Vector" />

<%// bean valorizzati se provengo da 'aggiungi' ed è presente già a sistema una annotazione %>
<jsp:useBean id="OrdinanzaGEAnn"   scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="FlagIndulto" scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="AnnotazioneManuale" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="codOggettoSige" scope="request" class="java.lang.String"/>


<%// bean utilizzati nelle sezioni commentate 

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

%>

<%
//==============================================================================
//          Form di inserimento dei dati della decisione del GE
// - include DettaglioSoggettoSentenza.jsp
// - Posizione Giuridica
// - Sezione con le annotazioni manuali relative a Richieste con Anticipazione degli effetti (non validate)
// - Sezione con le annotazioni manuali relative a Richieste senza Anticipazione (non validate)
// - Sezione contenente tutti i reati collegati al fascicolo (titoli di reato)
// - Sezione con la pena complessiva (se ergastolo) o PENA RESIDUA
// - SEZIONE CON I CAMPI DI IMPUT
//
// n.b. la pena residua visualizzata
//==============================================================================

	String lNonMeFrame = request.getParameter("nome_frame");
    FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)FascicoloSigeEsteso.getFascicoloSiep();
  
  // Valore di default del Flag Conforme (-, C, D)
  String lFlagConf = "-";
  if( AnnotazioneManuale != null && AnnotazioneManuale.getFlagConforme().length() > 0)
	  lFlagConf = AnnotazioneManuale.getFlagConforme();

  // Valore di default del Flag + o - 
  String lFlagPiuMeno = "";
  if( AnnotazioneManuale != null && AnnotazioneManuale.getFlagPiuMeno() != null && AnnotazioneManuale.getFlagPiuMeno().length() > 0)
	  lFlagPiuMeno = AnnotazioneManuale.getFlagPiuMeno();
  
  
  // Caricamento Combo DPR
  String lOptionSel = "-";
  if( AnnotazioneManuale != null && AnnotazioneManuale.getCodDpr().length() > 0)
	  lOptionSel = AnnotazioneManuale.getCodDpr();
  
  Option lOptionDpr  = new Option( DecodificheManager.getInstance().getDPR());
  Vector lVect = (Vector)DecodificheManager.getInstance().getDPR();
  DecodificheModel lDecMod = (DecodificheModel)lVect.lastElement();
  if (lOptionSel.equalsIgnoreCase("-"))
	lOptionDpr.setSelected(lDecMod.getCode());
  else
  	lOptionDpr.setSelected(lOptionSel);
  
  String listaDPR = lOptionDpr.toString();

  // Caricamento combo tipo beneficio (Amnistia/Indulto)
  Option lOptionTipoAnn = new Option( DecodificheManager.getInstance().getTipoAnnotazioneManualeBenefici());
  lOptionTipoAnn.setFilter(FlagIndulto);
  String TipoAnnotazioneManuale = lOptionTipoAnn.toString();
  
  
  
  String isTitoliEsecutivi=request.getParameter("isTitoliEsecutivi");
  if (isTitoliEsecutivi==null)
	  isTitoliEsecutivi="false";

  String idProvvedimento =request.getParameter("idProvvedimento");
 
%>

  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">

  var obbligatori;
  var dataesiste;

  //============================================================================
  // funzione per la verifica dei dati imputati in maschera
  //============================================================================
  function VerificaAnnMan()
  {
    //==========================================================================
    // I quantum non sono obbligatori se la decisione del GE è un Rigetto o 
    // 'dichiara inammissibile' una Richiesta senza anticipazione.
    // In tutti gli altri casi il quantum va imputato a mano.
    //==========================================================================
    // verifico se sono state selezionate delle richieste
    tipoRic = tipoRichiestaSelezionata();
    //alert ('Tipo Richieste = '+tipoRic);
    
    if (   (   document.<%=lNonMeFrame%>.TipoOrd[3].checked==true
            || document.<%=lNonMeFrame%>.TipoOrd[4].checked==true 
            || document.<%=lNonMeFrame%>.TipoOrd[5].checked==true
           )
        && (tipoRic=='senzaAnticipazione' )
       )
    {
      // Non controllo i quantum
//      alert("quantum non obbligatori 1");
//      return false;
    }
//    else if (document.<%=lNonMeFrame%>.TipoOrd[1].checked==true && tipoRic=='conAnticipazione' ) 
//    {
      // Decisione conforme a richiesta con anticipazione 
//      alert("quantum non obbligatori 2");
//      return false;
//    }
    else
    {
      // Flag +/- obbligatorio
      if (document.<%=lNonMeFrame%>.PM[document.<%=lNonMeFrame%>.PM.selectedIndex].value == "")
      {
        alert ("Selezionare tra + e -");
        return false;
      }
  
      obbligatori = (document.<%=lNonMeFrame%>.GRec.value!="" || document.<%=lNonMeFrame%>.MRec.value!="" || document.<%=lNonMeFrame%>.ARec.value!="");
      obbligatori = obbligatori || (document.<%=lNonMeFrame%>.Ammenda.value!="")
      obbligatori = obbligatori || (document.<%=lNonMeFrame%>.GArr.value!="" || document.<%=lNonMeFrame%>.MArr.value!="" || document.<%=lNonMeFrame%>.AArr.value!="");
      obbligatori = obbligatori || (document.<%=lNonMeFrame%>.Multa.value!="");
  
      if (!obbligatori)
      {
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");
  
        return false;
      }
    }
  
    //==========================================================================
    // Controlli su dati dell'ordinanza
    //==========================================================================
    if (document.<%=lNonMeFrame%>.tipoannotazione[document.<%=lNonMeFrame%>.tipoannotazione.selectedIndex].value=="-")
    {
      alert("Selezionare computo beneficio");
      return false;
    }

    if (document.<%=lNonMeFrame%>.dpr[document.<%=lNonMeFrame%>.dpr.selectedIndex].value=="-")
    {
      alert("Selezionare DPR");
      return false;
    }
	  return true;
  }


  function VerificaAnnMan_Quantum()
  {
    document.<%=lNonMeFrame%>.operazione.value="Quantum";
    var RetVer = VerificaAnnMan();
    return RetVer;
  }

  
  //========================================================================
  // Funzione per nascondere un elemento dalla lista (deselezionarlo)
  //========================================================================
  function rimuovi(id_record)
  {
    document.getElementById(id_record).style.display='none';
    document.getElementById('cb_'+id_record).checked=false;
    document.getElementById('sel'+id_record).style.display='block';
    var righe_tabella = document.getElementById('tabella_richieste').all;
    var removedRecord = document.getElementById(id_record);
    
    notSelBenefici.push(removedRecord.idAnnotazione);
    removeFromTheArray (selBenefici, removedRecord.idAnnotazione);
    
    isRichiestaSelezionata = 'false';
    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          //alert(righe_tabella(i).tagName);
          isRichiestaSelezionata='true';
        }
      }
    }
    if (isRichiestaSelezionata=='false'){
      document.getElementById('titolo_richieste').style.display='none';
    }
    
    //= Aggiorno i quantum solo se Richieste senza Anticipazione e Conforme[1]
    //= in questo caso il quantum da concedere è pari alla somma dei quantum richiesti
    //= Oppure rigetto[3] o dichiara inammissibile[4] richieste conAnticipazione, in questo
    //= caso i quantum vanno..
    //= Negli altri casi: senza richiesta [0], diforme[2] i quantum vanno imputati
    //= a mano.
    
    //alert (tipoRichiestaSelezionata());
    richiesta=tipoRichiestaSelezionata(id_record);
    if (document.<%=lNonMeFrame%>.TipoOrd[1].checked && richiesta=='senzaAnticipazione')
       aggiornaQuantum('Conferma');
    if (document.<%=lNonMeFrame%>.TipoOrd[3].checked && richiesta=='conAnticipazione')
       aggiornaQuantum('Annulla');
    if (document.<%=lNonMeFrame%>.TipoOrd[4].checked && richiesta=='conAnticipazione')
       aggiornaQuantum('Annulla');
    if (document.<%=lNonMeFrame%>.TipoOrd[1].checked && richiesta=='conAnticipazione')
        aggiornaQuantum('Annulla');
    
    
  }
  
  //==========================
  //
  //==========================
  function isConforme() {
//    if (document.<%=lNonMeFrame%>.TipoOrd[1].checked && tipoRichiestaSelezionata()=='senzaAnticipazione')
    if (document.<%=lNonMeFrame%>.TipoOrd[1].checked )
      aggiornaQuantum('Conferma');
  }
  
  //==========================
  //
  //==========================
  function isRigetto() {
    if (document.<%=lNonMeFrame%>.TipoOrd[3].checked && tipoRichiestaSelezionata()=='conAnticipazione')
      aggiornaQuantum('Annulla');
  }
  
  //==========================
  //
  //==========================
  function isInammissibile() {
    if (document.<%=lNonMeFrame%>.TipoOrd[4].checked && tipoRichiestaSelezionata()=='conAnticipazione')
      aggiornaQuantum('Annulla');
  }
  
 
  //========================================================================
  // Restituisce il tipo di richiesta selezionata
  // - nessuna
  // - conAnticipazione
  // - senzaAnticipazione
  // - misto
  //========================================================================
  function tipoRichiestaSelezionata (){
	 var righe_tabella = document.getElementById('tabella_richieste').all;
    isRichiestaSelezionata = 'false';
    isAnticipazione='false';
    isSenzaAnticipazione='false';
    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          alert(righe_tabella(i).tipoRich);
          isRichiestaSelezionata='true';
          if (righe_tabella(i).tipoRich=='A')
            isAnticipazione='true';
          if (righe_tabella(i).tipoRich=='R')
            isSenzaAnticipazione='true';
        }
      }
    }
    
    if (isRichiestaSelezionata=='false')
      return 'nessuna';
    if (isAnticipazione=='true' && isSenzaAnticipazione=='false')
      return 'conAnticipazione';
    if (isAnticipazione=='false' && isSenzaAnticipazione=='true')
      return 'senzaAnticipazione';
    if (isAnticipazione=='true' && isSenzaAnticipazione=='true')
      return 'misto';
  }

  //============================================================================
  // Aggiorna i campi Reclusione e Arresto sommando i dati delle richieste 
  // selezionate
  // Se tipoAggiornamento = Conferma 
  // Se tipoAggiornamento = Annulla
  //============================================================================
  function aggiornaQuantum (tipoAggiornamento){
    //
    var righe_tabella = document.getElementById('tabella_richieste').all;
    aaRec = 0;
    mmRec = 0;
    ggRec = 0;
    multa = 0.00;
    
    aaArr = 0;
    mmArr = 0;
    ggArr = 0;
    ammenda = 0.00;
    

    for(i = 0; i < righe_tabella.length; i++){
      if (righe_tabella(i).tagName=='TR'){
        if (   righe_tabella(i).style.display=='block'
            && righe_tabella(i).id!='titolo_richieste'
           )
        {
          //alert(righe_tabella(i).tagName);
          if (righe_tabella(i).segno=='+')
          { //alert("revocati");
            aaRec   = aaRec   + parseInt(righe_tabella(i).aaRec);
            mmRec   = mmRec   + parseInt(righe_tabella(i).mmRec);
            ggRec   = ggRec   + parseInt(righe_tabella(i).ggRec);
            multa   = multa   + parseFloat(righe_tabella(i).multa);
            
            aaArr   = aaArr   + parseInt(righe_tabella(i).aaArr);
            mmArr   = mmArr   + parseInt(righe_tabella(i).mmArr);
            ggArr   = ggArr   + parseInt(righe_tabella(i).ggArr);
            ammenda = ammenda + parseFloat(righe_tabella(i).ammenda);
          }
          else
          { //alert("concessi");
            aaRec   = aaRec   - parseInt(righe_tabella(i).aaRec);
            mmRec   = mmRec   - parseInt(righe_tabella(i).mmRec);
            ggRec   = ggRec   - parseInt(righe_tabella(i).ggRec);
            multa   = multa   - parseFloat(righe_tabella(i).multa);
            
            aaArr   = aaArr   - parseInt(righe_tabella(i).aaArr);
            mmArr   = mmArr   - parseInt(righe_tabella(i).mmArr);
            ggArr   = ggArr   - parseInt(righe_tabella(i).ggArr);
            ammenda = ammenda - parseFloat(righe_tabella(i).ammenda);
          }
        }
      }
    }

//    alert ('multa: '+multa);
//    alert ('ammenda: '+ammenda);

    recNorm = normalizzaQuantum(aaRec,mmRec,ggRec);
    annNorm = normalizzaQuantum(aaArr,mmArr,ggArr);
    
//    alert ('segno rec: '+recNorm[0]+','+recNorm[1]+','+recNorm[2]+','+recNorm[3] );
//    alert ('segno arr: '+annNorm[0]+','+annNorm[1]+','+annNorm[2]+','+annNorm[3] );
    
    if (recNorm[0]=='-' || annNorm[0]=='-')
    {
      if (tipoAggiornamento=='Annulla')
        document.<%=lNonMeFrame%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
      else
        document.<%=lNonMeFrame%>.PM[2].selected=true; // segno -
    }
    else if (recNorm[1]!='0' || recNorm[2]!='0' || recNorm[3]!='0' || annNorm[1]!='0' || annNorm[2]!='0' || annNorm[3]!='0')
    {
      if (tipoAggiornamento=='Annulla')
        document.<%=lNonMeFrame%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
      else
        document.<%=lNonMeFrame%>.PM[1].selected=true; // segno +
    }
    else {
      // quantum nulli ho solo la pena pecuniaria devo utilizzare il segno della
      // multa o ammenda
//      alert ('quantum nulli');
      if (multa<0 || ammenda<0){
        if (tipoAggiornamento=='Annulla')
          document.<%=lNonMeFrame%>.PM[1].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
        else
          document.<%=lNonMeFrame%>.PM[2].selected=true; // segno -
      }
      else{
        if (tipoAggiornamento=='Annulla')
          document.<%=lNonMeFrame%>.PM[2].selected=true; // sto rigettando per cui devo inserire i quantum con segno opposto
        else
          document.<%=lNonMeFrame%>.PM[1].selected=true; // segno +
      }
      
    }
    
    if (multa<0)   multa   = multa*(-1);
    if (ammenda<0) ammenda = ammenda*(-1);
      
    // Reclusione    
    document.<%=lNonMeFrame%>.ARec.value  = recNorm[1];
    document.<%=lNonMeFrame%>.MRec.value  = recNorm[2];
    document.<%=lNonMeFrame%>.GRec.value  = recNorm[3];
    document.<%=lNonMeFrame%>.Multa.value = parseInt(multa);
    document.<%=lNonMeFrame%>.Mul_dec.value = getParteDecimale(multa);
    // Arresti    
    document.<%=lNonMeFrame%>.AArr.value=annNorm[1];
    document.<%=lNonMeFrame%>.MArr.value=annNorm[2];
    document.<%=lNonMeFrame%>.GArr.value=annNorm[3];
    document.<%=lNonMeFrame%>.Ammenda.value=parseInt(ammenda); // recupera la parte intera
    document.<%=lNonMeFrame%>.Amm_dec.value=getParteDecimale(ammenda);
  }
  
  //========================================================================
  // 
  //========================================================================
  function normalizzaQuantum(anni,mesi,giorni)
  {
    tot_giorni=anni*30*12 + mesi*30 + giorni;
    segno='+';
    //alert("tot_giorni = "+tot_giorni);
    if (tot_giorni<0){
      tot_giorni=tot_giorni*(-1);
      segno='-';
    }
    giorni = tot_giorni % 30; // restituisce il resto dell'operazione
    tot_giorni = tot_giorni-giorni;
    tot_mesi = parseInt(tot_giorni/30); // es 55 mesi
    mesi = tot_mesi % 12;
    tot_mesi = tot_mesi - mesi;
    anni = parseInt(tot_mesi/12);
    //alert("anni = "+anni);
    //alert("mesi = "+mesi);
    //alert("giorni = "+giorni);
    var quantumNormalizzati = new Array(segno,anni ,mesi ,giorni );
    return quantumNormalizzati;
  }      
  
  //============================================================================
  // estrae la parte decimale da un number
  //============================================================================
  function getParteDecimale(valore){
    //alert("valore = "+valore);
    valoreStr = String(valore);
    //alert("valoreStr = "+valoreStr);
    posVirgola=-1;
    parteDecimale = "00";
    for (i=0;i<valoreStr.length;i++){
      //alert(valoreStr.substr(i,1));
      if (valoreStr.substr(i,1)=='.')
        posVirgola = i;
    }
    if (posVirgola<0)
      parteDecimale = "00";
    else
      parteDecimale = valoreStr.substr(posVirgola+1,valoreStr.length-(posVirgola+1));
      
//    alert ("parteDecimale = "+parteDecimale);
    return parteDecimale;
  }
  
  function gestComboMinusPlus(combo) {
	  var selValue=combo.value;
	  var codOggetto='<%=codOggettoSige%>';
	  var selEsito='';

	  if (codOggetto == '0090' && selValue=='-') {
		  selEsito='0140';
	  }
	  
      if (codOggetto=='0091' && selValue=='-') {
    	  selEsito='0141';
	  }
      
      if (codOggetto=='0090' && selValue=='+') {
    	  selEsito='0142';
	  }
	  
      if (codOggetto=='0091' && selValue=='+') {
    	  selEsito='0143';
	  }

      var selectorEsito=document.getElementById("CodEsitoSige");
      selectorEsito.value=selEsito;
  }
  
  function removeFromTheArray (theArray, theValue) {
	  for (var index = 0; index < theArray.length; ++index) {
		    if (theArray[index] == theValue) {
		    	theArray.splice (index, 1);
		    }	
		}
  }
</script>
    <input type="hidden" name="operazione" value="" />
    <input type="hidden" name="lFlagPage" value="AMNI" />
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0284" />
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="<%=FlagIndulto%>" />
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getIdAnnotazioneManuale())%>" />
    
<%
//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione già validate
// n.b. nasce con i record nascosti che vengono abilitati da Seleziona Richieste
//      simulando il caricamento dinamico in maschera
//==============================================================================
if( lFascicoloAssociato != null && RichiesteAlGE1 != null && RichiesteAlGE1.size() > 0) {
%>
<input type="hidden" name="NumTotRichieste" value="<%=RichiesteAlGE1.size()%>">
<table style="width: 95%;" id="tabella_richieste">
  <tr><td colspan="5" class="Titolonocap" style="font-size:14px !important;">Richieste del PM al Giudice dell'Esecuzione </td></tr>
  <tr>
  <%
  
  %>
    <td class="l" colspan="5">
         <jsp:include page="/jsp/files/siap/sige/richiesta/ListaRichiesteAlGE.jsp" />
    </td>
  </tr>
<%
int id_record = 0;
%>  
  <tr style="display:none;" id="titolo_richieste">
    <td class="c">Tipo</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
    <td class="c">Anticipazione</td>
    <td class="c">&nbsp;</td>
  </tr>
<%
  Iterator itx = RichiesteAlGE1.iterator();
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
    String tipoRichiesta = "R";
    if (   lAnnMod.getFlagAppProvvisoria()!=null 
           && lAnnMod.getFlagAppProvvisoria().equals("A")
       )
    {
      tipoRichiesta = "A";
    }
%>
  <tr style="display:none;" id="record_<%=id_record%>"
<%--   	  tipoRich="<%=tipoRichiesta%>" --%>
<%--       segno="<%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%>" --%>
<%--       aaRec="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%>" --%>
<%--       mmRec="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%>" --%>
<%--       ggRec="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%>" --%>
<%--       multa ="<%=StringUtils.toStringJSP(lAnnMod.getImportoMulta(),"0.00")%>"  --%>
<%--       aaArr="<%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%>" --%>
<%--       mmArr="<%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%>" --%>
<%--       ggArr="<%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%>" --%>
<%--       ammenda ="<%=StringUtils.toStringJSP(lAnnMod.getImportoAmmenda(),"0.00")%>" --%>
<%--       idAnnotazione="<%=lAnnMod.getIdAnnotazioneManuale().toString() %>"  --%>
      >
      
    <td class="l" style="text-align:center">
      <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno(),"")%></font>
    </td>
    <td class="l">
       Anni   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniReclusione(),"0")%></font>
       Mesi   <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiReclusione(),"0")%></font>
       Giorni <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniReclusione(),"0")%></font>
       Multa  <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font>
    </td>
    <td class="l">
       Anni    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumAnniArresto(),"0")%></font>
       Mesi    <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumMesiArresto(),"0")%></font>
       Giorni  <font class=campo><%=StringUtils.toStringJSP (lAnnMod.getNumGiorniArresto(),"0")%></font>
       Ammenda <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font>
    </td>
    <% if ( tipoRichiesta.equals("A")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else {%>
    <td class="l">&nbsp;</td>
    <% }%>
    <td class="l">
      <a href="Javascript:rimuovi('record_<%=id_record%>');">
        <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0" title="Elimina dalla lista">
      </a>
    </td>
    <td class="l" style="display:none;">
      <input type="checkbox" name="cb_record_<%=id_record%>" id="cb_record_<%=id_record%>" value="<%=lAnnMod.getIdAnnotazioneManuale()%>" >
    </td>
  </tr>
<% } %>
</table>
<%
} // endif( lFascicoloAssociato != null 

%>

<%
//==============================================================================
// Sezione contenente l'elenco delle richieste con anticipazione degli effetti.
// Quali, quelle non ancora validate
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaAntEffetti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta Anticipazioni degli effetti</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaAntEffetti.size(); i++)
  {
      out.println("<tr>");

      tmp=(AnnotazioneManualeModel)ReaAntEffetti.get(i);

      idRea=tmp.getReaIdReato();
      boolean found=false;
      for (int itR=0;itR<reati.size();itR++)
      {
        rtmp=(ReatoModel)reati.get(itR);
        if (rtmp.getIdReato().equals(idRea))
        {
          found=true;
          itR=reati.size()+1;
        }
      }

      out.println("<td class=\"l\">");

      if (found) // esiste reato associato all'annotazione
      {
        boolean lFlagAnnoNumero;
        lFlagAnnoNumero = false;
        if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
          lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
        </font>
        <font class="campo">
<%
        if(lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte()+" ");
          if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
            out.println(rtmp.getAnnoFonte());
          if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
            out.println("/"+rtmp.getNumeroFonte());
        }

        if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
          out.println("art."+rtmp.getArticolo());
        if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
          out.println(" "+rtmp.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
            out.println(rtmp.getDescrFonte());
        }

        if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
          out.println(" c. "+rtmp.getComma());
        if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
          out.println(" l. "+rtmp.getLettera());
        if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
          out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      { // non è stato associato un reato un fase di richiesta
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }
      out.println("</td>");
%>
      <td class="l">
         Anni   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
         Mesi   <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
         Multa  <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
      </td>
      <td class="l">
         Anni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
         Mesi <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
         Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
         Ammenda <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
      </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
  }  // fine elenco richieste con anticipazione


//==============================================================================
// Sezione con le annotazioni manuali relative a Richieste senza Anticipazione
// Per ogni annotazione vengono visualizzati i seguenti dati:
// - reato o Pena Complessiva
// - quantum reclusione richiesto
// - quantum arresto    richiesto
//==============================================================================
if (ReaRichiesti.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan=3 class="Titolo">Richiesta</td></tr>
  <tr>
    <td class="c">Applicata a</td>
    <td class="c">Reclusione</td>
    <td class="c">Arresto</td>
  </tr>
<%
  AnnotazioneManualeModel tmp = null;
  ReatoModel rtmp = new ReatoModel();
  BigDecimal idRea;

  for (int i=0; i<ReaRichiesti.size(); i++)
  {
    out.println("<tr>");
    tmp=(AnnotazioneManualeModel)ReaRichiesti.get(i);
    idRea=tmp.getReaIdReato();
    boolean found=false;
    for (int itR=0;itR<reati.size();itR++)
    {
      rtmp=(ReatoModel)reati.get(itR);
      if (rtmp.getIdReato().equals(idRea))
      {
       found=true;
       itR=reati.size()+1;
      }
    }

    out.println("<td class=\"l\">");

    if (found)
    {
      boolean lFlagAnnoNumero;
      lFlagAnnoNumero = false;
      if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
        lFlagAnnoNumero = true;
%>
        <font class="label">
<%
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
%>
      </font>
      <font class="campo">
<%
          if(lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte()+" ");
            if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
              out.println(rtmp.getAnnoFonte());
            if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
              out.println("/"+rtmp.getNumeroFonte());
          }

          if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
            out.println("art."+rtmp.getArticolo());
          if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
            out.println(" "+rtmp.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte());
          }

          if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
            out.println(" c. "+rtmp.getComma());
          if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
            out.println(" l. "+rtmp.getLettera());
          if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
            out.println(" n. "+rtmp.getNumero());%></font>
<%
      }
      else
      {
        out.println("<font class=\"campo\">Pena Complessiva</font>");
      }

      out.println("</td>");
%>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
           Multa <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
        </td>
        <td class="l">
           Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
           Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
           Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
           Ammenda <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
        </td>
<%
    out.println("</tr>");
   }
%>
   </table>
<%
}

//==============================================================================
//       Sezione contenente tutti i reati collegati al fascicolo
//==============================================================================
if (!reati.isEmpty())
{
%>
<table style="width: 95%;">
  <tr><td colspan=7 class="Titolonocap">Titoli di reato</td></tr>
  <tr>
    <td class="c">Reato</td>
    <td class="c">Durata</td>
    <td class="c">Sanzione</td>
    <td class="c">Sel.</td>
    <td class="c">Ann.Inserita</td>
  </tr>
<%
  ReatoModel lReato;
  boolean lFlagAnnoNumero;
  for (int i=0;i<reati.size();i++)
  {
    lReato=(ReatoModel)reati.get(i);
    lFlagAnnoNumero = false;
    
    if(  lReato.getAnnoFonte() != null
       && !lReato.getAnnoFonte().equals("")
       && lReato.getNumeroFonte() != null
       && !lReato.getNumeroFonte().equals("") )
    {
      lFlagAnnoNumero = true;
    }
%>
  <tr>
    <td class="l">
      <font class="label">
      <% if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals("")) { %>
        n.<%=lReato.getProgrNumeroManuale()%>: 
      <% } else { %>
        n.<%=lReato.getProgrReato()%>:
      <% } %>
      </font>
      <font class="L">
<%
        if(lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte()+" ");
          if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
            out.println(lReato.getAnnoFonte());
          if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
            out.println("/"+lReato.getNumeroFonte());
        }

        if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
          out.println("art."+lReato.getArticolo());
        if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
          out.println(" "+lReato.getDescrSottonumerazione());

        if(!lFlagAnnoNumero)
        {
          if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
            out.println(lReato.getDescrFonte());
        }

        if(lReato.getComma() != null && !lReato.getComma().equals(""))
          out.println(" c. "+lReato.getComma());
        if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
          out.println(" l. "+lReato.getLettera());
        if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
          out.println(" n. "+lReato.getNumero());%>
      </font>
      
      <% if(lReato.getStringaConsumazione()!= null) { %>
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
      <% } %>
    
      <%if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
      <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
      <% } %>

      <% if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
      <font class="label">Luogo</font>&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
      <% } %>
    </td>

    <td class="l">
      <table>
        <tr>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </tr>
      </table>
    </td>
    <td class="r">
      <font class="campo">
        <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
      </font>
      €
      <% if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0) { %>
        di
        <font class="campo">
          <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
        </font>&nbsp;
      <% } %>
    </td>
    <td class="c"><input type="radio" name="IdReato" value="<%= lReato.getIdReato() %>"></td>
    <% if(lReato.getFlagVisto()!= null && lReato.getFlagVisto().equals("S")) { %>
    <td class="C"><img src="/images/V.gif"> </td>
    <% } else { %>
    <td class="C"> &nbsp;</td>
    <% } %>
    </tr>
<%
		}
%>
    </table>
<%
  }
%>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: solo data inizio e data fine MAI
// - Se Libero vengono visualizzati i Quantum 
//   - PenRes1 = Reclusione
//   - PenRes2 = Arresti
// - Se detenuto viene visualizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
// se la Pena Complessiva è un ergastolo o ergastolo con isolamento

boolean flagPenacomplessiva = false;

if(flagPenacomplessiva)
{
if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
   && PenaComplessiva.getCodTipoPenaDetentiva() != ""
   && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
{
%>
<table style="width: 95%;">
  <tr>
    <td colspan=3 class="Titolonocap">Pena complessiva</td>
  </tr>
  <tr>
    <td class="l">
      <font class="campo">
        <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
      </font>
    </td>
    <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
    <td class="l">Data Fine : <font class="campo">MAI</font></td>
  </tr>
</table>
<%
}
else if (!PenRes1.getErrorMsg().equals("-"))
{
%>
<table style="width: 95%;">
  <tr>
  <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) { %>
    <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
  <% } else { %>
    <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
  <% } %>
  </tr>
  
  <%
  if (PenRes1.getErrorMsg().startsWith("Libero")) //Patch per gestire il titolo
  {
%>
  <tr>
    <td class="l"> Reclusione :
      Anni <font class=campo><%=PenRes1.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes1.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
      Multa <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
    </td>
    <td class="l"> Arresto :
      Anni <font class=campo><%=PenRes2.getNumAnni()%></font>
      Mesi <font class=campo><%=PenRes2.getNumMesi()%></font>
      Giorni <font class=campo><%=PenRes2.getNumGiorni()%></font>
      Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
    </td>
  </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
  else //non libero
  { // pena in espiazione ??? calcolo al volo la pena residua come intervallo
    // tra date
    //==========================================================================
    // Solo se la pena è in espiazione calcolo il quantum residuo, altrimenti
    // visualizzo solo decorrenza-scadenza.
    // n.b. la pena non viene considerata in espiazione se:
    //      1 - data inizio >= sysdate (pene con decorrenza futura)
    //      2 - data fine < sysdate (in questo caso il condannato viene considerato scarcerato)
    //     data inizio <= sysdate <data fine
    %>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr>
  
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
  }
%>
</table>
<%
  }
}
%>

<%
//====================================================================================
// SEZIONE CON I CAMPI DI IMPUT DEI DATI DELLA DECISIONE DEL GIUDICE DELL' ESECUZIONE
// se provengo da 'aggiungi' i campi vengono precaricati con i dati precedentemente
// inseriti non modificabili
//====================================================================================
%>
<table style="width: 95%;">
	<tr><td colspan=8 class="Titolonocap"> Quantum </td></tr>
	<tr>
    <td class="l" nowrap>Beneficio <font class="ob">(*)</font> :&nbsp;</td>
		<td class="l"><select name="tipoannotazione"><%= TipoAnnotazioneManuale %></select></td>
		<td class="l" nowrap>DPR <font class=ob>(*)</font> :&nbsp;<select name=dpr><%=listaDPR%></select></td>
	</tr>
 
 <%  if(RichiesteAlGE1 != null && RichiesteAlGE1.size() > 0) { 	 %>
  <tr>
    <td class="l" colspan=3>
      <input type="radio" name="TipoOrd" value="-" <% if (lFlagConf.equalsIgnoreCase("-")){ %> checked  <%} %> >senza richiesta &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="C" <% if (lFlagConf.equalsIgnoreCase("C")){ %> checked  <%} %> onclick="Javascript:isConforme();">in conformita' alla richiesta del PM &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="D" <% if (lFlagConf.equalsIgnoreCase("D")){ %> checked  <%} %> >in difformita' alla richiesta del PM
    </td>
  </tr>
    <tr>
    <td class="l" colspan=3>
      <input type="radio" name="TipoOrd" value="R"  <% if (lFlagConf.equalsIgnoreCase("R")){ %> checked  <%}%>     onclick="Javascript:isRigetto();">rigetta la richiesta del PM &nbsp;&nbsp;
    </td>
  </tr>
  
  
 <% } %>
</table>

<%
//==============================================================================
//                    Sezione con i quantum da imputare
//==============================================================================
%>
<table width="97%">
  <tr><td colspan=6><hr width="100%"></td></tr>
  <tr>
    <td valign="middle" class=c rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="PM" onChange="gestComboMinusPlus(this)">
        <option value=""></option>
        <option value="+" <%if(lFlagPiuMeno.equalsIgnoreCase("+")) { %> selected <%}%> >+</option>
        <option value="-"  <%if(lFlagPiuMeno.equalsIgnoreCase("-")) { %> selected <%}%> >-</option>
      </select>
  </td>
  <td class=titolo colspan=2>Reclusione</td>
  <td width=25>&nbsp;</td>
  <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
			<td class="c">
				<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Giorni</font><br>
				<input type="text" name="ARec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniReclusione(),"0")%>">&nbsp;
				<input type="text" name="MRec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiReclusione(),"0")%>">&nbsp;
				<input type="text" name="GRec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniReclusione(),"0")%>">
			</td>
			<td class="c">
				<font class="label">Multa</font><br>
				<input style="text-align:right" type="text" name="Multa" maxlength="8" size="6" value=<%=AnnotazioneManuale.getImportoMulta() != null ? StringUtils.toStringJSP(AnnotazioneManuale.getImportoMulta().intValue()) : ""%>>
				,
        <input style="text-align:left" type="text" name="Mul_dec" maxlength="2" size="2"  value=<%=AnnotazioneManuale.getImportoMulta() != null && AnnotazioneManuale.getImportoMulta().toString().indexOf(".") > -1  ? StringUtils.toStringJSP(AnnotazioneManuale.getImportoMulta()).substring(AnnotazioneManuale.getImportoMulta().toString().indexOf(".")+1) : ""%> >
			</td>
			<td width=25>&nbsp;</td>
			<td class="c">
				<font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
				<font class="label">Giorni</font><br>
				<font class=campo><input type="text" name="AArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumAnniArresto(),"0")%>"></font>&nbsp;
				<font class=campo><input type="text" name="MArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumMesiArresto(),"0")%>"></font>&nbsp;
				<font class=campo><input type="text" name="GArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(AnnotazioneManuale.getNumGiorniArresto(),"0")%>"></font>
			</td>
      <td class="c">
				<font class="label">Ammenda</font><br>
				<input style="text-align:right" type="text" name="Ammenda" maxlength="8" size="6" value=<%=AnnotazioneManuale.getImportoAmmenda() != null ? StringUtils.toStringJSP(AnnotazioneManuale.getImportoAmmenda().intValue()) : ""%>>
				,
        <input style="text-align:left" type="text" name="Amm_dec" maxlength="2" size="2" value=<%=AnnotazioneManuale.getImportoAmmenda() != null && AnnotazioneManuale.getImportoAmmenda().toString().indexOf(".") > -1  ? StringUtils.toStringJSP(AnnotazioneManuale.getImportoAmmenda()).substring(AnnotazioneManuale.getImportoAmmenda().toString().indexOf(".")+1) : ""%>>
			</td>
		</tr>
 	</table>
 	
 	<div style="display:none;">
 	  <input type="radio" name="TipoOrd" value="R"       onclick="Javascript:isRigetto();">rigetta &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="I" onclick="Javascript:isInammissibile();">dichiara inammissibile &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="U">riunisce &nbsp;&nbsp;
 	</div>
 	
 	
</html>