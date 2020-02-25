<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.jms.ICostantiJMS"%>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo"      scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="penaresidua"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<jsp:useBean id="Messaggio"               scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="MessaggioRichiesta"      scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="MessaggioInoltrante"     scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="listaSolleciti"  scope="request" class="java.util.Vector"/>

<jsp:useBean id="TornaQui"               scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Atti Trasmessi Per Competenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script>
      function invia(aAction){
        document.DettaglioAttoTrasmessoMS.AvantiButton.disabled=true; 
        if (aAction == 'Sollecito')
          document.DettaglioAttoTrasmessoMS.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActLoadInserisciSollecitoEsitoTrasmissione';   
        else
          document.DettaglioAttoTrasmessoMS.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.misurasicurezza.action.ActLoadInserisciAnnotaEsitoTrasmissione';   
        
        document.DettaglioAttoTrasmessoMS.submit();    
      }
    </script>
  </head>
  
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Atti Trasmessi Per Competenza</font>
          </td>
          <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>
      </table>
    </FORM>
    
    
    <!-- jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/ -->
    
<%
//==============================================================================
//
//==============================================================================
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
//
//==============================================================================
%>   
    
    <br>
    
<%
// n.b. 
// - dataTrasmissione = sempre la data di invio del messaggio di Richiesta
// - dataRisposta = se si visualizza il messaggio di Risposta a data è la data di scarico del messaggio ovvero dataInvio() del messaggio di risposta
String dataTrasmissione = DateUtils.getDateToString(MessaggioRichiesta.getDataInvio(),"dd-MM-yyyy HH:mm");
String dataRisposta = "&nbsp;";
String uffDestinatario = "";
if (ICostantiJMS.RICHIESTA.equals(Messaggio.getCodTipoMessaggio())) {
  dataRisposta = StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataEsito(),"dd-MM-yyyy HH:mm"),"&nbsp;");
  uffDestinatario = StringUtils.toStringJSP(Messaggio.getDescrUfficioDestinatario()+" di "+Messaggio.getDescrSedeUfficioDestinatario());
}
else {
  // Esito 
  dataRisposta = StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy HH:mm"),"&nbsp;");
  uffDestinatario = StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente()+" di "+Messaggio.getDescrSedeUfficioMittente());
}


%>    
    
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Titolo" colspan=4>Oggetto della Trasmissione</td>
      </tr>
      <tr>
        <td class="l"><font class="label">Oggetto</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(MessaggioRichiesta.getDescrTipoOperazione())%>&nbsp;</font></td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      
      
      
      <tr>
        <td class="Titolo" colspan=4>Dati Messaggio</td></tr>
      <tr>
        <td class="l"><font class="label">Stato Messaggio</font></td>
        <% if ("-".equals(Messaggio.getCodEsito())) { %>
        <td class="lRosso">In Attesa di risposta...&nbsp;</td>
        <% } else { %>
        <td class="lVerde">Ricevuto</td>
        <% } %>
      </tr>
      <tr>
        <td class="l"><font class="label">Data Trasmissione</font></td>
        <td class="l"><font class="campo"><%=dataTrasmissione%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Data Risposta</font></td>
        <td class="l"><font class="campo"><%=dataRisposta%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Destinatario</font></td>
        <td class="l">
          <font class="campo"><%=uffDestinatario%>&nbsp;</font>
          <% if (MessaggioInoltrante!=null && MessaggioInoltrante.getIdMessaggio()!=null) { %>
          <br>
          <font color="grey">Atti inoltrati da:
          <%= MessaggioInoltrante.getDescrUfficioMittente() +" "+ MessaggioInoltrante.getDescrSedeUfficioMittente()%>
          </font>
          <% } %>
        </td>
      </tr>

      <tr>
        <td class="l"><font class="label">Esito Trasmissione</font></td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrEsito(),"&nbsp;")%>&nbsp;
          <% if (ICostantiJMS.ISCRITTO_CLASSE_IV.equals(Messaggio.getCodEsito())) { %>
          <%=" ("+Messaggio.getChiaveAnnoSiep() + "/" + Messaggio.getChiaveProgrSiep()+")"%>
          <% } %>
          </font>
          <% if (ICostantiJMS.TRASFERITO.equals(Messaggio.getCodEsito())) { %>
          <font class="label"> a </font><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioInoltro())+" di "+StringUtils.toStringJSP(Messaggio.getDescrSedeUfficioInoltro())%></font>
          <% } %>
        </td>
      </tr>

      <tr>
        <td class="l"><font class="label">Motivazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getNote())%>&nbsp;</font></td>
      </tr>
      
      <% 
      if (listaSolleciti!=null && listaSolleciti.size()>0 ){
        for (int i = 0;i<listaSolleciti.size();i++){
          MessaggioModel lMessSoll = (MessaggioModel) listaSolleciti.elementAt(i);
          %>
          <tr>
            <td class="l"><font class="label">Sollecito</font></td>
            <td class="l">
              <font color="red">Inviato Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
            </td>
          </tr>
          <%
        }
      }
      %>
    </table>
    
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioAttoTrasmessoMS">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
    
      <table cellspacing=2 cellpadding=2 align="left">
        <tr>
          <% if ("-".equals(Messaggio.getCodEsito())) { %>
          <td>
            <input class="bottone" type="button" name="AvantiButton" value="Sollecito" 
                   onclick="invia('Sollecito')">
          </td>
          <% } else { %>
          <td>
            <input class="bottone" type="button" name="AvantiButton" value="Annotazione Esito" 
                   onclick="invia('Annotazione')">
          </td>
          <% } %>
        </tr>
      </table> 
    
    
    </FORM>

  </body>
</html>