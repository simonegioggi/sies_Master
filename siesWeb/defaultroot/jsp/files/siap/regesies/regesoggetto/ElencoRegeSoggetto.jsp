<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regesoggetto.action.ICostantiRegeSoggetto" %>
<%@ page import="siap.regesies.regesoggetto.model.RegeSoggettoModel" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="soggetti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="provvedimento" scope="request" class="siap.regesies.regesentenza.model.RegeSentenzaModel"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Soggetto - </title>
  </head>
<BODY class="corpo">
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Elenco Soggetti ReGe</font>
        </td>
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>
  </FORM>
  <table>
  <tr>
    <td class="LBGISI" width="15%" align="center" >
     <font class="campo"><%=provvedimento.getDescrTipoProvvedimento()%> <br>ReGe</font>
 </td>
<td>
 <table width="100%"><tr><td class="L" colspan=2>
         <font class="label">N. </font>
         <font class="campo"> <%=provvedimento.getAnnoSentenza()%>/<%=provvedimento.getNumeroSentenza()%> </font>
         <font class="label" > del </font>
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
         <font class="label" > emessa da </font>
         <font class="campo"><%=StringUtils.toStringJSP(provvedimento.getDescrTipoAutoritaEmittente())%></font>  <font class="label" > di </font>  <font class="campo" ><%=StringUtils.toStringJSP(provvedimento.getDescrLuogoEmittente())%></font>&nbsp;
    </td></tr>
   </table>
   </td>
   </tr>
   </table>
   <br>   <br>
  <table cellspacing=2 cellpadding=2>

  <tr><td class=titolo colspan=8>Elenco Rege Soggetti</td></tr>

   <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Sesso</td>
      <td class="int">Data di nascita</td>
      <td class="int">Comune Nascita</td>
      <td class="int">Stato Nascita</td>
      <td class="int">Comune Di Nascita Estero</td>
      <td class="int">Azione</td>

    </tr>

    <%Iterator lItx = soggetti.iterator();
    while(lItx.hasNext())
    {
      RegeSoggettoModel regesoggetto = (RegeSoggettoModel)lItx.next();
      %>
    <tr>
      <td class="l"><font class="campo"><%=regesoggetto.getCognome() %></font></td>
      <td class="l"><font class="campo"><%=regesoggetto.getNome() %></font></td>
      <td class="c"><font class="campo"><%=regesoggetto.getSesso()%></font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(regesoggetto.getDataNascita(),"dd-MM-yyyy"))%>&nbsp;
      </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(regesoggetto.getDescrComuneNascita() )%>
<%        if( (regesoggetto.getDescrComuneNascita() != null)
              && (!(regesoggetto.getDescrComuneNascita().equals("")))
              && (!(regesoggetto.getDescrComuneNascita().equals("-"))) )
          {%>(<%=regesoggetto.getCodProvinciaNascita()%>)<%          }%>
          &nbsp;
        </font>
      </td>
 	    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getDescrStatoNascita())%>&nbsp;</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
      <td class="c">
          <jsp:include page="<%=ICostantiRegeSies.PAGE_BUTTONS_REGE%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeSoggetto.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=regesoggetto.getIdFile()%>" />
          </jsp:include>
      </td>
  </tr>
      <%}%>
  </table>
  </body>
</html>