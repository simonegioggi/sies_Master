<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.web.IWebConstants"%>


<jsp:useBean id="avvocato"             scope="request" class="java.util.Vector" />
<jsp:useBean id="lFascicoloCompetenza" scope="request" class="java.lang.String" />

<html>
<head>
  <title>[S.I.E.S.] - Elenco Avvocati</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript">
  
    function VerifyChiamate(id)
    {
      <%if(lFascicoloCompetenza != null && lFascicoloCompetenza.equals("S")){%>
      if(id==1 || id==2){
        if( document.LoadRicercaAvvocato.numeroDifensori.value=="2"){
          if(document.LoadRicercaAvvocato.tipo[0].checked==false && document.LoadRicercaAvvocato.tipo[1].checked==false) {
            alert("Selezionare un difensore");
            return false;
          }
        }
        else {
          if(document.LoadRicercaAvvocato.tipo.checked==false){
            alert("Selezionare il difensore");
            return false;
          }
        }
      }
      
      if(id==1) {
        document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActDeassegnaDifensore";
      }
      else if(id==2){
        document.LoadRicercaAvvocato.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.avvocato.action.ActLoadSostituzioneDifensore";
      }
      <%}%>
    }
  </script>

</head>

<body class=corpo >
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaAvvocato" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Difensori</font></td>
    </tr>
  </table>

 <BR>
 <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
 <BR>

 <Table width="100%">
  <tr>
    <td class=int >Cognome e Nome</td>
    <td class=int >Foro</td>
    <td class=int >Tipo Difensore</td>
    <td class=int >Data Designazione/Nomina</td>
    <%if(lFascicoloCompetenza != null && lFascicoloCompetenza.equals("S")){%>
    <td class=int >Selezione</td>
    <%}%>
  </tr>

  <%
    Iterator itx = avvocato.iterator();
    while ( itx.hasNext()) {
      AvvocatoModel lAvv = (AvvocatoModel)itx.next();
  %>
	<tr>
  		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	    <%--td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td--%>
	    <td class=c><%=StringUtils.toStringJSP(lAvv.getCognome()) + " " +  StringUtils.toStringJSP(lAvv.getNome())%></td>
	    <td class=c><%=StringUtils.toStringJSP(lAvv.getForo())%></td>
	    <td class=c><%=StringUtils.toStringJSP(lAvv.getDescrTipo())%></td>
	    <td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAvv.getDataInizioValidita(),"dd-MM-yyyy"))%></td>
	    <%if(lFascicoloCompetenza != null && lFascicoloCompetenza.equals("S")){%>
	    <td class="c"><input type="radio" name="tipo" value="<%=lAvv.getIdAvvocato()%>" >
	    <%}%>
	</tr>
  <%}%>
  
  
  <tr><td>&nbsp;</td></tr>
  <%if(lFascicoloCompetenza != null && lFascicoloCompetenza.equals("S")){%>
  <tr>
    <input type="hidden" name="numeroDifensori" value="<%=avvocato.size()%>">
    <td>
      <input class=bottone  type="submit" name="Revoca" value="Revoca" onclick="javascript:return VerifyChiamate(1);">
    </td>
    <td>
      <input class=bottone  type="submit" name="Sostituzione" value="Sostituzione" onclick="javascript:return VerifyChiamate(2);">
    </td>
  </tr>
  <%}%>

</table>
</form>
</body>
</html>