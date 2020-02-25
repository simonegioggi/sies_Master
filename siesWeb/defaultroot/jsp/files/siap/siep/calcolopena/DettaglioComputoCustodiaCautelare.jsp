<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="NoteCssa"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutN"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutNC"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteAutE"      scope="request" class="java.lang.String"/>
<jsp:useBean id="NoteUDS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="flagPage"      scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaNC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="evento06" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="annotazioneManuale" scope="request" class="java.util.Vector"/>
<jsp:useBean id="annotazioneManualeGE" scope="request"  class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="ArrivoDaDettaglioProvv" scope="request" class="java.lang.String"/>
<jsp:useBean id="fogliocomplementare"       scope="request" class="java.lang.String"/>

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
%>
<html>
  <head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
    <title>[S.I.E.S.] -Dettaglio Decreto Computo C.C. delle pene espiate senza titolo art. 657 c.p.p.</title>
  </head>
  <body class="corpo">
<%
  //FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
  //PosizioneGiuridicaModel posizioneGiuridica = posizioneluogoaltra.getPosizioneGiuridica();
  String Posizione = lPosizione.getCodPosizioneGiuridica();
  String annoGE = null;

  AnnotazioneManualeModel lAnnPrimo = null;
  // Per "annoGE" si intende la presenza dell'annotazione manuale
  // e non la presenza del campo "annoGE" come prima
  if(annotazioneManualeGE.getIdAnnotazioneManuale() != null)
  {
    lAnnPrimo = annotazioneManualeGE;

    annoGE = "S";
  }
  else
  {
    annoGE = "N";
  }
%>
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Decreto Computo C.C. art. 657 c.p.p </font>
      </td>


<%
      if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
        if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
        {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--td class="LBG">
            <a href="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaComputoCustodiaCautelare&IdEvento=
            <%=eventonotifica.getEvento().getIdEvento()%>&codposizionegiuridica=
            <%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>&annoGE=
            <%=annoGE%>" onclick="javascript:lookUpload();">
              <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
            </a>
          </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaComputoCustodiaCautelare&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()+"&annoGE="+annoGE%>"/>
   </jsp:include>
<%
        }
%>
<%
	if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
 	{
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td class="LBG">
     <a href="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaComputoCustodiaCautelare&IdEvento=
     <%=eventonotifica.getEvento().getIdEvento() %>&codposizionegiuridica=
     <%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>&annoGE=
     <%=annoGE%>" onclick="javascript:lookUpload();">
     <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
     </a>
</td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActStampaComputoCustodiaCautelare&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codposizionegiuridica="+posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()+"&annoGE="+annoGE%>"/>
   </jsp:include>
<%
	}
	
	if( !(ArrivoDaDettaglioProvv!=null && ArrivoDaDettaglioProvv.equals("SI")) )
  {
%>
		<td class="LBG">
		  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniComputo" >
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	    </a>
		</td>
<%
	}
%>
  </tr>
</table>
	<br>
	  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>
	<input type="HIDDEN" name="flagPage" value="<%=flagPage%>">
  <table>
    <tr>
      <td class="l" colspan="4">Posizione Giuridica :
      <font class="campo">
<%
      if(lFascicoloAssociato.getFlagAltraCausa() !=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
      {
%>
        DETENUTO PER ALTRA CAUSA <%=StringUtils.toStringJSP(lAltraCausa.getDescrTipoPosGiuridica())%>
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
      <input type="hidden" name="codPosizioneGiu" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
      </td>
    </tr>
<%
		if (fogliocomplementare.equals("1"))
		{
%>
  	<tr>
    	<td class="l">
     		Foglio Complementare
    	</td>
   		<td class="c">
   			<font class="campo">&nbsp;
    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
    		</font>
    	</td>
		  <td class="l">Casellario Giudiziale</td>
   		<td class="l">
   		<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
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
  </table>
  <table>
<%
    if(annotazioneManualeGE.getIdAnnotazioneManuale() != null)
    {
%>
      <tr>
<%
      if(lAnnPrimo.getAnnoGe() != null || lAnnPrimo.getNumeroGe()!= null)
      {
%>
        <td class="L">Ordinanza GE: </td>
        <td class="L"><font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrimo.getAnnoGe())%> / <%=StringUtils.toStringJSP(lAnnPrimo.getNumeroGe())%></font>
        </td>
<%
      }
%>
      </tr>
<%
      if(lAnnPrimo.getDataGE() != null )
      {
%>
        <tr>
          <td class="l">Data Ordinanza GE: </td>
          <td class="L"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrimo.getDataGE(),"dd-MM-yyyy"))%></font>
          </td>
        </tr>
<%
      }

      if(lAnnPrimo.getMotivazioni() != null )
      {
%>
        <tr>
          <td class="l">Motivazioni  </td>
          <td class="campo">
            <%=StringUtils.toStringJSP(lAnnPrimo.getMotivazioni())%>
          </td>
        </tr>
<%
      }
    }
%>
  </table>
  <table>
<%
      int lIdx= 0;
      Iterator lItx = annotazioneManuale.iterator();
      while(lItx.hasNext())
      {
        AnnotazioneManualeModel lAnn =  (AnnotazioneManualeModel)lItx.next();
%>
<%if(lAnn.getDataReclusioneDa() != null){%>
        <tr>

            <td class="Titolo" colspan="5" width="100%">Computo custodia cautelare reclusione</td>
        </tr>


         <tr>
              <td class="l">Dalla data  </td>
               <td class="L">
               <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataReclusioneDa(),"dd-MM-yyyy"))%></font>
            </td>
            <td class="L">Alla data  </td>
            <td class="L">
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataReclusioneA(),"dd-MM-yyyy"))%></font>
            </td>
             <td class="L">Anni

                <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumAnniReclusione())%></font>
                 Mesi
                  <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumMesiReclusione())%></font>
                 Giorni
                 <font class="campo"> <%=StringUtils.toStringJSP(lAnn.getNumGiorniReclusione())%></font>

          </td>

       </tr>
    <%}%>
<%if(lAnn.getDataArrestoDa() != null){%>
        <tr>

            <td class="Titolo">Computo custodia cautelare Arresto</td>
        </tr>
         <tr>
              <td class="l">Dalla data  </td>
              <td class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataArrestoDa(),"dd-MM-yyyy"))%>
            </td>
            <td class="l">Alla data  </td>
              <td class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataArrestoA(),"dd-MM-yyyy"))%>
            </td>
       </tr>
       <tr>
         <td class="l">Anni

                <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumAnniArresto())%></font>
                 Mesi
                  <font class="campo"><%=StringUtils.toStringJSP(lAnn.getNumMesiArresto())%></font>
                 Giorni
                 <font class="campo"> <%=StringUtils.toStringJSP(lAnn.getNumGiorniArresto())%></font>

          </td>

       </tr>
    <%}%>
<%
    lIdx++;
  }
%>
<tr><td>&nbsp;</td></tr>

 </table>
<table width="100%" >
<%if(penaresidua != null){%>
        <tr>

            <td class="Titolo" colspan="2">Pena rideterminata</td>
        </tr>
         <tr>
         <td class="l">Reclusione</td>

         <td class="L">Anni

                <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione())%></font>
                 Mesi
                  <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione())%></font>
                 Giorni
                 <font class="campo"> <%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione())%></font>

          </td>

       </tr>
 <tr>
         <td class="l">Arresto</td>

         <td class="L">Anni

                <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto())%></font>
                 Mesi
                  <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto())%></font>
                 Giorni
                 <font class="campo"> <%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto())%></font>

          </td>

       </tr>
    <%}%>
</table>
  <table width="100%">

<tr>
       <%if(eventonotifica.getEvento().getDataEmissione()!= null){%>
        <td class="l">Data Emissione</td>
        <td class="L" >
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%>   </font>

         </td>
<%}%>
       <%if(eventonotifica!=null && eventonotifica.getNotifiche()!=null &&
            eventonotifica.getNotifiche().length > 0 &&
            eventonotifica.getNotifiche()[0]!=null &&
            eventonotifica.getNotifiche()[0].getDataInvio()!= null)
       {%>
        <td class="l">Data Trasmissione</td>
        <td class="L" >
             <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getNotifiche()[0].getDataInvio(),"dd-MM-yyyy") )%></font>
        </td>
      <%}%>
    </tr>

  <%if(magistrato != null){%>
  <tr>
   <td class="l">Magistrato Assegnatario
   <td class="L">
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
   </td>
  </tr>
<%}%>
</table>
<table width="100%">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="Titolo" colspan=6>Destinatari per la notifica</td>
   	</tr>
<%
// if(Posizione.equals("11") || Posizione.equals("12") || Posizione.equals("13") || Posizione.equals("14") || Posizione.equals("19"))
if (lPosizione.isMisAlt()) {
%>
	<tr>
		<td class="l">UEPE Competente</td>
		<td class="l">
        	<font class="campo"><%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%></font>
      	</td>
  	</tr>
<%
	if (NoteCssa != null && !NoteCssa.equals("")) {
%>
	<tr>
 		<td class="l">Note</td>
   		<td  class="L">
      		<font class="campo"><%=NoteCssa%>&nbsp;</font>
   		</td>
	</tr>
<%
	}
%>
   	<tr>
		<td class="l">Ufficio Preposto al controllo</td >
      	<td class="L"> <font class="campo">UFFICIO DI SORVEGLIANZA</font></td>
	</tr>
  	<tr>
     	<td class="l">Sede</td><td class="L">
      		<font class="campo"><%=StringUtils.toStringJSP(UffUDS)%></font>
     	</td>
  	</tr>
<%
	if (NoteUDS != null && !NoteUDS.equals("")) {
%>
	<tr>
 		<td class="l">Note</td>
  		<td  class="L">
     		<font class="campo"><%=NoteUDS%>&nbsp;</font>
   		</td>
	</tr>
<%
	}
}
if (lPosizione.isMisAlt() || (Posizione.equals("10") || Posizione.equals("07") && (evento06 != null  && evento06.getIdEvento()!= null))) {
	if (autoritaEsternaE != null && autoritaEsternaE.getIdAutoritaEsterna() != null) {
%>
	<tr>
		<td class="l">Autorità di polizia competente</td>
		<td class="L">
			<font class="campo"> <%=StringUtils.toStringJSP(autoritaEsternaE.getDescrTipoAutorita()) %></font>
		</td>
	</tr>
  	<tr>
    	<td class="l">Sede </td>
    	<td class="L">
      		<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaE.getDescrSede()) %></font>
    	</td>
  	</tr>
<%
		if (NoteAutE!= null && !NoteAutE.equals("")) {
%>
	<tr>
		<td class="l">Note</td>
     	<td class="L">
			<font class="campo"><%=NoteAutE%>&nbsp;</font>
      	</td>
	</tr>
<%
		}
	} else {
%>
	<tr>
		<td class="l">Autorità di polizia competente</td>
 		<td class="L">
      		<font class="campo"> <%=StringUtils.toStringJSP(autoritaEsternaN.getDescrTipoAutorita()) %></font>
     	</td>
 	</tr>
  	<tr>
    	<td class="l">Sede </td>
    	<td class="L">
      		<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaN.getDescrSede()) %></font>
    	</td>
  	</tr>
<%
		if (NoteAutN != null && !NoteAutN.equals("")) {
%>
	<tr>
		<td class="l">Note</td>
     	<td class="L">
			<font class="campo"><%=NoteAutN%>&nbsp;</font>
      	</td>
	</tr>
<%
		}
  	}
}
if (!lPosizione.isLibero()) {
	if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
		int cont = 0;
    	while (cont < eventonotifica.getNotifiche().length) {
      		if (eventonotifica.getNotifiche()[cont].getIstitutoDetenzione() != null) {
%>
	<tr>
		<td class="l">Istituto di Detenzione</td>
        <td class="l">
          	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;di
          	<font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getIstitutoDetenzione().getDescrComune())%></font>
		</td>
	</tr>
<%
        		if (eventonotifica.getNotifiche()[cont].getNote() != null && !eventonotifica.getNotifiche()[cont].getNote().equals("")) {
%>
	<tr>
		<td class="l">Note</td>
		<td class="L"><font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[cont].getNote())%></font></td>
	</tr>
 <%
				}
      		}
      		cont++;
    	}
	}
}
if (autoritaEsternaNC != null && autoritaEsternaNC.getIdAutoritaEsterna() != null) {
%>
 	<tr>
		<td class="l">Ufficiali Giudiziari per notifica al condannato  </td>
    	<td class="L">
    		<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaNC.getDescrTipoAutorita())%></font>
     	</td>
	</tr>
	<tr>
      	<td class="l">Sede  </td>
      	<td class="l">
			<font class="campo"><%=StringUtils.toStringJSP(autoritaEsternaNC.getDescrSede()) %>   </font>
      	</td>
	</tr>
<%
	if (NoteAutNC != null && !NoteAutNC.equals("")) {
%>
	<tr>
		<td class="l">Note</td>
   		<td class="l">
			<font class="campo"><%=NoteAutNC%>&nbsp;</font>
    	<td>
	</tr>
<%
	}
}
%>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="Titolo" colspan=6>Ufficiali Giudiziari per notifica al difensore</td>
   	</tr>
<%
if (eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length > 0) {
	int count = 0;
	while (count < eventonotifica.getNotifiche().length) {
  		NotificaModel lNotMod = eventonotifica.getNotifiche()[count];
 		if (lNotMod.getCodTipoNotifica().equals("ND") && lNotMod.getAutoritaEsterna() != null
 				&& lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
 			AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[count].getAvvSiep();
     		AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[count].getAutoritaEsterna();
%>
	<tr>
		<td class="l">Avvocato per  Notifica</td>
       	<td class="L" colspan="2">
        	<font class="campo"><%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome()) +" "+ StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>
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
		<td class="l">Autorita Notifica</td>
		<td class="L" colspan=2>
        	<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrTipoAutorita() )%></font>&nbsp;
       		di
        	<font class="campo"><%=StringUtils.toStringJSP( lAuMod.getDescrSede())%></font>
      	</td>
	</tr>
<%
			if (lNotMod.getNote() != null && !lNotMod.getNote().equals("")) {
%>
	<tr>
		<td class="l">Note</td>
      	<td class="L" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lNotMod.getNote())%></font>&nbsp;</td>
	</tr>
<%
			}
		}
		count++;
	}
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
			<input type="HIDDEN" name="flagPage" value="<%=flagPage%>">
			<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.calcolopena.action.ActUploadComputoCustodiaCautelare">
			<input type="HIDDEN" name="IdPosizioneGiuridica" value="<%=lPosizione.getIdPosizioneGiuridica()%>">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.calcolopena.action.ActDettaglioComputoCustodiaCautelare">
			<input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>