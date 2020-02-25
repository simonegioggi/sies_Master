<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>

<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>


<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="datiFinaliAggregatoModel" scope="request" class="siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel"/>

<%
//==============================================================================
// Elenco dei Provvedimenti iscritti in cumulo e relative Procure a cui inviare
// le comunicazioni
//
// In aggiunta gli uffici di Sorveglianza indicati in form
//==============================================================================

EventoNotificaModel lProvvedimentoCumulo = datiFinaliAggregatoModel.getProvvedimentoCumulo();

%>

<html>
<head>
  <title> [S.I.E.S.] - Comunicazioni </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
  
  <script language="JavaScript">    
   
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function stampaSiep(lAzione)
    {
      var  hrefStampa = lAzione;
      var lIndice = hrefStampa.indexOf("?");

      var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
      
      parametri+="&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>";

      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  parametri);
    }
    
  </script>
  
 
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
        <font class="campo">Comunicazioni Altre Autorità</font>&nbsp;
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia Delle Comunicazioni -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActGrigliaComunicazioni')">
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
    
    <br>
    
    
    <%
    //==========================================================================
    // Elenco Altri Destinatari    
    //==========================================================================
    %>
    <table cellspacing="2" cellpadding="2" align="center" width="95%">
      <tr>
        <td class="int" colspan="2">Tipologia Destinatario</td>
        <td class="int" >Stampa Comunicazione</td>
      </tr>
      
<%  int count=0;

	if(lProvvedimentoCumulo.getNotifiche() != null && lProvvedimentoCumulo.getNotifiche().length >0)
	{
	  while(count < lProvvedimentoCumulo.getNotifiche().length)
	  {
	    NotificaModel lNotMod = lProvvedimentoCumulo.getNotifiche()[count];
	  
	    if (lNotMod.getIstitutoDetenzione()!=null){
	      // Istituto di detenzione
	      IstitutoDetenzioneModel lIstituto = lNotMod.getIstitutoDetenzione();
	    %>
	    <tr>
	      <td class="l">Istituto Detenzione</td>
	      <td class="l">
	        <font class="campo"> <%= StringUtils.toStringJSP(lIstituto.getDescrTipoIstituto())%></font> di <font class="campo"><%= StringUtils.toStringJSP(lIstituto.getDescrizione())%>, <%= StringUtils.toStringJSP(lIstituto.getIndirizzo())%></font>
	      </td>
          <!--  Colonna Azioni  -->
          <td class="c" nowrap>
			<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniAltre&IdNotifica="+lNotMod.getIdNotifica()%>')">
          		<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Stampa comunicazione" width="12" height="12" border="0"></a>
          </td>
	      
	    </tr>       
	    <%
	    }
	    else if (lNotMod.getCSSA()!=null){
	      // UEPE
	      CSSAModel lCSSA = lNotMod.getCSSA();
	    %>
	    <tr>
	      <td class="l">UEPE</td>
	      <td class="l">
	        <font class="campo"> <%= StringUtils.toStringJSP(lCSSA.getComune())%> - <%= StringUtils.toStringJSP(lCSSA.getIndirizzo())%></font>
	      </td>
          <!--  Colonna Azioni  -->
          <td class="c" nowrap>
			<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniAltre&IdNotifica="+lNotMod.getIdNotifica()%>')">
          		<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Stampa comunicazione" width="12" height="12" border="0"></a>
          </td>
	    </tr>    
	    <%
	    }
	    else if (   lNotMod.getAutoritaEsterna()!=null 
	             && lNotMod.getAvvIdAvvocatoFascicoloSiep()==null
	            ) 
	    {
	      // Autorità di polizia
	      AutoritaEsternaModel lAutEst = lNotMod.getAutoritaEsterna();
	    %>
	    <tr>
	       <td class="l">Autorità di destinazione</td>
	       <td class="l">
	         <font class="campo"> <%=StringUtils.toStringJSP(lAutEst.getDescrTipoAutorita()) %> di <%=StringUtils.toStringJSP(lAutEst.getDescrSede()) %>
	         <% if (lNotMod.getNote()!=null) { %>
	         , <%=StringUtils.toStringJSP(lNotMod.getNote()) %>
	         <% } %>
	         </font>
	       </td>
          <!--  Colonna Azioni  -->
          <td class="c" nowrap>
			<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniAltre&IdNotifica="+lNotMod.getIdNotifica()%>')">
          		<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Stampa comunicazione" width="12" height="12" border="0"></a>
          </td>
	    </tr>
	    <% 
	    }   
	    
	    else if (   lNotMod.getAvvIdAvvocatoFascicoloSiep()!=null ) 
	    {
	      // Avvocato in Notifica
	      AvvocatoSiepModel lAvvSiep = lNotMod.getAvvSiep();
	    %>
	    <tr>
	       <td class="l">Avvocato </td>
	       <td class="l">
	         <font class="campo"> <%=StringUtils.toStringJSP(lAvvSiep.getAvvocato().getNome()+" "+lAvvSiep.getAvvocato().getCognome()) %> del foro di <%=StringUtils.toStringJSP(lAvvSiep.getAvvocato().getForo() ) %>
	         <% if (lNotMod.getNote()!=null) { %>
	         , <%=StringUtils.toStringJSP(lNotMod.getNote()) %>
	         <% } %>
	         </font>
	       </td>
         <!--  Colonna Azioni  -->
         <td class="c" nowrap>
			<a href="Javascript:stampaSiep('<%="/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActStampaComunicazioniAltre&IdNotifica="+lNotMod.getIdNotifica()%>')">
         		<img  align="middle" src="<%=IWebConstants.IMAGES_DIR%>print.gif" alt="Stampa comunicazione" width="12" height="12" border="0"></a>
         </td>
	    </tr>
	    <% 
	    }   

	    count++;
	  }
	}
%>
    </table>
  
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

