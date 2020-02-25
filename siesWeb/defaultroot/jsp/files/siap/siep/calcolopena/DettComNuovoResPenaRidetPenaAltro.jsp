<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="eventonotificacomputo" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>


<!-- DettComNuovoResPenaRidetPenaAltro -->
<% 
//==============================================================================
// Form per la visualizzazione del dettaglio dell'Evento di Comunicazione
// nuovo residuo pena a seguito di Rideterminazione Pena Altro.
// Dettaglio - Stampa - Validazione
//==============================================================================

  EventoModel lEveComu = eventonotifica.getEvento();

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel) session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  MagistratoModel lMagistrato = eventonotifica.getMagistrato();
  if( lMagistrato == null)
    lMagistrato = new MagistratoModel();
%>
<html>

<head>
  <title> [S.I.E.S.] - Dettaglio Evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio <%=lEveComu.getDescrTipoProvvedimento()%>&nbsp;<%=lEveComu.getDescrMotivo()%></font>
      </td>
      
      <%
      // Visualizzo il tasto di stampa se l'evento non è validato
      if (   lEveComu.getFlagDocumentoRegistrato()==null
          || (   lEveComu.getFlagDocumentoRegistrato()!=null
              && lEveComu.getFlagDocumentoRegistrato().equals("N")
             )
         )
      {
      %>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaComNuovoResPenaRidetPenaAltro&IdEvento="+lEveComu.getIdEvento()%>"/>
      </jsp:include>
      <%}%>

      <!-- TOOLBAR HEADER -->
      <td class="LBG">
          <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=lEveComu.getIdEvento()%>" />
            <jsp:param name="FlagDocumentoRegistrato" value="<%=lEveComu.getFlagDocumentoRegistrato()%>" />
          </jsp:include>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


  <%
  //============================================================================
  // Visualizzazione Posizione Giuridica e pena da eseguire
  //============================================================================
  %>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
      <td class="L" colspan=3><font class="campo">DETENUTO PER ALTRA CAUSA</font> </td>
      <%}else{%>
      <td class="L" colspan=3><font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font>
      <%}%>
    </tr>
    
    <%
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
    {
      if(lAltraCausa.getIstitutoDetenzione() != null)
      {
      %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=3><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
          </td>
        </tr>
      <%}%>
      
      <%if (lAltraCausa.getAltroLuogo()!=null && lAltraCausa.getAltroLuogo().trim().length()>0) { %>
        <tr>
          <td class="l">Altro Luogo </td>
          <td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp; </td>
        </tr>
      <%} %>
      <%
    } 
    else
    {
      if(lLuogoDetenzione.getIstitutoDetenzione() != null)
      {
      %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=3><font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
               di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
          </td>
        </tr>
      <%
      }
      
      if (lLuogoDetenzione.getAltroLuogo()!=null && lLuogoDetenzione.getAltroLuogo().trim().length()>0) 
      { 
      %>
        <tr>
          <td class="l" >Altro Luogo </td>
          <td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp; </td>
        </tr>
      <%
      }
    }
    
    
    //==========================================================
    // Sezione con la pena da espiare se diversa da ergastolo
    //==========================================================
    if(   penaresidua.getIdPenaResidua() != null 
       && (   penaresidua.getFlagErgastolo() == null
           || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) 
          ) 
      )
    {
      // Reclusione se presente
      if ( !penaresidua.isQuantumReclusioneZero() )  {
      %>
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
 
      <% if ( !penaresidua.isQuantumArrestoZero()) { %>
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


    <%
      //========================================================================
      // Visualizzazione delle date di decorrenza e scadenza.
      // n.b. Le date potrebbero essere presenti se OE contro detenuto agli
      //      arresti domiciliari o Altra Causa
      //========================================================================
    %>
    
    <tr>
      <% if (penaresidua.getDataInizio()!=null) { %>
         <td class="l">Data Decorrenza Pena</td>
         <td class="l" colspan="1">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(), "yyyy") )%></font>
         </td>
      <% } %>

      <%
      if (penaresidua.getFlagErgastolo() != null)
      {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        <%
        }
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {
        %>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
        <%
        }
      }


      // Data fine Se non ergastolo 
      if (    penaresidua.getDataFine()!=null
          && (    penaresidua.getFlagErgastolo() == null
              || ( penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D") )
             )
         )
      {
        if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
        {
        %>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
        <%
        } else {
        %>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
        <%
        }
      }
      %>
    </tr>
       
       
       
    <%
    //==========================================================================
    // Data Emissione - Data Trasmissione - Magistrato Firmatario
    //==========================================================================
    %>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=1>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveComu.getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
      <td class="l">Data Trasmissione</td>
      <%if(eventonotifica.getNotifiche()!= null && eventonotifica.getNotifiche().length > 0){%>
      <td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%></font></td>
      <%} %>
    </tr>
    
    
    <tr>
      <td class="l">Magistrato</td>
      <td class="L" colspan="2">
        <font class="campo">
        <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
        </font>
      </td>
    </tr>
    
    
    <%
    //==========================================================================
    //  Sezione con le Notifiche
    //  ==> 
    //==========================================================================
    %>
   <%
    if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
    {
      for (int i=0; i < eventonotifica.getNotifiche().length; i++)
      {
      
        NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
        
        //=============================================
        // Autorità per l'esecuzione
        //=============================================
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
           && lNotMod.getCodTipoNotifica().equals("E")
           && lNotMod.getAutoritaEsterna()!= null
           && lNotMod.getAutoritaEsterna().getCodTipoAutorita()!= null
           && lNotMod.getAutoritaEsterna().getCodSede()!= null
          )
        {
        %>
        <tr>
          <td class="l">Autorità di Destinazione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
            di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
          </td>
        </tr>
    
          <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals(""))
          {%>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="l">
              <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
            <td>
          </tr>
          <%
          }
        } // end

        //=============================================
        // Istituto di Detenzione
        //=============================================
        if(   lNotMod.getCodTipoNotifica().equals("E")
           && lNotMod.getIstitutoDetenzione()!= null
          )
        {
        %>
        <tr>
          <td class="l">Istituto Detenzione</td>
          <td class="l">
             <font class="campo"> <%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
             <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font>
          </td>
        </tr>
        <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) {%>
          <tr>
            <td class="l">Note</td>
            <td class="l">
              <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
            <td>
          </tr>
        <%}%>
        <%
        }

        //======================================================================
        // Altra Autorità (nel caso di Mis. Alt. è l'autoritrà di Controllo)
        //======================================================================
        // Se getCodTipoNotifica().equals("AA")
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
           && lNotMod.getCodTipoNotifica().equals("AA")
           && lNotMod.getAutoritaEsterna()!= null
           && lNotMod.getAutoritaEsterna().getCodTipoAutorita()!= null
           && lNotMod.getAutoritaEsterna().getCodSede()!= null
          )
        {
        %>
        <tr>
          <%if(lPosizione.isMisuraAlternativa()) {%>
          <td class="l">Autorità di Controllo</td>
          <% } else {%>
          <td class="l">Altra Autorità</td>
          <%}%>
  
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
             di 
             <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
          </td>
        </tr>

        <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) {%>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="l">
              <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
            <td>
          </tr>
        <%}
        }  // end if getCodTipoNotifica().equals("AA")

        //====================
        // UEPE
        //====================
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
           && lNotMod.getCodTipoNotifica().equals("CS")
           && lNotMod.getCSSA() != null && lNotMod.getCSSA().getComune() != null
           && !lNotMod.getCSSA().getIndirizzo().equals("")
          )
        {%>
        <tr>
          <td class="l">UEPE</td>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getCSSA().getComune())%>-<%=StringUtils.toStringJSP(lNotMod.getCSSA().getIndirizzo())%></font>
          </td>
        </tr>
        <%}

        //============================
        // Magistrato di Sorveglianza
        //============================
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null 
           && lNotMod.getCodTipoNotifica().equals("MS") 
           && lNotMod.getUfficio()!= null &&  lNotMod.getUfficio().getDescrComune() != null
          )
        {
        %>
        <tr>
          <td class="l">Destinatario MDS</td>
          <td class="L">
            <font class="campo">
             MAGISTRATO DI SORVEGLIANZA</font> di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%>
            </font>
          </td>
        </tr>
        <%}

        //============================
        // Tribunale di Sorveglianza
        //============================
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null 
           && lNotMod.getCodTipoNotifica().equals("TS") 
           && lNotMod.getUfficio()!= null &&  lNotMod.getUfficio().getDescrComune() != null
          )
        {%>
        <tr>
          <td class="l">Destinatario TDS</td >
          <td class="L">
            <font class="campo">
            TRIBUNALE DI SORVEGLIANZA</font> di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%>
            </font>
          </td>
        </tr>
        <%}
        
        
        //============================
        // Restituzione OE
        //============================
        if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
           && lNotMod.getCodTipoNotifica().equals("R") // restituzione ordine di esecuzione
           && lNotMod.getAutoritaEsterna()!= null
           && lNotMod.getAutoritaEsterna().getCodTipoAutorita()!= null
           && lNotMod.getAutoritaEsterna().getCodSede()!= null
          )
        {
        %>
        <tr>
          <td class="l">Autorità</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
             di 
             <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
          </td>
        </tr>

        <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) {%>
          <tr>
            <td class="l">Indirizzo</td>
            <td class="l">
              <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
            <td>
          </tr>
        <%}%>
        <tr>
          <td class="l" colspan="2">
            <font class="campo">Per la restituzione dell'Ordine di Esecuzione &nbsp;</font>
          <td>
        </tr>
        
        <%}  //         
      } // end while
    } // end if %>

    <%
    //==========================================================================
    //  Notifiche agli Avvocati
    //==========================================================================
    if (eventonotifica.getAvvocati() !=null)
    {
      for(int lIndex = 0;lIndex<eventonotifica.getAvvocati().length; lIndex++ )
      {
        AvvocatoSiepModel lAvvocatoFasSiep = eventonotifica.getAvvocati()[lIndex];
      %>
        <tr>
          <td class="l">Avvocato per Notifica</td>
          <td class="L" colspan="2">
            <% if (eventonotifica.getAvvocati()[lIndex]!=null && eventonotifica.getAvvocati()[lIndex].getAvvocato()!=null) {%>
              <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getAvvocati()[lIndex].getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(eventonotifica.getAvvocati()[lIndex].getAvvocato().getNome())%></font>&nbsp;
              &nbsp;Foro di&nbsp;
              <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getAvvocati()[lIndex].getAvvocato().getForo())%></font>
              &nbsp;Difensore di&nbsp;
              <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getAvvocati()[lIndex].getAvvocato().getDescrTipo())%></font>
            <% } %>
          </td>
        </tr>
              
        <%
        // Autorità per la notifica all'Avvocato (di solito l'UNEP)
        for (int i=0; i<eventonotifica.getNotifiche().length; i++)
        {
          NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
          
          if(   lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null 
             && lNotMod.getAvvIdAvvocatoFascicoloSiep().compareTo(lAvvocatoFasSiep.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())== 0 
            )
          {
             AutoritaEsternaModel lAutEst = lNotMod.getAutoritaEsterna();
          %>
          <tr>
            <td class="l">Autorita Notifica</td>
            <td class="L" colspan=2>
              <font class="campo"><%=StringUtils.toStringJSP( lAutEst.getDescrTipoAutorita() )%></font>&nbsp;
              di
              <font class="campo"><%=StringUtils.toStringJSP( lAutEst.getDescrSede())%></font>&nbsp;
            </td>
          </tr>
          <tr>
            <td class="l">Note</td>
            <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
          </tr>
        <%}%>
      <%} // end for %>
      <%
      } // end for
    } // enf if
    %>
    
    

    
  </table>
  
  <br>
  <%
  //============================================================================
  // DIV per l'upload del documento e la validazione
  //============================================================================
  %>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"              value="siap.siep.calcolopena.action.ActUploadComNuovoResPenaRidetPenaAltro">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"         value="<%=lEveComu.getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.calcolopena.action.ActLoadDettComNuovoResPenaRidetPenaAltro">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  
  <br>
  <br>
</body>

</html>