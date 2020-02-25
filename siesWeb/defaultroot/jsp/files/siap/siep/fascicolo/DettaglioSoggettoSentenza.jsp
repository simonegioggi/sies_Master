<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.SIEPException"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoFascicoloSiepModel" %>


<%@ page import="siap.siep.util.SIEPLookupRemote" %>
<%@ page import="siap.siep.avvocato.controller.IAvvocato" %>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresidua" scope="session" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%
  SoggettoModel soggetto = fascicolo.getSoggetto();
  SentenzaModel sentenza = fascicolo.getSentenza();
  
  // 07/2015 MEV29
  // Aggiunto controllo per segnalare Avvocati appartenenti a fori soppressi
  // n.b. sarebbe più corretto passare il dato alla jsp come attributo del fascicolo in sessione,
  //      senza dover accede dalla jsp direttamente ai controller, ma il "fascicolo" viene messo 
  //      in sessione in 94 classi differenti
  boolean isAvvocatoForoSoppresso = false;
  String strAlertAvvocato = "";
  if (   !"01".equals(fascicolo.getCodStatoFascicolo())
      && ( UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio()))
     )
  {
    try {
      Vector <AvvocatoModel> listaAvvocati = null;
      IAvvocato lCtrlAvvocato = SIEPLookupRemote.getAvvocatoRemote();
      
      AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
      lAvvFascMod.setFasSieIdFascicoloSiep(fascicolo.getIdFascicoloSiep());
      
      try{
     	 listaAvvocati = lCtrlAvvocato.ExRicercaAvvocatiAttualiFascicolo(null,lAvvFascMod);
      
      }catch(Exception e){
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.error("Exception",e);
      }
      
      Collection listaFori = DecodificheManager.getInstance().getForoAll();  
      
      int contaSoppressi = 0;
      if (listaAvvocati!=null){
        for (int i=0; i<listaAvvocati.size();i++) {
         
          AvvocatoModel lAvvocatoModel = listaAvvocati.elementAt(i);
          
          String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocatoModel.getForo());
    
          if ("SOPPRESSO".equals(lStatoForo)){
            isAvvocatoForoSoppresso = true;
            contaSoppressi++;
            
            strAlertAvvocato+= " L’Avvocato "+StringUtils.toStringJSP(lAvvocatoModel.getCognome())+" "
                               +StringUtils.toStringJSP(lAvvocatoModel.getNome())
                               +" risulta iscritto al Foro di "
                               +StringUtils.toStringJSP(lAvvocatoModel.getForo())
                               +" soppresso a seguito dell’accorpamento degli uffici giudiziari. ";
          }
        }
      }
    
      if (isAvvocatoForoSoppresso){
        if (contaSoppressi==1)
          strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
        else
          strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
      }
      
    }catch(Exception e){
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("Exception",e);
    }
  }

%>

<% if (isAvvocatoForoSoppresso) {  %>
<script type="text/javascript">
  function blink() {
    var blinks = document.getElementsByTagName('blink');
    
    for (var i = blinks.length - 1; i >= 0; i--) {
      var s = blinks[i];
      s.style.visibility = (s.style.visibility === 'visible') ? 'hidden' : 'visible';
    }
    window.setTimeout(blink, 500);  
  }
  if (document.addEventListener) document.addEventListener("DOMContentLoaded", blink, false);
  else if (window.addEventListener) window.addEventListener("load", blink, false);
  else if (window.attachEvent) window.attachEvent("onload", blink);
  else window.onload = blink;
</script>
<% } %>

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
<%
          if(fascicolo.getFlagCumulante()!=null && fascicolo.getFlagCumulante().equals("S"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
<%
          }

          if(fascicolo.getCodOperatoreInserimento() != null && fascicolo.getCodOperatoreInserimento().startsWith("res-"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
<%
          }

          if(   fascicolo.getCodStatoFascicolo() != null
             && (fascicolo.getCodStatoFascicolo().equals("01"))
             )
          {
%>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
<%
          }

        if((    penaresidua != null
             && penaresidua.getFlagPenaSospesa()!= null
             && penaresidua.getFlagPenaSospesa().equals("S"))
             || (     fascicolo!= null && fascicolo.getChiaveProgr() != null
                  && (fascicolo.getChiaveProgr().intValue() >= 30000
                  && fascicolo.getChiaveProgr().intValue() < 40000)))
        {
          if( fascicolo!= null && fascicolo.getChiaveProgr() != null
            && ( fascicolo.getChiaveProgr().intValue() >= 30000
            &&   fascicolo.getChiaveProgr().intValue() < 40000) )
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
<%
        }
%>
<%
          if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getChiaveUfficio())))
          {
%>
            &nbsp;
            <font class="label"><%=fascicolo.getDescrTipoUfficio() + " DI " + fascicolo.getDescrComuneUfficio() %></font>
<%
          }
%>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {
    	if (soggetto.getSesso().compareTo("F")==0)
    	{
%>
      		<font class="label">nata il :</font>&nbsp;
<%
    	} else {
%>
      		<font class="label">nato il :</font>&nbsp;
<%
    	}
    	
    	if(soggetto.getAnnoNascita() != null){
%>
        	<font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%
    	} else {
%>
			<font class="campo">**-**-****</font>&nbsp;
<%    	
   		}	
    	
   }else if (soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi()!=null ){ %>
      	<font class="label">Età Presunta: </font>
<%
		if (soggetto.getEtaPresuntaAnni() != null){
%>
			anni <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font>
<%
		}
		if (soggetto.getEtaPresuntaMesi() != null){
%>
			mesi <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font>
<%			
		}
%>

<% }else {%>
      <font class="campo">**-**-****</font>&nbsp;
<% }

}else {
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
%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%
} // chiude else presenza data nascita
%>

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
    <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=StringUtils.toStringJSP(sentenza.getAnnoSentenza())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroSentenza())%> </a>&nbsp;  
          <font class="label">del</font>&nbsp;

            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>

        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
    }else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
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
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr--%>
    
    <%
    //==========================================================================
    // Test per segnalare che uno o più avvocati associati al Procedimento risultano
    // iscritti a Fori Soppressi.
    //==========================================================================
    %>
    
    <% if (isAvvocatoForoSoppresso) { %>
    <tr>
      <td class="cRosso">
        <blink>Attenzione!!</blink> <%=strAlertAvvocato%>
      </td>
    </tr>
    <% } %>
    
  </table>