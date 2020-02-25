<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel"%>

<jsp:useBean id="documentiArch" scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ordinanza/Decreto GE/TDS/UDS</title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !documentiArch.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");

          window.parent.close();
        }
      }

      function insertIT( tipoAutorita,
                         sedeAutorita,
                         tipoprovvedimento,
                         giorno,
                         mese,
                         anno,
                         giornoric,
                         meseric,
                         annoric,
                         annoprov,
                         numprov
                         )
      {

       if (tipoAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=tipoAutorita;
   
        if (sedeAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.value=sedeAutorita;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.value="";
  
       if (tipoprovvedimento!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.value=tipoprovvedimento;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.value="";

        if (giorno!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value=giorno;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_EMISSIONE%>.value="";

        if (mese!='-' )
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value=mese;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_EMISSIONE%>.value="";

        if (anno!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value=anno;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_EMISSIONE%>.value="";

        if (giornoric!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_RICEZIONE%>.value=giornoric;

        if (meseric!='-' )
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_RICEZIONE%>.value=meseric;

        if (annoric!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_RICEZIONE%>.value=annoric;

       if (annoprov!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>.value=annoprov;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_ANNO_PROVVEDIMENTO%>.value="";

        if (numprov!='-' )
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>.value=numprov;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_NUM_PROVVEDIMENTO%>.value="";


        window.parent.close();
      }
  	</script>
  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">Ordinanza/Decreto GE/TDS/UDS

        </font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int">Autorità Emittente</td>
      <td class="int">Data Emissione</td>
      <td class="int">Oggetto</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( !documentiArch.isEmpty() )
    {
      Iterator itx = documentiArch.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
    	  MisuraAlternativaAggregatoModel lAgg = (MisuraAlternativaAggregatoModel)itx.next();
    	  EventoModel eventoModel = lAgg.getEventoNotifica().getEvento();
    	  
    	  BigDecimal lAnno = null;
    	  BigDecimal lNumero = null;
    	  if(lAgg.getDepositoDecreto() != null && lAgg.getDepositoDecreto().getAnnoS72() != null)
    	  {
    		  lAnno =  lAgg.getDepositoDecreto().getAnnoS72();
    		  lNumero = lAgg.getDepositoDecreto().getNumS72();
    	  }
    	  else if(lAgg.getDepositoOrdinanzaPc() != null && lAgg.getDepositoOrdinanzaPc().getAnnoS3() != null)
    	  {
    		  lAnno =  lAgg.getDepositoOrdinanzaPc().getAnnoS3();
    		  lNumero = lAgg.getDepositoOrdinanzaPc().getNumS3();
    	  }
%>
        <tr>
          <td class="l">
            <%=StringUtils.toStringJSP(eventoModel.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(eventoModel.getDescrLuogoEmittente(), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"dd-MM-yyyy"), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(eventoModel.getDescrMotivo(),"-")%>
          </td>
          <td class="c">        
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(eventoModel.getCodUfficio(),"-")%>',
            							 '<%=StringUtils.cStrForJS(eventoModel.getDescrLuogoEmittente())%>',
                                         '<%=StringUtils.toStringJSP(eventoModel.getCodTipoProvvedimento(),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataEmissione(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataRicezioneAtti(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataRicezioneAtti(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoModel.getDataRicezioneAtti(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(lAnno,"-")%>',
                                         '<%=StringUtils.toStringJSP(lNumero,"-")%>'
                                         );">
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
          </td>
        </tr>
<%
      }
    }
%>
    </table>
  </form>
</body>
</html>