<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@page import="org.apache.log4j.Logger"%>
<%@page import="f3b.log.LogF3B"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="StrdataInizioPena"     scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"           scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="istitutodetenzione"	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="eventonotifica"        scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="archiviazione"         scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="magistrato"            scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<%
  String FlagIstanza="";
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  /* 
   * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
   * Numero MEV : SIES v10
   * Autore    : gioggi
   * Data      : 28/gen/2016
   * Branch    : MEV_SIES v10
   */
//    List listaMisure =(List) request.getAttribute("listaMisureSic");
//    int nMis = 0;
//    if(listaMisure != null)
//      nMis = listaMisure.size();
   
//    MisuraSicurezzaModel nuovaMis = new MisuraSicurezzaModel();
//    if(nMis != 0)
//      nuovaMis = (MisuraSicurezzaModel)listaMisure.get(nMis-1);
  //***** FINE INTERVENTO MEV_SIES v10 *****//

//Gestione Autorità Esterne sulle Notifiche - 
String lCodTipoAutorita = "-";
NotificaModel lPrimaNotifica = new NotificaModel();
AutoritaEsternaModel lAutoritaEsterna = new AutoritaEsternaModel();

if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
{
	    if (eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null && 
	    	eventonotifica.getNotifiche()[0].getCodTipoNotifica().equals("AA")	)
	    {
	   	 	lPrimaNotifica = eventonotifica.getNotifiche()[0];
	      	lCodTipoAutorita = eventonotifica.getNotifiche()[0].getAutoritaEsterna().getCodTipoAutorita();
	    }

	    lAutoritaEsterna = lPrimaNotifica.getAutoritaEsterna();
}


%>
<!--     DettaglioArchiviazioneManuale    -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Archiviazione Manuale </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
   
    </script> 
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Archiviazione Manuale </font>
      </td>

<% 	if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
			<!-- BOTTONE DI VALIDAZIONE -->
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadAnnotazioneDecisioneDellaSorveglianza&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActDettaglioArchiviazioneManuale&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
          <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
        </a>
      </td>
      
<%   } %>

<%  if((eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) ||
     (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) )
    {%>
        <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaArchiviazione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo()+"&CodPosizioneGiuridica="+lPosizione.getCodPosizioneGiuridica()%>"/>
      </jsp:include>
	<!-- 	ICONA DI MODIFICA	 -->
		<td class="LBG">
			<a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaArchiviazioneManualeMS&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
       		<img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
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
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        </font>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
        <input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" >
        
      </tr>
  
<%  //fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && 
    ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null
      && !penaresidua.getFlagErgastolo().equals("S") 
      && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
          if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
              (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
              (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0) )
          {
            
          }
          else
          {
%>
      <tr>
              <td class="l">Reclusione</td>
              <td class="l" colspan=2>
                <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
                <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
                <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
              </td>
              <td class="l">Multa</td>
              <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
      </tr>
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
         <tr>
            <td class="l" >Arresto</td>
            <td class="l" colspan=2>
               <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
               <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
               <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
            </td>
            <td class="l">Ammenda</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
            </td>
          </tr> 
<%
          }

     }  // CHIUDO if(penaresidua...)
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

        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
            if (penaresidua.getDataFine() != null)
            {
%>

               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
              </td>

<%        }

      }
%>
  </tr>

 <!-- Misure di Sicurezza -->
<%--
/* 
 * ISSUE MEV : eseguito merge tra 15 MEV: interpretazione con codice commentato
 * Numero MEV : SIES v10
 * Autore    : gioggi
 * Data      : 28/gen/2016
 * Branch    : MEV_SIES v10
 */
 <%  if(nMis > 0) {
%>
       <table>
          <tr>
            <td class=C>Misura di Sicurezza da espiare</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getDescrTipo())%>&nbsp;</font></td>
              <td class=C> Anni</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumAnni(),"0")%>&nbsp;</font></td>
              <td class=C> Mesi</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumMesi(),"0")%>&nbsp;</font></td>
              <td class=C> Giorni</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(nuovaMis.getNumGiorni(),"0")%>&nbsp;</font></td> 
          </tr>     
        </table>
<%  }
  else
  { %>  
      <tr><td class="l" > Fascicolo privo di Misure di Sicurezza </td></tr> 
<%  } %>
//***** FINE INTERVENTO MEV_SIES v10 *****//
--%>

<%  List lMisure =(List) request.getAttribute("listaMisureSic");
	if(lMisure.size() > 0) {
    	Iterator itx = lMisure.iterator();
    	while ( itx.hasNext()) {
	      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
%>
          <tr>
            <td class=C>Misura di Sicurezza da espiare</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
              <td class=C> Anni</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
              <td class=C> Mesi</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
              <td class=C> Giorni</td>
              <td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td> 
          </tr>     
<%  	}
	}	
 %>  
</table>

  <br>  
  <table>
  <!--Emissione-->  
        <tr>
          <td class="l" width="20%">Ufficio Emittente </td>
          <td class="L" colspan=1 width="60%">
            <font class="campo">
              <%=archiviazione.getDescrTipoAutoritaEmittente()%>
            </font>&nbsp;
            di
            &nbsp;<font class="campo">
              <%=archiviazione.getDescrLuogoEmittente() %>
            </font>
          </td>
        </tr>

  <!--Magistrato Firmatario -->   
  <tr>
     <td class="l" width="20%">Magistrato firmatario</td>
     <td class="L" colspan=1>
          <font class="campo">
            <%=StringUtils.toStringJSP(magistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(magistrato.getNome())%>
          </font>
      </td>
  </tr>        
 <!--Definizione -->  
       <tr>
          <td class="l" width="20%">Data Definizione</td>
          <td class="L" colspan=1 width="60%">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataDefinizione() ,"dd-MM-yyyy")) %>
            </font>
          </td>
      </tr>
<!--Trasmissione -->     
<%  if(lPrimaNotifica.getDataInvio() != null )
  { %> 
     <tr>  
	    <td class="l" width="20%">Data Trasmissione</td>
	    <td class="L" colspan=1>
	      <font class="campo">
	        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPrimaNotifica.getDataInvio(),"dd-MM-yyyy"))%>
	        <!--  %=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataEmissione() ,"dd-MM-yyyy")) % -->
	      </font>
	    </td>
     </tr>
<%  } %>     
      <tr>
          <td class="l" width="20%">Oggetto Definizione </td>
          <td class="L" colspan=1 width="60%">
            <font class="campo">
              <%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%>
            </font>
          </td>
        </tr>
<%  if(archiviazione.getNote() != null )
  { %>      
      <tr>
          <td class="l" width="20%">Note</td>
          <td class="L" colspan=1 width="60%">
            <font class="campo">
              <%=StringUtils.toStringJSP(archiviazione.getNote())%>
            </font>
          </td>
        </tr> 
  
<%  } %>

<!--Tipo di Istituto (se presente) 	-->
<%		if(istitutodetenzione != null && istitutodetenzione.getDescrTipoIstituto() != null && 
			!istitutodetenzione.getDescrTipoIstituto().equals(""))
		{ 
				%>
		   <tr>
			    <td class="l" width="20%">Struttura Designata</td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrizione() )%></font>&nbsp;
			      -&nbsp;
			      <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getIndirizzo() )%></font>&nbsp;
			      ,&nbsp;
			       <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrComune() )%>&nbsp;
			       (
			       <font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getCodProvincia() )%>&nbsp;)
			    </td>
		   </tr>
<%		} %>

<!--Autorita ESTERNA o Altra Autorita 	PRIMA-->
<%	if(lPrimaNotifica.getAutoritaEsterna() != null)
	{ %>
		   <tr>
			    <td class="l" width="20%"> Autorita' </td>
			    <td class="L" colspan=2 width="60%">
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
			      di
			      <font class="campo"><%=StringUtils.toStringJSP( lPrimaNotifica.getAutoritaEsterna().getDescrSede())%></font>&nbsp;
			    </td>
		   </tr>
<%  
			if(lPrimaNotifica != null &&
			   lPrimaNotifica.getNote()!= null)
			{
%>
		   		<tr>
		    			<td class="l" width="20%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
		}
%>
	
<!-- 				Destinatario MDS		 -->
<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ 	
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			//siesLogger.debug("  ---- XXXXXX -- DettaglioOEInterna - Not = "+eventonotifica.getNotifiche()[i]);
			if(eventonotifica.getNotifiche()[i] != null 
			   && ("MS").equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica() ) )
			{	%>
					<tr>
				     	<td class="l" width="20%">Destinatario Sorveglianza </td>
			         	<td class="L" colspan=2 width="60%">
			            	<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio()) %>
			            	</font>&nbsp;
							di 
			          		<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrComune() ) %>
			          		</font>&nbsp;
			            </td>
			        </tr>
<%			}
		}
	}	%>
	<tr><td>&nbsp;</td></tr>
	</table>
<%
for (int i=0;i< eventonotifica.getNotifiche().length; i++)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
  
  if(   lNotMod.getCodTipoNotifica().equals("ND") 
     && lNotMod.getAutoritaEsterna()!=null 
     && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null
    )
  {
     AvvocatoSiepModel   lAvvMod = lNotMod.getAvvSiep();
     AutoritaEsternaModel lAuMod = lNotMod.getAutoritaEsterna();
  %>
  <table width="80%" >
  <tr>
    <td class="l">Notifica al Difensore</td>
    <td class="L" colspan="3">
      <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
      &nbsp;Foro di&nbsp;
      <font class="campo">
        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
      </font>
      &nbsp;Difensore di&nbsp;
      <font class="campo">
        <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
      </font>
    </td>
  </tr>
  <tr>
    <%  if (StringUtils.toStringJSP( lAuMod.getCodTipoAutorita().trim()).compareTo("C0") == 0){ %>
        <td class="l" width="20%"> tramite </td>
        <td class="L" colspan=3 width="60%">
          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;</td>
    <%  } else { %>
        <td class="l" width="20%"> presso </td>
        <td class="L" colspan=3 width="60%">
          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
        </td>
    <%  }  %>
  </tr>
  </table>
<% } // end if
} // end while
%>
<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->
<br>
 <div align=left style="visibility:hidden" id="upld" >
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
    <table>
      <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
      <tr>
              <td class="L">
                <input class="bottone" type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"             value="siap.siep.misurasicurezza.action.ActUploadEsecuzioneMS">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"        value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActDettaglioArchiviazioneManuale">
          </td>
        </tr>
    </table>
  </FORM> 
</div>
<%-- MEV_39: aggiunto tasto di reindirizzamento --%>
<%
if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
		&& "S".equals(eventonotifica.getEvento().getFlagDocumentoRegistrato())) {
%>
	<div id="restituzione" align="left" style="position:relative; top: -111px;">
	<form method="POST" name="restituzione" action="<%=IWebConstants.PG_MAIN%>">
	<table>
        <tr>
          	<td class="L">
            	<input class="bottone" type="submit" name="restituzione" value="Restituzione Comunicazione Ordine di Consegna">
            	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActLoadInserisciRestituzioneOrdineConsegna">
          	</td>
		</tr>
	</table>
	</form>
	</div>
<%
}
%>
</body>
</html>