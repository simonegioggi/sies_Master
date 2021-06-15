<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

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
  function controlla(id)
  {
     if(id=="S")
     {
      alert("Il difensore risulta sospeso");
      return false;
     }
     if(id=="R")
     {
      alert("Il difensore risulta radiato");
      return false;
     }
     if(id=="A")
     {
      alert("Il difensore risulta non in attività");
      return false;
     }
     if(id=="B")
     {
      return true;
     }
  }

  function avvocati()
  {
    if ("<%=avvocato.size()%>" == 0)
      alert('Attenzione! Nessun Difensore trovato.');
    if ("<%=avvocato.size()%>" == 200)
      alert('Attenzione! Visualizzati solo i primi 200 Difensori individuati. Perfezionare la ricerca!');
  }

  //function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,codicefiscale,luogoNascita,giornoNascita,meseNascita,annoNascita,residenza,stato)
  function insertIT(id,cognome,nome,foro,indirizzo,telefono,fax,email,codicefiscale,luogoNascita,giornoNascita,meseNascita,annoNascita,descComuneStudio,stato)
  {
    var flag=controlla(stato);

    if(flag)
    {
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>.value=id;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value=cognome;
        
      if(nome == "-")
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value="";
      }else
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_NOME%>.value=nome;
      }
      
      //window.parent.opener.document.<-%=request.getParameter("formname")%->.<-%=ICostantiAvvocato.CAMPO_NOME%->.value=nome;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FORO%>.value=foro;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>.value=indirizzo;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_TELEFONO%>.value=telefono;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_FAX%>.value=fax;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_E_MAIL%>.value=email;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>.value=codicefiscale;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>.value=luogoNascita;

      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>.value=giornoNascita;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>.value=meseNascita;
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>.value=annoNascita;
      <%-- 20210610 	MEV_21 sostituzione di CAMPO_COD_COMUNE_RESIDENZA con CAMPO_DESC_COMUNE_STUDIO  --%>
      <%-- 	window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>.value = residenza; --%>
      window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiAvvocato.CAMPO_DESC_COMUNE_STUDIO%>.value = descComuneStudio;
      window.parent.opener.document.<%=request.getParameter("formname")%>.lTipoAvv.value = "SIES";

      window.parent.close();
    }
  }
  </script>
 <%
  }
 %>
</head>

<body class=corpo onload ="avvocati();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
    </tr>
  </table>
 <% if (modalita.equals("NoPop"))
  {
  %>
 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>
  <% } %>
  
<form name="f">
<%if(avvocato.size()>0){%>
 <Table width="100%">
  <tr>
  <td class=int  width=40%>Nome</td>
  <td class=int  width=10%>Foro</td>
  <td class=int  width=40%>Indirizzo</td>

  <% if (! modalita.equals("NoPop"))
  { %>
    <td class=int width=10%>Seleziona</td>
  <% } %>
  </tr>
 <%
    Iterator itx = avvocato.iterator();

    while ( itx.hasNext())
    {
      AvvocatoModel lAvv = (AvvocatoModel)itx.next();
  %>
  <tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%></td>
    
    <%
    Collection listaFori = DecodificheManager.getInstance().getForoAll();
    String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvv.getForo());
    
    %>
    
    <% if ("SOPPRESSO".equals(lStatoForo)) { %>
    <td class=l><font class="cRosso"><%=StringUtils.toStringJSP(lAvv.getForo())%> (soppresso)</font></td>
    <% } else { %>
    <td class=l><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
    <% } %>
    
    <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo()) + " - " + StringUtils.toStringJSP(lAvv.getDescrComuneStudio())%></td>
<%
String stato=null;
if(lAvv.getDataSospensione()!= null)
{
     stato="S";

}else if(lAvv.getDataRadiazione()!= null)
{
     stato="R";

}else if(!lAvv.getCodNonAttivita().equals("-"))
{
     stato="A";

}else
{
      stato="B";
}

%>
        <% if (! modalita.equals("NoPop"))
        { %>
        <td class=c><a href="Javascript:insertIT('<%=lAvv.getIdAvvocato()%>','<%=StringUtils.cStrForJS(lAvv.getCognome())%>','<%=StringUtils.cStrForJS(lAvv.getNome())%>','<%=StringUtils.cStrForJS(lAvv.getForo())%>','<%=StringUtils.cStrForJS(lAvv.getIndirizzo())%>','<%=StringUtils.cStrForJS(lAvv.getTelefono())%>','<%=StringUtils.cStrForJS(lAvv.getFax())%>','<%=StringUtils.cStrForJS(lAvv.getEMail())%>','<%=StringUtils.cStrForJS(lAvv.getCodiceFiscale())%>','<%=StringUtils.cStrForJS(lAvv.getDescLuogoNascita())%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"dd"))%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"MM"))%>','<%=StringUtils.cStrForJS(DateUtils.getDateToString(lAvv.getDataNascita(),"yyyy"))%>','<%=StringUtils.cStrForJS(lAvv.getDescrComuneStudio() )%>','<%=stato%>');"><img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <% } %>
        </tr>
  <%
  }
%>

</table>
<%}%>
</form>
</body>
</html>