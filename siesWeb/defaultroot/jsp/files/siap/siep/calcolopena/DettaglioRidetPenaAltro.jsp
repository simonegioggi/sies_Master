<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="annotazioni"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="fogliocomplementare"       scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel       lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel    lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel              lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
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
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Dettaglio Rideterminazione Pena Altro</font>
      </td>
      <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
          if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0) {%>
          <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
            <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaRidPenaAltro&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
          </jsp:include>
          <%}%>

      <%if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null) {%>
      <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaRidPenaAltro&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
      <%}%>
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
//                Sezione contenente le Annotazioni inserite
//==============================================================================
%>
<table>
<%
  Iterator iter = annotazioni.iterator();
  while (iter.hasNext())
  {
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)iter.next();
  %>
    <tr>
      <% if(lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("-")) {%>
      <td class="Titolo"  colspan=10> Detratta Pena </td>
      <%}else if(lAnnMod != null && lAnnMod.getFlagPiuMeno()!= null && lAnnMod.getFlagPiuMeno().equals("+")) {%>
      <td class="Titolo"  colspan=10> Aggiunta Pena </td>
      <%}%>
    </tr>

    <tr>
      <td class="l"><font class="label">Reclusione / Multa : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione())%></font>&nbsp;</td>
      <td class="l"><font class="label">Importo</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toEuroFormat(lAnnMod.getImportoMulta()))%></font>&nbsp;</td>
    </tr>

		<tr>
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
  </table>
  <table>
		<tr>
      <td class="l">Motivazioni :</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getMotivazioni())%></font>&nbsp;</td>
		</tr>
<%
 } // end while
%>
</table>

<%
//==============================================================================
//                Sezione contenente la Pena Ricalcolata
//==============================================================================
%>
<table>
  <tr>
    <td class="Titolo"  colspan=10> Pena Ricalcolata </td>
  </tr>
  <% if (penaresidua != null && penaresidua.getIdPenaResidua() != null) { %>
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
  <%}
}  // end if penaresidua != null
%>
</table>

<%
//==============================================================================
//         Sezione contenente il Riepilogo dei dati del Provvedimento
// - foglio complementare
// - data trasmissione
// - magistrato competente
// - notifiche
//==============================================================================
%>
<table width=95%>
  <tr>
    <td class="Titolo" colspan=10> Provvedimento</td>
  </tr>
</table>

<table>
<%
	if (fogliocomplementare.equals("1"))
	{
%>
		<tr>
			<td class="l">
		 		Foglio Complementare
			</td>
				<td class="l">
	    			<img align="left" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
					&nbsp;Casellario Giudiziale:&nbsp;
<%
		  for (int i = 0; i < eventonotifica.getNotifiche().length; i++)
			{
			  // La notifica corrispondente al Casellario Giudiziale corrisponde al tipo "FC"
			  if( 	 eventonotifica.getNotifiche()[i] != null 
			      && "FC".equals(eventonotifica.getNotifiche()[i].getCodTipoNotifica()) 
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
		</tr>
<%
	}   	
%>
	<tr>
    <td class="l">Oggetto :&nbsp;</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getEvento().getDescrMotivo())%></font>
    </td>
	</tr>
</table>

<table>
  <tr>
    <td class="l">Data Emissione</td>
    <td class="L">
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>
    </td>

    <td class="l">Data Trasmissione</td>
    <td class="L" >
       <%if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
         {%>
      		<font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
       <%} %>
    </td>
  </tr>

  <%
  if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
  <%}%>

<%
  if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
  {
	  int count=0;
	  while(count < eventonotifica.getNotifiche().length)
	  {
	    NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
	
	    if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
	       && lNotMod.getCodTipoNotifica().equals("E")
	       && lNotMod.getAutoritaEsterna()!= null
	       && lNotMod.getAutoritaEsterna().getCodTipoAutorita()!= null
	       && lNotMod.getAutoritaEsterna().getCodSede()!= null
	      )
	    {
%>
	    <tr>
	      <td class="l">Autorità di Destinazione</td>
	      <td class="L">
	        <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
	        di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
	      </td>
	    </tr>
	
	      <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")){%>
	      <tr>
	        <td class="l">Indirizzo</td>
	        <td class="l">
	          <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
	        <td>
	      </tr>
<%
	      }
	    } // end

	    if(   lNotMod.getCodTipoNotifica().equals("E")
	       && lNotMod.getIstitutoDetenzione()!= null
	      )
    	{
%>
		    <tr>
		      <td class="l">Istituto Detenzione</td>
		      <td class="l">
		         <font class="campo"> <%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
		         <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getIstitutoDetenzione().getDescrComune())%></font>
		      </td>
		    </tr>
<%
    	}

	    // Se getCodTipoNotifica().equals("AA")
	    if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
	       && lNotMod.getCodTipoNotifica().equals("AA")
	       && lNotMod.getAutoritaEsterna()!= null
	       && lNotMod.getAutoritaEsterna().getCodTipoAutorita()!= null
	       && lNotMod.getAutoritaEsterna().getCodSede()!= null
	      )
    	{
%>
    <tr>
      <%if(lPosizione.isMisuraAlternativa()) {%>
      <td class="l">Autorità di Controllo</td>
      <%}
        else if(   lPosizione.isMisSosp()
                || (   lPosizione.getCodPosizioneGiuridica() != null
                    && (   lPosizione.getCodPosizioneGiuridica().equals("01")
                        || lPosizione.getCodPosizioneGiuridica().equals("02")
                        || lPosizione.getCodPosizioneGiuridica().equals("03")
                        || lPosizione.getCodPosizioneGiuridica().equals("04")
                        || lPosizione.getCodPosizioneGiuridica().equals("23")
                       )
                    )
                )
      {%>
      <td class="l">Altra Autorità</td>
      <%}%>

      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrTipoAutorita())%></font>
         di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getAutoritaEsterna().getDescrSede())%></font>
      </td>
    </tr>

      <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals("")) {%>
      <tr>
        <td class="l">Indirizzo</td>
        <td class="l">
          <font class="campo"><%=lNotMod.getNote()%>&nbsp;</font>
        <td>
      </tr>
      <%}
    }  // end if getCodTipoNotifica().equals("AA")


    // UEPE
    if(   lNotMod != null && lNotMod.getCodTipoNotifica() != null
       && lNotMod.getCodTipoNotifica().equals("CS")
       && lNotMod.getCSSA() != null && lNotMod.getCSSA().getComune() != null
       && !lNotMod.getCSSA().getIndirizzo().equals("")
      )
    {%>
    <tr>
      <td class="l">UEPE</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getCSSA().getComune())%>-<%=StringUtils.toStringJSP(lNotMod.getCSSA().getIndirizzo())%></font>
      </td>
    </tr>
    <%}

    if(  lNotMod != null && lNotMod.getCodTipoNotifica() != null && lNotMod.getCodTipoNotifica().equals("MS") &&
        lNotMod.getUfficio()!= null &&  lNotMod.getUfficio().getDescrComune() != null)
    {
    %>
    <tr>
      <td class="l">Destinatario MDS</td >
      <td class="L">
        <font class="campo">
         MAGISTRATO DI SORVEGLIANZA</font> di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%>
        </font>
    <%}

    if(lNotMod != null && lNotMod.getCodTipoNotifica() != null && lNotMod.getCodTipoNotifica().equals("TS") &&
       lNotMod.getUfficio()!= null &&  lNotMod.getUfficio().getDescrComune() != null)
    {%>
    <tr>
      <td class="l">Destinatario TDS</td >
      <td class="L">
        <font class="campo">
        TRIBUNALE DI SORVEGLIANZA</font> di <font class="campo"><%=StringUtils.toStringJSP(lNotMod.getUfficio().getDescrComune())%>
        </font>
    <%}

 if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAutoritaEsterna()!=null && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null)
  {
     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
    %>
     <tr>
       <td class="l">Notifica per difensore</td>
       <td class="L" colspan="2">
        <font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp;
        &nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
        </font>
       </td>
      </tr>
   <tr>
	    <td class="l">Autorità Notifica</td>
            <td class="L" colspan=2>
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
         di
        <font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>&nbsp;
      </td>
     </tr>
  <%if(lNotMod.getNote()!= null && !lNotMod.getNote().equals(""))
   {%>

     <tr>
      <td class="l">Note</td>
      <td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
     </tr>

<%
   }
}
count++;
} // end While sulle Notifiche
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
          <input  class=bottone  type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActUploadRidPenaAltro">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.calcolopena.action.ActLoadDettaglioRidetPena">
        </td>
      </tr>
    </table>
  </form>
</div>
<br>
<br>

</body>
</html>