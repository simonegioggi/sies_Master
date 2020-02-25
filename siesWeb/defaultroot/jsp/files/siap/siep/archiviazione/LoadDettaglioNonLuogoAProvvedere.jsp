<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="archiviazione"       scope="request" class="siap.siep.archiviazione.model.ArchiviazioneModel"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="autorita" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="istituto" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="ufficio" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="altraautorita" scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="documentoAllegato" scope="request" class="siap.sius.documentoallegato.model.DocumentoAllegatoModel" />
      
<%
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

	// MEV 16: gestione casistiche per far comparire l'icona del foglio complementare
	String[] lListaCodMotivi = ICostantiEvento.CODICI_AVVENUTA_ESECUZIONE_PENA;
	List<String> lCodMotivi = Arrays.asList(lListaCodMotivi);
	String lCodMotivo = eventonotifica.getEvento().getCodMotivo();
	boolean isPresentCodMotivo = false;
	if (lCodMotivo != null && lCodMotivo.length() > 0)
		isPresentCodMotivo = lCodMotivi.contains(lCodMotivo);
%>
<html>
	<head>
  		<title>[S.I.E.S.] - Gestione evento </title>
  		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  		<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  		<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	</head>
	<body class="corpo">
  		<table>
    		<tr>
      			<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      			<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      				<font class="campo">Dettaglio Definizione Procedimento - Non luogo a provvedere</font>
   				</td>
<%
	if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null)
		if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
%>
				<!-- BOTTONE DI VALIDAZIONE -->
		     	<td class="LBG">
		      		<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		        		<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		      		</a>
		     	</td>
 				<!-- BOTTONE DI STAMPA -->
	 			<td class="LBG">
	       			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
	         		<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaNonLuogoAProvvedere&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
	       			</jsp:include>
	       		</td>
<%
	} if (eventonotifica.getEvento().getFlagDocumentoRegistrato() == null) {
%>
				<!-- BOTTONE DI VALIDAZIONE -->
		     	<td class="LBG">
		      		<a  href="/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>=siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&<%=ICostantiEvento.CAMPO_VALIDA%>=S&noblob=S">
		        		<img  align="middle" src="/images/upload24.gif" alt="Valida Provvedimento" width="24" height="24" border="0">
		      		</a>
		     	</td>
	 			<!-- BOTTONE DI STAMPA -->
	 			<td class="LBG">
	       			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
	         		<jsp:param name="ActionLink" value="<%=  "/jsp/Main.jsp?Action=siap.siep.archiviazione.action.ActStampaNonLuogoAProvvedere&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
	       			</jsp:include>
	       		</td>
<%
	}
%>

<%
	// MEV 16: soltanto per sei casi particolari deve apparire l'icona del foglio complementare
	if (isPresentCodMotivo) {
		// FlagDocumentoRegistrato=S l'evento è stato validato  ==> il bottone "FC" deve essere visibile
		if (eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
				&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
			// FlagDocumentoRegistrato=A  il provvedimento è annullato ==> il bottone "FC" non deve essere visibile
			// FlagDocumentoRegistrato=N  il provvedimento non è validato ==> il bottone "FC" non deve essere visibile
			// FlagDocumentoRegistrato=S  l'evento è stato validato  ==> il bottone "FC" deve essere visibile
			if (documentoAllegato.getIdDocumentoAllegato() != null
					&& eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
					&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A") != 0
					&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") != 0
					&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
				if (documentoAllegato.getDataAnnullamento() == null) {//il foglio complementare esiste ==> azione: modifica foglio complementare
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=ModificaFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>&IdDocumentoAllegato=<%=documentoAllegato.getIdDocumentoAllegato()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Modifica Foglio Complementare" border="0">
					</a>
				</td>
<%
				} else {//il foglio complementare non esiste ==> azione: inserimento foglio complementare
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0">
					</a>
				</td>
<%
				}
			} else if (documentoAllegato.getIdDocumentoAllegato() == null
				&& eventonotifica.getEvento().getFlagDocumentoRegistrato() != null
				&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("A") != 0
				&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N") != 0
				&& eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("S") == 0) {
			//il foglio complementare non esiste ==> azione: inserimento foglio complementare						  
			//se provvedimento validato eventonotifica.getEvento().getFlagDocumentoRegistrato()=="S"
%>
				<td class="LBG">
					<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&Provenienza=InsertFC&IdEvento=<%=eventonotifica.getEvento().getIdEvento()%>">
						<img src="/images/fcNsc.gif" width="30" height="30" alt="Inserimento Foglio Complementare" border="0">
					</a>
				</td>
<%
			}
		}
	}
%>

			</tr>
    	</table>
		<br>
   		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=3>
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
           <td class="l">Detenuto presso </td>
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

// Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
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

<%
       if (penaresidua.getDataInizio() != null)
       {
%>
        <tr>
          <td class="l">Data Decorrenza Pena</td>
          <td class="L">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
            </font>
          </td>
        </tr>
<%
       }

       if(penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <tr>
            <td class="l">Pena Detentiva</td>
            <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
          </tr>
<%
        }
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <tr>
            <td class="l">Pena Detentiva</td>
            <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
          </tr>
<%
        }
      }
%>
    
      <tr>
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")&& !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
          String lClassTd="l";
          String lClassFont="campo";
          if( penaresidua.getDataFine() != null && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()) )
          {
            lClassTd="lRosso";
            lClassFont="lRosso";
          }
%>
          <td class="l">Data Fine Pena <!--Manuale--></td>
          <td class="<%=lClassTd%>">
            <font class="<%=lClassFont%>">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;
            </font>
          </td>
<%
        }
%>
      <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" >
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
<%
          if(penaresidua.getImportoMulta() != null && penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
%>
            <td class="l">Multa</td>
            <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
          }
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
%>
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
<%
      if(penaresidua.getImportoAmmenda() != null && penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
%>
        <td class="l">Ammenda</td>
        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
  }
%>
      </tr>
<%
      if(eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataEmissione()!= null)
      {
%>
        <tr>
          <td class="l">Data Emissione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%
      }
      if(eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataTrasmissioneAtti()!= null)
      {
%>
        <tr>
          <td class="l">Data Trasmissione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%
      }
      if(archiviazione.getDataDefinizione() != null)
      {
%>
        <tr>
          <td class="l">Data Definizione</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(archiviazione.getDataDefinizione(), "dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%
      }
%>
      <tr>
       <td class="l">Oggetto Definizione
       <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo() )%></font>
       </td>
      </tr>
<%
  if(magistrato != null)
  {
%>
    <tr>
     <td class="l">Magistrato
     <td class="L">
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
         <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
     </td>
    </tr>
<%
  }

  if(autorita.getIdNotifica() != null)
  {
%>
     <tr>
      <td class="l">Casellario Giudiziale</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(autorita.getAutoritaEsterna().getDescrSede())%>
        </font>&nbsp;
      </td>
     </tr>
     <tr>
       <td class="l">Note
       <td class="L">
         <font class="campo">
          <%=StringUtils.toStringJSP(archiviazione.getNote())%>
          &nbsp;
        </font>
       </td>
     </tr>
<%
  }

  if(istituto.getIdNotifica() != null)
  {
%>
     <tr>
      <td class="l">Istituto di Detenzione</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(istituto.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
        <font class="campo"><%=StringUtils.toStringJSP(istituto.getIstitutoDetenzione().getDescrComune())%></font>
      </td>
     </tr>
<%
  }
  if(ufficio.getIdNotifica() != null)
  {
%>
     <tr>
      <td class="l">Ufficio recupero crediti</td>
       <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(ufficio.getUfficio().getDescrTipoUfficio())%>
        </font>&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(ufficio.getUfficio().getDescrComune())%>
        </font>&nbsp;
      </td>
     </tr>
<%
  }  
  if(altraautorita.getIdNotifica() != null)
  {
%>
     <tr>
      <td class="l">Altra Autorità</td>
       <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(altraautorita.getAutoritaEsterna().getDescrTipoAutorita())%>
        </font>&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(altraautorita.getAutoritaEsterna().getDescrSede())%>
        </font>&nbsp;
      </td>
     </tr>
     <tr>
       <td class="l">Note
       <td class="L">
         <font class="campo">
          <%=StringUtils.toStringJSP(altraautorita.getNote())%>
          &nbsp;
        </font>
       </td>
     </tr>
<%
  }  
%>

</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActUploadNonLuogoAProvvedere">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere">
          </td>
        </tr>
      </table>
</form>
</div>
<br>
<br>
</body>
</html>