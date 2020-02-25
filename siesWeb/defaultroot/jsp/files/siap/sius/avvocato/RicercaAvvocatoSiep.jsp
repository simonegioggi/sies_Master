<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>


<jsp:useBean id="avvocato" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Comuni</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">

  <% if (! modalita.equals("NoPop"))
  {
  %>
        <script language="JavaScript">
        function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,codicefiscale,luogoNascita,giornoNascita,meseNascita,annoNascita,residenza)	
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value=indirizzo;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value=telefono;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value=fax;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value=email;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value=codicefiscale;
      
          // Popola altri campi
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value=giornoNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value=meseNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value=annoNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value=luogoNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value=residenza;
          window.parent.close();
        }
        </script>
 <%
  }
 %>
</head>

<body class=corpo>
  <table>
    <tr>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Avvocati</font></td>
    </tr>
  </table>
 <% if (modalita.equals("NoPop"))
  {
  %>
 <BR>
  <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
 <BR>
  <% } %>
  
<table width="100%">
  <tr>
    <td class=int>Nome</td>
    <td class=int>Foro</td>
    <td class=int>Indirizzo</td>
    <% if (! modalita.equals("NoPop")){ %>
      <td class=int>Seleziona</td>
    <% } %>
  </tr>
  
  
<%
  Iterator itx = avvocato.iterator();

  while ( itx.hasNext())
  {
    AvvocatoModel lAvv = (AvvocatoModel)itx.next();
    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td>
        
        <%
        Collection listaFori = DecodificheManager.getInstance().getForoAll();  
        String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvv.getForo());

        if ("SOPPRESSO".equals(lStatoForo))
        {
        %>
        <td class=l><font class="cRosso"><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%> (soppresso)</font></td>
        <% } else { %>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
        <% } %>
        
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></td>
        <% if (! modalita.equals("NoPop")) { %>
        <td class=c><a href="Javascript:insertIT('<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>','<%=StringUtils.cStrForJS(lAvv.getForo())%>','<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>','<%=StringUtils.cStrForJS(lAvv.getTelefono())%>','<%=StringUtils.cStrForJS(lAvv.getFax())%>','<%=StringUtils.cStrForJS(lAvv.getEMail())%>','<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>','<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>','<%=StringUtils.cStrForJS(lAvv.getDescComuneResidenza())%>');"><img align="middle" src="/images/fileselected.gif" border=0></a></td>  
        <% } %>
        </tr>
  <% } %>
</table>

</body>
</html>