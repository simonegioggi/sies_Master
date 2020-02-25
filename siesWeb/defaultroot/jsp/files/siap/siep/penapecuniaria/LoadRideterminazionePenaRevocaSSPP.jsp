<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.log.LogF3B"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%// Motivo ridet.pena %>
<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="motivoRidetPena"  scope="request" class="java.lang.String" />
<jsp:useBean id="descMotivoRidetPena" scope="request" class="java.lang.String" />

<%// Pena in decorrenza %>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="aPenaResidua"  	  scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="annotazione"         scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>

<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
  
<%
//==============================================================================
// form per l'inserimento del Provvedimento di Rideterminazione Pena Revoca SSPP
// menu: 'Rideterminazione Pena - Provvedimenti del PM - Altro'
//
// - Posizione giuridica
// - Pena Residua In espiazione/Da espiare
// -
// -
//==============================================================================
int maxNumComputi = 1;

BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel     lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel  lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel            lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

%>

<!-- LoadRidetPenaAltro_new.jsp -->
<html>

<head>
  <title> [S.I.E.S.] - Annotazioni Manuali - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    
    var lMaxNumComputi = <%=maxNumComputi%>;
    
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    function Verify()
    {
      // - Campo Oggetto obbligatori
      // - Quantum obbligatori (almeno uno): segno e quantità o importi
      // - Se provvedimento altra autorità obbligatori:
      //   * Tipo Provvedimento
      //   * Oggetto provvedimento
      //   * Autorità emittente tipo e sede
      // - Magistrato Firmatario (?)

      //===========================================
      // Controllo sui campi 
      //===========================================        
      // Quantum di computo
      var numComputi = 0;
      
      // Per ogni rigo visibile verifico la coerenza dei dati. Se presente il segno
      // vanno specificati anche i quantum. Se presenti i quantum è obbligatorio
      // il segno. E' accettato il rigo vuoto anche se visibile.
      i=0;
      // Verifico se presenti i quantum
      if (   trimStringa(document.getElementById('ARec_'+i).value) == "" 
          && trimStringa(document.getElementById('MRec_'+i).value) == "" 
          && trimStringa(document.getElementById('GRec_'+i).value) == "" 
          && trimStringa(document.getElementById('Multa_'+i).value) == "" 
          && trimStringa(document.getElementById('Mul_dec_'+i).value) == "" 
          && trimStringa(document.getElementById('AArr_'+i).value) == ""
          && trimStringa(document.getElementById('MArr_'+i).value) == "" 
          && trimStringa(document.getElementById('GArr_'+i).value) == "" 
          && trimStringa(document.getElementById('Ammenda_'+i).value) == "" 
          && trimStringa(document.getElementById('Amm_dec_'+i).value) == "" 
          //&& trimStringa(document.getElementById('motivazioni_'+i).value) == ""
         )
      {
        alert ("Selezionare la  durata del periodo per arresto o reclusione");
        document.getElementById('ARec_'+i).focus();
        return false;
      }

      
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
        return false;
      }     
      
      //=======================
      // Controllo Magistrato  
      //=======================
      if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Selezionare Magistrato firmatario");
        return false;
      }       

      return true;
    }
    
    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
       var desktop;
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
  </script>
</head>

<body class="corpo">

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciRidetPenaAltro">
  <input type="hidden" name="maxNumComputi" value="<%=maxNumComputi%>" >
  <input type="hidden" name="noteComputo" value="" >
  <input type="hidden" name="Multa_0" value="0" >
  <input type="hidden" name="Mul_dec_0" value="0" >
  <input type="hidden" name="Ammenda_0" value="0" >
  <input type="hidden" name="Amm_dec_0" value="0" >
  <input type="hidden" name="TipoOrd" value="dufficio">
  
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Rideterminazione della Pena - Revoca Sanzione Sostitutiva su Pena Pecuniaria </font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<%
//==============================================================================
// Sezione con:
// - la posizione giuridica
// - la pena residua (attuale)
//==============================================================================
%>
<table>
  <tr>
    <td class="l"> Posizione Giuridica :
      <font class="campo">
        <% if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
        <% } else { %>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <% } %>
      </font>
    </td>
  </tr>
</table>

<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio, MAI
// - Se Libero vengono visualizzati i Quantum
// - Se detenuto viene visulizzato il quantum residuo calcolato al volo tra la
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>

<%
  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(   PenaComplessiva.getCodTipoPenaDetentiva() != null
     && PenaComplessiva.getCodTipoPenaDetentiva() != ""
     && (   PenaComplessiva.getCodTipoPenaDetentiva().equals("03")
         || PenaComplessiva.getCodTipoPenaDetentiva().equals("04")
        )
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
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aPenaResidua.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else
  {
%>
  <table width="80%">
    <tr>
      <td class=Titolonocap colspan=6 width=80%> Pena residua da espiare </td>
    </tr>

    <% 
    // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info("aPenaResidua = "+aPenaResidua.toString()) ; %>
    
    <tr>
      <td class="l"> Reclusione :
        Anni   <font class=campo><%=aPenaResidua.getNumAnniReclusione()%></font>
        Mesi   <font class=campo><%=aPenaResidua.getNumMesiReclusione()%></font>
        Giorni <font class=campo><%=aPenaResidua.getNumGiorniReclusione()%></font>
        Multa  <font class=campo><%=StringUtils.toEuroFormat(aPenaResidua.getImportoMulta() )%></font>
      </td>
      <td class="l"> Arresto :
        Anni    <font class=campo><%=aPenaResidua.getNumAnniArresto()%></font>
        Mesi    <font class=campo><%=aPenaResidua.getNumMesiArresto()%></font>
        Giorni  <font class=campo><%=aPenaResidua.getNumGiorniArresto()%></font>
        Ammenda <font class=campo><%=StringUtils.toEuroFormat(aPenaResidua.getImportoAmmenda() )%></font>
      </td>
    </tr>
      <% if(LAConcesse != null && LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>
      <% if(LADaConcedere!=null && LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>
</table>
<%
  }  // Fine Pena Residua
%>

<%
//==============================================================================
// Sezione con i dati del provvedimento
//==============================================================================
%>

<table width="80%">
  <tr>
    <td class="Titolo" colspan="100%" >Rideterminazione della pena</td>
  </tr>
      <tr>
        <td class="l">Motivo Provvedimento </td>
		<td class="l" colspan="4">
			<font class="campo">
				<%=StringUtils.toStringJSP(descMotivoRidetPena)%>
			&nbsp;</font>
  		  <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=motivoRidetPena%>">			
		</td>
    </tr>
  
</table>

<%
//==============================================================================
//        Sezione per specificare i quantum di rideterminazione
// n.b. è possibile inserire più quantum
//==============================================================================
%>
<table width="80%" >
  <tr>
    <td>
      <table width="100%">
        <tr>
          <td class="titolo" colspan="2">Reclusione</td>
          <td class="titolo" colspan="2">Arresto</td>
        </tr>
        <tr>
		<td valign="middle" class="c" >+/- <font class="ob">(*)</font><br>
			<select name="PM_0" id="PM_0">
             	<option value="+">+</option>
			</select>
		</td>
    
          <td class="c" align="left">
            <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Giorni</font><br>
            <input type="text" name="ARec_0" id="ARec_0" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(annotazione.getNumAnniReclusione(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="MRec_0" id="MRec_0" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(annotazione.getNumMesiReclusione(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="GRec_0" id="GRec_0" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(annotazione.getNumGiorniReclusione(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>

		<td valign="middle" class="c" >+/- <font class="ob">(*)</font><br>
			<select name="PMA" id="PMA">
             	<option value="+">+</option>
			</select>
		</td>
    
          <td class="c">
            <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
            <font class="label">Giorni</font><br>
            <input type="text" name="AArr_0" id="AArr_0" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(annotazione.getNumAnniArresto(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="MArr_0" id="MArr_0" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(annotazione.getNumMesiArresto(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">&nbsp;
            <input type="text" name="GArr_0" id="GArr_0" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(annotazione.getNumGiorniArresto(), "0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
        </tr>
        <tr>
          <td class="c" colspan="1"><font class="label">Motivazioni:</font></td>
          <td class="c" colspan="3"><textarea cols="100" rows="3" name="motivazioni_0"></textarea></td>
        </tr>
      </table>
    </td>
  </tr>
  
</table>



<!-- 
================================================================================
      Sezione con data Emissione, data trasmissione e magistrato
================================================================================
-->

<table width="80%">
  <tr>
    <td class="Titolo"  colspan="6"> Magistrato Firmatario </td>
  </tr>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L" >
      <input title = "Giorno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Mese Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%>>
      -
      <input title = "Anno Data Emissione" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%>>
    </td>
  </tr>
  <tr>
    <td class="l">Magistrato Firmatario</td>
    <td class="L">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
      <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
      <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
</table>

<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>