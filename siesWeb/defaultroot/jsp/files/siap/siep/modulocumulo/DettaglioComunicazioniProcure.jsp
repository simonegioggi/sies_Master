<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.jms.ICostantiJMS"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>

<jsp:useBean id="fascicolo" 		scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<% // %>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<jsp:useBean id="ProcedimentiCumulati" scope="request" class="java.util.Vector"/>
<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
  NotificaModel[] lNotifiche = eventonotifica.getNotifiche(); 
%>

<!-- 		DettaglioComunicazioniProcure    -->
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
        <font class="campo">DETTAGLIO COMUNICAZIONI DI CUMULO ALLE PROCURE E UFFICI DI SORVEGLIANZA</font>
      </td>
  
      <%if(   !"S".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
           && !"A".equalsIgnoreCase(eventonotifica.getEvento().getFlagDocumentoRegistrato())
          )
       { %>
        <!-- BOTTONE DI STAMPA -->
          <input type="hidden" name="tipo" value="D">
      		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        		<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniProcure&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&IdIstruttoriaCumulo="+IstruttoriaCumulo.getIdIstruttoriaCumulo()%>"/>
      		</jsp:include>
      <%} %>
   
   
      <!-- TOOLBAR HEADER (per il tasto di TRASMISSIONE) -->  
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
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>&nbsp;&nbsp;
    </td>
    <td>&nbsp;</td>
    <%}%>
    
    <%
    if(   eventonotifica != null && eventonotifica.getNotifiche()!= null 
       && eventonotifica.getNotifiche().length >0 && eventonotifica.getNotifiche()[0] != null 
       && eventonotifica.getNotifiche()[0].getDataInvio() != null )
    { %>
    
      <td class="l">Data Trasmissione</td>
      <td class="L" >
        <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
      </td>
    <%}%>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>

  <table width=90%>
    <tr>
      <td class="Titolo" colspan="3">Uffici Destinatari della Comunicazione - Procure</td>
    </tr>
<%  
  String lLastCodUfficio = "";
	for (int i=0;i< lNotifiche.length;i++) {
		NotificaModel lNotifica = lNotifiche[i];

    if (   !"UDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
        && !"UDSM".equals(lNotifica.getUfficio().getCodTipoUfficio())
        && !"TDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
        && !"TDSM".equals(lNotifica.getUfficio().getCodTipoUfficio())
       )
    {
%>
    <tr> 
      <% if (lNotifica.getUfficio().getCodUfficio().equals(lLastCodUfficio)) { %>
      <td class="L"></td>
      <% } else { %>
      <td class="L">
        <font class="campo"><%=lNotifica.getUfficio().getDescrTipoUfficio()%>&nbsp;</font>
        di &nbsp;
        <font class="campo"> <%=lNotifica.getUfficio().getDescrComune()%>&nbsp; </font>
      </td>  
      <% } %>


      <%  
        lLastCodUfficio = lNotifica.getUfficio().getCodUfficio();

        Iterator ItxP = ProcedimentiCumulati.iterator();
        while(ItxP.hasNext())
        {
          ProcedimentoCumulatoModel ProcMod = (ProcedimentoCumulatoModel) ItxP.next();
          if(ProcMod!=null && ProcMod.getIdProcedimentoCumulato()!=null)  
          {
            if( lNotifica.getCurIdCuratore()!=null && lNotifica.getCurIdCuratore().equals(ProcMod.getTitIdTitoloCumulato()))  
            { 
              String nSiep = "";
              if ("S".equals(ProcMod.getFlagAccorpato()) && ProcMod.getUfficioOrigine()!=null ){
                UfficioModel lUfficioOrigine = ProcMod.getUfficioOrigine();
      
                nSiep = ProcMod.getChiaveAnnoFasCumulato() +"/"+ ProcMod.getChiaveProgrOrigine();
                nSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>"; 
              }
              else {
                nSiep = ProcMod.getChiaveAnnoFasCumulato() +"/"+ ProcMod.getChiaveProgrFasCumulato();
              }



%>              
          <td class="l"> Procedimento Cumulato:   Anno e Numero SIEP </td> 
          <td class="L" >
            <font class="campo"><%=nSiep%></font>
          </td>   
      <%    }
          }
        }
      %>          
		<input type="HIDDEN" name="<%=ICostantiJMS.COD_UFFICIO_DESTINATARIO%>"  value="<%= lNotifica.getUfficio().getCodUfficio() %>">
		<input type="HIDDEN" name="idNotifica"  value="<%= lNotifica.getIdNotifica() %>">
		<input type="HIDDEN" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"  value="<%= lNotifica.getCurIdCuratore() %>">
        
    </tr>
      <% } // end if is Procure %> 
    <% } // end for %>  
  </table>

<br>

  <table width=90%>
    <tr>
      <td class="Titolo" colspan="3">Uffici Destinatari della Comunicazione - Uffici di Sorveglianza</td>
    </tr>
<%	
  lLastCodUfficio = "";
	for (int i=0;i< lNotifiche.length;i++) {
		NotificaModel lNotifica = lNotifiche[i];
    
    if (   "UDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
        || "UDSM".equals(lNotifica.getUfficio().getCodTipoUfficio())
        || "TDS".equals(lNotifica.getUfficio().getCodTipoUfficio())
        || "TDSM".equals(lNotifica.getUfficio().getCodTipoUfficio())
       )
    {
%>
    <tr> 
      <% if (lNotifica.getUfficio().getCodUfficio().equals(lLastCodUfficio)) { %>
      <td class="L"></td>
      <% } else { %>
      <td class="L">
        <font class="campo"><%=lNotifica.getUfficio().getDescrTipoUfficio()%>&nbsp;</font>
        di &nbsp;
        <font class="campo"> <%=lNotifica.getUfficio().getDescrComune()%>&nbsp; </font>
      </td>  
      <% } %>


      <%  
        lLastCodUfficio = lNotifica.getUfficio().getCodUfficio();

        Iterator ItxP = ProcedimentiCumulati.iterator();
        while(ItxP.hasNext())
        {
          ProcedimentoCumulatoModel ProcMod = (ProcedimentoCumulatoModel) ItxP.next();
          if(ProcMod!=null && ProcMod.getIdProcedimentoCumulato()!=null)  
          {
            if( lNotifica.getCurIdCuratore()!=null && lNotifica.getCurIdCuratore().equals(ProcMod.getTitIdTitoloCumulato()))  { %>              
          <td class="l"> Procedimento Cumulato:   Anno e Numero SIEP </td> 
          <td class="L" >
      	  <font class="campo"><%=StringUtils.toStringJSP(ProcMod.getChiaveAnnoFasCumulato())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(ProcMod.getChiaveProgrFasCumulato())%></font>
          </td>   
      <%    }
          }
        }
      %>          
		<input type="HIDDEN" name="<%=ICostantiJMS.COD_UFFICIO_DESTINATARIO%>"  value="<%= lNotifica.getUfficio().getCodUfficio() %>">
		<input type="HIDDEN" name="idNotifica"  value="<%= lNotifica.getIdNotifica() %>">
		<input type="HIDDEN" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"  value="<%= lNotifica.getCurIdCuratore() %>">
        
    </tr>
      <% } // end if is sorveglianza %> 
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
            <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>"  value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" >
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActUploadComunicazioniProcure">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.modulocumulo.action.ActDettaglioComunicazioniProcure">
          </td>
        </tr>
      </table>
    </FORM>
  </div>

  <br>
</body>
</html>