<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>

<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>
<jsp:useBean id="IdEvento"        scope="request" class="java.lang.String"/>
<jsp:useBean id="lEve"        scope="request" class="siap.sico.evento.model.EventoModel"/>

<%
String dataInvio=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataInvioAtti() ,"dd-MM-yyyy"));
String ulterioreDescrizione=(lEve.getDescrizioneInvioAtti() == null?"":lEve.getDescrizioneInvioAtti());
String descrizioneTipologiaInvioAtti=lEve.getDescrizioneTipologiaInvioAtti();
String validato=(lEve.getFlagDocumentoRegistrato()==null?"N":lEve.getFlagDocumentoRegistrato());

%>
<html>
  <head>
    <title> [S.I.E.S.] -  Data Invio Atti in Archivio  - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>
    <script language="JavaScript" src="/html/gestisciUploadStampa.js"></script>  
    <script language="JavaScript" >
    function stampaSige(lAzione)
    {
        var template = "&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=";
        var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>="+lAzione+"&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEve.getIdEvento()%>";
        // Se nel dettaglio esiste la Lista di Template si legge il valore
        if (document.dettaglio != undefined && document.dettaglio.ListaTemplate != undefined)
        {
           template =  template + document.dettaglio.ListaTemplate.value;
           hrefStampa = hrefStampa + template;
           //alert("template ->" + template);
           //alert("hrefStampa ->" + hrefStampa);
        }

      stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
    }
    
    </script>

  </head>

 	<body class="corpo" onload="">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio  Data Invio Atti in Archivio </font>
        </td>
         <td class="LBG">
      <a href="Javascript:stampaSige('siap.sige.attiinarchivio.action.ActStampaAttiInArchivio')"  onclick="javascript:lookUpload();" >
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
      
      <%
      if (!validato.equalsIgnoreCase("S")) {
      %>
      
      <td class="LBG">
      <a  href="#1" onclick="javascript:lookUpload();">
        <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
      </a>
     </td>
        
        <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.attiinarchivio.action.ActLoadModificaDataInvioAttiInArchivio&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=lEve.getIdEvento()%>&ritorno=si&TornaQui=<%=TornaQui%>">
	        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Data Invio Atti in Archivio" width="24" height="24" border="0">
	   </a>
        </td>
        <%
      }
        %>
        
        <td class="LBG">
	        <a href="Javascript:conferma('siap.sige.attiinarchivio.action.ActCancellaDataInvioAttiInArchivio','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=lEve.getIdEvento()%>','TornaQui','<%=TornaQui%>');">
	            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Annulla Data Invio Atti in Archivio" width="24" height="24" border="0">
	        </a>
        </td>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  </tr>
  </table>

<jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
   <table cellspacing=2 cellpadding=2  width="95%">
    <tr>
      <td class="Titolo" colspan="4"> Data Invio Atti in Archivio </td>
    </tr>
    
    <tr>
      <td class="l" width="25%">Data Invio</td>
      <td class="L" colspan="3">
        <font class="campo"><%=dataInvio%></font>&nbsp;
      </td>
    </tr>
    
    <tr>
      <td class="l" width="25%">Tipologia Invio atti in Archivio</td>
      <td class="L" colspan="3">
        <font class="campo"><%=descrizioneTipologiaInvioAtti%></font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l" width="25%">Ulteriore Descrizione</td>
      <td class="L" colspan="3">
        <font class="campo"><%=ulterioreDescrizione%></font>
      </td>
    </tr>
</table>

<!--  <a name="upload" /> -->
   <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"   value="<%= lEve.getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sige.attiinarchivio.action.ActLoadDettaglioAttiInArchivio">
          </td>
        </tr>
     </table>
    </FORM>
   </div>
   <br />
  </body>
</html>