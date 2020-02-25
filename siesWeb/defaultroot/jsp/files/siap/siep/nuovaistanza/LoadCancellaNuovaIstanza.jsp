<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>

<jsp:useBean id="istanza"          scope="request" class="siap.siep.nuovaistanza.model.NuovaIstanzaModel"/>
<jsp:useBean id="camponota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Annullamento Istanza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
	   function Verify()
	   {
		  var lmotivazioni = document.f.motivazioni.value;
		  window.opener.top.frames['centrale'].frames['body'].location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.nuovaistanza.action.ActCancellaNuovaIstanza&<%=ICostantiNuovaIstanza.CAMPO_ID_NUOVA_ISTANZA%>="+<%=istanza.getIdNuovaIstanza()%>+"&motivazioni="+lmotivazioni;   
	      window.close();
	   }
    </script>
  </head>
  <body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
         <%          
         if(istanza != null &&  istanza.getCodStatoIstanza().equals("08")){ %>

		    <font class="campo">Motivazioni dell'Annullamento </font>
         <%}else{
        	 %>
		    <font class="campo">Annullamento Istanza</font>

        <%}%>
      </td>
    </tr>
  </table>

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">

    <table width="80%">
      <tr>
        <td class="l" width="20%">Motivazioni </td>
         <%if(istanza != null &&  istanza.getCodStatoIstanza().equals("08")){ %>
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
          <%if(istanza != null &&  istanza.getCodStatoIstanza().equals("08")){ %>
      <INPUT class="bottone" type="button" name="I" value="Chiudi" onClick="self.close();">

         <%}else{ %>

      <INPUT class="bottone" type="button" value="Conferma" onClick='Verify();'>
        <%}%>
       </td>
   </tr>
  </table>
  </form>
  </body>
</html>