<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Date"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="aEventoComputo"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aEventoAltraAut"     scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aCampoNota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="penaresiduavalidata" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
<jsp:useBean id="annotazioni"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="licenze"             scope="request" class="java.util.Vector"/>
<jsp:useBean id="aProvvAltraAut"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<!-- DettaglioRidetPenaScomputiSORV -->
<%
/*
03/06/2016
Attenzione questa jsp visualizza il dettaglio scomputi permesso e viene richiamata 2 volte
 - la prima dopo l'inserimento del provvedimento
 - la seconda dopo la conferma del calcolo pena
 
In generale la nuova versione (DECISIONI DELLA SORVEGLIANZA - SCOMPUTO PERMESSI) 
scrive i dati su LICENZA_LIBANTICIPATA, ma è possibile registrare lo scomputo anche 
passando per RIDETERMINAZIONE PENA - ALTRO
In questo secondo caso i dati vengono scritti su ANNOTAZIONE_MANUALE 
 
Per i vecchi scomputi iscritti su annotazione manuale, per il dettaglio da Elenco Provvedimenti del PM,
viene comunque agganciata questa jsp avendo gli stessi codici della nuova versione (vedi DETTAGLIO_PROVVEDIMENTO).
Per cui la jsp deve essere in grado di gestire entrambi i casi.

Il js function CalcoloPena(idEvento) reindirizza il flusso corretto per il calcolo pena
in funzione della provenienza

La action richiamata per il calcolo pena da function CalcoloPena(idEvento)
cambia quindi in funzione del flusso da seguire.
- ActCalcoloPenaComputo           se ridet. pena altro (AM)
- ActCalcoloPenaScompPermRidimLA  se decisioni sorv (LA)

*/
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel       lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel              lAltraCausa = posizioneluogoaltra.getAltraCausa();

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
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script>
    function CalcoloPena(idEvento){

    document.f.lFlagTipoScomputo.value="<%=StringUtils.toStringJSP(aEventoComputo.getDescrMotivo(),"")%>";
      <% if (annotazioni.size()>0) { %>
      document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaComputo";
      <% } else { %>
      document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.libertaanticipata.action.ActCalcoloPenaScompPermRidimLA";
      <% } %>
      if (typeof (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>)!="undefined")  
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value.length==1)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value.length==1)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value='0'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;

        var data_scarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value
                            +'/'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value
                            +'/'+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

        if (!ControllaDataPassaVuota(data_scarcerazione) )
        {
          alert('Data scarcerazione non valida');
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.focus();
          return false;
        }       
      }   

      try{
        document.f.CALCOLO.disabled=true;
      }
      catch(err) {
        //alert('Tasto CALCOLO Assente nella form');
      }
      
      try{
        document.f.STAMPA.disabled=true;
      }
      catch(err) {
        //alert('Tasto STAMPA  Assente nella form');
      }

      document.f.submit();

    }
    
    function caricaStampe(idEvento) {
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActLoadGrigliaRidetPenaRidimLA&<%=ICostantiEvento.CAMPO_ID_EVENTO%>="+idEvento;
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Rideterminazione Pena - <%=aEventoComputo.getDescrMotivo()%></font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
//==============================================================================
// Sezione Contenente
// - Posizione giuridica
// - Luogo di detenzione
//==============================================================================
%>
  <table style="width: 95%;">
    <tr>
      <td class="l">Posizione Giuridica:
      <!--td class="L" colspan=5-->
        <font class="campo">
        <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
        DETENUTO PER ALTRA CAUSA
        <%} else {%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <%}%>
        </font>
      </td>
    </tr>

    <%
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
    {
      if(lAltraCausa.getIstitutoDetenzione() != null )
      {
      %>
      <tr>
        <td class="l">Detenuto presso </td>
        <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
           di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
        <%if (lAltraCausa.getAltroLuogo()!=null) { %>
          <tr>
            <td class="l">Altro Luogo </td>
            <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
        <% }
      }  //
    }
    else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
    { // non detenuto altra causa
    %>
    <tr>
      <td class="l">Detenuto presso </td>
      <td class="L" colspan=5>
        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
      </td>
    </tr>
    <% } %>



    <%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(    lPosizione.getCodPosizioneGiuridica() != null
       && (   lPosizione.getCodPosizioneGiuridica().equals("02")
           || lPosizione.getCodPosizioneGiuridica().equals("04")
          )
      )
    {
      if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
      <tr>
        <td class="l">Indirizzo</td>
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
        </td>
       </tr>
       <% }
    }
    %>
  </table>

<%
//==============================================================================
//             Sezione contenente gli estremi del provvedimento
//==============================================================================
%>
<table width=95%>
  <tr>
    <td class="Titolo" colspan=10> Provvedimento</td>
  </tr>
  <tr>
    <td class="l">Provvedimento :&nbsp;</td>
    <td class="L">
      <% if (  aEventoAltraAut.getIdEvento()==null) {%>
      <font class="campo">D'ufficio</font>
      <% } else { %>
      <font class="campo">In esecuzione di provvedimento altro Ufficio</font>
      <% } %>
    </td>
    <% // if (  aEventoAltraAut.getIdEvento()==null) {%>
  </tr>
  <tr>
    <td class="l">Oggetto :&nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(aEventoComputo.getDescrMotivo())%></font>
    </td>
    <% //} %>
  </tr>  
  <tr>
    <td class="l">Nota :&nbsp;</td>
    <td class="L" colspan="100%">
      <font class="campo"><%=StringUtils.toStringJSP(aCampoNota.getDescr())%></font>
    </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "dd-MM-yyyy") )%></font>
    </td>
    <%if(magistrato != null && magistrato.getCodMagistrato()!=null && !magistrato.getCodMagistrato().equals("")){%>
    <td class="l">Magistrato Firmatario
    <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
    </td>
  <%}%>
  </tr>  
</table>

<% if (  aEventoAltraAut.getIdEvento()!=null) {%>
<table style="width: 95%;">
  <!-- Sezione con i dati del provvedimentio Altra Autorità -->
  <tr>
    <td colspan="100%">
      <table width="100%">
        <tr>
          <td colspan=4 class="titolo">Dati Provvedimento Altra Autorità</td>
        </tr>

	<tr>
		<td class="l" width="20%">Provvedimento emesso da</td>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(aEventoAltraAut.getDescrUfficioEmittente());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(aEventoAltraAut.getCodTipoUfficioEmittente())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
		<td class="l" colspan="3">
            <font class="campo"><%=descrTipoUfficio%></font>
            di 
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrLuogoEmittente(), "")%></font>
    	</td>
	</tr>

        <tr>
          <td class="l">Data ricezione provvedimento</td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataRicezioneAtti(), "dd-MM-yyyy") )%></font></td>
        </tr>
        <tr>
          <td class="l">Data emissione provvedimento</td>
          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataEmissione(), "dd-MM-yyyy") )%></font></td>
          <td class="l">Anno / Numero Provvedimento</td>
          <td class="l">
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getAnnoProtocollo(),"")%></font>/
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getProgrProtocollo(),"")%></font>
          </td>
        </tr>
    
        <tr>
          <td class="l">Tipo Provvedimento</td>
          <td class="l" colspan=3>
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrTipoProvvedimento(),"")%></font>
          </td>
        </tr>

        <tr>
          <td class="l">Oggetto Provvedimento</td>
          <td class="l" colspan=3>
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrMotivo(),"")%></font>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<% } %>

<%
//==============================================================================
//                Sezione contenente le Annotazioni inserite
//==============================================================================
%>
<br>

<% if (annotazioni.size()>0) { %>
<table>
<%
  //===================================================
  // Primo ciclo per caricare i quantum in detrazione
  //===================================================
  int lConta = 0;
  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);
    
    if (lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("-"))
    {
      lConta = lConta + 1;
    %>
    
    <% if (lConta==1) { %>
    <tr>
      <td class="Titolo"  colspan="100%"> Quantum in Detrazione </td>
    </tr>
    <% } %>

    <tr>
      <!-- Reclusione / Multa -->
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>

      <!-- Arresto/Ammenda -->
      <td class="l"><font class="label">Arresto / Ammenda : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda()))%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" colspan="100%"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
    </tr>
    
  <%
      } // end if in detrazione
   } // end while
  %>
  
  <%
  //===================================================
  // Secondo ciclo per caricare i quantum in Aumento
  //===================================================
  lConta = 0;
  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);
    
    if (lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lConta = lConta + 1;
    %>
    
    <% if (lConta==1) { %>
    <tr>
      <td class="Titolo"  colspan="100%"> Quantum in Aumento </td>
    </tr>
    <% } %>

    <tr>
      <!-- Reclusione / Multa -->
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>

      <!-- Arresto/Ammenda -->
      <td class="l"><font class="label">Arresto / Ammenda : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda()))%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" colspan="100%"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
    </tr>
    
  <%
      } // end if in detrazione
   } // end for
  %>
  
  <%
  //=================================================================
  // Calcolo e Visualizzazione del saldo (in detrazione/in aumento)
  //=================================================================
  CalendarModel lCalTotaleAggregatoRec = new CalendarModel();
  CalendarModel lCalTotaleAggregatoArr = new CalendarModel();
  CalendarUtil lCalUtil = new CalendarUtil();

  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);


    // Reclusione/Multa    
    CalendarModel lCalReclusione = new CalendarModel();
    lCalReclusione = lAnnMod.getQuantumReclusione();
    if (lAnnMod.getImportoMulta() != null)
      lCalReclusione.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

    if (lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lCalTotaleAggregatoRec = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoRec,lCalReclusione);
    } 
    else 
    {
      lCalTotaleAggregatoRec = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoRec,lCalReclusione);
    }
    
    // Arresti/Ammenda
    CalendarModel lCalArresti = new CalendarModel();
    lCalArresti = lAnnMod.getQuantumArresto();
    if (lAnnMod.getImportoAmmenda() != null)
      lCalArresti.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());
      
    
    if (lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lCalTotaleAggregatoArr = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoArr,lCalArresti);
    } 
    else 
    {
      lCalTotaleAggregatoArr = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoArr,lCalArresti);
    }    
    
  }    
  %>
  
  <tr>
    <td class="Titolo"  colspan="100%"> Per un totale da computare di  </td>
  </tr>
  
  <tr>
    <td class="l"><font class="label">Reclusione / Multa : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumAnni()))%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumMesi()))%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumGiorni()))%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(new BigDecimal(lCalTotaleAggregatoRec.getImportoMulta())))%></font></td>

    <td class="l"><font class="label">Arresto / Ammenda :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumAnni()))%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumMesi()))%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumGiorni()))%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(new BigDecimal(lCalTotaleAggregatoArr.getImportoAmmenda())))%></font></td>
  </tr>
</table>

<% } %>

<%
//==============================================================================
//  ciclo per la visualizzazione delle licenze (scomputo permesso)
//==============================================================================
if (licenze.size()>0)
{
  int lTotGiorni = 0;
  for (int i=0; i<licenze.size(); i++){
    LicenzaLibAnticipataModel lLicModel = (LicenzaLibAnticipataModel) licenze.elementAt(i);
    lTotGiorni = lTotGiorni + lLicModel.getNumeroGiorni().intValue();
  }
  %>
  <table>
    <tr>
      <td class="Titolo"  colspan="100%"> Totale giorni scomputati </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lTotGiorni))%></font>&nbsp;</td>
    </tr>
  </table>
  <%
}
%>


<%
//==============================================================================
// Sezione contenente la Pena Ricalcolata (se presente e associata al provvedimento)
// e l'eventuale fungibilità
//==============================================================================
%>
<% if (penaresidua != null && penaresidua.getIdPenaResidua() != null) { %>
  <table>
    <tr>
      <td class="Titolo"  colspan=10> Pena Rideterminata </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoMulta()))%></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Arresto / Ammenda :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoAmmenda()))%></font></td>
    </tr>
  </table>
  
  <table>
    <%if(penaresidua.getDataInizio() != null) {%>
    <tr>
      <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFineReclusione() != null) {%>
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <%
    }
  
    if(penaresidua.getDataInizioArresto() != null) {%>
    <tr>
      <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFine() != null || penaresidua.getDataFinePresunta()!= null) {%>
      <td class="l"><font  class="label">Data Fine Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%if(penaresidua.getDataFine() != null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"),"-")%>
        <%} else if(penaresidua.getDataFinePresunta()!= null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"),"-")%>
        <%}%>
        </font>
      </td>
    </tr>
    <% } %>
  </table>
  <%
  CalendarUtil lCalendarUtil = new CalendarUtil();
  if ( !lCalendarUtil.isZero(fungibilita.getQuantumFungibilita() ) ) {
  %>
  <table>
    <tr>
      <td class="Titolo" colspan=9><font  class="label">Pena Espiata In Eccesso</font></td>
      <%
        String GGFung = fungibilita.getNumGiorni()+"";
        String MMFung = fungibilita.getNumMesi()+"";
        String AAFung = fungibilita.getNumAnni()+"";
      %>
      <td class="l"><font class="label">Anni</font></td>
      <td class="l"><font class="Campo"><%=AAFung%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="l"><font class="Campo"><%=MMFung%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="l"><font class="Campo"><%=GGFung%></font></td>
    </tr>
  </table>
 <% } %>

  
<% }  // end if penaresidua != null  %>


<br>

<%
if (   aEventoComputo.getFlagDocumentoRegistrato()==null 
    || !aEventoComputo.getFlagDocumentoRegistrato().equals("A")
   ) 
{
%>
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Data per il calcolo della fungibilità: in questo caso data emissione provvedimento -->
  <% if (annotazioni.size()>0) { %>
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "yyyy") )%>">
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "MM") )%>">
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "dd") )%>">
  <% } %>
  <!-- Id dell'evento a cui collegare la pena residua, le annotazioni e la fungibilità -->
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(aEventoComputo.getIdEvento())%>">
  
  
  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="014">
  
  <input type="HIDDEN" name="lFlagPage" value="ALTRO">
  <input type="HIDDEN" name="lFlagTipoScomputo" value="">

  <table width="800px">
      <%  
      if (   (penaresidua==null || penaresidua.getIdPenaResidua() == null)
          && (   penaresiduavalidata != null 
              && penaresiduavalidata.getDataInizio() != null
              // MEV29 in caso di ergastolo non devo consentire il calcolo pena.
              && !penaresiduavalidata.isErgastolo()
             )         
         )
      {
        Date oggi = DateUtils.getSysDateAsDate("dd/MM/yyyy");
        Date dataFinePena = penaresiduavalidata.getDataFine(); 
      %>
      
      <% if (dataFinePena!=null && !DateUtils.isGreater(dataFinePena, oggi) && licenze.size()>0 ){%>
      <tr>
        <td class="L" colspan="2">
          <font color="red">
            Attenzione. La pena a sistema risulta interamente espiata al <%=StringUtils.toStringJSP(DateUtils.getDateToString(dataFinePena,"dd-MM-yyyy"))%>. I calcoli verranno effettuati considerando tale data come data di effettivo fine pena se non diversamente indicato.
           </font>        
        </td>
      </tr>
      <% } %>
            
      <tr>
        <td width="150px">
          <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:CalcoloPena(<%=StringUtils.toStringJSP(aEventoComputo.getIdEvento())%>);">
        </td>
        
        <% if (dataFinePena!=null && !DateUtils.isGreater(dataFinePena, oggi) && licenze.size()>0 ) { %>
        <td class="L">
          Data eventuale Scarcerazione
          <input type="text" maxlength="2" size="2" 
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" 
                 value="<%=DateUtils.getDateToString(dataFinePena,"dd")%>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
                 >
          <input type="text" maxlength="2" size="2" 
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>" 
                 value="<%=DateUtils.getDateToString(dataFinePena,"MM")%>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" 
                 >
          <input type="text" maxlength="4" size="4" 
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>" 
                 value="<%=DateUtils.getDateToString(dataFinePena,"yyyy")%>"
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" 
                 >
        </td>
        <% } %>        
      </tr>
      <% } %>
      
      
      <% // E' possibile procedere alla produzione del provvedimento correlato 
         // o alla validazione solo dopo aver effettuato il calcolo della pena
      if (   penaresidua != null && penaresidua.getIdPenaResidua() != null 
            // MEV29 in caso di ergastolo non devo consentire le stampe.
          && !penaresiduavalidata.isErgastolo()
          ) 
      { 
      %>
      <tr>
        <td>
          <INPUT class="bottone" type="button" name="STAMPA" value="Stampe" onClick="javascript:caricaStampe('<%=aEventoComputo.getIdEvento()%>');">
        </td>
      </tr>
      <% } %>
    </tr>
  </table>
</form>
<% } %>

</body>
</html>