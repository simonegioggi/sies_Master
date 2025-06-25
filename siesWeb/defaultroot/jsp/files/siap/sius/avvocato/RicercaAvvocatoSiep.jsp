<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="f3b.web.IWebConstants"%>

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
        function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,
                          pec,  <%-- MEV_21: nuovo dato --%>
                          codicefiscale,luogoNascita,
                          codStatoNascita, <%-- MEV_21: nuovo dato --%>
                          giornoNascita,meseNascita,annoNascita,
                          descComuneStudio, <%-- MEV_21: redidenza diventa descComuneStudio  --%>
                          stato)	<%-- MEV_21: nuovo dato --%>
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value=indirizzo;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value=telefono;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value=fax;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value=email;
          <%-- MEV_21: aggiunte PEC --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_PEC%>.value = pec;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value=codicefiscale;
    
          <%-- MEV_21: aggiunte visualizzazioneUlteriori informazioni --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_STATO_NASCITA%>.value = codStatoNascita;
          if (codStatoNascita == "039") {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = luogoNascita;
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = "";
          } else {
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value = "";
            window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_NASCITA_REGINDE%>.value = luogoNascita;
          }
          <%-- MEV_21: FINE --%>
  
          // Popola altri campi
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value=giornoNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value=meseNascita;
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value=annoNascita;
		      <%-- MEV_21 sostituzione di CAMPO_COD_COMUNE_RESIDENZA con CAMPO_DESC_COMUNE_STUDIO  --%>
          <%-- window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value=luogoNascita; --%>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
          <%-- window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value=residenza; --%>
          <%-- MEV_21 aggiunta gestione CAMPO_COD_NON_ATTIVITA  --%>          
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA%>.value = stato;           
            
          window.parent.close();
        }


  <%-- MEV_21: aggiunte visualizzazione Ulteriori informazioni --%>
  function altreInfo (idRecord) {
    var riga = document.getElementById(idRecord);
    if (riga.style.display =="none") {
        riga.style.display = "block";
        document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        document.images["image_"+idRecord].alt = "Collassa";
    } else {
        riga.style.display = "none";
        document.images["image_"+idRecord].src = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
        document.images["image_"+idRecord].alt = "Espandi";
    }
  }
  <%-- MEV_21: FINE --%>


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
  <%-- MEV_21 previsti nuovi campi
  <tr>
    <td class=int>Nome</td>
    <td class=int>Foro</td>
    <td class=int>Indirizzo</td>
    <% if (! modalita.equals("NoPop")){ %>
      <td class=int>Seleziona</td>
    <% } %>
  </tr>
  --%>

  <tr>
    <td class="int">Cognome e Nome</td>
    <td class="int">Codice Fiscale</td>
    <td class="int">Foro</td>
    <td class="int">Luogo e Data Nascita</td>
    <td class="int">Indirizzo Studio</td>
    <td class="int">Stato</td>
    <% if (! modalita.equals("NoPop")) { %>
    <td class="int">Seleziona</td>
    <% } %>
  </tr>
  
<%
  Iterator itx = avvocato.iterator();
  int id_record = 0;
  while ( itx.hasNext())
  {
    id_record += 1;
    AvvocatoModel lAvv = (AvvocatoModel)itx.next();
    String stato = StringUtils.cStrForJS(lAvv.getCodNonAttivita());

    %>
      <tr>
        <% if ("NO".equals(lAvv.getFlagRegInde())) { %>
        <td class=l><font class="cRosso"><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%><br>(non certificato RegInde)</font>
        <% } else { %>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%>
        <% } %>
          <a href="javascript:altreInfo('rec_<%=id_record%>')">
            <img name="image_rec_<%=id_record%>" style="vertical-align: middle;" align="middle" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" alt="Espandi" border="0">
          </a>
        </td>
        <%-- MEV_21: Aggiunto CF --%>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getCodiceFiscale())%>&nbsp;</td>

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
        
        <%-- MEV_21: Aggiunto Luogo e Data di nascita --%>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getDescLuogoNascita())%>,&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataNascita(),"dd-MM-yyyy"))%></td>

        <%-- MEV_21: Aggiunto Desc Comune Studio e stato attivita--%>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-") + " - " + StringUtils.toStringJSP(lAvv.getDescrComuneStudio())%></td>
        <td class=l><%=StringUtils.toStringJSP(lAvv.getDescrNonAttivita(),"-")%>&nbsp;</td>

        <% if (! modalita.equals("NoPop")) { %>
        <td class=c>
          <% if ("SI".equals(lAvv.getFlagRegInde())) { %>
          <a href="Javascript:insertIT('
                 <%=lAvv.getIdAvvocato()%>',
                '<%=StringUtils.cStrForJS(lAvv.getCognome())%>',
                '<%=StringUtils.cStrForJS(lAvv.getNome())%>',
                '<%=StringUtils.cStrForJS(lAvv.getForo())%>',
                '<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>',
                '<%=StringUtils.cStrForJS(lAvv.getTelefono())%>',
                '<%=StringUtils.cStrForJS(lAvv.getFax())%>',
                '<%=StringUtils.cStrForJS(lAvv.getEMail())%>',
                '<%=StringUtils.cStrForJS(lAvv.getPec())%>',  <%-- MEV_21: aggiunto--%>
                '<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>',
                '<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>',
                '<%=StringUtils.cStrForJS(lAvv.getCodStatoNascita())%>',  <%-- MEV_21: aggiunto--%>
                '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>',
                '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>',
                '<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>',
            <%-- MEV_21: sostituito '<%=StringUtils.cStrForJS(lAvv.getDescComuneResidenza())%>',  --%>
                '<%=StringUtils.cStrForJS(lAvv.getDescrComuneStudio())%>', <%-- MEV_21: aggiunto--%>
                '<%=stato%>' <%-- MEV_21: aggiunto--%>
              );">
          <img align="middle" src="/images/fileselected.gif" border=0></a>
          <% } %>&nbsp;
        </td>  
        <% } %>
        </tr>

        <%-- MEV_21: aggiunte ulteriori informazioni --%>
        <tr id="rec_<%=id_record%>" style="display:none;">
          <td class=l colspan="7">
            pec:&nbsp;<%=StringUtils.toStringJSP(lAvv.getPec())%>&nbsp;&nbsp;&nbsp;
            Tel:&nbsp;<%=StringUtils.cStrForJS(lAvv.getTelefono())%>&nbsp;&nbsp;&nbsp;
            Fax:&nbsp;<%=StringUtils.cStrForJS(lAvv.getFax())%>&nbsp;&nbsp;&nbsp;
            e-mail:&nbsp;<%=StringUtils.cStrForJS(lAvv.getEMail())%>
          </td>
        </tr>
        <%-- MEV_21: FINE --%>
  <% } %>
</table>

</body>
</html>