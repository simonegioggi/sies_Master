<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<!--
< jsp:useBean id="AnnotazioneOrdinanza" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel" />
-->
<jsp:useBean id="ListaAnnotazioni"           scope="request" class="java.util.Vector" />
<jsp:useBean id="AnnotazioneManualeInserita" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />
<jsp:useBean id="MotivoProvvedimento"        scope="request" class="java.lang.String" />
<jsp:useBean id="lFlagPage"                  scope="request" class="java.lang.String" />

<%
//==============================================================================
// Form di visualizzazione del dettaglio inserimento computi. lFlagPage indica
// il tipo di computo:
// lFlagPage = A = Altro Titolo (Fungibilità altro reato Misura Cautelare)
// lFlagPage = S = Senza Titolo (Fungibilità altro reato Pena Detentiva)
// lFlagPage = D = Stesso Titolo (Presofferto)
//==============================================================================
%>

<head>
  <title> [S.I.E.S.] - Dettaglio Annotazioni Manuali Computo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
  
    function funSubmit(azione)
    {
      switch(azione)
      {
        case "CALCOLO":
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaComputo";
        break; //si ferma qui

        case "AGGIUNGI":
<%
        if(lFlagPage.equals("S"))
        {
%>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadAnnotazioniManualiCompSenzaTitolo";
<%
        }
        else if(lFlagPage.equals("A"))
        {
%>
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadAnnotazioniManualiCompAltroTitolo";
 <%
        }
        else
        {
%>
          
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadAnnotazioniManualiMC";
<%
        }
%>
        break; //si ferma qui

        case "STAMPA":
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniComputo";
        break; //si ferma qui
      }

      document.f.CALCOLO.disabled=true;
      document.f.AGGIUNGI.disabled=true;
      document.f.STAMPA.disabled=true;

      document.f.submit();
    }
  </script>
</head>
<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getEveIdEvento())%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="<%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getCodTipoAnnotazione())%>">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=StringUtils.toStringJSP(MotivoProvvedimento)%>">
    <input type="HIDDEN" name="lFlagPage" value="<%=lFlagPage%>">

<table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        if(lFlagPage.equals("A"))
        { 
%>
          <font class="campo">Computo misura cautelare Computo altro reato (fungibilità)</font>
<%
        }
        else if(lFlagPage.equals("D"))
        {
%>
          <font class="campo">Computo misura cautelare stesso reato (presofferto)</font>
<%
        }
        else
        {
%>
          <font class="campo">Computo pena detentiva espiata per altro reato (fungibilità)</font>
<%
        }
%>
      </td>
      <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" />
           <jsp:param name="ValoreIdEntita" value="<%=AnnotazioneManualeInserita.getIdAnnotazioneManuale()%>" />
        </jsp:include>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<%
/*
  if(AnnotazioneOrdinanza.getAnnotazioneManuale() != null && AnnotazioneOrdinanza.getEvento() != null)
  {
    AnnotazioneManualeModel OrdinanzaGEAnn = AnnotazioneOrdinanza.getAnnotazioneManuale();
    EventoModel OrdinanzaGEEve = AnnotazioneOrdinanza.getEvento();
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <table width="70%">
      <tr><td colspan=3 class="Titolonocap">Decisione del Giudice dell' Esecuzione</td></tr>
      <tr>
        <td class="l">Declaratoria :</td>
        <td class="l">
          Anno/Numero
          <font class="campo">
            <%=StringUtils.toStringJSP( OrdinanzaGEAnn.getAnnoGe() )%>/<%=StringUtils.toStringJSP(OrdinanzaGEAnn.getNumeroGe())%>
          </font>
        </td>
        <td class="l">
          <font class="label">in data </font>
          &nbsp;&nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(OrdinanzaGEAnn.getDataGE(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio :</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrUfficioEmittente())%>&nbsp;
          </fonts.>
        </td>
      </tr>
      <tr>
        <td class="l">Sede :</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toStringJSP(OrdinanzaGEEve.getDescrLuogoEmittente())%>&nbsp;
          </font>
        </td>
      </tr>
    </table>
--%>
<%
/*
  }
*/
  if(!ListaAnnotazioni.isEmpty())
  {
%>
    <br>
    <!--table width="80%" border="0"-->
    <table>
      <tr><td colspan="9" class="Titolonocap">Periodi</td></tr>
<%
    for (Iterator lIter = ListaAnnotazioni.iterator(); lIter.hasNext(); )
    {
      AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel)lIter.next();
      //========================================================================
      // Altro Titolo (Fungibilità altro reato Misura Cautelare)
      //========================================================================
      if(lFlagPage.equals("A"))
      {
%>
       <tr>
         <td class="l" width="35%">Procedimento R.G.P.M.</td>
         <td class="l" >
           Anno/Numero
           <font class="campo">
<%
            if(lAnnMan.getAnnoRege() == null || lAnnMan.getAnnoRege().compareTo(new BigDecimal(0))==0)
            {
%>
              -
<%
            }
            else
            {
%>
              <%=StringUtils.toStringJSP(lAnnMan.getAnnoRege() )%>
<%
            }
%>
            /
            <%=StringUtils.toStringJSP(lAnnMan.getNumeroRege(), "-")%>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l">Procedimento B.D.M.C.</td>
        <td class="l">
          Anno/Numero
        <font class="campo">
<%
          if(lAnnMan.getAnnoMc() == null || lAnnMan.getAnnoMc().compareTo(new BigDecimal(0))==0)
          {
%>
            -
<%
          }
          else
          {
%>
            <%=StringUtils.toStringJSP(lAnnMan.getAnnoMc() )%>
<%
          }
%>
           / <%=StringUtils.toStringJSP(lAnnMan.getNumeroMc(),"-")%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" >Data Istanza :
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataRichiesta(), "dd-MM-yyyy"),"-")%>&nbsp;
        </font>
      </td>
    </tr>
<%
    }

    //==========================================================================
    // Senza Titolo (Fungibilità altro reato Pena Detentiva)
    //==========================================================================
    if(lFlagPage.equals("S"))
    {
%>
      <tr>
        <td class="l">Sentenza </td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
<%
          if( lAnnMan.getAnnoSentenzaSiap() == null || lAnnMan.getAnnoSentenzaSiap().compareTo(new BigDecimal(0))==0)
          {
%>
            -
<%
          }
          else
          {
%>
            <%=StringUtils.toStringJSP(lAnnMan.getAnnoSentenzaSiap() )%>
<%
          }
%>
            / <%=StringUtils.toStringJSP(lAnnMan.getNumeroSentenzaSiap(),"-")%>
          </font>
                &nbsp;  &nbsp;  in data &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataSentenzaSiap(),"dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%
    }

    if(lFlagPage.equals("A") || lFlagPage.equals("S"))
    {
%>
      <tr>
        <td class="l">Causale Computo </td>
        <td class="l" colspan="2">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnMan.getDescrCausaleComputo(),"-")%>
          </font>
        </td>
      </tr>
<%
    }
%>

<%
//==============================================================================
// Periodo e quantum
//==============================================================================
%>
      <tr>
        <td class="l">Dalla Data :
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneDa(), "dd-MM-yyyy"),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">Alla Data :
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataReclusioneA(), "dd-MM-yyyy"),"-")%>&nbsp;
          </font>
        </td>
        
        <td class="L">Pari a:
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"-")%></font>
        </td>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
        <td class="L" width="40%">
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniReclusione(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiReclusione(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniReclusione(),"-")%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Arresto : Dalla Data :
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataArrestoDa(), "dd-MM-yyyy"),"-")%>&nbsp;
          </font>
        </td>
        <td class="l">Alla Data :
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMan.getDataArrestoA(), "dd-MM-yyyy"),"-")%>&nbsp;
          </font>
        </td>
        <td class="L" width="40%">
          Anni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumAnniArresto(),"-")%></font>
          Mesi
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumMesiArresto(),"-")%></font>
          Giorni
          <font class="campo"><%=StringUtils.toStringJSP(lAnnMan.getNumGiorniArresto(),"-")%></font>
        </td>
--%>
      </tr>
<%
    }
%>
    </table>
<%
  }
%>
  <br>
  <table>
    <tr>
      <td colspan=2>
        <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:funSubmit('CALCOLO');">
      </td>
      <td colspan=2>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Aggiungi" onClick="javascript:funSubmit('AGGIUNGI');">
      </td>
      <td colspan=2>
        <INPUT class="bottone" type="button" name="STAMPA" value="Stampe" onClick="javascript:funSubmit('STAMPA');">
      </td>
    </tr>
  </table>
</form>
</body>
</html>