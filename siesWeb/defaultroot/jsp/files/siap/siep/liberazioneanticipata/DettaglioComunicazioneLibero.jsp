<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.w_magistrato.action.ICostantiWMagistrato"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="sedeautcompetente"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"    scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaN"    scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"    scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="sorveglianza"        scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"   scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="luogodetenzione"     scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="sedesorveglianza"    scope="request" class="java.lang.String"/>
<jsp:useBean id="tiposorveglianza"    scope="request" class="java.lang.String"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="lPosGiuModificata"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="UffUDS"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
<jsp:useBean id="flagfungibilita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="liberazione"         scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="liberazioninonconcesse"           scope="request" class="java.util.Vector"/>

<jsp:useBean id="Licenze" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="lTotGiorniConcessi"     scope="request" class="java.lang.String"/>

<%
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
%>
<html>

<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
   EventoModel lProvvedimento = new EventoModel(evento);

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();
   String verbaleSott = null;
   
   // 20/05/2014 - Nuova L.A.
 LicenzaLibAnticipataModel lLiceModel = new LicenzaLibAnticipataModel();
 
 Iterator IteLic = Licenze.iterator();
 
 int totOldLA = 0;
 int totggLA = 0;
 int totggLS = 0;
 int totggLI = 0;
 boolean NuovaLA = false;
 
 while(IteLic.hasNext())
 {
	  	lLiceModel = (LicenzaLibAnticipataModel)IteLic.next();
	  	if(lLiceModel.getFlagConcesso() != null && 
		 	lLiceModel.getFlagConcesso().compareTo("C") == 0 )
	  	{
	  		if(lLiceModel.getDescrStatoPermesso() != null)
	  		{
	  			if(lLiceModel.getDescrStatoPermesso().compareTo("LA") == 0)
	  			{
	  				NuovaLA = true;
	  				totggLA += lLiceModel.getNumeroGiorni().intValue();	
	  			}
	  			else if(lLiceModel.getDescrStatoPermesso().compareTo("LS") == 0)
	  		  	{
	  		  		NuovaLA = true;
	  		  		totggLS += lLiceModel.getNumeroGiorni().intValue();	
	  		  	}
	  			else if(lLiceModel.getDescrStatoPermesso().compareTo("LI") == 0)
	  			{
	  				NuovaLA = true;
	  			  	totggLI += lLiceModel.getNumeroGiorni().intValue();	
	  			}
	  			else
	  			{
	  			}
	  			
	  		}
	  		else
	  		{	
	  			totOldLA += lLiceModel.getNumeroGiorni().intValue(); 
	  		}	
	  	}
 }
 
%>
     <font class="campo">Dettaglio Comunicazione Concessione Liberazione Anticipata per Condannato Libero</font>
</td>
<%
  if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
    {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     <%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.ordinescarcerazione.action.ActStampaOSLiberazioneAnticipata&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>&codposizionegiuridica=<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>" onclick="javascript:lookUpload();">
            <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.sico.libertaanticipata.action.ActStampaComunicazioneLALibero&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%
    }

  if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
  {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class="LBG">
      <a href="/jsp/Main.jsp?Action=siap.siep.ordinescarcerazione.action.ActStampaOSLiberazioneAnticipata&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>&codposizionegiuridica=<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>" onclick="javascript:lookUpload();">
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
    </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.sico.libertaanticipata.action.ActStampaComunicazioneLALibero&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   </jsp:include>
<%
  }
%>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <br>
  <table>
    <tr><td><input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>"></td></tr>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
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
        if(lPosGiuModificata != null && lPosGiuModificata.getIdPosizioneGiuridica() != null)
        {
%>
          <%=lPosGiuModificata.getDescrPosizioneGiuridica()%>
<%
        }
        else
        {
%>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
        }
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
    else if(lLuogoDetenzione.getIstitutoDetenzione()!= null)
    {
%>
      <tr>
        <td class="l">Detenuto presso </td>
        <td class="L" colspan=5>
          <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
          // if(lLuogoDetenzione.getDescrLuogo()!=null)
          // {
%>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
           // }
%>
        </td>
      </tr>
<%
    }
%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
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
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
        </tr>
<%
      }
    }
%>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
        <td class="l">Reclusione</td>
        <td class="l">
          <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
        </td>
<%
        if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
        {
%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
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
      if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
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
%>
<%
       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
       {
          if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())){
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" >
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
         </td>
<%
        }
        else
        {
%>
          <td class="l">Data Fine Pena</td>
          <td class="lRosso" >
            <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;</font>
          </td>
<%
        }
      }
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
<%
    if(eventonotifica.getEvento().getDataEmissione()!= null)
    {
%>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
      </td>
<%
    }

    if(eventonotifica.getNotifiche()[0].getDataInvio()!= null)
    {
%>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%>
        </font>
      </td>
<%
    }
%>
    </tr>
    <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza </td>
    </tr>
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getAnnoSius())%> /<%=StringUtils.toStringJSP(liberazione.getNumeroSius())%>
        </font>
      </td>
      <td class="l"> Anno / Numero Ordinanza </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getAnnoOrdinanza())%>/<%=StringUtils.toStringJSP(liberazione.getNumeroOrdinanza())%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità emittente
      </td>
      <td class="l" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getDescrUfficioEmittente())%>&nbsp;
        </font>
        di
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>
        </font>
      </td>
    </tr>
</table>    

<!-- 	20/05/2014 Nuova Ordinanza L.A. : Suddivisione delle diverse tipologie di giorni concessi -->    
 <table style="width: 95%;">   
    <tr>
      <td width="10%" class="l">Giorni Concessi</td>
      <td width="5%"class="l" colspan=3>
        <font class="campo">
         	<%= StringUtils.toStringJSP(lTotGiorniConcessi) %>&nbsp;&nbsp;&nbsp;
        </font>
      </td>
<%	if(NuovaLA)
	{ 	%>
		<td class="L">
			<font class="label" style="text-align: center; font-size: 10pt" >
			Di cui :&nbsp; 
			</font>
<%		if(totggLA != 0)
	
		{ %>		
        	<font class="campo"><%= StringUtils.toStringJSP(totggLA)%></font>
			<font class="label" style="text-align: center; font-size: 10pt" >
			 					&nbsp;di Liberazione Anticipata ;&nbsp;
			 </font>					
<%		}

		if(totggLS != 0)
		{	%>    
         	<font class="campo"><%= StringUtils.toStringJSP(totggLS)%></font>
        	<font class="label" style="text-align: center; font-size: 10pt">
			 					&nbsp; di Liberazione Anticipata Speciale ;&nbsp;
			</font> 
<%		}
		
		if(totggLI != 0)
		{	%>
         	<font class="campo"><%= StringUtils.toStringJSP(totggLI)%></font>
			<font class="label" style="text-align: center; font-size: 10pt">
			 					&nbsp; di Integrazione Liberazione Anticipata ;&nbsp; 
			</font> 
<%		}
	}
//	%>
		 </td>
    </tr>
</table>

<!-- 	End Nuova Ordinanza L.A.  -->
<table>
<%
	Iterator iter = liberazioninonconcesse.iterator();
	while (iter.hasNext())
	{
		LicenzaPeriodiLibAnticipataModel lLibPerMod = (LicenzaPeriodiLibAnticipataModel)iter.next();
		
		if(lLibPerMod != null)
		{
			LicenzaLibAnticipataModel lLibMod = lLibPerMod.getLicenza();
			
			if(lLibMod != null && lLibMod.getFlagConcesso() != null)
			{
				if(lLibMod.getFlagConcesso().equals("R"))
				{
%>
					<tr>
					  <td class="l" height="30">Giorni Rigettati</td>
					  <td class="l" colspan=3>
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
				if(lLibMod.getFlagConcesso().equals("I"))
				{
%>
					<tr>
					  <td class="l" height="30">Giorni Inammissibili</td>
					  <td class="l" colspan=3>
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
				if(lLibMod.getFlagConcesso().equals("N"))
				{
%>
					<tr>
					  <td class="l" height="30">Giorni N.L.P./N.D.P.</td>
					  <td class="l" colspan=3>
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
			}
		}
	}

		if(liberazione.getDataEmissioneOrdinanza()!= null)
    {
%>
      <tr>
      <td class="l">Data Emissione Ordinanza</td>
      <td class="l" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(liberazione.getDataEmissioneOrdinanza(),"dd-MM-yyyy"))%>
        </font>
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(liberazione.getDataEmissioneOrdinanza()))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(liberazione.getDataEmissioneOrdinanza()))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(liberazione.getDataEmissioneOrdinanza()))%>">
     </tr>
<%
    }
%>
    <tr>
      <td class="Titolo" colspan='8'>Destinatari</td>
    </tr>
<%
    if(magistrato != null)
    {
%>
      <tr>
        <td class="l">Magistrato Competente
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
          <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
        </td>
      </tr>
<%
    }

    if(eventonotifica.getNotifiche()[0].getAutoritaEsterna() != null)
    {
%>
     <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrTipoAutorita() )%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
      </td>
     </tr>
     <tr>
      <td class="l">Indirizzo</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[0].getNote())%></font>&nbsp;</td>
     </tr>
<%
  }

int count=0;
while(count < eventonotifica.getNotifiche().length)
{
  NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
  if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Avvocato per  Notifica</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
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
	      <td class="l">Autorita Notifica</td>
        <td class="L" colspan=2>
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Note</td>
        <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
      </tr>
<%
  }
  count++;
}
%>
</table>
<%
  int cont=1+count;
  if(flagfungibilita.equals("S"))
  {
    if (eventonotifica.getNotifiche()[cont].getAutoritaEsterna()!=null)
    {
%>
      <table>
       <tr>
        <td class="l">Autorità Destinazione</td>
        <td class="L" colspan=2>
          <font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[cont].getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp;
          di
          <font class="campo"><%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[cont].getAutoritaEsterna().getDescrSede())%></font>&nbsp;
        </td>
       </tr>
       <tr>
        <td class="l">Note</td>
        <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getNote())%></font>&nbsp;</td>
       </tr>
      </table>
<%
    }

  if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
  {
    if(lAltraCausa.getAnno()!=null || lAltraCausa.getNumero()!=null)
    {
%>
    <table>
      <tr><td>&nbsp;</td></tr>
      <tr>
         <td class="l">
            SENTENZA N.  <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAnno())%></font>/<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getNumero())%></font>
         </td>
       </tr>
       <tr>
         <td class="l">
         <%if(lAltraCausa.getData()!=null){%> DEL : <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(),"dd-MM-yyyy"))%></font>
          <%}%>
        </td>
        </tr>
        <tr>
         <td class="l">
         <%if(lAltraCausa.getDescrAutorita()!=null){%> DA :  <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrAutorita())%></font><%}%>  &nbsp;
         <%if(lAltraCausa.getDescrLuogo()!=null){%> di <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%></font><%}%>
         </td>
      </tr>
      </table>
<%
    }
  }
%>

  <table>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
     <tr>
        <td class="c" colspan="3">
         SCADENZA EFFETTIVA DELLA PENA <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>
         INFERIORE ALLA DATA DEL PROVVEDIMENTO <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
        <br>
         CON UN PERIODO FUNGIBILE DI anni <%=StringUtils.toStringJSP(fungibilita.getNumAnni())%>
         mesi <%=StringUtils.toStringJSP(fungibilita.getNumMesi())%>
         giorni <%=StringUtils.toStringJSP(fungibilita.getNumGiorni())%>
       </td>
      </tr>
--%>
      <tr>
       <td class="c" colspan="3">
         ATTENZIONE: PERIODO DI PENA ESPIATO IN ECCESSO PARI A
         anni <%=StringUtils.toStringJSP(fungibilita.getNumAnni())%>
         mesi <%=StringUtils.toStringJSP(fungibilita.getNumMesi())%>
         giorni <%=StringUtils.toStringJSP(fungibilita.getNumGiorni())%>
       </td>
      </tr>
  </table>
<%
  }
%>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActUploadComunicazioneLALibero">
            <input type="HIDDEN" name="tipoMisura" value="COMU_LIBERAZIONE_ANTICIPATA">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.sico.libertaanticipata.action.ActDettaglioComunicazioneLALibero">
          </td>
        </tr>
      </table>
</form>
</div>
</body>
</html>