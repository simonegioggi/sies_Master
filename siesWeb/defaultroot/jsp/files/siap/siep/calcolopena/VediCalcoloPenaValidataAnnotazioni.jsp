<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<jsp:useBean id="CodPosizioneGiuridica"   scope="request" class="java.lang.String" />
<%
//==============================================================================
// Form di visualizzazione del dettaglio della pena e della scelta delle stampe
// nel caso di Decisioni del GE
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
    
      function subm(id)
      {
        if(id=="GE")
        {
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadEmissioneProvvedimento";
          document.f.flagPage.value=id;
          document.f.submit();
        }
        else
        {
          document.f.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.value=id;
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActLoadInserisciOrdineScarcerazionePerNuovaScadenzaPena";
          document.f.submit();
        }
      }
    
      function submComu(id, partenza)
      {
        document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadInserisciEmissioneComunicazioni&annotazioni="+id+"&puntoPartenza="+partenza;
      }
    
      function subVa()
      {
    
        var risposta = window.confirm("Attenzione si è richiesto di validare l'ordinanza del G.E.,\nsenza emissione di ulteriore provvedimento")
        if (risposta)
        {
          document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.annotazionemanuale.action.ActValidaProvvedimentoIndulto";
          document.f.submit();
        }
      }
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
    <title>[S.I.E.S.] - Provvedimenti e Stampe per annotazioni Manuali </title>
  </head>

<body class="corpo">
<!-- VediCalcoloPenaValidataAnnotazioni.jsp -->
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=lbg>
        <font class="label">Funzione :&nbsp;</font><font class="campo">Provvedimenti e Stampe per Decisione del GE</font>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="">
    <input type="HIDDEN" name="flagPage" value="">

    <jsp:include page="/jsp/files/siap/siep/calcolopena/IntestazionePenaValidataAnnotazioni.jsp"/>

    <br>

    <table cellpadding="1" cellspacing="1" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">
 <%
 	 if(!CodPosizioneGiuridica.equals("07") &&
 		!CodPosizioneGiuridica.equals("10") &&
 		!CodPosizioneGiuridica.equals("16") &&
 		!CodPosizioneGiuridica.equals("17") &&
 		!CodPosizioneGiuridica.equals("46") &&
 		!CodPosizioneGiuridica.equals("47")  )
 	 {	 
 %>
	      <tr>
	        <td class="titolo" colspan=3>ORDINE DI SCARCERAZIONE</td>
	      </tr>
	      <tr>
	        <!-- Depenalizzazione/Incostituzionalità -->
	        <!-- td width="32%" class="menulines" nowrap><a href="javascript:subm('0158');">Ex art. 673 c.p.p.</a></td> -->
	        <td width="32%" class="menulines" nowrap><a href="javascript:subm('0158');">Revoca sentenza per abolizione del reato <br> dichiarazione incostituzionalità</a></td>
	        <!-- Concessione (Art. 672) Amnistia (art 151) -->
	        <td width="32%" class="menulines" nowrap><a href="javascript:subm('0159');">Applica Amnistia - Ex artt. 151 c.p. e 672 c.p.p.</a></td>
	        <!-- Concessione (Art. 672) Indulto (art 174) -->
	        <td width="32%" class="menulines" nowrap><a href="javascript:subm('0160');">Concede/Rigetta Indulto - Ex.artt. 174 c.p. e 672 c.p.p.</a></td>
	      </tr>
	      <tr>
	        <!-- Revoca (Art. 674) Amnistia (art 151) -->
	        <td width="32%" class="menulines" nowrap><a href="javascript:subm('0174');">Revoca Amnistia - Ex.artt. 151 c.p. e 674 c.p.p.</a></td>
	        <!-- Revoca (Art. 674) Indulto (art 174) -->
	        <td width="32%" class="menulines" nowrap><a href="javascript:subm('0175');">Revoca Indulto - Ex.artt. 174 c.p. e 674 c.p.p.</a></td>
	      </tr>
	      <tr>
	        <td>&nbsp;</td>
	      </tr>
 
<%	} %> 
 
<!--
<tr>
  <td class="titolo" colspan=3>ORDINE PROVVISORIO DI SCARCERAZIONE</td>
</tr>
<tr>
  <td width="32%" class="menulines" nowrap><a href="javascript:subm('0163');">Ex artt. 673 c.p. e 672 comma 3° c.p.p.</a></td>
  <td width="32%" class="menulines" nowrap><a href="javascript:subm('0161');">Ex artt. 151 c.p. e 672 comma 3° c.p.p.</a></td>
  <td width="32%" class="menulines" nowrap><a href="javascript:subm('0162');">Ex artt. 174 c.p. e 672 comma 3° c.p.p.</a></td>
</tr>
<tr>
  <td>&nbsp;</td>
</tr>
-->
      <tr>
        <td class="titolo" colspan=3>COMUNICAZIONI</td>
      </tr>
      <tr>
        <!--  Comunicazione per DEPENALIZZAZIONE e INCOSTITUZIONALITÀ  -->
        <!-- td width="32%" class="menulines" nowrap><a href="javascript:submComu('0300','AN');">Revoca sentenza per abolizione reato</a></td -->
        <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0300','AN');">Revoca sentenza per abolizione del reato <br> dichiarazione incostituzionalità</a></td>
<!--
	    <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0188','AN');">Nuovo residuo pena per concessione benefici</a></td>
      <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0189','AN');">Nuovo residuo pena per revoca sentenza abol. reato</a></td>
	  </tr>
    <tr>
-->
        <!--  Comunicazione per AMINISTIA/INDULTO  -->
       <td width="32%" class="menulines" nowrap><a href="javascript:submComu('0301','AN');">Concede/Rigetta Benefici - ex.art. 672 c.p.p.</a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td class="titolo" colspan=3>Validazioni</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap><a href="javascript:subVa();">Valida Provvedimento</a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
  </table>
</form>
</body>
</html>