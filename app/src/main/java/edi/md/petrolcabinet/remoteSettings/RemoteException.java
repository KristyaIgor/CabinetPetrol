package edi.md.petrolcabinet.remoteSettings;


/**
 * Created by Igor on 10.08.2020
 */

public class RemoteException {

    public static String getServiceException(int errorCode) {

        switch (errorCode){
            case -1:
                return "Eroare internă";
            case 1:
                return "Sesiunea nu este valabilă!";
            case 2:
                return "Nu ați specificat identificatorul!";
            case 3:
                return "Nu ați specificat numărul dvs. de telefon sau parola!";
            case 4:
                return "Client cu un astfel de identificator nu a fost găsit!";
            case 5:
                return "Sesiunea nu există!";
            case 6:
                return "Sesiunea nu este validă, PIN co nevalid!";
            case 7:
                return "ID-ul contractului nu este valabil!";
            case 8:
                return "Contractul nu există!";
            case 9:
                return "Cardul cu un astfel de identificator nu a fost găsit!";
            case 10:
                return "ID-ul cardului nu este valabil!";
            case 11:
                return "Cardul clientului nu are un cont propriu!";
            case 12:
                return "Bani insuficienți pe contul de client!";
            case 13:
                return "Raportul nu există!";
            case 14:
                return "Întreprinderea nu există!";
            case 15:
                return "Tip de autentificare necunoscut!";
            case 16:
                return "Clientul a fost dezactivat!";
            case 17:
                return "Parolă veche nu este corectă!";
            case 18:
                return "Contul clientului nu există!";
            case 19:
                return "Grupul de sortiment duplicat!";
            case 20:
                return "Identificarea cardului nu este valabil Clientul nu este determinat!";
            case 21:
                return "Clientul cu un astfel de e-mail nu a fost găsit!";
            case 22:
                return "PIN nevalid!";
            case 23:
                return "ID-ul de asortiment nu a fost găsit!";
            case 24:
                return "ID de asortiment nevalid!";
            case 25:
                return "Numărul de telefon pe care l-ați introdus este deja atribuit unui cont de utilizator existent!";
            case 26:
                return "Asortiment duplicat!";
            case 27:
                return "ID-ul de asortiment nu a fost găsit în contract!";
            case 28:
                return "Grup de asortiment nevalid!";
            case 29:
                return "ID-ul de asortiment nu a fost găsit în card!";
            case 30:
                return "Factura nu există!";
            case 31:
                return "Modelul de raport nu există!";
            case 32:
                return "Factura după acest ID nu există!";
            case 33:
                return "Interzis să schimbi sortimentul de pe Internet!";
            case 34:
                return "Cardul este dezactivat!";
            case 35:
                return "Înregistrarea există deja!";
            case 36:
                return "Contract pentru cardul dat nu există!";
            case 37:
                return "PIN codul nu este valabil!";
            case 38:
                return "Contul cardului dat este inactiv!";
            default:
                return "Eroare necunoscută";
        }
    }
}
