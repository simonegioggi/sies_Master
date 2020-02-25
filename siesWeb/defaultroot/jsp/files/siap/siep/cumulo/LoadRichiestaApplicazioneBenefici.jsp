<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="TipoAnnotazioneManuale" scope="request" class="java.lang.String" />
<jsp:useBean id="listaDPR"               scope="request" class="java.lang.String"/>
<jsp:useBean id="PenaComplessiva"        scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="IdPenaResidua"          scope="request" class="java.lang.String" />

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
  <title> [S.I.E.S.] - Richiesta Applicazione Benefici - </title>
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

      if(document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value == "")
	  {
		  alert("La Data Richiesta è obbligatoria")
	      document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.focus();
	      return false;
	  }
    	
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

      return true;
    }

  </script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActRichiestaApplicazioneBenefici">
  <input type="HIDDEN" name="IdPenaResidua" value="<%=IdPenaResidua%>">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta Applicazione Benefici</font>
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
        <input type="checkbox" name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>" value="A" >
      </td>
    </tr>
  </table>

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
 if (!lIsErgastolo) {
  // Sezione visualizzata solo nel caso di soggetto in espiazione ma non in
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
      </td>
    </tr>
<% } %>

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
  </tr>
</table>

<table>
   <tr>
      <td class="l">
         <input type="checkbox" name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_BENEFICIO_DETRATTO%>" value="S">
         &nbsp; 
         Beneficio già detratto dal Quantum di Pena
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

        frmvalidator.setAddnlValidationFunction("Verify");
	</script>
</body>
</html>