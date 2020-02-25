<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPosizioneGiuridicaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"%>


<jsp:useBean id="IstruttoriaCumulo"   		 scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="lPosizioneGiuridicaCumulo"  scope="request" class="siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel"/>
<jsp:useBean id="TipoPosGiu"  				 scope="request" class="java.lang.String"/>

<%
IstitutoDetenzioneModel lIstituto = lPosizioneGiuridicaCumulo.getIstitutoDetenzione();

if (lIstituto==null) {
  lIstituto = new IstitutoDetenzioneModel();
}

%>



<html>
<head>
  <title> [S.I.E.S.] - Posizione Giuridica Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">  
    function loadModifica (azione) {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formName.<%=ICostantiModuloCumulo.MODALITA%>.value = "<%=ICostantiModuloCumulo.MODALITA_MODIFICA%>";
      document.formName.submit();
    }
        
    function eseguiNavigazione(azione) {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = azione;
      document.formName.submit();
    }
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaEspiato";
        document.formName.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.formName.submit();
      }
    }
    
  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Espiazione Attuale</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
     <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
     <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
  <br>
    
<div id="divPosizionamento" align="left" style="padding-left: 25px; border: 0px solid black;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" 			value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       			value="<%=lPosizioneGiuridicaCumulo.getTitIdTitoloCumulato()%>">
	<input type="hidden" name="<%=ICostantiPosizioneGiuridicaCumulo.CAMPO_ID_POSIZIONE_GIURIDICA_CUM%>" value="<%=lPosizioneGiuridicaCumulo.getIdPosizioneGiuridicaCum()%>">
    <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA%>" value="">
   
    <table cellspacing="2" cellpadding="2" width="800px">
      <tr><td class="Titolo" colspan="4" >Posizione Giuridica</td></tr>
      <tr>
        <td class="l" width="150px">Posizione Giuridica</td>
        <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getDescrPosizioneGiuridica()) %>&nbsp;</font></td>
      </tr>

      <% if ( lPosizioneGiuridicaCumulo.getDataInizio()!=null ) { %>
      <tr>
        <td class="l">Data di Decorrenza</td>
        <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizio(),"dd-MM-yyyy")) %>&nbsp;</font></td>
      </tr>
      <% } %>
      
      <% if ( TipoPosGiu.equals(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPIST) ) { %>
      <tr>
        <td class="l" width="150px">Istituto</td>
        <td class="l" colspan="3">
          <% if (lIstituto.getIdIstitutoDetenzione()!=null && !lIstituto.getIdIstitutoDetenzione().equals("")) {%>
          <font class="campo"><%=StringUtils.toStringJSP (lIstituto.getDescrTipoIstituto(),"")%>&nbsp;</font>
          di<font class="campo"> <%=StringUtils.toStringJSP (lIstituto.getDescrizione())%>,&nbsp;</font>
          <font class="campo"><%=StringUtils.toStringJSP (lIstituto.getIndirizzo())%></font>
          <% } %>&nbsp;
        </td>
      </tr>
      <% } %>
       
     
      <% if ( TipoPosGiu.equals(ICostantiPosizioneGiuridicaCumulo.CAMPO_CHECK_TIPO_POS_ESPALTRO) ) {  %>
      <tr>
        <td class="l" width="150px">Luogo di Espiazione</td>
        <td class="l" colspan="3" >
          <font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getAltroLuogo())%>&nbsp;</font>
        </td>
      </tr>
      <% } %>
   
	  <% 
    if (lPosizioneGiuridicaCumulo.getDataInizioMisura()!=null) {%>
    <tr>
      <td class="l">Data di inizio Misura</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataInizioMisura(),"dd-MM-yyyy")) %>&nbsp;</font></td>
    </tr>
    <% } %>

      <% 
      if (   lPosizioneGiuridicaCumulo.isMisura()
          || "14".equals (lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica()) // semilibero
          || "31".equals (lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica()) // sosp 51 ter Det Dom
          || "32".equals (lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica()) // sosp 51 ter Aff Prova
          || "33".equals (lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica()) // sosp 51 ter Semilibertà
         )
      {
      %>
      <tr>
        <td class="Titolo" colspan="4">Provvedimento di concessione della Misura</td>
      </tr>

      <tr>
        <td class="l">Anno / Numero SIUS</td>
        <td class="l">
          <font class="campo">
          <%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getChiaveAnnoFasSius())%></font>
          /<font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getChiaveProgrFasSius())%></font>
        </td>
        <td class="l"> Anno / Numero Provvedimento </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getAnnoRegistro())%></font>
          /<font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getNumeroRegistro())%></font>
        </td>
      </tr>
      <tr>
        <td class="l">Ufficio Emittente</td>
        <td class="l" colspan="3">
          <% if (lPosizioneGiuridicaCumulo.getUfficioSorv()!=null) {%>
          <font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getUfficioSorv().getDescrTipoUfficio())%></font>
           di <font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getUfficioSorv().getDescrComune())%></font>
          <% } %>
          &nbsp;
        </td>
      </tr>
      <tr>
        <td class="l">Tipo Provvedimento</td>
        <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP (lPosizioneGiuridicaCumulo.getDescrTipoProvvedimento())%></font></td>
      </tr>     

      <tr>
        <td class="l">Data Emissione</td>
        <td class="l" colspan="3">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataEmissioneProvv(),"dd-MM-yyyy")) %>&nbsp;</font>
        </td>
      </tr> 
    


      <% 
      if (   "16".equals(lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica())
          || "17".equals(lPosizioneGiuridicaCumulo.getCodPosizioneGiuridica())
         )
      {
      %>
      <tr>
        <td class="l">Rinvio Fino Al</td>
        <% if ( lPosizioneGiuridicaCumulo.getDataFineMisura()!=null) { %>
          <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneGiuridicaCumulo.getDataFineMisura(),"dd-MM-yyyy")) %></font>&nbsp;</td>
        <% } else if ("S".equals (lPosizioneGiuridicaCumulo.getFlagDecisioneTDS()) ) { %>
          <td class="l" colspan="1"><font class="campo">Alla decisione del Tribunale di Sorveglianza</font>&nbsp;</td>
        <% } %>
         
        <td class="l">Nella Misura di</td>
        <td class="l" colspan="1">
          <% 
          String lStrNellaMisuraDi = "";
          if (   lPosizioneGiuridicaCumulo.getNumAnniMisura()!=null
              || lPosizioneGiuridicaCumulo.getNumMesiMisura()!=null
              || lPosizioneGiuridicaCumulo.getNumGiorniMisura()!=null
             ) 
          {
            lStrNellaMisuraDi="<font class=\"campo\">Anni</font> "+StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumAnniMisura(),"0")
                             +" <font class=\"campo\">Mesi</font>  "+StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumMesiMisura(),"0")
                             +" <font class=\"campo\">Giorni</font>  "+StringUtils.toStringJSP(lPosizioneGiuridicaCumulo.getNumGiorniMisura(),"0");
          
          %>
            <%=lStrNellaMisuraDi%>
          <% } %>
          &nbsp;      
        </td>
      </tr>
      <% } %>
    <% } // end if isMisura()%>
 </table>
 <br>
  </form>
  
</div>
</body>

</html>