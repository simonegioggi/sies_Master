<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.log.LogF3B"%>
<%@page import="siap.util.SIESSwitch"%>
<%@page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>


<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<jsp:useBean id="ListaIstruttorieCumulo" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ListaFascicoliSIEP" scope="request" class="java.util.Vector"/>

<jsp:useBean id="CriteriRicerca"   scope="request" class="java.lang.String" />

<%
//==============================================================================
//          FORM di visualizzazione dell'elenco delle istruttorie
//==============================================================================
	String lNota = "N.B.: La presenza del simbolo (*) indica che il titolo della ricerca non corrisponde al titolo cumulante ma è presente nei titoli coinvolti nell'Istruttoria Cumulo.";
	boolean asterisco=false;
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function dettaglioIstrutoria(idRecord)
    {
      var riga = document.getElementById(idRecord); 
      if (riga.style.display =="none" ){
        riga.style.display = "block";
      }
      else {
        riga.style.display = "none";
      }
    }
    
    //==========================================================================
    // Ritorna alla Griglia Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.RicercaIstruttoriaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaIstruttoriaCumulo.submit();
    }
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Elenco Istruttorie</font>
        </td>
        <td class="LBG"><!-- Tasto indietro alla Lista dei fascicoli coinvolti -->
          <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadRicercaIstrCumuloEstesa')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
        <%--td class="LBG">
          <a href="javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td--%> 
    </tr>
  </table>

  <br>
<%  
    //==========================================================================
    // INTESTAZIONE criteri di Ricerca
    //==========================================================================
%>
  <table width="100%">
	<%  if(CriteriRicerca != null && !CriteriRicerca.equals("") )
	  {%>
	   <tr>
	    <td class=l>
	    <font style="color:green; font-size: 10pt;" class="label"> <%=CriteriRicerca %> </font>
	    </td>
	   </tr> 
	<%  }%>
  </table>
  <br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="RicercaIstruttoriaCumulo">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
  
  <table cellspacing="2" cellpadding="2" align="center" width="100%" >
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">N° Istruttoria</td>
      <td class="int">Data Apertura/<br>Chiusura</td>
      <td class="int">Titolo</td>
      <td class="int">Data Titolo</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Soggetto</td>
      <td class="int">Anno/Numero<br>SIEP Cumulante</td>
      <td class="int">Ufficio Esecuzione</td>
      <td class="int">Elenco Altri<br>Titoli</td>
    </tr>
    <%
      int id_record = 0;
      Iterator itx = ListaIstruttorieCumulo.iterator();
      Iterator ity = ListaFascicoliSIEP.iterator();
      while ( itx.hasNext() && 
    		  ity.hasNext()	  ) {
        IstruttoriaCumuloModel lIstruttoriaCumulo = (IstruttoriaCumuloModel)itx.next();
        FascicoloSiepModel lFascicoloSiep = (FascicoloSiepModel)ity.next();
        id_record = id_record+1;
    %>
    <tr>
      <%// Inserire qui le get dei campi da visualizzare %>
      <td class="c" nowrap>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&IdIstruttoriaCumulo=<%=lIstruttoriaCumulo.getIdIstruttoriaCumulo()%>&ChiaveFascicolo=<%=lIstruttoriaCumulo.getFasSieIdFascicoloSiep()%>&ParentFormName=ElencoEstesoIstruttorieCumulo" title="Istruttoria">
          <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getAnnoProtocollo())%>
          /
          <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getNumProtocollo())%></a>&nbsp;
    <%
    if (!CriteriRicerca.contains("tutte")) {
    %>
      <br><%=DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getStatoIstruttoriaCumulo(), lIstruttoriaCumulo.getFlagStato() ) %>
   <% } %>        
      </td>
      <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"),"&nbsp;")%>/<br>
                                 <%=StringUtils.toStringJSP(DateUtils.getDateToString(lIstruttoriaCumulo.getDataChiusura(),"dd-MM-yyyy"),"-")%></td>
      <td class="c" nowrap>
          <%=lFascicoloSiep.getSentenza().getDescrTipoProvvedimento()%>&nbsp;N.&nbsp;<br>
        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=lFascicoloSiep.getSentenza().getIdSentenza()%>" title="Sentenza">
	          <%=StringUtils.toStringJSP(lFascicoloSiep.getSentenza().getAnnoSentenza())%> / <%=StringUtils.toStringJSP(lFascicoloSiep.getSentenza().getNumeroSentenza())%></a>
        </font>
      </td>

      <td class="c" nowrap>
          <%=DateUtils.getDateToString(lFascicoloSiep.getSentenza().getDataProvvedimento(), "dd-MM-yyyy")%>
      </td>
      <td class="c">
        <font class="c"><%=StringUtils.toStringJSP(lFascicoloSiep.getSentenza().getDescrTipoAutoritaEmittente())%></font><br>
<%
        if (lFascicoloSiep.getSentenza().getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">&nbsp;(Sez.</font> <font class="c"><%=StringUtils.toStringJSP(lFascicoloSiep.getSentenza().getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="c"><%=StringUtils.toStringJSP(lFascicoloSiep.getSentenza().getDescrLuogoEmittente())%></font>
      </td>

      <td class="c">
       <font class="c">
          <%=StringUtils.toStringJSP(lFascicoloSiep.getSoggetto().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lFascicoloSiep.getSoggetto().getNome())%>
       </font>&nbsp;
      </td>

      <td class="c" nowrap>
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloSiep.getIdFascicoloSiep()%>" title="Procedimento">

<%	if (lFascicoloSiep.getChiaveProgrOrig()!=null){
%> 
		  <%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveAnno())%>
          /
          <%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveProgrOrig())%>
          <br> <font class="c" color="red">(Ex <%=StringUtils.toStringJSP(lFascicoloSiep.getCodTipoUfficioInserimento())%> di <%=StringUtils.toStringJSP(lFascicoloSiep.getDescrComuneUfficioInserimento())%>)</font>

<%	} else	{ %>                 
          <%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveAnno())%>
          /
          <%=StringUtils.toStringJSP(lFascicoloSiep.getChiaveProgr())%>
<%	} %>  
        </a>&nbsp;
        
<%		if (lIstruttoriaCumulo.getNote()!=null &&
			lIstruttoriaCumulo.getNote().trim().compareTo("*")==0 )
		{ asterisco=true;%><br><font class="cRosso">(*)</font><%}
%>
      </td>

      <td class="C" >
       <font class="label">
          <%=StringUtils.toStringJSP(lFascicoloSiep.getDescrTipoUfficio())%>
        </font>
        <font class="label"> di </font>
       <font class="c">
          <%=StringUtils.toStringJSP(lFascicoloSiep.getDescrComuneUfficio())%>
       </font>&nbsp;
      </td>

      <td class="c" nowrap>
        <a href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=lIstruttoriaCumulo.getIdIstruttoriaCumulo()%>&ParentFormName=ElencoEstesoIstruttorieCumulo">
          <img src="/images/vedi24.gif" title="Elenco altri Titoli" border="0"></a>
      </td>
    </tr>
    <tr style="display:none" id="rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lIstruttoriaCumulo.getNote(),"&nbsp;")%>
        </font>
      </td>
    </tr>
    <% } // end while su iterator %>
    <% if (ListaIstruttorieCumulo.size()==0){ %>
    <tr>
      <td>Nessun dato presente</td>
    </tr>
    <% } %>
  </table>
  
<% if (asterisco) { %>
  <table>
  	<tr><td>&nbsp;</td></tr>
  	<tr>
      <td class="cRosso" >
        <%=lNota%>
      </td>
   	</tr>
  </table>
<%} %> 
 
</FORM>
</body>
</html>