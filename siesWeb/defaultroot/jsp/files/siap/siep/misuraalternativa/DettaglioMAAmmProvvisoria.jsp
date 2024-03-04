<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="UffTDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lPosGiuModificata" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="NoteCssa"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutC"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteTDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="notificaE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="UfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="verbale"             scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la visualizzazione del dettaglio Ammissione Provvisoria sia nel
// caso di AFFIDAMENTO in prova che di DETENZIONE domiciliare.
// La Form viene utilizzata sia per il dettaglio del provvedimento collegato
// al decreto/ordinanza, sia per il dettaglio del provvedimento collegato al
// verbale di sottomissione agli obblighi dopo la registrazione data inizio misura
//==============================================================================

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

	<script language="JavaScript">
	<%-- MEV_9 si aggiunge il tasto di modifica --%>
    function modifica() {
      document.modifica.submit();
    }
  </script>
</head>
<body class="corpo">

<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <% if(tipoMisura.equals("AFFIDAMENTO")) {%>
      <font class="campo">Dettaglio Ammissone Provvisoria ad Affidamento in Prova</font>
      <% } else if(tipoMisura.equals("DETENZIONE")) {%>
      <font class="campo">Dettaglio Ammissione Provvisoria Detenzione Domiciliare</font>
      <% } %>
    </td>

<% EventoModel lProvvedimento = new EventoModel(evento);

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();

%>

<%
if (   "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
    || eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
   )
{
%>	
	<%-- MEV_9 si aggiunge il tasto di modifica --%>
  <td class="LBG">
    <a href="Javascript:modifica()">
      <img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Nota di Trasmissione" width="24" height="24" border="0">
    </a>
  </td>
 <!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActStampaMAAmmProvvisoria&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&tipoMisura="+tipoMisura%>"/>
   </jsp:include>
<%}%>


<%if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
      || "N".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
     ) 
{%>
   <!-- BOTTONE DI VALIDAZIONE DIRETTA -->
  <td class="LBG">
    <a href="/jsp/Main.jsp?Action=siap.siep.misuraalternativa.action.ActUploadMAAmmProvvisoria&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
      <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
    </a>
  </td>
<%}%>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<%
// MEV_9 si aggiunge il tasto di modifica
if (   eventonotifica.getEvento().getFlagDocumentoRegistrato() == null
    || "N".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())
   )
{
%>  
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="modifica">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento() %>">

	<% if(tipoMisura.equals("AFFIDAMENTO")) {%>
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActLoadModificaMAAmmProvAffi">
	<% } else if(tipoMisura.equals("DETENZIONE")) {%>
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActLoadModificaMAAmmProvDetDom">
	<% } %>

</form>
<% } %>   



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
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
              // }
%>
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
            </tr>
<%
          }
        }
%>
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
</tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
          <tr>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
           </tr>
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
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
         {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
           <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%></font>
         </td>
<%
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%></font>
         </td>


<%            }
      }
%>
</tr>

        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
         </td>
<%
if(eventonotifica !=null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0)
{
%>         
        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>

         </td>
<%}
%>         
      </tr>


 
<%
//==============================================================================
//
//==============================================================================
String lTipoProvvedimento = "Provvedimento";
String lTipoProvvedimentoArt = "il Provvedimento";
if("02".equals(misuraalternativa.getCodTipoDecisione() )){
  lTipoProvvedimento = "Decreto";
  lTipoProvvedimentoArt = "il Decreto";
}
else if("03".equals(misuraalternativa.getCodTipoDecisione() )) {
  lTipoProvvedimento = "Ordinanza"; 
  lTipoProvvedimentoArt = "l'Ordinanza";
}
%>
<tr>
<%if(misuraalternativa.getChiaveAnnoFascicoloSius()!= null) {%>
  <td class="l">Anno / Numero SIUS</td>
  <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font>
  </td>
<%} %>

<% if(misuraalternativa.getAnnoRegistro()!= null){ %>
  <td class="l"> Anno / Numero <%=lTipoProvvedimento %> </td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>
    <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font>
  </td>
<%}%>
 </tr>
 <tr>
    <td class="l">Ufficio che ha emesso <%=lTipoProvvedimentoArt%> </td>
    <%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
    <%
    	String descrTipoUfficio = StringUtils.toStringJSP(UfficioEmittente.getDescrTipoUfficio());
    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
    			"UDSM".equals(UfficioEmittente.getCodTipoUfficio())) {
    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
    	}
    %>
    <td class="l"> <font class="campo"><%=descrTipoUfficio%>&nbsp;di&nbsp;<%=StringUtils.toStringJSP(UfficioEmittente.getDescrComune())%></font></td>
 </tr>
 <tr>
   <td class="l">Oggetto <%=lTipoProvvedimento %> </td>
   <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
   <td class="l">Data Emissione <%=lTipoProvvedimento %> </td>
   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%></font></td>
 </tr>

 <tr>
   <td class="l">Data Esecutivita'</td>
   <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataEsecutivita(),"dd-MM-yyyy"))%></font></td>
 </tr>

 <%if(verbale.getDataEmissione()!= null) {%>
  <tr>
      <td class="l">Data Sottoscrizione Verbale Obblighi</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%></font></td>
      <input type="hidden" name="flag9" value="1">
  </tr>
 <%}%>
 
<tr>
  <% if(misuraalternativa.getDataInizioMisura()!= null ) {%>
    <td class="l">Data Inizio Misura</td>
    <td class="l"><font class="campo">
    <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%>
    </font></td>
  <%}%>
  
  <%if(misuraalternativa.getDataFineMisura()!= null) {%>
    <td class="l">Data Fine Misura</td>
    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(),"dd-MM-yyyy"))%></font></td>
  <%}%>
</tr>
  
<tr> 
<%if(tipoMisura.equals("DETENZIONE")){%>
  <td class="l">Luogo della Detenzione</td>
<%}else if(tipoMisura.equals("AFFIDAMENTO")){%>
  <td class="l">Luogo della Prova</td>
<%}%>
  <td class="l">
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>&nbsp;</font>
  <td>
</tr>

<%if(misuraalternativa != null && misuraalternativa.getNote() != null){%>
<tr>
  <td class="l">Note</td>
  <td class="l">
    <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getNote())%>&nbsp;</font>
  </td>
</tr>
<% } %>

<% if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}%>
<tr>
  <td class="Titolo" colspan="4">Destinatari</td>
</tr>
<%
int conta=0;
if(eventonotifica !=null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0)
{
  while(conta < eventonotifica.getNotifiche().length)
  {
    if( eventonotifica.getNotifiche()[conta].getCodTipoNotifica().equals("E") &&
       eventonotifica.getNotifiche()[conta].getIstitutoDetenzione() != null)
    {%>

    <tr>
      <td class="l">Istituto Detenzione</td>
      <td class="l">
        <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[conta].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
        <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[conta].getIstitutoDetenzione().getDescrComune())%></font>
      </td>
    </tr>
  
      <%if(eventonotifica.getNotifiche()[conta].getNote()!= null && !eventonotifica.getNotifiche()[conta].getNote().equals("")){%>
      <tr>
        <td  class="l">Note</td>
        <td  class="L"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[conta].getNote())%>&nbsp;</font></td>
      </tr>
      <% } %>
    <% } %>
    <% 
    conta++;
  } // end while
} %>


<%
  //============================================================================
  // Aggiungo la sezione dei destinatari per la Restituzione Ordine Esecuzione
  //============================================================================
  int count2=0;
  while(count2 < eventonotifica.getNotifiche().length)
  {
    NotificaModel lNotMod = eventonotifica.getNotifiche()[count2];

    if ( lNotMod.getCodTipoNotifica().equals("R") ) {
    %>
    <!--tr>
      <td class="Titolo" colspan="4">Destinatari per la Restituzione Ordine di Esecuzione</td>
    </tr-->
      <%
      if (lNotMod.getIstDetIdIstitutoDetenzione()!= null ) 
      {
        // Istituto di detenzione
        IstitutoDetenzioneModel istitutoModel = lNotMod.getIstitutoDetenzione();
      %>
      <tr>
        <td class="l" >Istituto di Detenzione </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(istitutoModel.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(istitutoModel.getDescrComune())%></font>
        </td>
      </tr>
      <%
      }
      else if (lNotMod.getAutoritaEsterna() != null) {
        // Autorità competente
        AutoritaEsternaModel lModAut = lNotMod.getAutoritaEsterna();
      %>
    <tr>
      <td class="l" >Destinatari per la Restituzione Ordine di Esecuzione </td>
      <td class="l" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(lModAut.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(lModAut.getDescrSede())%></font>
      </td>
    </tr>
      <%
      }
      %>
    <tr height="3"><td></td></tr>
    <%
    } // end if lNotMod.getCodTipoNotifica().equals("R")

    count2++;
  }  // end while
%>



<% if(autoritaEsternaE!= null && autoritaEsternaE.getCodTipoAutorita() != null && autoritaEsternaE.getCodSede()!= null) 
{%>
  <tr>
    <% if ((lPosizione.isLibero())&& tipoMisura.equals("DETENZIONE")) { %>
    <td class="l" width=30%>Destinatario per esecuzione</td>
    <% } else { %>
    <td class="l">Autorità di Polizia Competente per territorio</td>
    <%}%>
    
    <td class="L">
      <font class="campo">
           <%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita())%>
      </font>
      <% if(autoritaEsternaE !=null && !autoritaEsternaE.getDescrSede().equals("-")) { %>
            di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %></font>
      <%}%>
    </td>
  </tr>

  <% if(NoteAutE != null && !NoteAutE.equals("")) { %>
  <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
      <font class="campo"><%=NoteAutE%>&nbsp;</font>
    <td>
  </tr>
  <%
  }
}  // end if autoritaEsternaE!= null


if(daticssa != null && daticssa.getComune() != null && !daticssa.getIndirizzo().equals(""))
{%>
  <tr>
    <td class="l"><%=StringUtils.toStringJSP(daticssa.getTipoDesc())%></td>
    <td class="l" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
    </td>
  </tr>
  <%if(NoteCssa != null && !NoteCssa.equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td  class="L" colspan="3">
      <font class="campo"><%=NoteCssa%>&nbsp;</font>
    </td>
  </tr>

<%}}%>

<!--UEPE competente-->


<%
if(UffUDS!= null && !UffUDS.equals(""))
{%>
  <tr>
    <td class="l">Magistrato Preposto al controllo</td >
    <td class="L" colspan="3"><%=StringUtils.toStringJSP(UffUDS)%></td>
  </tr>
  <%if(NoteUDS!= null && !NoteUDS.equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td  class="L" colspan="3">
       <font class="campo"><%=NoteUDS%>&nbsp;</font>
     </td>
  </tr>
<%}}

if(UffTDS!= null && !UffTDS.equals(""))
{%>
  <tr>
    <td class="l">Tribunale Preposto al controllo</td >
    <td class="L" colspan="3"><%=StringUtils.toStringJSP(UffTDS)%></td>
  </tr>
  <%if(NoteTDS!= null && !NoteTDS.equals("")){%>
  <tr>
    <td class="l">Note</td>
    <td  class="L" colspan="3">
       <font class="campo"><%=NoteTDS%>&nbsp;</font>
    </td>
  </tr>
  <% } %>
<% } %>

<%if(autoritaEsternaC != null && autoritaEsternaC.getCodTipoAutorita()!= null && autoritaEsternaC.getCodSede()!= null)
{%>
 <tr>
    <td class="l">Autorità Competente per territorio</td>
    <td class="L">
      <font class="campo">
        <%=StringUtils.toStringJSP(autoritaEsternaC.getDescrTipoAutorita())%></font>
        <%if(autoritaEsternaC !=null && !autoritaEsternaC.getDescrSede().equals("-")){%>
        di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %></font>
        <%}%>
    </td>
 </tr>
  <%if(NoteAutC!= null && !NoteAutC.equals("")){%>
  <tr>
    <td class="l">Indirizzo</td>
    <td class="L">
         <font class="campo"><%=NoteAutC%>&nbsp;</font>
    </td>
  </tr>
<%}}%>

<%
int count=0;
if(eventonotifica !=null && eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
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
        <td class="l">Notifica per difensore</td>
        <td class="L" colspan="3">
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
        <td class="L" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
           di
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")){%>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
      </tr>
<%
}}
count++;
}
}
%>

</table>

 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActUploadMAAmmProvvisoria">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misuraalternativa.action.ActDettaglioMAAmmProvvisoria">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
          </td>
        </tr>
      </table>
    </form>
  </div>
  <br>
  <br>
</body>
</html>