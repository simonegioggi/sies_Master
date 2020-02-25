<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.reatopredisposto.action.ICostantiReatoPredisposto" %>
<%@ page import="siap.siep.reatopredisposto.model.ReatoPredispostoModel" %>

<jsp:useBean id="reatopredisposto" scope="request" class="java.util.Vector" />
<jsp:useBean id="ComingFromInsert" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <body class="corpo">
<form>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font> <font class=campo>Elenco Reati Predisposti</font></td>

    </tr>
  </table>
</form>
  <br>

  <table>
<tr><td class=titolo colspan=11>Reati Predisposti</td></tr>
    <tr>
      <td class="int" >Nome Elemento</td>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art.Qual.</td>
      <td class="int">Comma</td>
      <td class="int">Lettera</td>
      <td class="int">Numero</td>
      <td class="int">Note Elemento</td>
      <td class="int" width=5%>Azioni</td>
    </tr>

<%

	Iterator itx = reatopredisposto.iterator();
	while (itx.hasNext()) {
	
	  ReatoPredispostoModel reato = (ReatoPredispostoModel)itx.next();

	  String lNomeElemento = "";
	  String lNoteElemento = "";
	  
	  if(reato.getProgrNorma().intValue() == 1) {
	  	lNomeElemento = reato.getNomeElemento();
	  	lNoteElemento = reato.getNoteElemento();	  	
	  }

%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(lNomeElemento)%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getAnnoFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumeroFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getArticolo(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrSottonumerazione(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getComma(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getLettera(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumero(),"-")%>&nbsp;</td>      
      <td class="l"><%=StringUtils.toStringJSP(lNoteElemento)%>&nbsp;</td>
      <td class=c>
      <table>
      <tr>              
      	<td class="c">
<%
 String modificabile = "SI";   
	//if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicolo.getCodUfficioInserimento()))
      //{modificabile = "SI";}
//   else
//      {modificabile = "NO";}
  // */
%>
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiReatoPredisposto.CAMPO_ID_REATO_PREDISPOSTO%>" />
           <jsp:param name="ValoreIdEntita" value="<%=reato.getIdReatoPredisposto()%>" />
          <jsp:param name="Modificabile" value="<%=modificabile%>" />
        </jsp:include>      

              </td>
      </tr>
      </table>
      </td>
    </tr>
<%
}%>  
</table>


  </body>
</html>