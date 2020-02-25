<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.model.ArchiviazioneModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  ArchiviazioneModel lArcMod = archiviazione;

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

//   if(lArcMod == null)
//     lArcMod = new ArchiviazioneModel();
  
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
}
  
%>
<!-- 		LoadDettaglioArchiviazionePerProvvGiudiceCassazione		 -->
<html>
<head>
  <title>[S.I.E.S.] - Gestione Misure sicurezza Provvisorie o Fuori Sentenza -Definizione per Provvedimento Giudice/Cassazione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      	<font class="campo">Dettaglio Definizione Procedimento - Archiviazione per Provvedimento del Giudice/Cassazione</font>
      </td>

<%
if( eventonotifica.getEvento().getFlagDocumentoRegistrato()==null 
	|| ( eventonotifica.getEvento().getFlagDocumentoRegistrato() !=null 
      && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0 ) )
{ %>
<!-- BOTTONE DI VALIDAZIONE -->
     <td class="LBG">
       <a href="/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActUploadArchiviazionePerProvvGiudiceCassazione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvGiudiceCassazione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
        <img align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
       </a>
     </td>
<!-- BOTTONE DI STAMPA -->
       <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
         <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.misurasicurezza.action.ActStampaArchiviazione&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodMotivo="+eventonotifica.getEvento().getCodMotivo() %>"/>
       </jsp:include>
<!-- 	ICONA DI MODIFICA	 -->
	<td class="LBG">
	  <a href="Main.jsp?Action=siap.siep.misurasicurezza.action.ActLoadModificaArchiviazionePerProvvGiudiceCassazione&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&TornaQui=10">
      <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0">
      </a>
	</td>               
<%
 } %>

 </tr>
</table>
<br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=3>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
        </font>
      </td>
    </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

             </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>

                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
          </tr>
<%
        }

    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
           </tr>
<%
          }
        }

    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
%>
<tr>
<%
    	
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>

          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
<%
          if(penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      </tr>
</table>

<table width="80%">
 <!-- Misure di Sicurezza -->  
<%	List lMisure =(List) request.getAttribute("listaMisureSic");
	if(lMisure.size() > 0)
	{
 		Iterator itx = lMisure.iterator();
 		while ( itx.hasNext())
 		{
      		MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
%>
			    <tr>
			    	<td class=L>Misura di Sicurezza da espiare</td>
			      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
			    </tr>
		    	<tr>
		    		<td class=L width = "25%">&nbsp;  Durata  Misura  </td>  	
		      		<td class=L>
		      			<font class="label"> Anni </font>&nbsp;
		      			<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      			<font class="label"> Mesi </font>&nbsp;
		      			<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;&nbsp;&nbsp;</font>
		      			<font class="label"> Giorni </font>&nbsp;
		      			<font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;&nbsp;&nbsp;</font>
					</td>				    
			    </tr>
<%			if(lMis.getLuogoEsecuzioneMisura()!=null && lMis.getLuogoEsecuzioneMisura().compareTo("")!=0)
			{ %>
		    	<tr>
		    		<td class=L width = "25%">&nbsp;  Luogo Esecuzione Misura </td>
		    		<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getLuogoEsecuzioneMisura())%>&nbsp;</font></td>
		    	</tr>			    
<%			}
		}
	}	%>
 </table>
	<br>

<table width="80%">
<!--  Dati Provvedimento e Definizione -->

<%       if(lArcMod.getDataRicezione()!= null)
        {
%>
         <tr>
          <td class="l">Data Ricezione</td>
          <td class="L" colspan='3'>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataRicezione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%      } %>
 		<tr>
			<td class="l">Data Emissione Provvedimento</td>
			<td class="L" colspan='3'>
				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataEmissione(),"dd-MM-yyyy"))%></font>
			</td>
 		</tr>       
 		<tr>	<!--  Ordinanza/decreto -->
    	 <td class="l" width="20%">Tipo Provvedimento </td>
    	 <td class="L" colspan='3' width="60%">
    		<font class="campo">
    			<%=lArcMod.getDescrTipoProvvedimentoArc() %>
    		</font>
    	 </td>
   		</tr>
   		
<% 		if(lArcMod.getAnnoProvvedimento() != null &&
			lArcMod.getNumProvvedimento() != null	)
		{	%>
	        <tr>
	         	<td class="l">Anno/Numero Provvedimento </td>
	         	<td class="L">
	          		<font class="campo"><%=StringUtils.toStringJSP(lArcMod.getAnnoProvvedimento() )%></font>
	          		/ <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNumProvvedimento() ) %></font>
	         	</td>
	       </tr>
<%		} %>

	        <tr>
	         <td class="l">Autorità Emittente </td>
	         <td class="L" colspan='3'>
	          <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrTipoAutoritaEmittente())%></font>
	          di <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrLuogoEmittente()) %></font>
	         </td>
	       </tr>
	       
<%		if(lArcMod.getIndirizzoEmittente()!= null)
		{
%>					<!--   Eventuale Indirizzo UFFICIO EMITTENTE -->
	      <tr>
	       <td class="l">Note </td>
	       <td class="L" colspan='3'>
	         <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getIndirizzoEmittente())%></font>
	       </td>
	      </tr>
<%      }  %>	       	

<% 	//	if(lArcMod.getCodTipoAutoritaEmittente() != null && !lArcMod.getCodTipoAutoritaEmittente().equals("-")
    //     	&& lArcMod.getCodLuogoEmittente() != null && !lArcMod.getCodLuogoEmittente().equals("-"))
    //    {
%>  <!-- 
	        <tr>
	         <td class="l">Autorità Emittente </td>
	         <td class="L" colspan='3'>
	          <font class="campo">< %=StringUtils.toStringJSP(lArcMod.getDescrTipoAutoritaEmittente())%></font>
	          di <font class="campo">< %=StringUtils.toStringJSP(lArcMod.getDescrLuogoEmittente()) %></font>
	         </td>
	       </tr>	
	       			-->
<% 		//	} %>
	<!--  
		<tr>
	         <td class="l">Ufficio Emittente </td>   (LOCALE DELL'EVENTO)
	         <td class="L" colspan='3'>
	          <font class="campo">< %=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrUfficioEmittente())%></font>
	          di <font class="campo">< %=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrLuogoEmittente()) %></font>
	         </td>
	    </tr>
			 -->
			 
		<tr><td>&nbsp;</td></tr>
				
<%     	if(lArcMod.getDataDefinizione()!= null)
        {
%>
         <tr>
          <td class="l">Data Definizione</td>
          <td class="L" colspan='3'>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lArcMod.getDataDefinizione(), "dd-MM-yyyy") )%></font>
          </td>
         </tr>
<%
        }

		if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
		{
			NotificaModel lNotMod = eventonotifica.getNotifiche()[0];
%>
		<tr>
          <td class="l">Data Trasmissione</td>
          <td class="L" colspan='3'>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lNotMod.getDataInvio(), "dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%		}
		
       if(lArcMod.getCodOggettoDefinizione()!= null)
        {
%>
         <tr>
          <td class="l">Oggetto Definizione</td>
          <td class="L" colspan='3'>
            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getDescrOggettoDefinizione())%></font>
          </td>
         </tr>
<%
        }
       
        if(lArcMod.getNote() != null)
        {	%>   
			<tr>
	          <td class="l">Note</td>
	          <td class="L" colspan='3'>
	            <font class="campo"><%=StringUtils.toStringJSP(lArcMod.getNote())%></font>
	          </td>
	        </tr>
<% 		}

  if(magistrato != null)
   {
%>
         <tr>
          <td class="l">Magistrato firmatario</td>
           <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
            <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome())%></font>
           </td>
         </tr>
<% } %>

<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ %>
	<tr>
      <td colspan=4 class="titolo">	Destinatari	</td>
    </tr>
<%	} %>
    
<!--Autorita ESTERNA o Altra Autorita 	PRIMA-->
<%	if(lPrimaNotifica.getAutoritaEsterna() != null)
	{ %>
		   <tr>
			    <td class="l" width="30%"> Altra Autorita</td>
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
		    			<td class="l" width="30%">Note</td>
		    			<td class="L" colspan="2" width="60%"><font class="campo"><%=StringUtils.toStringJSP(lPrimaNotifica.getNote())%></font>&nbsp;</td>
		   		</tr>
<%			}
		}
%>

<!-- 				Destinatari Uffici Sorveglianza 		 -->
<%	if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
	{ 	
		for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
		{
			if(eventonotifica.getNotifiche()[i] != null && eventonotifica.getNotifiche()[i].getCodTipoNotifica() != null)
			{
				if(eventonotifica.getNotifiche()[i].getCodTipoNotifica().compareTo("MS")== 0)	
				{	%>
					<tr>
				     	<td class="l" width="30%">Ufficio Sorveglianza </td>
			         	<td class="L" colspan=2 width="60%">
			            	<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio()) %>
			            	</font>&nbsp;
							di 
			          		<font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getUfficio().getDescrComune() ) %>
			          		</font>&nbsp;
			            </td>
			        </tr>
<%				} 
			}	
		}
	}	%>

	<!-- 		Istituto Detenzione		 -->
<% 	for (int i=0;i< eventonotifica.getNotifiche().length; i++)
	{
	  NotificaModel lNotMod = eventonotifica.getNotifiche()[i];
	
	  if(lNotMod != null && lNotMod.getIstitutoDetenzione() != null )
	  { 
	  %>
	    <tr>
	     <td class="l">Struttura Designata </td>
	     <td class="l" colspan='3'>
	      <font class="campo"> <%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
	      <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrizione())%></font>
	     </td>
	    </tr>
	
	<% } %>

<%
  if(   lNotMod.getCodTipoNotifica().equals("ND") 
     && lNotMod.getAutoritaEsterna()!=null 
     && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null
    )
  {
     AvvocatoSiepModel   lAvvMod = lNotMod.getAvvSiep();
     AutoritaEsternaModel lAuMod = lNotMod.getAutoritaEsterna();
  %>
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
        <td class="L" colspan=2 width="60%">
          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;
    <%  } else { %>
        <td class="l" width="20%"> presso </td>
        <td class="L" colspan=2 width="60%">
          <font class="campo"><%=lAuMod.getDescrTipoAutorita().trim()%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
        </td>
    <%  }  %>
  </tr>
  
<% } // end if
} // end for
%>
</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActUploadArchiviazionePerProvvGiudiceCassazione">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvGiudiceCassazione">
          </td>
        </tr>
      </table>
</form>
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