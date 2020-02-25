<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>


<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="fascicoloBdmc"        scope="request" class="java.util.Vector"/>

<%
  EventoNotificaModel lEve = eventonotifica;

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

  MagistratoModel lMagistrato = lEve.getMagistrato();
  if( lMagistrato == null)
    lMagistrato = new MagistratoModel();
%>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Evento- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

  </head>
  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Ordine Esecuzione Condannato Detenuto per Altra Causa</font>
        </td>
 <%if (lEve.getEvento().getFlagDocumentoRegistrato()!=null)
 if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
{%>

<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaOEAltraCausa&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
   </jsp:include>
 <%}%>

<%if (lEve.getEvento().getFlagDocumentoRegistrato()==null)
 {%>

<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaOEAltraCausa&IdEvento="+lEve.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%}%>
      <!-- TOOLBAR HEADER -->
      <td class="LBG">
          <jsp:include page="<%=ICostantiEvento.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=eventonotifica.getEvento().getIdEvento()%>" />
          <jsp:param name="FlagDocumentoRegistrato" value="<%=eventonotifica.getEvento().getFlagDocumentoRegistrato()%>" />
          </jsp:include>
     </td>

      </tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table>
      <tr>
       <td class="l">Posizione Giuridica </td>
<%
			   if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
         {
%>
	         <td class="L" colspan=3>
	         	 <font class="campo">
	         	 	 DETENUTO PER ALTRA CAUSA
<% 
							if(    posizioneluogoaltra.getAltraCausa() != null 
							    && posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica() != null
							    && !"".equals(posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica())
							    && !"-".equals(posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica())
							   )
							{						    
%>              
              	<%=posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica()%>
<%
							}
%>            		
	         	 </font> 
	         </td>
<%
				 }
			   else
			   {
%>
         	 <td class="L" colspan=3>
         	   <font class="campo">
         	     <%=lPosizione.getDescrPosizioneGiuridica()%>
         	   </font>
         	 </td>
<%
				 }
%>

<%
      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
//modifica relativa al tipo istituto
    if(lAltraCausa.getIstitutoDetenzione() != null)
        //if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
        {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=3><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
              <%//if(lAltraCausa.getDescrLuogoIstituto()!=null){%>
              di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            <%//}%>
            </td>
           </tr>
           <%if (lAltraCausa.getAltroLuogo()!=null)
            { %>
            <tr>
             <td class="l">Altro Luogo </td >
             <td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp; </td>
          <%} %>
          <%}
       } else
        // if(!lLuogoDetenzione.getDescrTipoIstituto().equals("") && lLuogoDetenzione.getDescrTipoIstituto()!= null && !lLuogoDetenzione.getDescrTipoIstituto().equals("-"))
         if(lAltraCausa.getIstitutoDetenzione() != null)
          {%>
          <tr>
           <td class="l">Detenuto presso </td>
           <%if (lLuogoDetenzione!= null && lLuogoDetenzione.getIstitutoDetenzione()!= null){ %>
	           <td class="L" colspan=3><font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
	           <%//if(lLuogoDetenzione.getDescrLuogo()!=null){%>
	               di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
	           <%//}%>
	            </td>
            <%} %>
          </tr>
          <%if (lLuogoDetenzione.getAltroLuogo()!= null)
            { %>
            <tr>
             <td class="l" >Altro Luogo </td >
             <td class="L" colspan=3><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp; </td>
          <%} %>
             <td>
            </tr>
       <%}%>

         <%// fine modifica relativa al tipo istituto
    if(penaresidua.getIdPenaResidua() != null && ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
    {
       if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0)&&
             (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0))
          {}else{%>
      <tr><td class="l">Reclusione</td>
      <td class="l" colspan=1>
        <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
      </td>
      <td class="l">Multa</td>
      <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <%} %>
    <tr>
    <% if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
          {}else{%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=1>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=1><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
       <%}
    }%>
       <tr>
<%
        if (penaresidua.getDataInizio() != null)
        {
%>
          <td class="l">Data Decorrenza Pena</td>
          <td class="L" colspan="1"><font class="campo"><%=StrdataInizioPena%></font></td>
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
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
        }


        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }else {%>
         <td class="l">Data Fine Pena</td>
         <td class="lRosso" >
           <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>


<%            }
      }
%>
       </tr>
       <tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=1>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(), "dd/MM/yyyy"))%>
          </font>
        </td>
        <td class="l">Data Trasmissione</td>
        	<%if(lEve.getNotifiche() != null && lEve.getNotifiche().length > 0)
       		  {%>
         		<td class="L" colspan=1><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getNotifiche()[0].getDataInvio(),"dd/MM/yyyy"))%></font></td>
         	<%} %>
      </tr>

    <tr>
     <td class="l">Magistrato</td>
        <td class="L" colspan="2">
          <font class="campo">
            <%=StringUtils.toStringJSP(lMagistrato.getCognome())%> &nbsp;<%=StringUtils.toStringJSP(lMagistrato.getNome())%>
          </font>
        </td>
      </tr>
<%//modifica relativa al tipo istituto%>
<%if(lEve.getNotifiche() != null && lEve.getNotifiche().length > 0 && lEve.getNotifiche()[0].getIstitutoDetenzione() != null){%>
     <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getIstitutoDetenzione().getDescrComune())%></font>&nbsp;
      </td>
     </tr>

   <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0].getNote())%></font>&nbsp;</td>
     </tr>

<%}else if(lEve.getNotifiche() != null && lEve.getNotifiche().length > 0 && lEve.getNotifiche()[0].getAutoritaEsterna() != null){%>
     <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
     </tr>

   <tr>
      <td class="l">Indirizzo</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[0].getNote())%></font>&nbsp;</td>
     </tr>
<%}%>



      <% int count = 1;

         if (lEve.getAvvocati() !=null)
		 {
         for  (int lIndex = 0;lIndex<lEve.getAvvocati().length; lIndex++ ) {
%>
      <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lEve.getAvvocati()[lIndex].getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
    <%if(lEve.getNotifiche() != null && lEve.getNotifiche().length > 0 && lEve.getNotifiche()[count].getAutoritaEsterna()!=null)
 {%>
      <tr>
	    <td class="l">Autorita Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[count].getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[count].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
     </tr>
     <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lEve.getNotifiche()[count].getNote())%></font>&nbsp;</td>
     </tr>
<%}%>
  <%count++;}}%>
<% if (fascicoloBdmc != null && fascicoloBdmc.size() > 0) { 
	
	  FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel() ;
	  lFasMod = (FascicoloSiepBdmcModel) fascicoloBdmc.get(0);
	
	%>
	 <tr> 
      <td class="l">Anno/Numero Fasc. B.D.M.C.</td>
      <td class="L">
        <font class="campo"><%=lFasMod.getChiaveAnnoBdmc() %></font>&nbsp;
        /<font class="campo"><%=lFasMod.getChiaveProgrBdmc() %></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Autorità B.D.M.C.</td>
      <td class="L">
        <font class="campo"><%=UfficioUtils.getDescTipoUffByCodUfficio(lFasMod.getChiaveUfficioBdmc())%></font>&nbsp;
      </td>
      </tr>
 <% }%>
  </table>
  <br>
  <div align=left style="visibility:hidden" id="upld"><%-- onSubmit="return controllaUpload();" --%>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActUploadOE">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= lEve.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausa">
          </td>
        </tr>
      </table>
    </FORM>
  </div>
  <br>
  <br>
</body>

</html>