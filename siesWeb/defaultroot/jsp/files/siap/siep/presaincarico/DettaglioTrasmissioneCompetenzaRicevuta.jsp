<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.ufficio.controller.IUfficio"%>

<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="Messaggio"            scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="fascicoloSIEP"        scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascicoloCumulante"   scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresiduaCumulante" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="NoProcedimento"       scope="request" class="java.lang.String"/>

<jsp:useBean id="Competenza"           scope="request" class="siap.siep.competenza.model.CompetenzaModel"/>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<%
//=====================================================================================
// JSP per la visualizzazione del dettaglio atti ricevuti per competenza e Seguito Atti
// La jsp visualizza:
// - il dettaglio degli atti Ricevuti (titolo + fascicolo)
// - il dettaglio del titolo che da la competenza 
// - il dettaglio del Fascicolo Cumulante (se indicato e trovato a sistema)
// - I tasti funzione per procedere a: presa in carico/restituzione/inoltro
//======================================================================================
%>

<%
String lTitolo="Dettaglio Trasmissione Competenza Ricevuta";
if(Messaggio!=null && Messaggio.getIdMessaggio()!=null && Messaggio.getCodTipoOperazione()!=null)
{
	if(Messaggio.getCodTipoOperazione().compareTo("00078")==0)
	{
		lTitolo="Dettaglio Trasmissione Seguito Atti Competenza Ricevuti";
	}
	
	if(Messaggio.getCodTipoOperazione().compareTo("00079")==0)
	{
		lTitolo="Dettaglio Trasmissione Comunicazione alle Procure";
	}
}
%>

<html>
  <head>
    <title>[S.I.E.S.] - Trasmissione Competenza Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    
    <script language="javascript">
      function invia(aAction){
        disabilitaButtons();
        document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value=aAction;
        document.azioniPresaIncarico.submit();    
      }
      
      function restAtti(a_formname,a_fieldname, aAction){ 
      	//alert("come faccio a funzionare ??");
        //disabilitaButtons();    
        document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.sius.presaincarico.action.ActRestituzioneCompetenza';  
        
        mypopup = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.presaincarico.action.ActLoadRestituzioneAtti&formname="+a_formname+"&fieldname="+a_fieldname, "Restituzione_Atti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=300");
      }
      
      function associaCumulante(){
        //alert ("associaCumulante");
        if (document.azioniPresaIncarico.<%=ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE%>.value==''){
          alert ("Indicare l'anno del Procedimento a cui associare la trasmissione");
          document.azioniPresaIncarico.<%=ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE%>.focus();
          return;
        }
        
        if (document.azioniPresaIncarico.<%=ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE%>.value==''){
          alert ("Indicare il Numero del Procedimento a cui associare la trasmissione");
          document.azioniPresaIncarico.<%=ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE%>.focus();
          return;
        }
        
        
        document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.presaincarico.action.ActAssociaTrasmissioneAProcedimento';
        document.azioniPresaIncarico.submit();    
      }
      
      function disabilitaButtons(){
        document.azioniPresaIncarico.P.disabled=true;
        document.azioniPresaIncarico.R.disabled=true;
        //document.azioniPresaIncarico.T.disabled=true;
      } 
      
      function abilitaButtons(){
        document.azioniPresaIncarico.P.disabled=false;
        document.azioniPresaIncarico.R.disabled=false;
        //document.azioniPresaIncarico.T.disabled=false;
      }       
    </script>
    
 </head>
 
<body class="corpo">


<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;
      <font class="campo"><%=lTitolo%></font>
    </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>    
  </tr>
</table>

<br>

<% if(IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <br>
<% } %>

  
<%
//==============================================================================
//                     Dati del Fascicolo Ricevuto
//==============================================================================

SoggettoModel soggettoRicevuto = fascicoloSIEP.getSoggetto();
SentenzaModel sentenzaRicevuta = fascicoloSIEP.getSentenza();
%>

<form name="azioniPresaIncarico">

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan=4> Titolo Ricevuto per Competenza</td>
    </tr>
    <tr>  
      <td class="L" colspan=4>
        <font class="label"><%=sentenzaRicevuta.getDescrTipoProvvedimento().substring(0,1).toUpperCase()
          +sentenzaRicevuta.getDescrTipoProvvedimento().substring(1).toLowerCase()%>
        </font>
        <font class="label"> N.</font>
        <font class="campo">
          <%=sentenzaRicevuta.getAnnoSentenza()%>/<%=sentenzaRicevuta.getNumeroSentenza()%>
        </font>
        <font class="label">del</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(sentenzaRicevuta.getDataProvvedimento(), "dd-MM-yyyy")%>
        </font>
        &nbsp;<font class="label"> Emessa da: </font>
        <font class="campo"><%=sentenzaRicevuta.getDescrTipoAutoritaEmittente()%></font>&nbsp;
        <% if (sentenzaRicevuta.getNumSezioneAutoritaEmittente() != null){%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenzaRicevuta.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
        <% } %>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getDescrLuogoEmittente())%></font>
      </td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Numero Reg. Gen.: </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenzaRicevuta.getStringRegGen())%>&nbsp;</font>
      </td>
    </tr>
  </table>


<%
//==============================================================================
//
//==============================================================================
String lStrProcedimento = "";
if (fascicoloSIEP.getChiaveProgrOrig()!=null) {

  lStrProcedimento = fascicoloSIEP.getChiaveAnno()+"/"+fascicoloSIEP.getChiaveProgrOrig();


  BigDecimal lIncrement = null;
  lIncrement = fascicoloSIEP.getChiaveProgr().subtract (fascicoloSIEP.getChiaveProgrOrig());
  
  UfficioModel lUfficioAccorpato = new UfficioModel();
  
  try {
    IUfficio lUff = SICOLookupRemote.getUfficioRemote();
    lUfficioAccorpato = lUff.getUfficioAccorpatoByAccorpanteIncrement (fascicoloSIEP.getChiaveUfficio(), ""+lIncrement);
  } catch (Exception e) {}          
  
  lStrProcedimento += " <font class='cRosso'> (ex ";
  lStrProcedimento += " "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrComune();
  lStrProcedimento += ") </font>";
}
else {
  lStrProcedimento = fascicoloSIEP.getChiaveAnno()+"/"+fascicoloSIEP.getChiaveProgr();
}


%> 
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td colspan=4>&nbsp;</td>
      <!--td class="Titolo" colspan=4>Iscritto al Procedimento</td-->
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Iscritto al Procedimento N.</font>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          <%=lStrProcedimento%>
          - <%=fascicoloSIEP.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSIEP.getDescrComuneUfficio()%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Data Irrevocabilità:</font>
        <font class="campo"><%=DateUtils.getDateToString(fascicoloSIEP.getDataIrrevocabilita(), "dd-MM-yyyy") %></font>
      </td>
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Soggetto : </font>
        <font class="campo"><%=soggettoRicevuto.getCognome()%>&nbsp;<%=soggettoRicevuto.getNome()%></font>
        <%if (soggettoRicevuto.getSesso().compareTo("F")==0){%>
                <font class="label">nata il :</font>&nbsp;
        <%} else {%>
                <font class="label">nato il :</font>&nbsp;
        <%} %>
      
        <% 
        if(soggettoRicevuto.getDataNascita() == null)
        {
          if(soggettoRicevuto.getDataNascitaPresunta().equals("S")) {%>
              <font class="campo"><%=StringUtils.toStringJSP(soggettoRicevuto.getAnnoNascita())%></font>&nbsp;
        <%}
          else{%>
              <font class="campo">***</font>&nbsp;
        <%}
        }else{%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoRicevuto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
        <%}%>
      
        <font class="label">in : </font>
        <font class="campo"> 
        <%if (soggettoRicevuto.getDescrComuneNascita().compareTo("-")==0){%>
              <%=soggettoRicevuto.getDescComuneNascitaEstero()%>  (<%=soggettoRicevuto.getDescrStatoNascita().toUpperCase()%>)
        <%}else{%>
              <%=soggettoRicevuto.getDescrComuneNascita()%> (<%=soggettoRicevuto.getCodProvinciaNascita()%>)
        <% }%>
        </font>
        <font class="label">&nbsp;Codice CUI: </font>
        <font class="campo"><%=StringUtils.toStringJSP(soggettoRicevuto.getCodAfis(),"n.d.")%></font>
      </td>
    </tr>
  </table>
  
  <br>


  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="Titolo" colspan=4>Titolo che determina la competenza</td>
    </tr>
    <%
    int lTipoVisualizzazioneCumulante = 0;
    
    if (!NoProcedimento.equals("")){
        // Fascicolo indicato ma non trovato a sistema
        lTipoVisualizzazioneCumulante = 4;
    }
    else if (Competenza.getFasSieIdFascicoloSiep()!=null) {
      // Chi ha trasmesso ha selezionato il Cumulante, per cui i dati di
      // Sentenza e Fascicolo sono sicuramente corretti
      // Visualizzo i dati da fascicoloCumulante
      lTipoVisualizzazioneCumulante = 1;
    }
    else {
      // I dati del cumulante sono stati inseriti manualmente eventualmente l'utente
      // potrebbe non aver indicato i dati del fascicolo (anno e numero) 
      // o addirittura sbagliato ad indicarli
      
      if (fascicoloCumulante.getIdFascicoloSiep()!= null) {
        // Fascicolo indicato e trovato a sistema, visualizzo i dati da fascicoloCumulante
        // ma i dati del titolo potrebbero non essere corretti
        //
        lTipoVisualizzazioneCumulante = 2;
      }
      else if (Messaggio.getChiaveAnnoFasCumulante()==null && Messaggio.getChiaveProgrFasCumulante()==null){
        // Non sono stati indicati i dati del fascicolo cumulante ma solo del titolo
        lTipoVisualizzazioneCumulante = 3;
      }
    }    

    %>
    <!--tr>
      <td class="Titolo" colspan=4>Tipo visualizzazione <%=lTipoVisualizzazioneCumulante%></td>
    </tr-->
    
    <% if (lTipoVisualizzazioneCumulante==3 || lTipoVisualizzazioneCumulante==4) { %>
      <tr>
        <td class="l">Tipo Provvedimento</td>
        <td class="L"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrTipoProvvedimento(),"&nbsp;")%>&nbsp;</font></td>
        <td class="l">Anno/Numero</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getAnnoSentenza())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(Competenza.getNumeroSentenza())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l" width="25%">Data Provvedimento</td>
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Competenza.getDataProvvedimento(),"dd-MM-yyyy"),"&nbsp;")%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l" width="25%">Pronunciata da</td>   
        <td class="L" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrTipoAutoritaEmittente(),"&nbsp;")%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l">Luogo</td>
        <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getDescrLuogoEmittente(),"&nbsp;")%>&nbsp;</font></td>
        <td class="l">Sezione</td>
        <td class="L" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(Competenza.getNumSezioneAutoritaEmittente(),"&nbsp;")%>&nbsp;</font></td>
      </tr>
      <% if (lTipoVisualizzazioneCumulante==3) { %>
      <tr>
        <td class="L"><font class="label">Iscritto al Procedimento N.: </font> </td>
        <td class="L" colspan="3">
          <font class="cRosso">non indicato</font>&nbsp;
          Anno/Numero SIEP&nbsp;
          <input type="text" title="Anno" value="" name="<%=ICostantiJMS.CHIAVE_ANNO_FAS_CUMULANTE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" title="Numero SIEP" name="<%=ICostantiJMS.CHIAVE_PROGR_FAS_CUMULANTE%>" maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
          <input type="button" class="bottone" name="associa" value="Associa Procedimento" 
                 onclick="Javascript:associaCumulante('formAssocia');">
        </td>
      </tr>
      <% } else if (lTipoVisualizzazioneCumulante==4) { %>
      <tr>
        <td class="L" width=100% colspan=4>
          <font class="cRossoCumulo"><%=NoProcedimento%></font>
        </td>
      </tr>
      <% } %> 
    <% 
    } 
    else if (lTipoVisualizzazioneCumulante==1 || lTipoVisualizzazioneCumulante==2) 
    {
      //============================================================================
      // Dati del cumulante se indicato in fase di trasmissione e trovato a sistema
      //============================================================================
      SoggettoModel soggetto = fascicoloCumulante.getSoggetto();
      SentenzaModel sentenza = fascicoloCumulante.getSentenza();
    %>
        <tr>
          <td class="L">
            <font class="label">Procedimento : N.</font>
              <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloCumulante.getIdFascicoloSiep()%>" title="Procedimento">
                <%=fascicoloCumulante.getChiaveAnno()%>
                /
                <%=fascicoloCumulante.getChiaveProgr()%>
              </a>
              &nbsp;
              <%if("S".equals(fascicoloCumulante.getFlagCumulante())){%>
                <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
              <%}
    
              if(fascicoloCumulante.getCodOperatoreInserimento() != null && fascicoloCumulante.getCodOperatoreInserimento().startsWith("res-")){%>
                <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
              <% }
    
              if( "01".equals(fascicoloCumulante.getCodStatoFascicolo()) ){  %>
                <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
              <% }

              if(  (    penaresiduaCumulante != null
                     && "S".equals( penaresiduaCumulante.getFlagPenaSospesa())
                    )
                 || fascicoloCumulante.getClasseProcedimento()==3 
                )
              {
                if( fascicoloCumulante.getClasseProcedimento()==3 )
                {
                 %>
                  <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
                 <%}else{%>
                  <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
                  <%
                }
              }%>


              <% if( penaresiduaCumulante != null ) { %>          
                <% if ( "I".equals(penaresiduaCumulante.getFlagPenaSospesa())) { %>
                <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
                <% } else if ( "D".equals(penaresiduaCumulante.getFlagPenaSospesa())) { %>
                <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
                <% } %>
              <% } %>
    
            <%if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicoloCumulante.getChiaveUfficio()))){%>
              &nbsp;<font class="label"><%=fascicoloCumulante.getDescrTipoUfficio() + " DI " + fascicoloCumulante.getDescrComuneUfficio() %></font>
            <%}%>
          </td>
        </tr>
        
        
      <% // Dati del Soggetto Cumulante %>
      <% if (soggetto!=null) { %>
      <tr>
        <td class="L" width=100%>
          <font class="label">Soggetto : </font>
          <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
                <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
            </a>
          </font>&nbsp;
          <%if (soggetto.getSesso().compareTo("F")==0){%>
              <font class="label">nata il :</font>&nbsp;
          <%}else{%>
              <font class="label">nato il :</font>&nbsp;
          <%}%>
    
          <% if(soggetto.getDataNascita() != null) { %>
                 <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
          <% } else if("S".equals(soggetto.getDataNascitaPresunta())) {%> 
                 <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
          <% } else {%>
                 <font class="campo">***</font>&nbsp;
          <% } %>
            
          <font class="label">in : </font>
          <font class="campo">
          <%if (soggetto.getDescrComuneNascita().compareTo("-")==0){%>
             <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
          <%}else{%>
             <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
          <%}%>
          </font>
          <font class="label">&nbsp;Codice CUI: </font>
          <font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis(),"n.d.")%></font>
        </td>
      </tr>
      <% } %>
 
    
      <% // Dati della Sentenza Cumulante %>
      <% if (sentenza!=null) { %>
      <tr>
        <td class="L">
          <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
          <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
            <font class="label">del</font>&nbsp;  
              <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>  
          </font>
          &nbsp;<font class="label"> Emessa da: </font>
          <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
          <% if (sentenza.getNumSezioneAutoritaEmittente() != null){%>
            <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
          <% } %>
          <font class="label"> di </font>
          <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
        </td>
      </tr>
      <tr>
        <td class="L">
          <font class="label">Data irrevocabilità : </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicoloCumulante.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
        </td>
      </tr>
      <% } %>
    <%
      } // End Tipo visualizzazione
    %> 
    </table>

<%
//==============================================================================
//
//==============================================================================
%>  
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">

    <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=StringUtils.toStringJSP(IstruttoriaCumulo.getIdIstruttoriaCumulo())%>">

    <% // Valorizzato dalla pupop up in caso di restituzione %>
    <input type="HIDDEN" name="MotivoRestituzione" value="">

    <%
    String lDisablePresaInCarico = ""; 
    if (lTipoVisualizzazioneCumulante==3 || lTipoVisualizzazioneCumulante==4) { 
      lDisablePresaInCarico = "disabled";
    }
    
    String lDisabledRestiuzione = "";
    if("00079".equals(Messaggio.getCodTipoOperazione()) ) {
   	 lDisablePresaInCarico = "disabled";
   	 lDisabledRestiuzione = "disabled";
    }
    %>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input class=bottone type=button name="P" value="Presa in Carico"  <%=lDisablePresaInCarico%>
                 onclick="invia('siap.siep.presaincarico.action.ActLoadConfermaPresaincaricoCompetenza')">
        </td>
        <td>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input class=bottone type=button name="R" value="Restituzione Atti" <%=lDisabledRestiuzione%>
                 onclick="Javascript:restAtti('azioniPresaIncarico','SedeAltroDestinatario');">
        </td>
        <%--
        <td>
          <input class=bottone type=button name="T" value="Trasferimento per Competenza (Altro Ufficio)" 
                 onclick="invia('siap.siep.richiesta.action.ActLoadRitrasmissioneCompetenza')">
        </td>
        --%>
 <%	if("00079".equals(Messaggio.getCodTipoOperazione()) ) {   %>
		<td>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input class=bottone type=button name="E" value="Annota Esito" 
                 onclick="invia('siap.siep.presaincarico.action.ActLoadInsAnnotaEsitoTrasmComp')">
        </td>

<%	} %>      
      </tr>
    </table>
  </form>
</body>
</html>