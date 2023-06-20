import characterKlassen.*
import enemieKlassen.*

val SLEEP_TIME: Long = 0   // Von Gordon übernohmen


fun main() {


//    Charaktere

    val arena = Arena()

    var krieger1 = Worrior("Gladius", "Krieger", 300)
    var magier1 = Mage("Lenni", "Magier", 300)
    var heiler1 = Healer("Sienna", "Support", 300)

    arena.charListe.add(krieger1)
    arena.charListe.add(magier1)
    arena.charListe.add(heiler1)

//    Gegner

    var dragonBoss = Dragon("Smaug", "Feuer", 1000)

    arena.enemieListe.add(dragonBoss)



/*
Hier beginnt das Spiel.
Das Spiel geht so lange bis entweder alle Gegner oder alle Helden auf den Feld besiegt sind (jeweilige Liste ist leer).
Gegner sind in der class Arena in der Liste enemieListe
Helden sind in der class Arena in der Liste charListe
Gestartet wird mit 3 Helden und einem Boss (Drache) der maximal einen Gehilfen an seine Seite holen kann.
 */

    println("""
        
        
        
    ----------Willkommen!!!---------
    
    Ein episches Abenteuer erwartet dich.
    
    Du bist mit einer Heldengruppe aus Krieger, Magier und Heiler unterwegs. Euch stellt sich ein 
    riesiger Drache entgegen. Ihn gilt es zu besigen und die Welt vor seinen Flammen zu schützen!
    
    Viel Erfolg!
    
    """.trimIndent())

    Thread.sleep(SLEEP_TIME)

    println("""
        
        Der Drache Smaug türmt sich vor euch auf und der Kampf beginnt!
        
        
    """.trimIndent())

    Thread.sleep(SLEEP_TIME)

    do {

        spielerZug(arena)

        gegnerZug(arena)

        heldGestorben(arena)

    } while (arena.charListe.isNotEmpty() && arena.enemieListe.isNotEmpty())

    if (arena.enemieListe.isEmpty()) {
        println("Du hast gesiegt die Welt ist gerettet und kann erneut erblühen! Good Job, Mate!")
    } else {
        println("Das Böse hat gesiegt und die Welt wird untergehen! Blöd gelaufen...")
    }
}


/*
Der Angriff der Heldentruppe findet hier statt.
Anfangs soll entschieden werden ob das Inventar aufgerufen werden soll oder ein Angriff
stattfinden soll.
Bevor der Angirff gewählt wird, wird ein Ziel(Gegner) ausgesucht.
 */
fun spielerZug(arena: Arena) {

// Auswahl Kampf / Inventory
    for (held in arena.charListe) {

        held.hatSchutzStein = false  // Zum Beginn jeder Runde und Held wird diese Variable wieder zurückgesetzt.

        if (arena.enemieListe.size == 0) break

        var inventoryItems = arena.inventoryList.keys

        println("${held.name} (${held.klasse}) ist an der Reihe. Aktuell hast du ${held.hp} HP.")
        println("Was möchtest du machen?")
        println(
            """
            1: Kampfrunde beginnen!
            2: Inventar aufrufen
        """.trimIndent()
        )
        var eingabe = readln()
        var indexStartWahl = eingabe.toInt() - 1

//  Wahl des Items
        if (indexStartWahl == 1) {
            println("Welchen Gegenstand möchtest du einsetzen?")
            println(
                """
            1: ${(inventoryItems.elementAt(0).name)}
            2: ${(inventoryItems.elementAt(1).name)}
            3: ${(inventoryItems.elementAt(2).name)}
        """.trimIndent()
            )

            var eingabeGegenstand = readln()
            var indexGegenstand = eingabeGegenstand.toInt() - 1
            var nameGegenstand = inventoryItems.elementAt(indexGegenstand)

//            Heiltrank wird benutzt, Es kann gewählt werden wer den Heiltrank bekommt
            if(indexGegenstand == 0){
                var charHeilung = arena.inventoryList[nameGegenstand]!!
                println("Heiltrank gewählt. Wenn möchtest du Heilen?")
                println("""
                1: ${arena.charListe.elementAt(0).name}
                2: ${arena.charListe.elementAt(1).name}
                3: ${arena.charListe.elementAt(2).name}
            """.trimIndent())

                var eingabeCharHeilung = readln()
                var indexCharGeheilter = eingabeCharHeilung.toInt() - 1
                var geheilter = arena.charListe.elementAt(indexCharGeheilter)
                geheilter.hp += charHeilung

                println("${held.name} (${held.klasse}) heilt ${geheilter.name} und hat nun ${geheilter.hp} HP.\n")
                continue

//                Schutzstein wird benutzt; Alle Angriffe von Gegnern werden um 10 Schaden veringert für eine Runde
            } else if (indexGegenstand == 1) {

                println("Schutzstein gewählt. Wenn möchtest du Schützen?")
                println(
                    """
                1: ${arena.charListe.elementAt(0).name}
                2: ${arena.charListe.elementAt(1).name}
                3: ${arena.charListe.elementAt(2).name}
            """.trimIndent()
                )

                var eingabeCharSchutzStein = readln()
                var indexCharSchutz = eingabeCharSchutzStein.toInt() - 1
                var gehschuetzter = arena.charListe.elementAt(indexCharSchutz)
                gehschuetzter.hatSchutzStein = true // Hier wird die Variable geändert damit der Schutzstein wirkt.
                println("${gehschuetzter.name} ist jetzt durch einen Schutzstein geschützt.\n")
                continue

// Wurfmesser wird benutzt; Es kann wieder geweählt werden welcher Gegner getroffen werden soll
            } else if (indexGegenstand == 2 ){
                if (arena.enemieListe.size > 1) {
                    println("Wenn möchtest du engreifen?")
                    println(
                        """
            1: ${(arena.enemieListe.elementAt(0).name)}
            2: ${(arena.enemieListe.elementAt(1).name)}
        """.trimIndent()
                    )
                    var eingabe = readln()
                    var index = eingabe.toInt() - 1
                    var gegnerWahl = arena.enemieListe.elementAt(index)
                    if (gegnerWahl.kannAngegriffenWerden == true) {
                        gegnerWahl.hp -= 10
                        println("${gegnerWahl.name} bekommt 10 Schaden und hat nur noch ${gegnerWahl.hp} HP.\n")
                    } else {
                        println("${gegnerWahl.name} ist geschützt und nimmt keinen Schaden!\n")
                    }

                    if (gegnerWahl.hp <= 0) {
                        arena.enemieListe.remove(gegnerWahl)
                        println("${gegnerWahl.name} wurde besiegt!\n")
                    }

                } else {

                    if (arena.enemieListe.elementAt(0).kannAngegriffenWerden == true){
                    arena.enemieListe.elementAt(0).hp -= 10
                    println("${arena.enemieListe.elementAt(0).name} hat 10 Schaden bekommen und hat nun nur noch ${arena.enemieListe.elementAt(0).hp} HP.\n")
                    } else {
                        println("${arena.enemieListe.elementAt(0).name} ist geschützt und nimmt keinen Schaden.\n")
                    }

                    if (arena.enemieListe[0].hp == 0) {
                        arena.enemieListe.remove(arena.enemieListe[0])
                        println("Gegner wurde besiegt!\n")
                        break
                    }
                }
            }


/*
Wenn das Inventar nicht aufgerufen wird sondern gleich ein Angriff stattfinden soll geht es hier los
Sind mindestens 2 Gegner auf dem Feld wird vorab gefragt welcher Gegner angegriffen werden soll.
Wird ein Gegner besiegt wird er aus der enemieListe entfernt
 */
        } else {
            if (arena.enemieListe.size > 1) {
                println("Wenn möchtest du engreifen?")
                println(
                    """
            1: ${(arena.enemieListe.elementAt(0).name)}
            2: ${(arena.enemieListe.elementAt(1).name)}
          
        """.trimIndent()
                )
                var eingabe = readln()
                var index = eingabe.toInt() - 1
                var gegnerWahl = arena.enemieListe.elementAt(index)
                held.atkChar(gegnerWahl, arena)

                if (gegnerWahl.hp <= 0) {
                    arena.enemieListe.remove(gegnerWahl)
                    println("${gegnerWahl.name} wurde besiegt!\n")
                }

            } else {
                held.atkChar(arena.enemieListe[0], arena)
                if (arena.enemieListe[0].hp == 0) {
                    arena.enemieListe.remove(arena.enemieListe[0])
                    println("Gegner wurde besiegt!\n")
                    break
                }
            }
        }
    }
}


    /* In der Funktion werden über die for Schleife die einzelnen objekte in der Liste genommen um einen Angriff auszuführen.
Der Drache wird immer der erste Gegner sein und hat inherhalb der Angriffs Funktion die Möglichkeit weiter Gegner
erscheinen zu lassen.
Damit das alles klappt wird eine Kopie der enemieListe erstellt.
 */

    fun gegnerZug(arena: Arena) {
        for (enemie in arena.enemieListe.toList()) {
            if (enemie is Dragon) {
                if (enemie.hp > 0) {

                    var helden = arena.charListe.random()
                    enemie.angriffGegner(helden, arena)
                } else {
                    arena.enemieListe.remove(enemie)
                }
                continue
            }
            if (enemie.hp > 0) {

                var helden = arena.charListe.random()
                enemie.angriffGegner(helden, arena)
            } else {
                arena.enemieListe.remove(enemie)
            }
        }
    }

    /*
Hier wird kontrolliert ob ein Held noch am Leben ist
 Wenn der jeweilige Held keine HP mehr hat wird er aus der Liste entfernt und kann nicht mehr angreifen.
 */
    fun heldGestorben(arena: Arena) {
        for (held in arena.charListe) {
            if (held.hp <= 0)
                arena.charListe.remove((held))
        }
    }


