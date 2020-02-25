<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="regesentenza" scope="request" class="siap.regesies.regesentenza.model.RegeSentenzaModel"/>
<jsp:useBean id="NomeAzione" scope="request" class="java.lang.String" />

<% RegeSentenzaModel lSentenza = new RegeSentenzaModel(regesentenza);%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Rege Sentenza - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>">
    </script>
  </head>

  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Rege Sentenza</font>
        </td>
         <td class="LBG">
          <jsp:include page="<%=ICostantiRegeSies.PG_TOOLBAR_REGE_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=lSentenza.getIdFile()%>" />
             <jsp:param name="TornaIndietro" value="SI"/>
           </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>
    <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_SOLO_INCLUDE%>"/>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Data Arrivo Atto</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  	<tr>
      <td class="l">Data Inserimento</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataInserimento(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Iscrizione</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
  	<tr>
      <td class="l">Numero R.G.N.R.</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoRegePm())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%></font>&nbsp;
      </td>
    </tr>
    <tr>
<%
String reg="";
String anno_reg="";
String num_reg="";
if (lSentenza.getAnnoRegeCap()!=0)
{
  reg="CAP";
  anno_reg=lSentenza.getAnnoRegeCap()+"";
  num_reg=lSentenza.getNumeroRegeCap()+"";
}
if (lSentenza.getAnnoRegeCas()!=0)
{
  reg="CAS";
  anno_reg=lSentenza.getAnnoRegeCas()+"";
  num_reg=lSentenza.getNumeroRegeCas()+"";
}
if (lSentenza.getAnnoRegeDib()!=0)
{
  reg="DIB";
  anno_reg=lSentenza.getAnnoRegeDib()+"";
  num_reg=lSentenza.getNumeroRegeDib()+"";
}
if (lSentenza.getAnnoRegeCasap()!=0)
{
  reg="CASAP";
  anno_reg=lSentenza.getAnnoRegeCasap()+"";
  num_reg=lSentenza.getNumeroRegeCasap()+"";
}
if (lSentenza.getAnnoRegeGip()!=0)
{
  reg="GIP";
  anno_reg=lSentenza.getAnnoRegeGip()+"";
  num_reg=lSentenza.getNumeroRegeGip()+"";
}

if (!reg.equals(""))
{
%>
    <td class="l">Numero Reg.Gen.</td>
      <td class="L"><font class="campo">
      <%=anno_reg%> / <%=num_reg%>&nbsp;&nbsp;&nbsp;  <%=reg%></font>
      </td>
<%}%>
    </tr>
<tr><td class="Titolo" colspan=6>Sentenza da Eseguire</td> </tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="L">
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoSentenza())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
      </td>
		</tr>
<%
      if(lSentenza.getCodTipoAutoritaEmittente().equals("DIB") || lSentenza.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
%>
      <tr>
      <td class="l">Tipo Rito</td>
      <td class="L"  colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoRito())%></font>&nbsp;
      </td>
      </tr>
<%
      }
%>
   	<tr>
      <td class="l">Luogo Emittente</td>
      <td class="L"  colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente())%></font>&nbsp;
      </td>
		</tr>
    <tr>
      <td class="l">Sentenza di Applicazione Pena</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getFlagSentenzaApplicazPena())%></font>&nbsp;
      </td>
		</tr>
<tr><td class="Titolo" colspan=6>Altro Grado di Giudizio</td> </tr>
    <tr>
      <td class="l">Tipo Sentenza</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoProvvRif())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvRif(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoProvvRif())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroProvvRif())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="L"  colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaProvvRif())%></font>&nbsp;
      </td>
	</tr>
<%
      if(lSentenza.getCodTipoAutoritaProvvRif().equals("DIB") || lSentenza.getCodTipoAutoritaProvvRif().equals("TRIBSD")) {
%>
      <tr>
      <td class="l">Tipo Rito</td>
      <td class="L"  colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoRito())%></font>&nbsp;
      </td>
      </tr>
<%
      }
%>
    <tr>
      <td class="l">Luogo Emittente</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoProvvRif())%></font>&nbsp;
      </td>
		</tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=5>
         <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaProvvRif())%></font>&nbsp;
      </td>
		</tr>
<tr><td class="Titolo" colspan=6>Sentenza della Cassazione</td> </tr>
     <tr>
      <td class="l">Anno/Numero Reg.Gen. Cassazione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote1DecisioneCassazione())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote2DecisioneCassazione())%></font>&nbsp;
      </td>
	</tr>
    <tr>
      <td class="l">Anno/Numero Sentenza Cass.</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoSentenzaCassazione())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione())%></font>&nbsp;
      </td>
	</tr>
    <tr>
      <td class="l">Anno/Numero Raccolta Generale</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.intZerotoString(lSentenza.getAnnoRaccoltaGenerale())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Dispositivo Cassazione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoDecisioneCassazione())%></font>&nbsp;
      </td>
    </tr>
<tr><td class="Titolo" colspan=6>Irrevocabilità</td> </tr>
    <tr>
      <td class="l">Data Irrevocabilità</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Numero Ufficio Recupero Crediti</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrNumCampionePenale())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
      </td>
    </tr>

  <br>

  </table>
  </body>
</html>