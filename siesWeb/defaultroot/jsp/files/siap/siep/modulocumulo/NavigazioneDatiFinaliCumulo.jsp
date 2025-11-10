<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.util.ModuloCumuloUtils"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<!-- MEV_2025-48 - 2.12 Alert su continuazione -->
<%@ page import="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>




<jsp:useBean id="titoliDoppi"     scope="request" class="java.util.ArrayList"/>

<%
//==============================================================================
// jsp per la visualizzazione del Menu di navigazione dei dati finali cumulo
//==============================================================================
%>


<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="ListaTitoli"       scope="request" class="java.util.Vector"/>

<jsp:useBean id="FunzioneMenu"      scope="request" class="java.lang.String"/>

<!-- MEV_2025-48 - 2.12 Alert su continuazione -->
<jsp:useBean id="ListaTitContSganciate"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaTitRevBenSganciati"     scope="request" class="java.util.Vector"/>


<%
PenaRideterminataCumuloModel lPenaResidua = datiFinaliAggregatoModel.getPenaResiduaCumulo();
String lColorBTPena = "";
String lAsteriscoPena = "";
boolean lIsCalcoloDaRieffettuare = false;
if (lPenaResidua!=null && "S".equals(lPenaResidua.getIsPenaDaRicalcolare())) {
 lColorBTPena = "style=\"background-color: rgb(255,255,153);\"";
 lAsteriscoPena = " (*)";
 lIsCalcoloDaRieffettuare = true;
}

PosizioneGiuridicaCumuloModel lPosGiu = datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo();
EventoNotificaModel lEveNot = datiFinaliAggregatoModel.getProvvedimentoCumulo();


String lDFCPresente = "";
String lPenRidetPresente = "";
String lUltSanPresente = "";
String lPosPresente = "";
String lCalcPenaPresente = "";
String lProvvPresente = "";

if (datiFinaliAggregatoModel.getDatiFinaliCumulo()!=null)                 lDFCPresente = " <img src='/images/V.gif' style='border:0px;'> ";
if (datiFinaliAggregatoModel.getPenaRideterminataCumulo()!=null)          lPenRidetPresente = " <img src='/images/V.gif' style='border:0px;'> ";

boolean lIsAlertMisureSicurezza = false;
String lStrAlertMisureSicurezza = "<img src='/images/attenzione.jpg' style='border:0px; width:15px; height:15px;'> Non è stato ancora indicato se le Misure di Sicurezza selezionate vanno o meno iscritte in un fascicolo";
if (    (datiFinaliAggregatoModel.getListaMisureSicurezza()!=null && datiFinaliAggregatoModel.getListaMisureSicurezza().size()>0)
     || (datiFinaliAggregatoModel.getListaPeneAccessorie()!=null && datiFinaliAggregatoModel.getListaPeneAccessorie().size()>0)
   )
{
    // MEV_2025-48 - ALTRO -
    if (   datiFinaliAggregatoModel.getListaMisureSicurezza()!=null 
        && datiFinaliAggregatoModel.getListaMisureSicurezza().size()>0
        && datiFinaliAggregatoModel.getDatiFinaliCumulo().getFlagCreaFascicoloMs()==null
       )
    {
        lIsAlertMisureSicurezza = true;
        lUltSanPresente = " <img src='/images/attenzione.jpg' style='border:0px; width:15px; height:15px;'> "; // lUltSanPresente = " <img src='/images/V.gif' style='border:0px;'> ";
    } else 
        lUltSanPresente = " <img src='/images/V.gif' style='border:0px;'> ";
} 
  
if (datiFinaliAggregatoModel.getPosizioneGiuridicaCumulo()!=null)         lPosPresente = " <img src='/images/V.gif' style='border:0px;'> ";
if (datiFinaliAggregatoModel.getPenaResiduaCumulo()!=null)                lCalcPenaPresente = " <img src='/images/V.gif' style='border:0px;'> ";
if (datiFinaliAggregatoModel.getProvvedimentoCumulo()!=null)              lProvvPresente = " <img src='/images/V.gif' style='border:0px;'>  ";


String lColorBTProvv = "";
String lAsteriscoProvv = "";
boolean lIsProvvIncoerente = false;
if (lPosGiu!=null && lEveNot!=null){

  DatiFinaliCumuloModel lDatiFinaliModel = datiFinaliAggregatoModel.getDatiFinaliCumulo();
  
  boolean isGE = false;
  if ("03".equals(lDatiFinaliModel.getTipoUfficioEmissione())){
    isGE = true;
  }
  
  boolean isNLP = false;
  if (!lPenaResidua.isReclusione() && !lPenaResidua.isArresto() ){
    // non c'è detentiva
    isNLP = true;
  }    
  
  ModuloCumuloUtils lModCumUtil = new ModuloCumuloUtils(isGE, isNLP);
  
  //String [] lProvvPerPG = lModCumUtil.getCodMotivoByPosGiu (lPosGiu.getCodPosizioneGiuridica());
  String [] lProvvPerPG = lModCumUtil.getCodMotivoByPosGiu (lPosGiu);
  
  String lCodEvento = lEveNot.getEvento().getCodMotivo();
  
  boolean isCoerente = false;
  for (int i=0; i<lProvvPerPG.length; i++) {
    if (lCodEvento.equals(lProvvPerPG[i])) {
      isCoerente=true;
      break;
    }
  }
  
  if (!isCoerente){
    lColorBTProvv = "style=\"background-color: rgb(255,255,153);\"";
    lAsteriscoProvv = " (*)";
    lIsProvvIncoerente = true;
  }
}

boolean lEscluso = false;
int id_record = 0;
Iterator itx = ListaTitoli.iterator();
while ( itx.hasNext()) 
{
    id_record = id_record+1;

    TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel)itx.next();
    
    if (   lTitoloModel!=null 
        && lTitoloModel.getIdTitoloCumulato()!=null
       )
    {
      if(  lTitoloModel.getFlagEscluso()!=null 
        && lTitoloModel.getFlagEscluso().compareTo("S")==0 
        )     
      {
        lEscluso = true;
      } 
    }
}

// MEV_2025-48 - 2.12 Alert su continuazione
String lAlertContinuazioni = "Attenzione! Alcune Continuazioni non sono correttamente associate a un titolo in istruttoria. Selezionare 'modifica' per effettuare l'associazione";
/*
boolean lIsAlertContinuazioni = true;
lAlertContinuazioni+="<ul style='margin-top:1px; margin-bottom:0px; padding-top:1px; padding-bottom:0px;'>";
lAlertContinuazioni += "<li>ciao";
lAlertContinuazioni += "</li>";
lAlertContinuazioni += "</ul>";
*/

lAlertContinuazioni+="<ul style='margin-top:5px; margin-bottom:5px; padding-top:1px; padding-bottom:1px;'>";

boolean lIsAlertContinuazioni = false;
Iterator itxTitoli = ListaTitContSganciate.iterator();
while ( itxTitoli.hasNext()) 
{
    TitoloCumulatoModel lTitCont = (TitoloCumulatoModel) itxTitoli.next();
    
    if ("S".equals(lTitCont.getFlagEscluso()))
        continue;
    
    Vector <ContinuazioneCumuloModel> lListaContTitolo = (Vector) lTitCont.getPenaComplessivaCumulo().getContinuazioniCumulo();
    
    String lDescTitolo = lTitCont.getDescrTipoProvvedimento();
    lDescTitolo += " del " +  DateUtils.getDateToString( lTitCont.getDataProvvedimento(),"dd-MM-yyyy");
    lDescTitolo += " - "+lTitCont.getCodTipoAutoritaEmittente() + " "+ lTitCont.getDescrLuogoEmittente();
    
    if (lTitCont.getProcedimentoCumulato()!=null) {
        String nSiep = "";
        ProcedimentoCumulatoModel lProcMod = lTitCont.getProcedimentoCumulato();
        if ("S".equals(lProcMod.getFlagAccorpato()) && lProcMod.getUfficioOrigine()!=null ){
          UfficioModel lUfficioOrigine = lProcMod.getUfficioOrigine();

          nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrOrigine();
          nSiep += " <font class=\"cRosso\">Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+"</font>"; 
        }
        else {
          nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrFasCumulato();
          nSiep += " "+lProcMod.getCodTipoUfficioFasCumulato() + " di "+lProcMod.getDescrLuogoUfficioFasCumulato();
        }
        lDescTitolo += " ("+nSiep+")";
    }    

    lAlertContinuazioni+="<li> "+lDescTitolo;
    lAlertContinuazioni+="<ul style='margin-top:5px; margin-bottom:1px; padding-top:1px; padding-bottom:1px;'>";
    Iterator itxListaCont = lListaContTitolo.iterator();
    int contaContinuazione = 0;
    while ( itxListaCont.hasNext()) 
    {
        ContinuazioneCumuloModel lContCumModel = (ContinuazioneCumuloModel) itxListaCont.next();

	    String lLinkModificaCont = IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.modulocumulo.action.ActLoadModificaContinuazioneCumulo"
	            +"&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo()
	            +"&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+lContCumModel.getTitIdTitoloCumulato()
	            +"&"+ICostantiContinuazioneCumulo.CAMPO_ID_CONTINUAZIONE_CUM+"=";    

        lIsAlertContinuazioni = true;
        lLinkModificaCont += lContCumModel.getIdContinuazioneCum();
        
        lAlertContinuazioni += "<li>";
        lAlertContinuazioni += "  Tipo Continuazione: "+lContCumModel.getDescrTipoContinuazione();
        lAlertContinuazioni += ", Sentenza: "+lContCumModel.getAnnoSentenza()+"/"+lContCumModel.getNumSentenza();
        lAlertContinuazioni += " del "+DateUtils.getDateToString (lContCumModel.getDataSentenza(),"dd-MM-yyyy");
        lAlertContinuazioni += ", "+lContCumModel.getDescrTipoAutorita()+" di "+lContCumModel.getDescrLuogoAutorita();
        lAlertContinuazioni += " <a class='cliccabile' href='"+lLinkModificaCont+"' title='Modifica Continuazione'>Modifica Continuazione</a>";
        lAlertContinuazioni += "</li>";
    }  
    lAlertContinuazioni+="</ul>";
    lAlertContinuazioni+="</li>";
}
lAlertContinuazioni += "</ul>";

//MEV_2025-48 - 2.12 Alert su continuazione

// se l'istruttoria NON è aperta non visualizzo il messaggio di alert
if (!"A".equals(IstruttoriaCumulo.getFlagStato()))
    lIsAlertContinuazioni = false;

//=====================================================
// Check sulle revoche benefici non agganciate
//=====================================================
String lAlertRevoche = "Attenzione! Alcune Revoche Benefici non sono correttamente associate a un titolo in istruttoria. Selezionare 'modifica' per effettuare l'associazione";
lAlertRevoche+="<ul style='margin-top:5px; margin-bottom:1px; padding-top:1px; padding-bottom:1px;'>";

boolean lIsAlertRevocheBen = false;

Iterator itxTitoliRevoche = ListaTitRevBenSganciati.iterator();
while ( itxTitoliRevoche.hasNext()) 
{
    lIsAlertRevocheBen = true;
    
    TitoloCumulatoModel lTitRev = (TitoloCumulatoModel) itxTitoliRevoche.next();
    
    if ("S".equals(lTitRev.getFlagEscluso()))
        continue;
    
    String lDescTitolo = lTitRev.getDescrTipoProvvedimento();
    lDescTitolo += " del " +  DateUtils.getDateToString (lTitRev.getDataProvvedimento(),"dd-MM-yyyy");
    lDescTitolo += " - "+lTitRev.getCodTipoAutoritaEmittente() + " "+ lTitRev.getDescrLuogoEmittente();
    
    if (lTitRev.getProcedimentoCumulato()!=null) {
        String nSiep = "";
        ProcedimentoCumulatoModel lProcMod = lTitRev.getProcedimentoCumulato();
        if ("S".equals(lProcMod.getFlagAccorpato()) && lProcMod.getUfficioOrigine()!=null ){
          UfficioModel lUfficioOrigine = lProcMod.getUfficioOrigine();

          nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrOrigine();
          nSiep += " <font class=\"cRosso\">Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+"</font>"; 
        }
        else {
          nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrFasCumulato();
          nSiep += " "+lProcMod.getCodTipoUfficioFasCumulato() + " di "+lProcMod.getDescrLuogoUfficioFasCumulato();
        }
        lDescTitolo += " ("+nSiep+")";
    }
    
    lAlertRevoche+="<li> "+lDescTitolo;
    lAlertRevoche+="<ul style='margin-top:5px; margin-bottom:1px; padding-top:1px; padding-bottom:1px;'>";
    Iterator itxListaRevoche = lTitRev.getBeneficiCumulo().iterator();
    while ( itxListaRevoche.hasNext()) 
    {
        BeneficioCumuloModel lRevocaCumModel = (BeneficioCumuloModel) itxListaRevoche.next();
        
        String lTipoFormBeneficio = "";
        if ("01".equals(lRevocaCumModel.getCodTipoBeneficio())) // Sospensione condizionale
            lTipoFormBeneficio = "01";
        else if ("03".equals(lRevocaCumModel.getCodTipoBeneficio()))  // Indulto
            lTipoFormBeneficio = "02";        
        
        String lLinkModificaRev = IWebConstants.PG_MAIN+"?"+IWebConstants.ACTION_FIELD+"=siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo"
                +"&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo()
                +"&"+ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO+"="+lRevocaCumModel.getTitIdTitoloCumulato()
                +"&modalita=M"
                +"&tipoFormBeneficio="+lTipoFormBeneficio
                +"&"+ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO+"="; 
        
        lLinkModificaRev+=lRevocaCumModel.getIdBeneficioCumulo();
        
        lAlertRevoche += "<li>";
        lAlertRevoche += "  Tipo Revoca: "+lRevocaCumModel.getDescrTipoBeneficio();
        lAlertRevoche += " <a class='cliccabile' href='"+lLinkModificaRev+"' title='Modifica Revoca'>Modifica Revoca</a>";
        lAlertRevoche += "</li>";
    }
    
    lAlertRevoche+="</ul>";
    lAlertRevoche+="</li>";
    
}    

lAlertRevoche += "</ul>";

//se l'istruttoria NON è aperta non visualizzo il messaggio di alert
if (!"A".equals(IstruttoriaCumulo.getFlagStato()))
    lIsAlertRevocheBen = false;
%>


<script language="JavaScript">   
  function eseguiNavigazioneInclude(azione) {
    if (azione=="null"){
      alert("funzione in fase di implementazione");
      return;
    }    
   
    // 30/04/2019  MEV70 In caso di selezione di "Pene Rideterminate" o di "Calcolo Pena", se in Istruttoria ci sono Titoli doppi, viene chiesta conferma.
    var titDoppi = <%=titoliDoppi.size()%>;
    if (azione=="siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate"	||
    	azione=="siap.siep.modulocumulo.action.ActDettaglioPenaCumulo")	{
    	if ( titDoppi > 0 )
   	 	{
 	      	var msgConfirm = "Attenzione! In Istruttoria Cumulo sono presenti più Procedimenti\n con i seguenti estremi del Titolo Esecutivo:";
<%			for(int i=0; i<titoliDoppi.size(); i++)  {	 %> 
				msgConfirm+='\n'+'<%=titoliDoppi.get(i).toString()%>';
<%			} %>

			msgConfirm+=".\n\n Si vuole procedere ?";
			if (window.confirm(msgConfirm)) {
			}else {
				return;
			}
       		
       	}
    }
    document.formNavigazioneDatiFinali.<%=IWebConstants.ACTION_FIELD%>.value = azione;
    document.formNavigazioneDatiFinali.submit();      
  }
  
</script>  

  <script language="JavaScript1.2">
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formNavigazioneDatiFinali">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

<input type="hidden" name="FunzioneMenu" value="<%=FunzioneMenu%>">


    <table cellspacing="2" cellpadding="4" width="95%" align="center"
           onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" 
           onMousedown="over_effect(event,'inset')"  onMouseup="over_effect(event,'outset')">    
    
      <tr><td></td></tr>
      <%
      int numTasti = 6;
      int i = (int) Math.ceil(100/numTasti);
      String larghezza = i+"%"; 
      //String larghezza = "16%"; 
      %>
      <tr>
        <td width="<%=larghezza%>" class="menulines" nowrap>
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo')"><%=lDFCPresente%>Dati Finali</a>
        </td>
        <td width="<%=larghezza%>" class="menulines" nowrap>
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActDettaglioPeneRideterminate')"><%=lPenRidetPresente%>Pene Rideterminate</a>
        </td>
        <td width="<%=larghezza%>" class="menulines" nowrap>
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni')"><%=lUltSanPresente%>Ulteriori sanzioni</a>
        </td>
        <td width="<%=larghezza%>" class="menulines" nowrap>
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActDettaglioPosGiuridicaCumulo')"><%=lPosPresente%>Posizione giuridica</a>
        </td>
        <td width="<%=larghezza%>" class="menulines" nowrap <%=lColorBTPena%> >
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActDettaglioPenaCumulo')"><%=lCalcPenaPresente%>Calcolo Pena<%=lAsteriscoPena%></a>
        </td>
        <td width="<%=larghezza%>" class="menulines" nowrap <%=lColorBTProvv%> >
          <a href="javascript:eseguiNavigazioneInclude('siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo')"><%=lProvvPresente%>Emissione Provvedimento<%=lAsteriscoProvv%></a>
        </td>
      </tr>
      <% if (lIsCalcoloDaRieffettuare) {%>
      <tr>
        <td colspan="100%">
          <font class="cRosso">(*) Attenzione! I dati che concorrono a calcolo pena sono stati modificati. Rieffettuare il calcolo.</font>
        </td>
      </tr>
      <% } %>
      <% if (lIsProvvIncoerente) {%>
      <tr>
        <td colspan="100%">
          <font class="cRosso">(*) Attenzione! Il Provvedimento non è più coerente con la posizione giuridica. Aggiornare il provvedimento.</font>
        </td>
      </tr>
      <% } %> 
      
      <% if(lEscluso){ %>
         <tr>
           <td colspan="100%">
             <font class="cRosso">(*) Attenzione, uno o più Titoli iscritti in istruttoria risulta momentaneamente escluso. Per procedere alla emissione del provvedimento di cumulo è necessario prima provvedere ad escludere o includere definitivamente detto Titolo.</font>
           </td>
         </tr>
      <% } %>    
      
      <% if(lIsAlertContinuazioni) { %>
         <tr>
           <td colspan="100%">
             <font class="cRosso">(*) <%=lAlertContinuazioni %></font>
           </td>
         </tr>
      <% } %> 

      <% if(lIsAlertRevocheBen) { %>
         <tr>
           <td colspan="100%">
             <font class="cRosso">(*) <%=lAlertRevoche %></font>
           </td>
         </tr>
      <% } %>
      
      <!-- MEV_2025-48 - ALTRO -->
      <% if(lIsAlertMisureSicurezza) { %>
         <tr>
           <td colspan="100%">
             <font class="cRosso">(*) <%=lStrAlertMisureSicurezza %></font>
           </td>
         </tr>
      <% } %>
      
    </table>
  </form>
