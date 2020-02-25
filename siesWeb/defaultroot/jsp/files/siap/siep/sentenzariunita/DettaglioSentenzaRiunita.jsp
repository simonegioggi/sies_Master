<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenzariunita.model.SentenzaRiunitaModel"%>
<%@ page import="siap.siep.sentenzariunita.action.ICostantiSentenzaRiunita"%>

<jsp:useBean id="sentenzariunita" scope="request" class="siap.siep.sentenzariunita.model.SentenzaRiunitaModel"/>

<jsp:useBean id="sentenza" scope="session" class="siap.siep.sentenza.model.SentenzaModel" />

<%
  SentenzaRiunitaModel lSentenza = new SentenzaRiunitaModel(sentenzariunita);
%>

<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Sentenze Riunita- </title>
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
          <font class="campo">Dettaglio Sentenze di Primo grado riunite in Appello</font>
        </td>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiSentenzaRiunita.CAMPO_ID_SENTENZA_RIUNITA%>" />
             <jsp:param name="ValoreIdEntita" value="<%=sentenzariunita.getIdSentenzaRiunita()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="campo"><%=sentenza.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        <%if(!sentenza.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
      </td>
    </tr>

  </table>
  <br>
  <table cellspacing=4 cellpadding=4>
  	<tr>
      <td class="l">Anno/Numero R.G.N.R.</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoRegePm())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroRegePm())%></font>&nbsp;
      </td>
      <td class="l"></td>
      <td class="L"></td>
      <td class="l"></td>
      <td class="L"></td>
		</tr>
  	<%
String reg="";
String anno_reg="";
String num_reg="";

if (lSentenza.getAnnoRegeCas()!=null)
{
  reg="CAS";
  anno_reg=lSentenza.getAnnoRegeCas()+"";
  num_reg=lSentenza.getNumeroRegeCas()+"";
}
if (lSentenza.getAnnoRegeDib()!=null)
{
  reg="DIB";
  anno_reg=lSentenza.getAnnoRegeDib()+"";
  num_reg=lSentenza.getNumeroRegeDib()+"";
}

if (lSentenza.getAnnoRegeGip()!=null)
{
  reg="GIP";
  anno_reg=lSentenza.getAnnoRegeGip()+"";
  num_reg=lSentenza.getNumeroRegeGip()+"";
}

if (!reg.equals(""))
{
%>
<tr>
      <td class="l">Numero Reg.Gen.</td>
      <td class="L"><font class="campo">
      <%=anno_reg%> / <%=num_reg%>&nbsp;&nbsp;&nbsp;  <%=reg%></font>
      </td>
      <td class="l"></td>
      <td class="L"> </td>
</tr>
<%}%>
	<tr>
		<td class="l">Sede PM</td>
		<td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lSentenza
							.getDescrSedeNotiziaReato())%></font>&nbsp;</td>
	</tr>
    <tr>
      <td class="l">Data Sentenza</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataSentenza(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno/Numero Sentenza</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoSentenza())%></font>&nbsp;
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
   	<tr>
      <td class="l">Luogo Emittente</td>
      <td class="L"  colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getSezioneAutoritaEmittente())%></font>&nbsp;
      </td>
		</tr>

  </table>
  <br>
  </body>

</html>