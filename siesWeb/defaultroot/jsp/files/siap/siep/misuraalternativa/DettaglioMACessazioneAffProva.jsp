<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="lPosGiuModificata"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="nuovapenaresidua"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="eventonotifica"       scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="misuraalternativa"    scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="flagmisura"           scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"           scope="request" class="java.lang.String"/>
<%//Eventuale misura cessata%>
<jsp:useBean id="misuraconcessa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<%//==========================================================================%>
<jsp:useBean id="magistrato"           scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="UffTDS"               scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteTDS"              scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"               scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"              scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"     scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="sedeUfficioEmittente" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
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
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

</head>

<body class="corpo">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <%if(tipoMisura != null && tipoMisura.equals("INDULTINO")){%>
      <font class="campo">Dettaglio Cessazione</font>
      <%}else if(tipoMisura != null && tipoMisura.equals("AFFIDAMENTO"))  {%>
      <font class="campo">Dettaglio Cessazione Affidamento in Prova</font>
      <%}else if(tipoMisura != null && tipoMisura.equals(ICostantiMisuraAlternativa.ESP_PRESSO_DOM))  {%>
      <font class="campo">Dettaglio Cessazione Espiazione Pena presso Domicilio</font>
      <%}%>
  </td>
  
  <%
  if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
      || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato()) 
     )
  {
  %>   
     <!-- BOTTONE DI VALIDAZIONE DIRETTA -->
    <td class="LBG">
      <a href="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActUploadMACessazione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misuraalternativa.action.ActDettaglioCessazioneMAAffInProva&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
      </a>
    </td> 
   <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMACessazioneAffInProva&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
  <%}%>
  </tr>
</table>

 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
   
<table>
  <%if(flagmisura.equals("N")){%>
  <tr><td><input type="HIDDEN" name="flagmisura" value="N"></td></tr>
  <%}else{%>
  <tr><td><input type="HIDDEN" name="flagmisura" value="S"></td></tr>
  <%}%>


  <tr>
    <td class="l">Posizione Giuridica </td>
    <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
              DETENUTO PER ALTRA CAUSA
      <%}
        else
        {
            if(lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null)
            {
              %>
                   <%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
            <%
             }else
              {
                %>
                     <%=lPosizione.getDescrPosizioneGiuridica()%>

             <%}}%>
         </font>
   </td>
</tr>

<%
  if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
  {
    if(lAltraCausa.getIstitutoDetenzione() != null )
    {
%>
<tr>
  <td class="l">Detenuto presso </td>
  <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
    di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
  </td>
</tr>

<%
  if (lAltraCausa.getAltroLuogo()!=null)
  {
%>
  <tr>
    <td class="l">Altro Luogo </td >
    <td class="L" colspan=5>
      <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
    </td>
  </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
          </tr>
<%
        }%>
        
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }

       if (!lPosizione.isLibero() && penaresidua.getDataInizio() != null)
       {
%>
          <tr>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
           </tr>
<%
       }

       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta() != null)
       {
%>

<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        </tr>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        </tr>
<%
        }
       }
%>
      <tr>
<%
        if (!lPosizione.isLibero() && ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>


<%            }
      }
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
<tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
           <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>

<%
        }  }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
           <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>

      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          } }
    }
%>



<% if( misuraconcessa != null) { %>  
<tr>
  <% if (misuraconcessa.getDataInizioMisura() != null) { %>       
  <td class="l">Data Inizio Misura</td>
  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraconcessa.getDataInizioMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  <% } %> 

  <% if(misuraconcessa.getDataFineMisura() != null) { %>       
  <td class="l">Data Fine Misura</td>
  <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraconcessa.getDataFineMisura(),"dd-MM-yyyy"))%>&nbsp;</font></td>
  <% } %>
</tr>      
<% } %>

<tr>
  <td class="l">Data Emissione</td>
  <td class="L" >
    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
  </td>
  <td class="l">Data Trasmissione</td>
  <td class="L" >
    <%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0){%>
    <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
    <%} %>
  </td>
</tr>

<%
//==============================================================================
// Dati dell'Ordinanza/Decreto di Cessazione 
//==============================================================================
%>
<tr>
  <td class="Titolo" colspan="4"> Dati Del Provvedimento della Sorveglianza di Cessazione della Misura</td>
</tr>
<%
String lTipoProvvSorv = "Provvedimento";
if ("03".equals(misuraalternativa.getCodTipoDecisione()) )
  lTipoProvvSorv = "Ordinanza";
else if ("02".equals(misuraalternativa.getCodTipoDecisione()) )
  lTipoProvvSorv = "Decreto";
%>
<tr>
  <% if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null) { %>
  <td class="l">Anno / Numero Sius</td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
  </td>
  <% } %>

  <% if(misuraalternativa.getAnnoRegistro()!= null) { %>
     <td class="l" nowrap> Anno / Numero <%=lTipoProvvSorv%> </td>
     <td class="l" nowrap><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
       <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
  <% } %>
</tr>
 
<tr>
  <td class="l">Ufficio Emittente</td>
  <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(sedeUfficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
  <td class="l" colspan="3"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
</tr>
 
<tr>
  <td class="l">Oggetto <%=lTipoProvvSorv%> </td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
  <td class="l" nowrap>Data Emissione <%=lTipoProvvSorv%> </td>
  <td class="l" nowrap>
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
  </td>
</tr>

<%if(misuraalternativa != null && misuraalternativa.getDataInizioRevoca()!= null) {%>
<tr>
  <td class="l">Misura Alternativa Cessata dal</td>
  <td class="l">
    <font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioRevoca(),"dd-MM-yyyy"))%>
    </font>
  </td>
</tr>
<%}%>

<%if(  misuraalternativa.getNumAnniRevocaReclusione() != null 
    || misuraalternativa.getNumMesiRevocaReclusione() != null
    || misuraalternativa.getNumGiorniRevocaReclusione() != null 
    || misuraalternativa.getNumAnniRevocaArresto() != null 
    || misuraalternativa.getNumMesiRevocaArresto() != null
    || misuraalternativa.getNumGiorniRevocaArresto() != null)
 {%>
  <tr>
     <td class="l">Pena residua rideterminata</td>
     <td class="l" colspan="3"> RECLUSIONE
        Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaReclusione(),"0")%>
        </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaReclusione(),"0")%>
        </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaReclusione(),"0")%>
      </font>
         ARRESTO
          Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumAnniRevocaArresto(),"0")%>
          </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumMesiRevocaArresto(),"0")%>
          </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniRevocaArresto(),"0")%>
      </font>
      </td>
  </tr>
<%}%>

<%if(misuraalternativa!= null && misuraalternativa.getFlagPeriodoEspiato()!= null) {%>
<tr>
  <td class="l">Periodo espiato in Istituto da detrarre</td>
  <td class="l" colspan="3">
     Giorni: <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getFlagPeriodoEspiato())%>&nbsp;</font>
    </font>
  </td>
</tr>
<%}%>

<tr>
  <td colspan="4">&nbsp;</td>
</tr>

<%if(misuraalternativa != null && misuraalternativa.getNote() != null){%>
<tr>
  <td class="l">Note</td>
  <td class="l" colspan="3">
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%>&nbsp;</font>
  <td>
</tr>
<%}%>

<%if(misuraalternativa != null && misuraalternativa.getDataIngressoIstituto()!= null) {%>
<tr>
  <td class="l">Esecuzione contro soggetto detenuto dal</td>
  <td class="l" colspan="3">
    <font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataIngressoIstituto(),"dd-MM-yyyy"))%>
    </font>
  </td>
</tr>
<%}%>

<% if(nuovapenaresidua != null) {%>
  <tr>
    <td class="l"> Pena rideterminata</td>
    <td class="l" colspan="2">RECLUSIONE
            Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniReclusione())%>
            </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiReclusione())%>
            </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniReclusione())%></font>
         ARRESTO
  
            Anni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumAnniArresto())%>
            </font>Mesi &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumMesiArresto())%>
            </font>Giorni &nbsp;<font class="campo"><%=StringUtils.toStringJSP(nuovapenaresidua.getNumGiorniArresto())%></font>
    </td>
  </tr>

  <% if(nuovapenaresidua.getDataInizio() != null) { %>
  <tr>
    <td class="l">Data Decorrenza Pena da Espiare</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
  </tr>
  <% } %>

  <% if(nuovapenaresidua.getDataFine() != null) { %>
  <tr>
    <%if(nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta())) {%>
         <td class="l">Data Fine Pena da Espiare</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
    <%}else{%>
         <td class="l">Data Fine Pena da Espiare</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
    <%}%>
  </tr>
  <%}%>
<%}%>


<%if(magistrato != null){%>
<tr>
  <td class="Titolo" colspan="4"> Magistrato Firmatario</td>
</tr>
<tr>
  <td class="l">Magistrato Firmatario</td>
  <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
  </td>
</tr>
<%} %>

<%
//==============================================================================
//                        Sezione con i destinatari
// - Istituto detenzione (E) o Autorità per l'esecuzione
// - MDS (C)/TDS
// - Avvocati (N)
//==============================================================================
%>
<tr>
  <td class="Titolo" colspan="4"> Destinatari </td>
</tr>
<% if(autoritaEsternaE != null && autoritaEsternaE.getCodTipoAutorita() != null) {%>
  <tr>
    <td class="l">Destinatario per l'esecuzione</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%></font>
    <%if(   autoritaEsternaE != null && autoritaEsternaE.getDescrSede()!= null
         && !autoritaEsternaE.getDescrSede().equals("-"))
      {%>
      di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %>   </font>
    <%}%>
   </td>
  </tr>
  <%if(NoteAutE != null && !NoteAutE.equals("")){%>
  <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
      <font class="campo"><%=NoteAutE%>&nbsp;</font>
    <td>
  </tr>
  <%}
}%>

<%
int cont=0;
if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
  while(cont < eventonotifica.getNotifiche().length)
  {
    if(   eventonotifica.getNotifiche()[cont].getCodTipoNotifica().equals("E") 
       && eventonotifica.getNotifiche()[cont].getIstitutoDetenzione() != null
      )
    {%>
    <tr>
      <td class="l">Istituto Detenzione</td>
      <td class="l">
        <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
      </td>
    </tr>
    
    <%if(eventonotifica.getNotifiche()[cont].getNote()!= null && !eventonotifica.getNotifiche()[cont].getNote().equals("")){%>
    <tr>
      <td class="l">Note</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getNote())%>&nbsp;</font></td>
    </tr>
    <%}
 }
cont++;
}
}
%>

<%if (UffTDS.length()>0){%>
   <tr>
     <td class="l">Destinatario</td>
      <td class="L"> <font class="campo">TRIBUNALE DI SORVEGLIANZA</font> di
      <font class="campo"><%=StringUtils.toStringJSP(UffTDS)%></font>
     </td>
  </tr>
  <%if(NoteTDS != null && !NoteTDS.equals("")) {%>
  <tr>
   <td class="l">Note</td>
     <td  class="L">
        <font class="campo"><%=NoteTDS%>&nbsp;</font>
     </td>
  </tr>
  <%}%>
<%}%>

<%if (UffUDS.length()>0){%>
   <tr>
     <td class="l">Destinatario</td>
     <%-- MEV10-s3: sostituita string fissa ("UFFICIO DI SORVEGLIANZA") con variabile --%>
      <td class="L"> <font class="campo"><%=descrTipoUfficio%></font> di
      <font class="campo"><%=StringUtils.toStringJSP(UffUDS)%></font>
     </td>
  </tr>
  <%if(NoteUDS != null && !NoteUDS.equals("")) {%>
  <tr>
   <td class="l">Note</td>
     <td  class="L">
        <font class="campo"><%=NoteUDS%>&nbsp;</font>
     </td>
  </tr>
  <%}%>
<%}%>

<%int count=0;
if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();


     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
   <tr>
      <td class="l">Autorità Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
     </tr>
<%if(lNotMod != null && lNotMod.getNote()!= null)
  {%>
     <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>

<%}
}
count++;
}
}
%>

</table>
 <br>
 
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input type="submit" class="bottone"  value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadMACessazione">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioCessazioneMAAffInProva">
        </td>
      </tr>
    </table>
  </FORM>
</div>

  <br>
  <br>
</body>
</html>