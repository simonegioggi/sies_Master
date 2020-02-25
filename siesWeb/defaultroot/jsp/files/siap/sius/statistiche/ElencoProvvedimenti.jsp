<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.depositosentenza.action.ICostantiDepositoSentenza"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sius.esperto.model.EspertoModel" %>
<%@ page import="siap.sius.util.SIUSLookupRemote" %>
<%@ page import="siap.siep.notifica.model.NotificaFasSiusEveModel" %>
<%@ page import="siap.sius.statistiche.model.RicercaProvvedimentoModel" %>
<%@ page import="siap.sius.statistiche.model.EveFasGepSogProvModel"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>

<jsp:useBean id="Provvedimenti" scope="request" class="java.util.Vector" />
<jsp:useBean id="FiltoRicerca" scope="request" class="siap.sius.statistiche.model.RicercaProvvedimentoModel" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<html>
<%
   // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean debugmode = false; // Flag usato per debug
  
  String lNomeFunzione = "Elenco Provvedimenti";
  String lTipoProvvedimento = "Provvedimento";
  String lTestoData = "Data deposito";
  String lTestoStato = "Validato";
  boolean isImpugnazione = false;
  boolean isFoglioComplementare = false;
  
  if (FiltoRicerca.isRicercaXDecreto())
  {
    lNomeFunzione = "Elenco Decreti";
    lTipoProvvedimento = "Decreto";
  }
  else if(FiltoRicerca.isRicercaXImpugnazioneRicorso())
  {
    lNomeFunzione = "Elenco Ricorsi/Impugnazioni";
    lTipoProvvedimento = "Ricorso/Impugnazione";
    lTestoData = "Data arrivo in cancelleria";
    lTestoStato = "Stato";
    isImpugnazione = true;
  }
  else if(FiltoRicerca.isRicercaXFoglioComplementare())
  {
    lNomeFunzione = "Elenco Fogli Complementari";
    lTipoProvvedimento = "Foglio Complementare";
    lTestoData = "Data emissione";
    lTestoStato = "Stato";
    isFoglioComplementare = true;
  }
  else if(FiltoRicerca.isRicercaXSentenza())
  {
    lNomeFunzione = "Elenco Sentenze";
    lTipoProvvedimento = "Sentenza";
    isFoglioComplementare = true;
  }
   
%>

  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimenti per estremi Provvedimento</title>
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
  <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.statistiche.action.ActStampaElencoProcInExcel"><img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
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
    String lCriterio3 = "";
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
    else if (FiltoRicerca.isRicercaXDateArrivoCancelleria())
    {
        String lDataIniziale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataArrivoInCancelleriaIniziale(),"dd-MM-yyyy"), "-");
        String lDataFinale = " " + StringUtils.toStringJSP(DateUtils.getDateToString(FiltoRicerca.getDataArrivoInCancelleriaFinale(),"dd-MM-yyyy"), "-");
        lCriterio1 = " Data di arrivo in cancelleria tra  " + lDataIniziale + " e   " + lDataFinale;
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
    <% if(FiltoRicerca.getCodMagistrato().compareToIgnoreCase("Tutti") == 0) 
  { 
  %>
     <tr>
  <td class="lVerdeNB"> Tutti i procedimenti con magistrato assegnato
  </td>
  </tr>
    <%} else  
      if(FiltoRicerca.getCodMagistrato().compareToIgnoreCase("Nessuno") == 0) 
    { 
      %>
         <tr>
      <td class="lVerdeNB"> Tutti i procedimenti privi di Magistrato Assegnato
      </td>
      </tr>
        <%} 
      else  
          if((FiltoRicerca.getCodMagistrato().length() > 0) && (FiltoRicerca.getCodMagistrato().compareToIgnoreCase("-") != 0)) 
        {  
            String lMagistrato = " ";
            lMagistrato = FiltoRicerca.getDescMagistrato();
          %>
             <tr>
          <td class="lVerdeNB"> Magistrato: <%=lMagistrato%>
          </td>
          </tr>
            <%}
          else  
              if((FiltoRicerca.getCodEsperto() != null) && (FiltoRicerca.getCodEsperto().intValue() != 9999)) 
            { 
                String lEsperto = " ";
                lEsperto = FiltoRicerca.getDescEsperto();
              %>
                 <tr>
              <td class="lVerdeNB"> <%=lEsperto%>
              </td>
              </tr>
                <%} 
              else
                if ((FiltoRicerca.getCodEsperto() != null) && (FiltoRicerca.getCodEsperto().intValue() == 9999))
                { 
                    %>
                    <tr>
                    <td class="lVerdeNB"> Tutti i procedimenti con esperto assegnato
                    </td>
                    </tr>
                    <%  
                }

      %>
     <tr>
      <td class="lVerdeNB"><%=lCriterio1%> </td>
      
     </tr>
     <tr>
  <td class="lVerdeNB"><%=lCriterio2%> </td>
  </tr>
<% if(FiltoRicerca.isRicercaXImpugnazioneRicorso()) 
  {
  lCriterio3 = "Tipo di Ricorso/Impugnazione: " + FiltoRicerca.getDescTipoImpugnazione();
  %>
     <tr>
  <td class="lVerdeNB"> <%=lCriterio3%>
  </td>
  </tr>
<%	} %>

<% 
  if( !FiltoRicerca.getTipiControlliEsecuzione().equals("-") ) {
	  String lCriterio4 = new String();
   if(FiltoRicerca.getTipiControlliEsecuzione().length > 1 && 
		  FiltoRicerca.getTipiControlliEsecuzione()[0] != null && FiltoRicerca.getTipiControlliEsecuzione()[1] != null) {
	   		lCriterio4 = "Provvedimenti con Controllo tramite mezzi elettronici e Controllo tramite altri strumenti tecnici ";
   } else if(FiltoRicerca.getTipiControlliEsecuzione().length == 1) {
	   		if( FiltoRicerca.getTipiControlliEsecuzione()[0].equals("E") )
	   			lCriterio4 = "Solo Provvedimenti con Controllo tramite mezzi elettronici.";
	   		else if( FiltoRicerca.getTipiControlliEsecuzione()[0].equals("T") )
	   			lCriterio4 = "Solo Provvedimenti con Controllo tramite altri strumenti tecnici.";
   }
%>
	<tr>
		<td class="lVerdeNB"><%=lCriterio4%></td>
	</tr>
<%}%>



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
      <td class="int" width=10%>Numero SIUS</td>
      <td class="int" width=20%>Cognome Nome</td>
      <td class="int" width=10%>Data udienza</td>
      <td class="int" width=10%>Data emissione</td>
      <td class="int" width=10%><%=lTestoData%></td>
      <td class="int" width=10%>Oggetto</td>
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
      String lOggetto = "";
      String lEsito = "";
      String lFlagRegistrato = "";
      String lIdDocumentoAllegato = "-";
      // Link al dettaglio del provvedimento
      String hrefProv = "";
    
      //   Dati dal provvedimemnto
      if (provvedimento.getDepositoOrdinanzaPc() != null)
      {
        lIdProv = StringUtils.toStringJSP(provvedimento.getDepositoOrdinanzaPc().getIdDepositoOrdinanzaPc(), "-");
        lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDepositoOrdinanzaPc().getDataDeposito(),"dd-MM-yyyy"), "-");
        if (provvedimento.getDepositoOrdinanzaPc().getAnnoS3() != null && provvedimento.getDepositoOrdinanzaPc().getNumS3() != null)
        {
          lNumProv = provvedimento.getDepositoOrdinanzaPc().getAnnoS3().toString() + "/" + provvedimento.getDepositoOrdinanzaPc().getNumS3().toString();
          hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositoordinanzapc.action.ActRicercaDepositoOrdinanzaPcByAnnoNumUff";
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3 + "=" + provvedimento.getDepositoOrdinanzaPc().getAnnoS3();
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3 + "=" + provvedimento.getDepositoOrdinanzaPc().getNumS3();
          hrefProv += "&TornaQui=" + TornaQui;
        }
      }
      else if (provvedimento.getDepositoSentenza() != null)
      {
        lIdProv = StringUtils.toStringJSP(provvedimento.getDepositoSentenza().getIdDepositoSentenza(), "-");
        lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDepositoSentenza().getDataDeposito(),"dd-MM-yyyy"), "-");
        if (provvedimento.getDepositoSentenza().getAnnoSentenza() != null && provvedimento.getDepositoSentenza().getNumSentenza()!= null)
        {
          lNumProv = provvedimento.getDepositoSentenza().getAnnoSentenza().toString() + "/" + provvedimento.getDepositoSentenza().getNumSentenza();
          hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositosentenza.action.ActRicercaDepositoSentenzaByAnnoNumUff";
          hrefProv += "&" + ICostantiDepositoSentenza.CAMPO_ANNO + "=" + provvedimento.getDepositoSentenza().getAnnoSentenza();
          hrefProv += "&" + ICostantiDepositoSentenza.CAMPO_NUM + "=" + provvedimento.getDepositoSentenza().getNumSentenza();
          hrefProv += "&TornaQui=" + TornaQui;
        }
      }
      else if (provvedimento.getDepositoDecreto() != null)
      {
        lIdProv = StringUtils.toStringJSP(provvedimento.getDepositoDecreto().getIdDepositoDecreto(), "-");
        lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDepositoDecreto().getDataDeposito(),"dd-MM-yyyy"), "-");
        if (provvedimento.getDepositoDecreto().getAnnoS72() != null && provvedimento.getDepositoDecreto().getNumS72()!= null)
        {
          lNumProv = provvedimento.getDepositoDecreto().getAnnoS72().toString() + "/" + provvedimento.getDepositoDecreto().getNumS72();
          hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositodecreto.action.ActRicercaDepositoDecretoByAnnoNumUff";
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3 + "=" + provvedimento.getDepositoDecreto().getAnnoS72();
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3 + "=" + provvedimento.getDepositoDecreto().getNumS72();
          hrefProv += "&TornaQui=" + TornaQui;
        }
      }
      else if (provvedimento.getImpugnazione() != null)
      {
        lIdProv = StringUtils.toStringJSP(provvedimento.getImpugnazione().getIdImpugnazione(), "-");
        lDataDeposito = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getImpugnazione().getDataArrivoCancelleria(),"dd-MM-yyyy"), "-");
        if (provvedimento.getImpugnazione().getAnnoS7() != null && provvedimento.getImpugnazione().getProgrS7()!= null )
        {
          lNumProv = provvedimento.getImpugnazione().getAnnoS7().toString() + "/" + provvedimento.getImpugnazione().getProgrS7();
          hrefProv = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.statistiche.action.ActRicercaImpugnazioneByAnnoNumUff";
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_S3 + "=" + provvedimento.getImpugnazione().getAnnoS7();
          hrefProv += "&" + ICostantiDepositoOrdinanzaPc.CAMPO_NUM_S3 + "=" + provvedimento.getImpugnazione().getProgrS7();
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
      
      
      //  Dati dal Fascicolo Sius
      if (provvedimento.getFascicoloSius() != null)
      {
        lIdFascicolo = StringUtils.toStringJSP(provvedimento.getFascicoloSius().getIdFascicoloSius(), "-");
        if(provvedimento.getFascicoloSius().getChiaveAnno() != null && provvedimento.getFascicoloSius().getChiaveProgr()!= null)
          lNumFascicolo = provvedimento.getFascicoloSius().getChiaveAnno().toString() + "/" + provvedimento.getFascicoloSius().getChiaveProgr().toString();
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
      if (provvedimento.getGeneraleProcedimento()!= null)
      {
        lDataUdienza = StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getGeneraleProcedimento().getDataCameraConsiglio(),"dd-MM-yyyy"), "-");
      }  
         
      // Dati dell'Evento
      if (provvedimento.getEvento()!= null)
      {
        lIdEve = StringUtils.toStringJSP(provvedimento.getEvento().getIdEvento(), "-");
        lOggetto = provvedimento.getEvento().getDescrMotivo();
        lEsito = provvedimento.getEvento().getDescrEsito();
        lDataEmissione = StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getEvento().getDataEmissione(),"dd-MM-yyyy"), "-");
        if (provvedimento.getImpugnazione() != null)
        {
          // Nell'impugnazione l'annullamento non è nell'evento
          if (provvedimento.getImpugnazione().getFlagAnnullamento() != null && provvedimento.getImpugnazione().getFlagAnnullamento().equalsIgnoreCase("S"))
            lFlagRegistrato = "A";
        }
        else if(isFoglioComplementare)
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
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=hrefProv%>">
          <%=lNumProv%></a></font>
        </td>
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=lIdFascicolo%><%=retParam%>">
          <%=lNumFascicolo%></a></font></td>
        
        <td class="c"><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lIdSoggetto%>&TornaQui=<%=TornaQui%>">
          <%=StringUtils.toStringJSP( lCognome, "-" )+ " " + StringUtils.toStringJSP(lNome, "-")%></font></a></td>
        <td class="c"><font class="label"><%=lDataUdienza%></font></td>
        <td class="c"><font class="label"><%=lDataEmissione%></font></td>  
        <td class="c"><font class="label"><%=lDataDeposito%></font></td> 
        <td class="c"><font class="label"><%=lOggetto %></font></td> 
        <td class="c"><font class="label"><%=lEsito %></font></td>  
        <td class="c">
          <% if( debugmode ) { %>
            <font class="cRosso">
              id-Provv: <%=lIdProv%>
              id-Fasc: <%=lIdFascicolo%>
              id-Sogg: <%=lIdSoggetto%>
              id-Eve: <%=lIdEve%>
              id-DocAll: <%=lIdDocumentoAllegato%>
            </font>
            <br>
          <% } %>
        
          <% if(lFlagRegistrato.compareTo("A")==0) { %>
          <font class="cRosso">ANNULLATO</font>
          <% } else if ( !isImpugnazione && provvedimento.isDepositoValidato()) { %>
          <img src="/images/TickRed.gif">
          <% } else { %>
          &nbsp;
          <% } %>
        </td>
      </tr>
     <%
    } // ENDWHILE
%>     
      


    </table>
  <br>
  </body>
</html>