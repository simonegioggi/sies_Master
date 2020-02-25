<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel" %>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="dettagliofascicolo" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />
<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<%
  FascicoloSiepModel lFascicolo = dettagliofascicolo.getFascicoloSiep();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio Procedimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

    <script language="JavaScript">
    var vedo=true;
    var node;
    var nodeButton;
    function espandi()
      {
        node=document.getElementById('elenco');
        nodeButton = document.getElementById('PresaInCarico');
        if(vedo)
        {
          node.style.visibility='hidden';
         // nodeButton.style.top='-540px';
          vedo=false;
          node=document.getElementById('vedi');
          node.value="Espandi";
        }
        else
        {
          node.style.visibility='visible';
          vedo=true;
          node=document.getElementById('vedi');
          node.value="Nascondi";
        //  nodeButton.style.top='0px';
         // nodeButton.style.visibility='visible';
        }
      }
      
      function waitCursor(){
        document.body.style.cursor = 'wait';
        return true;
      }
      
      
    </script>
  </head>

  <body class="corpo" onLoad="javascript:espandi();">
    <form name="comandi">
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
          <td class="LBG">
            <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Procedimento da Trasferire</font>
          </td>
        </tr>
      </table>
    </form>

    <table cellspacing="2" cellpadding="2">
<!----------- MESSAGGIO --------------------->
       <tr>
        <td class="Titolo" colspan="4">Dati Messaggio</td>
      </tr>

     <tr>
        <td class="l"><font class="label">Stato Messaggio</font></td>
<%
        if (Messaggio.getMessaggioCorrelato() == null)
        {%>
          <td class="lRosso">In Attesa di risposta...&nbsp;</td>
<%      }
        else
        {%>
          <td class="lVerde"><font class="campo">Risposta Ricevuta&nbsp;</font></td>
<%      }%>
      </tr>
      <tr>
        <td class="l"><font class="label">Tipo Operazione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrTipoOperazione())%>&nbsp;</font></td>
      </tr>

      <tr>
        <td class="l"><font class="label">Data Invio</font></td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy  HH:mm:ss")%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Utente Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getCodiceUtenteMittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente() +" "+Messaggio.getDescrSedeUfficioMittente())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Ufficio Destinatario</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioDestinatario()+" "+Messaggio.getDescrSedeUfficioDestinatario())%>&nbsp;</font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Data Ricezione</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Messaggio.getDataEsito(),"dd-MM-yyyy HH:mm:ss"))%>&nbsp;</font></td>
      </tr>

    </table>

  <div id="PresaInCarico" style="width: 100%; visibility:visible; position:relative;">
   <form name="confermaSubmit">
     <table>
       <tr>
          <td>
            <input class="bottone"  type="submit" value="Conferma Trasferimento Procedimento">
          </td>
        </tr>

        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.jms.action.ActPresaInCaricoFascicoloSiep">
        <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getMessaggioCorrelato().getIdMessaggio()%>">
      </table>
    </form>
    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("confermaSubmit");
      frmvalidator.setAddnlValidationFunction("waitCursor");
    </script>    
   </div>

    <br>
      <jsp:include page="/jsp/files/siap/siep/jms/DettaglioSoggettoSentenzaPerTrasferimento.jsp"/>
    <br>

 <input type="button" name="vedi" value="Espandi" onClick="javascript:espandi();">
    <div id="elenco" style="width: 100%; visibility:hidden;">
    <table width=95%>
<%
      if(dettagliofascicolo.getResidenza()!= null && dettagliofascicolo.getDomicilio()!= null)
      {
%>
        <tr><td class="Titolo"  colspan="3">Residenza / Domicilio</td></tr>
<%
      }
			// Residenza
      ResidenzaModel lResidenza = dettagliofascicolo.getResidenza();
      if(lResidenza != null)
      {
%>
        <tr>
          <td class="l"><font class="label">Residenza: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>&nbsp;
<%
              String lStato = lResidenza.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA"))
              {
%>
                <%=StringUtils.toStringJSP(lResidenza.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lResidenza.getCodProvincia())%>)
<%            }
              else
              {
%>                <%=StringUtils.toStringJSP(lStato)%>
<%            }
%>          </font>
          </td>
        </tr>
<%    }
      
			// Domicilio
      ResidenzaModel lDomicilio = dettagliofascicolo.getDomicilio();
      if(lDomicilio != null)
      {
%>       <tr>
          <td class="l"><font class="label">Domicilio: </font></td>
          <td class="l">
            <font class="campo">
              <%=StringUtils.toStringJSP(lDomicilio.getIndirizzo())%>&nbsp;
<%
              String lStato = lDomicilio.getDescrStato();
              if(lStato != null && lStato.equalsIgnoreCase("ITALIA"))
              {
%>
                <%=StringUtils.toStringJSP(lDomicilio.getDescrComune())%>
                (<%=StringUtils.toStringJSP(lDomicilio.getCodProvincia())%>)
<%            }
              else
              {
%>               <%=StringUtils.toStringJSP(lStato)%>
<%            }
%>          </font>
          </td>
        </tr>
<%   }
  %>
    </table>
<%
		// Posizione giuridica
    PosizioneGiuridicaModel lPosizione = dettagliofascicolo.getPosizioneGiuridica();
    if(lPosizione != null)
    {
%>     <table style="width: 95%;">
         <tr><td class="Titolo" colspan="2">Posizione Giuridica</td></tr>
         <tr>
           <td class="L" width="30%"><font class="label">Posizione Giuridica: </font></td>
           <td class="l" width="70%">
             <font class="campo">
               <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
             </font>
           </td>
         </tr>
       </table>
<%  }

    List lReatiCirostanze = dettagliofascicolo.getReatiCircostanze();
    if(lReatiCirostanze != null && lReatiCirostanze.size() != 0)
    {
%>
        <table cellspacing="0" cellpadding="0" width="95%">
          <tr><td class="Titolo">Reati</td></tr>
<%
        Iterator lIterReati = lReatiCirostanze.iterator();
        while(lIterReati.hasNext())
        {
          ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
          ReatoModel lReato = lReatoCircostanza.getReato();
          ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

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
                <font class="campo">
<%
                //REATO
                if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
                {
%>                  <font class="campoNoCap">
<%                    out.println("Reato N." + lReato.getProgrNumeroManuale()+": ");
%>                  </font>
<%              }
                else
                {
                    out.println("Reato N." + lReato.getProgrReato()+": ");
                }

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
                }
%>
                </font>
<%               if(lReato.getDataInizio()!= null)
                  {
%>                  <font class="label">Data</font>
                    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lReato.getDataInizio(),"dd-MM-yyyy"))%></font>&nbsp;
<%                }
                 if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals(""))
                  {
%>                  <font class="label">Luogo</font>&nbsp;
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
<%                }
%>            </td>
            </tr>
<%      }
%>      </table>
<%  }
    
    PenaComplessivaSanzioneSostitutivaModel lPenComSanSost = dettagliofascicolo.getPenaComplessivaSanzioneSostitutiva();
    if(lPenComSanSost!=null)
    {
        PenaComplessivaModel lPenCom = lPenComSanSost.getPenaComplessiva();
        if(lPenCom != null)
        {
%>
          <table cellspacing="0" cellpadding="0" width="95%">
            <tr><td class="Titolo" colspan="3">Pena Complessiva</td></tr>
            <tr>
              <td class="l">
                <font class="campo">
<%
                  if((lPenCom.getNumAnniReclusione()!=null && lPenCom.getNumAnniReclusione().compareTo(new BigDecimal(0))!=0) || (lPenCom.getNumMesiReclusione()!=null && lPenCom.getNumMesiReclusione().compareTo(new BigDecimal(0))!=0) || (lPenCom.getNumGiorniReclusione()!=null && lPenCom.getNumGiorniReclusione().compareTo(new BigDecimal(0))!=0))
                  {
%>
                    Recl.&nbsp;AA&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;
                    MM&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;
                    GG&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%>&nbsp;
<%
                  }

                  if(lPenCom.getImportoMulta() != null && lPenCom.getImportoMulta().intValue() != 0)
                  {
%>
                    Multa&nbsp;<%=StringUtils.toEuroFormat(lPenCom.getImportoMulta())%>&nbsp;€&nbsp;
<%                }

                  if((lPenCom.getNumAnniArresto()!=null && lPenCom.getNumAnniArresto().compareTo(new BigDecimal(0))!=0) || (lPenCom.getNumMesiArresto()!=null && lPenCom.getNumMesiArresto().compareTo(new BigDecimal(0))!=0) || (lPenCom.getNumGiorniArresto()!=null && lPenCom.getNumGiorniArresto().compareTo(new BigDecimal(0))!=0))
                  {
%>
                    Arr.&nbsp;AA&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;
                    MM&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;
                    GG&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%>
<%
                  }

                 if(lPenCom.getImportoAmmenda() != null && lPenCom.getImportoAmmenda().intValue() != 0)
                  {
%>                   Amm.&nbsp;<%=StringUtils.toEuroFormat(lPenCom.getImportoAmmenda())%>&nbsp;&nbsp;
<%                }
                 if(lPenCom.getCodTipoPenaDetentiva().equals("03") || lPenCom.getCodTipoPenaDetentiva().equals("04"))
                  {
%>                 	<%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentiva())%>
<%                }%>
                </font>
              </td>
            </tr>
          </table>
<%      }
    }    
    
    if(lPenComSanSost!=null)
    {
		SanzioneSostitutivaModel lSanSos = lPenComSanSost.getSanzioneSostitutiva();
    	if(lSanSos != null)
    	{
%>        <table cellspacing="0" cellpadding="0" width="95%">
            <tr><td class="Titolo" colspan="3">Sanzione Sostitutiva</td></tr>
            <tr>
              <td class="l">
                
<%               if((lSanSos.getNumAnni()!=null && lSanSos.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumMesi()!=null && lSanSos.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lSanSos.getNumGiorni()!=null && lSanSos.getNumGiorni().compareTo(new BigDecimal(0))!=0))
                  {
%>  
	 				<font class="campo">
                   <%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;
                    AA&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;
                    MM&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;
                    GG&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%>
                    </font>
<%                }

                  if(lSanSos.getSanzionePecuniariaMulta() != null && lSanSos.getSanzionePecuniariaMulta().intValue() != 0)
                  {
%>                  <font class="campo">
					Sanz.Pec. Multa &nbsp;<%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>&nbsp;
					</font><br>
<%                }
                  if(lSanSos.getSanzionePecuniariaAmmenda() != null && lSanSos.getSanzionePecuniariaAmmenda().intValue() != 0)
                  {
%>                  <font class="campo">
					Sanz.Pec.Ammenda &nbsp;<%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>&nbsp;
					</font>        
              </td>
            </tr>
          </table>
<%
          }
        }
      }
      
      List lCirostanze = dettagliofascicolo.getCircostanze();
      if(lCirostanze != null && lCirostanze.size() != 0)
      {
%>
        <table cellspacing="0" cellpadding="0" width="95%">
          <tr><td class="Titolo">Aggravanti soggettive/Attenuanti</td></tr>
<%
        Iterator lIterCircostanze = lCirostanze.iterator();
        while(lIterCircostanze.hasNext())
        {
          CircostanzaModel lCircostanza = (CircostanzaModel)lIterCircostanze.next();

          boolean lFlagAnnoNumero = false;
          if( lCircostanza.getAnnoFonte() != null
              && !lCircostanza.getAnnoFonte().equals("")
              && lCircostanza.getNumeroFonte() != null
              && !lCircostanza.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }
%>
            <tr>
              <td class="l">
                <font class="campo">
<%
                  //REATO
                  if(lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte()+" ");
                    if(lCircostanza.getAnnoFonte() != null && !lCircostanza.getAnnoFonte().equals(""))
                      out.println(lCircostanza.getAnnoFonte());
                    if(lCircostanza.getNumeroFonte() != null && !lCircostanza.getNumeroFonte().equals(""))
                      out.println("/"+lCircostanza.getNumeroFonte());
                  }

                  if(lCircostanza.getArticolo() != null && !lCircostanza.getArticolo().equals(""))
                    out.println("art."+lCircostanza.getArticolo());
                  if(lCircostanza.getDescrSottonumerazione() != null && !lCircostanza.getDescrSottonumerazione().equals("") && !lCircostanza.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lCircostanza.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lCircostanza.getDescrFonte() != null && !lCircostanza.getDescrFonte().equals("") && !lCircostanza.getDescrFonte().equals("-"))
                      out.println(lCircostanza.getDescrFonte());
                  }

                  if(lCircostanza.getComma() != null && !lCircostanza.getComma().equals(""))
                    out.println(" c. "+lCircostanza.getComma());
                  if(lCircostanza.getLettera() != null && !lCircostanza.getLettera().equals(""))
                    out.println(" l. "+lCircostanza.getLettera());
                  if(lCircostanza.getNumero() != null && !lCircostanza.getNumero().equals(""))
                    out.println(" n. "+lCircostanza.getNumero());
%>
                </font>
              </td>
            </tr>
<%
        }
%>
        </table>
<%
      }
      
      List lPeneAccessorie = dettagliofascicolo.getPeneAccessorie();
      if(lPeneAccessorie != null && lPeneAccessorie.size() != 0)
      {
%>
        <table cellspacing="0" cellpadding="0" width="95%">
          <tr><td class="Titolo" colspan="3">Pene Accessorie</td></tr>
          <tr>
            <td class="l">
              <center><font class="label">Tipo</font></center>
            </td>
            <td class="l">
              <center><font class="label">Durata</font></center>
            </td>
            <td class="l">
              <center><font class="label">Condonata</font></center>
            </td>
          </tr>
<%
        Iterator lIterPeneAccessorie = lPeneAccessorie.iterator();
        while (lIterPeneAccessorie.hasNext())
        {
    	    PenaAccessoriaModel lPenAcc = (PenaAccessoriaModel)lIterPeneAccessorie.next();
%>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lPenAcc.getDescrTipoPenaAccessoria(), "-")%></font>
            </td>
            <td class="l">
              <font class="campo">
<%
              if(lPenAcc.getDescrDurata() != null && !lPenAcc.getDescrDurata().equals("-") && !lPenAcc.getDescrDurata().equals(""))
              {
                out.println(lPenAcc.getDescrDurata());
              }
              else if((lPenAcc.getNumAnni()!=null && lPenAcc.getNumAnni().compareTo(new BigDecimal(0))!=0) || (lPenAcc.getNumMesi()!=null && lPenAcc.getNumMesi().compareTo(new BigDecimal(0))!=0) || (lPenAcc.getNumGiorni()!=null && lPenAcc.getNumGiorni().compareTo(new BigDecimal(0))!=0))
              {
%>
                AA&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumAnni(), "0")%>&nbsp;
                MM&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumMesi(), "0")%>&nbsp;
                GG&nbsp;<%=StringUtils.toStringJSP(lPenAcc.getNumGiorni(), "0")%>
<%
              }
              else
              {
                out.println("-");
              }
%>
              </font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lPenAcc.getFlagCondonata(), "-")%></font>
            </td>
          </tr>
<%
	      }
%>
        </table>
<%
      }
      
      // Benefici
      List lBenefici = dettagliofascicolo.getBenefici();
      if(lBenefici != null && lBenefici.size() != 0)
      {
%>
        <table cellspacing="0" cellpadding="0" width="95%">
          <tr><td class="Titolo" colspan="3">Benefici</td></tr>
          <tr>
            <td class="l">
              <center><font class="label">Tipo</font></center>
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
	      }
%>
        </table>
<%
      }
  
      // Misure Cautelari
      List lMisureCautelari = dettagliofascicolo.getMisureCautelari();
      if(lMisureCautelari != null && lMisureCautelari.size() != 0)
      {
%>
        <table cellspacing=0 cellpadding=0 width=95%>
          <tr><td class="Titolo" colspan=3>Misure Cautelari</td></tr>
          <tr>
            <td class="l">
              <center><font class="label">Tipo</font></center>
            </td>
            <td class="l">
              <center><font class="label">Data Inizio</font></center>
            </td>
            <td class="l">
              <center><font class="label">Data Fine</font></center>
            </td>
          </tr>
<%
        Iterator lIterMisureCautelari = lMisureCautelari.iterator();
        while (lIterMisureCautelari.hasNext()) {
    	    MisuraCautelareModel lMisCau = (MisuraCautelareModel)lIterMisureCautelari.next();
%>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lMisCau.getDescrTipoMisura(), "-")%>&nbsp;</font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataInizio(), "dd-MM-yyyy"))%>&nbsp;</font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisCau.getDataFine(), "dd-MM-yyyy"))%>&nbsp;</font>
            </td>
          </tr>
<%
	      } //end while
%>
        </table>
<%
      } //endif
      
      List lAvvocati = dettagliofascicolo.getAvvocati();
      if(lAvvocati != null && lAvvocati.size() != 0)
      {
%>
        <table cellspacing=0 cellpadding=0 width=95%>
          <tr><td class="Titolo" colspan=3>Avvocati</td></tr>
          <tr>
            <td class="l">
              <center><font class="label">Cognome Nome</font></center>
            </td>
            <td class="l">
              <center><font class="label">Foro</font></center>
            </td>
            <td class="l">
              <center><font class="label">Indirizzo</font></center>
            </td>
          </tr>
<%
        Iterator lIter = lAvvocati.iterator();
        while (lIter.hasNext())
        {
    	    AvvocatoModel lAvv = (AvvocatoModel)lIter.next();
%>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></font>
            </td>
          </tr>
<%
	      } // end while
%>
        </table>
<%
      } // end if

      List lMisureSicurezza = dettagliofascicolo.getMisureSicurezza();
      if(lMisureSicurezza != null && lMisureSicurezza.size() != 0) {
%>
        <table cellspacing=0 cellpadding=0 width=95%>
          <tr><td class="Titolo" colspan=3>Misure Sicurezza</td></tr>
          <tr>
            <td class="l">
              <center><font class="label">Natura Misura</font></center>
            </td>
            <td class="l">
              <center><font class="label">Tipo Misura</font></center>
            </td>
            <td class="l">
              <center><font class="label">Durata Misura</font></center>
            </td>
          </tr>
<%
        	Iterator lIter = lMisureSicurezza.iterator();
        	while (lIter.hasNext()) {
    	    	MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel)lIter.next();
%>
          <tr>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrNatura(),"-")%></font>
            </td>
            <td class="l">
              <font class="campo"><%=StringUtils.toStringJSP(lMisSicu.getDescrTipo(),"-")%></font>
            </td>
            <td class="l">
              <font class="campo">
                AA&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumAnni(), "0")%>&nbsp;
                MM&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumMesi(), "0")%>&nbsp;
                GG&nbsp;<%=StringUtils.toStringJSP(lMisSicu.getNumGiorni(), "0")%>
              </font>
            </td>
          </tr>
<%
	      	} // end while
%>
        </table>
<%
      	} // end if
%>
  </div>
  <br>
  </body>
</html>