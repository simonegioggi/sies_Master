<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>


<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.camponota.model.CampoNotaModel"%>


<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="aEventoComputo"      scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aEventoAltraAut"     scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="aListaCampoNota"         scope="request" class="java.util.Vector"/>

<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
  
<jsp:useBean id="annotazioni"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="licenze"             scope="request" class="java.util.Vector"/>

<jsp:useBean id="aProvvAltraAut"      scope="request" class="siap.sico.evento.model.EventoModel"/>

<!-- DettaglioAnnotaPagamentoPP -->
<%
//==============================================================================
// Form per la visualizzazione del dettaglio del "provvedimento" di computo
// della 'Rideterminazione pena  Per Avvenuto pagamento di PenaPecuniaria' 
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel       lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel              lAltraCausa = posizioneluogoaltra.getAltraCausa();
  CampoNotaModel aCNotaMod = new CampoNotaModel();
  
  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  String Note ="";
  String Sezione ="";
  String ExCampione ="";
  
  for (int i=0; i< aListaCampoNota.size(); i++)
  {
	  aCNotaMod = null;
	  aCNotaMod = (CampoNotaModel)aListaCampoNota.elementAt(i);
      if (aCNotaMod != null )
      {
    		if (aCNotaMod.getOggettoNotaRes().equals("NOTE") )
    		{ 
    			Note = aCNotaMod.getDescr();
    		}
    		else if (aCNotaMod.getOggettoNotaRes().equals("EXCAMPIONE"))
    		{
    			ExCampione = aCNotaMod.getDescr();
    		}
    		else if (aCNotaMod.getOggettoNotaRes().equals("SEZIONE"))
    		{
    			Sezione = aCNotaMod.getDescr();
    		}
    		else
    		{
    		}
      }
     
  }    
 
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script>
    function CalcoloPena(idEvento)
    {
      
      <%if (annotazioni.size()>0) 
      	{ %>
      			document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.calcolopena.action.ActCalcoloPenaComputo";
      <%}
      	else
      	{ %>
      			document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.libertaanticipata.action.ActCalcoloPenaScompPermRidimLA";
      <%} %>

      	try
      	{
        	document.f.CALCOLO.disabled=true;
      	}
      	catch(err) 
      	{
        	//alert('Tasto CALCOLO Assente nella form');
      	}
      
      	document.f.submit();

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
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Annotazione Avvenuto Pagamento Pene Pecuniarie</font>
   	</td>	


  <%     if (penaresidua==null ||(penaresidua != null && penaresidua.getIdPenaResidua() == null) )
      	 { 
       	 }
  		 else
  		 {	 
 				if (aEventoComputo.getFlagDocumentoRegistrato() != null)
	      			if (aEventoComputo.getFlagDocumentoRegistrato().compareTo("N")==0)
	      			{
	%>
						<td class="LBG">	
				      		<a  href="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActUploadRidPenaAltro&IdEvento=<%=aEventoComputo.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.calcolopena.action.ActLoadDettaglioAnnotaPagamentoPP&IdEvento=<%=aEventoComputo.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		    					<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		    				</a>
		    			</td>	
		<!-- BOTTONE DI STAMPA -->
						<td class="LBG">
							<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
			     			<jsp:param name="ActionLink"  value="<%=  "/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaAvvenutoPagamentoPP&IdEvento="+aEventoComputo.getIdEvento()%>"/>
			   				</jsp:include>
		   				</td>	
	<%
	      			}
  		 }
  
  		 if (penaresidua==null ||(penaresidua != null && penaresidua.getIdPenaResidua() == null) )
	 	 { 
	 	 }
	 	 else
	 	 {
			 	if (aEventoComputo.getFlagDocumentoRegistrato() == null)
			 	{
%>
						<td class="LBG">	
				      		<a  href="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActUploadRidPenaAltro&IdEvento=<%=aEventoComputo.getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.calcolopena.action.ActLoadDettaglioAnnotaPagamentoPP&IdEvento=<%=aEventoComputo.getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		    					<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		    				</a>
		    			</td>
			     	 <!-- BOTTONE DI STAMPA -->
					 <td class="LBG">
				   		<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				     	<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaAvvenutoPagamentoPP&IdEvento="+aEventoComputo.getIdEvento()%>"/>
				   		</jsp:include>
				   	 </td>    			   	 	
<%
			 	}
    	}
%>

    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
//==============================================================================
// Sezione Contenente
// - Posizione giuridica
// - Luogo di detenzione
//==============================================================================
%>
  <table style="width: 95%;">
    <tr>
      <td class="l">Posizione Giuridica:
      <!--td class="L" colspan=5-->
        <font class="campo">
        <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
        DETENUTO PER ALTRA CAUSA
        <%} else {%>
        <%=lPosizione.getDescrPosizioneGiuridica()%>
        <%}%>
        </font>
      </td>
    </tr>

    <%
    if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
    {
	      if(lAltraCausa.getIstitutoDetenzione() != null )
	      {
	      %>
		      <tr>
		        <td class="l">Detenuto presso </td>
		        <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
		           di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
		        </td>
		      </tr>
		        <%if (lAltraCausa.getAltroLuogo()!=null) { %>
		          <tr>
		            <td class="l">Altro Luogo </td>
		            <td class="L" colspan=5>
		              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
		            </td>
		          </tr>
	        <% }
	      }  //
    }
    else if(lLuogoDetenzione.getIstitutoDetenzione() != null )
    { // non detenuto altra causa
    %>
	    <tr>
	      <td class="l">Detenuto presso </td>
	      <td class="L" colspan=5>
	        <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
	            di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
	      </td>
	    </tr>
    <% } %>



    <%
    // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
    if(    lPosizione.getCodPosizioneGiuridica() != null
       && (   lPosizione.getCodPosizioneGiuridica().equals("02")
           || lPosizione.getCodPosizioneGiuridica().equals("04")
          )
      )
    {
      if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
      <tr>
        <td class="l">Indirizzo</td>
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
        </td>
       </tr>
       <% }
    }
    %>
  </table>

<%
//==============================================================================
//             Sezione contenente gli estremi del provvedimento
//==============================================================================
%>
<table width=95%>
  <tr>
    <td class="Titolo" colspan=10> Provvedimento</td>
  </tr>
  <tr>
    <td class="l" colspan="2">Provvedimento :&nbsp;</td>
    <td class="L" colspan="3">
      <% if (  aEventoAltraAut.getIdEvento()==null) 
         {%>
      			<font class="campo">D'ufficio</font>
      <% }
         else 
         { %>
      	    	<font class="campo">In esecuzione di provvedimento altro Ufficio</font>
      <% } %>
    </td>
    <% if (  aEventoAltraAut.getIdEvento()==null) 
       {%>
		    <td class="l" colspan="2">Oggetto :&nbsp;</td>
		    <td class="L" colspan="3">
		      <font class="campo"><%=StringUtils.toStringJSP(aEventoComputo.getDescrMotivo())%></font>
		    </td>
    <% } %>
  </tr> 
   
	 <%if(Note != "")
	   {	 %> 
	  	<tr>
	    	<td class="l" colspan="2">Note :&nbsp;</td>
	    	<td class="L" colspan="100%">
	      		<font class="campo"><%=Note%></font>
	    	</td>
	  	</tr>
	<% } %>	
  
  <tr>
    <td class="l" colspan="2">Data Emissione</td>
    <td class="L" colspan="2">
    	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "dd-MM-yyyy") )%></font>
    </td>
    <%if(aEventoComputo.getCodMagistrato().equals("-"))
      {%>
		    <td class="l" colspan="3"> Funzionario Firmatario&nbsp;</td>
		    <td class="L" colspan="3">
		    	<font class="campo"><%=StringUtils.toStringJSP(aEventoComputo.getCognomeSoggettoPresentante())%></font>
		        <font class="campo"><%=StringUtils.toStringJSP(aEventoComputo.getNomeSoggettoPresentante())%></font>
		    </td>
<%	  }
      else
      {		          
    		if(magistrato != null && magistrato.getCodMagistrato()!=null && !magistrato.getCodMagistrato().equals(""))
      		{%>
				    <td class="l" colspan="3">Magistrato Firmatario&nbsp;</td>
				    <td class="L" colspan="3">
				       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
				       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
				    </td>
  <%  		}
      }	%>
  </tr>  
</table>

<% if (  aEventoAltraAut.getIdEvento()!=null) 
   {%>
			<table style="width: 95%;">
			  <!-- Sezione con i dati del provvedimentio Altra Autorità -->
			  <tr>
			    <td colspan="100%" class="titolo">Dati Provvedimento Altra Autorità</td>
			  </tr>
			</table>

			<table width="100%">  	 
        		<tr>
          			<td class="l" colspan="3">Comunicazione emessa da :&nbsp;</td>
          			<td class="l" colspan="7">UFFICIO RECUPERO CREDITI &nbsp;
            			<font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrUfficioEmittente(),"")%></font>
            			di 
            			<font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrLuogoEmittente(),"")%></font>
          			</td>
         		</tr>  
   	 <%		if(Sezione != "")
	   		{	 %>   
	   			 <tr>	     
		          	<td class="l" colspan="3">Sezione &nbsp;</td>
		          	<td class="l" colspan="7">
		           		<font class="campo"><%= Sezione%></font>
		          	</td>
		           </tr>	
	<%		} %>	           	
       
        <tr>
          <td class="l" colspan="3" width="30%">Data ricezione Comunicazione</td>
          <td class="l" colspan="2" width="20%"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataRicezioneAtti(), "dd-MM-yyyy") )%></font></td>

          <td class="l" colspan="3" width="30%">Data emissione Comunicazione</td>
          <td class="l" colspan="2" width="20%"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoAltraAut.getDataEmissione(), "dd-MM-yyyy") )%></font></td>
		</tr>
		<tr>          
          <td class="l" colspan="3" width="30%">Anno / Numero Partita di Credito</td>
          <td class="l" colspan="2" width="20%">
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getAnnoProtocollo(),"")%></font>/
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getProgrProtocollo(),"")%></font>&nbsp;&nbsp;
          </td>
        <%if(ExCampione !="")
          {	%>
                <td class="l" colspan="3" width="30%">Numero Ex Campione </td>
          		<td class="l" colspan="2" width="20%">
            		<font class="campo"><%=ExCampione%></font>
            	</td>
   <%	  } %>         		
        </tr>
    
        <tr>
          <td class="l" colspan="3">Tipo Provvedimento</td>
          <td class="l" colspan="7">
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrTipoProvvedimento(),"")%></font>
          </td>
        </tr>

        <tr>
          <td class="l" colspan="3">Oggetto Provvedimento</td>
          <td class="l" colspan="7">
            <font class="campo"><%=StringUtils.toStringJSP(aEventoAltraAut.getDescrMotivo(),"")%></font>
          </td>
        </tr>
      </table>

<% } %>

<%
//==============================================================================
//                Sezione contenente le Annotazioni inserite
//==============================================================================
%>
<br>

<% if (annotazioni.size()>0) { %>
<table>
<%
  //===================================================
  // Primo ciclo per caricare i quantum in detrazione
  //===================================================
  int lConta = 0;
  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);
    
    if (lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("-"))
    {
      lConta = lConta + 1;
    %>
    
    <% if (lConta==1) { %>
    <tr>
      <td class="Titolo"  colspan="100%"> Quantum in Detrazione </td>
    </tr>
    <% } %>

    <tr>
      <!-- Reclusione / Multa -->
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>

      <!-- Arresto/Ammenda -->
      <td class="l"><font class="label">Arresto / Ammenda : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda()))%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" colspan="100%"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
    </tr>
    
  <%
      } // end if in detrazione
   } // end while
  %>
  
  <%
  //===================================================
  // Secondo ciclo per caricare i quantum in Aumento
  //===================================================
  lConta = 0;
  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);
    
    if (lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lConta = lConta + 1;
    %>
    
    <% if (lConta==1) { %>
    <tr>
      <td class="Titolo"  colspan="100%"> Quantum in Aumento </td>
    </tr>
    <% } %>

    <tr>
      <!-- Reclusione / Multa -->
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>

      <!-- Arresto/Ammenda -->
      <td class="l"><font class="label">Arresto / Ammenda : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda()))%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Motivazioni :</td>
      <td class="l" colspan="100%"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
    </tr>
    
  <%
      } // end if in detrazione
   } // end for
  %>
  
  <%
  //=================================================================
  // Calcolo e Visualizzazione del saldo (in detrazione/in aumento)
  //=================================================================
  CalendarModel lCalTotaleAggregatoRec = new CalendarModel();
  CalendarModel lCalTotaleAggregatoArr = new CalendarModel();
  CalendarUtil lCalUtil = new CalendarUtil();

  for (int i=0; i< annotazioni.size(); i++)
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)annotazioni.elementAt(i);


    // Reclusione/Multa    
    CalendarModel lCalReclusione = new CalendarModel();
    lCalReclusione = lAnnMod.getQuantumReclusione();
    if (lAnnMod.getImportoMulta() != null)
      lCalReclusione.setImportoMulta(lAnnMod.getImportoMulta().doubleValue());

    if (lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lCalTotaleAggregatoRec = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoRec,lCalReclusione);
    } 
    else 
    {
      lCalTotaleAggregatoRec = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoRec,lCalReclusione);
    }
    
    // Arresti/Ammenda
    CalendarModel lCalArresti = new CalendarModel();
    lCalArresti = lAnnMod.getQuantumArresto();
    if (lAnnMod.getImportoAmmenda() != null)
      lCalArresti.setImportoAmmenda(lAnnMod.getImportoAmmenda().doubleValue());
      
    
    if (lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+"))
    {
      lCalTotaleAggregatoArr = lCalUtil.sommaGiornieValute(lCalTotaleAggregatoArr,lCalArresti);
    } 
    else 
    {
      lCalTotaleAggregatoArr = lCalUtil.sottraiGiorniValuteNew(lCalTotaleAggregatoArr,lCalArresti);
    }    
    
  }    
  %>
  
  <tr>
    <td class="Titolo"  colspan="100%"> Per un totale da computare di  </td>
  </tr>
  
  <tr>
    <td class="l"><font class="label">Reclusione / Multa : </font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumAnni()))%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumMesi()))%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoRec.getNumGiorni()))%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(new BigDecimal(lCalTotaleAggregatoRec.getImportoMulta())))%></font></td>

    <td class="l"><font class="label">Arresto / Ammenda :</font></td>
    <td class="l"><font class="label">Anni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumAnni()))%></font></td>
    <td class="l"><font class="label">Mesi</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumMesi()))%></font></td>
    <td class="l"><font class="label">Giorni</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lCalTotaleAggregatoArr.getNumGiorni()))%></font></td>
    <td class="l"><font class="label">Importo</font></td>
    <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(new BigDecimal(lCalTotaleAggregatoArr.getImportoAmmenda())))%></font></td>
  </tr>
</table>

<% } %>

<%
//==============================================================================
//  ciclo per la visualizzazione delle licenze (scomputo permesso)
//==============================================================================
if (licenze.size()>0)
{
  int lTotGiorni = 0;
  for (int i=0; i<licenze.size(); i++){
    LicenzaLibAnticipataModel lLicModel = (LicenzaLibAnticipataModel) licenze.elementAt(i);
    lTotGiorni = lTotGiorni + lLicModel.getNumeroGiorni().intValue();
  }
  %>
  <table>
    <tr>
      <td class="Titolo"  colspan="100%"> Totale giorni scomputati </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(new BigDecimal(lTotGiorni))%></font>&nbsp;</td>
    </tr>
  </table>
  <%
}
%>


<%
//==============================================================================
// Sezione contenente la Pena Ricalcolata (se presente e associata al provvedimento)
// e l'eventuale fungibilità
//==============================================================================
%>
<% if (penaresidua != null && penaresidua.getIdPenaResidua() != null) { %>
  <table>
    <tr>
      <td class="Titolo"  colspan=10> Pena Rideterminata </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoMulta()))%></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Arresto / Ammenda :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font></td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(penaresidua.getImportoAmmenda()))%></font></td>
    </tr>
  </table>
  
  <table>
    <%if(penaresidua.getDataInizio() != null) {%>
    <tr>
      <td class="l"><font class="label">Data Decorrenza Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFineReclusione() != null) {%>
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <%
    }
  
    if(penaresidua.getDataInizioArresto() != null) {%>
    <tr>
      <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
        </font>
      </td>
    <%
    }
  
    if(penaresidua.getDataFine() != null || penaresidua.getDataFinePresunta()!= null) {%>
      <td class="l"><font  class="label">Data Fine Pena : </font></td>
      <td class="l">
        <font class="campo">
        <%if(penaresidua.getDataFine() != null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"),"-")%>
        <%} else if(penaresidua.getDataFinePresunta()!= null){%>
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"),"-")%>
        <%}%>
        </font>
      </td>
    </tr>
    <% } %>
  </table>
  <%
  CalendarUtil lCalendarUtil = new CalendarUtil();
  if ( !lCalendarUtil.isZero(fungibilita.getQuantumFungibilita() ) ) {
  %>
  <table>
    <tr>
      <td class="Titolo" colspan=9><font  class="label">Pena Espiata In Eccesso</font></td>
      <%
        String GGFung = fungibilita.getNumGiorni()+"";
        String MMFung = fungibilita.getNumMesi()+"";
        String AAFung = fungibilita.getNumAnni()+"";
      %>
      <td class="l"><font class="label">Anni</font></td>
      <td class="l"><font class="Campo"><%=AAFung%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="l"><font class="Campo"><%=MMFung%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="l"><font class="Campo"><%=GGFung%></font></td>
    </tr>
  </table>
 <% } %>

  
<% }  // end if penaresidua != null  %>


<br>

<%
if (   aEventoComputo.getFlagDocumentoRegistrato()==null 
    || !aEventoComputo.getFlagDocumentoRegistrato().equals("A")
   ) 
{
%>
	  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
	  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
	  
	  <!-- Data per il calcolo della fungibilità: in questo caso data emissione provvedimento -->
	  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "yyyy") )%>">
	  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "MM") )%>">
	  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(aEventoComputo.getDataEmissione(), "dd") )%>">
	
	  <!-- Id dell'evento a cui collegare la pena residua, le annotazioni e la fungibilità -->
	  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(aEventoComputo.getIdEvento())%>">
	  <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="014">
	  <input type="HIDDEN" name="lFlagPage" value="ANNOTAPP">
	
	  <table>
      <tr>
      <% // Finchè non viene validato il provvedimento, consento di rieffettuare 
         // il calcolo della pena
         // Il calcolo della pena può essere effettuato una sola volta.

      if (   penaresidua==null  
          || (penaresidua != null && penaresidua.getIdPenaResidua() == null)
         ) 
      { %>
		      <td colspan="2">
		        <INPUT class="bottone" type="button" name="CALCOLO" value="Calcolo Pena" onClick="javascript:CalcoloPena();">
		      </td>
   <% } %>

    </tr>
  </table>
</form>
<%} %>

 <br>
  <div align=left style="visibility:hidden" id="upld">
	
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        	<tr>
          		<td class="L">
            		<input  class=bottone  type="submit" value="Conferma">
       				<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActUploadRidPenaAltro">
            		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= aEventoComputo.getIdEvento() %>">
            		<input type="HIDDEN" name="flagPage" value="RP">
            		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.calcolopena.action.ActLoadDettaglioAnnotaPagamentoPP">
          		</td>
        	</tr>
      </table>
	</form>
  </div>
  
</body>
</html>