<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua" %>

<%
//==============================================================================
// Form di visualizzazione dei dati derivanti dal primo calcolo della pena
//==============================================================================
//<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

%>

<jsp:useBean id="PenaComplessivaSentenza"       scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="SanzioneSostitutivaInSentenza" scope="request" class="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel" />

<jsp:useBean id="BeneficiConcessiReclusione"  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiConcessiArresto"     scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiRevocatiReclusione"  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="BeneficiRevocatiArresto"     scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="MisCauComputabiliReclusione" scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliArresto"    scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="MisCauComputabiliMisSicApplicata" 		  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliArrestiDomiciliari" 	  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliPermanenzaInCasa" 	  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliCollocamentoInComunita" scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliCameraDiSicurezza" 	  scope="request" class="siap.sico.calendar.model.CalendarModel" />
<jsp:useBean id="MisCauComputabiliPeriodoMessaAllaProva"  scope="request" class="siap.sico.calendar.model.CalendarModel" />

<jsp:useBean id="PenaComplessiva" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<jsp:useBean id="DataInizioPena"     scope="request" class="java.util.Date" />
<jsp:useBean id="DataFineReclusione" scope="request" class="java.util.Date" />
<jsp:useBean id="DataInizioArresto"  scope="request" class="java.util.Date" />
<jsp:useBean id="DataFinePena"       scope="request" class="java.util.Date" />


<jsp:useBean id="VedoJSP"               scope="request" class="java.lang.String" />
<jsp:useBean id="vedoDataIntermedia"    scope="request" class="java.lang.String" />
<jsp:useBean id="FlagAltraCausa"        scope="request" class="java.lang.String" />
<jsp:useBean id="CodPosizioneGiuridica" scope="request" class="java.lang.String" />
<jsp:useBean id="Segnalazione"          scope="request" class="java.lang.String" />
<% // ?????????????%>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="EsisteTrasmissioneAtti" scope="request" class="java.lang.String" />


<%
BigDecimal totalegiornilibanticipata    = (BigDecimal) request.getAttribute("totalegiornilibanticipata");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>" ></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>
    <script language="JavaScript">
     
    function Verify()
    {
      if (document.f.GPV.value!="" && document.f.MPV.value!="" && document.f.APV.value!="")
      {
        if (document.f.GPV.value.length==1)
          document.f.GPV.value='0'+document.f.GPV.value;
        if (document.f.MPV.value.length==1)
          document.f.MPV.value='0'+document.f.MPV.value;

        var data_to_verify = document.f.GPV.value+'/'+document.f.MPV.value+'/'+document.f.APV.value;
        
        if (data_to_verify.length>4)
        {
          if (!ControllaData(data_to_verify) )
          {
            alert('Data di Decorrenza non valida');
            return false;
          } else
            return true;
        }
      }else
        return true;
    }
    </script>
    <title>[S.I.E.S.] - Calcolo Pena</title>
  </head>
  
 <% CalendarUtil lCal=new CalendarUtil(); %>
 
  <body class="corpo">
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
        <td class=LBG>
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Calcolo Pena</font>
        </td>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    
    <table>
      <tr>
          <td class="Titolo" colspan=9><font  class="label">Pena Complessiva in Sentenza</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumAnniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumMesiReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumGiorniReclusione(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessivaSentenza.getImportoMulta())%></font></td>
      </tr>

      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumAnniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumMesiArresto(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaComplessivaSentenza.getNumGiorniArresto(),"0")%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessivaSentenza.getImportoAmmenda())%></font></td>
      </tr>

      <!--
      ==========================================================================
                                 BENEFICI CONCESSI
      ==========================================================================
      -->
      <% if (! (   lCal.isZero(BeneficiConcessiReclusione) 
                && BeneficiConcessiReclusione.getImportoMulta()==0 
                && lCal.isZero(BeneficiConcessiArresto) 
                && BeneficiConcessiReclusione.getImportoAmmenda()==0)
               )
      { %>
      <tr>
        <td class="Titolo" colspan=9><font  class="label">Benefici Concessi in Sentenza</font></td>
      </tr>
      <% if (!(lCal.isZero(BeneficiConcessiReclusione) && BeneficiConcessiReclusione.getImportoMulta()==0 ))
      { %>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumAnni()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumMesi()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiReclusione.getNumGiorni()%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiConcessiReclusione.getImportoMulta()))%></font></td>
      </tr>
      <% } %>
      
      <% if (!( lCal.isZero(BeneficiConcessiArresto) && BeneficiConcessiArresto.getImportoAmmenda()==0))
      { %>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumAnni()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumMesi()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="campo"><%=BeneficiConcessiArresto.getNumGiorni()%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiConcessiArresto.getImportoAmmenda())) %></font></td>
      </tr>
      <% } %>
      <% } %>
      
      
      <!--
      ==========================================================================
                                 BENEFICI REVOCATI
      ==========================================================================
      -->
      <% if ( !(   lCal.isZero(BeneficiRevocatiReclusione) 
                && BeneficiRevocatiReclusione.getImportoMulta()==0 
                && lCal.isZero(BeneficiRevocatiArresto) 
                && BeneficiRevocatiArresto.getImportoAmmenda()==0
               )
            )
      { %>
      <tr>
        <td class="Titolo" colspan="9"><font  class="label">Benefici Revocati in Sentenza</font></td>
      </tr>
      <% if (!(lCal.isZero(BeneficiRevocatiReclusione) && BeneficiRevocatiReclusione.getImportoMulta()==0 ))
      { %>
      <tr>
        <td class="l"><font class="label">Reclusione / Multa :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumAnni()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumMesi()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiReclusione.getNumGiorni()%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiRevocatiReclusione.getImportoMulta()))%></font></td>
      </tr>
      <% } %>
      
      <% if (!( lCal.isZero(BeneficiRevocatiArresto) && BeneficiRevocatiArresto.getImportoAmmenda()==0))
      { %>
      <tr>
        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumAnni()%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumMesi()%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="campo"><%=BeneficiRevocatiArresto.getNumGiorni()%></font></td>
        <td class="l"><font class="label">Importo</font></td>
        <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(new BigDecimal(BeneficiRevocatiArresto.getImportoAmmenda()))%></font></td>
      </tr>
      <% } %>
    <% } %>
  
      
      <%
      //========================================================================
      // Aggiungo l'eventuale Sanzione Sostitutiva In sentenza
      //========================================================================
      %>
      <%
      if (SanzioneSostitutivaInSentenza!=null && SanzioneSostitutivaInSentenza.getIdSanzioneSostitutiva()!=null)
      {
      %>
      <tr>
        <td class="Titolo" colspan="9"><font  class="label">Sanzione Sostitutiva Applicata in Sentenza</font></td>
      </tr>
      <tr>
        <td class="l"><%=SanzioneSostitutivaInSentenza.getDescrTipoSanzione()%> : &nbsp;</td>
        <% 
        if (   SanzioneSostitutivaInSentenza.getCodTipoSanzione().equals("S")
            || SanzioneSostitutivaInSentenza.getCodTipoSanzione().equals("L")
            || SanzioneSostitutivaInSentenza.getCodTipoSanzione().equals("E")
           ) 
        { 
        %>
        <td class="l"><font class="label">Anni</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(SanzioneSostitutivaInSentenza.getNumAnni(),"0")%></font></td>
        <td class="l"><font class="label">Mesi</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(SanzioneSostitutivaInSentenza.getNumMesi(),"0")%></font></td>
        <td class="l"><font class="label">Giorni</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(SanzioneSostitutivaInSentenza.getNumGiorni(),"0")%></font></td>
        <% } else if (SanzioneSostitutivaInSentenza.getCodTipoSanzione().equals("P")) {%>
          <td class="l" colspan="100%"><font class="l">Importo</font>
          <% if (   SanzioneSostitutivaInSentenza.getSanzionePecuniariaMulta()!=null
                 && SanzioneSostitutivaInSentenza.getSanzionePecuniariaMulta().intValue()!=0)
             {%>
            <font class="l">Multa&nbsp;</font><font class="campo" ><%=StringUtils.toEuroFormat(SanzioneSostitutivaInSentenza.getSanzionePecuniariaMulta())%>&nbsp;</font>
          <% } 
          
          if (   SanzioneSostitutivaInSentenza.getSanzionePecuniariaAmmenda()!=null
              && SanzioneSostitutivaInSentenza.getSanzionePecuniariaAmmenda().intValue()!=0)
          {%>
            <font class="l">Ammenda&nbsp;</font><font class="campo" ><%=StringUtils.toEuroFormat(SanzioneSostitutivaInSentenza.getSanzionePecuniariaAmmenda())%>&nbsp;</font>
          <% } %>
          </td>
          <% } %>
      </tr>
      
      <% } %>
      
      <% //===============================
         // Aggiungo Misure Cautelari 
         //===============================  
      %>
<%
      //if (!(lCal.isZero(MisCauComputabiliReclusione) && lCal.isZero(MisCauComputabiliArresto) ))
      if (!lCal.isZero(MisCauComputabiliReclusione) || !lCal.isZero(MisCauComputabiliArresto) ||
    	  !lCal.isZero(MisCauComputabiliMisSicApplicata) || !lCal.isZero(MisCauComputabiliArrestiDomiciliari) ||
    	  !lCal.isZero(MisCauComputabiliPermanenzaInCasa) || !lCal.isZero(MisCauComputabiliCollocamentoInComunita) ||
    	  !lCal.isZero(MisCauComputabiliCameraDiSicurezza) || !lCal.isZero(MisCauComputabiliPeriodoMessaAllaProva)
          )
      {
%>
	      <tr>
	        <td class="Titolo" colspan="9"><font  class="label">Misure Cautelari Computabili</font></td>
	      </tr>

<%
	      if (!lCal.isZero(MisCauComputabiliReclusione) || !lCal.isZero(MisCauComputabiliMisSicApplicata) )
	      {
%>
		      <tr>
		        <td class="Titolo"><font class="label">Misure Detentive</font></td>
		        <td class="Titolo" colspan="8">&nbsp;</td>
		      </tr>
<%	    	  
	    	  // Custodia cautelare in carcere
		      if (!lCal.isZero(MisCauComputabiliReclusione))
		      {
%>
		      <tr>
		        <td class="l"><font class="label">Custodia cautelare in carcere : </font></td>
		        <td class="l"><font class="label">Anni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumAnni()%></font></td>
		        <td class="l"><font class="label">Mesi</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumMesi()%></font></td>
		        <td class="l"><font class="label">Giorni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliReclusione.getNumGiorni()%></font></td>
		      </tr>
<%
		      }

		      // Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria
		      if (!lCal.isZero(MisCauComputabiliMisSicApplicata))
		      {
%>
		      <tr>
		        <td class="l"><font class="label">Custodia Cautelare in Misura di Sicurezza Applicata in via Provvisoria : </font></td>
		        <td class="l"><font class="label">Anni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliMisSicApplicata.getNumAnni()%></font></td>
		        <td class="l"><font class="label">Mesi</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliMisSicApplicata.getNumMesi()%></font></td>
		        <td class="l"><font class="label">Giorni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliMisSicApplicata.getNumGiorni()%></font></td>
		      </tr>
<%
		      }
          } // Fine Misure Detentive
	      

		  if (!lCal.isZero(MisCauComputabiliArresto) || !lCal.isZero(MisCauComputabiliArrestiDomiciliari) || 
			  !lCal.isZero(MisCauComputabiliPermanenzaInCasa) || !lCal.isZero(MisCauComputabiliCollocamentoInComunita) ||
			  !lCal.isZero(MisCauComputabiliCameraDiSicurezza)	  
			 )
		  {
%>
		    <tr>
		      <td class="Titolo"><font class="label">Misure Non Detentive</font></td>
		      <td class="Titolo" colspan="8">&nbsp;</td>
		    </tr>
<%	    	  
			// Custodia cautelare in Arresti domiciliari
	      	if (!lCal.isZero(MisCauComputabiliArresto))
	      	{
%>
	        <tr>
	          <td class="l"><font  class="label">Custodia Cautelare in Arresti Domiciliari :</font></td>
	          <td class="l"><font class="label">Anni</font></td>
	          <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumAnni()%></font></td>
	          <td class="l"><font class="label">Mesi</font></td>
	          <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumMesi()%></font></td>
	          <td class="l"><font class="label">Giorni</font></td>
	          <td class="l"><font class="campo"><%=MisCauComputabiliArresto.getNumGiorni()%></font></td>
	        </tr>
<%
	      	}

		    // Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr 309/90
		    if (!lCal.isZero(MisCauComputabiliArrestiDomiciliari))
		    {
%>
		    <tr>
		      <td class="l"><font class="label">Custodia Cautelare in Regime di Arresti Domiciliari ex art 89 dpr 309/90 : </font></td>
		      <td class="l"><font class="label">Anni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliArrestiDomiciliari.getNumAnni()%></font></td>
		      <td class="l"><font class="label">Mesi</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliArrestiDomiciliari.getNumMesi()%></font></td>
		      <td class="l"><font class="label">Giorni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliArrestiDomiciliari.getNumGiorni()%></font></td>
		    </tr>
<%
		    }
		    
		    // Custodia Cautelare in Regime di Permanenza in Casa
		    if (!lCal.isZero(MisCauComputabiliPermanenzaInCasa))
		    {
%>
		    <tr>
		      <td class="l"><font class="label">Custodia Cautelare in Regime di Permanenza in Casa : </font></td>
		      <td class="l"><font class="label">Anni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliPermanenzaInCasa.getNumAnni()%></font></td>
		      <td class="l"><font class="label">Mesi</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliPermanenzaInCasa.getNumMesi()%></font></td>
		      <td class="l"><font class="label">Giorni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliPermanenzaInCasa.getNumGiorni()%></font></td>
		    </tr>
<%
		    }		    
		    
		    // Custodia Cautelare in Collocamento in Comunita'
		    if (!lCal.isZero(MisCauComputabiliCollocamentoInComunita))
		    {
%>
		    <tr>
		      <td class="l"><font class="label">Custodia Cautelare in Collocamento in Comunita' : </font></td>
		      <td class="l"><font class="label">Anni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCollocamentoInComunita.getNumAnni()%></font></td>
		      <td class="l"><font class="label">Mesi</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCollocamentoInComunita.getNumMesi()%></font></td>
		      <td class="l"><font class="label">Giorni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCollocamentoInComunita.getNumGiorni()%></font></td>
		    </tr>
<%
		    }		    
		    
		    // Custodia cautelare in Camera di Sicurezza
		    if (!lCal.isZero(MisCauComputabiliCameraDiSicurezza))
		    {
%>
		    <tr>
		      <td class="l"><font class="label">Custodia cautelare in Camera di Sicurezza : </font></td>
		      <td class="l"><font class="label">Anni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCameraDiSicurezza.getNumAnni()%></font></td>
		      <td class="l"><font class="label">Mesi</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCameraDiSicurezza.getNumMesi()%></font></td>
		      <td class="l"><font class="label">Giorni</font></td>
		      <td class="l"><font class="campo"><%=MisCauComputabiliCameraDiSicurezza.getNumGiorni()%></font></td>
		    </tr>
<%
		    }		    
		    
		  }	// Fine Misure non Detentive 
		  
	      if (!lCal.isZero(MisCauComputabiliPeriodoMessaAllaProva) )
	      {
%>
		      <tr>
		        <td class="Titolo"><font class="label">Computo Periodo Messo alla Prova</font></td>
		        <td class="Titolo" colspan="8">&nbsp;</td>
		      </tr>
<%	    	  
	    	  // Computo Periodo di Messa alla Prova
		      if (!lCal.isZero(MisCauComputabiliPeriodoMessaAllaProva))
		      {
%>
		      <tr>
		        <td class="l"><font class="label">Computo Periodo di Messa alla Prova : </font></td>
		        <td class="l"><font class="label">Anni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliPeriodoMessaAllaProva.getNumAnni()%></font></td>
		        <td class="l"><font class="label">Mesi</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliPeriodoMessaAllaProva.getNumMesi()%></font></td>
		        <td class="l"><font class="label">Giorni</font></td>
		        <td class="l"><font class="campo"><%=MisCauComputabiliPeriodoMessaAllaProva.getNumGiorni()%></font></td>
		      </tr>
<%
		      }

          } // Fine Computo Periodo di Messa alla Prova
		  
      }
%>
  </table>
  
  <!-- 
  ==============================================================================
                              SEZIONE CON I QUANTUM  
  ==============================================================================
  -->
  <table>
    <tr>
      <td width="100%" colspan="2">
      <table width="100%">
        <tr>
          <td class="Titolo" colspan="9"><font  class="label">Pena Residua</font></td>
	      </tr>
<%
        //======================================================================
        // Se quantum <=0 visualizzo i quantum in Rosso
        //======================================================================
        if (   Segnalazione.equals("S") 
            && (   !lCal.isPositiveTime(PenaComplessiva.getQuantumReclusione())
                || !lCal.isPositiveTime(PenaComplessiva.getQuantumReclusione())
               )
           )
        {%>
        <tr>
          <td class="l"><font  class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font  class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="lRosso"><font class="lRosso"><%=PenaComplessiva.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="lRosso"><font class="lRosso"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmenda())%></font></td>
        </tr>
<%
      }
      else
      {  // Quantum rideterminati Positivi o nulli
%>
        <tr>
          <td class="l"><font class="label">Reclusione / Multa : </font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumAnniReclusione()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumMesiReclusione()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumGiorniReclusione()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMulta())%></font></td>
        </tr>
        <tr>
          <td class="l"><font class="label">Arresto / Ammenda :</font></td>
          <td class="l"><font class="label">Anni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumAnniArresto()%></font></td>
          <td class="l"><font class="label">Mesi</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumMesiArresto()%></font></td>
          <td class="l"><font class="label">Giorni</font></td>
          <td class="r"><font class="campo"><%=PenaComplessiva.getNumGiorniArresto()%></font></td>
          <td class="l"><font class="label">Importo</font></td>
          <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmenda())%></font></td>
        </tr>
<%
        //======================================================================
        // I giorni di LA vengono visualizzati SOLO se computati sul fine pena
        //======================================================================
        if(VedoJSP.equals("S") && totalegiornilibanticipata.compareTo(new BigDecimal(0)) != 0)
        {
        %>
          <tr>
            <td class="l" colspan=9>
              <font class="label">Giorni di Liberazione Anticipata Concessi già detratti:</font>
              <font class="campo"><%=totalegiornilibanticipata%></font>
            </td>
          </tr>
        <%
        }
        %>
      </table>
      </td>
    </tr>
  </table>


  <!-- NEW! 3.0 Visualizzazione delle Sanzioni Sostitutive da eseguire -->
  <% 
  if (   PenaComplessiva.getFlagSanzioneSostitutiva()!=null 
      && PenaComplessiva.getFlagSanzioneSostitutiva().equals("S")
      && PenaComplessiva.getCodTipoSanzione()!=null
      && (   PenaComplessiva.getCodTipoSanzione().equals("S")
          || PenaComplessiva.getCodTipoSanzione().equals("L")
          //|| PenaComplessiva.getCodTipoSanzione().equals("E")
         )
     ) 
  { %>
  <table>
    <tr>
      <td class="Titolo" colspan="9"><font class="label">Sanzione Sostitutiva da Eseguire</font></td>
    </tr>
    <tr>
    <% if (PenaComplessiva.getCodTipoSanzione().equals("S")) { %>
      <td class="l">Semidetenzione : &nbsp;</td>
    <% } else if (PenaComplessiva.getCodTipoSanzione().equals("L")) {  %>
      <td class="l">Libertà Controllata : &nbsp;</td>
    <% } else if (PenaComplessiva.getCodTipoSanzione().equals("P")) {  %>
      <td class="l">Pena Pecuniaria : &nbsp;</td>
    <% } else if (PenaComplessiva.getCodTipoSanzione().equals("E")) {  %>
      <td class="l">Espulsione: &nbsp;</td>
    <% }%>

    <% 
    if (   PenaComplessiva.getCodTipoSanzione().equals("S")
        || PenaComplessiva.getCodTipoSanzione().equals("L")
        || PenaComplessiva.getCodTipoSanzione().equals("E")
       ) 
    { %>
      <td class="l"> &nbsp;
        <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaComplessiva.getNumAnniSS(),"0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaComplessiva.getNumMesiSS(),"0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(PenaComplessiva.getNumGiorniSS(),"0")%></font>
      </td>
    <% } else if (PenaComplessiva.getCodTipoSanzione().equals("P")) {%>
      <td class="l">
        <font class="l">Importo&nbsp;</font>
        <% if (PenaComplessiva.getImportoMultaSS()!=null) { %>
        <font class="l">Multa&nbsp;</font>
        <font class="campo" ><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoMultaSS())%>&nbsp;</font>
        <%}%>
        <% if (PenaComplessiva.getImportoAmmendaSS()!=null) { %>
        <font class="l">Ammenda&nbsp;</font>
        <font class="campo" ><%=StringUtils.toEuroFormat(PenaComplessiva.getImportoAmmendaSS())%>&nbsp;</font>
        <%}%>
      </td>
    <% } %>
    </tr>
  </table>
  <% } %>

<!-- 
================================================================================
                        SEZIONE CON DECORRENZA E SCADENZA
 E tasto Valida Fine Pena                        
================================================================================
-->
<FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActInserisciPenaValidata">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
  <input type="HIDDEN" name="IdPenaResidua"   value="<%=PenaComplessiva.getIdPenaResidua()%>">
  
  <input type="HIDDEN" name="Gdatainiziopena" value="<%=DateUtils.getDayToString(DataInizioPena)%>">
  <input type="HIDDEN" name="Mdatainiziopena" value="<%=DateUtils.getMonthToString(DataInizioPena)%>">
  <input type="HIDDEN" name="Adatainiziopena" value="<%=DateUtils.getYearToString(DataInizioPena)%>">

  <input type="HIDDEN" name="Gdatafinepenapresunta" value="<%=DateUtils.getDayToString(DataFinePena)%>">
  <input type="HIDDEN" name="Mdatafinepenapresunta" value="<%=DateUtils.getMonthToString(DataFinePena)%>">
  <input type="HIDDEN" name="Adatafinepenapresunta" value="<%=DateUtils.getYearToString(DataFinePena)%>">
  
  <input type="HIDDEN" name="Grec"  value="<%=PenaComplessiva.getNumGiorniReclusione()%>">
  <input type="HIDDEN" name="Mrec"  value="<%=PenaComplessiva.getNumMesiReclusione()%>">
  <input type="HIDDEN" name="Arec"  value="<%=PenaComplessiva.getNumAnniReclusione()%>">
  <input type="HIDDEN" name="Multa" value="<%=PenaComplessiva.getImportoMulta()%>">
  
  <input type="HIDDEN" name="Garr"  value="<%=PenaComplessiva.getNumGiorniArresto()%>">
  <input type="HIDDEN" name="Marr"  value="<%=PenaComplessiva.getNumMesiArresto()%>">
  <input type="HIDDEN" name="Aarr"  value="<%=PenaComplessiva.getNumAnniArresto()%>">
  <input type="HIDDEN" name="Ammenda" value="<%=PenaComplessiva.getImportoAmmenda()%>">
  
  <input type="HIDDEN" name="FlagAltraCausa" value="<%=FlagAltraCausa%>">
  <input type="HIDDEN" name="CodPosizioneGiuridica" value="<%=CodPosizioneGiuridica%>">

  <input type="HIDDEN" name="EsisteTrasmissioneAtti" value="<%=EsisteTrasmissioneAtti%>" >

  <!--  Sanzione Sostitutiva -->
  <% 
  if (   PenaComplessiva.getFlagSanzioneSostitutiva()!=null
      && PenaComplessiva.getFlagSanzioneSostitutiva().equals("S")
     )
  {
  %>
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_FLAG_SANZIONE_SOSTITUTIVA%>" value="<%=StringUtils.toStringJSP(PenaComplessiva.getFlagSanzioneSostitutiva(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_COD_TIPO_SANZIONE%>" value="<%=StringUtils.toStringJSP(PenaComplessiva.getCodTipoSanzione(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_ANNI_SS%>"   value="<%=StringUtils.toStringJSP(PenaComplessiva.getNumAnniSS(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_MESI_SS%>"   value="<%=StringUtils.toStringJSP(PenaComplessiva.getNumMesiSS(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_NUM_GIORNI_SS%>" value="<%=StringUtils.toStringJSP(PenaComplessiva.getNumGiorniSS(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_IMPORTO_MULTA_SS%>"    value="<%=StringUtils.toStringJSP(PenaComplessiva.getImportoMultaSS(),"")%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_IMPORTO_AMMENDA_SS%>"  value="<%=StringUtils.toStringJSP(PenaComplessiva.getImportoAmmendaSS(),"")%>">
    
    <% if (PenaComplessiva.getDataInizioSS()!=null) { %>
    <input type="HIDDEN" name="gg_<%=ICostantiPenaResidua.CAMPO_DATA_INIZIO_SS%>" value="<%=DateUtils.getDayToString   (PenaComplessiva.getDataInizioSS())%>">
    <input type="HIDDEN" name="mm_<%=ICostantiPenaResidua.CAMPO_DATA_INIZIO_SS%>" value="<%=DateUtils.getMonthToString (PenaComplessiva.getDataInizioSS())%>">
    <input type="HIDDEN" name="aa_<%=ICostantiPenaResidua.CAMPO_DATA_INIZIO_SS%>" value="<%=DateUtils.getYearToString  (PenaComplessiva.getDataInizioSS())%>">
    <% } %>
  
    <% if (PenaComplessiva.getDataFineSS()!=null) { %>
    <input type="HIDDEN" name="gg_<%=ICostantiPenaResidua.CAMPO_DATA_FINE_SS%>" value="<%=DateUtils.getDayToString   (PenaComplessiva.getDataFineSS())%>">
    <input type="HIDDEN" name="mm_<%=ICostantiPenaResidua.CAMPO_DATA_FINE_SS%>" value="<%=DateUtils.getMonthToString (PenaComplessiva.getDataFineSS())%>">
    <input type="HIDDEN" name="aa_<%=ICostantiPenaResidua.CAMPO_DATA_FINE_SS%>" value="<%=DateUtils.getYearToString  (PenaComplessiva.getDataFineSS())%>">
    <% } %>
 
  <% } %>
  

  <table>
  <% if (VedoJSP.equals("S")) { %>
    <tr>
      <td class="l"><font  class="label">Data Decorrenza Pena:</font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioPena,"dd-MM-yyyy"))%>
        </font>
      </td>
    <% if (vedoDataIntermedia.equals("S")) { %>
      <input type="HIDDEN" name="Gdatafinereclusione" value="<%=DateUtils.getDayToString(DataFineReclusione)%>">
      <input type="HIDDEN" name="Mdatafinereclusione" value="<%=DateUtils.getMonthToString(DataFineReclusione)%>">
      <input type="HIDDEN" name="Adatafinereclusione" value="<%=DateUtils.getYearToString(DataFineReclusione)%>">
      <input type="HIDDEN" name="Gdatainizioarresto"  value="<%=DateUtils.getDayToString(DataInizioArresto)%>">
      <input type="HIDDEN" name="Mdatainizioarresto"  value="<%=DateUtils.getMonthToString(DataInizioArresto)%>">
      <input type="HIDDEN" name="Adatainizioarresto"  value="<%=DateUtils.getYearToString(DataInizioArresto)%>">
      <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFineReclusione,"dd-MM-yyyy"))%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l"><font class="label">Data Inizio Arresto : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataInizioArresto,"dd-MM-yyyy"))%>
        </font>
      </td>
    <% } %>
      <td class="l"><font class="label">Data Fine Pena Automatica : </font></td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DataFinePena,"dd-MM-yyyy"),"-")%>
        </font>
      </td>
    </tr>

    <tr>
        <td class="l" colspan="4"><font  class="label">Data Fine Pena Manuale : </font>
           <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(DataFinePena)%>">
            /
           <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(DataFinePena)%>">
            /
           <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(DataFinePena)%>">
        &nbsp;&nbsp;&nbsp;
        </td>
    </tr>

    <tr>
      <td class="l"colspan="4"><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
    </tr>
 <% }  // end if (VedoJSP.equals("S"))%>


<%} // end dell'else quantum >=0 %>
    
  </table>
</form>

<%
  // n.b. a seguito della revisione SS il tasto non deve più essere presente
  //1==2 // commentato a seguito correzioni SS
  if (   PenaComplessiva.getFlagSanzioneSostitutiva()!=null 
      && PenaComplessiva.getFlagSanzioneSostitutiva().equals("S")
      && PenaComplessiva.getCodTipoSanzione()!=null
      && (   PenaComplessiva.getCodTipoSanzione().equals("S")
          || PenaComplessiva.getCodTipoSanzione().equals("L")
          //|| PenaComplessiva.getCodTipoSanzione().equals("E")
         )
     ) 
  { 
%>

<FORM method="POST" name="cnrp" action="<%= IWebConstants.PG_MAIN%>">
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  	<%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena"--%>
  <%
  String lAzione = "";
  if (AzioneChiamante!=null && AzioneChiamante.equals("siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena") )
  {
    lAzione = AzioneChiamante;
  }
  else if (EsisteTrasmissioneAtti!=null && EsisteTrasmissioneAtti.equals("SI")){
    // Ancora primo calcolo, ma già emessa trasmissione atti, per cui vado su 
    // Comunicazione nuovo residuo pena
    lAzione = "siap.siep.sanzionesostitutiva.action.ActLoadInserisciComunicazioneNuovoResiduoPena";
  }
  else {
    lAzione = "siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione";
  }
  %>
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%-- input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActLoadInserisciTrasmissioneAttiEsecuzione" --%>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="FromCalcoloPena" value="S">
  
  <table>
    <tr>
      <td class="l" colspan="4">
        <!--INPUT class="bottone" type="submit" name="conferma" value="Comunicazione Nuovo Residuo Pena" -->
        <INPUT class="bottone" type="submit" name="conferma" value="Conferma" >
      </td>
    </tr>
  </table>
</form>
<% } %>

</body>
<% if (VedoJSP.equals("S") && Segnalazione.equals("N")){ %>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("GPV","maxlen=2","La lunghezza massima per il Giorno Pena Validata è di 2 caratteri");
  frmvalidator.addValidation("GPV","numeric","Il campo Giorno Pena Validata deve essere numerico");
  frmvalidator.addValidation("GPV","gt=1","Il campo Giorno Pena Validata deve essere maggiore di 0");
  frmvalidator.addValidation("GPV","lt=31","Il campo Giorno Pena Validata deve essere minore di 31");
  frmvalidator.addValidation("MPV","maxlen=2","La lunghezza massima per il Mese Pena Validata è di 2 caratteri");
  frmvalidator.addValidation("MPV","numeric","Il campo Mese Pena Validata deve essere numerico");
  frmvalidator.addValidation("MPV","gt=1","Il campo Mese Pena Validata deve essere maggiore di 0");
  frmvalidator.addValidation("MPV","lt=12","Il campo Mese Pena Validata deve essere minore di 12");
  frmvalidator.addValidation("APV","maxlen=4","La lunghezza massima per l'Anno Pena Validata è di 4 caratteri");
  frmvalidator.addValidation("APV","minlen=4","La lunghezza minima per l'Anno Pena Validata è di 4 caratteri");
  frmvalidator.addValidation("APV","numeric","Il campo Anno Pena Validata deve essere numerico");
  frmvalidator.addValidation("APV","gt=1900","Il campo Anno Pena Validata deve essere maggiore di 1900");
  frmvalidator.addValidation("APV","lt=2100","Il campo Anno Pena Validata deve essere minore di 2100");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
<%}%>
</html>