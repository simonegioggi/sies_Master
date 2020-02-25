<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="evento"          scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="IdEvento"      scope="request" class="java.lang.String"/>
<jsp:useBean id="camponota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>


<html>
  <head>
  <%if(evento != null &&  evento.getFlagDocumentoRegistrato().equals("A")){ %>

    <title>[S.I.E.S.] - Annullamento Provvedimento </title>
  <%}else{%>
    <title>[S.I.E.S.] - Motivazioni dell'Annullamento  </title>

  <%}%>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
   <script language="JavaScript">

function verify()
{
    var lmotivazioni = document.f.motivazioni.value;
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActCancellaOrdinanzeDecreti&IdEvento="+<%=IdEvento%>+"&motivazioni="+lmotivazioni;
    window.close();
}


</script>

  </head>
  <body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
         <%if(evento != null &&  evento.getFlagDocumentoRegistrato().equals("A")){ %>

		    <font class="campo">Motivazioni dell' Annullamento </font>
         <%}else{ %>
		    <font class="campo">Annullamento Provvedimento</font>

        <%}%>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActCancellaProvvedimento"--%>
    <table width="80%">
      <tr>
        <td class="l" width="20%">Motivazioni </td>
         <%if(evento != null &&  evento.getFlagDocumentoRegistrato().equals("A")){ %>
           <td  class="l">
              <TEXTAREA title="Note" name="motivazioni" readonly cols=50 rows=3><%=StringUtils.toStringJSP(camponota.getDescr())%></textarea>
          </td>

        <%}else{%>
           <td  class="l">
             <TEXTAREA title="Note" name="motivazioni" cols=50 rows=3></textarea>
            </td>
        <%}%>

   </tr>
   <tr>
       <td class="lNoBord" colspan="2">
       <br><br>
      <input type="HIDDEN" name="IdEvento" value="<%=IdEvento%>">
         <%if(evento != null &&  evento.getFlagDocumentoRegistrato().equals("A")){ %>
      <INPUT class="bottone" type="submit" name="I" value="Chiudi" onClick="self.close();">

         <%}else{ %>

      <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="verify()">
        <%}%>
       </td>
   </tr>


  </table>
	</form>
	</body>
</html>