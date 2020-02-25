<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<jsp:useBean id="scadenzario" 			scope="request" class="siap.siep.scadenzario.model.ScadenzarioModel" />

<%
//==============================================================================
//Form utilizzata nel dettaglio SCADENZARIO
//(In particolare per le Pene Sospese - Termine Ottemperanza Obblighi) 
//==============================================================================
%>

<html>
	<head>
		<title>[S.I.E.S.] - Dettaglio Stato Notifiche - Ottemperanza Obblighi </title>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
<body class="corpo">
	<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        	<font class="label">Funzione :</font>&nbsp;
        	<font class="campo">Dettaglio Stato Notifiche - Ottemperanza Obblighi</font>
      	</td>
 				<td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiScadenzario.CAMPO_ID_SCADENZARIO%>" />
          	<jsp:param name="ValoreIdEntita" value="<%=scadenzario.getIdScadenzario()%>" />
       		</jsp:include>
     		</td>
        <td class="LBG">
          <a href="javascript:history.go(-1);">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
     	</tr>
 		</table>
	</FORM>
<%
	ScadenzarioModel lSca = scadenzario;
	
	FascicoloSiepModel lFas = lSca.getFascicoloModel();
	SoggettoModel lSog = lFas.getSoggetto();
	SentenzaModel lSentMod = lFas.getSentenza();
%>
	<table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFas.getIdFascicoloSiep()%>" title="Procedimento">
          <%=lFas.getChiaveAnno()%>
          /
          <%=lFas.getChiaveProgr()%>
        </a>
      </td>
    </tr>
    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSog.getIdSoggetto()%>" title="Soggetto">
          <%=lSog.getCognome()%>&nbsp;<%=lSog.getNome()%>
        </a>
      </font>&nbsp;
<%
        if (lSog.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }
%>
      <font class="campo"><%=DateUtils.getDateToString(lSog.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (lSog.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=lSog.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=lSog.getDescrComuneNascita()+ "  ("+lSog.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
    <tr>
      <td class="L">
        <font class="campo"><%=lSentMod.getDescrTipoProvvedimento()%></font>&nbsp;<font class="label">N.</font>
        <font class="campo">
          <%=lSentMod.getAnnoSentenza()%> / <%=lSentMod.getNumeroSentenza()%>&nbsp;
          <font class="label">del</font>&nbsp;
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lSentMod.getIdSentenza()%>" title="Sentenza">
            <%=DateUtils.getDateToString(lSentMod.getDataProvvedimento(), "dd-MM-yyyy")%>
          </a>
        </font>
        <%if(!lSentMod.getCodTipoProvvedimento().equals("02")) { %> &nbsp;<font class="label"> Emessa da: </font> <% 
 		}else{%>&nbsp;<font class="label"> Emesso da: </font><%} %>
        <font class="campo"><%=lSentMod.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (lSentMod.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=lSentMod.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=lSentMod.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Data irrevocabilità : </font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(lFas.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
      </td>
    </tr>
  </table>
  <br>
   <table cellspacing=2 cellpadding=2>


          <tr>
                          <td class="l">N° SIEP</td>
                          <td class="l"><font class="campo"><%=lFas.getChiaveAnno() %>/<%=lFas.getChiaveProgr() %></font></td>
          </tr>
          <tr>
                          <td class="l">Cognome</td>
                          <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSog.getCognome()) %></font>&nbsp;</td>
          </tr>
          <tr>
                        <td class="l">Nome</td>
                        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSog.getNome()) %></font>&nbsp;</td>
          </tr>
          <tr>
                        <td class="l">Data di Nascita</td>
                        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSog.getDataNascita(),"dd/MM/yyyy")) %></font>&nbsp;</td>
          </tr>
          <tr>
                        <td class="l">Luogo di Nascita</td>
                        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lSog.getDescrComuneNascita())%></font></td>
          <tr>
                        <td class="l">Data Notifica</td>
                        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataInizioScadenza(),"dd/MM/yyyy")) %></font>&nbsp;</td>
          </tr>
          <tr>
                        <td class="l">Data Scadenza</td>
                        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSca.getDataFineScadenza(),"dd/MM/yyyy")) %></font>&nbsp;</td>
          </tr>

  </table>

</body>
</html>