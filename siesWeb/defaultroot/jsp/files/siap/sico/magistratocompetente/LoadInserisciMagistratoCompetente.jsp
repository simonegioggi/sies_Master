<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"      scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicolo"       scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function Verify()
      {
        if(document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");

          return false;
        }

        if(document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");

          return false;
        }

        if (document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value;
		    if (document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value;

		    var data_to_verify = document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data di inzio competenza non valida');

          return false;
  		  }

        if ((data_to_verify == document.LoadInserisciMagistratoCompetente.DataVecchia.value) && (document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO%>.value==document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value))
        {
          alert('Nessuna Modifica Richiesta');

          return false;
        }
        //inserisco il parametro return true per disattivare il pulsante "conferma" del form
        return true;
      }

      function ListaMagistrati(a_formname)
      {
        var a_codnum = document.LoadInserisciMagistratoCompetente.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>.value;

        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum="+a_codnum, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
  </script>
  </head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAzione = new String();

        if( modalita.equals("I") )
        {
          lAzione = "siap.sico.magistratocompetente.action.ActInserisciMagistratoCompetente";
%>
          <font class="campo">Assegnazione/Cambio Magistrato</font>
<%
        }
        else if( modalita.equals("M") )
        {
           //  lProvvedimento = new EventoModel(evento);
           //  lAzione = "siap.siep.ordineesecuzione.action.ActModificaOEDetenutoQC";
%>
          <font class="campo">Modifica di Ordine di Esecuzione </font>
<%
        }
%>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciMagistratoCompetente" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.magistratocompetente.action.ActInserisciMagistratoCompetente">
    <table>
     <tr>
       <td class="Titolo" colspan=6> Magistrato Assegnatario </td>
     </tr>
     <tr>
       <td class="l">Magistrato</td>
       <td class="L">
         <input title="Cognome Magistrato" readonly value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>"  type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>"   maxlength="35" size="25" >
         <input title= "Nome Magistrato" readonly value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>"  type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"        maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('LoadInserisciMagistratoCompetente');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
     </tr>
     <tr>
       <td class="l">Data Inizio Competenza</td>
       <td class="L">
<%
        if (magistrato.getMagistratoCompetente().getDataInizio() != null && !magistrato.getMagistratoCompetente().getDataInizio().toString().equals(""))
        {
%>
          <input value="<%=DateUtils.getDateToString(magistrato.getMagistratoCompetente().getDataInizio(), "dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getDateToString(magistrato.getMagistratoCompetente().getDataInizio(), "MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getDateToString(magistrato.getMagistratoCompetente().getDataInizio(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
        }
        else
        {
          if( fascicolo.getDataIscrizione() != null )
          {
%>
            <input value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getDateToString(fascicolo.getDataIscrizione(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
          }
          else
          {
%>
            <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
          }
        }
%>
      </td>
      <td>
        <input type="HIDDEN" title="CodiceMagistratoVecchio" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>"  maxlength="35" size="35">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
        <input type="HIDDEN" title="DataVecchia" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoCompetente().getDataInizio(),"dd/MM/yyyy")) %>" type="text" name="DataVecchia">
        <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
      </td>
    </tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("LoadInserisciMagistratoCompetente");

  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Cognome del Magistrato è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Nome del Magistrato è obbligatorio");

  frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO%>","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>