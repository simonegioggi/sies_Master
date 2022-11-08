<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<%

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

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
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function Verify()
      {
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>.value;
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>.value;
  
        var data_to_verify = document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE%>.value;
  
        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione non valida');
          return false;
        }
  
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>.value;
        if (document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>.value.length==1)
          document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>.value='0'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>.value;
  
        var data_to_verifica = document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>.value+'/'+document.LoadInserisciOrdineEsecuzione.<%=ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO%>.value;
  
        if (!ControllaData(data_to_verifica) )
        {
          alert('Data fine pena non valida');
          return false;
        }
      }
    </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione:</font>&nbsp;&nbsp;
        <font class="campo">Inserimento Variazione Decorrenza/Scadenza</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <form method="POST" name="LoadInserisciOrdineEsecuzione" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActLoadInserisciVariazioneDecorrenzaScadenza">
    
    <%
    //==========================================================================
    // Visualizzazione Posizione Giuridica e Luogo di Detenzione
    //==========================================================================
    %>
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
              DETENUTO PER ALTRA CAUSA <%=posizioneluogoaltra.getAltraCausa().getDescrTipoPosGiuridica()%>
          </font>
        </td>
      </tr>
      
      <% if(lAltraCausa.getIstitutoDetenzione() != null) { %>
      <tr>
        <td class="l">Detenuto presso </td>
        <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
        </td>
      </tr>
      <% } %>

      <% if (lAltraCausa.getAltroLuogo()!=null) { %>
      <tr>
        <td class="l">Altro Luogo </td >
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
        </td>
      </tr>
      <% } %>
      
      <%
      //========================================================================
      //              Pena Residua: Quantum 
      //========================================================================
      %>
      <tr>
      <%
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
      </tr>
      <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
      </tr>
      
      
      <tr>
<%
      //========================================================================
      //              Pena Residua: Decorrenza/Scadenza
      //========================================================================
  
//       if (false) { // PER ORA NON LO FACCIO ENTRARE QUI
//        if (penaresidua.getDataInizio() != null) {
%>
<!--          <td class="l">Data Decorrenza Pena</td> -->
<%--          <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td> --%>
<%-- <% --%>
<!-- //        } -->
<!-- //        if (penaresidua.getFlagErgastolo() != null) -->
<!-- //        { -->
<!-- //          if(penaresidua.getFlagErgastolo().equals("S")) -->
<!-- //          { -->
<%-- %> --%>
<!--           <td class="l">Pena Detentiva</td> -->
<!--           <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td> -->
<%-- <% --%>
<!-- //          } -->
<!-- //          else -->
<!-- //          if(penaresidua.getFlagErgastolo().equals("D")) -->
<!-- //          { -->
<%-- %> --%>
<!--           <td class="l">Pena Detentiva</td> -->
<!--           <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td> -->
<%-- <% --%>
<!-- //          } -->
<!-- //        } -->
<!-- //        if (    (    penaresidua.getFlagErgastolo() == null) -->
<!-- //             || (   penaresidua.getFlagErgastolo() != null -->
<!-- //                 && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D") -->
<!-- //                ) -->
<!-- //           ) -->
<!-- //        { -->
<!-- //         if( penaresidua.getDataFine() != null) -->
<!-- //         { -->
<!-- //           if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) -->
<!-- //           { -->
<%-- %> --%>
<!--             <td class="l">Data Fine Pena</td> -->
<!--             <td class="L" colspan=2> -->
<%--               <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO %>"> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO %>"> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO %>"> --%>
<!--             </td> -->
<%-- <% --%>
<!-- //           } -->
<!-- //           else -->
<!-- //           { -->
<%-- %> --%>
<!--             <td class="l">Data Fine Pena</td> -->
<!--             <td class="lRosso" colspan=2> -->
<%--               <font class="lRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO %>"> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO %>"> --%>
<%--               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO %>"> --%>
<!--             </td> -->
<%-- <% --%>
<!-- //           } -->
<!-- //         } -->
<!-- //       } -->
<!-- //   }  -->
<%-- %>                --%>
       </tr>
       </table>
       
      <table width=100%>
        <tr><td class="Titolo"> Variazione Decorrenza/Scadenza </td></tr>
      </table>
      
      <table>
        <tr>
          <td class="l">Data pervenimento richiesta variazione</td>
          <td class="L">
            <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input title="Anno Data Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
        
        <tr HEIGHT=100>  
          <td class="l">Motivazioni: </td>
          <td class="L">
            <TEXTAREA title="Motivazioni" name="<%=ICostantiOrdineEsecuzione.CAMPO_MOTIVAZIONI_VARIAZIONE%>" cols=50 ROWS=4></textarea>
          </td>
        </tr>
        
        <tr> 
          <td class="l">Nuova Data Fine Pena altro procedimento</td>
          <td class="L">
            <input value=""   type="text" size="2" maxlength="2" name="<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
            <input value=""   type="text" size="2" maxlength="2" name="<%= ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > -
            <input title="Anno Data Fine Pena" value="" type="text" size="4" maxlength="4" name="<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
     
        <tr><td>&nbsp;</td></tr>

        <tr>
          <td class="lNoBord" colspan="2">
            <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
          </td>
        </tr>
      </table>
      
      <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    </form>
    
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciOrdineEsecuzione");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_PERVENIMENTO_VARIAZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_MESE_PERVENIMENTO_VARIAZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_PERVENIMENTO_VARIAZIONE%>","lt=2099");


  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiOrdineEsecuzione.CAMPO_GIORNO_FINE_ALTRO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_MESE_FINE_ALTRO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiOrdineEsecuzione.CAMPO_ANNO_FINE_ALTRO%>","lt=2100");

</script>
</body>
</html>