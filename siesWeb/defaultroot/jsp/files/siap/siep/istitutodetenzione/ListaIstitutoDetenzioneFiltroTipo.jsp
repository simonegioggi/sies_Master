<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" %>

<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.util.StringUtils" %>

<jsp:useBean id="ListaComuni"    scope="request" class="java.util.Vector" />
<jsp:useBean id="modalita"       scope="request" class="java.lang.String" />
<jsp:useBean id="formname"       scope="request" class="java.lang.String" />
<jsp:useBean id="LoadDescEstesa" scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Comuni</title>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">

    <% if (! modalita.equals("NoPop")) { %>
    <script language="JavaScript">
        function insertIT(id,comuneind,tipo,istDesc, istInd)
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=id;
          
          <% if ("SI".equals(LoadDescEstesa)) { %>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("field2")%>.value=tipo+' di '+istDesc+' - '+istInd;
          <% } else { %>
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("field2")%>.value=tipo+' di '+comuneind;
          <% } %>
          window.parent.close();
          return;
        }
    </script>
    <% } %>
    
    <script language="JavaScript">
      function istituti()
      {
        if ("<%=ListaComuni.size()%>" == 0)
          alert('Attenzione! Nessun Istituto di Detenzione trovato.');
        if ("<%=ListaComuni.size()%>" == 200)
          alert('Attenzione! Visualizzati solo i primi 200 comuni individuati. Perfezionare la ricerca!');
      }
    </script>
  </head>

  <body class=corpo onload="istituti();">
    <table>
      <tr>
        <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Comuni Istituto Detenzione</font></td>
      </tr>
    </table>
<%if(ListaComuni.size()>0){%>
    <Table width="100%">
    <tr>
    <td class=int>Tipo</td>
    <td class=int>Indirizzo</td>
    <td class=int>Luogo</td>
    <% if (! modalita.equals("NoPop"))
    { %>
      <td class=int>Seleziona</td>
    <%
    } %>
    </tr>
    <%
    Iterator itx = ListaComuni.iterator();


    while ( itx.hasNext() )
    {
      IstitutoDetenzioneModel lIstituto = (IstitutoDetenzioneModel)itx.next();

      if(!lIstituto.getDescrTipoIstituto().equals("-"))
      {%>
      <tr>
        <td class=l><%=StringUtils.toStringJSP(lIstituto.getDescrTipoIstituto(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lIstituto.getIndirizzo(),"-")%></td>
        <td class=l><%=StringUtils.toStringJSP(lIstituto.getDescrizione(),"-")%></td>
      <%
      
      if (! modalita.equals("NoPop"))
      { %>
        <td class=c>
          <a href="Javascript:insertIT('<%=lIstituto.getIdIstitutoDetenzione()%>'
                                      ,'<%=StringUtils.cStrForJS(lIstituto.getDescrComune()) %>'
                                      ,'<%=StringUtils.cStrForJS(lIstituto.getDescrTipoIstituto())%>'
                                      ,'<%=StringUtils.cStrForJS(lIstituto.getDescrizione()) %>'
                                      ,'<%=StringUtils.cStrForJS(lIstituto.getIndirizzo()) %>'
                                      );"> 
            <img align="middle" src="/images/fileselected.gif" border=0>
          </a>
        </td>
        <% } %>
      </tr>
    <%}
    }
    %>
    </table>
<%}%>
  </body>
</html>