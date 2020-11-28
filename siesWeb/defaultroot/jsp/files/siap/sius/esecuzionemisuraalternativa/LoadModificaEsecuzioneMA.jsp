<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.esecuzionemisuraalternativa.action.ICostantiEsecuzioneMA" %>

<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel" %>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="misuraAlternativa" scope="request" class="siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel" />
<jsp:useBean id="fascicoloEsecuzione" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="misure" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  DettaglioFascicoloModel dettaglioFascSiep = (DettaglioFascicoloModel )request.getAttribute("dettaglioFascSiep");

  // STUB 06/03/2006
  //String lStrOrdDec = (misuraAlternativa.getDepOpidDepositoOrdinanzaPc()!= null) ? "Ordinanza N.ro : " : "Decreto N.ro : ";
  String lStrOrdDec = (fascicoloEsecuzione.getGeneraleProcedimentoModel().getCodTipoAtto().compareTo("04")==0 ) ? "Ordinanza N.ro : " : "Decreto N.ro : ";
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Modifica dell'Esecuzione Misura Alternativa</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Modifica dell'Esecuzione Misura Alternativa </font></td>

      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <table>
			<br>
      <tr>
        <td class="Titolo" colspan="3">Dati di dettaglio attuale dell'Esecuzione Misura Alternativa </td>
      </tr>
      <tr>
        <td class="cVerde">N.ro Procedimento M.A. : <font class="cVerde"><%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloEsecuzione.getFascicoloSiusModel().getChiaveProgr()%></font></td>
        <td class="c" colspan="2"> relativo a: <font class="campo"><%=misuraAlternativa.getDescrTipoMisura() %></font></td>
      </tr>
      <tr>
        <td class="c"><%=lStrOrdDec%> <font class="campo"><%=misuraAlternativa.getAnnoS07()%>/<%=misuraAlternativa.getProgrS07()%>
        <td class="c" colspan="2"><font class="campo"> <%=misuraAlternativa.getDescrTipoAutoritaEmittOrd()%> - <%=misuraAlternativa.getDescrLuogoAutoritaEmittOrd()%>
        </font><font class="label"> del: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraAlternativa.getDataOrdinanza(),"dd-MM-yyyy"),"-")%>
      </font></td> </font></td>
      </tr>
      <tr>
        <td class="c">Soggetto: <font class="campo"><%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getCognome()%>&nbsp;
        <%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getNome()%>
        <td class="c" colspan="2"><font class="label"> nato/a il: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%>
        </font><font class="label"> in : </font><font class="campo"><%=fascicoloEsecuzione.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita()%>
        </font></td> </font></td>
      </tr>

<%    if (dettaglioFascSiep != null) {
%>
        <tr>
          <td class="c">Titolo Esecutivo N.ro Siep : <font class="campo"><%=dettaglioFascSiep.getFascicoloSiep().getChiaveAnno()%>/<%=dettaglioFascSiep.getFascicoloSiep().getChiaveProgr()%></font></td>
          <td class="c" colspan="2"><font class="campo"> <%=dettaglioFascSiep.getFascicoloSiep().getDescrTipoUfficio()%> - <%=dettaglioFascSiep.getFascicoloSiep().getDescrComuneUfficio()%>
          </font><font class="label"> del: </font><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(dettaglioFascSiep.getFascicoloSiep().getDataIscrizione(),"dd-MM-yyyy"),"-")%>
          </font></td>
        </tr>
      <%}%>

      <tr>
        <td class="c">Data inizio misura: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraAlternativa.getDataInizioMisura(),"dd-MM-yyyy"),"-")%>
        </font></td>
        <td class="c">Data termine misura (iniziale): <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraAlternativa.getDataTermineIniziale(),"dd-MM-yyyy"),"-")%>
        </font></td>
        <td class="c">Data termine misura (corrente): <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraAlternativa.getDataTermineAttuale(),"dd-MM-yyyy"),"-")%>
        </font></td>
      </tr>

      <tr>
        <td class="c">Luogo esecuzione da Ordinanza: <font class="campo">
<%
          if (misuraAlternativa.getLuogoEsecuzioneMisura()!=null    &&
              misuraAlternativa.getLuogoEsecuzioneMisura().trim().length()>1)
          {%>
            <%=misuraAlternativa.getLuogoEsecuzioneMisura().trim()%>
          <%}else{%>-<%}%>
        </font></td>
<%
        String strLuogoEsecuzione="";
        Iterator itx1 = misure.iterator();
        while ( itx1.hasNext())
        {
          FascicoloGPModel fascicoloGP = (FascicoloGPModel)itx1.next();
          if (fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione()!=null &&
              fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione().trim().length()>1)
          strLuogoEsecuzione=fascicoloGP.getGeneraleProcedimentoModel().getDescrRichiestaDelegazione();
        }
        if (strLuogoEsecuzione.length()>1)
        {%>
          <td class="c" colspan="2">Luogo esecuzione corrente: <font class="campo"><%=strLuogoEsecuzione%>
          </font></td>
      <%}%>
      </tr>
    </table>
  </table>

  <br>

  <table cellspacing=0 cellpadding=0>
      <tr>
        <td class="Titolo" colspan="2">Dati dell'Esecuzione Misura Alternativa in modifica </td>
      </tr>
      <tr>
        <td class="l">Data inizio misura </td>
        <td class="L">
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataInizioMisura(),"dd")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_GIORNO_INIZIO_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataInizioMisura(),"MM")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_MESE_INIZIO_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataInizioMisura(),"yyyy")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_ANNO_INIZIO_MISURA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

      <tr>
        <td class="l">Data termine iniziale </td>
        <td class="L">
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineIniziale(),"dd")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_GIORNO_TERMINE_INIZIALE%>" maxlength="2" size="2" Title="Data Termine Iniziale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineIniziale(),"MM")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_MESE_TERMINE_INIZIALE%>" maxlength="2" size="2" Title="Data Termine Iniziale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineIniziale(),"yyyy")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_ANNO_TERMINE_INIZIALE%>" maxlength="4" size="4" Title="Data Termine Iniziale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>

      <tr>
        <td class="l">Data termine attuale </td>
        <td class="L">
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineAttuale(),"dd")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_GIORNO_TERMINE_ATTUALE%>" maxlength="2" size="2" Title="Data Termine Attuale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineAttuale(),"MM")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_MESE_TERMINE_ATTUALE%>" maxlength="2" size="2" Title="Data Termine Attuale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(misuraAlternativa.getDataTermineAttuale(),"yyyy")) %>" type="text" name="<%=ICostantiEsecuzioneMA.CAMPO_ANNO_TERMINE_ATTUALE%>" maxlength="4" size="4" Title="Data Termine Attuale" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>
      <tr>
        <td class="l">Luogo esecuzione misura </td>
        <td class="L">
          <input name="<%=ICostantiEsecuzioneMA.CAMPO_LUOGO_ESECUZIONE_MISURA%>"type="text" maxlength="200" size="50" Title="Luogo Esecuzione Misura"
<%          if ( misuraAlternativa.getLuogoEsecuzioneMisura() !=null )
            {
%>            value="<%=misuraAlternativa.getLuogoEsecuzioneMisura()%>"
          <%}%>>

        </td>
      </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionemisuraalternativa.action.ActModificaEsecuzioneMA" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA%>" value="<%=misuraAlternativa.getIdEsecuzioneMisuraAlternati()%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" value="<%=fascicoloEsecuzione.getFascicoloSiusModel().getIdFascicoloSius()%>" >
    <input type="HIDDEN" name="TornaQui" value="<%=TornaQui%>" >
    <input type="HIDDEN" name="<%=ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP%>"
<%  if ( dettaglioFascSiep !=null )
    {%>value="<%=dettaglioFascSiep.getFascicoloSiep().getIdFascicoloSiep()%>"
  <%}%> >
  </FORM>
  </body>
</html>