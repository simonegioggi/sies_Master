<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>


<jsp:useBean id="eventi"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="ModificaEseguita" scope="request" class="java.lang.String"/>


<html>
  <head>
    <title>[S.I.E.S.] - Gestione  Stato Esecuzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      //==========================================================================
      // Determina i quantum di pena residui alla data di systema
      //==========================================================================
      function CalcoloResiduoPena (){
        var calcoloURL = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.calcolopena.action.ActCalcolaPenaResiduaAl";
  
        desktop = window.open(calcoloURL, "Pena_Residua_Al", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=no, resizable=no, width=400, height=200, location=no");
      }
    </script>
  </head>


<%if(ModificaEseguita!=null && ModificaEseguita.equals("SI"))
{%>
<body class="corpo" onload="javascript:alert('Aggiornamento eseguito correttamente!');">
<!--script language="VBScript">
        MsgBox "Aggiornamento eseguito correttamente!",64
</script -->
<%}else{%>
<body class="corpo">
<%}%>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;
      <font class="campo">Gestione  Stato Esecuzione  </font>&nbsp;&nbsp;
     </td>
    </tr>
  </table>
  <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaEsecuzione.jsp"/>
  
  <div align=center>
    <table cellspacing=2 cellpadding=2 width='100%'>
      <tr><td>&nbsp;&nbsp;</td></tr>

<%if(eventi!= null && eventi.size()>0)
{%>
    <tr><td class="Titolo"colspan=6>Stato Esecuzione</td></tr>
    <tr><td>&nbsp;&nbsp;</td></tr>
  </table>
  
<FORM method="POST" name="StatoEsecuzione" action="<%= IWebConstants.PG_MAIN%>" onSubmit="javascript:document.StatoEsecuzione.Aggiorna.disabled=true;">
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Data Emissione</td>
      <td class="int">Provvedimento</td>
      <td class="int">Autorità</td>
      <td class="int">Stampa</td>
 	    <td class="int">Visualizza</td>
    </tr>
    <input type="hidden" name="<%=ICostantiEvento.CAMPO_FLAG_STAMPA_SIEP%>" value="<%=""%>">
    <input type="hidden" name="<%=ICostantiEvento.CAMPO_FLAG_VIDEO_SIEP%>" value="<%=""%>">
<%
  String chk= null;
  Iterator itx = eventi.iterator();
  String colore= "l";
  
  // 20171219 [EC] : CORREZIONE ANOMALIA (richiesta siep dalla procura di Foggia.docx): nell'elenco dello stato di esecuzione devono
  // essere esplicitati i provvedimenti con esito 0601 (Decreti di Fissazione Udienza
  String descrProvvvedimento="";
  

  while ( itx.hasNext())
  {
    EventoModel lEvento = (EventoModel)itx.next();
     if(lEvento.getFlagDocumentoRegistrato()!= null && !lEvento.getFlagDocumentoRegistrato().equals("") && lEvento.getFlagDocumentoRegistrato().equals("A"))
     {
       colore="lRosso";
     }
     else
     {
       colore= "l";
     }
     
     if("0601".equals(lEvento.getCodEsito())){
    	 descrProvvvedimento = lEvento.getDescrEsito();
     }
     else
     {
    	 descrProvvvedimento = lEvento.getDescrTipoProvvedimento();
     }
%>
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEvento.getIdEvento()%>">
    <tr>
      <td class="<%=colore%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%>&nbsp;</td>
      <%-- MEV_39: aggiunto spazio --%>
      <td class="<%=colore%>"><%=StringUtils.toStringJSP(descrProvvvedimento)%>&nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%></td>
      <td class="<%=colore%>"><%= lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente() %></td>
<%
        String stampa=lEvento.getFlagStampaSiep();

        if (stampa!= null && stampa.equals("S"))
        { chk = "checked"; }
        else
        { chk = ""; }
%>
      <td class="l">
    <% if(lEvento.getFlagDocumentoRegistrato()!= null && !lEvento.getFlagDocumentoRegistrato().equals("") &&lEvento.getFlagDocumentoRegistrato().equals("A"))
     {%>
          <input type="checkbox" disabled name="<%= ICostantiEvento.CAMPO_FLAG_STAMPA_SIEP %>"  value="<%=lEvento.getIdEvento()%>">
      <%}else{%>

        <input type="checkbox"  name="<%= ICostantiEvento.CAMPO_FLAG_STAMPA_SIEP %>" <%=chk%> value="<%=lEvento.getIdEvento()%>">

     <%}%>
     </td>
<%
      String video =lEvento.getFlagVideoSiep();
      if (video!= null && video.equals("S"))
      {
        chk = "checked";
      }
      else
      { chk = ""; }
%>
      <td class="l">
        <input type="checkbox" name="<%= ICostantiEvento.CAMPO_FLAG_VIDEO_SIEP %>" <%=chk%> value="<%=lEvento.getIdEvento()%>">
      </td>
    </tr>
<%
  }
  String lAzione = new String();
  lAzione = "siap.siep.ordineesecuzione.action.ActModificaStatoEsecuzioneEvento";
%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
    <tr>
       <td>
        <INPUT class="bottone" type="submit" name="Aggiorna" value="aggiorna" >
       </td>
     </tr>
<%}else{
%>
 </table>
 <table width="100%">
<tr><td class="Titolo"colspan=6>Stato Esecuzione</td></tr>
 </table>
<%}%>
    </table>
  </div>
  </form>
</body>
</html>