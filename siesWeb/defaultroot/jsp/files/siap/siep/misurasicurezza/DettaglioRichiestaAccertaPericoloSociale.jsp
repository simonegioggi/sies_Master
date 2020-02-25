<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizione"         scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="StrdataInizioPena" scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataFinePenaA"  scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"       scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>


<%

String FlagIstanza="";
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MagistratoModel lMagistrato = eventonotifica.getMagistrato();
if( lMagistrato == null)
  lMagistrato = new MagistratoModel();

// Gestione Autorità Esterne sulla prima Notifica
String lCodTipoAutorita = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lPrimaAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
      if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null)
      {
          lCodTipoAutorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
          lPrimaNotifica = eventonotifica.getNotifiche()[0];
          lPrimaAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
      }
}

String DescrAuto="-";
String TipoAuto="";
for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
{
  if(eventonotifica.getNotifiche()[i] != null 
  && ("MS".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica()) ||
	  "MM".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())   ) )
  {
      DescrAuto = eventonotifica.getNotifiche()[i].getUfficio().getDescrComune();
      TipoAuto = eventonotifica.getNotifiche()[i].getUfficio().getCodTipoUfficio();
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug(" --XX-- TipoAuto = "+TipoAuto );
  }
} 

%>
<!--  DettaglioRichiestaAccertaPericoloSociale -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Dettaglio Accertamento pericolosità sociale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript">
   
   function eseguiAzione(aIdEvento)
   {

          lAzione = "siap.siep.misurasicurezza.action.ActLoadTrasferisciRichiestaAccertaPericoloSociale";
          document.trasferisci.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value = aIdEvento;
          document.trasferisci.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.trasferisci.submit();

   }   
      
  </script>

</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Richiesta Accertamento Pericolosità Sociale</font>
      </td>
<%
    if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
        <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaRichiestaAccertaPericoloSociale&autorita="+lCodTipoAutorita+"&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&IdPosizioneGiuridica="+posizione.getCodPosizioneGiuridica()%>"/>
      </jsp:include>
<%   }

   if((eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null) &&
       (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) )
     {%>
      <!-- BOTTONE DI TRASFERIMENTO -->
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadTrasferisciRichiestaAccertaPericoloSociale&autorita=<%=DescrAuto%>&CodTipoSorv=<%=TipoAuto%>&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
              <img src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
            </a>
        </td>

<%   }%>


    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
         <%=posizione.getDescrPosizioneGiuridica()%>
        </font>
      </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
    </tr>
  
<%  //fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && 
    ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
      && !penaresidua.getFlagErgastolo().equals("S") 
      && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
 %>
      <tr>
          <td class="l">Pena da Espiare</td> 
              
 <%         if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
              (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
              (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
          {
            
          }
          else
          {
%>
              <td class="l"><font class="campo" >Reclusione</font></td>
              <td class="l" colspan=2>
                <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
                <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
                <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
              </td>
              <td class="l">Multa</td>
              <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
%>

<% 
      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
              (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {
        
        }
      else
        {
%>
            <td class="l" ><font class="campo" >Arresto</font></td>
            <td class="l" colspan=2>
               <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
               <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
               <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
            </td>
            <td class="l">Ammenda</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
            </td>
<%
          } %>
      
      </tr>         

<%     }  // CHIUDO if(penaresidua...)
%>
    <tr>
<%
      if (penaresidua.getDataInizio() != null)
      {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
      }
 
       if (penaresidua.getFlagErgastolo() != null)
       {
           if(penaresidua.getFlagErgastolo().equals("S"))
           {
  %>
              <td class="l">Pena Detentiva</td>
              <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
  <%
           }
           else if(penaresidua.getFlagErgastolo().equals("D"))
           {
  %>
              <td class="l">Pena Detentiva</td>
              <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
  <%
           }
       }
%>
<%
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {

            if( penaresidua.getDataFine() != null)
            {

%>
                 <td class="l">Data Fine Pena</td>
                 <td class="lRosso" colspan=2>
                   <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                   <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                   <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                   <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
                </td>

<%        }

        }
%>
  </tr>
</table>
<%  // Eventuali Misura di Sicurezza  %>

<%
  List lMisure =(List) request.getAttribute("listaMisure");
  if(lMisure.size() > 0)
  {
%>
      <table>
<%
      Iterator itx = lMisure.iterator();
      while ( itx.hasNext())
      {
          MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
  %>
        <tr>
          <td class=C>Misura di Sicurezza da espiare</td>
            <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
            <td class=C>Anni</td>
            <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
            <td class=C>Mesi</td>
            <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
            <td class=C>Giorni</td>
            <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td> 
        </tr>     
  <% 
      } %>
      
      </table>
<%  }
%>  
  <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=1>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L" colspan=1>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy"))%>
        </font>
      </td>
     </tr>
  </table>
 <!--Richiesta -->  
 <table >
   <tr>
        <td class="l" width="20%">Tipo Richiesta </td>
        <td class="L" colspan=1 width="60%">
          <font class="campo">
            <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
          </font>
        </td>
 <%   if(eventonotifica.getCampoNote()!= null && eventonotifica.getCampoNote().length >0 && eventonotifica.getCampoNote()[0] != null && eventonotifica.getCampoNote()[0].getDescr() != null)
    {
 %>
      <tr>
            <td class="l" width="20%">Note</td>
            <td class="L" colspan=1 width="60%"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr())%>
            </font>
            </td>
          </tr> 
    <%  } %>    

<!--Magistrato Firmatario -->   

  <tr>
     <td class="l">Magistrato </td>
     <td class="L" colspan="2">
          <font class="campo">
            <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          </font>
      </td>
      <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
  </tr>
  <tr><td>&nbsp;</td></tr>
</table>  

<table>
<!--Autorita di destinazione MDS -->
<% 
    for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
    {
      if(eventonotifica.getNotifiche()[i] != null 
        && ("MS".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())  	||
           ("MM".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica())) ) )
        {
%>
         <tr>
            <td class="l">Autorita destinazione</td>
          	<td class="L" colspan=2>
            	<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio()) %></font>
				di 
				<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrComune()) %></font>
            </td>
          </tr>
<%  
          if(eventonotifica.getNotifiche()[i].getNote()!= null)
          {
%>
            <tr>
                <td class="l">Note </td>
                <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getNote())%>
                  </font>
                </td>
             </tr>
<%        }
    	}
    }
%>  
<!--Autorita ESTERNA o Altra Autorita -->

<%
  if(lPrimaNotifica.getAutoritaEsterna() != null)
  { %>

       <tr>
          <td class="l">Altra Autorita</td>
          <td class="L" colspan=2>
            <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
            di
            <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
          </td>
       </tr>
<%  
      if(lPrimaNotifica.getNote()!= null)
      {
%>
          <tr>
              <td class="l">Note</td>
              <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
          </tr>
<%    } 
	} %>	 
</table>

<!-- 	Destinatari per conoscienza : Procure	 -->
<% 
    for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
    {
      if(eventonotifica.getNotifiche()[i] != null &&
         eventonotifica.getNotifiche()[i].getCodTipoNotifica()!=null &&
         eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("N") )
       {
%>
		<table>
         <tr>
            <td class="l">Destinatario per Conoscenza</td>
          	<td class="L" colspan=2>
            	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio() )%></font>
          	</td>
            <td class="l"> di </td>
          	<td class="L" colspan=2><font class="campo">
            	<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrComune() ) %>
              	</font>
            </td>
         </tr> 
       </table>   
<%
        }
    }
%>


<br>
  <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class="bottone" type="submit" value="Conferma">
            <input type="HIDDEN" name="motivo" value="<%=eventonotifica.getEvento().getCodMotivo()%>">
            <input type="HIDDEN" name="autorita" value="<%=lPrimaAutoritaEsterna.getCodTipoAutorita()%>">
            <input type="HIDDEN" name="CodTipoSorv" value="<%=TipoAuto%>">
            <input type="HIDDEN" name="DescrSedeSorv" value="<%=DescrAuto%>">
            <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>" value="<%=penaresidua.getIdPenaResidua()%>">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadRichiestaAccertaPericoloSociale">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioRichiestaAccertaPericoloSociale">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
<br>
<br>
</body>
</html>