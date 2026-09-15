<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaFascicoliModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="sentenzefascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Sentenza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
var node;
function effettoTree(a)
{
  node=document.getElementById("elenco"+a);
  node.style.display = (node.style.display == "none")? "block" : "none";
  document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
  return false;

}

 function fascicolo(id)
 {

  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=true;
  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=false;
  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.value=id;
 }

 function sentenza(id)
 {
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=false;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.value=id;
 }

 function nuovo()
 {
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>.disabled=true;
 }
</script>
  </head>

  <BODY class="corpo" onLoad="Javascript:nuovo()">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerTitoloEsecutivo">
  <input type="hidden" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value=""> 
  <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value=""> 
  
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Iscrizione Istanza - Elenco Titoli Esecutivi</font></td>

				<!-- BOTTONE DI RITORNO -->
				<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
			</tr>

    </table>

    <br>
 
<%if (!(RequestForPaging.equals("NO"))) {%>
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<%}%>

<br>
<br>
<%
  String wCol1="100";
  String wCol2="180";
  String wCol3="280";
  String wCol4="240";
  String wCol5="100";
  String wCol6="100";


%>
      <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
        <td class="l" colspan="5">
                  Inserire un Nuovo Titolo Esecutivo
        </td>
        <td class="c">          
                 <input type="radio" title="Inserire un Nuovo Titolo Esecutivo" checked name="radioins" onClick ="Javascript:nuovo()">
        </td>
  
    </tr>
    <tr>
        <td class="int" width="<%=wCol1%>">Data Tit.Esecutivo</td>
        <td class="int" width="<%=wCol2%>">Anno/Numero</td>
        <td class="int" width="<%=wCol3%>">Tipo Tit.Esecutivo</td>
        <td class="int" width="<%=wCol4%>">Autorità</td>
        <td class="int" width="<%=wCol5%>">Dett.</td>
        <td class="int" width="<%=wCol6%>">Sel</td>
  
    </tr>
    </table>
 
<%
    int jPA =0;
    Vector lFascicoli = new Vector();
    Iterator itx = sentenzefascicoli.iterator();
    while ( itx.hasNext())
    {
      SentenzaFascicoliModel lSenFasMod= (SentenzaFascicoliModel)itx.next();
      SentenzaModel sentenza = lSenFasMod.getSentenza();
%>
     	<table cellspacing=2 cellpadding=2 width="100%">
   			<tr>
       		<td class=C width="<%=wCol1%>">
         		<%=StringUtils.toStringJSP(DateUtils.getDateToString(sentenza.getDataProvvedimento(),"dd-MM-yyyy"))%>
       		</td>
       		<td class=C width="<%=wCol2%>">
         		<%=StringUtils.toStringJSP(sentenza.getNumeroSentenza()).length()>0
         			? StringUtils.toStringJSP(sentenza.getAnnoSentenza())+"/"+StringUtils.toStringJSP(sentenza.getNumeroSentenza()) : "-"%>
<%
       				if(lSenFasMod.getFascicoli() != null &&  lSenFasMod.getFascicoli().length>0)
     					{
%>
         				<a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Fascicoli" ></a>
<%
       				}
%>       
       		</td>
       		<td class=C width="<%=wCol3%>">
         		<%=StringUtils.toStringJSP(sentenza.getDescrTipoProvvedimento())%>
       		</td>
       		<td class=C width="<%=wCol4%>">
         		<%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
       		</td>
       		<td class=C width="<%=wCol5%>">
        	<jsp:include page="<%=ICostantiNuovaIstanza.PG_BUTTONS_SENTENZA_NUOVAISTANZA%>">
           	<jsp:param name="CampoIdEntita" value="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>"/>
           	<jsp:param name="ValoreIdEntita" value="<%=sentenza.getIdSentenza()%>"/>
        	</jsp:include>
     			</td>
     			<td class=C width="<%=wCol6%>">
         		<input type="radio" title="Selezione del solo Titolo Esecutivo" name="radioins"  onClick ="Javascript:sentenza('<%=sentenza.getIdSentenza() %>')">   
     			</td>
    		</tr>
<%
				//Caricamento Altri Esiti.
				if (lSenFasMod!=  null && lSenFasMod.getFascicoli() != null && lSenFasMod.getFascicoli().length>0)
				{
 					for(int j=0;j<lSenFasMod.getFascicoli().length;j++)
 					{
   					FascicoloSiepModel lFasMod = new FascicoloSiepModel(lSenFasMod.getFascicoli()[j]);
   					lFascicoli.add(lFasMod);
 					}
%>
 					</table>
   				<div id="elenco<%=jPA%>" style="display:none; width:100%;">
     				<%@include file="/jsp/files/siap/siep/nuovaistanza/ListaFascicoliPerNuovaIstanza.jspf" %>
   				</div>
<%
   				lFascicoli.clear();
   				jPA++;
				}
				else
				{%>
 					</table>
			<%}
		}
%>
<br>
  <table>
     <tr>
      <td>
        <input class="bottone" type="submit" name="Avanti" value="Avanti >>>">
      </td>
    </tr>
  </table>
  </FORM>
  <br>

</body>
</html>