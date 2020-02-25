<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istanza.model.IstanzaModel"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>

<jsp:useBean id="istanza"          scope="request" class="siap.siep.istanza.model.IstanzaModel"/>
<jsp:useBean id="camponota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>


<html>
  <head>
    <title>[S.I.E.S.] - Annullamento Istanza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
   <script language="JavaScript">

function verify()
{
    var lmotivazioni = document.f.motivazioni.value;
    window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istanza.action.ActCancellaIstanza&<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>="+<%=istanza.getIdIstanza()%>+"&motivazioni="+lmotivazioni+"&<%=IWebConstants.GOTO_PAGE%>=<%=StringUtils.toStringJSP((String)request.getAttribute(IWebConstants.GOTO_PAGE),"")%>";
    window.close();
}


</script>

  </head>
  <body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
         <%if(istanza != null &&  istanza.getCodStatoIstanza().equals("C")){ %>

		    <font class="campo">Motivazioni dell' Annullamento </font>
         <%}else{ %>
		    <font class="campo">Annullamento Istanza</font>

        <%}%>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
    <table width="80%">
      <tr>
        <td class="l" width="20%">Motivazioni </td>
         <%if(istanza != null &&  istanza.getCodStatoIstanza().equals("C")){ %>
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
      <input type="HIDDEN" name="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>" value="<%=istanza.getIdIstanza()%>">
         <%if(istanza != null &&  istanza.getCodStatoIstanza().equals("C")){ %>
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