<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="Provvedimento" 	scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<%
/*
Vector <ComputiCumuloModel> lListaComputi = Provvedimento.getListaComputi();
ComputiCumuloModel lComputo = new ComputiCumuloModel();

if (lListaComputi!=null) {

	Iterator itxComputi = lListaComputi.iterator();
	int ind = 0;
	while ( itxComputi.hasNext()) 
	{
		lComputo = (ComputiCumuloModel) itxComputi.next();
		ind++;
	}
	
    lComputo.setDescrOggettoDecisione ( DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getMotivoInterruzione(),lComputo.getCodOggettoDecisione() ));  
	
}
*/

	String lBeneRevo = "";
	if(Provvedimento.getFlagStato().equals("I")) {
	   if("S".equals(Provvedimento.getFlagTipoSosp()) ) {
	 	  lBeneRevo = "Sopensione Condizionale della pena";
	   } else if("M".equals(Provvedimento.getFlagTipoSosp()) ) {
	 	  lBeneRevo = "Non Menzione";
	   } else if("SM".equals(Provvedimento.getFlagTipoSosp()) ) {
	 	  lBeneRevo = "Sopensione Condizionale della pena, Non Menzione";
	   }
	} else if(Provvedimento.getFlagStato().equals("E")) {
	  	if(Provvedimento.getCodMotivo().equals("0818")) {
	  		lBeneRevo = "Sopensione Condizionale della pena";
	  	} else if(Provvedimento.getCodMotivo().equals("0822")) {
	  		lBeneRevo = "Non Menzione";
	  	}  
	}

%>

<html>
<head>
  <title> Dettaglio Revoca Benefici Concessi nel titolo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // 
    //==========================================================================
    function eseguiFunzione(aTipoAzione)
    {
      if (aTipoAzione=='Inserisci'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciAnnotazioneRevocaBeneficioCumulo";
        document.DettaAnnotazRevocaBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaAnnotazRevocaBen.modalita.value = "I";
        
        document.DettaAnnotazRevocaBen.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>.value = "";
        document.DettaAnnotazRevocaBen.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value = "";
        document.DettaAnnotazRevocaBen.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>.value = "";
        
        document.DettaAnnotazRevocaBen.submit();
      }      
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciAnnotazioneRevocaBeneficioCumulo";
        document.DettaAnnotazRevocaBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaAnnotazRevocaBen.modalita.value = "M";
        document.DettaAnnotazRevocaBen.submit();
      }
      else if (aTipoAzione=='Cancella'){
        var aStato = document.DettaAnnotazRevocaBen.<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>.value;
        
        if (aStato=='E' || aStato=='M'){
          // Cancellazione Logica Richiedo Motivazione
          lAzione = "siap.siep.modulocumulo.action.ActInserisciAnnotazioneRevocaBeneficioCumulo";
          document.DettaAnnotazRevocaBen.modalita.value = "C";
          document.DettaAnnotazRevocaBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          aMotivoModifica = document.DettaAnnotazRevocaBen.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>.value
          var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaAnnotazRevocaBen"
                                     + "&" + "<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
          window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I'){
          // Cancellazione fisica richiedo conferma
          var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
          if (window.confirm(msgConfirm)) {
            lAzione = "siap.siep.modulocumulo.action.ActInserisciAnnotazioneRevocaBeneficioCumulo";
            document.DettaAnnotazRevocaBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
            document.DettaAnnotazRevocaBen.modalita.value = "C";

            document.DettaAnnotazRevocaBen.submit();
          }
        }
      }
      else if (aTipoAzione=='Indietro'){
        lAzione = "siap.siep.modulocumulo.action.ActRicercaAnnotazioneRevocaBeneficioCumulo";
        document.DettaAnnotazRevocaBen.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.DettaAnnotazRevocaBen.submit();
      }
    }
    
    
  </script>
</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();"> 
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> 
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Revoca Benefici Concessi nel titolo &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('Indietro')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
</FORM>

  <table align="center" width="95%" border="0" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="<%=ICostantiIstruttoriaCumulo.PG_INCLUDE_DETTAGLIO_ISTRUTTORIA%>"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="<%=ICostantiTitoloCumulato.PG_INCLUDE_DETTAGLIO_TITOLO%>"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaAnnotazRevocaBen">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(Provvedimento.getIdStatoEsecTitoloCumulato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"      value="<%=StringUtils.toStringJSP(Provvedimento.getFlagStato()) %>">
  <input type="hidden" name="<%= ICostantiStatoEsecTitoloCumulato.CAMPO_MOTIVO_MODIFICA %>" value="<%=StringUtils.toStringJSP(StringUtils.cStrForJS(Provvedimento.getMotivoModifica()), "") %>">

  <input type="hidden" name="modalita" value="">


<div id="divPosizionamento" align="left" style="padding-left: 25px;">

  	<table align="center" width="95%" border="0" cellspacing="1" cellpadding="1">
		
		<tr>
	      <td class="titolo" colspan="4">Tipo  Beneficio  Revocato </td>
		</tr>
		     
		<tr>
	      <td class="l" width="25%">Tipologia Beneficio </td>
	      <td class="l" colspan="3">
	         <font class="campo"><%=lBeneRevo%></font>&nbsp;
	      </td>
	    </tr>
		<tr><td>&nbsp;</td><tr>    
	    <tr>
	      <td class="titolo" colspan="4">Estremi del provvedimento di Revoca del Giudice dell'Esecuzione </td>
		</tr>
			
		<tr>
		  <td class="l" width="25%" >Tipo Provvedimento</td>
		  <td class="l" colspan = "3">
       		<font class="campo">
   			<%=StringUtils.toStringJSP(Provvedimento.getDescrTipoProvvedimento())+" "+StringUtils.toStringJSP(Provvedimento.getDescrMotivo())%>
      		</font>
       	  </td>
       	</tr>
        	
       	<tr>
		  <td class="l" width="25%" >Data Provvedimento</td>
		  <td class="l" colspan = "3">
       		<font class="campo">
   			<%=StringUtils.toStringJSP(DateUtils.getDateToString(Provvedimento.getDataEmissione(),"dd-MM-yyyy"))%>
       		</font>
       	  </td>
       	</tr>

<%	if(Provvedimento.getAnnoProvvedimento()!=null && Provvedimento.getProgrProvvedimento()!=null  ) {	 %>	       	
       	<tr>
		  <td class="l" width="25%" >Anno/Numero Provvedimento</td>
		  <td class="l" colspan = "3">
       		<font class="campo">
   			<%=StringUtils.toStringJSP(Provvedimento.getAnnoProvvedimento())+" / "+StringUtils.toStringJSP(Provvedimento.getProgrProvvedimento())%>
      		</font>
       	  </td>
       	</tr>  	 
<%	} %>

	    <tr> 
	      <td class="l" width="25%">Emesso da </td>
	      <td class="l" colspan = "3">
			<font class="campo">
			<%=StringUtils.toStringJSP(Provvedimento.getDescrUfficioEmittente())+" di "+StringUtils.toStringJSP(Provvedimento.getDescrLuogoEmittente()) %> 
			</font>&nbsp;
	    </tr>

<%	if(Provvedimento.getSezioneAltro()!=null && !"".equals(Provvedimento.getSezioneAltro()) ) {	 %>		     
	    <tr> 
	      <td class="l" width="25%" >sezione</td>
	      <td class="l" colspan = "3">
	         <font class="campo"><%=StringUtils.toStringJSP( Provvedimento.getSezioneAltro() ) %></font>&nbsp;
		  </td>
	    </tr>
<%	} %>

<%	if(Provvedimento.getNote()!=null && !"".equals(Provvedimento.getNote()) ) {	 %>	
	    <tr> 
	      <td class="l" width="25%" >Motivo</td>
	      <td class="l" colspan = "3">
	         <font class="campo"><%=StringUtils.toStringJSP( Provvedimento.getNote() ) %></font>&nbsp;
		  </td>
	    </tr>
<%	} %>
    
	</table>
</div>
<br><br>
</form>
</body>
</html>
