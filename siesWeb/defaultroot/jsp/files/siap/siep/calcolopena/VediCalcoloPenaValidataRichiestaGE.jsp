<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>

<jsp:useBean id="IdAnnotazioneManuale" scope="request" class="java.lang.String"/>

<%

//==============================================================================
// Form che presenta le stampe per le RICHIESTE AL GE di
// - depenalizzazione
// - incostituzionalità
// - amnistia
// - indulto
//
// La form riporta nella prima sezione il quantum di pena rideterminato
// impostando la IntestazionePenaValidataAnnotazioni.jsp
// Nella seconda sezione l'elenco delle stampe
// ActLoadStampeAnnotazioniRichieste.java
//
//
// Nel caso di ergastolo a seguito calcolo della pena non viene visualizzato il dettaglio
// della pena in quanto i quantum non vengono modificati.
// ActCalcoloPenaGE.java
// ActInserisciNuovaPenaValidata.java
// ActRichiestaGE.java ?????
//==============================================================================
%>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

    <script language="JavaScript">
      function Verify()
      {
        if (document.f.annotazioni[document.f.annotazioni.selectedIndex].value=="-")
        {
          alert("Selezionare il Procedimento");
          return false;
        }
      }

      function submComu(id,partenza)
      {
        document.f.annotazioni.value=id;
        document.f.puntoPartenza.value=partenza;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciEmissioneComunicazioni";
        document.f.submit();
      }

      function submRich(id)
      {
        document.f.annotazioni.value=id;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciRichieste";
        document.f.submit();
      }

      function submRichConFlagTempl(id,testo)
      {
        document.f.codice.value=id;
        document.f.testo.value=testo;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciRichiesteConCodice";
        document.f.submit();
      }

      function submRichDetPenAboReato()
      {
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciRichDetPenAboReato";
        document.f.submit();
      }

      function submRich0210(template,testo)
      {
        document.f.codice.value=template;
        document.f.testo.value=testo;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciRichDepenIncost";
        document.f.submit();
      }

      function submOrdineScarcerazione(motivo)
      {
    	document.f.codice.value=motivo;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.richiesta.action.ActLoadInserisciOrdineScarcerazioneProvv";
        document.f.submit();
      }

/*
      function subm(id)
      {
        document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=id;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadInserisciOrdineScarcerazionePerNuovaScadenzaPena";
        document.f.submit();
      }
*/
  </script>
 <script language="JavaScript1.2">
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
  </script>

  <style>
	  .menulines
	  {
	  	border:2.5px solid #BEC6FC;
	  	text-align : center;
	  	font-family: 'Tahoma';
	  	color : Navy;
	  	font-size : 11px;
	  	text-decoration : none;
	  	height:100%;
	  	font-weight : normal;

	  }

	  .menulines a
	  {
	  	text-align : center;
	  	text-decoration:none;
	  	color:black;
	  	font-family: 'Tahoma';
	  	color : Navy;
	  	font-size : 11px;
	  	width:100%;
	  	height:100%;

	  }
</style>
   <title>[S.I.E.S.] - Calcolo Pena</title>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=lbg>
        <font  class="label">Funzione :&nbsp;</font><font class="campo">Provvedimenti e Stampe per Rideterminazione Pena</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="">
    <input type="HIDDEN" name="codice" value="">
    <input type="HIDDEN" name="testo" value="">
    <input type="HIDDEN" name="annotazioni" value="">
    <input type="HIDDEN" name="puntoPartenza" value="">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=IdAnnotazioneManuale%>">
    <jsp:include page="/jsp/files/siap/siep/calcolopena/IntestazionePenaValidataAnnotazioni.jsp"/>
    <br>

    <table cellpadding="1" cellspacing="1" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
    <%
    //==========================================================================
    //                               COMUNICAZIONI
    //==========================================================================
    %>
    <tr>
      <td class="titolo" colspan=3>COMUNICAZIONI</td>
    </tr>
    <tr>
      <!--td width="32%" class="menulines" nowrap><a href="javascript:submComu('0187','GE');">Revoca sentenza per abolizione reato</a></td-->
      <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0298','GE');">Nuovo residuo pena per concessione benefici</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0299','GE');">Nuovo residuo pena per revoca sentenza abol. reato</a></td>
    </tr>
    <!--tr>
      <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0186','GE');">Concessione benefici - ex.art. 672 c.p.p.</a></td>
    </tr-->
    <tr>
      <td>&nbsp;</td>
    </tr>

    <%
    //==============================================================================
    //                               RICHIESTE
    //==============================================================================
    %>
    <tr>
      <td class="titolo" colspan=3>RICHIESTE</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0287','Determinazione pena - ex artt. 671 c.p.p. e art. 174 c.p.');">Determinazione pena - ex artt. 671 c.p.p. e art. 174 c.p.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0291','Revoca benefici - ex artt. 174 c.p. e 674 c.p.p.');">Revoca Indulto - ex artt. 174 c.p. e 674 c.p.p.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRich0210('0295','Restituzione Ordine Esecuzione ex art. 673 c.p.p.');">Restituzione OE per Revoca sent. e Incost.</a></td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0288','Determinazione pena - artt. 671 c.p.p. e 151 c.p.');">Determinazione pena - artt. 671 c.p.p. e 151 c.p.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0292','Revoca benefici - ex artt.151 c.p. e 674 c.p.p.');">Revoca Amnistia - ex artt.151 c.p. e 674 c.p.p.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0296','Restituzione Ordine Esecuzione - ex art. 672 c.p.p.');">Restituzione Ordine Esecuzione  -ex art. 672 c.p.p.</a></td>
    </tr>
    <tr>
      <!-- <td width="32%" class="menulines" nowrap><a href="javascript:submRichDetPenAboReato('0210');">Determinazione pena - ex art. 671 e 673 c.p.p.</a></td> -->
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichDetPenAboReato();">Determinazione Pena per Revoca sent. e Incost.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRich0210('0293','Revoca sentenza ex art.673 c.p.p.');">Revoca sentenza e Incostituzionalità</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRich('0122');">Accertamento data commesso reato</a></td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0290','Applicazione benefici : Indulto');">Applicazione benefici : Indulto</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submRichConFlagTempl('0294','Applicazione benefici : Amnistia');">Applicazione benefici : Amnistia</a></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>

    <%
    //==============================================================================
    //                       ORDINE PROVVISORIO DI SCARCERAZIONE
    //==============================================================================
    %>
    <tr>
      <td class="titolo" colspan=3>ORDINE PROVVISORIO DI SCARCERAZIONE</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap><a href="javascript:submOrdineScarcerazione('0369');">Ordine scarc. per Revoca sent. e Incost.</a></td>
      <td width="32%" class="menulines" nowrap><a href="/jsp/Main.jsp?Action=siap.sico.web.ActionUnderConstruction">Ordine scarcerazione per amnistia</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submOrdineScarcerazione('0367');">Ordine scarcerazione per indulto</a></td>
   <!--Parte riguardante il sesto quinto togliere il commento quando opportuno-->
      <!--td width="32%" class="menulines" nowrap><a href="javascript:subm('0163');">Ex artt. 673 c.p. e 672 comma 3° c.p.p.</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:subm('0161');">Ordine scarcerazione per amnistia</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:subm('0162');">Ordine scarcerazione per indulto</a></td-->
    </tr>
  </table>
</form>
  </body>
</html>