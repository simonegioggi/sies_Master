<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliUlterioriSanzioni"%>

<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiComputiCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>


<%@ page import="f3b.log.LogF3B"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>


<jsp:useBean id="ListaMisureSicurezza"        scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaPeneAccessorie"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaSanzioniAmministrative" scope="request" class="java.util.Vector"/>

<jsp:useBean id="fascicoloEsecMS" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>


<%
//==============================================================================
// JSP per la visualizzazione del dettaglio delle Altre Sanzioni in Cumulo
//
//==============================================================================
DatiFinaliCumuloModel lDatiFinaliCumulo = datiFinaliAggregatoModel.getDatiFinaliCumulo();
BigDecimal idFascMS = new BigDecimal(-1);
if ("S".equals (lDatiFinaliCumulo.getFlagCreaFascicoloMs() ) ) {
  if (lDatiFinaliCumulo.getFasSieIdFascicoloSiepMs()!=null)
    idFascMS = lDatiFinaliCumulo.getFasSieIdFascicoloSiepMs();
  else
    idFascMS = new BigDecimal(0);
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
      document.formUlterioriSanzioni.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formUlterioriSanzioni.<%=ICostantiModuloCumulo.MODALITA%>.value = "<%=ICostantiModuloCumulo.MODALITA_MODIFICA%>";
      document.formUlterioriSanzioni.submit();
    }

    function eseguiAzioneMisureSicurezza (aTipoAzione, aIdIdentita, aIdTitolo)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadDettaglioMisuraSicurezzaCumulo";
        document.formMisuraSicurezza.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>.value = aIdIdentita;
        document.formMisuraSicurezza.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        
        document.formMisuraSicurezza.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formMisuraSicurezza.submit();
      }
      else if (aTipoAzione=='Modifica'){
        // non previsto per ora
        return;
      }
      else if (aTipoAzione=='Cancella'){
        // non previsto per ora
        return;      
      }
    }

    
    function eseguiAzionePeneAccessorie (aTipoAzione, aIdIdentita, aIdTitolo)
    {
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioPenaAccessoriaCumulo";
        document.formPeneAccessorie.<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>.value = aIdIdentita;
        document.formPeneAccessorie.<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>.value = aIdTitolo;
        
        document.formPeneAccessorie.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formPeneAccessorie.submit();
      }
      else if (aTipoAzione=='Modifica'){
        return;
        lAzione = "siap.siep.modulocumulo.action.ActLoadInsRichiesteComputiDatiFinali";
        document.form.<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>.value = aIdIdentita;
        document.form.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.form.modalita.value = "M";
        document.form.submit();
      }
      else if (aTipoAzione=='Cancella'){
        return;      
      }
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
    
    // Effettua l'opportuna 
    function eseguiNavigazione(azione) {
      //alert("eseguiNavigazione: "+azione);
      
      document.formUlterioriSanzioni.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formUlterioriSanzioni.submit();
      
    }
    
    $(document).ready(function(){
      //
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
        <font class="campo">Dati Finali Cumulo - Ulteriori Sanzioni</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/NavigazioneDatiFinaliCumulo.jsp"/>
  <br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formMisuraSicurezza">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="">
  
  <input type="hidden" name="modalita" value="">
</form> 

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formPeneAccessorie">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
  <input type="hidden" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="">
  
  <input type="hidden" name="modalita" value="">
</form> 

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formRichiesteAlGE">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiComputiCumulo.CAMPO_ID_COMPUTI_CUMULO%>" value="">
  
  <input type="hidden" name="modalita" value="">
</form>  
  
<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formUlterioriSanzioni">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">


<%
//==============================================================================
//                         Griglia altre sanzioni
//==============================================================================
%>

<div id="divAltreSanzioni">
<%
//==============================================================================
// Elenco Misure di sicurezza - Da visualizzare solo se presenti
//==============================================================================
%>

  <table cellspacing="2" cellpadding="2" width="800px">
    <tr><td class="Titolo" colspan="7" >MISURE DI SICUREZZA</td></tr>
    <tr><td colspan="6"></td></tr>
    <tr>      
      <td class="Titolo" nowrap><font class="label">Natura Misura</font></td>
      <td class="Titolo" nowrap><font class="label">Tipo Misura</font></td>
      <td class="Titolo" nowrap><font class="label">Anni</font></td>
      <td class="Titolo" nowrap><font class="label">Mesi</font></td>
      <td class="Titolo" nowrap><font class="label">Giorni</font></td>
      <td class="Titolo" nowrap><font class="label">Azioni</font></td>
    </tr>
    
    <% 
    if (ListaMisureSicurezza.size()==0) 
    {%>
    <tr>
      <td class="L" colspan="7"><font class="campoLow">Nessuna misura presente/selezionata</font></td>
    </tr>
    <% 
    } else {
      Iterator itx = ListaMisureSicurezza.iterator();
      while ( itx.hasNext()) 
      {
        MisuraSicurezzaCumuloModel lMisuraSicurezzaCumulo = (MisuraSicurezzaCumuloModel)itx.next();
    
      %>
      <tr>
        <td class="c"><font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrNatura(),"&nbsp;")%></font></td>
        <td class="c"><font class="campoLow"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrTipo(),"&nbsp;")%></font></td>
        <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumAnni(),"&nbsp;")%></font></td>
        <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumMesi(),"&nbsp;")%></font></td>
        <td class="c"><font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumGiorni(),"&nbsp;")%></font></td>
        
        <td class="azioni c">
          <a href="javascript:eseguiAzioneMisureSicurezza('Dettaglio',<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo())%>,<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getTitIdTitoloCumulato())%> )">
            <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
          <%--
          <a href="javascript:eseguiAzione('Modifica',<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo())%>)">
            <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
          <a href="javascript:eseguiAzione('Cancella',<%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo())%>,'I','')">
            <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
          --%>
        </td>
      </tr>
      <% } // end while%>
      
      <% if ( idFascMS.compareTo(new BigDecimal(0))>=0) {%>
      <tr>
        <td class="L" colspan="7">
          <!--Iscrivi sul procedimento <font class="cRosso">2015/40033</font> di questo ufficio-->
          <%-- MEV_2025-48 - ALTRO --%>
          <% if (   idFascMS.compareTo(new BigDecimal(0))==0 
                 && "S".equals(datiFinaliAggregatoModel.getDatiFinaliCumulo().getFlagCreaFascicoloMs())) {%>
          <font class="cRosso">Da Iscrivere su nuovo procedimento di Classe IV</font>
          <% } else { %>
          Da aggiungere al procedimento <font class="cRosso"><%=fascicoloEsecMS.getChiaveAnno()%>/<%=fascicoloEsecMS.getChiaveProgr()%></font> di questo ufficio
          <% } %>
        </td>
      </tr>
      <% } %>
      
      <%-- MEV_2025-48 - ALTRO --%>
      <%
      if (   datiFinaliAggregatoModel.getListaMisureSicurezza()!=null 
          && datiFinaliAggregatoModel.getListaMisureSicurezza().size()>0
         )
      {
          if (datiFinaliAggregatoModel.getDatiFinaliCumulo().getFlagCreaFascicoloMs()==null)
          {%>      
	      <tr>
	        <td class="L" colspan="7">
	          <img src='/images/attenzione.jpg' style='border:0px; width:15px; height:15px;'> 
	          <font class="cRosso">Non è stato ancora indicato se le Misure di Sicurezza selezionate vanno o meno iscritte in un fascicolo</font>
	        </td>
	      </tr>
          <% } else if ("N".equals(datiFinaliAggregatoModel.getDatiFinaliCumulo().getFlagCreaFascicoloMs())) { %>
          <tr>
            <td class="L" colspan="7">
              <font class="cRosso">Non iscrivere le MS a Procedimento classe IV</font>
            </td>
          </tr>      
          <% } %>
       <% } %>
      <%-- MEV_2025-48 - ALTRO --%>
   <% } %>
  </table>
  <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
  <table>
    <tr>
      <td>
        <input type="button" class="bottone" name="Seleziona Misura Sicurezza" value="Seleziona Misura Sicurezza" onClick="eseguiNavigazione('siap.siep.modulocumulo.action.ActLoadSelezionaMisuraSicurezzaDatiFinali')">
      </td>
    </tr>
  </table>
  <% } %>

<%
//==============================================================================
// Elenco Pene accessorie - Da visualizzare solo se presenti
//==============================================================================
%>
  <br>
  <table cellspacing="2" cellpadding="2" width="800px">
    <tr><td class="Titolo" colspan="6" >PENE ACCESSORIE</td></tr>
    <tr>
      <td class="Titolo">Tipo Pena</td>
      <td class="Titolo">Tipo Durata</td>
      <td class="Titolo">Anni</td>
      <td class="Titolo">Mesi</td>
      <td class="Titolo">Giorni</td>
      <!--td class="Titolo">Comunicazione eseguita in data</td-->
      <td class="Titolo azioni">Azioni</td>
    </tr>
    <% 
    if (ListaPeneAccessorie.size()==0) 
    {%>
    <tr>
      <td class="L" colspan="7"><font class="campoLow">Nessuna Pena Accessoria presente/selezionata</font></td>
    </tr>
    <% 
    } else {
      Iterator itx = ListaPeneAccessorie.iterator();
      while ( itx.hasNext()) 
      {
        PenaAccessoriaCumuloModel lPenaAccessoriaCumulo = (PenaAccessoriaCumuloModel)itx.next();
    
      %>    
    <tr>
      <td class="L"><font class="campoLow">&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getDescrTipoPenaAccessoria(),"&nbsp;")%></font></td>
      <td class="c"><font class="campoLow">&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getDescrDurata(),"&nbsp;")%></font></td>
      <td class="c"><font class="campoLow">&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumAnni(),"&nbsp;")%></font></td>
      <td class="c"><font class="campoLow">&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumMesi(),"&nbsp;")%></font></td>
      <td class="c"><font class="campoLow">&nbsp;<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumGiorni(),"&nbsp;")%></font></td>
      <!--td class="c"><font class="campoLow">10/09/2015</font></td-->
      <td class="azioni c">
        <a href="javascript:eseguiAzionePeneAccessorie('Dettaglio',<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo())%>,<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getTitIdTitoloCumulato())%> )">
          <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        <%--
        <a href="javascript:eseguiAzionePeneAccessorie('Modifica',<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo())%>,<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getTitIdTitoloCumulato())%>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzionePeneAccessorie('Cancella',<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo())%>,<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getTitIdTitoloCumulato())%>)">
          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a> 
        --%>
      </td>
    </tr> 
    <%} // end while %>
  <%} // end if %>
  </table>
  <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
  <table>
    <tr>
      <td>
        <input type="button" class="bottone" name="Seleziona Pene Accessorie" value="Seleziona Pene Accessorie" onClick="eseguiNavigazione('siap.siep.modulocumulo.action.ActLoadSelezionaPenaAccessoriaDatiFinali')">
      </td>
    </tr>
  </table>
  <% } %>
  
  
<%
//==============================================================================
// Elenco Sanzioni Amministrative - Da visualizzare solo se presenti
//==============================================================================
%>
<%--
PER ORA NON PRESENTI IN STEP1
  <br>
  <table cellspacing="2" cellpadding="2" width="800px">
    <tr><td class="Titolo" colspan="7" >SANZIONI AMMINISTRATIVE</td></tr>
    <tr>
      <td class="Titolo">Tipo Sanzione</td>
      <td class="Titolo">Tipo Durata</td>
      <td class="Titolo">Anni</td>
      <td class="Titolo">Mesi</td>
      <td class="Titolo">Giorni</td>
      <td class="Titolo azioni">Azioni</td>
    </tr>
    <% 
    if (ListaSanzioniAmministrative.size()==0) 
    {%>
    <tr>
      <td class="L" colspan="7"><font class="campoLow">Nessuna Sanzione Amministrativa presente/selezionata</font></td>
    </tr> 
    <% } else { %>
    <tr>
      <td class="c"><font class="campoLow">Chiusura definitiva dell'esercizio</font></td>
      <td class="c"><font class="campoLow">Durante la pena</font></td>
      <td class="c"><font class="campoLow">&nbsp;</font></td>
      <td class="c"><font class="campoLow">2</font></td>
      <td class="c"><font class="campoLow">&nbsp;</font></td>
      <td class="c"><font class="campoLow">10/09/2015</font></td>
      <td class="azioni c">
        <a href="javascript:eseguiAzione('Dettaglio',12232015 )">
          <img src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" alt="Dettaglio Misura" border="0"></a>
        <a href="javascript:eseguiAzione('Modifica',12232015)">
          <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella',12232015,'I','')">
          <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
      </td>
    </tr>
    <% } %>
  </table>
  <% if (IstruttoriaCumulo.getFlagStato().equals("A") ){ %>
  <table>
    <tr>
      <td>
        <input type="button" class="bottone" name="Seleziona Sanzioni Amministrative" value="Seleziona Sanzioni Amministrative" onClick="eseguiNavigazione('siap.siep.modulocumulo.action.ActLoadInserisciSanzioniAmministrativeCumulo')">
      </td>
    </tr>
  </table>
  <% } %>
--%>

</div>
  
  
  </form>
</div>
</body>

</html>