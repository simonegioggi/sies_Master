<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>


<jsp:useBean id="nextAction"  scope="request" class="java.lang.String"/>
<jsp:useBean id="functionName"  scope="request" class="java.lang.String"/>
<jsp:useBean id="idAvvocato"  scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="azioneChiamante" scope="request" class="java.lang.String"/>

<%
if (nextAction.equals(""))
  nextAction="siap.sige.fascicolo.action.ActRicercaFSigePuntuale";

if (functionName.equals(""))
  functionName="Ricerca Fascicolo Sige Puntuale";

FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel)session.getAttribute("FascicoloSigeEsteso");

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
      document.LoadRicercaFSigePuntuale.<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.focus();
   }

function setta()
{
<%
  if (lFascicoloEsteso != null && lFascicoloEsteso.getFascicoloSige() != null)
  {
%>
        document.getElementById("<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>").value="<%=lFascicoloEsteso.getFascicoloSige().getChiaveAnno()%>";
        document.getElementById("<%= ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN %>").value="<%=lFascicoloEsteso.getFascicoloSige().getChiaveProgr()%>";
<% }%>
}


 </script>
</head>
  <body class="corpo"  onLoad="Javascript:init();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=functionName%></font></td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaFSigePuntuale'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=nextAction%>" />
    <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="<%=idAvvocato%>" />
    <input type="HIDDEN" name="azioneChiamante" value="<%=azioneChiamante%>">
    
    <table cellspacing=2 cellpadding=2>
      <%--tr>
        <td class="l">Indicare gli estremi del titolo esecutivo :</td>
      </tr--%>

      <tr>
        <td class="l">Numero SIGE (Anno/Progressivo) <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="Anno SIGE"  type="text" name="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>" 
          <%-- Ticket: 20221220011: aggiunti id sui campi per la getElementById andava in errore su EDGE --%>
                                                  id="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>" 
                  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onblur="javascript:value=FillYear(value)">
          /<input Title="Numero SIGE" type="text" name="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN %>" 
          <%-- Ticket: 20221220011: aggiunti id sui campi per la getElementById andava in errore su EDGE --%>
                                                    id="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN %>"
                  maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          <input type="hidden" name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>" 
          <%-- Ticket: 20221220011: aggiunti id sui campi per la getElementById andava in errore su EDGE --%>          
                                 id="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>" value="">
          <input type="hidden" name="<%=IWebConstants.LINK_RITORNO %>" value="<%=TornaQui %>">
        </td>
<%  if (lFascicoloEsteso != null && lFascicoloEsteso.getFascicoloSige() != null)
  {
%>
        <td class="menulines" align="center">
          <a  style="" name="aa" href="Javascript:setta()" title="Seleziona Procedimento Corrente (<%=lFascicoloEsteso.getFascicoloSige().getChiaveAnno()%>/<%=lFascicoloEsteso.getFascicoloSige().getChiaveProgr()%>)">
                <img align="middle" src="../../images/ActiveSession.gif" width="32" height="32" border="0">
          </a>
        </td>
<%
}%>
      </tr>

      <tr>
        <td class="l">Ufficio Accorpato</td>
        <td class="l">
         	<select name="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>" 
            <%-- Ticket: 20221220011: aggiunti id sui campi per la getElementById andava in errore su EDGE --%>
         	          id="<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>">
         	<option value="0" >-</option>
  <%
  Iterator it = elencoUfficiAccorpati.iterator();
  int indice = 0;
  while (it.hasNext())
  {
  	UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
  %>
         	<option value="<%=ua.getIncrProgressivo()%>" ><%=ua.getDescrizione()%> (<%=ua.transCodingTipoUfficioSige()%>)</option>
  <%
	indice ++;
  }
  %>
         	</select>
        </td>
      </tr>

    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" type="submit"  name="RICERCA" value="Conferma" onClick="javascript:return checkNewProg();">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaFSigePuntuale");

    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","req","Il campo Chiave  Anno  è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","numeric");

    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>","req","Il campo Chiave  Progressivo è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric");

    function checkNewProg(){
        var numProg = document.getElementById("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR_ORIGIN%>").value;
        var offSet = document.getElementById("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_ACCORPATO%>").value;
        var newProg = parseInt(numProg) + parseInt(offSet);
        document.getElementById("<%=ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>").value = newProg;
        return true;
    }
  </script>
  </body>
</html>