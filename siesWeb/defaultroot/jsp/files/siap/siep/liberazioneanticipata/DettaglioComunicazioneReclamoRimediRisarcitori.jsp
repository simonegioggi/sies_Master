<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.w_magistrato.action.ICostantiWMagistrato"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>


<% // Dati dell'Evento Comunicazione %>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<% // Dati del Provvedimento della Sorveglianza   %>
<jsp:useBean id="EventoSIUS"          scope="request" class="siap.sico.evento.model.EventoModel"/> 
<jsp:useBean id="DepositoDecreto"     scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="DepositoOrdinanzaPc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>  <% // Vector <LicenzaPeriodiLibAnticipataModel>   %>

<% // Dati del Fascicolo   %>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<% // Se Ergastolo da verificare se serve%>
<jsp:useBean id="luogodetenzione"     scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>

<jsp:useBean id="lPosGiuModificata"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
<jsp:useBean id="flagfungibilita"     scope="request" class="java.lang.String"/>


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

<%
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
  <title>[S.I.E.S.] - Dettaglio Reclamo Rimedi Risarcitori </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
				<font class="campo">Dettaglio Comunicazione <%if(eventonotifica!=null && eventonotifica.getEvento()!=null && eventonotifica.getEvento().getDescrMotivo()!=null){ %>
				<%= eventonotifica.getEvento().getDescrMotivo()%><%//ticket 20190805017%>
					<%} %></font></td>
      
      <%
        // Bottone di Stampa se provvedimento non ancora validato
        if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
            || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           )
        {
      %>
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.sico.libertaanticipata.action.ActStampaComunicazioneReclamoRimediRisarcitori&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
      <% } %>

    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
 
<%
//============================================================================
// POSIZIONE GIURIDICA
// LUOGO DI DETENZIONE
//============================================================================
%>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=7>
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
    // Luogo di detenzione
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && "S".equals(lFascicoloAssociato.getFlagAltraCausa()))
    {  //Detenuto Altra Causa
       if( lAltraCausa.getIstitutoDetenzione()!= null )
       {
       %>
       <tr>
         <td class="l">Detenuto presso </td>
         <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
         </td>
       </tr>       
       <%
       }
               
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
     else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
     { // detenuto questa causa
     %>
      <tr>
       <td class="l">Detenuto presso </td>
       <td class="L" colspan=5>
        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
    <% } %>


    <%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(   lPosizione.getCodPosizioneGiuridica() != null 
       && (   lPosizione.getCodPosizioneGiuridica().equals("02") 
           || lPosizione.getCodPosizioneGiuridica().equals("04")
          ) 
      )
    {
        if(lLuogoDetenzione.getIstitutoDetenzione() != null)
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
    }
    %>
</table>

<%
//==============================================================================
// Pena Residua - Decorrenza/Scadenza
//==============================================================================
%>

<% if(  !penaresidua.isErgastolo()  )  { %>
<table>
  <% if ( !penaresidua.isQuantumReclusioneZero() ) {%>
  <tr>
    <td class="l">Reclusione</td>
    <td class="l" colspan=2>
      <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
    </td>
    <td class="l">Multa</td>
    <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
   </tr>
  <% } %>
   
   
  <% if (!penaresidua.isQuantumArrestoZero()) {%>
  <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
  </tr>
  <% } %>
</table>
<% } %>

<%
// Decorrenza Scadenza
%>
<table>
  <tr>
    <% if (penaresidua.getDataInizio() != null) { %>
    <td class="l">Data Decorrenza Pena</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
    <% } %>
  
    <% 
    if (penaresidua.getFlagErgastolo() != null) 
    {
      if(penaresidua.getFlagErgastolo().equals("S")) 
      {
      %>
         <td class="l">Pena Detentiva</td>
         <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
      <% } else if(penaresidua.getFlagErgastolo().equals("D")) { %>
         <td class="l">Pena Detentiva</td>
         <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
      <% }
    }
    %>    
    
    <%
    if ( !penaresidua.isErgastolo() && penaresidua.getDataFine()!=null)
    {
      if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
      {
      %>
        <td class="l">Data Fine Pena</td>
        <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
      <% } else { %>
        <td class="l">Data Fine Pena</td>
        <td class="lRosso" >
          <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
        </td>
      <%
      }
    }
    %>
  </tr>
</table>

<br>

<table>
  <tr>
    <% if(eventonotifica.getEvento().getDataEmissione()!= null) { %>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
    </td>
    <% } %>

    <% if(eventonotifica.getNotifiche()[0].getDataInvio()!= null) { %>
    <td class="l">Data Trasmissione</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%>
      </font>
    </td>
    <% } %>
  </tr>    
</table>


<%
//==============================================================================
//           Sezione con i dati del provvedimento della Sorveglianza
//==============================================================================
%> 

<%
LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel)LicenzePeriodi.firstElement();
LicenzaLibAnticipataModel lLicenzaModel = lLicenzaPeriodiModel.getLicenza();
%>
<br>
  <table width="90%">
    <tr>
      <td class="Titolo" colspan="4"> Dati <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %> </td>
    </tr>
    <tr>
      <td class="l" width="10%">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(lLicenzaModel.getAnnoSius())%> /<%=StringUtils.toStringJSP(lLicenzaModel.getNumeroSius())%>
        </font>
      </td>
      <td class="l"> Anno / Numero <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %> </td>
      <td class="l">
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
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnoProvvedimento)%>/<%=StringUtils.toStringJSP(lNumeroProvvedimento)%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"> Autorità emittente </td>
      <td class="l" colspan="3">
        <font class="campo">
          <%=StringUtils.toStringJSP(EventoSIUS.getDescrUfficioEmittente())%>&nbsp;
        </font>
        di
        <font class="campo">
          <%=StringUtils.toStringJSP(EventoSIUS.getDescrLuogoEmittente())%>
        </font>
      </td>
    </tr>
    <%
      Date lDataEmissione = null;
      if (DepositoDecreto!=null && DepositoDecreto.getIdDepositoDecreto()!=null){
        lDataEmissione = DepositoDecreto.getDataEmissione();
      }
      else if (DepositoOrdinanzaPc!=null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc()!=null){
        lDataEmissione = DepositoOrdinanzaPc.getDataCameraConsiglio();
      }
    %>
    <% if(lDataEmissione != null) { %>
    <tr>
      <td class="l" height="20" >Data Emissione <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %></td>
      <td class="l" colspan="3">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataEmissione,"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <% } %>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="L" nowrap> Totale giorni riduzione pena concessi: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lNumeroGiorniRiduzione,"")%></font></td>
    </tr>
    <tr>
      <td class="L" nowrap> Periodi valutati per riduzione pena: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_C,"")%> </font></td>
    </tr>
    
    
    <% if (lSommaLiquidata!=null && lSommaLiquidata.length()>0) { %>
    <tr>
      <td class="L" nowrap> Somma liquidata a titolo risarcimento danno: &euro; </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lSommaLiquidata,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    
    <% if (lStrPeriodi_SL_C.length()>0) { %>
    <tr>
      <td class="L"> Periodi valutati per liquidazione somma: </td>
      <td class="L" colspan="3"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_SL_C,"&nbsp;")%> </font></td>
    </tr>
    <% } %>

    <% if (lStrPeriodi_RD_R.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi Rigettati: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_R,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    <% if (lStrPeriodi_RD_I.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi Inammissibili: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_I,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
    <% if (lStrPeriodi_RD_N.length()>0) { %>
    <tr>
      <td class="L"> Periodi non concessi N.L.P./N.D.P.: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_N,"&nbsp;")%> </font></td>
    </tr>
    <% } %>
  </table>

<%
//==============================================================================
// Destinatari
//==============================================================================
%>
<br>
<table width="90%">
  <tr>
    <td class="Titolo" colspan='8'>Destinatari</td>
  </tr>

  <% if(magistrato != null) { %>
  <tr>
    <td class="l">Magistrato Competente
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
      <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
    </td>
  </tr>
  <% } %>

<% 
int i = 0;
while(i < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
  
  if(   lNotMod.getCodTipoNotifica().equals("E")
     && lNotMod.getIstitutoDetenzione()==null 
    )
  { // Autorità x l'esecuzione
  %>
  <tr>
    <td class="l">Autorità Destinazione</td>
    <td class="L" colspan=2>
      <font class="campo"><%=StringUtils.toStringJSP( lNotMod.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
      di
      <font class="campo"><%=StringUtils.toStringJSP( lNotMod.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">Indirizzo</td>
    <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
  </tr>
  <%
  }
  else if (   lNotMod.getCodTipoNotifica().equals("E")
           && lNotMod.getIstitutoDetenzione()!=null)
  { // Istituto di Detenzione
  %>
  <tr>
    <td class="l">Istituto di Detenzione</td>
    <td class="L" colspan=2>
      <font class="campo"><%=StringUtils.toStringJSP( lNotMod.getIstitutoDetenzione().getDescrTipoIstituto() )%></font>&nbsp;
      di
      <font class="campo"><%=StringUtils.toStringJSP( lNotMod.getIstitutoDetenzione().getDescrComune())%></font>,&nbsp;
      <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getIndirizzo())%></font>
    </td>
  </tr>
  <%
  }
  else if (   lNotMod.getCodTipoNotifica().equals("C")
           && lNotMod.getUfficio()!=null
          ) 
  { // Ufficio di Sorveglianza
  %>
  <tr>
    <td class="l"><%=lNotMod.getUfficio().getDescrTipoUfficio()%></td>
    <td class="L" colspan=2>
      di <font class="campo"><%=StringUtils.toStringJSP( lNotMod.getUfficio().getDescrComune() )%></font>&nbsp;
    </td>
  </tr>
  <%  
  }

  i++;
}
%>


  <%
  // Avvocati
int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
  if(   lNotMod.getCodTipoNotifica().equals("N") 
     && lNotMod.getAutoritaEsterna()!=null 
     && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
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
        <td class="l">Autorita Notifica</td>
        <td class="L" colspan=2>
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
      </tr>
<%
  }
  count++;
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
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActUploadComunicazioneReclamoRimediRisarcitori">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sico.libertaanticipata.action.ActDettaglioComunicazioneReclamoRimediRisarcitori">
        </td>
      </tr>
    </table>
  </form>
</div>

</body>
</html>