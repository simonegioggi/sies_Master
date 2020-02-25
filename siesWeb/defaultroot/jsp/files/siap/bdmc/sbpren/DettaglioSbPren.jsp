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

      function ListaComuni(a_formname,a_fieldname){
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  	
  	function ControllaCheckBox(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox) { 		
  		if (!(cBox.checked)  ) {
  	      ddDa.value="";  
  	      mmDa.value="";  
  	      aaDa.value="";  
  	      ddA.value=""; 
  	      mmA.value=""; 
  	      aaA.value="";  
  	      document.DettaglioProvvedimentoBDMC.appoCheck.value =999999999;
        }
        else {
          	   ddDa.focus(); 
        	}
  	}
  	
  	function ControllaGiornoDa(ddDa,cBox,mmDa) {
  	    
  		if ((!cBox.checked) && (ddDa.value > 0) && (document.DettaglioProvvedimentoBDMC.appoCheck.value !=cBox.value)) {
  	        alert("Cliccare sulla checkBox del periodo in oggetto prima di inserire la Data Da");
            ddDa.focus();
            return false;
        }
        
  	}
  	
  	function DisattivaVincoli(cBox){
  			
  				document.DettaglioProvvedimentoBDMC.appoCheck.value=cBox.value;
  	}
  
  	function AttivaVincoli(){
  		
  				document.DettaglioProvvedimentoBDMC.appoCheck.value=999999999;
  	}
  	
  	
 
     function ControllaPeriodo(ddDa,mmDa,aaDa,ddA,mmA,aaA,cBox,dtIni,dtFine)
      {
    	
		var dA=ddA.value+'/'+mmA.value+'/'+aaA.value;
		  var dDa=ddDa.value+'/'+mmDa.value+'/'+aaDa.value; 
		 if (cBox.checked) {
		   if (! ControllaData(dDa))
		      {
        		alert('Data inizio periodo prenotato non valida');
        		
        		ddDa.focus();
		        return 1;
		      }
		  if ( (!CompareDate(dtIni.value,dDa) ) || (!CompareDate(dDa,dtFine.value)) )
		  {
        		alert('Data inizio periodo fuori intervallo temporale consentito ');
        	
        		ddDa.focus();
		        return 1;
		      }
		      
		    if (! ControllaData(dA))
		      {
        		alert('Data fine periodo prenotato non valida');
        		
        		ddA.focus();
		        return 1;
		      }
		//  if ( (CompareDate(dA,dtIni.value) ) || (!CompareDate(dA,dtFine.value)) )
     	  if ( !CompareDate(dA,dtFine.value))
	
		  {
        		alert('Data Fine periodo fuori intervallo temporale consentito ');
        		
        		ddA.focus();
		        return 1;
		      }
		  if (! CompareDate(dDa,dA) )
		  {
        		alert('Data inizio periodo prenotato maggiore di Data Fine Periodo');
        		ddDa.focus();
		        return 1;
		      }
		    }
		  return 0;
  	}
  	
  	function Verify()
      {
    
	   if (document.DettaglioProvvedimentoBDMC.appoDataArrivoAtto.value > 0)
	   {
	      var d1=document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
	    
	      if (! ControllaData(d1))
	      {
	        alert('Data di arrivo atto non valida');
	        return false;
	      }
        }
        else
        {
        alert("Impossibile creare un nuovo fascicolo senza le informazioni relative alla Sentenza");
        return false;
        }
             //Data Sentenza
     if (isNaN(parseInt(document.DettaglioProvvedimentoBDMC.CheckPeriodi.length)))
	 {
		if (!document.DettaglioProvvedimentoBDMC.CheckPeriodi.checked) {
            alert("Selezionare almeno un periodo prenotato da importare!");
            return false;
        }   
     }
	 else {
	
	 var check=false;
	
	  for (lSel=0;lSel<document.DettaglioProvvedimentoBDMC.CheckPeriodi.length;lSel++) {
          
          if (document.DettaglioProvvedimentoBDMC.CheckPeriodi[lSel].checked) {
           
             check=true;
          }
        }
        if (!check) {
          alert("Selezionare almeno un periodo prenotato da importare!");
          return false;
        }
        }
    <% Vector lPeriodi = provvedimento.getSbPeriPren();
    if(lPeriodi != null && lPeriodi.size() != 0)
	{
		Iterator lIterPeriodi = lPeriodi.iterator();
		int lsel=0;
		if (lPeriodi.size() == 1) {
			
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else { %>
	        
	        var appo = ControllaPeriodo(document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_CHECK_PERIODI %>,document.DettaglioProvvedimentoBDMC.dtIni<%=lsel %>,document.DettaglioProvvedimentoBDMC.dtFine<%=lsel %>);
	       
	        	if (appo > 0)	{

	       		
	       		return false;
	       		}
	       		
	      
	        	
	        <%  }
			}
		while(lIterPeriodi.hasNext())
		{
			
			SbPeriprenModel lPeriodo= (SbPeriprenModel)lIterPeriodi.next();
	        if (!lPeriodo.getCodStatPrenPeri().equals("0")) { }
	        else {
	        	
		           String campoCheckBox = ICostantiSbPren.CAMPO_CHECK_PERIODI+"["+lsel+"]";  
	               String campoGiornoDa = ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel;
	            %>
	            
	          if (document.DettaglioProvvedimentoBDMC.CheckPeriodi[0] != null)  {
	         	 var appo = ControllaPeriodo(document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%=campoCheckBox %>,document.DettaglioProvvedimentoBDMC.dtIni<%=lsel %>,document.DettaglioProvvedimentoBDMC.dtFine<%=lsel %>);
		       		if (appo > 0)	{
	
		       		
		       		return false;
		       		}
	       		
	       		} else {
	       			var appo = ControllaPeriodo(document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_DALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_MESE_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%= ICostantiSbPren.CAMPO_ANNO_ALLA_DATA  + lsel %>,document.DettaglioProvvedimentoBDMC.<%=ICostantiSbPren.CAMPO_CHECK_PERIODI %>,document.DettaglioProvvedimentoBDMC.dtIni<%=lsel %>,document.DettaglioProvvedimentoBDMC.dtFine<%=lsel %>);
	       
		        	if (appo > 0)	{
	
		       		
		       		return false;
		       		}
	       		}
	       	  
	       <% lsel++;}  }}%>
     return true;
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
      String disableCheckReato = " checked=\"checked\" ";
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

<form method="POST" action="<%=ISIAPCostantiWeb.PG_MAIN%>" name="DettaglioProvvedimentoBDMC">
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


<%if (provvedimento.getSoggettiOmonimi()!=null && provvedimento.getSoggettiOmonimi().size()>0)
{//-----------------TROVATI SOGGETTI OMONIMI ----------------------------
  %>
<table cellspacing=1 cellpadding=1  width=100% border=<%=isBorder%>>
<tr>
    <td width=<%=largh%> valign="middle">&nbsp;</td>
<td>

    <table cellspacing=1 cellpadding=1 border=<%=isBorder%> width="50%">
    <tr>
    <td class="label" width="8%">
       <a><img name="image7" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(7);" alt="" border=0></a>
  &nbsp;</td>
    <td class="LBGISIV"><font class="cRosso">Trovati Soggetti Omonimi</font></td>
    </tr>
    </table>
    
    <div id="elenco7" style="display:block; width:100%;" >
    <table cellspacing=1 cellpadding=1 border=<%=isBorder%> width="50%">
    <tr>
    <td class="c" width="8%"> <input type="radio" name="<%=ICostantiSbPren.CAMPO_SOGGETTO_OMONIMO%>" value="NUOVO" checked="checked"/> </td>
    <td class="l" colspan="2"> Inserisci un nuovo soggetto con i dati BDMC</td><td></td>
    </tr>
 <%
  Iterator lItOmonimi = provvedimento.getSoggettiOmonimi().iterator();
  while (lItOmonimi.hasNext())
  {
	  SoggettoModel lSoggSiep = (SoggettoModel)lItOmonimi.next();
  %>
    <tr>
    <td class="c"> <input type="radio" name="<%=ICostantiSbPren.CAMPO_SOGGETTO_OMONIMO%>" value="<%=lSoggSiep.getIdSoggetto()%>"/></td>
    <td class="l"> <font class="campo"><%=lSoggSiep.getCognome()%>&nbsp;<%=lSoggSiep.getNome()%></font>&nbsp;
 <%if(lSoggSiep.getSesso().equals("F")){%> <font class="label">nata il&nbsp; <%}else{%> <font class="label">nato il&nbsp; <%}%>
 </font> <font class="campo"><%=DateUtils.getDateToString(lSoggSiep.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
 </font> <font class="label">in</font> <font class="campo">
 <%=lSoggSiep.getDescrComuneNascita()%>&nbsp;</font>
    </td>
    <td class="c">
       <a href="<%=ISIAPCostantiWeb.PG_MAIN%>?<%=ISIAPCostantiWeb.ACTION_FIELD%>=siap.bdmc.sbpren.action.ActDettaglioSoggettoOmonimoSbPren&IdSoggetto=<%=lSoggSiep.getIdSoggetto()%>">
           <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
       </a>
    </td>
    </tr>
<%}
%>
	</table> 
	</div>
</td></tr></table>
<%
}else{//non ci sono omonimi%>
 <input type="HIDDEN" name="<%=ICostantiSbPren.CAMPO_SOGGETTO_OMONIMO%>" value="NUOVO"/>
<%}%>

<br>


<!--------------SENTENZA--------------->
<jsp:include page="/jsp/files/siap/bdmc/sbpren/DettaglioSentenzaBdmcInclude.jsp"/>

<!--------------PENA COMPLESSIVA--------------->

<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  
 
 	<td class="LBGISI" width=<%=largh%> valign="middle">
  	   <a class="campoLow" title="Custodia" >
        PENA
       </a>
    </td>

    <td>

	<%  
	   SbViewProcpenaModel  penaBDMC=(SbViewProcpenaModel)lProcPena.get(0);
	   if (penaBDMC != null) {
	   String giorniReclusione = new String("0");
	   String mesiReclusione = new String("0");
	   String anniReclusione = new String("0");
	   String giorniArresto= new String("0");
	   String mesiArresto = new String("0");
	   String anniArresto = new String("0");
	   boolean flagPenaPresente = false;
	   if (penaBDMC.getFlagReclArreAppe() != null && penaBDMC.getFlagReclArreAppe().compareTo("R") == 0 ){
    	  if (penaBDMC.getMesiPenaAppe() != null && penaBDMC.getGiorPenaAppe() != null && penaBDMC.getAnniPenaAppe() != null) {
	    	  flagPenaPresente = true;
	    	  mesiReclusione = penaBDMC.getMesiPenaAppe().toString();
    		  giorniReclusione = penaBDMC.getGiorPenaAppe().toString();
    		  anniReclusione = penaBDMC.getAnniPenaAppe().toString();
    	  }
    	  }
      if (penaBDMC.getFlagReclArreAppe() != null && penaBDMC.getFlagReclArreAppe().compareTo("A") == 0 ) {
    	  if (penaBDMC.getMesiPenaAppe() != null && penaBDMC.getGiorPenaAppe() != null && penaBDMC.getAnniPenaAppe() != null) {
	    	  flagPenaPresente = true;
	    	  anniArresto = penaBDMC.getAnniPenaAppe().toString();
	    	  mesiArresto = penaBDMC.getMesiPenaAppe().toString();
	    	  giorniArresto = penaBDMC.getGiorPenaAppe().toString();
    	  }	  
      }  
      
//    ======================================================================
      //    Controllo se è presente la pena da dibattimento
      //    Controllo se è Arresto o reclusione
      //    Carico i campi della tabella pena_complessiva
      //    ======================================================================
      if (penaBDMC.getFlagReclArreDiba() != null && penaBDMC.getFlagReclArreDiba().compareTo("R") == 0 ){
    	  if (penaBDMC.getMesiPenaDiba() != null && penaBDMC.getGiorPenaDiba() != null && penaBDMC.getAnniPenaDiba() != null) {
	    	  flagPenaPresente = true;
	    	  mesiReclusione = penaBDMC.getMesiPenaDiba().toString();
	    	  giorniReclusione = penaBDMC.getGiorPenaDiba().toString();
	    	  anniReclusione = penaBDMC.getAnniPenaDiba().toString();
    	  }
    	  }
      if (penaBDMC.getFlagReclArreDiba() != null && penaBDMC.getFlagReclArreDiba().compareTo("A") == 0 ) {
    	  if (penaBDMC.getMesiPenaDiba() != null && penaBDMC.getGiorPenaDiba() != null && penaBDMC.getAnniPenaDiba() != null) {
	    	  flagPenaPresente = true;
	    	  anniArresto = penaBDMC.getAnniPenaDiba().toString();
	    	  mesiArresto = penaBDMC.getMesiPenaDiba().toString();
	    	  giorniArresto = penaBDMC.getGiorPenaDiba().toString();
    	  }	  
      }  
      
      //    ======================================================================
      //    Controllo se è presente la pena da Gigu
      //    Controllo se è Arresto o reclusione
      //    Carico i campi della tabella pena_complessiva
      //    ======================================================================
      if (penaBDMC.getFlagReclArreGigu() != null && penaBDMC.getFlagReclArreGigu().compareTo("R") == 0 ){
    	  if (penaBDMC.getMesiPenaGigu() != null && penaBDMC.getGiorPenaGigu() != null && penaBDMC.getAnniPenaGigu() != null) {
	    	  flagPenaPresente = true;
	    	  mesiReclusione = penaBDMC.getMesiPenaGigu().toString();
	    	  giorniReclusione = penaBDMC.getGiorPenaGigu().toString();
	    	  anniReclusione = penaBDMC.getAnniPenaGigu().toString();
    	  }
    	  }
      if (penaBDMC.getFlagReclArreGigu() != null && penaBDMC.getFlagReclArreGigu().compareTo("A") == 0 ) {
    	  if (penaBDMC.getMesiPenaGigu() != null && penaBDMC.getGiorPenaGigu() != null && penaBDMC.getAnniPenaGigu() != null) {
    		  flagPenaPresente = true;
    		  anniArresto = penaBDMC.getAnniPenaGigu().toString();
    		  mesiArresto = penaBDMC.getMesiPenaGigu().toString();
    		  giorniArresto = penaBDMC.getGiorPenaGigu().toString();
    	  }	  
      }  
      
      //    ======================================================================
   		//   Se è stata caricata una pena complessiva durante la prenotazine su Bdmc
      //    la inserisco nel Db di SIES
      //    ======================================================================
      if (flagPenaPresente) {
    	  
    	 
       %>
	
	 <table width="100%" >
	  <tr>
	    <td class="l" width="23%" >Anni Reclusione: </td>
	    <td class="L" ><font class="campo"><%=anniReclusione%> </font>&nbsp;</td>
	    <td class="l" width="23%" >Mesi Reclusione: </td>
	    <td class="L" ><font class="campo"><%=mesiReclusione%> </font>&nbsp;</td>
	    <td class="l" width="23%" >Giorni Reclusione: </td>
	    <td class="L" ><font class="campo"><%=giorniReclusione%> </font>&nbsp;</td>
	  </tr>
	  <tr>
      <td class="l" width="23%" >Anni Arresto: </td>
	    <td class="L" ><font class="campo"><%=anniArresto%> </font>&nbsp;</td>
	    <td class="l" width="23%" >Mesi Arresto: </td>
	    <td class="L" ><font class="campo"><%=mesiArresto%> </font>&nbsp;</td>
	    <td class="l" width="23%" >Giorni Arresto: </td>
	    <td class="L" ><font class="campo"><%=giorniArresto%> </font>&nbsp;</td>
	  </tr>
	

	</table>
	<%} else { %>
	<table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessuna Pena prenotata</font>
      			</td>
    		</tr>
    		</table>
 <%}
      } else { %>	
 
 <table cellspacing=1 cellpadding=1 width="50%" >
       		<tr>
        		<td class="l">
       			<font class="cGrigio">Nessuna Pena prenotata</font>
      			</td>
    		</tr>
    		</table>
   <%} %>
	</td>
</tr>
</table>
<br>


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
  <tr>  <td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_REATO%>" value="<%=ICostantiSbPren.CAMPO_CHECK_REATO%>" <%=disableCheckReato%>/> </td>
  <td>
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoCapoImpuInclude.jsp"/> 
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
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoCircostanzeInclude.jsp"/> 
</td></tr></table>

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
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiComputabili.jsp"/> 
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
 		<jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiNonComputabili.jsp"/> 
  </td></tr>
 </table>
 <br>
 </div>

<!-- <jsp:include page="/jsp/files/siap/bdmc/sbpren/ElencoPeriodiPresoffertoInclude.jsp"/> -->
	<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- Gestione fascicolo in sessione non gestito per questa funzione
   < %
  FascicoloSiepModel fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
	//Fascicolo SIEP in Sessione
  if(fascicolo!=null && fascicolo.getIdFascicoloSiep()!=null && fascicolo.getFlagValidato()!=null && fascicolo.getFlagValidato().equalsIgnoreCase("N"))
  {%>
  <tr>
     <td class="c"> <input type="radio" name="importa" value="NUOVO" checked="checked"/> </td>
   <td class="l">Importazione in nuovo fascicolo</td>
  </tr>
  <tr>
   <td class="c"> <input type="radio" name="importa" value="VECCHIO"/> </td>
   <td class="l">Importazione in fascicolo esistente (<font class="campo">< %=fascicolo.getChiaveAnno()%></font>/
   <font class="campo">< %=fascicolo.getChiaveProgr() %></font> emesso da
   <font class="campo">< %=fascicolo.getDescrTipoUfficio()%></font> di
   <font class="campo">< %=fascicolo.getDescrComuneUfficio()%>)</font></td>
  </tr>
  < %}%>
--%>
  <tr>
   <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Conferma Importazione Dati BDMC in SIEP" >
        <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbpren.action.ActConfermaImportaDatiBDMC">
        <input type="HIDDEN" name="<%=ICostantiSbPren.CAMPO_ID_FILE%>" value="<%=provvedimento.getSbPren().getIdPren()%>">
      </td>
  </tr></table>

</form>
  
<script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("DettaglioProvvedimentoBDMC");
    
     if (document.DettaglioProvvedimentoBDMC.appoDataArrivoAtto.value > 0)
	 {
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_MESE_DATA_ARRIVO_ATTO%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","req");
		 
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_MESE_DATA_ARRIVO_ATTO%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");
	
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","gt=1900");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","lt=3000");
	
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_ARRIVO_ATTO%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_ARRIVO_ATTO%>","lt=12");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","lt=31");
	
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_MESE_DATA_IRR%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","req");
		 
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_MESE_DATA_IRR%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","numeric");
	
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","numeric");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","gt=1900");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>","lt=3000");
	
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_IRR%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_IRR%>","lt=12");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>","lt=31");
	     }
	     
	     
	     if (document.DettaglioProvvedimentoBDMC.appoDataSentenza.value > 0)
	 {
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","req");
		 
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA%>","numeric");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","numeric");
	
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","numeric");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","gt=1900");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","lt=3000");
	
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA%>","lt=12");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","gt=1");
	      frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","lt=31");
	      
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA%>","req");
	      frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","req");
		  
		frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","req");	
		frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA%>","numeric");
	  	frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","minlen=4","La lunghezza minima per l'anno della sentenza  è di 4 caratteri");
	    frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","numeric");
	    frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","gt=1900");
	    frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA%>","lt=3000");
	  
	      }
	   if (document.DettaglioProvvedimentoBDMC.appoAnnoSentenza.value > 0){
		frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA %>","req");	
			frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA%>","numeric");
		  	frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA%>","minlen=4","La lunghezza minima per l'anno sentenza è di 4 caratteri");
		    frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA%>","gt=1900");
		    frmvalidator.addValidation("<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA%>","lt=3000");
		
	   }
	   if (document.DettaglioProvvedimentoBDMC.appoNumeroSentenza.value > 0){
		    frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA %>","req");	
			frmvalidator.addValidation("<%=ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA%>","numeric");
		  
	   }
	   if (document.DettaglioProvvedimentoBDMC.appoAutoEmi.value > 0){
		    frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_LUOGO_AUTORITA %>","req");	
			frmvalidator.addValidation("<%=ICostantiSbPren.CAMPO_AUTORITA%>","alphabetic");
	   }
	frmvalidator.setAddnlValidationFunction("Verify");

     </script>

</body>
</html>