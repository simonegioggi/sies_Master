<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<% // Dati del Provvedimento della Sorveglianza   %>
<jsp:useBean id="EventoSIUS"          scope="request" class="siap.sico.evento.model.EventoModel"/> 
<jsp:useBean id="DepositoDecreto"     scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="DepositoOrdinanzaPc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>  <% // Vector <LicenzaPeriodiLibAnticipataModel>   %>

<% // Dati del Fascicolo   %>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo"       scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// JSP per la visualizzazione del dettaglio del provvedimento della Sorveglianza
// (decreto/Ordinanza) di concessione dei "Recalmo Rimedi Risarcitori" DL 92/2014.
//
// Se non sono concessi giorni di riduzione, quindi solo somma liquidata, nessuna
// altra azione è possibile. Viene di fatto annotato solo il provvedimento SIUS.
//
// Se "Libero", Ergastolo, o pena terminata (fine pena < data di sistema): Comunicazione
//
// Se "In Espiazione" si procede al calcolo pena e Ordine di Scarcerazione
//
//==============================================================================
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
<title>[S.I.E.S.] - Dettaglio Liberazione Anticipata </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    function Verify()
    {
      
      //disabilità il taso di calcolo
      if(document.f.CALCOLO != "undefined")
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>!=undefined)
        {
          var giornoScarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
          var meseScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
          var annoScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
          
          var dataScarcerazione = giornoScarcerazione+'/'+meseScarcerazione+'/'+annoScarcerazione;
          var dataSystema = '<%=DateUtils.getDateToString(DateUtils.getSysDate(),"dd/MM/yyyy")%>';   
          if (dataScarcerazione!="//"){
            if (!ControllaDataPassaVuota(dataScarcerazione) ) {
              alert('Data Scarcerazione non valida');
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
              return false;
            }
           
            if (CompareDate(dataSystema,dataScarcerazione))
            {
              alert('La Data Scarcerazione deve essere < della data odierna');
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
              return false;
            }
          }
        }
        
        document.f.CALCOLO.disabled=true;
      }
    }
  </script>
</head>

<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActCalcoloPenaReclamoRimediRisarcitori">
  <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO %>" value="<%=StringUtils.toStringJSP(EventoSIUS.getIdEvento())%>">


  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Reclamo Rimedi Risarcitori D.L. 92/2014</font>
      </td>
<%
      // E' possibile eseguire la comunicazione solo
      // nel caso in cui non compare il bottone di calcolo pena
      if(  (    lPosizione.isLibero()
           || flagergastolo.equals("S")
            || (   penaresidua.getDataFine() != null
                && DateUtils.isLower(penaresidua.getDataFine(), DateUtils.getSysDate())
                && !DateUtils.isEquals(penaresidua.getDataFine(), DateUtils.getSysDate())
               )
           )
         && (lNumeroGiorniRiduzione!=null && lNumeroGiorniRiduzione.intValue()>0)  // presenti gg concessi
        )
      {
%>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=StringUtils.toStringJSP(EventoSIUS.getIdEvento())%>" />
          </jsp:include>
        </td>
<%
     }
%>
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
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
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




  
  <table>
<%
    if(!LicenzePeriodi.isEmpty())
    {
      LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel)LicenzePeriodi.firstElement();
%>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="L">
          Anno / Numero SIUS
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoSius())%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroSius())%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="L">
          Anno / Numero <% if ("02".equals(EventoSIUS.getCodTipoProvvedimento())) { %>Decreto<% } else { %>Ordinanza<% } %>
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
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnoProvvedimento)%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lNumeroProvvedimento)%>&nbsp;
          </font>
        </td>
      </tr>

	<tr>
		<td class="l">Autorità emittente</td>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(EventoSIUS.getDescrUfficioEmittente());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(EventoSIUS.getCodTipoUfficioEmittente())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
        <td class="l">
          	<font class="campo">
            	<%=descrTipoUfficio%>&nbsp;
          	</font>
          	di
          	<font class="campo">
            	<%=StringUtils.toStringJSP(EventoSIUS.getDescrLuogoEmittente())%>&nbsp;
          	</font>
		</td>
	</tr>

      <tr>
        <td class="L">
          Data Emissione
        </td>
        <td class="L">
        <%
        Date lDataEmissione = null;
        if (DepositoDecreto!=null && DepositoDecreto.getIdDepositoDecreto()!=null){
          lDataEmissione = DepositoDecreto.getDataEmissione();
        }
        else if (DepositoOrdinanzaPc!=null && DepositoOrdinanzaPc.getIdDepositoOrdinanzaPc()!=null){
          lDataEmissione = DepositoOrdinanzaPc.getDataCameraConsiglio();
        }
        %>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataEmissione, "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%
    }
%>
  </table>



  <br>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="L" nowrap> Totale giorni riduzione pena concessi: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lNumeroGiorniRiduzione,"")%></font></td>
    </tr>
    <tr>
      <td class="L" nowrap> Periodi valutati per riduzione pena: </td>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_C,"")%> </font></td>
    </tr>
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
  </table>

  <table cellspacing="2" cellpadding="2">
    <tr><td><br></td></tr>
    <tr>
      <td class="Titolo" colspan=6> Periodi non concessi Rigettati: </td>
    </tr>
    <tr>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_R,"")%> </font></td>
    </tr>
  </table>

  <table cellspacing="2" cellpadding="2">
    <tr><td><br></td></tr>
    <tr>
      <td class="Titolo" colspan=6> Periodi non concessi Inammissibili: </td>
    </tr>
    <tr>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_I,"")%> </font></td>
    </tr>
  </table>

  <table cellspacing="2" cellpadding="2">
    <tr><td><br></td></tr>
    <tr>
      <td class="Titolo" colspan=6> Periodi non concessi N.L.P./N.D.P.: </td>
    </tr>
    <tr>
      <td class="L"> <font class="campo"><%=StringUtils.toStringJSP(lStrPeriodi_RD_N,"")%> </font></td>
    </tr>
  </table>

  <br>
  
<%
  // E' possibile eseguire il calcolo della pena solo se è presente
  // la data fine sull'ultima pena validata o NON ERGASTOLO O NON LIBERO.
  // Inoltre il calcolo viene inibito se la data fine pena è già passata:
  // in questo caso infatti il condannato è formalmente libero
  // ed è possibile soltanto emettere una Comunicazione per Libero
  if(    !lPosizione.isLibero()
      && penaresidua.getDataFine() != null
      && !flagergastolo.equals("S")
      && (lNumeroGiorniRiduzione!=null && lNumeroGiorniRiduzione.intValue()>0)  // presenti gg concessi
      && (   DateUtils.isGreater(penaresidua.getDataFine(), DateUtils.getSysDate())
          || DateUtils.isEquals(penaresidua.getDataFine(), DateUtils.getSysDate())
         )
    )
  {
%>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td>
          <input class="bottone" name="CALCOLO" type="submit" value="Calcolo data fine pena" >
        </td>

        <% //A9RR007 %>       
        <td class="l" colspan="3">
          <font class="label">Data Eventuale Scarcerazione</font>
          &nbsp;&nbsp;
          <input type="text" maxlength="2" size="2"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" maxlength="2" size="2"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" maxlength="4" size="4"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
<%
  }
%>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>