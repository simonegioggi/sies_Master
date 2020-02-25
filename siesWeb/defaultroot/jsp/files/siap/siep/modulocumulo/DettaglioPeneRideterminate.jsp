<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliUlterioriSanzioni"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel "%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel"%>

<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"        scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="ListaRichiesteAlGE"       scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// JSP per la visualizzazione del dettaglio delle Pene Rideterminate in Cumulo
//
// n.b. per ora visualizza anche il TEST dettaglio Ulteriori sanzioni.
//      tali dettagli vanno spostati in apposita jsp
//==============================================================================


PenaRideterminataCumuloModel lPenaRideterminataCumulo = new PenaRideterminataCumuloModel(); 

if (datiFinaliAggregatoModel.getPenaRideterminataCumulo()!=null) {
  lPenaRideterminataCumulo = datiFinaliAggregatoModel.getPenaRideterminataCumulo();  
}


%>

<html>
<head>
  <title> [S.I.E.S.] - Dati Finali Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
  
    function loadModifica (azione) {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formName.<%=ICostantiModuloCumulo.MODALITA%>.value = "<%=ICostantiModuloCumulo.MODALITA_MODIFICA%>";
      document.formName.submit();
    }    

    function eseguiAzioneRichiesteGE (aTipoAzione, aIdIdentita)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiesteComputiDatiFinali";
        document.formRichiesteAlGE.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdIdentita;
        document.formRichiesteAlGE.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formRichiesteAlGE.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiesteComputiDatiFinali";
        document.formRichiesteAlGE.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdIdentita;
        document.formRichiesteAlGE.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formRichiesteAlGE.modalita.value = "M";
        document.formRichiesteAlGE.submit();
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActInsRichiesteComputiDatiFinali";
          document.formRichiesteAlGE.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdIdentita;
          document.formRichiesteAlGE.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.formRichiesteAlGE.modalita.value = "C";

          document.formRichiesteAlGE.submit();
        }        
      }
    } 
    
    
    //  
    function eseguiNavigazione(azione) {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formName.submit();
      
    }
    
    $(document).ready(function(){
      $('#divPeneRideterminate').show();
    });
    
    
    
  </script>

</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dati Finali Cumulo - Pene Determinate in cumulo</font>
      </td>
      <% if (IstruttoriaCumulo.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA) ){ %>
      <td class="LBG">
        <a href="javascript:loadModifica('siap.siep.modulocumulo.action.ActLoadInserisciPeneRideterminate')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <% } %>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>
  
  
  
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formRichiesteAlGE">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="">
  
  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="">
</form>  
  
  
<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="">
    
<%
//==============================================================================
//                      Dettaglio Pene rideterminate
//==============================================================================
%>
<div id="divPeneRideterminate">
  <table cellspacing="0" cellpadding="0" width="800px">
    <tr><td class="Titolo" colspan="1" >PENE DETERMINATE IN CUMULO</td></tr>
    
    <tr>
      <td>
        <table cellspacing="2" cellpadding="2" width="100%">
          <tr>
            <td class="Titolo" colspan="9">Pena Detentiva</td>
          </tr>
          
          <tr>
            <td class="L">Reclusione:</td>
            <td class="L">Anni</td>
            <td class="L" style="width:50px"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniReclusione()) %></font>&nbsp;</td>
            <td class="L">Mesi</td>
            <td class="L" style="width:50px"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiReclusione()) %></font>&nbsp;</td>
            <td class="L">Giorni</td>
            <td class="L" style="width:50px"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniReclusione()) %></font>&nbsp;</td>
            <td class="L">Multa</td>
            <td class="L" style="width:200px"><font class="campo"><%=(lPenaRideterminataCumulo.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaRideterminataCumulo.getImportoMulta()) %></font>&nbsp;</td>
          </tr>
          <tr>
            <td class="L">Arresto:</td>
            <td class="L">Anni</td>
            <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniArresto()) %></font>&nbsp;</td>
            <td class="L">Mesi</td>
            <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiArresto()) %></font>&nbsp;</td>
            <td class="L">Giorni</td>
            <td class="L"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniArresto()) %></font>&nbsp;</td>
            <td class="L">Ammenda</td>
            <td class="L"><font class="campo"><%=(lPenaRideterminataCumulo.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaRideterminataCumulo.getImportoAmmenda()) %></font>&nbsp;</td>
          </tr>
          <% if (lPenaRideterminataCumulo.isErgastolo()) {%>
          <tr>
            <td class="L">Ergastolo:</td>
            <td class="L" colspan="8">
              <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getDescrTipoPenaDetentiva()) %></font>&nbsp;
              <% if ("04".equals(lPenaRideterminataCumulo.getCodTipoPenaDetentiva())) { %>
              Durata Isolamento Diurno: Anni <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumAnniIsolamentoDiurno(),"-") %></font>
                                        Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumMesiIsolamentoDiurno(),"-") %></font>
                                        Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumGiorniIsolamentoDiurno(),"-") %></font>
              <% } %>
            </td>
          </tr> 
          <% } %>
        </table>
        
        
        <%
        //======================================================================
        // Sanzioni sostitutive
        //======================================================================
        %>
        <table cellspacing="2" cellpadding="2" width="100%">
          <tr>
            <!--td class="int" colspan="2">SANZIONI SOSTITUTIVE</td-->
            <td class="titolo" colspan="2">Sanzioni Sostitutive</td>
          </tr>
          
          <% if (datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione()!=null) { %>
          <tr>
            <td class="L" width="200px">Semidetenzione: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosSemidetenzione().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
          <% if (datiFinaliAggregatoModel.getUltSanSanSosLibertContrl()!=null) { %>
          <tr>
            <td class="L" width="200px">Libertà Controllata: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLibertContrl().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLibertContrl().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLibertContrl().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
          <% if (datiFinaliAggregatoModel.getUltSanSanSosPPMulta()!=null) { %>
          <tr>
            <td class="L" width="200px">Pena Pecuniaria: </td>
            <td class="L">
              Multa <font class="campo"><%=StringUtils.toEuroFormat(datiFinaliAggregatoModel.getUltSanSanSosPPMulta().getMulta()) %></font>
            </td>
          </tr>
          <% } %> 
          <% if (datiFinaliAggregatoModel.getUltSanSanSosPPAmmenda()!=null) { %>
          <tr>
            <td class="L" width="200px">Pena Pecuniaria: </td>
            <td class="L">
              Ammenda <font class="campo"><%=StringUtils.toEuroFormat(datiFinaliAggregatoModel.getUltSanSanSosPPAmmenda().getAmmenda()) %></font>
            </td>
          </tr>
          <% } %> 
          <% if (datiFinaliAggregatoModel.getUltSanSanSosEspulsione()!=null) { %>
          <tr>
            <td class="L" width="200px">Espulsione dallo Stato: </td>
            <td class="L">
              <% if ("P".equals(datiFinaliAggregatoModel.getUltSanSanSosEspulsione().getFlagEspulPerp())) {%>
              <font class="campo">Perpetua</font>
              <% } else { %>
              Temporanea per Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosEspulsione().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosEspulsione().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosEspulsione().getNumGiorni(),"0") %></font>
              <% } %>
           </td>
          </tr>
          <% } %>
          
          <% if (datiFinaliAggregatoModel.getUltSanSanSosLPU()!=null) { %>
          <tr>
            <td class="L" width="200px">Lavoro Pubblica Utilità: </td>
            <td class="L"><font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getDescrTipoLpu()) %></font></td>
          </tr>
          <tr>
            <td class="L" width="200px"></td>
            <td class="L">Nella misura di: Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumGiorni(),"0") %></font>
              <% if (datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumOreTot()!=null) {%>
              Pari ad ore complessive <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumOreTot()) %></font>
              <% } %>
            </td>
          </tr>
          <tr>
            <td class="L" width="200px"></td>
            <td class="L">Ore di lavoro Settimanali da svolgere: <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanSanSosLPU().getNumOreSett()) %></font>
                          Frequenza Settimanale: <font class="campo"><%=datiFinaliAggregatoModel.getUltSanSanSosLPU().getCodFreqSett().compareTo(new BigDecimal(ICostantiDatiFinaliUlterioriSanzioni.VAL_COD_FREQ_NON_DET))==0?"Non Determinata":"Determinata"%></font>
          </tr>
            <% if (   datiFinaliAggregatoModel.getUltSanSanSosLPU().getListaOrariLPU()!=null 
                   && datiFinaliAggregatoModel.getUltSanSanSosLPU().getListaOrariLPU().size()>0 
                  ) 
               { %>
            <tr> 
              <td class="L" width="200px"></td>
              <td class="L">
                <table id="tabOrarioLPU" cellspacing="2" cellpadding="4" width="100%">
                <%
                DatiFinaliUlterioriSanzioniModel lUltSan_SanSos_LPU = datiFinaliAggregatoModel.getUltSanSanSosLPU();
                int day = 1;
                while (day<=4){
                  String dayCol1 = "0"+day;
                  String dayCol2 = "0"+(day+4);
                %>  
                    <tr>
                      <td class="l">
                        <%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioGiorno(dayCol1))%>
                      </td>
                      <td class="l">
                        <% if (lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol1)!=null) { %>
                        dalle ore&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol1),"--")%></font>
                        alle ore&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioAlleOre(dayCol1),"--")%></font>
                        <% } else {%>
                        &nbsp;
                        <% } %>
                      </td>
                      <%  if (day<4) { %>
                      <td class="l"> 
                        <%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioGiorno(dayCol2))%>
                      </td>
                      <td class="l">
                        <% if (lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol2)!=null) { %>
                        dalle ore&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioDalleOre(dayCol2),"--")%></font>
                        alle ore&nbsp;<font class="campo"><%=StringUtils.toStringJSP(lUltSan_SanSos_LPU.getTipologiaOrarioAlleOre(dayCol2),"--")%></font>
                        <% } else {%>
                        &nbsp;
                        <% } %>      </td>
                      <% } else { %>
                      <td class="l"></td>
                      <td class="l"></td>
                      <% } %>
                    </tr>
                <%
                  day++;
                } //end while
                %>
                </table>
              </td>
            </tr>
            <% } %>
          <% } %>
        </table>
<br>

        <%
        //======================================================================
        // Liberazione Anticipata
        //======================================================================
        %>
        <table cellspacing="2" cellpadding="2" width="100%">
          <tr>
            <td class="titolo" colspan="9">Liberazione Anticipata Concessa da Detrarre dal Cumulo</td>
          </tr>
          <% 
          if (   (lPenaRideterminataCumulo.getNumeroGiorniLA()!=null && lPenaRideterminataCumulo.getNumeroGiorniLA().intValue()>0)
              || (lPenaRideterminataCumulo.getNumeroGiorniLS()!=null && lPenaRideterminataCumulo.getNumeroGiorniLS().intValue()>0)
              || (lPenaRideterminataCumulo.getNumeroGiorniLI()!=null && lPenaRideterminataCumulo.getNumeroGiorniLI().intValue()>0)
              || (lPenaRideterminataCumulo.getNumeroGiorniRiduzione()!=null && lPenaRideterminataCumulo.getNumeroGiorniRiduzione().intValue()>0)
              || (lPenaRideterminataCumulo.getNumeroGiorniScomputo()!=null && lPenaRideterminataCumulo.getNumeroGiorniScomputo().intValue()>0)
             ) 
          { %>
          <tr>
            <td></td>
            <td class="c" style="width:100px">Ordinaria</td>
            <td class="c" style="width:100px">Speciale</td>
            <td class="c" style="width:130px">Integrazione</td>
            <td class="c" style="width:130px">D.L.92/2014</td>
            <td class="c" style="width:100px">Scomputi</td>
          </tr>          
          <tr>
            <td class="l">
              <font class="label">Totale Liberazione Anticipata (giorni):</font>
            </td>
            <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLA(),"-") %></font>&nbsp;</td>
            <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLS(),"-") %></font>&nbsp;</td>
            <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniLI(),"-") %></font>&nbsp;</td>
            <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniRiduzione(),"-") %></font>&nbsp;</td>
            <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lPenaRideterminataCumulo.getNumeroGiorniScomputo(),"-") %></font>&nbsp;</td>
          </tr>
          <% } %> 
        </table>
<br>
        
        <%
        //======================================================================
        // Pena da conversione Pena Pecuniaria
        //======================================================================
        %>
        <table cellspacing="2" cellpadding="2" width="100%">
          <tr>
            <!--td class="int" colspan="9">Pena da conversione Pena Pecuniaria</td-->
            <td class="titolo" colspan="9">Pena da conversione Pena Pecuniaria</td>
          </tr>
          <% if (datiFinaliAggregatoModel.getUltSanConvPPLavSost()!=null) { %>
          <tr>
            <td class="L" width="200px">Lavoro Sostitutivo: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLavSost().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLavSost().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLavSost().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
          <% if (datiFinaliAggregatoModel.getUltSanConvPPLibCtrl()!=null) { %>
          <tr>
            <td class="L" width="200px">Libertà controllata: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLibCtrl().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLibCtrl().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanConvPPLibCtrl().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
        </table>
<br>
        <%
        //======================================================================
        // Sanzioni del giudice di pace
        //======================================================================
        %>
        <table cellspacing="2" cellpadding="2" width="100%">
          <tr>
            <!--td class="int" colspan="9">Sanzioni del giudice di pace</td-->
            <td class="titolo" colspan="9">Sanzioni del giudice di pace</td>
          </tr> 
          <% if (datiFinaliAggregatoModel.getUltSanGiuPacePermDom()!=null) { %>
          <tr>
            <td class="L" width="200px">Permanenza Domiciliare: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPacePermDom().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPacePermDom().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPacePermDom().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
          <% if (datiFinaliAggregatoModel.getUltSanGiuPaceLPU()!=null) { %>
          <tr>
            <td class="L" width="200px">Lavoro pubblica utilità: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLPU().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLPU().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLPU().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>          
          <% if (datiFinaliAggregatoModel.getUltSanGiuPaceLavSost()!=null) { %>
          <tr>
            <td class="L" width="200px">Lavoro sostitutivo: </td>
            <td class="L">
              Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLavSost().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLavSost().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceLavSost().getNumGiorni(),"0") %></font>
            </td>
          </tr>
          <% } %>
          <% if (datiFinaliAggregatoModel.getUltSanGiuPaceESP()!=null) { %>
          <tr>
            <td class="L" width="200px">Espulsione dallo Stato: </td>
            <td class="L">
              <% if ("P".equals(datiFinaliAggregatoModel.getUltSanGiuPaceESP().getFlagEspulPerp())) {%>
              <font class="campo">Perpetua</font>
              <% } else { %>
              Temporanea per Anni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceESP().getNumAnni(),"0") %></font>
              Mesi <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceESP().getNumMesi(),"0") %></font>
              Giorni <font class="campo"><%=StringUtils.toStringJSP(datiFinaliAggregatoModel.getUltSanGiuPaceESP().getNumGiorni(),"0") %></font>
              <% } %>
           </td>
          </tr>
          <% } %>
        </table>
      </td>
    </tr>   
  </table>
  
<%
//==============================================================================
// Elenco Richieste al GE - Da visualizzare solo se presenti
//==============================================================================
%>
  <br>
  <table cellspacing="2" cellpadding="2" width="800px">
    <tr><td class="Titolo" colspan="7" >Richieste Al Giudice dell’Esecuzione Con Anticipazione Degli Effetti</td></tr>
    <tr>
      <td class="titolo">Tipo Richiesta</td>
      <td class="titolo">+/-</td>
      <td class="titolo">Reclusione</td>
      <td class="titolo">Multa</td>
      <td class="titolo">Arresto</td>
      <td class="titolo">Ammenda</td>
      <td class="titolo azioni">Azioni</td>
    </tr>

    <% 
    if (ListaRichiesteAlGE.size()==0) 
    {%>
    <tr>
      <td class="L" colspan="7"><font class="campoLow">Nessuna Richiesta presente</font></td>
    </tr>
    <% 
    } else {
      Iterator itx = ListaRichiesteAlGE.iterator();
      while ( itx.hasNext()) 
      {
        ComputiCumuloModel lComputoCumulo = (ComputiCumuloModel)itx.next();
        
        String lStrReclusione = "";
        if (!lComputoCumulo.isQuantumReclusioneZero()){
          lStrReclusione+="Anni: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumAnniReclusione(),"0")+"</font>";
          lStrReclusione+=" Mesi: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumMesiReclusione(),"0")+"</font>";
          lStrReclusione+=" Giorni: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumGiorniReclusione(),"0")+"</font>";
        }
        else {
          lStrReclusione = "&nbsp;";
        }
        
        String lStrMulta = "";
        if (!lComputoCumulo.isMultaZero()) {
          lStrMulta = "<font class='campo'>"+StringUtils.toEuroFormat (lComputoCumulo.getImportoMulta())+"</font> Euro";
        }
        else {
          lStrMulta = "&nbsp;";
        }
        
        
        String lStrArresto = "";
        if (!lComputoCumulo.isQuantumArrestoZero()){
          lStrArresto+="Anni: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumAnniArresto(),"0")+"</font>";
          lStrArresto+=" Mesi: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumMesiArresto(),"0")+"</font>";
          lStrArresto+=" Giorni: <font class='campo'>"+StringUtils.toStringJSP (lComputoCumulo.getNumGiorniArresto(),"0")+"</font>";
        }
        else {
          lStrArresto = "&nbsp;";
        }   
        
        String lStrAmmenda = "";
        if (!lComputoCumulo.isAmmendaZero()) {
          lStrAmmenda = "<font class='campo'>"+StringUtils.toEuroFormat (lComputoCumulo.getImportoAmmenda())+"</font> Euro";
        }
        else {
          lStrAmmenda = "&nbsp;";
        }
        
      %>   
        
    <tr>
      <td class="c"><%=StringUtils.toStringJSP(lComputoCumulo.getDescrTipoAnnotazione()) %>&nbsp;</td>
      
      <td class="c"><%=StringUtils.toStringJSP(lComputoCumulo.getFlagPiuMeno()) %></td>
      
      <td class="c"><%=StringUtils.toStringJSP(lStrReclusione) %>&nbsp;</td>
      <td class="r"><%=StringUtils.toStringJSP(lStrMulta) %>&nbsp;</td>
      <td class="c"><%=StringUtils.toStringJSP(lStrArresto) %>&nbsp;</td>
      <td class="r"><%=StringUtils.toStringJSP(lStrAmmenda) %>&nbsp;</td>
      <td class="azioni c">
        <a href="javascript:eseguiAzioneRichiesteGE('Dettaglio',<%=StringUtils.toStringJSP(lComputoCumulo.getIdComputiCumulo()) %>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
        <a href="javascript:eseguiAzioneRichiesteGE('Modifica',<%=StringUtils.toStringJSP(lComputoCumulo.getIdComputiCumulo()) %>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzioneRichiesteGE('Cancella',<%=StringUtils.toStringJSP(lComputoCumulo.getIdComputiCumulo()) %>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
        <% } %>
      </td>
    </tr>    
      <% } // END While %>
    <% } // END if %>
  </table>  
  <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
  <table>
    <tr>
      <td>
        <input type="button" class="bottone" name="Richieste al GE" value="Aggiungi Richieste al GE" onClick="eseguiNavigazione('siap.siep.modulocumulo.action.ActLoadInsRichiesteComputiDatiFinali')">
      </td>
    </tr>
  </table>  
  <% } %>
  
</div>  <!-- divPeneRideterminate -->
  </form>
</div>  <!-- divPosizionamento -->
</body>

</html>