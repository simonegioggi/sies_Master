<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="MessaggioEsito"     scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="MessaggioTrasm"     scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="fascicoloInviato"   scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresiduaInviata" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="solleciti"          scope="request" class="java.util.Vector"/>

<%
//==============================================================================
//	DettaglioRiscontroTrasmissioneCompetenza.jsp
//==============================================================================
%>
<html>
  <head>
    <title>[S.I.E.S.] - Trasmissione Competenza Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>   


    <script language="JavaScript">
    
      // stampaSiep() esegue il provvedimento e la stampa del SOLLECITO (Cod Motivo = 5402)
      function stampaSiep(){
        var  hrefStampa = "/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaSollecitoTrasmCompetenza&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloInviato.getIdFascicoloSiep()%>";
        hrefStampa += "&<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>=<%=MessaggioEsito.getIdMessaggio()%>";
        var lAzione = hrefStampa;
        var lIndice = hrefStampa.indexOf("?");
  
        var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
        stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
      }
     
      function eseguiFunzione (tipoFunzione, stessaBdi){
        if (tipoFunzione=='Annotazione'){
          document.formFunzioni.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.presaincarico.action.ActLoadInsAnnotaEsitoTrasmComp';
          document.formFunzioni.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = <%=MessaggioEsito.getIdMessaggio()%>;
          document.formFunzioni.AnnotazioneButton.disabled=true;
          document.formFunzioni.submit();
        }
        else if (tipoFunzione=='Seguito'){
      		//document.formFunzioni.< %=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciTrasmissioneCompetenza";
      		document.formFunzioni.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciTrasmissioneSeguitoAttidaRiscontro";
      		if(stessaBdi=='SI')	
      		{	
      			document.formFunzioni.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = <%=MessaggioTrasm.getIdMessaggio()%>;
      		}
      		else
      		{
      			//document.formFunzioni.< %=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = < %=MessaggioTrasm.getIdRichiesta()%>;
      			document.formFunzioni.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>.value = <%=MessaggioTrasm.getIdMessaggio()%>;
      		}	
      		document.formFunzioni.submit();
        }
        else if (tipoFunzione=='Sollecito'){
          alert ("Funzione in fase di implementazione");
        }
      }
      
    
  </script>
    
    
    
  </head>
  
  <body class="corpo">
    <!-- FORM name="comandi" -->
      <table>
        <tr>
          <td class="LBG"><a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Esito Trasmissione Competenza</font>
          </td>
        <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        
        <% if( (MessaggioEsito.getCodEsito()+"").compareTo(ICostantiJMS.PRESAINCARICO)==0){ %>
      <!-- BOTTONE DI STAMPA INSERITO A MANO PERCHè IL FASCICOLO NON è IN SESSIONE!!!  SOLO SE LO STATO E' "PRESO IN CARICO" -->
      <%--  
	      <td class="LBG">
        <a href="Javascript:stampaSiep()"  onclick="javascript:lookUpload();">
          <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
	        </a>
	      </td>
   	  --%>
       <%} %>
      
         </tr>
      </table>
    <!--  /FORM  -->
    <BR>
    
    
<%
// SEZIONE CON I DATI DEL FASCIOLO TRASMESSO:
// - Procedimento
// - Soggetto
// - Sentenza
%> 
    <%
    SoggettoModel soggetto = fascicoloInviato.getSoggetto();
    SentenzaModel sentenza = fascicoloInviato.getSentenza();
    %>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloInviato.getIdFascicoloSiep()%>" title="Procedimento">
            <%=fascicoloInviato.getChiaveAnno()%>
            /
            <%=fascicoloInviato.getChiaveProgr()%>
          </a>
          &nbsp;
<%
          if(fascicoloInviato.getFlagCumulante()!=null && fascicoloInviato.getFlagCumulante().equals("S"))
          {
%>
            <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
<%
          }

          if(fascicoloInviato.getCodOperatoreInserimento() != null 
              && fascicoloInviato.getCodOperatoreInserimento().startsWith("res-")){
%>
            <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
<%
          }

          if(   fascicoloInviato.getCodStatoFascicolo() != null
             && (fascicoloInviato.getCodStatoFascicolo().equals("01"))
             ){
%>
            <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
<%
          }

        if((    penaresiduaInviata != null
             && penaresiduaInviata.getFlagPenaSospesa()!= null
             && penaresiduaInviata.getFlagPenaSospesa().equals("S"))
             || (     fascicoloInviato!= null && fascicoloInviato.getChiaveProgr() != null
                  && (fascicoloInviato.getChiaveProgr().intValue() >= 30000
                  && fascicoloInviato.getChiaveProgr().intValue() < 40000)))
        {
          if( fascicoloInviato!= null && fascicoloInviato.getChiaveProgr() != null
            && ( fascicoloInviato.getChiaveProgr().intValue() >= 30000
            &&   fascicoloInviato.getChiaveProgr().intValue() < 40000) )
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

        if(   penaresiduaInviata != null
           && penaresiduaInviata.getFlagPenaSospesa()!= null
           && penaresiduaInviata.getFlagPenaSospesa().equals("I"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
<%
        }
        if(   penaresiduaInviata != null
           && penaresiduaInviata.getFlagPenaSospesa()!= null
           && penaresiduaInviata.getFlagPenaSospesa().equals("D"))
        {
%>
          <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
<%
        }
%>
<%
          if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicoloInviato.getChiaveUfficio()))){
%>
            &nbsp;
            <font class="label"><%=fascicoloInviato.getDescrTipoUfficio() + " DI " + fascicoloInviato.getDescrComuneUfficio() %></font>
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

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
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
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
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
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicoloInviato.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr>
  </table>
<%
//==============================================================================
//  FINE SEZIONE CON I DATI DEL FASCIOLO TRASMESSO
//==============================================================================
%>  
  <BR>
  <BR>
  
  <table cellspacing=2 cellpadding=2 width="95%">    
    <tr>
      <td class="L" colspan=4>
        <font class="label">Data Trasmissione:</font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(MessaggioTrasm.getDataInvio(), "dd-MM-yyyy HH:mm") )%></font>
      </td>      
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Destinatario:</font>
          <font class="campo"><%=MessaggioEsito.getDescrUfficioMittente()%> &nbsp;<%=MessaggioEsito.getDescrSedeUfficioMittente() %></font>         
      </td>     
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Data Risposta:</font>
          <font class="campo"><%=DateUtils.getDateToString(MessaggioEsito.getDataInvio(), "dd-MM-yyyy HH:mm") %></font>
      </td>     
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Esito Trasmissione:</font>
          <font class="campo"><%=MessaggioEsito.getDescrEsito()%></font>
      </td>     
    </tr>
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Fascicolo Cumulante:</font>
          <font class="campo">
          <%=StringUtils.toStringJSP(MessaggioEsito.getChiaveAnnoFasCumulante(),"&nbsp;")%>
          /<%=StringUtils.toStringJSP(MessaggioEsito.getChiaveProgrFasCumulante(),"&nbsp;")%>
          </font>
      </td>     
    </tr>
    <%if(MessaggioEsito.getNote()!=null){ %>
      <tr>
        <td class="L" width=100% colspan=4>
          <font class="label">Motivazioni:</font>
          <font class="campo"><%=MessaggioEsito.getNote()%></font>
        </td>     
      </tr>
    <%}%>    
    </table>

  <br>


<%
//==============================================================================
// Tasti seguito atti e Annotazione esito solo se il Messaggio di trasmissione
// era una Trasmissione per Competenza
//==============================================================================
%>  
<% if (   ICostantiJMS.TRASFERIMENTO_COMPETENZA.equals (MessaggioTrasm.getCodTipoOperazione()) 
       || ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI.equals (MessaggioEsito.getCodTipoOperazione()) 
       // MEV_2025-48 - 2.15 Gestione Annotazioni Trasmissioni 
       // Si devono gestire anche gli esiti del seguito per mettere il flag a S
       || ICostantiJMS.ESITO_SEGUITO_ATTI.equals (MessaggioEsito.getCodTipoOperazione()) 
	  ) 
{ %> 
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formFunzioni"> 
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0740">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_COD_UFFICIO_DESTINATARIO%>" value="<%=MessaggioTrasm.getChiaveUfficioFasCumulante()%>">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_CHIAVE_ANNO_CUMULANTE%>" value="<%=MessaggioTrasm.getChiaveAnnoFasCumulante()%>">
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_CHIAVE_PROGR_CUMULANTE%>" value="<%=MessaggioTrasm.getChiaveProgrFasCumulante()%>">

  <table cellspacing=2 cellpadding=2 >    
    <tr>
      <td>
        <input class="bottone" type="button" name="AnnotazioneButton" value="Annotazione Esito" 
               onclick="eseguiFunzione('Annotazione','')">
               
<% 		if ( ICostantiJMS.TRASFERIMENTO_COMPETENZA.equals (MessaggioTrasm.getCodTipoOperazione()) ) { %>

    	<%	if(MessaggioTrasm.getCodBdiMittente().equals(MessaggioTrasm.getCodBdiDestinataria()) ) 	{	%>    
               	<input class="bottone" type="button" name="SeguitoButton" value="Seguito Atti"
               		onclick="eseguiFunzione('Seguito','SI')">
        <%	} else { %>
        		<input class="bottone" type="button" name="SeguitoButton" value="Seguito Atti"
               		onclick="eseguiFunzione('Seguito','')">
        <%	} %>       		
               		
<%		} %>


<% if( (MessaggioEsito.getCodEsito()+"").compareTo(ICostantiJMS.PRESAINCARICO)==0){ %>
        <input class="bottone" type="button" name="SollecitoButton" value="Esegui Sollecito" 
               title="Stampa del Sollecito" onclick="stampaSiep();lookUpload();">

 <% } %>


      </td>
      <td> &nbsp; </td>
      
    </tr>
  </table>  
</form>  

<% 	}	%>
 
<%
//==============================================================================
//  Visualizzazione dei solleciti
//==============================================================================
%>
<br>
<%
if(  (MessaggioEsito.getCodEsito()+"").compareTo(ICostantiJMS.PRESAINCARICO)==0
   && solleciti!=null && solleciti.size()!=0)
{
%>
    <BR>
    
      <table cellspacing=2 cellpadding=2 width="65%">
        <tr>
          <td class="int">Data Emissione</td>
          <td class="int">Tipo Documento</td>
        </tr>
        <%
          //Visualizzazione dei bottoni
          Iterator itxSoll = solleciti.iterator();
          MessaggioModel lMsg = null;
          while(itxSoll.hasNext())
          {
            lMsg = (MessaggioModel)itxSoll.next();
            %>
          <tr>
            <td class="L" colspan=><font class="label"><%=DateUtils.getDateToString(lMsg.getDataInvio(), "dd-MM-yyyy")%></font></td>
            <td class="L" colspan=><font class="label"><%=lMsg.getDescrTipoOperazione()%></font></td>
          </tr>
          <%
          }  
          %>
    </table>
<%
}
%>
   
      

 
  

  
  <br>
  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>"/>
        <tr>
          <td class="L">
            <input class="bottone"  type="submit" value="Conferma e Invia il Sollecito">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadSollecitoTrasmComp">
            <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=MessaggioEsito.getIdMessaggio()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.richiesta.action.ActLoadRiscontroTrasmissioneCompetenza">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>

</html>