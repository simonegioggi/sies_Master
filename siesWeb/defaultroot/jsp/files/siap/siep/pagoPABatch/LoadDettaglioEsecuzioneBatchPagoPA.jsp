<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.utente.action.ICostantiUtente" %>
<%@ page import="siap.siep.pagoPaBatch.model.BatchPagopaModel" %>
<%@ page import="siap.siep.pagoPaBatch.action.ICostantiBatchPagoPa" %>
<%@ page import="siap.siep.pagoPaBatch.model.InvocazionePagopaModel" %>
<%@ page import="siap.siep.pagoPA.model.BollettinoPagopaModel"%>
<%@ page import="siap.siep.pagoPaBatch.action.ICostantiInvocazionePagopa"%>


<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="BatchModel" scope="request" class="siap.siep.pagoPaBatch.model.BatchPagopaModel" />
<jsp:useBean id="ListaInvocazioni" scope="request" class="java.util.Vector" />


<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title> [S.I.E.S.] - Esecuzione Batch PagoPa - </title>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/gestisciUploadStampa2.js"></script>

  
  <script language="JavaScript">
    function visualizzaDettaglio(idVis)
    {
      if (document.getElementById(idVis).style.display == "block")
          document.getElementById(idVis).style.display = "none";
      else
          document.getElementById(idVis).style.display = "block";
    }
    
    function stampaSiep(lAzione)
    {
       var hrefStampa = lAzione;
       var lIndice = hrefStampa.indexOf("?");

      //alert (lAzione);
       var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
      // alert (ciccio);
       stampa2("/jsp/files/Stampa.jsp",  parametri);
    }
    
    function lancioBatch() {
    	document.getElementById("waitDiv").style.display = "block";
    	return true;
    }
    
  </script>
  
  <style>
    td.int,td.c {
      padding-left: 10px;
      padding-right: 10px;
    }
  </style>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Dettaglio Esecuzione Batch PagoPa</font></td>
      <td class="LBG">
         <a href="javascript:history.go(-1)">
           <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>  
    </tr>
  </table>

<br>
<% 
String colorErr= "";
if (BatchModel.getNumErroriInvocazione()!=null && BatchModel.getNumErroriInvocazione().intValue()>0)
   colorErr = " style='color:red;' ";
%>
  <table style="max-width: 80%;">
    <tr>
      <td class="int" colspan="2">Dettaglio esecuzione del batch</td>
    </tr>  
    <tr>
      <td class="l">Avviato</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(BatchModel.getDataInizioEsecuzione(),"dd-MM-yyyy - HH:mm:ss") ,"")%></td>
    </tr>
    <tr>
      <td class="l">Terminato</td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(BatchModel.getDataFineEsecuzione(),"dd-MM-yyyy - HH:mm:ss") ,"")%></td>
    </tr>  
    <tr>
      <td class="l" nowrap>Tempo di Esecuzione (hh:mm:ss)&nbsp;&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getDurataAsString(),"")%></td>
    </tr> 
    <tr>
      <td class="l">Posizioni Verificate</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getNumPosDebitorieVerificate(),"n.d.")%></td>
      <td class="l">Interrogazioni per Codice Fiscale</td>
    </tr>
    <tr>
      <td class="l">IUV Verificati</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getNumIUVVerificati(),"n.d.")%></td>
      <td class="l">Interrogazioni per IUV per i Bollettini privi di Codice Fiscale</td>
    </tr> 
    <tr>
      <td class="l">Bollettini Pagati</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getNumBollettiniAggiornati(),"n.d.")%></td>      
      <td class="l">Bollettini aggiornati in stato Pagato</td>
    </tr>   
    <tr>
      <td class="l" <%=colorErr%>>Errori Invocazione</td>
      <td class="l" <%=colorErr%>><%=StringUtils.toStringJSP(BatchModel.getNumErroriInvocazione(),"n.d.")%></td>
      <td class="l">Errori nelle chiamate a PagoPa</td>
    </tr>   
    <tr>
      <td class="l">Esito</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getEsitoEsecuzione()!=null ? BatchModel.getEsitoEsecuzione().replaceAll("\\n", "<br>") : null,"n.d.")%></td>
    </tr>
    <tr>
      <td class="l">Errori</td>
      <td class="l"><%=StringUtils.toStringJSP(BatchModel.getErroreEsecuzione()!=null ? BatchModel.getErroreEsecuzione().replaceFirst("\\n", "").replaceAll("\\n", "<br>") : null,"&nbsp;")%></td>
    </tr> 
  </table>  

<% if (BatchModel.getErroreEsecuzione()!=null || 1==1) { %>
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="ActLancioBatch">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.pagoPA.action.ActAvvioManualeBatch">
  <INPUT class="bottone" type="submit" name="Lancia Batch" value="Lancio Manuale Batch">
</form>
<div align="center" id="waitDiv" style="display:none;width:80%;">
   <table bgcolor="#EEEEEE">
     <tr>
       <td>
         <img src="<%=IWebConstants.IMAGES_DIR%>rotelle3.gif" style="width:100px;">
       </td>
       <td>
         <font size="+1" color1=navy>
           Attendere... Batch in esecuzione.
         </font>
       </td>
     </tr>
   </table>
 </div>


<% } %>


<br>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>

    <table cellspacing="2" cellpadding="2" style="width:80%;">
      <tr>
        <td class="int">Data Richiesta</td>
        <td class="int">Codice Fiscale Controllato</td>
        <td class="int">IUV Controllato</td>
        <td class="int">XML - Richiesta</td>
        <td class="int">XML - Risposta</td>
        <td class="int">Errore Richiesta</td>
        <td class="int">Dettaglio Bollettini Aggiornati</td>
      </tr>
      <% 
      // Scorrere la lista delle chiamate
      for (int i = 0; i< ListaInvocazioni.size(); i++) 
      { 
        InvocazionePagopaModel invocaModel = (InvocazionePagopaModel) ListaInvocazioni.elementAt(i);
        Vector <BollettinoPagopaModel> listaBollettini = invocaModel.getListaBollettini();
        
        String linkXmlRichiesta = ICostantiInvocazionePagopa.CAMPO_ID_INVOCAZIONE_PAGOPA+"="+invocaModel.getIdInvocazionePagopa()+"&"+ICostantiInvocazionePagopa.CAMPO_TIPO_XML+"="+ICostantiInvocazionePagopa.CAMPO_XML_RICHIESTA;
        String linkXmlRisposta  = ICostantiInvocazionePagopa.CAMPO_ID_INVOCAZIONE_PAGOPA+"="+invocaModel.getIdInvocazionePagopa()+"&"+ICostantiInvocazionePagopa.CAMPO_TIPO_XML+"="+ICostantiInvocazionePagopa.CAMPO_XML_RISPOSTA;
      %> 
      <tr>
          <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(invocaModel.getDataInvocazione(),"dd-MM-yyyy - HH:mm:ss"),"")%></td>
          <td class="c"><%=StringUtils.toStringJSP(invocaModel.getCodiceFiscale(),"&nbsp;")%></td>
          <td class="c"><%=StringUtils.toStringJSP(invocaModel.getIuv(),"&nbsp;")%></td>
          <td class="c">
            <% if (invocaModel.getXmlRichiesta()!=null && invocaModel.getXmlRichiesta().length()>0) { %>
            <table>
	            <tr>
	              <td class="c" style="border-style:none;">
		              <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActDownloadXMLInvocazionePagoPa&<%=linkXmlRichiesta%>">
		                <img  alt="Scarica XML Richiesta" src="<%=IWebConstants.IMAGES_DIR%>download.png" border="0" style="width:25px;"></a>
	              </td>
	              <td class="c" style="border-style:none;">
	                <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActDownloadXMLInvocazionePagoPa&<%=linkXmlRichiesta%>">Scarica XML Richiesta</a>
	              </td>
	            </tr>
            </table>
          <% } else { %>
                XML Richiesta non disponibile
          <% } %>
          </td>
          <td class="c">
            <% if (invocaModel.getXmlRisposta()!=null && invocaModel.getXmlRisposta().length()>0) { %>
            <table>
              <tr>
	              <td class="c" style="border-style:none;">
	                <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActDownloadXMLInvocazionePagoPa&<%=linkXmlRisposta%>">
	                  <img  alt="Scarica XML Risposta" src="<%=IWebConstants.IMAGES_DIR%>download.png" border="0" style="width:25px;"></a>&nbsp;
	              </td>
	              <td class="c" style="border-style:none;">
	                <a  href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.pagoPaBatch.action.ActDownloadXMLInvocazionePagoPa&<%=linkXmlRisposta%>">Scarica XML Risposta</a>
	              </td>
              </tr>
            </table>
            <% } else { %>
                XML Risposta non disponibile
            <% } %>             
          </td>
          <td class="c"><%=StringUtils.toStringJSP(invocaModel.getErrore(),"&nbsp;").replaceAll("\n","<br>")%></td>
          <td class="c">
          <% if (listaBollettini.size()>0) {%>
            <a href="#" onclick="visualizzaDettaglio(<%=invocaModel.getIdInvocazionePagopa()%>);">Visualizza Bollettini Aggiornati</a>
          <% } %>&nbsp;
          </td>
    </tr>
    
    <% if (listaBollettini.size()>0) {%>
     <tr style="display:none;" id="<%=invocaModel.getIdInvocazionePagopa()%>">
      <td class="c" colspan="7">
        <table>
          <tr>
            <td class="int">N.ro Ordine</td>
            <td class="int">Tipo Pagamento</td>
            <td class="int">IUV</td>
            <td class="int">Importo</td>
            <td class="int">Importo Pagato</td>
            <td class="int">Data Pagamento</td>
            <td class="int">Data Scadenza</td>
            <td class="int">Stato</td>
          </tr>    
    
    <% 
      for (int j = 0; j< listaBollettini.size(); j++) 
      { 
        BollettinoPagopaModel bollettino = listaBollettini.elementAt(j);
     %>
          <tr>
            <td class="c"><%=StringUtils.toStringJSP(bollettino.getProgRata())%></td>
            <td class="c"><%=StringUtils.toStringJSP(bollettino.getDescrTipoRateizzazione())%></td>
            <td class="c"><%=StringUtils.toStringJSP(bollettino.getIuv(), "-")%></td>
            <td class="c"><%=StringUtils.toEuroFormat(bollettino.getImportoRata())%></td>
            <td class="c"><%=StringUtils.toEuroFormat(bollettino.getImportoPagato())%></td>
            <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bollettino.getDataAvvPagamento(), "dd/MM/yyyy"), "-")%></td>
            <td class="c"><%=StringUtils.toStringJSP(DateUtils.getDateToString(bollettino.getDataScadenza(), "dd/MM/yyyy"), "-")%></td>
            <td class="c"><%=StringUtils.toStringJSP(bollettino.getDescrStatoPagamento())%></td>
          </tr>
    <% } // end for %>
     
     
          </table>
        </td>
      </tr>
    <% 
    } // end if 
    } // end for
    %>
    </table>
    
    
    <br><br>
    
  </form>
   <script language="JavaScript" type="text/javascript">
   var frmvalidator  = new Validator("ActLancioBatch");
   frmvalidator.setAddnlValidationFunction("lancioBatch");
 </script>
</body>
</html>

