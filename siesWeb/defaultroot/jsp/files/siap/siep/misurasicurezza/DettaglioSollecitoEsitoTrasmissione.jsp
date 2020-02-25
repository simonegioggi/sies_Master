<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>


<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="residenza"           scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"          scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"         scope="request" class="java.util.Vector"/>

<% // %>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="sollecitoEsito"      scope="request" class="siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel"/>
<jsp:useBean id="ufficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>


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
  <title>[S.I.E.S.] - Gestione Misure di Sicurezza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>


<body class="corpo">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">DETTAGLIO SOLLECITO ESITO TRASMISSIONE PER COMPETENZA ESECUZIONE MISURE DI SICUREZZA</font>
      </td>
  
      <%if(   !"S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           && !"A".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          )
       { %>
        <!-- BOTTONE DI STAMPA -->
        <input type="hidden" name="tipo" value="D">
        <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaSollecitoTrasmissione&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
        </jsp:include>
      <%} %>
   
   
      <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE -->  
      <%
        String lModificabile = "NO";
        if(    eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
            || "N".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          ) 
        {
          lModificabile = "SI";
        }
      %>
        <td class="LBG">  
          <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=eventonotifica.getEvento().getIdEvento()%>" />
            <jsp:param name="FlagDocumentoRegistrato" value="<%=eventonotifica.getEvento().getFlagDocumentoRegistrato()%>" />
            <jsp:param name="Modificabile" value="<%=lModificabile %>" />
          </jsp:include>
        </td>
        
        <% if( "S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())) { %>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        <% } %>

    </tr>
  </table>

  <br>  
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
//*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
%>

<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Residenza attuale
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile (?) o meno)
// - Misure di sicurezza: in sentenza o in cumulo
//==============================================================================
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
          <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
              DETENUTO PER ALTRA CAUSA
          <% } else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
          <% } %>
        </font>
      </td>
    </tr>
    <%
      // Se Detenuto altra causa: Istituto di detenzione o Altro Luogo
      if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        if( lAltraCausa.getIstitutoDetenzione()!= null ) { %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
              <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
          <% }
        } // fine istituto di detenzione
      }
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null ) { %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
          </td>
        </tr>
      <%}%>
      <%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(   lPosizione.getCodPosizioneGiuridica() != null
           && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
          <% }
        }
      %>
      <%
      //========================================================================
      // Residenza se presente
      //========================================================================      
      %>
      <% if (residenza!=null && residenza.getIdResidenza()!=null) { %>
      <tr>
        <td class="l">Residenza</td>
        <td class="L"> <font class="campo" ><%=StringUtils.toStringJSP(residenza.toStringaResidenza(),"&nbsp;")%></font></td>
      </tr>
      <% } %>
      <%
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
           )
        {}
        else
        { %>
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
        <%}%>
        <%
        //===============
        // Arresti
        //===============
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {}
        else
        { %>
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
      <% }
      }  // fine IF sulla pena residua
      %>
      <%
      //==========================================================================
      // Inserisco il rigo con Data Inizio e Tipo Ergastolo (se presente)
      //==========================================================================
      %>
      <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
           <td class="l">Data Decorrenza Pena</td>
           <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if (penaresidua.getFlagErgastolo() != null) {
           if(penaresidua.getFlagErgastolo().equals("S")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
           <% }
           else if(penaresidua.getFlagErgastolo().equals("D")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
           <% }
         }
      %>
      <%
      //========================================================================
      // Inserisco la data Fine pena
      // - se non libero o comunque detenuto pre altra causa
      // - se non in ergastolo
      // - se data editabile (se lapena residua recuperata è non validata)
      //   inserisco i campi altrimenti solo label
      //========================================================================
      if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) ) {
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
          %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           </td>
          <% }
          else if( penaresidua.getDataFine() != null) {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
            %>
               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td>
            <%
            }else{%>
               <td class="l">Data Fine Pena</td>
               <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td><%
            }
          }
        }
      } %>
    </tr>
</table>
  <%
  //===================================================================
  // Misure di sicurezza
  //===================================================================
  %>
  <table>
  <% if (penacumulo!=null && penacumulo.getIdPenaCumulo()!=null) { %>
  <tr><td class="Titolo" colspan="3">Misure Sicurezza in Cumulo</td></tr>
  <tr>
    <td class="l">
      <font class="campo">        
      <%=StringUtils.toStringJSP(penacumulo.getMisuraSicurezza())%>
      </font>
    </td>
  </tr>
  <% } else if (listaMisure != null && listaMisure.size() != 0){ %>
  <tr>
    <td class="Titolo" colspan="3">Misure Sicurezza</td>
  </tr>
  <tr>
    <td class="l">
      <center><font class="label">Natura Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Tipo Misura</font></center>
    </td>
    <td class="l">
      <center><font class="label">Durata Misura</font></center>
    </td>
  </tr>
      <%
      Iterator lIterMis = listaMisure.iterator();
      while (lIterMis.hasNext())
      {
        MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIterMis.next();
      %>
        <tr>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
          </td>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
          </td>
          <td class="l">            
              <font class="l">AA:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;</font>
              <font class="l">MM:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;</font>
              <font class="l">GG:&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%></font>
            </font>
          </td>
        </tr>
      <% } %>
  <% } %>
  </table>
--%>
<%
//==============================================================================
//  DATI DELL'EVENTO
//==============================================================================
%>   
<br>
<table>   
  <tr>
    <%if(eventonotifica.getEvento().getDataEmissione()!= null){%>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
    </td>
    <%}%>
    
    <%
    if(   eventonotifica != null && eventonotifica.getNotifiche()!= null 
       && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0] != null 
       && eventonotifica.getNotifiche()[0].getDataInvio()!= null)
    {%>
      <td class="l">Data Trasmissione</td>
      <td class="L" >
        <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%></font>
      </td>
    <%}%>
    </tr>
  </table>
  
  <br>
  
  <table width=90%>
    <tr>
      <td class="Titolo" colspan="2">Ufficio Destinatario Sollecito</td>
    </tr>
      
    <tr>
      <td class="l" nowrap>Ufficio Pubblico Ministero</td>
      <td class="l">
         <font class="campo"><%=ufficioDestinatario.getDescrTipoUfficio()%></font>&nbsp;
       </td>
    </tr>
    
    <tr>
      <td class="l">Luogo</td>
      <td class="l"><font class="campo"><%=ufficioDestinatario.getDescrComune()%></font>&nbsp;</td>
    </tr>

    <!--  DATI ATTO  -->
    <tr>
      <td class="Titolo" colspan="2">Dati Atto</td>
    </tr>
      
    <tr>
      <td class="l">Oggetto</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font></td>
    </tr>
      

    <%if(eventonotifica.getCampoNote()!= null && eventonotifica.getCampoNote().length >0 && eventonotifica.getCampoNote()[0] != null && eventonotifica.getCampoNote()[0].getDescr() != null){%>
    <tr>
        <td class="l">Contenuto </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%></font>
       </td>
    </tr>
    <% } %>
    
    <tr>
      <td class="l">Magistrato Firmatario
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
        <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
  
    <% if (eventonotifica.getNotifiche().length>1) {%>
    <tr>
      <td class="Titolo" colspan="2">Ulteriori Destinatari</td>
    </tr>
    <% } %>
    
    <% 
    NotificaModel[] lNotifiche = eventonotifica.getNotifiche();
    for (int i=0;i< lNotifiche.length;i++) {
      NotificaModel lNotifica = lNotifiche[i];
    %>

      <%      
      if (   "C".equals(lNotifica.getCodTipoNotifica())
          && lNotifica.getUffCodUfficio()!=null
          && lNotifica.getUfficio()!=null
          )
      {
      %>
      <tr>
        <td class="l">Magistrato di Sorveglianza
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(lNotifica.getUfficio().getDescrTipoUfficio() )%></font>
          di
          <font class="campo"><%=StringUtils.toStringJSP(lNotifica.getUfficio().getDescrComune())%></font>
        </td>
      </tr>
      <% } %>
      
      <%      
      if (   "C".equals(lNotifica.getCodTipoNotifica())
          && lNotifica.getIstDetIdIstitutoDetenzione()!=null
          && lNotifica.getIstitutoDetenzione()!=null
         )
      {
      %>
      <tr>
        <td class="l">Istituto di Detenzione
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(lNotifica.getIstitutoDetenzione().getDescrTipoIstituto() )%></font>
          di
          <font class="campo"><%=StringUtils.toStringJSP(lNotifica.getIstitutoDetenzione().getDescrComune())%></font>
        </td>
      </tr>
      <% } %>
      
      <%
      if (   "C".equals(lNotifica.getCodTipoNotifica())
          && lNotifica.getAutoritaEsterna()!=null
         )
      {
      %>
      <tr>
        <td class="l">Altro Destinatario
        <td class="L">
          <font class="campo"><%=lNotifica.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;</font>
          di 
          <font class="campo"> <%=lNotifica.getAutoritaEsterna().getDescrSede()%>&nbsp; </font>
        </td>
      </tr>
      <% } %>
    <% } // end for %>  
</table>

<br>

  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadSollecitoTrasmissione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.misurasicurezza.action.ActLoadDettaglioSollecitoEsitoTrasmissione">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>
</html>