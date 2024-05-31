package com.example.boardgamebuddy.data

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PopulateDatabase : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // List of board games to be added
        val boardGames = listOf(
            BoardGame(
                3,
                "7 Wonders Duel",
                2,
                "Bruno Cathala",
                "7 Wonders Duel is a two-player version of the popular 7 Wonders game. Players compete to build the most impressive civilization by drafting cards that represent various structures and wonders. The game features three different ways to achieve victory: military, scientific, and civilian. Strategic planning and careful card selection are essential to success.",
                "https://cf.geekdo-images.com/zdagMskTF7wJBPjX74XsRw__imagepage/img/HdJ4d4O1P89V4UIhZnL3zoYnjow=/fit-in/900x600/filters:no_upscale():strip_icc()/pic2576399.jpg"
            ),
            BoardGame(
                4,
                "Brass: Birmingham",
                4,
                "Gavan Brown",
                "Brass: Birmingham is an economic strategy game set during the Industrial Revolution in England. Players build and develop industries, create trade networks, and navigate the evolving market demands. The game features two distinct halves, the Canal Era and the Rail Era, each with unique challenges and opportunities. Strategic planning and resource management are crucial for victory.",
                "https://cf.geekdo-images.com/x3zxjr-Vw5iU4yDPg70Jgw__imagepage/img/-17KkOmxbTu2slJTabGrkO8ZW8s=/fit-in/900x600/filters:no_upscale():strip_icc()/pic3490053.jpg"
            ),
            BoardGame(
                5,
                "Terraforming Mars",
                5,
                "Jacob Fryxelius",
                "Terraforming Mars is a game where players take on the role of corporations tasked with transforming the Martian landscape to support human life. Players compete to raise the planet's temperature, create oceans, and build cities. The game combines resource management, card drafting, and strategic planning. Success requires balancing short-term gains with long-term objectives.",
                "https://cf.geekdo-images.com/wg9oOLcsKvDesSUdZQ4rxw__imagepage/img/FS1RE8Ue6nk1pNbPI3l-OSapQGc=/fit-in/900x600/filters:no_upscale():strip_icc()/pic3536616.jpg"
            ),
            BoardGame(
                6,
                "Pandemic",
                4,
                "Matt Leacock",
                "Pandemic is a cooperative board game where players work together as a team of disease-fighting specialists to treat infections and find cures for four deadly diseases before they overwhelm the world. The game requires strategic planning, teamwork, and quick decision-making to succeed. Players must balance their efforts between treating infections and researching cures.",
                "https://cf.geekdo-images.com/S3ybV1LAp-8SnHIXLLjVqA__imagepage/img/kIBu-2Ljb_ml5n-S8uIbE6ehGFc=/fit-in/900x600/filters:no_upscale():strip_icc()/pic1534148.jpg"
            ),
            BoardGame(
                7,
                "Catan",
                4,
                "Klaus Teuber",
                "Catan is a classic strategy game where players collect resources and use them to build roads, settlements, and cities on the island of Catan. Players compete for resources, trade with each other, and try to achieve the most victory points. Strategic planning, resource management, and negotiation skills are key to winning the game.",
                "https://cf.geekdo-images.com/W3Bsga_uLP9kO91gZ7H8yw__imagepage/img/M_3Vg1j2HlNgkv7PL2xl2BJE2bw=/fit-in/900x600/filters:no_upscale():strip_icc()/pic2419375.jpg"
            ),
            BoardGame(
                8,
                "Wingspan",
                5,
                "Elizabeth Hargrave",
                "Wingspan is a competitive bird-collection, engine-building game. Players are bird enthusiasts—researchers, bird watchers, ornithologists, and collectors—seeking to discover and attract the best birds to their network of wildlife preserves. Each bird extends a chain of powerful combinations in one of your habitats. The game features beautiful artwork and components, making it a visually appealing and strategic game.",
                "https://cf.geekdo-images.com/yLZJCVLlIx4c7eJEWUNJ7w__imagepage/img/uIjeoKgHMcRtzRSR4MoUYl3nXxs=/fit-in/900x600/filters:no_upscale():strip_icc()/pic4458123.jpg"
            ),
            BoardGame(
                9,
                "Azul",
                4,
                "Michael Kiesling",
                "Azul is a tile-placement game where players take turns drafting colored tiles from suppliers to their player board. Players score points based on how they've placed their tiles to decorate the palace. Extra points are scored for specific patterns and completing sets. The player with the most points at the end of the game wins. The game combines strategic planning and pattern recognition.",
                "https://cf.geekdo-images.com/aPSHJO0d0XOpQR5X-wJonw__imagepage/img/q4uWd2nXGeEkKDR8Cc3NhXG9PEU=/fit-in/900x600/filters:no_upscale():strip_icc()/pic6973671.png"
            ),
            BoardGame(
                10,
                "Gloomhaven",
                4,
                "Isaac Childres",
                "Gloomhaven is a cooperative game of tactical combat in a unique, evolving fantasy world. Players take on the role of adventurers with their own special skills and reasons for traveling to this dark corner of the world. They must work together to clear out menacing dungeons and forgotten ruins. The game features an expansive campaign with branching storylines and meaningful choices.",
                "https://cf.geekdo-images.com/sZYp_3BTDGjh2unaZfZmuA__imagepage/img/pBaOL7vV402nn1I5dHsdSKsFHqA=/fit-in/900x600/filters:no_upscale():strip_icc()/pic2437871.jpg"
            )
        )

        // Populate the database
        populateDatabase(boardGames)
        finish()
    }

    private fun populateDatabase(boardGames: List<BoardGame>) {
        for (game in boardGames) {
            db.collection("BoardGames")
                .document(game.id.toString())  // Use the game's ID as the document name
                .set(game)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e ->
                    println("Error writing document: $e")
                }
        }
    }
}
