<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.util.SiapStringUtil"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>

<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="AnnotazioneMan" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="strDescrUfficio"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="strDescrComune"     			scope="request" class="java.lang.String"/>

<jsp:useBean id="articolo"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="motivazione"     		scope="request" class="java.lang.String"/>

<jsp:useBean id="reati" scope="request" class="java.util.Vector" />
<jsp:useBean id="stringareati" scope="request" class="java.util.Vector" />

<jsp:useBean id="penacomplessiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" />

<%
PenaComplessivaSanzioneSostitutivaModel dettaglioPenaComplessiva=(PenaComplessivaSanzioneSostitutivaModel)request.getAttribute("penacomplessiva");
PenaComplessivaModel lPenCom = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessiva() != null) ? dettaglioPenaComplessiva.getPenaComplessiva() : null;
SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getSanzioneSostitutiva() : null;
	

 String lIdPenaCompStr = lPenCom != null ?  lPenCom.getIdPenaComplessiva().toString() : "";

if(lSanSos == null)
  lSanSos = new SanzioneSostitutivaModel();

%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"> 
  <script language="JavaScript">
  var desktop;  
  
// Richiamo della finestra di pop-up per inserire motivazione Annullamento
function conferma(a_action, idIstanza, eventodacanc, tipo)
 {
  if (window.confirm('Confermi la cancellazione ?'))
  {
    if (tipo=="A")
    {
       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&IdEvento=" + eventodacanc +"&IdIstanza=" + idIstanza+ "&TipoOp=" + tipo +"&TipoProvvedimento=Disposizione", "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=450,height=200");
         window.parent.close();
    }else
    {
      str = "/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActCancellaInoltroNuovaIstanza&IdEvento="+eventodacanc+"&IdIstanza="+idIstanza+"&TipoOp="+tipo+"&TipoProvvedimento=Disposizione";
               window.location.href=str;
    }
  }
 }

    
    function pulisciCampi() {
		
		for (i=0; i < 5; i++) {
			
			formName.cablati2(i).checked = false;
			formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).selectedIndex=0;
			formName.<%=ICostantiReato.CAMPO_ANNO_FONTE%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_ARTICOLO%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).selectedIndex=0;
			formName.<%=ICostantiReato.CAMPO_COMMA%>(i).value="";
			//***********************************************************************************
			//Federica - a9-rr-078
			//aggiunto campo Comma-Qualificante 
			formName.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>(i).selectedIndex=0;
			//***********************************************************************************
			formName.<%=ICostantiReato.CAMPO_LETTERA%>(i).value="";
			formName.<%=ICostantiReato.CAMPO_NUMERO%>(i).value="";
		}
		
		for (i=0; i < 31; i++) {
			
			formName.cablati(i).checked = false;
			
		}		
    }    
    function insNorme(str) {

		pulisciCampi();
		
    	arrNorme = str.split("<%=ICostantiReato.SEP_NORME%>");
		
		//alert("LarrNorme=" + arrNorme.length);
		
		i = 0;			
		for (c=0; c < arrNorme.length; c++) {
			
			arrCampi = arrNorme[c].split("<%=ICostantiReato.SEP_CAMPI%>");
			
			if (!testCheck(arrCampi)) {

				for (cf=0; cf < formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).options.length; cf++) {
					
					if (formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).options[cf].value == arrCampi[0]) {
						formName.<%=ICostantiReato.CAMPO_COD_FONTE%>(i).selectedIndex=cf;
					}									
				}
				
				for (cf=0; cf < formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).options.length; cf++) {
					
					if (formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).options[cf].value == arrCampi[4]) {
						formName.<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>(i).selectedIndex=cf;
					}									
				}			
				
				formName.<%=ICostantiReato.CAMPO_ANNO_FONTE%>(i).value=arrCampi[1];
				formName.<%=ICostantiReato.CAMPO_NUMERO_FONTE%>(i).value=arrCampi[2];
				formName.<%=ICostantiReato.CAMPO_ARTICOLO%>(i).value=arrCampi[3];
				formName.<%=ICostantiReato.CAMPO_COMMA%>(i).value=arrCampi[5];
				//***********************************************************************************
				//Federica - a9-rr-078
				//aggiunto campo Comma-Qualificante 
				for (cf=0; cf < formName.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>(i).options.length; cf++) {
					
					if (formName.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>(i).options[cf].value == arrCampi[6]) {
						formName.<%=ICostantiReato.CAMPO_COMMA_QUALIFICANTE%>(i).selectedIndex=cf;
					}									
				}			
				//***********************************************************************************
				formName.<%=ICostantiReato.CAMPO_LETTERA%>(i).value=arrCampi[7];
				formName.<%=ICostantiReato.CAMPO_NUMERO%>(i).value=arrCampi[8];
				
				i++;
			}

		}
      
      window.parent.close();
    }
  </script> 
</head>

<body class="corpo">
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioRichiestaRevocaSosp">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Dettaglio Richiesta Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
			</td>
      <%
      // Visualizzo il tasto di stampa se l'evento non è validato
      if (   evento.getEvento().getFlagDocumentoRegistrato()==null
          || (   evento.getEvento().getFlagDocumentoRegistrato()!=null
              && evento.getEvento().getFlagDocumentoRegistrato().equals("N")
             )
         )
      {
      %>
      <!-- BOTTONE DI STAMPA -->
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.penasospesa.action.ActStampaRichiestaRevoca&IdEvento="+evento.getEvento().getIdEvento()+"&CodStatoIstanza=1"%>"/>
      </jsp:include>
      <%}%>

<%
    if (evento.getEvento().getFlagDocumentoRegistrato() == null ||
        (evento.getEvento().getFlagDocumentoRegistrato() != null && 
        		evento.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
      {
%>
     <td class="LBG">
      <a  href="/jsp/Main.jsp?Action=siap.siep.penasospesa.action.ActUploadRichiestaRevoca&IdEvento=<%=evento.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.penasospesa.action.ActLoadDettaglioRichiestaRevoca&IdEvento=<%=evento.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S">
        <img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
      </a>
     </td>

<%
      }
%>
          <td class="LBG"> 
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
	</table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>    
	
	<table width="80%"  cellspacing=6 cellpadding=6>
	    <tr><td class="Titolo" colspan="2">Estremi del Provvedimento</td></tr>
    <tr>		
		<td class="l" width="30%">Data Emissione</td>
		<td class="l">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
	</tr>
    <tr>
    	<td class="l" width="30%">Articolo</td>
    	<td class="l">
			<font class="campo">
    			<%=articolo %>   
    		&nbsp;</font> 		
    	</td>
    </tr>
    <tr>
    	<td class="l" width="30%">Motivazione</td>
    	<td class="l">
			<font class="campo">
    			<%=motivazione %>   
    		&nbsp;</font> 		
    	</td>
    </tr>
    <tr>
      <td  class="l" width="30%">Note</td>
      <td class="l">
<%
		CampoNotaModel[] lListCampoNota = null;
		lListCampoNota = evento.getCampoNote();
		CampoNotaModel lCampoMod = null;
		if((lListCampoNota != null)  && !(lListCampoNota.length==0))
		{
  			lCampoMod = (CampoNotaModel)lListCampoNota[0]; // è previsto un solo campo nota

%>
              <font class="campoNoCap"><%=SiapStringUtil.formattaCampoNote(lCampoMod.getDescr(), "<br>")%><br></font>
<%
        }
%>
      </td>
    </tr>

<%    
      for (int i=0;i<evento.getNotifiche().length;i++)
      {
%>
      <tr>
        <td class="l">Destinatario</td>
        <td class="L" colspan=3>
         <table>
         <tr>
         	<td class="l"><font class="campo">
  			<%
  			if(evento.getNotifiche()[i].getUfficio()!= null){
  			%>
             	<%=StringUtils.toStringJSP(evento.getNotifiche()[i].getUfficio().getDescrTipoUfficio())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(evento.getNotifiche()[i].getUfficio().getDescrComune())%>
			<%}%>	
			</font></td>
          </tr>
          <tr>
          	<td class="L" colspan=3>
          			<font class="campo"> 
          			<%=StringUtils.toStringJSP(evento.getNotifiche()[i].getNote() )%></font>
          	</td>
        </tr>
        </table>

      </tr>
<%} %>

<%
	// 18/04/2019  MEV70 La sezione "Titolo che determina la revoca" va presentata solo se:
	//					- Il CodMotivo è diverso da 'Revoca Beneficio ex art. 165 c.p.p.' (1108)
	//					- Il CodMotivo è = 1108 ma sono valorizzati anno e Numero sentenza.
    if (!"1108".equals(evento.getEvento().getCodMotivo() ) 	||
       ( "1108".equals(evento.getEvento().getCodMotivo() )  &&    		
       (AnnotazioneMan.getAnnoSentenzaSiap()!=null && AnnotazioneMan.getAnnoSentenzaSiap().toString().length()>0 &&  
        AnnotazioneMan.getNumeroSentenzaSiap()!=null && AnnotazioneMan.getNumeroSentenzaSiap().length()>0 ) ) ) {  
%>
	    <tr><td class="Titolo" colspan="2">Titolo che determina la revoca</td></tr>
	<%
	String provvedimento="Ordinanza";
	if ((evento.getEvento().getAnnIdAnnotazioneManuale())!=null)
		provvedimento="Sentenza";	
	%>
    <tr>
    	<td class="l" width="30%">Tipo Provvedimento</td>
    	<td class="l">
			<font class="campo"><%=provvedimento%></font>
    	</td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Data Provvedimento</td>
      <td class="l">
			<font class="campo">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneMan.getDataSentenzaSiap(),"dd-MM-yyyy"))%> 
				</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero Provvedimento</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getAnnoSentenzaSiap())%>
        		/
        			<%=StringUtils.toStringJSP(AnnotazioneMan.getNumeroSentenzaSiap())%>&nbsp;
        		</font>
      		</td>
    	</tr>
     	<tr> 
      		<td class="l" width="30%"> Data Irrevocabilità</td>
      		<td class="l">
				<font class="campo">
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneMan.getDataIscrizioneSiep(),"dd-MM-yyyy"))%>&nbsp;
				</font>
      </td>
    </tr>
    <tr>
      <td class="l" width="30%">Ufficio Emittente</td>
      <td class="l">
			<font class="campo">
	          		<%=strDescrUfficio%> &nbsp;di&nbsp; <%=strDescrComune%> &nbsp;
	        </font>
      </td>
    </tr>
<%	} 

    if(reati.size()>0) {
    %>
      <tr>
        <td class="LBG">Elenco Reati</td>
      </tr>
		<%Iterator itx = reati.iterator();
		int cont = 0;
		String str = new String();
		String strReato = new String();
		String lProgressivo = new String();
		
      	while ( itx.hasNext()) {
      		
	        ReatoModel lReato = (ReatoModel)itx.next();
	        if (lReato.getProgrCircostanza().intValue() == 1) { 
	        	lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale().toString() : lReato.getProgrReato().toString();
	        %>
	            <tr>
	            <td class="l"><font class="cRosso">Reato <%=lProgressivo%>: </font>
	        <%
	        	strReato = (String)stringareati.get(cont);
	        %>
	        <%=StringUtils.cStrForJS(strReato)%>
	            </td>
	        <%
	        	//str = (String)stringacampi.get(cont);
	        	cont++;
	        %>
	        <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	        <%-- td class="c"><a href="Javascript:insNorme('<%=StringUtils.cStrForJS(str)%>')"><img align="middle" src="/images/fileselected.gif" border=0></a></td> --%>
	        	</tr>
	        <%
	        }
      	}
    }
      	%>    



</table>
<%if (lPenCom != null) { %>
  <table cellspacing=2 cellpadding=2 width=80%>
    <tr><td class="Titolo" colspan=4>Pena Complessiva</td></tr>
    <tr>
    <% 
    if ((lPenCom.getNumAnniReclusione() != null) || (lPenCom.getNumMesiReclusione() != null)
      || (lPenCom.getNumGiorniReclusione() != null)) { %>
      <td class="l">Reclusione</td>
      <td class="l">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%></font>
      </td>
    <% } %>
    <% if (lPenCom.getImportoMulta() != null) {%>
      <td class="l">Multa</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    <% } %>
    </tr>
    <tr>

  <% if ((lPenCom.getNumAnniArresto() != null) || (lPenCom.getNumMesiArresto() != null)
      || (lPenCom.getNumGiorniArresto() != null)) { %>
      <td class="l">Arresto</td>
      <td class="l">
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%></font>
      </td>
   <% } %>
   <% if (lPenCom.getImportoAmmenda() != null) {%>
      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
   <% } %>
    </tr>
<% 
		if (   (lPenCom.getDescrTipoPenaDetentivaDB() != null) 
		    && (!(lPenCom.getDescrTipoPenaDetentivaDB().equals("-")))) 
		{ 
%>
			<tr>
		  	<td class="l">Ergastolo</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%></font>&nbsp;</td>
		  </tr>
<%
		}
%>
    <tr>
<% 
		if (lPenCom.getDataInizioIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataFineIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
    </tr>
    <tr>
<% 
		if (	 (lPenCom.getNumAnniIsolamentoDiurno() != null) || (lPenCom.getNumMesiIsolamentoDiurno() != null)
    		|| (lPenCom.getNumGiorniIsolamentoDiurno() != null)) 
		{ 
%>
	    <td class="l">Durata Isolamento Diurno</td>
	    <td class="l">Anni
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniIsolamentoDiurno(), "0") %>&nbsp;</font>
	    		Mesi
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiIsolamentoDiurno(), "0")%>&nbsp;</font>
	    		Giorni
	     	<font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniIsolamentoDiurno(), "0")%>&nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataPrescrizione() != null) 
		{ 
%>
   		<td class="l">Data Prescrizione</td>
			<td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataPrescrizione(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
   </tr>
   <% if ((lSanSos.getDescrTipoSanzione() != null) || (lSanSos.getNumAnni() != null) ||
      (lSanSos.getNumMesi() != null) || (lSanSos.getNumGiorni() != null) ||
      (lSanSos.getSanzionePecuniariaMulta() != null)  ||
      (lSanSos.getSanzionePecuniariaAmmenda() != null)) { %>
    <tr><td class="Titolo" colspan=4>Sanzione Sostitutiva</td></tr>
    <% if (lSanSos.getDescrTipoSanzione() != null) { %>
    <tr>
      <td class="l">Tipo Sanzione Sostitutiva</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%></font>&nbsp;</td>
    </tr>
   <% } %>
   <% if ((lSanSos.getNumAnni() != null) ||
      (lSanSos.getNumMesi() != null) || (lSanSos.getNumGiorni() != null)) { %>
    <tr>
      <td class="l">Durata Sanzione Sostitutiva</td>
      <td class="l" colspan="3">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>
      </td>
    </tr>
  <% } %>
  <% if (lSanSos.getSanzionePecuniariaMulta() != null) { %>
    <tr>
      <td class="l" colspan="2">Pena Pecuniaria Sostitutiva Multa</td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
   <% if (lSanSos.getSanzionePecuniariaAmmenda() != null) { %>
    <tr>
      <td class="l" colspan="2">Pena Pecuniaria Sostitutiva Ammenda</td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
<% } %>
  </table>
<% } %>

	
</FORM>
 <div align=left style="visibility:hidden" id="upld">
         <FORM name="comandi" enctype="multipart/form-data" method="post">
             <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
             <tr><td class="L">
                 <input  class=bottone  type="submit" value="Conferma">
                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penasospesa.action.ActUploadRichiestaRevoca">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= evento.getEvento().getIdEvento() %>">
                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.penasospesa.action.ActLoadDettaglioRichiestaRevoca&IdEvento=<%=evento.getEvento().getIdEvento()%>">
              </td> </tr>
            </table>

          </FORM>
      </div>
    <br>
</body>
</html>