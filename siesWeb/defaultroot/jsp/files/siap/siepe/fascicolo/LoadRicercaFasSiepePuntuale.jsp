<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="nextAction"  scope="request" class="java.lang.String"/>
<jsp:useBean id="functionName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ChiaveAnno"  scope="session" class="java.lang.String"/>
<%
if (nextAction.equals(""))
  nextAction="siap.siepe.fascicolo.action.ActRicercaFasSiepePuntuale";

if (functionName.equals(""))
  functionName="Ricerca Fascicolo Siepe Puntuale";

FascicoloSiepeEstesoModel fasSiepeEM = (FascicoloSiepeEstesoModel)session.getAttribute("FascicoloSiepeEsteso");

%>
<html>
<head>
  <title>[S.I.E.S.] - <%=functionName%></title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript">
   function init()
   {
      //alert("init ");
      document.LoadRicercaFasSiepePuntuale.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.focus();
   }
var node;
function setta()
{
<%
  if (fasSiepeEM !=null && fasSiepeEM.getFascicoloSiepe()!=null)
  {
%>
        document.getElementById("ChiaveAnno").value="<%=fasSiepeEM.getFascicoloSiepe().getChiaveAnno()%>";
        document.getElementById("ChiaveProgr").value="<%=fasSiepeEM.getFascicoloSiepe().getChiaveProgr()%>";
<% }%>
}


 </script>
</head>
  <body class="corpo"  onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=functionName%></font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaFasSiepePuntuale'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value=<%=nextAction%>>
    <table cellspacing=2 cellpadding=2>
      <%--tr>
        <td class="l">Indicare gli estremi del titolo esecutivo :</td>
      </tr--%>

      <tr>
        <td class="l">Numero SIEPE (Anno/Progressivo) <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="Anno SIEPE"  type="text" name="<%= ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          /<input Title="Numero SIEPE" type="text" name="<%= ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        </td>
<%  if (fasSiepeEM !=null && fasSiepeEM.getFascicoloSiepe()!=null)
  {
%>
        <td class="menulines" align="center">
          <a  style="" name="aa" href="Javascript:setta()" title="Seleziona Procedimento Corrente (<%=fasSiepeEM.getFascicoloSiepe().getChiaveAnno()%>/<%=fasSiepeEM.getFascicoloSiepe().getChiaveProgr()%>)">
                <img align="middle" src="../../images/ActiveSession.gif" width="32" height="32" border="0">
          </a>
        </td>
<%
}%>
      </tr>
    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit"  name="RICERCA" value="Conferma">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaFasSiepePuntuale");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","req","Il campo Chiave  Anno  è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","numeric");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>","req","Il campo Chiave  Progressivo è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>","numeric");
  </script>
  </body>
</html>