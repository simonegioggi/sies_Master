<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneModel"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>
<%@ page import="siap.siep.scambiosanzione.model.ScambioSanzioneRichiestaConvModel"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>

<jsp:useBean id="lScaSanzRC"  scope="request" class="java.util.Vector" />
<jsp:useBean id="lCodTipPro"  scope="request" class="java.util.Vector" />

 
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Conversione Pena Pecuniaria - Elenco SIUS</title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
   
   
    function controlla()
    {
    <%
          boolean esistonoDati = false;
          if( !lScaSanzRC.isEmpty() )
              esistonoDati = true;
    %>
          if(<%=!esistonoDati%>)
          {
              alert("Nessun dato presente !");
              window.parent.close();
          }
          else {
            window.focus();
          }
    }  
      
      
    function insertIT(id,
                      descrTipoDecisione,
                      annoSius,
                      numeroSius,
                      annoRegistro,
                      numeroRegistro ,
                      descrUfficioEmittente,
                      comuneUfficioEmittente,
                      g_DataEmissione,
                      m_DataEmissione,
                      a_DataEmissione,
                      descrTipoSanzione,
                      descrNaturaSanzione,
                      codNaturaSanzione,
                      durataEsitoAnni,
                      durataEsitoMesi,
                      durataEsitoGiorni,
                      numeroRate,
                      valoreRata,
                      valoreRataI,
                      valoreRataD,
                      valoreUltRata,
                      valoreUltRataI,
                      valoreUltRataD,                      
                      GiornoDataInizioPaga,
                      MeseDataInizioPaga,
                      AnnoDataInizioPaga,
                      GiorniInizioPaga
                    )

    {
      window.parent.opener.resettaCampi();
      
    
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_ID_SCAMBIO_SANZIONE%>.value=id;
      
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC %>.value=descrTipoDecisione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS  %>.value=annoSius;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS %>.value=numeroSius;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO %>.value=annoRegistro;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO %>.value=numeroRegistro;
      
      if (descrUfficioEmittente.toUpperCase().indexOf("UFF")==0)
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value="UDS";
      else
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value="TDS";
      
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE  %>.value=comuneUfficioEmittente;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value=g_DataEmissione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value=m_DataEmissione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value=a_DataEmissione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE %>.value=descrTipoSanzione;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE %>.value=descrNaturaSanzione;
      //alert( "codNaturaSanzione = "+codNaturaSanzione);

      window.parent.opener.EsiDurata(codNaturaSanzione);
          
      if (codNaturaSanzione =='0156')
      {
        if (durataEsitoAnni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB %>.value=durataEsitoAnni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LIB %>.value="";

        if (durataEsitoMesi!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB %>.value=durataEsitoMesi;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LIB %>.value="";

        if (durataEsitoGiorni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB %>.value=durataEsitoGiorni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LIB %>.value="";
      }
      else if(codNaturaSanzione =='0157')
      {
        if (durataEsitoAnni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV %>.value=durataEsitoAnni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_LAV %>.value="";

        if (durataEsitoMesi!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV %>.value=durataEsitoMesi;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_LAV %>.value="";

        if (durataEsitoGiorni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV %>.value=durataEsitoGiorni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_LAV %>.value="";
      }
      else if(codNaturaSanzione =='0158')
      {
        if (durataEsitoAnni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF %>.value=durataEsitoAnni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_ANNI_DIFF %>.value="";

        if (durataEsitoMesi!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF %>.value=durataEsitoMesi;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_MESI_DIFF %>.value="";
  
        if (durataEsitoGiorni!='')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF %>.value=durataEsitoGiorni;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_DURATA_ESITO_GIORNI_DIFF%>.value="";
      }
      else if(codNaturaSanzione =='0159')
      {
        if (numeroRate!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE %>.value=numeroRate;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_NUMERO_RATE %>.value="";

        if (valoreRata!='-'){
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I %>.value=valoreRataI;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D %>.value=valoreRataD;
        }
        else {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_I %>.value="";
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_RATA_D %>.value="";
        }
        
        if (valoreUltRata!='-') {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I %>.value=valoreUltRataI;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D %>.value=valoreUltRataD;
        }
        else {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_I %>.value="";
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_VALORE_ULTIMA_RATA_D %>.value="";
        }
        
        if (GiornoDataInizioPaga!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA %>.value=GiornoDataInizioPaga;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_INIZIO_PAGA %>.value="";

        if (MeseDataInizioPaga!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA %>.value=MeseDataInizioPaga;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_MESE_DATA_INIZIO_PAGA %>.value="";

        if (AnnoDataInizioPaga!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA %>.value=AnnoDataInizioPaga;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_INIZIO_PAGA %>.value="";

        if (GiorniInizioPaga!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA %>.value=GiorniInizioPaga;
        else
         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaPecuniaria.CAMPO_GIORNI_INIZIO_PAGA %>.value="";
      }
      
      window.parent.opener.disabilitaCampi();
      window.close();
  }
    </script>
  </head>

<body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Elenco Dei Provvedimenti Sorveglianza</font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int" width=15%>Anno/Numero Sius</td>
      <td class="int" width=15%>Provvedimento</td>
      <td class="int" >Autorità Emittente</td>
      <td class="int" width=15%>Data</td>
      <td class="int" >Oggetto</td>
      <td class="int" >Esito</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
  
<%

  ScambioSanzioneRichiestaConvModel lScaRic = new ScambioSanzioneRichiestaConvModel();
  String lCodTP = null; 
  String lDesTP = null;

  Iterator itx = lScaSanzRC.iterator();
  Iterator itx1 = lCodTipPro.iterator();

  while ( itx.hasNext())
  {
    lScaRic = (ScambioSanzioneRichiestaConvModel)itx.next();
    lCodTP = (String)itx1.next();

    if(lScaRic.getScambioSanzione().getIdScambioSanzione() != null )
    {
        String lUfficio = StringUtils.toStringJSP(lScaRic.getScambioSanzione().getDescrUfficioEmittente(), "-");
        if( lUfficio.toUpperCase().startsWith("TRIB") )
        {
            lUfficio = "TDS";
        }
        else if(lUfficio.toUpperCase().startsWith("UFF"))
        {
            lUfficio = "UDS";
        }
        if( lCodTP.toUpperCase().compareTo("02")==0 )
        {
            lDesTP = "Decreto";
        }
        else if( lCodTP.toUpperCase().compareTo("03")==0 )
        {
            lDesTP = "Ordinanza";
        }
%>
          <tr>
              <td class="c">
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getChiaveAnnoFascicoloSius(), "-")%>
                /
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getChiaveProgrFascicoloSius(), "-")%>
              </td>
              <td class="c">
                <%=StringUtils.toStringJSP(lDesTP, "-")%>
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getAnnoRegistro(), "-")%>
                /
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getNumeroRegistro(), "-")%>
              </td>
              <td class="l">
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getDescrUfficioEmittente(),"-")%> di 
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getComuneUfficioEmittente(), "-")%>
              </td>
              <td class="l">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getScambioSanzione().getDataEmissione(),"dd-MM-yyyy"), "-")%>
              </td>
              <td class="l">
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getDescrTipoSanzione(), "-")%>
              </td>
              <td class="l">
                <%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getDescrNaturaSanzione(), "-")%>
              </td>
          
      <td class="c">
        <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getIdScambioSanzione(),"-")%>',
                                    '<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getCodTipoDecisione(),"-")%>',  
                                    '<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getChiaveAnnoFascicoloSius(),"-")%>',
                                    '<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getChiaveProgrFascicoloSius(),"-")%>',                                         
                                    '<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getAnnoRegistro(),"-")%>',
                                    '<%=StringUtils.toStringJSP(lScaRic.getScambioSanzione().getNumeroRegistro(),"-")%>',                                         
                                    '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lScaRic.getScambioSanzione().getDescrUfficioEmittente() ),"-")%>',
                                    '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lScaRic.getScambioSanzione().getComuneUfficioEmittente() ),"-")%>',
                                    '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getScambioSanzione().getDataEmissione(),"dd"),"-")%>',
                                    '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getScambioSanzione().getDataEmissione(),"MM"),"-")%>',
                                    '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getScambioSanzione().getDataEmissione(),"yyyy"),"-")%>',
                                    '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lScaRic.getScambioSanzione().getCodTipoSanzione() ),"-")%>',
                                    '<%=StringUtils.toStringJSP(lScaRic.getCodEsito(),"-")%>',                                     
                                    '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lScaRic.getScambioSanzione().getCodNaturaSanzione() ),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getDurataEsitoAnni(),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getDurataEsitoMesi(),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getDurataEsitoGiorni(),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getNumeroRate(),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getValoreRata(),"-")%>',
									 '<%=StringUtils.toStringJSP(StringUtils.getParteIntera(lScaRic.getRichiestaConv().getValoreRata()),"-")%>',
									 '<%=StringUtils.toStringJSP(StringUtils.getParteDecimale(lScaRic.getRichiestaConv().getValoreRata()),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getValoreUltimaRata(),"-")%>',
									 '<%=StringUtils.toStringJSP(StringUtils.getParteIntera(lScaRic.getRichiestaConv().getValoreUltimaRata()),"-")%>',
									 '<%=StringUtils.toStringJSP(StringUtils.getParteDecimale(lScaRic.getRichiestaConv().getValoreUltimaRata()),"-")%>',
                                     '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getRichiestaConv().getDataInizioPagamento(),"dd"),"-")%>',
                                     '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getRichiestaConv().getDataInizioPagamento(),"MM"),"-")%>',
                                     '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaRic.getRichiestaConv().getDataInizioPagamento(),"yyyy"),"-")%>',
                                     '<%=StringUtils.toStringJSP(lScaRic.getRichiestaConv().getNumeroGiorniInizioPagamento(),"-")%>'
                                   );">
                                          
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
          </td>
        </tr>
<%
        }  // Chiude if
      
  }  // Chiude while  
 
%>
    </table>
  </form>
</body>
</html>