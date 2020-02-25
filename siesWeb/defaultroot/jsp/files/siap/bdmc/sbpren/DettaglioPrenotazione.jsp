<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@page import="siap.bdmc.sbperipren.model.SbPeriprenModel"%>
<%@page import="siap.sico.ufficio.controller.UfficioUtils"%>
<jsp:useBean id="provvedimento"  scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>


<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";
%>
<%  //Carica la descrizione del tipo istituto penale e della misura di custodia dalla tabella SB_VIEW_PROCPENA 
	Collection lColTipoIst = null;
	lColTipoIst=DecodificheManager.getInstance().getTipoIstituto();
	Vector lProcPena = provvedimento.getSbViewProcpena();
	SbViewProcpenaModel lModPena = (SbViewProcpenaModel)lProcPena.get(0);
	String descrTipoIst= lModPena.getDescriIstiPena();

    String descrMisuCust=null;
    if (lModPena.getCodiMisuCust() != null) {
		if(lModPena.getCodiMisuCust().equals("0001")){
			descrMisuCust=ICostantiSbPren.MISU_CUST_CUSTODIA_CAUTELARE_IN_CARCERE;
		}else if (lModPena.getCodiMisuCust().equals("0002")){
			descrMisuCust=ICostantiSbPren.MISU_CUST_CUSTODIA_CAUTELARE_IN_LUOGO_DI_CURA;
		}else if (lModPena.getCodiMisuCust().equals("0003")){
			descrMisuCust=ICostantiSbPren.MISU_CUST_ARRESTI_DOMICILIARI;
		}else if (lModPena.getCodiMisuCust().equals("0004")){
			descrMisuCust=ICostantiSbPren.MISU_CUST_OSPEDALE_PSICHIATRICO;
		}else if (lModPena.getCodiMisuCust().equals("0005")){
			descrMisuCust=ICostantiSbPren.MISU_CUST_CASA_CURA_E_CUSTODIA;
		}
    }
%>

<%@page import="siap.sico.soggetto.model.SoggettoModel"%>
<html>
<head>
    <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var node;
      function effettoTree(a)
      {
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=ISIAPCostantiWeb.IMAGES_DIR%>expand.gif" : "<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif";
        return false;
      }

   
  </script>
</head>

<%
if(provvedimento.getSbViewProcpena()==null)
{%>
<BODY class="corpo" onload="javascript:document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>.focus()">
<%}else{%>
  <BODY class="corpo">
<%}%>
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Provvedimento BDMC</font>
        </td>
          <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
      <%

      //Setto le check box da disabilitare
      String disableCheckSentenza = " checked=\"checked\" ";
      
      //String disableCheckResidenza = " checked=\"checked\" ";
      //String disableCheckNotiziaReato = " checked=\"checked\" ";
      String disableCheckCircostanza = " checked=\"checked\" ";
      //String disableCheckDifensori = " checked=\"checked\" ";


      if ((provvedimento.getSbViewProcpena()== null) )
    	  disableCheckSentenza = "disabled=\"disabled\"";

      //if ((provvedimento.getReati() == null) || (provvedimento.getReati() != null && provvedimento.getReati().size()==0))
      //    disableCheckReato = "disabled=\"disabled\"";
      //if ((provvedimento.getCircostanze() == null) || (provvedimento.getCircostanze() != null && provvedimento.getCircostanze().size()==0))
      //    disableCheckCircostanza = "disabled=\"disabled\"";
      //if ((provvedimento.getNotizieDiReato() == null) || (provvedimento.getNotizieDiReato() != null && provvedimento.getNotizieDiReato().size()==0))
      //    disableCheckNotiziaReato = "disabled=\"disabled\"";
      //if ((provvedimento.getDifensori() == null) || (provvedimento.getDifensori() != null && provvedimento.getDifensori().size()==0))
      //    disableCheckDifensori = "disabled=\"disabled\"";
      
      %>
      <td class="LBG">
          <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiSbPren.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=provvedimento.getSbPren().getIdPren()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>

<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr> 
	 <td class="LBGISI" width=<%=largh%> valign="middle">
       <a class="campoLow" title="Dettaglio Prenotazione" >
         PRENOTAZIONE
       </a>
     </td>
    
<td>
	<table width="100%">
      <tr> 
      <td class="l" >Data Prenotazione </td>
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getSbPren().getDataPren(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Numero Prenotazione</td>
      <td class="L">
        <font class="campo"><%=provvedimento.getSbPren().getIdPren() %></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Anno/Numero Fasc. BDMC</td>
      <td class="L">
        <font class="campo"><%=provvedimento.getSbPren().getAnnoFascBdmc() %></font>&nbsp;
        /<font class="campo"><%=StringUtils.toStringJSP(provvedimento.getSbPren().getNumeFascBdmc())%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Autorità</td>
      <td class="L">
        <font class="campo"><%=UfficioUtils.getDescTipoUffByCodUfficio(provvedimento.getSbPren().getCodiSedeInst())%></font>&nbsp;
      </td>
      </tr>
      <tr> 
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(provvedimento.getSbPren().getNote())%></font>&nbsp;
      </td>
     </tr>
    </table>
</td></tr>
</table>
<br>
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  
 	<td class="LBGISI" width=<%=largh%> valign="middle">
  	   <a class="campoLow" title="Dettaglio Soggetto" >
         SOGGETTO
       </a>
    </td>
<td>
	<table width="50%">
	<tr> <td class="L" colspan=4 width="50%">
	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <font class="campo">
          <%=provvedimento.getSbPren().getCognSogg()%>&nbsp;<%=provvedimento.getSbPren().getNomeSogg()%>
      </font>
<%    if (provvedimento.getSbPren().getFlagSess().compareTo("F")==0)
       {%>  <font class="label">&nbsp; nata il </font><%}else{
%>          <font class="label">&nbsp; nato il </font><%}%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getSbPren().getDataNasc(),"dd-MM-yyyy"),"-")%></font>
       <font class="label">&nbsp; in  </font>
<%   if (provvedimento.getSbPren().getLuogNasc().compareTo("-")==0)
       {%> <font class="campo"><%=provvedimento.getSbPren().getCodiStat() %> </font>
<%     }else{%><font class="campo"><%=provvedimento.getSbPren().getLuogNasc() %></font>
<%     }%>
    </td></tr>
    </table>
</td></tr>
</table>

<!--------------SENTENZA--------------->
<jsp:include page="/jsp/files/siap/bdmc/sbpren/DettaglioSentenzaPrenotazione.jsp"/>


<!--------------CAPI IMPUTAZIONI E REATI--------------->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> valign="middle"   >
       <a class="campoLow" title="Elenco Reati"> 
         Reati      
       </a>
 
     <td class="label" width=<%=resto%> colspan=1>
       <a><img name="image2" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(2);" alt="" border=0></a>
  &nbsp;</td></tr>
 </table>
 <div id="elenco2" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>  <td width="7%"></td>
  <td>
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoCapoImpuPrenotazione.jsp"/> 
  </td></tr>
 </table>
 <br>
 </div>

<!------------LUOGO DETENZIONE ------->
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  
 
 	<td class="LBGISI" width=<%=largh%> valign="middle">
  	   <a class="campoLow" title="Custodia" >
         LUOGO DETENZIONE
       </a>
    </td>

    <td>
	
	<%
	if (descrMisuCust != null &&  descrTipoIst!= null)
	{ 
	%>
	 <table width="100%" >
	  <tr>
	    <td class="l" width="23%" >Misura di Custodia </td>
	    <td class="L" ><font class="campo"><%=descrMisuCust%> </font>&nbsp;</td>
	  </tr>
	  <tr>
	    <td class="l" width="23%" >Istituto Penale </td>
	    <td class="L" ><font class="campo"><%=descrTipoIst%> </font>&nbsp;</td>
	  </tr>
	

	</table>
	<%} else { %>
	<table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessun Luogo di Detenzione prenotato</font>
      			</td>
    		</tr>
    		</table>
 <%}%>	
	</td>
</tr>
</table>
<br>


<!--------------CIRCOSTANZE--------------->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> colspan=2 >
       <a class="campoLow" title="Elenco Circostanze"> 
         Circostanze&nbsp;
       </a>
 
   

 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoCircostanzePrenotazione.jsp"/> 
 
 

<!--------------PERIODI PRESOFFERTO COMPUTABILI ----->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%>  valign="middle" >
       <a class="campoLow" title="Periodi di presofferto estratti da Banca Dati Misure Cautelari - Computabili"> 
         Periodi  Computabili
       </a>
 
     <td class="label" width=<%=resto%> colspan=1>
       <a><img name="image4" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(4);" alt="" border=0></a>
  &nbsp;</td></tr>
 </table>
 <div id="elenco4" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>  <td width=<%=largh%> align="center"> </td>
  <td>
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiCompuPren.jsp"/> 
  </td></tr>
 </table>
 <br>
 </div>

<!--------------PERIODI PRESOFFERTO NON COMPUTABILI ----->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%>  valign="middle" >
       <a class="campoLow" title="Periodi di presofferto estratti da Banca Dati Misure Cautelari - Non Computabili"> 
         Periodi  Non Computabili
       </a>
 
     <td class="label" width=<%=resto%> colspan=1>
       <a><img name="image5" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(5);" alt="" border=0></a>
  &nbsp;</td></tr>
 </table>
 <div id="elenco5" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>  <td width=<%=largh%> align="center"> </td>
  <td>
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiNonCompuPren.jsp"/> 
  </td></tr>
 </table>
 <br>
 </div>



</body>
</html>