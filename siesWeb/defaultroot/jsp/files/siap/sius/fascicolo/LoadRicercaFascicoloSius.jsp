<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUSTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Ricerca Procedimento SIUS</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    var desktop;
    // Lista Procure per Distretti ( TDS )oppure Lista UDS
    function ListaTDS_UDS(a_formname,a_fieldname)
    {
      var valore = document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      var i = document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.selectedIndex;
      if ( i == 0 || i == 2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    }

    function ListaComuniTipoUfficio(a_formname, a_fieldname, a_typename) {
    	if ( a_typename == "UDS" || a_typename == "UDSM" || a_typename == "TDS" || a_typename == "TDSM")  {
    		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+a_formname+"&fieldname="+a_fieldname+"&typename="+a_typename, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        } else {
             alert("Selezionare il tipo di ufficio emittente");
        }
    }
    
    function ListaTDS_UDS_d(a_fieldname) {
      var a_typename = document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      var a_formname = "d";
      ListaComuniTipoUfficio(a_formname, a_fieldname, a_typename);
    }

    function ListaTDS_UDS_g(a_fieldname) {
      var a_typename = document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value;
      var a_formname = "g";
      ListaComuniTipoUfficio(a_formname, a_fieldname, a_typename);
    }

    function checkTipoUfficio() {
    	document.d.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
    	document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value="<%=ComuneUfficioConnesso%>";
    }

    function Verify()
    {
      return true;
    }

    function VerifyG()
    {
      return false;
    }
  </script>

  <script language="JavaScript">
    function radioBase()
    {
      var nodeIntervallo;
      var nodeDescIntervallo;
      var nodeData;
      var nodedivDesc;
      var nodePrincipale;

      checkTipoUfficio();

      nodeIntervallo=document.getElementById('intervallo');
      nodedivDesc=document.getElementById('divDesc');
      nodeData=document.getElementById('data');
      nodeDataIntervallo=document.getElementById('dataIntervallo');
      nodePrincipale=document.getElementById('Principale');

      if(document.f.tipoRicerche[0].checked)
      {
        document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>[0].selected;

        nodeIntervallo.style.visibility='hidden';
        nodedivDesc.style.visibility='hidden';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        nodePrincipale.style.visibility='visible';
        document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.focus();
      }
      else if (document.f.tipoRicerche[1].checked )
      {
        document.g.tipo[1].checked=true;
        document.g.tipo[0].checked=true;
        radio();
        nodedivDesc.style.visibility='visible';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        nodePrincipale.style.visibility='hidden';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
      }
    }

    function radio()
    {
      var nodeIntervallo;
      var nodeData;
      var nodeDataIntervallo;

      nodeIntervallo=document.getElementById('intervallo');
      nodeData=document.getElementById('Data');
      nodeDataIntervallo=document.getElementById('dataIntervallo');

      if(document.g.tipo[0].checked)
      {
        pulisci();
        nodeIntervallo.style.visibility='visible';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        document.f.valoreRadio.value='0';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
      }
      else if (document.g.tipo[1].checked )
      {
        pulisci();
        nodeIntervallo.style.visibility='hidden';
        nodeData.style.visibility='visible';
        nodeDataIntervallo.style.visibility='hidden';
        document.f.valoreRadio.value='1';
        document.b.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>.focus();
      }else{
        pulisci();
        nodeIntervallo.style.visibility='hidden';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='visible';
        document.f.valoreRadio.value='2';
        document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
      }
    }
    function pulisci()
    {
      document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.value='';
      document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>.value='';
      document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
      document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
      document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
      document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
      document.b.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>.value='';
      document.b.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>.value='';
      document.b.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
      document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
    }
    function VerifyD()
    {

      if (document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.value=="" )
      {
        alert("Valorizzare l'Anno ");
        document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }
      if (document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>.value=="" )
      {
        alert("Valorizzare il progressivo");
        document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
      return true;
    }
  </script>

  <script language="JavaScript">
    function VerifyA()
          {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno inizio ricerca");
        return false;
      }
/*      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero inizio ricerca");
        return false;
      }
*/// STUB 14/01/2005 Sostituito controllo con Impostazione Progressivo iniziale.
      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='1';

      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno di fine ricerca");
        return false;
      }
/*      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero di fine ricerca");
        return false;
      }
*/// STUB 14/01/2005 Sostituito controllo con Impostazione Progressivo finale.
      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value='999999';

      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
      if( (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        if(document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {
          alert("Anno inizio maggiore Anno fine");
          return false;
        }
        else if(document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {

          if(parseInt(document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value) < parseInt(document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value))
          {
            alert("Numero inizio maggiore Numero fine");
            return false;
          }
        }
      }
      return true;
    }

    function VerifyC()
    {
    if (document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value;
    if (document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value;

    if (document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value;
    if (document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value.length==1)
        document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value;

    var data_inizio=document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
    var data_fine=document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
      return true;
    }

    function VerifyChiamate(id)
    {
      if (document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value=="-" )
      {
        alert("L' Ufficio è obbligatorio");
        document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.focus();
        return false;
      }
      if (document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value=="")
      {
        alert('Il campo Sede Ufficio è obbligatorio');
        document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.focus();
        return false;
      }
      if(id==1)
      {
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.a.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
/*        if (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value=="")
        {
          alert('Il campo Anno Iscrizione Iniziale è obbligatorio');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
          return false;
        }
        if (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value=="")
        {
          alert('Il campo Progressivo Iscrizione Iniziale è obbligatorio');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.focus();
          return false;
        }
        if (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value=="")
        {
          alert('Il campo Anno Iscrizione Finale è obbligatorio');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
          return false;
        }
        if (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value=="")
        {
          alert('Il campo Progressivo Iscrizione Finale è obbligatorio');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
          return false;
        }
*/// STUB Controllo Commentato il 14/01/2005

        // L'anno iniziale deve essere = anno finale.
/*        if (document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value   !=
            document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {
          alert('I campi Anno Iniziale e Anno Finale devono essere uguali');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
          return false;
        }
*/// STUB Controllo Commentato il 14/01/2005
        // Il progr. finale deve essere > del progr. iniziale. La loro differenza non può essere > 200.
/*        var progr_finale   = document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value;
        var progr_iniziale = document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value;
        if (progr_finale - progr_iniziale > 200)
        {
          alert('Intervallo progressivi troppo ampio ( massimo 200 )!');
          document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.focus();
          return false;
        }
*/// STUB Controllo Commentato il 14/01/2005

        document.b.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
      }
      if(id==2)
      {
        document.b.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.b.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
        // Controllo della data iscrizione.
        var data_iscrizione=document.b.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>.value+'/'+document.b.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>.value+'/'+document.b.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if (! ControllaData(data_iscrizione))
        {
          alert('Data iscrizione non valida');
          return false;
        }
        // Controllo della data iscrizione <= data di sistema
        if (! CompareDate(data_iscrizione, data_sistema))
        {
          alert('Data iscrizione > della data odierna');
          return false;
        }

        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
      }
      if(id==3)
      {
        document.c.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.c.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
        // Controllo dell'intervallo date iscrizione.
        var gg_in = FillDM(document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value);
        var mm_in = FillDM(document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value);
        var aa_in = document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
        var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
        var gg_fi = FillDM(document.c.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value);
        var mm_fi = FillDM(document.c.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>.value);
        var aa_fi = document.c.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
        var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'


/*        if (dataIni.length == 2)
        {
          alert ("Data di inizio periodo mancante");
          return false;
        }
        else if (dataFine.length == 2)
        {
          alert ("Data di fine periodo mancante");
          return false;
        }
        else if (ControllaData (dataIni) == false)
        {
          // entrambe le date valorizzate
          alert ("Errore nella data : " + dataIni);
          return false;
        }
        else if (ControllaData (dataFine) == false)
        {
          alert ("Errore nella data : " + dataFine);
          return false;
        }
        else if (CompareDate(dataIni,dataFine)== false)
        {
          alert ("Data di Fine minore di Data inizio periodo");
          return false;
        }
        else if (CompareDate(dataFine, data_sistema)== false)
        {
          alert ("Data di Fine maggiore di Data sistema");
          return false;
        }
*/// STUB Controlli Sostituiti il 14/01/2005 con i seguenti:
        if (dataIni.length != 2 && dataIni.length != 10)
        {
          alert ("Data di inizio periodo errata");
          return false;
        }
        if (dataFine.length != 2 && dataFine.length != 10)
        {
          alert ("Data di fine periodo errata");
          return false;
        }
        else if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) )
        {
          // entrambe le date valorizzate
          alert ("Errore nella data : " + dataIni);
          return false;
        }
        else if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
        {
          alert ("Errore nella data : " + dataFine);
          return false;
        }
        else if ((dataFine.length == 10)               &&
                 ( dataIni.length == 10)               &&
                 CompareDate(dataIni,dataFine)== false )
        {
          alert ("Data di Fine minore di Data inizio periodo");
          return false;
        }
        else if ((dataFine.length == 10)                       &&
                  CompareDate(dataFine, data_sistema)== false)
        {
          alert ("Data di Fine maggiore di Data sistema");
          return false;
        }

        /* OK Date presenti */
/*        DataIni = new Date(aa_in, mm_in-1, gg_in);
        DataFine  = new Date(aa_fi, mm_fi-1, gg_fi);
        var periodo = 0;
        periodo = periodo + 1 + Math.floor((DataFine.getTime() - DataIni.getTime())/(1000*60*60*24));

        if(periodo>30)
        {
          alert ("Intervallo di date superiore a 30 giorni! ");
          return false;
        }
*/// STUB Controllo Commentato il 14/01/2005

        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
        document.a.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
        document.b.<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>.value='';
      }
    }

    function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2)
    {
      var TipoUff = document.d.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>

</head>
<body class="corpo" onLoad="radioBase();">
<form name="f">
<table>
   	<tr>
   		<td class="LBG">
   			<a href="Javascript:window.print();">
   				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
   			</a>
   		</td>
   		<td class="LBG">
   			<font class="label">Funzione:</font>&nbsp;<font class="campo">Ricerca Procedimento</font>
<%
FascicoloGPModel lModel = new FascicoloGPModel();
%>
   		</td>
 	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
<input type="HIDDEN" name="valoreRadio" value="">
<table width="85%">
 	<tr>
 		<td class="Titolo">Tipo Ricerca</td>
 	</tr>
 	<tr>
   		<td class="c">Base &nbsp;
   			<input type="radio" name="tipoRicerche" value="base" checked onClick="radioBase();">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Avanzata&nbsp;
            <input type="radio" name="tipoRicerche" value="avanzata"  onClick="radioBase();">
   		</td>
   	</tr>
</table>
</form>

<div id="divDesc" style="visibility:hidden; position:relative; top:-20px; width:100%;">
<form name="g">
<table width="85%">
	<tr>
		<td class="Titolo" colspan="2">Ricerca valida per procedimenti di un singolo Ufficio</td>
	</tr>
	<tr>
        <td class="l">Tipo Ufficio <font class=ob>(*)</font></td>
        <td class="L">
          	<select title="tipoUfficioSIUSTrattino" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>" >
          		<%= tipoUfficioSIUSTrattino %>
       		 </select>         
        </td>
	</tr>
	<tr>
        <td class="l">Sede <font class=ob>(*)</font> </td>
        <td class="l">
           	<input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>" value="<%=lModel.getFascicoloSiusModel().getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaTDS_UDS_g('<%= ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2 %>');">
            	<img src="/images/filefolder.gif" border="0">
            </a>
        </td>
	</tr>
	<tr>
		<td class="c"colspan ="2">Intervallo Numero Procedimenti &nbsp;
			<input type="radio" name="tipo" value="descIntervallo" onClick="radio();">
          	&nbsp;&nbsp;Data Iscrizione &nbsp;
          	<input type="radio" name="tipo" value="descData" onClick="radio();">
          	&nbsp;&nbsp;Intervallo Date di Iscrizione &nbsp;
          	<input type="radio" name="tipo" value="descIntervalloData" onClick="radio();">
		</td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
</form>
</div>

<div id="intervallo" style="visibility:visible; position:relative; top:-20px; width:100%;">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="a">
<table width="85%">
	<tr>
		<td class="Titolo" colspan="5">Intervallo Procedimenti</td>
	</tr>
	<tr>
        <td class="L" >
          	<font class="label">Anno/Numero Iniziale</font>
        </td>
        <td class="l">
          	<input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="L">
         	<font class="label">Anno/Numero Finale </font>
        </td>
        <td class="l">
          	<input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          	<input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="l" >
          	<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(1);">
        </td>
	</tr>
</table>
<br>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>">
</FORM>
<br><br>
</div>

<div id="data" style="visibility:hidden; position:relative; top:-125px; width:100%;">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="b">
<table width="85%">
	<tr>
      	<td class="Titolo" colspan="3">Specifica Data di Iscrizione</td>
	</tr>
	<tr>
        <td class="L" width="18%">Data <font class=ob>(*)</font></td>
        <td class="L">
          	<input type="text" title="Giorno Iscrizione" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Mese Iscrizione" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Anno Iscrizione" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">
          	<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(2);">
        </td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>">
</FORM>
</div>

<div id="dataIntervallo" style="visibility:hidden; position:relative; top:-175px; width:100%;">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
<table width="85%">
	<tr>
		<td class="Titolo" colspan="5">Intervallo Date di Iscrizione</td>
	</tr>
	<tr>
        <td class="L" width="20%">
          	<font class="label">Data Iniziale</font>
        </td>
        <td class="l" >
          	<input type="text" title="Giorno Iscrizione Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Mese Iscrizione Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Anno Iscrizione Iniziale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="L" >
          	<font class="label">Data Finale</font>
        </td>
        <td class="l">
          	<input type="text" title="Giorno Iscrizione Finale" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Mese Iscrizione Finale" name="<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          	<input type="text" title="Anno Iscrizione Finale" name="<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l" >
          	<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(3);">
        </td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO2%>">
<input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO2%>">
</form>
</div>
<br>

<div id="Principale" style="visibility:hidden; position:absolute; top:90px; width:100%;">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="d">
<table width="85%">
	<tr>
		<td class="Titolo" colspan="2">Specifico Procedimento</td>
	</tr>
	<tr>
        <td class="l" width=36%>Procedimento SIUS (Anno/Numero) <font class=ob>(*)</font></td>
        <td class="l">
          	<input Title="Anno SIUS"  type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          	/<input Title="Numero SIUS" type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
	</tr>
	<tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
        	<select title="tipoUfficioSIUSTrattino" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" >
          		<%= tipoUfficioSIUSTrattino %>
        	</select> 
        </td>
	</tr>
	<tr>
        <td class="l">Sede </td>
        <td class="l">
			<input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>" value="<%=lModel.getFascicoloSiusModel().getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaTDS_UDS_d('<%= ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO %>');">
            	<img src="/images/filefolder.gif" border="0">
            </a>
        </td>
	</tr>
	<tr>
        <td>
          	<input onClick="javascript:return VerifyD();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
	</tr>
</table>
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaFascicoloSius">
</form>
</div>

<script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("d");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>","numeric","Il campo Numero Procedimento può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","numeric","Il campo Anno può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("a");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric","Il campo Numero Procedimento Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>","numeric","Il campo Numero Procedimento Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("VerifyA");
  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("b");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE%>","lt=3000");

  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("c");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_ISCRIZIONE_FINALE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_ISCRIZIONE_FINALE%>","lt=3000");

    frmvalidator.setAddnlValidationFunction("VerifyC");
  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("g");

    frmvalidator.setAddnlValidationFunction("VerifyG");
  </script>

</body>
</html>