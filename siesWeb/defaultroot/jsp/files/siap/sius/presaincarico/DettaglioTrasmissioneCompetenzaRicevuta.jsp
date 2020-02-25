<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="Messaggio"            scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="fascicoloSIEP"        scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="fascicoloCumulante"   scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresiduaCumulante" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="NoProcedimento"       scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Deprecated: dal 09/2015:
// Per quanto riguarda la presa in carico atti trasmessi x competenza sul cumulo,
// la jsp è stata spostata sotto \siap\siep\presaincarico e modificata
//  
//==============================================================================
%>




<html>
  <head>
    <title>[S.I.E.S.] - Trasmissione Competenza Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
 </head>
 
<body class="corpo">
  <FORM name="DetTrasmRice"> 
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Trasmissione Competenza Ricevuta</font>
          </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      
       </tr>
      </table>

<%
  if(!NoProcedimento.equals(""))
  { 
%>
      <br><br>
  
      <table cellspacing=0 cellpadding=0 width=95%>
        <tr>
          <td class="L" width=100% colspan=4>
            <font class="cRossoCumulo"><%=NoProcedimento%></font>
          </td>
        </tr>
      </table>
 <% } %>          
 
<%
  SoggettoModel soggettoRicevuto = fascicoloSIEP.getSoggetto();
  SentenzaModel sentenzaRicevuta = fascicoloSIEP.getSentenza();
%>
<br>
  
<%
  SoggettoModel soggetto = new SoggettoModel();
  SentenzaModel sentenza = new SentenzaModel();

  if(fascicoloCumulante != null)
  { 
      SoggettoModel soggetto1 = fascicoloCumulante.getSoggetto();
      SentenzaModel sentenza1 = fascicoloCumulante.getSentenza();
      soggetto = soggetto1;
      sentenza = sentenza1;
      if(fascicoloCumulante.getChiaveAnno() != null)
      { 
%>    
      <table cellspacing=0 cellpadding=0 width=95%>
        <tr>
          <td class="L">

            <font class="label">Procedimento : N.</font>
              <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloCumulante.getIdFascicoloSiep()%>" title="Procedimento">
                <%=fascicoloCumulante.getChiaveAnno()%>
                /
                <%=fascicoloCumulante.getChiaveProgr()%>
              </a>
              &nbsp;
    <%
              if(fascicoloCumulante.getFlagCumulante()!=null && fascicoloCumulante.getFlagCumulante().equals("S"))
              {
    %>
                <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
    <%
              }
    
              if(fascicoloCumulante.getCodOperatoreInserimento() != null && fascicoloCumulante.getCodOperatoreInserimento().startsWith("res-"))
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
    <%
              }
    
              if(   fascicoloCumulante.getCodStatoFascicolo() != null
                 && (fascicoloCumulante.getCodStatoFascicolo().equals("01"))
                 )
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
    <%
              }

            if((    penaresiduaCumulante != null
                 && penaresiduaCumulante.getFlagPenaSospesa()!= null
                 && penaresiduaCumulante.getFlagPenaSospesa().equals("S"))
                 || (     fascicoloCumulante!= null && fascicoloCumulante.getChiaveProgr() != null
                      && (fascicoloCumulante.getChiaveProgr().intValue() >= 30000
                      && fascicoloCumulante.getChiaveProgr().intValue() < 40000)))
            {
              if( fascicoloCumulante!= null && fascicoloCumulante.getChiaveProgr() != null
                && ( fascicoloCumulante.getChiaveProgr().intValue() >= 30000
                &&   fascicoloCumulante.getChiaveProgr().intValue() < 40000) )
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
    <%
              }
              else
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
    <%
              }
            }

            if(   penaresiduaCumulante != null
               && penaresiduaCumulante.getFlagPenaSospesa()!= null
               && penaresiduaCumulante.getFlagPenaSospesa().equals("I"))
            {
    %>
              <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
    <%
            }
            if(   penaresiduaCumulante != null
               && penaresiduaCumulante.getFlagPenaSospesa()!= null
               && penaresiduaCumulante.getFlagPenaSospesa().equals("D"))
            {
    %>
              <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
    <%
            }
    %>
    <%
              if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicoloCumulante.getChiaveUfficio())))
              {
    %>
                &nbsp;
                <font class="label"><%=fascicoloCumulante.getDescrTipoUfficio() + " DI " + fascicoloCumulante.getDescrComuneUfficio() %></font>
    <%
              }
%>
          </td>
        </tr>
    <%    if(soggetto != null)
        { %>    
            <tr>
                <td class="L" width=100%><font class="label">Soggetto : </font>
                <font class="campo">
                  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
                      <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
                  </a>
                </font>&nbsp;
        <%
                if (soggetto.getSesso().compareTo("F")==0)
                {
        %>
                    <font class="label">nata il :</font>&nbsp;
        <%
                }
                else
                {
        %>
                    <font class="label">nato il :</font>&nbsp;
        <%
                }
    
            if(soggetto.getDataNascita() == null)
            {
                  if(soggetto.getDataNascitaPresunta().equals("S")) 
                  {%>
                      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
      <%          }
                  else
                  {%>
                      <font class="campo">***</font>&nbsp;
      <%          }
              }
            else
            {%>
                    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
    <%        }%>
              <font class="label">in : </font>
              <font class="campo">
        
         <%
              if (soggetto.getDescrComuneNascita().compareTo("-")==0)
              {
        %>
                  <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
        <%
              }
              else
              {
        %>
                  <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
        <%
              }
        %>
    
                </font>
            </td>
          </tr>
    <%    }  %>
  
      </table>
    
<%        if(sentenza != null)
        {
%>    
           <table cellspacing=0 cellpadding=0 width=95%> 
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
          <%
                  if (sentenza.getNumSezioneAutoritaEmittente() != null)
                  {
          %>
                    <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
          <%
                  }
          %>
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
            </table>
 <%       } 
      }
  } 
%> 
 
<br/>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan=4>Fascicolo Cumulato</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Procedimento N.</font>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          <%=fascicoloSIEP.getChiaveAnno()%>/<%=fascicoloSIEP.getChiaveProgr()%>
          - <%=fascicoloSIEP.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSIEP.getDescrComuneUfficio()%><BR>
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
      <%}
          else{%>
              <font class="label">nato il :</font>&nbsp;
      <%
          }
      
      if(soggettoRicevuto.getDataNascita() == null){
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
      <%}
          else{%>
            <%=soggettoRicevuto.getDescrComuneNascita()%> (<%=soggettoRicevuto.getCodProvinciaNascita()%>)
      <%
          }%>      
          </font>
      </td>
    </tr>
  </table>
  <br/>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan=4>
        <%=sentenzaRicevuta.getDescrTipoProvvedimento().substring(0,1).toUpperCase()
          +sentenzaRicevuta.getDescrTipoProvvedimento().substring(1).toLowerCase()%>
    </td>
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
<%
        if (sentenzaRicevuta.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenzaRicevuta.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=sentenzaRicevuta.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
        <tr>
      <td class="L" colspan=4>
        <font class="label">Numero Reg. Gen.: </font>
          <font class="campo"><%=sentenzaRicevuta.getNumeroRegistroGenerale()%>&nbsp;</font>
        </td>     
    </tr>    
  </table>
  <br>
  <script language="javascript">
    function invia(aAction){
      disComm();    
      document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value=aAction;   
      document.azioniPresaIncarico.submit();    
    }
    function restAtti(a_formname,a_fieldname, aAction){ 
      disComm();    
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sius.presaincarico.action.ActLoadRestituzioneAtti&formname="+a_formname+"&fieldname="+a_fieldname, "Restituzione_Atti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=300");
      document.azioniPresaIncarico.<%=IWebConstants.ACTION_FIELD%>.value='siap.sius.presaincarico.action.ActRestituzioneCompetenza';  
  }
  function disComm(){
    document.azioniPresaIncarico.P.disabled=true;
      document.azioniPresaIncarico.R.disabled=true;
      document.azioniPresaIncarico.T.disabled=true;
  } 
  </script>
  </FORM>
  
  <form name="azioniPresaIncarico">
    <table cellspacing=2 cellpadding=2 width=95% align="center">
        <tr>
          <td>
            <input class=bottone type=button name="P" value="Presa in Carico" 
            onclick="invia('siap.sius.presaincarico.action.ActLoadConfermaPresaincaricoCompetenza')">
          </td>
          <td>
            <input class=bottone type=button name="R" value="Restituzione Atti" 
            onclick="Javascript:restAtti('azioniPresaIncarico','SedeAltroDestinatario');">
          </td>
          <td>
            <input class=bottone type=button name="T" value="Trasferimento per Competenza (Altro Ufficio)" 
            onclick="invia('siap.siep.richiesta.action.ActLoadRitrasmissioneCompetenza')">
          </td>
        </tr>
    </table>
    <input type="HIDDEN" name="MotivoRestituzione" value="">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="CampoMotivazioni" value="">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_FLAG_VISTO%>" value="">
  </form>
</body>
</html>