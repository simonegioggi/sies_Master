<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel" %>

<jsp:useBean id="ListaProvv" scope="request" class="java.util.Vector" />

<!-- 	Popup	ListaProvvedimentiGiudiceCassazione		 -->
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
        if( !ListaProvv.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");

          window.parent.close();
        }
      }

      function insertIT( tipoAutorita,
    		  			 DescTipoAut,
                         sedeAutorita,
                         tipoprovvedimento,
                         DescTipoProvv,	
                         giorno,
                         mese,
                         anno,
                         giornoric,
                         meseric,
                         annoric,
                         annoprov,
                         numprov,
                         codesito
                         )
      {

    	 // alert('DescTipoAut = '+DescTipoAut);

       if (tipoAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value=tipoAutorita;

        if (DescTipoAut!='-')
           	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_DESC_TIPO_AUTORITA_EMITTENTE%>.value=DescTipoAut;
          
        if (sedeAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.value=sedeAutorita;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_LUOGO_EMITTENTE%>.value="";
  
       if (tipoprovvedimento!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.value=tipoprovvedimento;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_TIPO_PROVVEDIMENTO_ARC%>.value="";

       if (DescTipoProvv!='-')
         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_DESC_TIPO_PROVVEDIMENTO_ARC%>.value=DescTipoProvv;
          
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
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value=annoprov;
      	else
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ANNO_PROTOCOLLO%>.value="";

      	if (numprov!='-' )
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value=numprov;
      	else
        	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_PROGR_PROTOCOLLO%>.value="";
        	
   		//alert('codesito = '+codesito);
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_COD_ESITO%>.value=codesito;
        // Esito Giudice / Oggetto Archiviazione (ComboBox)
         	if(codesito=='0054' || codesito=='0400' || codesito=='0401' ||
         	   codesito=='0402' || codesito=='0404' || codesito=='0405' )
         	{
         		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.value="2791";
         	}
         	else
         	{
         		window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>.value="-";
         	}	

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
        <font class="campo">Lista Provvedimenti Giudice/Cassazione

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
    if( !ListaProvv.isEmpty() )
    {
      Iterator itx = ListaProvv.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
      	  DecretoOrdinanzaSiepModel lDec = (DecretoOrdinanzaSiepModel)itx.next();
%>
        <tr>
          <td class="l">
            <%=StringUtils.toStringJSP(lDec.getDescrTipoAutoritaEmittente(),"-")%> di <%=StringUtils.toStringJSP(lDec.getDescrLuogoEmittente(), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataEmissioneProvvedimento(),"dd-MM-yyyy"), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(lDec.getDescrOggettoProcedimento(),"-")%>
          </td>
          <td class="c">        
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(lDec.getCodTipoAutoritaEmittente(),"-")%>',
            							 '<%=StringUtils.cStrForJS(StringUtils.toStringJSP(lDec.getDescrTipoAutoritaEmittente(),"-"))%>',
            							 '<%=StringUtils.cStrForJS(StringUtils.toStringJSP(lDec.getDescrLuogoEmittente(),"-"))%>',
                                         '<%=StringUtils.toStringJSP(lDec.getCodTipoProvvedimento(),"-")%>',
                                         '<%=StringUtils.cStrForJS(lDec.getDescrTipoProvvedimento())%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataEmissioneProvvedimento(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataEmissioneProvvedimento(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataEmissioneProvvedimento(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataRicezioneProvvedimento(),"dd"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataRicezioneProvvedimento(),"MM"),"-")%>',
                                         '<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDec.getDataRicezioneProvvedimento(),"yyyy"),"-")%>',
                                         '<%=StringUtils.toStringJSP(lDec.getAnnoProvvedimento(),"-")%>',
                                         '<%=StringUtils.toStringJSP(lDec.getNumProvvedimento(),"-")%>',
                                         '<%=StringUtils.toStringJSP(lDec.getCodEsito(),"-")%>'
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