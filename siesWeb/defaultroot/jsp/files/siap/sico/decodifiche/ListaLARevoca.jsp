<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.log.LogF3B" %>


<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.util.DateUtils" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" %>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata" %>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel" %>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel" %>


<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>

<% // Vector <LicenzaPeriodiLibAnticipataModel>  licenze%>
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="licenze" scope="request" class="java.util.Vector" />


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Liberazioni Anticipate</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

    //=============================================================
    //
    //=============================================================
    function insertIT(id, 
                      codTipoProvvedimento,
                      id_lic, elaborato, conProvvValidato,
                      annoSius,
                      numeroSius,                                         
                      descrUfficioEmittente,
                      comuneUfficioEmittente,
                      g_DataEmissione,
                      m_DataEmissione,
                      a_DataEmissione,
                      numGiorniReclusione,
                      codTipoDecisione,
                      annoOrdinanza,
                      numeroOrdinanza,
                      giorniLA,
                      aPeriodi,
                      giorniLA_spe,
                      aPeriodi_spe,
                      giorniLA_int,
                      aPeriodi_int)
    {
      // Prima di caricare i dati in maschera provvedo a cancellare tutti i campi
      // onde evitare residui di dati sporchi
      window.parent.opener.resetDati();
    
      // Caricamentio dati Provvedimento SIUS
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS%>.value=annoSius;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS%>.value=numeroSius;
        
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE%>_AA.value=comuneUfficioEmittente;

      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>_AA.value=g_DataEmissione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>_AA.value=m_DataEmissione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>_AA.value=a_DataEmissione;
              
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>_AA.value=annoOrdinanza;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>_AA.value=numeroOrdinanza;

      // Id evento SIUS e id LicenzaLibAnt (???)
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value=id;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>.value=id_lic;
      
      // Tipo Ufficio (TDS/UDS)   
      for(var k=0;k<window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options.length;k++)
      {
        if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options[k].text==descrUfficioEmittente)
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE%>_AA.options[k].selected=true;
          break;
        }
      }

      // Tipo Provvedimento (Decret/Ordinanza)
      for(var k=0;k< window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.options.length;k++)
      {
        if(window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.options[k].value==codTipoProvvedimento)
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>_AA.options[k].selected=true;
          break;
        }
      }
      
      // Nascondo la sezione per i dati compilati manualmente
      window.parent.opener.document.getElementById("partecomune").style.display = 'none';
      window.parent.opener.document.getElementById("datiPeriodiLA").style.display = 'none';
      window.parent.opener.document.getElementById("datiPeriodiSPE").style.display = 'none';
      window.parent.opener.document.getElementById("datiPeriodiINT").style.display = 'none';
      
      // Nascondo le DIV, vanno visualizzate solo quelle contenenti dati
      window.parent.opener.document.getElementById("datiSorvLA").style.display = 'none';
      window.parent.opener.document.getElementById("datiSorvLA_SPE").style.display = 'none';
      window.parent.opener.document.getElementById("datiSorvLA_INT").style.display = 'none';

      // Visualizzo sezione dati caricati da questa popup
      //========================================
      // Carico Giorni e periodi L.A. Ordinaria
      //========================================
      if (giorniLA>0)
      {
        window.parent.opener.document.getElementById("datiSorvLA").style.display = 'block';
  
        window.parent.opener.document.<%=request.getParameter("formname")%>.giorniLA.value=giorniLA;
        window.parent.opener.document.<%=request.getParameter("formname")%>.NumGiorniLibanticipataSorv.value=giorniLA;
  
        appoPeriodi=aPeriodi;
        for(var k=0;k<6;k++)
        {       
          window.parent.opener.document.getElementById("InizioLA"+k).value='';
          window.parent.opener.document.getElementById("FineLA"+k).value='';
          appoPeriodi=appoPeriodi.substr(aPeriodi.indexOf(';')+1);
        }
  
        for(var k=0;k<6;k++)
        {       
          if (aPeriodi.indexOf('*')<0) break;
          window.parent.opener.document.getElementById("InizioLA"+k).value=aPeriodi.substr(0,10);
          window.parent.opener.document.getElementById("FineLA"+k).value=aPeriodi.substr(11,10);
          aPeriodi=aPeriodi.substr(aPeriodi.indexOf(';')+1);
        }
      }

      //=================================
      // Giorni e periodi L.A. SPECIALE 
      //=================================
      if (giorniLA_spe>0) {
        window.parent.opener.document.getElementById("datiSorvLA_SPE").style.display = 'block';

        window.parent.opener.document.<%=request.getParameter("formname")%>.giorniLA_SPE.value=giorniLA_spe;
        window.parent.opener.document.<%=request.getParameter("formname")%>.NumGiorniLibanticipataSorv_SPE.value=giorniLA_spe;
  
        appoPeriodi_spe = aPeriodi_spe;
        for(var k=0;k<6;k++)
        {       
          window.parent.opener.document.getElementById("InizioLA_SPE"+k).value='';
          window.parent.opener.document.getElementById("FineLA_SPE"+k).value='';
          appoPeriodi_spe=appoPeriodi_spe.substr(aPeriodi_spe.indexOf(';')+1);
        }
  
        for(var k=0;k<6;k++)
        {       
          if (aPeriodi_spe.indexOf('*')<0)
            break;
          window.parent.opener.document.getElementById("InizioLA_SPE"+k).value=aPeriodi_spe.substr(0,10);
          window.parent.opener.document.getElementById("FineLA_SPE"+k).value=aPeriodi_spe.substr(11,10);
          aPeriodi_spe=aPeriodi_spe.substr(aPeriodi_spe.indexOf(';')+1);
        }
      }

      //=======================================
      // Giorni e periodi L.A. INTEGRAZIONE 
      //=======================================
      if (giorniLA_int>0)
      {
        window.parent.opener.document.getElementById("datiSorvLA_INT").style.display = 'block';
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.giorniLA_INT.value=giorniLA_int;
        window.parent.opener.document.<%=request.getParameter("formname")%>.NumGiorniLibanticipataSorv_INT.value=giorniLA_int;
  
        appoPeriodi_int = aPeriodi_int;
        for(var k=0;k<6;k++)
        {       
          window.parent.opener.document.getElementById("InizioLA_INT"+k).value='';
          window.parent.opener.document.getElementById("FineLA_INT"+k).value='';
          appoPeriodi_int = appoPeriodi_int.substr(aPeriodi_int.indexOf(';')+1);
        }
  
        for(var k=0;k<6;k++)
        {       
          if (aPeriodi_int.indexOf('*')<0)
            break;
          window.parent.opener.document.getElementById("InizioLA_INT"+k).value = aPeriodi_int.substr(0,10);
          window.parent.opener.document.getElementById("FineLA_INT"+k).value = aPeriodi_int.substr(11,10);
          aPeriodi_int = aPeriodi_int.substr(aPeriodi_int.indexOf(';')+1);
        }
      }
      
      window.parent.close();

    }
      


    function controlla()
    {
      this.focus();
      if(document.elenco.numeroLiberazioni.value==0)
      {
        alert("Nessuna Liberazione Anticipata Presente");
        window.parent.close();
      }
    }

    </script>

  </head>

<body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <input type="hidden" name="numeroLiberazioni" value="<%=licenze.size()%>">
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Liberazioni Anticipate Revocate</font>
        </td>
      </tr>
    </table>
    <br>
    <table>
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Anno Sius</td>
        <td class="int">Numero Sius</td>
        <td class="int">Autorità Emittente</td>
        <td class="int">Data Emissione Ordinanza</td>
<!--
        <td class="int">Giorni concessi</td>
-->
        <td class="int" width=5%>Azioni</td>
      </tr>

<%
  if(licenze.size()>0)
  {
    BigDecimal lIdlicenza = ((LicenzaPeriodiLibAnticipataModel)licenze.get(0)).getLicenza().getIdLicenzaLibanticipata();
    String strperiodiTot  = new String();
    String strperiodi     = new String();
    String Appostrperiodi = new String();
    int TotNumeroGiorni =0;
    // -- > DL 146
    String strperiodiTot_spe =new String();
    String strperiodiTot_int =new String();
    String strperiodi_spe =new String();
    String strperiodi_int =new String();
    String Appostrperiodi_spe =new String();
    String Appostrperiodi_int =new String();
    int TotNumeroGiorni_spe =0;
    int TotNumeroGiorni_int =0;
//  < --  

    //==========================================================================
    // Ciclo sulle licenze.
    // n.b. va riportato un rigo per ogni Evento che può contenere anche più
    //      record LicenzaLibAnticipata
    //==========================================================================
    Iterator itx = licenze.iterator();
    LicenzaLibAnticipataModel AppolicenzeMod = null; 
    for (int i = 0; itx.hasNext(); i++)
    {
      //LicenzaPeriodiLibAnticipataModel licenzeMod = (LicenzaLibAnticipataModel)itx.next();
      LicenzaPeriodiLibAnticipataModel licPerMod = (LicenzaPeriodiLibAnticipataModel)itx.next();
      
      LicenzaLibAnticipataModel licenzeMod = licPerMod.getLicenza();
      EventoModel lEventoModel = licPerMod.getEvento();
      
      PeriodoLibAnticipataModel[] lPeriodiMod = (PeriodoLibAnticipataModel[])licPerMod.getPeriodi();
            
      // Confronto per evitare ripetizioni licenze appartenenti alla stessa ordinanza
      boolean lRipetizione = false;
      for(int j = 0; j < i && !lRipetizione; j++)
      {
        //LicenzaLibAnticipataModel licVecchia = (LicenzaLibAnticipataModel)licenze.g.get(j);
        LicenzaPeriodiLibAnticipataModel licVecchia = (LicenzaPeriodiLibAnticipataModel)licenze.get(j);
        if (licVecchia.getLicenza().getEveIdEvento() != null && licenzeMod.getEveIdEvento() != null && licVecchia.getLicenza().getEveIdEvento().equals(licenzeMod.getEveIdEvento()))
          lRipetizione = true;
      }
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        // // siesLogger.debug(" ");


//  -- >  DL 146

    //==========================================================================
    // Costruisce le stringhe concatenate riportanti i periodi nel formato 
    // [dataInizio*dataFine;dataInizio*dataFine;...]
    // da passare alla InsertIt
    //==========================================================================
    strperiodi= "";
    strperiodi_spe= "";
    strperiodi_int= "";    
    if(licenzeMod.getDescrStatoPermesso() != null)
    {
      if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LS"))
      {
        for (int ii = 0; ii <lPeriodiMod.length; ii++)
        {
          strperiodi_spe+=( DateUtils.getDateToString(lPeriodiMod[ii].getDataInizio(), "dd-MM-yyyy")
                           +"*"
                           +DateUtils.getDateToString(lPeriodiMod[ii].getDataFine(), "dd-MM-yyyy")
                           +";");

        }
      }
      else if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LI"))
      {
        for (int ii = 0; ii <lPeriodiMod.length; ii++)
        {
          strperiodi_int+=( DateUtils.getDateToString(lPeriodiMod[ii].getDataInizio(), "dd-MM-yyyy")
                           +"*"
                           +DateUtils.getDateToString(lPeriodiMod[ii].getDataFine(), "dd-MM-yyyy")
                           +";");
        }
      }
      else  
      { 
        for (int ii = 0; ii <lPeriodiMod.length; ii++)
        {
          strperiodi+=( DateUtils.getDateToString(lPeriodiMod[ii].getDataInizio(), "dd-MM-yyyy")
                       +"*"
                       + DateUtils.getDateToString(lPeriodiMod[ii].getDataFine(), "dd-MM-yyyy")
                       +";");
        }
      }
    }
    else
    { 
      for (int ii = 0; ii <lPeriodiMod.length; ii++)
      {
          strperiodi+=( DateUtils.getDateToString(lPeriodiMod[ii].getDataInizio(), "dd-MM-yyyy")
                       +"*"
                       +DateUtils.getDateToString(lPeriodiMod[ii].getDataFine(), "dd-MM-yyyy")
                       +";");
        }
    }     



    //==========================================================================
    //
    //==========================================================================
    if (!lRipetizione )
    {
      if (AppolicenzeMod != null)
      { 
        strperiodiTot="";
        strperiodiTot_spe="";
        strperiodiTot_int="";
        
%>
        <tr>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getDescrTipoLicenza(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(AppolicenzeMod.getDescrLuogoEmittente(),"-")%></td>
          <td class="l" style="text-align:center"><%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"dd-MM-yyyy"),"-")%></td>

          <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" value="<%=AppolicenzeMod.getIdLicenzaLibanticipata()%>">
          <input type="HIDDEN" name="FlagElaborato" value="<%=StringUtils.toStringJSP(AppolicenzeMod.getFlagElaborato())%>">

          <td class="c">
            <a href="Javascript:insertIT(<%=AppolicenzeMod.getEveIdEvento()%>,
                                         '<%=lEventoModel.getCodTipoProvvedimento()%>',
                                         <%=AppolicenzeMod.getIdLicenzaLibanticipata()%>,
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getFlagElaborato())%>', 
                                        '<%=AppolicenzeMod.isConProvvedimentoValidato()%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoSius(),"")%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroSius(),"")%>',
                                        '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(AppolicenzeMod.getDescrUfficioEmittente() ),"-")%>',
                                        '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(AppolicenzeMod.getDescrLuogoEmittente() ),"-")%>',
                                        '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"dd"),"")%>',
                                        '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"MM"),"")%>',
                                        '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"yyyy"),"")%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroGiorni(),"")%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getCodTipoLicenza(),"-")%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoOrdinanza(),"")%>',
                                        '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroOrdinanza(),"")%>',
                                        '<%=StringUtils.toStringJSP(TotNumeroGiorni,"0")%>',
                                        '<%=StringUtils.toStringJSP(Appostrperiodi,"")%>',
                                        '<%=StringUtils.toStringJSP(TotNumeroGiorni_spe,"0")%>',
                                        '<%=StringUtils.toStringJSP(Appostrperiodi_spe,"")%>',
                                        '<%=StringUtils.toStringJSP(TotNumeroGiorni_int,"0")%>',
                                        '<%=StringUtils.toStringJSP(Appostrperiodi_int,"")%>' );">
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
<%
            TotNumeroGiorni=0;
            TotNumeroGiorni_spe =0;
            TotNumeroGiorni_int =0;
            if( "S".equals(AppolicenzeMod.getFlagElaborato())
                || AppolicenzeMod.isConProvvedimentoValidato()
               )
            {
%>
              <!--font class="cRosso"> Elaborato </font-->
<%
            }
%>
          </td>
        </tr>
<%
      Appostrperiodi="";
      Appostrperiodi_spe="";
      Appostrperiodi_int="";
      }
      AppolicenzeMod = licenzeMod;
      }else {
        
        // strperiodi= "";// endif ripetizione
      }
    
    if (i == licenze.size()-1)
    {
      if (AppolicenzeMod != null)
      { 
        if (i == licenze.size()-1)
        {
          Appostrperiodi+=strperiodi;
          Appostrperiodi_spe += strperiodi_spe;
          Appostrperiodi_int += strperiodi_int;
        }
        if (i == licenze.size()-1) 
        {
          if(licenzeMod.getDescrStatoPermesso() != null)
          {
            if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LS"))  
            {
              TotNumeroGiorni_spe +=licenzeMod.getNumeroGiorni().intValue();
            }
            else if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LI"))
            { 
              TotNumeroGiorni_int +=licenzeMod.getNumeroGiorni().intValue();
            }
            else
            { 
              TotNumeroGiorni +=licenzeMod.getNumeroGiorni().intValue();
            } 
          }
          else
          { 
            TotNumeroGiorni +=licenzeMod.getNumeroGiorni().intValue();
        } 
              
        }
        
        strperiodiTot ="";
        strperiodiTot_spe ="";
        strperiodiTot_int ="";
%>
        <tr>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getDescrTipoLicenza(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroSius(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(AppolicenzeMod.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(AppolicenzeMod.getDescrLuogoEmittente(),"-")%></td>
          <td class="l" style="text-align:center"><%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"dd-MM-yyyy"),"-")%></td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
          <td class="l">< %=StringUtils.toStringJSP(AppolicenzeMod.getNumeroGiorni(),"-")%></td>
--%>
          <input type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA%>" value="<%=AppolicenzeMod.getIdLicenzaLibanticipata()%>">
          <input type="HIDDEN" name="FlagElaborato" value="<%=StringUtils.toStringJSP(AppolicenzeMod.getFlagElaborato())%>">

          <td class="c">
            <a href="Javascript:insertIT(<%=AppolicenzeMod.getEveIdEvento()%>,
                  '<%=lEventoModel.getCodTipoProvvedimento()%>',
                  <%=AppolicenzeMod.getIdLicenzaLibanticipata()%>,
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getFlagElaborato())%>', 
                 '<%=AppolicenzeMod.isConProvvedimentoValidato()%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoSius(),"")%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroSius(),"")%>',                                         
                 '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(AppolicenzeMod.getDescrUfficioEmittente() ),"-")%>',
                 '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(AppolicenzeMod.getDescrLuogoEmittente() ),"-")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"dd"),"")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"MM"),"")%>',
                 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(AppolicenzeMod.getDataEmissioneOrdinanza(),"yyyy"),"")%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroGiorni(),"")%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getCodTipoLicenza(),"-")%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getAnnoOrdinanza(),"")%>',
                 '<%=StringUtils.toStringJSP(AppolicenzeMod.getNumeroOrdinanza(),"")%>',
                 '<%=StringUtils.toStringJSP(TotNumeroGiorni,"0")%>',
                 '<%=StringUtils.toStringJSP(Appostrperiodi,"")%>',
                 '<%=StringUtils.toStringJSP(TotNumeroGiorni_spe,"0")%>',
                 '<%=StringUtils.toStringJSP(Appostrperiodi_spe,"")%>',
                 '<%=StringUtils.toStringJSP(TotNumeroGiorni_int,"0")%>',
                 '<%=StringUtils.toStringJSP(Appostrperiodi_int,"")%>' );">                 
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
<%
            TotNumeroGiorni =0;
            TotNumeroGiorni_spe =0;
            TotNumeroGiorni_int =0;
            if( "S".equals(AppolicenzeMod.getFlagElaborato())
                || AppolicenzeMod.isConProvvedimentoValidato()
               )
            {
%>
              <!--font class="cRosso"> Elaborato </font-->
<%
            }
%>
          </td>
        </tr>
<%
      }
      AppolicenzeMod = licenzeMod;
      
      }
      
        
      strperiodiTot+=strperiodi;
      Appostrperiodi = strperiodiTot;
//      
      strperiodiTot_spe += strperiodi_spe;
      Appostrperiodi_spe = strperiodiTot_spe;
      
      strperiodiTot_int += strperiodi_int;
      Appostrperiodi_int = strperiodiTot_int;

      if(licenzeMod.getDescrStatoPermesso() != null)
    {
      if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LS"))  
      {
        TotNumeroGiorni_spe +=licenzeMod.getNumeroGiorni().intValue();
      }
      else if(licenzeMod.getDescrStatoPermesso().substring(0,2).equals("LI"))
      { 
        TotNumeroGiorni_int +=licenzeMod.getNumeroGiorni().intValue();
      }
      else
      { 
          TotNumeroGiorni +=licenzeMod.getNumeroGiorni().intValue();
      } 
    }
    else
    { 
      TotNumeroGiorni +=licenzeMod.getNumeroGiorni().intValue();
    }
      
    } // CHIUDE Ciclo For Licenze
    
  } // CHIUDE if(licenze.size()>0)
%>
    </table>
  </form>
  </body>
</html>