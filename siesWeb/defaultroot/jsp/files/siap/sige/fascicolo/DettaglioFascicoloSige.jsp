<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.note.model.NoteModel" %>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel" %>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.sige.richiestaatti.model.ParereModel"%>

<jsp:useBean id="FascicoloSigeEsteso" 			scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="TornaQui"            			scope="request" class="java.lang.String" />
<jsp:useBean id="Modificabile"        			scope="request" class="java.lang.String" />
<jsp:useBean id="Cancellabile"        			scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso"      			scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="elencoFasUnificati"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="fascicoloUnificante" 			scope="request" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="elencoNote"          			scope="request" class="java.util.Vector"/>
<jsp:useBean id="sentenze"            			scope="request" class="java.util.Vector"/>
<jsp:useBean id="provvedimentiAltri"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="pareri"                		scope="request" class="java.util.Vector"/>
<jsp:useBean id="posizione_materiale" 			scope="request" class="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel"/>
<%-- MEV_57: aggiunto useBean --%>
<jsp:useBean id="etichettaEta" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaProcedimentoSiepDiCumulo" scope="request" class="java.util.Vector" />
<jsp:useBean id="lCumuloSiep" 					scope="request" class="java.util.Vector" />
<jsp:useBean id="ultima_impugnazione"      		scope="request" class="siap.sige.impugnazione.model.ImpugnazioneSigeModel"/>
<jsp:useBean id="fascicoloCollegato" 			scope="request" class="siap.sige.fascicolo.model.FascicoloSigeModel"/>

<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

// Fascicolo SIGE
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
// Richiesta SIGE
RichiestaSigeModel lRichiesta = FascicoloSigeEsteso.getRichiestaSige();
MagistratoAssegnatarioModel lMagistrato = FascicoloSigeEsteso.getMagAssegnatario();

// Link alla Gestione Oggetti 
RedirectTo lRedir = new RedirectTo();
lRedir.setPage(IWebConstants.PG_MAIN);
lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggettiRichiesta");
lRedir.setParameter(ICostantiTenoreSige.CAMPO_RIC_SIG_ID_RICHIESTA_SIGE, lRichiesta.getIdRichiestaSige().toString() );
lRedir.setParameter("TornaQui", TornaQui );

String lLinkOggetti = lRedir.toString();

String attiInArchivio=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getDataInvioAttiInArchivio() ,"dd-MM-yyyy"), "-");
String foglioComplementare=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getDataCompilazioneFoglioComplementare(),"dd-MM-yyyy"), "-");

String cognomeSoggetto =FascicoloSigeEsteso.getSoggetto().getCognome();
String nomeSoggetto =FascicoloSigeEsteso.getSoggetto().getNome();
String codCui=FascicoloSigeEsteso.getSoggetto().getCodAfis();
%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio Fascicolo Sige </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
      var node;
	function effettoTree(a) {
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        return false;
      }
      
	function caricaIscrizioneSentDecr() {
    	  // quando il soggetto non è noto (IGNOTO/IGNOTO)
    	  // nella combo delle funzioni viene preimpostata la 
    	  // funzione Inserimento Sentenza/Decreto
    	  if(document.comandi.cognomeSoggetto.value == 'IGNOTO' && document.comandi.nomeSoggetto.value == 'IGNOTO'
    		 && document.comandi.dataNascitaSoggetto.value == 'null'){
    		  var optionTag = document.getElementById("funzioni").options.namedItem("siap.sige.sentenza.action.ActLoadInserisciSentenzaDecreto");
    		  if(optionTag != null){ 
    		  	  optionTag.selected = true;
    		  }
    	  }
      }

	// 20180129: [SG] cambiata gestione refresh della pagina provenendo da popup
	function refresh() {
//     	  window.location.reload();
		var str = window.location.href;
		var n = str.indexOf("ActLoadDettaglioFascicolo") != -1;
		if (n == false)
			window.location.href = window.location.href+"?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFascicolo.getIdFascicoloSige()%>";
		else
			window.location.href = window.location.href;
	}

      // Chiamata funzione lista Procedimenti Siep Di Cumulo
      function ListaProcedimentoSiepDiCumulo(a_formname, a_fieldcode)
      {
        // Compone il link URL per passare i parametri alla pagina jsp
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadListaProcedimentoSiepDiCumulo";
            aLink += "&formname=" + a_formname;           
            aLink += "&fieldcode=" + a_fieldcode;
        desktop = window.open(aLink, "Lista_Titoli_Esecutivi","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }
      
</script>

</head>

<body class="corpo" onLoad="javascript:caricaIscrizioneSentDecr();">
<FORM name="comandi">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Procedimento SIGE</font>
      </td>
      <td class="LBG">
    <%       
		// Modificabilità solo delle note quando il Fascicolo non è modificabile ma appartiene allo stesso ufficio dell'Utente connesso
      if((UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(lFascicolo.getChiaveUfficio())==0))
      {
    	if(Modificabile.equalsIgnoreCase("NO"))
        {
        	Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
        	  //Visualizzazione del bottone di modifica
        	  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
        	  {
        	    Iterator lIterBottoni = lFunFiglie.iterator();
        	    FunctionModel lFun = null;
        	   	while(lIterBottoni.hasNext())
        	   	{
        	    	lFun = (FunctionModel)lIterBottoni.next();
        	    	if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)  )
        	    	{
     %>
               <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFascicolo.getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Note" width="24" height="24" border="0">
          </a>
<%
       				}
       			} // endwhile
        	}
        }
    }
%>
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lFascicolo.getIdFascicoloSige()%>" />
          <jsp:param name="Modificabile" value="<%=Modificabile%>" />
          <jsp:param name="Cancellabile" value="<%=Cancellabile%>" />
        </jsp:include>
     </td>

    <%if( request.getParameter("TornaQui") == null  && request.getParameter("StoTornando") == null ) {
          if( (request.getParameter("NomeAzione") == null) 
              || (   request.getParameter("NomeAzione") != null 
              && !request.getParameter("NomeAzione").equals("siap.sige.fascicolo.action.ActInserisciFascicolo")
                   ) 
           ) {
    %>
          <td class="LBG">
            <a href="javascript:history.go(-1);">
             <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
    <% 		 }
      }else{ 
    %>
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    <%} %> 

   </tr>
 </table>
  
  <input type="HIDDEN" name="cognomeSoggetto" value="<%=cognomeSoggetto%>" >
  <input type="HIDDEN" name="nomeSoggetto" value="<%=nomeSoggetto%>" >
  <input type="HIDDEN" name="dataNascitaSoggetto" value="<%=FascicoloSigeEsteso.getSoggetto().getDataNascita()%>" >
  
</FORM>
<table cellspacing="1" cellpadding="1" width="95%" border=<%=isBorder%>>
  <tr>
    <td class="label" width=17% valign=<%=isVALIGN%>> Procedimento </td>
    <td class="L" width=83%>
      <table>
      	<tr>
      				<td nowrap="nowrap">
        				<font class="label">Numero: </font>
       		 			<font class="campo">
       		 				<%=lFascicolo.getChiaveAnno()%>/<%=lFascicolo.getChiaveProgr()%>
            &nbsp;&nbsp;<%=lFascicolo.getDescrUfficio()%>
       		 </font>
					</td>
				</tr>
				<tr>
					<td>
         	<font class="label">Sezione: </font>
       		 			<font class="campo"><%=lFascicolo.getDescrSezione()%></font>
      	</td>
     	</tr>
   		<tr>
   			<td width="50%"> 
   				<font class="label">Data Iscrizione : &nbsp;</font>
   				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font>
				</td>
				<td>
   				<font class="label">Data Udienza : &nbsp;</font>
   				<font class="cRosso">
<%
	if (FascicoloSigeEsteso.getUdienzaProcedimento() != null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige() != null) {
%>
   					<%=StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy"),"-")%>
<%
	} else {
%>
							-
<%
	}
%>
   				</font>
  			</td>
  		</tr>
  		<tr>
       	<td width="50%"> 
      		<font class="label">Stato: &nbsp;</font>
         	 <font class="cRosso"><%=StringUtils.toStringJSP(lFascicolo.getDescrStatoFascicolo(),"-")%></font>
         	 <% 
         	 if(fascicoloUnificante != null && fascicoloUnificante.getFascicoloSige() != null && fascicoloUnificante.getFascicoloSige().getIdFascicoloSige()!=null) {
%>    
      					<font class="label">&nbsp;al&nbsp;</font>
      					<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fascicoloUnificante.getFascicoloSige().getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>">
       <%=fascicoloUnificante.getFascicoloSige().getChiaveAnno()%>
       /
       <%=fascicoloUnificante.getFascicoloSige().getChiaveProgr()%>
      </a>
<%
 	  }
%>
         	 
         	 
         	 
         	 
         	 
         	 
		</td>
		<td>
      		<font class="label">Data Definizione: &nbsp;</font>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString( lFascicolo.getDataDefinizione(),"dd-MM-yyyy"),"-")%></font>
      	</td>
  		</tr>
  		
  		<%
  		 if(lFascicolo.getCodStatoFascicolo() != null &&  lFascicolo.getCodStatoFascicolo().equals("16")){
  		%>
  		<tr>
  			<td width="50%">&nbsp; </td>
  		<td>
      		<font class="label">Data Provvedimento: &nbsp;</font>
            <font class="cRosso"><%=StringUtils.toStringJSP(DateUtils.getDateToString( ultima_impugnazione.getDataDecisione(),"dd-MM-yyyy"),"-")%></font>
      	</td>
  		</tr>
  		<%
 	  		}
		%>		
  		<tr>
  		
       	<td width="50%"> 
      		<font class="label">Data Compilazione Foglio Complementare: &nbsp;</font>
         	 <font class="campo"><%=foglioComplementare%></font>
				</td>
				<td>
      		<font class="label">Data Invio Atti in Archivio: &nbsp;</font>
          <font class="campo"><%=attiInArchivio%></font>
      	</td>
  		</tr>
<%
	if (posizione_materiale != null && posizione_materiale.getDescrPosizioneMateriale() != null && posizione_materiale.getDescrPosizioneMateriale().length() > 0) {
%>
   <tr>
      <td colspan="2"><font class="label">Posizione Materiale :</font>
      	<font class="cVerde"><%=posizione_materiale.getDescrPosizioneMateriale()%></font>
      </td>
    </tr>
<%
	}
%>
  </table>
    </td>
  </tr>

<%
	if (elencoFasUnificati != null && FascicoloSigeEsteso.getFascicoloSige().getNumeroFascicoliUnificati() != null
			&& FascicoloSigeEsteso.getFascicoloSige().getNumeroFascicoliUnificati().intValue() >0) {
%>
<br>

  <tr>
    <td class="label" width=15% valign=<%=isVALIGN%>> Proc. Unificati </td>
    	<td colspan="3">
     		<table cellspacing="1" cellpadding="1"  width="85%" border=0>
       <tr>
           <td class="L" colspan=1 width=100%>
<%
       Iterator itx4 = elencoFasUnificati.iterator();
       	while (itx4.hasNext()) {
         FascicoloSigeModel lFasUnificati = (FascicoloSigeModel)itx4.next();
%>
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFasUnificati.getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>">
             <%=lFasUnificati.getChiaveAnno()%>
             /
             <%=lFasUnificati.getChiaveProgr()%>
            </a>
            &nbsp;&nbsp;&nbsp;
<%
       }
%>
           </td>
         </tr>
     </table>
    </td>
  </tr>

<%
      }

	// Procedimento Collegato
	if(fascicoloCollegato != null && fascicoloCollegato.getIdFascicoloSige() != null){
%>		
  		<tr>
       		<td class="label" valign=<%=isVALIGN%>>Collegato al Proc.</td>
       		<td class="L" width=80%> 
      			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=fascicoloCollegato.getIdFascicoloSige()%>&TornaQui=<%=TornaQui%>">
       				<%=fascicoloCollegato.getChiaveAnno()%>
       				/
       				<%=fascicoloCollegato.getChiaveProgr()%>
      			</a>
<%
 	  }
%>
			</td>
		<td>
<%		
		
	//}

    if (!(nomeSoggetto.equalsIgnoreCase("ignoto") && cognomeSoggetto.equalsIgnoreCase("ignoto"))) {
%>  		
  <tr>
    <td class="label" valign=<%=isVALIGN%>>Soggetto</td>
         <td class="L" width=80%>
         <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_SOGGETTO%>"/>
<%-- MEV_57: aggiunta etichetta minorenne ed eventuali due campi se presenti --%>
<%
		if (etichettaEta != null && !"".equals(etichettaEta)) {
%>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<font class="campo">
    			<%=etichettaEta%>
    		</font>
<%
		}
%>

<%
		if (FascicoloSigeEsteso.getSoggetto().getDataReatoSius() != null || FascicoloSigeEsteso.getSoggetto().getEtaPresuntaAnni() != null) {
%>
		<table>
			<tr>				
<%
			if (FascicoloSigeEsteso.getSoggetto().getEtaPresuntaAnni() != null) {
%> 
				<td><font class="label">Età Presunta:</font>
					<font class="campo">
					<%=StringUtils.toStringJSP(FascicoloSigeEsteso.getSoggetto().getEtaPresuntaAnni())%>&nbsp;</font>anni
<%
				if (FascicoloSigeEsteso.getSoggetto().getEtaPresuntaMesi() != null) {
%> 
						&nbsp;e&nbsp;<font class="campo"><%=StringUtils.toStringJSP(FascicoloSigeEsteso.getSoggetto().getEtaPresuntaMesi())%>&nbsp;</font>mesi
<%
				}
					%>	
					</td>				
<%			}
if (FascicoloSigeEsteso.getSoggetto().getDataReatoSius() != null) {
%>
				
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>
					<font class="label">Data commesso reato:</font>
					<font class="campo">
						<%=DateUtils.getDateToString(FascicoloSigeEsteso.getSoggetto().getDataReatoSius(),"dd-MM-yyyy")%>
					</font>
				</td>
			</tr>
		</table>
<%
	} }
%>
		<table>
    		    <jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeResidenza.jsp"/>
   		    <tr>
 					<td>
 						<font class="label">Posizione Giuridica: </font>
   			<font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrPosizioneGiuridica(),"-")%></font>
   		</td>
 		</tr>
 		<tr>
					<td>
						<font class="label">Data Fine Pena: </font>
   				<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataFinePena(),"dd-MM-yyyy"),"-")%></font>
   		</td>
 		</tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_DETENZIONE%>"/>
  		
    </table>
    </td>
  </tr>
 <%
    }
    if (nomeSoggetto.equalsIgnoreCase("ignoto") && cognomeSoggetto.equalsIgnoreCase("ignoto")) {
 %>
<tr>
    <td class="label" valign=<%=isVALIGN%>>Soggetto</td>
         <td class="L" width=80%>
         <font class="campo">
        <a class="cliccabile">
          IGNOTO
        </a>
      </font>&nbsp;
       <table>
   		    <tr>
 			<td> <font class="label">Posizione Giuridica: </font>
   			<font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrPosizioneGiuridica(),"-")%></font>
   		</td>
 		</tr>
 		<tr>
			<td> <font class="label">Data Fine Pena: </font>
   			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFascicolo.getDataFinePena(),"dd-MM-yyyy"),"-")%></font>
   		</td>
 		</tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_DETENZIONE%>"/>
  		
    </table>
    </td>
  </tr> 
  <%
    }
  %>
  
   <tr>
    <td class="label" valign=<%=isVALIGN%>>Atto/Richiesta</td>
    
        <td class="L" width=83%>
      <table>
      	<tr>
      		<td>
						<font class="label">Tipo: </font>
						<font class="campo"><%=StringUtils.toStringJSP(lRichiesta.getDescrTipoAtto(), "-")%>&nbsp;&nbsp;&nbsp;</font>
						<font class="label">Data Atto: </font>
						<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>&nbsp;&nbsp;&nbsp;&nbsp;
						<font class="label">Data Arrivo in cancelleria: </font>
						<font class="campo"><%=StringUtils.toStringJSP( DateUtils.getDateToString(lRichiesta.getDataArrivoCancelleria(),"dd-MM-yyyy"),"-")%></font>&nbsp;
     	 		</td>
				</tr>
				<tr>
      		<td>
						<font class="label">Mittente: </font>
						<font class="campo"><%=StringUtils.toStringJSP(lRichiesta.getDescrTipoRichiedente(), "-")%>
									&nbsp;<%=StringUtils.toStringJSP(lRichiesta.getDescRichiedente(), "")%>&nbsp;&nbsp;</font>
						<font class="label">Sede: </font>
						<font class="campo"><%=StringUtils.toStringJSP(lRichiesta.getDescrSedeRichiedente(), "-")%></font>&nbsp;
					</td>
				</tr>   
        <jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeFasSiepRif.jsp"/>   	 
      </table>
    </td>
	</tr>

   <tr>
       <td class="label" valign=<%=isVALIGN%>>Titolo esecutivo di competenza</td>
       <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_SENTENZE%>">
       		<jsp:param name="Competenza" value="S" />
       </jsp:include>
   </tr>

<% if (lCumuloSiep != null && lCumuloSiep.size() > 0) { %> 
  <tr>
 	<td class="label" valign=<%=isVALIGN%>>
 		<a class="cliccabile" href="Javascript:ListaProcedimentoSiepDiCumulo('InserisciTenoreSige', '<%=ICostantiTenoreSige.CAMPO_SEN_ID_SENTENZA%>');">
 			Cumulo
 		</a>
 	</td>
    <td class="L" width=83%> 
    	<table>	
 <%
	if (ListaProcedimentoSiepDiCumulo != null || ListaProcedimentoSiepDiCumulo.size() > 0) {
		Iterator lIterProcedimentoSiepDiCumulo = ListaProcedimentoSiepDiCumulo.iterator();
  
		// Ciclo di Caricamento Titoli Esecutivi.
   		while ( lIterProcedimentoSiepDiCumulo.hasNext() ) {
   			SentenzaModel lSentenza = (SentenzaModel)lIterProcedimentoSiepDiCumulo.next();
 	%>
	      	<tr>
	      		<td>
					<% if (lSentenza.getDescrProvvCumulo() != null && !lSentenza.getDescrProvvCumulo().equals("")) {%>
						<font class="campo"><%=lSentenza.getDescrProvvCumulo()%> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%> </font>					
					<% } else { %>
						<font class="campo">Provvedimento di determinazione pene concorrenti <%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%> </font>
					<% } %>
				</td>
			</tr>
<% 
        }  // end while      
	}  // end if
%>
	    </table>
    </td>
  </tr>
<%  } %>  
  
  <tr>
    <td class="label" valign=<%=isVALIGN%>>Magistrato</td>
    <td colspan=3 >
 			<table cellspacing=0 cellpadding=0 width=100%>
<%
      if(lMagistrato != null && lMagistrato.getMagistrato() != null)
      {
%>       <tr>
           <td class="L" colspan=1 width=100%>
            <font class="campo"><%=StringUtils.toStringJSP(lMagistrato.getMagistrato().getCognome())+" "+StringUtils.toStringJSP(lMagistrato.getMagistrato().getNome())%></font>
           </td>
         </tr>
<%
       }else
       {%>
       <tr><td class="L" colspan=1 width=100%><font class="campo"> - </font></td></tr>
<%     }
%>
     	</table>
    </td>    
</tr>
  <tr>
    <td class="label" valign=<%=isVALIGN%>>Tipo rito</td>
<%
       if(lFascicolo.getDescrTipoGiudizio() != null && lFascicolo.getDescrTipoGiudizio().trim().length() > 0)
      {  
 %>   
    	<td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(lFascicolo.getDescrTipoGiudizio(), "-")%></font>
        </td>
<%
       }else
       {%>
       <td class="L"><font class="campo"> - </font></td>
<%     }
%>           
</tr>

  <!-- Note -->
  <tr>
    <td class="label" valign=<%=isVALIGN%>>Note</td>
<%		String lNote="-";
			if (lFascicolo.getNote()!=null) lNote=lFascicolo.getNote();
%>
  <td class="L" width=29%><font class="campo"><%=lNote%></font></td>
  </tr>
  
    <!-- Oggetti Atto/Richiesta -->
    <tr>
    	<td class="label" valign=<%=isVALIGN%>>Oggetti Atto/Richiesta
    	</td>
  		
  		<td class="L">
  		  <table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  			<tr>
    		 <td class="label" width=15% colspan=2>
             	<a class="cliccabile" href="<%=lLinkOggetti%>">
       	 	Oggetti
      		</a>
     		<td class="label" width=85% colspan=1>
      		<a><img name="image1" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(1);" alt="" border=0></a>
  			 </td>
  			</tr>
  			</table>
  		</td>
  	</tr>		
   	<tr>
     	<td class="label" valign=<%=isVALIGN%>></td>
   		
   		<td  class="L">
  			 <div id="elenco1" style="display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoTenori.jsp"/>
  			 </div>
 		</td>
 	</tr>
   
   <!-- Altri Titoli Esecutivi -->
<% if (sentenze.size() > 0){ %>
   <tr>
    	<td class="label" valign=<%=isVALIGN%>>Altri Titoli Esecutivi</td>
  		<td class="L">
  		  <table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  			<tr>
     		<td class="label" width=85% colspan=1>
      		<a><img name="image4" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(4);" alt="" border=0></a>
  			 </td>
  			</tr>
  			</table>
  		</td>
 	</tr>		
 	<tr>
     	<td class="label" valign=<%=isVALIGN%>></td>
    	<td  class="L">
 			<div id="elenco4" style="width: 100%; display:block" >
	 			<table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
		  			<tr>
		  				<jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_SENTENZE%>"/>
			 		</tr>
				</table>
			</div>
 		</td>
 	</tr>
<% } %>
<%-- 
   <!-- Altri Atti -->
<% if(provvedimentiAltri.size() > 0){ %>
   <tr>
    	<td class="label" valign=<%=isVALIGN%>>Altri Atti</td>
  		<td class="L">
  		  <table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  			<tr>
     		<td class="label" width=85% colspan=1>
      		<a><img name="image5" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(5);" alt="" border=0></a>
  			 </td>
  			</tr>
  			</table>
  		</td>
 	</tr>		
 	<tr>
     	<td class="label" valign=<%=isVALIGN%>></td>
    		<td  class="L">
 			<div id="elenco5" style="width: 100%; display:block" >
 			<table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  				<tr>
  					<jsp:include page="<%=ICostantiFascicoloSige.PG_ELENCO_ALTRI_PROVVEDIMENTI%>"/>
 			 	</tr>
			</table>
			</div>
 		 </td>
 	</tr>
<% } %>
--%>

   <!-- Richieste Istruttorie -->
<% if (pareri.size() > 0){ %>
   <tr>
    	<td class="label" valign=<%=isVALIGN%>>Pareri</td>
  		<td class="L">
  		  <table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  			<tr>
     		<td class="label" width=85% colspan=1>
      		<a><img name="image6" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(6);" alt="" border=0></a>
  			 </td>
  			</tr>
  			</table>
  		</td>
 	</tr>		
 	<tr>
     	<td class="label" valign=<%=isVALIGN%>></td>
    		<td  class="L">
 			<div id="elenco6" style="width: 100%; display:block" >
 			<table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  				<tr>
  					 <td>
  					 <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
      <td class="int" width=15%>Data Richiesta</td>
      <td class="int" width=40% >Destinatario</td>
      <td class="int" width=30%>Esito Parere</td>
      <td class="int" width=15% >Data Esito</td>
    </tr>
<%
    
    Iterator itx = pareri.iterator();

    String sUfficio = "Procura";
    while ( itx.hasNext())
    {
      ParereModel parere = (ParereModel)itx.next();
      
    
%>
      <tr>
      	<td class="c" style="text-align:left !important;"><font class="label"><%=DateUtils.getDateToString(parere.getDataEmissione(),"dd-MM-yyyy")%></font></td>
       	<td class="c" style="text-align:left !important;"><font class="label"><%=StringUtils.toStringJSP(parere.getDescrUfficioDestinatario() , "-")%></font></td>
       	<td class="c" style="text-align:left !important;"><font class="label"><%=parere.getDescrEsito()%></font></td>
      	<td class="c" style="text-align:left !important;"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(parere.getDataRicezioneAtti(),"dd-MM-yyyy"), "-")%></font></td>
      	
      </tr>
<%
  }
%>
    </table>
  					 
  					 
  					 </td>
 			 	</tr>
			</table>
			</div>
 		 </td>
 	</tr>
<%
}

if(elencoNote != null && elencoNote.size() >0) {
%>
	  <tr>
	    <td class="label" valign=<%=isVALIGN%>> Note </td>
	    <td colspan=3 >
	     <table cellspacing="1" cellpadding="1" width="<%=lWidth%>" border="0">
	
	      <tr>
	       <td class="L" colspan=1 width=100%>
	<%
	          Iterator itx5 = elencoNote.iterator();
	          while ( itx5.hasNext())
	          {
	            NoteModel lNoteRidefinizioneTitolo = (NoteModel)itx5.next();
	%>
	            &bull; <font class="cVerde"><%=DateUtils.getDateToString(lNoteRidefinizioneTitolo.getData(),"dd-MM-yyyy")%> - <%=lNoteRidefinizioneTitolo.getDescrizione()%></font>
	            <br>
	
	<%        }%>
	       </td>
	      <tr>
	
	     </table>
	    </td>
	  </tr>
<% } %>
      
   <!-- Difensore -->
   <tr>
    	<td class="label" valign=<%=isVALIGN%>>Difensore</td>
  		<td class="L">
  		  <table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  			<tr>
     		<td class="label" width=85% colspan=1>
      		<a><img name="image2" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(2);" alt="" border=0></a>
  			 </td>
  			</tr>
  			</table>
  		</td>
 	</tr>		
 	<tr>
     	<td class="label" valign=<%=isVALIGN%>></td>
    		<td  class="L">
 			<div id="elenco2" style="width: 100%; display:block" >
 			<table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
  				<tr>
  					<jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_LOAD_ELENCOAVVOCATI%>"/>
 				</tr>
			</table>
			</div>
 		 </td>
 	</tr>

<%String lAction = "siap.sige.provvedimento.action.ActRicercaProvvedimenti";%>
   <tr>
     <td class="L">
 		<table cellspacing="1" cellpadding="1"  width="100%" border=<%=isBorder%>>
 			<tr>
				<td class="label" width=17% valign=<%=isVALIGN%>>
		    	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFascicolo.getIdFascicoloSige()%><%=retParam%>" title="Elenco Provvedimenti">
		        Provvedimenti</a>&nbsp;
				</td>
    			<td class="label" width=85% colspan=1>
      				<a><img name="image3" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(3);" alt="" border=0></a>
  			 	</td>
  			</tr>
  		</table>
  	 </td>
   </tr>		
   <tr>
   	 <td class="label" valign=<%=isVALIGN%>></td>
   	 <td  class="L">
 		<div id="elenco3" style="width: 100%; display:block" >
 			<table cellspacing="1" cellpadding="1" width="100%" border=<%=isBorder%>>
  				<tr>
		  			<jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeProvvedimenti.jsp"/>
 		 		</tr>
			</table>
		</div>
	 </td>
   </tr>
   <br/>
 	 		
  </table>

</body>
</html>
