<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sige.statistiche.model.RicercaFogliCompModel" %>
<%@ page import="siap.sige.statistiche.model.EveFasGepSogProvModel"%>
<%@ page import="siap.sige.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="Provvedimenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="FiltoRicerca"  scope="request" class="siap.sige.statistiche.model.RicercaFogliCompModel" />
<jsp:useBean id="TornaQui"      scope="request" class="java.lang.String"/>

<html>
<%
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean debugmode = false; // Flag usato per debug
  
  String lNomeFunzione = "Elenco Ordinanze";
  String lTipoProvvedimento = "Ordinanza";
  String lTestoData = "Data deposito";
  String lTestoStato = "Validato";
  boolean isImpugnazione = false;
  boolean isFoglioComplementare = false;
  
  if(FiltoRicerca.isRicercaXFoglioComplementare())
  {
	  lNomeFunzione = "Elenco Fogli Complementari";
	  lTipoProvvedimento = "Foglio Complementare";
	  lTestoData = "Data emissione";
	  lTestoStato = "Stato";
	  isFoglioComplementare = true;
  }
   
%>

  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti per estremi ordinanza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
 <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
 
 </head>

  <BODY class="corpo">

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo"><%=lNomeFunzione%></font> </td>
     <!-- BOTTONE DI STAMPA  -->
     <td class=l>
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          <jsp:param name="CampoIdEntita" value="N" />
          <jsp:param name="ValoreIdEntita" value="N" />
        </jsp:include>
     </td>
    
    <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
	<td class=l>
	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.statistiche.action.ActStampaElencoProcInExcel"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
	</a></td>
	
     <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>


  <table cellspacing=2 cellpadding=2>
  <% if (debugmode)
	  {%>
        <tr>
        <td class="lNoBord"><font class="cRosso">Modalità Debug </font></td>
      </tr>
 <%} %>
      <tr>
        <td class="lNoBord"><font class="label">Criteri di Ricerca selezionati:</font></td>
      </tr>
      
    <%
    String lCriterio1 = "";
    String lCriterio2 = "";

    if (FiltoRicerca.isTipoIntervalloRicercaXEstremiProvvedimento())
    {
    	String lNumIniziale = FiltoRicerca.getAnnoIniziale().toString() + "/" + FiltoRicerca.getNumIniziale().toString(); 
    	String lNumFinale = FiltoRicerca.getAnnoFinale().toString() + "/" + FiltoRicerca.getNumFinale().toString(); 
    	lCriterio1 = " Dal N. " + lNumIniziale + " al " + lNumFinale;
    }
    else if (FiltoRicerca.isRicercaXDateDeposito())
    {
        String lDataIniziale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataDepositoIniziale(),"dd-MM-yyyy"), "-");
        String lDataFinale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataDepositoFinale(),"dd-MM-yyyy"), "-");
        lCriterio1 = " Data di deposito tra  " + lDataIniziale + " e   " + lDataFinale;
    }
    else if (FiltoRicerca.isRicercaXDateEmissione())
    {
        String lDataIniziale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataEmissioneIniziale(),"dd-MM-yyyy"), "-");
        String lDataFinale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataEmissioneFinale(),"dd-MM-yyyy"), "-");
        lCriterio1 = " Data di emissione tra  " + lDataIniziale + " e   " + lDataFinale;
    }
    
    
    if (FiltoRicerca.getStatoValidazione() != null  )
    {
    	if (FiltoRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.ANNULLATI))
    		lCriterio2 = "Annullato";
    	else if (FiltoRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.VALIDATI))
    		lCriterio2 = "Validato";
    	else if (FiltoRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.TUTTI))
    		lCriterio2 = "Tutti";
    	else if (FiltoRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_VALIDATI))
    		lCriterio2 = "Non Validato";
    	else if (FiltoRicerca.getStatoValidazione().equalsIgnoreCase(ICostantiStatistiche.NON_ANNULLATI))
    		lCriterio2 = "Non Annullato";
    	lCriterio2 = "Stato: " + lCriterio2;
    }
    %>

     <tr>
     	<td class="lVerdeNB"><%=lCriterio1%> </td>
     </tr>
     <tr>
		<td class="lVerdeNB"><%=lCriterio2%> </td>
	 </tr>
     <tr>
 		<td class="lNoBord"><font class="label"> <%=FiltoRicerca.getDescCalcoli() %>
 		</font></td>
 	 </tr>	 
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

 <table cellspacing=2 cellpadding=2 width=95%>
    <tr>
       <td class="int" width=10%>Numero <%=lTipoProvvedimento%></td>
       <td class="int" width=10%>Numero SIGE</td>
       <td class="int" width=20%>Cognome Nome</td>
       <td class="int" width=10%>Data udienza</td>
       <td class="int" width=10%>Data emissione</td>
       <td class="int" width=10%><%=lTestoData%></td>
       <td class="int" width=10% >Esito</td>
       <td class="int" width=10% ><%=lTestoStato %></td>
    </tr>
<%
 
    Iterator itx = Provvedimenti.iterator();

    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
    	EveFasGepSogProvModel provvedimento = (EveFasGepSogProvModel) itx.next();
   	
		// DATI
    	String lNumProv = "";
    	String lIdProv = "";
    	String lDataDeposito = "";
    	String lDataEmissione = "";
       	String lNumFascicolo = "";
    	String lIdFascicolo = "";
    	String lCognome = "-";
    	String lNome = "-";
    	String lIdSoggetto = "-";
    	String lDataDiNascita = "";
    	String lDataUdienza = "";
    	String lIdEve = "";
       	String lEsito = "";
       	String lFlagRegistrato = "";
       	String lIdDocumentoAllegato = "-";
       	// Link al dettaglio del provvedimento
       	String hrefProv = "";
  	
		// Dati dal provvedimemnto
    	if (provvedimento.getProvvedimentoSige() != null)
    	{
    		lIdProv = StringUtils.toStringJSP(provvedimento.getProvvedimentoSige().getIdProvvedimentoSige(), "-");
    		lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getProvvedimentoSige().getDataDeposito(),"dd-MM-yyyy"), "-");
    		if (provvedimento.getProvvedimentoSige().getChiaveAnno() != null && provvedimento.getProvvedimentoSige().getChiaveProgr() != null)
    		{
        		lNumProv = provvedimento.getProvvedimentoSige().getChiaveAnno().toString() + "/" + provvedimento.getProvvedimentoSige().getChiaveProgr().toString();
    			hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff";
    			hrefProv += "&" + ICostantiProvvedimentoSige.CAMPO_CHIAVE_ANNO + "=" + provvedimento.getProvvedimentoSige().getChiaveAnno();
    			hrefProv += "&" + ICostantiProvvedimentoSige.CAMPO_CHIAVE_PROGR + "=" + provvedimento.getProvvedimentoSige().getChiaveProgr();
     			hrefProv += "&TornaQui=" + TornaQui;
    		}
    	}
    	else if (isFoglioComplementare && provvedimento.getDocumentoAllegato() != null)
    	{
       		lIdProv = StringUtils.toStringJSP(provvedimento.getDocumentoAllegato().getIdDocumentoAllegato(), "-");
    		lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDocumentoAllegato().getDataEmissione(),"dd-MM-yyyy"), "-");
    		if (provvedimento.getDocumentoAllegato().getAnnoFoglioComplementare() != null && provvedimento.getDocumentoAllegato().getProgrFoglioComplementare() != null)
    		{	
    			lNumProv = provvedimento.getDocumentoAllegato().getAnnoFoglioComplementare().toString() + "/" + provvedimento.getDocumentoAllegato().getProgrFoglioComplementare();
      			hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.documentoallegato.action.ActRicercaFoglioComplementareByAnnoNumUff";
    			hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3 + "=" + provvedimento.getDocumentoAllegato().getAnnoFoglioComplementare();
    			hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3 + "=" + provvedimento.getDocumentoAllegato().getProgrFoglioComplementare();
    			hrefProv += "&TornaQui=" + TornaQui;
    		}
    		
    	}	
    	
		//  Dati dal Fascicolo Sige
    	if (provvedimento.getFascicoloSige() != null)
    	{
     		lIdFascicolo = StringUtils.toStringJSP(provvedimento.getFascicoloSige().getIdFascicoloSige(), "-");
    		if(provvedimento.getFascicoloSige().getChiaveAnno() != null && provvedimento.getFascicoloSige().getChiaveProgr()!= null)
     			lNumFascicolo = provvedimento.getFascicoloSige().getChiaveAnno().toString() + "/" + provvedimento.getFascicoloSige().getChiaveProgr().toString();
    	}
		// Dati dal Soggetto
    	if (provvedimento.getSoggetto() != null)
    	{
    		lCognome = StringUtils.toStringJSP(provvedimento.getSoggetto().getCognome(), "-");
    		lNome = StringUtils.toStringJSP(provvedimento.getSoggetto().getNome(), "-");
    		lIdSoggetto = StringUtils.toStringJSP(provvedimento.getSoggetto().getIdSoggetto(), "-");
    		lDataDiNascita = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getSoggetto().getDataNascita(),"dd-MM-yyyy"), "-");
    	}
		// Dati dal Generale Procedimento
     	if (provvedimento.getUdienzaSige() != null)
    	{
    		lDataUdienza = StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getUdienzaSige().getDataUdienza(),"dd-MM-yyyy"), "-");
    	}   	
		// Dati dell'Evento
     	if (provvedimento.getEvento()!= null)
    	{
     		lIdEve = StringUtils.toStringJSP(provvedimento.getEvento().getIdEvento(), "-");
    		lEsito = provvedimento.getEvento().getDescrEsito();
    		lDataEmissione = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(),"dd-MM-yyyy"), "-");
			if(isFoglioComplementare)
    		{
    			if (provvedimento.getDocumentoAllegato()!= null && provvedimento.getDocumentoAllegato().getFlagDocumentoRegistrato() != null)
    				lFlagRegistrato = provvedimento.getDocumentoAllegato().getFlagDocumentoRegistrato();
    		}
      		else if (provvedimento.getEvento().getFlagDocumentoRegistrato() != null)
    			lFlagRegistrato = provvedimento.getEvento().getFlagDocumentoRegistrato();
     	}   	
		// Dati del documento allegato
		if (provvedimento.getDocumentoAllegato() != null)
		{
			lIdDocumentoAllegato = StringUtils.toStringJSP(provvedimento.getDocumentoAllegato().getIdDocumentoAllegato(), "-");
		}
 	
		
    	%>
      <tr>
        <td class="c">
        	<font class="label">
        		<%-- MERGE v10: modificato commento --%>
             	<%-- <a class="cliccabile" href="<%//=hrefProv%>"> --%>
          		<%=lNumProv%><!--</a>-->
          	</font>
        </td>
       	<td class="c"><font class="label">
      	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lIdFascicolo%><%=retParam%>">
      	<%=lNumFascicolo%></a></font></td>
      	
       	<td class="c"><font class="label">
       	  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lIdSoggetto%>&TornaQui=<%=TornaQui%>">
         	<%=StringUtils.toStringJSP( lCognome, "-" )+ " " + StringUtils.toStringJSP(lNome, "-")%></font></a></td>
           <td class="c"><font class="label"><%=lDataUdienza%></font></td>
           <td class="c"><font class="label"><%=lDataEmissione%></font></td>	
           <td class="c"><font class="label"><%=lDataDeposito%></font></td>	
           <td class="c"><font class="label"><%=lEsito %></font></td>	
       	<td class="c">
      <%  	if( debugmode )
      {
    	  %>
    	  <font class="cRosso">
    	  id-Provv: <%=lIdProv%>
      	  id-Fasc: <%=lIdFascicolo%>
      	  id-Sogg: <%=lIdSoggetto%>
      	  id-Eve: <%=lIdEve%>
      	  id-DocAll: <%=lIdDocumentoAllegato%>
       	</font>
      	<br>
      <%
      }
      if(lFlagRegistrato.compareTo("A")==0)
      {
%>
      <font class="cRosso">ANNULLATO</font>
<%
      }
      else if ( !isImpugnazione && provvedimento.isDepositoValidato())
      {
      %>
      	<img src="/images/TickRed.gif">
 <%
  	  }
      else {
%>
		<font class="label">&nbsp;</font>
<%    	  
      }
       	  
    } // ENDWHILE
%>     
      </td>
      </tr>

    </table>
  <br>
  </body>
</html>