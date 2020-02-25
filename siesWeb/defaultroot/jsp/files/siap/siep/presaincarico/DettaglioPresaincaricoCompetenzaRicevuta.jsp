<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.util.CalendarUtil" %>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.penacumulo.model.PenaCumuloModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.ufficio.controller.IUfficio"%>


<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="Messaggio"            scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="dettaglioFasSIEP"     scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="fascicoloCumulante"   scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="penaresiduaCumulante" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="NoProcedimento"       scope="request" class="java.lang.String"/>

<jsp:useBean id="actionConferma"       scope="request" class="java.lang.String"/>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoliInIstruttoria"  scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// JSP per la visulizzazione del dettaglio più particolareggiato del procedimento
// ricevuto e per procedere alla presa in carico (CUMULO)
//==============================================================================
%>

<%
String lTitolo="Presa in carico Atti Ricevuti per Competenza";
if(Messaggio!=null && Messaggio.getIdMessaggio()!=null && Messaggio.getCodTipoOperazione()!=null)
{
	if(Messaggio.getCodTipoOperazione().compareTo("00078")==0)
	{
		lTitolo="Presa in carico Seguito Atti Ricevuti per Competenza";
	}
}

// 26/04/2019 MEV70 Controllo presenza titolo in Istruttoria.
String stessoTitolo = ""; 
String titoloCorrente = dettaglioFasSIEP.getFascicoloSiep().getChiaveAnno()+"/"+dettaglioFasSIEP.getFascicoloSiep().getChiaveProgr(); 
Vector<TitoloCumulatoModel> VecTitCum = new Vector(ListaTitoliInIstruttoria);

%>

<html>
  <head>
    <title>[S.I.E.S.] - Trasmissione Competenza Ricevuta</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    
  </head>

  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo"><%=lTitolo%></font>
          </td>
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
         </tr>
      </table>
    </FORM>
    
    
<% if(IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null){%>
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <br>
<% } %>    
    
    
<%
  if(!NoProcedimento.equals(""))
  { 
%>
    <table cellspacing=2 cellpadding=2 width="95%">
      <tr><td class="Titolo" colspan=4>Procedimento che attribuisce la competenza al cumulo</td></tr>
    </table>

    <br><br>
    
    <table cellspacing=0 cellpadding=0 width=95%>
      <tr>
        <td class="L" width=100% colspan=4>
          <font class="cRossoCumulo"><%=NoProcedimento%></font>
        </td>
      </tr>
    </table>
 <% } %>
  
<%
  // n.b. dettaglioFasSIEP = fascicolo ricevuto in Messaggio
  FascicoloSiepModel fascicoloSIEP  = dettaglioFasSIEP.getFascicoloSiep();
  
  SoggettoModel soggettoRicevuto    = fascicoloSIEP.getSoggetto();
  SentenzaModel sentenzaRicevuta    = fascicoloSIEP.getSentenza();
  PosizioneGiuridicaModel posRicevuta = dettaglioFasSIEP.getPosizioneGiuridica();
  PenaResiduaModel penaRicevuta   = dettaglioFasSIEP.getPenaResidua();

	// 26/04/2019 MEV70 Controllo eventuale presenza del titolo esecutivo legato al Procedimento da Importare tra i Procedimenti in Cumulo.  
	stessoTitolo = ""; 
	if (VecTitCum != null) {
		for (int i=0; i<VecTitCum.size();i++){
			TitoloCumulatoModel lTitoloCumModel = (TitoloCumulatoModel) VecTitCum.elementAt(i);
	        	if (sentenzaRicevuta != null &&
	        		lTitoloCumModel.isStessoTitolo(sentenzaRicevuta) ) {
	             	stessoTitolo = lTitoloCumModel.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoloCumModel.getProcedimentoCumulato().getChiaveProgrFasCumulato();
	    			break;
	        	}
		}
	}
  
  
  SoggettoModel soggetto = new SoggettoModel();
  SentenzaModel sentenza = new SentenzaModel();

  if(fascicoloCumulante != null && fascicoloCumulante.getIdFascicoloSiep()!=null )
  { 
      SoggettoModel soggetto1 = fascicoloCumulante.getSoggetto();
      SentenzaModel sentenza1 = fascicoloCumulante.getSentenza();
      soggetto = soggetto1;
      sentenza = sentenza1;
      if(fascicoloCumulante.getChiaveAnno() != null)
      {       
%>
        <table cellspacing=2 cellpadding=2 width="95%">
          <tr><td class="Titolo" colspan=4>Procedimento che attribuisce la competenza al cumulo</td></tr>
        </table>
          
        <table cellspacing=0 cellpadding=0 width=95%>
          <tr>
            <td class="L">
              <font class="label">Procedimento : N.</font>
                <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloCumulante.getIdFascicoloSiep()%>" title="Procedimento">
                  <%=fascicoloCumulante.getChiaveAnno()%>
                  /
                  <%=fascicoloCumulante.getChiaveProgr()%>
                </a>
                &nbsp;
<%

              if(fascicoloCumulante.getFlagCumulante()!=null && fascicoloCumulante.getFlagCumulante().equals("S"))
              {
    %>
                <font class="cRossoCumulo"> &nbsp;C&nbsp; </font> &nbsp;
    <%
              }

              if(fascicoloCumulante.getCodOperatoreInserimento() != null && fascicoloCumulante.getCodOperatoreInserimento().startsWith("res-"))
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Migrato&nbsp; </font> &nbsp;
    <%
              }

              if(   fascicoloCumulante.getCodStatoFascicolo() != null
                 && (fascicoloCumulante.getCodStatoFascicolo().equals("01"))
                 )
              {
    %>
                <font class="cRossoCumulo"> &nbsp;Archiviato&nbsp;</font> &nbsp;
    <%
              }
              


              if((    penaresiduaCumulante != null
                 && penaresiduaCumulante.getFlagPenaSospesa()!= null
                 && penaresiduaCumulante.getFlagPenaSospesa().equals("S"))
                 || (     fascicoloCumulante!= null && fascicoloCumulante.getChiaveProgr() != null
                     && (fascicoloCumulante.getChiaveProgr().intValue() >= 30000
                     && fascicoloCumulante.getChiaveProgr().intValue() < 40000)))
              {
                 if( fascicoloCumulante!= null && fascicoloCumulante.getChiaveProgr() != null
                   && ( fascicoloCumulante.getChiaveProgr().intValue() >= 30000
                   &&   fascicoloCumulante.getChiaveProgr().intValue() < 40000) )
                 {%>
                      <font class="cRossoCumulo"> &nbsp;Pena Sospesa Condizionalmente&nbsp;</font>&nbsp;
<%
                   }
                   else
                   {
%>
                      <font class="cRossoCumulo"> &nbsp;Esecuzione Sospesa&nbsp;</font>&nbsp;
<%
                   }  
                   }

              if(   penaresiduaCumulante != null
                 && penaresiduaCumulante.getFlagPenaSospesa()!= null
                 && penaresiduaCumulante.getFlagPenaSospesa().equals("I"))
              {
      %>
                <font class="cRossoCumulo"> &nbsp;Pena Interrotta&nbsp;</font>&nbsp;
      <%
              }
              if(   penaresiduaCumulante != null
                 && penaresiduaCumulante.getFlagPenaSospesa()!= null
                 && penaresiduaCumulante.getFlagPenaSospesa().equals("D"))
              {
      %>
                <font class="cRossoCumulo"> &nbsp;Pena Differita&nbsp;</font>&nbsp;
      <%
              }
              
              if (!(UtenteConnesso.getUfficioUtente().getCodUfficio().equals(fascicoloCumulante.getChiaveUfficio())))
              {
    %>
                &nbsp;
                <font class="label"><%=fascicoloCumulante.getDescrTipoUfficio() + " DI " + fascicoloCumulante.getDescrComuneUfficio() %></font>
    <%
              }
%>
              </td>
          </tr>
        
        
<%        if(soggetto != null)
        { %>            
          <tr>
              <td class="L" width=100%><font class="label">Soggetto : </font>
                <font class="campo">
                <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=soggetto.getIdSoggetto()%>" title="Soggetto">
                  <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
                </a>
                </font>&nbsp;
<%
                if (soggetto.getSesso().compareTo("F")==0)
                {
%>
                    <font class="label">nata il :</font>&nbsp;
<%
                }
                else
                {
%>
                    <font class="label">nato il :</font>&nbsp;
<%
                }

            if(soggetto.getDataNascita() == null)
            {
                  if(soggetto.getDataNascitaPresunta().equals("S")) 
                  {%>
                        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
            <%    }
                  else
                  {%>
                        <font class="campo">***</font>&nbsp;
<%                }
            }
            else
            {%>
                    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%            }%>

                <font class="label">in : </font>
                <font class="campo">

 <%
                if (soggetto.getDescrComuneNascita().compareTo("-")==0)
                {
    %>
                  <%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)
    <%
                }
                else
                {
    %>
                  <%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)
    <%
                }
%>
                </font>
            </td>
          </tr>
          
        <%  } // Chiude if(soggetto != null) %>

      </table>
    
<%        if(sentenza != null)
        {
%>    
           <table cellspacing=0 cellpadding=0 width="95%"> 
              <tr>
                  <td class="L">
                    <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
                    <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
                      <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
                      <font class="label">del</font>&nbsp;
                      <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>
                    </font>
                    
                  &nbsp;<font class="label"> Emessa da: </font>
                  <font class="campo"><%=sentenza.getDescrTipoAutoritaEmittente()%></font>&nbsp;
          <%
                  if (sentenza.getNumSezioneAutoritaEmittente() != null)
                  {
%>
                      <font class="label">(Sez.</font> <font class="campo"><%=sentenza.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
                  }
%>
                    <font class="label"> di </font>
                    <font class="campo"><%=sentenza.getDescrLuogoEmittente()%></font>
                 </td>
            </tr>
            
              <tr>
                <td class="L">
                  <font class="label">Data irrevocabilità : </font>&nbsp;
                  <font class="campo"><%=DateUtils.getDateToString(fascicoloCumulante.getDataIrrevocabilita(), "dd-MM-yyyy")%></font>
                </td>
              </tr>
              
             </table>
             
 <%       } //  Chiude If SENTENZA
      
      }   // Chiude If FASCICOLOCUMULANTE.getchiaveanno
      
  }   // Chiude If FASCICOLOCUMULANTE...  
%>             

  <br>
  
<%
String lStrProcedimento = "";
if (fascicoloSIEP.getChiaveProgrOrig()!=null) {

  lStrProcedimento = fascicoloSIEP.getChiaveAnno()+"/"+fascicoloSIEP.getChiaveProgrOrig();


  BigDecimal lIncrement = null;
  lIncrement = fascicoloSIEP.getChiaveProgr().subtract (fascicoloSIEP.getChiaveProgrOrig());
  
  UfficioModel lUfficioAccorpato = new UfficioModel();
  
  try {
    IUfficio lUff = SICOLookupRemote.getUfficioRemote();
    lUfficioAccorpato = lUff.getUfficioAccorpatoByAccorpanteIncrement (fascicoloSIEP.getChiaveUfficio(), ""+lIncrement);
  } catch (Exception e) {}          
  
  lStrProcedimento += " <font class='cRosso'> (ex ";
  lStrProcedimento += " "+lUfficioAccorpato.getCodTipoUfficio()+" di "+lUfficioAccorpato.getDescrComune();
  lStrProcedimento += ") </font>";
}
else {
  lStrProcedimento = fascicoloSIEP.getChiaveAnno()+"/"+fascicoloSIEP.getChiaveProgr();
}
%>
  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="Titolo" colspan=4>Procedimento da inserire nel cumulo</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Procedimento N.</font>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          <%=lStrProcedimento%>
          - <%=fascicoloSIEP.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSIEP.getDescrComuneUfficio()%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">Data Iscrizione :</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(fascicoloSIEP.getDataIscrizione(), "dd-MM-yyyy") %>
        </font>&nbsp;
      </td>
      <td class="L"> 
        <font class="label">Data Irrevocabilità :</font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicoloSIEP.getDataIrrevocabilita(), "dd-MM-yyyy")) %>&nbsp;</font>
      </td> 
     </tr>
     
     <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Soggetto :</font>&nbsp;
        <font class="campo"><%=soggettoRicevuto.getCognome()%>&nbsp;<%=soggettoRicevuto.getNome()%></font>
        <%if (soggettoRicevuto.getSesso().compareTo("F")==0){%>&nbsp;
              <font class="label">nata il :</font>&nbsp;
        <%}
          else{%>
              <font class="label">nato il :</font>&nbsp;
      <%
          }
      
      if(soggettoRicevuto.getDataNascita() == null){
          if(soggettoRicevuto.getDataNascitaPresunta().equals("S")) {%>
              <font class="campo"><%=StringUtils.toStringJSP(soggettoRicevuto.getAnnoNascita())%></font>&nbsp;
        <%}
          else{%>
              <font class="campo">***</font>&nbsp;
        <%}
        }else{%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoRicevuto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
      <%}%>
          <font class="label">in :</font>&nbsp;
          <font class="campo"> 
          <%if (soggettoRicevuto.getDescrComuneNascita().compareTo("-")==0){%>
            <%=soggettoRicevuto.getDescComuneNascitaEstero()%>  (<%=soggettoRicevuto.getDescrStatoNascita().toUpperCase()%>)
      <%}
          else{%>
            <%=soggettoRicevuto.getDescrComuneNascita()%> (<%=soggettoRicevuto.getCodProvinciaNascita()%>)
      <%
          }%>      
          </font>
      </td>
    </tr>
  </table>
  <br>

  <table class="L" width="95%">
    <tr>
      <td class="Titolo" colspan=4>Sentenza</td>
    </tr>
    <tr>    
      <td class="L" colspan=4>
        <font class="label"><%=sentenzaRicevuta.getDescrTipoProvvedimento().substring(0,1).toUpperCase()
          +sentenzaRicevuta.getDescrTipoProvvedimento().substring(1).toLowerCase()%>
        </font>
        <font class="label"> N.</font>
        <font class="campo">
          <%=sentenzaRicevuta.getAnnoSentenza()%>/<%=sentenzaRicevuta.getNumeroSentenza()%>
        </font>
        <font class="label">del</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(sentenzaRicevuta.getDataProvvedimento(), "dd-MM-yyyy")%>
        </font>
        &nbsp;<font class="label"> Emessa da :</font>&nbsp;
        <font class="campo"><%=sentenzaRicevuta.getDescrTipoAutoritaEmittente()%></font>&nbsp;
<%
        if (sentenzaRicevuta.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=sentenzaRicevuta.getNumSezioneAutoritaEmittente()%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>&nbsp;
        <font class="campo"><%=sentenzaRicevuta.getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table>
  
<table>      

    <%
    //============================================================================
    // PENA IRROGATA IN SENTENZA
    //============================================================================
    //============================================================================
    // Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
    //============================================================================
  if (!"S".equals(dettaglioFasSIEP.getFascicoloSiep().getFlagCumulante()))
  {
      if(dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva()!=null)
      {
          PenaComplessivaSanzioneSostitutivaModel lPenaSostMod=dettaglioFasSIEP.getPenaComplessivaSanzioneSostitutiva();

          if(lPenaSostMod!=null)
          {
              PenaComplessivaModel lPenCompMod=lPenaSostMod.getPenaComplessiva();
              if(lPenCompMod!=null)
              {
              %>
                <tr>
                  <td class="L" colspan=4>
                    <font class="label">Pena irrogata in sentenza : </font>&nbsp;
                    <%
                    if (   (lPenCompMod.getNumAnniReclusione()!=null && lPenCompMod.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
                        || (lPenCompMod.getNumMesiReclusione()!=null && lPenCompMod.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
                        || (lPenCompMod.getNumGiorniReclusione()!=null && lPenCompMod.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
                       )
                    {%>
                            <font class="campo">Reclusione</font>&nbsp;
                            <font class="label">Anni</font>
                            <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniReclusione(),"0")%></font>&nbsp;
                            <font class="label">Mesi</font>
                            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiReclusione(),"0")%></font>&nbsp;
                            <font class="label">Giorni</font>
                            <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
               <%}%>

                    <%if(lPenCompMod.getImportoMulta()!=null && lPenCompMod.getImportoMulta().compareTo(new BigDecimal(0))!=0)
                      {%>
                        <font class="label">Multa </font>&nbsp;
                        <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
                    <%}%>
          
                    <%
                    if (   (lPenCompMod.getNumAnniArresto()!=null && lPenCompMod.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
                        || (lPenCompMod.getNumMesiArresto()!=null && lPenCompMod.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
                        || (lPenCompMod.getNumGiorniArresto()!=null && lPenCompMod.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
                       )
                    {%>
                        <font class="campo">Arresto</font>&nbsp;
                        <font class="label">Anni</font>
                        <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniArresto(),"0")%></font>&nbsp;
                        <font class="label">Mesi</font>
                        <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiArresto(),"0")%></font>&nbsp;
                        <font class="label">Giorni</font>
                        <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
                    <%}%>
          
                    <%if(lPenCompMod.getImportoAmmenda()!=null && lPenCompMod.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) 
                    {%>
                        <font class="label">Ammenda </font>&nbsp;
                        <font class="campo"><%=StringUtils.toEuroFormat(lPenCompMod.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
                    <%}%>
          
                    <%if (lPenCompMod.getCodTipoPenaDetentiva().equals("03") || lPenCompMod.getCodTipoPenaDetentiva().equals("04")) 
                      {%>
                          <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getDescrTipoPenaDetentiva())%></font>
                    <%    if(lPenCompMod.getCodTipoPenaDetentiva().equals("04"))
                          {%>
                        <%      if(lPenCompMod.getNumAnniIsolamentoDiurno()!=null)
                                {%>
                                    <font class="label">Anni</font>
                                    <font class="campo"><%=StringUtils.toStringJSP(lPenCompMod.getNumAnniIsolamentoDiurno(),"0")%></font>
                            <%}%>
          
                        <%      if(lPenCompMod.getNumMesiIsolamentoDiurno()!=null)
                                {%>
                                    <font class="label">Mesi</font>
                                    <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumMesiIsolamentoDiurno(),"0")%></font>
                            <%}%>

              <%                if(lPenCompMod.getNumGiorniIsolamentoDiurno()!=null)
                                {%>
                                    <font class="label">Giorni</font>
                                    <font class="campo"> <%=StringUtils.toStringJSP(lPenCompMod.getNumGiorniIsolamentoDiurno(),"0")%></font>
                            <%}%>
                          <%}%>
                     <%}%>
               </td>
            </tr>
<%
              }
        }
    }
  }  //
  else 
  {
  //============================================================================
  // Aggiunta nuova versione calcolo pena: viene visualizzata la pena in Cumulo
  //============================================================================
      if(dettaglioFasSIEP.getPenaCumulo()!=null)
      {
        PenaCumuloModel lPenaCumulo = dettaglioFasSIEP.getPenaCumulo();
      %>
      <tr>
        <td class="L" colspan=4>
          <font class="label">Pena Irrogata in Cumulo : </font>
          <%
          if (   (lPenaCumulo.getNumAnniReclusione()!=null && lPenaCumulo.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumMesiReclusione()!=null && lPenaCumulo.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumGiorniReclusione()!=null && lPenaCumulo.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Reclusione</font>&nbsp;
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniReclusione(),"0")%></font>&nbsp;
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiReclusione(),"0")%></font>&nbsp;
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniReclusione(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoMulta()!=null && lPenaCumulo.getImportoMulta().compareTo(new BigDecimal(0))!=0){%>
          <font class="label">Multa </font>&nbsp;
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoMulta())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <%
          if (   (lPenaCumulo.getNumAnniArresto()!=null && lPenaCumulo.getNumAnniArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumMesiArresto()!=null && lPenaCumulo.getNumMesiArresto().compareTo(new BigDecimal(0))!=0)
              || (lPenaCumulo.getNumGiorniArresto()!=null && lPenaCumulo.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0)
             )
          {%>
          <font class="campo">Arresto</font>&nbsp;
          <font class="label">Anni</font>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniArresto(),"0")%></font>&nbsp;
          <font class="label">Mesi</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiArresto(),"0")%></font>&nbsp;
          <font class="label">Giorni</font>
          <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniArresto(),"0")%></font>&nbsp;&nbsp;
          <%}%>

          <%if(lPenaCumulo.getImportoAmmenda()!=null && lPenaCumulo.getImportoAmmenda().compareTo(new BigDecimal(0))!=0) {%>
          <font class="label">Ammenda </font>&nbsp;
          <font class="campo"><%=StringUtils.toEuroFormat(lPenaCumulo.getImportoAmmenda())%></font>&nbsp;&euro;&nbsp;
          <%}%>

          <% if (lPenaCumulo.getCodTipoPenaDetentiva().equals("E") || lPenaCumulo.getCodTipoPenaDetentiva().equals("I")) 
          {
          %>
          <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getDescrTipoPenaDetentiva())%></font>
            <%if(lPenaCumulo.getCodTipoPenaDetentiva().equals("I")){%>
              <%if(lPenaCumulo.getNumAnniIsolamentoDiurno()!=null){%>
              <font class="label">Anni</font>
              <font class="campo"><%=StringUtils.toStringJSP(lPenaCumulo.getNumAnniIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>

              <%if(lPenaCumulo.getNumMesiIsolamentoDiurno()!=null){%>
              <font class="label">Mesi</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumMesiIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>

              <%if(lPenaCumulo.getNumGiorniIsolamentoDiurno()!=null){%>
              <font class="label">Giorni</font>
              <font class="campo"> <%=StringUtils.toStringJSP(lPenaCumulo.getNumGiorniIsolamentoDiurno(),"0")%></font>&nbsp;
              <%}%>
            <%}%>
         <%}%>
      </td>
    </tr>
<%
  }
}%>     
  </table>
  <br/>

  <table cellspacing=2 cellpadding=2 width="95%">
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Posizione Giuridica :</font>&nbsp;
          <font class="campo">
            <%=posRicevuta.getDescrPosizioneGiuridica()%>
          </font>
      </td>     
    </tr>
    <%
    // MEV 26 cumulo. I classe IV possono essere trasmessi ma non hanno la PR
    if (penaRicevuta!=null) 
    {
      penaRicevuta.calcolaStringaArresto(); 
      penaRicevuta.calcolaStringaReclusione();
      %>
    <tr>

      <%if(penaRicevuta.getStringaReclusione()!=null)
      {%>
          <td class="L"><font class="label">Reclusione</font></td>
          <td class="L"><font class="campo">
          <%=penaRicevuta.getStringaReclusione()%>&nbsp;
          </font></td>
      <%}%> 
      
      <%if(penaRicevuta.getImportoMulta().intValue() !=0 )
      {%>
          <td class="L"><font class="label">Multa</font></td>
          <td class="L"><font class="campo">
          <%=penaRicevuta.getImportoMulta()%>&nbsp;
          </font><font class="label"> Euro</font></td>
      <%}%>   
      
    </tr>
    <tr>

      <%if(penaRicevuta.getStringaArresto()!=null  )
      {%>
            <td class="L"><font class="label">Arresto</font></td>
            <td class="L"><font class="campo">
            <%=penaRicevuta.getStringaArresto()%>&nbsp;
            </font></td>
      <%}%>


      <%if(penaRicevuta.getImportoAmmenda().intValue() != 0 )
      {%>
          <td class="L"><font class="label">Ammenda</font></td>
          <td class="L"><font class="campo">
          <%=penaRicevuta.getImportoAmmenda()%>&nbsp;
          </font><font class="label"> Euro</font></td>
      <%}%>         
      
    </tr>
    <% } %>
  </table>  
  <br>
   
<%    
//==============================================================================    
//                                  BENEFICI
//==============================================================================    
List lBenefici = dettaglioFasSIEP.getBenefici();
if(lBenefici != null && lBenefici.size() != 0)
{
%>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="Titolo" colspan=4>Benefici</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Tipo</font></center>
      </td>
      <td class="l">
        <center><font class="label">Subordinata</font></center>
      </td>
      <td class="l">
        <center><font class="label">DPR</font></center>
      </td>
       <td class="l">
        <center><font class="label">Pena</font></center>
      </td>
    </tr>
    <%
    Iterator lIterBenefici = lBenefici.iterator();
    while (lIterBenefici.hasNext())
    {
      BeneficioModel lBene = (BeneficioModel)lIterBenefici.next();
    %>
    <tr>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrNaturaBeneficio(), "-")%></font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoBeneficio(), "-")%></font>
      </td>
      <td class="l">
        <%if(lBene.getDescrTipoSospSubordinata()!= null && !lBene.getDescrTipoSospSubordinata().equals("")){%>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrTipoSospSubordinata(),"-")%></font>
        <%}%>
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getDescrDpr(), "-")%></font>
      </td>
      <td class="l">
        <%
        if((lBene.getNumAnniReclusione()!=null && lBene.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiReclusione()!=null && lBene.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniReclusione()!=null && lBene.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
        {
        %>
        <font class="campo">Reclusione</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniReclusione(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiReclusione(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniReclusione(), "0")%></font>
        <%
        }
              
              
        if(lBene.getImportoMulta()!=null && lBene.getImportoMulta().compareTo(new BigDecimal(0))!=0)
        {
        %>
        <font class="label">Multa </font>
        <font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoMulta())%></font>&nbsp;€&nbsp;
        <%
        }
        
        if((lBene.getNumAnniArresto()!=null && lBene.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumMesiArresto()!=null && lBene.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lBene.getNumGiorniArresto()!=null && lBene.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
        {
        %>
        <font class="campo">Arresto</font>
        <font class="label">Anni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumAnniArresto(), "0")%></font>
        <font class="label">Mesi</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumMesiArresto(), "0")%></font>
        <font class="label">Giorni</font>
        <font class="campo"><%=StringUtils.toStringJSP(lBene.getNumGiorniArresto(), "0")%></font>
        <%
        }
        
        if(lBene.getImportoAmmenda()!=null && lBene.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
        {
        %>
        <font class="label">Ammenda </font>
        <font class="campo"><%=StringUtils.toEuroFormat(lBene.getImportoAmmenda())%></font>&nbsp;€&nbsp;
        <%
        }
        %>
            &nbsp;
      </td>
    </tr>
<% 
 }  // end while
%>
</table>
<%
} // end Benefici
%>      
  
   
<% 
//==========================================================================
//                               REATI
//==========================================================================
List lReatiCirostanze = dettaglioFasSIEP.getReatiCircostanze();
if(lReatiCirostanze != null && lReatiCirostanze.size() != 0)
{
%>
    <table cellspacing=1 cellpadding=1 width=95%>
      <tr>
        <td class="Titolo">Reati</td>
      </tr>
<%
      Iterator lIterReati = lReatiCirostanze.iterator();
      while(lIterReati.hasNext())
      {
        ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
        ReatoModel lReato = lReatoCircostanza.getReato();
        ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

        boolean lFlagAnnoNumero = false;
        if(   lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals("")
           && lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("") 
          )
        {
          lFlagAnnoNumero = true;
        }
%>
        <tr>
          <td class="l">
<%
          if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
          {%>
            <font class="label">
            <%out.println("Reato " + lReato.getProgrNumeroManuale()+": ");%>
            </font>
          <%
          } 
          else 
          {
            out.println("Reato " + lReato.getProgrReato()+": ");
          }
          %>
          
          <font class="campo">
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
              out.println(" n. "+lReato.getNumero());

            //CIRCOSTANZE
            if(lCircostanze != null)
            {
              ReatoModel lCirc = null;
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
              }
            } // end if CIRCOSTANZE
%>
                </font>
        <%

        if(lReato.getStringaConsumazione()!= null) { %>
          <!--  <font class="label">Data</font> -->
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
        <% }

        if(lReato.getNote() != null && !lReato.getNote().equals("")) { %>
          <font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
        <% } 

        if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals("")) { %>
          <font class="label">Luogo</font>&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
        <% }
        
        %>
          </td>
        </tr>
<%
        } // end While sui reati
  }
%>
</table>



  
<%    
//==============================================================================      
//                             MISURE CAUTELARI
//==============================================================================      

List lMisureCautelari = dettaglioFasSIEP.getMisureCautelari();
if(lMisureCautelari != null && lMisureCautelari.size() != 0)
{
%>

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr><td class="Titolo" colspan="4">Misure Cautelari</td></tr>
    <tr>
      <td class="l">
        <center><font class="label">Misura</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Inizio</font></center>
      </td>
      <td class="l">
        <center><font class="label">Data Fine</font></center>
      </td>    
      <td class="l">
        <center><font class="label">Totale</font></center>
      </td>
    </tr>

    <%
    
    CalendarUtil cu=new CalendarUtil();
    CalendarModel cm;
    CalendarModel ctot=new CalendarModel();

    Iterator lIterMisureCautelari = lMisureCautelari.iterator();
    while (lIterMisureCautelari.hasNext())
    {
      MisuraCautelareModel lMisCau = (MisuraCautelareModel)lIterMisureCautelari.next();
      String classFont = "CVerde";
      String isComputabile = "";
 
      if (lMisCau.getDataFine()!=null && lMisCau.getDataInizio()!=null)
      {
        cm=new CalendarModel();
        cm.setDataFine(lMisCau.getDataFine());
        cm.setDataInizio(lMisCau.getDataInizio());
        cm=cu.ricalcolaGAM(cu.CalcolaNumGiorniMesiAnni(cm));
        
        isComputabile="Anni " +cm.getNumAnni()+" Mesi "+cm.getNumMesi()+" Giorni "+cm.getNumGiorni();
        
        if (lMisCau.getFlagComputabile().equals("S"))
        {
          ctot=cu.sommaGiorni(ctot,cm);
          classFont="CVerde";
        } 
        else
        {
          isComputabile="NON COMPUTABILE";
          classFont="C";
        }
      } 
      else
      {
        if (lMisCau.getFlagComputabile().equals("S"))
          classFont="CVerde";
          
        isComputabile="-";
      }    
    %>
    <tr>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(lMisCau.getDescrTipoMisura(), "-")%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataInizio(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <td class="l">
        <font class="<%=classFont%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
      </td>
      <td class=<%=classFont%>><%=isComputabile%></td>
    </tr>
    <% } // end while%>
  </table>
<%
}  // end misure cautelari
%>

  <br>
  <form name="confermaSubmit">
    <table> 
      <tr>
        <td> 
            <input class="bottone"  value="salva" type="submit"  onClick="javascript:return Verifica();">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.presaincarico.action.ActConfermaPresaInCaricoAttiSiep">
            <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
            <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=fascicoloSIEP.getIdFascicoloSiep()%>">    
            <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=StringUtils.toStringJSP(IstruttoriaCumulo.getIdIstruttoriaCumulo())%>">
        </td>
      </tr>
    </table>
  </form>
  
	<script language="JavaScript">
	    function Verifica(){
	    	var aStessoTitolo = '<%=stessoTitolo%>';
	    	var aTitoloCorrente = '<%=titoloCorrente%>';
		    if (aStessoTitolo.length > 0	&&
		    	aStessoTitolo!=aTitoloCorrente) { 
				var msgConfirm = "Attenzione! Già è presente in Istruttoria Cumulo\n il Procedimento "+aStessoTitolo+" con estremi del Titolo Esecutivo\n uguali a quelli del procedimento che si sta per prendere in carico.\n Si vuole procedere all'iscrizione in Istruttoria del Titolo selezionato?";
			    if (window.confirm(msgConfirm))
			    	return true;
			    else
			    	return false;
		    } else
		    	return true;
	    }
	</script>

  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("confermaSubmit");
  </script>
  
  
</body>
</html>