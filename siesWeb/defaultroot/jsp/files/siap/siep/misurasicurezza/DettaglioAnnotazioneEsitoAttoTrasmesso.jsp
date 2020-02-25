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
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="magistrato"        scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="annotazioneEsito"  scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>
<jsp:useBean id="ufficioEsito"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioInoltro"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>


<jsp:useBean id="noteautoritaEsterna" scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>

<% // Bean per il dettaglio dei dati del fascicolo %>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="residenza"           scope="request" class="siap.sico.residenza.model.ResidenzaModel"/>
<jsp:useBean id="penacumulo"          scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel"/>
<jsp:useBean id="listaMisure"         scope="request" class="java.util.Vector"/>

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
        <font class="campo">DETTAGLIO ANNOTAZIONE ESITO ATTI TRASMISSIONI PER COMPETENZA</font>
      </td>
  
      <%if(   !"S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           && !"A".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          )
       { %>
        <!-- BOTTONE DI STAMPA -->
        <input type="hidden" name="tipo" value="D">
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadAnnotaEsitoTrasmissione&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotaEsitoTrasmissione&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
            <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
          </a>
        </td>
        
        <%
        // La stampa non è più prevista. Solo validazione.
        %>
      <%} %>
   

      <%
        //TOOLBAR HEADER (per il tasto di MODIFICA e CANCELLAZIONE)
        String lModificabile = "NO";
        if(   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
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
// Eventualmente da copiare dal DettaglioTrasmissioneCompetenza.jsp
%>


       
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
      <td class="Titolo" colspan="2">Dati Provvedimento</td>
    </tr>
      
    <tr>
      <td class="l">Tipologia Atto</td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrTipoProvvedimento())%></font></td>
    </tr>
    <tr>
      <td class="l">Oggetto Atto</td>
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
  
    <% if ( eventonotifica.getNotifiche().length>0 && eventonotifica.getNotifiche()[0].getAutoritaEsterna() !=null) {%>
    <tr>
      <td class="l">Altro Destinatario
      <td class="L">
        <font class="campo">
        <%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">Luogo
      <td class="L">
        <font class="campo">
        <%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede()%>&nbsp;
        </font>
      </td>   
    </tr> 
  <%}%>
</table>

<br>

<table width=90%>
  <tr>
    <td class="Titolo" colspan="2">Esito Trasmissione</td>
  </tr>
  <tr>
    <td class="l">Esito comunicato da Ufficio del Pubblico Ministero</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(ufficioEsito.getDescrTipoUfficio(),"")%></font></td>
  </tr>
  <tr>  
    <td class="l">Luogo</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(ufficioEsito.getDescrComune(),"")%></font></td>
  </tr>  
  <tr>  
    <td class="l">Data Esito</td>
    <td class="L"><font class="campo"><%=DateUtils.getDateToString(annotazioneEsito.getDataEsito(),"dd/MM/yyyy")%></font></td> 
  </tr>
  
  <tr>
    <td class="l">Esito Provvedimento</td>
    <% if (ICostantiJMS.TRASFERITO.equals(annotazioneEsito.getCodEsito()) ) { %>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font>
      <font class="label"> a </font>
      <font class="campo"><%=ufficioInoltro.getDescrTipoUfficio()+" di "+ufficioInoltro.getDescrComune()%> </font>
    </td>
    <% } else {%>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font></td>
    <% } %>
  </tr>
  
  <% if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(annotazioneEsito.getCodEsito()) ) {%>        
  <tr>
    <td class="l">Procedimento di classe IV iscritto <br>(Anno/Progressivo)</td>
    <td class="L"><font class="campo"><%=annotazioneEsito.getChiaveAnno()%>/<%=annotazioneEsito.getChiaveProgr()%></font></td>
  </tr>  
  <% } %>
  
  <tr>
    <td class="l">Motivazioni Esito</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"&nbsp;")%></font></td> 
  </tr>
</table>

<% 
// Dopo la validazione, nel caso di 'iscritto in classe IV'  visualizzo il tasto 
// per agganciare la action di Definizione Manuale
if (   "S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
    && ICostantiJMS.ISCRITTO_CLASSE_IV.equals(annotazioneEsito.getCodEsito()) 
   ) 
{ %>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciEsitoTrasmissioneCompetenza" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"       value="siap.siep.misurasicurezza.action.ActLoadInserisciArchiviazioneManuale">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0612">

  <table>
    <tr>
      <td class="lNoBord" colspan="2">
        <INPUT class="bottone" type="submit" name="I" value="Definizione Procedimento" >
      </td>
    </tr>
  </table>

</form>

<% } %>
 
<br>

  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadAnnotaEsitoTrasmissione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.misurasicurezza.action.ActLoadDettaglioAnnotaEsitoTrasmissione">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>
</html>