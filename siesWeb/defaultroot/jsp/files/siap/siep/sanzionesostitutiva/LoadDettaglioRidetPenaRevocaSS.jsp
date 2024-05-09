<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%> 

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%> 
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%> 
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%> 
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%> 
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>


<jsp:useBean id="aEveNotRichiesta"       scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="aNuovaPenaResidua"      scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="aPenaResiduaPrecedente" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="aListaAnnotazioni"      scope="request" class="java.util.Vector"/>


<jsp:useBean id="aAzioneChiamante"      scope="request" class="java.lang.String" />
<jsp:useBean id="aPosizioneluogoaltra" 	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="magistrato"            scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<%
//==============================================================================
//  Pagina per la Load Dettaglio Rideterminazione Pena a Seguito Revoca SS
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione       = aPosizioneluogoaltra.getPosizioneGiuridica();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();
  
%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <!-- Load Dettaglio OE Ridet Pena Revoca/Conversione SS  -->
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
        </a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Rideterminazione della Pena a seguito Revoca/Conversione Sanzioni Sostitutive</font>
      </td>
<%
// Bottone di stampa (se non validato)
if (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>
    <!-- BOTTONE DI STAMPA -->
    <input type="hidden" name="tipo" value="D">
    <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
      <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActStampaRideterminazionePenaRevocaSS&IdEvento="+aEveNotRichiesta.getEvento().getIdEvento()%>"/>
    </jsp:include>
<%}%>
      
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <%
  //============================================================================
  // Sezione con il dettaglio della posizione giuridica
  //============================================================================
  %>
  <%
if (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()==null 
    || (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()!=null 
        && aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
       )
   ) 
{%>
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
  </table>  
<% } %>  

<%
//==============================================================================
// Sezione con la pena rideterminazta (e i quantum convertiti)
//==============================================================================
%>
<table>
  <!-- ================== -->
  <!-- PENA PRECEDENTE    -->
  <!-- ================== -->
  <tr>
    <td class="titolo" colspan="100%"> Pena Residua da Espiare </td>
  </tr>
  <tr>
    <td class="l"><font class="label">Reclusione / Multa : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumAnniReclusione()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumMesiReclusione()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumGiorniReclusione()%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(aPenaResiduaPrecedente.getImportoMulta())%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Arresto / Ammenda :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumAnniArresto()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumMesiArresto()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=aPenaResiduaPrecedente.getNumGiorniArresto()%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(aPenaResiduaPrecedente.getImportoAmmenda())%></font></td>
  </tr>
  
  <!--
  =================================================== 
         Sezione con Decorrenza e Scadenza
  =================================================== 
  -->
  <% if (aPenaResiduaPrecedente.getDataInizio()!=null) {%>
  <tr><td colspan="100%"><table>
    <tr>
      <td class="l"><font  class="label">Data Decorrenza Pena:</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResiduaPrecedente.getDataInizio(),"dd-MM-yyyy"))%>
        </font>
      </td>
      
      
      <% if (aPenaResiduaPrecedente.getDataFineReclusione()!=null) { %>
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResiduaPrecedente.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResiduaPrecedente.getDataInizioArresto(),"dd-MM-yyyy"))%>
        </font>
      </td>
      <% } %>
      
      
      <td class="l"><font class="label">Data Fine Pena  : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResiduaPrecedente.getDataFine(),"dd-MM-yyyy"),"-")%>
        </font>
      </td>
    </tr>
  </table></td></tr>
  <% } %>
  
  <!-- ================== -->
  <!-- PENA CONVERTITA    -->
  <!-- ================== -->
  <%
  CalendarModel lTotCalReclusione = new CalendarModel();
  CalendarModel lTotCalArresti = new CalendarModel();
  CalendarUtil  lCalUtil = new CalendarUtil();
  
  for (int i=0; i<aListaAnnotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnoMod = (AnnotazioneManualeModel) aListaAnnotazioni.elementAt(i);
    
    CalendarModel lCalReclusione = lAnnoMod.getQuantumReclusione();
    CalendarModel lCalArresti = lAnnoMod.getQuantumArresto();
    
    lTotCalReclusione = lCalUtil.sommaGiornieValute(lTotCalReclusione,lCalReclusione);
    lTotCalArresti    = lCalUtil.sommaGiornieValute(lTotCalArresti,lCalArresti);
  }
  %>
  
  <tr>
    <td class="titolo" colspan="100%"> Pena Convertita </td>
  </tr>
  <tr>
    <td class="l"><font class="label">Reclusione : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=lTotCalReclusione.getNumAnni()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=lTotCalReclusione.getNumMesi()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=lTotCalReclusione.getNumGiorni()%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Arresto :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=lTotCalArresti.getNumAnni()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=lTotCalArresti.getNumMesi()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=lTotCalArresti.getNumGiorni()%></font></td>
  </tr>
  
  <!-- ================== -->
  <!-- PENA RIDETERMINATA -->
  <!-- ================== -->
  <%
  if (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()==null 
      || (   aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato()!=null 
          && aEveNotRichiesta.getEvento().getFlagDocumentoRegistrato().compareTo("A")!=0
         )
     ) 
  {  // n.b. non visualizzo la pena se evento annullato in quanto è stata cancellata
  %>
  <tr>
    <td class="titolo" colspan="100%"> Pena Ricalcolata </td>
  </tr>
  <tr>
    <td class="l"><font class="label">Reclusione / Multa : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumAnniReclusione()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumMesiReclusione()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumGiorniReclusione()%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(aNuovaPenaResidua.getImportoMulta())%></font></td>
  </tr>
  <tr>
    <td class="l"><font class="label">Arresto / Ammenda :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumAnniArresto()%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumMesiArresto()%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=aNuovaPenaResidua.getNumGiorniArresto()%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(aNuovaPenaResidua.getImportoAmmenda())%></font></td>
  </tr>
  
  <!--
  =================================================== 
         Sezione con Decorrenza e Scadenza
  =================================================== 
  -->
  <% if (aNuovaPenaResidua.getDataInizio()!=null) {%>
  <tr>
    <td colspan="100%">
      <table>
        <tr>
          <td class="l"><font  class="label">Data Decorrenza Pena:</font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(aNuovaPenaResidua.getDataInizio(),"dd-MM-yyyy"))%>
            </font>
          </td>
          
          
          <% if (aNuovaPenaResidua.getDataFineReclusione()!=null) { %>
          <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(aNuovaPenaResidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
            </font>
          </td>
        </tr>
        <tr>
          <td class="l"><font class="label">Data Inizio Arresto : </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(aNuovaPenaResidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
            </font>
          </td>
          <% } %>
          
          
          <td class="l"><font class="label">Data Fine Pena  : </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(aNuovaPenaResidua.getDataFine(),"dd-MM-yyyy"),"-")%>
            </font>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <% } %>
  
  <% } %>
</table>

<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="AzioneChiamante"    value="<%=aAzioneChiamante%>">
  
  <%
  //============================================================================
  //       
  //============================================================================
  %>  
  <table style="width: 95%;  border: 0;">
    <tr>
      <td class="titolo" colspan="100%"><%=StringUtils.toStringJSP(aEveNotRichiesta.getEvento().getDescrTipoProvvedimento() )%></td>
    </tr>

    <tr>
      <td class="l">Data Emissione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotRichiesta.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>

    <tr>
      <td class="l">Data Trasmissione</td>
      <td class="l">
        <% if (aEveNotRichiesta.getNotifiche() != null && aEveNotRichiesta.getNotifiche().length > 0) { %>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEveNotRichiesta.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%></font>
        <% } else {%>
        <font class="campo">&nbsp;</font>
        <% } %>
      </td>
    </tr>
    
    <tr>
      <td class="l">Oggetto</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(aEveNotRichiesta.getEvento().getDescrMotivo() )%></font>
      </td>
    </tr>


    <%if(   magistrato.getCodMagistrato() != null
         && !magistrato.getCodMagistrato().equals("-") 
        )
    {%>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Magistrato Firmatario
      <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
      </td>
    </tr>
    <%}%>
    
    <!-- 
    ============================================================================
                        Sezione con i destinatari
    ============================================================================
    -->
    <tr>
      <td class="titolo" colspan="100%"> Destinatari </td>
    </tr>


    <%
    if(aEveNotRichiesta != null && aEveNotRichiesta.getNotifiche() != null && aEveNotRichiesta.getNotifiche().length > 0)
    {
      
      for (int i=0; i< aEveNotRichiesta.getNotifiche().length; i++)
      {
        NotificaModel lNotMod = aEveNotRichiesta.getNotifiche()[i];
        
        if (lNotMod.getAutEstIdAutoritaEsterna()!=null && lNotMod.getCodTipoNotifica().equals("E"))
        {
        %>
        <tr>
          <td class="l">Autorità di Destinazione</td>
          <td class="L" colspan="2">    <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font> 
                                     di <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%> </font>
          </td>
        </tr>
        <%
        }
        else if (lNotMod.getAutEstIdAutoritaEsterna()!=null && lNotMod.getCodTipoNotifica().equals("C"))
        {
        %>
        <tr>
          <td class="l">Autorità Competente per il Territorio</td>
          <td class="L" colspan="2">    <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrTipoAutorita()%></font> 
                                     di <font class="campo"><%=lNotMod.getAutoritaEsterna().getDescrSede()%> </font>
          </td>
        </tr>
        <%
        }
        else if (lNotMod.getCodTipoNotifica().equals("MS"))
        {
        %>
        <tr>
          <td class="l">Magistrato di Sorveglianza</td>
          <td class="L" colspan="2"> di <font class="campo"><%=lNotMod.getUfficio().getDescProvincia()%> </font></td>
        </tr>
        <% 
        } 
        else if (lNotMod.getCodTipoNotifica().equals("TS"))
        {
        %>
        <tr>
          <td class="l">Tribunale di Sorveglianza</td>
          <td class="L" colspan="2"> di <font class="campo"><%=lNotMod.getUfficio().getDescProvincia()%> </font></td>
        </tr>
        <%
        }
        else if (lNotMod.getCSSA()!=null && lNotMod.getCSSA().getIdCSSA()!=null)
        {
        %>
        <tr>
          <td class="l">UEPE</td>
          <td class="L" colspan="2"> di <font class="campo"><%=lNotMod.getCSSA().getComune()%>&nbsp;<%=lNotMod.getCSSA().getIndirizzo()%></font></td>
        </tr>
        <%
        }
      }
    }
    %>
    
    <!-- Avvocati -->
    <%
    if(aEveNotRichiesta != null && aEveNotRichiesta.getNotifiche() != null && aEveNotRichiesta.getNotifiche().length > 0)
    {
      int count=0;
      
      while(count < aEveNotRichiesta.getNotifiche().length)
      {
        NotificaModel lNotMod = aEveNotRichiesta.getNotifiche()[count];
        
        if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
        {
           AvvocatoSiepModel lAvvMod = aEveNotRichiesta.getNotifiche()[count].getAvvSiep();
           AutoritaEsternaModel lAuMod = aEveNotRichiesta.getNotifiche()[count].getAutoritaEsterna();
        %>
        <tr><td height="5px"></td></tr>
        <tr>
          <td class="l">Avvocato per  Notifica</td>
          <td class="L" colspan="2">
            <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
              &nbsp;Foro di&nbsp;
            <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%></font>
              &nbsp;Difensore di&nbsp;
            <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%></font>
          </td>
        </tr>
        
        <% if (lAuMod!=null) { %>
        <tr>
          <td class="l">Autorita Notifica</td>
          <td class="L" colspan="2">
            <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
             di
            <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
          </td>
        </tr>
        <% } %> 
         
        <% if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) { %>
        <tr>
          <td class="l">Note</td>
          <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
        </tr>
        <% } %>
        
      <% } 
 
   count++;
  }  //while
}
%>
    
</table>
  
</form>
<%
//==============================================================================
// Sezione per la Validazione
//==============================================================================
%>
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table width="90%">
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.sanzionesostitutiva.action.ActUploadRideterminazionePenaRevocaSS">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= aEveNotRichiesta.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sanzionesostitutiva.action.ActLoadDettaglioRideterminazionePenaRevocaSS">
        </td>
      </tr>
    </table>
  </FORM>
</div>

</body>
</html>  