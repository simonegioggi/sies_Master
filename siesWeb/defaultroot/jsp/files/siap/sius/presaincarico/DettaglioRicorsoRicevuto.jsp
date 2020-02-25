<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>

<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="eventoNotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="tenoreEsito" scope="request" class="siap.sius.tenore.model.TenoreModel"/>
<jsp:useBean id="impugnazione" scope="request" class="siap.sius.impugnazione.model.ImpugnazioneModel"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Ricorso Ricevuto </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Ricorso Ricevuto</font>
          </td>
         </tr>
      </table>
    </FORM>
<%
  //SoggettoModel soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();
  SoggettoModel soggetto = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto();
%>
  <table cellspacing=2 cellpadding=2 width=85%>
    <tr><td class="Titolo" colspan=4>Fascicolo SIUS</td></tr>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgr()%>
          - <%=fascicoloSiusGP.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiusGP.getFascicoloSiusModel().getDescrComuneUfficio()%><BR>
        </font>
        <font class="label"> relativo a: </font>
        <font class="campo">
          <%=fascicoloSiusGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font>
       </td>
    </tr>

    <tr>
      <td class="L" width=100%><font class="label">Soggetto:</font>
      <font class="campo">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
        </a>
      </font>&nbsp;
<%
        if(soggetto.getSesso().compareTo("F")==0)
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
      <font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
      <font class="label">in : </font>
      <font class="campo">
<%
      if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {
%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {
%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
<%
      }
%>
      </font>
     </td>
    </tr>
<%  if ((fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP())!=null)
    {
%>
      <tr>
        <td class="L">
          <font class="label">Titolo Esecutivo: N.ro SIEP </font>

          <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep()%>">
              <%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveAnnoSIEP()%>/<%=fascicoloSiusGP.getFascicoloSiusModel().getChiaveProgrSIEP()%>
            </a>
          </font>&nbsp;
          <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <font class="campo"><%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%></font><font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicoloSiusGP.getFascicoloSiusModel().getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
        </td>
      </tr>
<%
    }
%>
    <tr>
      <td class="L">
        <font class="label">Data Udienza : </font>
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString( fascicoloSiusGP.getGeneraleProcedimentoModel().getDataCameraConsiglio(), "dd-MM-yyyy"), "-" )%></font>
        </font>
      </td>
    </tr>
  </table>
  <br>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ConfermaPresaInCaricoRicorso" onSubmit="document.ConfermaPresaInCaricoRicorso.but.disabled=true;">
  <table cellspacing=2 cellpadding=2 width="85%">

  <tr><td class="Titolo" colspan=4><%=impugnazione.getDescrTipoImpugnazione()%></td></tr>
   <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoNotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l"><font class="label">Natura Provvedimento</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tenoreEsito.getDescrEsitoTenore())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Oggetto Procedimento</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(tenoreEsito.getDescrOggettoTenore())%>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l">Note</td>
      <td class="L" colspan=2>
       <TEXTAREA title="note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>" cols=80 rows=5></textarea>
      </td>
    </tr>
  </table>
  <br>
  <table cellspacing=2 cellpadding=2 width=95%>
      <tr>
        <td>
          <input class=bottone  name="but" type="submit" value="Conferma Presa in Carico">
        </td>
      </tr>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActConfermaPresaInCaricoRicorso">
      <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
  </table>
  </form >
</body>
</html>