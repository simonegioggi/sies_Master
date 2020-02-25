<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.modulocumulo.model.DatiFinaliCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiDatiFinaliCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="ListaComunicazioni"  scope="request" class="java.util.Vector"/>


<%
//==============================================================================
//     
//==============================================================================

%>

<html>
<head>
  <title> [S.I.E.S.] - Comunicazioni </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">    
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }    
    
  </script>
  
   <script language="JavaScript1.2">

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

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE> 
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Comunicazioni</font>&nbsp;
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Della Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>


  
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    
    <table cellpadding="4" cellspacing="4" width="95%" align="center"
           onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')">

      <tr>
        <td colspan=3 class="Titolonocap">COMUNICAZIONI</td>
      </tr>
      <tr>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniEsecSorv')">Uffici Esecuzione Penale/Sorveglianza</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniCancellerie')">Cancellerie</a>
        </td>
        <td width="32%" class="menulines" nowrap>
          <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadComunicazioniAltro')">Altre Autorità</a>
        </td>
      </tr>
    </table>

    <br>
    
    <%
    //==========================================================================
    // Tabelle con le eventuali trasmissioni già effetuate
    //==========================================================================
    %>
    <!--
    <table cellspacing="2" cellpadding="2" align="center" width="95%">
      <tr>
        <td class="titolo" colspan="3">Comunicazioni Effettuate</td>
      </tr>
      <tr>  
        <td class="int">Destinatario</td>
        <td class="int">Tipo Notifica</td>
        <td class="int">Data Invio</td>
      </tr>
      <tr>  
        <td class="L">Procura della repubblica presso il tribunale Ordinario di NAPOLI</td>
        <td class="C">JMS</td>
        <td class="C">15/01/2015</td>
      </tr>
      <tr>  
        <td class="L">Cancelleria presso il tribunale Ordinario di NAPOLI</td>
        <td class="C">PEC</td>
        <td class="C">15/01/2015</td>
      </tr>      
    </table>  
    -->
  </form>
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
</body>

</html>

