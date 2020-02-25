<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato" %>
<%@ page import="siap.sico.magistrato.model.MagistratoModel" %>
<%@ page import="siap.sico.w_magistrato.model.WMagistratoModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>

<jsp:useBean id="magistrati" scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="formname" scope="request" class="java.lang.String" />


<html>
  <head>
    <title>[S.I.E.S.] - Lista Magistrati</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <%
    if (!modalita.equals("NoPop")) {
    %>
    <script language="JavaScript">
        function insertIT(cod,cognome,nome)
        {
          // controlla che all'interno della form di provenienza ci siano i campi per inserire il magistrato
          // se esistoino scrive tutti i campi
          if (   window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%> != null
              && window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMagistrato.CAMPO_COGNOME%> != null
              && window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMagistrato.CAMPO_NOME%> != null
            ) 
          
          {
            window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value=cod;
            window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value=cognome;
            window.parent.opener.document.<%=formname%>.<%=ICostantiMagistrato.CAMPO_NOME%>.value=nome;
          }
          else
          { // se i campi non esistono carico nel campo dal quale è stato chiamata la funzione il cognome e nome del magistrato
            var chk_fieldname = "<%=request.getParameter("fieldname")%>";
            
            if(chk_fieldname != "null")
            {
              var CognNom = cognome+" "+nome;
              var nomeForm = "<%=request.getParameter("formname")%>";
              var nomeField = "<%=request.getParameter("fieldname")%>";
              eval("window.parent.opener.document."+ nomeForm +"."+ nomeField +".value='" + CognNom+"'");
            }
          }
          
          window.parent.close();
          return;
        }
    </script>
    <%
    }
    %>
  </head>

  <body class=corpo>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Magistrati Ufficio</font></td>
      </tr>
    </table>

    <Table width="100%">
    <tr>
    <td class=int>Cod</td>
    <td class=int>Nome</td>
    <!-- A8RR084 Oscurata data di nascita -->
    <!-- td class=int>Data Nascita</td-->
    <%
    if (!modalita.equals("NoPop")) {
    %>
      <td class=int>Seleziona</td>
    <%
    }
    %>
    </tr>
    <%
      Iterator itx = magistrati.iterator();

      while (itx.hasNext()) {
        MagistratoModel lMag = (MagistratoModel) itx.next();
    %>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lMag.getCodMagistrato(), "-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lMag.getCognome(), "-")+ " " + StringUtils.toStringJSP(lMag.getNome(), "-")%></td>
        <!-- A8RR084 Oscurata data di nascita -->
        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--td class=l><%=DateUtils.getDateToString(lMag.getDataNascita(),"dd-MM-yyyy")%> </td--%>
      <%
      if (!modalita.equals("NoPop")) {
      %>
        <td class=c><a href="Javascript:insertIT('<%=StringUtils.cStrForJS(lMag.getCodMagistrato())%>','<%=StringUtils.cStrForJS(lMag.getCognome())%>','<%=StringUtils.cStrForJS(lMag.getNome())%>');"> <img align="middle" src="/images/fileselected.gif" border=0></a></td>
        <%
        }
        %>
      </tr>
    <%
    }
    %>
    </table>
  </body>
</html>