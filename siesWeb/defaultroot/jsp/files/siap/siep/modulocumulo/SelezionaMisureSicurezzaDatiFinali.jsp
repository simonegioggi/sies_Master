<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel" %>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="datiFinaliAggregatoModel"   scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="ElencoMisureSicurezzaInIstruttoria" scope="request" class="java.util.Vector"/>

<jsp:useBean id="ElencoProcedimentiMS" scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//     FORM di Selezione delle Misure di Sicurezza da includere nel cumulo
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
// MEV_2025-48 - ALTRO
BigDecimal idFascMS = new BigDecimal(-2);
if ("S".equals (lDatiFinaliCumulo.getFlagCreaFascicoloMs() ) ) {
  if (lDatiFinaliCumulo.getFasSieIdFascicoloSiepMs()!=null)
    idFascMS = lDatiFinaliCumulo.getFasSieIdFascicoloSiepMs();
  else
    idFascMS = new BigDecimal(0);
}
//MEV_2025-48 - ALTRO
else if ("N".equals (lDatiFinaliCumulo.getFlagCreaFascicoloMs() ) ) {
  idFascMS = new BigDecimal(-1);
}

lDatiFinaliCumulo.getFasSieIdFascicoloSiepMs();

int Nmisure = 0;
if(ElencoMisureSicurezzaInIstruttoria.size() > 0 )
{
	Nmisure = 1;
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
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formIndietro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formIndietro.submit();
    }
    
    //==========================================================================
    //
    //==========================================================================
    function checkAbilitaDisabilitaCampi(checkObject){
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')){
        jQueryObj.closest('td').next('td').find('input').prop('disabled',false);
      }
      else {
        jQueryObj.closest('td').next('td').find('input').prop('checked',false);
        jQueryObj.closest('td').next('td').find('input').prop('disabled',true);
      }
    }
    
    
    function checkProcedimentoEsecuzione (checkObject) {
      var jQueryObj = $(checkObject);
      
      if (jQueryObj.prop('checked')){
        $('input[type=checkbox][name=<%=ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS%>]').each(function (index) {
          if ($(this).attr("id") != jQueryObj.attr("id"))
            $(this).prop('checked',false);              
        });

      }
    }
    
    
    function Verify()
    {
      return true;
    }
    
    
    //================================================
    // Funzione richiamata al caricamento della form
    //================================================
    $(document).ready( function() {
      //Inizilaizzazione delle check
      $('input[type=checkbox][flagDatiFinali=S]').each( function (index) {
        checkAbilitaDisabilitaCampi(this);
      });
      
      // disabilita check Nuovo Procedimento Classe IV
      var Num_Misure = <%=Nmisure%>;
      if(Num_Misure == 0)
      {
      	$('input:checkbox[name=<%=ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS %>]').prop('disabled',true);
      }	
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
        <font class="campo">Elenco Misure Sicurezza Sui Titoli Coinvolti</font>&nbsp;
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Altre Sanzioni -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActDettaglioAltreSanzioni')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formIndietro">
    <%//FORM per richiamare i dettagli in POST ma evitare di inviare inutilmente i dati della FORM principale%>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActDettaglioDatiFinaliCumulo">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  </form>
  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActSelezionaMisuraSicurezzaDatiFinali">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    <input type="hidden" name="<%=ICostantiDatiFinaliCumulo.CAMPO_ID_DATI_FINALI_CUMULO%>" value="<%=StringUtils.toStringJSP(lDatiFinaliCumulo.getIdDatiFinaliCumulo()) %>">
    
    <input type="hidden" name="modalita" value="<%=modalita%>">

<%
//==============================================================================
// 
//==============================================================================
%>

<div id="divPosizionamento" align="center" style="position:relative;">

  <table cellspacing="2" cellpadding="2" align="center" width="70%">
    <tr>
      <td class="titolo" colspan="3">Procedimento in cui iscrivere le Misure</td>
    </tr>
    <%-- MEV_2025-48 - ALTRO --%>
    <tr>
      <td class="c">
        <input type="checkbox" id="checkIdC4_NULL" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS%>" value="-1"
               <%=(idFascMS.compareTo(new BigDecimal(-1))==0)?"checked":""%>
               onClick="Javascript:checkProcedimentoEsecuzione(this)">
      </td>
      <td class="l">Non iscrivere le MS a Procedimento classe IV</td>
    </tr>
    <%-- MEV_2025-48 - ALTRO --%>
    <tr>
      <td class="c">
        <input type="checkbox" id="checkIdC4_0" 
               name="<%=ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS%>" value="0"
               <%=(idFascMS.compareTo(new BigDecimal(0))==0)?"checked":""%>
               onClick="Javascript:checkProcedimentoEsecuzione(this)">
      </td>
      <td class="l">Nuovo Procedimento classe IV</td>
    </tr>
    
    <% 
    Iterator iIterProcClasseIV  = ElencoProcedimentiMS.iterator();
    int i=0;
    while ( iIterProcClasseIV.hasNext() ) 
    {
      i++;
      ProcedimentoCumulatoModel lProcedimento = (ProcedimentoCumulatoModel) iIterProcClasseIV.next();
    %>
    <tr>
      <td class="c">
        <input type="checkbox" name="<%=ICostantiDatiFinaliCumulo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP_MS%>"  id="checkIdC4_<%=i%>"
               value="<%=StringUtils.toStringJSP(lProcedimento.getIdFascicoloSiepOrigine())%>"
               <%=(idFascMS.compareTo(lProcedimento.getIdFascicoloSiepOrigine())==0)?"checked":""%>
               onClick="Javascript:checkProcedimentoEsecuzione(this)"
               >
      </td>
      <td class="l">Aggiungi al procedimento N° 
        <font class="cRosso"><%=StringUtils.toStringJSP(lProcedimento.getChiaveAnnoFasCumulato())%>
        /<%=StringUtils.toStringJSP(lProcedimento.getChiaveProgrFasCumulato())%></font>
        di questo Ufficio
    </tr>
    <% } %>
  </table>

  
  <br>

  <table cellspacing="2" cellpadding="2" align="center" width="95%">
  
    <tr>
      <td class="titolo" colspan="11">Misure di Sicurezza sui titoli caricati in cumulo</td>
    </tr>

<%	if(ElencoMisureSicurezzaInIstruttoria.size() < 1)
	{	%>
	<tr>
      <td class="c" colspan="11" style="color:red" >Nessuna Misura di Sicurezza presente nei titoli caricati in Istruttoria</td>
    </tr>
    <br>	
<%	} %>	  
    <%
      BigDecimal lastIdTitolo = new BigDecimal (0);
      boolean isNuovoTitolo = true ;

      Iterator itx = ElencoMisureSicurezzaInIstruttoria.iterator();
      while ( itx.hasNext()) {
        MisuraSicurezzaCumuloModel lMisuraSicurezzaCumulo = (MisuraSicurezzaCumuloModel)itx.next();
        TitoloCumulatoModel lTitolo = lMisuraSicurezzaCumulo.getTitoloCumulato();
        ProcedimentoCumulatoModel lProcedimentoCumulato = lTitolo.getProcedimentoCumulato();
        
        if (lTitolo.getIdTitoloCumulato().compareTo(lastIdTitolo)!=0) {
          isNuovoTitolo = true;
        }
        else {
          isNuovoTitolo = false;
        }

        lastIdTitolo = lTitolo.getIdTitoloCumulato();

        
        // Compongo la descizione del Titolo
        String lDescrTitolo = lTitolo.getDescrTipoProvvedimento();
        if ("02".equals (lTitolo.getCodTipoProvvedimento())) {
          String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
          if (!Arrays.asList(lUfficiSorv).contains(lTitolo.getCodTipoAutoritaEmittente())){
            // Rimappo il codice per poterlo gestire nella jsp
            lDescrTitolo = "Decreto Penale";
          }
        }
        
        lDescrTitolo += " N. <font class=\"campo\">"+lTitolo.getAnnoSentenza()+" / "+lTitolo.getNumeroSentenza()+"</font>";
        
        lDescrTitolo += " Del <font class=\"campo\">"+DateUtils.getDateToString(lTitolo.getDataProvvedimento(), "dd-MM-yyyy")+"</font>";
        
        if(!lTitolo.getCodTipoProvvedimento().equals("02")) { 
          lDescrTitolo += " Emessa da: <font class=\"campo\">"+lTitolo.getDescrTipoAutoritaEmittente()+"</font>";
        } else {
          lDescrTitolo += " Emesso da: <font class=\"campo\">"+lTitolo.getDescrTipoAutoritaEmittente()+"</font>";
        }
        
        if (lTitolo.getNumSezioneAutoritaEmittente() != null) {
          lDescrTitolo += " (Sez. <font class=\"campo\">"+lTitolo.getNumSezioneAutoritaEmittente()+"</font>)";
        }
        
        lDescrTitolo += " di <font class=\"campo\">"+lTitolo.getDescrLuogoEmittente()+"</font>";
        
        if (lTitolo.getDataIrrevocabilita()!=null) {
          lDescrTitolo += " Data irrevocabilità : <font class=\"campo\">"+DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(), "dd-MM-yyyy")+"</font>";
        }

        if (lProcedimentoCumulato!=null){
          if ("S".equals (lProcedimentoCumulato.getFlagAccorpato()) ){
            lDescrTitolo += "<br> Iscritta al procedimento N° <font class=\"cRosso\">"+lProcedimentoCumulato.getChiaveAnnoFasCumulato()+"/"+lProcedimentoCumulato.getChiaveProgrOrigine()+"</font>";
            lDescrTitolo += " (<font class=\"cRosso\">Ex "+StringUtils.toStringJSP(lProcedimentoCumulato.getUfficioOrigine().getDescrTipoUfficio())+" di "+StringUtils.toStringJSP(lProcedimentoCumulato.getUfficioOrigine().getDescrComune())+"</font>)";
          } else {
            lDescrTitolo += "<br> Iscritta al procedimento N° <font class=\"cRosso\">"+lProcedimentoCumulato.getChiaveAnnoFasCumulato()+"/"+lProcedimentoCumulato.getChiaveProgrFasCumulato()+"</font>";
            lDescrTitolo += " di <font class=\"campo\">"+StringUtils.toStringJSP(lProcedimentoCumulato.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato())+"</font>";
          }
        }

        String lStato = "";
        String lDescStato = "";
        String lFontColor = "";
        String lAnnullata="";
        
        if      ( lMisuraSicurezzaCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
        
        if(lMisuraSicurezzaCumulo.getFlagAnnullaMisura()!=null && lMisuraSicurezzaCumulo.getFlagAnnullaMisura().compareTo("A")==0)
        {
          lAnnullata="SI";
          lFontColor="style=\"color:red\"";
        }
      %>

    <% if (isNuovoTitolo) { %>
    <tr>
      <td colspan="11">&nbsp;</td>
    </tr>
    <tr style="background-color: rgb(255,255,153);">
      <td class="L" colspan="11"><%=lDescrTitolo%></td>
    </tr>
    
    <tr>
      <td class="titolo" width="8%" nowrap>Natura Misura</td>
      <td class="titolo" width="30%">Tipo Misura</td>
      <td class="titolo" width="5%">Anni</td>
      <td class="titolo" width="5%">Mesi</td>
      <td class="titolo" width="5%">Giorni</td>
      <td class="titolo" width="5%">Stato</td>      
      <td class="titolo" width="10%" nowrap>Data Fine Validita</td>
      <td class="titolo" width="8%">Includi</td>
      <!--td class="titolo" width="8%" nowrap>Genera Classe IV</td-->
    </tr>    
    <% } %>
      
    <tr>
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrNatura(),"&nbsp;")%></font></td>
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrTipo(),"&nbsp;")%></font></td>
      
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumAnni(),"&nbsp;")%></font></td>
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumMesi(),"&nbsp;")%></font></td>
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumGiorni(),"&nbsp;")%></font></td>

      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrFlagStatoMisura(),"&nbsp;")%></font></td>
      <td class="c" <%=lFontColor%> >&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"dd-MM-yyyy"),"-") %></font></td>

      <td class="c" <%=lFontColor%> >
        <input type="checkbox" 
               <%=lMisuraSicurezzaCumulo.getDataFineValidita()!=null?"disabled":""%>
               <%="S".equals(lMisuraSicurezzaCumulo.getFlagDatiFinali())?"checked":""%>
               name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_DATI_FINALI%>" 
               value="<%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo()%>"
               onClick="Javascript:checkAbilitaDisabilitaCampi(this);">
      </td>
    </tr>
      <%
      }  // End While
    %>
    
  </table>
  
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td>
        <input type="submit" name="Conferma" value="Conferma">
      </td>
    </tr>
  </table>
  
</div>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

