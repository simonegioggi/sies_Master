<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="fascicolo" 		scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
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

<!-- 		DettaglioSollecitoRichiestaAttiTrasfCompCumulo    -->
<html>
<head>
  <title>[S.I.E.S.] - Riscontro Trasmissione Atti per Trasf.Comp. - Sollecito </title>
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
        <font class="campo">DETTAGLIO SOLLECITO RICHIESTA ATTI PER TRASMISSIONE COMPETENZA CUMULO</font>
      </td>
  
      <%if(   !"S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           && !"A".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          )
       { %>
        <!-- BOTTONE DI STAMPA -->
          <input type="hidden" name="tipo" value="D">
      		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaSollecitoRichiestaAttiTrasfCompCumulo&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      		</jsp:include>
      <%} %>
   
   
      <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE -->  
      <%
      if ( UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lFascicoloAssociato.getChiaveUfficio()))
      {
	        String lModificabile = "NO";
	        if(    eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
	            || "N".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
	          ) 
	          		lModificabile = "SI";	
      %>
		    <td class="LBG">  
		      <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
		        <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
		        <jsp:param name="ValoreIdEntita" value="<%=eventonotifica.getEvento().getIdEvento()%>" />
		        <jsp:param name="FlagDocumentoRegistrato" value="<%=eventonotifica.getEvento().getFlagDocumentoRegistrato()%>" />
		        <jsp:param name="Modificabile" value="<%=lModificabile %>" />
		      </jsp:include>
		    </td>
		
  <%  } %> 
        
    </tr>
  </table>

  <br>  
 <%
  //===================================================================
  // 	INTESTAZIONE
  //===================================================================
	SoggettoModel soggetto = fascicolo.getSoggetto();
	SentenzaModel sentenza = fascicolo.getSentenza();  
%>    
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>" title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>
          &nbsp;
          <% if(fascicolo.getFlagCumulante()!=null && fascicolo.getFlagCumulante().equals("S")) { %>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
          <% } %>

          <% if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-")) {%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
          <% } %>

          <% if(   fascicolo.getCodStatoFascicolo() != null && fascicolo.getCodStatoFascicolo().equals("01")) { %>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
          <% } %>

          <%
            if(   (   penaresidua != null
                   && "S".equals(penaresidua.getFlagPenaSospesa())
                  )
               || (    fascicolo!= null && fascicolo.getChiaveProgr() != null
                    && fascicolo.getChiaveProgr().intValue() >= 30000
                    && fascicolo.getChiaveProgr().intValue() < 40000
                  )
              )
            {
              if(   fascicolo!= null && fascicolo.getChiaveProgr() != null
                 && fascicolo.getChiaveProgr().intValue() >= 30000
                 && fascicolo.getChiaveProgr().intValue() < 40000
                )
              {
              %>
              <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
              <% } else { %>
              <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
              <%
              }
            }

            if(   penaresidua != null
               && penaresidua.getFlagPenaSospesa()!= null
               && penaresidua.getFlagPenaSospesa().equals("I"))
            {
            %>
            <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
            <%
            }
        
            if(   penaresidua != null
               && penaresidua.getFlagPenaSospesa()!= null
               && penaresidua.getFlagPenaSospesa().equals("D"))
            {
            %>
            <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
            <% } %>
            
            <% if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))) { %>
              &nbsp;
              <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
            <% } %>
      </td>
    </tr>
    
    
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
      <% if (soggetto.getSesso().compareTo("F")==0) { %>
          <font class="label">nata il :</font>&nbsp;
      <% } else { %>
          <font class="label">nato il :</font>&nbsp;
      <% } %>

      <% 
      if(soggetto.getDataNascita() == null)
      {
          if(soggetto.getDataNascitaPresunta().equals("S")) {%>
          <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
          <% } else {%>
          <font class="campo">***</font>&nbsp;
          <%}
      } else {%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <% } %>
      
      <font class="label">in : </font>
      <font class="campo">
      <% if (soggetto.getDescrComuneNascita().compareTo("-")==0) { %>
          <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
      <% } else { %>
          <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
      <% } %>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> </a>&nbsp;  
          <font class="label">del</font>&nbsp;
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> 
        &nbsp;<font class="label"> Emessa da: </font> <% 
        }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
        <% if (sentenza.getNumSezioneAutoritaEmittente() != null) { %>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table>

<%--  jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/ --%> 

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
    { %>
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
  
    <% if (eventonotifica.getNotifiche().length>1) { %>
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
      if (   "AA".equals(lNotifica.getCodTipoNotifica())
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
             <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActUploadSollecitoRichiestaAttiTrasfCompCumulo">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.modulocumulo.action.ActDettaglioSollecitoRichiestaAttiTrasfCompCumulo">
          </td>
        </tr>
      </table>
    </FORM>
  </div>

  <br>
</body>
</html>