<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="DepositoOrdinanzaPc" scope="request" class="siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel"/>
<jsp:useBean id="LicenzePeriodi"      scope="request" class="java.util.Vector"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();

  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if (lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Dettaglio Liberazione Anticipata </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    function Verify()
    {
      
      //disabilità il taso di calcolo
      if(document.f.CALCOLO != "undefined")
      {
        if (document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>!=undefined)
        {
          var giornoScarcerazione = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>.value;
          var meseScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>.value;
          var annoScarcerazione   = document.f.<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>.value;
          
          var dataScarcerazione = giornoScarcerazione+'/'+meseScarcerazione+'/'+annoScarcerazione;
          var dataSystema = '<%=DateUtils.getDateToString(DateUtils.getSysDate(),"dd/MM/yyyy")%>';   
          if (dataScarcerazione!="//"){
            if (!ControllaDataPassaVuota(dataScarcerazione) ) {
              alert('Data Scarcerazione non valida');
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
              return false;
            }
           
            if (CompareDate(dataSystema,dataScarcerazione))
            {
              alert('La Data Scarcerazione deve essere < della data odierna');
              document.f.<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE %>.focus();
              return false;
            }
          }
        }
        
        document.f.CALCOLO.disabled=true;
      }
    }
  </script>
</head>
<body class="corpo">
<%
  int[] num  = {0,0,0,0};
  int[] n = {0,0,0,0};
  String[] titolo = new String[5];
  String[] cod = {"C", "R", "I", "N"};
  titolo[0] = "Semestri positivamente valutati: ";

  Iterator itx = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itx.hasNext())
  {
    LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
    if( lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0)
    {
        num[0]++;
        if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
            titolo[0] = "Semestri positivamente valutati: ";
            //titolo[0] = "Periodi concessi: ";
    }
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
        num[1]++;
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
        num[2]++;
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
        num[3]++;
  }

  titolo[0] = titolo[0] + num[0];
  titolo[1] = "Periodi non concessi Rigettati: " + num[1];
  titolo[2] = "Periodi non concessi Inammissibili: " + num[2];
  titolo[3] = "Periodi non concessi N.L.P./N.D.P.: " + num[3];
%>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <INPUT type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActCalcoloPenaLiberazioneAnticipata">
        <INPUT type="HIDDEN" name="<%=ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO %>" value="<%=StringUtils.toStringJSP(DepositoOrdinanzaPc.getIdEventoGenerato())%>">
        <font class="campo">Dettaglio Liberazione Anticipata</font>
      </td>
<%
      // E' possibile eseguire la comunicazione solo
      // nel caso in cui non compare il bottone di calcolo pena
      //
      // N.B. Vedere il commento sotto relativo al bottone
      if( (    lPosizione.isLibero()
            || flagergastolo.equals("S")
            || (   penaresidua.getDataFine() != null
                && DateUtils.isLower(penaresidua.getDataFine(), DateUtils.getSysDate())
                && !DateUtils.isEquals(penaresidua.getDataFine(), DateUtils.getSysDate())
               )
           )
            && num[0] != 0  // numero gg concessi
          )
      {
%>
        <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiLicenzaLibanticipata.CAMPO_EVE_ID_EVENTO%>" />
            <jsp:param name="ValoreIdEntita" value="<%=StringUtils.toStringJSP(DepositoOrdinanzaPc.getIdEventoGenerato())%>" />
          </jsp:include>
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
      <td class="l">Posizione Giuridica </td>
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
%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
<%
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
/*
       if (penaresidua.getDataInizio() != null)
       {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
        <tr>
          <td class="l">Data Decorrenza Pena</td>
          <td class="L">
            <font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
            </font>
          </td>
        </tr>
--%>
<%
/*
       }

/*
       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFinePresunta() != null)
       {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <tr>
      <td class="l">Data Fine Pena Automatica</td>
      <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
    </tr>
--%>
<%
/*
       }

       if ( penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("S") )
       {
*/
%>
<!--
        <tr>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
        </tr>
-->
<%
//       }
%>
<!--
      </tr>
      <tr>
-->
<%
/*
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFine()!=null)
        {
          String lClassTd="l";
          String lClassFont="campo";
          if( penaresidua.getDataFine() != null && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()) )
          {
            lClassTd="lRosso";
            lClassFont="lRosso";
          }
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
     <td class="l">Data Fine Pena</td>
     <td class="< %=lClassTd%>">
       <font class="< %=lClassFont%>">
         <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>&nbsp;
       </font>
     </td>
--%>
<%
//        }
%>
<!--
      <tr>
-->
<%
/*
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <td class="l">Reclusione</td>
    <td class="l" >
      <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
      <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
      <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
    </td>
--%>
<%
/*
          if(penaresidua.getImportoMulta().compareTo(new BigDecimal(0))!=0)
          {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <td class="l">Multa</td>
    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
--%>
<%
/*
          }
        }
*/
%>
<!--
   </tr>
   <tr>
-->
<%
/*
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}
    else
    {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
      <td class="l" >Arresto</td>
      <td class="l" >
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
--%>
<%
/*
      if(penaresidua.getImportoAmmenda().compareTo(new BigDecimal(0))!=0)
      {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <td class="l">Ammenda</td>
    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
--%>
<%
/*
      }
    }
  }
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>">
    </tr>
--%>
      <td class="L">
        Data emissione
      </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(DepositoOrdinanzaPc.getDataCameraConsiglio(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
    <tr>
      <td class="L">
        Totale giorni concessi
      </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DepositoOrdinanzaPc.getNumGiorniLibanticipata())%>&nbsp;
        </font>
      </td>
    </tr>
<%
    if(!LicenzePeriodi.isEmpty())
    {
      LicenzaPeriodiLibAnticipataModel lLicenzaPeriodiModel = (LicenzaPeriodiLibAnticipataModel)LicenzePeriodi.firstElement();
%>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="L">
          Anno / Numero SIUS
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoSius())%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroSius())%>&nbsp;
          </font>
        </td>
      </tr>
      <tr>
        <td class="L">
          Anno / Numero Ordinanza
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getAnnoOrdinanza())%>&nbsp;
          </font>
          /
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getNumeroOrdinanza())%>&nbsp;
          </font>
        </td>
      </tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
      <tr>
        <td class="L">
          Tribunale di sorveglianza
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getDescrLuogoEmittente())%>&nbsp;
          </font>
        </td>
      </tr>
--%>
	<tr>
		<td class="l">Autorità emittente</td>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getDescrUfficioEmittente());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(lLicenzaPeriodiModel.getLicenza().getCodTipoUfficioEmittente())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
        <td class="l"> <font class="campo"><%=descrTipoUfficio%>&nbsp;</font>di<font class="campo"><%=StringUtils.toStringJSP(lLicenzaPeriodiModel.getLicenza().getDescrLuogoEmittente())%>&nbsp;</font></td>
	</tr>

      <tr>
        <td class="L">
          Data Emissione
        </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lLicenzaPeriodiModel.getLicenza().getDataEmissioneOrdinanza(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
<%
    }
%>
  </table>
<%
  for (int k= 0; k < 4; k++)
  {
%>
	    <table cellspacing="2" cellpadding="2">
	      <tr><td><br></td></tr>
	      <tr>
	        <td class="Titolo" colspan=6> <%=titolo[k]%><td>
	      </tr>
	    </table>
<%
	    if (num[k] > 0)
	    {
	      	Iterator itxC = LicenzePeriodi.iterator();
%>
	      	<table cellspacing="2" cellpadding="2">
<%
		      while ( itxC.hasNext() )
		      {
			        LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC.next();
			        if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(cod[k]) == 0)
			        {
				        	String tipo="";
			      			if(lLicPerConc.getLicenza().getDescrStatoPermesso() != null)
			      			{
				      				if(lLicPerConc.getLicenza().getDescrStatoPermesso().equals("LS") )
					        				tipo = "Liberazione Anticipata Speciale";
					        		else if(lLicPerConc.getLicenza().getDescrStatoPermesso().equals("LI") )
					        				tipo = "Integrazione Liberazione Anticipata";	
					        		else if(lLicPerConc.getLicenza().getDescrStatoPermesso().equals("LA") )
					        				tipo = "Liberazione Anticipata";
			      			}
			      			else
			      			{
			      				tipo = "Liberazione Anticipata";
			      			}
			      			
				           n[k]++;
				           PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
				%>
				           <tr>
					           <td class="L">
					                <font class="l"><%= tipo%>&nbsp;<%=n[k]%>)<br></font>
					           </td>
				<%
				           for (int i = 0; i < p.length; i++)
				           {
			%>
					              <td class="L">
					                <font class="l">
					                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
					                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
					                </font>
					              </td>
			<%
				           }
			
				           if( k==0 ) //Soltanto per ogni riga dei semestri concessi
				           {
			%>
					              <td class="L">
					                <font class="l">
					                  giorni concessi: <%=lLicPerConc.getLicenza().getNumeroGiorni()%> &nbsp;
					                </font>
					              </td>
				            </tr>
			<%
				           }
			        }
			        
		      }	// chiude ciclo while( itxC.hasNext())
%>
      	  	</table>
<%  
		}	// chiude  if (num[k] > 0)
			
  }	// chiude ciclo for (int k= 0; k < 4; k++)
%>
  <br>
<%
  // E' possibile eseguire il calcolo della pena solo se è presente
  // la data fine sull'ultima pena validata o NON ERGASTOLO O NON LIBERO.
  // Inoltre il calcolo viene inibito se la data fine pena è già passata:
  // in questo caso infatti il condannato è formalmente libero
  // ed è possibile soltanto emettere una Comunicazione per Libero
  if(    !lPosizione.isLibero()
      && penaresidua.getDataFine() != null
      && !flagergastolo.equals("S")
      && num[0] != 0  // numero gg concessi
      && (   DateUtils.isGreater(penaresidua.getDataFine(), DateUtils.getSysDate())
          || DateUtils.isEquals(penaresidua.getDataFine(), DateUtils.getSysDate())
         )
    )
  {
%>
    <table cellspacing="2" cellpadding="2">
      <tr>
        <td>
          <input class="bottone" name="CALCOLO" type="submit" value="Calcolo data fine pena" >
        </td>

        <% //A9RR007 %>       
        <td class="l" colspan="3">
          <font class="label">Data Eventuale Scarcerazione</font>
          &nbsp;&nbsp;
          <input type="text" maxlength="2" size="2"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" maxlength="2" size="2"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" maxlength="4" size="4"
                 name="<%=ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_SCARCERAZIONE%>"
                 value=""
                 onFocus="javascript:textboxSelect(this)"
                 onkeypress="return TicTabNumField(this,event)"
                 onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
<%
  }
%>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>