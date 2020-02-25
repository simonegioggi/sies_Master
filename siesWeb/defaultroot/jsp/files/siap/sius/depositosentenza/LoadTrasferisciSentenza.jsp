<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.jms.ICostantiJMS" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.ufficio.model.UfficiProvvedimentoModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>
<jsp:useBean id="datiSentenza" scope="request" class="siap.sius.depositosentenza.model.SentenzaEventoTenoriPrescrizioniModel"/>
<jsp:useBean id="tenoreEsito" scope="request" class="siap.sius.tenore.model.TenoreModel"/>
<jsp:useBean id="ufficioMagistratoComp" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="CSSA" scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="ufficiInteressati" scope="request" class="java.util.Vector"/>
<jsp:useBean id="postTitle" scope="request" class="java.lang.String"/>
<jsp:useBean id="UEPE" scope="request" class="java.lang.String"/>

<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Trasferimento Sentenza</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Lista Uffici per TIPO_UFFICIO
    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function Verify()
    {
      return true;
    }

  </script>

</head>
  <body class="corpo" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        lAction = "siap.sius.depositosentenza.action.ActConfermaTrasmissione";
        if(postTitle.compareTo("")==0)
        {%>
          <font class="campo">Trasferimento Sentenza</font>
      <%}else{%>
          <font class="campo"><%=postTitle%> </font>
      <%}%>
    </tr>
  </table>

  <br>
     <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadTrasferisciSentenza">
    <td> <font class="campo">Sentenza N. <%=StringUtils.toStringJSP(datiSentenza.getSentenza().getAnnoSentenza())%>/<%=StringUtils.toStringJSP(datiSentenza.getSentenza().getNumSentenza())%> </font> <font class="Label"> del </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiSentenza.getEvento().getDataEmissione(), "dd-MM-yyyy"))%> </font> <font class="Label"> depositata il </font> <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(datiSentenza.getSentenza().getDataDeposito(), "dd-MM-yyyy"))%> </font> </td>
    <br>

    <table width="100%" cellspacing=2 cellpadding=2>

      <tr>
        <td class="l"><font class="label">Data Deposito</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(datiSentenza.getSentenza().getDataDeposito(), "dd-MM-yyyy"))%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Natura Provvedimento</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tenoreEsito.getDescrEsitoTenore())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Oggetto Procedimento</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tenoreEsito.getDescrOggettoTenore())%>&nbsp;</font></td>
      </tr>
      <!-- <tr>
        <td class="l"><font class="label">Ufficio di Sorveglianza di</font></td>
        <td class="l"><font class="campo"><//%=StringUtils.toStringJSP(ufficioMagistratoComp.getDescrComune())%>&nbsp;</font></td>
      </tr> -->
      <!-- <tr>
        <td class="l"><font class="label">UEPE Competente</font></td>
        <td class="l"><font class="campo"><//%=StringUtils.toStringJSP(CSSA.getComune())%>&nbsp;</font></td>
      </tr> -->
      <!-- <tr>
        <td class="l"><font class="label">Luogo Svolgimento Prova</font></td>
        <td class="l"><font class="campo"><//%=StringUtils.toStringJSP(datiSentenza.getSentenza().getLuogoSvolgimentoProva())%>&nbsp;</font></td>
      </tr> -->
      <!-- 
      <tr>
        <td class="l"><font class="label">Servizio Terapeutico Competente</font></td>
        <td class="l"><font class="campo"><//%=StringUtils.toStringJSP(datiSentenza.getOrdinanza().getServizioTerapeuticoComp())%>&nbsp;</font></td>
      </tr> -->
      <tr><td>&nbsp;</td></tr>
    </table>

    <table cellspacing=2 cellpadding=2>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="l">Destinatario </td>
        <td class="L">
         <select Title="Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=uffici%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede Destinatario </td>
        <td class="L">
          <input title="Sede Destinatario" type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
            <a href="Javascript:ListaUfficiPerTipo('LoadTrasferisciSentenza','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>[0]',document.LoadTrasferisciSentenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0][document.LoadTrasferisciSentenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[0].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="l">UEPE Destinatario</td>
        <td class="L">
         <select Title="UEPE Destinatario" name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
           <%=UEPE%>
         </select>
        </td>
      </tr>
      <tr>
        <td class="l">Sede UEPE Destinatario </td>
        <td class="L">
          <input title="Sede UEPE Destinatario" type="text" name="<%= ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
            <a href="Javascript:ListaUfficiPerTipo('LoadTrasferisciSentenza','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>[1]',document.LoadTrasferisciSentenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1][document.LoadTrasferisciSentenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[1].selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IDEvento%>">
<%    if(postTitle.compareTo("")!=0)
        {%>
          <input type="HIDDEN" name="codTipoOperazione" value="<%=ICostantiJMS.TRASFERIMENTO_RICORSO%>">
      <%}%>
    </table>
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadTrasferisciSentenza");
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>
</html>