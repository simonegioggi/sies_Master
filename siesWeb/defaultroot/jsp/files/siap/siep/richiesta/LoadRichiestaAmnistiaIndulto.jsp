<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<html>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.util.PenaResiduaUtil"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>

<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />

<%// Reati %>
<jsp:useBean id="reati"                  scope="request" class="java.util.Vector" />
<jsp:useBean id="TipiReato"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiFontiReato"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="PeriodoConsumazione"    scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiPeneDetentive"      scope="request" class="java.lang.String"/>

<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>

<%// Dati della Pena %>
<jsp:useBean id="PenaComplessiva"        scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"                scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"                scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenaResidua"            scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="PenaResiduaCorrente"    scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />


<jsp:useBean id="isAnticipazione"        scope="request" class="java.lang.String"/>

<%
Boolean isCalcoloPenaAbInitio = (Boolean) request.getAttribute("isCalcoloPenaAbInitio");
%>

<%
BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");



/*
PenaComplessiva = pena complessiva in sentenza (sempre presente)
PenaResidua = Ultima pena residua validata (se esiste) o ultima non validata se non abInizio
Se libero = pena residua se esiste, pena complessiva altrimenti)
Se non libero = solo data inizio e data fine se pena residua validata sul PenRes1
PenRes1 = Reclusione
PenRes2 = Arresto
*/

// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug("PenRes1 = "+PenRes1);
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug("PenRes2 = "+PenRes2);
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug("PenaResidua = "+PenaResidua);

//BigDecimal LAConcesse = new BigDecimal(0);
%>

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

  boolean lIsErgastolo = false;
  if (   PenaComplessiva.getCodTipoPenaDetentiva() != null
      && PenaComplessiva.getCodTipoPenaDetentiva() != ""
      && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
          || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
         )
     )
  {
    lIsErgastolo = true;
  }

%>

<head>
  <title> [S.I.E.S.] - Richiesta/Anticipazione Amnistia/Indulto - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    var data_to_verify;
    var obbligatori;
    var dataesiste;

    //==========================================================================
    // Determina i quantum di pena residui alla data scarcerazione
    //==========================================================================
    function CalcoloResiduoPena (a_formname){
      var giornoScarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
      var meseScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
      var annoScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;

      var dataScarcerazione = giornoScarcerazione+'/'+meseScarcerazione+'/'+annoScarcerazione;
      if (dataScarcerazione=="//"){
        alert('Inserire la Data Scarcerazione');
        document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
        return;
      }

      if (!ControllaDataPassaVuota(dataScarcerazione) ) {
        alert('Data Scarcerazione non valida');
        document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
        return;
      }
      var calcoloURL = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActCalcolaPenaResiduaAl&formname="+a_formname+"&giornoScarcerazione="+giornoScarcerazione+"&meseScarcerazione="+meseScarcerazione+"&annoScarcerazione="+annoScarcerazione;

      desktop = window.open(calcoloURL, "Pena_Residua_Al", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=no, resizable=no, width=400, height=200, location=no");
    }




    function Verify()
    {

      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.selectedIndex].value == "")
      {
      alert ("Selezionare tra + e -");

       return false;
      }

      obbligatori = (document.f.GRec.value!="" || document.f.MRec.value!="" || document.f.ARec.value!="");
      obbligatori = obbligatori || (document.f.Ammenda.value!="")
      obbligatori = obbligatori || (document.f.GArr.value!="" || document.f.MArr.value!="" || document.f.AArr.value!="");
      obbligatori = obbligatori || (document.f.Multa.value!="");

      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.selectedIndex].value != "" && !obbligatori)
      {
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");

        return false;
      }

      //if (obbligatori && document.f.< %=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO% >[document.f.< %= ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO% >.selectedIndex].value == "")
      //{
      //  alert ("Selezionare tra + e -");

      //  return false;
      //}
<%
/*
      if (reati.size()>1)
      {
*/
%>
/*
       var sel=false;
       for (var k=0;k<<%=reati.size()%>;k++)
       {
         if (document.f.IdReato[k].checked)
           sel=true;
       }
       if (!sel)
       {
          alert("Selezionare il reato");
          return false;
       }
*/
<%
/*
     }
     else if (reati.size()>0)
     {
*/
%>
/*
       if (!document.f.IdReato.checked)
       {
          alert("Selezionare il reato");
          return false;
       }
*/
<%
//      }
%>

      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>.selectedIndex].value=="-")
      {
        alert("Selezionare computo beneficio");
        return false;
      }
      if (document.f.dpr[document.f.dpr.selectedIndex].value=="-")
      {
        alert("Selezionare DPR");
        return false;
      }

      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value.length>0 || document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value.length>0 || document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>.value.length>0)
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value.length<2)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value="0"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value;
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value.length<2)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value="0"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value;

        data_to_verify = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value +"/"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value+"/"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>.value;

        if (! ControllaData(data_to_verify))
        {
          alert('Data Richiesta non valida');
          return false;
        }
      }

      //Riabilita il CheckBox disabilitato
      document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>.disabled=false;

      //document.f.subm2.disabled=true;

      //document.f.submit();
      return true;
    }

    function Verify_ReturnHere()
    {
      document.f.operazione.value="Torna";
      Verify();
    }

    function Verify_Quantum()
    {
      document.f.operazione.value="Quantum";
      var RetVer = Verify();
	  return RetVer;
    }

    function Verify_Stampa()
    {
      document.f.operazione.value="Stampa";
      Verify();
    }
  </script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">

  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActRichiestaAmnistiaIndulto">
  <input type="hidden" name="operazione" value="">
  <input type="hidden" name="lFlagPage" value="RICH_AMNI">


  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0122">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta/Anticipazione Amnistia/Indulto</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table>
    <tr>
      <td class="l">
        Posizione Giuridica :
        <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {%>
          DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
        <% } else { %>
          <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
        </font>
      </td>
    </tr>
  </table>


  <table>
    <tr>
      <td class="l">
        Anticipazione degli effetti&nbsp;&nbsp;
      <%
        String lReadOnly = "disabled";
        String lChecked = "";

        if(isAnticipazione.equals("true"))
          lChecked = "checked";

        if(isAnticipazione.equals(""))
          lReadOnly = "";
      %>
        <input type="checkbox" name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>" value="A" <%=lChecked%> <%=lReadOnly%>>
      </td>
    </tr>
  </table>


<%
  //============================================================================
  // SEZIONE RELATIVA AI REATI
  //============================================================================
  if (!reati.isEmpty())
  {
%>
    <table style="width: 95%;">
      <tr><td colspan=7 class="Titolonocap">Titoli di Reato</td></tr>
      <tr>
        <td class="c">Reato</td>
        <td class="c">Durata</td>
        <td class="c">Sanzione</td>
        <td class="c">Sel.</td>
        <td class="c">Ann.Inserita</td>
      </tr>
<%
    ReatoModel lReato;
    boolean lFlagAnnoNumero;

    for (int i=0;i<reati.size();i++)
    {
      lReato=(ReatoModel)reati.get(i);
      lFlagAnnoNumero = false;

      if( lReato.getAnnoFonte() != null
        && !lReato.getAnnoFonte().equals("")
        && lReato.getNumeroFonte() != null
        && !lReato.getNumeroFonte().equals("") )
      {
        lFlagAnnoNumero = true;
      }
%>
      <tr>
        <td class="l">
          <font class="label">
<%
            if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
            {
%>
              <font class="campoNoCap">
<%
                out.println("N." + lReato.getProgrNumeroManuale()+": ");
%>
              </font>
<%
            }
            else
            {
              out.println("N." + lReato.getProgrReato()+": ");
            }
%>
          </font>
          <font class="L">
<%
          if(lFlagAnnoNumero)
          {
            if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
              out.println(lReato.getDescrFonte()+" ");
            if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
              out.println(lReato.getAnnoFonte());
            if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
              out.println("/"+lReato.getNumeroFonte());
          }

          if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
            out.println("art."+lReato.getArticolo());
          if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
            out.println(" "+lReato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
              out.println(lReato.getDescrFonte());
          }

          if(lReato.getComma() != null && !lReato.getComma().equals(""))
            out.println(" c. "+lReato.getComma());
          if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
            out.println(" l. "+lReato.getLettera());
          if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
            out.println(" n. "+lReato.getNumero());%></font>

       <% if(lReato.getStringaConsumazione()!= null) { %>
            <!--  <font class="label">Data</font> -->
            <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
       <% }

          if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
            <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
       <% }

          if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
            <font class="label">Luogo</font>&nbsp;
            <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
      <% } %>

      </td>

      <td class="l">
        <table>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </table>
      </td>
      <td class="r">
          <font class="campo">
            <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
          </font>
          €
<%
          if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0)
          {
%>
          di
            <font class="campo">
              <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
            </font>&nbsp;
<%
          }
%>
        </td>
        <td class="c">
          <input type="radio" name="IdReato" value="<%= lReato.getIdReato() %>">
        </td>
<%
     if(lReato.getFlagVisto() != null && lReato.getFlagVisto().equals("S"))
     {
%>
       <td class="C"><img src="/images/V.gif"> </td>
<%
     }
     else
     {
%>
       <td class="C"> &nbsp;</td>
<%
     }
%>
				</tr>
<%
		}
%>
		</table>
<%
  }
%>

<%
//==============================================================================
//                  SEZIONE CONTENENTE IL DETTAGLIO DELLA PENA
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio e fine pena MAI
// - Se Libero vengono visualizzati i Quantum 
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================

//==============================================================================
%>
<% // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if (lIsErgastolo)
  {
%>
    <table style="width: 95%;">
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else if (!PenRes1.getErrorMsg().equals("-"))  // sempre vero
  {  //
%>
  <table style="width: 95%;  border: 0;">
    <% if (PenRes1.getErrorMsg().equalsIgnoreCase("Libero")) { %>
      <tr>
        <td class="Titolonocap" colspan="6"> Pena residua da espiare </td>
      </tr>
      <tr>
        <td class="l"> Reclusione :
           Anni   <font class="campo"><%=PenRes1.getNumAnni()%></font>
           Mesi   <font class="campo"><%=PenRes1.getNumMesi()%></font>
           Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
           Multa  <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
        </td>
        <td class="l"> Arresto :
           Anni    <font class="campo"><%=PenRes2.getNumAnni()%></font>
           Mesi    <font class="campo"><%=PenRes2.getNumMesi()%></font>
           Giorni  <font class="campo"><%=PenRes2.getNumGiorni()%></font>
           Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
        </td>
      </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
      
    <%
    }
    else //non libero
    {
      //========================================================================
      // Visualizzo la pena residua calcolata al volo tra la data odierna e la 
      // data fine pena prevista
      //========================================================================
      CalendarUtil lCU = new CalendarUtil();
      // Attenzione!!! Anche se non libero non è detto che esiste una data
      // di decorrenza
      // PenaResiduaModel lPenaRicalcolata = PenaResidua;
      
      PenaResiduaModel lPenaRicalcolata = PenaResiduaCorrente;
      
      if (PenRes1.getDataFine()!=null) {
        // Calcolo la pena residua da espiare tra la data odierna e la data
        // fine pena prevista
        PenRes2.setDataFine   (PenRes1.getDataFine());
        PenRes2.setDataInizio (DateUtils.getSysDate());

        //PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2));
        // pongo il flag a true in modo da non considerare la data inizio (oggi)
        // come da espiare
        // Attenzione la Data inizio pena potrebbe essere futura, in questo
        // caso non ha senso calcolare la pena residua ad oggi perchè coincide
        // con il totale della pena
        if (!DateUtils.isGreater(PenRes1.getDataInizio(),DateUtils.getSysDate())){
          CalendarModel appo = lCU.CalcolaNumGiorniMesiAnni(PenRes2,true);
          // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug("appo = "+appo);
          if (lCU.getTotGiorni(appo)<0){
            PenRes2 = new CalendarModel();
          } 
          else {
            PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2,true));
          }
        }

        // Modifica 18/09/2006
        /*********************************************************************
          Se il calcolo della pena è NON è abInizio, tutti i calcoli partiranno dalla
          dall'ultima pena residua validata. Tale pena va visualizzata in questa
          maschera per consentire all'utente di avere un'idea della situazione della
          pena prima di imputare i quantumn di indulto.
          Il problema è, se il soggetto no è libero, nei calcolo non verrà utilizzato
          il quantum della pena residua validata, ma verrà effettuata prima una
          normalizzazione, quindi vale la pena visualizzare in questa maschera il
          quantum normalizzato in modo da non confondere l'operatore
        *********************************************************************/

        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("isCalcoloPenaAbInitio = "+isCalcoloPenaAbInitio);
//        if (isCalcoloPenaAbInitio.booleanValue() == false  ){
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//          siesLogger.debug("normalizzo i dati");
//          lPenaRicalcolata = PenaResiduaUtil.calcolaPenaNuovaDataFine(PenaResidua.getDataFine(), PenaResidua, false);
//       }
//        else{
//          lPenaRicalcolata = PenaResidua;
//        }
      } // fine if PenRes1.getDataFine()!=null
      %>

      <%  if (PenRes1.getDataInizio()!=null && !DateUtils.isGreater(PenRes1.getDataInizio(), DateUtils.getSysDate())){ %>
      <tr>
        <td class="Titolonocap" colspan="6"> Pena in espiazione </td>
      </tr>
      <% } else { %>
      <tr>
        <td class="Titolonocap" colspan="6"> Pena residua da espiare </td>
      </tr>
      <% } %>
      <tr>
        <td class="l" colspan="1"> Reclusione :
           Anni   <font class="campo"><%=lPenaRicalcolata.getNumAnniReclusione()%></font>
           Mesi   <font class="campo"><%=lPenaRicalcolata.getNumMesiReclusione()%></font>
           Giorni <font class="campo"><%=lPenaRicalcolata.getNumGiorniReclusione()%></font>
           Multa  <font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%></font>
        </td>
        <td class="l" colspan="1"> Arresto :
           Anni    <font class="campo"><%=lPenaRicalcolata.getNumAnniArresto()%></font>
           Mesi    <font class="campo"><%=lPenaRicalcolata.getNumMesiArresto()%></font>
           Giorni  <font class="campo"><%=lPenaRicalcolata.getNumGiorniArresto()%></font>
           Ammenda <font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%></font>
        </td>
      </tr>
      
      <% if (PenRes1.getDataInizio()!=null) {%>
      <tr>
          <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
          <%
            String dataFine         = StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFine(),"dd/MM/yyyy"));
            String dataFinePresunta = StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFinePresunta(),"dd/MM/yyyy"));
          %>
          <% if (    dataFinePresunta!=null && !dataFinePresunta.equals("")
                  && !dataFinePresunta.equals(dataFine)
                ) {%>
          <td class="l">Data Fine : <font color=red ><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy")) %> </font></td>
          <% } else { %>
          <td class="l">Data Fine : <font class=campo ><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy")) %> </font></td>
          <% } %>
      </tr>
      <% } %>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  

      <% if (PenRes1.getDataInizio()!=null && !DateUtils.isGreater(PenRes1.getDataInizio(),DateUtils.getSysDate())){ %>
      <tr>
        <td class="l">per un residuo al <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(),"dd/MM/yyyy"))%></font> (data odierna) di:</td>
        <td class="l">
          Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
          Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
          Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>
        </td>
      </tr>
      <% } %>
      
<%
       }
%>
  </table>
<%
  }
%>
<!--
Fine Pena Residua
-->
<%
//==============================================================================
//           SEZIONE CON LA RICHIESTA AL GIUDICE DELL'ESECUZIONE
//==============================================================================
%>
  <table style="width: 95%;">
		<tr>
      <td colspan=3 class="Titolonocap">Richiesta al Giudice dell' Esecuzione</td></tr>
		<tr>
      <td class="l">Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
			<td class="l">
        <select name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>">
          <%= TipoAnnotazioneManuale %>
        </select>
      </td>
			<td class="l">
        <select name="dpr">
          <%=listaDPR%>
        </select>
      </td>
		</tr>
<%
  //i dati dell'ordinanza dovranno essere prelevati se esistenti da una tabella x
%>
    <tr>
      <td class="l" colspan=3>
        <font class="label">Data Richiesta</font>
        &nbsp;&nbsp;
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>" maxlength="2" size="2" value="<%=DateUtils.getSysDate("dd")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>" maxlength="2" size="2" value="<%=DateUtils.getSysDate("MM")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>" maxlength="4" size="4" value="<%=DateUtils.getSysDate("yyyy")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

<%
//if ( !lIsErgastolo  && !PenRes1.getErrorMsg().equalsIgnoreCase("Libero") ) {
 if (   !lIsErgastolo
     && PenRes1.getDataInizio()!=null
     && !DateUtils.isGreater(PenRes1.getDataInizio(),DateUtils.getSysDate())
     ) {
  // Sezione visualizzato solo nel caso di soggetto in espiazione ma non in
  // ergastolo. In questo caso l'operatore può effettuare i calcoli della pena
  // residua a una determinata data
%>
    <tr valign="top">
      <td class="l" colspan="3">
        <font class="label">Data Eventuale Scarcerazione</font>
        &nbsp;&nbsp;
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        &nbsp;&nbsp;
        <a href="Javascript:CalcoloResiduoPena('f');">
          <img src="/images/calc32b.gif" border=0 title="Calcola Pena Residua alla data scarcerazione">
        </a>
        <!--Calcolo pena residua alla data scarcerazione-->
        <!--input type="checkbox" name="DataScarcerazione" > &nbsp; selezionare casella per conferma data scarcerazione -->
      </td>
    </tr>
<% } %>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
      <td class="Titolo">Fonte</td>
      <td class="Titolo">Anno</td>
      <td class="Titolo">Numero</td>
      <td class="Titolo">Articolo</td>
      <td class="Titolo">Art.qualificante</td>
      <td class="Titolo">Comma</td>
      <td class="Titolo">Lettera</td>
      <td class="Titolo">Numero</td>
    </tr>
<tr>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size=4 maxlength=4 title="Anno Fonte" value="" type="text" name="<%=ICostantiReato.CAMPO_ANNO_FONTE %>">
      </td>
      <td class="c">
        <input size=6 maxlength=6 title="Numero Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="c">
        <input size=5 maxlength=5 title="Articolo Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>">
      </td>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
        <strong>C</strong>
        <input size=10 maxlength=10 title="Comma" value="" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>">
      </td>
      <td class="c">
        <strong>L</strong>
        <input size=2 maxlength=2 title="Lettera" value="" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>">
      </td>
      <td class="c">
       <strong>N</strong>
       <input size=2 maxlength=2 title="Numero" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>">
      </td>
    </tr>
--%>
</table>
<table width="97%">
  <tr>
    <td colspan=6>
      <hr width="100%">
    </td>
  </tr>
  <tr>
    <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>">
        <option value=""></option>
        <option value="+">+</option>
        <option value="-">-</option>
      </select>
    </td>
    <td class="titolo" colspan=2>Reclusione</td>
    <td width="25">&nbsp;</td>
    <td class="titolo" colspan=2>Arresto</td>
  </tr>
  <tr>
    <td class="c">
      <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Giorni</font><br>
      <input type="text" name="ARec" maxlength="2" size="2" value="">&nbsp;
      <input type="text" name="MRec" maxlength="2" size="2" value="">&nbsp;
      <input type="text" name="GRec" maxlength="4" size="4" value="">
    </td>
    <td class="c">
      <font  class="label">Multa</font><br>
      <input style="align:right" type="text" name="Multa" maxlength="7" size="7" value="">
      ,
      <input style="align:right" type="text" name="Mul_dec" maxlength="2" size="2" value="">
    </td>
    <td width="25">&nbsp;</td>
    <td class=c>
      <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Giorni</font><br>
      <input  type="text" name="AArr" maxlength="2" size="2" value="">&nbsp;
      <input  type="text" name="MArr" maxlength="2" size="2" value="">&nbsp;
      <input  type="text" name="GArr" maxlength="4" size="4" value="">
    </td>
    <td class="c">
      <font  class="label">Ammenda</font><br>
      <input style="align:right" type="text" name="Ammenda" maxlength="7" size="7" value="">
      ,
      <input style="align:right" type="text" name="Amm_dec" maxlength="2" size="2" value="">
    </td>
  </tr>
  <tr>
    <td class="c" colspan=5>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="60" rows="2" name="noteRec"></textarea>
    </td>
    <td width=20>&nbsp;</td>
<!--
    <td class=l colspan=2>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="30" rows="2" name="noteArr"></textarea>
    </td>
-->
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <!-- <INPUT class="bottone" type="button" name="subm"  value="Inserisci altre richieste" onClick="javascript:Verify_ReturnHere();">&nbsp;&nbsp; -->
      <!-- <INPUT class="bottone" type="button" name="subm2" value="Conferma" onClick="javascript:Verify_Quantum();">&nbsp;&nbsp;-->
      <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
<!--
      <INPUT class="bottone" type="button" name="subm3" value="Stampa" onClick="javascript:Verify_Stampa();">&nbsp;&nbsp;
-->
    </td>
  </tr>
</table>
</form>
    <script language="JavaScript" type="text/javascript">

		var frmvalidator  = new Validator("f");

        // Controllo campi Reclusione
        frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
        frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
		frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
        frmvalidator.addValidation("Multa","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
        frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");

        // Controllo campi Arresto
        frmvalidator.addValidation("AArr","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
        frmvalidator.addValidation("MArr","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
		frmvalidator.addValidation("GArr","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
        frmvalidator.addValidation("Ammenda","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
        frmvalidator.addValidation("Amm_dec","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");


        frmvalidator.setAddnlValidationFunction("Verify_Quantum");
	</script>
</body>
</html>