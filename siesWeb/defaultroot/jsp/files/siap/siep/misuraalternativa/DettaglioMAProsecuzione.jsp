<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="sedesorveglianza"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="sedeautcompetente"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<!-- <jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>-->
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="sorveglianza"       scope="request" class="java.lang.String"/>
<jsp:useBean id="Cssa"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteCssa"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UffTDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="verbale"      scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="notificaCSSA" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="NoteTDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="nuovapenaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"       scope="request" class="java.lang.String"/>

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
  <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
  <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<% EventoModel lProvvedimento = new EventoModel(evento);

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();

%>
<%if(tipoMisura.equals("DETENZIONE")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Detenzione Domiciliare (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("AFFIDAMENTO")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Affidamento In Prova (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("SEMILIBERTA")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Semilibertà (SENZA CUMULO)</font>
<%}else if(tipoMisura.equals("DETENZIONECUMULO")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Detenzione Domiciliare (CON CUMULO)</font>
<%}else if(tipoMisura.equals("AFFIDAMENTOCUMULO")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Affidamento In Prova (CON CUMULO)</font>
<%}else if(tipoMisura.equals("SEMILIBERTACUMULO")){%>
    <font class="campo">Dettaglio Prosecuzione Provvisoria Semilibertà (CON CUMULO)</font>
<%}%>

</td>
<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
 if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAProsecuzione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
 <%}%>

<%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 {%>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAProsecuzione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
<%}%>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<table>
<%if(flagmisura.equals("N"))
{%>

  <tr><td><input type="HIDDEN" name="flagmisura" value="N"></td></tr>
 <%}else{%>
   <tr><td><input type="HIDDEN" name="flagmisura" value="S"></td></tr>
<%}%>
 <tr><td><input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>"></td></tr>

    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
      <font class="campo">
      <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
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
<%
            //  if(lLuogoDetenzione.getDescrLuogo()!=null)
            //  {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
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

       if (penaresidua.getDataInizio() != null)
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
 <%--tr>
         <td class="l">Data Fine Pena Automatica</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
 </tr--%>
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
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
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

<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>

         </td>
        <td class="l">Data Trasmissione</td>
        <td class="L" >
        	<%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0)
        	  {
        	%>
             	<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
            <%} %>
         </td>
      </tr>

 <tr>
<%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null){%>
      <td class="l">Anno / Numero Sius</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
      <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
     </td>
<%}
if(misuraalternativa.getAnnoRegistro()!= null){%>
     <td class="l"> Anno / Numero Ordinanza </td>
     <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
     <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
     </td>
<%}%>
</tr>
 <tr>
    <td class="l">Ufficio Emittente </td>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrTipoUfficio())%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescProvincia())%></font></td>
 </tr>
 <tr>
   <td class="l">Oggetto Decreto </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
   <td class="l">Data Emissione Decreto </td>
   <td class="l">
<font class="campo">
   <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%>
    </font>
   </td>
  </tr>
<%if(misuraalternativa != null && misuraalternativa.getDataInizioMisura()!= null)
{%>
   <tr>
     <td class="l">Data decorrenza misura</td>
     <td class="l"><font class="campo">
      <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
      </font></td>
  </tr>
<%}%>


<%if(misuraalternativa != null && misuraalternativa.getNote() != null){%>
<tr>
<td class="l">Note</td>
    <td class="l">
         <font class="campo"><%=misuraalternativa.getNote()%>&nbsp;</font>
    <td>
</tr>
<%}%>


<%if(nuovapenaresidua != null)
{%>

<tr>
  <td class="l">Data Decorrenza Pena da Espiare</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
</tr>

<tr>
<%if(nuovapenaresidua.getDataFine().equals(nuovapenaresidua.getDataFinePresunta()))
{
%>
         <td class="l">Data Fine Pena da Espiare</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }else
        {%>
         <td class="l">Data Fine Pena da Espiare</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(nuovapenaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%}%>


</tr>
<%}%>
<%
 if(misuraalternativa != null  && misuraalternativa.getDataAltroTitolo() != null)
   {%>

 <tr>
        <td class="l">Data Sentenza</td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataAltroTitolo(),"dd-MM-yyyy"))%>
          </font>
   </td>
</tr>
    <%}%>


  <%  if(misuraalternativa!= null &&  misuraalternativa.getAnnoAltroTitolo()!= null &&  misuraalternativa.getNumAltroTitolo()!= null)
     {%>
<tr>
   <td class="l" >Anno / Numero Sentenza</td>
    <td class="l" ><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getAnnoAltroTitolo())%> /
      <%=StringUtils.toStringJSP(misuraalternativa.getNumAltroTitolo())%></font></td>
</tr>
   <%}%>


<%if(misuraalternativa != null && misuraalternativa.getCodAutoritaAltroTitolo() != null && !misuraalternativa.getCodAutoritaAltroTitolo().equals("-")
    && misuraalternativa.getCodLuogoAltroTitolo() != null && !misuraalternativa.getCodLuogoAltroTitolo().equals("-"))
{%>
<tr>
    <td class="l">Autorità Emittente</td>
   <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescAutoritaAltroTitolo())%> DI <%=StringUtils.toStringJSP(misuraalternativa.getDescLuogoAltroTitolo())%></font></td>
 </tr>
<%}%>

<%if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}%>

<%int cont=0;
while(cont < eventonotifica.getNotifiche().length)
{
  if(eventonotifica.getNotifiche()[cont].getCodTipoNotifica().equals("E") &&
    eventonotifica.getNotifiche()[cont].getIstitutoDetenzione() != null)
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
<td  class="l">Note</td>
      <td  class="L"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getNote())%>&nbsp;</font></td>
</tr>

 <%}}
cont++;
}%>

<%if(daticssa != null && daticssa.getComune() != null && !daticssa.getIndirizzo().equals("")){%>

<tr>
      <td class="l">UEPE</td>
         <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
      </td>
  </tr>
<%if(NoteCssa != null && !NoteCssa.equals("")){%>

<tr>
 <td class="l">Note</td>
   <td  class="L">
      <font class="campo"><%=NoteCssa%>&nbsp;</font>
   </td>
</tr>

<%}}%>

<%if(UffUDS!= null && !UffUDS.equals(""))
 {%>
   <tr>
     <td class="l">Magistrato Preposto al controllo</td >
      <td class="L">
      <font class="campo">
             MAGISTRATO DI SORVEGLIANZA</font> di <font class="campo"><%=StringUtils.toStringJSP(UffUDS)%>
      </font>

<%if(NoteUDS!= null && !NoteUDS.equals(""))
 {%>
<tr>
 <td class="l">Note</td>
   <td  class="L">
     <font class="campo"><%=NoteUDS%>&nbsp;</font>
   </td>
</tr>
<%}
}
%>

   <tr>
     <td class="l">Destinatario</td >
      <td class="L"> <font class="campo">TRIBUNALE DI SORVEGLIANZA</font> di

      <font class="campo"><%=StringUtils.toStringJSP(UffTDS)%></font>
     </td>
  </tr>
<%if(NoteTDS != null && !NoteTDS.equals(""))
{%>
<tr>
 <td class="l">Note</td>
   <td  class="L">
      <font class="campo"><%=NoteTDS%>&nbsp;</font>
   </td>
</tr>
<%}%>

<%int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();


     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
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
	    <td class="l">Autorita Notifica</td>
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
            <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
            <input type="HIDDEN" name="tipoMisura" value="<%=tipoMisura%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadMAProsecuzione">

            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioMAProsecuzione">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>