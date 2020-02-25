<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>


<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>



<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>



<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="StrdataInizioPena"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagfungibilita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
<jsp:useBean id="motivazioni"         scope="request" class="java.lang.String"/>

<%
  EventoNotificaModel lEve = eventonotifica;

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  MagistratoModel lMagistrato = lEve.getMagistrato();
  if( lMagistrato == null)
    lMagistrato = new MagistratoModel();
%>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Evento - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  </head>
  
  
  <% 
  if(lEve.getEvento().getFlagDocumentoRegistrato()!= null)
  {
    if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
    %>
      <BODY class="corpo" onload="javascript:lookUpload();">
    <% } else { %>
      <BODY class="corpo">
    <%}%>
  <% } else { %>
     <BODY class="corpo">
  <% } %>


  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione: </font>&nbsp;
        <font class="campo">Dettaglio Variazione Decorrenza/Scadenza</font>
     </td>
     
     <% 
     // Visualizzo il tasto di stampa solo se non validato
     if (   lEve.getEvento().getFlagDocumentoRegistrato()== null 
         || (   lEve.getEvento().getFlagDocumentoRegistrato()!= null 
             && lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0
            )
        )
     {
     %>
     <!-- INIZIO INCLUDE PG_BUTTONS_STAMPA_SIEP -->
     <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
       <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaVariazioneDecorrenzaScadenzaQC&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
     </jsp:include>
     <!-- FINE INCLUDE PG_BUTTONS_STAMPA_SIEP -->
     <% } %>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=3><font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font> </td>
      <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>" type="text">
    </tr>
    
    <% if (lLuogoDetenzione.getIstitutoDetenzione()!=null){%>
    <tr>
      <td class="l">Detenuto presso </td>
      <td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> 
        di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%></font>
      </td>
    </tr>
    <% } %>
           
    <%        
    if(   penaresidua.getIdPenaResidua() != null 
       && (   penaresidua.getFlagErgastolo() == null
           || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))
          )
      )
    {
    %>
      <% if (!penaresidua.isQuantumReclusioneZero() ) { %>
      <tr>
        <td class="l">Reclusione</td>
        <td class="l" colspan=1>
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
        </td>
        <td class="l">Multa</td>
        <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
      <% } %>
      
      <% if (!penaresidua.isQuantumArrestoZero()) { %>
      <tr>
        <td class="l" >Arresto</td>
        <td class="l" colspan=1>
           <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
           <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
           <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
        </td>
        <td class="l">Ammenda</td>
        <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>       
      <% } %>
    <% } %>
    
    <tr>
      <td class="l">Data pervenimento richiesta variazione</td>
      <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataRichiesta(), "dd-MM-yyyy"),"")%></font></td>
    </tr>
    <tr>       
      <td class="l">Motivazioni</td>
      <td class="L" colspan="1"><font class="campo"><%=motivazioni%></font></td>
    </tr>   
    
    <tr>    
      <% if (penaresidua.getDataInizio() != null) { %>
      <td class="l">Nuova Data Decorrenza Pena</td>
      <td class="L" colspan="1"><font class="campo"><%=StrdataInizioPena%></font></td>
      <% }

      if (penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("S") )
      {
      %>
        <td class="l">Pena Detentiva</td>
        <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
      <% } else if(penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("D")) { %>
        <td class="l">Pena Detentiva</td>
        <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
      <% } else { %>
        <% if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){ %>
        <td class="l">Data Fine Pena</td>
        <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
        <% } else {%>
        <td class="l">Data Fine Pena</td>
        <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
        <% } %>
      <%}%>
    </tr>
       
       
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=1>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(), "dd-MM-yyyy"))%>
        </font>
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%></font></td>
    </tr>

    <tr>
      <td class="l">Magistrato</td>
      <td class="L" colspan="2">
          <font class="campo">
            <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          </font>
      </td>
    </tr>
  </table>  
    
  <%
  //============================================================================
  // Lista avvocati
  //============================================================================
  int lIdxAvv = 0;
  Iterator lItxAvv = avvocati.iterator();
  while(lItxAvv.hasNext())
  {
    AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
  %>    
    <table>
      <tr>
        <td class="l">Avvocato&nbsp;
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
  <%
    lIdxAvv++;
  }
  %>

  <!-- ===================================================================== -->
  <!--    Destinatari                                                        -->
  <!-- ===================================================================== -->
  <table>
    <tr>
      <td class="Titolo" colspan=6> Destinatari</td>
    </tr>
  <%
  for (int i=0;i<lEve.getNotifiche().length;i++)
  {
    NotificaModel lNotifica = lEve.getNotifiche()[i];
  %>
    <tr>
      <% if (lNotifica.getCodTipoNotifica().equals("TS") || lNotifica.getCodTipoNotifica().equals("MS")) { %>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getUfficio().getDescrTipoUfficio())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getUfficio().getDescrComune())%></font>&nbsp;
      </td>
      <% 
       } else if (lNotifica.getCssIdCssa()!=null) {
      %>
      <td class="L" colspan=2>
        <font class="campo">UFFICIO DI ESECUZIONE PENALE ESTERNA</font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getCSSA().getComune())%></font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getCSSA().getIndirizzo())%></font>&nbsp;
      </td>
      <% 
       } else if (lNotifica.getIstitutoDetenzione() != null) {
      %>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getIstitutoDetenzione().getDescrComune())%></font>&nbsp;
      </td>
      <% 
       } else if (lNotifica.getAutoritaEsterna()!= null && lNotifica.getAvvIdAvvocatoFascicoloSiep()==null) {
      %>
      <!-- AUTORITA' ESTERNA -->
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lNotifica.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
      <% } %>
    </tr>
  <%
  }
  %>
  </table>

  <% if(flagfungibilita.equals("S")) { %>
  <table>
    <tr>
      <td class="c" colspan="3">
         SCADENZA EFFETTIVA DELLA PENA <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>
         INFERIORE ALLA DATA DEL PROVVEDIMENTO <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
        <br>
         CON UN PERIODO FUNGIBILE DI anni <%=StringUtils.toStringJSP(fungibilita.getNumAnni())%>
         mesi <%=StringUtils.toStringJSP(fungibilita.getNumMesi())%>
         giorni <%=StringUtils.toStringJSP(fungibilita.getNumGiorni())%>
      </td>
    </tr>
    <tr>
    <%if(lLuogoDetenzione.getIstitutoDetenzione()!=null) {%>
    <tr><td class="Titolo" colspan=6>NOTIFICA ISTITUTO </td></tr>
    <tr>
      <td class="l">Autorità Destinazione : </td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%></font>
      </td>
    </tr>
    <%}%>
  </table>
  <% } %>

  <br>
  
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActUploadVariazioneDecorrenzaScadenzaQC">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.ordineesecuzione.action.ActDettaglioVariazioneDecorrenzaScadenzaQC">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  
  <br>
  <br>
</body>
</html>