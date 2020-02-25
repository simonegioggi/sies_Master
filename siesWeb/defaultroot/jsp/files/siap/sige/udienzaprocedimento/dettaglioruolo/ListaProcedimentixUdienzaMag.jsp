<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel" %>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<jsp:useBean id="procedimenti" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="data_udi" 						scope="request" class="java.util.Date"/>

<jsp:useBean id="ruoloMagistrato" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProcuratore" 		scope="request" class="java.lang.String"/>

<jsp:useBean id="udienza" 						scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="tipo" 								scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProc" 						scope="request" class="java.lang.String"/>
<jsp:useBean id="lStatoProcedimento" 	scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteConnesso" 	scope="session" class="siap.sico.utente.model.UtenteModel"/>
<!-- intervento 11.2.1 -->
<jsp:useBean id="listaIdUdienze" 						scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
 <head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title>[S.I.E.S.] - ListaProcedimentixUdienza Magistrati</title>
  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript">
      var node;
      function effettoTree(a){
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        return false;
      }
  </script>
 </head>

  <body class="corpo">

  <table>
    <tr>

    	<td class="LBG">
    		<a href="Javascript:window.print();">
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    		</a>
    	</td>

      <td class="LBG">
      	<font class="label">Funzione :</font>
      	<font class="campo"> Elenco Procedimenti Fissati all'Udienza del <%=DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy")%> </font>
      </td>
      <td class="LBG" nowrap>
       <jsp:include page="<%=ICostantiUdienzaProcedimentoSige.PG_COMBOTEMPLATE_RUOLOUDIENZA%>">
          <jsp:param name="CampoIdEntitaPP" value="tipo" />
          <jsp:param name="ValoreIdEntitaPP" value="<%="M"+tipo%>" />
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_DATA_UDIENZA%>" />          
          <jsp:param name="ValoreIdEntita" value="<%=DateUtils.getDateToString(udienza.getDataUdienza(),"yyyyMMdd")%>" />              
          <jsp:param name="CampoIdEntitaProvv" value="tipoProc" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=tipoProc%>" />
          <jsp:param name="ChiaveQuattro" value="lStatoProcedimento" />
          <jsp:param name="ValoreQuattro" value="<%=lStatoProcedimento%>" />
          <jsp:param name="ChiaveMagis" value="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" />
          <jsp:param name="ValoreMagis" value="<%=udienza.getCodMagistratoAss()%>" /> 
       </jsp:include>
      </td> 	
    	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>

	<jsp:include page="<%=ICostantiUdienzaProcedimentoSige.PG_SINTESIDETTAGLIOUDIENZA%>"/>
  <br>

<%
  // Elenco dei procedimenti dello stesso magistrato relatore
  Vector procMagistrato = new Vector();
  Iterator itxMag = procedimenti.iterator();
  int j = 0;
  int rj = 0;
  int jMag = 0;
  int jTot = 0;
  int rTot = 0;
  
  String lCodMagistrato = null;
  String lCodMagCorrente = null;
  
  BigDecimal lIdEsperto = null;
  BigDecimal lIdEspCorrente = null;

%>
  <table width=90%>
    <tr>
      <td class="int" width=15%>Procedimenti</td>
      <td class="int" width=50%>MAGISTRATO</td>
      <td class="int" width=13%>Totale procedimenti</td>
      <td class="int" width=12%>Di cui gia Rinviati</td>
      <td class="int" width=10%>Azioni</td>
    </tr>
<%

while(itxMag.hasNext()) {
  	ProcedimentixUdienzaModel procedimento = (ProcedimentixUdienzaModel)itxMag.next();
   	
  	// 2011-01-24
  	//lCodMagCorrente = procedimento.getCognomeMagistrato();
  	
  	lCodMagCorrente = procedimento.getCodMagistrato();
  	
  	//lCodMagCorrente = 
   	//  udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCodMagistrato();
   
   	// imposta nel model il nome e cognome del magistrato, 
   	// per riuso parte codice... 
   	//procedimento.setCognomeMagistrato(
   	//    udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome());
   	//procedimento.setNomeMagistrato(
   	//    udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome());
   	//procedimento.setCodMagistrato(
   	//    udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCodMagistrato());
   	
   	if( procedimento.getFlagRinviata().equals("S") ) 
     	rj++;rTot++;   
   	
		if( lCodMagCorrente != null && lCodMagCorrente.trim().length() < 1 )
  		lCodMagCorrente = null;
   	
		lIdEspCorrente = procedimento.getIdEsperto();
   	// Solo procedimenti con Magistrato Relatore (esperto)
  	if (lCodMagCorrente != null || lIdEspCorrente != null) {
    	// Il Magistrato Relatore (o l' Esperto) non è lo stesso del precedente  o il precedente non esisteva (prima riga)
    	if( !( ( lCodMagCorrente != null && lCodMagistrato != null && 
        		 	 lCodMagCorrente.compareTo(lCodMagistrato) ==0 ) || 
        	 	 ( lIdEspCorrente != null && lIdEsperto !=  null && lIdEsperto.compareTo(lIdEspCorrente) == 0 )))
    	{
      	// Codice magistrato (esperto)  precedente valorizzato quindi chiusura riga precedente
      	if(lCodMagistrato != null || lIdEsperto != null ){
%>
         <td class=l width=13%><%=j%></td>
         <td class=l width=12%><%=rj%></td>
         <td class=l width=10% colspan=1>

         <a href="Javascript:stampaT('siap.sige.udienzaprocedimento.dettaglioruolo.action.ActStampaUdienzaProcedimento&tipo=<%=tipo%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=udienza.getIdUdienzaSige()%><%if(lCodMagistrato != null ){%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>=<%=lCodMagistrato%><%}%><%if(lIdEsperto != null ){%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_ESPERTO%>=<%=lIdEsperto%><%}%>');">
           <img src="/images/print.gif" alt="Stampa procedimenti del Magistrato" width="12" height="12" border="0">
         </a>
				</td>			
        </table>
        <div id="elenco<%=jMag%>" style="display:none; width:100%;">        
        	<%@ include file="/jsp/files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienzaMagChild.jsp"%>
        </div>
<%      procMagistrato.clear(); %>      
				<table width=90%>
<%
        rj=0;
      }
      jTot += j;
      j=1; // inizializzazione
      jMag++;
      lCodMagistrato = lCodMagCorrente;
      // Inizializzazione dell'ID Esperto
      lIdEsperto = lIdEspCorrente;

      // L'attuale procedimento viene aggiunto al Vector procMagistrato
      procMagistrato.add(procedimento) ;
%>
      <tr>
       <td class=l width=15% colspan=1>
        	<a href="#1" onClick="return effettoTree(<%=jMag%>)">
        		<img name="image<%=jMag%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0">
        	</a>
       	<td class=l width=50%>
       	<% 
       	if( procedimento.getCodMagistrato() != null ){ 
       	%>
       		<%=StringUtils.toStringJSP(procedimento.getCognomeMagistrato(),"-") + " " + StringUtils.toStringJSP(procedimento.getNomeMagistrato(),"-")%>
       	<%}else{%>
       		<%=StringUtils.toStringJSP("Nessun Magistrato Assegnatario")%>
       	<%}%>
       	</td>
<%
    }else{
      // Il Magistrato Relatore (o l' Esperto) è lo stesso del precedente
      procMagistrato.add(procedimento);
      j++;
    }
   } // endif
  } // endwhile

  // Ultima riga fuori ciclo
  if(lCodMagistrato != null || lIdEsperto != null )
%>
			<td class=l width=13%><%=j%></td>
    	<td class=l width=12%><%=rj%></td>
    	<td class=l width=10% colspan=1>
    		<a href="Javascript:stampaT('siap.sige.udienzaprocedimento.dettaglioruolo.action.ActStampaUdienzaProcedimento&tipo=<%=tipo%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_UDI_ID_UDIENZA_SIGE%>=<%=udienza.getIdUdienzaSige()%><%if(lCodMagistrato != null ){%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>=<%=lCodMagistrato%><%}%><%if(lIdEsperto != null ){%>&<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_ESPERTO%>=<%=lIdEsperto%><%}%>');">
    			<img src="/images/print.gif" alt="Stampa procedimenti del Magistrato" width="12" height="12" border="0">
    		</a>
			</td>

   	</tr>
    </table>
      <div id="elenco<%=jMag%>" style="display:none; width:100%;">
       <%@ include file="/jsp/files/siap/sige/udienzaprocedimento/dettaglioruolo/ListaProcedimentixUdienzaMagChild.jsp" %>
      </div>
<%    jTot += j; %>    
			<table width=90%>
        <td class=l width=15% colspan=1></td>
        <td class=l align=center width=50%>Totale</td>
        <td class=l width=13%><%=jTot%></td>
        <td class=l width=12%><%=rTot%></td>
        <td class=l width=10%></td>
      </table>

    </table>
    </div>
  </form>
  <br>
  </body>
</html>