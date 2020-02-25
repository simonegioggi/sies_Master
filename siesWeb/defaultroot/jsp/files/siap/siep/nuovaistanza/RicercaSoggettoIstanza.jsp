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
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="soggettifascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="RequestForPaging" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" 		scope="request" class="java.lang.String" />

<%@page import="siap.sico.soggetto.model.SoggettoFascicoliModel"%>
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
		//alert(">>>>>>>> IdFascicolo = "+id)
		document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=true;
		document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=false;
		document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.value=id;
 }

 function soggetto(id)
 {
		//alert(">>>>>>>> IdSoggetto = "+id)
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=false;
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.value=id;
 }

 function nuovo()
 {
	  document.elenco.<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>.disabled=true;
	  document.elenco.<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>.disabled=true;
 }
</script>
  </head>

  <BODY class="corpo" onLoad="Javascript:nuovo()">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.nuovaistanza.action.ActLoadInserisciIstanzaPerSoggetto">
  <input type="hidden" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value=""> 
  <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value=""> 
  
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Iscrizione Istanza - Elenco Soggetti</font></td>

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
  String wCol1="25%";
  String wCol2="14%";
  String wCol3="18%";
  String wCol4="14%";
  String wCol5="14%";
  String wCol6="8%";
  String wCol7="5%";
  String wCol8="5%";


%>
    <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
        <td class="l" colspan="5">Inserire un Nuovo Soggetto</td>
        <td class="c">          
           <input type="radio" title="Inserire un Nuovo Soggetto" checked name="radioins" onClick ="Javascript:nuovo()">
        </td>
    </tr>
    <tr>
      <%-- td class="int" width="<%=wCol1%>">Data Tit.Esecutivo</td>
      <td class="int" width="<%=wCol2%>">Anno/Numero</td>
      <td class="int" width="<%=wCol3%>">Tipo Tit.Esecutivo</td>
      <td class="int" width="<%=wCol4%>">Autorità</td>
      <td class="int" width="<%=wCol5%>">Dett.</td>
      <td class="int" width="<%=wCol6%>">Sel</td--%>
      <td class="int" width="<%=wCol1%>">Cognome e Nome</td>
      <td class="int" width="<%=wCol2%>">Data Nascita/Età Presunta</td>
      <td class="int" width="<%=wCol3%>">Luogo Nascita</td>
      <td class="int" width="<%=wCol4%>">Paternità</td>
      <td class="int" width="<%=wCol5%>">Maternità</td>
      <td class="int" width="<%=wCol6%>">Cod. CUI</td>
      <td class="int" width="<%=wCol7%>" Title="Dettaglio Soggetto">Dett.</td>
      <td class="int" width="<%=wCol8%>" Title="Selezione">Sel.</td>
    </tr>
    </table>
 
<%
    int jPA =0;
    Vector lFascicoli = new Vector();
    Iterator itx = soggettifascicoli.iterator();
    while ( itx.hasNext())
    {
    	SoggettoFascicoliModel soggettofascicoli = (SoggettoFascicoliModel)itx.next();
    	SoggettoModel soggetto = soggettofascicoli.getSoggetto();
			
    	//SentenzaFascicoliModel lSenFasMod= (SentenzaFascicoliModel)itx.next();
      //SentenzaModel sentenza = lSenFasMod.getSentenza();
%>
     	<table cellspacing=2 cellpadding=2 width="100%">
   			<tr>
   			<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
       		<td class=C width="<%=wCol1%>">
    				<%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%></font></td>
       		</td>
       		<td class=C width="<%=wCol2%>">
<%      		if ((soggetto.getDataNascita())==null || soggetto.getDataNascita().equals(""))
        		{
					if(soggetto.getEtaPresuntaAnni()!=null){
%>
						<%=StringUtils.toStringJSP(soggetto.getEtaPresuntaAnni())%>&nbsp;</font> anni
<%						
					} 
					
					if (soggetto.getEtaPresuntaMesi()!=null){
%>
						e <font class="campo"><%=StringUtils.toStringJSP(soggetto.getEtaPresuntaMesi())%>&nbsp;</font> mesi
<%						
					}
					
					if(soggetto.getEtaPresuntaAnni() != null || soggetto.getEtaPresuntaMesi() != null){
						if(soggettofascicoli.getFascicoli() != null &&  soggettofascicoli.getFascicoli().length>0)
	     				{%>
	         			<a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Fascicoli" ></a>
						<%}
					}	
	
				}
        		else {%>
          	<%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>
<%
       			if(soggettofascicoli.getFascicoli() != null &&  soggettofascicoli.getFascicoli().length>0)
     				{%>
         			<a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Ulteriori Fascicoli" ></a>
					<%}%>       
        <%}%>
       		</td>
       		<td class=C width="<%=wCol3%>">
      		<%if (soggetto.getDescrComuneNascita().compareTo("-")==0){
      				if(soggetto.getDescComuneNascitaEstero().length()>0) 
      				{%>
        				<%=soggetto.getDescComuneNascitaEstero()%>&nbsp;
        	    	<%if(soggetto.getDescrStatoNascita().length()>1) 
              		{%>
        		  		(<%=soggetto.getDescrStatoNascita().toUpperCase()%>)		
        				<%}%>
        	  <%}%>&nbsp; 
      		<%}else {%>
        		<%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)&nbsp;
      		<%}%>
       		</td>
       		<td class=C width="<%=wCol4%>">
						<%=soggetto.getPaternita()%>&nbsp;
       		</td>
       		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
       		<td class=C width="<%=wCol5%>">
						<%=soggetto.getCognomeMadre()%>&nbsp;<%=soggetto.getNomeMadre()%>&nbsp;
       		</td>
       		<td class=C width="<%=wCol6%>">
						<%=StringUtils.toStringJSP(soggetto.getCodAfis())%>&nbsp;
       		</td>
       		<td class=C width="<%=wCol7%>">
          	<jsp:include page="<%=ICostantiNuovaIstanza.PG_BUTTONS_SOGGETTO_NUOVAISTANZA%>">
           	<jsp:param name="CampoIdEntita" value="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>"/>
           	<jsp:param name="ValoreIdEntita" value="<%=soggetto.getIdSoggetto()%>"/>
        	</jsp:include>
     			</td>
     			<td class=C width="<%=wCol8%>">
         		<input type="radio" title="Selezione del Soggetto" name="radioins"  onClick ="Javascript:soggetto('<%=soggetto.getIdSoggetto() %>')">   
     			</td>
    		</tr>
<%
				//Caricamento Altri Esiti.
				if (soggettofascicoli!=  null && soggettofascicoli.getFascicoli() != null && soggettofascicoli.getFascicoli().length>0)
				{
 					for(int j=0;j<soggettofascicoli.getFascicoli().length;j++)
 					{
   					FascicoloSiepModel lFasMod = new FascicoloSiepModel(soggettofascicoli.getFascicoli()[j]);
   					lFascicoli.add(lFasMod);
 					}
%>
 					</table>
   				<div id="elenco<%=jPA%>" style="display:none; width:100%;">
     				<%@include file="/jsp/files/siap/siep/nuovaistanza/ListaFascicoliPerNuovaIstanza.jsp" %>
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