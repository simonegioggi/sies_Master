<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>

<jsp:useBean id="IstruttoriaCumulo"    		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="RichiestaAlGE" 			scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="TipoAnnotazioneManuale" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="listaDPR"               	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 					scope="request" class="java.lang.String"/>
<jsp:useBean id="TitoliRichiesta"      		scope="request" class="java.util.Vector"/>

<!-- 		LoadModRichiestaGEBenefici		 -->

<% 
//============================================================================== 
// Form per l'inserimento e la modifica delle richieste al GE di applicazione 
// Benefici
//============================================================================== 
%>

<html>
<head>
  <title> Gestione Cumulo - Richieste del PM </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
  
    function tornaIndietro(action)
    {
      document.indietroForm.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.indietroForm.submit();
    }
    
    function ElencoTitoliPopup (a_form_name, a_form_type)
    {
      <%
      String lStrParametri = "";
      lStrParametri +="&"+ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO+"="+IstruttoriaCumulo.getIdIstruttoriaCumulo();
      %>
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.modulocumulo.action.ActCaricaElencoTitoliPerSelezione&ParentFormName="+a_form_name+"&ParentFormType="+a_form_type+"<%=lStrParametri%>"
                          , "Elenco_Titoli"
                          , "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=yes, width=800, height=500");
    }     

    function Verify() 
    { 
       // Data Emissione
       if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
         document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
    	// Data Emissione deve essere <= Data del Giorno
     	var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		if(!CompareDate(data_to_verify, data_od))
		{
			alert('Data Richiesta NON può essere superiore alla Data Odierna');
			document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
       		return false;
		}
		
		// Quantum
		var pos = document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.indexOf('.');
		if(pos > 0)
		{
			alert('inserire correttamente il valore INTERO della Multa');
        	document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.focus();
        	return false;
		}
		
		pos = document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.indexOf('.');
		if(pos > 0)
		{
			alert('inserire correttamente il valore INTERO della Ammenda');
        	document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.focus();
        	return false;
		}
		
      	if(document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.value == ''  )
      	{
  			alert('digitare il segno + / - quntum pena');
           	document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R%>.focus();
           	return false;
      	}
      
      	if( ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '' ||
       		  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value == '0' ||
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.value.length == 0 )  &&
            ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '' ||
           	  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value == '0' ||	  
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R%>.value.length == 0  ) &&  
            ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '' ||
           	  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value == '0' ||	  
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R%>.value.length == 0 ) &&
               
            ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '' ||
           	  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value == '0' ||
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R%>.value.length == 0 ) &&
            ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '' ||
           	  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value == '0' ||
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R%>.value.length == 0 ) &&
            ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '' ||
           	  document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value == '0' ||
              document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R%>.value.length == 0 ) &&  
               
             ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT.value.length == 0 ) &&
             ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>DEC.value.length == 0 ) &&  
             ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT.value.length == 0 ) &&
             ( document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>DEC.value.length == 0 ) 
           ) 
      	{
     		alert('digitare correttamente una quantità di pena');
         	document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>.focus();
         	return false;
      	}
      
      return true; 
    } 

  </script>
</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Richiesta Applicazione Beneficio &nbsp;</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
 
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="indietroForm">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
</form> 
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaGEBenefici">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

	  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" 		value="<%=StringUtils.toStringJSP(RichiestaAlGE.getCodTipoRichiesta(),"")%>">
	  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=StringUtils.toStringJSP(RichiestaAlGE.getIdRichiestePmInCumulo(),"")%>">
    
  <%
  //============================================================================
  // Seleziona dalla lista : X il momento Eliminata la selezione che invece avviene a monte della form
  //============================================================================
  %>
  <%-- table width="95%" align="center">
    <tr>
      <td class="l" colspan="1">
        <a href="Javascript:ElencoTitoliPopup('formName','<%=ICostantiRichiestePmInCumulo.FORM_TYPE_RIC_APP_BENEFICI%>');">
          Seleziona Titolo Esecutivo <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
  </table--%>
  
  <!-- 							DATI RELATIVI AI TITOLI CUMULATI COINVOLTI NELLA RICHIESTA							 -->
  
<% 	
  //String DescProvv="";
  String DescAuto="";
  String AnnoNumero ="";
  Iterator itx = TitoliRichiesta.iterator();
  while(itx.hasNext())
  {	
    TitoloCumulatoModel lTitolo = (TitoloCumulatoModel)itx.next();
    
    //DescProvv = lTitolo.getDescrTipoProvvedimento() +" N. "+lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
    AnnoNumero = lTitolo.getAnnoSentenza() +"/"+lTitolo.getNumeroSentenza();
 	DescAuto = lTitolo.getDescrTipoAutoritaEmittente() +" di "+lTitolo.getDescrLuogoEmittente();
%> 
  <table width="95%" align="center">
    <tr><td colspan="8" style="text-align:left" class="Titolonocap">Titolo coinvolto nella Richiesta</td></tr>
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(lTitolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
        <font class="campo"><%=AnnoNumero%></font>&nbsp;
        <font class="label"> del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;
         
        <font class="label"> Emessa da </font>
        <font class="campo"><%=DescAuto%></font>&nbsp;
        &nbsp;<font class="label"> Irrevocabile il  </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
      </td>
    </tr>    
  </table>

<%	// 	------------		REATI		--------------
	if(lTitolo.getReatoCircostanzaCumulo()!=null && lTitolo.getReatoCircostanzaCumulo().size() > 0)
	{	%>  
  <table width="95%" align="center">
    <tr>
      <td class="c" style="color:red; text-align:left" >Reati Selezionati</td>
      <td class="c">Durata</td>
      <td class="c">Sanzione</td>
    </tr>
<%	  String lStringaSanzione="";
	  String lStringPenaReato ="";
	  for(int kr = 0; kr < lTitolo.getReatoCircostanzaCumulo().size(); kr++ )
	  {
		ReatoCircostanzaCumuloModel lReatoCircostanza = (ReatoCircostanzaCumuloModel)lTitolo.getReatoCircostanzaCumulo().get(kr);
    	ReatoCumuloModel lReato = lReatoCircostanza.getReatoCum();
    	ReatoCumuloModel[] lCircostanze = lReatoCircostanza.getCircostanzeCum();
   		
    	boolean lFlagAnnoNumero = false;
        if( lReato.getAnnoFonte() != null
         && !lReato.getAnnoFonte().equals("")
         && lReato.getNumeroFonte() != null
         && !lReato.getNumeroFonte().equals("") )
        {
        	lFlagAnnoNumero = true;
        }
			%>  
    <tr>
      <td class="l">
<%      //REATO
       if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
       {	%>
         <font class="label">
         &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrNumeroManuale()) %> : </font>
         </font>
<%     }
       else
       {	%>
         <font class="label">
         &nbsp;&nbsp;<font class="label"> N. <%=StringUtils.toStringJSP(lReato.getProgrReato()) %> : </font>
		 </font> 	
<%     }  %>    

		<font class="campo">
<%		if(lFlagAnnoNumero)
		{
		   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		     out.println(lReato.getDescrFonte()+" ");
		   if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
		     out.println(lReato.getAnnoFonte());
		   if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
		     out.println("/"+lReato.getNumeroFonte());
 		}

		if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
		   out.println("art."+lReato.getArticolo());
		if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
		   out.println(" "+lReato.getDescrSottonumerazione());

		if(!lFlagAnnoNumero)
		{
		   if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
		     out.println(lReato.getDescrFonte());
 		}

		if(lReato.getComma() != null && !lReato.getComma().equals(""))
			out.println(" c. "+lReato.getComma());
	 	if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
	   		out.println(" l. "+lReato.getLettera());
	 	if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
	  		out.println(" n. "+lReato.getNumero());	
		 	
       	//CIRCOSTANZE
       	if(lCircostanze != null)
       	{
       		ReatoCumuloModel lCirc = null;
       		for(int i=0; i<lCircostanze.length; i++)
       		{
          		lCirc = lCircostanze[i];
%>		
		 ,
		 
<%	
			   boolean lFlagAnnoNumeroCirc = false;
		       if( lCirc.getAnnoFonte() != null
		           && !lCirc.getAnnoFonte().equals("")
		           && lCirc.getNumeroFonte() != null
		           && !lCirc.getNumeroFonte().equals("") )
		       {
			         lFlagAnnoNumeroCirc = true;
		       }
		       if(lFlagAnnoNumeroCirc)
		       {
		         if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
		           out.println(lCirc.getDescrFonte()+" ");
		         if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
		           out.println(lCirc.getAnnoFonte());
		         if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
		           out.println("/"+lCirc.getNumeroFonte());
		       }
			
		       if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
		         out.println("art."+lCirc.getArticolo());
		       if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
		         out.println(" "+lCirc.getDescrSottonumerazione());
			
		       if(!lFlagAnnoNumeroCirc)
		       {
		         if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
		           out.println(lCirc.getDescrFonte());
		       }

		       if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
		         out.println(" c. "+lCirc.getComma());
		       if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
		         out.println(" l. "+lCirc.getLettera());
		       if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
		         out.println(" n. "+lCirc.getNumero());
		       
   			} // Chiude for
           		
		}  // Chiude if(Circostanze) 
   			
   		lStringaSanzione = "";
   		lStringPenaReato = "";
       		
   		// Quantum Pena Detentiva
	    if(lReato.getDescrTipoPenaDetentiva()!=null && 
       		lReato.getDescrTipoPenaDetentiva().length()>0)
       	{
       	    lStringPenaReato = lReato.getDescrTipoPenaDetentiva();
       	    
       	  	if (lReato.getNumAnni()!=null)
       	   	{
       	       	if (lReato.getNumAnni().intValue()!= 0)
       	       		lStringPenaReato += " Anni " + lReato.getNumAnni();
       	   	}
       		   	
       	   	if (lReato.getNumMesi()!= null)
       	   	{
       	      	if (lReato.getNumMesi().intValue()!= 0)
       	       		lStringPenaReato += " Mesi " + lReato.getNumMesi();
       	   	}
       	      
       	   	if (lReato.getNumGiorni()!= null)
       	   	{
       	       	if (lReato.getNumGiorni().intValue()!= 0)
       	       		lStringPenaReato += " Giorni " + lReato.getNumGiorni();
       	   	}
       	}
       		 
       	// Quantum Sanzione Pecuniaria
       	if(lReato.getDescrTipoSanzione()!=null && lReato.getDescrTipoSanzione().length()>0)
       	{
       		lStringaSanzione = lReato.getDescrTipoSanzione();
       	      
       	   	if ( lStringaSanzione.length() > 1)
       	   	{
       	       	//lStringaSanzione += ""+"&Euro;"+"";
       	       	lStringaSanzione += ": "+lReato.getSanzionePecuniaria()+" "+"&euro;";
       	   	}
       	}
	%>			
		</font> 
      </td>
      <td class="l" nowrap><center><font class="campo"><%=StringUtils.toStringJSP(lStringPenaReato ,"")%></font></center></td>

      <td class="r" nowrap><center><font class="campo"><%=StringUtils.toStringJSP(lStringaSanzione ,"")%></font></center></td>
    </tr>
<%	  }  // chiude ciclo(for reatoCirc) %>
  </table>
<%  } // Chiude if(Titolo.getReatoCirc) %>

<%  // ------- 	 	MISURA di SICUREZZA		-------------------
	if(lTitolo.getMisureSicurezzaCumulo()!=null && lTitolo.getMisureSicurezzaCumulo().size() > 0 )
	{	    %>
    <table width="95%" align="center">
<%	  for(int km = 0; km < lTitolo.getMisureSicurezzaCumulo().size(); km++ )
	  {
		MisuraSicurezzaCumuloModel lMisura = (MisuraSicurezzaCumuloModel) lTitolo.getMisureSicurezzaCumulo().get(km);  %>      
      <tr>
 <%		if(km == 0) 
      	{	%>  
        <td class="l" style="color:red; text-align:left" width="12%">Misure Sicurezza: </td>
<%	 	}
 		else
 		{	%>  
 		<td class="l" width="10%">&nbsp;</td>
 <%		} %>
  
 		<td class="l">            	
         <font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getDescrTipo(),"") %></font>&nbsp;&nbsp;
         <font class="label"> per la durata di </font>&nbsp;&nbsp;
          Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumAnni(), " - ") %></font>&nbsp;&nbsp;
          Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumMesi(), " - ") %></font>&nbsp;&nbsp;
          Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lMisura.getNumGiorni(), " - ") %></font>&nbsp;&nbsp;
        </td>
      </tr>
   <% } %>  
    </table>
<%	}	 %>    

<%	// -----------------------	PENE ACCESSORIE	-----------------------------------------  
	if(lTitolo.getPeneAccessorieCumulo()!=null && lTitolo.getPeneAccessorieCumulo().size() > 0 )
	{	%>
    <table width="95%" align="center">
<% 	  for(int kp = 0; kp < lTitolo.getPeneAccessorieCumulo().size(); kp++ )
	  {	
		PenaAccessoriaCumuloModel lPenAcc = (PenaAccessoriaCumuloModel)lTitolo.getPeneAccessorieCumulo().get(kp);	%>
      <tr>
 <%		if(kp == 0) 
      	{	%>  
        <td class="l" style="color:red; text-align:left" width="12%">Pene Accessorie: </td>
<%	 	}
 		else
 		{	%>  
 		<td class="l" width="10%">&nbsp;</td>
 <%		} %>      
        <td class="l">
         <font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getDescrTipoPenaAccessoria(),"") %></font>&nbsp;&nbsp;
         
         <% if ("D".equals(lPenAcc.getDurata()) || "P".equals(lPenAcc.getDurata())) { %>
         <font class="label"> Durata: <%=StringUtils.toStringJSP(lPenAcc.getDescrDurata(), "") %> </font>
         <% } else { %>
         <font class="label"> per la durata di </font>&nbsp;&nbsp;
          Anni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumAnni(), " - ") %></font>&nbsp;&nbsp;
          Mesi&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumMesi(), " - ") %></font>&nbsp;&nbsp;
          Giorni&nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lPenAcc.getNumGiorni(), " - ") %></font>&nbsp;&nbsp;
          <% } %>
        </td>
      </tr>
<%	  } 	%>
	 </table>
<% 	} // Chiude Pene_Accessoria			%>
  
  <br>
  
<%
  } // CHIUDE CICLO while sui Titoli %>  
  
  <!-- 							DATI MODIFICABILI RELATIVI ALLA RICHIESTA 							 -->
   
  <table width="95%" align="center">
    <tr>
      <td colspan=3 class="Titolonocap">Richiesta al Giudice dell' Esecuzione</td>
    </tr>
    <tr>
      <td class="l" width="20%" >Data Richiesta </td>
      <td class="L" colspan="2">
      	<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "dd"), "") %>"   type="text" size="2" maxlength="2" 
      		name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "MM"), "") %>"   type="text" size="2" maxlength="2" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaAlGE.getDataEmissione(), "yyyy"), "") %>" type="text" size="4" maxlength="4" 
        	name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>  
    <tr>
      <td class="l" width="20%" >Estremi Beneficio <font class="ob">(*)</font> :&nbsp;</td>
      <td class="l">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>">
          <%=TipoAnnotazioneManuale%>
        </select>
      </td>
      <td class="l">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR%>">
          <%=listaDPR%>
        </select>
      </td>
    </tr>
  </table>
 
  
  <table width="95%" align="center">
    <tr>
      <td colspan=6>
        <hr width="100%">
      </td>
    </tr>
    <tr>
      <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
        <select name="<%= ICostantiRichiestePmInCumulo.CAMPO_FLAG_PIU_MENO_R %>">
    <%	if( "-".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
    	{	%>    
          <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" selected >-</option>
   <%	}
    	else if( "+".equals(RichiestaAlGE.getFlagPiuMenoR()) ) 
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" selected >+</option>
          <option value = "-" >-</option>
  <%	}
    	else
    	{ %>
    	  <option value = "" ></option>
          <option value = "+" >+</option>
          <option value = "-" >-</option>
  <%	} %>  	  	       
        </select>
      </td>
      <td class="titolo" colspan=2>Reclusione</td>
      <td width="25">&nbsp;</td>
      <td class="titolo" colspan=2>Arresto</td>
    </tr>
    <tr>
      <td class="c">
        <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font class="label">Giorni</font><br>
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_RECLUSIONE_R%>" 
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniReclusioneR(),"")%>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_RECLUSIONE_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiReclusioneR()) %>"
               onkeypress="return TicTabNumField(this,event)">&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_R %>"                 
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniReclusioneR()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Multa</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R%>INT"
               value="<%=StringUtils.getParteIntera(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)" >
        ,
        <input type="text" maxlength="2" size="2"  style="align:right"
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_MULTA_R %>DEC"
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoMultaR()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td width="25">&nbsp;</td>
      <td class=c>
        <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
        <font  class="label">Giorni</font><br>
        <input type="text" maxlength="2" size="2"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_ANNI_ARRESTO_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumAnniArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="2" size="2" 
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_MESI_ARRESTO_R %>"  
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumMesiArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)" >&nbsp;
        <input type="text" maxlength="4" size="4"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUM_GIORNI_ARRESTO_R %>"          
               value="<%=StringUtils.toStringJSP(RichiestaAlGE.getNumGiorniArrestoR()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c">
        <font  class="label">Ammenda</font><br>
        <input type="text" maxlength="7" size="7" style="align:right"
               name="<%=ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R%>INT"         
               value="<%=StringUtils.getParteIntera(RichiestaAlGE.getImportoAmmendaR()) %>"               
               onkeypress="return TicTabNumField(this,event)">
        ,
        <input type="text" maxlength="2" size="2" style="align:right" 
               name="<%= ICostantiRichiestePmInCumulo.CAMPO_IMPORTO_AMMENDA_R %>DEC"  
               value="<%=StringUtils.getParteDecimale(RichiestaAlGE.getImportoAmmendaR()) %>"
               onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>
    <tr>
      <td class="c" colspan=5>
        <font class="label" style="vertical-align: top;">Note</font>
        <textarea cols="60" rows="2" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"><%=StringUtils.toStringJSP(RichiestaAlGE.getMotivazioni()) %></textarea>
      </td>
      <td width=20>&nbsp;</td>
    </tr>
  </table>
  
  <table width="95%" align="center">
    <tr>
      <td class="l">
        Anticipazione degli effetti&nbsp;&nbsp;
      
        <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_APP_PROVVISORIA %>" value="A"  
               <%="A".equals(RichiestaAlGE.getFlagAppProvvisoria())?"checked":""%>>
      </td>
    </tr>
  </table>
  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>


</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("formName");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 