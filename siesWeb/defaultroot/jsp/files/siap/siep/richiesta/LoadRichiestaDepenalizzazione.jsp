<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<%// Pena in decorrenza %>
<jsp:useBean id="PenaComplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="PenRes1"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>
<jsp:useBean id="PenRes2"             scope="request" class="siap.sico.calendar.model.CalendarModel"/>

<%// Reati %>
<jsp:useBean id="reati"                scope="request" class="java.util.Vector" />

<%// mai passati alla form %>
<jsp:useBean id="TipiFontiReato"       scope="request" class="java.lang.String"/> 
<jsp:useBean id="TipiSottonumerazione" scope="request" class="java.lang.String"/> 

<jsp:useBean id="isAnticipazione" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// jsp di visualizzazione della form di inserimento delle Richieste al GE di 
// Depenalizzazione
//==============================================================================
%>
<%
  BigDecimal LAConcesse    = (BigDecimal) request.getAttribute("LAConcesse");
  BigDecimal LADaConcedere = (BigDecimal) request.getAttribute("LADaConcedere");

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>

<head>
  <title> [S.I.E.S.] - Richiesta/Anticipazione Depenalizzazione - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
   
	//==========================================================================
	// MEV 37 Inizio	
  	function mettiFonte()
  	{
    	if(document.f.Radio_Depe_Ammi[0].checked)
    	{
    		document.f.<%= ICostantiReato.CAMPO_COD_FONTE %>.value="03";
    		document.f.<%= ICostantiReato.CAMPO_ANNO_FONTE %>.value="2016";
    		document.f.<%= ICostantiReato.CAMPO_NUMERO_FONTE %>.value="7";
    	}
    	else if(document.f.Radio_Depe_Ammi[1].checked)
    	{
    		document.f.<%= ICostantiReato.CAMPO_COD_FONTE %>.value="03";
    		document.f.<%= ICostantiReato.CAMPO_ANNO_FONTE %>.value="2016";
    		document.f.<%= ICostantiReato.CAMPO_NUMERO_FONTE %>.value="8";   		
    	}	

  	}
	// MEV 37 - fine 
	
    var data_to_verify;
    var obbligatori;

    //==========================================================================
    // 
    //==========================================================================
    function Verify()
    {
   		// MEV 37 Inizio
   	 	if(document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>.checked == true)
   	 	{	
		      // Quantum della richiesta obbligatori solo se seleziono 'Anticipazione degli effetti'
		      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.selectedIndex].value == "")
		      {
		        alert ("Selezionare tra + e -");
		        document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.focus();
		        return false;
		      }
   	 	}
   	 	
   	 	if(document.f.Radio_Depe_Ammi[0].checked == false && 
   	 	   document.f.Radio_Depe_Ammi[1].checked == false )
   	 	{
   	 		alert("Selezionare 'DEPENALIZZAZIONE' o 'ILLECITO AMMINISTRATIVO' ");
   	 		document.f.Radio_Depe_Ammi[0].focus();
   	 		return false;
   	 	}	
   		// MEV 37 - Fine
   		
      obbligatori = (document.f.GRec.value!="" || document.f.MRec.value!="" || document.f.ARec.value!="");
      obbligatori = obbligatori || (document.f.Ammenda.value!="")
      obbligatori = obbligatori || (document.f.GArr.value!="" || document.f.MArr.value!="" || document.f.AArr.value!="");
      obbligatori = obbligatori || (document.f.Multa.value!="");

      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.selectedIndex].value != "" && !obbligatori)
      {
        alert ("Selezionare la  durata del periodo per arresto o reclusione oppure la sanzione");

        return false;
      }

<%
/*
      if (reati.size()>1)
      {
*/
%>
/*
       var sel=false;
       for (var k=0;k<<%=reati.size()%>;k++)
       {
         if (document.f.IdReato[k].checked)
           sel=true;
       }
       if (!sel)
       {
          alert("Selezionare il reato");
          return false;
       }
*/
<%
/*
     }
     else if (reati.size()>0)
     {
*/
%>
/*
       if (!document.f.IdReato.checked)
       {
          alert("Selezionare il reato");
          return false;
       }
*/
<%
//      }
%>
      // Data della Richiesta Obbligatoria
      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value.length>0 || document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value.length>0 || document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>.value.length>0)
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value.length<2)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value="0"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value;
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value.length<2)
          document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value="0"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value;

        data_to_verify = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>.value +"/"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>.value+"/"+document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>.value;

        if (! ControllaData(data_to_verify))
        {
          alert('Data Richiesta non valida');
          return false;
        }
      }

      //Riabilita il CheckBox disabilitato
      document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>.disabled=false;

      //document.f.subm2.disabled=true;
      //document.f.submit();

      // MEV 37 Inizio
      var msgConfirm = "Sei sicuro di voler procedere senza aver valorizzato \n i quantum di Pena da Detrarre/Aumentare? "; 
      if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>[document.f.<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>.selectedIndex].value == "")
      {
		  	if (!window.confirm(msgConfirm)) 
		  	{
			  	return false;
		  	}
      }	
   	  // MEV 37 - Fine
   
      return true;
    }
    
    //==========================================================================
    //
    //==========================================================================
    function Verify_ReturnHere()
    {
      document.f.operazione.value="Torna";
      Verify();
    }

    //==========================================================================
    //
    //==========================================================================
    function Verify_Quantum()
    {
      document.f.operazione.value="Quantum";
      var RetVer = Verify();
	  return RetVer;
    }


  </script>

</head>

<body class="corpo">
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActRichiestaDepenalizzazione">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="004">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="0210">
    <input type="hidden" name="lFlagPage" value="RICH_DEPEN">
    <input type="hidden" name="operazione" value="">
    
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta/Anticipazione Depenalizzazione</font>
      </td>
    </tr>
  </table>

    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  
<table>
  <tr><td>&nbsp;</td></tr>
<%if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
  { %>  
  <tr>
    <td class="l">Posizione Giuridica: <font class="campo">DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%></font>
    </td>
  </tr>
<%}
  else
  { %>
  <tr>
  	<td class="l">Posizione Giuridica: <font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font>
    </td>
  </tr>
<%} %>
</table>
<%
  String lReadOnly = "disabled";
  String lChecked = "";

  if(isAnticipazione.equals("true"))
    lChecked = "checked";

  if(isAnticipazione.equals(""))
    lReadOnly = "";
%>
  <table width="40%">
    <tr>
      <td class="l"colspan="2"> Anticipazione degli effetti&nbsp;&nbsp;
        <input type="checkbox" name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_APP_PROVVISORIA%>" value="A" <%=lChecked%> <%=lReadOnly%>>
      </td>
    </tr>
    
    <!-- // MEV 37 Inizio -->
    <tr>  
      <td class="l" width="15%"> Depenalizzazione 
      	<input type="radio" name="Radio_Depe_Ammi" value="D" onClick="javascript:mettiFonte();" >
      </td>
      
      <td class="l" width="20%"> Illecito Amministrativo 
      	<input type="radio" name="Radio_Depe_Ammi" value="I" onClick="javascript:mettiFonte();" >
      </td>
    </tr>
    <!-- // MEV 37 - Fine-->
   </table>
<%
/*
  if (ReaAntEffetti.size()!=0)
  {
*/
%>
<!--
   <table style="width: 95%;">
     <tr><td colspan=3 class="Titolo">Richieste Anticipazioni degli effetti</td></tr>
     <tr>
        <td class="c">Reato</td>
        <td class="c">Reclusione</td>
        <td class="c">Arresto</td>
     </tr>
-->
<%
/*
   AnnotazioneManualeModel tmp = null;
   ReatoModel rtmp = new ReatoModel();
   BigDecimal idRea;
   String DescReato="";

   for (int i=0;i<ReaAntEffetti.size();i++)
   {
      out.println("<tr>");
      tmp=(AnnotazioneManualeModel)ReaAntEffetti.get(i);
      idRea=tmp.getReaIdReato();
      boolean found=false;
      for (int itR=0;itR<reati.size();itR++)
      {
        rtmp=(ReatoModel)reati.get(itR);
        if (rtmp.getIdReato().equals(idRea))
        {
           found=true;
           itR=reati.size()+1;
        }
      }
      out.println("<td class=\"l\">");
      if (found)
      {
        boolean lFlagAnnoNumero;
        lFlagAnnoNumero = false;
        if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
            lFlagAnnoNumero = true;
*/
%>
<!--
        <font class="label">
-->
<%
/*
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
*/
%>
<!--
      </font>
      <font class="campo">
-->
<%
/*
          if(lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte()+" ");
            if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
              out.println(rtmp.getAnnoFonte());
            if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
              out.println("/"+rtmp.getNumeroFonte());
          }

          if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
            out.println("art."+rtmp.getArticolo());
          if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
            out.println(" "+rtmp.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte());
          }

          if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
            out.println(" c. "+rtmp.getComma());
          if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
            out.println(" l. "+rtmp.getLettera());
          if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
            out.println(" n. "+rtmp.getNumero());
*/
%>
<!--
  </font>
-->
<%
/*
      }
      out.println("</td>");
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">
   Anni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
   Mesi <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
   Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
   Multa <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
</td>
<td class="l">
   Anni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
   Mesi <font class=campo><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
   Giorni <font class=campo><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
   Ammenda <font class=campo><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
</td>
--%>
<%
/*
    out.println("</tr>");
   }
*/
%>
<!--
   </table>
-->
<%
/*
  }

  if (ReaRichiesti.size()!=0)
  {
*/
%>
<!--
   <table style="width: 95%;">
     <tr><td colspan=3 class="Titolo">Richieste</td></tr>
     <tr>
        <td class="c">Reato</td>
        <td class="c">Reclusione</td>
        <td class="c">Arresto</td>
     </tr>
-->
<%
/*
   AnnotazioneManualeModel tmp=null;
   ReatoModel rtmp=new ReatoModel();
   BigDecimal idRea;
   String DescReato="";
   String DataReato="";
   String CodTipoPena="";
   String TipoPena="";
   String Durata="";
   String Sanzione="";

   for (int i=0;i<ReaRichiesti.size();i++)
   {
      out.println("<tr>");
      tmp=(AnnotazioneManualeModel)ReaRichiesti.get(i);
      idRea=tmp.getReaIdReato();
      boolean found=false;
      for (int itR=0;itR<reati.size();itR++)
      {
        rtmp=(ReatoModel)reati.get(itR);
        if (rtmp.getIdReato().equals(idRea))
        {
           found=true;
           itR=reati.size()+1;
        }
      }
      out.println("<td class=\"l\">");
      if (found)
      {
        DataReato=StringUtils.toStringJSP(DateUtils.getDateToString(rtmp.getDataReato(),"dd/MM/yyyy"),"-");
        TipoPena=rtmp.getDescrTipoPenaDetentiva();
        CodTipoPena=rtmp.getCodTipoPenaDetentiva();
        boolean lFlagAnnoNumero;
        lFlagAnnoNumero = false;
        if( rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals("") && rtmp.getNumeroFonte() != null  && !rtmp.getNumeroFonte().equals("") )
            lFlagAnnoNumero = true;
*/
%>
<!--
        <font class="label">
-->
<%
/*
        if (rtmp.getProgrNumeroManuale() != null && !rtmp.getProgrNumeroManuale().equals(""))
        {
          out.println("n." + rtmp.getProgrNumeroManuale()+": ");
        }
        else
        {
          out.println("n." + rtmp.getProgrReato()+": ");
        }
*/
%>
<!--
      </font>
      <font class="campo">
-->
<%
/*
          if(lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte()+" ");
            if(rtmp.getAnnoFonte() != null && !rtmp.getAnnoFonte().equals(""))
              out.println(rtmp.getAnnoFonte());
            if(rtmp.getNumeroFonte() != null && !rtmp.getNumeroFonte().equals(""))
              out.println("/"+rtmp.getNumeroFonte());
          }

          if(rtmp.getArticolo() != null && !rtmp.getArticolo().equals(""))
            out.println("art."+rtmp.getArticolo());
          if(rtmp.getDescrSottonumerazione() != null && !rtmp.getDescrSottonumerazione().equals("") && !rtmp.getDescrSottonumerazione().equals("-"))
            out.println(" "+rtmp.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(rtmp.getDescrFonte() != null && !rtmp.getDescrFonte().equals("") && !rtmp.getDescrFonte().equals("-"))
              out.println(rtmp.getDescrFonte());
          }

          if(rtmp.getComma() != null && !rtmp.getComma().equals(""))
            out.println(" c. "+rtmp.getComma());
          if(rtmp.getLettera() != null && !rtmp.getLettera().equals(""))
            out.println(" l. "+rtmp.getLettera());
          if(rtmp.getNumero() != null && !rtmp.getNumero().equals(""))
            out.println(" n. "+rtmp.getNumero());
*/
%>
<!--
        </font>
-->
<%
/*
      }

      out.println("</td>");
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">
   Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniReclusione(),"0")%></font>
   Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiReclusione(),"0")%></font>
   Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniReclusione(),"0")%></font>
   Multa <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoMulta())%></font>
</td>
<td class="l">
   Anni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumAnniArresto(),"0")%></font>
   Mesi <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumMesiArresto(),"0")%></font>
   Giorni <font class="campo"><%=StringUtils.toStringJSP(tmp.getNumGiorniArresto(),"0")%></font>
   Ammenda <font class="campo"><%=StringUtils.toEuroFormat(tmp.getImportoAmmenda())%></font>
</td>
--%>
<%
/*
    out.println("</tr>");
   }
*/
%>
<!--
   </table>
-->
<%
//}

//==============================================================================
//                        SEZIONE CAPI DI IMPUTAZIONE
//==============================================================================
  if (!reati.isEmpty())
  {
%>
    <table style="width: 95%;">
      <br>	
      <tr><td colspan=7 class="Titolonocap">Capi di imputazione</td></tr>
      <tr>
        <td class="c">Reato</td>
        <td class="c">Durata</td>
        <td class="c">Sanzione</td>
        <td class="c">Sel.</td>
        <td class="c">Ann.Inserita</td>
      </tr>
<%
		ReatoModel lReato;
		boolean lFlagAnnoNumero;
		for (int i=0;i<reati.size();i++)
		{
			lReato=(ReatoModel)reati.get(i);
			lFlagAnnoNumero = false;
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
          <font class="label">
<%
            if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
            {
%>
              <font class="campoNoCap">
<%
                out.println("N." + lReato.getProgrNumeroManuale()+": ");
%>
              </font>
<%
            }
            else
            {
              out.println("N." + lReato.getProgrReato()+": ");
            }
%>
          </font>
          <font class="L">
<%
          if(lFlagAnnoNumero)
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
            out.println(" n. "+lReato.getNumero());%></font>
<% if(lReato.getStringaConsumazione()!= null)
                  {
%>
                    <!--  <font class="label">Data</font> -->
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
<%
                  }
	if(lReato.getNote() != null && !lReato.getNote().equals(""))
        {
%>
        			<font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
<%
        }

                  if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals(""))
                  {
%>
                    <font class="label">Luogo</font>&nbsp;
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
<%
                  }
%>


			</td>

      <td class="l">
        <table>
          <td class="lnobord"><font class="label">AA</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumAnni(),"0")%></font></td>
          <td class="lnobord"><font class="label">MM</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumMesi(),"0")%></font></td>
          <td class="lnobord"><font class="label">GG</font></td>
          <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lReato.getNumGiorni(),"0")%></font></td>
        </table>
       </td>
        <td class="r">
          <font class="campo">
            <%= StringUtils.toEuroFormat(lReato.getSanzionePecuniaria())%>
          </font>
          €
<%
          if (lReato.getSanzionePecuniaria() != null && lReato.getSanzionePecuniaria().compareTo(new BigDecimal(0)) != 0)
          {
%>
          di
            <font class="campo">
              <%= StringUtils.toStringJSP(lReato.getDescrTipoSanzione())%>
            </font>&nbsp;
<%
          }
%>
         </td>
				<td class="c">
          <input type="radio" name="IdReato" value="<%= lReato.getIdReato() %>">
        </td>
<%
     if(lReato.getFlagVisto() != null && lReato.getFlagVisto().equals("S"))
     {
%>
       <td class="C"><img src="/images/V.gif"> </td>
<%
     }
     else
     {
%>
       <td class="C"> &nbsp;</td>
<%
     }
%>
				</tr>
<%
		}
%>
		</table>
<%
  }
%>
<%
//==============================================================================
//               Sezione con la Visualizzazione della Pena
// - Se ERGASTOLO: data inizio e fine pena MAI
// - Se Libero vengono visualizzati i Quantum 
// - Se detenuto viene visualizzato il quantum residuo calcolato al volo tra la 
//   data di sistema e la data fine pena prevista (da correggere) e le date di
//   decorrenza
//==============================================================================
%>
<% // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(  PenaComplessiva.getCodTipoPenaDetentiva() != null
    && PenaComplessiva.getCodTipoPenaDetentiva() != ""
    && ( PenaComplessiva.getCodTipoPenaDetentiva().equals("03") || PenaComplessiva.getCodTipoPenaDetentiva().equals("04") )
    )
  {
%>
    <table style="width: 95%;">
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessiva.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l">Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(),"dd/MM/yyyy"))%> </font></td>
        <td class="l">Data Fine : <font class="campo">MAI</font></td>
      </tr>
    </table>
<%
  }
  else if (!PenRes1.getErrorMsg().equals("-"))
  { 
  // n.b. getErrorMsg = - = solo in caso di errore
  //      getErrorMsg = "Non Libero" se data iniziio <> null
  //      getErrorMsg = "Libero" altrimenti
%>
  <table style="width: 95%;">
    <tr>
      <td class="Titolonocap" colspan="6"> Pena residua da espiare </td>
    </tr>

    <% if (PenRes1.getErrorMsg().equalsIgnoreCase("Libero")) { %>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    <tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
    <%
    }
    else //non libero
    {
      //========================================================================
      // Visualizzo la pena residua calcolata al volo tra la data odierna e la 
      // data fine pena prevista
      //========================================================================
      //CalendarUtil lCU=new CalendarUtil();
      //PenRes2.setDataFine(PenRes1.getDataFine());
      //PenRes2.setDataInizio(DateUtils.getSysDate());
      //PenRes2=lCU.ricalcolaGAM(lCU.CalcolaNumGiorniMesiAnni(PenRes2,true));
    %>
    <!-- Mod 29-01-2007 il residuo calcolato al volo scompare dalle form -->
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="l">
        Anni : <font class=campo><%=PenRes2.getNumAnni()%> </font>
        Mesi : <font class=campo><%=PenRes2.getNumMesi()%> </font>
        Giorni : <font class=campo><%=PenRes2.getNumGiorni()%></font>
      </td>
      <td class="lNoBord">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="lNoBord">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr--%>
    <tr>
      <td class="l"> Reclusione :
         Anni <font class="campo"><%=PenRes1.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes1.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes1.getNumGiorni()%></font>
         Multa <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes1.getImportoMulta()))%></font>
      </td>
      <td class="l"> Arresto :
         Anni <font class="campo"><%=PenRes2.getNumAnni()%></font>
         Mesi <font class="campo"><%=PenRes2.getNumMesi()%></font>
         Giorni <font class="campo"><%=PenRes2.getNumGiorni()%></font>
         Ammenda <font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(PenRes2.getImportoAmmenda()))%></font>
      </td>
    </tr>
    <tr>
      <td class="l">Data Inizio : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataInizio(), "dd/MM/yyyy"))%></font></td>
      <td class="l">Data Fine : <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenRes1.getDataFine(), "dd/MM/yyyy"))%></font></td>
    </tr>
      <% if(LAConcesse.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
          <font class="campo"><%=LAConcesse%></font>
        </td>
      </tr>
      <% } %>  
      <% if(LADaConcedere.compareTo(new BigDecimal(0)) != 0) { %>
      <tr>
        <td class="l" colspan="9">
          <font class="label">Giorni di Liberazione Anticipata Concessi da detrarre:</font>
          <font class="campo"><%=LADaConcedere%></font>
        </td>
      </tr>
      <% } %>  
<%
       }
%>
  </table>
<%
  }
%>
<!--
Fine Pena Residua
-->
  <table style="width: 95%;">
    <tr>
      <td class="Titolonocap" colspan=8>
        Richiesta al Giudice dell' Esecuzione
      </td>
    </tr>
    <tr>
      <td class="c">Fonte</td>
      <td class="c">Anno</td>
      <td class="c">Numero</td>
      <td class="c">Articolo</td>
      <td class="c">Art.qualificante</td>
      <td class="c">Comma</td>
      <td class="c">Lettera</td>
      <td class="c">Numero</td>
    </tr>
    <tr>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>">
          <%=TipiFontiReato%>
        </select>
      </td>
      <td class="c">
        <input size=4 maxlength=4 title="Anno Fonte" value="" type="text" name="<%=ICostantiReato.CAMPO_ANNO_FONTE %>">
      </td>
      <td class="c">
        <input size=6 maxlength=6 title="Numero Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>">
      </td>
      <td class="c">
        <input size=5 maxlength=5 title="Articolo Fonte" value="" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>">
      </td>
      <td class="c">
        <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>">
          <%=TipiSottonumerazione %>
        </select>
      </td>
      <td class="c">
        <strong>C</strong>
        <input size=10 maxlength=10 title="Comma" value="" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>">
      </td>
      <td class="c">
        <strong>L</strong>
        <input size=2 maxlength=2 title="Lettera" value="" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>">
      </td>
      <td class="c">
       <strong>N</strong>
       <input size=2 maxlength=2 title="Numero" value="" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>">
      </td>
    </tr>
    <tr>
      <td class="l" colspan=8>
        <font class="label">Data Richiesta</font>
        &nbsp;&nbsp;
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_RICHIESTA%>" maxlength="2" size="2" value="<%=DateUtils.getSysDate("dd")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_RICHIESTA%>" maxlength="2" size="2" value="<%=DateUtils.getSysDate("MM")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_RICHIESTA%>" maxlength="4" size="4" value="<%=DateUtils.getSysDate("yyyy")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
</table>
<table width="97%">
  <tr>
    <td colspan=6>
      <hr width="100%">
    </td>
  </tr>
  <tr>
    <td valign="middle" class="c" rowspan=3>+/- <font class="ob">(*)</font><br>
      <select name="<%=ICostantiAnnotazioneManuale.CAMPO_FLAG_PIU_MENO%>">
        <option value=""></option>
        <option value="+">+</option>
        <option value="-">-</option>
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
      <input type="text" name="ARec" maxlength="2" size="2" value="">&nbsp;
      <input type="text" name="MRec" maxlength="2" size="2" value="">&nbsp;
      <input type="text" name="GRec" maxlength="4" size="4" value="">
    </td>
    <td class="c">
      <font  class="label">Multa</font><br>
      <input style="align:right" type="text" name="Multa" maxlength="7" size="7" value="">
      ,
      <input style="align:right" type="text" name="Mul_dec" maxlength="2" size="2" value="">
    </td>
    <td width="25">&nbsp;</td>
    <td class=c>
      <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Giorni</font><br>
      <input  type="text" name="AArr" maxlength="2" size="2" value="">&nbsp;
      <input  type="text" name="MArr" maxlength="2" size="2" value="">&nbsp;
      <input  type="text" name="GArr" maxlength="4" size="4" value="">
    </td>
    <td class="c">
      <font  class="label">Ammenda</font><br>
      <input style="align:right" type="text" name="Ammenda" maxlength="7" size="7" value="">
      ,
      <input style="align:right" type="text" name="Amm_dec" maxlength="2" size="2" value="">
    </td>
  </tr>
  <tr>
    <td class="c" colspan=5>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="60" rows="2" name="noteRec"></textarea>
    </td>
    <td width=20>&nbsp;</td>
<!--
    <td class=l colspan=2>
      <font class="label" style="vertical-align: top;">Note</font>
      <textarea cols="30" rows="2" name="noteArr"></textarea>
    </td>
-->
  </tr>
</table>
<table>
  <tr>
    <td class="lNoBord" colspan="2">
      <!-- <INPUT class="bottone" type="button" name="subm"  value="Inserisci altre richieste" onClick="javascript:Verify_ReturnHere();">&nbsp;&nbsp; -->
      <!-- <INPUT class="bottone" type="button" name="subm2" value="Conferma" onClick="javascript:Verify_Quantum();">&nbsp;&nbsp;-->
      <INPUT class="bottone" type="submit" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>

 <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%= ICostantiReato.CAMPO_ANNO_FONTE%>","numeric");

  // Controllo campi Reclusione
  frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("Multa","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");
  frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa Reclusione può contenere solo caratteri numerici");

  // Controllo campi Arresto
  frmvalidator.addValidation("AArr","numeric","Il campo Anni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("MArr","numeric","Il campo Mesi Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("GArr","numeric","Il campo Giorni Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("Ammenda","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");
  frmvalidator.addValidation("Amm_dec","numeric","Il campo Ammenda Arresto può contenere solo caratteri numerici");

  frmvalidator.setAddnlValidationFunction("Verify_Quantum");
</script>
</body>
</html>