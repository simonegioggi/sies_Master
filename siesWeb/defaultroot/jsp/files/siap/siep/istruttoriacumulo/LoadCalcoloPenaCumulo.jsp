<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaRideterminataCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaRideterminataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraCautelareCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ComputiCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>

<%@page import="org.apache.log4j.Logger"%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<jsp:useBean id="aCalcoloPenaModel" scope="request" class="siap.siep.modulocumulo.util.CalcoloPenaCumuloModel" />

<%--
================================================================================
 Popup con i componenti il calcolo della Pena Cumulo
================================================================================
--%>

<html>
<head>
<title>[S.I.E.S.] - Riepilogo delle pene</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>

<script language="JavaScript">
      window.focus();
      
      <% if (request.getParameter("ParentFormName")!=null) { %>
      function insertIT(){
        var parentForm = window.parent.opener.document.<%=request.getParameter("ParentFormName")%>;
        
        <%
        PenaRideterminataCumuloModel lTotaleDaCaricare = aCalcoloPenaModel.getPenaPrincipaleTotNetta();
        %>
        
        //Reclusione
        var checkRec = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_RECLUSIONE_PD%>;
        <% if (lTotaleDaCaricare.isReclusione()) { %>        
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumAnniReclusione(),"&nbsp;")%>;
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_RECLUSIONE %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumMesiReclusione(),"&nbsp;")%>;
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumGiorniReclusione(),"&nbsp;")%>;
        checkRec.checked = true;
        checkRec.fireEvent("onClick");
        <% } else { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_RECLUSIONE %>.value='';
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_RECLUSIONE %>.value='';
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_RECLUSIONE %>.value='';
        checkRec.checked = false;
        checkRec.fireEvent("onClick");
        <% } %>
      
        // Multa
        var checkMulta = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_MULTA_PD%>;

        <% if (lTotaleDaCaricare.isMulta()) { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA%>INT.value="<%=StringUtils.getParteIntera  (lTotaleDaCaricare.getImportoMulta()) %>";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA%>DEC.value="<%=StringUtils.getParteDecimale(lTotaleDaCaricare.getImportoMulta()) %>";
        checkMulta.checked = true;
        <% } else { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA%>INT.value="";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_MULTA%>DEC.value="";
        checkMulta.checked = false;
        <% } %>

        checkMulta.fireEvent("onClick");
       
        
        // Arresto 
        var checkArr = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ARRESTO_PD%>;
        <% if (lTotaleDaCaricare.isArresto()) { %>        
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ARRESTO %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumAnniArresto(),"&nbsp;")%>;
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ARRESTO %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumMesiArresto(),"&nbsp;")%>;
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ARRESTO %>.value=<%=StringUtils.toStringJSP(lTotaleDaCaricare.getNumGiorniArresto(),"&nbsp;")%>;
        checkArr.checked = true;
        <% } else { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ARRESTO %>.value='';
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ARRESTO %>.value='';
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ARRESTO %>.value='';
        checkArr.checked = false;
        <% } %>
        checkArr.fireEvent("onClick");
        
        // Ammenda
        var checkAmmenda = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_AMMENDA_PD%>;

        <% if (lTotaleDaCaricare.isAmmenda()) { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value="<%=StringUtils.getParteIntera  (lTotaleDaCaricare.getImportoAmmenda()) %>";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value="<%=StringUtils.getParteDecimale(lTotaleDaCaricare.getImportoAmmenda()) %>";
        checkAmmenda.checked = true;
        <% } else { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA%>INT.value="";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_IMPORTO_AMMENDA%>DEC.value="";
        checkAmmenda.checked = false;
        <% } %>

        checkAmmenda.fireEvent("onClick");
        
        // Ergastolo
        var checkErgastolo = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_ERGASTOLO_PD%>;
        <% if (aCalcoloPenaModel.getErgastolo()!=null) { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO %>.value="<%=StringUtils.toStringJSP   (aCalcoloPenaModel.getErgastolo().getNumAnniIsolamentoDiurno(),"")%>";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO %>.value="<%=StringUtils.toStringJSP   (aCalcoloPenaModel.getErgastolo().getNumMesiIsolamentoDiurno(),"")%>";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO %>.value="<%=StringUtils.toStringJSP (aCalcoloPenaModel.getErgastolo().getNumGiorniIsolamentoDiurno(),"")%>";
        checkErgastolo.checked = true;
        <% } else { %>
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO %>.value="";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_MESI_ISOLAMENTO_DIURNO %>.value="";
        parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO %>.value="";
        checkErgastolo.checked = false;
        <% } %>
        checkErgastolo.fireEvent("onClick");

        // Liberazione Anticipata
        var doCheckLA = false;
        var checkLA = parentForm.<%=ICostantiPenaRideterminataCumulo.CAMPO_CHECK_LIBANT%>;

        <% if (aCalcoloPenaModel.getTotaliLA("LA",null)>0) { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LA%>.value="<%=aCalcoloPenaModel.getTotaliLA("LA",null)%>";
          doCheckLA = true;
        <% } else { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LA%>.value="";
        <% } %>
        
        <% if (aCalcoloPenaModel.getTotaliLA("LS",null)>0) { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LS%>.value="<%=aCalcoloPenaModel.getTotaliLA("LS",null)%>";
          doCheckLA = true;
        <% } else { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LS%>.value="";
        <% } %>

        <% if (aCalcoloPenaModel.getTotaliLA("LI",null)>0) { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LI%>.value="<%=aCalcoloPenaModel.getTotaliLA("LI",null)%>";
          doCheckLA = true;
        <% } else { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_LI%>.value="";
        <% } %>

        <% if (aCalcoloPenaModel.getTotaliRimedi(null)>0) { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_RIDUZIONE%>.value="<%=aCalcoloPenaModel.getTotaliRimedi(null)%>";
          doCheckLA = true;
        <% } else { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_RIDUZIONE%>.value="";
        <% } %>
       
        <% if (aCalcoloPenaModel.getTotaliScomputi(null)>0) { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_SCOMPUTO%>.value="<%=aCalcoloPenaModel.getTotaliScomputi(null)%>";
          doCheckLA = true;
        <% } else { %>
          parentForm.<%= ICostantiPenaRideterminataCumulo.CAMPO_NUMERO_GIORNI_SCOMPUTO%>.value="";
        <% } %>


        checkLA.checked = doCheckLA;
        checkLA.fireEvent("onClick");
        if (doCheckLA) {
          window.parent.opener.espandi('tabLA');
        }
        
        window.parent.close();

      }
    <% } %>
      
      
      function espandiTutto (aPar)
      {
        if (aPar == '+') {
          espandi('idTabPD','idExpCollPD'); // Pena Principale
          espandi('idTabMC','idExpCollMC'); // Carcerazione sofferta (Misure Cautelari)
          espandi('idTabRMA','idExpCollRMA'); // Revoca Misure Alternative
          espandi('idTabSD','idExpCollSD'); // Sospensioni / Differimenti
          espandi('idTabPP','idExpCollPP');
          espandi('idTabLA','idExpCollLA'); // LA
          espandi('idTabBen','idExpCollBen'); // Benefici in sentenza
          espandi('idTabRichGE','idExpCollRichGE'); // Richieste Concessione Benefici
          espandi('idTabRidPen','idExpCollRidPen'); // Rideterminazione Pena PM Altro
          espandi('idTabRichGERev','idExpCollRichGERev'); // Richieste Revoca Benefici
        }
        else {
          collassa('idTabPD','idExpCollPD'); // Pena Principale
          collassa('idTabMC','idExpCollMC'); // Carcerazione sofferta
          collassa('idTabRMA','idExpCollRMA'); // Revoca Misure Alternative
          collassa('idTabSD','idExpCollSD'); // Sospensioni / Differimenti
          collassa('idTabPP','idExpCollPP');
          collassa('idTabLA','idExpCollLA'); // LA
          collassa('idTabBen','idExpCollBen'); // Benefici in sentenza
          collassa('idTabRichGE','idExpCollRichGE'); // Richieste Concessione Benefici
          collassa('idTabRidPen','idExpCollRidPen'); // Rideterminazione Pena PM Altro
          collassa('idTabRichGERev','idExpCollRichGERev'); // Richieste Revoca Benefici
        }
        
      }      
      
      //============================
      function espandi(idTabella, idTextHref){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";        
        var hrefNew = "Javascript:collassa('"+idTabella+"','"+idTextHref+"');";
        
        $('#'+idTextHref+'').attr('href',hrefNew);
        $('#'+idTextHref+'').children().attr('src',collapseGif);
        $('#'+idTextHref+'').children().attr('alt','Riduci');
        
        
        //var hrefNew = "Javascript:collassa('"+idTabella+"','"+idTextHref+"');";
        //$('#'+idTextHref).attr('href',hrefNew); 
        //$('#'+idTextHref).html('Collassa');
        
        $('#'+idTabella+' tr[id=SI]').show();
        //$('#'+idTabella+' tr[id=SI]').fadeIn();
        
        
        if (idTabella=="idTabMC"){
          $('#tdTipMisura').html('Tipo Misura');
          $('#tdPeriodoDa').html('Periodo dal');
          $('#tdPeriodoA').html('Periodo al');
        }
      }
    
      function collassa(idTabella,idTextHref){
        var collapseGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
        var hrefNew = "Javascript:espandi('"+idTabella+"','"+idTextHref+"');";
        
        $('#'+idTextHref+'').attr('href',hrefNew);
        $('#'+idTextHref+'').children().attr('src',collapseGif);
        $('#'+idTextHref+'').children().attr('alt','Espandi');
        
        //var hrefNew = "Javascript:espandi('"+idTabella+"','"+idTextHref+"');";
        //$('#'+idTextHref).attr('href',hrefNew); 
        //$('#'+idTextHref).html('Espandi');  
        
        $('#'+idTabella+' tr[id=SI]').hide();
        //$('#'+idTabella+' tr[id=SI]').fadeOut()
        
        if (idTabella=="idTabMC"){
          $('#tdTipMisura').html('');
          $('#tdPeriodoDa').html('');
          $('#tdPeriodoA').html('');
        }        
      }

      $(document).ready(function(){
        //espandi('idTabRichGE','idExpCollRichGE'); // Richieste 
        //espandi('idTabRichGERev','idExpCollRichGERev'); // Richieste 
        espandiTutto('+');
      });
      
    </script>
</head>

<body class="corpo">
	<table>
		<tr>
			<td class="LBG"><a href="Javascript:window.print();"><img align="middle"
					src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif"
					alt="Stampa questa videata" border=0></a></td>
			<td class="LBG"><font class="label">Funzione :</font>&nbsp;<font
				class="campo">Riepilogo delle pene &nbsp;</font></td>

			<td class="LBG"><a href="javascript:window.location.reload();">
					<img align="middle" src="/images/bottoni/modifica.gif"
					alt="Aggiorna" width="24" height="24" border="0">
			</a></td>
		</tr>
	</table>

	<br>

	<table cellspacing="2" cellpadding="4" width="800px">
		<tr>
			<td colspan="8">
				<font class='cBlack'>N.B. Di seguito sono riportate : </font>
				<font class='cRosso'>in Rosso le quantità di pena da sommare alla Pena Principale, </font>
				<font class='Label'>in Blu quelle da Detrarre. </font>
			</td>
		</tr>
		
		<tr>
			<td colspan="8"><a href="Javascript:espandiTutto('+');"
				id="idEspandiTutto"><img align="middle" alt="Espandi"
					src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />Espandi
					tutto</a> &nbsp;&nbsp;&nbsp;&nbsp; <a
				href="Javascript:espandiTutto('-');" id="idCollassaTutto"><img align="middle" alt="Comprimi"
					src="<%=IWebConstants.IMAGES_DIR%>collapse.gif" border="0" />Comprimi
					tutto</a></td>
		</tr>
	</table>

	<%--
  ============================================================================== 
   Pena Principali
  ============================================================================== 
  --%>
	<table id="idTabPD" cellspacing="2" cellpadding="4" width="800px">
		<tr>
			<td class="Titolo" colspan="9">Il cumulo delle pene risulta
				essere pari a - Pena Principale &nbsp;<a
				href="Javascript:espandi('idTabPD','idExpCollPD');"
				id="idExpCollPD"><img align="middle" alt="Espandi"
					src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td></td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Multa</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Ammenda</td>
		</tr>

		<% 
    Vector <PenaComplessivaCumuloModel> lListaPeneComplessive;
    lListaPeneComplessive = aCalcoloPenaModel.getListaPeneComplessive();
    String colorStylePP = "style='color: red;'";    	  
    
    for (int i=0; i<lListaPeneComplessive.size(); i++) 
    {
      PenaComplessivaCumuloModel lPenaComp = lListaPeneComplessive.elementAt(i);
      
      String lDescTitolo = null;
      
      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lPenaComp.getTitIdTitoloCumulato());
      
      lDescTitolo = lTitolo.getDescrTipoProvvedimento();
	  //lDescTitolo += " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
	  lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
      
      if (lTitolo.getProcedimentoCumulato()!=null) {
      //    lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
      //                   + "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
          String nSiep = "";
          ProcedimentoCumulatoModel lProcMod = lTitolo.getProcedimentoCumulato();
          if ("S".equals(lProcMod.getFlagAccorpato()) && lProcMod.getUfficioOrigine()!=null ){
            UfficioModel lUfficioOrigine = lProcMod.getUfficioOrigine();
  
            nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrOrigine();
            nSiep += " <font class=\"cRosso\">Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+"</font>"; 
          }
          else {
            nSiep = lProcMod.getChiaveAnnoFasCumulato() +"/"+ lProcMod.getChiaveProgrFasCumulato();
			nSiep += " "+lProcMod.getCodTipoUfficioFasCumulato() + " di "+lProcMod.getDescrLuogoUfficioFasCumulato();
          }
          lDescTitolo += " ("+nSiep+")";

      }

      // Ergastolo
      if (lPenaComp.isErgastolo()){
		// lDescTitolo += "&nbsp;<font class='cRosso'>"+lPenaComp.getDescrTipoPenaDetentiva()+"</font>";

        String lStrErgastolo = lPenaComp.getDescrTipoPenaDetentiva();

        if ("04".equals(lPenaComp.getCodTipoPenaDetentiva())){
          lPenaComp.calcolaStringaIsolamentoCum();
          lStrErgastolo += " ("+lPenaComp.getStringaIsolamentoDiurno()+") ";
          //lDescTitolo += " Anni "+StringUtils.toStringJSP(lPenaComp.getNumAnniIsolamentoDiurno(),"&nbsp;");
          //lDescTitolo += " Mesi "+StringUtils.toStringJSP(lPenaComp.getNumMesiIsolamentoDiurno(),"&nbsp;");
          //lDescTitolo += " Giorni "+StringUtils.toStringJSP(lPenaComp.getNumGiorniIsolamentoDiurno(),"&nbsp;");
        }
        lDescTitolo += "&nbsp;<font class='cRosso'>"+lStrErgastolo+"</font>";

      }
      
      
      String lDescrTitleSentenza = "";
      
      String colorStyle ="";
      String lInfoSS = "";
      if (lPenaComp!=null && lPenaComp.getSanzioneSostitutivaCumulo()!=null
           && !lPenaComp.getSanzioneSostitutivaCumulo().getIsRevocata()      
         ) 
      {
        colorStyle = "style='color: grey;'";
        
        SanzioneSostitutivaCumuloModel lSSModel = lPenaComp.getSanzioneSostitutivaCumulo();
        lSSModel.calcolaStringaSanzione();
        String lInfSSText = "Pena Sostituita con la sanzione sostitutiva "+lSSModel.getStringaSanzione();
        
        lInfoSS = "&nbsp;<img align='absmiddle' src='/images/info.gif' border=0 title='"+StringUtils.encodeHTML(lInfSSText)+"'>";
      }
      // MEV70
      else {
          colorStyle = "style='color: red;'";    	  
      }
      
      
      //TODO evidenziare le PP sostituite
    %>
		<tr style="display: none" id="SI">
			<td class="l" <%=colorStyle%> title="<%=lDescrTitleSentenza%>"><%=lDescTitolo%><%=lInfoSS%></td>

<%-- 			<% if (lPenaComp.isErgastolo() && 1==2){ %> --%>
<%-- 			<td class="r" colspan="3"><font class='cRosso'><%=lPenaComp.getDescrTipoPenaDetentiva()%></font> --%>
<!-- 			</td> -->
<%-- 			<td class="r" <%=colorStyle%>><%=(lPenaComp.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaComp.getImportoMulta()) %></td> --%>
<!-- 			<td class="r" colspan="3">&nbsp;</td> -->
<%-- 			<td class="r" <%=colorStyle%>><%=(lPenaComp.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaComp.getImportoAmmenda()) %></td> --%>

			<%--
      <td class="r" colspan="8">
        <table>
          <tr>
            <td><font class='cRosso'><%=lPenaComp.getDescrTipoPenaDetentiva()%></font></td>
          </tr>
        </table>
      </td>
--%>
<%-- 			<% } else { %> --%>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumGiorniReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=(lPenaComp.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaComp.getImportoMulta()) %></td>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumAnniArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumMesiArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=StringUtils.toStringJSP(lPenaComp.getNumGiorniArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStyle%>><%=(lPenaComp.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaComp.getImportoAmmenda()) %></td>
<%-- 			<% } %> --%>
		</tr>
		<% } %>


		<%
    PenaRideterminataCumuloModel lPenaRideterminataLorda = aCalcoloPenaModel.getPenaPrincipaleTotLorda();
    %>
		<tr id="idTrTotPD">
			<td class="l" <%=colorStylePP%>>Totale Pene Temporanee</td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumGiorniReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=(lPenaRideterminataLorda.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaRideterminataLorda.getImportoMulta()) %></td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumAnniArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumMesiArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=StringUtils.toStringJSP(lPenaRideterminataLorda.getNumGiorniArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStylePP%>> <%=(lPenaRideterminataLorda.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lPenaRideterminataLorda.getImportoAmmenda()) %></td>
		</tr>
		<% 
    if (aCalcoloPenaModel.getErgastolo()!=null) { 
      PenaComplessivaCumuloModel lErgastolo = aCalcoloPenaModel.getErgastolo();
    %>
		<tr id="idTrTotPD">
			<td class="l"><font class="cRosso"><%=lErgastolo.getDescrTipoPenaDetentiva() %></font></td>
			<% if (lErgastolo.isDurataIsolamentoDiurnoZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumAnniIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumMesiIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumGiorniIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } %>
		</tr>
		<% } %>


	</table>

	<%--
  ============================================================================== 
  ==  Periodi di carcerazione sofferti
  ==   - Inserire le MISURE_CAUTELARI_CUMULO
  ==   - Inserire i Provvediment di Presofferto e Fungibilità
  ==   - Inserire l'Espiato
  ============================================================================== 
  --%>
	<br>
	<table id="idTabMC" cellspacing="2" cellpadding="4" width="800px">
		<tr>
			<td class="Titolo" colspan="7">Dedotti i Periodi di carcerazione sofferti &nbsp;
				<a href="Javascript:espandi('idTabMC','idExpCollMC');" id="idExpCollMC"><img align="middle" alt="Espandi"
					src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
				</a>
			</td>
		</tr>


		<tr>
			<td class="int" id="tdTipMisura"></td>
			<td class="int" id="tdPeriodoDa"></td>
			<td class="int" id="tdPeriodoA"></td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<!--td class="int">Tot Giorni</td-->
		</tr>
		<% 
    Vector <MisuraCautelareCumuloModel> lListaMisureCautelari;
    lListaMisureCautelari = aCalcoloPenaModel.getListaMisureCautelari();
    
    for (int i=0; i<lListaMisureCautelari.size(); i++) 
    {
      MisuraCautelareCumuloModel lMCCumuloModel = (MisuraCautelareCumuloModel) lListaMisureCautelari.elementAt(i);
      
      String lTotGGMessaAllaProva = "";
      if ("CL".equals(lMCCumuloModel.getCodTipoMisura())) {
        lTotGGMessaAllaProva = " (totale giorni "+StringUtils.toStringJSP(lMCCumuloModel.getGiorni(),"&nbsp;")+")";
      }
      
      
      %>
		<tr style="display: none" id="SI">
			<td class="l"><%=StringUtils.toStringJSP(lMCCumuloModel.getDescrTipoMisura(),"&nbsp;")%>
				<%=lTotGGMessaAllaProva%></td>
			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMCCumuloModel.getDataInizio(),"dd-MM-yyyy"),"")%>
			</td>
			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMCCumuloModel.getDataFine(),"dd-MM-yyyy"),"")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lMCCumuloModel.getNumAnni(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lMCCumuloModel.getNumMesi(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lMCCumuloModel.getNumGiorni(),"&nbsp;")%></td>
			<%-- td class="c"><%=StringUtils.toStringJSP(lMCCumuloModel.getGiorni(),"&nbsp;")%></td--%>
		</tr>

		<%
    }
    %>

	<%
    //==========================================================================
    // Computi disposti con provvedimento    
    //==========================================================================
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvComputo = aCalcoloPenaModel.getProvvComputi();
    
    for (StatoEsecTitoloCumulatoModel lProvvedimento:lListaProvvComputo) {
      String lDescrComputo = "(P) "; // Con provedimento
      
      String lDescrProvv = "";
      lDescrProvv += "con "+lProvvedimento.getDescrTipoProvvedimento()
                   +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lProvvedimento.getDataEmissione(),"dd-MM-yyyy"));
      
      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato (lProvvedimento.getTitIdTitoloCumulato());
      
      String lDescTitolo = lTitolo.getDescrTipoProvvedimento() + " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();

      ProcedimentoCumulatoModel lProcedimentoCumulato = lTitolo.getProcedimentoCumulato();

      if (lProcedimentoCumulato!=null){
        lDescTitolo += " - "+lProcedimentoCumulato.getChiaveAnnoFasCumulato() + "/"+ lProcedimentoCumulato.getChiaveProgrFasCumulato();
        lDescTitolo += "  "+lProcedimentoCumulato.getCodTipoUfficioFasCumulato() + "/"+ lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato();
      }

      Vector <ComputiCumuloModel> lListaComputi = lProvvedimento.getListaComputi();
      String lColorRevocati = "";
      
      int contaComputo = 0;
      for (ComputiCumuloModel lComputo : lListaComputi) 
      {
        contaComputo++;
        if (contaComputo==1) 
          lDescrComputo += lComputo.getDescrTipoAnnotazione();
        else 
          lDescrComputo = "";
        %>
		<tr style="display: none" id="SI">
			<td class="l" Title="<%=lDescrProvv+" ["+lDescTitolo+"]"%> "><%=lDescrComputo%></td>

			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"),"")%>
			</td>
			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"),"")%></td>

			<td class="c" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="c" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="c" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"&nbsp;")%></td>
		</tr>
		<% } %>
		<% } %>

		<%
    //==========================================================================
    //  AGGIUNGERE ESPIATO
    //==========================================================================
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvEspiato = aCalcoloPenaModel.getProvvEspiato();
    
    for (StatoEsecTitoloCumulatoModel lProvvedimento: lListaProvvEspiato) {
      String lDescrComputo = "(P) "; // Con provedimento
      
      String lDescrProvv = "";
//      lDescrProvv += "con "+lProvvedimento.getDescrTipoProvvedimento()
      lDescrProvv += " "+lProvvedimento.getDescrMotivo()
                   +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lProvvedimento.getDataEmissione(),"dd-MM-yyyy"));
      
      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato (lProvvedimento.getTitIdTitoloCumulato());
      
      String lDescTitolo = lTitolo.getDescrTipoProvvedimento() + " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();

      
      if (lProvvedimento.getListaComputi()!=null && lProvvedimento.getListaComputi().size()==1) {
        ComputiCumuloModel lComputo = (ComputiCumuloModel) lProvvedimento.getListaComputi().elementAt(0);
        
        //lDescrComputo += lComputo.getDescrTipoAnnotazione();
        lDescrComputo += lProvvedimento.getDescrMotivo();
      
        // Escludo i computi senza Quantum (legati a interruzioni del GE)
        if (!lComputo.isQuantumReclusioneZero() || !lComputo.isQuantumArrestoZero() ) {
        %>
		<tr style="display: none" id="SI">
			<td class="l" Title="<%=lDescrProvv+" ["+lDescTitolo+"]"%> "><%=lDescrComputo%></td>

			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneDa(),"dd-MM-yyyy"),"")%>
			</td>
			<td class="r"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lComputo.getDataReclusioneA(),"dd-MM-yyyy"),"")%></td>

			<td class="c"><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"&nbsp;")%></td>
		</tr>
		<% } %>
	  <% } %>
	<% } %>


	<%
    MisuraCautelareCumuloModel lTotMisCautModel = aCalcoloPenaModel.getMisureCautelariTotali();
    %>
		<tr>
			<td class="r" colspan="3" width="650px">Per un totale di</td>
			<td class="c"><%=StringUtils.toStringJSP(lTotMisCautModel.getNumAnni(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lTotMisCautModel.getNumMesi(),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(lTotMisCautModel.getNumGiorni(),"&nbsp;")%></td>
		</tr>
	</table>

  <%--
  =============================================================================== 
  ==  MEV70 - Periodi di carcerazione espiati a seguito revoca Misura Alternativa
  =============================================================================== 
  --%>
  	<% 
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvEspiatoRevocaMA = aCalcoloPenaModel.getProvvEspiatoRevocaMA();
  	if (lListaProvvEspiatoRevocaMA.size()>0) {
  	%>
		<br>
		<table id="idTabRMA" cellspacing="2" cellpadding="4" width="800px">
			<tr>
				<td class="Titolo" colspan="7">Dedotti i Periodi di carcerazione sofferti a seguito revoca di Misure Alternative&nbsp;
					<a href="Javascript:espandi('idTabRMA','idExpCollRMA');" id="idExpCollRMA"><img align="middle" alt="Espandi"
						src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
					</a>
				</td>
			</tr>
	
			<tr>
				<td class="c" colspan="3" width="600px">&nbsp;</td>
				<td class="int">Anni</td>
				<td class="int">Mesi</td>
				<td class="int">Giorni</td>
			</tr>
	
		<%

	    for (int i=0; i<lListaProvvEspiatoRevocaMA.size(); i++) 
	    {    
	    	StatoEsecTitoloCumulatoModel lRevocaMA = lListaProvvEspiatoRevocaMA.elementAt(i);
	    	Vector <ComputiCumuloModel> lComputiRevMA = lRevocaMA.getListaComputi();
	    	for (int j=0; j<lComputiRevMA.size(); j++)
	        {    
				ComputiCumuloModel lComputoRevMA = lComputiRevMA.elementAt(j);
	    		String lColorRevocati ="";
				String lTextRevocati ="";
				if ("+".equals(lComputoRevMA.getFlagPiuMeno())) {
	        		lColorRevocati = "style='color:red'";
	        		lTextRevocati = " <font class='cRosso'>(Revoca)</font>";
	      		}
	      
	      		TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lRevocaMA.getTitIdTitoloCumulato());
	      
	      		String lDescTitolo = "";
      			lDescTitolo = lTitolo.getDescrTipoProvvedimento();
	      		lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
	      		lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
	      		if (lTitolo.getProcedimentoCumulato()!=null) {
	        		lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
	                       		+ "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
	      		}

	      		String lDescrProvv = "";
	            lDescrProvv += "con "+DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(), lRevocaMA.getCodTipoProvvedimento()) 
	                         +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lComputoRevMA.getDataEmissioneProvv(),"dd-MM-yyyy"));
	            lDescrProvv += " - "+StringUtils.toStringJSP(lRevocaMA.getDescrMotivo());
	            lDescrProvv += " - "+StringUtils.toStringJSP(lComputoRevMA.getDescUfficioEmittenteProvv()) +" di "+StringUtils.toStringJSP(lRevocaMA.getDescrLuogoEmittente());
	            
	    %>
				<tr>
					<td class="l" colspan="3" <%=lColorRevocati%> title="<%=lDescrProvv%>">
						<%=lTextRevocati%><%=lDescTitolo%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoRevMA.getNumAnniReclusione(),"&nbsp;")%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoRevMA.getNumMesiReclusione(),"&nbsp;")%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoRevMA.getNumGiorniReclusione(),"&nbsp;")%></td>
				</tr>
		<%
		    } // end for computi
	    } // end for Provvedimenti Revoca MA

	    // Totale Computi di Revoca MA
	    CalendarModel lTotReclusioneRMA = aCalcoloPenaModel.getRevocaMATotali();
	    %>
			<tr>
				<td class="r" colspan="3">Per un totale di</td>
				<% if (lTotReclusioneRMA.isQuantumZero() ) {%>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<% } else { %>
				<td class="r"><%=lTotReclusioneRMA.getNumAnni()%></td>
				<td class="r"><%=lTotReclusioneRMA.getNumMesi()%></td>
				<td class="r"><%=lTotReclusioneRMA.getNumGiorni()%></td>
				<% } %>
			</tr>
		</table>

	<% } // end if	%>

  <%--
  =============================================================================== 
  ==  MEV70 - Periodi di sospensione/differimento Pena.
  =============================================================================== 
  --%>
  	<% 
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvSospDiff = aCalcoloPenaModel.getProvvSospDiff();
  	if (lListaProvvSospDiff.size()>0) {
  	%>
		<br>
		<table id="idTabSD" cellspacing="2" cellpadding="4" width="800px">
			<tr>
				<td class="Titolo" colspan="7">Dedotti i Periodi di carcerazione sofferti a seguito Sospensione / Differimento della Pena&nbsp;
					<a href="Javascript:espandi('idTabSD','idExpCollSD');" id="idExpCollSD"><img align="middle" alt="Espandi"
						src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
					</a>
				</td>
			</tr>
	
			<tr>
				<td class="c" colspan="3" width="600px">&nbsp;</td>
				<td class="int">Anni</td>
				<td class="int">Mesi</td>
				<td class="int">Giorni</td>
			</tr>
	
		<%

	    for (int i=0; i<lListaProvvSospDiff.size(); i++) 
	    {    
	    	StatoEsecTitoloCumulatoModel lSospDiff = lListaProvvSospDiff.elementAt(i);
	    	Vector <ComputiCumuloModel> lComputiSD = lSospDiff.getListaComputi();
	    	for (int j=0; j<lComputiSD.size(); j++)
	      {    
          ComputiCumuloModel lComputoSD = lComputiSD.elementAt(j);

          // add DF 07/05/2019
          if (!lComputoSD.isQuantumReclusioneZero() || !lComputoSD.isQuantumArrestoZero() ) {


	    		String lColorRevocati ="";
				String lTextRevocati ="";
				if ("+".equals(lComputoSD.getFlagPiuMeno())) {
	        		lColorRevocati = "style='color:red'";
	        		lTextRevocati = " <font class='cRosso'>(Revoca)</font>";
	      		}
	      
	      		TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lComputoSD.getTitIdTitoloCumulato());
	      
	      		String lDescTitolo = "";
      			lDescTitolo = lTitolo.getDescrTipoProvvedimento();
	      		lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
	      		lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
	      		if (lTitolo.getProcedimentoCumulato()!=null) {
	        		lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
	                       		+ "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
	      		}

	      		String lDescrProvv = "";
	            lDescrProvv += "con "+DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(), lSospDiff.getCodTipoProvvedimento()) 
	                         +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lComputoSD.getDataEmissioneProvv(),"dd-MM-yyyy"));
	            lDescrProvv += " - "+StringUtils.toStringJSP(lSospDiff.getDescrMotivo());
	            lDescrProvv += " - "+StringUtils.toStringJSP(lComputoSD.getDescUfficioEmittenteProvv()) +" di "+StringUtils.toStringJSP(lSospDiff.getDescrLuogoEmittente());
	            
	    %>
				<tr>
					<td class="l" colspan="3" <%=lColorRevocati%> title="<%=lDescrProvv%>">
						<%=lTextRevocati%><%=lDescTitolo%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoSD.getNumAnniReclusione(),"&nbsp;")%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoSD.getNumMesiReclusione(),"&nbsp;")%></td>
					<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputoSD.getNumGiorniReclusione(),"&nbsp;")%></td>
				</tr>
		<%
          } // end if computi nulli
		    } // end for computi
	    } // end for Provvedimenti di Sospensione / Differimento della Pena.

	    // Totale Computi di Sospensione / Differimento pena.
	    CalendarModel lTotSospDiff = aCalcoloPenaModel.getSospDiffTotali();
	    %>
			<tr>
				<td class="r" colspan="3">Per un totale di</td>
				<% if (lTotSospDiff.isQuantumZero() ) {%>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<% } else { %>
				<td class="r"><%=lTotSospDiff.getNumAnni()%></td>
				<td class="r"><%=lTotSospDiff.getNumMesi()%></td>
				<td class="r"><%=lTotSospDiff.getNumGiorni()%></td>
				<% } %>
			</tr>
		</table>

	<% } // end if	%>

  <%--
  ============================================================================== 
  ==  Benefici
  ==  - Amnistia Indulto concessi in sentenza
  ==  - Indulto revocato in sentenza
  ==  - Amnistia / Indulto / Depenalizzazione concessi con provvedimento 
  ============================================================================== 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px" id="idTabBen">
		<tr>
			<td class="Titolo" colspan="9">Dedotti i benefici &nbsp;
				<a href="Javascript:espandi('idTabBen','idExpCollBen');" id="idExpCollBen">
					<img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
				</a>
			</td>
		</tr>
		<tr>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td class="int">Beneficio</td>
			<td class="int" nowrap>Anni</td>
			<td class="int" nowrap>Mesi</td>
			<td class="int" nowrap>Giorni</td>
			<td class="int" nowrap>Multa</td>
			<td class="int" nowrap>Anni</td>
			<td class="int" nowrap>Mesi</td>
			<td class="int" nowrap>Giorni</td>
			<td class="int" nowrap>Ammenda</td>
		</tr>
		<% 
    Vector <BeneficioCumuloModel> lListaBenefici;
    lListaBenefici = aCalcoloPenaModel.getListaBenefici();
    
    for (int i=0; i<lListaBenefici.size(); i++) 
    {    
      BeneficioCumuloModel lBeneficio =  lListaBenefici.elementAt(i);
     
if (   (lBeneficio.isQuantumReclusioneZero())
    && (lBeneficio.getImportoMulta()==null || (lBeneficio.getImportoMulta()!=null && lBeneficio.getImportoMulta().compareTo(new BigDecimal(0))==0) )
    && (lBeneficio.isQuantumArrestoZero())
    && (lBeneficio.getImportoAmmenda()==null || (lBeneficio.getImportoAmmenda()!=null && lBeneficio.getImportoAmmenda().compareTo(new BigDecimal(0))==0 ))
   )
{
  continue;  // salto il record non ha dati significativi. Forse condono della sola PA
}

      String lColorRevocati ="";
      String lTextRevocati ="";
      if ("R".equals(lBeneficio.getCodNaturaBeneficio())) {
        lColorRevocati = "style='color:red'";
        lTextRevocati = " <font class='cRosso'>(Revoca)</font>";
      }
      
      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lBeneficio.getTitIdTitoloCumulato());
      
      String lDescTitolo = lTitolo.getDescrTipoProvvedimento();
//+ " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
      //lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();

      lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
      
      if (lTitolo.getProcedimentoCumulato()!=null) {
        lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
                       + "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
      }

    %>
		<tr style="display: none" id="SI">
			<td class="l" <%=lColorRevocati%> title="<%=lDescTitolo%>"><%=StringUtils.toStringJSP(lBeneficio.getDescrTipoBeneficio(),"&nbsp;")%>&nbsp;<%=StringUtils.toStringJSP(lBeneficio.getDescrDpr(),"&nbsp;")%>
				<%=lTextRevocati%><%=" - "+lDescTitolo%></td>
			<% if (lBeneficio.isQuantumReclusioneZero()) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumGiorniReclusione(),"&nbsp;")%></td>
			<% } %>

			<% if (lBeneficio.isQuantumReclusioneZero() && (lBeneficio.getImportoMulta()==null || lBeneficio.getImportoMulta().compareTo(new BigDecimal(0))==0 )) { %>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lBeneficio.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lBeneficio.getImportoMulta()) %></td>
			<% } %>


			<% if (lBeneficio.isQuantumArrestoZero()) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumAnniArresto(),"&nbsp;")%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumMesiArresto(),"&nbsp;")%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=StringUtils.toStringJSP(lBeneficio.getNumGiorniArresto(),"&nbsp;")%></td>
			<% } %>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lBeneficio.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lBeneficio.getImportoAmmenda()) %></td>
		</tr>
		<%
    } // end for sui benefici
    %>

		<%
    //==========================================================================
    // Benefici disposti/revocati con provvedimento    
    //==========================================================================
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvBenefici = aCalcoloPenaModel.getProvvBenefici();
    
    for (StatoEsecTitoloCumulatoModel lProvvedimento:lListaProvvBenefici) {
      String lDescrBeneficio = "(P) "; // Con provedimento
      
      String lDescrProvv = "";
      lDescrProvv += "con "+lProvvedimento.getDescrTipoProvvedimento()
                   +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lProvvedimento.getDataEmissione(),"dd-MM-yyyy"));
      
      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato (lProvvedimento.getTitIdTitoloCumulato());

      
      String lDescTitolo = lTitolo.getDescrTipoProvvedimento() + " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();

      Vector <ComputiCumuloModel> lListaComputi = lProvvedimento.getListaComputi();
      
      for (ComputiCumuloModel lComputo : lListaComputi) 
      {      
        String lColorRevocati = "";
        String lSegno = "";
        if ("+".equals(lComputo.getFlagPiuMeno())) {
          lColorRevocati = "style='color:red'";
          lSegno = "-";
        }      
      
        lDescrBeneficio += lComputo.getDescrTipoAnnotazione();
        
        if (   "002".equals(lComputo.getCodTipoAnnotazione()) // Amnistia
            || "003".equals(lComputo.getCodTipoAnnotazione()) // Indulto
           ) 
        {
          lDescrBeneficio +=" "+ lComputo.getDescDpr() ;
        }
        else if ("013".equals(lComputo.getCodTipoAnnotazione())){
          lDescrBeneficio +=" - Sentenza CC "+ lComputo.getAnnoSentenza()+"/"+lComputo.getNumeroSentenza()
                           +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lComputo.getDataSentenza(),"dd-MM-yyyy"));
        }
        else if ("004".equals(lComputo.getCodTipoAnnotazione()) || "017".equals(lComputo.getCodTipoAnnotazione())){
          lDescrBeneficio +=" - "+ lComputo.getDescrFonte()+" Anno "+lComputo.getAnnoFonte()+" Num. "+lComputo.getNumeroFonte();
        }
   
    %>
		<tr style="display: none" id="SI">
			<td class="l" Title="<%=lDescrProvv+" ["+lDescTitolo+"]"%> "
				<%=lColorRevocati%>><%=lDescrBeneficio%></td>

			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumAnniReclusione()==null||lComputo.getNumAnniReclusione().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumAnniReclusione())%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumMesiReclusione()==null||lComputo.getNumMesiReclusione().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumMesiReclusione())%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumGiorniReclusione()==null||lComputo.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumGiorniReclusione())%></td>

			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.isMultaZero()) ? "&nbsp;":lSegno+StringUtils.toEuroFormat(lComputo.getImportoMulta()) %></td>

			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumAnniArresto()==null||lComputo.getNumAnniArresto().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumAnniArresto())%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumMesiArresto()==null||lComputo.getNumMesiArresto().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumMesiArresto())%></td>
			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.getNumGiorniArresto()==null||lComputo.getNumGiorniArresto().compareTo(new BigDecimal(0))==0)?"&nbsp;":(lSegno+lComputo.getNumGiorniArresto())%></td>

			<td class="r" nowrap <%=lColorRevocati%>><%=(lComputo.isAmmendaZero()) ? "&nbsp;":lSegno+StringUtils.toEuroFormat(lComputo.getImportoAmmenda()) %></td>
		</tr>
		<% } // enf for computi %>
		<% } // end for provvedimenti %>


		<%
    // Totale Benefici
    CalendarModel lTotReclusione = aCalcoloPenaModel.getBeneficiTotali("R");
    CalendarModel lTotArresto = aCalcoloPenaModel.getBeneficiTotali("A");
    
    String colorStyleTotaliBenRec = "";
    String colorStyleTotaliBenArr = "";
    CalendarUtil lCalUtilBen = new CalendarUtil();
    if (!lCalUtilBen.isPositiveTime(lTotReclusione)) {
      colorStyleTotaliBenRec = "style='color: red;'";
    }
    if (!lCalUtilBen.isPositiveTime(lTotArresto)) {
      colorStyleTotaliBenArr = "style='color: red;'";
    }

    //CalendarModel lTotReclusioneBenToView = lCalUtilRP.abs(lTotReclusioneRP);
    lTotReclusione = lCalUtilBen.ricalcolaGAM (lTotReclusione);

    //CalendarModel lTotArrestoRPToView = lCalUtilRP.abs(lTotArrestoRP);
    lTotArresto = lCalUtilBen.ricalcolaGAM (lTotArresto);
    %>

		<tr>
			<td class="r" colspan="1">Per un totale di</td>
			<% if (lTotReclusione.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r" nowrap <%=colorStyleTotaliBenRec%> ><%=lTotReclusione.getNumAnni()%></td>
			<td class="r" nowrap <%=colorStyleTotaliBenRec%> ><%=lTotReclusione.getNumMesi()%></td>
			<td class="r" nowrap <%=colorStyleTotaliBenRec%> ><%=lTotReclusione.getNumGiorni()%></td>
			<% } %>
      
      <% if (lTotReclusione.getImportoMulta()<0) { %>
      <td class="r" nowrap style='color: red;'><%=StringUtils.toEuroFormat(new BigDecimal (lTotReclusione.getImportoMulta())) %></td>
      <% } else {%>
      <td class="r" nowrap><%=StringUtils.toEuroFormat(new BigDecimal (lTotReclusione.getImportoMulta()))%></td>
      <% } %>
      

			<% if (lTotArresto.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r" nowrap <%=colorStyleTotaliBenArr%> ><%=lTotArresto.getNumAnni()%></td>
			<td class="r" nowrap <%=colorStyleTotaliBenArr%> ><%=lTotArresto.getNumMesi()%></td>
			<td class="r" nowrap <%=colorStyleTotaliBenArr%> ><%=lTotArresto.getNumGiorni()%></td>
			<% } %>
      
      
      <% if (lTotArresto.getImportoAmmenda()<0) { %>
      <td class="r" nowrap style='color: red;'><%=StringUtils.toEuroFormat(new BigDecimal (lTotArresto.getImportoAmmenda())) %></td>
      <% } else {%>
      <td class="r" nowrap><%=StringUtils.toEuroFormat(new BigDecimal (lTotArresto.getImportoAmmenda()))%></td>
      <% } %>
		</tr>
	</table>

	<%--
  //============================================================================ 
  //==   Pagamenti PP
  //============================================================================ 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px" id="idTabPP">
		<tr>
			<td class="Titolo" colspan="3">Dedotte le somme già pagate (multa/ammenda) &nbsp;
				<a href="Javascript:espandi('idTabPP','idExpCollPP');" id="idExpCollPP">
					<img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td class="c">&nbsp;</td>
			<td class="int" width="150px">Multa</td>
			<td class="int" width="150px">Ammenda</td>
		</tr>
		<%  
  Vector <StatoEsecTitoloCumulatoModel> lListaProvvPP = aCalcoloPenaModel.getProvvPagamentoPP();
  CalendarModel lTotPagamentiPP = aCalcoloPenaModel.getPagamentoPPTotale();
  
  for (StatoEsecTitoloCumulatoModel lProvvedimentoPP:lListaProvvPP) {  
  
    TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lProvvedimentoPP.getTitIdTitoloCumulato());
    
    String lDescTitolo = lTitolo.getDescrTipoProvvedimento() + " N° " + lTitolo.getAnnoSentenza() + "/" + lTitolo.getNumeroSentenza();
    lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
  
    ProcedimentoCumulatoModel lProcedimentoCumulato = lTitolo.getProcedimentoCumulato();

    if (lProcedimentoCumulato!=null){
      lDescTitolo += " - "+lProcedimentoCumulato.getChiaveAnnoFasCumulato() + "/"+ lProcedimentoCumulato.getChiaveProgrFasCumulato();
      lDescTitolo += "  "+lProcedimentoCumulato.getCodTipoUfficioFasCumulato() + "/"+ lProcedimentoCumulato.getDescrLuogoUfficioFasCumulato();
    }
        
    Vector <ComputiCumuloModel> lListaComputi = lProvvedimentoPP.getListaComputi();
    
    int conta = 0;
    for (ComputiCumuloModel lComputo : lListaComputi) 
    { 
      conta++;
    %>
		<tr style="display: none" id="SI">
			<% if (conta==1) {%>
			<td class="l" title="<%=lDescTitolo%>">Annotazione avvenuto
				pagamento di Pena Pecuniaria in data <%=DateUtils.getDateToString(lProvvedimentoPP.getDataEmissione(),"dd-MM-yyyy")%></td>
			<% } else {%>
			<td class="l"></td>
			<% } %>
			<td class="r"><%=(lComputo.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lComputo.getImportoMulta()) %></td>
			<td class="r"><%=(lComputo.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lComputo.getImportoAmmenda()) %></td>
		</tr>
		<%
    }
  }  
  %>
		<tr>
			<td class="r" colspan="1">Per un totale di</td>
			<td class="r"><%=(lTotPagamentiPP.getImportoMulta()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal (lTotPagamentiPP.getImportoMulta())) %></td>
			<td class="r"><%=(lTotPagamentiPP.getImportoAmmenda()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal (lTotPagamentiPP.getImportoAmmenda())) %></td>
		</tr>
	</table>

<!-- MEV70 - Inizio Ridet Pena PM Altro -->

  <%--
  ============================================================================== 
  ==  Rideterminazione Pena PM Altro
  
  ==  - Ridet Pena dell'Ufficio
  ==  - Ridet Pena Altra Autorità
  ==  - Ridet Pena del GE 
  ==  - Ridet Pena della Sorveglianza 
  ============================================================================== 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px" id="idTabRidPen">
		<tr>
			<td class="Titolo" colspan="9">Quantum di pena a seguito di rideterminazione pena - altro &nbsp;
				<a href="Javascript:espandi('idTabRidPen','idExpCollRidPen');" id="idExpCollRidPen">
					<img align="middle" alt="Espandi" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
				</a>
			</td>
		</tr>
		<tr>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td class="int">Rideterminazione Pena</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Multa</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Ammenda</td>
		</tr>
	<% 
    //Vector <BeneficioCumuloModel> lListaBenefici;
    //lListaBenefici = aCalcoloPenaModel.getListaBenefici();
	Vector <StatoEsecTitoloCumulatoModel> lListaRidetPenaPMAltro = aCalcoloPenaModel.getRidetPenaPMAltro();
    
    for (int i=0; i<lListaRidetPenaPMAltro.size(); i++) 
    {    
    	StatoEsecTitoloCumulatoModel lRidetPena = lListaRidetPenaPMAltro.elementAt(i);
    	Vector <ComputiCumuloModel> lComputi = lRidetPena.getListaComputi();
    	for (int j=0; j<lComputi.size(); j++)
        {    
			ComputiCumuloModel lComputo = lComputi.elementAt(j);
    		String lColorRevocati ="";
			String lTextRevocati ="";
			if ("+".equals(lComputo.getFlagPiuMeno())) {
        		lColorRevocati = "style='color:red'";
        		lTextRevocati = " <font class='cRosso'></font>";
      		}
      
      		TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lRidetPena.getTitIdTitoloCumulato());

      		String lDescTitolo = "";
  			lDescTitolo = lTitolo.getDescrTipoProvvedimento();
      		lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
      		lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
      		if (lTitolo.getProcedimentoCumulato()!=null) {
        		lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
                       		+ "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
      		}

      		String lDescrProvv = "";
            lDescrProvv += "con "+DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(), lComputo.getCodTipoProvv()) 
	                    +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lComputo.getDataEmissioneProvv(),"dd-MM-yyyy"));
	       lDescrProvv += " - "+StringUtils.toStringJSP(lRidetPena.getDescrMotivo());
	       lDescrProvv += " - "+StringUtils.toStringJSP(lComputo.getDescUfficioEmittenteProvv()) +" di "+StringUtils.toStringJSP(lComputo.getDescluogoUfficioEmittenteProvv());
            
    %>
			<tr style="display: none" id="SI">
				<td class="l" <%=lColorRevocati%> title="<%=lDescrProvv%>">
					<%=lTextRevocati%><%=lDescTitolo%></td>
				<% if (lComputo.isQuantumReclusioneZero()) { %>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<% } else {%>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumAnniReclusione(),"&nbsp;")%></td>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumMesiReclusione(),"&nbsp;")%></td>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumGiorniReclusione(),"&nbsp;")%></td>
				<% } %>
	
				<% if (lComputo.isQuantumReclusioneZero() && (lComputo.getImportoMulta()==null || lComputo.getImportoMulta().compareTo(new BigDecimal(0))==0 )) { %>
				<td class="r">&nbsp;</td>
				<% } else {%>
				<td class="r" <%=lColorRevocati%>><%=(lComputo.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lComputo.getImportoMulta()) %></td>
				<% } %>
	
	
				<% if (lComputo.isQuantumArrestoZero()) { %>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<td class="r">&nbsp;</td>
				<% } else {%>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumAnniArresto(),"&nbsp;")%></td>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumMesiArresto(),"&nbsp;")%></td>
				<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lComputo.getNumGiorniArresto(),"&nbsp;")%></td>
				<% } %>
				<td class="r" <%=lColorRevocati%>><%=(lComputo.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lComputo.getImportoAmmenda()) %></td>
			</tr>
			<%
	    } // end for computi
    } // end for Provvedimenti Rideterminazione Pena

    // Totale Computi Rideterminazione Pena
    CalendarModel lTotReclusioneRP = aCalcoloPenaModel.getRidetPenaPMAltroTotali("R");
    CalendarModel lTotArrestoRP = aCalcoloPenaModel.getRidetPenaPMAltroTotali("A");

  String colorStyleTotaliRPRec = "";
  String colorStyleTotaliRPArr = "";
  CalendarUtil lCalUtilRP = new CalendarUtil();
  if (lCalUtilRP.isPositiveTime(lTotReclusioneRP)) {
    colorStyleTotaliRPRec = "style='color: red;'";
  }
  if (lCalUtilRP.isPositiveTime(lTotArrestoRP)) {
    colorStyleTotaliRPArr = "style='color: red;'";
  }

  CalendarModel lTotReclusioneRPToView = lCalUtilRP.abs(lTotReclusioneRP);
  lTotReclusioneRPToView = lCalUtilRP.ricalcolaGAM (lTotReclusioneRPToView);

  CalendarModel lTotArrestoRPToView = lCalUtilRP.abs(lTotArrestoRP);
  lTotArrestoRPToView = lCalUtilRP.ricalcolaGAM (lTotArrestoRPToView);
  
    %>
		<tr>
			<td class="r" colspan="1">Per un totale di</td>
			<% if (lTotReclusioneRP.isQuantumZero() ) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %> 
			<td class="r" <%=colorStyleTotaliRPRec%> ><%=lTotReclusioneRPToView.getNumAnni()%></td>
			<td class="r" <%=colorStyleTotaliRPRec%> ><%=lTotReclusioneRPToView.getNumMesi()%></td>
			<td class="r" <%=colorStyleTotaliRPRec%> ><%=lTotReclusioneRPToView.getNumGiorni()%></td>
			<% } %>
      
      <% // Multa > 0 è in aumento, rossa %>
      <% if (lTotReclusioneRP.getImportoMulta()>0) {%>
			<td class="r" style='color: red;'>
        <%= StringUtils.toEuroFormat(new BigDecimal (lTotReclusioneRP.getImportoMulta()))%>
      </td>     
      <% } else { %>
			<td class="r"><%=(lTotReclusioneRP.getImportoMulta()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotReclusioneRP.getImportoMulta()).abs())%></td>
      <% } %>
      
      
			<% if (lTotArrestoRP.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r" <%=colorStyleTotaliRPArr%> ><%=lTotArrestoRP.getNumAnni()%></td>
			<td class="r" <%=colorStyleTotaliRPArr%> ><%=lTotArrestoRP.getNumMesi()%></td>
			<td class="r" <%=colorStyleTotaliRPArr%> ><%=lTotArrestoRP.getNumGiorni()%></td>
			<% } %>
      
      
      <% // Ammenda > 0 è in aumento, rossa %>
      <% if (lTotArrestoRP.getImportoAmmenda()>0) {%>
			<td class="r" style='color: red;'>
        <%= StringUtils.toEuroFormat(new BigDecimal (lTotArrestoRP.getImportoAmmenda()))%>
      </td>     
      <% } else { %>
			<td class="r"><%=(lTotArrestoRP.getImportoAmmenda()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotArrestoRP.getImportoAmmenda()).abs())%></td>
      <% } %>
      
		</tr>
	</table>


<!-- Fine RidetPena PM Altro -->

<%--
  ============================================================================== 
  ==   Liberazione Anticipata
  ============================================================================== 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px" id="idTabLA">
		<tr>
			<td class="Titolo" colspan="6">Considerata la liberazione anticipata/Rimedi risarcitori/Scomputi Permessi &nbsp;
				<a href="Javascript:espandi('idTabLA','idExpCollLA');" id="idExpCollLA">
					<img align="middle" alt="Espandi"src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" />
				</a>
			</td>
		</tr>

		<tr>
			<td></td>
			<td class="int" style="width: 70px">Ordinaria</td>
			<td class="int" style="width: 70px">Speciale</td>
			<td class="int" style="width: 100px">Integrazione</td>
			<td class="int" style="width: 100px">D.L.92/2014</td>
			<td class="int" style="width: 70px">Scomputi</td>
		</tr>

	<%    
    Vector <StatoEsecTitoloCumulatoModel> lListaProvvLA = aCalcoloPenaModel.getProvvLADL92();
    for (StatoEsecTitoloCumulatoModel lStatEsecModel:lListaProvvLA) {
	      TitoloCumulatoModel lTitolo = aCalcoloPenaModel.getTitoloCumulato(lStatEsecModel.getTitIdTitoloCumulato());
	      
	      String lDescTitolo = lTitolo.getDescrTipoProvvedimento();
	      lDescTitolo += " del " +  DateUtils.getDateToString( lTitolo.getDataProvvedimento(),"dd-MM-yyyy");
	      lDescTitolo += " - "+lTitolo.getCodTipoAutoritaEmittente() + " "+ lTitolo.getDescrLuogoEmittente();
	      
	      if (lTitolo.getProcedimentoCumulato()!=null) {
	        lDescTitolo += " ("+lTitolo.getProcedimentoCumulato().getChiaveAnnoFasCumulato() 
	                       + "/"+ lTitolo.getProcedimentoCumulato().getChiaveProgrFasCumulato()+" Siep)";
	      }
	
	      String lDescrProvv = "";
	      lDescrProvv += "con "+lStatEsecModel.getDescrTipoProvvedimento()
	                   +" del "+StringUtils.toStringJSP (DateUtils.getDateToString(lStatEsecModel.getDataEmissione(),"dd-MM-yyyy"));
	      lDescrProvv += " - "+StringUtils.toStringJSP(lStatEsecModel.getDescrMotivo());
	      lDescrProvv += " - "+StringUtils.toStringJSP(lStatEsecModel.getDescrUfficioEmittente()) +" di "+StringUtils.toStringJSP(lStatEsecModel.getDescrLuogoEmittente());
	
	      Vector <LibAnticipataCumuloModel> lListaLibAnticipate = lStatEsecModel.getListaLiberazioniAnticipate();
	      //lDescTitolo += " "+ lStatEsecModel.getIdStatoEsecTitoloCumulato();
	      
	      int lTotLA = aCalcoloPenaModel.getTotaliLA("LA", lStatEsecModel.getIdStatoEsecTitoloCumulato());
	      int lTotLS = aCalcoloPenaModel.getTotaliLA("LS", lStatEsecModel.getIdStatoEsecTitoloCumulato());
	      int lTotLI = aCalcoloPenaModel.getTotaliLA("LI", lStatEsecModel.getIdStatoEsecTitoloCumulato());
	      int lTotDL92 = aCalcoloPenaModel.getTotaliRimedi(lStatEsecModel.getIdStatoEsecTitoloCumulato());
	      int lTotScomputi = aCalcoloPenaModel.getTotaliScomputi(lStatEsecModel.getIdStatoEsecTitoloCumulato());
	
	
	
	      if (lTotLA!=0 || lTotLS!=0 || lTotLI!=0 || lTotDL92!=0 || lTotScomputi!=0) {
	    %>
		<tr style="display: none" id="SI">
			<td class="l" Title="<%=lDescrProvv%>" nowrap><font
				class="label"><%=lDescTitolo%></font></td>
			<td class="c"><%=(lTotLA==0?"&nbsp;":(lTotLA>0?lTotLA:"<font class='cRosso'>"+lTotLA+"</font>"))%></td>
			<td class="c"><%=(lTotLS==0?"&nbsp;":(lTotLS>0?lTotLS:"<font class='cRosso'>"+lTotLS+"</font>"))%></td>
			<td class="c"><%=(lTotLI==0?"&nbsp;":(lTotLI>0?lTotLI:"<font class='cRosso'>"+lTotLI+"</font>"))%></td>
			<td class="c"><%=(lTotDL92==0?"&nbsp;":(lTotDL92>0?lTotDL92:"<font class='cRosso'>"+lTotDL92+"</font>"))%></td>
			<td class="c"><%=(lTotScomputi==0?"&nbsp;":(lTotScomputi>0?lTotScomputi:"<font class='cRosso'>"+lTotScomputi+"</font>"))%></td>
		</tr>
		<% }
      }  %>

		<%
    // Richieste di Revoca alla Sorveglianza
    Vector <RichiestePmInCumuloModel> lListaRichiestePMxLA = aCalcoloPenaModel.getListaRichiestePM();
    for (RichiestePmInCumuloModel lRichiesta:lListaRichiestePMxLA) {
      if ("020".equals(lRichiesta.getCodTipoAnnotazione())){
        ProvvedimentoGeSorvCumModel lDecisione = lRichiesta.getDecisioneGeSorvCum();

        if (lDecisione!=null || "A".equals(lRichiesta.getFlagAppProvvisoria())){
          int lTotLA = 0;
          int lTotLS = 0;
          int lTotLI = 0;
          String lDataRevoca = "";
          String lDescDecisione = "";

          if (lDecisione!=null) {
            if (lDecisione.getDataD()!=null)
              lDataRevoca = " del "+DateUtils.getDateToString(lDecisione.getDataD(),"dd-MM-yyyy");
            lDescDecisione = "Revoca"+lDataRevoca+" disposta su Richiesta e Decisione";
            lTotLA = (lDecisione.getNumGiorniRevocaLaD()!=null?-1*lDecisione.getNumGiorniRevocaLaD().intValue():0);
            lTotLS = (lDecisione.getNumGiorniRevocaLsD()!=null?-1*lDecisione.getNumGiorniRevocaLsD().intValue():0);
            lTotLI = (lDecisione.getNumGiorniRevocaLiD()!=null?-1*lDecisione.getNumGiorniRevocaLiD().intValue():0);
          } else {
            if (lRichiesta.getDataEmissione()!=null)
              lDataRevoca = " del "+DateUtils.getDateToString(lRichiesta.getDataEmissione(),"dd-MM-yyyy");            
            lDescDecisione = "Revoca"+lDataRevoca+" disposta su Richiesta con Anticipazione";
            lTotLA = (lRichiesta.getNumGiorniRevocaLA()!=null?-1*lRichiesta.getNumGiorniRevocaLA().intValue():0);
            lTotLS = (lRichiesta.getNumGiorniRevocaLS()!=null?-1*lRichiesta.getNumGiorniRevocaLS().intValue():0);
            lTotLI = (lRichiesta.getNumGiorniRevocaLI()!=null?-1*lRichiesta.getNumGiorniRevocaLI().intValue():0);            
          }
          %>
		<tr style="display: none" id="SI">
			<td class="l" nowrap><font class="label"><%=lDescDecisione%></font></td>
			<td class="c"><%=(lTotLA==0?"&nbsp;":(lTotLA>0?lTotLA:"<font class='cRosso'>"+lTotLA+"</font>"))%></td>
			<td class="c"><%=(lTotLS==0?"&nbsp;":(lTotLS>0?lTotLS:"<font class='cRosso'>"+lTotLS+"</font>"))%></td>
			<td class="c"><%=(lTotLI==0?"&nbsp;":(lTotLI>0?lTotLI:"<font class='cRosso'>"+lTotLI+"</font>"))%></td>
			<td class="c">&nbsp;</td>
			<td class="c">&nbsp;</td>
		</tr>
		<%
        }
      }
    }
    %>

		<tr>
			<td class="r" nowrap><font class="label">Totale (giorni):</font></td>
			<td class="c"><%=StringUtils.toStringJSP(aCalcoloPenaModel.getTotaliLA("LA",null),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(aCalcoloPenaModel.getTotaliLA("LS",null),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(aCalcoloPenaModel.getTotaliLA("LI",null),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(aCalcoloPenaModel.getTotaliRimedi(null),"&nbsp;")%></td>
			<td class="c"><%=StringUtils.toStringJSP(aCalcoloPenaModel.getTotaliScomputi(null),"&nbsp;")%></td>
		</tr>
	</table>


	<%--
  ============================================================================== 
  ==  Richieste al GE:
  ==  - Applicazione Amnistia/Indulto
  ==  - Applicazione Depenalizzazione/Incostituzionalità
  ============================================================================== 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px" id="idTabRichGE">
		<tr>
			<td class="Titolo" colspan="10">Richieste al GE Concessione
				Benefici &nbsp;<a
				href="Javascript:espandi('idTabRichGE','idExpCollRichGE');"
				id="idExpCollRichGE"><img align="middle" alt="Espandi"
					src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td class="c" colspan="2">&nbsp;</td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td class="int">&nbsp;</td>
			<td class="int">Beneficio</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Multa</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Ammenda</td>
		</tr>
		<% 
    Vector <RichiestePmInCumuloModel> lListaRichiestePM;
    lListaRichiestePM = aCalcoloPenaModel.getListaRichiestePM();
    
    for (int i=0; i<lListaRichiestePM.size(); i++) 
    { 
      RichiestePmInCumuloModel    lRichiesta =  lListaRichiestePM.elementAt(i);      
      ProvvedimentoGeSorvCumModel lDecisione = lRichiesta.getDecisioneGeSorvCum();
      
      // 002 = Indulto
      // 003 = Amnistia
      // 004 = Depenalizzazione
      // 020 = Revoca Liberazione Anticipata
      // 021 = Revoca Beneficio
      // 023 = Revoca Sanzioni Sostitutive
      if ("021".equals(lRichiesta.getCodTipoAnnotazione()) || "023".equals(lRichiesta.getCodTipoAnnotazione()) ) 
        continue;
      else if ("020".equals(lRichiesta.getCodTipoAnnotazione())) 
        continue;
      
      String lColorRevocati ="";
      String lTextRevocati ="";
      if ("021".equals(lRichiesta.getCodTipoAnnotazione())) {
        lColorRevocati = "style='color:red'";
        lTextRevocati = "<font class='cRosso'>(Revoca)</font>";
      }
      
      String lDescrNorna = "";
      if ("002".equals(lRichiesta.getCodTipoAnnotazione()) || "003".equals(lRichiesta.getCodTipoAnnotazione()))
        lDescrNorna="("+StringUtils.toStringJSP(lRichiesta.getDescrDpr(),"&nbsp;")+")";
      else if ("004".equals(lRichiesta.getCodTipoAnnotazione()) || "017".equals(lRichiesta.getCodTipoAnnotazione())){
        lDescrNorna = "("+lRichiesta.getDescrFonteSigla()+" "+lRichiesta.getAnnoFonte()+"/"+lRichiesta.getNumeroFonte();
        if (lRichiesta.getArticolo()!=null && !"-".equals(lRichiesta.getArticolo()))
          lDescrNorna = lDescrNorna + " Art. " + lRichiesta.getArticolo();
        if (lRichiesta.getDescrSottonumerazione()!=null && !"-".equals(lRichiesta.getDescrSottonumerazione()))
          lDescrNorna = lDescrNorna + " " + lRichiesta.getDescrSottonumerazione();
        if (lRichiesta.getComma()!=null && !"-".equals(lRichiesta.getComma()))
          lDescrNorna = lDescrNorna + " C." + lRichiesta.getComma();
        if (lRichiesta.getLettera()!=null && !"-".equals(lRichiesta.getLettera()))
          lDescrNorna = lDescrNorna + " L." + lRichiesta.getLettera();
        if (lRichiesta.getNumero()!=null && !"-".equals(lRichiesta.getNumero()))
          lDescrNorna = lDescrNorna + " N." + lRichiesta.getNumero();
          
        lDescrNorna+=")";
      }
      else if ("013".equals(lRichiesta.getCodTipoAnnotazione()))
        lDescrNorna=" (Sentenza C.C. "+lRichiesta.getAnnoCc()+"/"+lRichiesta.getNumeroCc()+")";
      
      CalendarModel lCalReclusione = null;
      CalendarModel lCalArresti    = null;
      
      if (lDecisione!=null) {
        lCalReclusione = lDecisione.getQuantumReclusione();
        lCalArresti    = lDecisione.getQuantumArresto();
      }
      else {
        lCalReclusione = lRichiesta.getQuantumReclusione();
        lCalArresti    = lRichiesta.getQuantumArresto();
      }
      
// Se la richiesta non ha quantum o parte pecuniaria non la visualizza
// es: richiesta applicazione beneficio condono della sola Pena Accessoria
if (   lCalReclusione.isQuantumZero() && lCalArresti.isQuantumZero()
    && lCalReclusione.getImportoMulta()==0 && lCalArresti.getImportoAmmenda()==0
   )
{
  continue;  // salto il record non ha dati significativi. Forse condono della sola PA
}

      CalendarUtil lCalendarUtil = new CalendarUtil();
    %>
		<tr style="display: none" id="SI">

			<% if (lDecisione!=null) { %>
			<td class="c" title="Decisione del GE">D</td>
			<% } else {%>
			<td class="c" title="Richiesta con Anticipazione">A</td>
			<% } %>

			<td class="l" <%=lColorRevocati%> title=""><%=StringUtils.toStringJSP(lRichiesta.getDescrTipoAnnotazione(),"&nbsp;")%>&nbsp;<%=StringUtils.toStringJSP(lDescrNorna,"&nbsp;")%>&nbsp;<%=lTextRevocati%></td>

			<% if (lCalendarUtil.isZero(lCalReclusione) ) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumAnni(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumMesi(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumGiorni(),"&nbsp;")%></td>
			<% } %>

			<% if (lCalendarUtil.isZero(lCalReclusione) && lCalReclusione.getImportoMulta()==0) { %>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=(lCalReclusione.getImportoMulta()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal(lCalReclusione.getImportoMulta())) %></td>
			<% } %>


			<% if (lCalendarUtil.isZero(lCalArresti) ) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumAnni(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumMesi(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumGiorni(),"&nbsp;")%></td>
			<% } %>

			<% if (lCalendarUtil.isZero(lCalArresti) && lCalArresti.getImportoAmmenda()==0) { %>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=(lCalArresti.getImportoAmmenda()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal(lCalArresti.getImportoAmmenda())) %></td>
			<% } %>

		</tr>
		<%
    } // end for sulle Richieste
    %>

		<%
    // Totale Benefici Richiesti/Concessi
    CalendarModel lTotReclusioneRich = aCalcoloPenaModel.getRichiesteTotali("R","C");
    CalendarModel lTotArrestoRich = aCalcoloPenaModel.getRichiesteTotali("A","C");
    %>
		<tr>
			<td class="r" colspan="2">Per un totale di</td>
			<% if (lTotReclusioneRich.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=lTotReclusioneRich.getNumAnni()%></td>
			<td class="r"><%=lTotReclusioneRich.getNumMesi()%></td>
			<td class="r"><%=lTotReclusioneRich.getNumGiorni()%></td>
			<% } %>
			<td class="r"><%=(lTotReclusioneRich.getImportoMulta()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotReclusioneRich.getImportoMulta()))%></td>

			<% if (lTotArrestoRich.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=lTotArrestoRich.getNumAnni()%></td>
			<td class="r"><%=lTotArrestoRich.getNumMesi()%></td>
			<td class="r"><%=lTotArrestoRich.getNumGiorni()%></td>
			<% } %>
			<td class="r"><%=(lTotArrestoRich.getImportoAmmenda()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotArrestoRich.getImportoAmmenda()))%></td>
		</tr>
	</table>


	<%--
  ============================================================================== 
  ==  Richieste al GE:
  ==  - Revoca Indulto, Revoca  SS
  ============================================================================== 
  --%>
	<br>
	<table cellspacing="2" cellpadding="4" width="800px"
		id="idTabRichGERev">
		<tr>
			<td class="Titolo" colspan="10">Richieste al GE Revoca Benefici
				&nbsp;<a
				href="Javascript:espandi('idTabRichGERev','idExpCollRichGERev');"
				id="idExpCollRichGERev"><img align="middle" alt="Espandi"
					src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0" /></a>
			</td>
		</tr>
		<tr>
			<td class="c" colspan="2">&nbsp;</td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td class="int">&nbsp;</td>
			<td class="int">Beneficio</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Multa</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Ammenda</td>
		</tr>
		<% 
    
    for (int i=0; i<lListaRichiestePM.size(); i++) 
    { 
      RichiestePmInCumuloModel    lRichiesta =  lListaRichiestePM.elementAt(i);      
      ProvvedimentoGeSorvCumModel lDecisione = lRichiesta.getDecisioneGeSorvCum();
      
      //021=richiesta revoca beneficio, 023=Revoca Sanzioni Sostitutive
      if (!"021".equals(lRichiesta.getCodTipoAnnotazione())
          && !"023".equals(lRichiesta.getCodTipoAnnotazione())) 
        continue;
      
      String lColorRevocati ="";
      String lTextRevocati ="";
      if (   "021".equals(lRichiesta.getCodTipoAnnotazione())
          || "023".equals(lRichiesta.getCodTipoAnnotazione())  
         ) 
      {
        lColorRevocati = "style='color:red'";
        lTextRevocati = "<font class='cRosso'>(Revoca)</font>";
      }
      
      // 002=Indulto, 003=Amnistia, 004=Depenalizzazione, 017=Illecito Amministrativo, 013=Incostituzionelit?
      String lDescrNorna = "";
      if ("002".equals(lRichiesta.getCodTipoAnnotazione()) || "003".equals(lRichiesta.getCodTipoAnnotazione()))
        lDescrNorna="("+StringUtils.toStringJSP(lRichiesta.getDescrDpr(),"&nbsp;")+")";
      else if ("004".equals(lRichiesta.getCodTipoAnnotazione()) || "017".equals(lRichiesta.getCodTipoAnnotazione())){
        lDescrNorna = "("+lRichiesta.getDescrFonteSigla()+" "+lRichiesta.getAnnoFonte()+"/"+lRichiesta.getNumeroFonte();
        if (lRichiesta.getArticolo()!=null && !"-".equals(lRichiesta.getArticolo()))
          lDescrNorna = lDescrNorna + " Art. " + lRichiesta.getArticolo();
        if (lRichiesta.getDescrSottonumerazione()!=null && !"-".equals(lRichiesta.getDescrSottonumerazione()))
          lDescrNorna = lDescrNorna + " " + lRichiesta.getDescrSottonumerazione();
        if (lRichiesta.getComma()!=null && !"-".equals(lRichiesta.getComma()))
          lDescrNorna = lDescrNorna + " C." + lRichiesta.getComma();
        if (lRichiesta.getLettera()!=null && !"-".equals(lRichiesta.getLettera()))
          lDescrNorna = lDescrNorna + " L." + lRichiesta.getLettera();
        if (lRichiesta.getNumero()!=null && !"-".equals(lRichiesta.getNumero()))
          lDescrNorna = lDescrNorna + " N." + lRichiesta.getNumero();
          
        lDescrNorna+=")";
      }
      else if ("013".equals(lRichiesta.getCodTipoAnnotazione()))
        lDescrNorna=" (Sentenza C.C. "+lRichiesta.getAnnoCc()+"/"+lRichiesta.getNumeroCc()+")";
      
      CalendarModel lCalReclusione = null;
      CalendarModel lCalArresti    = null;
      
      if (lDecisione!=null) {
        lCalReclusione = lDecisione.getQuantumReclusione();
        lCalArresti    = lDecisione.getQuantumArresto();
      }
      else {
        lCalReclusione = lRichiesta.getQuantumReclusione();
        lCalArresti    = lRichiesta.getQuantumArresto();
      }
      
// Se la richiesta non ha quantum o parte pecuniaria non la visualizza
// es: richiesta applicazione beneficio condono della sola Pena Accessoria
if (   lCalReclusione.isQuantumZero() && lCalArresti.isQuantumZero()
    && lCalReclusione.getImportoMulta()==0 && lCalArresti.getImportoAmmenda()==0
   )
{
  continue;  // salto il record non ha dati significativi. Forse richiesta revoca Sospensione Condizionale
}

      CalendarUtil lCalendarUtil = new CalendarUtil();
    %>
		<tr style="display: none" id="SI">

			<% if (lDecisione!=null) { %>
			<td class="c" title="Decisione del GE">D</td>
			<% } else {%>
			<td class="c" title="Richiesta con Anticipazione">A</td>
			<% } %>

			<td class="l" <%=lColorRevocati%> title="">
              <%=StringUtils.toStringJSP(lRichiesta.getDescrTipoAnnotazione(),"&nbsp;")%>
        &nbsp;<%=StringUtils.toStringJSP(lDescrNorna,"&nbsp;")%>
        &nbsp;<%=lTextRevocati%>
      </td>

			<% if (lCalendarUtil.isZero(lCalReclusione) ) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumAnni(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumMesi(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalReclusione.getNumGiorni(),"&nbsp;")%></td>
			<% } %>

			<% if (lCalendarUtil.isZero(lCalReclusione) && lCalReclusione.getImportoMulta()==0) { %>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=(lCalReclusione.getImportoMulta()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal(lCalReclusione.getImportoMulta())) %></td>
			<% } %>


			<% if (lCalendarUtil.isZero(lCalArresti) ) { %>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumAnni(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumMesi(),"&nbsp;")%></td>
			<td class="r" <%=lColorRevocati%>><%=StringUtils.toStringJSP(lCalArresti.getNumGiorni(),"&nbsp;")%></td>
			<% } %>

			<% if (lCalendarUtil.isZero(lCalArresti) && lCalArresti.getImportoAmmenda()==0) { %>
			<td class="r">&nbsp;</td>
			<% } else {%>
			<td class="r" <%=lColorRevocati%>><%=(lCalArresti.getImportoAmmenda()==0) ? "&nbsp;":StringUtils.toEuroFormat(new BigDecimal(lCalArresti.getImportoAmmenda())) %></td>
			<% } %>

		</tr>
		<%
    } // end for sulle Richieste
    %>

		<%
    // Totale Benefici Richiesti/Concessi
    lTotReclusioneRich = aCalcoloPenaModel.getRichiesteTotali("R","R");
    lTotArrestoRich = aCalcoloPenaModel.getRichiesteTotali("A","R");
    %>
		<tr>
			<td class="r" colspan="2">Per un totale di</td>
			<% if (lTotReclusioneRich.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=lTotReclusioneRich.getNumAnni()%></td>
			<td class="r"><%=lTotReclusioneRich.getNumMesi()%></td>
			<td class="r"><%=lTotReclusioneRich.getNumGiorni()%></td>
			<% } %>
			<td class="r"><%=(lTotReclusioneRich.getImportoMulta()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotReclusioneRich.getImportoMulta()))%></td>

			<% if (lTotArrestoRich.isQuantumZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=lTotArrestoRich.getNumAnni()%></td>
			<td class="r"><%=lTotArrestoRich.getNumMesi()%></td>
			<td class="r"><%=lTotArrestoRich.getNumGiorni()%></td>
			<% } %>
			<td class="r"><%=(lTotArrestoRich.getImportoAmmenda()==0) ? "&nbsp;":  StringUtils.toEuroFormat(new BigDecimal (lTotArrestoRich.getImportoAmmenda()))%></td>
		</tr>
	</table>

	<%--
  ============================================================================== 
  =   TOTALI QUANTUM DA ESPIARE
  ============================================================================== 
  --%>
	<%
  PenaRideterminataCumuloModel lTotaleDaScontare = aCalcoloPenaModel.getPenaPrincipaleTotNetta();
  
  String colorStyleTotaliRec = "";  
  if (lTotaleDaScontare.isNegativeReclusione()) {
    colorStyleTotaliRec = "style='color: red;'";
  }
  
  String colorStyleTotaliArr = "";  
  if (lTotaleDaScontare.isNegativeArresto()) {
    colorStyleTotaliArr = "style='color: red;'";
  }
      
  %>
	<br>
	<table id="idTabPD" cellspacing="2" cellpadding="4" width="800px">
		<tr>
			<td class="Titolo" colspan="9">Totale quantum</td>
		</tr>
		<tr>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Reclusione</td>
			<td class="c">&nbsp;</td>
			<td class="int" colspan="3">Arresto</td>
			<td class="c">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Multa</td>
			<td class="int">Anni</td>
			<td class="int">Mesi</td>
			<td class="int">Giorni</td>
			<td class="int">Ammenda</td>
		</tr>

		<tr>
			<td class="l">Totale Pene</td>
			<td class="r" <%=colorStyleTotaliRec%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumAnniReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStyleTotaliRec%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumMesiReclusione(),"&nbsp;")%></td>
			<td class="r" <%=colorStyleTotaliRec%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumGiorniReclusione(),"&nbsp;")%></td>
			
      <% if (lTotaleDaScontare.getImportoMulta()!=null && lTotaleDaScontare.getImportoMulta().intValue()<0) {%>
      <td class="r" style='color: red;'>
      <% } else { %>
      <td class="r">
			<% } %>
      <%=(lTotaleDaScontare.getImportoMulta()==null) ? "&nbsp;":StringUtils.toEuroFormat(lTotaleDaScontare.getImportoMulta()) %></td>

      <td class="r" <%=colorStyleTotaliArr%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumAnniArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStyleTotaliArr%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumMesiArresto(),"&nbsp;")%></td>
			<td class="r" <%=colorStyleTotaliArr%> ><%=StringUtils.toStringJSP(lTotaleDaScontare.getNumGiorniArresto(),"&nbsp;")%></td>
			
      <% if (lTotaleDaScontare.getImportoAmmenda()!=null && lTotaleDaScontare.getImportoAmmenda().intValue()<0  ) {%>
      <td class="r" style='color: red;'>
      <% } else { %>
      <td class="r">
			<% } %>
      <%=(lTotaleDaScontare.getImportoAmmenda()==null) ? "&nbsp;":StringUtils.toEuroFormat(lTotaleDaScontare.getImportoAmmenda()) %></td>
		</tr>

		<% 
    if (aCalcoloPenaModel.getErgastolo()!=null) { 
      PenaComplessivaCumuloModel lErgastolo = aCalcoloPenaModel.getErgastolo();
    %>
		<tr>
			<td class="l"><font class="cRosso"><%=lErgastolo.getDescrTipoPenaDetentiva() %></font></td>
			<% if (lErgastolo.isDurataIsolamentoDiurnoZero()) {%>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } else { %>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumAnniIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumMesiIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r"><%=StringUtils.toStringJSP(lErgastolo.getNumGiorniIsolamentoDiurno(),"&nbsp;")%></td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<td class="r">&nbsp;</td>
			<% } %>
		</tr>
		<% } %>
	</table>



	<%
  //============================================================================
  // 2 possibilità
  // 1) la popup carica i campi della form
  // 2) la popup effettua una submit che ricarica la form  
  //============================================================================
  %>
	<% if (request.getParameter("ParentFormName")!=null) { %>
	<table cellspacing="2" cellpadding="4" width="800px">
		<tr>
			<td><input type="button" onClick="javascript:insertIT();"
				name="Carica" value="Carica"></td>
		</tr>
	</table>
	<% } %>
</body>
</html>      