<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ContinuazioneCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiContinuazioneCumulo"%>


<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="ListaTitoli"        scope="request" class="java.util.Vector" />
<jsp:useBean id="ListaContinuazioni" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Modulo Cumulo </title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
      window.focus();
      
      function controlla()
      {
        <% if( ListaTitoli.isEmpty() ) { %>
          alert("Nessun dato presente");
          window.parent.close();
        <% }%>
      }

      function insertIT( idTitolo,
                         annoSentenza, numeroSentenza,
                         giornoSentenza, meseSentenza, annoSentenza,
                         tipoAutorita, sedeAutorita,
                         annoRGNR, numeroRGNR,
                         annoREGE, numeroREGE, tipoREGE)
      {
        // idTitolo
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_TIT_ID_TITOLO_CUMULATO_CONT%>.value=idTitolo;
      
        // Anno/Numero sentenza
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_SENTENZA%>.value=annoSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_SENTENZA%>.value=numeroSentenza;

        // Data Sentenza
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_GIORNO_DATA_SENTENZA%>.value=giornoSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_MESE_DATA_SENTENZA%>.value=meseSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_DATA_SENTENZA%>.value=annoSentenza;

        // Tipo Autorità/ Sede
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_COD_TIPO_AUTORITA%>.value=tipoAutorita;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_COD_LUOGO_AUTORITA%>.value=sedeAutorita;

        // Anno/Numero RGNR
        if (annoRGNR!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value=annoRGNR;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REGE_PM%>.value="";

        if (numeroRGNR!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value=numeroRGNR;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_NUM_REGE_PM%>.value="";

        // Anno/Numero/Tipo REGE        
        if (annoREGE!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value=annoREGE;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_ANNO_REG_GEN%>.value="";

        if (numeroREGE!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value=numeroREGE;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_NUMERO_REG_GEN%>.value="";
        
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiContinuazioneCumulo.CAMPO_TIPO_REG_GEN%>.value=tipoREGE;

        try {
          //
          window.parent.opener.disableCampiTitolo();
        }
        catch(err){  }

        window.parent.close();
      }
    </script>
  </head>

<body class="corpo" onload="controlla();">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Titoli in Istruttoria Cumulo</font>
      </td>
    </tr>
  </table>
  
  <br>

  <table cellspacing="2" cellpadding="2" width="95%">
    <tr>
      <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
      <td class="int">Titolo</td><!-- Sentenza/Decreto/Cumulo-->
      <td class="int">N°</td>
      <td class="int">Data Titolo</td>
      <td class="int">Autorità Emittente</td>
      <td class="int">Anno/Numero <br>R.G.N.R.</td>
      <td class="int">Anno/Numero <br>Reg.Gen.</td>
      <!--
      <td class="int">Definitivo il</td>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Autorità</td>
      -->
      <td class="int">&nbsp;</td>
    </tr>
  <%
  Iterator itx = ListaTitoli.iterator();
  while ( itx.hasNext()) 
  {
    boolean isTitoloCorrente = false;
    boolean isInContinuazione = false;

    TitoloCumulatoModel lTitoloModel = (TitoloCumulatoModel)itx.next();
    
    if (lTitoloModel.getIdTitoloCumulato().compareTo(TitoloInCumulo.getIdTitoloCumulato())==0 )
      continue;
      
    // Verifico se già in continuazione
    Iterator lIterContinuazioni = ListaContinuazioni.iterator();
    while (lIterContinuazioni.hasNext()){
      ContinuazioneCumuloModel lContinuazione = (ContinuazioneCumuloModel) lIterContinuazioni.next();
      if (   lContinuazione.getTitIdTitoloCumulatoCont()!=null 
          && lContinuazione.getTitIdTitoloCumulatoCont().compareTo(lTitoloModel.getIdTitoloCumulato())==0
         )
      {
        isInContinuazione = true;
        break;
      }
    }
    
    String lDescrtipoTitolo = lTitoloModel.getDescrTipoProvvedimento();
    
    if ("02".equals(lTitoloModel.getCodTipoProvvedimento())) {
      String [] lUfficiSorv = new String[] {"UDS","TDS","UDSM"};
      if (!Arrays.asList(lUfficiSorv).contains(lTitoloModel.getCodTipoAutoritaEmittente())){
        lDescrtipoTitolo = "Decreto Penale";
      }
    }

    String anno_RGNR = StringUtils.toStringJSP(lTitoloModel.getAnnoRegePm(),"");
    String numero_RGNR = StringUtils.toStringJSP(lTitoloModel.getNumeroRegePm(),"");
    String lRGNR = anno_RGNR+"/"+numero_RGNR;
    if (lRGNR.equals("/")) lRGNR = "";
    
    String tipo_reg = StringUtils.toStringJSP(lTitoloModel.getTipoRegGen(),"");
    String anno_reg = StringUtils.toStringJSP(lTitoloModel.getAnnoRegGen(),"");
    String num_reg  = StringUtils.toStringJSP(lTitoloModel.getNumeroRegGen(),"");
    String lRegGen = anno_reg+"/"+num_reg+"&nbsp;"+tipo_reg;
    if (lRegGen.equals("/&nbsp;")) lRegGen = "";

    String AutEmi = lTitoloModel.getDescrTipoAutoritaEmittente()+" di "+lTitoloModel.getDescrLuogoEmittente();
    
    if (lTitoloModel.getNumSezioneAutoritaEmittente()!=null)
       AutEmi += " - sez. "+lTitoloModel.getNumSezioneAutoritaEmittente();

    String nSiep = "&nbsp;";
    String AutoritaSiep = "";
    ProcedimentoCumulatoModel lProcedimentoCumulatoModel = lTitoloModel.getProcedimentoCumulato();
    if (lProcedimentoCumulatoModel!=null)
    {
      nSiep = lProcedimentoCumulatoModel.getChiaveAnnoFasCumulato() +"/"+ lProcedimentoCumulatoModel.getChiaveProgrFasCumulato();
      AutoritaSiep = StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrTipoUfficioFasCumulato())+" di "+StringUtils.toStringJSP(lProcedimentoCumulatoModel.getDescrLuogoUfficioFasCumulato());
    }    
    
    %>
    <tr>
      <td class="c"><%=lDescrtipoTitolo%></td>
      <td class="l" nowrap>          
        <%=lTitoloModel.getAnnoSentenza()%> / <%=lTitoloModel.getNumeroSentenza()%>
      </td>
      <td class="c"  nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></td>
      <td class="l" >&nbsp;<%=AutEmi%></td>
      <td class="l"  nowrap>&nbsp;<%=lRGNR%></td>
      <td class="l"  nowrap>&nbsp;<%=lRegGen%></td>
      <%--
      <td class="c" nowrap>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoloModel.getDataIrrevocabilita(),"dd-MM-yyyy"))%></td>
      <td class="c"  nowrap><%=nSiep%></td>
      <td class="c"  >&nbsp;<%=AutoritaSiep%></td>
      --%>
      <% if (isInContinuazione) { %>
      <td class="c">
        <font class="cRosso"> In Continuazione </font>
      </td>
      <% } else { %>
      <td class="c">
        <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(lTitoloModel.getIdTitoloCumulato(),"-")%>',
        
                                     '<%=StringUtils.toStringJSP(lTitoloModel.getAnnoSentenza(),"-")%>',
                                     '<%=StringUtils.toStringJSP(lTitoloModel.getNumeroSentenza(),"-")%>',
                                     
                                     '<%=StringUtils.toStringJSP (DateUtils.getDateToString (lTitoloModel.getDataProvvedimento(),"dd"),"-")%>',
                                     '<%=StringUtils.toStringJSP (DateUtils.getDateToString (lTitoloModel.getDataProvvedimento(),"MM"),"-")%>',
                                     '<%=StringUtils.toStringJSP (DateUtils.getDateToString (lTitoloModel.getDataProvvedimento(),"yyyy"),"-")%>',

                                     '<%=StringUtils.toStringJSP (lTitoloModel.getCodTipoAutoritaEmittente(),"-")%>',
                                     '<%=StringUtils.cStrForJS   (lTitoloModel.getDescrLuogoEmittente())%>',

                                     '<%=StringUtils.toStringJSP (lTitoloModel.getAnnoRegePm(),"-")%>',
                                     '<%=StringUtils.toStringJSP (lTitoloModel.getNumeroRegePm(),"-")%>',
                                     
                                     '<%=StringUtils.toStringJSP (lTitoloModel.getAnnoRegGen(),"-")%>',
                                     '<%=StringUtils.toStringJSP (lTitoloModel.getNumeroRegGen(),"-")%>',
                                     '<%=StringUtils.toStringJSP (lTitoloModel.getTipoRegGen(),"-")%>'
                                    );">
          <img align="middle" src="/images/fileselected.gif" border=0>
        </a>
      </td>
      <% } %>
    </tr>
    <%
  }
%>
</table>
</body>
</html>