<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione"%>

<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="magistrato"        scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="annotazioneEsito"  scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>
<jsp:useBean id="ufficioEsito"      scope="request" class="siap.sico.ufficio.model.UfficioModel"/>


<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Misure di Sicurezza </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript">
  
  function eseguiFunzione(action)
    {
      document.Dett.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.Dett.submit();
    }
  
  </script>
  
  <script language="JavaScript1.2">
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>  
</head>


<body class="corpo">

  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
      </td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">DETTAGLIO ANNOTAZIONE ESITO ATTI TRASMISSIONI PER COMPETENZA (ex artt. 663 e 665 comma 4 c.p.p.)</font>
      </td>
  
      <%if(   !"S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           && !"A".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          )
       { %>
        <!-- BOTTONE DI VALIDAZIONE -->
        <input type="hidden" name="tipo" value="D">
        <td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.presaincarico.action.ActValidaAnnotaEsitoTrasmComp&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.presaincarico.action.ActDettAnnotaEsitoTrasmComp&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
            <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
          </a>
        </td>
        
        <%
        // La stampa non è più prevista. Solo validazione.
        %>
      <%} %>
   

      <%  //TOOLBAR HEADER (per il tasto di MODIFICA e CANCELLAZIONE)
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

  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="Dett">
   <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   <input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO %>" value="<%=eventonotifica.getEvento().getIdEvento() %>">
   <input type="hidden" name="<%=ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ID_ESITO_TRASMISSIONE %>" value="<%=annotazioneEsito.getIdEsitoTrasmissione()%>">

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
  </tr>
</table>
  
  <br>
  
  <table width="90%">
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

<table width="90%">
  <tr>
    <td class="Titolo" colspan="2">Esito Trasmissione</td>
  </tr>
  <tr>
    <td class="l">Esito comunicato da Ufficio del Pubblico Ministero</font></td>
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
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getDescrEsito(),"&nbsp;")%></font></td>
  </tr>
  
  <% if ( ICostantiJMS.PRESAINCARICO.equals(annotazioneEsito.getCodEsito()) || 
		  ICostantiJMS.ASSORBITO_IN_CUMULO.equals(annotazioneEsito.getCodEsito()) ) { %>        
  <tr>
    <td class="l">Procedimento che determina la competenza <br>(Anno/Progressivo)</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getChiaveAnno(),"-")%>/<%=StringUtils.toStringJSP(annotazioneEsito.getChiaveProgr(),"-")%></font></td>
  </tr>  
  <% } %>
  
  <tr>
    <td class="l">Motivazioni Esito</td>
    <td class="L"><font class="campo"><%=StringUtils.toStringJSP(annotazioneEsito.getNoteEsito(),"&nbsp;")%></font></td> 
  </tr>
</table> 


<%
//==============================================================================
// CUMULO STEP2 - Test x agganciare le funzioni di Archiviazione dopo 
// l'annotazione del provvedimento di cumulo
//==============================================================================
%>
<br>
<% if( ICostantiJMS.ASSORBITO_IN_CUMULO.equals(annotazioneEsito.getCodEsito()) && 
	   "S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato()) )  { %> 
	<table  onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" 
         	onMousedown="over_effect(event,'inset')"  onMouseup="over_effect(event,'outset')" >
	  <tr><td class="Titolo" colspan="2">Archiviazione</td></tr>
	
	  <tr>
	    <td width="50%" class="menulines" nowrap>
	      <a href="javascript:eseguiFunzione('siap.siep.archiviazione.action.ActLoadInserisciAssorCumulo')">Perdita di competenza</a>
	    </td>
	    <td width="50%" class="menulines" nowrap>
	      <a href="javascript:eseguiFunzione('siap.siep.archiviazione.action.ActLoadInserisciAnnProvCumulo')">Provvedimento di cumulo (Dopo archiviazione)</a>
	    </td>   
	  </tr>
	    
	</table>
	<br>
 <% } %>
 
</form> 
</body>
</html>