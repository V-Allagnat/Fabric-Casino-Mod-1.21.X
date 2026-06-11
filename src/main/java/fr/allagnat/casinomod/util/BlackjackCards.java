package fr.allagnat.casinomod.util;

import net.minecraft.util.Pair;

import java.util.*;

public class BlackjackCards {
    private final Random random;
    public List<Pair<String, Integer>> cardsList;
    public static final Map<String, String> LOOKUP_PATHS = new HashMap<>();

    public BlackjackCards() {
        this.random = new Random();
        this.cardsList = new ArrayList<>();
        resetCards();
        initLookup();
    }

    private static void initLookup() {
        // for some reason I had issues with Map.of(...) so I had to add all the paths individually with map.put(...);
        if (LOOKUP_PATHS.isEmpty()) {
            LOOKUP_PATHS.put("?", "textures/gui/blackjack_table/card_back.png");
            LOOKUP_PATHS.put("A♥", "textures/gui/blackjack_table/ace_of_hearts.png");
            LOOKUP_PATHS.put("A♦", "textures/gui/blackjack_table/ace_of_diamonds.png");
            LOOKUP_PATHS.put("A♣", "textures/gui/blackjack_table/ace_of_clubs.png");
            LOOKUP_PATHS.put("A♠", "textures/gui/blackjack_table/ace_of_spades.png");
            LOOKUP_PATHS.put("2♥", "textures/gui/blackjack_table/two_of_hearts.png");
            LOOKUP_PATHS.put("2♦", "textures/gui/blackjack_table/two_of_diamonds.png");
            LOOKUP_PATHS.put("2♣", "textures/gui/blackjack_table/two_of_clubs.png");
            LOOKUP_PATHS.put("2♠", "textures/gui/blackjack_table/two_of_spades.png");
            LOOKUP_PATHS.put("3♥", "textures/gui/blackjack_table/three_of_hearts.png");
            LOOKUP_PATHS.put("3♦", "textures/gui/blackjack_table/three_of_diamonds.png");
            LOOKUP_PATHS.put("3♣", "textures/gui/blackjack_table/three_of_clubs.png");
            LOOKUP_PATHS.put("3♠", "textures/gui/blackjack_table/three_of_spades.png");
            LOOKUP_PATHS.put("4♥", "textures/gui/blackjack_table/four_of_hearts.png");
            LOOKUP_PATHS.put("4♦", "textures/gui/blackjack_table/four_of_diamonds.png");
            LOOKUP_PATHS.put("4♣", "textures/gui/blackjack_table/four_of_clubs.png");
            LOOKUP_PATHS.put("4♠", "textures/gui/blackjack_table/four_of_spades.png");
            LOOKUP_PATHS.put("5♥", "textures/gui/blackjack_table/five_of_hearts.png");
            LOOKUP_PATHS.put("5♦", "textures/gui/blackjack_table/five_of_diamonds.png");
            LOOKUP_PATHS.put("5♣", "textures/gui/blackjack_table/five_of_clubs.png");
            LOOKUP_PATHS.put("5♠", "textures/gui/blackjack_table/five_of_spades.png");
            LOOKUP_PATHS.put("6♥", "textures/gui/blackjack_table/six_of_hearts.png");
            LOOKUP_PATHS.put("6♦", "textures/gui/blackjack_table/six_of_diamonds.png");
            LOOKUP_PATHS.put("6♣", "textures/gui/blackjack_table/six_of_clubs.png");
            LOOKUP_PATHS.put("6♠", "textures/gui/blackjack_table/six_of_spades.png");
            LOOKUP_PATHS.put("7♥", "textures/gui/blackjack_table/seven_of_hearts.png");
            LOOKUP_PATHS.put("7♦", "textures/gui/blackjack_table/seven_of_diamonds.png");
            LOOKUP_PATHS.put("7♣", "textures/gui/blackjack_table/seven_of_clubs.png");
            LOOKUP_PATHS.put("7♠", "textures/gui/blackjack_table/seven_of_spades.png");
            LOOKUP_PATHS.put("8♥", "textures/gui/blackjack_table/eight_of_hearts.png");
            LOOKUP_PATHS.put("8♦", "textures/gui/blackjack_table/eight_of_diamonds.png");
            LOOKUP_PATHS.put("8♣", "textures/gui/blackjack_table/eight_of_clubs.png");
            LOOKUP_PATHS.put("8♠", "textures/gui/blackjack_table/eight_of_spades.png");
            LOOKUP_PATHS.put("9♥", "textures/gui/blackjack_table/nine_of_hearts.png");
            LOOKUP_PATHS.put("9♦", "textures/gui/blackjack_table/nine_of_diamonds.png");
            LOOKUP_PATHS.put("9♣", "textures/gui/blackjack_table/nine_of_clubs.png");
            LOOKUP_PATHS.put("9♠", "textures/gui/blackjack_table/nine_of_spades.png");
            LOOKUP_PATHS.put("10♥", "textures/gui/blackjack_table/ten_of_hearts.png");
            LOOKUP_PATHS.put("10♦", "textures/gui/blackjack_table/ten_of_diamonds.png");
            LOOKUP_PATHS.put("10♣", "textures/gui/blackjack_table/ten_of_clubs.png");
            LOOKUP_PATHS.put("10♠", "textures/gui/blackjack_table/ten_of_spades.png");
            LOOKUP_PATHS.put("J♥", "textures/gui/blackjack_table/jack_of_hearts.png");
            LOOKUP_PATHS.put("J♦", "textures/gui/blackjack_table/jack_of_diamonds.png");
            LOOKUP_PATHS.put("J♣", "textures/gui/blackjack_table/jack_of_clubs.png");
            LOOKUP_PATHS.put("J♠", "textures/gui/blackjack_table/jack_of_spades.png");
            LOOKUP_PATHS.put("Q♥", "textures/gui/blackjack_table/queen_of_hearts.png");
            LOOKUP_PATHS.put("Q♦", "textures/gui/blackjack_table/queen_of_diamonds.png");
            LOOKUP_PATHS.put("Q♣", "textures/gui/blackjack_table/queen_of_clubs.png");
            LOOKUP_PATHS.put("Q♠", "textures/gui/blackjack_table/queen_of_spades.png");
            LOOKUP_PATHS.put("K♥", "textures/gui/blackjack_table/king_of_hearts.png");
            LOOKUP_PATHS.put("K♦", "textures/gui/blackjack_table/king_of_diamonds.png");
            LOOKUP_PATHS.put("K♣", "textures/gui/blackjack_table/king_of_clubs.png");
            LOOKUP_PATHS.put("K♠", "textures/gui/blackjack_table/king_of_spades.png");
        }
    }

    public void resetCards() {
        // Using 4 standard 52 cards decks by default, can be increased further
        final int NUMBER_OF_DECKS = 4;
        cardsList.clear();
        for (int i = 0; i < NUMBER_OF_DECKS; i++) {
            cardsList.add(new Pair<>("A♥", 1));
            cardsList.add(new Pair<>("A♦", 1));
            cardsList.add(new Pair<>("A♣", 1));
            cardsList.add(new Pair<>("A♠", 1));

            cardsList.add(new Pair<>("2♥", 2));
            cardsList.add(new Pair<>("2♦", 2));
            cardsList.add(new Pair<>("2♣", 2));
            cardsList.add(new Pair<>("2♠", 2));

            cardsList.add(new Pair<>("3♥", 3));
            cardsList.add(new Pair<>("3♦", 3));
            cardsList.add(new Pair<>("3♣", 3));
            cardsList.add(new Pair<>("3♠", 3));

            cardsList.add(new Pair<>("4♥", 4));
            cardsList.add(new Pair<>("4♦", 4));
            cardsList.add(new Pair<>("4♣", 4));
            cardsList.add(new Pair<>("4♠", 4));

            cardsList.add(new Pair<>("5♥", 5));
            cardsList.add(new Pair<>("5♦", 5));
            cardsList.add(new Pair<>("5♣", 5));
            cardsList.add(new Pair<>("5♠", 5));

            cardsList.add(new Pair<>("6♥", 6));
            cardsList.add(new Pair<>("6♦", 6));
            cardsList.add(new Pair<>("6♣", 6));
            cardsList.add(new Pair<>("6♠", 6));

            cardsList.add(new Pair<>("7♥", 7));
            cardsList.add(new Pair<>("7♦", 7));
            cardsList.add(new Pair<>("7♣", 7));
            cardsList.add(new Pair<>("7♠", 7));

            cardsList.add(new Pair<>("8♥", 8));
            cardsList.add(new Pair<>("8♦", 8));
            cardsList.add(new Pair<>("8♣", 8));
            cardsList.add(new Pair<>("8♠", 8));

            cardsList.add(new Pair<>("9♥", 9));
            cardsList.add(new Pair<>("9♦", 9));
            cardsList.add(new Pair<>("9♣", 9));
            cardsList.add(new Pair<>("9♠", 9));

            cardsList.add(new Pair<>("10♥", 10));
            cardsList.add(new Pair<>("10♦", 10));
            cardsList.add(new Pair<>("10♣", 10));
            cardsList.add(new Pair<>("10♠", 10));

            cardsList.add(new Pair<>("J♥", 10));
            cardsList.add(new Pair<>("J♦", 10));
            cardsList.add(new Pair<>("J♣", 10));
            cardsList.add(new Pair<>("J♠", 10));

            cardsList.add(new Pair<>("Q♥", 10));
            cardsList.add(new Pair<>("Q♦", 10));
            cardsList.add(new Pair<>("Q♣", 10));
            cardsList.add(new Pair<>("Q♠", 10));

            cardsList.add(new Pair<>("K♥", 10));
            cardsList.add(new Pair<>("K♦", 10));
            cardsList.add(new Pair<>("K♣", 10));
            cardsList.add(new Pair<>("K♠", 10));
        }
    }

    public Pair<String, Integer> drawCard() {
        if (cardsList.isEmpty()) {
            resetCards();
            // return new Pair<>("No cards left", 999);
        }
        Pair<String, Integer> card = cardsList.get(random.nextInt(cardsList.size()));
        cardsList.remove(card);
        return card;
    }
}
