<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Collection"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>
<%@ page import="siap.sius.produzioneatti.model.ParereModel" %>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils" %>
<%@ page import="siap.sico.util.SICOLookupRemote" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>


<jsp:useBean id="Pareri" scope="request" class="java.util.Vector" />
<jsp:useBean id="InTesta" scope="request" class="java.lang.String" />
<jsp:useBean id="FiltriPareri" scope="request" class="siap.sius.produzioneatti.model.ParereModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Titolo Esecutivo</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
 <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <script language="JavaScript">

   function stampaSius(lAzione)
   {
       var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>="+lAzione;
     stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   }
 </script>

 </head>

  <BODY class="corpo">

  <table >
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"> <font class=label>Funzione:</font>&nbsp; <font class="campo">Elenco dei pareri richiesti  <%=InTesta%></font> </td>
     <!-- BOTTONE DI STAMPA  specifico-->
     <td class="LBG">
      <a href="Javascript:stampaSius('siap.sius.produzioneatti.action.ActStampaPareri')" >
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
     </td>

     <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>
  </table>


  <table cellspacing=2 cellpadding=2>
<%
    if(!(FiltriPareri.getCodiceUtente().length()<2 &&
         FiltriPareri.getCodMotivo().equals("") &&
         FiltriPareri.getCodOggettoProcedimento().equals("") ))
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(FiltriPareri.getDescrMotivo().length()> 1 )
      {%>
        <tr>
          <td class="lVerdeNB">Tipo Parere Selezionato : <%=FiltriPareri.getDescrMotivo().trim()%></td>
        </tr>
<%    }else{%>
        <tr>
          <td class="lVerdeNB">Tipo Parere Selezionato : Tutti</td>
        </tr>
<%    }
      if(FiltriPareri.getDescrOggettoProcedimento().length()> 1 )
      {%>
        <tr>
          <td class="lVerdeNB">Contenuto degli atti di cui si è richiesto parere : <%=FiltriPareri.getDescrOggettoProcedimento().trim()%></td>
        </tr>
<%    }
      if(FiltriPareri.getCodiceUtente().length()>1 )
      {%>
        <tr>
          <td class="lVerdeNB">Codice Utente selezionato : <%=FiltriPareri.getCodiceUtente().trim()%></td>
        </tr>
<%    }
    }
%>
  </table>





  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

 <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="int" width=10%>Numero SIUS</td>
      <td class="int" width=10%>Data Richiesta</td>
      <td class="int" width=10% >Tipo Parere</td>
      <td class="int" width=10%>Cognome</td>
      <td class="int" width=10%>Nome</td>
      <td class="int" width=10%>Data Nascita</td>
      <td class="int" width=10%>Contenuto</td>
      <td class="int" width=10%>Esito Parere</td>
      <td class="int" width=10% >Data ESito</td>
      <td class="int" width=10% >Azioni</td>
    </tr>
<%
    // Costruzione tabella di traduzione COD_ESITO_PROVVEDIMENTO
/*
    Collection lColesitoProv = null;
    DecodificheModel lModel = new DecodificheModel();
    IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
    lModel.setContesto("ESITO_PROVVEDIMENTO");
    lColesitoProv = lDecodifiche.ExRicercaDecodifiche(lModel);
*/
    Iterator itx = Pareri.iterator();

    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      ParereModel parere = (ParereModel)itx.next();
%>
      <tr>
      	<td class="c"><font class="label"><%=parere.getAnnoFascicoloSius()%>/<%=parere.getProgrFascicoloSius()%></font></td>
      	<td class="c"><font class="label"><%=DateUtils.getDateToString(parere.getDataEmissione(),"dd-MM-yyyy")%></font></td>
       	<td class="c"><font class="label"><%=StringUtils.toStringJSP(parere.getDescrMotivo(), "-")%></font></td>
       	<td class="c"><font class="label"><%=StringUtils.toStringJSP(parere.getCognome(), "-")%></font></td>
       	<td class="c"><font class="label"><%=StringUtils.toStringJSP(parere.getNome(), "-")%></font></td>
       	<td class="c"><font class="label"><%=DateUtils.getDateToString(parere.getDataNascita(),"dd-MM-yyyy")%></font></td>
      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(parere.getDescrOggettoProcedimento(), "-")%></font></td>
      	<td class="c"><font class="label"><%=parere.getDescrEsito()%></font></td>
      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(parere.getDataRicezioneAtti(),"dd-MM-yyyy"), "-")%></font></td>
      	<td class="c">
          <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          	<jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=parere.getIdEvento()%>" />
            <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>" />
            <jsp:param name="ValoreIdEntitaProvv" value="<%=parere.getIdFascicoloSius()%>" />
         </jsp:include>
        </td>
      </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>