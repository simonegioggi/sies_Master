<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regesoggetto.action.ICostantiRegeSoggetto" %>
<%@ page import="siap.regesies.regesoggetto.model.RegeSoggettoModel" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="regesoggetto" scope="request" class="siap.regesies.regesoggetto.model.RegeSoggettoModel"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String"/>

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
          <font  class="label">Funzione :&nbsp;</font><font class="campo">
          <%if(AzioneChiamante!=null && AzioneChiamante.length()==0){%>
          Dettaglio Rege Soggetto
          <%}else{
            %>
           Dettaglio Soggetto SIEP Omonimo
          <%}%>
          </font>
        </td><td class="LBG">
           <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeSoggetto.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=regesoggetto.getIdFile()%>" />
            </jsp:include>
      </td>
      <td class="LBG">
      <a href="javascript:history.go(-1)">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
      </tr>
    </table>
  </FORM>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE%>"/>

  <table cellspacing=2 cellpadding=2>
   <%if(AzioneChiamante!=null && AzioneChiamante.length()==0){%>
        <tr><td class=titolo colspan=2> Rege Soggetto</td></tr>
          <%}else{ %>
    <tr><td class=titolo colspan=2>Soggetto SIEP Omonimo</td></tr>
     <%}%>
   <tr>
      <td class="l" width="25%"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=regesoggetto.getCognome() %>&nbsp;&nbsp;<%=regesoggetto.getNome() %></font></td>
    </tr>
    <tr>
      <td class="l" width="25%"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=regesoggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(regesoggetto.getDataNascita(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>

    </tr>
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(regesoggetto.getDescrComuneNascita() )%>
<%        if( (regesoggetto.getDescrComuneNascita() != null)
              && (!(regesoggetto.getDescrComuneNascita().equals("")))
              && (!(regesoggetto.getDescrComuneNascita().equals("-"))) )
          {
%>
            (<%=regesoggetto.getCodProvinciaNascita()%>)
<%
          }
%>
          &nbsp;
        </font>
      </td>
    </tr>
	<tr>
	  <td class="l"><font class="label">Nazionalità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getNazionalita())%>&nbsp;</font></td>
  </tr>
	<tr>

      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getDescrStatoNascita())%>&nbsp;</font></td>
   </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(regesoggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getCodFiscale())%>&nbsp;</font></td>
     </tr>
    <tr>
    <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>

    <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getCodAfis() )%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(regesoggetto.getNote())%>&nbsp;</font></td>
    </tr>


  <br>

  </table>
  </body>
</html>