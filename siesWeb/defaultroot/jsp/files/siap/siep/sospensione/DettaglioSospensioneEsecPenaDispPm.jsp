<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.sius.documentoallegato.model.DocumentoAllegatoModel"%>
<%@ page import="siap.sius.documentoallegato.action.ICostantiDocumentoAllegato" %>

<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>

<jsp:useBean id="eventonotifica"    scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="eventoOE"    scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="documentoAllegato"    scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />
<%
//<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
%>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffTDS"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="Istituto"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="decretoordinanza" scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="autorita" scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="sospensione" scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="autoritaUGC" scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaAvv" scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="uffGE" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="Noteautorita"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUGC"      scope="request" class="java.lang.String"/>
<jsp:useBean id="cssa" scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="motivoNonInvio"      scope="request" class="java.lang.String"/>
<%-- MEV_66: aggiunto campo in visualizzazione = tdsm + udsm --%>
<jsp:useBean id="uffUDS"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

PosizioneGiuridicaModel lPosizione = (posizioneluogoaltra.getPosizioneGiuridica() != null) ? posizioneluogoaltra.getPosizioneGiuridica() : new PosizioneGiuridicaModel();

LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

if (lLuogoDetenzione == null)
	lLuogoDetenzione = new LuogoDetenzioneModel();

if (lAltraCausa == null)
  	lAltraCausa = new AltraCausaModel();

// Inizializzazione campi se SOSPENSIONE non trovata (per evitare eventuale NullPointerException)
if (sospensione.getIdSospensione() == null)
  	sospensione.setQuantumZero();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Sospensione Esecuzione della pena disposta da pm</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  <script language="JavaScript">

  </script>
  <script language="JavaScript1.2">
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--
      function over_effect(e,state) {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else {
          while(source4.tagName!="TABLE") {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
	--%>
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>
<body class="corpo">
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciSosp">

  <table>
    <tr>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <INPUT type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
      <font class="campo">Dettaglio Sospensione Esecuzione della pena disposta da pm </font>
    </td>
    
<%
	//MEV 16: gestite casistiche per cui far vedere l'icona del FC
	String[] lListaCodMotivi = ICostantiEvento.CODICI_SOSPENSIONE_DELLA_PENA;
	List<String> lCodMotivi = Arrays.asList(lListaCodMotivi);
	String lCodMotivo = eventonotifica.getEvento().getCodMotivo();
	boolean isPresentCodMotivo = false;
	if (lCodMotivo != null && lCodMotivo.length() > 0)
		isPresentCodMotivo = lCodMotivi.contains(lCodMotivo);
	if (isPresentCodMotivo) {
		// FlagDocumentoRegistrato=A  il provvedimento è annullato ==> il bottone "FC" non deve essere visibile
		// FlagDocumentoRegistrato=N  il provvedimento non è validato ==> il bottone "FC" non deve essere visibile
		// FlagDocumentoRegistrato=S  l'evento è stato validato  ==> il bottone "FC" deve essere visibile
		if (documentoAllegato.getIdDocumentoAllegato() != null 								&&
				eventonotifica.getEvento().getFlagDocumentoRegistrato() != null 			&& 
	    		eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A") != 0 &&
	    		eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") != 0 &&
	    		eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
	    	if (documentoAllegato.getDataAnnullamento()==null) {//il foglio complementare esiste ==> azione: modifica foglio complementare
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=ModificaFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Modifica Foglio Complementare" border="0"> 
					</a>
				</td>
<%
	      	} else {//il foglio complementare non esiste(annullato) ==> azione: inserimento foglio complementare
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0"> 
					</a>
				</td>
<%
	      	}
		}
		else if (documentoAllegato.getIdDocumentoAllegato()==null &&
		    	eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null &&
		    	eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A")!=0 &&
		    	eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")!=0 &&
		    	eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S")==0) {
			//il foglio complementare non esiste ==> azione: inserimento foglio complementare 
			//se il provvedimento è stato validato eventonotifica.getEvento().getFlagDocumentoRegistrato()=="S"
%>
			<td class="LBG">  		
				<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
					<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0"> 
				</a>
			</td>
<%
		}
	}
%>

<%
    if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
      if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
      {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneArt47&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
        <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneEsecPenaDispPm&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
        </jsp:include>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>
<%
      }

      if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
      {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td class="LBG">
          <a href="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneArt47&IdEvento=<%= eventonotifica.getEvento().getIdEvento() %>" onclick="javascript:lookUpload();">
            <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
          </a>
        </td--%>
<!-- BOTTONE DI STAMPA -->
        <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
          <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.sospensione.action.ActStampaSospensioneEsecPenaDispPm&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
        </jsp:include>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
        </td>
<%
    }
%>
    </tr>
  </table>
  <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica</td>
      <td class="L" colspan=7>
        <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
            DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
        </font>
      </td>
    </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null )
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             //  if(lAltraCausa.getDescrLuogoIstituto()!=null)
              // {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               //}
%>
             </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if(lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso</td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
<%
             // if(lLuogoDetenzione.getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
             // }
%>
            </td>
          </tr>
<%
        }
%>
		<input type="HIDDEN" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">
<%
    	// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if (lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>

<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" 
	name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
</tr>
--%>
<%
	  if (  sospensione.getNumAnniPenaEspiata().intValue()!=0
	  	 || sospensione.getNumMesiPenaEspiata().intValue()!=0
	  	 || sospensione.getNumGiorniPenaEspiata().intValue()!=0 )
	  {
%>
       <tr>
				<td class="l">
					<font class="label">Pena Espiata</font>
				</td>
				<td class="l">
					<font class="label">Anni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%></font>
					<font class="label">Mesi</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%></font>
					<font class="label">Giorni</font>
					<font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
				</td>
			</tr>
<%
	  }
	  if (  flagergastolo.equals("N")
       && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
       || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
	  	 || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
      )
	  {
%>
      <tr>
				<td class="l">
					<font class="label">Pena Residua</font>
				</td>
				<td class="l">
<%
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
	  	       || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0)
             )
          {
%>
            <font class="label">Reclusione : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
<%
          }
          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0) )
          {
%>
            <font class="label"> Arresto : </font>
            <font class="label">Anni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%></font>
            <font class="label">Mesi</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%></font>
            <font class="label">Giorni</font>
            <font class="Campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
<%
          }

          if (  flagergastolo.equals("N")
             && (sospensione.getNumAnniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumMesiPenaResiduaReclus().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaReclus().intValue()!=0
             || sospensione.getNumAnniPenaResiduaArres().intValue()!=0
             || sospensione.getNumMesiPenaResiduaArres().intValue()!=0
             || sospensione.getNumGiorniPenaResiduaArres().intValue()!=0)
            )
          {
%>
              </td>
            </tr>
<%
          }
    }

    if(flagergastolo.equals("S"))
    {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO</font>
        </td>
      </tr>
<%
    }else if(flagergastolo.equals("D"))
     {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
     }
%>
  </table>
  <br>
  <table style="width: 95%;">
    <tr>
      <td colspan ="4" class="titolo">Riepilogo Dati del provvedimento di sospensione dell'esecuzione</td>
    </tr>
    <tr>
      <td class="l">
        Istanza presentata da
      </td>
      <td class="l">
<%
        if(decretoordinanza != null && decretoordinanza.getFlagPresentanteIstanza() != null)
        {
          if(decretoordinanza.getFlagPresentanteIstanza().equals("D"))
          {
%>
            <font class="campo">Difensore</font>&nbsp;
<%
          }
          else if(decretoordinanza.getFlagPresentanteIstanza().equals("I"))
          {
%>
            <font class="campo">Interessato</font>&nbsp;
<%
          }
          else if(decretoordinanza.getFlagPresentanteIstanza().equals("U"))
          {
%>
            <font class="campo">Ufficio</font>&nbsp;
<%
          }
        }
%>
      </td>
<%
      if( decretoordinanza.getDataDepositoIstanza() != null )
      {
%>
        <td class="l">
          Depositato in data
        </td>
        <td class="l">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataDepositoIstanza(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
<%
      }
%>
    </tr>

<%
    if ("S".equals(eventonotifica.getEvento().getFlagPiuMeno()))
    {
%>
      <tr>
        <td class="l">
          Foglio Complementare
        </td>
        <td class="l">
          <font class="campo">&nbsp;
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
          </font>
						&nbsp;&nbsp;&nbsp;Casellario Giudiziale:&nbsp;
<%
			  for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
				{
				  // in Questa Funzione La notifica corrispondente al Casellario Giudiziale corrisponde al tipo "CS"
				  if( 	 eventonotifica.getNotifiche()[i] != null 
				      && "CS".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica()) 
				      && eventonotifica.getNotifiche()[i].getAutoritaEsterna() != null
				  		)
				  {
%>
			      <font class="campo">
			        <%=StringUtils.toStringJSP( eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrSede() )%>
			      </font>
<%
				  }
				}
%>
   		</td>
<%
		}
%>   	
      </tr>
    <tr>
      <td class="l">
        Contenuto
      </td>
      <td class="l" colspan ="3">
        <font class="campo" ><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Data sospensione/revoca
      </td>
      <td class="l" colspan ="3"><font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(),"dd-MM-yyyy"))%>&nbsp;&nbsp;</font>
      </td>
    </tr>
  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l" colspan ="3">
     <font class="campo"><%=StringUtils.toStringJSP( decretoordinanza.getMotivazioni() )%>&nbsp;</FONT>
    </td>
  </tr>
<%
  if(   eventoOE.getIdEvento() != null
     && eventonotifica.getEvento() != null
     && (   "0920".equals(eventonotifica.getEvento().getCodMotivo())
         || "0921".equals(eventonotifica.getEvento().getCodMotivo())
         )
     )
  {
%>
      <tr>
        <td class="l">
          Ordine di esecuzione
        </td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(eventoOE.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(eventoOE.getDescrLuogoEmittente(), "-")%>
            del
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoOE.getDataEmissione(),"dd-MM-yyyy"), "-")%>
          </font>
        </td>
      </tr>
<%
  }
%>
</table>
<table style="width: 95%;">
    <tr>
      <td colspan ="2" class="titolo">Dati del provvedimento</td>
    </tr>
	<tr>
      	<td class="l" width="30%">Data emissione</td>
       	<td class="l">
       		<font class="campo">
       			<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
			</font>
		</td>
    </tr>
	<tr>
		<td class="l">Data trasmissione</td>
		<td class="l">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy"))%>
            </font>
		</td>
    </tr>
	<tr>
   		<td class="l">Magistrato Competente</td>
   		<td class="L">
       		<font class="campo">
       			<%=StringUtils.toStringJSP(magistratocompetente.getCognome() )%>&nbsp;<%=StringUtils.toStringJSP(magistratocompetente.getNome() )%>
      		</font>
      	</td>
  	</tr>

<%if (autoritaUGC != null && autoritaUGC.getIdAutoritaEsterna() != null){%>

 <tr>
     <td class="l" width=30%>Notifica al condannato</td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(autoritaUGC.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(autoritaUGC.getDescrSede())%>
</font>
</td>
</tr>
<%}%>
<%if(NoteUGC!= null && !"".equals(NoteUGC)){%>

<tr>
 <td class="l">Indirizzo</td>
   <td class="L">
      <font class="campo"><%=NoteUGC%>&nbsp;</font>
   </td>
</tr>
<%}
%>

<%if (Istituto != null && !"".equals(Istituto.getDescrTipoIstituto())){%>

 <tr>
     <td class="l" width=30%>Istituto di Detenzione </td>
  <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Istituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(Istituto.getDescrComune())%>
</font>
</td>
</tr>
<%}%>

<%if (!"".equals(uffGE.getDescrComune())){%>

 <tr>
      <td class="l">GE Competente</td>
       <td class="l"><font class="campo">
        <%=StringUtils.toStringJSP(uffGE.getDescrTipoUfficio())%> di <%=StringUtils.toStringJSP(uffGE.getDescrComune())%>
      </font>
     </td>
</tr>
<%}%>

<%-- MEV_66: aggiunto campo in visualizzazione = tdsm + udsm + uepe --%>
<%
if (!"".equals(uffTDS.getDescrComune())) {
%>
	<tr>
		<td class="l"><%=StringUtils.toStringJSP(uffTDS.getDescrTipoUfficio())%></td>
       	<td class="l">
       		<font class="campo"><%=StringUtils.toStringJSP(uffTDS.getDescrComune())%></font>
     	</td>
	</tr>
<%
}
if (!"".equals(uffUDS.getDescrComune())) {
	String descrTipoUfficio = uffUDS.getDescrTipoUfficio();
	if ("UDSM".equals(uffUDS.getCodTipoUfficio()))
		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
%>
	<tr>
		<td class="l"><%=StringUtils.toStringJSP(descrTipoUfficio)%></td>
       	<td class="l">
       		<font class="campo"><%=StringUtils.toStringJSP(uffUDS.getDescrComune())%></font>
     	</td>
	</tr>
<%
}
if (!"".equals(cssa.getComune())) {
	String descUff = "Ufficio Esecuzione Penale Esterna";
	if ("USSM".equals(cssa.getTipoDesc()))
		descUff = "Ufficio Servizi Sociali Minorili";
%>

	<tr>
<!-- 	<td class="l" width=30%>UEPE</td> -->
		<td class="l" width=30%><%=StringUtils.toStringJSP(descUff)%></td>
  		<td class="l">
  			<font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())%> - <%=StringUtils.toStringJSP(cssa.getIndirizzo())%></font>
		</td>
	</tr>
<%
}
if (autorita != null && autorita.getIdAutoritaEsterna() != null) {
%>

 	<tr>
		<td class="l" width=30%>Autorità per esecuzione</td>
  		<td class="l">
  			<font class="campo"><%=StringUtils.toStringJSP(autorita.getDescrTipoAutorita())%> di <%=StringUtils.toStringJSP(autorita.getDescrSede())%></font>
		</td>
	</tr>
<%
}
if (Noteautorita!= null && !"".equals(Noteautorita)) {
%>

	<tr>
 		<td class="l">Indirizzo</td>
   		<td class="L">
      		<font class="campo"><%=Noteautorita%></font>
   		</td>
	</tr>
<%
}

int count = 0;
while (count < eventonotifica.getNotifiche().length) {
	NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 	if (lNotMod.getCodTipoNotifica().equals("N")
 			&& lNotMod.getAutoritaEsterna() != null
 			&& lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
 		AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     	AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
%>
	<tr>
       	<td class="l">Avvocato per  Notifica</td>
       	<td class="L" colspan="2">
        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>
        	&nbsp;Foro di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%></font>
        	&nbsp;Difensore di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%></font>
       	</td>
	</tr>
   	<tr>
	    <td class="l">Autorita Notifica</td>
		<td class="L" colspan=2>
        	<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>
        	&nbsp;di&nbsp;
        	<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>
      	</td>
	</tr>
<%
		if (lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) {
%>

	<tr>
      	<td class="l">Note</td>
      	<td class="L" colspan="2">
      		<font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>
      	</td>
	</tr>

<%
		}
	}
	count++;
}
%>
</table>
</form>
<br>
<div align=left style="visibility:hidden" id="upld">
	<FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
	<table>
		<jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          	<td class="L">
            	<input  class=bottone  type="submit" value="Conferma">
            	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActUploadSospensioneEsecPenaDispPm">
            	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
            	<%--input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadDettaglioSospensioneEsecPenaDispPm"--%>
            	<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.sospensione.action.ActLoadTrasferisciProvvedimentoDS">           
          	</td>
		</tr>
	</table>
	</form>
</div>
</body>
</html>