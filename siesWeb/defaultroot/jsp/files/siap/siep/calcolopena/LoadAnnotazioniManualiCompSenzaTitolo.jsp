<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<jsp:useBean id="CausaleComputo" scope="request" class="java.lang.String" />


<%
//==============================================================================
// Form per l'inserimento dei dati del computo Fungibilità Pena Detentiva
// - PenRes1 = Reclusione
// - PenRes2 = Arresti
// - LAConcesse = 
//==============================================================================
%>

<%
BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>
<head>
  <title> [S.I.E.S.] - Calcolo Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
/*
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
*/

    function Verify()
    {
      if (document.f.DaGidalR.value.length<2)
        document.f.DaGidalR.value="0"+document.f.DaGidalR.value;
      if (document.f.DaMedalR.value.length<2)
        document.f.DaMedalR.value="0"+document.f.DaMedalR.value;

      var dataDAL=document.f.DaGidalR.value +"/"+document.f.DaMedalR.value+"/"+document.f.DaAndalR.value;

      if (! ControllaData(dataDAL))
      {
        alert('Data di Inizio Periodo non valida');
        document.f.DaGidalR.focus();

        return false;
      }

      if (document.f.DaGialR.value.length<2)
        document.f.DaGialR.value="0"+document.f.DaGialR.value;
      if (document.f.DaMealR.value.length<2)
        document.f.DaMealR.value="0"+document.f.DaMealR.value;

      var dataAL=document.f.DaGialR.value +"/"+document.f.DaMealR.value+"/"+document.f.DaAnalR.value;

      if (! ControllaData(dataAL))
      {
        alert('Data di Fine Periodo non valida');
        document.f.DaGialR.focus();

        return false;
      }

      if(!CompareDate(dataDAL, dataAL))
      {
        alert('La Data di Fine Periodo non può essere precedente a quella di Inizio');
        document.f.DaGidalR.focus();

        return false;
      }

      document.f.subm2.disabled=true;

      document.f.submit();
    }

    function Verify_Quantum()
    {
      document.f.operazione.value="Quantum";
      Verify();
    }
  </script>
</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciAnnotazioniManualiCompSenzaTitolo">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0213">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="007">
    <input type="HIDDEN" name="lFlagPage" value="S">

    <input type="HIDDEN" name="operazione" value="">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Computo pena detentiva espiata per altro reato (fungibilità)</font>
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
<%
      if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
%>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
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
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: solo data inizio e data fine MAI
// - Se Libero vengono visualizzati i Quantum 
//   - PenRes1 = Reclusione
//   - PenRes2 = Arresti
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>
<% // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(  PenaComplessiva.getCodTipoPenaDetentiva() != null
    && PenaComplessiva.getCodTipoPenaDetentiva() != ""
    && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
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
  else if (!PenRes1.getErrorMsg().equals("-"))
  {
%>
  <table style="width: 95%;">
    <tr>
    <% if (PenRes1.getErrorMsg().endsWith("COMPLESSIVA")) { %>
      <td class=Titolonocap colspan=6 width=80%> Pena complessiva </td>
    <% } else { %>
      <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
    <% } %>
    </tr>
<%
  if (PenRes1.getErrorMsg().startsWith("Libero")) //Patch per gestire il titolo
  {
%>
    <tr>
      <td class="l"> Reclusione :
        Anni <font class=campo><%=PenRes1.getNumAnni()%></font>
        Mesi <font class=campo><%=PenRes1.getNumMesi()%></font>
        Giorni <font class=campo><%=PenRes1.getNumGiorni()%></font>
        Multa <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
        Anni <font class=campo><%=PenRes2.getNumAnni()%></font>
        Mesi <font class=campo><%=PenRes2.getNumMesi()%></font>
        Giorni <font class=campo><%=PenRes2.getNumGiorni()%></font>
        Ammenda <font class=campo><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
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
    //==========================================================================
    // Se non libero visualizzo il quantum calcolato al tra la data di sistema
    // e la data fine prevista
    //==========================================================================
    //CalendarUtil lCU = new CalendarUtil();
    //PenRes2.setDataFine(PenRes1.getDataFine());
    //PenRes2.setDataInizio(DateUtils.getSysDate());
    //PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2, true));
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="l">
        Anni: <font class=campo><%=PenRes2.getNumAnni()%></font>
        Mesi: <font class=campo><%=PenRes2.getNumMesi()%></font>
        Giorni: <font class=campo><%=PenRes2.getNumGiorni()%></font>
      </td>
      <td class=l>Data Inizio: <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy")%></font></td>
      <td class=l>Data Fine: <font class=campo><%=DateUtils.getDateToString(PenRes1.getDataFine(),"dd/MM/yyyy")%></font></td>
    </tr--%>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
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
%>
</table>
<%
  }
%>
  <table style="width: 95%;">
  <tr>
    <td colspan=5 class="Titolonocap">Dati identificativi del procedimento SIEP cui si riferisce la pena espiata in eccesso</td>
  </tr>
  <tr>
    <td class="l">Sentenza : </td>
    <td class="l">Anno/Numero
      <input type="text" name="annoSentenza" size=4 maxlength=4>
      /
      <input type="text" name="numeroSentenza" size=4 maxlength=4>
    </td>
    <td class="l">
      <font class="label">in data </font>
      &nbsp;&nbsp;
      <input type="text" name="DaGiSent" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaMeSent" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaAnSent" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
  <tr>
    <td class="l">Causale computo : </td>
    <td class="l" colspan=2>
      <select Title="Causale Computo" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_CAUSALE_COMPUTO%>">
        <%=CausaleComputo%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" colspan=3>
      <input type="radio" name="TipoOrd" value="SenzaRichiestaPPP" checked>d'ufficio &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="ConformePPP">su richiesta difensore &nbsp;&nbsp;
      <input type="radio" name="TipoOrd" value="DifformePPP">su richiesta interessato
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td><input type="hidden" name="PM" value="-"></td>
  </tr>
  <tr>
    <td class="titolo" colspan=6>Periodo di pena espiata senza titolo</td>
  </tr>
  <tr>
    <td class=l colspan=1>
      <font class="label">Dalla Data <font class="ob">(*)</font></font>
      <input type="text" name="DaGidalR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaMedalR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input  type="text" name="DaAndalR" maxlength="4" size="4" value=""  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class=l colspan=1>
      <font  class="label">Alla Data <font class="ob">(*)</font></font>
      <input type="text" name="DaGialR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" name="DaMealR" maxlength="2" size="2" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input  type="text" name="DaAnalR" maxlength="4" size="4" value="" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td class="c">
      <font class="label">Note</font>
    </td>
    <td class="l">
      <textarea cols="80" rows="6" name="noteRec"></textarea>
    </td>
  </tr>
</table>
<br>
<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <INPUT class="bottone" type="button" name="subm2" value="Conferma" onClick="javascript:Verify_Quantum();">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

</body>
</html>