<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>


<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

<jsp:useBean id="istanze" scope="request" class="java.util.ArrayList" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Istanze</title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !istanze.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessuna istanza presente!");

          window.parent.close();
        }
      }

      function insertIT( id,
                         annoRegistro,
                         numeroRegistro,
                         dataIstanza,
                         codContenuto,
                         note)
      {
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>.value=id;

        if(annoRegistro!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO%>.value=annoSius;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO%>.value="";

        if(numeroRegistro!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO%>.value=numeroSius;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_PROGR_REGISTRO%>.value="";

        if (dataIstanza!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO%>.value=annoOrdinanza;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_ANNO_REGISTRO%>.value="";

        if (codContenuto!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO%>.value=numeroOrdinanza;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_COD_CONTENUTO%>.value="";

        if (contenuto!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_NOTE%>.value=tipoAutorita;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiNuovaIstanza.CAMPO_NOTE%>.value="";


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
        <font class="campo">
          Selezione Istanze

        </font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int">Anno/Numero Registro</td>
      <td class="int">Contenuto</td>
      <td class="int">Data</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( !istanze.isEmpty() )
    {
      Iterator itx = istanze.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
        NuovaIstanzaModel istanzaModel = (NuovaIstanzaModel)itx.next();
             
%>
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(istanzaModel.getAnnoRegistro(), "-")%>
            /
            <%=StringUtils.toStringJSP(istanzaModel.getProgrRegistro(), "-")%>
          </td>
          <td class="l">
            <%=StringUtils.toStringJSP(istanzaModel.getCodContenuto(),"-")%> 
            <br>del&nbsp;
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(istanzaModel.getDataIstanza(),"dd-MM-yyyy"), "-")%>

          </td>
         

          <td class="c">
            <a href="Javascript:insertIT('<%=StringUtils.toStringJSP(istanzaModel.getIdNuovaIstanza(),"-")%>',
                                         '<%=StringUtils.toStringJSP(istanzaModel.getAnnoRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(istanzaModel.getProgrRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(istanzaModel.getAnnoRegistro(),"-")%>',
                                         '<%=StringUtils.toStringJSP(istanzaModel.getCodContenuto(),"-")%>',
                                         '<%=StringUtils.cStrForJS(istanzaModel.getNote())%>'                                         
                                         );">
              <img align="middle" src="/images/fileselected.gif" border="0">
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