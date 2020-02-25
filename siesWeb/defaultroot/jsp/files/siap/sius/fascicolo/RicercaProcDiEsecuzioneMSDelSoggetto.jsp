<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="ufOTribunale" scope="request" class="java.lang.String" />
<jsp:useBean id="codDistretto" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="descrContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalInCancelleria" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlInCancelleria" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti di Esecuzione Misure Sicurezza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti di Esecuzione Misure Sicurezza per Soggetto </font></td>


<%
       FascicoloGPModel fascicoloUno = (FascicoloGPModel) fascicoli.get(0);
%>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>

    </tr>
     <tr> </tr>
     <tr> </tr>

     <tr>
       <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
    </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
<%
    if(!(codDistretto.equals("") && ufOTribunale.equals("") &&
       lIncludeArchiviati.equals("") && codContenuto.equals("-") &&
       (dataDalInCancelleria.equals("")) && (dataAlInCancelleria.equals("")) ))
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(codDistretto.length()==1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizzazione di tutti i procedimenti di Esecuzione Misure Sicurezza, compresi quelli pervenuti da altri Distretti</td>
        </tr>
<%    }
      if(codDistretto.length()> 1 )
      {
%>
        <tr>
          <td class="lVerdeNB">Visualizzazione dei procedimenti di Esecuzione M. S. dell'intero Distretto</td>
        </tr>
<%    }
      if(!lIncludeArchiviati.equals(""))
      {
%>
        <tr>
          <td class="lVerdeNB">Anche i procedimenti definiti</td>
        </tr>
<%    }
      if(!codContenuto.equals("-"))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti di : <%=descrContenuto%></td>
        </tr>
<%    }
      if(!(dataDalInCancelleria.equals(""))||!(dataAlInCancelleria.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti di Esecuzione S.S. con Data arrivo in cancelleria :&nbsp;&nbsp;
<%
          if(!(dataDalInCancelleria.equals("")))
          {
%>
            Dal <%=dataDalInCancelleria%>&nbsp;&nbsp;
<%        }
          if(!(dataAlInCancelleria.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAlInCancelleria%>
            </td>
<%        }
       %></tr><%
      }
    }
%>
  </table>
  <br>
<%
  Iterator itx = fascicoli.iterator();
  String sUfficio = "";
  String prevUfficio = "";
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Contenuto</td>
      <td class="int">Data Udienza</td>
      <td class="int">Provvedimento</td>
      <td class="int">Data Emissione</td>
      <td class="int">Oggetto Provvedimento</td>
      <td class="int">Esito Provvedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
    while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
      sUfficio = fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()+fascicolo.getFascicoloSiusModel().getDescrComuneUfficio();
      if (!(sUfficio.compareTo(prevUfficio)==0))
      {
        prevUfficio=sUfficio;
%>
        <tr>
          <td class="lVerdeNB" colspan="7">&nbsp;</td>
        </tr>
        <tr>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <td class="lVerdeNB" colspan="7">Elenco Procedimenti di : <%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%></td>
        </tr>
<%
      }
%>
      <tr>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrDefinizione()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrTipoAtto()%></font></td>
        <td class="c">
          <%-- Settaggio dei criteri di ricerca. --%>
          <%--jsp:include page="<%=ICostantiSecurity.PG_BUTTONS%>"--%>
          <jsp:include page="<%=ICostantiFascicoloSius.PG_BUTTONS_PROC_DI_EMS%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
            <jsp:param name="ValoreIdEntita" value="<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>" />
            <jsp:param name="idSoggetto" value="<%=fascicolo.getFascicoloSiusModel().getSoggetto().getIdSoggetto()%>" />
            <jsp:param name="idFascicoloSiep" value="<%=fascicolo.getFascicoloSiusModel().getFasSieIdFascicoloSiep() %>" />
          </jsp:include>
        </td>
      </tr>
<%
  }
%>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td class="lVerdeNB" >
        N.B.: Per richiedere l'elenco di tutti i procedimenti relativi all'esecuzione di una misura sicurezza, selezionare l'icona : </td>
      <td><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>dettagli.gif" width="12" height="12" border="0"> </td>
    </tr>
  </table>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="lVerdeNB" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        Per procedere all'iscrizione di un nuovo procedimento selezionare l'icona : </td>
      <td><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" width="12" height="12" border="0"> </td>
    </tr>
  </table>


  </FORM>
  <br>

  </body>
</html>